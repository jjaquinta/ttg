package jo.ttg.lang.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.LangClassic;
import jo.ttg.lang.ui.ILanguageEditor;
import jo.ttg.lang.ui.edit.ClassicEditPanel;
import jo.util.utils.io.ResourceUtils;
import jo.util.utils.obj.IntegerUtils;

public class LangClassicDriver implements ILanguageDriver
{
    private static final String[] DEFAULT_SOURCE = 
    {
        "text/aslan.json",
        "text/bwap.json",
        "text/darrian.json",
        "text/droyne.json",
        "text/ithklur.json",
        "text/luriani.json",
        "text/kkree.json",
        "text/neoicelandic.json",
        "text/vargr.json",
        "text/vilani.json",
        "text/zhodani.json",
    };
    
    private List<ILanguage> mDefaults = null;

    @Override
    public boolean isDriverFor(JSONObject json)
    {
        return LangClassic.VERSION.equals(json.get("$version"));
    }

    @Override
    public boolean isDriverFor(ILanguage lang)
    {
        return lang instanceof LangClassic;
    }
    
    @Override
    public ILanguage getLanguage(JSONObject json)
    {
        return new LangClassic(json);
    }
    
    @Override
    public ILanguageEditor getEditor()
    {
        return new ClassicEditPanel();
    }

    @Override
    public String getWord(ILanguage lang, Random rnd)
    {
        LangClassic l = (LangClassic)lang;
        int wordLength = IntegerUtils.parseInt(getWeightedOption(l.getWordLength(), rnd));
        StringBuffer word = new StringBuffer();
        boolean alt = false;
        for (int i = 0; i < wordLength; i++)
        {
            String type;
            if (alt)
                type = getWeightedOption(l.getFinalSyllableTable(), rnd);
            else
                type = getWeightedOption(l.getInitialSyllableTable(), rnd);
            if (type.startsWith("c"))
                word.append(getWeightedOption(l.getFirstConsonant(), rnd));
            word.append(getWeightedOption(l.getVowel(), rnd));
            if (type.endsWith("c"))
            {
                word.append(getWeightedOption(l.getLastConsonant(), rnd));
                alt = true;
            }
            else
                alt = false;
        }
        return word.toString();
    }
    
    private String getWeightedOption(Map<String,Integer> options, Random rnd)
    {
        String[] keys = options.keySet().toArray(new String[0]);
        int total = 0;
        for (String key : keys)
            total += options.get(key);
        int roll = rnd.nextInt(total);
        for (String key : keys)
        {
            roll -= options.get(key);
            if (roll < 0)
                return key;
        }
        throw new IllegalStateException("We fell off the end of the table");
    }

    @Override
    public List<ILanguage> getDefaults()
    {
        if (mDefaults == null)
        {
            mDefaults = new ArrayList<ILanguage>();
            for (String src : DEFAULT_SOURCE)
            {
                String text;
                try
                {
                    text = new String(ResourceUtils.loadSystemResourceBinary(src, LangClassicDriver.class), "utf-8");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    continue;
                }
                JSONObject json = JSONUtils.readJSONString(text);
                LangClassic lang = new LangClassic(json);
                lang.setDefault(true);
                mDefaults.add(lang);
            }
        }
        return mDefaults;
    }

    @Override
    public ILanguage newLanguage(String name, String code, ILanguage cloneLang)
    {
        LangClassic lang;
        if (cloneLang instanceof LangClassic)
            lang = new LangClassic(cloneLang.toJSON());
        else
            lang = new LangClassic();
        lang.setName(name);
        lang.setCode(code);
        lang.setDefault(false);
        return lang;
    }
    
    @Override
    public String toString()
    {
        return "Classic";
    }
}
