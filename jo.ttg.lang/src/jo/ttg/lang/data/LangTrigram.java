package jo.ttg.lang.data;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import jo.util.utils.obj.IntegerUtils;

public class LangTrigram implements ILanguage
{
    public static final String    VERSION = "TrigramLanguage";
    private String                mName;
    private String                mCode;
    private Map<String, Integer>  mTrigrams = new HashMap<String, Integer>();
    private boolean               mIsDefault;

    // constructors
    public LangTrigram()
    {
    }

    public LangTrigram(JSONObject json)
    {
        fromJSON(json);
    }

    // interface functions

    @Override
    public JSONObject toJSON()
    {
        JSONObject json = new JSONObject();
        json.put("$version", VERSION);
        json.put("name", mName);
        json.put("code", mCode);
        JSONObject trigrams = new JSONObject();
        for (String key : mTrigrams.keySet())
            trigrams.put(key, mTrigrams.get(key));
        json.put("trigrams", trigrams);
        JSONObject wordLength = new JSONObject();
        json.put("wordLength", wordLength);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json)
    {
        mName = (String)json.get("name");
        mCode = (String)json.get("code");
        mTrigrams.clear();
        JSONObject trigrams = (JSONObject)json.get("trigrams");
        for (String key : trigrams.keySet())
            mTrigrams.put(key, IntegerUtils.parseInt(trigrams.get(key)));
    }

    @Override
    public String getName()
    {
        return mName;
    }

    @Override
    public String getCode()
    {
        return mCode;
    }

    // utilities

    @Override
    public String toString()
    {
        return getName();
    }

    // getters and setters

    public boolean isDefault()
    {
        return mIsDefault;
    }

    public void setDefault(boolean isDefault)
    {
        mIsDefault = isDefault;
    }

    public Map<String, Integer> getTrigrams()
    {
        return mTrigrams;
    }

    public void setTrigrams(Map<String, Integer> trigrams)
    {
        mTrigrams = trigrams;
    }

    public boolean isIsDefault()
    {
        return mIsDefault;
    }

    public void setIsDefault(boolean isDefault)
    {
        mIsDefault = isDefault;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public void setCode(String code)
    {
        mCode = code;
    }
}
