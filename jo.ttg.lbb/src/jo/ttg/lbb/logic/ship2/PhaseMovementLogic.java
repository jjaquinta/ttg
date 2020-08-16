package jo.ttg.lbb.logic.ship2;

import java.util.List;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Missile;
import jo.ttg.lbb.data.ship2.Planet;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Point3DLogic;
import jo.util.utils.obj.DoubleUtils;

public class PhaseMovementLogic
{
	public static void startPhase(Combat combat)
	{
		Point3D zero = new Point3D();
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips())
		{
			ship.setAcceleration(zero);
			ship.getPastMovement().add(0, new Point3D(ship.getLocation()));
		}
	}
	
	public static void setMovement(Combat combat, Ship2Instance ship, double sx, double sy, double sz)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_MOVEMENT, Combat.PHASE_NATIVE_MOVEMENT);
		CombatLogic.validateShip(combat, ship);
		if (Ship2Logic.isOutOfFuel(ship))
			throw new IllegalArgumentException("Ship: "+ship.getName()+" does not have any fuel left.");
		Point3D newLocation = new Point3D(sx, sy, sz);
		Point3D newAcceleration = newLocation.sub(ship.getLocation().add(ship.getVelocity()));
		double delta = newAcceleration.mag();
		double g = Ship2Logic.getG(ship)*100;
		if (delta > g)
			throw new IllegalArgumentException("An acceleration of "+delta+" is required to get to "+newLocation+" and "+ship.getName()+" only has one of "+g);
		ship.getAcceleration().set(newAcceleration);
	}
	
	public static void setAcceleration(Combat combat, Ship2Instance ship, double ax, double ay, double az)
	{
		CombatLogic.validatePhase(combat, Combat.PHASE_INTRUDER_MOVEMENT, Combat.PHASE_NATIVE_MOVEMENT);
		CombatLogic.validateShip(combat, ship);
		Point3D newAcceleration = new Point3D(ax, ay, az);
		double delta = newAcceleration.mag();
		double g = Ship2Logic.getG(ship)*100;
		if (DoubleUtils.greaterThan(delta, g))
			throw new IllegalArgumentException("Requested an acceleration of "+delta+", "+ship.getName()+" only has one of "+g);
		ship.getAcceleration().set(newAcceleration);
	}
	
	public static void endPhase(Combat combat)
	{
		CombatSide side = CombatLogic.getSide(combat);
		for (Ship2Instance ship : side.getShips().toArray(new Ship2Instance[0]))
			move(combat, ship, combat.getScenario().getPlanets());
		for (Missile missile : side.getMissiles().toArray(new Missile[0]))
			move(combat, missile, combat.getScenario().getPlanets());
	}

	private static void move(Combat combat, Ship2Instance ship, List<Planet> planets)
	{
		if (!DoubleUtils.equals(ship.getAcceleration().mag(), 0))
			ship.setSandHalo(0);
		boolean survive = move(combat, ship.getName(), ship, ship.getLocation(), ship.getVelocity(), ship.getAcceleration(), planets);
		if (!survive)
			CombatLogic.getSide(combat).getShips().remove(ship);
	}

	private static void move(Combat combat, Missile missile, List<Planet> planets)
	{
		Point3D acceleartion = new Point3D();
		if (missile.getTarget() != null)
			if (CombatLogic.isShipExists(combat, missile.getTarget()))
			{
				Point3D delta = missile.getTarget().getLocation().sub(missile.getLocation().add(missile.getVelocity()));
				if (delta.mag() > 600)
					delta.setMag(600);
				acceleartion.set(delta);
			}
			else
				missile.setTarget(null);
		boolean survive = move(combat, "Missile "+missile.getName(), missile, missile.getLocation(), missile.getVelocity(), acceleartion, planets);
		if (!survive)
			CombatLogic.getSide(combat).getMissiles().remove(missile);
	}

	private static boolean move(Combat combat, String name, Object obj, Point3D s, Point3D v, Point3D a, List<Planet> planets)
	{
	    //System.out.println("Moving "+name+" s="+s.toIntString()+", v="+v.toIntString()+" a="+a.toIntString());
		Point3D oldS = new Point3D(s);
		v.incr(a);
		s.incr(v);
		for (Planet p : planets)
		{
			// collision, gravity, atmospheric braking
			double d = Point3DLogic.distPointToLineSegment(p.getLocation(), oldS, s);
			double r = PlanetLogic.getRadius(p);
			if (d < r)
			{	// we've crashed into the planet!
				CombatLogic.addMessageHigh(combat, name+" "+s.toIntString()+" has crashed into "+p.getName()+" "+p.getLocation().toIntString(),
						obj);
				return false;
			}
			if (p.isAtmosphere() && (d < r + 10))
			{	// atmospheric braking
				double vmag = v.mag();
				if (vmag >= 1)
				{
					if (vmag > 10)
						vmag -= 10;
					else
						vmag = 0;
					v.setMag(vmag);
					CombatLogic.addMessageMedium(combat, name+" brakes in the atmosphere of "+p.getName(), obj);
				}
			}
			double g = PlanetLogic.getGravAt(p, d)*100;
			if (g > 1.0)
			{
				Point3D dir = p.getLocation().sub(s);
				dir.normalize();
				dir.mult(g);
				v.incr(dir);
			}
		}
		if (oldS.dist(s) > 1)
			CombatLogic.addMessageLow(combat, name+" moves to "+(int)s.x+","+(int)s.y+","+(int)s.z, obj);
		return true;
	}

}
