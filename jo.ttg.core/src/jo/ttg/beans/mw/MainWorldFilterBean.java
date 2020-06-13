package jo.ttg.beans.mw;

import java.util.HashMap;
import java.util.Map;

import jo.util.utils.obj.StringUtils;

public class MainWorldFilterBean
{
    public static final String PORT = "port";
    public static final String SIZE = "size";
    public static final String ATMO = "atmosphere";
    public static final String HYDRO = "hydrosphere";
    public static final String POP = "population";
    public static final String GOV = "government";
    public static final String LAW = "law";
    public static final String TECH = "tech";
    public static final String TRADE = "trade";
    public static final String BASE = "base";
    public static final String ZONE = "zone";
    public static final String ALLIEGENCE = "alliegence";

    public static final String[] FILTERS = {
        PORT,
        SIZE,
        ATMO,
        HYDRO,
        POP,
        GOV,
        LAW,
        TECH,
        TRADE,
        BASE,
        ZONE,
        ALLIEGENCE,
    };

    public static final String EQUAL = "=";
    public static final String NOTEQUAL = "\u2260";
    public static final String LESSTHAN = "<";
    public static final String GREATERTHAN = ">";
    public static final String LESSOREQUAL = "\u2264";
    public static final String GREATEROREQUAL = "\u2265";
    
    public static final String[] RELOPS = {
      EQUAL,
      NOTEQUAL,
      LESSTHAN,
      GREATERTHAN,
      LESSOREQUAL,
      GREATEROREQUAL,
    };
    
    private Map<String,String> mFilters;
    
    public MainWorldFilterBean()
    {
        mFilters = new HashMap<String, String>();
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        for (String key : MainWorldFilterBean.FILTERS)
        {
            String val = getFilter(key);
            if (!StringUtils.isTrivial(val))
            {
                if (sb.length() > 0)
                    sb.append(", ");
                sb.append(key);
                sb.append(' ');
                if (key.equals(PORT))
                {
                    char pval = (char)Integer.parseInt(val.substring(val.length() - 2, val.length()));
                    sb.append(val.substring(0, val.length() - 2));
                    sb.append(pval);
                }
                else
                    sb.append(val);
            }
        }
        return sb.toString();
    }
    
    public void addFilter(String filter, String limit)
    {
        mFilters.put(filter, limit);
    }
    
    public void removeFilter(String filter)
    {
        mFilters.remove(filter);
    }
    
    public boolean isFilter(String filter)
    {
        return mFilters.containsKey(filter);
    }
    
    public String getFilter(String filter)
    {
        return mFilters.get(filter);
    }
}
