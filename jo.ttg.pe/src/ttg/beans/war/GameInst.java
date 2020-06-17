package ttg.beans.war;

import java.util.ArrayList;
import java.util.HashMap;

import jo.ttg.beans.OrdBean;
import jo.ttg.gen.IGenScheme;
import jo.util.beans.PCSBean;

public class GameInst extends PCSBean
{
	private Game		mGame;
	private ArrayList	mShips;
	private ArrayList	mSides;
	private HashMap		mWorlds;
	private int			mTurn;
	private int			mPhase;
	private int			mRound;
	private ArrayList	mStatusHistory;
	private String		mStatus;
	private IGenScheme	mScheme;
	private OrdBean 	mUpperBound;
	private OrdBean 	mLowerBound;
	
	public GameInst()
	{
		mShips = new ArrayList();
		mSides = new ArrayList();
		mWorlds = new HashMap();
		mStatusHistory = new ArrayList();
	}
	
    public Game getGame()
    {
        return mGame;
    }

    public ArrayList getShips()
    {
        return mShips;
    }

    public ArrayList getSides()
    {
        return mSides;
    }

    public void setGame(Game game)
    {
        mGame = game;
    }

    public void setShips(ArrayList list)
    {
        mShips = list;
    }

    public void setSides(ArrayList list)
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

    public ArrayList getStatusHistory()
    {
        return mStatusHistory;
    }

    public void setStatus(String string)
    {
        queuePropertyChange("status", mStatus, string);
        mStatus = string;
        firePropertyChange();
    }

    public void setStatusHistory(ArrayList list)
    {
        mStatusHistory = list;
    }

    public HashMap getWorlds()
    {
        return mWorlds;
    }

    public void setWorlds(HashMap map)
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
