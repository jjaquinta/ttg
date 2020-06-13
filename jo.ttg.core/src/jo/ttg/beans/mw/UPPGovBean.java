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

public class UPPGovBean extends UPPDigitBean
{
    public static final int[] VALID_VALUES = {
        0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15,
    };


    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return "Government";
    }

    /**
     * @see ttg.model.UPPDigitBean#getTitleName()
     */
    public String getTitleName()
    {
        return "Gov";
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueDescription()
     */
    public String getValueDescription()
    {
        switch (mValue)
        {
            case 0 :
                return "No Government Structure. In many cases, family bonds predominate.";
            case 1 :
                return "Company/Corporation. Government by a company managerial elite; citizens are company employees.";
            case 2 :
                return "Participating Democracy. Government by advice and consent of the citizen.";
            case 3 :
                return "Self-Perpetuating Oligarchy. Government by a restricted minority, with little or no input from the masses.";
            case 4 :
                return "Representative Democracy. Government by elected representatives.";
            case 5 :
                return "Feudal Technocracy. Government by specific individuals for those who agree to be ruled. Relationships are based on the performance of technical activities which are mutually beneficial.";
            case 6 :
                return "Captive Government/Colony. Government by a leadership answerable to an outside group; a colony or conquered area.";
            case 7 :
                return "Balkanization. No central ruling authority exists; rival governments compete for control.";
            case 8 :
                return "Civil Service Bureaucracy. Government by agencies employing individuals selected for their expertise.";
            case 9 :
                return "Impersonal Bureaucracy. Government by agencies which are insulated from the governed.";
            case 10 :
                return "Charismatic Dictator. Government by a single leader enjoying the confidence of the citizens.";
            case 11 :
                return "Non-Charismatic Dictator. A previous charismatic dictator has been replaced by a leader through normal channels.";
            case 12 :
                return "Charismatic Oligarchy. Government by a select group, organisation, or class enhoying overwhelming confidence of the citizenry.";
            case 13 :
                return "Religious Dictatorship. Government by a religious minority which has little regard for the needs of the citizenry.";
            case 14 :
                return "Religious Autocracy. Government by a single religious leader having absolute power over the citizenry.";
            case 15 :
                return "Toltalitarian Oligarchy. Government by an all-powerful minority which maintains absolute control through widespread coercion and oppression.";
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
                return "No Government Structure";
            case 1 :
                return "Company/Corporation";
            case 2 :
                return "Participating Democracy";
            case 3 :
                return "Self-Perpetuating Oligarchy";
            case 4 :
                return "Representative Democracy";
            case 5 :
                return "Feudal Technocracy";
            case 6 :
                return "Captive Government/Colony";
            case 7 :
                return "Balkanization";
            case 8 :
                return "Civil Service Bureaucracy";
            case 9 :
                return "Impersonal Bureaucracy";
            case 10 :
                return "Charismatic Dictator";
            case 11 :
                return "Non-Charismatic Dictator";
            case 12 :
                return "Charismatic Oligarchy";
            case 13 :
                return "Religious Dictatorship";
            case 14 :
                return "Religious Autocracy";
            case 15 :
                return "Toltalitarian Oligarchy";
        }
        return "Unknown";
    }

    public int getTechMod()
    {
        if ((mValue == 0) || (mValue == 5))
            return 1;
        else if (mValue == 0xd)
            return -2;
        return 0;
    }

    /**
     * This returns the TCS government modifer.
     * If type is 0 then the peace value is
     * returned. If type is 1 then the war
     * value is returned.
     */
    public double getGovMod(int type)
    {
        if (type == 0)
            switch (mValue)
            {
                case 0 :
                    return .5;
                case 1 :
                    return .8;
                case 2 :
                    return 1.0;
                case 3 :
                    return .9;
                case 4 :
                    return .85;
                case 5 :
                    return .95;
                case 8 :
                    return 1.1;
                case 9 :
                    return 1.15;
                case 10 :
                    return 1.2;
                case 11 :
                    return 1.1;
                case 12 :
                    return 1.2;
                case 13 :
                    return .75;
            }
        if (type == 1)
            switch (mValue)
            {
                case 0 :
                    return 1.5;
                case 1 :
                    return 1.4;
                case 2 :
                    return 1.5;
                case 3 :
                    return 1.2;
                case 4 :
                    return 1.45;
                case 5 :
                    return 1.4;
                case 8 :
                    return 1.2;
                case 9 :
                    return 1.2;
                case 10 :
                    return 1.5;
                case 11 :
                    return 1.2;
                case 12 :
                    return 1.5;
                case 13 :
                    return 1.5;
            }
        return 1.0;
    }
    /**
     * This returns the PE Base Tax.
     */
    public double getBaseTax()
    {
        switch (mValue)
        {
            case 0 :
                return .05;
            case 1 :
                return .20;
            case 2 :
                return .30;
            case 3 :
                return .20;
            case 4 :
                return .25;
            case 5 :
                return .25;
            case 8 :
                return .40;
            case 9 :
                return .35;
            case 10 :
                return .30;
            case 11 :
                return .25;
            case 12 :
                return .30;
            case 13 :
                return .40;
            case 14 :
                return .35;
            case 15 :
                return .35;
        }
        return .10;
    }
    /**
     * This returns the PE Governmental Expense.
     */
    public double getExpense()
    {
        switch (mValue)
        {
            case 0 :
                return 0.95;
            case 1 :
                return 1.10;
            case 2 :
                return 1.40;
            case 3 :
                return 1.30;
            case 4 :
                return 1.15;
            case 5 :
                return 1.35;
            case 8 :
                return 1.30;
            case 9 :
                return 1.35;
            case 10 :
                return 1.05;
            case 11 :
                return 1.00;
            case 12 :
                return 1.25;
            case 13 :
                return 1.05;
            case 14 :
                return 1.10;
            case 15 :
                return 1.20;
        }
        return 1.10;
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
