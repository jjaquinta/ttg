/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.ai.handler;

import java.util.Iterator;

import jo.util.utils.DebugUtils;
import ttg.beans.war.ShipInst;
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
public class SetupHandler extends BaseHandler
{
	public SetupHandler(ComputerPlayer player)
	{
		super(player);
	}

	
	public void setup()
	{
		DebugUtils.debug = false;
		DebugUtils.beginGroup("setup side='"+getSide().getSide().getName()+"'");
		// find unique ships
		mPlayer.getUniqueShips().addAll(getGame().getGame().getShipLibrary());
		for (Iterator i = getSide().getShips().iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			ShipLogic.addUnique(mPlayer.getUniqueShips(), ship.getShip());
		}
		// weight worlds
		DebugUtils.beginGroup("worlds");
		WorldInst[] worlds = new WorldInst[getSide().getWorlds().size()];
		getSide().getWorlds().toArray(worlds);
		int[] weights = new int[worlds.length];
		int totalWeight = 0;
		for (int i = 0; i < weights.length; i++)
		{
			weights[i] = getSetupWorldWeight(worlds[i]);
			DebugUtils.debug(worlds[i].getWorld().getName()+"="+weights[i]);
			totalWeight += weights[i];
		}
		sortWorlds(worlds, weights);
		DebugUtils.endGroup("worlds");
		// get ships
		ShipInst[] ships = new ShipInst[getSide().getShips().size()];
		getSide().getShips().toArray(ships);
		// sort ships
		int totalAttack = sortShips(ships);
		if (getGame().getGame().isAllowIntrinsicDefense())
			totalAttack += totalIntrinsicDefense(worlds);
		distributeShips(worlds, weights, totalWeight, ships, totalAttack);
		DebugUtils.endGroup("setup");
		DebugUtils.debug = true;
	}

	private void distributeShips(
		WorldInst[] worlds,
		int[] weights,
		int totalWeight,
		ShipInst[] ships,
		int totalAttack)
	{
		DebugUtils.beginGroup("ships");
		double attackPerWeight = (double)totalAttack/(double)totalWeight;
		int[] attacksGiven = new int[worlds.length];
		if (getGame().getGame().isAllowIntrinsicDefense())
			for (int i = 0; i < worlds.length; i++)
				attacksGiven[i] = WorldLogic.getIntrinsicDefense(worlds[i]);
		for (int i = 0; i < ships.length; i++)
		{
			if (ships[i].getContainedBy() != null)
				continue;
			for (int j = 0; j < worlds.length; j++)
			{
				double idealAttack = weights[j]*attackPerWeight;
				if (attacksGiven[j] < idealAttack)
				{
					ShipLogic.setDestination(getGame(), ships[i], worlds[j]);
					attacksGiven[j] += getSetupShipWeight(ships[i]);
					DebugUtils.debug(ships[i].getShip().getName()+"->"+worlds[j].getWorld().getName());
					break;
				}
			}
			if (ships[i].getDestination() == null)
			{	// got to place somewhere
				ShipLogic.setDestination(getGame(), ships[i], worlds[0]);
				attacksGiven[0] += getSetupShipWeight(ships[i]);
				DebugUtils.debug(ships[i].getShip().getName()+"->"+worlds[0].getWorld().getName());
			}
		}
		DebugUtils.endGroup("ships");
	}

	private int sortShips(ShipInst[] ships)
	{
		int totalAttack = 0;
		for (int i = 0; i < ships.length - 1; i++)
		{
			for (int j = i; j < ships.length; j++)
				if (getSetupShipWeight(ships[i]) < getSetupShipWeight(ships[j]))
				{
					ShipInst stmp = ships[i];
					ships[i] = ships[j];
					ships[j] = stmp;
				}
			totalAttack += getSetupShipWeight(ships[i]);
		}
		return totalAttack;
	}

	private void sortWorlds(WorldInst[] worlds, int[] weights)
	{
		for (int i = 0; i < worlds.length - 1; i++)
			for (int j = i + 1; j < worlds.length; j++)
				if (weights[i] < weights[j])
				{
					WorldInst wtmp = worlds[i];
					worlds[i] = worlds[j];
					worlds[j] = wtmp;
					int itmp = weights[i];
					weights[i] = weights[j];
					weights[j] = itmp;
				}
	}

	private int getSetupWorldWeight(WorldInst world)
	{
		return WorldLogic.eval(world, getGame().getGame().getVPHaveWorld())
				+ WorldLogic.eval(world, getGame().getGame().getVPLoseWorld());
	}

	private int getSetupShipWeight(ShipInst ship)
	{
		if (ship.getContainedBy() != null)
			return 0;
		return ShipLogic.getAttackRecursive(ship);
	}
}
