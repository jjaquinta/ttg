/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.cbt;

import jo.ttg.ship.beans.ShipStatsWeapon;
import jo.util.beans.Bean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WeaponToFireBean extends Bean
{
    public static final int TURRET = 1;
    public static final int BAY = 2;
    public static final int SPINAL = 3;
    
    private CombatShipBean	mShip;
    private int				mPhylum;
    private int				mType;
    private int				mFactor;
    private ShipStatsWeapon	mWeapon;
    
    public String toString()
    {
        StringBuffer ret = new StringBuffer(mWeapon.getName());
        if (mPhylum == TURRET)
            ret.append(" Turret");
        else if (mPhylum == BAY)
            ret.append(" Bay");
        else if (mPhylum == SPINAL)
            ret.append(" Spinal");
        return ret.toString();
    }
    
    public ShipStatsWeapon getWeapon()
    {
        return mWeapon;
    }
    public void setWeapon(ShipStatsWeapon weapon)
    {
        mWeapon = weapon;
    }
    public int getType()
    {
        return mType;
    }
    public void setType(int type)
    {
        mType = type;
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
     * @return Returns the factor.
     */
    public int getFactor()
    {
        return mFactor;
    }
    /**
     * @param factor The factor to set.
     */
    public void setFactor(int factor)
    {
        mFactor = factor;
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
