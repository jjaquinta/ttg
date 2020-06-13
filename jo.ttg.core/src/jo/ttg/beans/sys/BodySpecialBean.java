/**
 * Created on Oct 16, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public abstract class BodySpecialBean extends BodyBean
{
    public BodySpecialBean()
    {
        setType(BT_SPECIAL);
    }
    
    /**
      * SubType of Body
      */
    private int mSubType;
    /**
      * Station SubType
      * @see ttg.beans.sys.BodyBean#SubType
      */
    public static final int ST_STARPORT = 0;
    /**
      * Refinery SubType
      * @see ttg.beans.sys.BodyBean#SubType
      */
    public static final int ST_REFINERY = 1;
    /**
     * Refinery SubType
     * @see ttg.beans.sys.BodyBean#SubType
     */
   public static final int ST_SCOUTBASE = 2;
   /**
    * Refinery SubType
    * @see ttg.beans.sys.BodyBean#SubType
    */
  public static final int ST_NAVYBASE = 3;
  /**
   * Refinery SubType
   * @see ttg.beans.sys.BodyBean#SubType
   */
 public static final int ST_LOCALBASE = 4;
 /**
  * Refinery SubType
  * @see ttg.beans.sys.BodyBean#SubType
  */
public static final int ST_LABBASE = 5;
/**
 * Refinery SubType
 * @see ttg.beans.sys.BodyBean#SubType
 */
public static final int ST_SPACEPORT = 6;

    /**
     * Returns the subType.
     * @return int
     */
    public int getSubType()
    {
        return mSubType;
    }

    /**
     * Sets the subType.
     * @param size The subType to set
     */
    public void setSubType(int subType)
    {
        mSubType = subType;
    }

    /**
     * One line description of gas giant
     * Generate a one line description
     * @return gas giant description
     * @see ttg.TBody#sOneLine
     */
    public abstract String getOneLineDesc();

    /**
     * Calculate temperature at specific orbit
     * Deferrs to parental object since a Gas Giant does not contribute to the temperature
     * of its satelites
     * @param Orb orbit in orbit numbers
     * @return temperature in Kelvins
     * @see ttg.TBody#TemperatureAt(double,double,double)
     */
    public double getTemperatureAt(double Orb)
    {
        return getPrimary().getTemperatureAt(getOrbit());
    }
    /**
     * One line description of gas giant
     * Generate a one line description
     * @return gas giant description
     * @see ttg.beans.sys.BodyBean#sOneLine
     */
    public String toString()
    {
        return getOneLineDesc();
    }
    
    private Bean	mSpecialInfo;
    
    public Bean getSpecialInfo()
    {
        return mSpecialInfo;
    }
    public void setSpecialInfo(Bean specialInfo)
    {
        mSpecialInfo = specialInfo;
    }
}
