package jo.ttg.lbb.data.ship5;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.IJSONAble;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

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

    public static final String[] TYPE_PRIMARY = {
            "A - Merchant",
            "B - Battle",
            "C - Cruiser; Carrier",
            "D - Destroyer",
            "E - Escort",
            "F - Frigate; Fighter",
            "G - Gig; Refinery",
            "H - ",
            "I,J - Intruder",
            "K - Pinnace",
            "L - Corvette; Lab",
            "M - Merchant",
            "N - ",
            "P - Planetoid",
            "Q - Auxiliary",
            "R - Liner",
            "S - Scout; Station",
            "T - Tanker; Tender",
            "U - ",
            "V - ",
            "W - Barge",
            "X - Express",
            "Y - Yacht",
            "Z - ",
    };

    public static final String[] TYPE_QUALIFIER = {
            "A - Armored",
            "B - Battle; Boat",
            "C - Cruiser; Close",
            "D - Destroyer",
            "E - Escort",
            "F - Fast; Fleet",
            "G - Gunned",
            "H - Heavy",
            "I,J - ",
            "K - ",
            "L - Leader; Light",
            "M - Missile",
            "N - Non-standard",
            "P - Provincial",
            "Q - Decoy",
            "R - Raider",
            "S - Strike",
            "T - Troop; Transport",
            "U - Unpowered",
            "V - Vehicle",
            "W - ",
            "X - ",
            "Y - Shuttle; Cutter",
            "Z - Experimental",
    };

    public static final String[] CONFIG_NAMES = {
        "1 Needle/Wedge",
        "2 Cone",
        "3 Cylinder",
        "4 Close",
        "5 Sphere",
        "6 Flat Sphere",
        "7 Dispersed",
        "8 Planetoid",
        "9 Buffered Planetoid",
    };

    public static final String[] COMPUTER_NAMES = {
            "0 None",
            "1 Computer I",
            "A Computer Ifib",
            "R Computer Ibis",
            "2 Computer II",
            "B Computer IIfib",
            "S Computer IIbis",
            "3 Computer III",
            "C Computer IIIbis",
            "4 Computer IV",
            "D Computer IVbis",
            "5 Computer V",
            "E Computer Vbis",
            "6 Computer VI",
            "F Computer VIbis",
            "7 Computer VII",
            "G Computer VIIbis",
            "8 Computer VIII",
            "H Computer VIIIbis",
            "9 Computer IX",
            "J Computer IXbis",
    };

    public static final String[] MAJOR_NAMES = {
        " None",
        "Meson",
        "Particle Accelerator",
    };

    public static final String[] MAJOR_CODES = {
        "0",
        "1",
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
    };
    
    private String                      mShipName;
    private String                      mShipType;
    private String                      mShipURI;
    private String                      mClassName;
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
        mShipName = "Lollipop";
        mShipType = "ML";
        mShipURI = "";
        mClassName = "Sweetums";
        mOwnerURI = "";
        mShipyardURI = "";
        mTechLevel = 15;
        mHullTonnage = 1000;
        mHullConfigurationCode = CONFIG_CLOSE;
        mComputerCode = COMPUTER_1;
        mMajorWeapon = MAJOR_NONE;
        mScreenNuclearCode = '0';
        mScreenMesonCode = '0';
        mScreenForceCode = '0';
    }
    
    // io
    @Override
    public JSONObject toJSON()
    {
        return BeanHandler.doToJSON(this);
    }
    
    @Override
    public void fromJSON(JSONObject o)
    {
        BeanHandler.doFromJSONInto(o, this);
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
        firePropertyChange();
    }

    public String getClassName()
    {
        return mClassName;
    }

    public void setClassName(String className)
    {
        queuePropertyChange("className", mClassName, className);
        mClassName = className;
        firePropertyChange();
    }

    public String getOwnerURI()
    {
        return mOwnerURI;
    }

    public void setOwnerURI(String ownerURI)
    {
        queuePropertyChange("ownerURI", mOwnerURI, ownerURI);
        mOwnerURI = ownerURI;
        firePropertyChange();
    }

    public String getShipyardURI()
    {
        return mShipyardURI;
    }

    public void setShipyardURI(String shipyardURI)
    {
        queuePropertyChange("shipyardURI", mShipyardURI, shipyardURI);
        mShipyardURI = shipyardURI;
        firePropertyChange();
    }

    public int getTechLevel()
    {
        return mTechLevel;
    }

    public void setTechLevel(int techLevel)
    {
        queuePropertyChange("techLevel", mTechLevel, techLevel);
        mTechLevel = techLevel;
        firePropertyChange();
    }

    public char getHullConfigurationCode()
    {
        return mHullConfigurationCode;
    }

    public void setHullConfigurationCode(char hullConfigurationCode)
    {
        queuePropertyChange("hullConfigurationCode", mHullConfigurationCode, hullConfigurationCode);
        mHullConfigurationCode = hullConfigurationCode;
        firePropertyChange();
    }

    public int getManeuverDriveNumber()
    {
        return mManeuverDriveNumber;
    }

    public void setManeuverDriveNumber(int maneuverDriveNumber)
    {
        queuePropertyChange("maneuverDriveNumber", mManeuverDriveNumber, maneuverDriveNumber);
        mManeuverDriveNumber = maneuverDriveNumber;
        firePropertyChange();
    }

    public int getJumpDriveNumber()
    {
        return mJumpDriveNumber;
    }

    public void setJumpDriveNumber(int jumpDriveNumber)
    {
        queuePropertyChange("jumpDriveNumber", mJumpDriveNumber, jumpDriveNumber);
        mJumpDriveNumber = jumpDriveNumber;
        firePropertyChange();
    }

    public int getPowerPlantNumber()
    {
        return mPowerPlantNumber;
    }

    public void setPowerPlantNumber(int powerPlantNumber)
    {
        queuePropertyChange("powerPlantNumber", mPowerPlantNumber, powerPlantNumber);
        mPowerPlantNumber = powerPlantNumber;
        firePropertyChange();
    }

    public int getHullTonnage()
    {
        return mHullTonnage;
    }

    public void setHullTonnage(int hullTonnage)
    {
        queuePropertyChange("hullTonnage", mHullTonnage, hullTonnage);
        mHullTonnage = hullTonnage;
        firePropertyChange();
    }

    public int getFuelTankage()
    {
        return mFuelTankage;
    }

    public void setFuelTankage(int fuelTankage)
    {
        queuePropertyChange("fuelTankage", mFuelTankage, fuelTankage);
        mFuelTankage = fuelTankage;
        firePropertyChange();
    }

    public boolean isFuelScoops()
    {
        return mFuelScoops;
    }

    public void setFuelScoops(boolean fuelScoops)
    {
        queuePropertyChange("fuelScoops", mFuelScoops, fuelScoops);
        mFuelScoops = fuelScoops;
        firePropertyChange();
    }

    public boolean isFuelPurification()
    {
        return mFuelPurification;
    }

    public void setFuelPurification(boolean fuelPurification)
    {
        queuePropertyChange("fuelPurification", mFuelPurification, fuelPurification);
        mFuelPurification = fuelPurification;
        firePropertyChange();
    }

    public int getBridgeAuxiliaryCount()
    {
        return mBridgeAuxiliaryCount;
    }

    public void setBridgeAuxiliaryCount(int bridgeAuxiliaryCount)
    {
        queuePropertyChange("bridgeAuxiliaryCount", mBridgeAuxiliaryCount, bridgeAuxiliaryCount);
        mBridgeAuxiliaryCount = bridgeAuxiliaryCount;
        firePropertyChange();
    }

    public char getComputerCode()
    {
        return mComputerCode;
    }

    public void setComputerCode(char computerCode)
    {
        queuePropertyChange("computerCode", mComputerCode, computerCode);
        mComputerCode = computerCode;
        firePropertyChange();
    }

    public int getArmorFactors()
    {
        return mArmorFactors;
    }

    public void setArmorFactors(int armorFactors)
    {
        queuePropertyChange("armorFactors", mArmorFactors, armorFactors);
        mArmorFactors = armorFactors;
        firePropertyChange();
    }

    public char getMajorWeapon()
    {
        return mMajorWeapon;
    }

    public void setMajorWeapon(char majorWeapon)
    {
        queuePropertyChange("majorWeapon", mMajorWeapon, majorWeapon);
        mMajorWeapon = majorWeapon;
        firePropertyChange();
    }

    public char getMajorCode()
    {
        return mMajorCode;
    }

    public void setMajorCode(char majorCode)
    {
        queuePropertyChange("majorCode", mMajorCode, majorCode);
        mMajorCode = majorCode;
        firePropertyChange();
    }

    public int getBays100Meson()
    {
        return mBays100Meson;
    }

    public void setBays100Meson(int bays100Meson)
    {
        queuePropertyChange("bays100Meson", mBays100Meson, bays100Meson);
        mBays100Meson = bays100Meson;
        firePropertyChange();
    }

    public int getBays100Particle()
    {
        return mBays100Particle;
    }

    public void setBays100Particle(int bays100Particle)
    {
        queuePropertyChange("bays100Particle", mBays100Particle, bays100Particle);
        mBays100Particle = bays100Particle;
        firePropertyChange();
    }

    public int getBays100Repulsor()
    {
        return mBays100Repulsor;
    }

    public void setBays100Repulsor(int bays100Repulsor)
    {
        queuePropertyChange("bays100Repulsor", mBays100Repulsor, bays100Repulsor);
        mBays100Repulsor = bays100Repulsor;
        firePropertyChange();
    }

    public int getBays100Missile()
    {
        return mBays100Missile;
    }

    public void setBays100Missile(int bays100Missile)
    {
        queuePropertyChange("bays100Missile", mBays100Missile, bays100Missile);
        mBays100Missile = bays100Missile;
        firePropertyChange();
    }

    public int getBays50Meson()
    {
        return mBays50Meson;
    }

    public void setBays50Meson(int bays50Meson)
    {
        queuePropertyChange("bays50Meson", mBays50Meson, bays50Meson);
        mBays50Meson = bays50Meson;
        firePropertyChange();
    }

    public int getBays50Particle()
    {
        return mBays50Particle;
    }

    public void setBays50Particle(int bays50Particle)
    {
        queuePropertyChange("bays50Particle", mBays50Particle, bays50Particle);
        mBays50Particle = bays50Particle;
        firePropertyChange();
    }

    public int getBays50Repulsor()
    {
        return mBays50Repulsor;
    }

    public void setBays50Repulsor(int bays50Repulsor)
    {
        queuePropertyChange("bays50Repulsor", mBays50Repulsor, bays50Repulsor);
        mBays50Repulsor = bays50Repulsor;
        firePropertyChange();
    }

    public int getBays50Missile()
    {
        return mBays50Missile;
    }

    public void setBays50Missile(int bays50Missile)
    {
        queuePropertyChange("bays50Missile", mBays50Missile, bays50Missile);
        mBays50Missile = bays50Missile;
        firePropertyChange();
    }

    public int getBays50Plasma()
    {
        return mBays50Plasma;
    }

    public void setBays50Plasma(int bays50Plasma)
    {
        queuePropertyChange("bays50Plasma", mBays50Plasma, bays50Plasma);
        mBays50Plasma = bays50Plasma;
        firePropertyChange();
    }

    public int getBays50Fusion()
    {
        return mBays50Fusion;
    }

    public void setBays50Fusion(int bays50Fusion)
    {
        queuePropertyChange("bays50Fusion", mBays50Fusion, bays50Fusion);
        mBays50Fusion = bays50Fusion;
        firePropertyChange();
    }

    public int getTurretMissile()
    {
        return mTurretMissile;
    }

    public void setTurretMissile(int turretMissile)
    {
        queuePropertyChange("turretMissile", mTurretMissile, turretMissile);
        mTurretMissile = turretMissile;
        firePropertyChange();
    }

    public int getTurretBeamLaser()
    {
        return mTurretBeamLaser;
    }

    public void setTurretBeamLaser(int turretBeamLaser)
    {
        queuePropertyChange("turretBeamLaser", mTurretBeamLaser, turretBeamLaser);
        mTurretBeamLaser = turretBeamLaser;
        firePropertyChange();
    }

    public int getTurretPulseLaser()
    {
        return mTurretPulseLaser;
    }

    public void setTurretPulseLaser(int turretPulseLaser)
    {
        queuePropertyChange("turretPulseLaser", mTurretPulseLaser, turretPulseLaser);
        mTurretPulseLaser = turretPulseLaser;
        firePropertyChange();
    }

    public int getTurretPlasmaGun()
    {
        return mTurretPlasmaGun;
    }

    public void setTurretPlasmaGun(int turretPlasmaGun)
    {
        queuePropertyChange("turretPlasmaGun", mTurretPlasmaGun, turretPlasmaGun);
        mTurretPlasmaGun = turretPlasmaGun;
        firePropertyChange();
    }

    public int getTurretFusionGun()
    {
        return mTurretFusionGun;
    }

    public void setTurretFusionGun(int turretFusionGun)
    {
        queuePropertyChange("turretFusionGun", mTurretFusionGun, turretFusionGun);
        mTurretFusionGun = turretFusionGun;
        firePropertyChange();
    }

    public int getTurretSandcaster()
    {
        return mTurretSandcaster;
    }

    public void setTurretSandcaster(int turretSandcaster)
    {
        queuePropertyChange("turretSandcaster", mTurretSandcaster, turretSandcaster);
        mTurretSandcaster = turretSandcaster;
        firePropertyChange();
    }

    public int getTurretParticle()
    {
        return mTurretParticle;
    }

    public void setTurretParticle(int turretParticle)
    {
        queuePropertyChange("turretParticle", mTurretParticle, turretParticle);
        mTurretParticle = turretParticle;
        firePropertyChange();
    }

    public int getBarbetteParticle()
    {
        return mBarbetteParticle;
    }

    public void setBarbetteParticle(int barbetteParticleGun)
    {
        queuePropertyChange("barbetteParticleGun", mBarbetteParticle, barbetteParticleGun);
        mBarbetteParticle = barbetteParticleGun;
        firePropertyChange();
    }

    public char getScreenNuclearCode()
    {
        return mScreenNuclearCode;
    }

    public void setScreenNuclearCode(char screenNuclearCode)
    {
        queuePropertyChange("screenNuclearCode", mScreenNuclearCode, screenNuclearCode);
        mScreenNuclearCode = screenNuclearCode;
        firePropertyChange();
    }

    public char getScreenMesonCode()
    {
        return mScreenMesonCode;
    }

    public void setScreenMesonCode(char screenMesonCode)
    {
        queuePropertyChange("screenMesonCode", mScreenMesonCode, screenMesonCode);
        mScreenMesonCode = screenMesonCode;
        firePropertyChange();
    }

    public char getScreenForceCode()
    {
        return mScreenForceCode;
    }

    public void setScreenForceCode(char screenForceCode)
    {
        queuePropertyChange("screenForceCode", mScreenForceCode, screenForceCode);
        mScreenForceCode = screenForceCode;
        firePropertyChange();
    }

    public String getShipURI()
    {
        return mShipURI;
    }

    public void setShipURI(String shipURI)
    {
        queuePropertyChange("shipURI", mShipURI, shipURI);
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
        queuePropertyChange("shipName", mShipName, shipName);
        mShipName = shipName;
        firePropertyChange();
    }

        public String getShipURI()
        {
            return mShipURI;
        }

        public void setShipURI(String shipURI)
    {
        queuePropertyChange("shipURI", mShipURI, shipURI);
        mShipURI = shipURI;
        firePropertyChange();
    }

        public int getHullTonnage()
        {
            return mHullTonnage;
        }

        public void setHullTonnage(int hullTonnage)
    {
        queuePropertyChange("hullTonnage", mHullTonnage, hullTonnage);
        mHullTonnage = hullTonnage;
        firePropertyChange();
    }

        public int getCrew()
        {
            return mCrew;
        }

        public void setCrew(int crew)
    {
        queuePropertyChange("crew", mCrew, crew);
        mCrew = crew;
        firePropertyChange();
    }

        public int getQuantity()
        {
            return mQuantity;
        }

        public void setQuantity(int quantity)
    {
        queuePropertyChange("quantity", mQuantity, quantity);
        mQuantity = quantity;
        firePropertyChange();
    }

        public long getCost()
        {
            return mCost;
        }

        public void setCost(long cost)
    {
        queuePropertyChange("cost", mCost, cost);
        mCost = cost;
        firePropertyChange();
    }

        public boolean isVehicle()
        {
            return mVehicle;
        }

        public void setVehicle(boolean vehicle)
    {
        queuePropertyChange("vehicle", mVehicle, vehicle);
        mVehicle = vehicle;
        firePropertyChange();
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
        queuePropertyChange("capacity", mCapacity, capacity);
        mCapacity = capacity;
        firePropertyChange();
    }

        public int getQuantity()
        {
            return mQuantity;
        }

        public void setQuantity(int quantity)
    {
        queuePropertyChange("quantity", mQuantity, quantity);
        mQuantity = quantity;
        firePropertyChange();
    }
    }

    public List<Ship5DesignSubCraft> getSubCraft()
    {
        return mSubCraft;
    }

    public void setSubCraft(List<Ship5DesignSubCraft> subCraft)
    {
        queuePropertyChange("subCraft", mSubCraft, subCraft);
        mSubCraft = subCraft;
        firePropertyChange();
    }

    public List<Ship5DesignLaunchTube> getLaunchTubes()
    {
        return mLaunchTubes;
    }

    public void setLaunchTubes(List<Ship5DesignLaunchTube> launchTubes)
    {
        queuePropertyChange("launchTubes", mLaunchTubes, launchTubes);
        mLaunchTubes = launchTubes;
        firePropertyChange();
    }

    public int getCrewTroops()
    {
        return mCrewTroops;
    }

    public void setCrewTroops(int crewTroops)
    {
        queuePropertyChange("crewTroops", mCrewTroops, crewTroops);
        mCrewTroops = crewTroops;
        firePropertyChange();
    }

    public int getStaterooms()
    {
        return mStaterooms;
    }

    public void setStaterooms(int staterooms)
    {
        queuePropertyChange("staterooms", mStaterooms, staterooms);
        mStaterooms = staterooms;
        firePropertyChange();
    }

    public int getLowBerths()
    {
        return mLowBerths;
    }

    public void setLowBerths(int lowBerths)
    {
        queuePropertyChange("lowBerths", mLowBerths, lowBerths);
        mLowBerths = lowBerths;
        firePropertyChange();
    }

    public int getEmergencyLowBerths()
    {
        return mEmergencyLowBerths;
    }

    public void setEmergencyLowBerths(int emergencyLowBerths)
    {
        queuePropertyChange("emergencyLowBerths", mEmergencyLowBerths, emergencyLowBerths);
        mEmergencyLowBerths = emergencyLowBerths;
        firePropertyChange();
    }

    public String getShipType()
    {
        return mShipType;
    }

    public void setShipType(String shipType)
    {
        queuePropertyChange("shipType", mShipType, shipType);
        mShipType = shipType;
        firePropertyChange();
    }
}

