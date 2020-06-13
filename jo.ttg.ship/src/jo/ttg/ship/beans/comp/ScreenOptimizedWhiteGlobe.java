/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.comp;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ScreenOptimizedWhiteGlobe extends ScreenComponent
{
	/**
	 * 
	 */
	public ScreenOptimizedWhiteGlobe()
	{
		super();
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Optimized White Globe";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return 21;
	}

	/**
	 *
	 */
	public double getVolume()
	{
		return 60;
	}

	/**
	 *
	 */
	public double getWeight()
	{
		return 55;
	}

	/**
	 *
	 */
	public double getPower()
	{
		return 50;
	}

	/**
	 *
	 */
	public double getPrice()
	{
		return 850.0*1000000.0;
	}

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_SCREENS;
	}

	public int getFactor()
	{
		return 1;
	}
}
