package jo.ttg.ship.beans.comp;

public class Bay100Missile extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 8, 13};
	
	/**
	 *
	 */

	public Bay100Missile()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Missile Bay x"+getNumber();
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
		return 50*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 5*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (20+1)*1000000*getNumber();
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
		if (mTechLevel < 10)
			return 0x7;
		else if (mTechLevel < 12)
			return 0x8;
		else
			return 0x9;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_MISSILE;
    }
}
