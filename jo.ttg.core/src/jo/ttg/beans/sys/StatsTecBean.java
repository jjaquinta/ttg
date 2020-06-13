package jo.ttg.beans.sys;

import jo.util.beans.Bean;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class StatsTecBean extends Bean
{
    /**
      * Low Average Tech
      */
    private int mLow;
    /**
      * Energy Tech Level
      */
    private int mEnergy;
    /**
      * Robotic Tech Level
      */
    private int mRobot;
    /**
      * Communications Tech Level
      */
    private int mCommo;
    /**
      * Medical Tech Level
      */
    private int mMedical;
    /**
      * Environmental systems Tech Level
      */
    private int mEnvironmental;
    /**
      * Land Transport Tech Level
      */
    private int mLand;
    /**
      * Water Transport Tech Level
      */
    private int mWater;
    /**
      * Air Transport Tech Level
      */
    private int mAir;
    /**
      * Space Transport Tech Level
      */
    private int mSpace;
    /**
      * Personal Military Tech Level
      */
    private int mPersonalMilitary;
    /**
      * Heavy Military Tech Level
      */
    private int mHeavyMilitary;
    /**
      * Novelty Tech Level
      */
    private int mNovel;

    // constructor

    public StatsTecBean()
    {
        mLow = 0;
        mEnergy = 0;
        mRobot = 0;
        mCommo = 0;
        mMedical = 0;
        mEnvironmental = 0;
        mLand = 0;
        mWater = 0;
        mAir = 0;
        mSpace = 0;
        mPersonalMilitary = 0;
        mHeavyMilitary = 0;
        mNovel = 0;
    }

    /**
     * Returns the air.
     * @return int
     */
    public int getAir()
    {
        return mAir;
    }

    /**
     * Returns the commo.
     * @return int
     */
    public int getCommo()
    {
        return mCommo;
    }

    /**
     * Returns the energy.
     * @return int
     */
    public int getEnergy()
    {
        return mEnergy;
    }

    /**
     * Returns the environmental.
     * @return int
     */
    public int getEnvironmental()
    {
        return mEnvironmental;
    }

    /**
     * Returns the heavyMilitary.
     * @return int
     */
    public int getHeavyMilitary()
    {
        return mHeavyMilitary;
    }

    /**
     * Returns the land.
     * @return int
     */
    public int getLand()
    {
        return mLand;
    }

    /**
     * Returns the low.
     * @return int
     */
    public int getLow()
    {
        return mLow;
    }

    /**
     * Returns the medical.
     * @return int
     */
    public int getMedical()
    {
        return mMedical;
    }

    /**
     * Returns the novel.
     * @return int
     */
    public int getNovel()
    {
        return mNovel;
    }

    /**
     * Returns the personalMilitary.
     * @return int
     */
    public int getPersonalMilitary()
    {
        return mPersonalMilitary;
    }

    /**
     * Returns the robot.
     * @return int
     */
    public int getRobot()
    {
        return mRobot;
    }

    /**
     * Returns the space.
     * @return int
     */
    public int getSpace()
    {
        return mSpace;
    }

    /**
     * Returns the water.
     * @return int
     */
    public int getWater()
    {
        return mWater;
    }

    /**
     * Sets the air.
     * @param air The air to set
     */
    public void setAir(int air)
    {
        mAir = air;
    }

    /**
     * Sets the commo.
     * @param commo The commo to set
     */
    public void setCommo(int commo)
    {
        mCommo = commo;
    }

    /**
     * Sets the energy.
     * @param energy The energy to set
     */
    public void setEnergy(int energy)
    {
        mEnergy = energy;
    }

    /**
     * Sets the environmental.
     * @param environmental The environmental to set
     */
    public void setEnvironmental(int environmental)
    {
        mEnvironmental = environmental;
    }

    /**
     * Sets the heavyMilitary.
     * @param heavyMilitary The heavyMilitary to set
     */
    public void setHeavyMilitary(int heavyMilitary)
    {
        mHeavyMilitary = heavyMilitary;
    }

    /**
     * Sets the land.
     * @param land The land to set
     */
    public void setLand(int land)
    {
        mLand = land;
    }

    /**
     * Sets the low.
     * @param low The low to set
     */
    public void setLow(int low)
    {
        mLow = low;
    }

    /**
     * Sets the medical.
     * @param medical The medical to set
     */
    public void setMedical(int medical)
    {
        mMedical = medical;
    }

    /**
     * Sets the novel.
     * @param novel The novel to set
     */
    public void setNovel(int novel)
    {
        mNovel = novel;
    }

    /**
     * Sets the personalMilitary.
     * @param personalMilitary The personalMilitary to set
     */
    public void setPersonalMilitary(int personalMilitary)
    {
        mPersonalMilitary = personalMilitary;
    }

    /**
     * Sets the robot.
     * @param robot The robot to set
     */
    public void setRobot(int robot)
    {
        mRobot = robot;
    }

    /**
     * Sets the space.
     * @param space The space to set
     */
    public void setSpace(int space)
    {
        mSpace = space;
    }

    /**
     * Sets the water.
     * @param water The water to set
     */
    public void setWater(int water)
    {
        mWater = water;
    }

}
