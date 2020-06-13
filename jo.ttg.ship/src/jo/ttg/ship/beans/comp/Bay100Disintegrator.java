package jo.ttg.ship.beans.comp;

public class Bay100Disintegrator extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 17, 21};
	
	/**
	 *
	 */

	public Bay100Disintegrator()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Disintegrator Bay x"+getNumber();
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
		return 75*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 65000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (150+1)*1000000*getNumber();
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
			case 17:
				return 7;
			case 18:
				return 7;
			case 19:
				return 8;
			case 20:
				return 8;
			case 21:
				return 9;
		}
		return 0;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_DISINTEGRATOR;
    }
}
