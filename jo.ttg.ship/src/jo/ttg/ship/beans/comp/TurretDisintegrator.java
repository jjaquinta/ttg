package jo.ttg.ship.beans.comp;

public class TurretDisintegrator extends Weapon
{
	/**
	 *
	 */

	public TurretDisintegrator()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Disintegrator Turret x"+getNumber();
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return 18;
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
		return 6*getNumber();
	}

	/**
	 *
	 */

	public double getPower()
	{
		return 4000*getNumber();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		return 750000*getNumber();
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
		return 1.0/2.0*getNumber();
	}

    /* (non-Javadoc)
     * @see ttg.beans.ship.comp.Weapon#getType()
     */
    public int getType()
    {
        return TYPE_DISINTEGRATOR;
    }
}
