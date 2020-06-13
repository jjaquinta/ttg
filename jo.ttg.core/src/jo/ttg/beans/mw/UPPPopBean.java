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

public class UPPPopBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
    };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Population";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Pop";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        switch (mValue)
        {
            case 0 :
                return "Low (less than 10)";
            case 1 :
                return "Low (tens)";
            case 2 :
                return "Low (hundreds)";
            case 3 :
                return "Low (thousands)";
            case 4 :
                return "Mod (ten thousands)";
            case 5 :
                return "Mod (hundred thousands)";
            case 6 :
                return "Mod (millions)";
            case 7 :
                return "Mod (ten millions)";
            case 8 :
                return "Mod (hundred millions)";
            case 9 :
                return "High (billions)";
            case 10 :
                return "High (ten billions)";
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
                return "Low";
            case 4 :
            case 5 :
            case 6 :
            case 7 :
                return "Mod";
            case 8 :
            case 9 :
            case 10 :
                return "High";
        }
        return "Unknown";
    }

    public int getTechMod()
    {
        if ((mValue >= 1) && (mValue <= 5))
            return 1;
        else if (mValue >= 10)
            return 4;
        else if (mValue >= 9)
            return 2;
        else if (mValue == 0)
            return -99;
        return 0;
    }
    
    public double getPopulation()
    {
        return 5*Math.pow(10, getValue());
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
