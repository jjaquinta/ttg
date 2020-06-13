/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.cbt;

import jo.util.beans.Bean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SensorPingBean extends Bean
{
    public static final int NONE = 0;
    public static final int LOCATED = 1;
    public static final int LOCKED = 2;
    
    private CombatShipBean	mShip;
    private int					mDegree;
    private double				mDisplacement;
    private double				mPower;
    private String				mType;
    
    public int getDegree()
    {
        return mDegree;
    }
    public void setDegree(int degree)
    {
        mDegree = degree;
    }
    public double getDisplacement()
    {
        return mDisplacement;
    }
    public void setDisplacement(double displacement)
    {
        mDisplacement = displacement;
    }
    public double getPower()
    {
        return mPower;
    }
    public void setPower(double power)
    {
        mPower = power;
    }
    public CombatShipBean getShip()
    {
        return mShip;
    }
    public void setShip(CombatShipBean ship)
    {
        mShip = ship;
    }
    public String getType()
    {
        return mType;
    }
    public void setType(String type)
    {
        mType = type;
    }
}
