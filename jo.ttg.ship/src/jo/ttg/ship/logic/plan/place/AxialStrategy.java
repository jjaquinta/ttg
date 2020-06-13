package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.IPlacementStrategy;

public class AxialStrategy implements IPlacementStrategy
{
    public static final int X_AXIS = 0;
    public static final int Y_AXIS = 1;
    public static final int Z_AXIS = 2;
    
    private int mAxis;
    private int mExtends;
    private ShipPlanBean mLastPlan;
    private int   mCompartment;
    
    public AxialStrategy(int axis, int extension)
    {
        mAxis = axis;
        mExtends = extension;
        mLastPlan = null;
    }

    @Override
    public void place(ShipPlanBean plan, List<PlanItem> items)
    {
        Map<Double, List<PlanItem>> groups = FillStrategy.groupItems(items);
        for (List<PlanItem> g : groups.values())
            doPlace(plan, g);
    }
    
    private void doPlace(ShipPlanBean plan, List<PlanItem> items)
    {
        if (mLastPlan != plan)
        {
            mLastPlan = plan;
            mCompartment = 1;
        }
        else
            mCompartment++;
        List<Point3i> freeSpace = getFreeSpace(plan, items.get(0).getVolume());
        for (PlanItem item : items)
            allocate(item.getVolume(), item.getType(), item.getNotes(), plan, freeSpace);
    }

    private List<Point3i> getFreeSpace(ShipPlanBean plan, double targetVolume)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        List<Point3i> freeSpace = new ArrayList<Point3i>();
        final Map<Point3i,Double> dist = new HashMap<>();
        int upperA = get(upper, mAxis);
        int lowerA = get(lower, mAxis);
        int baseLength = upperA - lowerA;
        int ext = (baseLength*mExtends)/100;
        int alow = lowerA - ext;
        int ahigh = upperA + ext;
        int length = ahigh - alow + 1;
        double r = Math.sqrt(targetVolume/(Math.PI*length*1.5));
        int rad = (int)Math.ceil(r);
        for (int a1 = -rad; a1 <= rad; a1++)
            for (int a2 = -rad; a2 <= rad; a2++)
            {
                double sr = Math.sqrt(a1*a1 + a2*a2);
                if (sr > r)
                    continue;
                for (int a0 = alow; a0 <= ahigh; a0++)
                {
                    Point3i p = makePoint(a0, a1, a2);
                    freeSpace.add(p);
                    dist.put(p, sr);
                }
            }
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
    
    private Point3i makePoint(int a0, int a1, int a2)
    {
        if (mAxis == X_AXIS)
            return new Point3i(a0, a1, a2);
        else if (mAxis == Y_AXIS)
            return new Point3i(a2, a0, a1);
        else if (mAxis == Z_AXIS)
            return new Point3i(a1, a2, a0);
        throw new IllegalArgumentException("No such axis "+mAxis);
    }
    
    private int get(Point3i p, int axis)
    {
        if (axis == X_AXIS)
            return p.x;
        else if (axis == Y_AXIS)
            return p.y;
        else if (axis == Z_AXIS)
            return p.z;
        throw new IllegalArgumentException("No such axis "+axis);
    }
    
    private boolean allocate(Double volume, int type, String notes, ShipPlanBean plan,
            List<Point3i> freeSpace)
    {
        if (volume == null)
            return true;
        int todo = (int)Math.ceil(volume/6.75);
        for (int i = 0; i < todo; i++)
        {
            if (freeSpace.size() == 0)
            {
                plan.println("Out of space (need "+(todo - i)+" more) allocating #"+type+", compartment "+mCompartment+" notes="+notes);
                return false;
            }
            Point3i ords = freeSpace.get(0);
            freeSpace.remove(0);
            ShipSquareBean sq = new ShipSquareBean(ords, type);
            sq.setType(type);
            sq.setCompartment(mCompartment);
            sq.setNotes(notes);
            plan.setSquare(sq);
        }
        return true;
    }

}
