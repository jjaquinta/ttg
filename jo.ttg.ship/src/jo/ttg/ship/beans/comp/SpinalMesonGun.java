package jo.ttg.ship.beans.comp;

public class SpinalMesonGun extends WeaponLarge implements TechLevelComponent
{
	private static final int S_SMALL = 0;
	//private static final int S_MEDIUM = 1;
	//private static final int S_LARGE = 2;
	//private static final int S_HUGE = 3;
	private static final int S_ENORMOUS = 4;
	
	private int	mTechLevel;
	private int	mSize;
	private static final int[]	mSizeRange = { S_SMALL, S_ENORMOUS };
	private static final String[]	mSizeDescription = { "class I", "class II", "class III", "class IV", "class V" };
	
	/**
	 *
	 */

	public SpinalMesonGun()
	{
		super();
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Spinal Meson Gun";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}
	
	private static final int[] tlOff = {
		0, 2, 5, 9, 14, 18, 19, 21, 23,
	};

	private int getOff()
	{
		return tlOff[mTechLevel-11] + mSize;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		65000, 110000, 25000, 65000, 11000, 15000, 25000, 65000,
		110000, 15000, 25000, 55000, 95000, 110000, 15000,
		25000, 65000, 95000, 110000, 95000, 110000, 65000,
		95000, 65000,
	};

	public double getVolume()
	{
		return volRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		13000, 22000, 5000, 12500, 20000, 3000, 45000, 12500,
		19000, 3000, 4500, 11000, 19000, 19000, 4000, 4000,
		12000, 18000, 18500, 14500, 18000, 11500, 17000, 11000,
	};

	public double getWeight()
	{
		return weightRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		125000, 150000, 150000, 175000, 250000, 175000, 200000,
		250000, 275000, 200000, 225000, 250000, 275000, 300000,
		225000, 250000, 275000, 300000, 325000, 325000, 350000,
		325000, 350000, 375000,
	};

	public double getPower()
	{
		return powRange[getOff()]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		10000, 12000, 3000, 5000, 10000, 800, 1000, 3000,
		5000, 400, 600, 800, 1000, 2000, 400, 600, 800,
		1000, 2000, 1200, 2000, 1000, 1200, 800,
	};

	public double getPrice()
	{
		return priceRange[getOff()]*1000000*getNumber();
	}

	private static final int[] mTechLevelRange = { 11, 19 };

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
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
		50, 80, 20, 50, 80, 10, 20, 50, 80, 10, 20,
		40, 70, 80, 10, 20, 50, 70, 80, 70, 80, 50,
		70, 50,
	};

	public double getHardponits()
	{
		return hardpointRange[getOff()]*getNumber();
	}

	private static final int[] factorRange = {
		'A', 'B', 'C', 'D', 'K', 'E', 'F', 'L', 'P',
		'G', 'H', 'M', 'Q', 'S', 'J', 'N', 'R', 'T',
		'U', 'V', 'X', 'W', 'Y', 'Z',
	};

	public int getFactor()
	{
		return factorRange[getOff()] -'A' + 10;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_MESON;
    }
}
