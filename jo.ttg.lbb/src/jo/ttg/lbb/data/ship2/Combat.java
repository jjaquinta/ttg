package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jo.util.beans.PCSBean;

public class Combat extends PCSBean
{
	public static final int PHASE_INTRUDER_MOVEMENT = 0;
	public static final int PHASE_INTRUDER_LASER_FIRE = 1;
	public static final int PHASE_NATIVE_LASER_RETURN_FIRE = 2;
	public static final int PHASE_INTRUDER_ORDANCE_LAUNCH = 3;
	public static final int PHASE_INTRUDER_COMPUTER_REPROGRAM = 4;
	public static final int PHASE_NATIVE_MOVEMENT = 5;
	public static final int PHASE_NATIVE_LASER_FIRE = 6;
	public static final int PHASE_INTRUDER_LASER_RETURN_FIRE = 7;
	public static final int PHASE_NATIVE_ORDANCE_LAUNCH = 8;
	public static final int PHASE_NATIVE_COMPUTER_REPROGRAM = 9;
	public static final int PHASE_INTERPHASE = 10;
	
	public static final String[] PHASE_NAMES =
	{
		"Intruder Movement",
		"Intruder Laser Fire",
		"Native Laser Return Fire",
		"Intruder Ordance Launch",
		"Intruder Computer Reprogram",
		"Native Movement",
		"Native Laser Fire",
		"Intruder Laser Return Fire",
		"Native Ordance Launch",
		"Native Computer Reprogram",
		"Game Turn Interphase",
	};
	
	private String		mID;
	private Scenario	mScenario;
	private String		mName;
	private int			mTurn;
	private int			mPhase;
	private List<CombatSide> 	mSides;
	private List<CombatMessage>	mMessages;
	private Random		mRND;
	
	public Combat()
	{
		mSides = new ArrayList<CombatSide>();
		mMessages = new ArrayList<CombatMessage>();
		mRND = new Random();
	}

	public String getID()
	{
		return mID;
	}

	public void setID(String iD)
	{
		mID = iD;
	}

	public Scenario getScenario()
	{
		return mScenario;
	}

	public void setScenario(Scenario scenario)
	{
		mScenario = scenario;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		mName = name;
	}

	public int getTurn()
	{
		return mTurn;
	}

	public void setTurn(int turn)
	{
		queuePropertyChange("turn", mTurn, turn);
		mTurn = turn;
		firePropertyChange();
	}

	public int getPhase()
	{
		return mPhase;
	}

	public void setPhase(int phase)
	{
		queuePropertyChange("phase", mPhase, phase);
		mPhase = phase;
		firePropertyChange();
	}

	public List<CombatSide> getSides()
	{
		return mSides;
	}

	public void setSides(List<CombatSide> sides)
	{
		mSides = sides;
	}

	public List<CombatMessage> getMessages()
	{
		return mMessages;
	}

	public void setMessages(List<CombatMessage> messages)
	{
		mMessages = messages;
	}

	public Random getRND()
	{
		return mRND;
	}

	public void setRND(Random rND)
	{
		mRND = rND;
	}
}
