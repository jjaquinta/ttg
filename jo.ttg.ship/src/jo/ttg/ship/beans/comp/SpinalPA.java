package jo.ttg.ship.beans.comp;

public class SpinalPA extends WeaponLarge implements TechLevelComponent
{
	private static final int S_SMALL = 0;
	private static final int S_MEDIUM = 1;
	private static final int S_LARGE = 2;
	
	private int	mTechLevel;
	private int	mSize;
	private static final int[]	mSizeRange = { S_SMALL, S_LARGE };
	private static final String[]	mSizeDescription = { "small", "medium", "large" };
	
	/**
	 *
	 */

	public SpinalPA()
	{
		super();
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Spinal Particle Accelerator";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}

	private int getOff()
	{
		if (mSize == S_SMALL)
			return mTechLevel - 8;
		if (mSize == S_MEDIUM)
			return mTechLevel - 10 + 8;
		return mTechLevel - 12 + 14;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		75000, 65000, 60000, 55000, 45000, 40000, 35000, 35000,
		60000, 60000, 55000, 45000, 40000, 35000,
		60000, 55000, 45000, 40000, 35000, 25000, 20000, 15000, 15000,
	};

	public double getVolume()
	{
		return volRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		18000, 16000, 15000, 14000, 11000, 10000, 9000, 8500,
		15500, 14500, 13500, 10500, 9500, 8000,
		14000, 13000, 10000, 9000, 8000, 6000, 5000, 3500, 3000,
	};

	public double getWeight()
	{
		return weightRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		125000, 125000, 125000, 150000, 150000, 150000, 175000, 175000,
		200000, 200000, 200000, 225000, 225000, 225000,
		250000, 250000, 250000, 250000, 275000, 275000, 275000, 300000, 325000,
	};

	public double getPower()
	{
		return powRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		3500, 3000, 2400, 1500, 1200, 1200, 800, 500,
		3000, 2000, 1600, 1200, 1000, 800,
		2000, 1500, 1200, 1000, 1000, 800, 600, 800, 600,
	};

	public double getPrice()
	{
		return priceRange[getOff()]*1000000*getNumber();
	}

	private static final int[][] techLevelRange =
	{
		{ 8, 15 },
		{ 10, 15 },
		{ 12, 20 },
	};

	public int[] getTechLevelRange()
	{
		return techLevelRange[mSize];
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	public int getSize()
	{
		return mSize;
	}

	public String getSizeDescription()
	{
		return mSizeDescription[mSize];
	}

	public int[] getSizeRange()
	{
		return mSizeRange;
	}

	public void setSize(int i)
	{
		mSize = i;
	}


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_WEAPONS;
	}

	private static final int[] hardpointRange = {
		55, 50, 45, 40, 35, 30, 25, 25,
		50, 45, 40, 35, 30, 25,
		45, 40, 35, 30, 25, 20, 15, 10, 10,
	};

	public double getHardponits()
	{
		return hardpointRange[getOff()]*getNumber();
	}
	
	public int getFactor()
	{
		return getOff() + 10;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_PA;
    }
}
