package jo.ttg.ship0.beans;

import jo.util.beans.Bean;

public class T0ShipHardpoint extends Bean
{
    private int mTurretSize;
    private int mMissiles;
    private int mSandcasters;
    private int mBeamLasers;
    private int mPulseLasers;
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append(mTurretSize);
        sb.append(mMissiles);
        sb.append(mSandcasters);
        sb.append(mBeamLasers);
        sb.append(mPulseLasers);
        return sb.toString();
    }
    
    public void fromString(String str)
    {
        mTurretSize = Integer.parseInt(str.substring(0, 1));
        mMissiles = Integer.parseInt(str.substring(1, 2));
        mSandcasters = Integer.parseInt(str.substring(2, 3));
        mBeamLasers = Integer.parseInt(str.substring(3, 4));
        mPulseLasers = Integer.parseInt(str.substring(4, 5));
    }
    
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
