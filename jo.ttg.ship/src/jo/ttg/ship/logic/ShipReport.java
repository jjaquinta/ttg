package jo.ttg.ship.logic;

import java.util.List;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.ShipStatsError;
import jo.ttg.ship.beans.ShipStatsWeapon;
import jo.ttg.ship.beans.ShipTotals;
import jo.ttg.ship.beans.comp.Airlock;
import jo.ttg.ship.beans.comp.Avionics;
import jo.ttg.ship.beans.comp.Bay100Disintegrator;
import jo.ttg.ship.beans.comp.Bay100JumpDamper;
import jo.ttg.ship.beans.comp.Bay100MesonGun;
import jo.ttg.ship.beans.comp.Bay100Missile;
import jo.ttg.ship.beans.comp.Bay100PA;
import jo.ttg.ship.beans.comp.Bay100Repulsor;
import jo.ttg.ship.beans.comp.Bay100Tractors;
import jo.ttg.ship.beans.comp.Bay50Disintegrator;
import jo.ttg.ship.beans.comp.Bay50FusionGun;
import jo.ttg.ship.beans.comp.Bay50JumpDamper;
import jo.ttg.ship.beans.comp.Bay50MesonGun;
import jo.ttg.ship.beans.comp.Bay50Missile;
import jo.ttg.ship.beans.comp.Bay50PA;
import jo.ttg.ship.beans.comp.Bay50PlasmaGun;
import jo.ttg.ship.beans.comp.Bay50Repulsor;
import jo.ttg.ship.beans.comp.Bay50Tractors;
import jo.ttg.ship.beans.comp.CommoRadio;
import jo.ttg.ship.beans.comp.Computer;
import jo.ttg.ship.beans.comp.ControlComponent;
import jo.ttg.ship.beans.comp.ControlHeadsUp;
import jo.ttg.ship.beans.comp.ControlHeadsUpHolo;
import jo.ttg.ship.beans.comp.ControlLargeHolodisplay;
import jo.ttg.ship.beans.comp.FuelPurifier;
import jo.ttg.ship.beans.comp.FuelTank;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.JumpDrive;
import jo.ttg.ship.beans.comp.LowBerth;
import jo.ttg.ship.beans.comp.ManeuverDrive;
import jo.ttg.ship.beans.comp.Missile;
import jo.ttg.ship.beans.comp.NumberedComponent;
import jo.ttg.ship.beans.comp.PowerPlant;
import jo.ttg.ship.beans.comp.ScreenBlackGlobe;
import jo.ttg.ship.beans.comp.ScreenComponent;
import jo.ttg.ship.beans.comp.ScreenMeson;
import jo.ttg.ship.beans.comp.ScreenNuclearDamper;
import jo.ttg.ship.beans.comp.ScreenOptimizedBlackGlobe;
import jo.ttg.ship.beans.comp.ScreenOptimizedMeson;
import jo.ttg.ship.beans.comp.ScreenOptimizedNuclearDamper;
import jo.ttg.ship.beans.comp.ScreenOptimizedProton;
import jo.ttg.ship.beans.comp.ScreenOptimizedWhiteGlobe;
import jo.ttg.ship.beans.comp.ScreenProton;
import jo.ttg.ship.beans.comp.ScreenWhiteGlobe;
import jo.ttg.ship.beans.comp.Seat;
import jo.ttg.ship.beans.comp.SensorActiveEMS;
import jo.ttg.ship.beans.comp.SensorEMSJammer;
import jo.ttg.ship.beans.comp.SensorHighDensitometer;
import jo.ttg.ship.beans.comp.SensorLowDensitometer;
import jo.ttg.ship.beans.comp.SensorNeutrino;
import jo.ttg.ship.beans.comp.SensorPassiveEMS;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.beans.comp.SpinalDisintegrator;
import jo.ttg.ship.beans.comp.SpinalJumpProjector;
import jo.ttg.ship.beans.comp.SpinalMesonGun;
import jo.ttg.ship.beans.comp.SpinalPA;
import jo.ttg.ship.beans.comp.Stateroom;
import jo.ttg.ship.beans.comp.SubordinateCraftBay;
import jo.ttg.ship.beans.comp.TurretBeamLaser;
import jo.ttg.ship.beans.comp.TurretDisintegrator;
import jo.ttg.ship.beans.comp.TurretFG;
import jo.ttg.ship.beans.comp.TurretMissile;
import jo.ttg.ship.beans.comp.TurretPA;
import jo.ttg.ship.beans.comp.TurretPG;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.TurretSandcaster;
import jo.ttg.ship.beans.comp.Weapon;
import jo.ttg.ship.beans.comp.WeaponLarge;
import jo.ttg.utils.DisplayUtils;
import jo.util.utils.MathUtils;

public class ShipReport
{
    public static final int BATT_MAX_NUM = 0;
    public static final int BATT_MAX_FAC = 1;
    public static final int BATT_MEDIAN = 2;
    
    public static ShipStats report(ShipBean ship)
    {
        return report(ship, BATT_MEDIAN);
    }
	public static ShipStats report(ShipBean ship, int batteryStrategy)
	{
		ShipStats ret = new ShipStats();
		ShipTotals tot = findTotals(ship, ret);
        reportCraftID(tot, ret);
        reportHull(tot, ret);
		reportPower(tot, ret);
		reportLoco(tot, ret);
		reportCommo(tot, ret);
		reportSensors(tot, ret);
		reportControl(tot, ret);
		reportOff(tot, ret, batteryStrategy);
		reportDef(tot, ret);
		reportAccom(tot, ret);
		reportOther(tot, ret);
		ret.setTotals(tot);
		return ret;
	}

	private static final int[] missileFactors = { 1, 3, 6, 12, 19, 30 };
	private static final int[] missileTech = { 7, 13, 21 };
	private static final int[] beamLaserFactors = { 1, 2, 3, 6, 10, 15, 21, 30 };
	private static final int[] beamLaserTech = { 7, 13, 16 };
	private static final int[] pulseLaserFactors = { 1, 3, 6, 10, 21, 30 };
	private static final int[] pulseLaserTech = { 7, 13, 16 };
	private static final int[] particleAcceleratorFactors = { 1, 2, 4, 6, 8, 10 };
	private static final int[] particleAcceleratorTech = { 14, 15, 16, 18 };
	private static final int[] plasmaGunFactors = { 1, 4, 10, 16, 20 };
	private static final int[] plasmaGunTech = { 10, 11, 12, 16 };
	private static final int[] fusionGunFactors = { 0, 0, 0, 1, 4, 10, 16, 20 };
	private static final int[] fusionGunTech = { 12, 14, 17 };
	private static final int[] disintegratorFactors = { 1, 4, 10, 16, 20 };
	private static final int[] disintegratorTech = { 18 };
	private static final int[] sandcasterFactors = { 1, 3, 6, 8, 10, 20, 30 };
	private static final int[] sandcasterTech = { 7, 8, 10, 16 };

	private static final double[][] bearingTable = 
	{
		{  20000, .95 },
		{  30000, .90 },
		{  40000, .85 },
		{  50000, .80 },
		{  75000, .75 },
		{ 100000, .70 },
		{ 200000, .65 },
		{ 300000, .60 },
		{ 400000, .55 },
	};

	/**
	 * @param tot
	 * @param ret
	 */
	private static void reportOff(ShipTotals tot, ShipStats ret, int batteryStrategy)
	{
		if (tot.hull == null)
			ret.setHardpoints(0);
		else
			ret.setHardpoints(Math.max(1, (int)(tot.hull.getVolume()/1350.0)));
		ret.setUsedHardpoints((int)Math.ceil(tot.usedHardpoints));
		if (tot.usedHardpoints > ret.getHardpoints())
			ret.addError(ShipStatsError.TOO_MANY_WEAPONS, "You have used "+tot.usedHardpoints+" hardpoints, you have only "+ret.getHardpoints());
		double bearing;
		if (tot.hull == null)
			bearing = 0;
		else
		{
			if (tot.hull.getVolume() < 20000*13.5)
				bearing = 1.0;
			else if (tot.hull.getVolume() > 400000*13.5)
				bearing = 0.5;
			else
				bearing = MathUtils.tableLookup(0, tot.hull.getVolume()/13.5, 1, bearingTable);
		}
		ShipStatsWeapon missiles = addWeapon("Missile", ret.getWeapons(), tot.missileNum, tot.missileTech, missileFactors, missileTech, bearing, tot.missile, batteryStrategy);
		if (missiles != null)
		{
			int weapPerBattery = tot.missileNum/missiles.getTurretBatteries();
			if (tot.missileMagazine > 0)
				ret.setBatteryRounds(tot.missileMagazine/weapPerBattery);
		}
		addWeapon("BLaser", ret.getWeapons(), tot.beamLaserNum, tot.beamLaserTech, beamLaserFactors, beamLaserTech, bearing, tot.beamLaser, batteryStrategy);
		addWeapon("PLaser", ret.getWeapons(), tot.pulseLaserNum, tot.pulseLaserTech, pulseLaserFactors, pulseLaserTech, bearing, tot.pulseLaser, batteryStrategy);
		addWeapon("PA  ", ret.getWeapons(), tot.particleAcceleratorNum, tot.particleAcceleratorTech, particleAcceleratorFactors, particleAcceleratorTech, bearing, tot.particleAccelerator, batteryStrategy);
		addWeapon("Plasma", ret.getWeapons(), tot.plasmaGunNum, tot.plasmaGunTech, plasmaGunFactors, plasmaGunTech, bearing, tot.plasmaGun, batteryStrategy);
		addWeapon("Fusion", ret.getWeapons(), tot.fusionGunNum, tot.fusionGunTech, fusionGunFactors, fusionGunTech, bearing, tot.fusionGun, batteryStrategy);
		addWeapon("Dsntgrt", ret.getWeapons(), tot.disintegratorNum, 18, disintegratorFactors, disintegratorTech, bearing, tot.disintegrator, batteryStrategy);
		addWeapon("Sand", ret.getDefense(), tot.sandcasterNum, tot.sandcasterTech, sandcasterFactors, sandcasterTech, bearing, tot.sandcaster, batteryStrategy);
		for (ShipComponent comp : ShipLogic.getComponentList(tot.ship))
		{
			if (!(comp instanceof WeaponLarge))
				continue;
			WeaponLarge weap = (WeaponLarge)comp;
			if ((weap instanceof Bay100Disintegrator) || (weap instanceof Bay50Disintegrator))
				addBay("Dsntgrt", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100JumpDamper) || (weap instanceof Bay50JumpDamper))
				addBay("JmpDmp", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100MesonGun) || (weap instanceof Bay50MesonGun))
				addBay("Meson", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100Missile) || (weap instanceof Bay50Missile))
				addBay("Missile", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100PA) || (weap instanceof Bay50PA))
				addBay("PA  ", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100Repulsor) || (weap instanceof Bay50Repulsor))
				addBay("Rplsr", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if ((weap instanceof Bay100Tractors) || (weap instanceof Bay50Tractors))
				addBay("Trctr", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof Bay50FusionGun)
				addBay("Fusion", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof Bay50PlasmaGun)
				addBay("Plasma", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof SpinalDisintegrator)
				addSpinal("Dsntgrt", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof SpinalJumpProjector)
				addSpinal("JmpPrj", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof SpinalMesonGun)
				addSpinal("Meson", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
			else if (weap instanceof SpinalPA)
				addSpinal("PA  ", ret.getWeapons(), weap.getNumber(), weap.getFactor(), bearing, weap);
		}
	}
	
	private static void addSpinal(String name, List<ShipStatsWeapon> weapons, int num, int factor, double bearing, Weapon weap)
	{
		if (num == 0)
			return;
		ShipStatsWeapon ssw = null;
		for (ShipStatsWeapon tmp : weapons)
		{
			if (tmp.getName().equals(name))
			{
				if (tmp.getSpineFactor() == factor)
				{
					ssw = tmp;
					break;
				}
				else if (tmp.getSpineFactor() == 0)
				{
					tmp.setSpineFactor(factor);
					ssw = tmp;
					break;
				}
			}
		}
		if (ssw == null)
		{
			ssw = new ShipStatsWeapon();
			ssw.setName(name);
			ssw.setComponent(weap);
			ssw.setSpineFactor(factor);
			ssw.setSpineBatteries(num);
			weapons.add(ssw);
		}
		else
			ssw.setSpineBatteries(ssw.getSpineBatteries() + num);
		ssw.setSpineBearing((int)(ssw.getSpineBatteries()*bearing));
	}
	
	private static void addBay(String name, List<ShipStatsWeapon> weapons, int num, int factor, double bearing, Weapon weap)
	{
		if (num == 0)
			return;
		ShipStatsWeapon ssw = null;
		for (ShipStatsWeapon tmp : weapons)
		{
			if (tmp.getName().equals(name))
			{
				if (tmp.getBayFactor() == factor)
				{
					tmp = ssw;
					break;
				}
				else if (tmp.getBayFactor() == 0)
				{
					tmp.setBayFactor(factor);
					ssw = tmp;
					break;
				}
			}
		}
		if (ssw == null)
		{
			ssw = new ShipStatsWeapon();
			ssw.setName(name);
			ssw.setComponent(weap);
			ssw.setBayFactor(factor);
			ssw.setBayBatteries(num);
			weapons.add(ssw);
		}
		else
			ssw.setBayBatteries(ssw.getBayBatteries() + num);
		ssw.setBayBearing((int)(ssw.getBayBatteries()*bearing));
	}
	
	private static ShipStatsWeapon addWeapon(String name, List<ShipStatsWeapon> weapons, int num, int tech, int[] factors, int[] techs, double bearing, Weapon weap, int batteryStrategy)
	{
		if (num == 0)
			return null;
		ShipStatsWeapon ssw = new ShipStatsWeapon();
		ssw.setName(name);
		ssw.setComponent(weap);
		int baseFactor;
		for (baseFactor = 0; techs[baseFactor] <= tech; baseFactor++)
			;
        int bestFactor = 0;
        if (batteryStrategy == BATT_MAX_NUM)
        {
    		for (int i = 0; i < factors.length; i++)
    		{
                int batteries = num/factors[i];
                if (batteries <= 10)
                {
                    bestFactor = i;
                    break;
                }
    		}
        }
        else if (batteryStrategy == BATT_MAX_FAC)
        {
            for (int i = factors.length - 1; i > 0; i--)
            {
                int batteries = num/factors[i];
                if (batteries > 0)
                {
                    bestFactor = i;
                    break;
                }
            }
        }
        else if (batteryStrategy == BATT_MEDIAN)
        {
            for (int i = factors.length - 1; i > 0; i--)
            {
                if (num%factors[i] == 0)
                {
                    if (bestFactor == 0)
                        bestFactor = i;
                    else
                    {
                        bestFactor = i;
                        break;
                    }
                }
            }
        }
        ssw.setTurretFactor(baseFactor + bestFactor);
        int batt = num/factors[bestFactor];
        if (batt > 10)
            batt = 10;
        ssw.setTurretBatteries(batt);
		ssw.setTurretBearing((int)(ssw.getTurretBatteries()*bearing));
        //System.out.println(num+" "+name+" = "+ssw.getTurretBatteries()+" x "+ssw.getTurretFactor());
		weapons.add(ssw);
		return ssw;
	}

	/**
	 * @param tot
	 * @param ret
	 */
	private static void reportDef(ShipTotals tot, ShipStats ret)
	{
		int defDM = 0;
		if (tot.computer != null)
			defDM += tot.computer.getModel();
		defDM += ret.getAgility();
		if (tot.hull != null)
		{
			double v = tot.hull.getVolume();
			if (v < 1350)
				defDM += 2;
			else if (v < 13500)
				defDM += 1;
			else if (v > 1350000)
				defDM -= 2;
			else if (v > 135000)
				defDM -= 1;
			ret.setDefDM(defDM);
		}
		ret.setNuclearDamperFactor(tot.nuclearDamperFactor);
		ret.setMesonFactor(tot.mesonFactor);
		ret.setBlackGlobeFactor(tot.blackGlobeFactor);
		ret.setProtonFactor(tot.protonFactor);
		ret.setWhiteGlobeFactor(tot.whiteGlobeFactor);
	}

	private static void reportOther(ShipTotals tot, ShipStats ret)
	{
		ret.setFuel((int)tot.fuelTankage);
		if (tot.fuelPurificationRate > 0)
			ret.setPurificationTime((int)(tot.fuelTankage/tot.fuelPurificationRate));
		if (tot.hull != null)
			ret.setCargo(Math.floor((tot.hull.getVolume() - tot.volume)/13.5)*13.5);
		else
			ret.setCargo(0);
		ret.setHangerVolume(tot.hangerVolume);
	}

	private static void reportAccom(ShipTotals tot, ShipStats ret)
	{
		ret.setAirlocks(tot.airlocks);
		if (tot.airlocks < 1)
			ret.addError(ShipStatsError.NOT_ENOUGH_AIRLOCKS, "No airlocks installed");
		ret.setStaterooms(tot.staterooms);
        ret.setSeats(tot.seats);
		ret.setLowBerths(tot.berths);
		double cpMult;
		if (tot.computer == null)
			cpMult = 1;
		else
			cpMult = tot.computer.getCPMultipler();
		if (tot.jumpVolume > 0)
		{
			// bridge crew
			ret.setCrewBridge((int)Math.ceil(tot.totalCP/cpMult/750));
			if (ret.getCrewBridge() > 10)
				ret.setCrewBridge(10 + ret.getCrewBridge()/10);
			if (ret.getCrewBridge() < 2)
				ret.setCrewBridge(2);
			// engineering crew
			ret.setCrewEngineering((int)Math.ceil(
				(tot.sectionCP[ShipComponent.S_POWER]+tot.sectionCP[ShipComponent.S_LOCO])
				/cpMult/400
				));
			// maintenance crew
			if (tot.hull == null)
				ret.setCrewMaintenence(0);
			else
			{
				ret.setCrewMaintenence((int)Math.floor(
				(tot.sectionCP[ShipComponent.S_HULL]+tot.hull.getVolume()/100)
				/cpMult/400
				));
			}
			// gunners
			ret.setCrewGunners((int)Math.floor(
				tot.sectionCP[ShipComponent.S_WEAPONS]/cpMult/10
				));
			// command
			ret.setCrewCommand(
				(ret.getCrewBridge()+ret.getCrewEngineering()
				+ret.getCrewMaintenence()+ret.getCrewGunners())/6
			);
			// stupid loop thingy because determination of
			// high passage customers is recursive
			int passengers = 0;
			int nonCommandCrew = ret.getCrewBridge()+ret.getCrewEngineering()
				+ret.getCrewMaintenence()+ret.getCrewGunners();
			for (int i = 0; i < 3; i++)
			{
				int stewards = (passengers + ret.getCrewCommand())/8
					+ nonCommandCrew/50;
				if ((passengers > 0) && (stewards == 0))
					stewards = 1;
				ret.setCrewSteward(stewards);
				int soFar = nonCommandCrew + ret.getCrewCommand() 
					+ passengers + ret.getCrewSteward();
				int medical = soFar/120 + ret.getLowBerths()/20;
				ret.setCrewMedical(medical);
				int newPass = ret.getStaterooms() - (soFar + medical) + passengers;
				if ((newPass < 0) || (newPass == passengers))
					break;
				passengers = newPass;
			}
			ret.setCrew(
				ret.getCrewBridge()+ret.getCrewCommand()+ret.getCrewEngineering()
				+ret.getCrewGunners()+ret.getCrewMaintenence()+ret.getCrewMedical()
				+ret.getCrewSteward()
				);
			ret.setPassengers(passengers);
			if (ret.getStaterooms() < ret.getCrew() + passengers)
				ret.addError(ShipStatsError.NOT_ENOUGH_STATEROOMS, "You only have "+ret.getStaterooms()+", you need at least "+(ret.getCrew() + passengers),
					ret.getCrew() + passengers);
		}
		else
		{	// spaceship
			ret.setCrewBridge(1);
			if (tot.totalCP > cpMult)
				ret.setCrewCommand(1);
			if (tot.sectionCP[ShipComponent.S_WEAPONS] > cpMult)
				ret.setCrewGunners(1);
            int passengers = 0;
            for (int i = 0; i < 3; i++)
            {
                int stewards = passengers/8;
                if ((passengers > 0) && (stewards == 0))
                    stewards = 1;
                ret.setCrewSteward(stewards);
                int soFar = ret.getCrewBridge() + ret.getCrewGunners() + ret.getCrewCommand() 
                    + passengers + ret.getCrewSteward();
                int medical = soFar/120 + ret.getLowBerths()/20;
                ret.setCrewMedical(medical);
                int newPass = ret.getStaterooms() - (soFar + medical) + passengers;
                if ((newPass < 0) || (newPass == passengers))
                    break;
                passengers = newPass;
            }
			ret.setCrew(
				ret.getCrewBridge()+ret.getCrewCommand()+ret.getCrewGunners()
                +ret.getCrewSteward()+ret.getCrewMedical()
				);
            ret.setPassengers(passengers);
            if (ret.getDurationShifts() > 3)
            {
                if (ret.getStaterooms() < ret.getCrew() + passengers)
                    ret.addError(ShipStatsError.NOT_ENOUGH_STATEROOMS, "You only have "+ret.getStaterooms()+", you need at least "+(ret.getCrew() + passengers),
                        ret.getCrew() + passengers);
            }
			if (ret.getDurationShifts() <= 1)
			{
				if (tot.seats < ret.getCrew())
					ret.addError(ShipStatsError.NOT_ENOUGH_SEATS, "You only have "+tot.seats+", you need at least "+ret.getCrew(),
						ret.getCrew(), tot.seats);
			}
			else if (ret.getDurationShifts() <= 3)
			{
				if (tot.seats < ret.getCrew()*2)
					ret.addError(ShipStatsError.NOT_ENOUGH_SEATS, "You only have "+tot.seats+", you need at least "+ret.getCrew()*2,
						ret.getCrew()*2);
			}
			else
			{
				if (ret.getStaterooms() < ret.getCrew())
					ret.addError(ShipStatsError.NOT_ENOUGH_STATEROOMS, "You only have "+ret.getStaterooms()+", you need at least "+ret.getCrew(),
						ret.getCrew());
			}
		}
		ret.setSubordinateCraft(tot.subcraft);
	}

	private static void reportControl(ShipTotals tot, ShipStats ret)
	{
		ret.setControlProvided(tot.controlProvided);
		if (tot.computer == null)
		{
			ret.addError(ShipStatsError.NO_COMPUTER, "You need a computer");
			ret.setComputer("");
			ret.setControlNeeded(tot.totalCP);
		}
		else
		{
			if (tot.computerNum < 3)
				ret.addError(ShipStatsError.NOT_ENOUGH_COMPUTER, "You only have "+tot.computerNum);
			if (tot.totalCP > tot.computer.getMaximumCP())
				ret.addError(ShipStatsError.NOT_ENOUGH_COMPUTER_CONTROL, "You need to cotnrol "+tot.totalCP, tot.totalCP);
			double cp = tot.totalCP/tot.computer.getCPMultipler();
			if (cp > tot.controlProvided)
				ret.addError(ShipStatsError.NOT_ENOUGH_CONTROLS, "You need to cotnrol "+cp,
					cp, cp - tot.controlProvided);
			ret.setComputer(tot.computer.getTechLevelDescription()+"x"+tot.computerNum);
			ret.setComputerModel(tot.computer.getModel());
			ret.setControlNeeded(cp);
		}
		ret.setPanels(tot.panels);
		ret.setSpecial(tot.special);
	}
	
	private static final double[] man_speed = 
	{	0, 1200, 2120, 2850, 3400, 3840, 4200,
	};
	private static final String[] dens_scan = 
	{
		"Formidible", "Formidible", "Difficult",
		"Difficult", "Routine", "Routine",
		"Routine", "Simple",
	};

	private static void reportSensors(ShipTotals tot, ShipStats ret)
	{
		if (tot.passEMS != null)
		{
			ret.setPassiveEMS(tot.passEMS.getRangeDescription());
		}
		else
		{
			ret.setPassiveEMS("");
		}
		if (tot.actEMS != null)
		{
			ret.setActiveEMS(tot.actEMS.getRangeDescription());
			ret.setActiveObjectPinpoint(tot.actEMS.getScanTask());		
			ret.setActiveObjectScan(tot.actEMS.getScanTask());		
		}
		else
		{
			ret.setActiveEMS("");		
			ret.setActiveObjectPinpoint("");		
			ret.setActiveObjectScan("");		
		}
		if (tot.emsJammer != null)
		{
			ret.setEMSJammer(tot.emsJammer.getRangeDescription());
		}
		else
		{
			ret.setEMSJammer("");		
		}
		if (tot.neutrino != null)
		{
			ret.setNeutrino(tot.neutrino.getTechLevelDescription());
		}
		else
		{
			ret.setNeutrino("");
		}
		String dens;		
		if (tot.lowDens != null)
		{
			dens = tot.lowDens.getTechLevelDescription();
		}
		else
		{
			dens = "-";
		}
		dens += "/";		
		if (tot.highDens != null)
		{
			dens += tot.highDens.getTechLevelDescription();
		}
		else
		{
			dens += "-";
		}
		if (dens.equals("-/-"))
			ret.setDensitometer("");
		else
			ret.setDensitometer(dens);
		// passive object scan
		if ((tot.lowDens != null) && (tot.highDens != null))
		{
			int tl = Math.min(tot.lowDens.getTechLevel(), tot.highDens.getTechLevel());
			if (tl < 10)
				ret.setPassiveObjectScan("");
			else
				ret.setPassiveObjectScan(dens_scan[tl-10]);
		}
		else
		{
			ret.setPassiveObjectScan("");
		}
		// passive object pinpoint
		if (tot.highDens != null)
		{
			ret.setPassiveObjectPinpoint(tot.highDens.getScanTask());
		}
		else if (tot.highDens != null)
		{
			ret.setPassiveObjectPinpoint(tot.lowDens.getScanTask());
		}
		else
			ret.setPassiveObjectPinpoint("");
		// passive energy scan
		int engScanTot = 0;
		if (tot.passEMS != null)
			engScanTot += tot.passEMS.getEnergyScanValue();
		if (tot.neutrino != null)
			engScanTot += tot.neutrino.getEnergyScanValue();
		engScanTot /= 2;
		if (engScanTot <= 0)
			ret.setPassiveEnergyScan("");
		else if (engScanTot <= 1)
			ret.setPassiveEnergyScan("Formidible");
		else if (engScanTot <= 3)
			ret.setPassiveEnergyScan("Difficult");
		else if (engScanTot <= 6)
			ret.setPassiveEnergyScan("Routine");
		else //if (engScanTot <= 12)
			ret.setPassiveEnergyScan("Simple");
		// passive energy pinpoint
		if (tot.neutrino == null)
			ret.setPassiveEnergyPinpoint("");
		else
			ret.setPassiveEnergyPinpoint(tot.neutrino.getScanTask());
	}

	private static void reportCommo(ShipTotals tot, ShipStats ret)
	{
		if (tot.radio != null)
			ret.setRadio(tot.radio.getRangeDescription());
		else
			ret.setRadio("none");		
	}

	private static void reportLoco(ShipTotals tot, ShipStats ret)
	{
		if (tot.hull == null)
		{
		    ret.addError(ShipStatsError.NOT_ENOUGH_HULL, "No hull set!", 100.0);
		    return;
		}
		int jump = 0;
		if (tot.jumpVolume > 0)
		{
			jump  = (int)Math.floor(tot.jumpVolume/tot.hull.getVolume()*100) - 1;
			if (jump <= 0)
			{
				ret.addError(ShipStatsError.NOT_ENOUGH_JUMP,
					"need at least "+DisplayUtils.formatVolume(tot.hull.getVolume()*.02)+" for jump-1",
					tot.hull.getVolume()*.02);
			}
		}
		if (jump > 0)
		{
			if (jump + 9 > tot.jumpMinTech)
			{
				ret.addError(ShipStatsError.NOT_ENOUGH_JUMP_TECH,
					"Must be tech "+(jump+9)+" for Jump "+jump
					+". Need at least "
					+DisplayUtils.formatVolume(tot.hull.getVolume()*.02)+" for jump-1");
				jump = tot.jumpMinTech - 9;
			}
			ret.setJump(jump);
			ret.setJumpInoperative((int)Math.ceil(tot.jumpVolume/15));
			if (tot.ecp)
				ret.setJumpInoperative((int)Math.ceil(tot.jumpVolume/15*1.5));
			else
				ret.setJumpInoperative((int)Math.ceil(tot.jumpVolume/15));
			ret.setJumpDestroyed((int)Math.ceil(tot.jumpVolume/6));
		}
		else
		{
			ret.setJump(0);
			ret.setJumpInoperative(0);
			ret.setJumpDestroyed(0);
		}
		double man = 0; 
		if (tot.manVolume> 0)
		{
			man = Math.rint(((tot.manVolume/tot.hull.getVolume()*100 + 1)/3)*10)/10;
			if (man <= 0)
			{
				ret.addError(ShipStatsError.NOT_ENOUGH_MAN, 
					"need at least "+DisplayUtils.formatVolume(tot.hull.getVolume()*.02)+" for man-1",
					tot.hull.getVolume()*.02);
			}
		}
		if (man > 0)
		{
			if (man > 6)
			{
				ret.addError(ShipStatsError.TOO_MUCH_MAN, "Excess will be ignored",
					tot.hull.getVolume()*.32);
				man = 6;
			}
			ret.setManeuver(man);
			if (tot.ecp)
				ret.setManeuverInoperative((int)Math.ceil(tot.manVolume/15*1.5));
			else
				ret.setManeuverInoperative((int)Math.ceil(tot.manVolume/15));
			ret.setManeuverDestroyed(ret.getManeuverInoperative()*2);
			if (tot.avionics != null)
				ret.setNOESpeed(String.valueOf(tot.avionics.getNOE()));
			else
				ret.setNOESpeed("40");
			int vacc = (int)MathUtils.interpolate(man, man_speed);
			int cruise;
			if (tot.hull.getStreamlining() == 0)
				cruise = 300;
			else if (tot.hull.getStreamlining() == 1)
				cruise = 1000;
			else
				cruise = (int)(vacc*.9);
			ret.setVaccuumSpeed(String.valueOf(vacc));
			ret.setTopSpeed(String.valueOf(cruise));
			ret.setCruiseSpeed(String.valueOf((cruise*3)/4));
			int agility = (int)((tot.powerGen - tot.powerUse - tot.powerAux)/tot.weight*5.4);
			if (agility > 0)
				ret.setAgility(agility);
			else
				ret.setAgility(0);
			int emergencyAgility = (int)((tot.powerGen - tot.powerUse)/tot.weight*5.4);
			if (emergencyAgility > 0)
				ret.setEmergencyAgility(emergencyAgility);
			else
				ret.setEmergencyAgility(0);
		}
		else
		{
			ret.setManeuver(0);
			ret.setManeuverInoperative(0);
			ret.setManeuverDestroyed(0);
			ret.setNOESpeed("0");
			ret.setVaccuumSpeed("0");
			ret.setTopSpeed("0");
			ret.setCruiseSpeed("0");
			ret.setAgility(0);
		}
	}

	private static void reportPower(ShipTotals tot, ShipStats ret)
	{
		ret.setPowerProduced(tot.powerGen);
		ret.setPowerConsumed(tot.powerUse + tot.powerAux);
		if (tot.powerGen < tot.powerUse + tot.powerAux)
		{
			ret.addError(ShipStatsError.NOT_ENOUGH_POW, 
				"Power demand is "+DisplayUtils.formatPower(tot.powerUse + tot.powerAux),
				tot.powerUse + tot.powerAux);
		}
		double pts = Math.max(1, Math.ceil(tot.powerVolume/15));
		ret.setPowerDestroyed((int)(pts*2));
		if (tot.ecp)
			pts *= 1.5;
		ret.setPowerInoperative((int)pts);
		ret.setPower(DisplayUtils.formatPower(tot.powerGen));
		if (tot.powerFuelUsage > 0)
		{
			// usage sans auxiliary power
			double usage = tot.powerFuelUsage * tot.powerUse/tot.powerGen;
			double hours = tot.powerFuelTankage/usage;
			int days = (int)Math.floor(hours/24);
			int shifts = (int)Math.floor(hours/8);
			//if ((days < 30) && (tot.jumpVolume > 0))
            // need fuel for insystemers, not sure why we had the jump drive requirement
            // JJ 15 Oct 2005
            if (days < 30)
			{
				double needed = 30*24 - hours;
				needed *= usage;
				ret.addError(ShipStatsError.NOT_ENOUGH_FUEL_POW,
					"Fuel for only "+days+" days of power. "
					+" Power plant consumes "+DisplayUtils.formatVolume(usage)+" per hour."
					+" For 30 days we need "+DisplayUtils.formatVolume(needed)+" more.",
					needed);
			}
			ret.setDurationDays(days);
			ret.setDurationShifts(shifts);
			ret.setDurationHours((int)hours);
		}
		else
		{
			ret.setDurationDays(0);
			ret.setDurationShifts(0);
		}
	}

    private static void reportHull(ShipTotals tot, ShipStats ret)
    {
        if (tot.hull == null)
        {
			ret.setHullInoperative(0);
			ret.setHullDestroyed(0);
			ret.setDisplacement(0);
			ret.setConfig("0");
			ret.setArmorClass(0);
			ret.setArmor("0");
			ret.setUnloadedWeight("0");
			ret.setLoadedWeight("0");
		}
        else
        {
        	ret.setHullInoperative((int)Math.max(1, (int)Math.ceil(tot.hull.getVolume()/15)));
        	ret.setHullDestroyed((int)Math.max(1, (int)Math.ceil(tot.hull.getVolume()/6)));
        	ret.setDisplacement(tot.hull.getVolume());
        	String config = String.valueOf(tot.hull.getConfig());
        	if (tot.hull.getStreamlining() == Hull.FRAME_STREAMLINED)
        	{
        		config += "SL";
        		ret.setFuelScoops(true);
			}
			else if (tot.hull.getStreamlining() == Hull.FRAME_AIRFRAME)
			{
				config += "AF";
				ret.setFuelScoops(true);
			}
			else
				ret.setFuelScoops(false);
			ret.setConfig(config);
			ret.setArmorClass(tot.hull.getArmor());
			String armor = String.valueOf(tot.hull.getArmor());
			switch (tot.hull.getTechLevel())
			{
				case 5:
					armor += "A";
					break;
				case 6:
					armor += "B";
					break;
				case 7:
				case 8:
					armor += "C";
					break;
				case 9:
					armor += "D";
					break;
				case 10:
				case 11:
					armor += "E";
					break;
				case 12:
				case 13:
					armor += "F";
					break;
				case 14:
				case 16:
					armor += "G";
					break;
				case 17:
					armor += "H";
					break;
			}
			ret.setArmor(armor);
			double weight = tot.weight;
			ret.setUnloadedWeight(DisplayUtils.formatWeight(weight));
			weight += tot.fuelTankage*.07;
			weight += tot.cargoSpace*1;
			ret.setLoadedWeight(DisplayUtils.formatWeight(weight));
        }
    }

    private static void reportCraftID(ShipTotals tot, ShipStats ret)
    {
        ret.setCraftName(tot.ship.getName());
        ret.setCraftType(tot.ship.getType());
        ret.setTechLevel(tot.tech);
        ret.setCost(DisplayUtils.formatCurrency(tot.cost));
        ret.setRawCost(tot.cost);
    }
	
	private static ShipTotals findTotals(ShipBean ship, ShipStats ret)
	{
		ShipTotals tot = new ShipTotals();
		tot.ship = ship;
		tot.ecp = true;
		tot.tech = 0;
		tot.jumpMinTech = 21;
		// find hull first
		for (ShipComponent comp : ShipLogic.getComponentList(ship))
		{
			if (comp instanceof Hull)
			{
				if (tot.hull != null)
					ret.addError(ShipStatsError.TOO_MANY_HULLS, "Extra ignored.");
				else
					tot.hull = (Hull)comp;
			}
		}
		// do the rest
		for (ShipComponent comp : ShipLogic.getComponentList(ship))
		{
			if (comp instanceof Hull)
			{
				tot.sectionCP[ShipComponent.S_HULL] += ((Hull)comp).getHullControlPoints();
				tot.sectionCP[ShipComponent.S_BRIDGE] += ((Hull)comp).getEnvironControlPoints();
			}
			else
				tot.sectionCP[comp.getSection()] += comp.getControlPoints();
			tot.totalCP += comp.getControlPoints();
			tot.weight += comp.getWeight();
			tot.cost += comp.getPrice();
			if (comp instanceof FuelTank)
				tot.fuelTankage += ComponentLogic.getVolume(comp, tot.hull);
			if (comp instanceof FuelPurifier)
				tot.fuelPurificationRate += ((FuelPurifier)comp).getRate();
			if (!(comp instanceof Hull))
				tot.volume += ComponentLogic.getVolume(comp, tot.hull);
			if (comp instanceof PowerPlant)
			{
				PowerPlant pp = (PowerPlant)comp;
				tot.powerGen += pp.getPower();
				tot.powerVolume += pp.getVolume();
				tot.powerFuelUsage += pp.getFuelConsumtion();
			}
			else if (comp.getSection() != ShipComponent.S_WEAPONS)
				tot.powerUse += comp.getPower();
			else
				tot.powerAux += comp.getPower();
			if (comp instanceof Computer)
			{
				if (!((Computer)comp).isECP())
					tot.ecp = false;
				tot.computerNum += ((Computer)comp).getNumber();
				if ((tot.computer == null) || ((Computer)comp).getCPMultipler() < tot.computer.getCPMultipler())
					tot.computer = (Computer)comp;
			}
			else if (comp instanceof ControlComponent)
			{
				if (!((ControlComponent)comp).isECP())
					tot.ecp = false;
				tot.controlProvided += ((ControlComponent)comp).getCP();
				if ((comp instanceof ControlHeadsUp) || (comp instanceof ControlLargeHolodisplay)
					|| (comp instanceof ControlHeadsUpHolo))
				{
					tot.special += comp.getName();
					if (((ControlComponent)comp).getNumber() > 1)
						tot.special += "x" +((ControlComponent)comp).getNumber();
					tot.special += ", "; 
				}
				else
				{
					tot.panels += comp.getName();
					if (((ControlComponent)comp).getNumber() > 1)
						tot.panels += "x" +((ControlComponent)comp).getNumber();
					tot.panels += ", "; 
				}
			}
			if (comp.getTechLevel() > tot.tech)
				tot.tech = comp.getTechLevel();
			if (comp instanceof JumpDrive)
			{
				tot.jumpVolume += comp.getVolume();
				if (comp.getTechLevel() < tot.jumpMinTech)
					tot.jumpMinTech = comp.getTechLevel();
			}
			if (comp instanceof ManeuverDrive)
				tot.manVolume += comp.getVolume();
			if (comp instanceof Avionics)
				tot.avionics = (Avionics)comp;
			if (comp instanceof CommoRadio)
				tot.radio = (CommoRadio)comp;
			if (comp instanceof SensorActiveEMS)
				tot.actEMS = (SensorActiveEMS)comp;
			if (comp instanceof SensorPassiveEMS)
				tot.passEMS = (SensorPassiveEMS)comp;
			if (comp instanceof SensorHighDensitometer)
				tot.highDens = (SensorHighDensitometer)comp;
			if (comp instanceof SensorEMSJammer)
				tot.emsJammer = (SensorEMSJammer)comp;
			if (comp instanceof SensorLowDensitometer)
				tot.lowDens = (SensorLowDensitometer)comp;
			if (comp instanceof SensorNeutrino)
				tot.neutrino = (SensorNeutrino)comp;
			if (comp instanceof Seat)
				tot.seats += ((Seat)comp).getNumber();
			if (comp instanceof Stateroom)
				tot.staterooms += ((Stateroom)comp).getNumber();
			if (comp instanceof LowBerth)
				tot.berths += ((LowBerth)comp).getNumber();
			if (comp instanceof Airlock)
				tot.airlocks += ((Airlock)comp).getNumber();
			if (comp instanceof SubordinateCraftBay)
			{
				tot.subcraft += ((SubordinateCraftBay)comp).getSubordinateCraftName()
					+"x"+((SubordinateCraftBay)comp).getNumber()+", ";
				tot.hangerVolume += ((SubordinateCraftBay)comp).getVolume();
			}
			if (comp instanceof Missile)
				tot.missileMagazine += ((NumberedComponent)comp).getNumber();
			if (comp instanceof Weapon)
				tot.usedHardpoints += ((Weapon)comp).getHardponits();
			if (comp instanceof TurretMissile)
			{
				tot.missile = (Weapon)comp;
				tot.missileNum += ((NumberedComponent)comp).getNumber();
				tot.missileSalvo += ((NumberedComponent)comp).getNumber();
				if ((tot.missileTech == 0) || (tot.missileTech > comp.getTechLevel()))
					tot.missileTech = comp.getTechLevel();
			}
			if (comp instanceof Bay100Missile)
				tot.missileSalvo += 100*((NumberedComponent)comp).getNumber();
			if (comp instanceof Bay50Missile)
				tot.missileSalvo += 50*((NumberedComponent)comp).getNumber();
			if (comp instanceof TurretBeamLaser)
			{
				tot.beamLaser = (Weapon)comp;
				tot.beamLaserNum += ((NumberedComponent)comp).getNumber();
				if ((tot.beamLaserTech == 0) || (tot.beamLaserTech > comp.getTechLevel()))
					tot.beamLaserTech = comp.getTechLevel();
			}
			if (comp instanceof TurretPulseLaser)
			{
				tot.pulseLaser = (Weapon)comp;
				tot.pulseLaserNum += ((NumberedComponent)comp).getNumber();
				if ((tot.pulseLaserTech == 0) || (tot.pulseLaserTech > comp.getTechLevel()))
					tot.pulseLaserTech = comp.getTechLevel();
			}
			if (comp instanceof TurretSandcaster)
			{
				tot.sandcaster = (Weapon)comp;
				tot.sandcasterNum += ((NumberedComponent)comp).getNumber();
				if ((tot.sandcasterTech == 0) || (tot.sandcasterTech > comp.getTechLevel()))
					tot.sandcasterTech = comp.getTechLevel();
			}
			if (comp instanceof TurretPG)
			{
				tot.plasmaGun = (Weapon)comp;
				tot.plasmaGunNum += ((NumberedComponent)comp).getNumber();
				if ((tot.plasmaGunTech == 0) || (tot.plasmaGunTech > comp.getTechLevel()))
					tot.plasmaGunTech = comp.getTechLevel();
			}
			if (comp instanceof TurretDisintegrator)
			{
				tot.disintegrator = (Weapon)comp;
				tot.disintegratorNum += ((NumberedComponent)comp).getNumber();
			}
			if (comp instanceof TurretFG)
			{
			    tot.fusionGun = (Weapon)comp;
				tot.fusionGunNum += ((NumberedComponent)comp).getNumber();
				if ((tot.fusionGunTech == 0) || (tot.fusionGunTech > comp.getTechLevel()))
					tot.fusionGunTech = comp.getTechLevel();
			}
			if (comp instanceof TurretPA)
			{
			    tot.particleAccelerator = (Weapon)comp;
				tot.particleAcceleratorNum += ((NumberedComponent)comp).getNumber();
				if ((tot.particleAcceleratorTech == 0) || (tot.particleAcceleratorTech > comp.getTechLevel()))
					tot.particleAcceleratorTech = comp.getTechLevel();
			}
			if ((comp instanceof ScreenNuclearDamper) || (comp instanceof ScreenOptimizedNuclearDamper))
				tot.nuclearDamperFactor = Math.max(tot.nuclearDamperFactor, ((ScreenComponent)comp).getFactor());
			if ((comp instanceof ScreenMeson) || (comp instanceof ScreenOptimizedMeson))
				tot.mesonFactor = Math.max(tot.mesonFactor, ((ScreenComponent)comp).getFactor());
			if ((comp instanceof ScreenBlackGlobe) || (comp instanceof ScreenOptimizedBlackGlobe))
				tot.blackGlobeFactor = Math.max(tot.blackGlobeFactor, ((ScreenComponent)comp).getFactor());
			if ((comp instanceof ScreenProton) || (comp instanceof ScreenOptimizedProton))
				tot.protonFactor = Math.max(tot.protonFactor, ((ScreenComponent)comp).getFactor());
			if ((comp instanceof ScreenWhiteGlobe) || (comp instanceof ScreenOptimizedWhiteGlobe))
				tot.whiteGlobeFactor = Math.max(tot.whiteGlobeFactor, ((ScreenComponent)comp).getFactor());
		}
		if (tot.hull == null)
			ret.addError(ShipStatsError.NO_HULL, "Abort.");
		else
		{
			tot.cargoSpace = tot.hull.getUsableVolume() - tot.volume;
			if (tot.cargoSpace < 0)
			{
				ret.addError(ShipStatsError.NOT_ENOUGH_HULL, "Need "+DisplayUtils.formatVolume(-tot.cargoSpace)+" more",
					-tot.cargoSpace); 
				tot.cargoSpace = 0;
			}
		}
		if ((tot.jumpVolume == 0) || (tot.hull == null) || (tot.jumpMinTech < 9))
			tot.jump = 0;
		else
		{
			tot.jump = (int)Math.floor(tot.jumpVolume/tot.hull.getVolume()*100.0 - 1.0);
			if ((tot.jumpMinTech < 11) && (tot.jump >= 2))
				tot.jump = 1;
			else if ((tot.jumpMinTech < 12) && (tot.jump >= 3))
				tot.jump = 2;
			else if ((tot.jumpMinTech < 13) && (tot.jump >= 4))
				tot.jump = 3;
			else if ((tot.jumpMinTech < 14) && (tot.jump >= 5))
				tot.jump = 4;
			else if ((tot.jumpMinTech < 15) && (tot.jump >= 6))
				tot.jump = 5;
		}
		tot.jumpFuelVolume = tot.jumpVolume*5;
		tot.powerFuelTankage = tot.fuelTankage - tot.jumpFuelVolume;
		if (tot.powerFuelTankage < 0)
		{
			ret.addError(ShipStatsError.NOT_ENOUGH_FUEL_JUMP,
				"Need total of "+DisplayUtils.formatVolume(tot.jumpFuelVolume),
				tot.jumpFuelVolume, -tot.powerFuelTankage);
			tot.powerFuelTankage = 0;
		}
		return tot;
	}
}

