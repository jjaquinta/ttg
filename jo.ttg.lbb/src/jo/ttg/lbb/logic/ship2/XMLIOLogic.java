package jo.ttg.lbb.logic.ship2;

import java.util.List;

import jo.ttg.lbb.data.ship2.Planet;
import jo.ttg.lbb.data.ship2.Scenario;
import jo.ttg.lbb.data.ship2.ScenarioSide;
import jo.ttg.lbb.data.ship2.Ship2Design;
import jo.ttg.lbb.data.ship2.Ship2HardpointDesign;
import jo.ttg.lbb.data.ship2.Ship2HardpointInstance;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class XMLIOLogic
{
	public static Scenario readScenario(Node s)
	{
		Scenario scenario = new Scenario();
		scenario.setID(XMLUtils.getAttribute(s, "id"));
		scenario.setName(XMLUtils.getAttribute(s, "name"));
		for (Node d : XMLUtils.findNodes(s, "designs/design"))
			scenario.getDesigns().add(readDesign(d));
		for (Node si : XMLUtils.findNodes(s, "sides/side"))
			scenario.getSides().add(readSide(si, scenario.getDesigns()));		
		for (Node p : XMLUtils.findNodes(s, "planets/planet"))
			scenario.getPlanets().add(readPlanet(p));		
		return scenario;
	}

	private static Planet readPlanet(Node p)
	{
		Planet planet = new Planet();
		planet.setID(XMLUtils.getAttribute(p, "id"));
		planet.setName(XMLUtils.getAttribute(p, "name"));
		planet.getLocation().x = getDoubleAttribute(p, "sx", 0);
		planet.getLocation().y = getDoubleAttribute(p, "sy", 0);
		planet.getLocation().z = getDoubleAttribute(p, "sz", 0);
		planet.getVelocity().x = getDoubleAttribute(p, "vx", 0);
		planet.getVelocity().y = getDoubleAttribute(p, "vy", 0);
		planet.getVelocity().z = getDoubleAttribute(p, "vz", 0);
		planet.setSize(getIntAttribute(p, "size", 4));
		planet.setDensity(getDoubleAttribute(p, "density", 1));
		planet.setAtmosphere(BooleanUtils.parseBoolean(XMLUtils.getAttribute(p, "atmosphere")));
		return planet;
	}

	private static Ship2Design readDesign(Node d)
	{
		Ship2Design design = new Ship2Design();
		design.setID(XMLUtils.getAttribute(d, "id"));
		design.setShipName(XMLUtils.getAttribute(d, "shipName"));
		design.setShipClass(XMLUtils.getAttribute(d, "shipClass"));
		design.setSize(IntegerUtils.parseInt(XMLUtils.getAttribute(d, "size")));
		design.setJumpType(XMLUtils.getAttribute(d, "jumpType").charAt(0));
		design.setManType(XMLUtils.getAttribute(d, "manType").charAt(0));
		design.setPowType(XMLUtils.getAttribute(d, "powType").charAt(0));
		design.setFuelSize(getIntAttribute(d, "fuelSize", 0));
		design.setHoldSize(getIntAttribute(d, "holdSize", 0));
		design.setCabins(getIntAttribute(d, "cabins", 0));
		design.setBerths(getIntAttribute(d, "berths", 0));
		design.setComp(parseComp(XMLUtils.getAttribute(d, "comp")));
		design.setCost(getDoubleAttribute(d, "cost", 0));
		design.setStreamlined(BooleanUtils.parseBoolean(XMLUtils.getAttribute(d, "streamlined")));
		design.setMilitary(BooleanUtils.parseBoolean(XMLUtils.getAttribute(d, "military")));
		for (Node hp : XMLUtils.findNodes(d, "hardpoints/hardpoint"))
			design.getHardpoints().add(readHardpoint(hp));
		return design;
	}

	private static Ship2HardpointDesign readHardpoint(Node hp)
	{
		Ship2HardpointDesign hardpoint = new Ship2HardpointDesign(); 
		hardpoint.setTurretSize(getIntAttribute(hp, "turretSize", 2));
		hardpoint.setMissiles(getIntAttribute(hp, "missiles", 0));
		hardpoint.setSandcasters(getIntAttribute(hp, "sandcasters", 0));
		hardpoint.setBeamLasers(getIntAttribute(hp, "beamLasers", 0));
		hardpoint.setPulseLasers(getIntAttribute(hp, "pulseLasers", 0));
		return hardpoint;
	}

	private static ScenarioSide readSide(Node si, List<Ship2Design> designs)
	{
		ScenarioSide side = new ScenarioSide();
		side.setID(XMLUtils.getAttribute(si, "id"));
		side.setName(XMLUtils.getAttribute(si, "name"));
		for (Node s : XMLUtils.findNodes(si, "ships/ship"))
			side.getShips().add(readShipInstance(s, designs));
		return side;
	}
	
	private static Ship2Instance readShipInstance(Node s, List<Ship2Design> designs)
	{
		Ship2Instance ship = new Ship2Instance();
		ship.setID(XMLUtils.getAttribute(s, "id"));
		ship.setName(XMLUtils.getAttribute(s, "name"));
		String designID = XMLUtils.getAttribute(s, "design");
		for (Ship2Design shipDesign : designs)
			if (shipDesign.getID().equals(designID))
			{
				ship.setDesign(shipDesign);
				break;
			}
		ship.getLocation().x = getDoubleAttribute(s, "sx", 0);
		ship.getLocation().y = getDoubleAttribute(s, "sy", 0);
		ship.getLocation().z = getDoubleAttribute(s, "sz", 0);
		ship.getVelocity().x = getDoubleAttribute(s, "vx", 0);
		ship.getVelocity().y = getDoubleAttribute(s, "vy", 0);
		ship.getVelocity().z = getDoubleAttribute(s, "vz", 0);
		ship.setPilotSkill(getIntAttribute(s, "pilotSkill", 1));
		ship.setMissiles(getIntAttribute(s, "missiles", 0));
		ship.setSands(getIntAttribute(s, "sands", 0));
		ship.setJumpNow(getCharAttribute(s, "jumpNow", ship.getDesign().getJumpType()));
		ship.setManNow(getCharAttribute(s, "manNow", ship.getDesign().getManType()));
		ship.setPowNow(getCharAttribute(s, "powNow", ship.getDesign().getPowType()));
		ship.setHullHits(getIntAttribute(s, "hullHits", 0));
		ship.setHoldHits(getIntAttribute(s, "holdHits", 0));
		ship.setCrewHits(getIntAttribute(s, "crewHits", 0));
		ship.setCompHits(getIntAttribute(s, "compHits", 0));
		ship.setFuelHits(getIntAttribute(s, "fuelHits", 0));
		//System.out.println("Ship "+ship.getName()+" designed with "+ship.getDesign().getHardpoints().size()+" hard points");
		int num = 0;
		for (Node t : XMLUtils.findNodes(s, "turrets/turret"))
		{
			Ship2HardpointInstance turret = readTurret(t);
			turret.setDesign(ship.getDesign().getHardpoints().get(ship.getTurrets().size()));
			turret.setName("Turret "+(++num));
			ship.getTurrets().add(turret);
		}
		for (Node lp : XMLUtils.findNodes(s, "loadedPrograms/program"))
			ship.getLoadedPrograms().add(XMLUtils.getText(lp));
		for (Node lp : XMLUtils.findNodes(s, "storedPrograms/program"))
			ship.getStoredPrograms().add(XMLUtils.getText(lp));
		for (Node si : XMLUtils.findNodes(s, "smallCraft/ship"))
			ship.getSmallCraft().add(readShipInstance(si, designs));
		return ship;
	}

	private static Ship2HardpointInstance readTurret(Node t)
	{
		Ship2HardpointInstance turret = new Ship2HardpointInstance();
		turret.setDamage(BooleanUtils.parseBoolean(XMLUtils.getAttribute(t, "damage")));
		turret.setMissiles(getIntAttribute(t, "Missiles", 6));
		turret.setSand(getIntAttribute(t, "Sand", 6));
		turret.setGunnerSkill(getIntAttribute(t, "GunnerSkill", 0));
		return turret;
	}

	private static char getCharAttribute(Node n, String attrName, char def)
	{
		String attrValue = XMLUtils.getAttribute(n, attrName);
		if (StringUtils.isTrivial(attrValue))
			return def;
		else
			return attrValue.charAt(0);
	}

	private static int getIntAttribute(Node n, String attrName, int def)
	{
		String attrValue = XMLUtils.getAttribute(n, attrName);
		if (StringUtils.isTrivial(attrValue))
			return def;
		else
			return Integer.parseInt(attrValue);
	}

	private static double getDoubleAttribute(Node n, String attrName, double def)
	{
		String attrValue = XMLUtils.getAttribute(n, attrName);
		if (StringUtils.isTrivial(attrValue))
			return def;
		else
			return DoubleUtils.parseDouble(attrValue);
	}

	private static int parseComp(String s)
	{
		if (s.equals("1bis"))
			return Ship2Design.S_COMP1BIS;
		if (s.equals("2bis"))
			return Ship2Design.S_COMP2BIS;
		int c = IntegerUtils.parseInt(s);
		if (c == 1)
			return Ship2Design.S_COMP1;
		if (c == 2)
			return Ship2Design.S_COMP2;
		return c - 3 + Ship2Design.S_COMP3;
	}
}
