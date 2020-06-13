package jo.ttg.ship.beans.comp;

public class Bay100MesonGun extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 13, 19};
	
	/**
	 *
	 */

	public Bay100MesonGun()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Meson Gun Bay x"+getNumber();
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
		return 80*getNumber();
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
		return (70+1)*1000000*getNumber();
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
			case 13:
				return 3;
			case 14:
				return 5;
			case 15:
				return 9;
			case 16:
				return 11;
			case 17:
				return 13;
			case 18:
				return 15;
			case 19:
				return 19;
		}
		return 0;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_MESON;
    }
}
