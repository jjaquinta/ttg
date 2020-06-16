package jo.ttg.gen.tm;

import jo.ttg.beans.OrdBean;
import jo.util.net.http.HTTPUtils;

import org.w3c.dom.Document;

public class TMUtils
{
    private static String encode(String txt)
    {
        char[] ch = txt.toCharArray();
        StringBuffer ret = new StringBuffer();
        for (int i = 0; i < ch.length; i++)
            if (ch[i] == ' ')
                ret.append("+");
            else
                ret.append(ch[i]);
        return ret.toString();
    }
    
    public static Document getUniverse()
    {
        String url = "https://www.travellermap.com/Universe.aspx";
        Document uni = HTTPUtils.getURLAsXML(url);
        return uni;
    }

    public static Document getCredits(OrdBean ords)
    {
        long x = ords.getX();
        long y = ords.getY();
        x -= 4*32;
        y -= 1*40;
        String url = "https://www.travellermap.com/Credits.aspx?x="+x+"&y="+y;
        Document credits = HTTPUtils.getURLAsXML(url);
        return credits;
    }
    
    public static String getMSEC(String sectorName)
    {
        System.out.print("[msec://"+sectorName+"...");
        String url = "https://www.travellermap.com/MSEC.aspx?sector="+encode(sectorName);
        String msec = HTTPUtils.getURLAsString(url);
        System.out.println("]");
        return msec;
    }
    
    public static String getSEC(String sectorName)
    {
        System.out.print("[sec://"+sectorName+"...");
        String url = "https://www.travellermap.com/SEC.aspx?sector="+encode(sectorName);
        String sec =  HTTPUtils.getURLAsString(url);
        System.out.println("]");
        return sec;
    }
}
