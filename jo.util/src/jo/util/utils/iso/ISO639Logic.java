package jo.util.utils.iso;


import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.io.ResourceUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ISO639Logic
{
    private static final Map<String, ISO639Bean> INDEX_1 = new HashMap<String, ISO639Bean>();
    private static final Map<String, ISO639Bean> INDEX_2b = new HashMap<String, ISO639Bean>();
    private static final Map<String, ISO639Bean> INDEX_2t = new HashMap<String, ISO639Bean>();
    private static final Map<String, ISO639Bean> INDEX_3 = new HashMap<String, ISO639Bean>();
    private static boolean mInitialized;
    
    private static synchronized void init()
    {
        if (mInitialized)
            return;
        InputStream is = ResourceUtils.loadSystemResourceStream("iso639.xml", ISO639Logic.class);
        Document doc = XMLUtils.readStream(is);
        for (Node tr : XMLUtils.findNodes(doc, "tbody/tr"))
        {
            Node[] td = XMLUtils.findNodes(tr, "td").toArray(new Node[0]);
            if (td.length < 8)
                continue;
            String family = XMLUtils.getText(XMLUtils.findFirstNode(td[1], "a"));
            String languageName = XMLUtils.getText(XMLUtils.findFirstNode(td[2], "a"));
            String nativeName = XMLUtils.getText(td[3]);
            String iso639_1 = XMLUtils.getText(td[4]);
            String iso639_2t = XMLUtils.getText(td[5]);
            String iso639_2b = XMLUtils.getText(td[6]);
            String iso639_3 = XMLUtils.getText(td[7]);
            ISO639Bean bean = new ISO639Bean();
            bean.setFamily(family);
            bean.setLanguageName(languageName);
            bean.setNativeName(nativeName);
            bean.setISO639_1(iso639_1);
            bean.setISO639_2b(iso639_2b);
            bean.setISO639_2t(iso639_2t);
            bean.setISO639_3(iso639_3);
            INDEX_1.put(bean.getISO639_1().toLowerCase(), bean);
            INDEX_2b.put(bean.getISO639_2b().toLowerCase(), bean);
            INDEX_2t.put(bean.getISO639_2t().toLowerCase(), bean);
            INDEX_3.put(bean.getISO639_3().toLowerCase(), bean);
        }
        mInitialized = true;
    }
    
    public static ISO639Bean[] getAll()
    {
        init();
        ISO639Bean[] beans = INDEX_1.values().toArray(new ISO639Bean[0]);
        Arrays.sort(beans);
        return beans;
    }
    
    public static ISO639Bean lookup(String code)
    {
        ISO639Bean bean = lookup1(code);
        if (bean == null)
        {
            bean = lookup2b(code);
            if (bean == null)
            {
                bean = lookup2t(code);
                if (bean == null)
                    bean = lookup3(code);
            }
        }
        return bean;
    }
    
    public static ISO639Bean lookup1(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_1.get(code.toLowerCase());
    }
    
    public static ISO639Bean lookup2t(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_2t.get(code.toLowerCase());
    }
    
    public static ISO639Bean lookup2b(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_2b.get(code.toLowerCase());
    }
    
    public static ISO639Bean lookup3(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_3.get(code.toLowerCase());
    }
}
