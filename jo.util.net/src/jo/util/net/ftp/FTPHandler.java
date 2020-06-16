/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jo.util.net.data.URLReferenceBean;
import jo.util.net.url.ReferenceHandler;

import org.apache.commons.net.ftp.FTPFile;

public class FTPHandler implements ReferenceHandler
{
    public String getPrefix()
    {
        return "ftp:";
    }

    public URLReferenceBean makeReference(String url)
    {
        URLReferenceBean ref = new URLReferenceBean();
        ref.setURL(url);
        int o = url.lastIndexOf('/');
        String parent = url.substring(0, o);
        String file = url.substring(o + 1);
        try
        {
            FTPFile[] list = FTPLogic.listFilesFTP(parent);
            for (int i = 0; i < list.length; i++)
                if (list[i].getName().equalsIgnoreCase(file))
                {
                    ref.setReference(list[i]);
                    break;
                }
        }
        catch (IOException e)
        {
            //e.printStackTrace();
        }
        return ref;
    }

    public URLReferenceBean makeReference(String url, Object ref)
    {
        URLReferenceBean ret = new URLReferenceBean();
        ret.setURL(url);
        ret.setReference(ref);
        return ret;
    }

    public boolean exists(URLReferenceBean ref)
    {
        return ref.getReference() != null;
    }

    public boolean isFile(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).isFile();
    }

    public boolean isDirectory(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).isDirectory();
    }

    public long length(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).getSize();
    }

    public boolean mkdirs(URLReferenceBean ref)
    {
        try
        {
            return FTPLogic.mkdirsFTP(ref.getURL());
        }
        catch (IOException e)
        {
            return false;
        }
    }

    public InputStream getInputStream(URLReferenceBean ref) throws IOException
    {
        return FTPLogic.getFromFTP(ref.getURL());
    }

    public OutputStream getOutputStream(URLReferenceBean ref)
            throws IOException
    {
        return FTPLogic.sendToFTP(ref.getURL());
    }

    public URLReferenceBean[] list(URLReferenceBean ref, boolean recursive) throws IOException
    {
        List<URLReferenceBean> list = new ArrayList<URLReferenceBean>();
        doList(ref.getURL(), (FTPFile)ref.getReference(), list, recursive);
        if (!recursive)
            list.remove(0); // zap current
        URLReferenceBean[] ret = new URLReferenceBean[list.size()];
        list.toArray(ret);
        return ret;
    }
    
    private void doList(String url, FTPFile file, List<URLReferenceBean> list, boolean recursive) throws IOException
    {
        list.add(makeReference(url, file));
        FTPFile[] files = FTPLogic.listFilesFTP(url);
        for (int i = 0; i < files.length; i++)
            if (files[i].getName().equals("."))
                continue;
            else if (files[i].getName().equals(".."))
                continue;
            else if (files[i].isFile() || !recursive)
                list.add(makeReference(url + "/" + files[i].getName(), files[i]));
            else if (files[i].isDirectory() && recursive)
                doList(url + "/" + files[i].getName(), files[i], list, recursive);
    }

    public boolean canWrite(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).hasPermission(FTPFile.USER_ACCESS, FTPFile.WRITE_PERMISSION);
    }
    
    public boolean canRead(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).hasPermission(FTPFile.USER_ACCESS, FTPFile.READ_PERMISSION);
    }
    
    public long lastModified(URLReferenceBean ref)
    {
        return ((FTPFile)ref.getReference()).getTimestamp().getTimeInMillis();
    }
}
