package jo.ttg.ship.beans.comp;

public class Bay50JumpDamper extends WeaponLarge
{
	/**
	 *
	 */

	public Bay50JumpDamper()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "50t Jump Damper Bay x"+getNumber();
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return 21;
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
		return 100000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (120+1)*1000000*getNumber();
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
		return 1;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_JUMPDAMPER;
    }
}
