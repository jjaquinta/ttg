package ttg.logic.war;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

import jo.ttg.beans.mw.MainWorldBean;
import jo.util.utils.DebugUtils;
import ttg.beans.war.GameInst;
import ttg.beans.war.PlayerInterface;
import ttg.beans.war.PlayerMessage;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;

public class PhaseLogic extends Thread
{
	private GameInst	mGame;
	private Random		mRnd;
	
	public static void start(GameInst game)
	{
		Thread t = new PhaseLogic(game);
		t.start(); 
	}
	
	public PhaseLogic(GameInst game)
	{
		mGame = game;
		mRnd = new Random();
	}
	
	public void run()
	{
		mGame.setTurn(0);
		setupPhase();
		mGame.setTurn(1);
		for (;;)
		{
			movementPhase();
			combatPhase();
			constructionPhase();
			repairPhase();
			findLosers();
			sendMessage(PlayerMessage.ENDOFTURN, "End of turn "+mGame.getTurn(), null);
			mGame.setTurn(mGame.getTurn()+1);
			if ((mGame.getGame().getGameLength() > 0)
				&& (mGame.getTurn() > mGame.getGame().getGameLength()))
					break;
		}
		mGame.getGame().setAllowOmniscentSensors(true);
		sendMessage(PlayerMessage.GAMEOVER, null, null);
	}
	
	private void movementPhase()
	{
		mGame.setPhase(1);
		// tank up ships
		for (Iterator j = mGame.getShips().iterator(); j.hasNext(); )
		{
			ShipInst ship = (ShipInst)j.next();
			ShipLogic.setupFuel(ship);
		}
		// collect moves
		for (Iterator i = mGame.getSides().iterator(); i.hasNext(); )
		{
			SideInst side = (SideInst)i.next();
			side.getPlayer().move();
		} 
		// reset moves
		for (Iterator j = mGame.getShips().iterator(); j.hasNext(); )
		{
			ShipInst ship = (ShipInst)j.next();
			ship.setHasMoved(false);
		}
		// execute moves
		for (Iterator j = mGame.getShips().iterator(); j.hasNext(); )
		{
			ShipInst ship = (ShipInst)j.next();
			if ((ship.getShip().getJump() == 0) || (ship.getContainedBy() != null))
				continue;
			String moveError = ShipLogic.validateMove(mGame, ship);
			if (moveError == null)
			{
				ShipLogic.useFuel(mGame, ship);
				ShipLogic.place(mGame, ship);
			}
			else
			{
				sendMessage(ship.getSideInst(), PlayerMessage.CANTMOVE, ship, moveError);
				//DebugLogic.debug("PhaseLogic.movementPhase "+ship.getShip().getName()+" lacks range for "+WorldLogic.getName(ship.getDestination()));
			}
		}
	}
	
	private void combatPhase()
	{
		mGame.setPhase(2);
		// find worlds
		for (Iterator i = mGame.getWorlds().values().iterator(); i.hasNext(); )
		{
			WorldInst world = (WorldInst)i.next();
			boolean anyCombat = false;
			SideInst hasFactors = null;
			HashSet sides = new HashSet();
			for (Iterator j = world.getShips().iterator(); j.hasNext(); )
			{
				ShipInst ship = (ShipInst)j.next();
				ship.setHasFired(false);
				ship.setFleeing(false);
				ship.setTarget(null);
				sides.add(ship.getSideInst());
				if (ShipLogic.getAttackRecursive(ship) > 0)
					if (hasFactors == null)
						hasFactors = ship.getSideInst();
					else if (ship.getSideInst() != hasFactors)
						anyCombat = true;
			}
			if (anyCombat)
				performCombat(world, sides);
			assessOwnership(world);
		}
	}
	
	private void constructionPhase()
	{
		mGame.setPhase(2);
		// resource generation
		for (Iterator i = mGame.getWorlds().values().iterator(); i.hasNext(); )
			WorldLogic.addResourceProduction(mGame, (WorldInst)i.next());
		if (!mGame.getGame().isAllowConstruction())
			return;
		for (Iterator i = mGame.getWorlds().values().iterator(); i.hasNext(); )
		{
			WorldInst world = (WorldInst)i.next();
			constructUpon(world);
		}
	}
	
	private void constructUpon(WorldInst world)
	{
		MainWorldBean mw = world.getWorld();
		if (mw == null)
			return;
		SideInst side = world.getSide();
		if (side == null)
			return;
		int port = mw.getPopulatedStats().getUPP().getPort().getValue();
		if ((port < 'A') || (port > 'B'))
			return;
		int canDo = Math.min(WorldLogic.getConstructionPerTurn(mGame, world),
			side.getResources());
		if (canDo <= 0)
			return;
		ArrayList ships = world.getUnderConstruction();
		Object[] designs = ships.toArray();
		for (int j = 0; j < designs.length; j++)
		{
			Ship design = (Ship)designs[j];
			if (design.getJump() > WorldLogic.getMaxJumpConstruction(world))
			{
				ships.remove(design);
				continue;
			}
			int cost = ShipLogic.cost(design);
			int done = world.getConstructionDone();
			int todo = Math.min(canDo, cost - done);
			if (todo <= 0)
				break;
			world.setConstructionDone(done + todo);
			SideLogic.addResources(side, -todo);
			done += todo;
			canDo -= todo;
			//DebugLogic.debug(side.getSide().getName()+", "+design.getName()+", "+done+" of "+cost);
			if (done < cost)
				break;
			world.setConstructionDone(0);
			ShipInst ship = ShipLogic.buy(mGame, side, design);
			ship.setDestination(world);
			ShipLogic.place(mGame, ship);
			sendMessage(side, PlayerMessage.NEWSHIP, ship, null);
			ships.remove(design);
		}
	}
	
	private void repairPhase()
	{
		mGame.setPhase(3);
		// find worlds
		for (Iterator i = mGame.getWorlds().values().iterator(); i.hasNext(); )
		{
			WorldInst world = (WorldInst)i.next();
			world.setRepairsThisTurn(0);
			int canRepair = WorldLogic.canRepair(world);
			if (canRepair == 0)
				continue;
			ArrayList ships = new ArrayList();
			for (Iterator j = world.getShips().iterator(); j.hasNext(); )
			{
				ShipInst ship = (ShipInst)j.next();
				if (!ShipLogic.canBeRepaired(ship, world))
					continue;
				ship.setToDo(true);
				ships.add(ship);
			}
			if (ships.size() == 0)
				continue;
			world.getSide().getPlayer().repair(world, ships);
			for (Iterator j = ships.iterator(); j.hasNext(); )
			{
				ShipInst ship = (ShipInst)j.next();
				if (!ship.isToDo())
				{
					if (canRepair > 0)
					{
						ShipLogic.repair(world, ship);
						sendMessage(world.getSide(), PlayerMessage.DIDREPAIR, ship, null);
						canRepair--;
					}
					else
					{
						sendMessage(world.getSide(), PlayerMessage.CANTREPAIR, ship, null);
					}
				}
			}
		}
	}
	
	private void setupPhase()
	{
		mGame.setPhase(0);
		for (Iterator i = mGame.getSides().iterator(); i.hasNext(); )
		{
			SideInst side = (SideInst)i.next();
			PlayerInterface player = side.getPlayer();
			player.setup();
		}
		for (Iterator i = mGame.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			ShipLogic.place(mGame, ship);
		}
	}
	
	private void performCombat(WorldInst world, HashSet sides)
	{
		SideInst[] sidesList = new SideInst[sides.size()];
		sides.toArray(sidesList);
		sendMessage(sidesList, PlayerMessage.COMBATSTART, world, null);
		// initial flee phase
		mGame.setRound(0);
		performFleeing(world, sides);
		if (!isCombatOver(world))
		{
			// iterate through combat
			for (int round = 1; ; round++)
			{
				// target ships
				for (Iterator i = sides.iterator(); i.hasNext(); )
				{
					SideInst side = (SideInst)i.next();
					side.getPlayer().target(world);
				}
				// work out attack factors
				ArrayList toFlee = new ArrayList();
				int[] att = new int[world.getShips().size()];
				for (Iterator i = world.getShips().iterator(); i.hasNext(); )
				{
					ShipInst attacker = (ShipInst)i.next();
					if (attacker.isFleeing())
						continue;
					ShipInst target = attacker.getTarget();
					if ((target == null) || target.isFleeing())
						continue;
					int idx = world.getShips().indexOf(target);
					if (idx < 0)
						continue;						
					if (target == attacker) // flee!
						toFlee.add(attacker);
					else
					{
						att[idx] += ShipLogic.getAttack(attacker);
						attacker.setHasFired(true);
					}
				}
				// conduct attacks
				Object[] ships = world.getShips().toArray();
				for (int i = 0; i < att.length; i++)
				{
					if (att[i] == 0)
						continue;
					ShipInst ship = (ShipInst)ships[i];
					conductAttack(ship, att[i], sidesList);
				}
				for (Iterator i = toFlee.iterator(); i.hasNext(); )
				{
					ShipInst ship = (ShipInst)i.next();
					ship.setFleeing(true);
				}
				if (isCombatOver(world))
					break;
			}
		}
		sendMessage(sidesList, PlayerMessage.COMBATEND, world, null);
	}

	private void performFleeing(WorldInst world, HashSet sides)
	{
		// clear todo
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			ship.setToDo(true);
		}
		// find fleeing ships
		for (Iterator i = sides.iterator(); i.hasNext(); )
		{
			SideInst side = (SideInst)i.next();
			side.getPlayer().flee(world);
		}
		// flee ships
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (!ship.isToDo())
				ship.setFleeing(true);
		}
	}
	
	private void conductAttack(ShipInst ship, int att, SideInst[] sides)
	{
		int def = ShipLogic.getDefense(ship);
		if (def == 0)
		{
			conductHit(ship, sides);
			return;
		}
		double odds = getOdds(att, def);
		int target = getTarget(odds);
		int roll = 2 + mRnd.nextInt(6) + mRnd.nextInt(6);
		if (roll >= target)
			conductHit(ship, sides);
		else
			conductMiss(ship, sides);
	}
	
	private void conductHit(ShipInst ship, SideInst[] sides)
	{
		if (ship.isDamaged())
		{
			SideLogic.victoryPoints(ship.getSideInst(), -ShipLogic.eval(ship, mGame.getGame().getVPHaveShip()));
			SideLogic.victoryPoints(ship.getSideInst(), -ShipLogic.eval(ship, mGame.getGame().getVPLoseShip()));
			sendMessage(sides, PlayerMessage.SHIPDESTROYED, ship, null);
			ShipLogic.destroy(mGame, ship);
		}
		else
		{
			ShipLogic.damage(ship);
			sendMessage(sides, PlayerMessage.SHIPDAMAGED, ship, null);
		}
	}
	
	private void conductMiss(ShipInst ship, SideInst[] sides)
	{
		sendMessage(sides, PlayerMessage.SHIPMISSED, ship, null);
	}
	
	private boolean isCombatOver(WorldInst world)
	{
		SideInst owner = null;
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ship.isFleeing())
				continue;
			if ((owner != null) && (owner != ship.getSideInst()))
				return false;
			owner = ship.getSideInst();
		}
		return true;
	}

	private void assessOwnership(WorldInst world)
	{
		if ((world.getSide() == null) && !mGame.getGame().isAllowConvertNeutral())
			return;
		if (world.getWorld() == null)
			return; // no one owns empty space!		
		SideInst owner = null;
		int attackFactors = 0;
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ship.isFleeing())
			{
				ship.setFleeing(false);
				continue;
			}
			if (ship.getShip().getAttack() == 0)
				continue;
			if ((owner != null) && (owner != ship.getSideInst()))
			{
				DebugUtils.debug("****** "+world+" has multiple active ships!");
			}
			owner = ship.getSideInst();
			attackFactors += ShipLogic.getAttack(ship);
		}
		SideInst oldOwner = world.getSide();
		if ((owner == null) || (oldOwner == owner))
			return;
		if (mGame.getGame().isAllowIntrinsicDefense() 
			&& (attackFactors < WorldLogic.getIntrinsicDefense(world)))
				return;
		world.setSide(owner);
		world.getUnderConstruction().clear();
		world.setConstructionDone(0);
		if (oldOwner != null)
		{
			oldOwner.getWorlds().remove(world);
			SideLogic.victoryPoints(oldOwner, -WorldLogic.eval(world, mGame.getGame().getVPLoseWorld()));
			SideLogic.victoryPoints(oldOwner, -WorldLogic.eval(world, mGame.getGame().getVPHaveWorld()));
		}
		owner.getWorlds().add(world);
		SideLogic.victoryPoints(owner, WorldLogic.eval(world, mGame.getGame().getVPGainWorld()));
		SideLogic.victoryPoints(owner, WorldLogic.eval(world, mGame.getGame().getVPHaveWorld()));
		sendMessage(PlayerMessage.NEWOWNER, world, oldOwner);
	}
	
	private void findLosers()
	{
		ArrayList sides = mGame.getSides();
		boolean[] active = new boolean[sides.size()];
		for (Iterator i = mGame.getWorlds().values().iterator(); i.hasNext(); )
		{
			WorldInst world = (WorldInst)i.next();
			SideInst side = world.getSide();
			if (side != null)
				active[sides.indexOf(side)] = true;
		}
		for (Iterator i = mGame.getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ShipLogic.getAttack(ship) != 0)
			{
				SideInst side = ship.getSideInst();
				active[sides.indexOf(side)] = true;
			}
		}
		for (int i = 0; i < active.length; i++)
			if (!active[i])
			{
				SideInst side = (SideInst)sides.get(i);
				if (side.getShips().size() > 0)
				{
					sendMessage(side, PlayerMessage.YOULOSE, side, null);
					Object[] ships = side.getShips().toArray();
					for (int j = 0; j < ships.length; j++)
					{
						ShipInst ship = (ShipInst)ships[j];
						ShipLogic.destroy(mGame, ship);
					}
				}
			}
	}
	
	private void sendMessage(SideInst side, int id, Object arg1, Object arg2)
	{
		PlayerMessage msg = new PlayerMessage();
		msg.setID(id);
		msg.setArg1(arg1);
		msg.setArg2(arg2);
		side.getPlayer().message(msg);
	}
	
	private void sendMessage(SideInst[] sides, int id, Object arg1, Object arg2)
	{
		for (int i = 0; i < sides.length; i++)
			sendMessage(sides[i], id, arg1, arg2);
	}
	
	private void sendMessage(int id, Object arg1, Object arg2)
	{
		for (Iterator i = mGame.getSides().iterator(); i.hasNext(); )
		{
			SideInst s = (SideInst)i.next();
			sendMessage(s, id, arg1, arg2);
		}
	}
	
	public static double getOdds(int att, int def)
	{
		return (double)att/(double)def;
	}
	
	public static String getOddsDesc(int att, int def)
	{
		double odds = getOdds(att, def);
		if (odds >= 6) // 6:1
			return "6:1";
		else if (odds >= 5) // 5:1
			return "5:1";
		else if (odds >= 4) // 4:1
			return "4:1";
		else if (odds >= 3) // 3:1
			return "3:1";
		else if (odds >= 2) // 2:1
			return "2:1";
		else if (odds >= 1.5) // 3:2
			return "3:2";
		else if (odds >= 1.0) // 1:1
			return "1:1";
		else if (odds >= 0.666) // 2:3
			return "2:3";
		else if (odds >= .5) // 1:2
			return "1:2";
		else if (odds >= .333) // 1:3
			return "1:3";
		else
			return "1:4";
	}
	
	public static int getTarget(double odds)
	{
		int target;
		if (odds >= 6) // 6:1
			target = 2;
		else if (odds >= 5) // 5:1
			target = 3;
		else if (odds >= 4) // 4:1
			target = 4;
		else if (odds >= 3) // 3:1
			target = 5;
		else if (odds >= 2) // 2:1
			target = 6;
		else if (odds >= 1.5) // 3:2
			target = 7;
		else if (odds >= 1.0) // 1:1
			target = 8;
		else if (odds >= 0.666) // 2:3
			target = 9;
		else if (odds >= .5) // 1:2
			target = 10;
		else if (odds >= .333) // 1:3
			target = 11;
		else
			target = 12;
		return target;
	}
}
