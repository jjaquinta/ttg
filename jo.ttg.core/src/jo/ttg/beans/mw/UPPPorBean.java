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

public class UPPPorBean extends UPPDigitBean
{
    public static final String[] PORTS = 
        {
            "A",
            "B",
            "C",
            "D",
            "E",
            "F",
            "G",
            "H",
            "X",
            "Y",
        };
    public static final String[] STARPORTS = 
        {
            "A",
            "B",
            "C",
            "D",
            "E",
            "X",
        };
    public static final String[] SPACEPORTS = 
        {
            "F",
            "G",
            "H",
            "Y",
        };
    public static final String TITLE = "Starport";
    public static final String SHORT_TITLE = "Prt";
    public static final int[] VALID_VALUES = 
        {
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'X',
            'Y',
        };

    /**
     * @see ttg.model.UPPDigitBean#getTitleDescription()
     */
    public String getTitleDescription()
    {
        return TITLE;
    }

    /**
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
            case 'A' :
                return "Excellent Starport";
            case 'B' :
                return "Good Starport";
            case 'C' :
                return "Routine Starport";
            case 'D' :
                return "Poor Starport";
            case 'E' :
                return "Frontier Starport";
            case 'F' :
                return "Good Spaceport";
            case 'G' :
                return "Poor Spaceport";
            case 'H' :
                return "Primitive Spaceport";
            case 'X' :
                return "No Starport";
            case 'Y' :
                return "No Spaceport";
        }
        return "Unknown";
    }
    
    public boolean isExcellent()
    {
        return mValue == 'A';
    }
    
    public boolean isGood()
    {
        return (mValue == 'B') || (mValue == 'F');
    }

    public String getValueDigit()
    {
        return String.valueOf((char)getValue());
    }

    /**
     * @see ttg.model.UPPDigitBean#getValueName()
     */
    public String getValueName()
    {
        switch (mValue)
        {
            case 'A' :
                return "Excellent";
            case 'B' :
                return "Good";
            case 'C' :
                return "Routine";
            case 'D' :
                return "Poor";
            case 'E' :
                return "Frontier";
            case 'F' :
                return "Good";
            case 'G' :
                return "Poor";
            case 'H' :
                return "Primative";
            case 'X' :
                return "None";
            case 'Y' :
                return "None";
        }
        return "Unknown";
    }

    public boolean isCanDock()
    {
        switch (mValue)
        {
            case 'X' :
            case 'Y' :
                return false;
        }
        return true;
    }
    public boolean getHasFuel()
    {
        switch (mValue)
        {
            case 'A' :
            case 'B' :
            case 'C' :
            case 'D' :
            case 'F' :
            case 'G' :
                return true;
        }
        return false;
    }
    public boolean getHasRefinedFuel()
    {
        switch (mValue)
        {
            case 'A' :
            case 'B' :
            case 'C' :
            case 'D' :
            case 'F' :
            case 'G' :
                return true;
        }
        return false;
    }
    public int getTechMod()
    {
        if (mValue == 'A')
            return 6;
        else if (mValue == 'B')
            return 4;
        else if (mValue == 'C')
            return 2;
        else if (mValue == 'X')
            return -4;
        return 0;
    }
    public double getTradeCostMod()
    {
        switch (mValue)
        {
            case 'A' :
                return 3000.0 / 4000.0;
            case 'B' :
                return 5000.0 / 4000.0;
            case 'C' :
                return 5000.0 / 4000.0;
            case 'D' :
                return 6000.0 / 4000.0;
            case 'E' :
                return 7000.0 / 4000.0;
            case 'F' :
                return 5000.0 / 4000.0;
            case 'G' :
                return 6000.0 / 4000.0;
            case 'H' :
                return 7000.0 / 4000.0;
            case 'X' :
                return 9000.0 / 4000.0;
            case 'Y' :
                return 9000.0 / 4000.0;
        }
        return 1.0;
    }

    protected void validate()
    {
       if ((mValue < 'A') || ((mValue > 'H') && (mValue < 'X')) || (mValue > 'Y'))
            mValue = 'X';
    }

    @Override
    public int[] getValidValues()
    {
        return VALID_VALUES;
    }
}
