package jo.ttg.lbb.data.ship2;

import java.util.HashSet;
import java.util.Set;

public class CombatMessage
{
	public static final int HIGH = 0;
	public static final int MEDIUM = 1;
	public static final int LOW = 2;
	public static final int DEBUG = 3;
	
	private String		mMessage;
	private int			mTurn;
	private int			mPhase;
	private int			mPriority;
	private Set<Object>	mFilters;
	
	public CombatMessage()
	{
		mMessage = "";
		mPriority = LOW;
		mFilters = new HashSet<Object>();
	}
	
	public String toString()
	{
		return mMessage;
	}

	public String getMessage()
	{
		return mMessage;
	}

	public void setMessage(String message)
	{
		mMessage = message;
	}

	public int getPriority()
	{
		return mPriority;
	}

	public void setPriority(int priority)
	{
		mPriority = priority;
	}

	public Set<Object> getFilters()
	{
		return mFilters;
	}

	public void setFilters(Set<Object> filters)
	{
		mFilters = filters;
	}

	public int getTurn()
	{
		return mTurn;
	}

	public void setTurn(int turn)
	{
		mTurn = turn;
	}

	public int getPhase()
	{
		return mPhase;
	}

	public void setPhase(int phase)
	{
		mPhase = phase;
	}
}
