package jo.ttg.ship.beans.comp;

public class LaunchTubes extends AccomodationComponent
{
	private double mCraftVolume;
	
    public String getName()
    {
        return "Launch Tubes";
    }

    public int getTechLevel()
    {
        return 0;
    }

    public double getVolume()
    {
        return mCraftVolume*getNumber()*25;
    }

    public double getWeight()
    {
        return 0;
    }

    public double getPower()
    {
        return 0;
    }

    public double getPrice()
    {
        return 150*getVolume();
    }

    public double getCraftVolume()
    {
        return mCraftVolume;
    }

    public void setCraftVolume(double d)
    {
        mCraftVolume = d;
    }


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_ACCOM;
	}
}
