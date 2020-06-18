package ttg.beans.war;

import java.util.ArrayList;
import java.util.List;

public class Side
{
    private String       mName;
    private int          mVictoryPoints;
    private int          mStartingResources;
    private List<String> mSetupWorlds;

    public Side()
    {
        mSetupWorlds = new ArrayList<>();
    }

    public String getName()
    {
        return mName;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public List<String> getSetupWorlds()
    {
        return mSetupWorlds;
    }

    public void setSetupWorlds(List<String> list)
    {
        mSetupWorlds = list;
    }

    /*
     * (non-Javadoc)
     * 
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
