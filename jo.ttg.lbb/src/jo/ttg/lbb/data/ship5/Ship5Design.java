package jo.ttg.lbb.data.ship5;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.FromJSONLogic;
import org.json.simple.IJSONAble;
import org.json.simple.JSONObject;
import org.json.simple.ToJSONLogic;

import jo.util.beans.PCSBean;

public class Ship5Design extends PCSBean implements IJSONAble
{
    public static final char            CONFIG_NEEDLE             = '1';
    public static final char            CONFIG_CONE               = '2';
    public static final char            CONFIG_CYLINDER           = '3';
    public static final char            CONFIG_CLOSE              = '4';
    public static final char            CONFIG_SPHERE             = '5';
    public static final char            CONFIG_FLAT_SPHERE        = '6';
    public static final char            CONFIG_DISPERSED          = '7';
    public static final char            CONFIG_PLANETOID          = '8';
    public static final char            CONFIG_BUFFERED_PLANETOID = '9';

    public static final char            COMPUTER_NONE             = '0';
    public static final char            COMPUTER_1                = '1';
    public static final char            COMPUTER_1_FIB            = 'A';
    public static final char            COMPUTER_1_BIS            = 'R';
    public static final char            COMPUTER_2                = '2';
    public static final char            COMPUTER_2_FIB            = 'B';
    public static final char            COMPUTER_2_BIS            = 'S';
    public static final char            COMPUTER_3                = '3';
    public static final char            COMPUTER_3_FIB            = 'C';
    public static final char            COMPUTER_4                = '4';
    public static final char            COMPUTER_4_FIB            = 'D';
    public static final char            COMPUTER_5                = '5';
    public static final char            COMPUTER_5_FIB            = 'E';
    public static final char            COMPUTER_6                = '6';
    public static final char            COMPUTER_6_FIB            = 'F';
    public static final char            COMPUTER_7                = '7';
    public static final char            COMPUTER_7_FIB            = 'G';
    public static final char            COMPUTER_8                = '8';
    public static final char            COMPUTER_8_FIB            = 'H';
    public static final char            COMPUTER_9                = '9';
    public static final char            COMPUTER_9_FIB            = 'J';

    public static final char            MAJOR_NONE                = ' ';
    public static final char            MAJOR_MESON               = 'M';
    public static final char            MAJOR_PARTICLE            = 'P';

    private String                      mShipName;
    private String                      mShipType;
    private String                      mShipURI;
    private String                      mClassName;
    private boolean                     mFirstInClass;
    private String                      mOwnerURI;
    private String                      mShipyardURI;
    private int                         mTechLevel;
    private int                         mHullTonnage;
    private char                        mHullConfigurationCode;
    private int                         mManeuverDriveNumber;
    private int                         mJumpDriveNumber;
    private int                         mPowerPlantNumber;
    private int                         mFuelTankage;
    private boolean                     mFuelScoops;
    private boolean                     mFuelPurification;
    private int                         mBridgeAuxiliaryCount;
    private char                        mComputerCode;
    private int                         mArmorFactors;
    private char                        mMajorWeapon;
    private char                        mMajorCode;
    private int                         mBays100Meson;
    private int                         mBays100Particle;
    private int                         mBays100Repulsor;
    private int                         mBays100Missile;
    private int                         mBays50Meson;
    private int                         mBays50Particle;
    private int                         mBays50Repulsor;
    private int                         mBays50Missile;
    private int                         mBays50Plasma;
    private int                         mBays50Fusion;
    private int                         mTurretMissile;
    private int                         mTurretBeamLaser;
    private int                         mTurretPulseLaser;
    private int                         mTurretPlasmaGun;
    private int                         mTurretFusionGun;
    private int                         mTurretSandcaster;
    private int                         mTurretParticle;
    private int                         mBarbetteParticle;
    private char                        mScreenNuclearCode;
    private char                        mScreenMesonCode;
    private char                        mScreenForceCode;
    private List<Ship5DesignSubCraft>   mSubCraft                 = new ArrayList<Ship5Design.Ship5DesignSubCraft>();
    private List<Ship5DesignLaunchTube> mLaunchTubes              = new ArrayList<Ship5Design.Ship5DesignLaunchTube>();
    private int                         mCrewTroops;
    private int                         mStaterooms;
    private int                         mLowBerths;
    private int                         mEmergencyLowBerths;

    // constructors

    public Ship5Design()
    {

    }
    
    // io
    @Override
    public JSONObject toJSON()
    {
        return (JSONObject)ToJSONLogic.toJSON(this);
    }
    
    @Override
    public void fromJSON(JSONObject o)
    {
        FromJSONLogic.fromJSONInto(o, this);
    }

    // utility

    public boolean isAnyWeapons()
    {
        return (getMajorWeapon() != Ship5Design.MAJOR_NONE)
                || (getBays100Meson() > 0) || (getBays100Particle() > 0)
                || (getBays100Repulsor() > 0) || (getBays100Missile() > 0)
                || (getBays50Meson() > 0) || (getBays50Particle() > 0)
                || (getBays50Repulsor() > 0) || (getBays50Missile() > 0)
                || (getBays50Plasma() > 0) || (getBays50Fusion() > 0)
                || (getTurretMissile() > 0) || (getTurretBeamLaser() > 0)
                || (getTurretPulseLaser() > 0) || (getTurretPlasmaGun() > 0)
                || (getTurretFusionGun() > 0) || (getTurretSandcaster() > 0)
                || (getTurretParticle() > 0) || (getBarbetteParticle() > 0);
    }

    public boolean isAnyMesonWeapons()
    {
        return (getMajorWeapon() == Ship5Design.MAJOR_MESON)
                || (getBays100Meson() > 0) || (getBays50Meson() > 0);
    }

    public boolean isAnyParticleWeapons()
    {
        return (getMajorWeapon() == Ship5Design.MAJOR_PARTICLE)
                || (getBays100Particle() > 0) || (getBays50Particle() > 0)
                || (getTurretParticle() > 0) || (getBarbetteParticle() > 0);
    }

    public boolean isAnyLaserWeapons()
    {
        return (getTurretBeamLaser() > 0) || (getTurretPulseLaser() > 0);
    }

    public boolean isAnyEnergyWeapons()
    {
        return (getBays50Plasma() > 0) || (getBays50Fusion() > 0)
                || (getTurretPlasmaGun() > 0) || (getTurretFusionGun() > 0);
    }

    public boolean isAnyRepulsorWeapons()
    {
        return (getBays100Repulsor() > 0) || (getBays50Repulsor() > 0);
    }

    public boolean isAnySandWeapons()
    {
        return (getTurretSandcaster() > 0);
    }

    public boolean isAnyMissileWeapons()
    {
        return (getBays100Missile() > 0) || (getBays50Meson() > 0)
                || (getBays50Missile() > 0) || (getTurretMissile() > 0);
    }

    public boolean isAnyScreens()
    {
        return (getScreenNuclearCode() > '0') || (getScreenMesonCode() > '0')
                || (getScreenForceCode() > '0');
    }

    public int getBayWeaponCount()
    {
        return getBays100Meson() + getBays100Particle() + getBays100Repulsor()
                + getBays100Missile() + getBays50Meson() + getBays50Particle()
                + getBays50Repulsor() + getBays50Missile() + getBays50Plasma()
                + getBays50Fusion();
    }

    public int getTurretWeaponCount()
    {
        return getTurretBeamLaser() + getTurretFusionGun() + getTurretMissile()
            + getTurretParticle() + getTurretPlasmaGun() + getTurretPulseLaser()
            + getTurretSandcaster() + getTurretWeaponCount();
    }

    public int getScreenCount()
    {
        int count = 0;
        if (getScreenNuclearCode() > '0')
            count++;
        if (getScreenMesonCode() > '0')
            count++;
        if (getScreenForceCode() > '0')
            count++;
        return count;
    }

    // getters and setters

    public String getShipName()
    {
        return mShipName;
    }

    public void setShipName(String shipName)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mShipName = shipName;
    }

    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String className)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mClassName = className;
    }

    public boolean isFirstInClass()
    {
        return mFirstInClass;
    }

    public void setFirstInClass(boolean firstInClass)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mFirstInClass = firstInClass;
    }

    public String getOwnerURI()
    {
        return mOwnerURI;
    }

    public void setOwnerURI(String ownerURI)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mOwnerURI = ownerURI;
    }

    public String getShipyardURI()
    {
        return mShipyardURI;
    }

    public void setShipyardURI(String shipyardURI)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mShipyardURI = shipyardURI;
    }

    public int getTechLevel()
    {
        return mTechLevel;
    }

    public void setTechLevel(int techLevel)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTechLevel = techLevel;
    }

    public char getHullConfigurationCode()
    {
        return mHullConfigurationCode;
    }

    public void setHullConfigurationCode(char hullConfigurationCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mHullConfigurationCode = hullConfigurationCode;
    }

    public int getManeuverDriveNumber()
    {
        return mManeuverDriveNumber;
    }

    public void setManeuverDriveNumber(int maneuverDriveNumber)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mManeuverDriveNumber = maneuverDriveNumber;
    }

    public int getJumpDriveNumber()
    {
        return mJumpDriveNumber;
    }

    public void setJumpDriveNumber(int jumpDriveNumber)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mJumpDriveNumber = jumpDriveNumber;
    }

    public int getPowerPlantNumber()
    {
        return mPowerPlantNumber;
    }

    public void setPowerPlantNumber(int powerPlantNumber)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mPowerPlantNumber = powerPlantNumber;
        firePropertyChange();
    }

    public int getHullTonnage()
    {
        return mHullTonnage;
    }

    public void setHullTonnage(int hullTonnage)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mHullTonnage = hullTonnage;
        firePropertyChange();
    }

    public int getFuelTankage()
    {
        return mFuelTankage;
    }

    public void setFuelTankage(int fuelTankage)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mFuelTankage = fuelTankage;
        firePropertyChange();
    }

    public boolean isFuelScoops()
    {
        return mFuelScoops;
    }

    public void setFuelScoops(boolean fuelScoops)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mFuelScoops = fuelScoops;
        firePropertyChange();
    }

    public boolean isFuelPurification()
    {
        return mFuelPurification;
    }

    public void setFuelPurification(boolean fuelPurification)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mFuelPurification = fuelPurification;
        firePropertyChange();
    }

    public int getBridgeAuxiliaryCount()
    {
        return mBridgeAuxiliaryCount;
    }

    public void setBridgeAuxiliaryCount(int bridgeAuxiliaryCount)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBridgeAuxiliaryCount = bridgeAuxiliaryCount;
        firePropertyChange();
    }

    public char getComputerCode()
    {
        return mComputerCode;
    }

    public void setComputerCode(char computerCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mComputerCode = computerCode;
        firePropertyChange();
    }

    public int getArmorFactors()
    {
        return mArmorFactors;
    }

    public void setArmorFactors(int armorFactors)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mArmorFactors = armorFactors;
        firePropertyChange();
    }

    public char getMajorWeapon()
    {
        return mMajorWeapon;
    }

    public void setMajorWeapon(char majorWeapon)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mMajorWeapon = majorWeapon;
        firePropertyChange();
    }

    public char getMajorCode()
    {
        return mMajorCode;
    }

    public void setMajorCode(char majorCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mMajorCode = majorCode;
        firePropertyChange();
    }

    public int getBays100Meson()
    {
        return mBays100Meson;
    }

    public void setBays100Meson(int bays100Meson)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays100Meson = bays100Meson;
        firePropertyChange();
    }

    public int getBays100Particle()
    {
        return mBays100Particle;
    }

    public void setBays100Particle(int bays100Particle)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays100Particle = bays100Particle;
        firePropertyChange();
    }

    public int getBays100Repulsor()
    {
        return mBays100Repulsor;
    }

    public void setBays100Repulsor(int bays100Repulsor)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays100Repulsor = bays100Repulsor;
        firePropertyChange();
    }

    public int getBays100Missile()
    {
        return mBays100Missile;
    }

    public void setBays100Missile(int bays100Missile)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays100Missile = bays100Missile;
        firePropertyChange();
    }

    public int getBays50Meson()
    {
        return mBays50Meson;
    }

    public void setBays50Meson(int bays50Meson)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Meson = bays50Meson;
        firePropertyChange();
    }

    public int getBays50Particle()
    {
        return mBays50Particle;
    }

    public void setBays50Particle(int bays50Particle)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Particle = bays50Particle;
        firePropertyChange();
    }

    public int getBays50Repulsor()
    {
        return mBays50Repulsor;
    }

    public void setBays50Repulsor(int bays50Repulsor)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Repulsor = bays50Repulsor;
        firePropertyChange();
    }

    public int getBays50Missile()
    {
        return mBays50Missile;
    }

    public void setBays50Missile(int bays50Missile)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Missile = bays50Missile;
        firePropertyChange();
    }

    public int getBays50Plasma()
    {
        return mBays50Plasma;
    }

    public void setBays50Plasma(int bays50Plasma)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Plasma = bays50Plasma;
        firePropertyChange();
    }

    public int getBays50Fusion()
    {
        return mBays50Fusion;
    }

    public void setBays50Fusion(int bays50Fusion)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBays50Fusion = bays50Fusion;
        firePropertyChange();
    }

    public int getTurretMissile()
    {
        return mTurretMissile;
    }

    public void setTurretMissile(int turretMissile)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretMissile = turretMissile;
        firePropertyChange();
    }

    public int getTurretBeamLaser()
    {
        return mTurretBeamLaser;
    }

    public void setTurretBeamLaser(int turretBeamLaser)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretBeamLaser = turretBeamLaser;
        firePropertyChange();
    }

    public int getTurretPulseLaser()
    {
        return mTurretPulseLaser;
    }

    public void setTurretPulseLaser(int turretPulseLaser)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretPulseLaser = turretPulseLaser;
        firePropertyChange();
    }

    public int getTurretPlasmaGun()
    {
        return mTurretPlasmaGun;
    }

    public void setTurretPlasmaGun(int turretPlasmaGun)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretPlasmaGun = turretPlasmaGun;
        firePropertyChange();
    }

    public int getTurretFusionGun()
    {
        return mTurretFusionGun;
    }

    public void setTurretFusionGun(int turretFusionGun)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretFusionGun = turretFusionGun;
        firePropertyChange();
    }

    public int getTurretSandcaster()
    {
        return mTurretSandcaster;
    }

    public void setTurretSandcaster(int turretSandcaster)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretSandcaster = turretSandcaster;
        firePropertyChange();
    }

    public int getTurretParticle()
    {
        return mTurretParticle;
    }

    public void setTurretParticle(int turretParticle)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mTurretParticle = turretParticle;
        firePropertyChange();
    }

    public int getBarbetteParticle()
    {
        return mBarbetteParticle;
    }

    public void setBarbetteParticle(int barbetteParticleGun)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mBarbetteParticle = barbetteParticleGun;
        firePropertyChange();
    }

    public char getScreenNuclearCode()
    {
        return mScreenNuclearCode;
    }

    public void setScreenNuclearCode(char screenNuclearCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mScreenNuclearCode = screenNuclearCode;
        firePropertyChange();
    }

    public char getScreenMesonCode()
    {
        return mScreenMesonCode;
    }

    public void setScreenMesonCode(char screenMesonCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mScreenMesonCode = screenMesonCode;
        firePropertyChange();
    }

    public char getScreenForceCode()
    {
        return mScreenForceCode;
    }

    public void setScreenForceCode(char screenForceCode)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mScreenForceCode = screenForceCode;
        firePropertyChange();
    }

    public String getShipURI()
    {
        return mShipURI;
    }

    public void setShipURI(String shipURI)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mShipURI = shipURI;
        firePropertyChange();
    }

    public class Ship5DesignSubCraft
    {
        private String  mShipName;
        private String  mShipURI;
        private int     mHullTonnage;
        private long    mCost;
        private int     mCrew;
        private int     mQuantity;
        private boolean mVehicle;

        public String getShipName()
        {
            return mShipName;
        }

        public void setShipName(String shipName)
        {
            mShipName = shipName;
        }

        public String getShipURI()
        {
            return mShipURI;
        }

        public void setShipURI(String shipURI)
        {
            mShipURI = shipURI;
        }

        public int getHullTonnage()
        {
            return mHullTonnage;
        }

        public void setHullTonnage(int hullTonnage)
        {
            mHullTonnage = hullTonnage;
        }

        public int getCrew()
        {
            return mCrew;
        }

        public void setCrew(int crew)
        {
            mCrew = crew;
        }

        public int getQuantity()
        {
            return mQuantity;
        }

        public void setQuantity(int quantity)
        {
            mQuantity = quantity;
        }

        public long getCost()
        {
            return mCost;
        }

        public void setCost(long cost)
        {
            mCost = cost;
        }

        public boolean isVehicle()
        {
            return mVehicle;
        }

        public void setVehicle(boolean vehicle)
        {
            mVehicle = vehicle;
        }
    }

    public class Ship5DesignLaunchTube
    {
        private int mCapacity;
        private int mQuantity;

        public int getCapacity()
        {
            return mCapacity;
        }

        public void setCapacity(int capacity)
        {
            mCapacity = capacity;
        }

        public int getQuantity()
        {
            return mQuantity;
        }

        public void setQuantity(int quantity)
        {
            mQuantity = quantity;
        }
    }

    public List<Ship5DesignSubCraft> getSubCraft()
    {
        return mSubCraft;
    }

    public void setSubCraft(List<Ship5DesignSubCraft> subCraft)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mSubCraft = subCraft;
        firePropertyChange();
    }

    public List<Ship5DesignLaunchTube> getLaunchTubes()
    {
        return mLaunchTubes;
    }

    public void setLaunchTubes(List<Ship5DesignLaunchTube> launchTubes)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mLaunchTubes = launchTubes;
        firePropertyChange();
    }

    public int getCrewTroops()
    {
        return mCrewTroops;
    }

    public void setCrewTroops(int crewTroops)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mCrewTroops = crewTroops;
        firePropertyChange();
    }

    public int getStaterooms()
    {
        return mStaterooms;
    }

    public void setStaterooms(int staterooms)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mStaterooms = staterooms;
        firePropertyChange();
    }

    public int getLowBerths()
    {
        return mLowBerths;
    }

    public void setLowBerths(int lowBerths)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mLowBerths = lowBerths;
        firePropertyChange();
    }

    public int getEmergencyLowBerths()
    {
        return mEmergencyLowBerths;
    }

    public void setEmergencyLowBerths(int emergencyLowBerths)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mEmergencyLowBerths = emergencyLowBerths;
        firePropertyChange();
    }

    public String getShipType()
    {
        return mShipType;
    }

    public void setShipType(String shipType)
    {
        queuePropertyChange("shipName", mShipName, shipName);
        mShipType = shipType;
        firePropertyChange();
    }
}
