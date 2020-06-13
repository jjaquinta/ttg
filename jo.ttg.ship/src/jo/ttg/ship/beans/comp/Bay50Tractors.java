package jo.ttg.ship.beans.comp;

public class Bay50Tractors extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 20, 21};
	
	/**
	 *
	 */

	public Bay50Tractors()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Tractor Bay x"+getNumber();
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
		return 35*getNumber();
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
		return (24+1)*1000000*getNumber();
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
			case 20:
				return 3;
			case 21:
				return 5;
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
