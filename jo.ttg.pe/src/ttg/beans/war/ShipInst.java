package ttg.beans.war;

import java.util.ArrayList;

public class ShipInst
{
	private Ship			mShip;
	private SideInst		mSideInst;
	private ShipInst		mContainedBy;
	private ArrayList		mContains;
	private boolean			mDamaged;
	private boolean			mHasMoved;
	private boolean			mHasFired;
	private boolean			mFleeing;
	private boolean			mToDo;
	private WorldInst		mLocation;
	private WorldInst		mDestination;
	private ShipInst		mTarget;
	private int				mFuel;
	
	public ShipInst()
	{
		mContains = new ArrayList();
	}
	
    public ArrayList getContains()
    {
        return mContains;
    }

    public boolean isDamaged()
    {
        return mDamaged;
    }

    public WorldInst getDestination()
    {
        return mDestination;
    }

    public boolean isHasFired()
    {
        return mHasFired;
    }

    public boolean isHasMoved()
    {
        return mHasMoved;
    }

    public WorldInst getLocation()
    {
        return mLocation;
    }

    public Ship getShip()
    {
        return mShip;
    }

    public ShipInst getTarget()
    {
        return mTarget;
    }

    public void setContains(ArrayList list)
    {
        mContains = list;
    }

    public void setDamaged(boolean b)
    {
        mDamaged = b;
    }

    public void setDestination(WorldInst bean)
    {
        mDestination = bean;
    }

    public void setHasFired(boolean b)
    {
        mHasFired = b;
    }

    public void setHasMoved(boolean b)
    {
        mHasMoved = b;
    }

    public void setLocation(WorldInst bean)
    {
        mLocation = bean;
    }

    public void setShip(Ship ship)
    {
        mShip = ship;
    }

    public void setTarget(ShipInst inst)
    {
        mTarget = inst;
    }

    public SideInst getSideInst()
    {
        return mSideInst;
    }

    public void setSideInst(SideInst inst)
    {
        mSideInst = inst;
    }

    public boolean isFleeing()
    {
        return mFleeing;
    }

    public void setFleeing(boolean b)
    {
        mFleeing = b;
    }

    public boolean isToDo()
    {
        return mToDo;
    }

    public void setToDo(boolean b)
    {
        mToDo = b;
    }

	/**
	 * @return
	 */
	public ShipInst getContainedBy()
	{
		return mContainedBy;
	}

	/**
	 * @param inst
	 */
	public void setContainedBy(ShipInst inst)
	{
		mContainedBy = inst;
	}

    public int getFuel()
    {
        return mFuel;
    }

    public void setFuel(int i)
    {
        mFuel = i;
    }

}
