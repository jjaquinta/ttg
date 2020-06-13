package jo.ttg.ship.beans.comp;

import jo.ttg.ship.beans.ShipStats;

public class JumpDrive extends ShipComponent implements TechLevelComponent, VolumeComponent
{
	private double mVolume;
	private int mTechLevel;
	private int[] mTechLevelRange = { 9, 15 };
	private int[] mJumpRange = { 0, 6 };
	
    /**
     *
     */

    public JumpDrive()
    {
        super();
    }

    /**
     *
     */

    public String getName()
    {
        return "Jump Drive";
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
        return mVolume;
    }

    /**
     *
     */

    public double getWeight()
    {
        return mVolume/13.5*27.0;
    }

    /**
     *
     */

    public double getPower()
    {
        return 0;
    }

    /**
     *
     */

    public double getPrice()
    {
		return mVolume/13.5*3000000.0;
    }

    public void setTechLevel(int i)
    {
        mTechLevel = i;
    }

    public void setVolume(double d)
    {
        mVolume = d;
    }


	/**
	 *
	 */

	public int getJump(ShipStats s)
	{
		if (s.getDisplacement() <= 0)
			return 0;
		double pc = getVolume()/s.getDisplacement();
		int man = (int)Math.floor((pc-0.01)/0.01 + .5);
		return man;
	}

	public void setJump(int v, ShipStats s)
	{
		if (s.getDisplacement() <= 0)
			return;
		if (v == 0)
			setVolume(0);
		else
		{
			double pc = (v+1)*.01;
			setVolume(pc*s.getDisplacement());
		}
	}

	public int[] getJumpRange()
	{
		return mJumpRange;
	}


	/* (non-Javadoc)
	 * @see ttg.beans.ship.ShipComponent#getSection()
	 */
	public int getSection()
	{
		return S_LOCO;
	}
}
