package jo.ttg.ship.beans.comp;

public class Bay50MesonGun extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 15, 21};
	
	/**
	 *
	 */

	public Bay50MesonGun()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Meson Gun Bay x"+getNumber();
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
		return 675*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		return 40*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 25000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (50+1)*1000000*getNumber();
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
			case 15:
				return 4;
			case 16:
				return 6;
			case 17:
				return 9;
			case 18:
				return 11;
			case 19:
				return 13;
			case 20:
				return 15;
			case 21:
				return 17;
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
