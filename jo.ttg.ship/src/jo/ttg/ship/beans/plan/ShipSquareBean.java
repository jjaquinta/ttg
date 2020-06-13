package jo.ttg.ship.beans.plan;

import javax.vecmath.Point3i;

import org.json.simple.JSONObject;

import jo.ttg.ship.logic.plan.ShipPlanUtils;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.IntegerUtils;

public class ShipSquareBean
{
    public static final int UNSET = 0;
    public static final int HULL = 1;
    public static final int MANEUVER = 2;
    public static final int JUMP = 3;
    public static final int POWER = 4;
    public static final int FUEL = 5;
    public static final int TURRET = 6;
    public static final int CARGO = 7;
    public static final int HANGER = 8;
    public static final int BAY = 9;
    public static final int SPINE = 10;
    public static final int STATEROOM = 11;
    public static final int CORRIDOR = 12;

    public static final String[] NAMES = {
            "UNSET",
            "HULL",
            "MANEUVER",
            "JUMP",
            "POWER",
            "FUEL",
            "TURRET",
            "CARGO",
            "HANGER",
            "BAY",
            "SPINE",
            "STATEROOM",
            "CORRIDOR",
    };

    public static final char[] SYMBOLS = {
            '.', // UNSET
            '@', // HULL
            'M', // MANEUVER
            'J', // JUMP
            'P', // POWER
            '~', // FUEL
            't', // TURRET
            '_', // CARGO
            'H', // HANGER
            'b', // BAY
            '=', // SPINE
            '%', // STATEROOM
            '#', // CORRIDOR
    };
    
    private Point3i mPoint;
    private int mType;
    private int mCompartment;
    private String  mNotes;
    private Boolean mNeedsAir;
    private boolean mCeilingAccess;
    private boolean mFloorAccess;
    private boolean mPlusXAccess;
    private boolean mMinusXAccess;
    private boolean mPlusYAccess;
    private boolean mMinusYAccess;

    // constructor
    
    public ShipSquareBean(Point3i p, int type)
    {
        mPoint = p;
        mType = type;
        if (ShipPlanUtils.needsAir(mType))
            mNeedsAir = Boolean.TRUE;
        else
            mNeedsAir = Boolean.FALSE;
    }
    
    public ShipSquareBean(int x, int y, int z, int type)
    {
        this(new Point3i(x, y, z), type);
    }

    public ShipSquareBean(ShipSquareBean sq)
    {
        mPoint = sq.mPoint;
        mType = sq.mType;
        mCompartment = sq.mCompartment;
        mNotes = sq.mNotes;
        mNeedsAir = sq.mNeedsAir;
        mCeilingAccess = sq.mCeilingAccess;
        mFloorAccess = sq.mFloorAccess;
        mPlusXAccess = sq.mPlusXAccess;
        mMinusXAccess = sq.mMinusXAccess;
        mPlusYAccess = sq.mPlusYAccess;
        mMinusYAccess = sq.mMinusYAccess;
    }

    public ShipSquareBean(JSONObject json)
    {
        fromJSON(json);
    }
    
    // utilities

    public boolean isNeedsAir()
    {
        return mNeedsAir == Boolean.TRUE;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer(NAMES[mType]);
        if (mCompartment > 0)
            sb.append(":"+mCompartment);
        if (mNotes != null)
            sb.append(" "+mNotes);
        sb.append(" "+mPoint);
        return sb.toString();
    }
    
    // I/O

    public JSONObject toJSON()
    {
        JSONObject json = new JSONObject();
        if (mPoint != null)
        {
            json.put("x", mPoint.x);
            json.put("y", mPoint.y);
            json.put("z", mPoint.z);
        }
        json.put("type", mType);
        json.put("compartment", mCompartment);
        if (mNotes != null)
            json.put("notes", mNotes);
        if (mNeedsAir != null)
            json.put("needsAir", mNeedsAir);
        json.put("ceilingAccess", mCeilingAccess);
        json.put("floorAccess", mFloorAccess);
        json.put("plusXAccess", mPlusXAccess);
        json.put("minusXAccess", mMinusXAccess);
        json.put("plusYAccess", mPlusYAccess);
        json.put("minusYAccess", mMinusYAccess);
        return json;
    }

    public void fromJSON(JSONObject json)
    {
        mPoint = new Point3i();
        mPoint.x = IntegerUtils.parseInt(json.get("x"));
        mPoint.y = IntegerUtils.parseInt(json.get("y"));
        mPoint.z = IntegerUtils.parseInt(json.get("z"));
        mType = IntegerUtils.parseInt(json.get("type"));
        mCompartment = IntegerUtils.parseInt(json.get("compartment"));
        if (json.containsKey("notes"))
            mNotes = json.getString("notes");
        if (json.containsKey("needsAir"))
            mNeedsAir = BooleanUtils.parseBoolean(json.get("needsAir"));
        mCeilingAccess = BooleanUtils.parseBoolean(json.get("ceilingAccess"));
        mFloorAccess = BooleanUtils.parseBoolean(json.get("floorAccess"));
        mPlusXAccess = BooleanUtils.parseBoolean(json.get("plusXAccess"));
        mMinusXAccess = BooleanUtils.parseBoolean(json.get("minusXAccess"));
        mPlusYAccess = BooleanUtils.parseBoolean(json.get("plusYAccess"));
        mMinusYAccess = BooleanUtils.parseBoolean(json.get("minusYAccess"));
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

    public boolean isCeilingAccess()
    {
        return mCeilingAccess;
    }

    public void setCeilingAccess(boolean ceilingAccess)
    {
        mCeilingAccess = ceilingAccess;
    }

    public boolean isFloorAccess()
    {
        return mFloorAccess;
    }

    public void setFloorAccess(boolean floorAccess)
    {
        mFloorAccess = floorAccess;
    }

    public boolean isPlusXAccess()
    {
        return mPlusXAccess;
    }

    public void setPlusXAccess(boolean plusXAccess)
    {
        mPlusXAccess = plusXAccess;
    }

    public boolean isMinusXAccess()
    {
        return mMinusXAccess;
    }

    public void setMinusXAccess(boolean minusXAccess)
    {
        mMinusXAccess = minusXAccess;
    }

    public boolean isPlusYAccess()
    {
        return mPlusYAccess;
    }

    public void setPlusYAccess(boolean plusYAccess)
    {
        mPlusYAccess = plusYAccess;
    }

    public boolean isMinusYAccess()
    {
        return mMinusYAccess;
    }

    public void setMinusYAccess(boolean minusYAccess)
    {
        mMinusYAccess = minusYAccess;
    }

    public Point3i getPoint()
    {
        return mPoint;
    }

    public void setPoint(Point3i point)
    {
        mPoint = point;
    }

    public Boolean getNeedsAir()
    {
        return mNeedsAir;
    }

    public void setNeedsAir(Boolean needsAir)
    {
        mNeedsAir = needsAir;
    }
    
}
