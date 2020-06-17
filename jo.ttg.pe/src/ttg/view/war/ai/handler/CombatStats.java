package ttg.view.war.ai.handler;

import java.util.ArrayList;
import java.util.Iterator;

import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;
import ttg.logic.war.ShipLogic;

public class CombatStats
{
	int ourFullAttack;
	int ourFullDefense;
	int ourDamagedAttack;
	int ourDamagedDefense;
	int ourAttack;
	int ourDefense;
	ArrayList ours;
	ArrayList ourFull;
	ArrayList ourDamaged;
	int theirAttack;
	int theirDefense;
	ArrayList theirs;
	
	public CombatStats(WorldInst world, SideInst side)
	{	
        init();
		for (Iterator i = world.getShips().iterator(); i.hasNext(); )
			setup((ShipInst)i.next(), side);
		ourAttack = ourFullAttack + ourDamagedAttack;
		ourDefense = ourFullDefense + ourDamagedDefense;
	}

    private void init()
    {
        ourFullAttack = 0;
        ourFullDefense = 0;
        ourDamagedAttack = 0;
        ourDamagedDefense = 0;
        ours = new ArrayList();
		ourFull = new ArrayList();
		ourDamaged = new ArrayList();
        theirAttack = 0;
        theirDefense = 0;
        theirs = new ArrayList();
    }

	private void setup(ShipInst ship, SideInst side)
	{
		if (ship.isFleeing())
			return;
		if (ship.getSideInst() != side)
		{
			theirs.add(ship);
			theirAttack += ShipLogic.getAttack(ship);
			theirDefense += ShipLogic.getDefense(ship);
		}
		else
		{
			ours.add(ship);
			if (ship.isDamaged())
			{
				ourDamagedAttack += ShipLogic.getAttack(ship);
				ourDamagedDefense += ShipLogic.getDefense(ship);
				ourDamaged.add(ship);
			}
			else
			{
				ourFullAttack += ShipLogic.getAttack(ship);
				ourFullDefense += ShipLogic.getDefense(ship);
				ourFull.add(ship);
			}
		}
	}
}
