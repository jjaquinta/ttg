package jo.ttg.ship.beans.comp;

public class ControlComponent extends NumberedComponent implements TechLevelComponent, ECPComponent
{
	private int	mTechLevel;
	private static final int[] mTechLevelRange = { 5, 13 };
	
	public ControlComponent()
	{
		mTechLevel = 10;
	}
	
	private static final double[] mCPTable = { .2, .4, .5, .7, .8, 1, 1, 1, 1.5 
	};
	
	public double getCP()
	{
		return mCPTable[mTechLevel - 5]*getNumber();
	}
    
    public double getCPPerItem()
    {
        return mCPTable[mTechLevel - 5];
    }

	private static final String[] mNameTable = {
		"Basic Mechanical",
		"Enhanced Mechanical",
		"Electronic",
		"Electronic Linked",
		"Computer Linked",
		"Dynamic Linked",
		"Dynamic Linked",
		"Dynamic Linked",
		"Holographic Linked", 
	};

	public String getName()
	{
		return mNameTable[mTechLevel - 5]+"x"+getNumber();
	}

	public int getTechLevel()
	{
		return mTechLevel;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}
	
	public void setTechLevel(int tl)
	{
		mTechLevel = tl;
	}

	private static final double[] mVolumeTable = { .4, .4, .1, .01, .01, .02, .02, .02, .03  
	};
	
	public double getVolume()
	{
		return mVolumeTable[mTechLevel - 5]*getNumber();
	}

	private static final double[] mWeightTable = { .1, .5, .005, .005, .001, .01, .01, .01, .02  
	};
	
	public double getWeight()
	{
		return mWeightTable[mTechLevel - 5]*getNumber()*(isECP() ? 1.5 : 1.0);
	}

	private static final double[] mPowerTable = { 0, .0005, .0005, .0005, .0005, .001, .001, .001, .002  
	};
	
	public double getPower()
	{
		return mPowerTable[mTechLevel - 5]*getNumber();
	}

	private static final double[] mPriceTable = { 5, 30, 70, 200, 350, 500, 500, 500, 1000  
	};
	
	public double getPrice()
	{
		return mPriceTable[mTechLevel - 5]*getNumber()*(isECP() ? 1.5 : 1.0);
	}

	private boolean	mECP;
    public boolean isECP()
    {
        return mECP;
    }

    public void setECP(boolean b)
    {
        mECP = b;
    }

	public int getSection()
	{
		return S_BRIDGE;
	}
}
