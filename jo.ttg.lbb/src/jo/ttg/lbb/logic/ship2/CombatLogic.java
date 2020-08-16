package jo.ttg.lbb.logic.ship2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatMessage;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Missile;
import jo.ttg.lbb.data.ship2.Planet;
import jo.ttg.lbb.data.ship2.Scenario;
import jo.ttg.lbb.data.ship2.ScenarioSide;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.util.geom3d.Point3D;
import jo.util.utils.obj.StringUtils;

public class CombatLogic
{
	private static Map<String, Combat> COMBATS = new HashMap<String, Combat>();
	
	public static Combat getCombat(String id)
	{
		return COMBATS.get(id);
	}
	
	public static List<Combat> getCombats()
	{
	    List<Combat> combats = new ArrayList<Combat>();
	    combats.addAll(COMBATS.values());
	    Collections.sort(combats, new Comparator<Combat>(){
	        @Override
	        public int compare(Combat o1, Combat o2)
	        {
	            return o1.getName().compareTo(o2.getName());
	        }
	    });
	    return combats;
	}
	
	public static Combat newCombat(String scenarioID, String name)
	{
		Scenario scenario = ScenarioLogic.getScenario(scenarioID);
		if (scenario == null)
			throw new IllegalArgumentException("Unknown scenario="+scenarioID);
		Combat combat = new Combat();
		combat.setID("cbt"+System.currentTimeMillis());
		if (StringUtils.isTrivial(name))
		    combat.setName(scenario.getName());
		else
		    combat.setName(name);
		combat.setScenario(scenario);
		combat.setTurn(0);
		combat.setPhase(0);
		for (ScenarioSide ss : scenario.getSides())
			combat.getSides().add(newSide(ss));
		PhaseLogic.kickOff(combat);
		return combat;
	}
	
	public static void done(Combat combat)
	{
	    COMBATS.remove(combat.getID());
	}

	private static CombatSide newSide(ScenarioSide ss)
	{
		CombatSide cs = new CombatSide();
		cs.setScenarioSide(ss);
		cs.setName(ss.getName());
		for (Ship2Instance ship : ss.getShips())
			try { cs.getShips().add((Ship2Instance)ship.clone()); } catch (CloneNotSupportedException e) { }
		return cs;
	}
	
	private static int getSideOffset(Combat combat)
	{
		int phase = combat.getPhase();
		switch (phase)
		{
			case Combat.PHASE_INTRUDER_MOVEMENT:
				return 0;
			case Combat.PHASE_INTRUDER_LASER_FIRE:
				return 0;
			case Combat.PHASE_NATIVE_LASER_RETURN_FIRE:
				return 1;
			case Combat.PHASE_INTRUDER_ORDANCE_LAUNCH:
				return 0;
			case Combat.PHASE_INTRUDER_COMPUTER_REPROGRAM:
				return 0;
			case Combat.PHASE_NATIVE_MOVEMENT:
				return 1;
			case Combat.PHASE_NATIVE_LASER_FIRE:
				return 1;
			case Combat.PHASE_INTRUDER_LASER_RETURN_FIRE:
				return 0;
			case Combat.PHASE_NATIVE_ORDANCE_LAUNCH:
				return 1;
			case Combat.PHASE_NATIVE_COMPUTER_REPROGRAM:
				return 1;
			case Combat.PHASE_INTERPHASE:
				return 1;			
		}
		return -1;
	}
	
	public static CombatSide getSide(Combat combat)
	{
		int side = getSideOffset(combat);
		if (side >= 0)
			return combat.getSides().get(side);
		return null;
	}
	
	public static CombatSide getOffSide(Combat combat)
	{
		int side = getSideOffset(combat);
		if (side >= 0)
			return combat.getSides().get(1 - side);
		return null;
	}
	
	public static CombatSide getSide(Combat combat, Ship2Instance ship)
	{
		for (CombatSide side : combat.getSides())
			if (side.getShips().contains(ship))
				return side;
		return null;
	}
	
	public static String getPhaseName(Combat combat)
	{
		return Combat.PHASE_NAMES[combat.getPhase()];
	}
	
	public static String getTranslatedPhaseName(Combat combat)
	{
		String name = getPhaseName(combat);
		name = name.replace("Native", combat.getSides().get(1).getName());
		name = name.replace("Intruder", combat.getSides().get(0).getName());
		return name;
	}

	public static Point3D[] getBounds(Combat combat)
	{
		Point3D[] bounds = new Point3D[2];
		for (CombatSide side : combat.getSides())
		{
			for (Ship2Instance ship : side.getShips())
			{
				extendBounds(bounds, ship.getLocation(), ship.getVelocity());
				for (Point3D p : ship.getPastMovement())
					extendBounds(bounds, p, null);
			}
			for (Missile m : side.getMissiles())
				extendBounds(bounds, m.getLocation(), m.getVelocity());
		}
		for (Planet p : combat.getScenario().getPlanets())
			extendBounds(bounds, p.getLocation(), p.getVelocity());
		return bounds;
	}
	
    private static void extendBounds(Point3D[] bounds, Point3D location,
			Point3D velocity)
	{
		double m = 0;
		if (velocity != null)
			m = velocity.mag();
		if (bounds[0] == null)
		{
			bounds[0] = new Point3D(location);
			bounds[0].decr(new Point3D(m, m, m));
		}
		else
		{
			bounds[0].x = Math.min(bounds[0].x, location.x - m);
			bounds[0].y = Math.min(bounds[0].y, location.y - m);
			bounds[0].z = Math.min(bounds[0].z, location.z - m);
		}
		if (bounds[1] == null)
		{
			bounds[1] = new Point3D(location);
			bounds[1].incr(new Point3D(m, m, m));
		}
		else
		{
			bounds[1].x = Math.max(bounds[1].x, location.x + m);
			bounds[1].y = Math.max(bounds[1].y, location.y + m);
			bounds[1].z = Math.max(bounds[1].z, location.z + m);
		}
	}

	public static boolean isShipExists(Combat combat, Ship2Instance ship)
    {
        if (ship == null)
            return false;
        for (CombatSide side : combat.getSides())
            if (side.getShips().contains(ship))
                return true;
        return false;
    }
    
    public static boolean isShip(Combat combat, Ship2Instance ship)
    {
        CombatSide side = getSide(combat);
        if (side == null)
            return false;
        if (!side.getShips().contains(ship))
            return false;
        return true;
    }

    public static boolean isPhase(Combat combat, int phase1, int phase2)
    {
        if ((combat.getPhase() != phase1) && (combat.getPhase() != phase2))
            return false;
        return true;
    }

    public static boolean isFuel(Ship2Instance ship)
    {
        if (Ship2Logic.isOutOfFuel(ship))
            return false;
        return true;
    }

    public static boolean isPower(Ship2Instance ship)
    {
        if (ship.getPowNow() == ' ')
            return false;
        return true;
    }

    public static boolean isTurret(Ship2Instance ship, Ship2HardpointInstance turret)
    {
        if (!ship.getTurrets().contains(turret))
            return false;
        return true;
    }

    public static boolean isProgramLoaded(Ship2Instance ship, String program)
    {
        if (!ship.getLoadedPrograms().contains(program))
            return false;
        return true;
    }
	
	public static void validateShip(Combat combat, Ship2Instance ship)
	{
		CombatSide side = getSide(combat);
		if (side == null)
			throw new IllegalArgumentException("Cannot operate on "+ship.getName()+" during "+getPhaseName(combat));
		if (!side.getShips().contains(ship))
			throw new IllegalArgumentException("Ship "+ship.getName()+" does not belong to phasing side "+side.getName());
	}
	
	public static void validateOffShip(Combat combat, Ship2Instance ship)
	{
		CombatSide side = getOffSide(combat);
		if (side == null)
			throw new IllegalArgumentException("Cannot operate on "+ship.getName()+" during "+getPhaseName(combat));
		if (!side.getShips().contains(ship))
			throw new IllegalArgumentException("Ship "+ship.getName()+" does not belong to non-phasing side "+side.getName());
	}

	public static void validatePhase(Combat combat, int phase1, int phase2)
	{
		if ((combat.getPhase() != phase1) && (combat.getPhase() != phase2))
			throw new IllegalStateException("You cannot conduct this operation during "+getTranslatedPhaseName(combat)+" phase");
	}

	public static void validateTurret(Ship2Instance ship, Ship2HardpointInstance turret)
	{
		if (!ship.getTurrets().contains(turret))
			throw new IllegalArgumentException("Ship "+ship.getName()+" does not contain turret");
	}

	public static void validateProgramLoaded(Ship2Instance ship, String program)
	{
		if (!ship.getLoadedPrograms().contains(program))
			throw new IllegalArgumentException("Ship "+ship.getName()+" is not running '"+program+"'");
	}

	public static void validateProgramStored(Ship2Instance ship, String program)
	{
		if (!ship.getStoredPrograms().contains(program))
			throw new IllegalArgumentException("Ship "+ship.getName()+" is not storing '"+program+"'");
	}

	public static void validateFuel(Ship2Instance ship)
	{
		if (Ship2Logic.isOutOfFuel(ship))
			throw new IllegalArgumentException("Ship "+ship.getName()+" has no fuel.");
	}

	public static void validatePower(Ship2Instance ship)
	{
		if (ship.getPowNow() == ' ')
			throw new IllegalArgumentException("Ship "+ship.getName()+" has no power.");
	}

	public static boolean computerOperates(Combat combat, Ship2Instance ship)
	{
		int roll = CombatLogic.D(combat, 2) - ship.getCompHits();
		return roll >= 1;
	}
	
	public static int getMultiTarget(Combat combat, Ship2Instance ship)
	{
		if (computerOperates(combat, ship))
		{
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MULTI_TARGET_4))
				return 4;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MULTI_TARGET_3))
				return 3;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MULTI_TARGET_2))
				return 2;
		}
		return 1;
	}

	public static int getPredict(Combat combat, Ship2Instance ship)
	{
		if (computerOperates(combat, ship))
		{
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_PREDICT_5))
				return 3;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_PREDICT_4))
				return 3;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_PREDICT_3))
				return 2;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_PREDICT_2))
				return 2;
			if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_PREDICT_1))
				return 1;
		}
		return 0;
	}

	public static int getEvade(Ship2Instance ship)
	{
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_6))
			return -5;
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_5))
			return -ship.getPilotSkill();
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_4))
			return -ship.getPilotSkill();
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_3))
			return -ship.getPilotSkill()*3/4;
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_2))
			return -ship.getPilotSkill()/2;
		if (ship.getLoadedPrograms().contains(Ship2Instance.PROGRAM_MAN_EVA_1))
			return -ship.getPilotSkill()/4;
		return 0;
	}
	
	public static int D(Combat combat, int num)
	{
		int tot = num;
		while (num-- > 0)
			tot += combat.getRND().nextInt(6);
		return tot;
	}
	
	public static void addMessage(Combat combat, String msg, int priority, Object filter1, Object filter2)
	{
		CombatMessage m = new CombatMessage();
		m.setMessage(msg);
		m.setPriority(priority);
		m.setPhase(combat.getPhase());
		m.setTurn(combat.getTurn());
		CombatSide side = getSide(combat);
		if (side != null)
			m.getFilters().add(side);
		if (filter1 != null)
			m.getFilters().add(filter1);
		if (filter2 != null)
			m.getFilters().add(filter2);
		combat.getMessages().add(m);
		combat.fireMonotonicPropertyChange("messages");
	}
	
	public static void addMessage(Combat combat, String msg, int priority, Object filter1)
	{
		addMessage(combat, msg, priority, filter1, null);
	}
	
	public static void addMessageHigh(Combat combat, String msg, Object filter1)
	{
		addMessage(combat, msg, CombatMessage.HIGH, filter1, null);
	}
	
	public static void addMessageMedium(Combat combat, String msg, Object filter1)
	{
		addMessage(combat, msg, CombatMessage.MEDIUM, filter1, null);
	}
	
	public static void addMessageLow(Combat combat, String msg, Object filter1)
	{
		addMessage(combat, msg, CombatMessage.LOW, filter1, null);
	}
}
