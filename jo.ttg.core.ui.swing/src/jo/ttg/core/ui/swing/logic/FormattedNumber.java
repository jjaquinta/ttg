package jo.ttg.core.ui.swing.logic;
/*
 * Created on Jan 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FormattedNumber implements Comparable<FormattedNumber>
{
    private double  mValue;
    private String  mFormat;
    
    public FormattedNumber(double value, String format)
    {
        mValue = value;
        mFormat = format;
    }
    
    public String toString()
    {
        return mFormat;
    }
    
    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(FormattedNumber o)
    {
        try
        {
            return (int)Math.ceil(mValue - o.mValue);
        }
        catch (ClassCastException e)
        {
            return 0;
        }
    }

}
