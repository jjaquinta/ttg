package jo.ttg.ship.beans.comp;

public class Bay50PA extends WeaponLarge implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 10, 21};
	
	/**
	 *
	 */

	public Bay50PA()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Particle Accelerator Bay x"+getNumber();
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
		return 7500*getNumber();
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
		return (mTechLevel - 4)/2;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_PA;
    }
}
