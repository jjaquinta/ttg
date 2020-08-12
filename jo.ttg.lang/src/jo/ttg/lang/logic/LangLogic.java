package jo.ttg.lang.logic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.lang.data.ILanguage;

public class LangLogic
{
    private static ILanguageDriver[] DRIVERS = {
            new LangClassicDriver(),
    };
    private static Random mRND = new Random();

    private static ILanguageDriver getDriverFor(JSONObject l)
    {
        for (ILanguageDriver driver : DRIVERS)
            if (driver.isDriverFor(l))
                return driver;
        return null;
    }
    
    private static ILanguageDriver getDriverFor(ILanguage l)
    {
        for (ILanguageDriver driver : DRIVERS)
            if (driver.isDriverFor(l))
                return driver;
        return null;
    }
    
    private static List<ILanguage> mDefaultLanguages = null;
    
    public static List<ILanguage> getDefaultLanguages()
    {
        if (mDefaultLanguages == null)
        {
            mDefaultLanguages = new ArrayList<ILanguage>();
            for (ILanguageDriver driver : DRIVERS)
                mDefaultLanguages.addAll(driver.getDefaults());
        }
        return mDefaultLanguages;
    }
    
    private static List<ILanguage> mAllLanguages = null;
    
    public static List<ILanguage> getAllLanguages()
    {
        if (mAllLanguages == null)
        {
            mAllLanguages = new ArrayList<>();
            mAllLanguages.addAll(getDefaultLanguages());
        }
        return mAllLanguages;
    }
    
    public static void addLanguage(ILanguage lang)
    {
        getAllLanguages().add(lang);
    }
    
    public static void removeLanguage(ILanguage lang)
    {
        getAllLanguages().remove(lang);
    }
    
    @SuppressWarnings("unchecked")
    public static void saveLanguages(File f) throws IOException
    {
        JSONObject json = new JSONObject();
        JSONArray languages = new JSONArray();
        json.put("languages", languages);
        for (ILanguage lang : getAllLanguages())
            if (!lang.isDefault())
            {
                JSONObject l = lang.toJSON();
                languages.add(l);
            }
        JSONUtils.writeJSON(f, json);
    }
    
    public static void loadLanguages(File f) throws IOException
    {
        JSONObject json = JSONUtils.readJSON(f);
        JSONArray languages = (JSONArray)json.get("languages");
        // clean out old
        for (ILanguage lang : getAllLanguages().toArray(new ILanguage[0]))
            if (!lang.isDefault())
                removeLanguage(lang);
        // add in new   
        for (int i = 0; i < languages.size(); i++)
        {
            JSONObject l = (JSONObject)languages.get(i);
            ILanguageDriver driver = getDriverFor(l);
            if (driver != null)
                addLanguage(driver.getLanguage(l));
        }
    }
    
    public static String generateWord(ILanguage lang, Random rnd)
    {
        ILanguageDriver driver = getDriverFor(lang);
        if (driver == null)
            return null;
        if (rnd == null)
            rnd = mRND;
        return driver.getWord(lang, rnd);
    }
}
