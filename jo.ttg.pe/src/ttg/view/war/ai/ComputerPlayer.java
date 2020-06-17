package ttg.view.war.ai;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import jo.util.utils.DebugUtils;
import ttg.beans.war.GameInst;
import ttg.beans.war.PlayerInterface;
import ttg.beans.war.PlayerMessage;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.view.war.ai.handler.FleeHandler;
import ttg.view.war.ai.handler.MessageHandler;
import ttg.view.war.ai.handler.MoveHandler;
import ttg.view.war.ai.handler.RepairHandler;
import ttg.view.war.ai.handler.SetupHandler;
import ttg.view.war.ai.handler.TargetHandler;

public class ComputerPlayer implements PlayerInterface
{
	private static String DEBUG_ONLY = "Cararialta Empire";

	// handlers
	private SetupHandler	mSetupHandler;
	private MessageHandler	mMessageHandler;
	private MoveHandler		mMoveHandler;
	private FleeHandler		mFleeHandler;
	private TargetHandler	mTargetHandler;
	private RepairHandler	mRepairHandler;
	
	// data
	private Random		mRnd;
	private GameInst	mGame;
	private SideInst	mSide;
	private double		mDesiredAttackThreshold;
	private double		mMinimalAttackThreshold;
	private HashMap		mLastCombat;
	private ArrayList	mUniqueShips;
	
	public ComputerPlayer(GameInst game, SideInst side)
	{
		mRnd = new Random();
		mGame = game;
		mSide = side;
		mDesiredAttackThreshold = 4.0;
		mMinimalAttackThreshold = 2.0;
		mLastCombat = new HashMap();
		mUniqueShips = new ArrayList();
		mSetupHandler = new SetupHandler(this); 
		mMessageHandler = new MessageHandler(this); 
		mMoveHandler = new MoveHandler(this); 
		mFleeHandler = new FleeHandler(this); 
		mTargetHandler = new TargetHandler(this);
		mRepairHandler = new RepairHandler(this); 
	}
	
	private void beginPhase(String phase)
	{
		if (DEBUG_ONLY != null)
			DebugUtils.debug = mSide.getSide().getName().equals(DEBUG_ONLY);
	}
	
	private void endPhase(String phase)
	{
		DebugUtils.debug = true;
	}
	
    public void setup()
    {
    	beginPhase("setup");
    	mSetupHandler.setup();
		endPhase("setup");
    }

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#message(ttg.beans.war.PlayerMessage)
	 */
	public void message(PlayerMessage msg)
	{
		beginPhase("message");
		mMessageHandler.message(msg);
		endPhase("message");
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#move()
	 */
	public void move()
	{
		beginPhase("move");
		mMoveHandler.move();
		endPhase("move");
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#flee(ttg.beans.war.WorldInst)
	 */
	public void flee(WorldInst world)
	{
		beginPhase("flee");
		mFleeHandler.flee(world);
		endPhase("flee");
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#target(ttg.beans.war.WorldInst)
	 */
	public void target(WorldInst world)
	{
		beginPhase("target");
		mTargetHandler.target(world);
		endPhase("target");
	}

    public void repair(WorldInst world, ArrayList ships)
    {
		beginPhase("repair");
    	mRepairHandler.repair(world, ships);
		endPhase("repair");
    }
	/**
	 * @return
	 */
	public double getDesiredAttackThreshold()
	{
		return mDesiredAttackThreshold;
	}

	/**
	 * @return
	 */
	public GameInst getGame()
	{
		return mGame;
	}

	/**
	 * @return
	 */
	public HashMap getLastCombat()
	{
		return mLastCombat;
	}

	/**
	 * @return
	 */
	public double getMinimalAttackThreshold()
	{
		return mMinimalAttackThreshold;
	}

	/**
	 * @return
	 */
	public Random getRnd()
	{
		return mRnd;
	}

	/**
	 * @return
	 */
	public SideInst getSide()
	{
		return mSide;
	}

	/**
	 * @return
	 */
	public ArrayList getUniqueShips()
	{
		return mUniqueShips;
	}

	/**
	 * @param d
	 */
	public void setDesiredAttackThreshold(double d)
	{
		mDesiredAttackThreshold = d;
	}

	/**
	 * @param inst
	 */
	public void setGame(GameInst inst)
	{
		mGame = inst;
	}

	/**
	 * @param map
	 */
	public void setLastCombat(HashMap map)
	{
		mLastCombat = map;
	}

	/**
	 * @param d
	 */
	public void setMinimalAttackThreshold(double d)
	{
		mMinimalAttackThreshold = d;
	}

	/**
	 * @param random
	 */
	public void setRnd(Random random)
	{
		mRnd = random;
	}

	/**
	 * @param inst
	 */
	public void setSide(SideInst inst)
	{
		mSide = inst;
	}

	/**
	 * @param list
	 */
	public void setUniqueShips(ArrayList list)
	{
		mUniqueShips = list;
	}

}
