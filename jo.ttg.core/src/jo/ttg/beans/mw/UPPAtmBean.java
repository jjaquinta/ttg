/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.mw;

/**
 * Class for Atmosphere Digit of UWP
 *
 * @author  Joseph Jaquinta
 * @version     3.0, 01 July, 2001
 * */

public class UPPAtmBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
    };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Atmosphere";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Atm";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        switch (mValue)
        {
            case 0 :
                return "Vacuum";
            case 1 :
                return "Vacuum (trace)";
            case 2 :
                return "Vacuum (very thin tainted)";
            case 3 :
                return "Vacuum (very thin)";
            case 4 :
                return "Thin (tainted)";
            case 5 :
                return "Thin";
            case 6 :
                return "Standard";
            case 7 :
                return "Standard (tainted)";
            case 8 :
                return "Dense";
            case 9 :
                return "Dense (tainted)";
            case 10 :
                return "Exotic";
            case 11 :
                return "Exotic (corrosive)";
            case 12 :
                return "Exotic (insidious)";
            case 13 :
                return "Exotic (dense, high)";
            case 14 :
                return "Exotic (ellipsoid)";
            case 15 :
                return "Exotic (thin, low)";
        }
        return "Unknown";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueName()
     */
    public String getValueName()
    {
        switch (mValue)
        {
            case 0 :
            case 1 :
            case 2 :
            case 3 :
                return "Vacuum";
            case 4 :
            case 5 :
                return "Thin";
            case 6 :
            case 7 :
                return "Std";
            case 8 :
            case 9 :
                return "Dense";
            case 10 :
            case 11 :
            case 12 :
            case 13 :
            case 14 :
            case 15 :
                return "Exotic";
        }
        return "Unknown";
    }

    public int getTechMod()
    {
        if ((mValue <= 3) || (mValue >= 10))
            return 1;
        return 0;
    }

    protected void validate()
    {
        if (mValue < 0)
            mValue = 0;
        else if (mValue > 15)
            mValue = 15;
    }

    @Override
    public int[] getValidValues()
    {
        return VALID_VALUES;
    }

}
