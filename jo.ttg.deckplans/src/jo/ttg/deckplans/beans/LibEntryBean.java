package jo.ttg.deckplans.beans;

import org.json.simple.JSONObject;

public class LibEntryBean
{
    private String     mTitle;
    private int        mTechLevel;
    private int        mDisplacement;
    private int        mJump;
    private int        mManeuver;
    private String     mHTML;
    private JSONObject mJSON;
    
    public String getTitle()
    {
        return mTitle;
    }
    public void setTitle(String title)
    {
        mTitle = title;
    }
    public int getTechLevel()
    {
        return mTechLevel;
    }
    public void setTechLevel(int techLevel)
    {
        mTechLevel = techLevel;
    }
    public int getDisplacement()
    {
        return mDisplacement;
    }
    public void setDisplacement(int displacement)
    {
        mDisplacement = displacement;
    }
    public int getJump()
    {
        return mJump;
    }
    public void setJump(int jump)
    {
        mJump = jump;
    }
    public int getManeuver()
    {
        return mManeuver;
    }
    public void setManeuver(int maneuver)
    {
        mManeuver = maneuver;
    }
    public String getHTML()
    {
        return mHTML;
    }
    public void setHTML(String hTML)
    {
        mHTML = hTML;
    }
    public JSONObject getJSON()
    {
        return mJSON;
    }
    public void setJSON(JSONObject jSON)
    {
        mJSON = jSON;
    }
}
