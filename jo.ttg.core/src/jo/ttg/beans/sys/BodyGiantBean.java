/**
 * Created on Oct 16, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.util.utils.FormatUtils;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BodyGiantBean extends BodyBean
{
    public BodyGiantBean()
    {
        setType(BT_GIANT);
    }
    
    /**
      * Size of Gas Giant
      */
    private int mSize;
    /**
      * Small Gas Giant Type
      * @see ttg.TBodyGiant#Size
      */
    public static final int GS_S = 0;
    /**
      * Large Gas Giant Type
      * @see ttg.TBodyGiant#Size
      */
    public static final int GS_L = 1;

    /**
     * Returns the size.
     * @return int
     */
    public int getSize()
    {
        return mSize;
    }

    /**
     * Sets the size.
     * @param size The size to set
     */
    public void setSize(int size)
    {
        mSize = size;
    }
    /**
     * Text description of size.
     * Returns text abbreviation of size classification of gas giant.
     *
     * @return text description
     * @see ttg.TBodyGiant#Size
     */
    public String getMiscDesc()
    {
        return ((getSize() == GS_S) ? "SGG" : "LGG");
    }
    /**
     * One line description of gas giant
     * Generate a one line description
     * @return gas giant description
     * @see ttg.TBody#sOneLine
     */
    public String getOneLineDesc()
    {
        return FormatUtils.leftJustify(getName(), 14) + " " + getMiscDesc();
    }
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
     * @see ttg.TBodyGiant#sOneLine
     */
    public String toString()
    {
        return getOneLineDesc();
    }
}
