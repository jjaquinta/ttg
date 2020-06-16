/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import jo.util.net.data.URLReferenceBean;
import jo.util.net.url.ReferenceHandler;
import jo.util.utils.obj.StringUtils;

public class FileHandler implements ReferenceHandler
{
    public String getPrefix()
    {
        return "file:";
    }
    public URLReferenceBean makeReference(String url)
    {
        URLReferenceBean ret = new URLReferenceBean();
        ret.setURL(url);
        File file = new File(StringUtils.unwebify(url.substring(7)));
        ret.setReference(file);
        return ret;
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
        return ((File)ref.getReference()).exists();
    }

    public boolean isFile(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).isFile();
    }

    public boolean isDirectory(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).isDirectory();
    }

    public long length(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).length();
    }

    public boolean mkdirs(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).mkdirs();
    }

    public InputStream getInputStream(URLReferenceBean ref) throws IOException
    {
        return new FileInputStream(((File)ref.getReference()));
    }

    public OutputStream getOutputStream(URLReferenceBean ref) throws IOException
    {
        return new FileOutputStream(((File)ref.getReference()));
    }

    public URLReferenceBean[] list(URLReferenceBean ref, boolean recursive) throws IOException
    {
        List<URLReferenceBean> list = new ArrayList<URLReferenceBean>();
        doList(ref.getURL(), (File)ref.getReference(), list, recursive);
        if (!recursive)
            list.remove(0); // zap current
        URLReferenceBean[] ret = new URLReferenceBean[list.size()];
        list.toArray(ret);
        return ret;
    }
    
    private void doList(String url, File file, List<URLReferenceBean> list, boolean recursive)
    {
        list.add(makeReference(url, file));
        File[] files = file.listFiles();
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
        return ((File)ref.getReference()).canWrite();
    }
    
    public boolean canRead(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).canWrite();
    }
    
    public long lastModified(URLReferenceBean ref)
    {
        return ((File)ref.getReference()).lastModified();
    }
}
