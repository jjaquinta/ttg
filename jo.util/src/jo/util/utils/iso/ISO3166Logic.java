package jo.util.utils.iso;


import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.io.ResourceUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class ISO3166Logic
{
    private static final Map<String, ISO3166Bean> INDEX_2 = new HashMap<String, ISO3166Bean>();
    private static final Map<String, ISO3166Bean> INDEX_3 = new HashMap<String, ISO3166Bean>();
    private static final Map<String, ISO3166Bean> INDEX_N = new HashMap<String, ISO3166Bean>();
    private static boolean mInitialized;
    
    private static synchronized void init()
    {
        if (mInitialized)
            return;
        InputStream is = ResourceUtils.loadSystemResourceStream("iso3166.xml", ISO3166Logic.class);
        Document doc = XMLUtils.readStream(is);
        for (Node tr : XMLUtils.findNodes(doc, "tbody/tr"))
        {
            Node[] td = XMLUtils.findNodes(tr, "td").toArray(new Node[0]);
            if (td.length < 4)
                continue;
            Node a = XMLUtils.findFirstNode(td[0], "a");
            if (a == null)
                continue;
            Node i = XMLUtils.findFirstNode(td[0], "span/img");
            String countryName = XMLUtils.getText(a);
            String flagURL;
            if (i != null)
                flagURL = XMLUtils.getAttribute(i, "src");
            else
                flagURL = "";
            String iso3166_2 = XMLUtils.getText(XMLUtils.findFirstNode(td[1], "a/tt"));
            String iso3166_3 = XMLUtils.getText(XMLUtils.findFirstNode(td[2], "tt"));
            String iso3166_N = XMLUtils.getText(XMLUtils.findFirstNode(td[3], "tt"));
            ISO3166Bean bean = new ISO3166Bean();
            bean.setCountryName(countryName);
            bean.setFlagURL(flagURL);
            bean.setISO3166_2(iso3166_2);
            bean.setISO3166_3(iso3166_3);
            bean.setISO3166_N(iso3166_N);
            INDEX_2.put(bean.getISO3166_2().toLowerCase(), bean);
            INDEX_3.put(bean.getISO3166_3().toLowerCase(), bean);
            INDEX_N.put(bean.getISO3166_N().toLowerCase(), bean);
        }
        mInitialized = true;
    }
    
    public static ISO3166Bean[] getAll()
    {
        init();
        ISO3166Bean[] beans = INDEX_2.values().toArray(new ISO3166Bean[0]);
        Arrays.sort(beans);
        return beans;
    }
    
    public static ISO3166Bean lookup(String code)
    {
        ISO3166Bean bean = lookup2(code);
        if (bean == null)
        {
            bean = lookup3(code);
            if (bean == null)
                bean = lookupN(code);
        }
        return bean;
    }
    
    public static ISO3166Bean lookup2(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_2.get(code.toLowerCase());
    }
    
    public static ISO3166Bean lookupN(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_N.get(code.toLowerCase());
    }
    
    public static ISO3166Bean lookup3(String code)
    {
        if (code == null)
            return null;
        init();
        return INDEX_3.get(code.toLowerCase());
    }
}
