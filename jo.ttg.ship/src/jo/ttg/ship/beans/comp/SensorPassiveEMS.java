package jo.ttg.ship.beans.comp;

public class SensorPassiveEMS extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[] mTechLevelRange = { 10, 18 };
	private int[] mRangeRange = { R_DISTANT, R_INTERSTELLAR };
	
	/**
	 *
	 */

	public SensorPassiveEMS()
	{
		super();
		mTechLevel = 10;
		setRange(R_INTERPLANETARY);
	}

	public int[] getRangeRange()
	{
		return mRangeRange;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Passive EMS Array";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	/**
	 *
	 */

	public double getVolume()
	{
		return getWeight()*2;
	}

	/**
	 *
	 */

	private static final int[] tlOff = { 0, 1, 2, 3, 4, 4, 5, 5, 6 };
	private static final int[] rangeOff = { 0, 1, 1, 1, 2, 2, 2, 2, 3, 4 };

	public double getWeight()
	{
		return compWeight[rangeOff[getRange()]][tlOff[mTechLevel - 10]];
	}

	/**
	 *
	 */

	public double getPower()
	{
		return getWeight()*10;
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return getWeight()*20000000;
	}

	public void setTechLevel(int d)
	{
		mTechLevel = d;
	}
	
	public int getEnergyScanValue()
	{
		switch (getRange())
		{
			case R_DISTANT:
				return 1;
			case R_VERY_DISTANT:
				return 2;
			case R_REGIONAL:
				return 3;
			case R_CONTINENTAL:
				return 4;
			case R_PLANETARY:
				return 6;
			case R_FAR_ORBIT:
			case R_SYSTEM:
				return 6;
			case R_INTERPLANETARY:
				return 12;
			case R_SUBSTELLAR:
				return 14;
			case R_INTERSTELLAR:
				return 18;
		}
		return 1;
	}

	private static double[][] compWeight = 
	{
		{ 0.002 ,0.001 ,0.001 ,0.001 ,0.001 ,0.001 ,0.001 }, 
		{ 0.014 ,0.009 ,0.007 ,0.005 ,0.004 ,0.003 ,0.002 }, 
		{ 0.070 ,0.025 ,0.015 ,0.009 ,0.007 ,0.005 ,0.004 },
		{ 0.130 ,0.060 ,0.025 ,0.015 ,0.012 ,0.009 ,0.006 }, 
		{ 0.250 ,0.110 ,0.050 ,0.020 ,0.016 ,0.012 ,0.008 }, 
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SENSORS;
	}
}
