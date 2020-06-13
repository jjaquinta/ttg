package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.vecmath.data.SparseMatrix;

public class ShipPlanUtils
{

    public static void printGroup(ShipPlanBean ship, Collection<Point3i> group)
    {
        SparseMatrix<String> m = new SparseMatrix<>();
        for (Point3i p : group)
            m.set(p, "x");
        int z = group.iterator().next().z;
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        m.getBounds(lower, upper);
        ship.println("From "+lower+" to "+upper);
        for (int x = lower.x; x <= upper.x; x++)
        {
            StringBuffer line = new StringBuffer();
            for (int y = lower.y; y <= upper.y; y++)
                if (m.contains(x, y, z))
                {
                    ShipSquareBean sq = ship.getSquare(x, y, z);
                    if (sq.getNeedsAir() == Boolean.TRUE)
                        line.append("@");
                    else if (sq.getNeedsAir() == null)
                        line.append("o");
                    else
                        line.append("-");
                }
                else
                    line.append(" ");
            ship.println(line.toString());
        }
    }

    public static boolean needsAir(int type)
    {
        switch (type)
        {
            case ShipSquareBean.MANEUVER:
            case ShipSquareBean.JUMP:
            case ShipSquareBean.POWER:
            case ShipSquareBean.TURRET:
            case ShipSquareBean.CARGO:
            case ShipSquareBean.HANGER:
            case ShipSquareBean.BAY:
            case ShipSquareBean.SPINE:
            case ShipSquareBean.STATEROOM:
                return true;
        }
        return false;
    }

    public static List<Point3i> getNeighbors(Point3i p, boolean TD)
    {
        List<Point3i> near = new ArrayList<>();
        near.add(new Point3i(p.x - 1, p.y, p.z));
        near.add(new Point3i(p.x + 1, p.y, p.z));
        near.add(new Point3i(p.x, p.y - 1, p.z));
        near.add(new Point3i(p.x, p.y + 1, p.z));
        if (TD)
        {
            near.add(new Point3i(p.x, p.y, p.z - 1));
            near.add(new Point3i(p.x, p.y, p.z + 1));
        }
        return near;
    }

    public static int dist3(Point3i p1, Point3i p2)
    {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z);
    }

    // manhattan distance
    public static int dist2(Point3i p1, Point3i p2)
    {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    public static int dist(Collection<Point3i> corridors, Collection<Point3i> targets, Point3i from, Point3i to)
    {
        int bestd = Integer.MAX_VALUE;
        for (Point3i p2 : targets)
        {
            for (Point3i p1 : corridors)
            {
                int d = Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y) + Math.abs(p1.z - p2.z)*5;
                if (d < bestd)
                {
                    bestd = d;
                    from.set(p1);
                    to.set(p2);
                }
            }
        }
        return bestd;
    }

    public static void moveTowards(Point3i from, Point3i to)
    {
        Point3i delta = new Point3i(to);
        delta.sub(from);
        if (Math.abs(delta.x) > Math.abs(delta.y))
            if (Math.abs(delta.x) > Math.abs(delta.z))
                from.x += (int)Math.signum(delta.x);
            else
                from.z += (int)Math.signum(delta.z);
        else 
            if (Math.abs(delta.y)> Math.abs(delta.z))
                from.y += (int)Math.signum(delta.y);
            else
                from.z += (int)Math.signum(delta.z);
    }

    public static boolean contains(Set<Point3i> interior, int x, int y, int z)
    {
        Point3i p = new Point3i(x, y, z);
        if (interior.contains(p))
            return true;
        else
            return false;
    }
}
