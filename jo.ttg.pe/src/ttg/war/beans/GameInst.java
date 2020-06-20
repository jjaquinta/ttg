package ttg.war.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.OrdBean;
import jo.ttg.gen.IGenScheme;
import jo.util.beans.PCSBean;

public class GameInst extends PCSBean
{
    private Game                   mGame;
    private List<ShipInst>         mShips;
    private List<SideInst>         mSides;
    private Map<String, WorldInst> mWorlds;
    private int                    mTurn;
    private int                    mPhase;
    private int                    mRound;
    private List<String>           mStatusHistory;
    private String                 mStatus;
    private IGenScheme             mScheme;
    private OrdBean                mUpperBound;
    private OrdBean                mLowerBound;

    public GameInst()
    {
        mShips = new ArrayList<>();
        mSides = new ArrayList<>();
        mWorlds = new HashMap<>();
        mStatusHistory = new ArrayList<>();
    }

    public Game getGame()
    {
        return mGame;
    }

    public List<ShipInst> getShips()
    {
        return mShips;
    }

    public List<SideInst> getSides()
    {
        return mSides;
    }

    public void setGame(Game game)
    {
        mGame = game;
    }

    public void setShips(List<ShipInst> list)
    {
        mShips = list;
    }

    public void setSides(List<SideInst> list)
    {
        mSides = list;
    }

    public int getTurn()
    {
        return mTurn;
    }

    public void setTurn(int i)
    {
        queuePropertyChange("turn", mTurn, i);
        mTurn = i;
        firePropertyChange();
    }

    public int getPhase()
    {
        return mPhase;
    }

    public void setPhase(int i)
    {
        queuePropertyChange("phase", mPhase, i);
        mPhase = i;
        firePropertyChange();
    }

    public String getStatus()
    {
        return mStatus;
    }

    public List<String> getStatusHistory()
    {
        return mStatusHistory;
    }

    public void setStatus(String string)
    {
        queuePropertyChange("status", mStatus, string);
        mStatus = string;
        firePropertyChange();
    }

    public void setStatusHistory(List<String> list)
    {
        mStatusHistory = list;
    }

    public Map<String, WorldInst> getWorlds()
    {
        return mWorlds;
    }

    public void setWorlds(Map<String, WorldInst> map)
    {
        mWorlds = map;
    }

    /**
     * @return
     */
    public int getRound()
    {
        return mRound;
    }

    /**
     * @param i
     */
    public void setRound(int i)
    {
        mRound = i;
    }

    /**
     * @return
     */
    public IGenScheme getScheme()
    {
        return mScheme;
    }

    /**
     * @param scheme
     */
    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

    /**
     * @return
     */
    public OrdBean getLowerBound()
    {
        return mLowerBound;
    }

    /**
     * @return
     */
    public OrdBean getUpperBound()
    {
        return mUpperBound;
    }

    /**
     * @param bean
     */
    public void setLowerBound(OrdBean bean)
    {
        mLowerBound = bean;
    }

    /**
     * @param bean
     */
    public void setUpperBound(OrdBean bean)
    {
        mUpperBound = bean;
    }
}
