package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;

public class AxisLogic
{
    private static final int X = 2;
    private static final int Y = 1;
    private static final int Z = 0;

    public static void allocateAxialCorridors(ShipPlanBean ship)
    {
        CorridorStats stats = new CorridorStats();
        stats.ship = ship;
        ship.getSquares().getBounds(stats.lower, stats.upper);
        List<Set<Point3i>> corridors = new ArrayList<>();
        makeCorridors(stats, corridors);
        stats.corridors = stats.ship.getComponent(ShipSquareBean.CORRIDOR);
        stats.unset = stats.ship.getComponent(ShipSquareBean.UNSET);
        linkCorridors(stats, corridors);
    }

    private static void linkCorridors(CorridorStats stats,
            List<Set<Point3i>> corridors)
    {
        if (corridors.size() == 0)
            return;
        Collections.sort(corridors, new Comparator<Set<Point3i>>() {
            @Override
            public int compare(Set<Point3i> o1, Set<Point3i> o2)
            {
                return o2.size() - o1.size();
            }
        });
        Set<Point3i> c1 = corridors.get(0);
        while (corridors.size() > 1)
        {
            Point3i from = new Point3i();
            Point3i to = new Point3i();
            Set<Point3i> c2 = corridors.get(1);
            int dist = ShipPlanUtils.dist(c1, c2, from, to);
            for (int i = 2; i < corridors.size(); i++)
            {
                Set<Point3i> c = corridors.get(i);
                Point3i f = new Point3i();
                Point3i t = new Point3i();
                int d = ShipPlanUtils.dist(c1, c, f, t);
                if (d < dist)
                {
                    c2 = c;
                    from.set(f);
                    to.set(t);
                    dist = d;
                }
            }
            corridors.remove(c2);
            c1.addAll(c2);
            List<Point3i> path = PathLogic.findPathBetween(stats.ship, from, to, stats.unset.getSquares(), stats.corridors.getSquares());
            //System.out.println("Path from "+from+" to "+to+" is "+path);
            if (path == null)
                continue;
            for (int i = 0; i < path.size(); i++)
            {
                Point3i p = path.get(i);
                if (!stats.ship.isType(p, ShipSquareBean.CORRIDOR))
                    stats.ship.setSquare(p, ShipSquareBean.CORRIDOR);
                if (i > 0)
                    CorridorLogic.addAccess(stats.ship.getSquare(path.get(i-1)), stats.ship.getSquare(p));
                c1.add(p);
            }
        }
    }

    private static void makeCorridors(CorridorStats stats,
            List<Set<Point3i>> corridors)
    {
        // Z axis
        List<Point3i> axis = findBestAxis(stats,
                stats.getPlane(null, null, stats.lower.z),
                stats.getPlane(null, null, stats.upper.z),
                Z_AXIS_DIST);
        for (Point3i a : axis)
            fillIfUnset(stats, stats.getPlane(a.x, a.y, null), Z, corridors);
        // Y axis
        axis = findBestAxis(stats, stats.getPlane(null, stats.lower.y, null),
                stats.getPlane(null, stats.upper.y, null),
                Y_AXIS_DIST);
        for (Point3i a : axis)
            fillIfUnset(stats, stats.getPlane(a.x, null, a.z), Y, corridors);
        // X axis
        axis = findBestAxis(stats, stats.getPlane(stats.lower.x, null, null),
                stats.getPlane(stats.upper.x, null, null),
                X_AXIS_DIST);
        for (Point3i a : axis)
            fillIfUnset(stats, stats.getPlane(null, a.y, a.z), X, corridors);
    }

    private static final int X_AXIS_DIST = 16;
    private static final int Y_AXIS_DIST = 8;
    private static final int Z_AXIS_DIST = 32;

    private static void fillIfUnset(CorridorStats stats, List<Point3i> points,
            int orientation, List<Set<Point3i>> corridors)
    {
        ShipSquareBean first = null;
        ShipSquareBean last = null;
        Set<Point3i> corridor = new HashSet<>();
        for (Point3i p : points)
            if (stats.ship.isType(p, ShipSquareBean.UNSET))
            {
                ShipSquareBean sq = CorridorLogic.setCorridor(stats, p);
                if (first == null)
                    first = sq;
                last = sq;
                if (orientation == Z)
                {
                    sq.setCeilingAccess(true);
                    sq.setFloorAccess(true);
                }
                corridor.add(p);
            }
            else if (corridor.size() > 0)
            {
                corridors.add(corridor);
                corridor = new HashSet<>();
            }
        if (corridor.size() > 0)
            corridors.add(corridor);
        if (first != null)            
            if (orientation == Y)
            {
                first.setMinusYAccess(true);
                last.setPlusYAccess(true);
            }
            else if (orientation == X)
            {
                first.setMinusXAccess(true);
                last.setPlusXAccess(true);
            }
    }
    
    private static List<Point3i> findBestAxis(CorridorStats stats,
            List<Point3i> lowers, List<Point3i> uppers, int separation)
    {
        List<Point3i> best = new ArrayList<>();
        best.addAll(uppers);
        final Map<Point3i,Integer> dists = new HashMap<>();
        for (int i = 0; i < lowers.size(); i++)
        {
            Point3i u = uppers.get(i);
            Point3i l = lowers.get(i);
            int d = countUnset(stats, u, l);
            dists.put(u, d);
        }
        Collections.sort(best, new Comparator<Point3i>() {
            @Override
            public int compare(Point3i o1, Point3i o2)
            {
                int d1 = dists.get(o1);
                int d2 = dists.get(o2);
                return d2 - d1;
            }
        });
        // narrow down those that are too close
        for (int i = 0; i < best.size() - 1; i++)
        {
            Point3i p1 = best.get(i);
            for (int j = i + 1; j < best.size(); j++)
            {
                Point3i p2 = best.get(j);
                if ((dists.get(p2) < 4) || (ShipPlanUtils.dist3(p1, p2) < separation))
                {
                    best.remove(j);
                    j--;
                }
            }
        }
        return best;
    }
    
    private static int countUnset(CorridorStats stats, Point3i u, Point3i l)
    {
        int count = 0;
        Point3i delta = new Point3i((int)Math.signum(u.x - l.x), (int)Math.signum(u.y - l.y), (int)Math.signum(u.z - l.z));
        for (Point3i p = new Point3i(l); !p.equals(u); p.add(delta))
        {
            if (stats.ship.isType(p, ShipSquareBean.UNSET))
                count++;
        }
        return count;
    }
}
