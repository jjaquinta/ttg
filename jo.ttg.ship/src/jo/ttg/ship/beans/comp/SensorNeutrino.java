package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class SensorNeutrino extends SensorComponent implements TechLevelComponent
{
	private int mTechLevel;
	private int[] mTechLevelRange = { 11, 16 };
	
	/**
	 *
	 */

	public SensorNeutrino()
	{
		super();
		mTechLevel = 11;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Neutrino Sensor";
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
		if (mTechLevel <= 10)
			return "-";
		else if (mTechLevel == 11)
			return "1 Gw";
		else if (mTechLevel == 12)
			return "1 Mw";
		else if (mTechLevel == 13)
			return "100kw";
		else if (mTechLevel <= 16)
			return "10kw";
		return "1kw";
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

	public int getEnergyScanValue()
	{
		return (int)MathUtils.tableLookup(0, mTechLevel, 5, compTable);
	}
	
	public String getScanTask()
	{
		if (mTechLevel <= 10)
			return "Impossible";
		else if (mTechLevel == 11)
			return "Formidible";
		else if (mTechLevel == 12)
			return "Formidible";
		else if (mTechLevel == 13)
			return "Difficult";
		return "Routine";
	}

	private static final double[][] compTable =
	{
		{ 10,0.008,0.005,0.004,1000,0 },
		{ 11,1,0.5,0.45,60000,1 },
		{ 12,0.8,0.4,0.3,75000,2 },
		{ 13,0.4,0.3,0.17,90000,4 },
		{ 14,0.2,0.2,0.095,110000,6 },
		{ 16,0.1,0.1,0.07,120000,8 },
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SENSORS;
	}
}
