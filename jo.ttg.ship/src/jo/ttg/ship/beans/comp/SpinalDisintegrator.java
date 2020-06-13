package jo.ttg.ship.beans.comp;

public class SpinalDisintegrator extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 17, 21 };
	
	/**
	 *
	 */

	public SpinalDisintegrator()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Spinal Disintegrator";
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
	private static final double[] volRange = {
		60000, 55000, 45000, 40000, 35000,
	};

	public double getVolume()
	{
		return volRange[mTechLevel - 17]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		13000, 12000, 10000, 8500, 7500,
	};

	public double getWeight()
	{
		return weightRange[mTechLevel - 17]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		500000, 550000, 550000, 575000, 575000,
	};

	public double getPower()
	{
		return powRange[mTechLevel - 17]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		5000, 3500, 2400, 1500, 1200,
	};

	public double getPrice()
	{
		return priceRange[mTechLevel - 17]*1000000*getNumber();
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_WEAPONS;
	}

	private static final int[] hardpointRange = {
		45, 40, 35, 30, 25,
	};

	public double getHardponits()
	{
		return hardpointRange[mTechLevel - 17]*getNumber();
	}

	public int getFactor()
	{
		return mTechLevel - 17 + 10;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_DISINTEGRATOR;
    }
}
