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
public class SensorToScanBean extends Bean
{
    public static final int T_SCAN = 1;
    public static final int T_PINPOINT = 2;
    public static final int P_ACTIVE = 1;
    public static final int P_PASSIVE = 2;
    
    private CombatShipBean	mShip;
    private int				mPhylum;
    private int				mType;
    private int				mDifficulty;
    private String			mSensor;
    
    public String toString()
    {
        return mSensor;
    }
    
    /**
     * @return Returns the difficulty.
     */
    public int getDifficulty()
    {
        return mDifficulty;
    }
    /**
     * @param difficulty The difficulty to set.
     */
    public void setDifficulty(int difficulty)
    {
        mDifficulty = difficulty;
    }
    /**
     * @return Returns the sensor.
     */
    public String getSensor()
    {
        return mSensor;
    }
    /**
     * @param sensor The sensor to set.
     */
    public void setSensor(String sensor)
    {
        mSensor = sensor;
    }
    /**
     * @return Returns the ship.
     */
    public CombatShipBean getShip()
    {
        return mShip;
    }
    /**
     * @param ship The ship to set.
     */
    public void setShip(CombatShipBean ship)
    {
        mShip = ship;
    }
    /**
     * @return Returns the type.
     */
    public int getType()
    {
        return mType;
    }
    /**
     * @param type The type to set.
     */
    public void setType(int type)
    {
        mType = type;
    }
    /**
     * @return Returns the phylum.
     */
    public int getPhylum()
    {
        return mPhylum;
    }
    /**
     * @param phylum The phylum to set.
     */
    public void setPhylum(int phylum)
    {
        mPhylum = phylum;
    }
}
