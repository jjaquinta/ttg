/*
 * Created on Nov 30, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import jo.util.utils.io.OutputDevice;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefaultOutputDevice implements OutputDevice
{
    public void print(String msg)
    {
        System.out.print(msg);
    }
    public void println(String msg)
    {
        System.out.println(msg);
    }
    public void println()
    {
        System.out.println();
    }
}
