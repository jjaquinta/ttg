package jo.ttg.port.beans;

import javax.vecmath.Point3i;

import org.json.simple.JSONObject;

import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.IntegerUtils;

public class PortSquareBean
{
    private Point3i          mPoint;
    private PortItemBean     mItem;
    private int              mItemInstance = 1;
    private boolean          mCeilingAccess;
    private boolean          mFloorAccess;
    private boolean          mPlusXAccess;
    private boolean          mMinusXAccess;
    private boolean          mPlusYAccess;
    private boolean          mMinusYAccess;

    // constructor

    public PortSquareBean(Point3i p, PortItemBean item, int instnace)
    {
        mPoint = p;
        mItem = item;
        mItemInstance = instnace;
    }

    public PortSquareBean(int x, int y, int z, PortItemBean item, int instnace)
    {
        this(new Point3i(x, y, z), item, instnace);
    }

    public PortSquareBean(Point3i p, PortItemBean item)
    {
        this(p, item, 1);
    }

    public PortSquareBean(int x, int y, int z, PortItemBean item)
    {
        this(new Point3i(x, y, z), item, 1);
    }

    public PortSquareBean(Point3i p, PortItemInstance ii)
    {
        this(p, ii.getItem(), ii.getInstance());
    }

    public PortSquareBean(int x, int y, int z, PortItemInstance ii)
    {
        this(new Point3i(x, y, z), ii.getItem(), ii.getInstance());
    }

    public PortSquareBean(PortSquareBean sq)
    {
        mPoint = sq.mPoint;
        mItem = sq.mItem;
        mItemInstance = sq.mItemInstance;
        mCeilingAccess = sq.mCeilingAccess;
        mFloorAccess = sq.mFloorAccess;
        mPlusXAccess = sq.mPlusXAccess;
        mMinusXAccess = sq.mMinusXAccess;
        mPlusYAccess = sq.mPlusYAccess;
        mMinusYAccess = sq.mMinusYAccess;
    }

    public PortSquareBean(JSONObject json)
    {
        fromJSON(json);
    }

    // utilities

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer(mItem.toString());
        sb.append(" " + mPoint);
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
        mCeilingAccess = BooleanUtils.parseBoolean(json.get("ceilingAccess"));
        mFloorAccess = BooleanUtils.parseBoolean(json.get("floorAccess"));
        mPlusXAccess = BooleanUtils.parseBoolean(json.get("plusXAccess"));
        mMinusXAccess = BooleanUtils.parseBoolean(json.get("minusXAccess"));
        mPlusYAccess = BooleanUtils.parseBoolean(json.get("plusYAccess"));
        mMinusYAccess = BooleanUtils.parseBoolean(json.get("minusYAccess"));
    }

    // getters and setters

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

    public PortItemBean getItem()
    {
        return mItem;
    }

    public void setItem(PortItemBean item)
    {
        mItem = item;
    }

    public int getItemInstance()
    {
        return mItemInstance;
    }

    public void setItemInstance(int itemInstance)
    {
        mItemInstance = itemInstance;
    }
}
