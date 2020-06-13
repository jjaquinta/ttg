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

public class UPPHydBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    };


    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Hydrographics";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Hyd";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        switch (mValue)
        {
            case 0 :
                return "Desert World (0%)";
            case 1 :
                return "Dry World (10%)";
            case 2 :
                return "Dry World (20%)";
            case 3 :
                return "Wet World (30%)";
            case 4 :
                return "Wet World (40%)";
            case 5 :
                return "Wet World (50%)";
            case 6 :
                return "Wet World (60%)";
            case 7 :
                return "Wet World (70%)";
            case 8 :
                return "Wet World (80%)";
            case 9 :
                return "Wet World (90%)";
            case 10 :
                return "Water World (100%)";
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
                return "Desert";
            case 1 :
            case 2 :
                return "Dry";
            case 3 :
            case 4 :
            case 5 :
            case 6 :
            case 7 :
            case 8 :
            case 9 :
                return "Wet";
            case 10 :
                return "Water";
        }
        return "Unknown";
    }

    public int getTechMod()
    {
        if (mValue >= 10)
            return 2;
        else if (mValue >= 9)
            return 1;
        return 0;
    }

    protected void validate()
    {
        if (mValue < 0)
            mValue = 0;
        else if (mValue > 10)
            mValue = 10;
    }

    @Override
    public int[] getValidValues()
    {
        return VALID_VALUES;
    }

}
