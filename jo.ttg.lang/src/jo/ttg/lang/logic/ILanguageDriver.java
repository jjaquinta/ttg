package jo.ttg.lang.logic;

import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import jo.ttg.lang.data.ILanguage;

public interface ILanguageDriver
{
    public boolean isDriverFor(JSONObject json);
    public boolean isDriverFor(ILanguage lang);
    public ILanguage getLanguage(JSONObject json);
    public String  getWord(ILanguage lang, Random rnd);
    public List<ILanguage> getDefaults();
}
