package jo.ttg.lang.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import jo.ttg.beans.LanguageBean;
import jo.ttg.beans.RandBean;
import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.LangClassic;
import jo.ttg.logic.LanguageLogic;

public class LangClassicDriver implements ILanguageDriver
{
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
    public String getWord(ILanguage lang, Random rnd)
    {
        LangClassic l = (LangClassic)lang;
        RandBean r = new RandBean(rnd.nextLong());
        String word = LanguageLogic.getWord(l.getLanguage(), r);
        return word;
    }

    @Override
    public List<ILanguage> getDefaults()
    {
        if (mDefaults == null)
        {
            mDefaults = new ArrayList<ILanguage>();
            for (LanguageBean l : LanguageLogic.getLoadedLanguages())
            {
                LangClassic lang = new LangClassic(l);
                lang.setDefault(true);
                mDefaults.add(lang);
            }
        }
        return mDefaults;
    }

}
