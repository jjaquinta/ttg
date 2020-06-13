/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.logic.cbt;

import java.util.List;

import jo.ttg.beans.LocBean;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.CombatSideBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface CombatCallback
{

    /**
     * @param cbt
     * @param side
     * @return
     */
    public CombatShipBean pickShipToFight(CombatSideBean side);

    /**
     * @param cbt
     * @param side
     * @param ship
     * @return
     */
    public double shipAccelerateThisTurn(CombatShipBean ship);

    /**
     * @param cbt
     * @param side
     * @param ship
     * @return
     */
    public LocBean shipMoveThisTurn(CombatShipBean ship);

    /**
     * @param cbt
     * @param side
     * @param ship
     * @param targets
     * @param b
     * @return
     */
    public CombatShipBean pickScanTarget(CombatShipBean ship, List<CombatShipBean> targets, boolean willUseUpWeapon);

    /**
     * @param cbt
     * @param side
     * @param ship
     * @param weaponsLeftToUse
     * @return
     */
    public WeaponToFireBean pickAttackWeapon(CombatShipBean ship, List<WeaponToFireBean> weaponsLeftToUse);

    /**
     * @param cbt
     * @param side
     * @param ship
     * @param targets
     * @return
     */
    public CombatShipBean pickAttackTarget(CombatShipBean ship, List<CombatShipBean> targets);

    /**
     * @param target
     * @param wtf
     * @param activeDefenses
     * @return
     */
    public WeaponToFireBean pickDefenseWeapon(CombatShipBean target, WeaponToFireBean wtf, List<WeaponToFireBean> activeDefenses);
}
