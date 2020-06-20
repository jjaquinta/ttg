package ttg.war.view.ai.handler;

import java.util.ArrayList;
import java.util.List;

import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.ShipLogic;

public class CombatStats
{
	int ourFullAttack;
	int ourFullDefense;
	int ourDamagedAttack;
	int ourDamagedDefense;
	int ourAttack;
	int ourDefense;
	List<ShipInst> ours;
	List<ShipInst> ourFull;
	List<ShipInst> ourDamaged;
	int theirAttack;
	int theirDefense;
	List<ShipInst> theirs;
	
	public CombatStats(WorldInst world, SideInst side)
	{	
        init();
		for (ShipInst ship : world.getShips())
			setup(ship, side);
		ourAttack = ourFullAttack + ourDamagedAttack;
		ourDefense = ourFullDefense + ourDamagedDefense;
	}

    private void init()
    {
        ourFullAttack = 0;
        ourFullDefense = 0;
        ourDamagedAttack = 0;
        ourDamagedDefense = 0;
        ours = new ArrayList<>();
		ourFull = new ArrayList<>();
		ourDamaged = new ArrayList<>();
        theirAttack = 0;
        theirDefense = 0;
        theirs = new ArrayList<>();
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
