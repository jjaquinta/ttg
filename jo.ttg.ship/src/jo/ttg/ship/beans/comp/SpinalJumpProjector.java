package jo.ttg.ship.beans.comp;

public class SpinalJumpProjector extends WeaponLarge
{
	private static final int S_SMALL = 0;
	private static final int S_LARGE = 1;
	
	private int	mSize;
	private static final int[]	mSizeRange = { S_SMALL, S_LARGE };
	private static final String[]	mSizeDescription = { "small", "large" };
	
	/**
	 *
	 */

	public SpinalJumpProjector()
	{
		super();
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Spinal Jump Projector";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return 21;
	}
	
	/**
	 *
	 */
	private static final double[] volRange = {
		55000, 95000
	};

	public double getVolume()
	{
		return volRange[mSize]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		15000, 25000,
	};

	public double getWeight()
	{
		return weightRange[mSize]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		8000000, 9500000,
	};

	public double getPower()
	{
		return powRange[mSize]*getNumber();
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		1000, 1200,
	};

	public double getPrice()
	{
		return priceRange[mSize]*1000000*getNumber();
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
		40, 70,
	};

	public double getHardponits()
	{
		return hardpointRange[mSize]*getNumber();
	}

	private static final int[] factorRange = {
		'A', 'B',
	};

	public int getFactor()
	{
		return factorRange[mSize] -'A' + 10;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_JUMPPROJECTOR;
    }
}
