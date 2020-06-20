package ttg.war.beans;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.OrdLogic;

public class WorldInst
{
    private OrdBean        mOrds;
    private MainWorldBean  mWorld;
    private SideInst       mSide;
    private int            mRepairsThisTurn;
    private int            mRepairsThisGame;
    private List<ShipInst> mShips;
    private List<ShipInst> mShipsEnRoute;
    private List<Ship>     mUnderConstruction;
    private int            mConstructionDone;

    public WorldInst()
    {
        mShips = new ArrayList<>();
        mShipsEnRoute = new ArrayList<>();
        mUnderConstruction = new ArrayList<>();
    }

    public int getRepairsThisTurn()
    {
        return mRepairsThisTurn;
    }

    public List<ShipInst> getShips()
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

    public void setShips(List<ShipInst> list)
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

    public List<ShipInst> getShipsEnRoute()
    {
        return mShipsEnRoute;
    }

    public void setShipsEnRoute(List<ShipInst> list)
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

    public List<Ship> getUnderConstruction()
    {
        return mUnderConstruction;
    }

    public void setConstructionDone(int i)
    {
        mConstructionDone = i;
    }

    public void setUnderConstruction(List<Ship> ship)
    {
        mUnderConstruction = ship;
    }

}
