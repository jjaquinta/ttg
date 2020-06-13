package jo.util.html;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

public class URLLogic
{
    public static String encode(String txt)
    {
        try
        {
            return URLEncoder.encode(txt, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return txt;
        }
    }
    public static String decode(String txt)
    {
        try
        {
            return URLDecoder.decode(txt, "utf-8");
        }
        catch (UnsupportedEncodingException e)
        {
            return txt;
        }
    }
}
