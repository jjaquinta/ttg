package jo.ttg.ship0.beans;

import java.util.StringTokenizer;

import jo.util.beans.Bean;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;

public class T0ShipDesign extends Bean
{
    public static final String SCHEME = "ship0://";
    
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
    //private T0ShipHardpoint mHardpoints[];
    private int[] mTurretSize;
    private int[] mMissiles;
    private int[] mSandcasters;
    private int[] mBeamLasers;
    private int[] mPulseLasers;


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

    public T0ShipDesign()
    {
        mTurretSize = new int[0];
    }

    // GETS
    public String   getName()       { return mShipName; }
    public int      getHoldSize()   { return mHoldSize; }
    public int      getCabins()     { return mCabins; }
    public int      getBerths()     { return mBerths; }
    public int      getFuelSize()   { return mFuelSize; }
    public double   getCost()       { return mCost; }
    public int      getComp()       { return mComp; }

    public String toString()
    {
        StringBuffer sb = new StringBuffer(
            mShipName+";"+
            mShipClass+";"+
            mSize+";"+
            mJumpType+";"+
            mManType+";"+
            mPowType+";"+
            mFuelSize+";"+
            mHoldSize+";"+
            mComp+";"+
            mCabins+";"+
            mBerths+";"+
            mCost+";"+
            mStreamlined+";"+
            mMilitary);
        for (int i = 0; i < mTurretSize.length; i++)
        {
            sb.append(";");
            sb.append(mTurretSize[i]);
            sb.append(":");
            sb.append(mMissiles[i]);
            sb.append(":");
            sb.append(mSandcasters[i]);
            sb.append(":");
            sb.append(mBeamLasers[i]);
            sb.append(":");
            sb.append(mPulseLasers[i]);
        }
        return sb.toString();
    }

    //"The Parshidona;Scout/Courier;100;A;A;A;40;4;4;0;2.883E7;true;true;0",
    public void fromString(String s)
    {
        StringTokenizer st = new StringTokenizer(s, ";");
        mShipName = st.nextToken();
        mShipClass = st.nextToken();
        mSize = IntegerUtils.parseInt(st.nextToken());
        mJumpType = st.nextToken().charAt(0);
        mManType = st.nextToken().charAt(0);
        mPowType = st.nextToken().charAt(0);
        mFuelSize = IntegerUtils.parseInt(st.nextToken());
        mHoldSize = IntegerUtils.parseInt(st.nextToken());
        mComp = IntegerUtils.parseInt(st.nextToken());
        mCabins = IntegerUtils.parseInt(st.nextToken());
        mBerths = IntegerUtils.parseInt(st.nextToken());
        mCost = DoubleUtils.parseDouble(st.nextToken());
        mStreamlined = BooleanUtils.parseBoolean(st.nextToken());
        mMilitary = BooleanUtils.parseBoolean(st.nextToken());
        mTurretSize = new int[mSize/100];
        mMissiles = new int[mSize/100];
        mSandcasters = new int[mSize/100];
        mBeamLasers = new int[mSize/100];
        mPulseLasers = new int[mSize/100];
        for (int i = 0; i < mTurretSize.length; i++)
        {
            StringTokenizer st2 = new StringTokenizer(st.nextToken(), ":");
            mTurretSize[i] = IntegerUtils.parseInt(st2.nextToken());
            mMissiles[i] = IntegerUtils.parseInt(st2.nextToken());
            mSandcasters[i] = IntegerUtils.parseInt(st2.nextToken());
            mBeamLasers[i] = IntegerUtils.parseInt(st2.nextToken());
            mPulseLasers[i] = IntegerUtils.parseInt(st2.nextToken());
        }
    }

    public String toURI()
    {
        URIBuilder uri = new URIBuilder();
        uri.setScheme(SCHEME);
        if (getOID() != -1)
            uri.setAuthority(String.valueOf(getOID()));
        uri.setQuery("ShipName", mShipName);
        uri.setQuery("ShipClass", mShipClass);
        uri.setQuery("Size", String.valueOf(mSize));
        uri.setQuery("JumpType", String.valueOf(mJumpType));
        uri.setQuery("ManType", String.valueOf(mManType));
        uri.setQuery("PowType", String.valueOf(mPowType));
        uri.setQuery("FuelSize", String.valueOf(mFuelSize));
        uri.setQuery("HoldSize", String.valueOf(mHoldSize));
        uri.setQuery("Comp", String.valueOf(mComp));
        uri.setQuery("Cabins", String.valueOf(mCabins));
        uri.setQuery("Berths", String.valueOf(mBerths));
        uri.setQuery("Cost", String.valueOf(mCost));
        uri.setQuery("Streamlined", String.valueOf(mStreamlined));
        uri.setQuery("Military", String.valueOf(mMilitary));
        for (int i = 0; i < mTurretSize.length; i++)
        {
            if (mTurretSize[i] > 0)
                uri.setQuery("TurretSize"+i, String.valueOf(mTurretSize[i]));
            if (mMissiles[i] > 0)
                uri.setQuery("Missiles"+i, String.valueOf(mMissiles[i]));
            if (mSandcasters[i] > 0)
                uri.setQuery("Sandcasters"+i, String.valueOf(mSandcasters[i]));
            if (mBeamLasers[i] > 0)
                uri.setQuery("BeamLasers"+i, String.valueOf(mBeamLasers[i]));
            if (mPulseLasers[i] > 0)
                uri.setQuery("PulseLasers"+i, String.valueOf(mPulseLasers[i]));
        }
        return uri.toString();
    }

    public void fromURI(String u)
    {
        URIBuilder uri = new URIBuilder(u);
        setOID(LongUtils.parseLong(uri.getAuthority()));
        mShipName = uri.getQuery("ShipName");
        mShipClass = uri.getQuery("ShipClass");
        mSize = IntegerUtils.parseInt(uri.getQuery("Size"));
        mJumpType = uri.getQuery("JumpType").charAt(0);
        mManType = uri.getQuery("ManType").charAt(0);
        mPowType = uri.getQuery("PowType").charAt(0);
        mFuelSize = IntegerUtils.parseInt(uri.getQuery("FuelSize"));
        mHoldSize = IntegerUtils.parseInt(uri.getQuery("HoldSize"));
        mComp = IntegerUtils.parseInt(uri.getQuery("Comp"));
        mCabins = IntegerUtils.parseInt(uri.getQuery("Cabins"));
        mBerths = IntegerUtils.parseInt(uri.getQuery("Berths"));
        mCost = DoubleUtils.parseDouble(uri.getQuery("Cost"));
        mStreamlined = BooleanUtils.parseBoolean(uri.getQuery("Streamlined"));
        mMilitary = BooleanUtils.parseBoolean(uri.getQuery("Military"));
        mTurretSize = new int[mSize/100];
        mMissiles = new int[mSize/100];
        mSandcasters = new int[mSize/100];
        mBeamLasers = new int[mSize/100];
        mPulseLasers = new int[mSize/100];
        for (int i = 0; i < mTurretSize.length; i++)
        {
            mTurretSize[i] = IntegerUtils.parseInt(uri.getQuery("TurretSize"+i));
            mMissiles[i] = IntegerUtils.parseInt(uri.getQuery("Missiles"+i));
            mSandcasters[i] = IntegerUtils.parseInt(uri.getQuery("Sandcasters"+i));
            mBeamLasers[i] = IntegerUtils.parseInt(uri.getQuery("BeamLasers"+i));
            mPulseLasers[i] = IntegerUtils.parseInt(uri.getQuery("PulseLasers"+i));
        }
    }

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

    /*
    public T0ShipHardpoint[] getHardpoints()
    {
        return mHardpoints;
    }

    public void setHardpoints(T0ShipHardpoint[] hardpoints)
    {
        this.mHardpoints = hardpoints;
    }
    */

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

    public int[] getTurretSize()
    {
        return mTurretSize;
    }

    public void setTurretSize(int[] turretSize)
    {
        mTurretSize = turretSize;
    }

    public int[] getMissiles()
    {
        return mMissiles;
    }

    public void setMissiles(int[] missiles)
    {
        mMissiles = missiles;
    }

    public int[] getSandcasters()
    {
        return mSandcasters;
    }

    public void setSandcasters(int[] sandcasters)
    {
        mSandcasters = sandcasters;
    }

    public int[] getBeamLasers()
    {
        return mBeamLasers;
    }

    public void setBeamLasers(int[] beamLasers)
    {
        mBeamLasers = beamLasers;
    }

    public int[] getPulseLasers()
    {
        return mPulseLasers;
    }

    public void setPulseLasers(int[] pulseLasers)
    {
        mPulseLasers = pulseLasers;
    }
}