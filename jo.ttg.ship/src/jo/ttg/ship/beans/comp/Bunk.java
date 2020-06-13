package jo.ttg.ship.beans.comp;

public class Bunk extends AccomodationComponent
{

    /**
     *
     */

    public String getName()
    {
        return "Bunk x"+getNumber();
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
        return .5*getNumber();
    }

    /**
     *
     */

    public double getPower()
    {
        return 0;
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
		return S_ACCOM;
	}

}
