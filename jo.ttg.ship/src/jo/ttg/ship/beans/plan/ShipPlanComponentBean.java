package jo.ttg.ship.beans.plan;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.vecmath.Point3i;

public class ShipPlanComponentBean extends ShipPlanPerimeterBean
{
    private List<ShipPlanPerimeterBean> mSubComponents = new ArrayList<ShipPlanPerimeterBean>();
    
    // constructors
    
    public ShipPlanComponentBean(ShipPlanBean ship, int type, int compartment, String notes)
    {
        super(ship, type, compartment, notes);
    }
    
    public ShipPlanComponentBean(ShipPlanBean ship, ShipSquareBean sq)
    {
        super(ship, sq);
    }

    // utilities
    
    @Override
    public String toString()
    {
        return super.toString()+", "+mSubComponents.size()+" sub components";
    }
    
    public void updatePerimeter()
    {
        if (!mNeedsPermiterUpdate)
            return;
        super.updatePerimeter();
        mSubComponents.clear();
        if ((getType() == ShipSquareBean.UNSET) || (getType() == ShipSquareBean.CORRIDOR) || (getType() == ShipSquareBean.HULL))
            return;
        findContiguous(getSquares());
    }
    
    private void findContiguous(Collection<Point3i> points)
    {
        // pass one
        List<Set<Point3i>> groups = new ArrayList<>();
        for (Point3i p : points)
        {
            for (Set<Point3i> group : groups)
                if (nextTo(group, p))
                {
                    group.add(p);
                    p = null;
                    break;
                }
            if (p != null)
            {
                Set<Point3i> group = new HashSet<>();
                group.add(p);
                groups.add(group);
            }
        }
        // pass 2
        for (int i = 0; i < groups.size() - 1; i++)
        {
            Set<Point3i> g1 = groups.get(i);
            for (int j = i + 1; j < groups.size(); j++)
            {
                Set<Point3i> g2 = groups.get(j);
                if (nextTo(g1, g2))
                {
                    g1.addAll(g2);
                    groups.remove(j);
                    j--;
                }
            }
        }
        // pass 3
        for (Set<Point3i> g : groups)
        {
            Point3i[] ps = g.toArray(new Point3i[0]);
            ShipPlanPerimeterBean peri = new ShipPlanPerimeterBean(getShip(), getShip().getSquare(ps[0]));
            for (int i = 1; i < ps.length; i++)
                peri.add(getShip().getSquare(ps[i]));
            mSubComponents.add(peri);
        }
    }
    
    private boolean nextTo(Collection<Point3i> g1, Collection<Point3i> g2)
    {
        for (Point3i p : g2)
            if (nextTo(g1, p))
                return true;
        return false;
    }
    
    private boolean nextTo(Collection<Point3i> group, Point3i p)
    {
        for (Point3i p2 : group)
            if (nextTo(p, p2))
                return true;
        return false;
    }
    
    private boolean nextTo(Point3i p1, Point3i p2)
    {
        if (p1.z != p2.z)
            return false;
        int s1 = Math.abs(p1.x - p2.x);
        int s2 = Math.abs(p1.y - p2.y);
        if ((s1 == 0) && (s2 == 1))
            return true;
        if ((s2 == 0) && (s1 == 1))
            return true;
        return false;
    }
    
    public ShipPlanPerimeterBean findSubComponent(Point3i p)
    {
        updatePerimeter();
        for (ShipPlanPerimeterBean peri : getSubComponents())
            if (peri.contains(p))
                return peri;
        return null;
    }
    
    public List<ShipPlanPerimeterBean> findSubComponents(int z)
    {
        updatePerimeter();
        List<ShipPlanPerimeterBean> peris = new ArrayList<>();
        for (ShipPlanPerimeterBean peri : getSubComponents())
            if (peri.getSquares().iterator().next().z == z)
                peris.add(peri);
        return peris;
    }
    
    // getters and setters

    public List<ShipPlanPerimeterBean> getSubComponents()
    {
        return mSubComponents;
    }

    public void setSubComponents(List<ShipPlanPerimeterBean> subComponents)
    {
        mSubComponents = subComponents;
    }
    
}
