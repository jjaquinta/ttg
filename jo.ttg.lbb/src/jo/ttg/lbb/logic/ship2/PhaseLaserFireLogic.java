package jo.ttg.lbb.logic.ship2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatMessage;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.util.geom3d.Point3DLogic;

public class PhaseLaserFireLogic
{
	public static void startPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips())
		{
			for (Ship2HardpointInstance hp : ship.getTurrets())
				hp.setTarget(null);
		}
		side = CombatLogic.getOffSide(combat);
		for (Ship2Instance ship : side.getShips())
			ship.getFiredUponBy().clear();
	}
	
	public static void setTarget(Combat combat, Ship2Instance firingShip, Ship2Instance targetShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_LASER_FIRE, Combat.PHASE_NATIVE_LASER_FIRE);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateFuel(firingShip);
		CombatLogic.validatePower(firingShip);
		CombatLogic.validateOffShip(combat, targetShip);
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
					fire(combat, ship, hp);
		}
	}

	static void fire(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent)
	{
		if ((firingTurrent.getDesign().getBeamLasers() == 0) && (firingTurrent.getDesign().getPulseLasers() == 0))
			return;
		Ship2Instance targetShip = firingTurrent.getTarget();
		targetShip.getFiredUponBy().add(firingShip);
		int attackDM = 0;
		attackDM += CombatLogic.getPredict(combat, firingShip);
		if (firingShip.getLoadedPrograms().contains(Ship2Instance.PROGRAM_GUNNER_INTERACT))
			attackDM += firingTurrent.getGunnerSkill();
		int defenseDM = 0;
		defenseDM += CombatLogic.getEvade(targetShip);
		if (targetShip.getLoadedPrograms().contains(Ship2Instance.PROGRAM_AUTO_EVADE))
			defenseDM -= 2;
		double range = Point3DLogic.dist(firingShip.getLocation(), targetShip.getLocation());
		if (range >= 5000)
			defenseDM -= 5;
		else if (range >= 2500)
			defenseDM -= 2;
		// TODO: Sandcasters
		for (int i = 0; i < firingTurrent.getDesign().getBeamLasers(); i++)
			fireGun(combat, firingShip, firingTurrent, "Beam Laser #"+(i+1), targetShip, attackDM, defenseDM);
		for (int i = 0; i < firingTurrent.getDesign().getPulseLasers(); i++)
			fireGun(combat, firingShip, firingTurrent, "Pulse Laser #"+(i+1), targetShip, attackDM, defenseDM);
	}
	
	private static void fireGun(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent,
			String firingGun, Ship2Instance targetShip, int attackDM, int defenseDM)
	{		
		int turretNum = firingShip.getTurrets().indexOf(firingTurrent)+1;
		int roll = CombatLogic.D(combat, 2) + attackDM + defenseDM;
		if (roll < 8)
		{
			CombatLogic.addMessageLow(combat, firingShip.getName()+", Turrent "+turretNum+", "+firingGun+" misses "+targetShip.getName(), firingShip);
			return; // miss
		}
		CombatLogic.addMessage(combat, firingShip.getName()+", Turrent "+turretNum+", "+firingGun+" hits "+targetShip.getName(), 
				CombatMessage.MEDIUM, firingShip, targetShip);
		DamageLogic.hit(combat, targetShip);
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
        if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_LASER_FIRE, Combat.PHASE_NATIVE_LASER_FIRE))
            return false;
        if (!CombatLogic.isShip(combat, firingShip))
            return false;
        if (!CombatLogic.isFuel(firingShip))
            return false;
        if (!CombatLogic.isPower(firingShip))
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
        if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_LASER_FIRE, Combat.PHASE_NATIVE_LASER_FIRE))
            return false;
        if (!CombatLogic.isShip(combat, firingShip))
            return false;
        if (!CombatLogic.isFuel(firingShip))
            return false;
        if (!CombatLogic.isPower(firingShip))
            return false;
        if (!CombatLogic.isTurret(firingShip, firingTurret))
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
