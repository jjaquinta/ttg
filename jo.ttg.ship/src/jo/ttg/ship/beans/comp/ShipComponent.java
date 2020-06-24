package jo.ttg.ship.beans.comp;

import jo.ttg.utils.ConvUtils;
import jo.ttg.utils.DisplayUtils;
import jo.util.beans.Bean;

public abstract class ShipComponent extends Bean implements Cloneable
{
	public static final int S_HULL = 0;
	public static final int S_POWER = 1;
	public static final int S_LOCO = 2;
	public static final int S_COMMO = 3;
	public static final int S_SENSORS = 4;
	public static final int S_WEAPONS = 5;
	public static final int S_SCREENS = 6;
	public static final int S_BRIDGE = 7;
	public static final int S_ACCOM = 8;
	public static final int S_OTHER = 9;
	
	public static final String[] mSectionDescriptions = 
	{
		"HULL",
		"POWER",
		"LOCO",
		"COMMO",
		"SENSORS",
		"WEAPONS",
		"SCREENS",
		"BRIDGE",
		"ACCOM",
		"OTHER",
	};
    
    public static final String[] mSectionShortDescriptions = 
    {
        "HULL",
        "POW",
        "LOCO",
        "COM",
        "SNSR",
        "WEPN",
        "SCRN",
        "BRDG",
        "ACOM",
        "OTH",
    };
	
    /**
     *
     */
    public ShipComponent()
    {
        super();
    }

	public abstract String getName();
	public abstract int getTechLevel();
	public abstract double getVolume();
	public abstract double getWeight();
	public abstract double getPower();
	public abstract double getPrice();
	public abstract int getSection();

	public String getVolumeDescription()
	{
		return DisplayUtils.formatVolume((int)(getVolume()/13.5));
	}

	public String getPriceDescription()
	{
		return DisplayUtils.formatCurrency(getPrice());
	}

	public String getPowerDescription()
	{
		return DisplayUtils.formatPower(getPower());
	}

	public String getWeightDescription()
	{
	    double kg = getWeight();
	    double sm = ConvUtils.convMTToSM(kg);
		return DisplayUtils.formatWeight(sm);
	}

	public String getSectionDescription()
	{
		return mSectionDescriptions[getSection()];
	}
	
	public int getControlPoints()
	{
		return (int)(getPrice()/1000000.0*getTechLevel());
	}
	
    /**
     *
     */

    public Object clone() throws CloneNotSupportedException
    {
        Bean ret = (Bean)super.clone();
        ret.set(this);
        return ret;
    }


    /**
     *
     */

    public String toString()
    {
        return getName();
    }


    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj)
    {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return super.equals(obj);
        ShipComponent c2 = (ShipComponent)obj;
        if (getName().compareTo(c2.getName()) != 0)
            return false;
        if (getTechLevel() != c2.getTechLevel())
            return false;
        if (getVolume() != c2.getVolume())
            return false;
        if (getWeight() != c2.getWeight())
            return false;
        if (getPower() != c2.getPower())
            return false;
        if (getPrice() != c2.getPrice())
            return false;
        if (getSection() != c2.getSection())
            return false;
        return true;
    }
}
