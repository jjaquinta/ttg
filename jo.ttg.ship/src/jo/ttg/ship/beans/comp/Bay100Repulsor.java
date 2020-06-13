package jo.ttg.ship.beans.comp;

public class Bay100Repulsor extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 10, 21};
	
	/**
	 *
	 */

	public Bay100Repulsor()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Repulsor Bay x"+getNumber();
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
		return 60*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 2500*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (10+1)*1000000*getNumber();
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
			case 10:
				return 2;
			case 11:
				return 4;
			case 12:
				return 6;
			case 13:
				return 7;
			case 14:
				return 8;
			case 15:
				return 9;
			case 16:
				return 10;
			case 17:
				return 11;
			case 18:
				return 12;
			case 19:
				return 13;
			case 20:
				return 14;
			case 21:
				return 15;
		}
		return 0;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_REPULSOR;
    }
}
