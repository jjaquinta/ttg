/*
 * Created on Feb 5, 2005
 *
 */
package jo.ttg.ship.logic.cbt;

import jo.ttg.logic.RandLogic;
import jo.ttg.ship.beans.cbt.CombatNotifyEvent;
import jo.ttg.ship.beans.cbt.CombatShipBean;
import jo.ttg.ship.beans.cbt.SensorToScanBean;
import jo.ttg.ship.beans.cbt.WeaponToFireBean;
import jo.ttg.ship.beans.comp.TurretPulseLaser;
import jo.ttg.ship.beans.comp.Weapon;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.StringUtils;


/**
 * @author Jo
 *
 */
public class DamageLogic
{

    /**
     * @param ship
     * @param wtf
     * @param target
     */
    public static void doDamage(CombatShipBean ship, WeaponToFireBean wtf, CombatShipBean target)
    {
        int hits = workOutHits(ship, wtf, target);
        int criticalHits = workOutCriticalHits(wtf, target);
        while (hits-- > 0)
            doDamageHit(wtf, target);
        while (criticalHits-- > 0)
            doDamageCritical(target, 0);
    }
    
    private static final String[] mSurfaceExplosionWeapons = {
            "Fusion", "Plasma", "Laser", "Missile", "PA", "Particle", "Disintegrator"  
    };
    private static final String[] mRadiationWeapons = {
            "PA", "Particle", "Meson",  
    };
    private static final String[] mInteriorExplosionWeapons = {
            "Meson", "Disintegrator"  
    };
    
    private static void doDamageHit(WeaponToFireBean wtf, CombatShipBean target)
    {
        int dm = 0;
        if (target.getShipStats().getArmorClass() > 40)
            dm -= (target.getShipStats().getArmorClass() - 40)/3;
        if (wtf.getFactor() >= 10)
            dm += 4;
        // if nuclear missile += 6;
        if (wtf.getWeapon().getComponent() instanceof TurretPulseLaser)
            dm += 2;
        String weapName = wtf.getWeapon().getName();
        if (StringUtils.indexOf(weapName, mSurfaceExplosionWeapons) >= 0)
            doDamageSurfaceExplosion(target, dm);
        if (StringUtils.indexOf(weapName, mRadiationWeapons) >= 0)
            doDamageRadiation(target, dm);
        if (StringUtils.indexOf(weapName, mInteriorExplosionWeapons) >= 0)
            doDamageInteriorExplosion(target, dm);
    }
    
    private static int workOutHits(CombatShipBean ship, WeaponToFireBean wtf, CombatShipBean target)
    {
        int hits = 1;
        if (wtf.getPhylum() == WeaponToFireBean.SPINAL)
        {
            int extraHits = wtf.getFactor() - 9;
            if (wtf.getType() != Weapon.TYPE_MESON)
                extraHits -= (target.getShipStats().getArmorClass() - 40)/3;
            if (extraHits > 0)
                hits += extraHits;
        }
        return hits;
    }
    
    private static final int[] mCriticalHitDistribution =
    {
             0, 100, 200, 300, 400, 500, 600, 700, 800, 900,
             1000, 2000, 3000, 4000, 5000, 6000, 7000, 8000, 9000,
             10000, 20000, 30000, 40000, 50000, 75000, 
             100000, 200000, 300000, 400000, 500000, 700000, 900000
    };
    
    private static int workOutCriticalHits(WeaponToFireBean wtf, CombatShipBean target)
    {
        int baseUCP = 0;
        int tons = (int)(target.getShipStats().getDisplacement()/13.5);
        for (int i = 1; i < mCriticalHitDistribution.length; i++)
            if (tons >= mCriticalHitDistribution[i])
                baseUCP++;
            else
                break;
        int hits = wtf.getFactor() - baseUCP;
        if (wtf.getType() != Weapon.TYPE_MESON)
            hits -= (target.getShipStats().getArmorClass() - 40)/3;
        if (hits > 0)
            return hits;
        else
            return 0;
    }

    private static final String[] mDamageSurfaceExplosion =
    {
            "Weapon-1",
            "Weapon-1",
            "Fuel-1",
            "Weapon-1",
            "Weapon-1",
            "Fuel-1",
            "Weapon-1",
            "Weapon-1",
            "Fuel-1",
            "Maneuver-1",
            "Weapon-2",
            "Fuel-2",
            "Maneuver-1",
            "Weapon-3",
            "Fuel-3",
            "Maneuver-2",
            "Interior Explosion",
            "Interior Explosion",
            "Interior Explosion",
    };
    
    /**
     * @param target
     * @param dm
     */
    private static void doDamageSurfaceExplosion(CombatShipBean target, int dm)
    {
        int roll = RandLogic.D(target.getSide().getCombat().getRnd(), 2) + dm - 3;
        if (roll < 0)
            return; // no effect
        if (roll >= mDamageSurfaceExplosion.length)
            doDamageCritical(target, 0);
        if (mDamageSurfaceExplosion[roll].equals("Interior Explosion"))
            doDamageInteriorExplosion(target, 0);
        else
            resolveDamage(target, mDamageSurfaceExplosion[roll]);
    }
    
    private static final String[] mDamageRadiation =
    {
            "Weapon-1",
            "Weapon-1",
            "Weapon-1",
            "Weapon-1",
            "Weapon-2",
            "Sensor-1",
            "Computer-1",
            "Weapon-2",
            "Sensor-2",
            "Computer-2",
            "Weapon-4",
            "Sensor-2",
            "Computer-1",
            "Computer-2",
            "Crew-1",
            "Computer-3",
            "Crew-1",
            "Computer-4",
            "Crew-2",
    };
    
    /**
     * @param target
     * @param dm
     */
    private static void doDamageRadiation(CombatShipBean target, int dm)
    {
        int roll = RandLogic.D(target.getSide().getCombat().getRnd(), 2) + dm - 3;
        if (roll < 0)
            return; // no effect
        if (roll >= mDamageRadiation.length)
            doDamageCritical(target, 0);
        resolveDamage(target, mDamageRadiation[roll]);
    }
    
    private static final String[] mDamageInteriorExplosion =
    {
            "Power Plant-1",
            "Jump-1",
            "Screens-1",
            "Sensors-1",
            "Power Plant-1",
            "Jump-1",
            "Screens-1",
            "Computer-1",
            "Power Plant-1",
            "Sensors-2",
            "Computer-1",
            "Crew-1",
            "Power Plant-2",
            "Jump-2",
            "Screens-3",
            "Sensors-3",
            "Fuel-10",
    };

    /**
     * @param target
     * @param dm
     */
    private static void doDamageInteriorExplosion(CombatShipBean target, int dm)
    {
        int roll = RandLogic.D(target.getSide().getCombat().getRnd(), 2) + dm - 3;
        if (roll < 0)
            return; // no effect
        if (roll >= mDamageInteriorExplosion.length)
            doDamageCritical(target, 0);
        resolveDamage(target, mDamageInteriorExplosion[roll]);
    }
    
    private static final String[] mDamageCritical =
    {
            "Ship Vaporized",
            "Bridge Destroyed",
            "Computer-10",
            "Maneuver-10",
            "Screen-10",
            "Jump-10",
            "Sensors-10",
            "Power Plant-10",
            "Crew-1",
            "Spinal Mount",
            "Frozen Watch",
    };

    /**
     * @param target
     * @param dm
     */
    private static void doDamageCritical(CombatShipBean target, int dm)
    {
        int roll = RandLogic.D(target.getSide().getCombat().getRnd(), 2) - 2;
        if (roll < 0)
            return; // no effect
        if (roll >= mDamageCritical.length)
            doDamageCritical(target, 0); // try again
        resolveDamage(target, mDamageCritical[roll]);
    }
    
    private static void resolveDamage(CombatShipBean target, String damage)
    {
        CombatLogic.notify(target.getSide().getCombat(), CombatNotifyEvent.DAMAGE, target, damage);
        int val = 0;
        int o = damage.indexOf("-");
        if (o > 0)
        {
            val = IntegerUtils.parseInt(damage.substring(o+1));
            damage = damage.substring(0, o);
        }
        if (damage.equals("Bridge Destroyed"))
            target.setBridgeDestroyed(true);
        else if (damage.equals("Computer"))
            target.setDamageComputer(target.getDamageComputer() + val);
        else if (damage.equals("Crew"))
            target.setDamageCrew(target.getDamageCrew() + val);
        else if (damage.equals("Fuel"))
            target.setDamageFuel(target.getDamageFuel() + val);
        else if (damage.equals("Jump"))
            target.setDamageJump(target.getDamageJump() + val);
        else if (damage.equals("Maneuver"))
            target.setDamageManeuver(target.getDamageManeuver() + val);
        else if (damage.equals("Power Plant"))
            target.setDamagePowerPlant(target.getDamagePowerPlant() + val);
        else if (damage.equals("Screen"))
        {
            if (target.getMesonFactor() > 0)
                target.setMesonFactor(target.getMesonFactor() - val);
            else if (target.getProtonFactor() > 0)
                target.setProtonFactor(target.getProtonFactor() - val);
            else if (target.getNuclearDamperFactor() > 0)
                target.setNuclearDamperFactor(target.getNuclearDamperFactor() - val);
        }
        else if (damage.equals("Sensors"))
        {
            while (val-- > 0)
            {
                int tot = target.getSensors().size();
                if (tot == 0)
                    break;
                int roll = RandLogic.rand(target.getSide().getCombat().getRnd())%tot;
                SensorToScanBean sensor = (SensorToScanBean)target.getSensors().get(roll);
                target.getSensors().remove(sensor);
                target.fireMonotonicPropertyChange("sensors");
                target.getDamageSensors().add(sensor);
                target.fireMonotonicPropertyChange("damageSensors");
            }
    	}
        else if (damage.equals("Spinal Mount"))
        {
            for (WeaponToFireBean wtf : target.getWeapons())
            {
                if (wtf.getPhylum() == WeaponToFireBean.SPINAL)
                {
                    target.getWeapons().remove(wtf);
                    target.fireMonotonicPropertyChange("weapons");
                    target.getWeaponsLeftToUse().remove(wtf);
                    target.fireMonotonicPropertyChange("weaponsLeftToUse");
                    target.getDamageWeapons().add(wtf);
                    target.fireMonotonicPropertyChange("damageWeapons");
                    break;
                }
            }
        }
        else if (damage.equals("Weapon"))
        {
            while (val-- > 0)
            {
                int tot = target.getWeapons().size() + target.getDefenses().size();
                if (tot == 0)
                    break;
                int roll = RandLogic.rand(target.getSide().getCombat().getRnd())%tot;
                if (roll < target.getWeapons().size())
                {
                    WeaponToFireBean wtf = (WeaponToFireBean)target.getWeapons().get(roll);
                    target.getWeapons().remove(wtf);
                    target.fireMonotonicPropertyChange("weapons");
                    target.getWeaponsLeftToUse().remove(wtf);
                    target.fireMonotonicPropertyChange("weaponsLeftToUse");
                    target.getDamageWeapons().add(wtf);
                    target.fireMonotonicPropertyChange("damageWeapons");
                }
                else
                {
                    roll -= target.getWeapons().size();
                    WeaponToFireBean wtf = (WeaponToFireBean)target.getDefenses().get(roll);
                    target.getDefenses().remove(wtf);
                    target.getDefensesLeftToUse().remove(wtf);
                    target.fireMonotonicPropertyChange("defensesLeftToUse");
                    target.getDamageWeapons().add(wtf);
                    target.fireMonotonicPropertyChange("damageWeapons");
                }
            }
        }
        else if (damage.equals("Ship Vaporized"))
        {
            target.getSide().getShipsDestroyed().add(target);
            target.getSide().getShips().remove(target);
            target.getSide().getShipsLeftToMove().remove(target);
        }
        else
            System.err.println("Unknown damage resolution: "+damage);
    }
}
