package jo.ttg.lbb.logic.ship2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;

public class PhaseLaserReturnFireLogic
{
	public static void startPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips())
			for (Ship2HardpointInstance hp : ship.getTurrets())
				hp.setTarget(null);
	}
	
	public static void setTarget(Combat combat, Ship2Instance firingShip, Ship2Instance targetShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_LASER_RETURN_FIRE, Combat.PHASE_NATIVE_LASER_RETURN_FIRE);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateFuel(firingShip);
		CombatLogic.validatePower(firingShip);
		CombatLogic.validateOffShip(combat, targetShip);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_RETURN_FIRE);
		if (!firingShip.getFiredUponBy().contains(targetShip))
			throw new IllegalArgumentException(firingShip.getName()+" was not fired upon by "+targetShip.getName());
		// TODO: Detect Range?
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
			hp.setTarget(targetShip);
	}
	
	public static void setTarget(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret, Ship2Instance targetShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_LASER_FIRE, Combat.PHASE_NATIVE_LASER_FIRE);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateFuel(firingShip);
		CombatLogic.validatePower(firingShip);
		CombatLogic.validateOffShip(combat, targetShip);
		CombatLogic.validateTurret(firingShip, firingTurret);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_RETURN_FIRE);
		if (!firingShip.getFiredUponBy().contains(targetShip))
			throw new IllegalArgumentException(firingShip.getName()+" was not fired upon by "+targetShip.getName());
		Set<Ship2Instance> targets = new HashSet<Ship2Instance>();
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
			if ((hp != firingTurret) && (hp.getTarget() != null))
					targets.add(hp.getTarget());
		if (!targets.contains(targetShip))
		{
			int allowedTargets = CombatLogic.getMultiTarget(combat, firingShip);
			if (allowedTargets <= targets.size())
				throw new IllegalArgumentException(firingShip.getName()+" can only fire at a maximum of "+allowedTargets+" target(s)");
		}
		firingTurret.setTarget(targetShip);
	}
	
	public static void endPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips())
		{
			if (Ship2Logic.isOutOfFuel(ship))
				continue;
			if (ship.getPowNow() == ' ')
				continue;
			for (Ship2HardpointInstance hp : ship.getTurrets())
				if (hp.getTarget() != null)
					PhaseLaserFireLogic.fire(combat, ship, hp);
		}
	}
    
    public static List<Ship2Instance> whoCanSetTarget(Combat combat)
    {
        List<Ship2Instance> ships = new ArrayList<Ship2Instance>();
        for (Ship2Instance ship : CombatLogic.getSide(combat).getShips())
            if (canSetTarget(combat, ship))
                ships.add(ship);
        return ships;
    }
    
    public static boolean canSetTarget(Combat combat, Ship2Instance firingShip)
    {
        if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_LASER_RETURN_FIRE, Combat.PHASE_NATIVE_LASER_RETURN_FIRE))
            return false;
        if (!CombatLogic.isShip(combat, firingShip))
            return false;
        if (!CombatLogic.isFuel(firingShip))
            return false;
        if (!CombatLogic.isPower(firingShip))
            return false;
        if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET))
            return false;
        if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_RETURN_FIRE))
            return false;
        // TODO: Detect Range?
        return true;
    }
    
    public static List<Ship2HardpointInstance> canSetTargetHardpoints(Combat combat, Ship2Instance firingShip)
    {
        return canSetTargetHardpoints(combat, firingShip, null);
    }
    
    public static List<Ship2HardpointInstance> canSetTargetHardpoints(Combat combat, Ship2Instance firingShip, Ship2Instance targetShip)
    {
        List<Ship2HardpointInstance> ships = new ArrayList<Ship2HardpointInstance>();
        if (canSetTarget(combat, firingShip))
            for (Ship2HardpointInstance hp : firingShip.getTurrets())
                if (canSetTargetHardpoint(combat, firingShip, hp, targetShip))
                    ships.add(hp);
        return ships;
    }
    
    public static boolean canSetTargetHardpoint(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret)
    {
        return canSetTargetHardpoint(combat, firingShip, firingTurret, null);
    }
    
    public static boolean canSetTargetHardpoint(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret, Ship2Instance targetShip)
    {
        if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_LASER_RETURN_FIRE, Combat.PHASE_NATIVE_LASER_RETURN_FIRE))
            return false;
        if (!CombatLogic.isShip(combat, firingShip))
            return false;
        if (!CombatLogic.isFuel(firingShip))
            return false;
        if (!CombatLogic.isPower(firingShip))
            return false;
        if (!CombatLogic.isTurret(firingShip, firingTurret))
            return false;
        if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET))
            return false;
        if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_RETURN_FIRE))
            return false;
        if (targetShip != null)
        {
            Set<Ship2Instance> targets = new HashSet<Ship2Instance>();
            for (Ship2HardpointInstance hp : firingShip.getTurrets())
                if ((hp != firingTurret) && (hp.getTarget() != null))
                        targets.add(hp.getTarget());
            if (!targets.contains(targetShip))
            {
                int allowedTargets = CombatLogic.getMultiTarget(combat, firingShip);
                if (allowedTargets <= targets.size())
                    return false;
            }
        }
        return true;
    }
}
