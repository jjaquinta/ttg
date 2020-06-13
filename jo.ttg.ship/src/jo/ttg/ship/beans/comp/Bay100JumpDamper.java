package jo.ttg.ship.beans.comp;

public class Bay100JumpDamper extends WeaponLarge
{
	/**
	 *
	 */

	public Bay100JumpDamper()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "100t Jump Damper Bay x"+getNumber();
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
		return 1350*getNumber();
	}

	/**
	 *
	 */

	public double getWeight()
	{
		return 80*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 200000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return (200+1)*1000000*getNumber();
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
		return 2;
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_JUMPDAMPER;
    }
}
