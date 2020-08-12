package jo.ttg.lang.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.ttg.beans.LanguageBean;
import jo.ttg.beans.LanguageStatsBean;
import jo.util.utils.obj.IntegerUtils;

public class LangClassic implements ILanguage
{
    public static final String VERSION = "TravClassic";
    private LanguageBean    mLanguage;
    private boolean         mIsDefault;

    // constructors
    public LangClassic()
    {
    }
    
    public LangClassic(LanguageBean lang)
    {
        mLanguage = lang;
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
        json.put("name", mLanguage.getName());
        json.put("code", mLanguage.getAlliegence());
        LanguageStatsBean stats = mLanguage.getLanguageStats();
        json.put("wordLength", toJSONArray(stats.getWordLength()));
        json.put("initialSyllableTable", toJSONArray(stats.getInitialSyllableTable()));
        json.put("finalSyllableTable", toJSONArray(stats.getFinalSyllableTable()));
        json.put("firstConsonant", toJSONArray(stats.getFirstConsonant()));
        json.put("lastConsonant", toJSONArray(stats.getLastConsonant()));
        json.put("vowel", toJSONArray(stats.getVowel()));
        json.put("firstConsonantText", toJSONArray(stats.getFirstConsonantText()));
        json.put("lastConsonantText", toJSONArray(stats.getLastConsonantText()));
        json.put("vowelText", toJSONArray(stats.getVowelText()));
        return json;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray toJSONArray(int[] arr)
    {
        JSONArray json = new JSONArray();
        for (int i : arr)
            json.add(i);
        return json;
    }
    
    @SuppressWarnings("unchecked")
    private JSONArray toJSONArray(String[] arr)
    {
        JSONArray json = new JSONArray();
        for (String i : arr)
            json.add(i);
        return json;
    }

    @Override
    public void fromJSON(JSONObject json)
    {
        if (mLanguage == null)
            mLanguage = new LanguageBean();
        mLanguage.setName((String)json.get("name"));
        mLanguage.setAlliegence((String)json.get("code"));
        LanguageStatsBean stats = mLanguage.getLanguageStats();
        stats.setWordLength(toIntArray((JSONArray)json.get("wordLength")));
        stats.setInitialSyllableTable(toIntArray((JSONArray)json.get("initialSyllableTable")));
        stats.setFinalSyllableTable(toIntArray((JSONArray)json.get("finalSyllableTable")));
        stats.setFirstConsonant(toIntArray((JSONArray)json.get("firstConsonant")));
        stats.setLastConsonant(toIntArray((JSONArray)json.get("lastConsonant")));
        stats.setVowel(toIntArray((JSONArray)json.get("vowel")));
        stats.setFirstConsonantText(toStringArray((JSONArray)json.get("firstConsonantText")));
        stats.setLastConsonantText(toStringArray((JSONArray)json.get("lastConsonantText")));
        stats.setVowelText(toStringArray((JSONArray)json.get("vowelText")));
    }
    
    private int[] toIntArray(JSONArray arr)
    {
        int[] iarr = new int[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            iarr[i] = IntegerUtils.parseInt(arr.get(i));
        return iarr;
    }
    
    private String[] toStringArray(JSONArray arr)
    {
        String[] sarr = new String[arr.size()];
        for (int i = 0; i < arr.size(); i++)
            sarr[i] = arr.get(i).toString();
        return sarr;
    }

    @Override
    public String getName()
    {
        return mLanguage.getName();
    }

    @Override
    public String getCode()
    {
        return mLanguage.getAlliegence();
    }
    
    // utilities
    
    @Override
    public String toString()
    {
        return getName();
    }

    // getters and setters
    
    public LanguageBean getLanguage()
    {
        return mLanguage;
    }

    public void setLanguage(LanguageBean language)
    {
        mLanguage = language;
    }

    public boolean isDefault()
    {
        return mIsDefault;
    }

    public void setDefault(boolean isDefault)
    {
        mIsDefault = isDefault;
    }
}
