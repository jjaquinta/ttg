package jo.ttg.ship.beans.comp;

public class Bay50PlasmaGun extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 10, 21};
	
	/**
	 *
	 */

	public Bay50PlasmaGun()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Plasma Gun Bay x"+getNumber();
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
		return 2500*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (5+1)*1000000*getNumber();
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
		if (mTechLevel == 10)
			return 2;
		if (mTechLevel == 11)
			return 4;
		return mTechLevel - 6;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_BEAM;
    }
}
