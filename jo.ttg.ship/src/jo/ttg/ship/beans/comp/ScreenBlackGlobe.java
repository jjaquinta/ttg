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
public class ScreenBlackGlobe extends ScreenComponent implements TechLevelComponent
{
	private static final int S_SMALL = 0;
	//private static final int S_MEDIUM = 1;
	private static final int S_LARGE = 2;
	private static final int S_HUGE = 3;
	
	private int	mTechLevel;
	private static final int[]	mTechLevelRange = { 15, 19 };
	private int	mSize;
	private static final int[][]	mSizeRange = 
	{
		{ S_SMALL, S_HUGE },
		{ S_SMALL, S_LARGE },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
		{ S_SMALL, S_SMALL },
	};
	private static final String[]	mSizeDescription = { "class I", "class II", "class III" };
	
	/**
	 * 
	 */
	public ScreenBlackGlobe()
	{
		super();
		mTechLevel = 15;
		mSize = S_SMALL;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Black Globe";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}
	
	private static final int[] tlOff = {
		0, 4, 7, 8, 9,
	};

	private int getOff()
	{
		return tlOff[mTechLevel-15] + mSize;
	}

	/**
	 *
	 */
	private static final double[] volRange = {
		135, 200, 270, 335, 270, 400, 475, 270, 270, 270,
	};

	public double getVolume()
	{
		return volRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] weightRange = {
		130, 190, 250, 320, 240, 350, 140, 240, 230, 210,
	};

	public double getWeight()
	{
		return weightRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] powRange = {
		50, 55, 60, 65, 65, 70, 75, 75, 75, 75,
	};

	public double getPower()
	{
		return powRange[getOff()];
	}

	/**
	 *
	 */
	private static final double[] priceRange = {
		400, 600, 800, 1000, 500, 700, 900, 500, 500, 500,
	};

	public double getPrice()
	{
		return priceRange[getOff()]*1000000;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange;
	}

	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	public int getSize()
	{
		return mSize;
	}

	public String getSizeDescription()
	{
		return mSizeDescription[mSize];
	}

	public int[] getSizeRange()
	{
		return mSizeRange[mTechLevel-15];
	}

	public void setSize(int i)
	{
		mSize = i;
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
		return getOff() + 1;
	}
}
