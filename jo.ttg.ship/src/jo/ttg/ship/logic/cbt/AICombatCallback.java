/*
 * Created on Feb 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic.cbt;

import java.util.List;

import jo.ttg.beans.LocBean;
import jo.ttg.logic.LocLogic;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.SensorPingBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AICombatCallback implements CombatCallback
{

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#pickShipToFight(ttg.beans.ship.cbt.CombatSideBean)
     */
    public CombatShipBean pickShipToFight(CombatSideBean side)
    {
        CombatShipBean ret = (CombatShipBean)side.getShipsLeftToMove().get(0);
        notify("Picking "+getName(ret)+" to fight.");
        return ret;
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#shipAccelerateThisTurn(ttg.beans.ship.cbt.CombatShipBean)
     */
    public double shipAccelerateThisTurn(CombatShipBean ship)
    {
        double ret = ship.getShipStats().getManeuver();
        notify("Ship "+getName(ship)+" accelerating at "+ret+".");
        return ret;
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#shipMoveThisTurn(ttg.beans.ship.cbt.CombatShipBean)
     */
    public LocBean shipMoveThisTurn(CombatShipBean ship)
    {
        CombatShipBean target = null;
        for (CombatSideBean side : ship.getSide().getCombat().getSides())
        {
            if (side != ship.getSide())
            {
                target = findViableTarget(side.getShips());
                if (target != null)
                    break;
            }
        }
        if (target == null)
            return null;
        if (ship.getWeapons().size() == 0)
        {
            LocBean ret = LocLogic.diff(ship.getLocation(), target.getLocation());
            LocLogic.mult(ret, ship.getVelocity(), ship.getVelocity(), ship.getVelocity());
            ship.setFleeing(true);
            notify("Ship "+getName(ship)+" fleeing away from "+ret+" ("+getName(target)+").");
            return ret;
        }
        else
        {
            LocBean ret = LocLogic.diff(target.getLocation(), ship.getLocation());
            notify("Ship "+getName(ship)+" moving towards "+ret+" ("+getName(target)+").");
            return ret;
        }
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#pickScanTarget(ttg.beans.ship.cbt.CombatShipBean, java.util.ArrayList, boolean)
     */
    public CombatShipBean pickScanTarget(CombatShipBean ship, List<CombatShipBean> targets, boolean willUseUpWeapon)
    {
        if (willUseUpWeapon)
        {
            for (SensorPingBean ping : ship.getSensorPings().values())
                if (ping.getDegree() == SensorPingBean.LOCKED)
                    return null;
        }
        CombatShipBean ret = null;
        // preference aggressive located ship
        for (CombatShipBean target : targets)
        {
            if (!isAnyWeapons(target))
                continue;
            SensorPingBean ping = (SensorPingBean)ship.getSensorPings().get(target);
            if ((ping != null) && (ping.getDegree() == SensorPingBean.LOCATED))
            {
                ret = target;
                break;
            }
        }
        if (ret == null)
            // if none located, take any
            for (CombatShipBean target : targets)
            {
                if (!isAnyWeapons(target))
                    continue;
                SensorPingBean ping = (SensorPingBean)ship.getSensorPings().get(target);
                if ((ping == null) || (ping.getDegree() != SensorPingBean.LOCKED))
                {
                    ret = target;
                    break;
                }
            }
        if (ret != null)
            notify("Ship "+getName(ship)+" scanning "+getName(ret)+".");
        return ret;
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#pickAttackWeapon(ttg.beans.ship.cbt.CombatShipBean, java.util.ArrayList)
     */
    public WeaponToFireBean pickAttackWeapon(CombatShipBean ship, List<WeaponToFireBean> weaponsLeftToUse)
    {
        WeaponToFireBean ret = (WeaponToFireBean)weaponsLeftToUse.get(0);
        notify("Ship "+getName(ship)+" firing "+ret.getWeapon().getName()+".");
        return ret;
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#pickAttackTarget(ttg.beans.ship.cbt.CombatShipBean, java.util.ArrayList)
     */
    public CombatShipBean pickAttackTarget(CombatShipBean ship, List<CombatShipBean> targets)
    {
        CombatShipBean ret = findViableTarget(targets);
        if (ret != null)
            notify("Ship "+getName(ship)+" firing at "+getName(ret)+".");
        return ret;
    }

    /* (non-Javadoc)
     * @see ttg.logic.ship.cbt.CombatCallback#pickDefenseWeapon(ttg.beans.ship.cbt.CombatShipBean, ttg.beans.ship.cbt.WeaponToFireBean, java.util.ArrayList)
     */
    public WeaponToFireBean pickDefenseWeapon(CombatShipBean target, WeaponToFireBean wtf, List<WeaponToFireBean> activeDefenses)
    {
        WeaponToFireBean ret = (WeaponToFireBean)activeDefenses.get(0);
        notify("Ship "+getName(target)+" defending with "+ret.getWeapon().getName()+".");
        return ret;
    }
    
    private String getName(CombatShipBean ship)
    {
        return ship.getShipDesign().getName()+" ("+ship.getSide().getName()+")";
    }
    
    private CombatShipBean findViableTarget(List<CombatShipBean> ships)
    {
        for (CombatShipBean ship : ships)
            if (isAnyWeapons(ship))
                return ship;
        return null;
    }
    
    private boolean isAnyWeapons(CombatShipBean ship)
    {
        if (ship.getDamageFuel() >= 10)
            return false;
        if (ship.getDamageCrew() > 5)
            return false;
        if (ship.getWeapons().size() > 0)
            return true;
        return false;
    }
    
    public void notify(String msg)
    {
        // to be overrideen
    }
}
