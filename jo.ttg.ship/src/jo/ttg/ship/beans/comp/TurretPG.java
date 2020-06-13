package jo.ttg.ship.beans.comp;

public class TurretPG extends Weapon implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 10, 16};
	
	/**
	 *
	 */

	public TurretPG()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Plasma Gun Turret x"+getNumber();
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
		return 27*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		if (mTechLevel == 10)
			return 5*getNumber();
		if (mTechLevel == 11)
			return 4*getNumber();
		if (mTechLevel < 16)
			return 3*getNumber();
		return 2*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 250*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return 1500000*getNumber();
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
        return .5*getNumber();
    }

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_BEAM;
    }
}
