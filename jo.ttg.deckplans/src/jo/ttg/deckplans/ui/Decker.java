/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.deckplans.ui;

import jo.ttg.deckplans.logic.RuntimeLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Decker
{
	public static String VERSION = "1.0.0";

	public Decker()
	{
	    RuntimeLogic.init();
	}
	
	public void run()
	{
		DeckerFrame adv = new DeckerFrame();
		adv.setVisible(true);
	}
	
	public static void main(String[] argv)
	{
		Decker app = new Decker();
		app.run();
	}
}
