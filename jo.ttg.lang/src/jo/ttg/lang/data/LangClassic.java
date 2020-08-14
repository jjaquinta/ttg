package jo.ttg.lang.data;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import jo.util.utils.obj.IntegerUtils;

public class LangClassic implements ILanguage
{
    public static final String VERSION = "TravClassic";

    private String                mName;
    private String                mCode;
    private Map<String, Integer>  mWordLength = new HashMap<String, Integer>();
    private Map<String, Integer>  mInitialSyllableTable = new HashMap<String, Integer>();
    private Map<String, Integer>  mFinalSyllableTable = new HashMap<String, Integer>();
    private Map<String, Integer>  mFirstConsonant = new HashMap<String, Integer>();
    private Map<String, Integer>  mLastConsonant = new HashMap<String, Integer>();
    private Map<String, Integer>  mVowel = new HashMap<String, Integer>();
    private boolean               mIsDefault;

    // constructors
    public LangClassic()
    {
        mName = "Simlish";
        mCode = "Si";
        mWordLength.put("1", 1);
        mWordLength.put("2", 1);
        mWordLength.put("3", 1);
        mWordLength.put("4", 1);
        mWordLength.put("5", 1);
        mWordLength.put("6", 1);
        mInitialSyllableTable.put("v", 1);
        mInitialSyllableTable.put("cv", 1);
        mInitialSyllableTable.put("vc", 1);
        mInitialSyllableTable.put("cvc", 1);
        mFinalSyllableTable.put("v", 1);
        mFinalSyllableTable.put("cv", 1);
        mFinalSyllableTable.put("vc", 1);
        mFinalSyllableTable.put("cvc", 1);
        mFirstConsonant.put("f", 1);
        mVowel.put("u", 1);
        mLastConsonant.put("k", 1);
    }
    
    public LangClassic(JSONObject json)
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
        json.put("wordLength", toJSONObject(mWordLength));
        json.put("initialSyllableTable", toJSONObject(mInitialSyllableTable));
        json.put("finalSyllableTable", toJSONObject(mFinalSyllableTable));
        json.put("firstConsonant", toJSONObject(mFirstConsonant));
        json.put("lastConsonant", toJSONObject(mLastConsonant));
        json.put("vowel", toJSONObject(mVowel));
        return json;
    }
    
    private JSONObject toJSONObject(Map<String,Integer> map)
    {
        JSONObject json = new JSONObject();
        for (String key : map.keySet())
            json.put(key, map.get(key));
        return json;
    }

    @Override
    public void fromJSON(JSONObject json)
    {
        mName = (String)json.get("name");
        mCode = (String)json.get("code");
        mWordLength = toMap((JSONObject)json.get("wordLength"));
        mInitialSyllableTable = toMap((JSONObject)json.get("initialSyllableTable"));
        mFinalSyllableTable = toMap((JSONObject)json.get("finalSyllableTable"));
        mFirstConsonant = toMap((JSONObject)json.get("firstConsonant"));
        mLastConsonant = toMap((JSONObject)json.get("lastConsonant"));
        mVowel = toMap((JSONObject)json.get("vowel"));
    }
    
    private Map<String,Integer> toMap(JSONObject obj)
    {
        Map<String,Integer> map = new HashMap<>();
        for (String key : obj.keySet())
            map.put(key, IntegerUtils.parseInt(obj.get(key)));
        return map;
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

    public String getName()
    {
        return mName;
    }

    public void setName(String name)
    {
        mName = name;
    }

    public String getCode()
    {
        return mCode;
    }

    public void setCode(String code)
    {
        mCode = code;
    }

    public Map<String, Integer> getWordLength()
    {
        return mWordLength;
    }

    public void setWordLength(Map<String, Integer> wordLength)
    {
        mWordLength = wordLength;
    }

    public Map<String, Integer> getInitialSyllableTable()
    {
        return mInitialSyllableTable;
    }

    public void setInitialSyllableTable(Map<String, Integer> initialSyllableTable)
    {
        mInitialSyllableTable = initialSyllableTable;
    }

    public Map<String, Integer> getFinalSyllableTable()
    {
        return mFinalSyllableTable;
    }

    public void setFinalSyllableTable(Map<String, Integer> finalSyllableTable)
    {
        mFinalSyllableTable = finalSyllableTable;
    }

    public Map<String, Integer> getFirstConsonant()
    {
        return mFirstConsonant;
    }

    public void setFirstConsonant(Map<String, Integer> firstConsonant)
    {
        mFirstConsonant = firstConsonant;
    }

    public Map<String, Integer> getLastConsonant()
    {
        return mLastConsonant;
    }

    public void setLastConsonant(Map<String, Integer> lastConsonant)
    {
        mLastConsonant = lastConsonant;
    }

    public Map<String, Integer> getVowel()
    {
        return mVowel;
    }

    public void setVowel(Map<String, Integer> vowel)
    {
        mVowel = vowel;
    }

    public boolean isIsDefault()
    {
        return mIsDefault;
    }

    public void setIsDefault(boolean isDefault)
    {
        mIsDefault = isDefault;
    }
}
