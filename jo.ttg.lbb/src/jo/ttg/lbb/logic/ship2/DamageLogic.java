package jo.ttg.lbb.logic.ship2;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Ship2Instance;

public class DamageLogic
{
	private static final int HIT_POWERPLANT = 0;
	private static final int HIT_MANEUVER = 1;
	private static final int HIT_JUMP = 2;
	private static final int HIT_FUEL = 3;
	private static final int HIT_HULL = 4;
	private static final int HIT_HOLD = 5;
	private static final int HIT_COMPUTER = 6;
	private static final int HIT_TURRET = 7;
	private static final int HIT_CRITICAL = 8;
	private static final int HIT_CABIN = 9;
	private static final int HIT_CREW = 10;
	private static final int HIT_EXPLODE = 11;
	
	private static final int HIT_LOCATIONS[][] = {
		{ HIT_MANEUVER, HIT_MANEUVER, HIT_MANEUVER, HIT_MANEUVER, HIT_CABIN, HIT_COMPUTER, HIT_CABIN, HIT_CABIN, HIT_TURRET, HIT_TURRET, HIT_CRITICAL },
		{ HIT_POWERPLANT, HIT_MANEUVER, HIT_MANEUVER, HIT_FUEL, HIT_HULL, HIT_HULL, HIT_HOLD, HIT_COMPUTER, HIT_TURRET, HIT_TURRET, HIT_CRITICAL },
		{ HIT_POWERPLANT, HIT_MANEUVER, HIT_JUMP, HIT_FUEL, HIT_HULL, HIT_HULL, HIT_HOLD, HIT_COMPUTER, HIT_TURRET, HIT_TURRET, HIT_CRITICAL },
	};
	
	private static final int CRITICAL_HIT_LOCATIONS[][] = {
		{ HIT_MANEUVER, HIT_MANEUVER, HIT_MANEUVER, HIT_CREW, HIT_COMPUTER, HIT_EXPLODE },
		{ HIT_POWERPLANT, HIT_MANEUVER, HIT_MANEUVER, HIT_CREW, HIT_COMPUTER, HIT_EXPLODE },
		{ HIT_POWERPLANT, HIT_MANEUVER, HIT_JUMP, HIT_CREW, HIT_COMPUTER, HIT_EXPLODE },
	};
	
	public static void hit(Combat combat, Ship2Instance ship)
	{
		int type = Ship2Logic.getShipType(ship.getDesign());
		int hit = HIT_LOCATIONS[type][CombatLogic.D(combat, 2) - 2];
		boolean effect = false;
		switch (hit)
		{
			case HIT_POWERPLANT:
                if (ship.getPowNow() >= 'A')
                {
                    ship.setPowNow(decrement(ship.getPowNow()));
                    CombatLogic.addMessageHigh(combat, ship.getName()+": power plant hit.", ship);
                    effect = true;
                }
				break;
			case HIT_MANEUVER:
                if (ship.getManNow() >= 'A')
                {
                    ship.setManNow(decrement(ship.getManNow()));
                    CombatLogic.addMessageHigh(combat, ship.getName()+": maneuver drive hit.", ship);
                    effect = true;
                }
				break;
			case HIT_JUMP:
                if (ship.getJumpNow() >= 'A')
                {
                    ship.setJumpNow(decrement(ship.getJumpNow()));
                    CombatLogic.addMessageHigh(combat, ship.getName()+": jump drive hit.", ship);
                    effect = true;
                }
				break;
			case HIT_FUEL:
				if (Ship2Logic.isOutOfFuel(ship))
				{
	                CombatLogic.addMessageMedium(combat, ship.getName()+": fuel hit.", ship);
	                ship.setFuelHits(ship.getFuelHits()+1);
	                effect = true;
				}
				break;
			case HIT_HULL:
                CombatLogic.addMessageLow(combat, ship.getName()+": hull hit.", ship);
                ship.setHullHits(ship.getHullHits()+1);
                effect = true;
				break;
			case HIT_HOLD:
                ship.setHoldHits(ship.getHoldHits()+1);
                CombatLogic.addMessageLow(combat, ship.getName()+": hold hit.", ship);
                effect = true;
                // TODO: damage subsidiary craft
				break;
			case HIT_COMPUTER:
                ship.setCompHits(ship.getCompHits()+1);
                CombatLogic.addMessageHigh(combat, ship.getName()+": computer hit.", ship);
                effect = true;
				break;
			case HIT_TURRET:
                int which = combat.getRND().nextInt(ship.getTurrets().size());
                if (!ship.getTurrets().get(which).isDamage())
                {
                	ship.getTurrets().get(which).setDamage(true);
	                CombatLogic.addMessageHigh(combat, ship.getName()+": turret "+(which+1)+" hit.", ship);
                    effect = true;
                }
				break;
			case HIT_CRITICAL:
                effect = hitCritical(combat, ship, type);
				break;
		}
		if (effect == false)
			CombatLogic.addMessageLow(combat, ship.getName()+": hit has no effect", ship);
	}
	
	public static boolean hitCritical(Combat combat, Ship2Instance ship, int type)
	{
		int hit = CRITICAL_HIT_LOCATIONS[type][CombatLogic.D(combat, 1) - 1];
        boolean effect = false;
		switch (hit)
		{
			case HIT_POWERPLANT:
                if (ship.getPowNow() >= 'A')
                {
                    ship.setPowNow(' ');
                    CombatLogic.addMessageHigh(combat, ship.getName()+": power plant destroyed!", ship);
                    effect = true;
                }
				break;
			case HIT_MANEUVER:
                if (ship.getManNow() >= 'A')
                {
                    ship.setManNow(' ');
                    CombatLogic.addMessageHigh(combat, ship.getName()+": maneuver drive destroyed!", ship);
                    effect = true;
                }
				break;
			case HIT_JUMP:
                if (ship.getJumpNow() >= 'A')
                {
                    ship.setJumpNow(' ');
                    CombatLogic.addMessageHigh(combat, ship.getName()+": jump drive destroyed!", ship);
                    effect = true;
                }
				break;
			case HIT_CREW:
                ship.setCrewHits(ship.getCrewHits()+1);
                CombatLogic.addMessageHigh(combat, ship.getName()+": crew killed!", ship);
                effect = true;
				break;
			case HIT_COMPUTER:
                ship.setCompHits(ship.getCompHits() + 1000);
                CombatLogic.addMessageHigh(combat, ship.getName()+": computer destroyed!", ship);
                effect = true;
				break;
			case HIT_EXPLODE:
                CombatLogic.addMessageHigh(combat, ship.getName()+": ship explodes!", ship);
                CombatSide side = CombatLogic.getSide(combat, ship);
                if (side != null)
                	side.getShips().remove(ship);
                effect = true;
				break;
		}
		return effect;
	}

    private static char decrement(char val)
    {
        val--;
        if (val == 'O')
            val--;
        else if (val == 'I')
            val--;
        return val;
    }

}
