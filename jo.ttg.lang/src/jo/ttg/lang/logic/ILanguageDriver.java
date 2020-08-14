package jo.ttg.lang.logic;

import java.util.List;
import java.util.Random;

import org.json.simple.JSONObject;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.ui.ILanguageEditor;

public interface ILanguageDriver
{
    public boolean isDriverFor(JSONObject json);
    public boolean isDriverFor(ILanguage lang);
    public ILanguage getLanguage(JSONObject json);
    public ILanguage newLanguage(String name, String code, ILanguage lang);
    public String  getWord(ILanguage lang, Random rnd);
    public List<ILanguage> getDefaults();
    public ILanguageEditor getEditor();
}
