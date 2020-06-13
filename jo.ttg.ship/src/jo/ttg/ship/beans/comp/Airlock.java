package jo.ttg.ship.beans.comp;

public class Airlock extends NumberedComponent
{

    /**
     *
     */

    public Airlock()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Airlock x"+getNumber();
    }

    /**
     *
     */

    public int getTechLevel()
    {
        return 0;
    }

    /**
     *
     */

    public double getVolume()
    {
        return 3*getNumber();
    }

    /**
     *
     */

    public double getWeight()
    {
        return .2*getNumber();
    }

    /**
     *
     */

    public double getPower()
    {
        return .002*getNumber();
    }

    /**
     *
     */

    public double getPrice()
    {
        return 5000*getNumber();
    }

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_BRIDGE;
	}

}
