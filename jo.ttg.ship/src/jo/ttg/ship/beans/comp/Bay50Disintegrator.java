package jo.ttg.ship.beans.comp;

public class Bay50Disintegrator extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 18, 21};
	
	/**
	 *
	 */

	public Bay50Disintegrator()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Disintegrator Bay x"+getNumber();
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
		return 32500*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (90+1)*1000000*getNumber();
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
			case 18:
				return 6;
			case 19:
				return 6;
			case 20:
				return 7;
			case 21:
				return 7;
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
