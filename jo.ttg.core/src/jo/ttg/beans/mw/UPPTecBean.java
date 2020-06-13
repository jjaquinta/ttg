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

public class UPPTecBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Technology Level";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Tec";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        return getValueDescription(mValue);
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public static String getValueDescription(int value)
    {
        switch (value)
        {
            case 0 :
                return "Pre-Industrial (primitive)";
            case 1 :
                return "Pre-Industrial (bronze, iron)";
            case 2 :
                return "Pre-Industrial (printing press)";
            case 3 :
                return "Pre-Industrial (basic science)";
            case 4 :
                return "Industrial (internal combustion)";
            case 5 :
                return "Industrial (mass production)";
            case 6 :
                return "Pre-Stellar (nuclear power)";
            case 7 :
                return "Pre-Stellar (mini. electronics)";
            case 8 :
                return "Pre-Stellar (superconductors)";
            case 9 :
                return "Early Stellar (fusion power)";
            case 10 :
                return "Early Stellar (jump drive)";
            case 11 :
                return "Average Stellar (large starships)";
            case 12 :
                return "Average Stellar (sophisticated robots)";
            case 13 :
                return "Average Stellar (holo data storage)";
            case 14 :
                return "High Stellar (antigrav cities)";
            case 15 :
                return "High Stellar (anagathics)";
            case 16 :
                return "Extreme Stellar (global terraforming)";
            case 17 :
                return "Extreme Stellar";
            case 18 :
                return "Extreme Stellar";
            case 19 :
                return "Extreme Stellar";
            case 20 :
                return "Extreme Stellar";
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
                return "Pre-Industrial";
            case 4 :
            case 5 :
                return "Industrial";
            case 6 :
            case 7 :
            case 8 :
                return "Pre-Stellar";
            case 9 :
            case 10 :
                return "Early Stellar";
            case 11 :
            case 12 :
            case 13 :
                return "Average Stellar";
            case 14 :
            case 15 :
                return "High Stellar";
            case 16 :
            case 17 :
            case 18 :
            case 19 :
            case 20 :
                return "Extreme Stellar";
        }
        return "Unknown";
    }

    protected void validate()
    {
        if (mValue < 0)
            mValue = 0;
        else if (mValue > 20)
            mValue = 20;
    }

    @Override
    public int[] getValidValues()
    {
        return VALID_VALUES;
    }
}
