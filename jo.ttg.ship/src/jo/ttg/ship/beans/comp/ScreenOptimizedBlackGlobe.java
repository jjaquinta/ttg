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
public class ScreenOptimizedBlackGlobe extends ScreenComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 15, 21 };
	
	/**
	 * 
	 */
	public ScreenOptimizedBlackGlobe()
	{
		super();
		mTechLevel = 15;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Optimized Black Globe";
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
		135, 130, 127, 125, 122, 120, 119,
	};

	public double getVolume()
	{
		return volRange[mTechLevel - 15];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		130, 128, 126, 124, 122, 120, 118,
	};

	public double getWeight()
	{
		return weightRange[mTechLevel - 15];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		50, 40, 35, 30, 25, 20, 15,
	};

	public double getPower()
	{
		return powRange[mTechLevel-15];
	}

	/**
	 *
	 */
	public double getPrice()
	{
		return 400.0*1000000.0;
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
