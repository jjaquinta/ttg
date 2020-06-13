package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class Computer extends NumberedComponent implements TechLevelComponent, ECPComponent
{
	private int	mTechLevel;
	private int[]	mTechLevelRange = { 5, 21 };
	private String[]	mTechLevelDescription = 
	{
		"Model 1",
		"Model 1/bis",
		"Model 2",
		"Model 2/bis",
		"Model 3",
		"Model 4",
		"Model 5",
		"Model 6",
		"Model 7",
		"Model 8",
		"Model 9",
		"Model 10",
		"Model 11",
		"Model 12",
		"Model 13",
		"Model 14",
		"Model 15",
	};
	private boolean	mECP;
	
    public String getName()
    {
        return "Computer x"+getNumber();
    }

    public int getTechLevel()
    {
        return mTechLevel;
    }

    public double getVolume()
    {
        return MathUtils.tableLookup(0, mTechLevel, 1, compTable)*getNumber();
    }

    public double getWeight()
    {
		double ret = MathUtils.tableLookup(0, mTechLevel, 3, compTable)*getNumber();
		if (isECP())
			ret *= 1.5;
		return ret;
    }

    public double getPower()
    {
		return MathUtils.tableLookup(0, mTechLevel, 2, compTable)*getNumber();
    }

	public double getMaximumCP()
	{
		return MathUtils.tableLookup(0, mTechLevel, 4, compTable);
	}

	public double getCPMultipler()
	{
		return MathUtils.tableLookup(0, mTechLevel, 5, compTable);
	}

    public double getPrice()
    {
		double ret = MathUtils.tableLookup(0, mTechLevel, 6, compTable)*1000000*getNumber();
		if (isECP())
			ret *= 1.5;
		return ret;
    }

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

    public String getTechLevelDescription()
    {
        return mTechLevelDescription[mTechLevel-5];
    }

    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

	private static final double[][] compTable =
	{
		{ 5,2*100,0.002*100,0.5*100,5000,10,0.4*100 },
		{ 6,2*10,0.003*10,0.5*10,7500,15,0.9*10 },
		{ 7,3.5*2,0.003*2,0.9*2,10000,15,1.9*2 },
		{ 8,3.5,0.004,0.9,15000,20,3.8 },
		{ 9,4,0.004,1,20000,25,3.8 },
		{ 10,5.5,0.005,1.4,50000,30,6.4 },
		{ 11,6.5,0.006,1.6,100000,35,9.7 },
		{ 12,9,0.007,2.4,500000,45,11.8 },
		{ 13,12,0.008,3,5000000,65,17.4 },
		{ 14,14,0.009,3.5,50000000,95,23.9 },
		{ 15,17,0.01,4.4,100000000,120,30.5 },
		{ 16,20,0.011,5,500000000,200,43 },
		{ 17,13,0.012,4.4,20000000000.0,1000,65.5 },
		{ 18,15,0.013,4.9,1E+11,10000,87 },
		{ 19,18,0.014,4.5,5E+11,25000,108.5 },
		{ 20,21,0.015,5.3,2E+12,50000,130 },
		{ 21,24,0.016,6,5E+13,100000,150.5 },
	};
    public boolean isECP()
    {
        return mECP;
    }

    public void setECP(boolean b)
    {
        mECP = b;
    }

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_BRIDGE;
	}

	public int getModel()
	{
		if (getTechLevel() <= 6)
			return 1;
		if (getTechLevel() <= 8)
			return 2;
		return getTechLevel() - 6;
	}
}
