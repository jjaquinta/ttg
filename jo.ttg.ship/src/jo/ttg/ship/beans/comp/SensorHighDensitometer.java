package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class SensorHighDensitometer extends SensorComponent implements TechLevelComponent
{
	private int mTechLevel;
	private static final int[] mTechLevelRange = { 11, 16 };
	private static final String[] mTechLevelDescription = 
	{
		"1m",
		"50m",
		"100m",
		"250m",
		"1km",
		"25km",
	};
	private static final String[] mTechLevelScanTask = 
	{
		"Formidible",
		"Difficult",
		"Difficult",
		"Routine",
		"Routine",
		"Routine",
	};
	/**
	 *
	 */

	public SensorHighDensitometer()
	{
		super();
		mTechLevel = 11;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "High Penetration Densitometer";
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
		return mTechLevelDescription[mTechLevel-11];
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
		{ 11,30,2.5,10,750000 },
		{ 12,15,1,5,900000 },
		{ 13,12,0.9,3,950000 },
		{ 14,9,0.5,2,1000000 },
		{ 15,7,0.4,1.5,1500000 },
		{ 16,4,0.3,1,1500000 },
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SENSORS;
	}
}
