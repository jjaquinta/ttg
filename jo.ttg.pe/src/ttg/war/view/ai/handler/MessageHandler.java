/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.ai.handler;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.mw.MainWorldBean;
import ttg.war.beans.PlayerMessage;
import ttg.war.beans.Ship;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.ShipLogic;
import ttg.war.logic.WorldLogic;
import ttg.war.view.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MessageHandler extends BaseHandler
{
	private boolean		mFirstMessage;
	private List<Ship>	mUniqueSDBs;
	private List<Ship>	mUniqueFighters;
	private List<Ship>	mUniqueStarships;

	public MessageHandler(ComputerPlayer player)
	{
		super(player);
		mFirstMessage = true;
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#message(ttg.beans.war.PlayerMessage)
	 */
	public void message(PlayerMessage msg)
	{
		ShipInst ship;
		WorldInst world;
		SideInst side;
		
		if (mFirstMessage)
		{
			mFirstMessage = false;
			orderConstruction();
		}
		switch (msg.getID())
		{
			case PlayerMessage.CANTREPAIR:
				ship = (ShipInst)msg.getArg1();
				debug("Can't repair "+
					name(ship)+" at "+
					name(ship.getLocation()));
				break;
			case PlayerMessage.CANTMOVE:
				ship = (ShipInst)msg.getArg1();
				debug("Can't move "+
					name(ship)+" at "+
					name(ship.getLocation())+
					" to "+name(ship.getDestination())+
					" because "+msg.getArg2());
				break;
			case PlayerMessage.NEWSHIP:
				ship = (ShipInst)msg.getArg1();
				debug("Created "+
					name(ship)+" at "+
					name(ship.getLocation()));
				if (ship.getLocation().getSide() == getSide())
					orderConstruction(ship.getLocation());
				break;
			case PlayerMessage.ENDOFTURN:
				debug((String)msg.getArg1());
				orderConstruction();
				break;
			case PlayerMessage.NEWOWNER:
				world = (WorldInst)msg.getArg1();
				side = (SideInst)msg.getArg2();
				debug(name(world)+" transfered from "+
					name(side)+" to "+
					name(world.getSide()));
				if (world.getSide() == getSide())
					orderConstruction(world);
				break;
			case PlayerMessage.COMBATSTART:
				world = (WorldInst)msg.getArg1();
				side = (SideInst)msg.getArg2();
				debug("Combat starting at "+
					name(world));
				break;
			default:
				debug(msg.toString());
				break;
		}
		if ((msg.getID() == PlayerMessage.COMBATSTART) || (msg.getID() == PlayerMessage.NEWOWNER))
		{
			world = (WorldInst)msg.getArg1();
			resetPeace(world);
		}
	}

	private void orderConstruction()
	{
		if (!getGame().getGame().isAllowConstruction())
			return;
		setupShips();
		for (WorldInst world : getSide().getWorlds())
			orderConstruction(world);
	}
    
	private void orderConstruction(WorldInst world)
	{
		if (!getGame().getGame().isAllowConstruction())
			return;
		MainWorldBean mw = world.getWorld();
		if (mw == null)
			return; 
		int port = mw.getPopulatedStats().getUPP().getPort().getValue();
		if ((port != 'A') && (port != 'B'))
			return;
		List<Ship> designs = world.getUnderConstruction();
		if (designs.size() > 0)
			return;
		if (port == 'A')
			orderConstructionA(world);
		else
			orderConstructionB(world);
	}
	private void orderConstructionA(WorldInst world)
	{
		//DebugLogic.beginGroup("shipConstructionA");
		int[] shipTypes = countShips(world);
		if (shipTypes[0] < idealDefenseFactors(world))
			buildOneOf(world, mUniqueSDBs);
		else
			buildOneOf(world, mUniqueStarships);
		//DebugLogic.endGroup("shipConstructionA");
	}
    
	private void orderConstructionB(WorldInst world)
	{
		//DebugLogic.beginGroup("shipConstructionB");
		int[] shipTypes = countShips(world);
		if (shipTypes[0] < idealDefenseFactors(world))
			buildOneOf(world, mUniqueSDBs);
		else if (shipTypes[1] < 2)
			buildOneOf(world, mUniqueFighters);
		//DebugLogic.endGroup("shipConstructionB");
	}
	
	private void buildOneOf(WorldInst world, List<Ship> ships)
	{
		// work out parameters
		int peace = getPeace(world);
		int acceptableCost = peace*WorldLogic.getConstructionPerTurn(getGame(), world);
		Ship design = null;
		int designCost = 0;
		// try to template from existing ship
		for (Ship unique : ships)
		{
			unique = ShipLogic.techDown(world, unique);
			int cost = ShipLogic.cost(unique);
			if (design == null)
			{
				design = unique;
				designCost = cost;
				continue;
			}
			// we're either looking for the most expensive within budget
			// or the lest expensive outside of budget
			if (designCost <= acceptableCost)
			{
				if ((cost <= acceptableCost) && (cost > designCost))
				{
					design = unique;
					designCost = cost;
				}
			}
			else
			{
				if (cost < designCost)
				{
					design = unique;
					designCost = cost;
				}
			}
		}
		if (design != null)
		{
			debug("design="+name(design)+", $"+designCost);
			Ship newDesign = new Ship();
			ShipLogic.copy(design, newDesign);
			world.getUnderConstruction().add(newDesign);
		}
	}
	
	private void setupShips()
	{
		if (mUniqueSDBs != null)
			return;
		mUniqueSDBs = new ArrayList<>();
		mUniqueFighters = new ArrayList<>();
		mUniqueStarships = new ArrayList<>();
		for (Ship ship : mPlayer.getUniqueShips())
		{
			if (ShipLogic.isSDB(ship))
				mUniqueSDBs.add(ship);
			else if (ShipLogic.isFighter(ship))
				mUniqueFighters.add(ship);
			else
				mUniqueStarships.add(ship);
		}
	}
}
