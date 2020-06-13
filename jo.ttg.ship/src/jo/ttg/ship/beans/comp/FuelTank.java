package jo.ttg.ship.beans.comp;

public class FuelTank extends ShipComponent implements VolumeComponent
{
	private double	mVolume;
	
    /**
     *
     */

    public FuelTank()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Fuel Tank";
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
        return mVolume;
    }

    /**
     *
     */

    public double getWeight()
    {
        return 0.07*mVolume;
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
        return 0;
    }

    public void setVolume(double d)
    {
        mVolume = d;
    }


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_OTHER;
	}
}
