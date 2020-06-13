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

public class UPPSizBean extends UPPDigitBean
{
    public static final int S_RING = -2;
    public static final int S_TINY = -1;
    public static final String TITLE = "Size";
    public static final String SHORT_TITLE = "Siz";
    public static final int[] VALID_VALUES = { S_RING, S_TINY, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return TITLE;
    }

    /**
     * 
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return SHORT_TITLE;
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        switch (mValue)
        {
            case S_RING :
                return "Ring";
            case S_TINY :
                return "Very Small (400 km)";
            case 0 :
                return "Belt";
            case 1 :
                return "Small (1600 km)";
            case 2 :
                return "Small (Luna; 3200 km)";
            case 3 :
                return "Small (Mercury; 4800 km)";
            case 4 :
                return "Small (Mars; 6400 km)";
            case 5 :
                return "Medium (8000 km)";
            case 6 :
                return "Medium (9600 km)";
            case 7 :
                return "Medium (11,200 km)";
            case 8 :
                return "Large (Terra; 12,800 km)";
            case 9 :
                return "Large (14,400 km)";
            case 10 :
                return "Large (16,000 km)";
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
            case S_RING:
                return "Ring";
            case S_TINY:
                return "Very Small";
            case 0 :
                return "Belt";
            case 1 :
            case 2 :
            case 3 :
            case 4 :
                return "Small";
            case 5 :
            case 6 :
            case 7 :
                return "Medium";
            case 8 :
            case 9 :
            case 10 :
                return "Large";
        }
        return "Unknown";
    }

    public int getTechMod()
    {
        if (mValue <= 1)
            return 2;
        else if (mValue <= 4)
            return 1;
        return 0;
    }

    protected void validate()
    {
        if (mValue < -2)
            mValue = -2;
        else if (mValue > 10)
            mValue = 10;
    }

    @Override
    public int[] getValidValues()
    {
        return VALID_VALUES;
    }
}
