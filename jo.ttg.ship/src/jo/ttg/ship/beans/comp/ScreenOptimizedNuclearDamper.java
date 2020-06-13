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
public class ScreenOptimizedNuclearDamper extends ScreenComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 14, 21 };
	
	/**
	 * 
	 */
	public ScreenOptimizedNuclearDamper()
	{
		super();
		mTechLevel = 14;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Optimized Nuclear Damper";
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
		28, 16, 13, 11, 9, 8, 7, 6,
	};

	public double getVolume()
	{
		return volRange[mTechLevel - 14];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		30, 18, 14, 12, 10, 8, 7, 5,
	};

	public double getWeight()
	{
		return weightRange[mTechLevel - 14];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		750, 500, 450, 425, 400, 450, 550, 610,
	};

	public double getPower()
	{
		return powRange[mTechLevel-14];
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		40, 35, 25, 15, 10, 5, 4, 3,
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
