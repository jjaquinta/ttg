package jo.ttg.logic.mw;

import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.MainWorldFilterBean;
import jo.ttg.beans.mw.UPPDigitBean;
import jo.util.utils.obj.StringUtils;

public class MainWorldFilterLogic
{
    public static void filter(List<MainWorldBean> mws, MainWorldFilterBean filter)
    {
        for (Iterator<MainWorldBean> i = mws.iterator(); i.hasNext(); )
        {
            MainWorldBean mw = i.next();
            if (!isFiltered(mw, filter))
                i.remove();
        }
    }
    
    public static boolean isFiltered(MainWorldBean mw, MainWorldFilterBean filter)
    {
        if (filter.isFilter(MainWorldFilterBean.PORT))
            if (!isFilter(mw.getPopulatedStats().getUPP().getPort(), filter.getFilter(MainWorldFilterBean.PORT)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.SIZE))
            if (!isFilter(mw.getPopulatedStats().getUPP().getSize(), filter.getFilter(MainWorldFilterBean.SIZE)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.ATMO))
            if (!isFilter(mw.getPopulatedStats().getUPP().getAtmos(), filter.getFilter(MainWorldFilterBean.ATMO)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.HYDRO))
            if (!isFilter(mw.getPopulatedStats().getUPP().getHydro(), filter.getFilter(MainWorldFilterBean.HYDRO)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.POP))
            if (!isFilter(mw.getPopulatedStats().getUPP().getPop(), filter.getFilter(MainWorldFilterBean.POP)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.GOV))
            if (!isFilter(mw.getPopulatedStats().getUPP().getGov(), filter.getFilter(MainWorldFilterBean.GOV)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.LAW))
            if (!isFilter(mw.getPopulatedStats().getUPP().getLaw(), filter.getFilter(MainWorldFilterBean.LAW)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.TECH))
            if (!isFilter(mw.getPopulatedStats().getUPP().getTech(), filter.getFilter(MainWorldFilterBean.TECH)))
                return false;
        if (filter.isFilter(MainWorldFilterBean.BASE))
            if (!isFilter(mw.getPopulatedStats().getBasesDesc(), filter.getFilter(MainWorldFilterBean.BASE), 1))
                return false;
        if (filter.isFilter(MainWorldFilterBean.ZONE))
            if (!isFilter(String.valueOf(mw.getPopulatedStats().getTravelZone()), filter.getFilter(MainWorldFilterBean.ZONE), 1))
                return false;
        if (filter.isFilter(MainWorldFilterBean.TRADE))
        {
            String tc = UPPLogic.getTradeCodesDesc(mw.getPopulatedStats().getUPP());
            if (!isFilter(tc, filter.getFilter(MainWorldFilterBean.TRADE), 2))
                return false;
        }
        return true;
    }

    private static boolean isFilter(UPPDigitBean upp, String filter)
    {
        int v = upp.getValue();
        Object[] params = splitRelop(filter);
        String relop = MainWorldFilterBean.RELOPS[(Integer)params[0]];
        int m = (Integer)params[1];
        if (MainWorldFilterBean.EQUAL.equals(relop))
            return v == m;
        else if (MainWorldFilterBean.GREATERTHAN.equals(relop))
            return v > toValue(filter.charAt(1));
        else if (MainWorldFilterBean.LESSTHAN.equals(relop))
            return v < toValue(filter.charAt(1));
        else if (MainWorldFilterBean.GREATEROREQUAL.equals(relop))
            return v >= toValue(filter.charAt(1));
        else if (MainWorldFilterBean.LESSOREQUAL.equals(relop))
            return v <= toValue(filter.charAt(1));
        else if (MainWorldFilterBean.NOTEQUAL.equals(relop))
            return v != toValue(filter.charAt(1));
        return false;
    }

    private static boolean isFilter(String v, String filter, int stride)
    {
        for (int i = 0; i < v.length(); i += stride)
            if (v.substring(i, i + stride).equalsIgnoreCase(filter))
                return true;
        return false;
    }
    
    private static int toValue(char c)
    {
        if ((c >= '0') && (c <= '9'))
            return c - '0';
        if ((c >= 'A') && (c <= 'Z'))
            return c - 'A' + 10;
        if ((c >= 'a') && (c <= 'z'))
            return c - 'a' + 10;
        return UPPDigitBean.UPP_UNKNOWN;
    }
    
    public static String toString(MainWorldFilterBean filter)
    {
        StringBuffer sb = new StringBuffer();
        for (String key : MainWorldFilterBean.FILTERS)
        {
            String val = filter.getFilter(key);
            if (!StringUtils.isTrivial(val))
            {
                if (sb.length() > 0)
                    sb.append(',');
                sb.append(key);
                sb.append(';');
                sb.append(val);
            }
        }
        return sb.toString();
    }
    
    public static MainWorldFilterBean fromString(String txt)
    {
        MainWorldFilterBean filter = new MainWorldFilterBean();
        for (StringTokenizer st = new StringTokenizer(txt, ","); st.hasMoreTokens(); )
        {
            String kv = st.nextToken();
            int o = kv.indexOf(';');
            String key = kv.substring(0, o);
            String val = kv.substring(o + 1);
            filter.addFilter(key, val);
        }
        return filter;
    }
    
    public static Object[] splitRelop(String v)
    {
        if (v == null)
            return null;
        for (int i = 0; i < MainWorldFilterBean.RELOPS.length; i++)
            if (v.startsWith(MainWorldFilterBean.RELOPS[i]))
                return new Object[] { i, Integer.parseInt(v.substring(MainWorldFilterBean.RELOPS[i].length())) };
        return new Object[] { -1, Integer.parseInt(v) };
    }
}
