package jo.util.html;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class HTMLLogic
{
    static final long TIMEOUT = 30*1000; // 30 seconds
    
    public static String getHTML(String path, String referer)
    {
    	if (path == null)
    		return null;
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        HTTPThread getThread = new HTTPThread(os, path, referer, null);
        getThread.start();
        long killTime = System.currentTimeMillis() + TIMEOUT;
        while (!getThread.isDone())
        {
            if (System.currentTimeMillis() > killTime)
            {
                getThread.setDone(true);
                getThread.setError("timeout");
                break;
            }
        }
        if (getThread.isDone() && (getThread.getError() == null))
        {
            String html = new String(os.toByteArray());
            return html;
        }
        return null;
    }
    
    public static String extract(String txt, String prefix, String suffix)
    {
    	if (txt == null)
    		return null;
        int o = txt.indexOf(prefix);
        if (o < 0)
            return null;
        String ret = txt.substring(o+prefix.length());
        if (suffix != null)
        {
	        o = ret.indexOf(suffix);
	        if (o < 0)
	            return null;
	        ret = ret.substring(0, o);
        }
        return ret;
    }
    
    public static Collection<String> extractArray(String txt, String prefix, String suffix)
    {
        List<String> ret = new ArrayList<String>();
        for (;;)
        {
            int o1 = txt.indexOf(prefix);
            if (o1 < 0)
                break;
            txt = txt.substring(o1 += prefix.length());
            int o2 = txt.indexOf(suffix);
            if (o2 < 0)
                break;
            String str = txt.substring(0, o2);
            txt = txt.substring(o2 + suffix.length());
            ret.add(str);
        }
        return ret;
    }
    
    public static String downloadFile(String path, String referer, File file, Collection<String> dupLengths)
    {
        File parent = file.getParentFile();
        parent.mkdirs();
        OutputStream os;
        try
        {
            os = new BufferedOutputStream(new FileOutputStream(file));
        }
        catch (FileNotFoundException e)
        {
            return "no-file";
        }
        HTTPThread getThread = new HTTPThread(os, path, referer, dupLengths);
        getThread.start();
        while (!getThread.isDone())
        {
            if (System.currentTimeMillis() - getThread.getLastActive() > TIMEOUT)
            {
                getThread.setDone(true);
                getThread.setError("timeout");
                try { os.close(); } catch (IOException ee) { }
                file.delete();
                break;
            }
            try
            {
                Thread.sleep(1000);
            }
            catch (InterruptedException e)
            {
            }
        }
        try { os.close(); } catch (IOException ee) { }
        if (getThread.isDone() && (getThread.getError() == null) && !isTextFile(file))
        {
            return null;
        }
        file.delete();
        return getThread.getError();
    }

    static boolean isTextFile(File f)
    {
        if (f.length() < 32)
            return true;
        try
        {
            byte[] inbuf = new byte[1024];
            FileInputStream fis = new FileInputStream(f);
            fis.read(inbuf, 0, inbuf.length);
            fis.close();
            String s = new String(inbuf);
            if (s.toLowerCase().indexOf("<html") >= 0)
                return true;
        }
        catch (IOException e)
        {
            return true;
        }
        return false;
    }
}
