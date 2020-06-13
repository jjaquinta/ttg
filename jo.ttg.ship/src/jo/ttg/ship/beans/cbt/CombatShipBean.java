/*
 * Created on Jan 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.ship.beans.cbt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.LocBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.chr.CharBean;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.util.beans.PCSBean;

/**
 * @author jgrant
 * 
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class CombatShipBean extends PCSBean implements URIBean
{
    private CombatSideBean                      mSide;

    private String                              mShipName;
    private int                                 mShipType;
    private ShipBean                            mShipDesign;
    private ShipStats                           mShipStats;
    private List<CharBean>                      mCrew;
    private int                                 mShellsExpolsive;
    private int                                 mShellsNuclear;
    private int                                 mShellsSand;
    private LocBean                             mLocation;
    private double                              mVelocity;
    private int                                 mShipTactics;
    private int                                 mShipTacticsUsed;
    private List<WeaponToFireBean>              mWeapons;
    private List<WeaponToFireBean>              mWeaponsLeftToUse;
    private int                                 mWeaponShots;
    private List<WeaponToFireBean>              mDefenses;
    private List<WeaponToFireBean>              mDefensesLeftToUse;
    private List<SensorToScanBean>              mSensors;
    private Map<CombatShipBean, SensorPingBean> mSensorPings;
    private boolean                             mFleeing;
    // damage
    private boolean                             mBridgeDestroyed;
    private int                                 mDamageArmor;
    private int                                 mDamageCrew;
    private int                                 mDamageComputer;
    private int                                 mDamageFuel;
    private int                                 mDamageJump;
    private int                                 mDamageManeuver;
    private int                                 mDamagePowerPlant;
    private List<SensorToScanBean>              mDamageSensors;
    private List<WeaponToFireBean>              mDamageWeapons;
    private int                                 mNuclearDamperFactor;
    private int                                 mMesonFactor;
    private int                                 mBlackGlobeFactor;
    private int                                 mProtonFactor;
    private int                                 mWhiteGlobeFactor;

    public CombatShipBean()
    {
        mCrew = new ArrayList<CharBean>();
        mLocation = new LocBean();
        mWeapons = new ArrayList<WeaponToFireBean>();
        mWeaponsLeftToUse = new ArrayList<WeaponToFireBean>();
        mDefenses = new ArrayList<WeaponToFireBean>();
        mDefensesLeftToUse = new ArrayList<WeaponToFireBean>();
        mSensors = new ArrayList<SensorToScanBean>();
        mSensorPings = new HashMap<CombatShipBean, SensorPingBean>();
        mDamageSensors = new ArrayList<SensorToScanBean>();
        mDamageWeapons = new ArrayList<WeaponToFireBean>();
    }

    public String toString()
    {
        return getShipDesign().getName();
    }

    public List<CharBean> getCrew()
    {
        return mCrew;
    }

    public void setCrew(List<CharBean> crew)
    {
        mCrew = crew;
    }

    public int getShellsExpolsive()
    {
        return mShellsExpolsive;
    }

    public void setShellsExpolsive(int shellsExpolsive)
    {
        queuePropertyChange("shellsExplosive", mShellsExpolsive,
                shellsExpolsive);
        mShellsExpolsive = shellsExpolsive;
        firePropertyChange();
    }

    public int getShellsNuclear()
    {
        return mShellsNuclear;
    }

    public void setShellsNuclear(int shellsNuclear)
    {
        queuePropertyChange("shellsNuclear", mShellsNuclear, shellsNuclear);
        mShellsNuclear = shellsNuclear;
        firePropertyChange();
    }

    public int getShellsSand()
    {
        return mShellsSand;
    }

    public void setShellsSand(int shellsSand)
    {
        queuePropertyChange("shellsSand", mShellsSand, shellsSand);
        mShellsSand = shellsSand;
        firePropertyChange();
    }

    public ShipBean getShipDesign()
    {
        return mShipDesign;
    }

    public void setShipDesign(ShipBean ship)
    {
        queuePropertyChange("shipDesign", mShipDesign, ship);
        mShipDesign = ship;
        firePropertyChange();
    }

    public LocBean getLocation()
    {
        return mLocation;
    }

    public void setLocation(LocBean location)
    {
        queuePropertyChange("location", mLocation, location);
        mLocation = location;
        firePropertyChange();
    }

    public ShipStats getShipStats()
    {
        return mShipStats;
    }

    public void setShipStats(ShipStats shipStats)
    {
        queuePropertyChange("shipStats", mShipStats, shipStats);
        mShipStats = shipStats;
        firePropertyChange();
    }

    public int getShipTactics()
    {
        return mShipTactics;
    }

    public void setShipTactics(int shipTactics)
    {
        queuePropertyChange("shipTactics", mShipTactics, shipTactics);
        mShipTactics = shipTactics;
        firePropertyChange();
    }

    public int getShipTacticsUsed()
    {
        return mShipTacticsUsed;
    }

    public void setShipTacticsUsed(int shipTacticsUsed)
    {
        queuePropertyChange("shipTacticsUsed", mShipTacticsUsed,
                shipTacticsUsed);
        mShipTacticsUsed = shipTacticsUsed;
        firePropertyChange();
    }

    public double getVelocity()
    {
        return mVelocity;
    }

    public void setVelocity(double velocity)
    {
        queuePropertyChange("velocity", mVelocity, velocity);
        mVelocity = velocity;
        firePropertyChange();
    }

    public List<WeaponToFireBean> getWeapons()
    {
        return mWeapons;
    }

    public void setWeapons(List<WeaponToFireBean> weapons)
    {
        mWeapons = weapons;
    }

    public List<WeaponToFireBean> getWeaponsLeftToUse()
    {
        return mWeaponsLeftToUse;
    }

    public void setWeaponsLeftToUse(List<WeaponToFireBean> weaponsLeftToUse)
    {
        mWeaponsLeftToUse = weaponsLeftToUse;
    }

    public Map<CombatShipBean, SensorPingBean> getSensorPings()
    {
        return mSensorPings;
    }

    public void setSensorPings(Map<CombatShipBean, SensorPingBean> sensorPings)
    {
        mSensorPings = sensorPings;
    }

    public int getWeaponShots()
    {
        return mWeaponShots;
    }

    public void setWeaponShots(int weaponShots)
    {
        queuePropertyChange("weaponShots", mWeaponShots, weaponShots);
        mWeaponShots = weaponShots;
        firePropertyChange();
    }

    /**
     * @return Returns the side.
     */
    public CombatSideBean getSide()
    {
        return mSide;
    }

    /**
     * @param side
     *            The side to set.
     */
    public void setSide(CombatSideBean side)
    {
        queuePropertyChange("side", mSide, side);
        mSide = side;
        firePropertyChange();
    }

    /**
     * @return Returns the defenses.
     */
    public List<WeaponToFireBean> getDefenses()
    {
        return mDefenses;
    }

    /**
     * @param defenses
     *            The defenses to set.
     */
    public void setDefenses(List<WeaponToFireBean> defenses)
    {
        mDefenses = defenses;
    }

    /**
     * @return Returns the defensesLeftToUse.
     */
    public List<WeaponToFireBean> getDefensesLeftToUse()
    {
        return mDefensesLeftToUse;
    }

    /**
     * @param defensesLeftToUse
     *            The defensesLeftToUse to set.
     */
    public void setDefensesLeftToUse(List<WeaponToFireBean> defensesLeftToUse)
    {
        mDefensesLeftToUse = defensesLeftToUse;
    }

    /**
     * @return Returns the bridgeDestroyed.
     */
    public boolean isBridgeDestroyed()
    {
        return mBridgeDestroyed;
    }

    /**
     * @param bridgeDestroyed
     *            The bridgeDestroyed to set.
     */
    public void setBridgeDestroyed(boolean bridgeDestroyed)
    {
        queuePropertyChange("bridgeDestroyed", mBridgeDestroyed,
                bridgeDestroyed);
        mBridgeDestroyed = bridgeDestroyed;
        firePropertyChange();
    }

    /**
     * @return Returns the damageArmor.
     */
    public int getDamageArmor()
    {
        return mDamageArmor;
    }

    /**
     * @param damageArmor
     *            The damageArmor to set.
     */
    public void setDamageArmor(int damageArmor)
    {
        queuePropertyChange("damageArmor", mDamageArmor, damageArmor);
        mDamageArmor = damageArmor;
        firePropertyChange();
    }

    /**
     * @return Returns the damageComputer.
     */
    public int getDamageComputer()
    {
        return mDamageComputer;
    }

    /**
     * @param damageComputer
     *            The damageComputer to set.
     */
    public void setDamageComputer(int damageComputer)
    {
        queuePropertyChange("damageComputer", mDamageComputer, damageComputer);
        mDamageComputer = damageComputer;
        firePropertyChange();
    }

    /**
     * @return Returns the damageCrew.
     */
    public int getDamageCrew()
    {
        return mDamageCrew;
    }

    /**
     * @param damageCrew
     *            The damageCrew to set.
     */
    public void setDamageCrew(int damageCrew)
    {
        queuePropertyChange("damageCrew", mDamageCrew, damageCrew);
        mDamageCrew = damageCrew;
        firePropertyChange();
    }

    /**
     * @return Returns the damageFuel.
     */
    public int getDamageFuel()
    {
        return mDamageFuel;
    }

    /**
     * @param damageFuel
     *            The damageFuel to set.
     */
    public void setDamageFuel(int damageFuel)
    {
        queuePropertyChange("damageFuel", mDamageFuel, damageFuel);
        mDamageFuel = damageFuel;
        firePropertyChange();
    }

    /**
     * @return Returns the damageJump.
     */
    public int getDamageJump()
    {
        return mDamageJump;
    }

    /**
     * @param damageJump
     *            The damageJump to set.
     */
    public void setDamageJump(int damageJump)
    {
        queuePropertyChange("damageJump", mDamageJump, damageJump);
        mDamageJump = damageJump;
        firePropertyChange();
    }

    /**
     * @return Returns the damageManeuver.
     */
    public int getDamageManeuver()
    {
        return mDamageManeuver;
    }

    /**
     * @param damageManeuver
     *            The damageManeuver to set.
     */
    public void setDamageManeuver(int damageManeuver)
    {
        queuePropertyChange("damageManeuver", mDamageManeuver, damageManeuver);
        mDamageManeuver = damageManeuver;
        firePropertyChange();
    }

    /**
     * @return Returns the damagePowerPlant.
     */
    public int getDamagePowerPlant()
    {
        return mDamagePowerPlant;
    }

    /**
     * @param damagePowerPlant
     *            The damagePowerPlant to set.
     */
    public void setDamagePowerPlant(int damagePowerPlant)
    {
        queuePropertyChange("damagePowerPlant", mDamagePowerPlant,
                damagePowerPlant);
        mDamagePowerPlant = damagePowerPlant;
        firePropertyChange();
    }

    /**
     * @return Returns the damageSensors.
     */
    public List<SensorToScanBean> getDamageSensors()
    {
        return mDamageSensors;
    }

    /**
     * @param damageSensors
     *            The damageSensors to set.
     */
    public void setDamageSensors(List<SensorToScanBean> damageSensors)
    {
        mDamageSensors = damageSensors;
    }

    /**
     * @return Returns the damageWeapons.
     */
    public List<WeaponToFireBean> getDamageWeapons()
    {
        return mDamageWeapons;
    }

    /**
     * @param damageWeapons
     *            The damageWeapons to set.
     */
    public void setDamageWeapons(List<WeaponToFireBean> damageWeapons)
    {
        mDamageWeapons = damageWeapons;
    }

    /**
     * @return Returns the sensors.
     */
    public List<SensorToScanBean> getSensors()
    {
        return mSensors;
    }

    /**
     * @param sensors
     *            The sensors to set.
     */
    public void setSensors(List<SensorToScanBean> sensors)
    {
        mSensors = sensors;
    }

    /**
     * @return Returns the blackGlobeFactor.
     */
    public int getBlackGlobeFactor()
    {
        return mBlackGlobeFactor;
    }

    /**
     * @param blackGlobeFactor
     *            The blackGlobeFactor to set.
     */
    public void setBlackGlobeFactor(int blackGlobeFactor)
    {
        queuePropertyChange("blackGlobeFactor", mBlackGlobeFactor,
                blackGlobeFactor);
        mBlackGlobeFactor = blackGlobeFactor;
        firePropertyChange();
    }

    /**
     * @return Returns the mesonFactor.
     */
    public int getMesonFactor()
    {
        return mMesonFactor;
    }

    /**
     * @param mesonFactor
     *            The mesonFactor to set.
     */
    public void setMesonFactor(int mesonFactor)
    {
        queuePropertyChange("mesonFactor", mMesonFactor, mesonFactor);
        mMesonFactor = mesonFactor;
        firePropertyChange();
    }

    /**
     * @return Returns the nuclearDamperFactor.
     */
    public int getNuclearDamperFactor()
    {
        return mNuclearDamperFactor;
    }

    /**
     * @param nuclearDamperFactor
     *            The nuclearDamperFactor to set.
     */
    public void setNuclearDamperFactor(int nuclearDamperFactor)
    {
        queuePropertyChange("nuclearDamperFactor", mNuclearDamperFactor,
                nuclearDamperFactor);
        mNuclearDamperFactor = nuclearDamperFactor;
        firePropertyChange();
    }

    /**
     * @return Returns the protonFactor.
     */
    public int getProtonFactor()
    {
        return mProtonFactor;
    }

    /**
     * @param protonFactor
     *            The protonFactor to set.
     */
    public void setProtonFactor(int protonFactor)
    {
        queuePropertyChange("protonFactor", mProtonFactor, protonFactor);
        mProtonFactor = protonFactor;
        firePropertyChange();
    }

    /**
     * @return Returns the whiteGlobeFactor.
     */
    public int getWhiteGlobeFactor()
    {
        return mWhiteGlobeFactor;
    }

    /**
     * @param whiteGlobeFactor
     *            The whiteGlobeFactor to set.
     */
    public void setWhiteGlobeFactor(int whiteGlobeFactor)
    {
        queuePropertyChange("whiteGlobeFactor", mWhiteGlobeFactor,
                whiteGlobeFactor);
        mWhiteGlobeFactor = whiteGlobeFactor;
        firePropertyChange();
    }

    public int getShipType()
    {
        return mShipType;
    }

    public void setShipType(int shipType)
    {
        queuePropertyChange("shipType", mShipType, shipType);
        mShipType = shipType;
        firePropertyChange();
    }

    public String getShipName()
    {
        return mShipName;
    }

    public void setShipName(String shipName)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mShipName = shipName;
        firePropertyChange();
    }

    public boolean isFleeing()
    {
        return mFleeing;
    }

    public void setFleeing(boolean fleeing)
    {
        mFleeing = fleeing;
    }

    public String getURI()
    {
        return "combatShip://" + getShipName() + "?side=" + mSide.getName();
    }
}
