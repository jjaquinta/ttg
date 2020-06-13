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
public class ScreenOptimizedMeson extends ScreenComponent implements HullDependantPower, TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 14, 21 };
	
	/**
	 * 
	 */
	public ScreenOptimizedMeson()
	{
		super();
		mTechLevel = 14;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Optimized Meson Screen";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		108, 90, 84, 65, 40, 33,
	};

	public double getVolume()
	{
		return volRange[mTechLevel - 14];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		85, 75, 65, 55, 45, 40,
	};

	public double getWeight()
	{
		return weightRange[mTechLevel - 14];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		.135, .1, .065, .035, .015, .010,
	};

	public double getPower()
	{
		throw new IllegalArgumentException();
	}
	
	public double getPower(Hull h)
	{
		return powRange[mTechLevel-14]*h.getVolume();
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		55, 23, 17, 9, 4, 1,
	};

	public double getPrice()
	{
		return priceRange[mTechLevel-14]*1000000;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
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
