package jo.ttg.lbb.data.ship2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jo.util.geom3d.Point3D;

public class Ship2Instance
{
	public static final String PROGRAM_MANEUVER = "Maneuver";
	public static final String PROGRAM_JUMP_1 = "Jump 1";
	public static final String PROGRAM_JUMP_2 = "Jump 2";
	public static final String PROGRAM_JUMP_3 = "Jump 3";
	public static final String PROGRAM_JUMP_4 = "Jump 4";
	public static final String PROGRAM_JUMP_5 = "Jump 5";
	public static final String PROGRAM_JUMP_6 = "Jump 6";
	public static final String PROGRAM_NAVIGATION = "Navigation";
	public static final String PROGRAM_GENERATE = "Generate";
	public static final String PROGRAM_ANTI_HIJACK = "Anti-Hijack";
	public static final String PROGRAM_LIBRARY = "Library";
	public static final String PROGRAM_MAN_EVA_1 = "Maneuver/evade 1";
	public static final String PROGRAM_MAN_EVA_2 = "Maneuver/evade 2";
	public static final String PROGRAM_MAN_EVA_3 = "Maneuver/evade 3";
	public static final String PROGRAM_MAN_EVA_4 = "Maneuver/evade 4";
	public static final String PROGRAM_MAN_EVA_5 = "Maneuver/evade 5";
	public static final String PROGRAM_MAN_EVA_6 = "Maneuver/evade 6";
	public static final String PROGRAM_AUTO_EVADE = "Auto/evade";
	public static final String PROGRAM_RETURN_FIRE = "Return Fire";
	public static final String PROGRAM_ANTI_MISSILE = "Anti-Missile";
	public static final String PROGRAM_ECM = "ECM";
	public static final String PROGRAM_PREDICT_1 = "Predict 1";
	public static final String PROGRAM_PREDICT_2 = "Predict 2";
	public static final String PROGRAM_PREDICT_3 = "Predict 3";
	public static final String PROGRAM_PREDICT_4 = "Predict 4";
	public static final String PROGRAM_PREDICT_5 = "Predict 5";
	public static final String PROGRAM_GUNNER_INTERACT = "Gunner Interact";
	public static final String PROGRAM_TARGET = "Target";
	public static final String PROGRAM_SELECTIVE_1 = "Selective 1";
	public static final String PROGRAM_SELECTIVE_2 = "Selective 2";
	public static final String PROGRAM_SELECTIVE_3 = "Selective 3";
	public static final String PROGRAM_MULTI_TARGET_2 = "Multi-target 2";
	public static final String PROGRAM_MULTI_TARGET_3 = "Multi-target 3";
	public static final String PROGRAM_MULTI_TARGET_4 = "Multi-target 4";
	public static final String PROGRAM_LAUNCH = "Launch";
	public static final String PROGRAM_DOUBLE_FIRE = "Double Fire";
	
	private String		mID;
	private Ship2Design	mDesign;
	private String		mName;
	private Point3D		mLocation; // in mm with a scale of 1:1000,00,000
	private Point3D		mVelocity;
	private Point3D		mAcceleration;
	private int			mMissiles;
	private int			mSands;
    // data members
	private int			mPilotSkill;
    private char        mJumpNow;
    private char        mManNow;
    private char        mPowNow;
    private int         mHullHits;
    private int         mHoldHits;
    private int         mCrewHits;
    private int         mCompHits;
    private int         mFuelHits;
    private int			mSandHalo;
    private int			mTotalMissilesFired;
    private List<Ship2HardpointInstance> 	mTurrets;
    private List<String>mLoadedPrograms;
    private List<String>mStoredPrograms;
    private List<Ship2Instance> 	mSmallCraft;
    private Set<Ship2Instance> 	mFiredUponBy;
    private List<Point3D> 	mPastMovement;

    public Ship2Instance()
    {
    	mLocation = new Point3D();
    	mVelocity = new Point3D();
    	mAcceleration = new Point3D();
    	mTurrets = new ArrayList<Ship2HardpointInstance>();
    	mLoadedPrograms = new ArrayList<String>();
    	mStoredPrograms = new ArrayList<String>();
    	mSmallCraft = new ArrayList<Ship2Instance>();
    	mFiredUponBy = new HashSet<Ship2Instance>();
    	mPastMovement = new ArrayList<Point3D>();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
    	Ship2Instance ship = new Ship2Instance();
    	ship.mID = mID;
    	ship.mDesign = mDesign;
    	ship.mName = mName;
    	ship.getLocation().set(mLocation);
    	ship.getVelocity().set(mVelocity);
    	ship.mPilotSkill = mPilotSkill;
    	ship.mJumpNow = mJumpNow;
    	ship.mManNow = mManNow;
    	ship.mPowNow = mPowNow;
    	ship.mHullHits = mHullHits;
    	ship.mHoldHits = mHoldHits;
    	ship.mCrewHits = mCrewHits;
    	ship.mCompHits = mCompHits;
    	ship.mFuelHits = mFuelHits;
    	ship.mSandHalo = mSandHalo;
    	ship.mTotalMissilesFired = mTotalMissilesFired;
    	for (Ship2HardpointInstance hp : mTurrets)
    		ship.getTurrets().add((Ship2HardpointInstance)hp.clone());
    	ship.getLoadedPrograms().addAll(mLoadedPrograms);
    	ship.getStoredPrograms().addAll(mStoredPrograms);
    	for (Ship2Instance sc : mSmallCraft)
    		ship.getSmallCraft().add((Ship2Instance)sc.clone());
    	for (Point3D p : mPastMovement)
    		ship.getPastMovement().add(new Point3D(p));
    	return ship;
    }

    public void setDesign(Ship2Design design)
    {
        mDesign = design;
        mJumpNow = mDesign.getJumpType();
        mManNow = mDesign.getManType();
        mPowNow = mDesign.getPowType();
    }
    public Ship2Design getDesign()
    {
    	return mDesign;
    }

    public char getJumpNow()
    {
        return mJumpNow;
    }

    public void setJumpNow(char jumpNow)
    {
        mJumpNow = jumpNow;
    }

    public char getManNow()
    {
        return mManNow;
    }

    public void setManNow(char manNow)
    {
        mManNow = manNow;
    }

    public char getPowNow()
    {
        return mPowNow;
    }

    public void setPowNow(char powNow)
    {
        mPowNow = powNow;
    }

    public int getHullHits()
    {
        return mHullHits;
    }

    public void setHullHits(int hullHits)
    {
        mHullHits = hullHits;
    }

    public int getHoldHits()
    {
        return mHoldHits;
    }

    public void setHoldHits(int holdHits)
    {
        mHoldHits = holdHits;
    }

    public int getCrewHits()
    {
        return mCrewHits;
    }

    public void setCrewHits(int crewHits)
    {
        mCrewHits = crewHits;
    }

    public int getCompHits()
    {
        return mCompHits;
    }

    public void setCompHits(int compHits)
    {
        mCompHits = compHits;
    }

    public int getFuelHits()
    {
        return mFuelHits;
    }

    public void setFuelHits(int fuelHits)
    {
        mFuelHits = fuelHits;
    }

	public String getID()
	{
		return mID;
	}

	public void setID(String iD)
	{
		mID = iD;
	}

	public String getName()
	{
		return mName;
	}

	public void setName(String name)
	{
		mName = name;
	}

	public Point3D getLocation()
	{
		return mLocation;
	}

	public void setLocation(Point3D location)
	{
		mLocation = location;
	}

	public Point3D getVelocity()
	{
		return mVelocity;
	}

	public void setVelocity(Point3D velocity)
	{
		mVelocity = velocity;
	}

	public List<String> getLoadedPrograms()
	{
		return mLoadedPrograms;
	}

	public void setLoadedPrograms(List<String> loadedPrograms)
	{
		mLoadedPrograms = loadedPrograms;
	}

	public List<String> getStoredPrograms()
	{
		return mStoredPrograms;
	}

	public void setStoredPrograms(List<String> storedPrograms)
	{
		mStoredPrograms = storedPrograms;
	}

	public List<Ship2Instance> getSmallCraft()
	{
		return mSmallCraft;
	}

	public void setSmallCraft(List<Ship2Instance> smallCraft)
	{
		mSmallCraft = smallCraft;
	}

	public List<Ship2HardpointInstance> getTurrets()
	{
		return mTurrets;
	}

	public void setTurrets(List<Ship2HardpointInstance> turrets)
	{
		mTurrets = turrets;
	}

	public int getPilotSkill()
	{
		return mPilotSkill;
	}

	public void setPilotSkill(int pilotSkill)
	{
		mPilotSkill = pilotSkill;
	}

	public Point3D getAcceleration()
	{
		return mAcceleration;
	}

	public void setAcceleration(Point3D acceleration)
	{
		mAcceleration = acceleration;
	}

	public Set<Ship2Instance> getFiredUponBy()
	{
		return mFiredUponBy;
	}

	public void setFiredUponBy(Set<Ship2Instance> firedUponBy)
	{
		mFiredUponBy = firedUponBy;
	}

	public int getMissiles()
	{
		return mMissiles;
	}

	public void setMissiles(int missiles)
	{
		mMissiles = missiles;
	}

	public int getSands()
	{
		return mSands;
	}

	public void setSands(int sands)
	{
		mSands = sands;
	}

	public int getSandHalo()
	{
		return mSandHalo;
	}

	public void setSandHalo(int sandHalo)
	{
		mSandHalo = sandHalo;
	}

	public int getTotalMissilesFired()
	{
		return mTotalMissilesFired;
	}

	public void setTotalMissilesFired(int totalMissilesFired)
	{
		mTotalMissilesFired = totalMissilesFired;
	}

	public List<Point3D> getPastMovement()
	{
		return mPastMovement;
	}

	public void setPastMovement(List<Point3D> pastMovement)
	{
		mPastMovement = pastMovement;
	}
}
