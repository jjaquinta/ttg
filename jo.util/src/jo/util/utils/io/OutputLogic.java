/*
 * Created on Sep 16, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.io;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.DefaultOutputDevice;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class OutputLogic
{
    public static OutputDevice	mSystemLogger;
    public static Map<Thread,OutputDevice>		mAncilliaryLoggers;
    
	private static synchronized void init()
	{
	    if (mSystemLogger != null)
	        return;
        mSystemLogger = new DefaultOutputDevice();
        mAncilliaryLoggers = new HashMap<Thread,OutputDevice>();
    }
	
	public static OutputDevice getOutput()
	{
	    init();
	    OutputDevice ret = (OutputDevice)mAncilliaryLoggers.get(Thread.currentThread());
	    if (ret == null)
	        ret = mSystemLogger;
	    return ret;
	}
	
	public static void setOutput(OutputDevice log)
	{
	    init();
	    if (log == null)
	        mAncilliaryLoggers.remove(Thread.currentThread());
	    else
	        mAncilliaryLoggers.put(Thread.currentThread(), log);
	}
	
	public static void print(String msg)
	{
	    getOutput().print(msg);
	}
	
	public static void println(String msg)
	{
	    getOutput().println(msg);
	}
	
	public static void println()
	{
	    getOutput().println();
	}
}