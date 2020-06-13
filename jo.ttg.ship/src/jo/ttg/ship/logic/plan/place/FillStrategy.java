package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;

public class FillStrategy implements Comparator<Point3i>
{
    public static final int MINUS = 0;
    public static final int PLUS = 1;
    public static final int CENTER = 2;
    public static final int OUTSIDE = 3;
    public static final int LOWQ = 4;
    public static final int HIGHQ = 5;
    public static final int BOTHQ = 6;
    public static final int NONE = 6;
    
    public static final int X = 0x01;
    public static final int Y = 0x02;
    public static final int Z = 0x04;

    private int[] mStrategy;
    private int[] mAxis;
    private Point3i mLower;
    private Point3i mUpper;
    
    public FillStrategy(int[] strategy, int[] axis, ShipPlanBean ship)
    {
        mStrategy = strategy;
        mAxis = axis;
        mLower = new Point3i();
        mUpper = new Point3i();
        ship.getSquares().getBounds(mLower, mUpper);
    }
    
//    private Set<Point3i> done = new HashSet<Point3i>();
    
    @Override
    public int compare(Point3i o1, Point3i o2)
    {
        double weight1 = getWeight(o1);
        double weight2 = getWeight(o2);
        return (int)Math.signum(weight1 - weight2);
    }
    public int compare(int axis, int strategy, Point3i o1, Point3i o2)
    {
        int weight1 = 0;
        int weight2 = 0;
        if ((axis&X) != 0)
        {
            weight1 += getWeight(strategy, o1.x, mLower.x, mUpper.x);
            weight2 += getWeight(strategy, o2.x, mLower.x, mUpper.x);
        }
        if ((axis&Y) != 0)
        {
            weight1 += getWeight(strategy, o1.y, mLower.y, mUpper.y);
            weight2 += getWeight(strategy, o2.y, mLower.y, mUpper.y);
        }
        if ((axis&Z) != 0)
        {
            weight1 += getWeight(strategy, o1.z, mLower.z, mUpper.z);
            weight2 += getWeight(strategy, o2.z, mLower.z, mUpper.z);
        }
        return weight1 - weight2;
    }

    private double getWeight(Point3i o)
    {
        double weight = 0;
        for (int i = 0; i < mAxis.length; i++)
        {
            if ((mAxis[i]&X) != 0)
            {
                weight += getWeight(mStrategy[i], o.x, mLower.x, mUpper.x);
            }
            if ((mAxis[i]&Y) != 0)
            {
                weight += getWeight(mStrategy[i], o.y, mLower.y, mUpper.y);
            }
            if ((mAxis[i]&Z) != 0)
            {
                weight += getWeight(mStrategy[i], o.z, mLower.z, mUpper.z);
            }
        }
        return weight;
    }

    // the higher the value the less desirable this position is
    private double getWeight(int strategy, int v, int low, int high)
    {
        double weight = 0;
        if (strategy == MINUS)
            weight = v - low;
        else if (strategy == PLUS)
            weight = high - v;
        else if (strategy == CENTER)
            weight = Math.abs(v);
        else if (strategy == OUTSIDE)
            weight = Math.min(v - low, high - v);
        else if (strategy == HIGHQ)
            weight = Math.abs(v - (high*3 + low)/4);
        else if (strategy == LOWQ)
            weight = Math.abs(v - (low*3 + high)/4);
        else if (strategy == BOTHQ)
            weight = Math.min(Math.abs(v - (high*3 + low)/4), Math.abs(v - (low*3 + high)/4));
        else if (strategy == NONE)
            return 0;
        return weight/(high - low);
    }
   
    public static Map<Double, List<PlanItem>> groupItems(List<PlanItem> items)
    {
        Map<Double, List<PlanItem>> groups = new HashMap<Double, List<PlanItem>>();
        for (PlanItem item : items)
        {
            List<PlanItem> g = groups.get(item.getVolume());
            if (g == null)
            {
                g = new ArrayList<>();
                groups.put(item.getVolume(), g);
            }
            g.add(item);
        }
        return groups;
    }
    
    public static Map<String, List<PlanItem>> groupItemsByNotes(List<PlanItem> items)
    {
        Map<String, List<PlanItem>> groups = new HashMap<String, List<PlanItem>>();
        for (PlanItem item : items)
        {
            String n = item.getNotes();
            List<PlanItem> g = groups.get(n);
            if (g == null)
            {
                g = new ArrayList<>();
                groups.put(n, g);
            }
            g.add(item);
        }
        return groups;
    }

    public static void markComponent(ShipPlanBean plan, Point3i todo, int xSize,
            int ySize, int zSize, PlanItem item, int compartment)
    {
        for (int dx = 0; dx < xSize; dx++)
            for (int dy = 0; dy < ySize; dy++)
                for (int dz = 0; dz < zSize; dz++)
                {
                    ShipSquareBean sq = new ShipSquareBean(todo.x + dx, todo.y + dy, todo.z + dz, item.getType());
                    sq.setType(item.getType());
                    sq.setCompartment(compartment);
                    sq.setNotes(item.getNotes());
                    plan.setSquare(sq);
                }
    }
}
