package jo.ttg.ship.beans.comp;

public class TurretPA extends Weapon implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange  = { 14, 18};
	
	/**
	 *
	 */

	public TurretPA()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Particle Accelerator Turret x"+getNumber();
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
		if (mTechLevel == 14)
			return 25*getNumber();
		if (mTechLevel == 15)
			return 12*getNumber();
		if (mTechLevel < 17)
			return 9*getNumber();
		return 6*getNumber();
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
		if (mTechLevel == 14)
			return 4000000*getNumber();
		return 3000000*getNumber();
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
        return 1.0*getNumber();
    }

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_PA;
    }
}
