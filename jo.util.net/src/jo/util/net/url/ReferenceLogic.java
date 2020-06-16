/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.url;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jo.util.net.data.URLReferenceBean;
import jo.util.net.file.FileHandler;
import jo.util.net.ftp.FTPHandler;
import jo.util.utils.io.StreamUtils;

public class ReferenceLogic
{
    private static ReferenceHandler[] HANDLERS =
    {
        new FileHandler(),
        new FTPHandler(),
    };
    
    private static ReferenceHandler getHandler(String url)
    {
        for (int i = 0; i < HANDLERS.length; i++)
            if (url.startsWith(HANDLERS[i].getPrefix()))
                return HANDLERS[i];
        return null;
    }

    public static URLReferenceBean makeReference(String url)
    {
        return getHandler(url).makeReference(url);
    }
    public static boolean exists(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).exists(ref);
    }
    public static boolean isFile(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).isFile(ref);
    }
    public static boolean isDirectory(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).isDirectory(ref);
    }
    public static long length(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).length(ref);
    }
    public static boolean mkdirs(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).mkdirs(ref);
    }
    public static InputStream getInputStream(URLReferenceBean ref) throws IOException
    {
        return getHandler(ref.getURL()).getInputStream(ref);
    }
    public static OutputStream getOutputStream(URLReferenceBean ref) throws IOException
    {
        return getHandler(ref.getURL()).getOutputStream(ref);
    }
    public static URLReferenceBean[] list(URLReferenceBean ref, boolean recursive) throws IOException
    {
        return getHandler(ref.getURL()).list(ref, recursive);
    }
    public static boolean canRead(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).canRead(ref);
    }
    public static boolean canWrite(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).canWrite(ref);
    }
    public static long lastModified(URLReferenceBean ref)
    {
        return getHandler(ref.getURL()).lastModified(ref);
    }
    
    public static void copy(URLReferenceBean from, URLReferenceBean to) throws IOException
    {
        InputStream is = getInputStream(from);
        OutputStream os = getOutputStream(to);
        StreamUtils.copy(is, os);
        is.close();
        os.close();
    }
}
