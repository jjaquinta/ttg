/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.war;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class War
{
	public static String VERSION = "1.0.4.30";

	public War()
	{
	}
	
	public void run()
	{
		WarSplash splash = new WarSplash();
		splash.setVisible(true);
		WarFrame adv = new WarFrame(splash);
		adv.setVisible(true);
	}
	
	public static void main(String[] argv)
	{
		War app = new War();
		app.run();
	}
}
