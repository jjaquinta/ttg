/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic;

import java.util.Iterator;
import java.util.StringTokenizer;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.sys.BodyBean;

public class LocationURILogic
{
    
    public static LocationURI fromURI(String uri)
    {
        LocationURI l = new LocationURI();
        setURI(l, uri);
        return l;
    }
    
    public static void setURI(LocationURI l, String uri)
    {
        l.setType(-1);
        l.getParams().clear();
        if (uri == null)
            return;
        int o = uri.indexOf("://");
        if (o < 0)
            return;
        String type = uri.substring(0, o);
        uri = uri.substring(o+3);
        String ords = "";
        String path = "";
        o = uri.indexOf("/");
        if (o < 0)
        {
            if (uri.length() > 0)
                ords = uri;
        }
        else
        {
            ords = uri.substring(0, o);
            path = uri.substring(o+1);
        }
        o = path.indexOf("?");
        if (o >= 0)
        {
            StringTokenizer p = new StringTokenizer(path.substring(o+1), "&");
            path = path.substring(0, o);
            while (p.hasMoreTokens())
            {
                String kv = p.nextToken();
                o = kv.indexOf("=");
                if (o < 0)
                    l.getParams().put(kv, "true");
                else
                    l.getParams().put(kv.substring(0, o), kv.substring(o+1));
            }
        }
        if (type.equals("uni"))
            l.setType(LocationURI.UNIVERSE);
        else if (type.equals("sec"))
        {
            l.setType(LocationURI.SECTOR);
            l.setOrds(OrdLogic.parseString(ords));
        }
        else if (type.equals("sub"))
        {
            l.setType(LocationURI.SUBSECTOR);
            l.setOrds(OrdLogic.parseString(ords));
        }
        else if (type.equals("mw"))
        {
            l.setType(LocationURI.MAINWORLD);
            l.setOrds(OrdLogic.parseString(ords));
        }
        else if (type.equals("sys"))
        {
            l.setType(LocationURI.SYSTEM);
            l.setOrds(OrdLogic.parseString(ords));
            l.setPath(path);
        }
        else if (type.equals("body"))
        {
            l.setType(LocationURI.BODY);
            l.setOrds(OrdLogic.parseString(ords));
            l.setPath(path);
        }
    }
    
    public static String getURI(LocationURI l)
    {
        StringBuffer ret = new StringBuffer();
        switch (l.getType())
        {
            case LocationURI.UNIVERSE:
                ret.append("uni://");
                break;
            case LocationURI.SECTOR:
                ret.append("sec://");
                ret.append(l.getOrds().toURIString());
                break;
            case LocationURI.SUBSECTOR:
                ret.append("sub://");
                ret.append(l.getOrds().toURIString());
                break;
            case LocationURI.MAINWORLD:
                ret.append("mw://");
                ret.append(l.getOrds().toURIString());
                break;
            case LocationURI.SYSTEM:
                ret.append("sys://");
                ret.append(l.getOrds().toURIString());
                ret.append('/');
                break;
            case LocationURI.BODY:
                ret.append(BodyBean.SCHEME);
                ret.append(l.getOrds().toURIString());
                ret.append('/');
                ret.append(l.getPath());
                break;
        }
        boolean first = true;
        for (Iterator<?> i = l.getParams().keySet().iterator(); i.hasNext(); )
        {
            if (first)
            {
                ret.append('?');
                first = false;
            }
            else
                ret.append('&');
            String key = (String)i.next();
            String val = l.getParams().getProperty(key);
            ret.append(key);
            ret.append('=');
            ret.append(val);
        }
        return ret.toString();
    }

    public static String setURIParam(String uri, String key, String val)
    {
    	LocationURI u = fromURI(uri);
    	u.setParam(key, val);
    	return getURI(u);
    }
}
