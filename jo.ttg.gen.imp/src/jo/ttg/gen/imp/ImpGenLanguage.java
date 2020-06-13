package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.LanguageBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.logic.LanguageLogic;
import jo.util.utils.obj.StringUtils;

public class ImpGenLanguage implements IGenLanguage
{
    ImpGenScheme   scheme;
    private LanguageBean    mDefault;

    public ImpGenLanguage(ImpGenScheme _scheme)
    {
        scheme = _scheme;
        mDefault = LanguageLogic.getLoadedLanguage("Im");
    }

    /*
    // languages
    static LanguageStats    lang_im;
    static int[] lang_imWordLength = { 19,11,5,1,0,0 };
    static int[] lang_imInitialSyllableTable = { 5,11,8,12 };
    static int[] lang_imFinalSyllableTable = { 17,3,8,8 };
    static int[] lang_imFirstConsonant = { 10,2,13,3,9,10,2,8,2,14,2,4,13,12,6,7,2,2,7,10,7,3,13,21,2,9,13,3,7 };
    static int[] lang_imLastConsonant = { 3,3,22,5,2,3,2,3,2,5,2,4,41,9,9,2,3,3,36,2,2,16,2,4,21,2,3,2,3 };
    static int[] lang_imVowel = { 49,73,40,38,7,9 };
    static String[] lang_imFirstConsonantText = { "b","br","c","ch","d","f","fr","g","gr","h","j","k","l","m","n","p","pl","pr","r","s","sh","st","t","th","tr","v","w","wh","y" };
    static String[] lang_imLastConsonantText = { "c","ck","d","f","ft","gh","ht","l","ld","ll","ly","m","n","nd","ng","ns","nt","p","r","rd","rs","s","ss","st","t","th","w","x","y" };
    static String[] lang_imVowelText = { "a","e","i","o","ou","u" };
    */

    public LanguageBean generateLanguage(OrdBean ul, OrdBean lr, String alliegence)
    {
        LanguageBean ret = LanguageLogic.getLoadedLanguage(alliegence);
        if (ret == null)
            ret = mDefault;
        //System.out.println(alliegence+"->"+ret.getName());
        // drift
        // ...
        return ret;
    }    
    
    public String generatePlaceName(OrdBean ul, OrdBean lr, String alliegence, RandBean r)
    {
        LanguageBean lang = generateLanguage(ul, lr, alliegence);
        return StringUtils.initialCaptial(LanguageLogic.getName(lang, r));
    }

    public String generatePersonalName(OrdBean ul, OrdBean lr, String alliegence, RandBean r)
    {
        LanguageBean lang = generateLanguage(ul, lr, alliegence);
        return StringUtils.initialCaptial(LanguageLogic.getName(lang, r)) + " " + StringUtils.initialCaptial(LanguageLogic.getName(lang, r));
    }

    public List<String> generateWords(OrdBean ul, OrdBean lr, String alliegence, RandBean r, int num)
    {
        LanguageBean lang = generateLanguage(ul, lr, alliegence);
        List<String> ret = new ArrayList<String>();
        while (num-- > 0)
        {
            ret.add(LanguageLogic.getName(lang, r));
        }
        return ret;
    }
}