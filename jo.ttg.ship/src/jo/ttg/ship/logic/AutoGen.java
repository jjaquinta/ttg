/*
 * Created on Jan 7, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.ship.logic;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.ShipStatsError;
import jo.ttg.ship.beans.comp.CommoRadio;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.JumpDrive;
import jo.ttg.ship.beans.comp.LowBerth;
import jo.ttg.ship.beans.comp.ManeuverDrive;
import jo.ttg.ship.beans.comp.Missile;
import jo.ttg.ship.beans.comp.NumberedComponent;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.RangedComponent;
import jo.ttg.ship.beans.comp.SensorActiveEMS;
import jo.ttg.ship.beans.comp.SensorHighDensitometer;
import jo.ttg.ship.beans.comp.SensorLowDensitometer;
import jo.ttg.ship.beans.comp.SensorNeutrino;
import jo.ttg.ship.beans.comp.SensorPassiveEMS;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.TurretMissile;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.TurretSandcaster;
import jo.ttg.ship.beans.comp.Weapon;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AutoGen
{
	public static final int S_MERCHANT = 0;
	public static final int S_SCOUT = 1;
	public static final int S_MILITARY = 2;
    public static final int S_PIRATE = 3;
    public static final int S_HAULER = 4;
    public static final int S_EXPLORER = 5;
    public static final int S_FIGHTER = 6;
    
    public static String[] S_DESCRIPTION =
    {
        "Merchant",
        "Scout",
        "Military",
        "Pirate",
        "Hauler",
        "Explorer",
        "Fighter",
    };
    
    public static int getAutoTypeFromType(String type)
    {
        switch (type.charAt(0))
        {
            case 'A':
                return S_PIRATE;
            case 'M':
                return S_MERCHANT;
            case 'B':
                return S_MILITARY;
            case 'S':
                return S_SCOUT;
        }
        return S_MERCHANT;
    }
    
    public static int getAutoTypeFromDescription(String desc)
    {
        for (int i = 0; i < S_DESCRIPTION.length; i++)
            if (desc.equalsIgnoreCase(S_DESCRIPTION[i]))
                return i;
        return S_MERCHANT;
    }
	
	public static void genMerchant(ShipBean ship, int tl, int tonnage, int jump, double maneuver, int rooms, int berths, int shipType)
	{
		// basics
		int compTL = tl;
		compTL = selectHull(ship, tl, tonnage, shipType, compTL);
		ShipStats stats = report(ship, shipType);
		selectJump(ship, tl, jump, stats);
		selectManeuver(ship, tl, maneuver, stats);
		selectBerths(ship, tl, berths);
		selectSensors(ship, tl, shipType);
		selectWeapons(ship, tl, shipType, stats);
		selectComputer(ship, compTL);
		stats = fixEverything(ship, shipType);
		selectPurifier(ship, tl, shipType, stats);
		stats = selectRooms(ship, rooms, stats, shipType);
		selectAgility(ship, maneuver, shipType, stats);
        stats = fixEverything(ship, shipType);
		selectType(ship, tonnage, jump, rooms, berths, shipType);
        // optimize if errors
        if (stats.getErrors().size() > 0)
        {
            int agility = stats.getAgility();
            NumberedComponent weapon = getAnyEnergyWeapon(ship);
            for (;;)
            {
                boolean didAny = false;
                if (agility > 0)
                {
                    //reportErrors(ship, stats);
                    agility--;
                    //System.out.println("  agility -> "+agility);
                    ShipFixLogic.setAgility(ship, stats, agility);
                    ShipComponent tank = ShipLogic.findComponent(ship, FuelTank.class);
                    ShipModify.remove(ship, tank);
                    ShipComponent plant = ShipLogic.findComponent(ship, PowerPlant.class);
                    ShipModify.remove(ship, plant);
                    stats = fixEverything(ship, shipType);
                    if (stats.getErrors().size() == 0)
                        break;
                    didAny = true;
                }
                if (weapon != null)
                {
                    if ((weapon instanceof TurretMissile) && (weapon.getNumber() == 1))
                        break;
                    //reportErrors(ship, stats);
                    if (weapon.getNumber() > 1)
                        weapon.setNumber(weapon.getNumber() - 1);
                    else
                    {
                        ShipModify.remove(ship, weapon);
                        if (weapon instanceof TurretMissile)
                        {
                            weapon = null;
                            break;
                        }
                        else
                        {
                            weapon = new TurretMissile();
                            addNumberedComponent(ship, tl, weapon, 3);
                        }
                    }
                    //System.out.println("  "+weapon.getName()+" -> "+weapon.getNumber());
                    ShipFixLogic.setAgility(ship, stats, agility); // adjusts power plant appropriately
                    ShipComponent tank = ShipLogic.findComponent(ship, FuelTank.class);
                    ShipModify.remove(ship, tank);
                    ShipComponent plant = ShipLogic.findComponent(ship, PowerPlant.class);
                    ShipModify.remove(ship, plant);
                    stats = fixEverything(ship, shipType);
                    if (stats.getErrors().size() == 0)
                        break;
                    didAny = true;
                }
                if (!didAny)
                    break;
            }
        }
        URIBuilder uri = new URIBuilder();
        uri.setScheme(ShipBean.SCHEME);
        uri.setAuthority(S_DESCRIPTION[shipType]);
        uri.setQuery("tl", String.valueOf(tl));
        uri.setQuery("tons", String.valueOf(tonnage));
        if (jump > 0)
            uri.setQuery("jump", String.valueOf(jump));
        if (maneuver - Math.floor(maneuver) > 0)
            uri.setQuery("man", String.valueOf(maneuver));
        else
            uri.setQuery("man", String.valueOf((int)maneuver));
        if (rooms > 0)
            uri.setQuery("rooms", String.valueOf(rooms));
        if (berths > 0)
            uri.setQuery("berths", String.valueOf(berths));
        ship.setNotes(uri.toString());
        stats = report(ship, shipType);
//		if (stats.getErrors().size() > 0)
//		{
//            System.out.println("Couldn't do it...");
//			reportErrors(ship, stats);
//		}
	}

//    private static void reportErrors(ShipBean ship, ShipStats stats)
//    {
//        System.out.println("Cannot make!");
//        for (Iterator i = stats.getErrors().iterator(); i.hasNext(); )
//        	System.out.println("  "+i.next());
//        for (Iterator i = ship.getComponents().iterator(); i.hasNext(); )
//        {
//            ShipComponent comp = (ShipComponent)i.next();
//            System.out.println("  "+comp.getVolumeDescription()+", "+comp.getPowerDescription()+", "+comp);
//        }
//    }
    
    public static void genMerchant(ShipBean ship, String u)
    {
        URIBuilder uri = new URIBuilder(u);
        int shipType = getAutoTypeFromDescription(uri.getAuthority());
        int tl = IntegerUtils.parseInt(uri.getQuery("tl"));
        int tonnage = IntegerUtils.parseInt(uri.getQuery("tons"));
        int jump = IntegerUtils.parseInt(uri.getQuery("jump"));
        double maneuver = DoubleUtils.parseDouble(uri.getQuery("man"));
        int rooms = IntegerUtils.parseInt(uri.getQuery("rooms"));
        int berths = IntegerUtils.parseInt(uri.getQuery("berths"));
        genMerchant(ship, tl, tonnage, jump, maneuver, rooms, berths, shipType);
        if (uri.getQuery().containsKey("name"))
            ship.setName(uri.getQuery("name"));
        if (uri.getQuery().containsKey("type"))
            ship.setType(uri.getQuery("type"));
    }
    
    private static NumberedComponent getAnyEnergyWeapon(ShipBean ship)
    {
        for (ShipComponent comp : ship.getComponents())
        {
            if (!(comp instanceof Weapon))
                continue;
            if (!(comp instanceof NumberedComponent))
                continue;
            if (comp.getPower() <= 0.0)
                continue;
            return (NumberedComponent)comp;
        }
        return null;
    }
    
    private static void selectType(ShipBean ship, int tonnage, int jump, int rooms, int berths, int shipType)
    {
        String primary = "";
		String qualifier = "";
		if ((shipType == S_MERCHANT) || (shipType == S_PIRATE) || (shipType == S_HAULER))
		{
		    if (rooms + berths >= 20)
		        primary = "R"; // liner
		    else
		        primary = "M"; // merchant
		    if (jump <= 1)
		        qualifier = "W";
		    else if (jump > 2)
		        qualifier = "F";
		}
		else if ((shipType == S_SCOUT) || (shipType == S_EXPLORER))
		{
		    if (jump > 3)
		        primary = "X"; // express 
	        else
	            primary = "S"; // scout
		    if (rooms > 4)
		        qualifier = "K"; // courier
		}
		else if ((shipType == S_MILITARY) || (shipType == S_FIGHTER))
		{
		    if (tonnage < 100)
		        primary = "f"; // fighter
		    else if (tonnage <= 200)
		        primary = "E"; // escort
		    else if (tonnage <= 400)
		        primary = "F"; // frigate
		    else if (tonnage <= 800)
		        primary = "D"; // destroyer
		    else if (tonnage <= 1600)
		        primary = "c"; // cruiser
		    else
		        primary = "B"; // battle
		    if (jump == 0)
		        qualifier = "c"; // close
		    else if (jump > 4)
		        qualifier = "F"; // fast
		}
		ship.setType(primary+qualifier);
    }

    private static void selectAgility(ShipBean ship, double maneuver, int shipType, ShipStats stats)
    {
        if ((shipType == S_MILITARY) && (maneuver >= 2))
			ShipFixLogic.setAgility(ship, stats, (int)(maneuver/2));
        else if (shipType == S_FIGHTER)
            ShipFixLogic.setAgility(ship, stats, (int)(maneuver/2));
    }

    private static ShipStats selectRooms(ShipBean ship, int rooms, ShipStats stats, int shipType)
    {
        if (rooms > 0)
		{
			stats = report(ship, shipType);
			ShipFixLogic.setPassengers(ship, stats, rooms);
		}
        return stats;
    }

    private static void selectPurifier(ShipBean ship, int tl, int shipType, ShipStats stats)
    {
        addComponent(ship, tl, new FuelPurifier());
		if ((shipType == S_MERCHANT) || (shipType == S_HAULER))
			ShipFixLogic.setFuelPurifier(ship, stats, 24);
        else if (shipType == S_PIRATE)
            ShipFixLogic.setFuelPurifier(ship, stats, 48);
		else if ((shipType == S_SCOUT)|| (shipType == S_EXPLORER))
			ShipFixLogic.setFuelPurifier(ship, stats, 12);
		else if (shipType == S_MILITARY)
			ShipFixLogic.setFuelPurifier(ship, stats, 6);
    }

    private static void selectComputer(ShipBean ship, int compTL)
    {
        addNumberedComponent(ship, compTL, new Computer(), 3);
    }

    private static void selectWeapons(ShipBean ship, int tl, int shipType, ShipStats stats)
    {
        int hp = stats.getHardpoints();
		if (shipType == S_MERCHANT)
		{
			addNumberedComponent(ship, tl, new TurretMissile(), hp);
			addNumberedComponent(ship, tl, new TurretSandcaster(), hp*2);
			addNumberedComponent(ship, tl, new Missile(), hp*10);
		}
        else if (shipType == S_PIRATE)
        {
            addNumberedComponent(ship, tl, new TurretMissile(), hp*2);
            addNumberedComponent(ship, tl, new TurretSandcaster(), hp);
            addNumberedComponent(ship, tl, new Missile(), hp*5);
        }
		else if (shipType == S_SCOUT)
		{
			addNumberedComponent(ship, tl, new TurretMissile(), hp);
			addNumberedComponent(ship, tl, new TurretPulseLaser(), hp);
			addNumberedComponent(ship, tl, new TurretSandcaster(), hp);
			addNumberedComponent(ship, tl, new Missile(), hp*10);
		}
		else if (shipType == S_MILITARY)
		{
			addNumberedComponent(ship, tl, new TurretPulseLaser(), hp*2);
			addNumberedComponent(ship, tl, new TurretSandcaster(), hp);
		}
        else if (shipType == S_FIGHTER)
        {
            addNumberedComponent(ship, tl, new TurretPulseLaser(), 3);
        }
    }

    private static void selectSensors(ShipBean ship, int tl, int shipType)
    {
        if ((shipType == S_MERCHANT) || (shipType == S_PIRATE) || (shipType == S_HAULER))
		{
			addRangedComponent(ship, tl, new CommoRadio(), 1);
			addRangedComponent(ship, tl, new SensorActiveEMS(), 1);
			addRangedComponent(ship, tl, new SensorPassiveEMS(), 1);
		}
		else if ((shipType == S_SCOUT) || (shipType == S_EXPLORER))
		{
			addRangedComponent(ship, tl, new CommoRadio(), 0);
			addRangedComponent(ship, tl, new SensorActiveEMS(), 0);
			addRangedComponent(ship, tl, new SensorPassiveEMS(), 0);
			addComponent(ship, tl, new SensorHighDensitometer());
			addComponent(ship, tl, new SensorLowDensitometer());
			addComponent(ship, tl, new SensorNeutrino());
		}
		else if (shipType == S_FIGHTER)
		{
			addRangedComponent(ship, tl, new CommoRadio(), 0);
			addRangedComponent(ship, tl, new SensorActiveEMS(), 0);
			addRangedComponent(ship, tl, new SensorPassiveEMS(), 0);
			addComponent(ship, tl, new SensorHighDensitometer());
			addComponent(ship, tl, new SensorLowDensitometer());
			addComponent(ship, tl, new SensorNeutrino());
		}
        else if (shipType == S_MILITARY)
        {
            addRangedComponent(ship, tl, new CommoRadio(), 0);
            addRangedComponent(ship, tl, new SensorActiveEMS(), 0);
            addRangedComponent(ship, tl, new SensorPassiveEMS(), 0);
            addComponent(ship, tl, new SensorHighDensitometer());
            addComponent(ship, tl, new SensorLowDensitometer());
            addComponent(ship, tl, new SensorNeutrino());
            // redundancy
            addRangedComponent(ship, tl, new CommoRadio(), 0);
            addRangedComponent(ship, tl, new SensorActiveEMS(), 0);
            addRangedComponent(ship, tl, new SensorPassiveEMS(), 0);
            addComponent(ship, tl, new SensorHighDensitometer());
            addComponent(ship, tl, new SensorLowDensitometer());
            addComponent(ship, tl, new SensorNeutrino());
        }
    }

    private static void selectBerths(ShipBean ship, int tl, int berths)
    {
        if (berths > 0)
			addNumberedComponent(ship, tl, new LowBerth(), berths);
    }

    private static void selectManeuver(ShipBean ship, int tl, double maneuver, ShipStats stats)
    {
        if (maneuver > 0)
		{
			ManeuverDrive m = new ManeuverDrive();
			ShipModify.setTechLevel(m, tl);
			m.setManeuver(maneuver, stats);
			ShipModify.add(ship, m);
		}
    }

    private static void selectJump(ShipBean ship, int tl, int jump, ShipStats stats)
    {
        if (jump > 0)
		{
			JumpDrive j = new JumpDrive();
			ShipModify.setTechLevel(j, tl);
			j.setJump(jump, stats);
			ShipModify.add(ship, j);
		}
    }

    private static int selectHull(ShipBean ship, int tl, int tonnage, int shipType, int compTL)
    {
        Hull h = new Hull();
		h.setArmor(40);
		if ((shipType == S_MERCHANT) || (shipType == S_PIRATE) || (shipType == S_HAULER))
		{
			h.setConfig(4);
			h.setStreamlining(1);
			if (compTL > 9)
				compTL = 9;
		}
		else if (shipType == S_SCOUT || (shipType == S_EXPLORER))
		{
			h.setConfig(2);
			h.setStreamlining(1);
			if (compTL > 9)
				compTL = (9+compTL)/2;
		}
		else if (shipType == S_MILITARY || (shipType == S_FIGHTER))
		{
			h.setConfig(1);
			h.setStreamlining(2);
		}
		ShipModify.setTechLevel(h, tl);
		h.setVolume(tonnage*13.5);
		ShipModify.add(ship, h);
        return compTL;
    }
    
    private static ShipStats report(ShipBean ship, int type)
    {
        ShipStats stats;
        //boolean smallCraft = (type == S_HAULER) || (type == S_EXPLORER) || (type == S_FIGHTER);
        stats = ShipReport.report(ship);
        /*
        for (Iterator i = stats.getErrors().iterator(); i.hasNext(); )
        {
            ShipStatsError e = (ShipStatsError)i.next();
            if ((e.getId() == ShipStatsError.NOT_ENOUGH_STATEROOMS) && smallCraft)
                i.remove();
        }
        */
        return stats;
    }
	
	private static ShipStats fixEverything(ShipBean ship, int type)
	{
        ShipStats stats;
		for (;;)
		{
			stats = report(ship, type);
            //System.out.println("Fixing...");
            //reportErrors(ship, stats);
			boolean didSomething = false;
			for (ShipStatsError e : stats.getErrors())
			{
				if (ShipFixLogic.canBeFixed(ship, e))
				{
                    ShipFixLogic.fix(ship, stats, e);
					didSomething = true;
					break; 
				}
			}
			if (!didSomething)
				break;
		}
        return stats;
	}
	
	private static void addRangedComponent(ShipBean ship, int tl, RangedComponent c, int delta)
	{
		ShipModify.setTechLevel(c, tl);
		c.setRange(c.getRangeRange()[1] - delta);
		ShipModify.add(ship, c);
	}
	
	private static void addNumberedComponent(ShipBean ship, int tl, NumberedComponent c, int amnt)
	{
		ShipModify.setTechLevel(c, tl);
		c.setNumber(amnt);
		ShipModify.add(ship, c);
	}
	
	private static void addComponent(ShipBean ship, int tl, ShipComponent c)
	{
		ShipModify.setTechLevel(c, tl);
		ShipModify.add(ship, c);
	}
	
	private static final int[] tonnage = { 100, 200, 400, 800 };
	private static final int[] rooms = { 2, 4, 8, 12 };
	private static final int[] berths = { 4, 8, 16, 32 };

    public static void generateShips(List<ShipBean> ships)
    {
        generateMerchants(ships);
        generateScouts(ships);
        generateMilitary(ships);
        generatePirate(ships);
        generateHaulers(ships);
        generateExplorers(ships);
        generateFighters(ships);
    }
	
	public static void generateMerchants(List<ShipBean> ships)
	{
		ShipBean ship;
		ShipStats stats;
		int merchants = 0;
		// merchants
		for (int tl = 12; tl <= 15; tl++)
			for (int jump = 1; jump <= 2; jump++)
				for (int t = 0; t < 4; t++)
				{
					ship = new ShipBean();
					genMerchant(ship, tl, tonnage[t], jump, 1.0, rooms[t], berths[t], S_MERCHANT);
					stats = report(ship, S_MERCHANT);
					if (stats.getErrors().size() > 0)
						continue;
					StringBuffer name = new StringBuffer();
					name.append("TL");
					name.append(tl);
					name.append(" ");
					if (jump == 2)
						name.append("Far ");
					if (t == 0)
						name.append("Merchant Hopper");
					else if (t == 1)
						name.append("Merchant");
					else if (t == 2)
						name.append("Fat Merchant");
					else if (t == 3)
						name.append("Stretch Merchant");
					ship.setName(name.toString());
					ship.setType("M");
                    ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
					ships.add(ship);
					merchants++;
				}
		System.out.println("Merchants="+merchants);
	}
    
    public static void generateScouts(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int scouts = 0;
        // scout/couriers
        for (int jump = 2; jump <= 6; jump++)
            for (int tl = Math.max(12,9+jump); tl <= 15; tl++)
            {
                ship = new ShipBean();
                int man = 2;
                if (jump == 5)
                    man = 3;
                else if (jump == 6)
                    man = 4;
                genMerchant(ship, tl, 100, jump, man, 2, 0, S_SCOUT);
                stats = report(ship, S_SCOUT);
                if (stats.getErrors().size() > 0)
                    continue;
                StringBuffer name = new StringBuffer();
                name.append("TL");
                name.append(tl);
                name.append(" ");
                if (jump == 2)
                    name.append("Scout");
                else if (jump == 3)
                    name.append("Far Scout");
                else if (jump == 4)
                    name.append("Fast Scout");
                else if (jump == 5)
                    name.append("Courier");
                else if (jump == 6)
                    name.append("Far Courier");
                ship.setName(name.toString());
                ship.setType("S");
                ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                ships.add(ship);
                scouts++;
            }
        System.out.println("Scouts="+scouts);
    }
    
    public static void generateMilitary(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int military = 0;
        // military
        for (int jump = 2; jump <= 6; jump += 2)
            for (int tl = Math.max(12,9+jump); tl <= 15; tl++)
                for (int man = 2; man <= 6; man += 2)
                    for (int t = 1; t < 4; t++)
                    {
                        ship = new ShipBean();
                        genMerchant(ship, tl, tonnage[t], jump, man, 0, 0, S_MILITARY);
                        stats = report(ship, S_MILITARY);
                        if (stats.getErrors().size() > 0)
                            continue;
                        StringBuffer name = new StringBuffer();
                        name.append("TL");
                        name.append(tl);
                        name.append(" ");
                        if (jump == 2)
                            name.append("Short ");
                        else if (jump == 4)
                            name.append("Far ");
                        else if (jump == 5)
                            name.append("Fast ");
                        else if (jump == 6)
                            name.append("Rapid Deployment ");
                        if (man == 2)
                            name.append("Support ");
                        else if (man == 4)
                            name.append("Fighting ");
                        else if (man == 5)
                            name.append("Tactical ");
                        else if (man == 6)
                            name.append("Rapid Engagement ");
                        if (t == 0)
                            name.append("Patrol Boat");
                        else if (t == 1)
                            name.append("Corvette");
                        else if (t == 2)
                            name.append("Escort");
                        else if (t == 3)
                            name.append("Destroyer");
                        ship.setName(name.toString());
                        ship.setType("C");
                        ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                        ships.add(ship);
                        military++;
                    }
        System.out.println("Military="+military);
    }
    
    public static void generatePirate(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int pirate = 0;
        // pirate
        for (int jump = 1; jump < 4; jump++)
            for (int tl = Math.max(12,9+jump); tl <= 15; tl += 2)
                for (int man = 1; man <= 4; man++)
                    for (int t = 1; t <= 3; t++)
                    {
                        ship = new ShipBean();
                        genMerchant(ship, tl, tonnage[t], jump, man, 0, 0, S_PIRATE);
                        stats = report(ship, S_PIRATE);
                        if (stats.getErrors().size() > 0)
                            continue;
                        StringBuffer name = new StringBuffer();
                        name.append("TL");
                        name.append(tl);
                        name.append(" ");
                        if (jump == 1)
                            name.append("Short ");
                        else if (jump == 3)
                            name.append("Far ");
                        else if (jump == 4)
                            name.append("Fast ");
                        if (man == 1)
                            name.append("Support ");
                        else if (man == 2)
                            name.append("Fighting ");
                        else if (man == 3)
                            name.append("Tactical ");
                        else if (man == 4)
                            name.append("Lead ");
                        if (t == 1)
                            name.append("Corsair");
                        else if (t == 2)
                            name.append("Buccaneer");
                        else if (t == 3)
                            name.append("Marauder");
                        ship.setName(name.toString());
                        ship.setType("P");
                        ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                        ships.add(ship);
                        pirate++;
                    }
        System.out.println("Pirate="+pirate);
    }

    private static int[] haulerTons = { 4, 8, 12, 20, 40, 60, 80 };
    private static String[] haulerName = { "Air/Raft", "Air/Truck", "Hauler", "Pinnace", "Shuttle", "Cutter", "Transport" };
    private static int[] explorerTons = { 4, 12, 20, 40, 60, 80 };
    private static String[] explorerName = { "Lander", "Recon", "Observer", "Scout", "Prospector", "Investegator" };
    private static int[] fighterTons = { 10, 20, 40, 60, 80 };
    private static String[] fighterName = { "Defender", "Interceptor", "Fighter", "Lancer", "Heavy Fighter" };
    
    public static void generateHaulers(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int haulers = 0;
        // haulers
        for (int tl = 12; tl <= 15; tl++)
            for (int t = 0; t < haulerTons.length; t++)
            {
                ship = new ShipBean();
                genMerchant(ship, tl, haulerTons[t], 0, 1.0, 0, 0, S_HAULER);
                stats = report(ship, S_HAULER);
                if (stats.getErrors().size() > 0)
                    continue;
                StringBuffer name = new StringBuffer();
                name.append("TL");
                name.append(tl);
                name.append(" ");
                name.append(haulerName[t]);
                ship.setName(name.toString());
                ship.setType("M");
                ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                ships.add(ship);
                haulers++;
            }
        System.out.println("Haulers="+haulers);
    }
    
    public static void generateExplorers(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int explorers = 0;
        // explorers
        for (int t = 0; t < explorerTons.length; t++)
            for (int man = 2; man <= 4; man++)
                for (int tl = 12; tl <= 15; tl++)
                {
                    ship = new ShipBean();
                    genMerchant(ship, tl, explorerTons[t], 0, man, 2, 0, S_EXPLORER);
                    stats = report(ship, S_EXPLORER);
                    if (stats.getErrors().size() > 0)
                        continue;
                    StringBuffer name = new StringBuffer();
                    name.append("TL");
                    name.append(tl);
                    name.append(" ");
                    if (man == 2)
                        name.append("Slow ");
                    else if (man == 4)
                        name.append("Fast ");
                    name.append(explorerName[t]);
                    ship.setName(name.toString());
                    ship.setType("S");
                    ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                    ships.add(ship);
                    explorers++;
                }
        System.out.println("Explorers="+explorers);
    }
    
    public static void generateFighters(List<ShipBean> ships)
    {
        ShipBean ship;
        ShipStats stats;
        int fighters = 0;
        // fighters
        for (int t = 0; t < fighterTons.length; t++)
            for (int tl = 12; tl <= 15; tl++)
                for (int man = 2; man <= 6; man += 2)
                {
                    ship = new ShipBean();
                    genMerchant(ship, tl, fighterTons[t], 0, man, 0, 0, S_FIGHTER);
                    stats = report(ship, S_FIGHTER);
                    if (stats.getErrors().size() > 0)
                        continue;
                    StringBuffer name = new StringBuffer();
                    name.append("TL");
                    name.append(tl);
                    name.append(" ");
                    if (man == 2)
                        name.append("Support ");
                    else if (man == 4)
                        name.append("Fighting ");
                    else if (man == 5)
                        name.append("Tactical ");
                    else if (man == 6)
                        name.append("Rapid Engagement ");
                    name.append(fighterName[t]);
                    ship.setName(name.toString());
                    ship.setType("C");
                    ship.setNotes(ship.getNotes()+"&name="+ship.getName()+"&type="+ship.getType());
                    ships.add(ship);
                    fighters++;
                }
        System.out.println("Fighters="+fighters);
    }

    public static ShipBean genTheMarchHarrier()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 200, 1, 1, 10, 20, AutoGen.S_MERCHANT);
        ship.setName("The March Harrier");
        ship.setType("Free Trader");
        return ship;
    }
    
    public static ShipBean genTheBountifulPlenty()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 400, 1, 1, 13, 9, AutoGen.S_MERCHANT);
        ship.setName("The Bountiful Plenty");
        ship.setType("Subsidised Merchant");
        return ship;
    }
    
    public static ShipBean genTheSweetRepose()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 600, 1, 3, 30, 20, AutoGen.S_MERCHANT);
        ship.setName("The Sweet Repose");
        ship.setType("Subsidised Liner");
        return ship;
    }
    
    public static ShipBean genTheParshidona()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 100, 2, 2, 4, 0, AutoGen.S_SCOUT);
        ship.setName("The Parshidona");
        ship.setType("Scout/Courier");
        return ship;
    }
    
    public static ShipBean genTheIdleTrickster()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 200, 1, 1, 14, 0, AutoGen.S_MERCHANT);
        ship.setName("The Idle Trickster");
        ship.setType("Yacht");
        return ship;
    }
    
    public static ShipBean genThePersuasiveArgument()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 800, 3, 3, 25, 0, AutoGen.S_MILITARY);
        ship.setName("The Persuasive Argument");
        ship.setType("Mercenary Cruiser");
        return ship;
    }
    
    public static ShipBean genTheRaidersLament()
    {
        ShipBean ship = new ShipBean();
        AutoGen.genMerchant(ship, 15, 400, 4, 3, 12, 4, AutoGen.S_MILITARY);
        ship.setName("The Raider's Lament");
        ship.setType("Patrol Cruiser");
        return ship;
    }
    
    public static List<ShipBean> genStandardShips()
    {
        List<ShipBean> ships = new ArrayList<ShipBean>();
        ships.add(genTheMarchHarrier());
        ships.add(genTheBountifulPlenty());
        ships.add(genTheSweetRepose());
        ships.add(genTheParshidona());
        ships.add(genThePersuasiveArgument());
        ships.add(genTheRaidersLament());
        return ships;
    }
    
//	private static void save(ShipBean ship) throws IOException
//	{
//		ShipStats stats = ShipReport.report(ship);
//		System.out.println(
//			"\""+ship.getName()+"\","+
//			stats.getCost()+","+
//			stats.getJump()+","+
//			stats.getManeuver()+","+
//			stats.getCargo()
//			);
//		File f = new File("c:\\temp\\data\\ttg\\ship\\"
//			+StringUtils.trimAll(ship.getName())
//			+".ship.xml");
//		EncodeUtils.saveFile(f, ship);
//	}
    
    public static void main(String[] argv)
    {
        List<ShipBean> ships = new ArrayList<ShipBean>();
        generateShips(ships);        
        for (ShipBean ship : ships)
        {
            System.out.println(ship.getNotes());
//            try
//            {
//                save(ship);
//            }
//            catch (IOException e)
//            {
//                e.printStackTrace();
//            }
        }
    }
}
