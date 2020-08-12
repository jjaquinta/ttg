/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.LanguageBean;
import jo.ttg.beans.LanguageStatsBean;
import jo.ttg.beans.RandBean;
import jo.util.utils.io.ReaderUtils;
import jo.util.utils.obj.IntegerUtils;

public class LanguageLogic
{

    private static void    appendSyllable(StringBuffer buf, RandBean r, int arr[], String sylbuf[])
    {
        int v, t;

        v = RandLogic.rand(r)%216;
        t = syslookup(arr, v);
        if (!sylbuf[t].equals(""))
        {
            v++;
        }
        buf.append(sylbuf[t]);
    }
    private static String   getText(LanguageBean lang, RandBean r, int sylMod)
    {
        StringBuffer    nbuf;
        int leng, type;

        if (r == null)
            r = mRND;
        leng = syslookup(lang.getLanguageStats().getWordLength(), RandLogic.rand(r)%36) + sylMod;
        nbuf = new StringBuffer();
        type = 0;
        do {
            type = syslookup(
                (type != 0) ?
                        lang.getLanguageStats().getFinalSyllableTable() :
                            lang.getLanguageStats().getInitialSyllableTable(),
                RandLogic.rand(r)%36);
            if ((type%2) != 0)
            {
                appendSyllable(nbuf, r, lang.getLanguageStats().getFirstConsonant(), lang.getLanguageStats().getFirstConsonantText());
            }
            appendSyllable(nbuf, r, lang.getLanguageStats().getVowel(), lang.getLanguageStats().getVowelText());
            if (type>1)
            {
                appendSyllable(nbuf, r, lang.getLanguageStats().getLastConsonant(), lang.getLanguageStats().getLastConsonantText());
            }
            type = type/2;
        } while (leng-- > 0);
        String s = nbuf.toString();
        return s;
    }
    public static String   getName(LanguageBean lang, RandBean r)
    {
        String s = getText(lang, r, 1);
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }
    public static String   getWord(LanguageBean lang, RandBean r)
    {
        String s = getText(lang, r, 0);
        return s;
    }
    private static int     syslookup(int arr[], int tot)
    {
        int i;

        //System.out.println("Looking up "+tot+" out of "+totUp(arr));
        i = 0;
        do {
            tot -= arr[i++];
        } while (tot >= 0);
        return(i - 1);
    }
    
//    private static int totUp(int[] arr)
//    {
//        int tot = 0;
//        for (int i = 0; i < arr.length; i++)
//            tot += arr[i];
//        return tot;
//    }
    
    private static RandBean mRND = new RandBean();
    private static List<LanguageBean> mLanguages = null;
    private static final String[] DEFAULT_LANGUAGES = {
        "aelyael.lng",
        "aslan.lng",
        "bwap.lng",
        "darrian.lng",
        "droyne.lng",
        "gurvin.lng",
        "imperial.lng",
        "ithklur.lng",
        "kkree.lng",
        "neoicelandic.lng",
        "vargr.lng",
        "vilani.lng",
        "zhodani.lng",        
    };
    
    public static void loadLanguages()
    {
        if (mLanguages != null)
            return;
        mLanguages = new ArrayList<LanguageBean>();
        for (int i = 0; i < DEFAULT_LANGUAGES.length; i++)
        {
            try
            {
                String path = "langs/"+DEFAULT_LANGUAGES[i];
                InputStream is = LanguageLogic.class.getResourceAsStream(path);
                LanguageBean lang = readLanguage(new InputStreamReader(is, "utf-8"));
                is.close();
                mLanguages.add(lang);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
    
    public static Collection<LanguageBean> getLoadedLanguages()
    {
        loadLanguages();
        return mLanguages;
    }
    
    public static LanguageBean getLoadedLanguage(String name)
    {
        loadLanguages();
        if (name == null)
            return null;
        for (Iterator<LanguageBean> i = mLanguages.iterator(); i.hasNext(); )
        {
            LanguageBean lang = i.next();
            if (name.equalsIgnoreCase(lang.getName()))
                return lang;
            if (name.equalsIgnoreCase(lang.getAlliegence()))
                return lang;
        }
        return null;
    }

    public static LanguageBean readLanguage(String fname) throws IOException
    {
        Reader fis = new InputStreamReader(new FileInputStream(fname), "utf-8");
        LanguageBean lang = readLanguage(fis);
        fis.close();
        return lang;
    }

    public static LanguageBean readLanguage(Reader rdr) throws IOException
    {
        
        String data = ReaderUtils.readStream(rdr);
        LanguageBean lang = new LanguageBean();
        StringTokenizer st = new StringTokenizer(data, ";");
        lang.setName(st.nextToken().trim());
        lang.setAlliegence(st.nextToken().trim());
        LanguageStatsBean stats = lang.getLanguageStats();
        stats.setWordLength(parseFrequency(st.nextToken()));
        stats.setInitialSyllableTable(parseFrequency(st.nextToken()));
        stats.setFinalSyllableTable(parseFrequency(st.nextToken()));
        String line = st.nextToken();
        stats.setFirstConsonant(parseFrequency(line));
        stats.setFirstConsonantText(parseValues(line));
        line = st.nextToken();
        stats.setLastConsonant(parseFrequency(line));
        stats.setLastConsonantText(parseValues(line));
        line = st.nextToken();
        stats.setVowel(parseFrequency(line));
        stats.setVowelText(parseValues(line));
//        System.out.println(lang.getName());
//        System.out.println(totUp(stats.getWordLength())+", "+totUp(stats.getInitialSyllableTable())+", "+totUp(stats.getFinalSyllableTable())
//                +", "+totUp(stats.getFirstConsonant())+", "+totUp(stats.getLastConsonant())+", "+totUp(stats.getVowel()));
        return lang;
    }
    
    private static int[] parseFrequency(String line)
    {
        StringTokenizer st = new StringTokenizer(line, "[], \t\r\n\"");
        int[] freq = new int[st.countTokens()/2];
        int tot = 0;
        for (int i = 0; i < freq.length; i++)
        {
            st.nextToken();
            freq[i] = IntegerUtils.parseInt(st.nextToken());
            tot += freq[i];
        }
        int last = freq[freq.length - 1];
        tot -= last;
        freq[freq.length - 1] = last - tot;
        return freq;
    }
    
    private static String[] parseValues(String line)
    {
        StringTokenizer st = new StringTokenizer(line, "[], \t\r\n\"");
        String[] vals = new String[st.countTokens()/2];
        for (int i = 0; i < vals.length; i++)
        {
            vals[i] = st.nextToken();
            st.nextToken();
        }
        return vals;
    }
    
    public static String getName(String name, RandBean r)
    {
        if (r == null)
            r = mRND;
        LanguageBean lang = getLoadedLanguage(name);
        if (lang == null)
            lang = getLoadedLanguage("Im");
        return getName(lang, r);
    }
    
}
