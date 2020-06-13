package jo.ttg.ship.beans.comp;

public class CommoRadio extends RangedComponent implements TechLevelComponent
{
	private int	mTechLevel;
	private int[] mTechLevelRange = { 5, 17 };
	private int[] mRangeRange = { R_DISTANT, R_SYSTEM };
	
    /**
     *
     */

    public CommoRadio()
    {
        super();
        mTechLevel = 5;
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
        return "Radio";
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
		return mTechLevelRange;
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
        return radioWeight[getRange()][mTechLevel - 5];
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
    	if (mTechLevel == 5)
    		mod = 3;
		else if (mTechLevel == 6)
			mod = 2;
		else
			mod = 1;
        return radioPrice[getRange()]*mod;
    }

    public void setTechLevel(int d)
    {
        mTechLevel = d;
    }

	private static double[][] radioWeight = 
	{
		{ 0.02,0.001,0.0001,0.0001,0.0001,0.0001,0.0001,0.0001,0.0001,0.0001,0.0001 },
		{ 0.07,0.007,0.0006,0.0005,0.0004,0.0002,0.0001,0.0001,0.0001,0.0001,0.0001 },
		{ 0.15,0.015,0.0013,0.0012,0.001,0.0007,0.0005,0.0003,0.0001,0.0001,0.0001 },
		{ 0.3,0.03,0.003,0.002,0.0015,0.0012,0.001,0.0007,0.0005,0.0003,0.0001 },
		{ 0.7,0.07,0.007,0.006,0.004,0.002,0.0013,0.001,0.0007,0.0007,0.0001 },
		{ 7,0.15,0.015,0.013,0.012,0.01,0.007,0.0005,0.002,0.0013,0.001 },
		{ 70,0.3,0.04,0.02,0.018,0.016,0.014,0.01,0.007,0.005,0.003 },
	};
	private static double[] radioPrice = { 85, 250, 500, 5000, 30000, 90000, 150000 }; 

	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_COMMO;
	}
}
