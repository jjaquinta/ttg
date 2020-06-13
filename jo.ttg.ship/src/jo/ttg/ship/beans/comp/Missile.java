package jo.ttg.ship.beans.comp;

public class Missile extends NumberedComponent
{

    /**
     *
     */

    public Missile()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Missile Magazine ("+getNumber()+")";
    }

    /**
     *
     */

    public int getTechLevel()
    {
        return 10;
    }

    /**
     *
     */

    public double getVolume()
    {
        return .1*getNumber();
    }

    /**
     *
     */

    public double getWeight()
    {
        return .05*getNumber();
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
        return 20000*getNumber();
    }


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_OTHER;
	}
}
