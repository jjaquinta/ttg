/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic.cbt;

import java.util.List;

import jo.ttg.beans.LocBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.logic.chr.TaskLogic;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.ShipStatsWeapon;
import jo.ttg.ship.beans.cbt.CombatBean;
import jo.ttg.ship.beans.cbt.CombatNotifyEvent;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.SensorToScanBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;
import jo.ttg.ship.logic.ShipReport;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CombatLogic
{
    public static CombatBean newCombat(RandBean rnd)
    {
        CombatBean cbt = new CombatBean();
        cbt.setRnd(rnd);
        return cbt;
    }
    
    public static CombatSideBean addSide(CombatBean cbt, String name)
    {
        CombatSideBean side = new CombatSideBean();
        side.setCombat(cbt);
        side.setName(name);
        cbt.getSides().add(side);
        cbt.fireMonotonicPropertyChange("sides", side);
        return side;
    }
    
    public static void removeSide(CombatSideBean side)
    {
        side.getCombat().getSides().remove(side);
        side.getCombat().fireMonotonicPropertyChange("sides", side);
    }
    
    public static CombatSideBean getSide(CombatBean cbt, String name)
    {
        for (CombatSideBean side : cbt.getSides())
            if (side.getName().equals(name))
                return side;
        return null;
    }
    
    public static CombatShipBean addShip(CombatSideBean side, ShipBean shipDesign, LocBean location, double velocity, List<? extends CharBean> crew)
    {
        return addShip(side, shipDesign, ShipReport.report(shipDesign), location, velocity, crew);
    }
    public static CombatShipBean addShip(CombatSideBean side, ShipBean shipDesign, ShipStats shipStats, LocBean location, double velocity, List<? extends CharBean> crew)
    {
        CombatShipBean ship = new CombatShipBean();
        ship.setSide(side);
        side.getShips().add(ship);
        ship.setShipDesign(shipDesign);
        ship.setShipStats(shipStats);
        ship.getLocation().set(location);
        ship.setVelocity(velocity);
        if (crew != null)
        {
            ship.getCrew().addAll(crew);
            ship.fireMonotonicPropertyChange("crew");
            for (CharBean c : crew)
            {
                int shipTactics = c.getSkill("Ship Tactics");
                int fleetTactics = c.getSkill("Fleet Tactics");
                if (shipTactics > 0)
                    ship.setShipTactics(ship.getShipTactics() + shipTactics);
                if (fleetTactics > 0)
                    side.setFleetTactics(side.getFleetTactics() + fleetTactics);
            }
        }
        addShipWeapons(ship);
        addShipSensors(ship);
        addShipScreens(ship);
        ship.getSide().fireMonotonicPropertyChange("ships", ship);
        ship.getSide().getCombat().fireMonotonicPropertyChange("sides", side);
        notify(ship.getSide().getCombat(), CombatNotifyEvent.JOIN, side, ship);
        return ship;
    }
    
    public static void removeShip(CombatShipBean ship)
    {
        ship.getSide().getShips().remove(ship);
        ship.getSide().fireMonotonicPropertyChange("ships", ship);
        ship.getSide().getCombat().fireMonotonicPropertyChange("sides", ship.getSide());
    }
    
    public static CombatShipBean getShip(CombatSideBean side, String name)
    {
        for (CombatShipBean ship : side.getShips())
        {
            if (ship.getShipName().equals(name))
                return ship;
            if (ship.getShipDesign().getName().equals(name))
                return ship;
        }
        return null;
    }
    
    /**
     * @param ship
     */
    private static void addShipSensors(CombatShipBean ship)
    {
        if (ship.getShipStats().getActiveObjectScan().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("ActiveObjectScan");
            sensor.setPhylum(SensorToScanBean.P_ACTIVE);
            sensor.setType(SensorToScanBean.T_SCAN);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getActiveObjectScan()));
            ship.getSensors().add(sensor);
        }
        if (ship.getShipStats().getActiveObjectPinpoint().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("ActiveObjectPinpoint");
            sensor.setPhylum(SensorToScanBean.P_ACTIVE);
            sensor.setType(SensorToScanBean.T_PINPOINT);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getActiveObjectPinpoint()));
            ship.getSensors().add(sensor);
        }
        if (ship.getShipStats().getPassiveObjectScan().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("PassiveObjectScan");
            sensor.setPhylum(SensorToScanBean.P_PASSIVE);
            sensor.setType(SensorToScanBean.T_SCAN);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getPassiveObjectScan()));
            ship.getSensors().add(sensor);
        }
        if (ship.getShipStats().getPassiveObjectPinpoint().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("PassiveObjectPinpoint");
            sensor.setPhylum(SensorToScanBean.P_PASSIVE);
            sensor.setType(SensorToScanBean.T_PINPOINT);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getPassiveObjectPinpoint()));
            ship.getSensors().add(sensor);
        }
        if (ship.getShipStats().getPassiveEnergyScan().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("PassiveEnergyScan");
            sensor.setPhylum(SensorToScanBean.P_PASSIVE);
            sensor.setType(SensorToScanBean.T_SCAN);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getPassiveEnergyScan()));
            ship.getSensors().add(sensor);
        }
        if (ship.getShipStats().getPassiveEnergyPinpoint().length() > 0)
        {
            SensorToScanBean sensor = new SensorToScanBean();
            sensor.setShip(ship);
            sensor.setSensor("PassiveEnergyPinpoint");
            sensor.setPhylum(SensorToScanBean.P_PASSIVE);
            sensor.setType(SensorToScanBean.T_PINPOINT);
            sensor.setDifficulty(TaskLogic.convStringToDifficulty(ship.getShipStats().getPassiveEnergyPinpoint()));
            ship.getSensors().add(sensor);
        }
        ship.fireMonotonicPropertyChange("sensors");
    }
    
    /**
     * @param ship
     */
    private static void addShipWeapons(CombatShipBean ship)
    {
        for  (ShipStatsWeapon ssw : ship.getShipStats().getWeapons())
        {
            for (int j = 0; j < ssw.getTurretBatteries(); j++)
            {
                WeaponToFireBean wtf = new WeaponToFireBean();
                wtf.setPhylum(WeaponToFireBean.TURRET);
                wtf.setType(ssw.getComponent().getType());
                wtf.setFactor(ssw.getTurretFactor());
                wtf.setWeapon(ssw);
                wtf.setShip(ship);
                if (ssw.getComponent().getType() < 0)
                    ship.getDefenses().add(wtf);
                else
                    ship.getWeapons().add(wtf);
        	}
            for (int j = 0; j < ssw.getBayBatteries(); j++)
            {
                WeaponToFireBean wtf = new WeaponToFireBean();
                wtf.setPhylum(WeaponToFireBean.BAY);
                wtf.setType(ssw.getComponent().getType());
                wtf.setFactor(ssw.getBayFactor());
                wtf.setWeapon(ssw);
                wtf.setShip(ship);
                if (ssw.getComponent().getType() < 0)
                    ship.getDefenses().add(wtf);
                else
                    ship.getWeapons().add(wtf);
        	}
            for (int j = 0; j < ssw.getSpineBatteries(); j++)
            {
                WeaponToFireBean wtf = new WeaponToFireBean();
                wtf.setPhylum(WeaponToFireBean.SPINAL);
                wtf.setType(ssw.getComponent().getType());
                wtf.setFactor(ssw.getSpineFactor());
                wtf.setWeapon(ssw);
                wtf.setShip(ship);
                if (ssw.getComponent().getType() < 0)
                    ship.getDefenses().add(wtf);
                else
                    ship.getWeapons().add(wtf);
        	}
        }
        ship.fireMonotonicPropertyChange("weapons");
    }
    
    /**
     * @param ship
     */
    private static void addShipScreens(CombatShipBean ship)
    {
        ship.setBlackGlobeFactor(ship.getShipStats().getBlackGlobeFactor());
        ship.setMesonFactor(ship.getShipStats().getMesonFactor());
        ship.setNuclearDamperFactor(ship.getShipStats().getNuclearDamperFactor());
        ship.setProtonFactor(ship.getShipStats().getProtonFactor());
        ship.setWhiteGlobeFactor(ship.getShipStats().getWhiteGlobeFactor());
    }

    public static void setShipConsumables(CombatShipBean ship, int explosive, int nuclear, int sand)
    {
        ship.setShellsExpolsive(explosive);
        ship.setShellsNuclear(nuclear);
        ship.setShellsSand(sand);
    }
    
    public static void notify(CombatBean combat, CombatNotifyEvent ev)
    {
        for (CombatNotifyListener listener : combat.getCombatNotifyListeners())
            listener.combatNotification(ev);
    }
    
    public static void notify(CombatBean combat, int id, Object subject, Object adjective)
    {
        notify(combat, new CombatNotifyEvent(combat, id, subject, adjective));
    }
}
