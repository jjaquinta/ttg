package jo.ttg.ship.beans.comp;

public class Bay50Repulsor extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 14, 21};
	
	/**
	 *
	 */

	public Bay50Repulsor()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Repulsor Bay x"+getNumber();
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
		return 30*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 1250*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (6+1)*1000000*getNumber();
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
		if (mTechLevel < 18)
			return (mTechLevel - 13)*2 + 1;
		return mTechLevel - 8;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_REPULSOR;
    }
}
