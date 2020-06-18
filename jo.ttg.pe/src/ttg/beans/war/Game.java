/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.beans.war;

import java.util.ArrayList;
import java.util.List;

import jo.util.beans.PCSBean;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class Game extends PCSBean
{
    private String       mName;
    private String       mDescription;
    private String       mAuthor;
    private String       mVersion;
    private List<Side>   mSides;
    private List<Ship>   mShips;
    private List<String> mOverrides;
    private String       mUpperBound;
    private String       mLowerBound;
    private boolean      mAllowConvertNeutral;
    private boolean      mAllowOmniscentSensors;
    private boolean      mAllowFleetReconfiguration;
    private boolean      mAllowIntrinsicDefense;
    private boolean      mAllowConstruction;
    private String       mVPGainWorld;
    private String       mVPLoseWorld;
    private String       mVPHaveWorld;
    private String       mVPGainShip;
    private String       mVPLoseShip;
    private String       mVPHaveShip;
    private int          mGameLength;
    private String       mRawText;
    private String       mResourceGenerationFormula;
    private String       mConstructionPerTurnFormula;
    private List<Ship>   mShipLibrary;

    public Game()
    {
        mName = "";
        mDescription = "";
        mAuthor = "";
        mVersion = "";
        mSides = new ArrayList<>();
        mShips = new ArrayList<>();
        mOverrides = new ArrayList<>();
        mVPGainWorld = "";
        mVPLoseWorld = "";
        mVPHaveWorld = "";
        mVPLoseShip = "";
        mVPHaveShip = "";
        mAllowOmniscentSensors = true;
        mResourceGenerationFormula = "Pop*Port*2";
        mConstructionPerTurnFormula = "200";
        mShipLibrary = new ArrayList<>();
    }

    public String toString()
    {
        return mName;
    }

    /**
     * @return
     */
    public List<Ship> getShips()
    {
        return mShips;
    }

    /**
     * @param list
     */
    public void setShips(List<Ship> list)
    {
        mShips = list;
    }

    public String getUpperBound()
    {
        return mUpperBound;
    }

    public void setUpperBound(String v)
    {
        mUpperBound = v;
    }

    public String getLowerBound()
    {
        return mLowerBound;
    }

    public void setLowerBound(String v)
    {
        mLowerBound = v;
    }

    public List<Side> getSides()
    {
        return mSides;
    }

    public void setSides(List<Side> list)
    {
        mSides = list;
    }

    /**
     * @return
     */
    public boolean isAllowConvertNeutral()
    {
        return mAllowConvertNeutral;
    }

    /**
     * @param b
     */
    public void setAllowConvertNeutral(boolean b)
    {
        mAllowConvertNeutral = b;
    }

    public String getVPGainWorld()
    {
        return mVPGainWorld;
    }

    public String getVPHaveShip()
    {
        return mVPHaveShip;
    }

    public String getVPHaveWorld()
    {
        return mVPHaveWorld;
    }

    public String getVPLoseShip()
    {
        return mVPLoseShip;
    }

    public String getVPLoseWorld()
    {
        return mVPLoseWorld;
    }

    public void setVPGainWorld(String string)
    {
        mVPGainWorld = string;
    }

    public void setVPHaveShip(String string)
    {
        mVPHaveShip = string;
    }

    public void setVPHaveWorld(String string)
    {
        mVPHaveWorld = string;
    }

    public void setVPLoseShip(String string)
    {
        mVPLoseShip = string;
    }

    public void setVPLoseWorld(String string)
    {
        mVPLoseWorld = string;
    }

    public int getGameLength()
    {
        return mGameLength;
    }

    public void setGameLength(int i)
    {
        mGameLength = i;
    }

    public String getAuthor()
    {
        return mAuthor;
    }

    public String getDescription()
    {
        return mDescription;
    }

    public String getName()
    {
        return mName;
    }

    public void setAuthor(String string)
    {
        mAuthor = string;
    }

    public void setDescription(String string)
    {
        mDescription = string;
    }

    public void setName(String string)
    {
        mName = string;
    }

    public List<String> getOverrides()
    {
        return mOverrides;
    }

    public void setOverrides(List<String> list)
    {
        mOverrides = list;
    }

    /**
     * @return
     */
    public String getRawText()
    {
        return mRawText;
    }

    /**
     * @param string
     */
    public void setRawText(String string)
    {
        mRawText = string;
    }

    /**
     * @return
     */
    public String getVersion()
    {
        return mVersion;
    }

    /**
     * @param string
     */
    public void setVersion(String string)
    {
        mVersion = string;
    }

    /**
     * @return
     */
    public String getVPGainShip()
    {
        return mVPGainShip;
    }

    /**
     * @param string
     */
    public void setVPGainShip(String string)
    {
        mVPGainShip = string;
    }

    public boolean isAllowOmniscentSensors()
    {
        return mAllowOmniscentSensors;
    }

    public void setAllowOmniscentSensors(boolean b)
    {
        mAllowOmniscentSensors = b;
    }

    public boolean isAllowFleetReconfiguration()
    {
        return mAllowFleetReconfiguration;
    }

    public void setAllowFleetReconfiguration(boolean b)
    {
        mAllowFleetReconfiguration = b;
    }

    public boolean isAllowIntrinsicDefense()
    {
        return mAllowIntrinsicDefense;
    }

    public void setAllowIntrinsicDefense(boolean b)
    {
        mAllowIntrinsicDefense = b;
    }

    public boolean isAllowConstruction()
    {
        return mAllowConstruction;
    }

    public void setAllowConstruction(boolean b)
    {
        mAllowConstruction = b;
    }

    public String getResourceGenerationFormula()
    {
        return mResourceGenerationFormula;
    }

    public void setResourceGenerationFormula(String string)
    {
        mResourceGenerationFormula = string;
    }

    /**
     * @return
     */
    public String getConstructionPerTurnFormula()
    {
        return mConstructionPerTurnFormula;
    }

    /**
     * @param string
     */
    public void setConstructionPerTurnFormula(String string)
    {
        mConstructionPerTurnFormula = string;
    }

    /**
     * @return
     */
    public List<Ship> getShipLibrary()
    {
        return mShipLibrary;
    }

    /**
     * @param list
     */
    public void setShipLibrary(List<Ship> list)
    {
        mShipLibrary = list;
    }

}
