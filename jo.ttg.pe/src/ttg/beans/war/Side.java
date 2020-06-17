package ttg.beans.war;

import java.util.ArrayList;

public class Side
{
	private String		mName;
	private int			mVictoryPoints;
	private int			mStartingResources;
	private ArrayList	mSetupWorlds;
	
	public Side()
	{
		mSetupWorlds = new ArrayList();
	}
	
    public String getName()
    {
        return mName;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public ArrayList getSetupWorlds()
    {
        return mSetupWorlds;
    }

    public void setSetupWorlds(ArrayList list)
    {
        mSetupWorlds = list;
    }

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		return mName;
	}

	/**
	 * @return
	 */
	public int getVictoryPoints()
	{
		return mVictoryPoints;
	}

	/**
	 * @param i
	 */
	public void setVictoryPoints(int i)
	{
		mVictoryPoints = i;
	}

    public int getStartingResources()
    {
        return mStartingResources;
    }

    public void setStartingResources(int i)
    {
        mStartingResources = i;
    }

}
