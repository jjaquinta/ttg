package jo.ttg.lbb.data.ship2;

import jo.util.beans.Bean;

public class Ship2HardpointDesign extends Bean
{
    private int mTurretSize;
    private int mMissiles;
    private int mSandcasters;
    private int mBeamLasers;
    private int mPulseLasers;
    
    public int getTurretSize()
    {
        return mTurretSize;
    }
    public void setTurretSize(int turretSize)
    {
        mTurretSize = turretSize;
    }
    public int getMissiles()
    {
        return mMissiles;
    }
    public void setMissiles(int missiles)
    {
        mMissiles = missiles;
    }
    public int getSandcasters()
    {
        return mSandcasters;
    }
    public void setSandcasters(int sandcasters)
    {
        mSandcasters = sandcasters;
    }
    public int getBeamLasers()
    {
        return mBeamLasers;
    }
    public void setBeamLasers(int beamLasers)
    {
        mBeamLasers = beamLasers;
    }
    public int getPulseLasers()
    {
        return mPulseLasers;
    }
    public void setPulseLasers(int pulseLasers)
    {
        mPulseLasers = pulseLasers;
    }
}
