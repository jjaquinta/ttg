/*
 * Created on Sep 14, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.io;

public interface LogEngine
{
    public void log(int severity, String msg, Throwable exception);
}
