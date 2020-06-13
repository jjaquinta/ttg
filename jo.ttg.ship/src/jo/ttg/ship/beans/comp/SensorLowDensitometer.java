package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class SensorLowDensitometer extends SensorComponent implements TechLevelComponent
{
	private int mTechLevel;
	private static final int[] mTechLevelRange = { 10, 16 };
	private static final String[] mTechLevelDescription = 
	{
		"Surface",
		"Surface",
		"1m",
		"50m",
		"100m",
		"250m",
		"1km",
	};
	private static final String[] mTechLevelScanTask = 
	{
		"Impossible",
		"Impossible",
		"Formidible",
		"Difficult",
		"Difficult",
		"Routine",
		"Routine",
	};
   /**
     *
     */

    public SensorLowDensitometer()
    {
        super();
        mTechLevel = 10;
    }

    /**
     *
     */

    public String getName()
    {
        return "Low Penetration Densitometer";
    }

    /**
     *
     */

    public int getTechLevel()
    {
        return mTechLevel;
    }

	public void setTechLevel(int v)
	{
		mTechLevel = v;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	public String getTechLevelDescription()
	{
		return mTechLevelDescription[mTechLevel-10];
	}

    /**
     *
     */

    public double getVolume()
    {
        return MathUtils.tableLookup(0, mTechLevel, 2, compTable);
    }

    /**
     *
     */

    public double getWeight()
    {
		return MathUtils.tableLookup(0, mTechLevel, 3, compTable);
    }

    /**
     *
     */

    public double getPower()
    {
		return MathUtils.tableLookup(0, mTechLevel, 1, compTable);
    }

    /**
     *
     */

    public double getPrice()
    {
		return MathUtils.tableLookup(0, mTechLevel, 4, compTable);
    }
	
	public String getScanTask()
	{
		return mTechLevelScanTask[mTechLevel-10];
	}

	private static final double[][] compTable =
	{
		{ 10,0.020 ,0.002 ,0.001 ,10000 },
		{ 11,0.010 ,0.001 ,0.001 ,10000 },
		{ 12,2.000 ,0.200 ,0.800 ,116000 },
		{ 13,1.000 ,0.100 ,0.400 ,145000 },
		{ 14,0.900 ,0.090 ,0.300 ,205000 },
		{ 15,0.300 ,0.080 ,0.100 ,205000 },
		{ 16,0.200 ,0.070 ,0.100 ,205000 },
	};


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SENSORS;
	}
}
