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
public class ScreenOptimizedProton extends ScreenComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 20, 21 };
	
	/**
	 * 
	 */
	public ScreenOptimizedProton()
	{
		super();
		mTechLevel = 20;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Optimized Proton Screen";
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
		12, 10,
	};

	public double getVolume()
	{
		return volRange[mTechLevel - 20];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		6, 5,
	};

	public double getWeight()
	{
		return weightRange[mTechLevel - 20];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		9000, 8000,
	};

	public double getPower()
	{
		return powRange[mTechLevel-20];
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		90, 78,
	};

	public double getPrice()
	{
		return priceRange[mTechLevel-20]*1000000;
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
