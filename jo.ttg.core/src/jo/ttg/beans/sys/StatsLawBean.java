package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StatsLawBean extends Bean
{
    /**
      * Legal Uniformity unset
      * @see ttg.TBodyWorld#lUnif
      */
    public static final int LU_UNSET = 0;
    /**
      * Personal Legal Uniformity
      * @see ttg.TBodyWorld#lUnif
      */
    public static final int LU_PERSONAL = 1;
    /**
      * Territorial Legal Uniformity
      * @see ttg.TBodyWorld#lUnif
      */
    public static final int LU_TERRITORIAL = 2;
    /**
      * Undivided Legal Uniformity
      * @see ttg.TBodyWorld#lUnif
      */
    public static final int LU_UNDIVIDED = 3;

    public static final String[] UNIFORMITY_DESC = new String[] {
            "Unset",
            "Personal",
            "Territorial",
            "Undivided"
    };
    
    /**
      * Uniformity of Law
      */
    private int mUniformity;
    /**
      * Weapons Law Level
      */
    private int mWeapons;
    /**
      * Trade Law Level
      */
    private int mTrade;
    /**
      * Criminal Law Level
      */
    private int mCriminal;
    /**
      * Civil Law Level
      */
    private int mCivil;
    /**
      * Personal Freedom
      */
    private int mPersonalFreedom;

    // constructor

    public StatsLawBean()
    {
        mUniformity = 0;
        mWeapons = 0;
        mTrade = 0;
        mCriminal = 0;
        mCivil = 0;
        mPersonalFreedom = 0;
    }
    
    // utilities
    public String getUniformityDesc()
    {
        return UNIFORMITY_DESC[mUniformity];
    }
    
    // getters and setters

    /**
     * Returns the civil.
     * @return int
     */
    public int getCivil()
    {
        return mCivil;
    }

    /**
     * Returns the criminal.
     * @return int
     */
    public int getCriminal()
    {
        return mCriminal;
    }

    /**
     * Returns the personalFreedom.
     * @return int
     */
    public int getPersonalFreedom()
    {
        return mPersonalFreedom;
    }

    /**
     * Returns the trade.
     * @return int
     */
    public int getTrade()
    {
        return mTrade;
    }

    /**
     * Returns the uniformity.
     * @return int
     */
    public int getUniformity()
    {
        return mUniformity;
    }

    /**
     * Returns the weapons.
     * @return int
     */
    public int getWeapons()
    {
        return mWeapons;
    }

    /**
     * Sets the civil.
     * @param civil The civil to set
     */
    public void setCivil(int civil)
    {
        mCivil = civil;
    }

    /**
     * Sets the criminal.
     * @param criminal The criminal to set
     */
    public void setCriminal(int criminal)
    {
        mCriminal = criminal;
    }

    /**
     * Sets the personalFreedom.
     * @param personalFreedom The personalFreedom to set
     */
    public void setPersonalFreedom(int personalFreedom)
    {
        mPersonalFreedom = personalFreedom;
    }

    /**
     * Sets the trade.
     * @param trade The trade to set
     */
    public void setTrade(int trade)
    {
        mTrade = trade;
    }

    /**
     * Sets the uniformity.
     * @param uniformity The uniformity to set
     */
    public void setUniformity(int uniformity)
    {
        mUniformity = uniformity;
    }

    /**
     * Sets the weapons.
     * @param weapons The weapons to set
     */
    public void setWeapons(int weapons)
    {
        mWeapons = weapons;
    }

}
