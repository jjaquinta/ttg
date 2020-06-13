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
import jo.ttg.logic.chr.TaskLogic;
import jo.ttg.ship.beans.cbt.CombatNotifyEvent;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.SensorPingBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;
import jo.ttg.ship.beans.comp.Weapon;
import jo.util.utils.obj.IntegerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WeaponLogic
{

    /**
     * @param cbt
     * @param side
     * @param ship
     * @param wtf
     * @param target
     */
    public static boolean doWeaponAttack(CombatShipBean ship, WeaponToFireBean wtf, CombatShipBean target, CombatCallback callback)
    {
        if (!doWeaponHit(ship, wtf, target))
            return false;
        // try to penetrate active defenses
        List<WeaponToFireBean> activeDefenses = getActiveDefenses(wtf, target);
        while (activeDefenses.size() > 0)
        {
            WeaponToFireBean activeDefense = callback.pickDefenseWeapon(target, wtf, activeDefenses);
            if (activeDefense == null)
                break;
            if (activeDefense.getType() < 0)
                target.getDefensesLeftToUse().remove(activeDefense);
            else
                target.getWeaponsLeftToUse().remove(activeDefense);
            activeDefenses.remove(activeDefense);
            if (!doWeaponPenetrateActive(ship, target, wtf, activeDefense))
                return false;
        }
        // try to penetrate passive defenses
        if (!doWeaponPenetratePassives(ship, wtf, target))
            return false;
        // we got through!
        return true;
    }
    
    private static boolean doWeaponHit(CombatShipBean ship, WeaponToFireBean wtf, CombatShipBean target)
    {
        // try to hit
        int off = CombatShipLogic.getComputer(ship);
        off += getWeaponDM(wtf);
        off += getRangeDM(wtf, target);
        int def = target.getShipStats().getDefDM();
        boolean success = TaskLogic.attemptFatefulTask(TaskLogic.DIFF_DIFFICULT, ship.getSide().getCombat().getRnd(), +off, -def);
        CombatLogic.notify(target.getSide().getCombat(), success ? CombatNotifyEvent.HIT : CombatNotifyEvent.MISS, target, wtf);
        return success;
    }
    
    private static boolean doWeaponPenetrateActive(CombatShipBean off, CombatShipBean def, WeaponToFireBean offWeap, WeaponToFireBean defWeap)
    {
        // try to defend
        int offDM = CombatShipLogic.getComputer(off);
        int defDM = CombatShipLogic.getComputer(def);
        defDM -= getActiveDefenseDM(offWeap, defWeap);
        boolean success = TaskLogic.attemptFatefulTask(TaskLogic.DIFF_DIFFICULT, off.getSide().getCombat().getRnd(), +offDM, -defDM);
        CombatLogic.notify(off.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, def, defWeap);
        return success;
    }
    
    private static boolean doWeaponPenetratePassives(CombatShipBean ship, WeaponToFireBean wtf, CombatShipBean target)
    {
        int dm;
        boolean success;
        switch (wtf.getType())
        {
            case Weapon.TYPE_DISINTEGRATOR:
                if (target.getNuclearDamperFactor() > 0)
                {
                    dm = getPassiveDefenseDisintegratorDM(wtf, target.getNuclearDamperFactor());
                    success = doWeaponPenetratePassive(ship, target, wtf, dm);
                    CombatLogic.notify(ship.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, target, "Nuclear Damper");
                    return success;
                }
                break;
            case Weapon.TYPE_MESON:
                if (target.getMesonFactor() > 0)
                {
                    dm = getPassiveDefenseMesonScreenDM(wtf, target.getMesonFactor());
                    success = doWeaponPenetratePassive(ship, target, wtf, dm);
                    CombatLogic.notify(ship.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, target, "Meson Screen");
                    if (!success)
                        return false;
                }
                dm = getPassiveDefenseMesonConfigDM(wtf, IntegerUtils.parseInt(target.getShipStats().getConfig()));
            	success = doWeaponPenetratePassive(ship, target, wtf, dm);
                CombatLogic.notify(ship.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, target, "Ship Configuration");
            	return success;
            case Weapon.TYPE_MISSILE:
                if (target.getNuclearDamperFactor() > 0)
                {
                    dm = getPassiveDefenseMissileDM(wtf, target.getNuclearDamperFactor());
                    success = doWeaponPenetratePassive(ship, target, wtf, dm);
                    CombatLogic.notify(ship.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, target, "Nuclear Damper");
                    if (!success)
                        return false;
                }
	            if (target.getProtonFactor() > 0)
	            {
	                dm = getPassiveDefenseMissileDM(wtf, target.getProtonFactor());
                	success = doWeaponPenetratePassive(ship, target, wtf, dm);
                    CombatLogic.notify(ship.getSide().getCombat(), success ? CombatNotifyEvent.PENETRATE : CombatNotifyEvent.DEFLECT, target, "Proton Screen");
                	return success;
	            }
	            break;
        }
        return true;
    }
    
    private static boolean doWeaponPenetratePassive(CombatShipBean off, CombatShipBean def, WeaponToFireBean wtf, int screenDM)
    {
        int offDM = CombatShipLogic.getComputer(off);
        int defDM = CombatShipLogic.getComputer(def);
        defDM -= screenDM;
        return TaskLogic.attemptFatefulTask(TaskLogic.DIFF_DIFFICULT, off.getSide().getCombat().getRnd(), +offDM, -defDM);
    }
    
    private static int getRangeDM(WeaponToFireBean wtf, CombatShipBean target)
    {
        boolean isBeyondPlanetary = getRange(wtf.getShip(), target) > 50000.0;
        String name = wtf.getWeapon().getName();
        if (name.indexOf("Laser") >= 0)
            return isBeyondPlanetary ? -1 : 0;
        if (name.indexOf("Missile") >= 0)
            return isBeyondPlanetary ? +1 : 0;
        if (name.indexOf("Meson") >= 0)
            return isBeyondPlanetary ? -1 : 0;
        if (name.indexOf("Fusion") >= 0)
            return isBeyondPlanetary ? -999 : 0;
        if (name.indexOf("Plasma") >= 0)
            return isBeyondPlanetary ? -999 : 0;
        if (name.indexOf("Tractor") >= 0)
            return isBeyondPlanetary ? -999 : 0;
        return 0;
    }

    // return range in kilometers
    private static double getRange(CombatShipBean ship1, CombatShipBean ship2)
    {
        double dist = LocLogic.dist(ship1.getLocation(), ship2.getLocation());
        return dist*25000.0;
    }
    
    private static int getWeaponDM(WeaponToFireBean wtf)
    {
        int factor = getFactor(wtf);
        switch (wtf.getWeapon().getComponent().getType())
        {
            case Weapon.TYPE_DISINTEGRATOR:
                return getDisintegratorDM(factor);
            case Weapon.TYPE_JUMPPROJECTOR:
                return getJumpProjectorDM(factor);
            case Weapon.TYPE_TRACTOR:
                return getTractorDM(factor);
            case Weapon.TYPE_BEAM:
                return getBeamDM(factor);
            case Weapon.TYPE_MESON:
                return getMesonDM(factor);
            case Weapon.TYPE_MISSILE:
                return getMissileDM(factor);
            case Weapon.TYPE_PA:
                return getPADM(factor);
        }
        return 0;
    }
    
    /**
     * @param factor
     * @return
     */
    private static int getDisintegratorDM(int factor)
    {
        switch (factor)
        {
            case 1:
                return 5;
            case 2: case 3:
                return 6;
            case 4: case 5:
                return 7;
            case 6: case 7:
                return 8;
            case 8: case 9:
                return 9;
            case 10: case 11:
                return 10;
            case 12: case 13:
                return 11;
            case 14: case 15:
                return 12;
        }
        return 0;
    }

    /**
     * @param factor
     * @return
     */
    private static int getJumpProjectorDM(int factor)
    {
        switch (factor)
        {
            case 10: case 11:
                return 7;
        }
        return 0;
    }

    /**
     * @param factor
     * @return
     */
    private static int getTractorDM(int factor)
    {
        switch (factor)
        {
            case 1: case 2:
                return 5;
            case 3: case 4:
                return 6;
            case 5: case 6:
                return 7;
            case 7: case 8:
                return 8;
            case 9:
                return 9;
        }
        return 0;
    }

    /**
     * @param factor
     * @return
     */
    private static int getBeamDM(int factor)
    {
        switch (factor)
        {
            case 1:
                return 3;
            case 2: case 3:
                return 4;
            case 4: case 5:
                return 5;
            case 6: case 7:
                return 6;
            case 8: case 9:
                return 7;
            case 10: case 11:
                return 8;
            case 12:
                return 9;
            case 13:
                return 10;
        }
        return 0;
    }

    /**
     * @param factor
     * @return
     */
    private static int getMesonDM(int factor)
    {
        switch (factor)
        {
            case 1: case 2:
                return 4;
            case 3:
                return 5;
            case 4: case 5: case 6:
                return 6;
            case 7: case 8: case 9:
                return 7;
            case 10: case 11: case 12:
                return 8;
            case 13: case 14: case 15: case 16: case 17:
            case 18: case 19: case 20: case 21: case 22:
            case 23: case 24: case 25: case 26: case 27:
            case 28:
                return 9;
            case 29: case 30: case 31: case 32: case 33:
            case 34:
                return 10;
        }
        return 0;
    }

    /**
     * @param factor
     * @return
     */
    private static int getMissileDM(int factor)
    {
        switch (factor)
        {
            case 1: case 2:
                return 4;
            case 3: case 4:
                return 5;
            case 5: case 6:
                return 6;
            case 7: case 8:
                return 7;
            case 9: case 10:
                return 8;
            case 11: case 12:
                return 9;
        }
        return 0;
    }


    /**
     * @param factor
     * @return
     */
    private static int getPADM(int factor)
    {
        switch (factor)
        {
            case 1:
                return 2;
            case 2:
                return 3;
            case 3: case 4:
                return 4;
            case 5: case 6:
                return 5;
            case 7: case 8:
                return 6;
            case 9:
                return 7;
            case 10: case 11: case 12: case 13: case 14:
                return 8;
            case 15: case 16: case 17: case 18: case 19:
                return 9;
            case 20: case 21: case 22: case 23: case 24:
                return 10;
            case 25: case 26:
                return 11;
        }
        return 0;
    }

    private static int getFactor(WeaponToFireBean wtf)
    {
        if (wtf.getPhylum() == WeaponToFireBean.TURRET)
            return wtf.getWeapon().getTurretFactor();
        if (wtf.getPhylum() == WeaponToFireBean.BAY)
            return wtf.getWeapon().getBayFactor();
        if (wtf.getPhylum() == WeaponToFireBean.SPINAL)
            return wtf.getWeapon().getSpineFactor();
        return 0;
    }
    
    private static List<WeaponToFireBean> getActiveDefenses(WeaponToFireBean wtf, CombatShipBean ship)
    {
        List<WeaponToFireBean> candidates = new ArrayList<WeaponToFireBean>();
        candidates.addAll(ship.getDefensesLeftToUse());
        candidates.addAll(ship.getWeaponsLeftToUse());
        List<WeaponToFireBean> ret = new ArrayList<WeaponToFireBean>();
        int attType = wtf.getType();
        for (WeaponToFireBean def : candidates)
        {
            int defType = def.getType();
            switch (attType)
            {
                case Weapon.TYPE_DISINTEGRATOR:
                    break;
                case Weapon.TYPE_JUMPPROJECTOR:
                    if (defType == Weapon.TYPE_JUMPDAMPER)
                        ret.add(def);
                    break;
                case Weapon.TYPE_TRACTOR:
                    if (defType == Weapon.TYPE_REPULSOR)
                        ret.add(def);
                    break;
                case Weapon.TYPE_BEAM:
                    if (defType == Weapon.TYPE_SAND)
                        ret.add(def);
                    break;
                case Weapon.TYPE_MESON:
                    break;
                case Weapon.TYPE_MISSILE:
                    if ((defType == Weapon.TYPE_DISINTEGRATOR)
                            || (defType == Weapon.TYPE_SAND)
                            || (defType == Weapon.TYPE_BEAM)
                            || (defType == Weapon.TYPE_REPULSOR))
                        		ret.add(def);
                    break;
                case Weapon.TYPE_PA:
                    break;
            }
        }
        return ret;
    }
    
    private static int getActiveDefenseDM(WeaponToFireBean off, WeaponToFireBean def)
    {
        switch (off.getType())
        {
            case Weapon.TYPE_JUMPPROJECTOR:
                return getActiveDefenseJumpProjectorDM(off, def);
            case Weapon.TYPE_TRACTOR:
                return getActiveDefenseTractorDM(off, def);
            case Weapon.TYPE_BEAM:
                return getActiveDefenseBeamDM(off, def);
            case Weapon.TYPE_MISSILE:
                return getActiveDefenseMissileDM(off, def);
        }
        return 0;
    }
    
    /**
     * @param off
     * @param def
     * @return
     */
    private static int getActiveDefenseJumpProjectorDM(WeaponToFireBean off, WeaponToFireBean def)
    {
        if ((off.getFactor() == 10) && (def.getFactor() == 2))
            return 1;
        else
            return 2;
    }
    
    /**
     * @param off
     * @param def
     * @return
     */
    private static int getActiveDefenseTractorDM(WeaponToFireBean off, WeaponToFireBean def)
    {
        int dm = off.getFactor() + 8;
        dm -= def.getFactor() - 1;
        if (dm > 11)
            dm = 11;
        return dm;
    }

    /**
     * @param off
     * @param def
     * @return
     */
    private static int getActiveDefenseBeamDM(WeaponToFireBean off, WeaponToFireBean def)
    {
        int dm = off.getFactor() + 4;
        dm -= def.getFactor() - 1;
        if (dm > 11)
            dm = 11;
        return dm;
    }

    /**
     * @param off
     * @param def
     * @return
     */
    private static int getActiveDefenseMissileDM(WeaponToFireBean off, WeaponToFireBean def)
    {
        int dm;
        if (def.getType() != Weapon.TYPE_REPULSOR)
            dm = off.getFactor() + 5;
        else
            dm = off.getFactor() - 5;
        dm -= def.getFactor() - 1;
        if (dm > 11)
            dm = 11;
        return dm;
    }

    /**
     * @param off
     * @param def
     * @return
     */
    private static int getPassiveDefenseDisintegratorDM(WeaponToFireBean off, int defFactor)
    {
        int dm = off.getFactor()/2 + 2;
        dm -= (defFactor - 1)/2;
        return dm;
    }

    /**
     * @param off
     * @param def
     * @return
     */
    private static int getPassiveDefenseMesonScreenDM(WeaponToFireBean off, int defFactor)
    {
        int dm = off.getFactor()/2 - 5;
        dm -= (defFactor - 1)/2;
        return dm;
    }

    private static int[] mPassiveDefenseMesonConfigDM =
    { 0, 2, 3, 4, 7, 8, 6, -1, 10, 0, };
    
    /**
     * @param off
     * @param def
     * @return
     */
    private static int getPassiveDefenseMesonConfigDM(WeaponToFireBean off, int config)
    {
        int dm = off.getFactor()/3 - 3;
        dm += mPassiveDefenseMesonConfigDM[config];
        return dm;
    }
    
    /**
     * @param off
     * @param def
     * @return
     */
    private static int getPassiveDefenseMissileDM(WeaponToFireBean off, int defFactor)
    {
        int dm = off.getFactor();
        dm -= (defFactor - 1);
        return dm;
    }
    
    public static List<CombatShipBean> getTargets(CombatShipBean ship)
    {
        CombatSideBean shipSide = ship.getSide(); 
        List<CombatShipBean> targets = new ArrayList<CombatShipBean>();
        for (CombatSideBean otherSide : ship.getSide().getCombat().getSides())
        {
            if (otherSide != shipSide)
                for (CombatShipBean otherShip : otherSide.getShips())
                {
                    SensorPingBean ping = ship.getSensorPings().get(otherShip);
                    if ((ping != null) && (ping.getDegree() == SensorPingBean.LOCKED))
                        targets.add(otherShip);
                }
        }
        return targets;
    }
}
