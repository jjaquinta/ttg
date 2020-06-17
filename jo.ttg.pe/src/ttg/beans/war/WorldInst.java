package ttg.beans.war;

import java.util.ArrayList;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.OrdLogic;


public class WorldInst
{
	private OrdBean			mOrds;
	private MainWorldBean	mWorld;
	private SideInst		mSide;
	private int				mRepairsThisTurn;
	private int				mRepairsThisGame;
	private ArrayList		mShips;
	private ArrayList		mShipsEnRoute;
	private ArrayList		mUnderConstruction;
	private int				mConstructionDone;
	
	public WorldInst()
	{
		mShips = new ArrayList();
		mShipsEnRoute = new ArrayList();
		mUnderConstruction = new ArrayList();
	}
    public int getRepairsThisTurn()
    {
        return mRepairsThisTurn;
    }

    public ArrayList getShips()
    {
        return mShips;
    }

    public SideInst getSide()
    {
        return mSide;
    }

    public MainWorldBean getWorld()
    {
        return mWorld;
    }

    public void setRepairsThisTurn(int i)
    {
        mRepairsThisTurn = i;
    }

    public void setShips(ArrayList list)
    {
        mShips = list;
    }

    public void setSide(SideInst inst)
    {
        mSide = inst;
    }

    public void setWorld(MainWorldBean bean)
    {
        mWorld = bean;
    }

	/**
	 * @return
	 */
	public int getRepairsThisGame()
	{
		return mRepairsThisGame;
	}

	/**
	 * @param i
	 */
	public void setRepairsThisGame(int i)
	{
		mRepairsThisGame = i;
	}

    public ArrayList getShipsEnRoute()
    {
        return mShipsEnRoute;
    }

    public void setShipsEnRoute(ArrayList list)
    {
        mShipsEnRoute = list;
    }

    /**
     *
     */

    public String toString()
    {
        if (mWorld != null)
        	return mWorld.getName();
        else
			return OrdLogic.getShortNum(mOrds);
    }

    public OrdBean getOrds()
    {
        return mOrds;
    }

    public void setOrds(OrdBean bean)
    {
        mOrds = bean;
    }

    public int getConstructionDone()
    {
        return mConstructionDone;
    }

    public ArrayList getUnderConstruction()
    {
        return mUnderConstruction;
    }

    public void setConstructionDone(int i)
    {
        mConstructionDone = i;
    }

    public void setUnderConstruction(ArrayList ship)
    {
        mUnderConstruction = ship;
    }

}
