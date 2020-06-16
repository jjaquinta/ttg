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

public interface ReferenceHandler
{
    public String getPrefix();
    public URLReferenceBean makeReference(String url);
    public boolean exists(URLReferenceBean ref);
    public boolean isFile(URLReferenceBean ref);
    public boolean isDirectory(URLReferenceBean ref);
    public boolean canWrite(URLReferenceBean ref);
    public boolean canRead(URLReferenceBean ref);
    public long lastModified(URLReferenceBean ref);
    public long length(URLReferenceBean ref);
    public boolean mkdirs(URLReferenceBean ref);
    public InputStream getInputStream(URLReferenceBean ref) throws IOException;
    public OutputStream getOutputStream(URLReferenceBean ref) throws IOException;
    public URLReferenceBean[] list(URLReferenceBean ref, boolean recursive) throws IOException;
}
