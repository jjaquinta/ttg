package jo.ttg.ship.beans.comp;

public class Stateroom extends AccomodationComponent
{

    /**
     *
     */

    public String getName()
    {
        return "Middle Passage Stateroom";
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
        return 27.0*getNumber();
    }

    /**
     *
     */

    public double getWeight()
    {
        return 2*getNumber();
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
        return 200000*getNumber();
    }


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_ACCOM;
	}
}
