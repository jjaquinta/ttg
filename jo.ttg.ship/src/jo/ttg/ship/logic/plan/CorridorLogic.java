package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipPlanComponentBean;
import jo.ttg.ship.beans.plan.ShipPlanPerimeterBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.util.utils.DebugUtils;
import jo.util.utils.ThreadHelper;

public class CorridorLogic
{
    public static final int RADIUS = 8;
    public static boolean   dump   = false;

    public static void allocateCorridors(ShipPlanBean ship)
    {
        CorridorStats stats = new CorridorStats();
        stats.ship = ship;
        findInitialValues(stats);
        dump("Initial values:", stats);
        // try to reach the rest of the ship
        int step = 1;
        while (stats.needsAir.size() > 0)
        {
            boolean didIt = addNextCorridor(stats);
            dump("After step " + step + ":", stats);
            step++;
            if (!didIt)
                break; // stuck
            if (ThreadHelper.isCanceled())
                return;
        }
        joinCloseCorridors(stats);
    }

    private static void findInitialValues(CorridorStats stats)
    {
        stats.ship.updatePerimiters();
        stats.ship.getSquares().getBounds(stats.lower, stats.upper);
        stats.corridors = stats.ship.getComponent(ShipSquareBean.CORRIDOR);
        stats.unset = stats.ship.getComponent(ShipSquareBean.UNSET);
        for (Point3i p : stats.corridors.getSquares().toArray(new Point3i[0]))
            setCorridor(stats, p);
        stats.needsAir.clear();
        for (ShipPlanComponentBean comp : stats.ship.getComponents().values())
        {
            comp.updatePerimeter();
            for (ShipPlanPerimeterBean chunk : comp.getSubComponents())
                if (needsAir(chunk))
                    stats.needsAir.add(chunk);
        }
    }
    
    private static boolean needsAir(ShipPlanPerimeterBean comp)
    {
        Point3i tmp = new Point3i();
        ShipPlanBean ship = comp.getShip();
        for (Point3i p : comp.getSquares())
            if (needsAir(ship, p, tmp))
                return true;
        return false;
    }
    
    private static boolean needsAir(ShipPlanBean ship, Point3i p, Point3i q)
    {
        if (!ship.getSquare(p).isNeedsAir())
            return false;
        q.set(p);
        if (ship.isType(p.x+1, p.y, p.z, ShipSquareBean.UNSET))
        {
            q.x++;
            return true;
        }
        if (ship.isType(p.x-1, p.y, p.z, ShipSquareBean.UNSET))
        {
            q.x--;
            return true;
        }
        if (ship.isType(p.x, p.y+1, p.z, ShipSquareBean.UNSET))
        {
            q.y++;
            return true;
        }
        if (ship.isType(p.x, p.y-1, p.z, ShipSquareBean.UNSET))
        {
            q.y--;
            return true;
        }
        return false;
    }

    private static boolean addNextCorridor(CorridorStats stats)
    {
        DebugUtils.debug = false;
        Point3i need = new Point3i();
        Point3i start = new Point3i();
        ShipPlanPerimeterBean comp = findClosest(stats, start, need);
        if (comp == null)
            return false;
        if (dump)
            stats.println("Digging from " + stats.ship.getSquare(start)+" to " + stats.ship.getSquare(need));
        List<Point3i> path = PathLogic.findPathBetween(stats.ship, start, need, stats.unset.getSquares(), stats.corridors.getSquares());
        if (path == null)
        {
            stats.println("Can't dig from " + stats.ship.getSquare(start)+" to " + stats.ship.getSquare(need));
            stats.ship.setSquare(need, ShipSquareBean.HULL);
            Point3i lower = new Point3i(Math.min(start.x, need.x) - 3, Math.min(start.y, need.y) - 3, Math.min(start.z, need.z));
            Point3i upper = new Point3i(Math.max(start.x, need.x) - 3, Math.max(start.y, need.y) - 3, Math.max(start.z, need.z));
            String chunk = ShipPlanTextLogic.getLevelText(stats.ship.getSquares(), lower, upper);
            stats.print(chunk);
            StringBuffer sb = new StringBuffer();
            ShipPlanTextLogic.printLevelText(sb, stats.ship.getSquares(), start.z);
            stats.print(sb.toString());
            return false;
        }
        Point3i prev = start;
        for (int i = 0; i < path.size(); i++)
        {
            Point3i p = path.get(i);
            setCorridor(stats, p);
            addAccess(stats.ship.getSquare(prev), stats.ship.getSquare(p));
            prev = p;
        }
        if (!needsAir(comp))
            stats.needsAir.remove(comp);
        HullNode.NODE_CACHE.clear();
        return true;
    }
    
    static void addAccess(ShipSquareBean sq1, ShipSquareBean sq2)
    {
        boolean bothCorrdiors = (sq1.getType() == ShipSquareBean.CORRIDOR) && (sq2.getType() == ShipSquareBean.CORRIDOR);
        if (bothCorrdiors)
        {
            if (sq2.getPoint().z + 1 == sq1.getPoint().z)
            {
                sq1.setCeilingAccess(true);
                sq2.setFloorAccess(true);
            }
            else if (sq2.getPoint().z - 1 == sq1.getPoint().z)
            {
                sq1.setFloorAccess(true);
                sq2.setCeilingAccess(true);
            }
        }
        else
        {
            if (sq2.getPoint().y + 1 == sq1.getPoint().y)
            {
                sq1.setMinusYAccess(true);
                sq2.setPlusYAccess(true);
            }
            else if (sq2.getPoint().y - 1 == sq1.getPoint().y)
            {
                sq1.setPlusYAccess(true);
                sq2.setMinusYAccess(true);
            }
            else if (sq2.getPoint().x + 1 == sq1.getPoint().x)
            {
                sq1.setMinusXAccess(true);
                sq2.setPlusXAccess(true);
            }
            else if (sq2.getPoint().x - 1 == sq1.getPoint().x)
            {
                sq1.setPlusXAccess(true);
                sq2.setMinusXAccess(true);
            }
        }
    }

    private static void inflate(CorridorStats stats, ShipSquareBean sq, int radius)
    {
        if (sq.getNeedsAir() == Boolean.TRUE)
            sq.setNeedsAir(null);
        ShipPlanPerimeterBean component = stats.ship.getSubComponent(sq.getPoint());
        if (component == null)
            return; // ???
        if (dump)
        {
            stats.println("Before inflation:");
            ShipPlanUtils.printGroup(stats.ship, component.getSquares());
        }
        for (Point3i p : component.getSquares())
            if (ShipPlanUtils.dist2(p, sq.getPoint()) <= radius)
            {
                ShipSquareBean sq2 = stats.ship.getSquare(p);
                if (sq2.getNeedsAir() == Boolean.TRUE)
                    sq2.setNeedsAir(null);
            }
        if (dump)
        {
            stats.println("After inflation:");
            ShipPlanUtils.printGroup(stats.ship, component.getSquares());
        }
    }

    private static void joinCloseCorridors(CorridorStats stats)
    {
        Point3i[] cs = stats.oldCorridors.toArray(new Point3i[0]);
        for (int i = 0; i < cs.length - 1; i++)
            for (int j = i + 1; j < cs.length; j++)
                if (((cs[i].x == cs[j].x) || (cs[i].y == cs[j].y))
                        && (cs[i].z == cs[j].z))
                    if (ShipPlanUtils.dist2(cs[i], cs[j]) == 2)
                    {
                        Point3i tween = new Point3i((cs[i].x + cs[j].x) / 2,
                                (cs[i].y + cs[j].y) / 2, cs[i].z);
                        if (stats.oldUnset.contains(tween))
                            setCorridor(stats, tween);
                    }
    }

    private static ShipPlanPerimeterBean findClosest(CorridorStats stats, Point3i start, Point3i need)
    {
        ShipPlanPerimeterBean best = null;
        int bestv = -1;
        Point3i from = new Point3i();
        Point3i to = new Point3i();
        for (Iterator<ShipPlanPerimeterBean> i = stats.needsAir.iterator(); i.hasNext(); )
        {
            ShipPlanPerimeterBean comp = i.next();
            comp.updatePerimeter();
            Set<Point3i> targets = new HashSet<>();
            Point3i q = new Point3i();
            for (Point3i p : comp.getPerimeter())
                if (needsAir(stats.ship, p, q))
                {
                    targets.add(q);
                    q = new Point3i();
                }
            if (targets.size() == 0)
            {
                i.remove();
                continue;
            }
            int d = ShipPlanUtils.dist(stats.corridors.getSquares(), targets, from, to);
            if ((best == null) || (d < bestv))
            {
                best = comp;
                start.set(from);
                need.set(to);
                bestv = d;
            }
        }
        return best;
    }

    static ShipSquareBean setCorridor(CorridorStats stats, Point3i p)
    {
        ShipSquareBean sq = stats.ship.getSquare(p);
        if (sq == null)
            sq = new ShipSquareBean(p, ShipSquareBean.CORRIDOR);
        else
            sq = new ShipSquareBean(sq);
        sq.setType(ShipSquareBean.CORRIDOR);
        stats.ship.setSquare(sq);
        // see if we need a door
        ShipSquareBean minusy = stats.ship.getSquare(p.x, p.y-1, p.z);
        if ((minusy != null) && (minusy.getNeedsAir() == Boolean.TRUE))
        {
            addAccess(sq, minusy);
            inflate(stats, minusy, RADIUS);
        }
        ShipSquareBean plusy = stats.ship.getSquare(p.x, p.y+1, p.z);
        if ((plusy != null) && (plusy.getNeedsAir() == Boolean.TRUE))
        {
            addAccess(sq, plusy);
            inflate(stats, plusy, RADIUS);
        }
        ShipSquareBean minusx = stats.ship.getSquare(p.x-1, p.y, p.z);
        if ((minusx != null) && (minusx.getNeedsAir() == Boolean.TRUE))
        {
            addAccess(sq, minusx);
            inflate(stats, minusx, RADIUS);
        }
        ShipSquareBean plusx = stats.ship.getSquare(p.x+1, p.y, p.z);
        if ((plusx != null) && (plusx.getNeedsAir() == Boolean.TRUE))
        {
            addAccess(sq, plusx);
            inflate(stats, plusx, RADIUS);
        }
        return sq;
    }

    public static void removeExcessHatches(ShipPlanBean ship)
    {
        Set<Point3i> hatches = new HashSet<>();
        Set<Point3i> corridors = new HashSet<>();
        for (Iterator<Point3i> i = ship.getSquares().iteratorNonNull(); i
                .hasNext();)
        {
            Point3i p = i.next();
            ShipSquareBean sq = ship.getSquare(p);
            if (sq.isFloorAccess())
                hatches.add(p);
            if (sq.getType() == ShipSquareBean.CORRIDOR)
                corridors.add(p);
        }
        Point3i[] h = hatches.toArray(new Point3i[0]);
        for (int i = 0; i < h.length - 1; i++)
            if (h[i] != null)
                for (int j = i + 1; j < h.length; j++)
                    if ((h[j] != null) && (h[i].z == h[j].z))
                    {
                        int d = ShipPlanUtils.dist2(h[i], h[j]);
                        if ((d <= 2) && connected(h[i], h[j], corridors, d))
                        { // get rid of second hatch
                            ShipSquareBean top = ship.getSquare(h[j]);
                            ShipSquareBean bot = ship.getSquare(h[j].x, h[j].y,
                                    h[j].z + 1);
                            top.setFloorAccess(false);
                            bot.setCeilingAccess(false);
                            h[j] = null;
                        }
                    }
    }

    private static boolean connected(Point3i p1, Point3i p2,
            Set<Point3i> corridors, int d)
    {
        if (d == 1)
            return true;
        if (!connected(p1.x, p1.y, p1.z, p2.x, p2.y, p2.z, corridors))
            return false;
        if (!connected(p1.x, p1.y, p1.z + 1, p2.x, p2.y, p2.z + 1, corridors))
            return false;
        return true;
    }

    private static boolean connected(int x1, int y1, int z1, int x2, int y2,
            int z2, Set<Point3i> corridors)
    { // assuming length is 2
        if ((x1 == x2) || (y1 == y2))
        {
            Point3i tween = new Point3i((x1 + x2) / 2, (y1 + y2) / 2, z1);
            return corridors.contains(tween);
        }
        else
        {
            Point3i tween1 = new Point3i(x1, y2, z1);
            Point3i tween2 = new Point3i(x2, y1, z1);
            return corridors.contains(tween1) || corridors.contains(tween2);
        }
    }
    
    private static void dump(String label, CorridorStats stats)
    {
        if (!CorridorLogic.dump)
            return;
        stats.println(label);
        Point3i p = new Point3i();
        for (p.z = stats.lower.z; p.z <= stats.upper.z; p.z++)
        {
            stats.println("Deck " + p.z);
            for (p.x = stats.upper.x; p.x >= stats.lower.x; p.x--)
            {
                StringBuffer line = new StringBuffer();
                for (p.y = stats.lower.y; p.y <= stats.upper.y; p.y++)
                {
                    if (stats.corridors.contains(p))
                        line.append("#");
                    else if (stats.unset.contains(p))
                        line.append(".");
                    else
                    {
                        ShipSquareBean sq = stats.ship.getSquare(p);
                        if (sq == null)
                            line.append(" ");
                        else if (sq.getNeedsAir() == Boolean.FALSE)
                            line.append("-");
                        else
                        {
                            ShipPlanComponentBean comp = stats.ship
                                    .getComponent(sq);
                            if (comp == null)
                                line.append(" ");
                            else if (!comp.getPerimeter().contains(p))
                                line.append("-");
                            else if (sq.getNeedsAir() == null)
                                line.append("o");
                            else if (sq.getNeedsAir() == Boolean.TRUE)
                                line.append("@");
                        }
                    }
                }
                if (line.toString().trim().length() > 0)
                    stats.println(line.toString());
            }
        }
    }
}

class CorridorStats
{
    ShipPlanBean ship;
    Point3i lower = new Point3i();
    Point3i upper = new Point3i();
    ShipPlanComponentBean corridors;
    ShipPlanComponentBean unset;
    List<ShipPlanPerimeterBean> needsAir = new ArrayList<>();
    
    Set<Point3i> oldCorridors = new HashSet<>();
    Set<Point3i> oldNeedsAir = new HashSet<>();
    Set<Point3i> interiors = new HashSet<>();
    Set<Point3i> oldUnset = new HashSet<>();
    
    public int[] getRange(int low, int high)
    {
        int[] range = new int[high - low + 1];
        for (int i = 0; i < range.length; i++)
            range[i] = low + i;
        return range;
    }
    
    public List<Point3i> getPlane(Integer x, Integer y, Integer z)
    {
        int[] rangex = (x == null) ? getRange(lower.x, upper.x) : new int[] { x };
        int[] rangey = (y == null) ? getRange(lower.y, upper.y) : new int[] { y };
        int[] rangez = (z == null) ? getRange(lower.z, upper.z) : new int[] { z };
        List<Point3i> points = new ArrayList<>();
        for (int xx : rangex)
            for (int yy : rangey)
                for (int zz : rangez)
                    points.add(new Point3i(xx, yy, zz));
        return points;
    }
    
    public void println(String msg)
    {
        ship.println(msg);
    }
    
    public void print(String msg)
    {
        ship.print(msg);
    }
}