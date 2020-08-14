package jo.ttg.lang.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.simple.JSONObject;

import jo.ttg.lang.data.ILanguage;
import jo.ttg.lang.data.LangTrigram;
import jo.ttg.lang.ui.ILanguageEditor;
import jo.ttg.lang.ui.edit.TrigramEditPanel;
import jo.util.utils.io.ResourceUtils;

public class LangTrigramDriver implements ILanguageDriver
{
    private static final Map<String,String> DEFAULT_SOURCE = new HashMap<>();
    static
    {
        DEFAULT_SOURCE.put("Catalonian", "text/ca.txt");
        DEFAULT_SOURCE.put("German", "text/de.txt");
        DEFAULT_SOURCE.put("English", "text/en_US.txt");
        DEFAULT_SOURCE.put("Spanish", "text/es.txt");
        DEFAULT_SOURCE.put("French", "text/fr.txt");
        DEFAULT_SOURCE.put("Italian", "text/it.txt");
        DEFAULT_SOURCE.put("Japanese", "text/ja.txt");
        DEFAULT_SOURCE.put("Portuguese", "text/pt.txt");
        DEFAULT_SOURCE.put("Russian", "text/ru.txt");
        DEFAULT_SOURCE.put("Swedish", "text/sv.txt");
        DEFAULT_SOURCE.put("Chinese", "text/zh.txt");
    }
    
    private List<ILanguage> mDefaults = null;

    @Override
    public boolean isDriverFor(JSONObject json)
    {
        return LangTrigram.VERSION.equals(json.get("$version"));
    }

    @Override
    public boolean isDriverFor(ILanguage lang)
    {
        return lang instanceof LangTrigram;
    }
    
    @Override
    public ILanguage getLanguage(JSONObject json)
    {
        return new LangTrigram(json);
    }
    
    @Override
    public ILanguageEditor getEditor()
    {
        return new TrigramEditPanel();
    }

    @Override
    public String getWord(ILanguage lang, Random rnd)
    {
        LangTrigram l = (LangTrigram)lang;
        StringBuffer word = new StringBuffer(findTrigram(l, rnd, " "));
        while (!word.toString().endsWith(" "))
        {
            String prefix = word.substring(word.length() - 2);
            String newTrigram = findTrigram(l, rnd, prefix);
            word.append(newTrigram.charAt(2));
        }
        return word.toString().trim();
    }
    
    private String findTrigram(LangTrigram lang, Random rnd, String prefix)
    {
        int total = 0;
        List<String> tris = new ArrayList<>();
        for (String tri : lang.getTrigrams().keySet())
            if (tri.startsWith(prefix))
            {
                tris.add(tri);
                total += lang.getTrigrams().get(tri);
            }
        int roll = rnd.nextInt(total);
        while (tris.size() > 0)
        {
            String tri = tris.get(0);
            tris.remove(0);
            roll -= lang.getTrigrams().get(tri);
            if (roll < 0)
                return tri;
        }
        return "   ";
    }

    @Override
    public List<ILanguage> getDefaults()
    {
        if (mDefaults == null)
        {
            mDefaults = new ArrayList<ILanguage>();
            for (String name : DEFAULT_SOURCE.keySet())
            {
                LangTrigram lang = new LangTrigram();
                lang.setName(name);
                String src = DEFAULT_SOURCE.get(name);
                String code = src;
                int o = code.lastIndexOf('/');
                if (o >= 0)
                    code = code.substring(o + 1);
                o = code.indexOf('.');
                if (o > 0)
                    code = code.substring(0, o);
                lang.setCode(code);
                String text;
                try
                {
                    text = new String(ResourceUtils.loadSystemResourceBinary(src, LangTrigramDriver.class), "utf-8");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    continue;
                }
                if (!code.startsWith("de"))
                    text = text.toLowerCase();
                addText(lang, text);
                lang.setDefault(true);
                mDefaults.add(lang);
            }
        }
        return mDefaults;
    }

    private void addTrigram(LangTrigram lang, String tri)
    {
        if (tri.length() != 3)
            System.out.println("Quack");
        if (lang.getTrigrams().containsKey(tri))
            lang.getTrigrams().put(tri, lang.getTrigrams().get(tri) + 1);
        else
            lang.getTrigrams().put(tri, 1);
    }

    private void addWord(LangTrigram lang, String word)
    {
        if (word.length() == 1)
            addTrigram(lang, " "+word+" ");
        else if (word.length() == 2)
        {
            addTrigram(lang, " "+word);
            addTrigram(lang, word+" ");
        }
        else
        {
            addTrigram(lang, " "+word.substring(0, 2));
            for (int i = 0; i < word.length() - 2; i++)
                addTrigram(lang, word.substring(i, i + 3));
            addTrigram(lang, word.substring(word.length() - 2)+" ");
        }
    }

    private void addText(LangTrigram lang, String text)
    {
        StringBuffer word = new StringBuffer();
        for (char ch : text.toCharArray())
            if (Character.isLetter(ch))
                word.append(ch);
            else
            {
                if (word.length() > 0)
                    addWord(lang, word.toString());
                word.setLength(0);
            }
        if (word.length() > 0)
            addWord(lang, word.toString());
    }
    
    public void appendTrigrams(LangTrigram lang, String text)
    {
        addText(lang, text);
    }
    
    public void replaceTrigrams(LangTrigram lang, String text)
    {
        lang.getTrigrams().clear();
        addText(lang, text);
    }

    @Override
    public ILanguage newLanguage(String name, String code, ILanguage cloneLang)
    {
        LangTrigram lang = new LangTrigram();
        lang.setName(name);
        lang.setCode(code);
        if (cloneLang instanceof LangTrigram)
        {
            LangTrigram cl = (LangTrigram)cloneLang;
            for (String key : cl.getTrigrams().keySet())
                lang.getTrigrams().put(key, cl.getTrigrams().get(key));
        }
        return lang;
    }
    
    @Override
    public String toString()
    {
        return "Trigram";
    }

}