package jo.ttg.ship.beans.comp;

public class CommoLaser extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[][] mTechLevelRange = 
	{
		{ 8, 14 },
		{ 8, 15 },
		{ 8, 17 },
		{ 8, 18 },
		{ 8, 20 },
		{ 8, 20 },
		{ 9, 20 },
	};
	private int[] mRangeRange = { R_DISTANT, R_SYSTEM };
	
	/**
	 *
	 */

	public CommoLaser()
	{
		super();
		mTechLevel = 8;
		setRange(R_SYSTEM);
	}

	public int[] getRangeRange()
	{
		return mRangeRange;
	}

	/**
	 *
	 */

	public String getName()
	{
		return "Laser";
	}

	/**
	 *
	 */

	public int getTechLevel()
	{
		return mTechLevel;
	}

	public int[] getTechLevelRange()
	{
		return mTechLevelRange[getRange()];
	}

	/**
	 *
	 */

	public double getVolume()
	{
		return getWeight()*2;
	}

	/**
	 *
	 */

	public double getWeight()
	{
		return laserWeight[getRange()][mTechLevel - 8];
	}

	/**
	 *
	 */

	public double getPower()
	{
		return getWeight();
	}

	/**
	 *
	 */

	public double getPrice()
	{
		double mod;
		if (mTechLevel == 8)
			mod = 2;
		else
			mod = 1;
		return laserPrice[getRange()]*mod;
	}

	public void setTechLevel(int d)
	{
		mTechLevel = d;
	}

    // TODO: bug in this table!
	private static double[][] laserWeight = 
	{
		{ .004, .002, .0015,.001, .001, .000, .000, .000, .000, .000, .000 },
		{ .016, .008, .006, .004, .002, .001, .000, .000, .000, .000, .000 },
		{ .020, .010, .008, .005, .003, .002, .001, .001, .000, .000, .000 },
		{ .040, .020, .015, .010, .005, .003, .003, .002, .001, .000, .000 },
		{ .070, .035, .025, .018, .009, .005, .005, .003, .002, .001, .001 },
		{ .000, .012, .070, .050, .030, .015, .010, .010, .007, .005, .005 },
	};
	private static double[] laserPrice = { 1200, 5000, 11000, 21000, 36000, 56000, 180000 }; 

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_COMMO;
	}

}
