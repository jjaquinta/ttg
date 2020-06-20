/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.ai.handler;

import java.util.List;

import ttg.war.beans.ShipInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.ShipLogic;
import ttg.war.view.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class TargetHandler extends BaseHandler
{
	public TargetHandler(ComputerPlayer player)
	{
		super(player);
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#target(ttg.beans.war.WorldInst)
	 */
	public void target(WorldInst world)
	{
		CombatStats stats = new CombatStats(world, getSide());
		if (stats.ourAttack == 0)
			return; // no one to fire
		// If our non-repairable attack cannot stand on its own
		// merge in damaged ships
		double attackThreshold;
		if (stats.theirDefense == 0)
			attackThreshold = 999.0;
		else
			attackThreshold =  (double)stats.ourFullAttack/(double)stats.theirDefense;
		if (attackThreshold < mPlayer.getDesiredAttackThreshold())
		{
			stats.ourFull.addAll(stats.ourDamaged);
			stats.ourFullAttack += stats.ourDamagedAttack;
			stats.ourFullDefense += stats.ourDamagedDefense;
			attackThreshold =  (double)stats.ourAttack/(double)stats.theirDefense;
			// do we stand a chance?
			if (attackThreshold < mPlayer.getMinimalAttackThreshold())
			{	// bug out!
				targetFlee(stats.ourFull);
				return;
			}
		}
		else
		{	// try to evac damaged
			targetFlee(stats.ourDamaged);
		}
		ShipInst[] theirShips = sortDescendingDefense(stats.theirs);
		int[] attackedWith = new int[theirShips.length];
		ShipInst[] ourShips = sortDescendingAttack(stats.ourFull);
		for (int i = 0; i < ourShips.length; i++)
		{
			// spread attacks to achieve desired threshold
			if (achieveMinimum(theirShips, attackedWith, ourShips[i]))
				continue;
			// met minimum, now spread attacks to achieve desired threshold
			if (achieveDesired(theirShips, attackedWith, ourShips[i]))
				continue;
			// met desired, now spread attacks to achieve max threshold
			achieveMax(theirShips, attackedWith, ourShips[i]);
		}
	}

	private void targetFlee(List<ShipInst> ships)
	{
		for (ShipInst ship : ships)
			ship.setTarget(ship);
	}

	private boolean achieveMax(
		ShipInst[] theirShips,
		int[] attackedWith,
		ShipInst ourShip)
	{
		for (int j = 0; j < theirShips.length; j++)
		{
			int def = ShipLogic.getDefense(theirShips[j]);
			if (def == 0) 
				continue;
			double attackThreshold = (double)attackedWith[j]/(double)def;
			if (attackThreshold < 6.0)
			{
				ourShip.setTarget(theirShips[j]);
				attackedWith[j] += ShipLogic.getAttack(ourShip);
				return true;
			}
		}
		return false;
	}

	private boolean achieveDesired(
		ShipInst[] theirShips,
		int[] attackedWith,
		ShipInst ourShip)
	{
		for (int j = 0; j < theirShips.length; j++)
		{
			int def = ShipLogic.getDefense(theirShips[j]);
			if (def == 0) 
				continue;
			double attackThreshold = (double)attackedWith[j]/(double)def;
			if (attackThreshold < mPlayer.getDesiredAttackThreshold())
			{
				ourShip.setTarget(theirShips[j]);
				attackedWith[j] += ShipLogic.getAttack(ourShip);
				return true;
			}
		}
		return false;
	}

	private boolean achieveMinimum(
		ShipInst[] theirShips,
		int[] attackedWith,
		ShipInst ourShip)
	{
		double attackThreshold;
		for (int j = 0; j < theirShips.length; j++)
		{
			int def = ShipLogic.getDefense(theirShips[j]);
			if (def == 0) 
				if (attackedWith[j] == 0)
				{
					ourShip.setTarget(theirShips[j]);
					attackedWith[j] += ShipLogic.getAttack(ourShip);
					return true;
				}
				else
					continue;
			attackThreshold = (double)attackedWith[j]/(double)def;
			if (attackThreshold < mPlayer.getDesiredAttackThreshold())
			{
				ourShip.setTarget(theirShips[j]);
				attackedWith[j] += ShipLogic.getAttack(ourShip);
				return true;
			}
		}
		return false;
	}
}
