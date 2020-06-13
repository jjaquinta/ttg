package jo.ttg.ship.beans.comp;

public class Bay100Tractors extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 16, 20};
	
	/**
	 *
	 */

	public Bay100Tractors()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Tractor Bay x"+getNumber();
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
		return 1350*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		return 70*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 50000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (40+1)*1000000*getNumber();
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

	public double getHardponits()
	{
		return 10*getNumber();
	}

	public int getFactor()
	{
		switch (mTechLevel)
		{
			case 16:
				return 2;
			case 17:
				return 4;
			case 18:
				return 6;
			case 19:
				return 7;
			case 20:
				return 8;
		}
		return 0;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_TRACTOR;
    }
}
