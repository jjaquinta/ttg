/*
 * Created on Jan 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.utils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class URIUtils
{

    public static String extractName(String uri)
    {
        int o = uri.indexOf("?");
        if (o >= 0)
            uri = uri.substring(0, o);
        o = uri.lastIndexOf("/");
        if (o >= 0)
            uri = uri.substring(o+1);
        return uri;
    }

    public static String extractOrds(String uri)
    {
        int o = uri.indexOf("//");
        if (o >= 0)
            uri = uri.substring(o + 2);
        o = uri.indexOf("/");
        if (o >= 0)
            uri = uri.substring(0, o);
        return uri;
    }
    
    public static boolean isSameBaseURI(String uri1, String uri2)
    {
        int o = uri1.indexOf('?');
        if (o >= 0)
            uri1 = uri1.substring(0, o);
        o = uri2.indexOf('?');
        if (o >= 0)
            uri2 = uri2.substring(0, o);
        return uri1.equals(uri2);
    }
}
