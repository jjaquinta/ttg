/**
 * Created on Oct 16, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.logic.mw.UPPLogic;
import jo.ttg.utils.DisplayUtils;
import jo.util.utils.FormatUtils;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BodyToidsBean extends BodyBean implements BodyPopulated
{
    /**
      * Maximum Diamter of individual Planetoids in AU.
      */
    private double mMaxDiam;
    /**
      * Percentage of Non-metalic zone.
      */
    private double mNZone;
    /**
      * Percentage of metalic zone.
      */
    private double mMZone;
    /**
      * Percentage of carbonaceous zone.
      */
    private double mCZone;
    /**
      * Width of planetoid belt in AU.
      */
    private double mWidth;
    /**
      * Mainworld statistics for populated belt.
      */
    private PopulatedStatsBean mPopulatedStats;

    /**
     * Returns the cZone.
     * @return double
     */
    public double getCZone()
    {
        return mCZone;
    }

    /**
     * Returns the maxDiam.
     * @return double
     */
    public double getMaxDiam()
    {
        return mMaxDiam;
    }

    /**
     * Returns the mZone.
     * @return double
     */
    public double getMZone()
    {
        return mMZone;
    }

    /**
     * Returns the nZone.
     * @return double
     */
    public double getNZone()
    {
        return mNZone;
    }

    /**
     * Returns the populatedStats.
     * @return PopulatedStatsBean
     */
    public PopulatedStatsBean getPopulatedStats()
    {
        return mPopulatedStats;
    }

    /**
     * Returns the width.
     * @return double
     */
    public double getWidth()
    {
        return mWidth;
    }

    /**
     * Sets the cZone.
     * @param cZone The cZone to set
     */
    public void setCZone(double cZone)
    {
        mCZone = cZone;
    }

    /**
     * Sets the maxDiam.
     * @param maxDiam The maxDiam to set
     */
    public void setMaxDiam(double maxDiam)
    {
        mMaxDiam = maxDiam;
    }

    /**
     * Sets the mZone.
     * @param mZone The mZone to set
     */
    public void setMZone(double mZone)
    {
        mMZone = mZone;
    }

    /**
     * Sets the nZone.
     * @param nZone The nZone to set
     */
    public void setNZone(double nZone)
    {
        mNZone = nZone;
    }

    /**
     * Sets the populatedStats.
     * @param populatedStats The populatedStats to set
     */
    public void setPopulatedStats(PopulatedStatsBean populatedStats)
    {
        this.mPopulatedStats = populatedStats;
    }

    /**
     * Sets the width.
     * @param width The width to set
     */
    public void setWidth(double width)
    {
        mWidth = width;
    }

    /**
     * Text description of details.
     * Returns text abbreviation of name, sizes, and zones of belt.
     *
     * @return text description
     */
    public String getDetailsDesc()
    {
        if (getMaxDiam() > getDiameter())
            return FormatUtils.leftJustify(getName(), 14)
                + " "
                + DisplayUtils.formatDistance(getDiameter())
                + "/"
                + getMaxDiamDesc()
                + ", "
                + getZonesDesc()
                + ", "
                + getWidthDesc();
        return FormatUtils.leftJustify(getName(), 14)
            + " "
            + getMaxDiamDesc()
            + ", "
            + getZonesDesc()
            + ", "
            + getWidthDesc();
    }
    /**
     * Text description of Maximum Diameter.
     * @return Maximum Diameter
     */
    public String getMaxDiamDesc()
    {
        return DisplayUtils.formatDistance(getMaxDiam());
    }
    /**
     * One line description of belt
     * Generate a one line description
     * @return belt description
     * @see ttg.TBody#sOneLine
     */
    public String getOneLineDesc()
    {
        return (getName() + "              ").substring(0, 14)
            + " "
            + UPPLogic.getUPPDesc(mPopulatedStats.getUPP())
            + " "
            + mPopulatedStats.getBasesDesc()
            + " "
            + UPPLogic.getTradeCodesDesc(mPopulatedStats.getUPP())
            + " "
            + String.valueOf(mPopulatedStats.getTravelZone())
            + "00"
            /*+ TUtil.sNum(mPopulatedStats.getPopDigit())*/;
    }
    /**
     * Text description of belt width.
     * @return belt width
     */
    public String getWidthDesc()
    {
        return DisplayUtils.formatDistance(getWidth());
    }
    /**
     * Text description of planetoid zones.
     * @return planetoid zones
     */
    public String getZonesDesc()
    {
        return "n-"
            + String.valueOf((int) (getNZone() * 100))
            + " m-"
            + String.valueOf((int) (getMZone() * 100))
            + " c-"
            + String.valueOf((int) (getCZone() * 100));
    }
    /**
     * Calculate temperature at specific orbit
     * Deferrs to parental object since a planetoid belt does not contribute to the temperature
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
     * One line description of belt
     * Generate a one line description
     * @return belt description
     * @see ttg.TBodyToids#sOneLine
     */
    public String toString()
    {
        return getOneLineDesc();
    }

    // constructor

    public BodyToidsBean()
    {
        mMaxDiam = 0;
        mNZone = 0;
        mMZone = 0;
        mCZone = 0;
        mWidth = 0;
        mPopulatedStats = new PopulatedStatsBean();
        setType(BT_TOIDS);
    }
}
