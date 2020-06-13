package jo.ttg.ship0.beans;



public class T0ShipInstance extends T0ShipDesign
{
    // data members
    private char        mJumpNow;
    private char        mManNow;
    private char        mPowNow;
    private int         mHullHits;
    private int         mHoldHits;
    private int         mCrewHits;
    private int         mCompHits;
    private int         mFuelHits;
    private boolean     mTurretDamage[];

    public T0ShipInstance()
    {
    }

    public void setDesign(T0ShipDesign design)
    {
        set(design);
        mJumpNow = getJumpType();
        mManNow = getManType();
        mPowNow = getPowType();
        //mTurretDamage = new boolean[mDesign.getHardpoints().length];
        mTurretDamage = new boolean[getTurretSize().length];
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

    public boolean[] getTurretDamage()
    {
        return mTurretDamage;
    }

    public void setTurretDamage(boolean[] turretDamage)
    {
        mTurretDamage = turretDamage;
    }
}
