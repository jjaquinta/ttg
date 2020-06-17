/*
 * Created on Apr 16, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.ai.handler;

import java.util.Iterator;

import ttg.beans.war.ShipInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.ShipLogic;
import ttg.view.war.ai.ComputerPlayer;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class FleeHandler extends BaseHandler
{
	public FleeHandler(ComputerPlayer player)
	{
		super(player);
	}

	/* (non-Javadoc)
	 * @see ttg.beans.war.PlayerInterface#flee(ttg.beans.war.WorldInst)
	 */
	public void flee(WorldInst world)
	{
		//DebugLogic.beginGroup("flee side='"+mSide.getSide().getName()+"'");
		CombatStats stats = new CombatStats(world, getSide());
		fleeNonAttackers(stats);
		if (stats.ours.size() == 0)
			return; // everyone flees
		if (stats.theirDefense == 0)
			return; // everyone stays
		// If our non-damaged attack can stand on its own
		// then all damaged ships should flee
		double attackThreshold =  (double)stats.ourFullAttack/(double)stats.theirDefense;
		//DebugLogic.debug("fullAttackThreshold="+attackThreshold);
		if (attackThreshold >= mPlayer.getDesiredAttackThreshold())
		{
			fleeDamaged(stats);
			return;
		}
		// If everything we have doesn't meet our minimum
		// comfort level then everyone should flee
		attackThreshold =  (double)stats.ourAttack/(double)stats.theirDefense;
		//DebugLogic.debug("fullAndDamagedAttackThreshold="+attackThreshold);
		if (attackThreshold < mPlayer.getMinimalAttackThreshold())
		{
			fleeEveryone(stats);
			return;
		}
		//DebugLogic.endGroup("flee");
	}

	private void fleeEveryone(CombatStats stats)
	{
		//DebugLogic.debug("doesn't meet minimum ("+mMinimalAttackThreshold+", flee!");
		for (Iterator i = stats.ours.iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			ship.setToDo(false);
		}
		//DebugLogic.endGroup("flee");
	}

	private void fleeDamaged(CombatStats stats)
	{
		//DebugLogic.debug("fleeDamagedOnly");
		for (Iterator i = stats.ours.iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ship.isDamaged())
				ship.setToDo(false);
		}
		//DebugLogic.endGroup("flee");
	}

	private void fleeNonAttackers(CombatStats stats)
	{
		for (Iterator i = stats.ours.iterator(); i.hasNext(); )
		{
			ShipInst ship = (ShipInst)i.next();
			if (ShipLogic.getAttack(ship) == 0)
			{
				//DebugLogic.debug("noAttackFlee ship='"+ship.getShip().getName()+"'");
				ship.setToDo(false);
				i.remove(); 
			} 
		}
	}

}
