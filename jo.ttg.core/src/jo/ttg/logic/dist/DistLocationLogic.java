/*
 * Created on Dec 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.dist;

import java.util.StringTokenizer;

import jo.ttg.beans.dist.DistLocation;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.LocLogic;
import jo.ttg.logic.OrdLogic;
import jo.util.utils.obj.StringUtils;

/**
 * @author jgrant
 *
 * Supported URIs:
 * On surface of a world:
 * sys://[x,y,z]/star/giant/world?orbit=0
 * In orbit around a world:
 * sys://[x,y,z]/star/giant/world?orbit=x
 * where 0 < x <= 100
 * Interplanetary space:
 * sys://[x,y,z]/?s=[x,y,z]
 * Interstellar space:
 * sys://[x,y,z]/?destSys=[x,y,z]&destPath=star/giant/world&timeLeft=h
 * where h is in minutes
 */
public class DistLocationLogic
{
    public static DistLocation fromURI(String sUri) throws TraverseException
    {
        DistLocation ret = new DistLocation();
        setURI(ret, sUri);
        return ret;
    }
    public static void setURI(DistLocation loc, String sUri) throws TraverseException
    {
        String scheme = "";
        String authority = "";
        String path = "";
        String query = "";
        int o = sUri.indexOf("://");
        if (o >= 0)
        {
            scheme = sUri.substring(0, o);
            sUri = sUri.substring(o+3);
        }
        o = sUri.indexOf("/");
        if (o >= 0)
        {
            authority = sUri.substring(0, o);
            sUri = sUri.substring(o+1);
            o = sUri.indexOf("?");
            if (o >= 0)
            {
                path = sUri.substring(0, o);
                query = sUri.substring(o+1);
            }
            else
                path = sUri;
        }
        else
            authority = sUri;
        if (scheme.equals("sys") || scheme.equals("body"))
        {
            loc.setOrds(OrdLogic.parseString(authority));
            if (path.length() > 0)
                loc.setPath(StringUtils.toStringArray(path, "/"));
            if (query.length() > 0)
            {
                StringTokenizer st = new StringTokenizer(query, "&");
                while (st.hasMoreTokens())
                {
                    String tok = st.nextToken();
                    o = tok.indexOf("=");
                    if (o < 0)
                        throw new TraverseException("Invalid query string:"+sUri);
                    String key = tok.substring(0, o);
                    String val = tok.substring(o+1);
                    if (key.equals("s"))
                        loc.setSpace(LocLogic.fromString(val));
                    else if (key.equals("orbit"))
                        loc.setOrbit(Double.parseDouble(val));
                    else if (key.equals("destSys"))
                        loc.setDestOrds(OrdLogic.parseString(val));
                    else if (key.equals("timeLeft"))
                        loc.getDestTimeLeft().setMinutes((int)Double.parseDouble(val));
                    else if (key.equals("destPath"))
                        loc.setDestPath(StringUtils.toStringArray(val, "/"));
                    //else
                    //    throw new TraverseException("Unknown query key ("+key+") "+sUri);
                }
            }
        }
        else
            throw new TraverseException("Scheme not supported:"+sUri);
    }
    
    public static void resetURI(DistLocation loc) throws TraverseException
    {
        StringBuffer uri = new StringBuffer("sys://");
        uri.append(loc.getOrds().toURIString());
        uri.append('/');
        if (isSurface(loc) || isOrbit(loc))
        {
            uri.append(StringUtils.fromStringArray(loc.getPath(), "/"));
            uri.append('?');
            uri.append("orbit=");
            uri.append(loc.getOrbit());
        }
        else if (isInterplanetary(loc))
        {
            uri.append('?');
            uri.append("orbit=");
            uri.append(loc.getSpace().toString());            
        }
        else if (isInterstellar(loc))
        {
            uri.append('?');
            uri.append("destSys=");
            uri.append(loc.getDestOrds().toURIString());            
            uri.append('&');
            uri.append("destPath=");
            uri.append(StringUtils.fromStringArray(loc.getDestPath(), "/"));            
            uri.append("timeLeft=");
            uri.append(String.valueOf(DateLogic.toMinutes(loc.getDestTimeLeft())));            
        }
        else
            throw new TraverseException("Invalid values in DistLocation!");
        loc.setURI(uri.toString());
    }
    
    public static boolean isSurface(DistLocation loc)
    {
        if (loc.getPath().length == 0)
            return false;
        if (loc.getOrbit() > 0)
            return false;
        return true;
    }
    
    public static boolean isOrbit(DistLocation loc)
    {
        if (loc.getPath().length == 0)
            return false;
        if (loc.getOrbit() == 0)
            return false;
        return true;
    }
    
    public static boolean isInterplanetary(DistLocation loc)
    {
        if (loc.getPath().length != 0)
            return false;
        if (LocLogic.getMag(loc.getSpace()) == 0)
            return false;
        return true;
    }
    
    public static boolean isInterstellar(DistLocation loc)
    {
        if (loc.getPath().length != 0)
            return false;
        if (LocLogic.getMag(loc.getSpace()) != 0)
            return false;
        return true;
    }
}
