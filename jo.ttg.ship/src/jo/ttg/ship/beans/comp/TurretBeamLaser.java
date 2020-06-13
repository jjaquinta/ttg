package jo.ttg.ship.beans.comp;

public class TurretBeamLaser extends Weapon implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 7, 16};
	
	/**
	 *
	 */

	public TurretBeamLaser()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Beam Laser Turret x"+getNumber();
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
		return 13.5/3*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		if (mTechLevel < 13)
			return 5*getNumber();
		if (mTechLevel < 16)
			return 4*getNumber();
		return 3*getNumber();
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
		return 1000000*getNumber();
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
        return TYPE_BEAM;
    }
}
