package jo.ttg.ship.beans.comp;

public class TurretFG extends Weapon implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 12, 17};
	
	/**
	 *
	 */

	public TurretFG()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Fusion Gun Turret x"+getNumber();
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
		if (mTechLevel < 14)
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
		return 500*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return 2000000*getNumber();
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
