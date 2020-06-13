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

public class UPPLawBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
    };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Law Level";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Law";
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
                return "No law (no prohibitions)";
            case 1 :
                return "Low law (body pistols, explosives, poison gas prohibited)";
            case 2 :
                return "Low law (portable energy weapons prohibited)";
            case 3 :
                return "Low law (machineguns, automatic rifles prohibited)";
            case 4 :
                return "Moderate law (light assult weapons prohibited)";
            case 5 :
                return "Moderate law (personal concealable weapons prohibited)";
            case 6 :
                return "Moderate law (all firearms except shotguns prohibited)";
            case 7 :
                return "Moderate law (shotguns prohibited)";
            case 8 :
                return "High law (blade weapons controlled; no open display)";
            case 9 :
                return "High law (weapon possession prohibited)";
            case 10 :
                return "Extreme law (weapon possession prohibited)";
            case 11 :
                return "Extreme law (rigid control of civilian movement)";
            case 12 :
                return "Extreme law (unrestricted invasion of privacy)";
            case 13 :
                return "Extreme law (paramilitary law enforcement)";
            case 14 :
                return "Extreme law (full-fledged police state)";
            case 15 :
                return "Extreme law (all facets of daily life regidly controlled)";
            case 16 :
                return "Extreme law (severe punishment for petty infractions)";
            case 17 :
                return "Extreme law (legalised oppressive practices)";
            case 18 :
                return "Extreme law (routinely oppressive and restrictive)";
            case 19 :
                return "Extreme law (excessively oppressive and restrictive)";
            case 20 :
                return "Extreme law (totally oppressive and restrictive)";
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
                return "No";
            case 1 :
            case 2 :
                return "Low";
            case 3 :
            case 4 :
            case 5 :
            case 6 :
            case 7 :
                return "Moderate";
            case 8 :
            case 9 :
                return "High";
            case 10 :
            case 11 :
            case 12 :
            case 13 :
            case 14 :
            case 15 :
            case 16 :
            case 17 :
            case 18 :
            case 19 :
            case 20 :
                return "Extreme";
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
