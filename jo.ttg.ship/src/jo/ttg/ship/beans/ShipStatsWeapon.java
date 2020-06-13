package jo.ttg.ship.beans;

import jo.ttg.ship.beans.comp.Weapon;
import jo.ttg.utils.DisplayUtils;
import jo.util.utils.FormatUtils;

public class ShipStatsWeapon
{
	private String	mName;
	private Weapon	mComponent;
	private int		mSpineFactor;
	private int		mSpineBatteries;
	private int		mSpineBearing;
	private int		mBayFactor;
	private int		mBayBatteries;
	private int		mBayBearing;
	private int		mTurretFactor;
	private int		mTurretBatteries;
	private int		mTurretBearing;
    /**
     *
     */

    public ShipStatsWeapon()
    {
        super();
    }
    
	public String sLine1()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(mName);
		ret.append("=");
		ret.append(DisplayUtils.formatHex(mSpineFactor));
		ret.append(DisplayUtils.formatHex(mBayFactor));
		ret.append(DisplayUtils.formatHex(mTurretFactor));
		return ret.toString();
	}

	public String sLine2()
	{
		StringBuffer ret = new StringBuffer();
		ret.append(FormatUtils.rightJustify("Batt", mName.length()+1));
		ret.append(DisplayUtils.formatHex(mSpineBatteries));
		ret.append(DisplayUtils.formatHex(mBayBatteries));
		ret.append(DisplayUtils.formatHex(mTurretBatteries));
		return ret.toString();
	}

    public String sLine3()
    {
    	StringBuffer ret = new StringBuffer();
		ret.append(FormatUtils.rightJustify("Bear", mName.length()+1));
		ret.append(DisplayUtils.formatHex(mSpineBearing));
		ret.append(DisplayUtils.formatHex(mBayBearing));
		ret.append(DisplayUtils.formatHex(mTurretBearing));
		return ret.toString();
    }

    public int getBayBatteries()
    {
        return mBayBatteries;
    }

    public int getBayBearing()
    {
        return mBayBearing;
    }

    public int getBayFactor()
    {
        return mBayFactor;
    }

    public String getName()
    {
        return mName;
    }

    public int getSpineBatteries()
    {
        return mSpineBatteries;
    }

    public int getSpineBearing()
    {
        return mSpineBearing;
    }

    public int getSpineFactor()
    {
        return mSpineFactor;
    }

    public int getTurretBatteries()
    {
        return mTurretBatteries;
    }

    public int getTurretBearing()
    {
        return mTurretBearing;
    }

    public int getTurretFactor()
    {
        return mTurretFactor;
    }

    public void setBayBatteries(int i)
    {
        mBayBatteries = i;
    }

    public void setBayBearing(int i)
    {
        mBayBearing = i;
    }

    public void setBayFactor(int i)
    {
        mBayFactor = i;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public void setSpineBatteries(int i)
    {
        mSpineBatteries = i;
    }

    public void setSpineBearing(int i)
    {
        mSpineBearing = i;
    }

    public void setSpineFactor(int i)
    {
        mSpineFactor = i;
    }

    public void setTurretBatteries(int i)
    {
        mTurretBatteries = i;
    }

    public void setTurretBearing(int i)
    {
        mTurretBearing = i;
    }

    public void setTurretFactor(int i)
    {
        mTurretFactor = i;
    }
    /**
     * @return Returns the component.
     */
    public Weapon getComponent()
    {
        return mComponent;
    }
    /**
     * @param component The component to set.
     */
    public void setComponent(Weapon component)
    {
        mComponent = component;
    }
}
