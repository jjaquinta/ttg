/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic.cbt;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jo.ttg.beans.LocBean;
import jo.ttg.logic.LocLogic;
import jo.ttg.ship.beans.cbt.CombatBean;
import jo.ttg.ship.beans.cbt.CombatNotifyEvent;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CombatRoundLogic
{
    private static FleetTacticsComparator	mSideDeterminator = new FleetTacticsComparator();
    
    public static void fightCombatRound(CombatBean cbt, CombatCallback callback)
    {
        startOfRound(cbt);
        // do each ship in turn
        boolean doneAny;
        do
        {
            doneAny = false;
            for (CombatSideBean side : cbt.getSidesLeftToMove())
            {
                if (side.getShipsLeftToMove().size() > 0)
                {
                    fightCombatRound(side, callback);
                    doneAny = true;
                }
            }
        } while (doneAny);
        endOfRound(cbt);
    }
    
    private static void fightCombatRound(CombatSideBean side, CombatCallback callback)
    {
        CombatShipBean ship;
        ship = callback.pickShipToFight(side);
        if (ship == null)
            return;
        side.getShipsLeftToMove().remove(ship);
        if (ship.getDamageFuel() >= 10)
            return;
//        int weapons = 0;
//        for  (ShipStatsWeapon ssw : ship.getShipStats().getWeapons())
//            weapons += ssw.getTurretBatteries() + ssw.getBayBatteries() + ssw.getSpineBatteries();
        if (!ship.isBridgeDestroyed())
        {
            doShipAccelerate(ship, callback);
            doShipMove(ship, callback);
            CombatLogic.notify(side.getCombat(), CombatNotifyEvent.MOVE, ship, null);
        }
        doSensorScans(ship, callback);
        if (ship.getDamageCrew() <= 5)
            doWeaponAttacks(ship, callback);
        CombatLogic.notify(side.getCombat(), CombatNotifyEvent.DONE, ship, null);
    }
    
    /**
     * @param cbt
     * @param side
     * @param ship
     * @param callback
     */
    private static void doShipAccelerate(CombatShipBean ship, CombatCallback callback)
    {
        double delta = callback.shipAccelerateThisTurn(ship);
        if (delta <= 0)
            return;
        delta = Math.min(delta, CombatShipLogic.getManeuver(ship));
        double newVelocity = ship.getVelocity() + delta;
        newVelocity = Math.max(newVelocity, 0);
        ship.setVelocity(newVelocity);        
    }
    
    /**
     * @param cbt
     * @param side
     * @param ship
     * @param callback
     */
    private static void doShipMove(CombatShipBean ship, CombatCallback callback)
    {
        LocBean newLoc = callback.shipMoveThisTurn(ship);
        if (newLoc == null)
            return;
        double delta = LocLogic.dist(ship.getLocation(), newLoc);
        if (delta > ship.getVelocity())
        {
            LocBean vector = LocLogic.diff(newLoc, ship.getLocation());
            LocLogic.makeNorm(vector);
            LocLogic.mult(vector, ship.getVelocity(), ship.getVelocity(), ship.getVelocity());
            newLoc = LocLogic.add(ship.getLocation(), vector);
        }
        ship.setLocation(newLoc);
    }
    
    /**
     * @param cbt
     * @param side
     * @param ship
     * @param callback
     */
    private static void doSensorScans(CombatShipBean ship, CombatCallback callback)
    {
        CombatShipBean target;
        List<CombatShipBean> targets = SensorLogic.getTargets(ship);
        if (targets.size() == 0)
            return;
        
        target = callback.pickScanTarget(ship, targets, false);
        if (target == null)
            return;
        SensorLogic.doSensorScan(ship, target);
        targets = SensorLogic.getTargets(ship);
        if (targets.size() == 0)
            return;
        if (ship.getWeapons().size() == 0)
        {
            target = callback.pickScanTarget(ship, targets, false);
            if (target == null)
                return;
            SensorLogic.doSensorScan(ship, target);
            return;
        }
        while (ship.getWeaponShots() > 0)
        {
            target = callback.pickScanTarget(ship, targets, true);
            if (target == null)
                return;
            SensorLogic.doSensorScan(ship, target);
            ship.setWeaponShots(ship.getWeaponShots() - 1);
            targets = SensorLogic.getTargets(ship);
            if (targets.size() == 0)
                return;
        }
    }
    
    /**
     * @param cbt
     * @param side
     * @param ship
     * @param callback
     */
    private static void doWeaponAttacks(CombatShipBean ship, CombatCallback callback)
    {
        CombatShipBean target;
        List<CombatShipBean> targets = WeaponLogic.getTargets(ship);
        if (targets.size() == 0)
            return;
        
        while ((ship.getWeaponShots() > 0) && (ship.getWeaponsLeftToUse().size() > 0))
        {
            WeaponToFireBean wtf = callback.pickAttackWeapon(ship, ship.getWeaponsLeftToUse());
            if (wtf == null)
                return;
            target = callback.pickAttackTarget(ship, targets);
            if (target == null)
                return;
            ship.getWeaponsLeftToUse().remove(wtf);
            ship.fireMonotonicPropertyChange("weaponsLeftToUse");
            ship.setWeaponShots(ship.getWeaponShots() - 1);
            if (WeaponLogic.doWeaponAttack(ship, wtf, target, callback))
                DamageLogic.doDamage(ship, wtf, target);
        }
    }
    
    private static void startOfRound(CombatBean cbt)
    {
        cbt.setRound(cbt.getRound() + 1);
        cbt.getSidesLeftToMove().clear();
        cbt.getSidesLeftToMove().addAll(cbt.getSides());
        Collections.sort(cbt.getSidesLeftToMove(), mSideDeterminator);
        for (CombatSideBean side : cbt.getSidesLeftToMove())
            startOfRound(side);
    }
    
    private static void startOfRound(CombatSideBean side)
    {
        side.getShipsLeftToMove().clear();
        side.getShipsLeftToMove().addAll(side.getShips());
        for (CombatShipBean ship : side.getShipsLeftToMove())
            startOfRound(ship);
    }
    
    private static void startOfRound(CombatShipBean ship)
    {
        ship.getWeaponsLeftToUse().clear();
        ship.getWeaponsLeftToUse().addAll(ship.getWeapons());
        ship.fireMonotonicPropertyChange("weaponsLeftToUse");
        ship.getDefensesLeftToUse().clear();
        ship.getDefensesLeftToUse().addAll(ship.getDefenses());
        ship.fireMonotonicPropertyChange("defensesLeftToUse");
        ship.setWeaponShots(ship.getWeaponsLeftToUse().size());
    }
    
    private static void endOfRound(CombatBean cbt)
    {
    }
}

class FleetTacticsComparator implements Comparator<CombatSideBean>
{
    @Override
    public int compare(CombatSideBean arg0, CombatSideBean arg1)
    {
        return arg0.getFleetTactics() - arg1.getFleetTactics();
    }    
}
