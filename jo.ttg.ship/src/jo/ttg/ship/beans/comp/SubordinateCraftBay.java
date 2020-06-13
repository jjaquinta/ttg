package jo.ttg.ship.beans.comp;

import jo.ttg.utils.DisplayUtils;

public class SubordinateCraftBay extends AccomodationComponent
{
	private double	mCraftVolume;
	private String	mSubordinateCraftName;

    /**
     *
     */

    public SubordinateCraftBay()
    {
        super();
        mCraftVolume = 4*13.5;
        mSubordinateCraftName = DisplayUtils.formatVolume(mCraftVolume)+" craft";
    }

    /**
     *
     */

    public String getName()
    {
        return "Subordinate Craft Bay";
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
//    	if (stats.getConfig().startsWith("7"))
//    		return 0;
    	double base = getModifiedVolume(mCraftVolume);
        return base*getNumber();
    }

    public static double getModifiedVolume(double craftVolume)
    {
        if (craftVolume <= 270)
            return craftVolume * 1.5;
        else if (craftVolume <= 1350)
            return craftVolume * 1.3;
        else
            return craftVolume * 1.1;
    }
    
    /**
     *
     */

    public double getWeight()
    {
        return 0;
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
        return 150*mCraftVolume*getNumber();
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
    public String getSubordinateCraftName()
    {
        return mSubordinateCraftName;
    }

    public void setSubordinateCraftName(String string)
    {
        mSubordinateCraftName = string;
    }

}
