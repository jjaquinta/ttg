package jo.ttg.lang.logic;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.function.BiConsumer;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.RuntimeBean;
import jo.util.utils.PCSBeanUtils;

public class RuntimeLogic
{
    private static RuntimeBean mRuntime = null;
    
    public static RuntimeBean getInstance()
    {
        if (mRuntime == null)
            init();
        return mRuntime;
    }
    
    public static void init()
    {
        mRuntime = new RuntimeBean();
        updateLanguages();
        loadSettings();
    }
    
    public static void term()
    {
        saveSettings();
    }
    
    private static void loadSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        File pfile = new File(pdir, "linguistics.json");
        if (!pfile.exists())
        {
            mRuntime.setSettings(new JSONObject());
            return;
        }
        try
        {
            JSONObject json = JSONUtils.readJSON(pfile);
            mRuntime.setSettings(json);
            serializeFromSettings();
        }
        catch (IOException e)
        {
            mRuntime.setError(e.toString());
            mRuntime.setSettings(new JSONObject());
        }
    }
    
    static void saveSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        pdir.mkdirs();
        File pfile = new File(pdir, "linguistics.json");
        serializeToSettings();
        try
        {
            JSONUtils.writeJSON(pfile, mRuntime.getSettings());
        }
        catch (IOException e)
        {
            mRuntime.setError(e.toString());
        }
    }
    
    private static void serializeFromSettings()
    {
        if (mRuntime.getSettings().containsKey("selectedLanguage"))
            updateSelectedLanguage(mRuntime.getSettings().getString("selectedLanguage"));
    }
    
    private static void serializeToSettings()
    {
        if (mRuntime.getSelectedLanguage() == null)
            mRuntime.getSettings().remove("selectedLanguage");
        else
            mRuntime.getSettings().put("selectedLanguage", mRuntime.getSelectedLanguage().getName());
    }
    
    public static Object getSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path);
    }
    
    public static boolean isSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path) != null;
    }
    
    public static void listen(String prop, BiConsumer<Object, Object> action)
    {
        PCSBeanUtils.listen(RuntimeLogic.getInstance(), prop, action);
    }
    
    private static void updateLanguages()
    {
        String selected = null;
        if (mRuntime.getSelectedLanguage() != null)
            selected = mRuntime.getSelectedLanguage().getName();
        mRuntime.getLanguages().clear();
        mRuntime.getLanguages().addAll(LangLogic.getAllLanguages());
        Collections.sort(mRuntime.getLanguages(), new Comparator<ILanguage>() {
            @Override
            public int compare(ILanguage o1, ILanguage o2)
            {
                return o1.getName().compareTo(o2.getName());
            }
        });
        mRuntime.fireMonotonicPropertyChange("languages", mRuntime.getLanguages());
        updateSelectedLanguage(selected);
    }

    protected static void updateSelectedLanguage(String selected)
    {
        if (selected == null)
            mRuntime.setSelectedLanguage(null);
        else
            for (ILanguage lang : mRuntime.getLanguages())
                if (lang.getName().equals(selected))
                {
                    mRuntime.setSelectedLanguage(lang);
                    break;
                }
    }

    public static void setSelectedLanguage(ILanguage lang)
    {
        if (lang != mRuntime.getSelectedLanguage())
            mRuntime.setSelectedLanguage(lang);
    }
}
