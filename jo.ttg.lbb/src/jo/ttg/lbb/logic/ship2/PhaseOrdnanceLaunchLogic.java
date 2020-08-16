package jo.ttg.lbb.logic.ship2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Missile;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;

public class PhaseOrdnanceLaunchLogic
{
	public static void startPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips())
		{
			for (Ship2HardpointInstance hp : ship.getTurrets())
			{
				hp.setTarget(null);
				hp.setFireSand(false);
				hp.setGunnerReloading(false);
			}
		}
	}
	
	public static void setTarget(Combat combat, Ship2Instance firingShip, Ship2Instance targetShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET);
		CombatLogic.validateOffShip(combat, targetShip);		
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (hp.getDesign().getMissiles() == 0)
				continue;
			hp.setTarget(targetShip);
			hp.setGunnerReloading(false);
		}
	}
	
	public static void setTarget(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret, Ship2Instance targetShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET);
		CombatLogic.validateOffShip(combat, targetShip);
		CombatLogic.validateTurret(firingShip, firingTurret);
		if (firingTurret.getDesign().getMissiles() == 0)
			return;
		if (firingTurret.getMissiles() == 0)
			return;
		firingTurret.setTarget(targetShip);
		firingTurret.setGunnerReloading(false);
	}
	
	public static void setFireSand(Combat combat, Ship2Instance firingShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH);
		CombatLogic.validateShip(combat, firingShip);
		CombatLogic.validateProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH);
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (!canFireSand(combat, firingShip, hp))
				continue;
			hp.setFireSand(true);
			hp.setGunnerReloading(false);
		}
	}
	
	public static void setFireSand(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret)
	{
		if (!canFireSand(combat, firingShip, firingTurret))
			return;
		firingTurret.setFireSand(true);
		firingTurret.setGunnerReloading(false);
	}
	
	public static void setReload(Combat combat, Ship2Instance firingShip)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH);
		CombatLogic.validateShip(combat, firingShip);
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (!canReload(combat, firingShip, hp))
				continue;
			hp.setTarget(null);
			hp.setFireSand(false);
			hp.setGunnerReloading(true);
		}
	}
	
	public static void setReload(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret)
	{
		if (!canReload(combat, firingShip, firingTurret))
			return;
		firingTurret.setTarget(null);
		firingTurret.setFireSand(false);
		firingTurret.setGunnerReloading(true);
	}
	
	public static void endPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		detonateMissiles(combat, side);
		fireTurrets(combat, side);
	}

	private static void fireTurrets(Combat combat, CombatSide side)
	{
		for (Ship2Instance ship : side.getShips())
		{
			for (Ship2HardpointInstance hp : ship.getTurrets())
				if (hp.getTarget() != null)
					fire(combat, ship, hp);
		}
	}

	private static void detonateMissiles(Combat combat, CombatSide side)
	{
		for (Iterator<Missile> i = side.getMissiles().iterator(); i.hasNext(); )
		{
			Missile m = i.next();
			if (!CombatLogic.isShipExists(combat, m.getTarget()))
			{
				if (m.getTarget() != null)
					CombatLogic.addMessageLow(combat,  "Missle loses target "+m.getTarget().getName(), m);
				else
					CombatLogic.addMessageLow(combat,  "Missle loses target", m);
				i.remove();
			}
			else
			{
				double d = m.getLocation().dist(m.getTarget().getLocation());
				if (d < 5)
				{
					CombatLogic.addMessageMedium(combat, "Missles hits "+m.getTarget().getName(), m.getTarget());
					int damage = CombatLogic.D(combat, 1);
					while (damage-- > 0)
						DamageLogic.hit(combat, m.getTarget());
					i.remove();
				}
				else
				{
					m.setTurns(m.getTurns() - 1);
					if (m.getTurns() == 0)
					{
						CombatLogic.addMessageLow(combat, "Missles fails to reach "+m.getTarget().getName(), m.getTarget());
						i.remove();
					}
				}
			}
		}
	}

	private static void fire(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent)
	{
		if (firingTurrent.isGunnerReloading())
			reload(combat, firingShip, firingTurrent);
		else
		{
			if (firingTurrent.isFireSand())
				fireSand(combat, firingShip, firingTurrent);
			if (firingTurrent.getTarget() != null)
				fireMissile(combat, firingShip, firingTurrent);
		}
	}
	
	private static void reload(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent)
	{
		if ((firingTurrent.getDesign().getMissiles() == 0) && (firingTurrent.getDesign().getSandcasters() == 0))
			return;
		int toLoadMissles = Math.max(firingTurrent.getDesign().getMissiles()*3 - firingTurrent.getMissiles(), firingShip.getMissiles());
		if (toLoadMissles > 0)
		{
			CombatLogic.addMessageLow(combat, firingShip.getName()+": loads "+toLoadMissles+" missile(s)", firingShip);
			firingShip.setMissiles(firingShip.getMissiles() - toLoadMissles);
			firingTurrent.setMissiles(firingTurrent.getMissiles() + toLoadMissles);
		}
		int toLoadSand = Math.max(firingTurrent.getDesign().getSandcasters()*3 - firingTurrent.getSand(), firingShip.getSands());
		if (toLoadSand > 0)
		{
			CombatLogic.addMessageLow(combat, firingShip.getName()+": loads "+toLoadSand+" sand", firingShip);
			firingShip.setSands(firingShip.getSands() - toLoadSand);
			firingTurrent.setSand(firingTurrent.getSand() + toLoadSand);
		}
	}
	
	private static void fireSand(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent)
	{
		if ((firingTurrent.getDesign().getSandcasters() == 0))
			return;
		if ((firingTurrent.getSand() == 0))
			return;
		int toFire = Math.min(firingTurrent.getDesign().getSandcasters(), firingTurrent.getSand());
		firingTurrent.setSand(firingTurrent.getSand() - toFire);
		CombatLogic.addMessageMedium(combat, firingShip.getName()+", "+firingTurrent.getName()+": fires "+toFire+" sand", firingShip);
		firingShip.setSandHalo(firingShip.getSandHalo() + 15);
	}
	
	private static void fireMissile(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurrent)
	{
		if ((firingTurrent.getDesign().getMissiles() == 0))
			return;
		if ((firingTurrent.getMissiles() == 0))
			return;
		int toFire = Math.min(firingTurrent.getDesign().getMissiles(), firingTurrent.getMissiles());
		firingTurrent.setMissiles(firingTurrent.getMissiles() - toFire);
		CombatSide side = CombatLogic.getSide(combat);
		CombatLogic.addMessageMedium(combat, firingShip.getName()+", "+firingTurrent.getName()+": launches "+toFire+" missile(s)", firingShip);
		for (int i = 0; i < toFire; i++)
		{
			firingShip.setTotalMissilesFired(firingShip.getTotalMissilesFired() + 1);
			Missile m = new Missile();
			m.setName(firingShip.getName()+" #"+firingShip.getTotalMissilesFired());
			m.getLocation().set(firingShip.getLocation());
			m.getVelocity().set(firingShip.getVelocity());
			m.setTarget(firingTurrent.getTarget());
			m.setTurns(6);
			side.getMissiles().add(m);
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

    public static List<Ship2Instance> whoCanFireSand(Combat combat)
    {
        List<Ship2Instance> ships = new ArrayList<Ship2Instance>();
        for (Ship2Instance ship : CombatLogic.getSide(combat).getShips())
            if (canFireSand(combat, ship))
                ships.add(ship);
        return ships;
    }    

    public static List<Ship2Instance> whoCanReload(Combat combat)
    {
        List<Ship2Instance> ships = new ArrayList<Ship2Instance>();
        for (Ship2Instance ship : CombatLogic.getSide(combat).getShips())
            if (canReload(combat, ship))
                ships.add(ship);
        return ships;
    }    
	
	public static boolean canSetTarget(Combat combat, Ship2Instance firingShip)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET))
			return false;
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (hp.getDesign().getMissiles() == 0)
				continue;
			if (hp.getMissiles() == 0)
				continue;
			return true;
		}
		return false;
	}
	
	public static boolean canSetTarget(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret, Ship2Instance targetShip)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_TARGET))
			return false;
		if (!CombatLogic.isTurret(firingShip, firingTurret))
			return false;
		if (firingTurret.getDesign().getMissiles() == 0)
			return false;
		if (firingTurret.getMissiles() == 0)
			return false;
		return true;
	}
	
	public static boolean canFireSand(Combat combat, Ship2Instance firingShip)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH))
			return false;
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (hp.getDesign().getSandcasters() == 0)
				continue;
			if (hp.getSand() == 0)
				continue;
			return true;
		}
		return false;
	}
	
	public static boolean canFireSand(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		if (!CombatLogic.isProgramLoaded(firingShip, Ship2Instance.PROGRAM_LAUNCH))
			return false;
		if (!CombatLogic.isTurret(firingShip, firingTurret))
			return false;
		if (firingTurret.getDesign().getSandcasters() == 0)
			return false;
		if (firingTurret.getSand() == 0)
			return false;
		return true;
	}
	
	public static boolean canReload(Combat combat, Ship2Instance firingShip)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		for (Ship2HardpointInstance hp : firingShip.getTurrets())
		{
			if (hp.getDesign().getMissiles() > 0)
			{
				if (hp.getMissiles() < hp.getDesign().getMissiles()*3)
					if (firingShip.getMissiles() > 0)
						return true;
			}
			if (hp.getDesign().getSandcasters() > 0)
			{
				if (hp.getSand() < hp.getDesign().getSandcasters()*3)
					if (firingShip.getSands() > 0)
						return true;
			}
		}
		return false;
	}
	
	public static boolean canReload(Combat combat, Ship2Instance firingShip, Ship2HardpointInstance firingTurret)
	{
		if (!CombatLogic.isPhase(combat, Combat.PHASE_INTRUDER_ORDANCE_LAUNCH, Combat.PHASE_NATIVE_ORDANCE_LAUNCH))
			return false;
		if (!CombatLogic.isShip(combat, firingShip))
			return false;
		if (!CombatLogic.isTurret(firingShip, firingTurret))
			return false;
		if (firingTurret.getDesign().getMissiles() > 0)
		{
			if (firingTurret.getMissiles() < firingTurret.getDesign().getMissiles()*3)
				if (firingShip.getMissiles() > 0)
					return true;
		}
		if (firingTurret.getDesign().getSandcasters() > 0)
		{
			if (firingTurret.getSand() < firingTurret.getDesign().getSandcasters()*3)
				if (firingShip.getSands() > 0)
					return true;
		}
		return false;
	}
}
