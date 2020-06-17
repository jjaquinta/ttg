/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.ai.handler;

import java.util.ArrayList;
import java.util.Iterator;

import jo.ttg.beans.mw.MainWorldBean;
import jo.util.utils.DebugUtils;
import ttg.beans.war.GameInst;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.ShipLogic;
import ttg.logic.war.WorldLogic;
import ttg.view.war.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BaseHandler
{
	protected ComputerPlayer	mPlayer;
	
	public void debug(String msg)
	{
		DebugUtils.debug("["+getSide().getSide().getName()+"] - "+msg);
	}
	
	public BaseHandler(ComputerPlayer player)
	{
		mPlayer = player;
	}
    
    public SideInst getSide()
    {
    	return mPlayer.getSide();
    }
    
	public GameInst getGame()
	{
		return mPlayer.getGame();
	}
    
	public int worldValue(WorldInst world)
	{
		int ret;
		if (world.getSide() == getSide())
		{
			ret = WorldLogic.eval(world, getGame().getGame().getVPHaveWorld())
				+ WorldLogic.eval(world, getGame().getGame().getVPLoseWorld());
		}
		else if ((world.getSide() != null) || getGame().getGame().isAllowConvertNeutral())
		{
			ret = WorldLogic.eval(world, getGame().getGame().getVPHaveWorld())
				+ WorldLogic.eval(world, getGame().getGame().getVPGainWorld());
		}
		else
			ret = 0;
		if (getGame().getGame().isAllowConstruction())
		{
			MainWorldBean mw = world.getWorld();
			if (mw != null)
			{
				int port = mw.getPopulatedStats().getUPP().getPort().getValue();
				if (port == 'A')
					ret *= 4;
				else if (port == 'B')
					ret *= 2;
			}
			ret += WorldLogic.getResourceGeneration(getGame(), world);
		}
		return ret;
	}

	public int totalIntrinsicDefense(Object[] worlds)
	{
		int ret = 0;
		for (int i = 0; i < worlds.length; i++)
			ret += WorldLogic.getIntrinsicDefense((WorldInst)worlds[i]);
		return ret;
	}
	
	public ShipInst[] sortDescendingDefense(ArrayList shipList)
	{
		ShipInst[] ships = new ShipInst[shipList.size()];
		shipList.toArray(ships);
		for (int i = 0; i < ships.length - 1; i++)
			for (int j = i + 1; j < ships.length; j++)
				if (ShipLogic.getDefense(ships[i]) < ShipLogic.getDefense(ships[j]))
				{
					ShipInst tmp = ships[i];
					ships[i] = ships[j];
					ships[j] = tmp;
				}
		return ships;
	}
	
	public ShipInst[] sortDescendingAttack(ArrayList shipList)
	{
		ShipInst[] ships = new ShipInst[shipList.size()];
		shipList.toArray(ships);
		for (int i = 0; i < ships.length - 1; i++)
			for (int j = i + 1; j < ships.length; j++)
				if (ShipLogic.getAttack(ships[i]) < ShipLogic.getAttack(ships[j]))
				{
					ShipInst tmp = ships[i];
					ships[i] = ships[j];
					ships[j] = tmp;
				}
		return ships;
	}
	
	public void sortDescending(Object[] objs, int[] vals)
	{
		for (int i = 0; i < objs.length - 1; i++)
			for (int j = i + 1; j < objs.length; j++)
				if (vals[i] < vals[j])
				{
					Object otmp = objs[i];
					objs[i] = objs[j];
					objs[j] = otmp;
					int itmp = vals[i];
					vals[i] = vals[j];
					vals[j] = itmp;
				}
	}
	
	public boolean isScout(ShipInst ship)
	{
		return (ShipLogic.getAttack(ship) == 0)
			&& (ShipLogic.capacity(ship.getShip()) == 0)
			&& (ShipLogic.getJump(ship) > 0);
	}
    
	public void determineThresholds(WorldInst world)
	{
		int vpWorld = worldValue(world);
		int vpMine = getSide().getVictoryPoints();
		int vpBest = getLeadingPE();
		//DebugLogic.debug("Total Victory Points="+vpMine);
		//DebugLogic.debug("World's Victory Points="+vpWorld);
		double worldPC;
		if (vpMine == 0)
			worldPC = 100;
		else
			worldPC = (double)vpWorld/(double)vpMine;
		//DebugLogic.debug("World Percentage="+(worldPC*100));
		double sidePC = 1.0 - (double)vpMine/(double)vpBest;
		//DebugLogic.debug("Side Percentage="+(sidePC*100));
		double pc = Math.max(worldPC, sidePC);
		//DebugLogic.debug("Overall Percentage="+(pc*100));
		if (pc < .1)
			mPlayer.setDesiredAttackThreshold(4.0);
		else if (pc < .2)
			mPlayer.setDesiredAttackThreshold(3.0);
		else if (pc < .3)
			mPlayer.setDesiredAttackThreshold(2.0);
		else if (pc < .4)
			mPlayer.setDesiredAttackThreshold(1.5);
		else if (pc < .5)
			mPlayer.setDesiredAttackThreshold(1.0);
		else if (pc < .6)
			mPlayer.setDesiredAttackThreshold(0.666);
		else if (pc < .7)
			mPlayer.setDesiredAttackThreshold(0.5);
		else if (pc < .8)
			mPlayer.setDesiredAttackThreshold(0.333);
		else
			mPlayer.setDesiredAttackThreshold(0.25);
		mPlayer.setMinimalAttackThreshold(mPlayer.getDesiredAttackThreshold()/2);
		//DebugLogic.debug("DesiredAttackThreshold ="+mDesiredAttackThreshold );
	}

	public int getLeadingPE()
	{
		int max = 0;
		for (Iterator i = getGame().getSides().iterator(); i.hasNext(); )
		{
			SideInst side = (SideInst)i.next();
			if (side.getVictoryPoints() > max)
				max = side.getVictoryPoints();
		}
		return max;
	}
	
	public int findIndex(Object[] arr, Object obj)
	{
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == obj)
				return i;
		return -1;
	}
	
	public int getPeace(WorldInst world)
	{
		int peace = 999;
		Integer lastCombat = (Integer)mPlayer.getLastCombat().get(world.getOrds());
		if (lastCombat != null)
			peace = getGame().getTurn() - lastCombat.intValue();
		return peace;
	}
	
	public void resetPeace(WorldInst world)
	{
		mPlayer.getLastCombat().put(world.getOrds(), new Integer(getGame().getTurn()));
	}
	
	public static String name(WorldInst world)
	{
		return WorldLogic.getName(world);
	}
	
	public static String name(ShipInst ship)
	{
		return ship.getShip().getName()+" #"+ship.hashCode();
	}
	
	public static String name(Ship ship)
	{
		return ship.getName();
	}
	
	public static String name(SideInst side)
	{
		if (side == null)
			return "neutral";
		return side.getSide().getName();
	}

	public double getParanoia(WorldInst world)
	{
		ArrayList worlds = WorldLogic.hexesWithin(getGame(), world.getOrds(), 4);
		double tot = 0;
		double fact = 0;
		for (Iterator i = worlds.iterator(); i.hasNext(); )
		{
			WorldInst hex = (WorldInst)i.next();
			if (hex.getWorld() == null)
				continue;
			SideInst side = hex.getSide();
			if (side == world.getSide())
				fact += .25;
			else if (side == null)
				fact += .50;
			else
				fact += 1.0;
			tot += 1.0;
		}
		return fact/tot;
	}

	public int idealDefenseFactors(WorldInst world)
	{
		int pop = world.getWorld().getPopulatedStats().getUPP().getPop().getValue();
		int port = world.getWorld().getPopulatedStats().getUPP().getPort().getValue();
		int worldDefense = pop;
		if (port == 'A')
			worldDefense += 8;
		else if (port == 'B')
			worldDefense += 4;
		else if (port == 'C')
			worldDefense += 2;
		else if (port == 'D')
			worldDefense += 1;
		// calc required to defend fuel
		int systemDefense = WorldLogic.getFuelPoints(world)*4;
		if (systemDefense > worldDefense + 4)
			return worldDefense; // not worth holding as a strategic point
		return Math.max(worldDefense, systemDefense);
	}
	
	public int[] countShips(WorldInst world)
	{
		int SDBs = 0;
		int Fighters = 0;
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ShipLogic.isSDB(ship.getShip()))
				SDBs += ShipLogic.getDefense(ship);
			else if (ShipLogic.isFighter(ship.getShip()))
				Fighters++;
		}
		int[] ret = new int[2];
		ret[0] = SDBs;
		ret[1] = Fighters;
		return ret;
	}
}
