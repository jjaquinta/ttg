package jo.ttg.deckplans.logic;

import java.io.File;
import java.io.IOException;
import java.util.function.BiConsumer;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.deckplans.beans.RuntimeBean;
import jo.util.beans.PCSBean;
import jo.util.utils.BeanUtils;
import jo.util.utils.PCSBeanUtils;

public class RuntimeLogic
{
    private static RuntimeBean mRuntime;
    
    public static void init()
    {
        LibraryLogic.init();
        mRuntime = new RuntimeBean();
        loadSettings();
    }
    
    public static RuntimeBean getInstance()
    {
        return mRuntime;
    }
    
    public static void term()
    {
        saveSettings();
    }
    
    private static void loadSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        File pfile = new File(pdir, "decker.json");
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
            mRuntime.setStatus("");
        }
        catch (IOException e)
        {
            mRuntime.setStatus(e.toString());
            mRuntime.setSettings(new JSONObject());
        }
        mRuntime.setDirty(false);
    }
    
    static void saveSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        pdir.mkdirs();
        File pfile = new File(pdir, "decker.json");
        serializeToSettings();
        try
        {
            JSONUtils.writeJSON(pfile, mRuntime.getSettings());
            mRuntime.setStatus("");
        }
        catch (IOException e)
        {
            mRuntime.setStatus(e.toString());
        }
        mRuntime.setDirty(false);
    }
    
    private static void serializeFromSettings()
    {
        if (mRuntime.getSettings().containsKey("lastScan"))
            mRuntime.setLastScan(new File(mRuntime.getSettings().getString("lastScan")));
        if (mRuntime.getSettings().containsKey("lastPlan"))
            mRuntime.setLastPlan(new File(mRuntime.getSettings().getString("lastPlan")));
        if (mRuntime.getSettings().containsKey("lastDeck"))
            mRuntime.setLastDeck(new File(mRuntime.getSettings().getString("lastDeck")));
    }
    
    private static void serializeToSettings()
    {
        if (mRuntime.getLastPlan() != null)
            mRuntime.getSettings().put("lastPlan", mRuntime.getLastPlan().toString());
        if (mRuntime.getLastScan() != null)
            mRuntime.getSettings().put("lastScan", mRuntime.getLastScan().toString());
        if (mRuntime.getLastDeck() != null)
            mRuntime.getSettings().put("lastDeck", mRuntime.getLastDeck().toString());
    }
    
    public static Object getSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path);
    }
    
    public static boolean isSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path) != null;
    }
    
    public static void listen(String prop, BiConsumer<Object, Object> op)
    {
        PCSBean obj = mRuntime;
        for (;;)
        {
            int idx = prop.indexOf('.');
            if (idx < 0)
            {
                PCSBeanUtils.listen(obj, prop, op);
                break;
            }
            obj = (PCSBean)BeanUtils.get(obj, prop.substring(0, idx));
            prop = prop.substring(idx + 1);
        }
    }
    
    public static void setMode(int mode)
    {
        mRuntime.setMode(mode);
    }
    
    public static void setStatus(String status)
    {
        mRuntime.setStatus(status);
    }
}
