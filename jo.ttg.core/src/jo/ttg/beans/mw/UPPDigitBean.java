/**
 * Created on Sep 20, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.mw;

import jo.util.beans.Bean;

/**
 * Class for digit of UWP
 *
 * @author  Joseph Jaquinta
 * @version     3.0, 01 July, 2001
 * */
public abstract class UPPDigitBean extends Bean
{
	public static final int UPP_UNKNOWN = -99;
	
    protected int   mValue;

    public UPPDigitBean()
    {
        mValue = UPP_UNKNOWN;
    }

    /**
     * Returns the value.
     * @return int
     */
    public int getValue()
    {
        return mValue;
    }

    /**
     * Sets the value.
     * @param value The value to set
     */
    public void setValue(int value)
    {
        this.mValue = value;
        if (mValue != UPP_UNKNOWN)
        	validate();
    }

    public String getValueDigit()
    {
        return Integer.toString(getValue(), 36).toUpperCase();
    }

    public abstract String getTitleDescription();
    public abstract String getTitleName();
    public abstract String getValueDescription();
    public abstract String getValueName();
    protected abstract void validate();
    public abstract int[] getValidValues();

}
