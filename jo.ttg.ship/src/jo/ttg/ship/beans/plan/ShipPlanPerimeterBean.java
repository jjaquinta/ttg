package jo.ttg.ship.beans.plan;

import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Point3i;

import jo.ttg.ship.logic.plan.ShipPlanUtils;

public class ShipPlanPerimeterBean
{
    private ShipPlanBean mShip;
    private int mType;
    private int mCompartment;
    private String  mNotes;
    private Set<Point3i> mSquares = new HashSet<>();
    private Set<Point3i> mPerimeter = new HashSet<>();
    
    protected boolean mNeedsPermiterUpdate = true;
    
    // constructors
    
    public ShipPlanPerimeterBean(ShipPlanBean ship, int type, int compartment, String notes)
    {
        mShip = ship;
        mType = type;
        mCompartment = compartment;
        mNotes = notes;
    }
    
    public ShipPlanPerimeterBean(ShipPlanBean ship, ShipSquareBean sq)
    {
        this(ship, sq.getType(), sq.getCompartment(), sq.getNotes());
        add(sq);
    }

    // utilities
    
    @Override
    public String toString()
    {
        String str = ShipSquareBean.NAMES[mType]+" #"+mCompartment;
        if (mNotes != null)
            str += " " + mNotes;
        return str;
    }

    public static String getKey(int type, int compartment, String notes)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(type);
        sb.append(":");
        sb.append(compartment);
        if (notes != null)
        {
            sb.append(":");
            sb.append(notes);
        }
        return sb.toString();
    }
    
    public static String getKey(ShipSquareBean sq)
    {
        return getKey(sq.getType(), sq.getCompartment(), sq.getNotes());
    }
    
    public String getKey()
    {
        return getKey(mType, mCompartment, mNotes);
    }
    
    public void add(ShipSquareBean sq)
    {
        mSquares.add(sq.getPoint());
        mNeedsPermiterUpdate = true;
    }
    
    public void remove(ShipSquareBean sq)
    {
        mSquares.remove(sq.getPoint());
        mPerimeter.remove(sq.getPoint());
        mNeedsPermiterUpdate = true;
    }
    
    public void updatePerimeter()
    {
        if (!mNeedsPermiterUpdate)
            return;
        mPerimeter.clear();
        for (Point3i p : mSquares)
            if (isEdge(p))
                mPerimeter.add(p);
        mNeedsPermiterUpdate = false;
    }
    
    private boolean isEdge(Point3i p)
    {
        for (Point3i n : ShipPlanUtils.getNeighbors(p, false))
            if (!mSquares.contains(n))
                return true;
        return false;
    }
    
    public boolean needsAir()
    {
        updatePerimeter();
        for (Point3i p : mPerimeter)
            if (mShip.getSquare(p).isNeedsAir())
                return true;
        return false;
    }
    
    public boolean contains(Point3i p)
    {
        return mSquares.contains(p);
    }
    
    // getters and setters
    
    public int getType()
    {
        return mType;
    }

    public void setType(int type)
    {
        mType = type;
    }

    public int getCompartment()
    {
        return mCompartment;
    }

    public void setCompartment(int compartment)
    {
        mCompartment = compartment;
    }

    public String getNotes()
    {
        return mNotes;
    }

    public void setNotes(String notes)
    {
        mNotes = notes;
    }

    public Set<Point3i> getSquares()
    {
        return mSquares;
    }

    public void setSquares(Set<Point3i> squares)
    {
        mSquares = squares;
    }

    public Set<Point3i> getPerimeter()
    {
        return mPerimeter;
    }

    public void setPerimeter(Set<Point3i> perimeter)
    {
        mPerimeter = perimeter;
    }

    public ShipPlanBean getShip()
    {
        return mShip;
    }

    public void setShip(ShipPlanBean ship)
    {
        mShip = ship;
    }
    
}
