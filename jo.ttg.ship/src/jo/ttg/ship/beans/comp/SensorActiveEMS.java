package jo.ttg.ship.beans.comp;

public class SensorActiveEMS extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[] mTechLevelRange = { 10, 20 };
	private int[] mRangeRange = { R_DISTANT, R_FAR_ORBIT };
	
	/**
	 *
	 */

	public SensorActiveEMS()
	{
		super();
		mTechLevel = 10;
		setRange(R_FAR_ORBIT);
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
		return "Active EMS Array";
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

	private static final int[] tlOff = { 0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6 };

	public double getWeight()
	{
		return compWeight[getRange()][tlOff[mTechLevel - 10]];
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
	
	public String getScanTask()
	{
		switch (getRange())
		{
			case R_DISTANT:
				return "Formidible";
			case R_VERY_DISTANT:
			case R_REGIONAL:
			case R_CONTINENTAL:
			case R_PLANETARY:
				return "Difficult";
			case R_FAR_ORBIT:
			case R_INTERPLANETARY:
				return "Routine";
			case R_SUBSTELLAR:
			case R_INTERSTELLAR:
				return "Simple";
		}
		return "-";
	}

	private static double[][] compWeight = 
	{
		{ 0.002 ,0.001 ,0.001 ,0.001 ,0.001 ,0.001 ,0.001 }, 
		{ 0.010 ,0.008 ,0.006 ,0.005 ,0.004 ,0.003 ,0.002 }, 
		{ 0.030 ,0.016 ,0.010 ,0.008 ,0.006 ,0.005 ,0.004 },
		{ 0.070 ,0.030 ,0.016 ,0.010 ,0.008 ,0.006 ,0.005 }, 
		{ 0.130 ,0.060 ,0.025 ,0.014 ,0.012 ,0.008 ,0.006 }, 
		{ 0.260 ,0.120 ,0.055 ,0.030 ,0.020 ,0.015 ,0.010 }, 
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SENSORS;
	}
}
