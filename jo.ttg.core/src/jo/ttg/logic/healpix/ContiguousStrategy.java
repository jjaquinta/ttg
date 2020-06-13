package jo.ttg.logic.healpix;

import java.util.function.Function;

import jo.ttg.beans.surf.MapHexBean;
import jo.util.heal.HEALLogic;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class ContiguousStrategy
{
    public static final int STRAT_NORM     = 0;
    public static final int STRAT_UNDERLAY = 1;
    public static final int STRAT_BOUNDRY  = 2;
    public static final int STRAT_UNDERLAYFIRST = 3;

    // Strategy
    private int mStrategy;
    public int getStrategy()
    {
        return mStrategy;
    }
    public void setStrategy(int v)
    {
        mStrategy = v;
    }

    // Number
    private int mNumber;
    public int getNumber()
    {
        return mNumber;
    }
    public void setNumber(int v)
    {
        mNumber = v;
    }

	// Loosness
	private double mLoosness;
	public double getLoosness()
	{
		return mLoosness;
	}
	public void setLoosness(double v)
	{
		mLoosness = v;
	}

    // Matcher
    private Function<MapHexBean,Boolean> mMatcher;
    public Function<MapHexBean,Boolean> getMatcher()
    {
        return mMatcher;
    }
    public void setMatcher(Function<MapHexBean,Boolean> v)
    {
        mMatcher = v;
    }

    // HexValue
    private int mHexValue;
    public int getHexValue()
    {
        return mHexValue;
    }
    public void setHexValue(int v)
    {
        mHexValue = v;
    }

    // AltType
    private int mAltType;
    public int getAltType()
    {
        return mAltType;
    }
    public void setAltType(int v)
    {
        mAltType = v;
    }

    // AltValue
    private int mAltValue;
    public int getAltValue()
    {
        return mAltValue;
    }
    public void setAltValue(int v)
    {
        mAltValue = v;
    }

    // constructor
    public ContiguousStrategy()
    {
        mStrategy = STRAT_NORM;
        mNumber = 0;
        mLoosness = 1.0/6.0;
        mHexValue = 0;
        mMatcher = null;
        mAltValue = 0;
        mAltType = 0;
    }

    // utils

    public static boolean candidateHex(MapHexBean hex, int type, int value)
    {
        return hex.getByType(type) == value;
    }

    public boolean candidateHex(IHEALGlobe<MapHexBean> globe, MapHexBean hex)
    {
        if (!mMatcher.apply(hex))
            return false;
        if (mStrategy == STRAT_NORM)
            return true;
        if (mStrategy == STRAT_UNDERLAY)
            return candidateHex(hex, mAltType, mAltValue);
        if (mStrategy == STRAT_UNDERLAYFIRST)
            if (mAltValue == -1)
                return true;
            else
                return candidateHex(hex, mAltType, mAltValue);
        // else boundary
        for (IHEALCoord nextC : HEALLogic.getNeighbors(hex.getLocation()))
        {
            MapHexBean neighbor = globe.get(nextC);
            if (!candidateHex(neighbor, mAltType, mAltValue))
                return false;
        }
        return true;
    }
}
