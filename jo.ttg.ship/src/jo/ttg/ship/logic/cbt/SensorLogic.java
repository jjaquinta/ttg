/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic.cbt;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.logic.LocLogic;
import jo.ttg.logic.chr.SkillLogic;
import jo.ttg.logic.chr.TaskLogic;
import jo.ttg.ship.beans.cbt.CombatBean;
import jo.ttg.ship.beans.cbt.CombatNotifyEvent;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.SensorPingBean;
import jo.ttg.ship.beans.cbt.SensorToScanBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SensorLogic
{

    /**
     * @param cbt
     * @param side
     * @param ship
     * @param target
     */
    public static void doSensorScan(CombatShipBean ship, CombatShipBean target)
    {
        SensorPingBean ping = (SensorPingBean)ship.getSensorPings().get(target);
        if (ping == null)
        {
            ping = new SensorPingBean();
            ping.setDegree(SensorPingBean.NONE);
            ping.setShip(target);
            ship.getSensorPings().put(target, ping);
            ship.fireMonotonicPropertyChange("sensorPings");
        }
        int off = CombatShipLogic.getComputer(ship);
        int ops = SkillLogic.getBestSkill("Sensor Ops", ship.getCrew());
        if (ops > off)
            off = ops;
        int def = (int)LocLogic.dist(ship.getLocation(), target.getLocation());
        int oldDegree = ping.getDegree();
        boolean usedActive;
        if (ping.getDegree() == SensorPingBean.NONE)
            usedActive = rollSensorScan(ship, target, ping, off, def);
        else
            usedActive = rollSensorPinpoint(ship, target, ping, off, def);
        if (ping.getDegree() > oldDegree)
        {
	        if ((ping.getDegree() == SensorPingBean.LOCATED))
	            CombatLogic.notify(ship.getSide().getCombat(), CombatNotifyEvent.LOCATED, ship, target);
	        else if (ping.getDegree() == SensorPingBean.LOCKED)
	            CombatLogic.notify(ship.getSide().getCombat(), CombatNotifyEvent.LOCKED, ship, target);
        }
        else
            CombatLogic.notify(ship.getSide().getCombat(), CombatNotifyEvent.NOSCAN, ship, target);
        if (ship.getShipStats().getRadio().length() > 0)
            propogatePing(ship, ping);
        if (usedActive && 
                ((getBestSensorPinpoint(target) != null)
                        || (getBestSensorScan(target) != null)))
        {
            ping = (SensorPingBean)target.getSensorPings().get(ship);
            if (ping == null)
            {
                ping = new SensorPingBean();
                ping.setDegree(SensorPingBean.NONE);
                ping.setShip(ship);
                target.getSensorPings().put(ship, ping);
            }
            if (ping.getDegree() == SensorPingBean.NONE)
            {
                ping.setDegree(SensorPingBean.LOCATED);
                propogatePing(target, ping);
                CombatLogic.notify(ship.getSide().getCombat(), CombatNotifyEvent.LOCATED, target, ship);
            }
        }
    }    

    /**
     * @param side
     * @param ping
     */
    private static void propogatePing(CombatShipBean ship, SensorPingBean ping)
    {
        CombatSideBean side = ship.getSide();
        for (CombatShipBean otherShip : side.getShips())
        {
            if (ship == otherShip)
                continue;
            if (otherShip.getShipStats().getRadio().length() < 0)
                continue;
            SensorPingBean otherPing = (SensorPingBean)otherShip.getSensorPings().get(ping.getShip());
            if ((otherPing == null) || (otherPing.getDegree() < ping.getDegree()))
            {
                otherShip.getSensorPings().put(ping.getShip(), ping);
                otherShip.fireMonotonicPropertyChange("sensorPings");
            }
        }
    }

    /**
     * @param cbt
     * @param ship
     * @param target
     * @param ping
     * @param off
     * @param def
     */
    private static boolean rollSensorPinpoint(CombatShipBean ship, CombatShipBean target, SensorPingBean ping, int off, int def)
    {
        CombatBean cbt = ship.getSide().getCombat();
        SensorToScanBean sensor = getBestSensorPinpoint(ship);
        if (sensor == null)
            return false;
        int result = TaskLogic.attemptUncertainTask(sensor.getDifficulty(), cbt.getRnd(), off, -def);
        if ((result&TaskLogic.RES_TOTAL_TRUTH) != 0)
        {
            ping.setDegree(SensorPingBean.LOCKED);
            ping.setDisplacement(target.getShipStats().getDisplacement());
            ping.setPower(target.getShipStats().getPowerProduced());
            ping.setType(target.getShipStats().getCraftType());
        }
        else if ((result&TaskLogic.RES_SOME_TRUTH) != 0)
        {
            ping.setDisplacement(target.getShipStats().getDisplacement());
            ping.setPower(target.getShipStats().getPowerProduced());
        }
        return sensor.getPhylum() == SensorToScanBean.P_ACTIVE;
    }

    /**
     * @param cbt
     * @param ship
     * @param target
     * @param ping
     * @param off
     * @param def
     */
    private static boolean rollSensorScan(CombatShipBean ship, CombatShipBean target, SensorPingBean ping, int off, int def)
    {
        CombatBean cbt = ship.getSide().getCombat();
        SensorToScanBean sensor = getBestSensorScan(ship);
        int result = TaskLogic.attemptTask(sensor.getDifficulty(), cbt.getRnd(), off, -def);
        if (result == TaskLogic.RES_MARGINAL_FAILURE)
        {
            if (target.getShipStats().getDisplacement() >= 13.5*1000.0)
            {
                ping.setDegree(SensorPingBean.LOCATED);
            }
        }
        else if (result == TaskLogic.RES_MARGINAL_SUCCESS)
        {
            if (target.getShipStats().getDisplacement() >= 13.5*100.0)
            {
                ping.setDegree(SensorPingBean.LOCATED);
            }
        }
        else if (result == TaskLogic.RES_SUCCESS)
        {
            ping.setDegree(SensorPingBean.LOCATED);
        }
        return sensor.getPhylum() == SensorToScanBean.P_ACTIVE;
    }

    private static SensorToScanBean getBestSensorScan(CombatShipBean ship)
    {
        SensorToScanBean best = null;
        for (SensorToScanBean sensor : ship.getSensors())
        {
            if (sensor.getType() != SensorToScanBean.T_SCAN)
                continue;
            if ((best == null) || (sensor.getDifficulty() < best.getDifficulty()))
                best = sensor;
        }
        return best;
    }

    private static SensorToScanBean getBestSensorPinpoint(CombatShipBean ship)
    {
        SensorToScanBean best = null;
        for (SensorToScanBean sensor : ship.getSensors())
        {
            if (sensor.getType() != SensorToScanBean.T_PINPOINT)
                continue;
            if ((best == null) || (sensor.getDifficulty() < best.getDifficulty()))
                best = sensor;
        }
        return best;
    }
    
    public static List<CombatShipBean> getTargets(CombatShipBean ship)
    {
        List<CombatShipBean> targets = new ArrayList<CombatShipBean>();
        for (CombatSideBean otherSide : ship.getSide().getCombat().getSides())
        {
            if (otherSide == ship.getSide())
                continue;
            for (CombatShipBean otherShip : otherSide.getShips())
            {
                SensorPingBean ping = ship.getSensorPings().get(otherShip);
                if ((ping == null) || (ping.getDegree() < SensorPingBean.LOCKED))
                    targets.add(otherShip);
            }
        }
        return targets;
    }

    /**
     * @param pointOfView
     * @param ship
     * @return
     */
    public static SensorPingBean getBestPing(CombatSideBean side, CombatShipBean target)
    {
        SensorPingBean best = null;
        for (CombatShipBean ship : side.getShips())
        {
            SensorPingBean ping = ship.getSensorPings().get(target);
            if ((best == null) || ((ping != null) && (ping.getDegree() > best.getDegree())))
                best = ping;
        }
        return best;
    }
}
