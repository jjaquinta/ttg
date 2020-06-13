/*
 * Created on Oct 9, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.cmd;

import jo.util.utils.DebugUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

public abstract class BaseCmd implements Runnable
{
	// default constructor
	public BaseCmd()
	{
	}

	// arguments, defaults
 //   public static final int     defDebug           = 3;

	// arguments, help
//	  public static final String  helpDebug      = "Debug level (1=CRITICAL,... 5=TRACE)";

	// arguments, implementation

	// arguments, gets
  //  public int     getDebug            () { return Util.getDebug(); }

	// arguments, sets
 //   public void setDebug               (int v) { Util.setDebug(v); }

	public void init(String[] argv)
	{
		if (initArgs(argv))
		{
			run();
			DebugUtils.info("Done.");
		}
	}

	public boolean initArgs(String[] argv)
	{
		argv = ArgLogic.normalizeArgs(argv);
		DebugUtils.trace("Parsing args");
		if (ArgLogic.parseArgs(this, argv))
			return false;
		DebugUtils.trace("Printing args");
		ArgLogic.printArgs(this);
		return true;
	}

	public boolean unknownArg(String key, String val)
	{
		DebugUtils.warn("Can't set: "+key+" to "+val+", type -help for a list of arguments.");
		return false;
	}

	public void knownArg(String key, String val)
	{

	}
	
	public String defaultArg()
	{
		return null;
	}
	
	public abstract void run();
	public abstract String getOneLineDesc();
	public abstract String getLongDesc();
	
	public boolean pause(int hh, int mm, int ss)
	{
	    try
        {
            Thread.sleep(((hh*60 + mm)*60 + ss)*1000);
        }
        catch (InterruptedException e)
        {
            return false;
        }
        return true;
	}
}