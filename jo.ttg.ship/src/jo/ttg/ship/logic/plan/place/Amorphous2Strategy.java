package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.IPlacementStrategy;
import jo.util.utils.MathUtils;

public class Amorphous2Strategy implements IPlacementStrategy
{
    private Point3i[] mControlPoints;
    private ShipPlanBean mLastPlan;
    private int   mCompartment;
    private int   mExtends;
    private Point3i mSize = new Point3i(2, 2, 1);
    
    public Amorphous2Strategy(Point3i... controlPoints)
    {
        mControlPoints = controlPoints;
        mLastPlan = null;
    }
    
    public Amorphous2Strategy(int extention, Point3i... controlPoints)
    {
        this(controlPoints);
        mExtends = extention;
    }

    @Override
    public void place(ShipPlanBean plan, List<PlanItem> items)
    {
        if (mLastPlan != plan)
        {
            mLastPlan = plan;
            mCompartment = 1;
        }
        else
            mCompartment++;
        List<Point3i> freeSpace = makeFreeSpace(plan);
        for (PlanItem item : items)
        {
            int count = item.getQuantity();
            for (int i = 0; i < count; i++)
            {
                if (!allocate(item.getVolume(), item.getType(), item.getNotes(), plan, freeSpace, mCompartment, mSize))
                    break;
            }
        }
    }

    private List<Point3i> makeFreeSpace(ShipPlanBean plan)
    {
        List<Point3i> cps = makeLocalCPs(plan);
        List<Point3i> freeSpace = new ArrayList<Point3i>();
        freeSpace.addAll(getFreeSpace(plan, ShipSquareBean.UNSET, mExtends, mSize));
        final Map<Point3i, Double> dist = makeDistances(cps, freeSpace);
        Collections.sort(freeSpace, new Comparator<Point3i>() {
            @Override
            public int compare(Point3i o1, Point3i o2)
            {
                double d1 = dist.get(o1);
                double d2 = dist.get(o2);
                return (int)Math.signum(d1 - d2);
            }
        });
        return freeSpace;
    }

    private Map<Point3i, Double> makeDistances(List<Point3i> cps,
            List<Point3i> freeSpace)
    {
        Map<Point3i,Double> dist = new HashMap<>();
        for (Point3i p : freeSpace)
        {
            double d = Double.MAX_VALUE;
            for (Point3i cp : cps)
                d = Math.min(d, dist(p, cp));
            dist.put(p, d);
        }
        return dist;
    }
    
    private double dist(Point3i p1, Point3i p2)
    {
        int dx = p1.x - p2.x;
        int dy = p1.y - p2.y;
        int dz = (p1.z - p2.z)*2;
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private List<Point3i> makeLocalCPs(ShipPlanBean plan)
    {
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        List<Point3i> cps = new ArrayList<>();
        for (Point3i p : mControlPoints)
        {
            Point3i cp = new Point3i();
            cp.x = (int)MathUtils.interpolate(p.x, -10, 10, lower.x, upper.x);
            cp.y = (int)MathUtils.interpolate(p.y, -10, 10, lower.y, upper.y);
            cp.z = (int)MathUtils.interpolate(p.z, -10, 10, lower.z, upper.z);
            cps.add(cp);
        }
        return cps;
    }

    public static Set<Point3i> getFreeSpace(ShipPlanBean plan, int freeType, int xtends, Point3i size)
    {
        // find candidates
        Set<Point3i> candidates = new HashSet<Point3i>();        
        for (Iterator<Point3i> i = plan.getSquares().iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            if (plan.getSquares().get(p).getType() == freeType)
            {
                candidates.add(p);
                if (plan.getSquare(p.x + xtends, p.y, p.z) == null)
                    candidates.add(new Point3i(p.x + xtends, p.y, p.z));
                if (plan.getSquare(p.x - xtends, p.y, p.z) == null)
                    candidates.add(new Point3i(p.x - xtends, p.y, p.z));
                if (plan.getSquare(p.x, p.y + xtends, p.z) == null)
                    candidates.add(new Point3i(p.x, p.y + xtends, p.z));
                if (plan.getSquare(p.x - xtends, p.y, p.z) == null)
                    candidates.add(new Point3i(p.x, p.y - xtends, p.z));
            }
        }
        Set<Point3i> freeSpace = new HashSet<Point3i>();        
        // adjust for size
        if ((size.y > 1) || (size.y > 1) || (size.z > 1))
        {
            for (Point3i p : candidates)
                if (doesThisFit(p, candidates, size))
                    freeSpace.add(p);
        }
        else
            freeSpace.addAll(candidates);
        return freeSpace;
    }

    private static boolean doesThisFit(Point3i p, Set<Point3i> available, Point3i size)
    {
        Point3i p2 = new Point3i();
        for (int dx = 0; dx < size.x; dx++)
        {
            p2.x = p.x + dx;
            for (int dy = 0; dy < size.y; dy++)
            {
                p2.y = p.y + dy;
                for (int dz = 0; dz < size.z; dz++)
                    if ((dx != 0) || (dy != 0) || (dz != 0))
                    {
                        p2.z = p.z + dz;
                        if (!available.contains(p2))
                            return false;
                    }
            }
        }
        return true;
    }
    
    public static boolean allocate(Double volume, int type, String notes, ShipPlanBean plan,
            List<Point3i> freeSpace, int compartment, Point3i size)
    {
        if (volume == null)
            return true;
        int todo = (int)Math.ceil(volume/6.75);
        while (todo > 0)
        {
            if (freeSpace.size() == 0)
            {
                plan.println("Out of space (need "+todo+" more) allocating #"+type+", compartment "+compartment+" notes="+notes);
                return false;
            }
            Point3i p = freeSpace.get(0);
            freeSpace.remove(0);
            for (int dx = 0; dx < size.x; dx++)
                for (int dy = 0; dy < size.y; dy++)
                    for (int dz = 0; dz < size.z; dz++)
                    {
                        Point3i p2 = new Point3i(p.x + dx, p.y + dy, p.z + dz);
                        if (!plan.isType(p2, type))
                        {
                            ShipSquareBean sq = new ShipSquareBean(p2, type);
                            sq.setType(type);
                            sq.setCompartment(compartment);
                            sq.setNotes(notes);
                            plan.setSquare(sq);
                            todo--;
                        }
                    }
        }
        return true;
    }

}
