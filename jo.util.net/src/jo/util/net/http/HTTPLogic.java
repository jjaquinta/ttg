/*
 * Created on May 3, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.http;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.StateMachine;

public class HTTPLogic
{
    public static HTTPBean makeHTTP(String url) throws MalformedURLException
    {
        return makeHTTP(null, url);
    }
    
    public static HTTPBean makeHTTP(HTTPBean parent, String url) throws MalformedURLException
    {
        HTTPBean ret = new HTTPBean();
        ret.setURL(new URL(url));
        ret.setParent(parent);
        ret.setState(HTTPBean.STATE_URL);
        if (parent != null)
            ret.setClient(parent.getClient());
        return ret;
    }
    
    public static void makePOST(HTTPBean http, String params)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        StringTokenizer st = new StringTokenizer(params, "&");
        while (st.hasMoreTokens())
        {
            String kv = st.nextToken();
            int o = kv.indexOf("=");
            map.put(kv.substring(0, o), kv.substring(o+1));
        }
        makePOST(http, map);
    }
    
    public static void makePOST(HTTPBean http, Map<String,String> params)
    {
        HashMap<String,String> headers = new HashMap<String,String>();
        headers.put("TYPE", "POST");
        headers.put("Content-type", "application/x-www-form-urlencoded");
        http.setHeaders(headers);
        http.setParams(params);
    }
    
    public static void makeGET(HTTPBean http, Map<String,String> params) throws MalformedURLException
    {
        String url = http.getURL().toExternalForm();
        boolean first = true;
        for (Iterator<String> i = params.keySet().iterator(); i.hasNext(); )
        {
            String key = i.next();
            String val = params.get(key);
            if (first)
            {
                url += "?";
                first = false;
            }
            else
                url += "&";
            url += encode(key);
            url += "=";
            url += encode(val);
        }
        http.setURL(new URL(url));
    }
    
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
    
    public static HTTPThread getContent(HTTPBean http) throws IOException
    {
        return getContent(http, 0);
    }
    
    public static HTTPThread getContent(HTTPBean http, long timeout) throws IOException
    {
        HTTPThread thread = new HTTPThread(http);
        if (timeout == 0)
        {   // no timeout
            thread.run();
        }
        else if (timeout < 0)
        {   // caller manages timeout
            thread.start();
        }
        else
        {   // wait up to timeout
            thread.start();
            long end = System.currentTimeMillis() + timeout;
            while (!thread.isDone())
            {
                if (System.currentTimeMillis() > end)
                    break;
                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                }
            }
        }
        return thread;
    }
    
    private static String[] anchorProcessor = 
    {
        /* 0 */ "<:1",
        /* 1 */ "Aa:2,*:0",
        /* 2 */ "Hh:3,>:0",
        /* 3 */ "Rr:4,*:0",
        /* 4 */ "Ee:5,*:0",
        /* 5 */ "Ff:6,*:0",
        /* 6 */ "=:7, :6,*:0",
        /* 7 */ "\":8,':9,*:+10",
        /* 8 */ "\":$0,*:+",
        /* 9 */ "\':$0,*:+",
        /*10 */ " >:$0,*:+",
    };
    
    private static String[] imageProcessor = 
    {
        /* 0 */ "<:1",
        /* 1 */ "Ii:2,Ee:12,*:0",
        /* 2 */ "Mm:3,*:0",
        /* 3 */ "Gg:4,*:0",
        /* 4 */ "Ss:5,>:0",
        /* 5 */ "Rr:6,*:0",
        /* 6 */ "Cc:7,*:0",
        /* 7 */ "=:8, :7,*:0",
        /* 8 */ "\":9,':10,*:+11",
        /* 9 */ "\":$0,*:+",
        /*10 */ "\':$0,*:+",
        /*11 */ " >:$0,*:+",
        /*12 */ "Mm:13,*:0",
        /*13 */ "Bb:14,*:0",
        /*14 */ "Ee:15,*:0",
        /*15 */ "Dd:16,*:0",
        /*16 */ "Ss:17,>:0",
        /*17 */ "Rr:18,>:0,*:16",
        /*18 */ "Cc:19,>:0,*:16",
        /*19 */ "=:20, :19,*:0",
        /*20 */ "\":21,':22,*:+23",
        /*21 */ "\":$0,*:+",
        /*22 */ "\':$0,*:+",
        /*23 */ " >:$0,*:+",
        
    };
    public static void getLinks(HTTPBean http)
    {
        http.setState(HTTPBean.STATE_CHILDREN);
        if (!http.getContentType().startsWith("text/html"))
            return;
        process(http, imageProcessor);
        process(http, anchorProcessor);
    }
    
    private static void process(HTTPBean http, String[] instructions)
    {
        StateMachine sm = new StateMachine(instructions);
        try
        {
            sm.process(new InputStreamReader(new ByteArrayInputStream(http.getContent())));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        List<String> links = sm.getResult();
        for (String link : links)
        {
            if (link.startsWith("javascript:"))
                continue;
            if (link.startsWith("mailto:"))
                continue;
            try
            {
                URL url = new URL(http.getURL(), link);
                if (!alreadyThere(http.getChildren(), url))
                {
                    HTTPBean child = makeHTTP(http, url.toString());
                    http.getChildren().add(child);
                }
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    private static boolean alreadyThere(List<HTTPBean> list, URL url)
    {
        String externalForm = url.toExternalForm();
        for (HTTPBean http : list)
        {
            if (http.getURL().toExternalForm().equals(externalForm))
                return true;
        }
        return false;
    }
}
