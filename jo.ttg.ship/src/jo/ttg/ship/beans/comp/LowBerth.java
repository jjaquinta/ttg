package jo.ttg.ship.beans.comp;

public class LowBerth extends AccomodationComponent
{

    /**
     *
     */

    public String getName()
    {
        return "Low Berth x"+getNumber();
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
        return 13.5*getNumber();
    }

    /**
     *
     */

    public double getWeight()
    {
        return 1*getNumber();
    }

    /**
     *
     */

    public double getPower()
    {
        return .001*getNumber();
    }

    /**
     *
     */

    public double getPrice()
    {
        return 50000*getNumber();
    }

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_ACCOM;
	}

}
