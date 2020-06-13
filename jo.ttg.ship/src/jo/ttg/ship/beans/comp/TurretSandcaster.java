package jo.ttg.ship.beans.comp;

public class TurretSandcaster extends Weapon implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 7, 16};
	
	/**
	 *
	 */

	public TurretSandcaster()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Sandcaster Turret x"+getNumber();
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
		return 13.5*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		if (mTechLevel < 8)
			return 4*getNumber();
		if (mTechLevel < 10)
			return 3*getNumber();
		if (mTechLevel < 16)
			return 2*getNumber();
		return 1*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 1*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return 250000*getNumber();
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
        return 1.0/3.0*getNumber();
    }

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_SAND;
    }
}
