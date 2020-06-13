package jo.ttg.ship.beans.comp;

public class CommoMaser extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[][] mTechLevelRange = 
	{
		{ 8, 15 },
		{ 8, 17 },
		{ 8, 18 },
		{ 8, 18 },
		{ 8, 20 },
		{ 8, 20 },
		{ 9, 20 },
	};
	private int[] mRangeRange = { R_DISTANT, R_SYSTEM };
	
	/**
	 *
	 */

	public CommoMaser()
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
		return "Maser";
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
		return maserWeight[getRange()][mTechLevel - 8];
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
		return maserPrice[getRange()]*mod;
	}

	public void setTechLevel(int d)
	{
		mTechLevel = d;
	}

	private static double[][] maserWeight = 
	{
		{ .008, .004, .003, .002, .001, .001, .000, .000, .000, .000, .000 },
		{ .032, .020, .015, .010, .005, .003, .003, .002, .001, .000, .000 },
		{ .040, .020, .015, .010, .005, .003, .003, .002, .001, .000, .000 },
		{ .080, .040, .030, .020, .010, .005, .005, .003, .002, .000, .000 },
		{ .140, .070, .060, .035, .018, .009, .009, .005, .003, .002, .002 },
		{ .220, .110, .075, .055, .028, .014, .014, .007, .005, .003, .003 },
		{ .000, .250, .200, .130, .060, .045, .040, .025, .013, .007, .007 },
	};
	private static double[] maserPrice = { 2400, 10000, 22000, 42000, 72000, 112000, 250000 };  

	public int getSection()
	{
		return S_COMMO;
	}

}
