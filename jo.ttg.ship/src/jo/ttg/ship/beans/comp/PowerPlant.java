package jo.ttg.ship.beans.comp;

import jo.util.utils.MathUtils;

public class PowerPlant extends ShipComponent implements TechLevelComponent, VolumeComponent
{
	private double	mVolume;
	private int	mTechLevel;
	private int[]	mTechLevelRange = {9, 16};

    /**
     *
     */

    public PowerPlant()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Fusion Power Plant";
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
        return mVolume;
    }

	public double[] getVolumeRange()
	{
		double[] volumeRange = new double[2];
		volumeRange[0] = MathUtils.tableLookup(0, mTechLevel, 3, fusionTable);
		volumeRange[1] = 1000.0;
		return volumeRange;
	}

    /**
     *
     */

    public double getWeight()
    {
    	double mod = MathUtils.tableLookup(0, mTechLevel, 2, fusionTable);
        return mVolume*mod;
    }

    /**
     *
     */

    public double getPower()
    {
		double mod = MathUtils.tableLookup(0, mTechLevel, 1, fusionTable);
		if (mVolume >= 14)
			mod *= 3;
		else if (mVolume >= 10)
			mod *= 2;
		else if (mVolume >= 6)
			mod *= 1.5;
		else if (mVolume <= .25)
			mod *= 0.25;
		else if (mVolume <= .5)
			mod *= 0.5;
		else if (mVolume <= .75)
			mod *= 0.67;
		return mVolume*mod;
    }

    /**
     *
     */

    public double getPrice()
    {
		return mVolume*200000;
    }

    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

    public void setVolume(double d)
    {
        mVolume = d;
    }

	/**
	 *
	 */

	public double getFuelConsumtion()
	{
		double mod = MathUtils.tableLookup(0, mTechLevel, 4, fusionTable);
		return mVolume*mod;
	}

	private static final double[][] fusionTable =
	{
		{  9, 2, 4, 10.000, 0.003 },
		{ 10, 2, 4,  2.000, 0.003 },
		{ 11, 2, 4,  1.000, 0.003 },
		{ 12, 2, 4,  0.250, 0.003 },
		{ 13, 3, 3,  0.150, 0.005 },
		{ 14, 3, 3,  0.100, 0.005 },
		{ 15, 6, 2,  0.090, 0.005 },
		{ 16, 7, 1,  0.080, 0.010 },
	};

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_POWER;
	}
}
