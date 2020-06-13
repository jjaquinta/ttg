package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class FuelPurifier extends ShipComponent implements TechLevelComponent, VolumeComponent
{
	private double mVolume;
	private int mTechLevel;
	private int[] mTechLevelRange = { 8, 17 };
	
    /**
     *
     */

    public FuelPurifier()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Fuel Purification Plant";
    }

    /**
     *
     */

    public int getTechLevel()
    {
        return mTechLevel;
    }

    /**
     *
     */

    public double getVolume()
    {
        return mVolume;
    }

    /**
     *
     */

    public double getWeight()
    {
        return MathUtils.tableLookup(0, mTechLevel, 3, compTable)/MathUtils.tableLookup(0, mTechLevel, 1, compTable)*mVolume;
    }

    /**
     *
     */

    public double getPower()
    {
		return MathUtils.tableLookup(0, mTechLevel, 2, compTable)/MathUtils.tableLookup(0, mTechLevel, 1, compTable)*mVolume;
    }

    /**
     *
     */

    public double getPrice()
    {
		return MathUtils.tableLookup(0, mTechLevel, 4, compTable)/MathUtils.tableLookup(0, mTechLevel, 1, compTable)*mVolume;
    }

	/**
	 *
	 */

	public double getRate()
	{
		return (1.0/6.0)*mVolume/MathUtils.tableLookup(0, mTechLevel, 1, compTable);
	}

	private static final double[][] compTable = 
	{ 
		{ 8,0.7,0.01,1.5,200,135 },
		{ 9,0.6,0.009,1.2,190,120 },
		{ 10,0.55,0.008,1.1,180,105 },
		{ 11,0.45,0.007,0.9,170,95 },
		{ 12,0.4,0.006,0.8,160,80 },
		{ 13,0.35,0.005,0.7,150,65 },
		{ 14,0.25,0.005,0.5,140,55 },
		{ 15,0.2,0.005,0.4,150,40 },
		{ 16,0.15,0.005,0.3,160,25 },
		{ 17,0.05,0.005,0.1,170,15 },
	};
    public int[] getTechLevelRange()
    {
        return mTechLevelRange;
    }

    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

    public void setVolume(double d)
    {
        mVolume = d;
    }

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_OTHER;
	}

}
