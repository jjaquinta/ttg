package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.List;

public class Ship2Design
{
	private String		mID;
    // data members
    private String      mShipName;
    private String      mShipClass;
    private int         mSize;
    private char        mJumpType;
    private char        mManType;
    private char        mPowType;
    private int         mFuelSize;
    private int         mHoldSize;
    private int         mCabins;
    private int         mBerths;
    private int         mComp;
    private double      mCost;
    private boolean     mStreamlined;
    private boolean     mMilitary;
    private List<Ship2HardpointDesign> mHardpoints;

    public static final int S_COMP1    = 1;
    public static final int S_COMP1BIS = 2;
    public static final int S_COMP2    = 3;
    public static final int S_COMP2BIS = 4;
    public static final int S_COMP3    = 5;
    public static final int S_COMP4    = 6;
    public static final int S_COMP5    = 7;
    public static final int S_COMP6    = 8;
    public static final int S_COMP7    = 9;

    // position constants
    public static final int P_PILOT    = 1;
    public static final int P_NAV      = 2;
    public static final int P_ENGINEER = 3;
    public static final int P_STEWARD  = 4;
    public static final int P_MEDIC    = 5;
    public static final int P_GUNNER   = 6;
    
	public static final int	TYPE_SMALL_CRAFT	= 0;
	public static final int	TYPE_SPACESHIP	= 1;
	public static final int	TYPE_STARSHIP	= 2;

    public Ship2Design()
    {
        mHardpoints = new ArrayList<Ship2HardpointDesign>();
    }

    // GETS
    public String   getName()       { return mShipName; }
    public int      getHoldSize()   { return mHoldSize; }
    public int      getCabins()     { return mCabins; }
    public int      getBerths()     { return mBerths; }
    public int      getFuelSize()   { return mFuelSize; }
    public double   getCost()       { return mCost; }
    public int      getComp()       { return mComp; }

    public int getSize()
    {
        return mSize;
    }

    public void setSize(int size)
    {
        this.mSize = size;
    }

    public char getJumpType()
    {
        return mJumpType;
    }

    public void setJumpType(char jumpType)
    {
        this.mJumpType = jumpType;
    }

    public char getManType()
    {
        return mManType;
    }

    public void setManType(char manType)
    {
        this.mManType = manType;
    }

    public char getPowType()
    {
        return mPowType;
    }

    public void setPowType(char powType)
    {
        this.mPowType = powType;
    }

    public void setComp(int comp)
    {
        this.mComp = comp;
    }

    public String getShipName()
    {
        return mShipName;
    }

    public void setShipName(String shipName)
    {
        mShipName = shipName;
    }

    public String getShipClass()
    {
        return mShipClass;
    }

    public void setShipClass(String shipClass)
    {
        mShipClass = shipClass;
    }

    public boolean isStreamlined()
    {
        return mStreamlined;
    }

    public void setStreamlined(boolean streamlined)
    {
        mStreamlined = streamlined;
    }

    public boolean isMilitary()
    {
        return mMilitary;
    }

    public void setMilitary(boolean military)
    {
        mMilitary = military;
    }

    public void setFuelSize(int fuelSize)
    {
        mFuelSize = fuelSize;
    }

    public void setHoldSize(int holdSize)
    {
        mHoldSize = holdSize;
    }

    public void setCabins(int cabins)
    {
        mCabins = cabins;
    }

    public void setBerths(int berths)
    {
        mBerths = berths;
    }

    public void setCost(double cost)
    {
        mCost = cost;
    }

	public String getID()
	{
		return mID;
	}

	public void setID(String iD)
	{
		mID = iD;
	}

	public List<Ship2HardpointDesign> getHardpoints()
	{
		return mHardpoints;
	}

	public void setHardpoints(List<Ship2HardpointDesign> hardpoints)
	{
		mHardpoints = hardpoints;
	}
}
