package jo.util.net.http;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.util.utils.FormatUtils;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;

/**
 * @author Joseph Jaquinta
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class HTTPThread extends Thread
{
    private HTTPBean    mHTTP;
    private boolean     mDone;
    private String      mError;
    private String      mComment;
    private int         mTotalBytes;
    private int         mCurrentBytes;
    private long        mLastActive;
    private File        mFile;

    public HTTPThread(HTTPBean http)
    {
        mDone = false;
        mHTTP = http;
        mError = null;
        setDaemon(true);
        setLastActive(System.currentTimeMillis());
        setName("HTTP Fetch of "+mHTTP.getURL().toString());
    }

    public void run()
    {
        synchronized (this.getClass())
        {
            HttpClient client = mHTTP.getClient();
            if (client == null)
            {
                client = new HttpClient();
                mHTTP.setClient(client);
            }
            URL url = mHTTP.getURL();
            try
            {
                if ((mHTTP.getHeaders() != null) && "POST".equals(mHTTP.getHeaders().get("TYPE")))
                    doPost(client, url);
                else
                    doGet(client, url);
            }
            catch (IOException e)
            {
                mError = "network";
                e.printStackTrace();
            }
            mDone = true;
        }
    }
    
    private void doPost(HttpClient client, URL url) throws IOException
    {
        PostMethod post = new PostMethod(url.toExternalForm());
        setupHeaders(post);
        setupParams(post);
        int ret = client.executeMethod(post);
        if (ret == 302)
        {
            Header locationHeader = post.getResponseHeader("location");
            post.releaseConnection();
            String location = locationHeader.getValue();
            if (!location.startsWith("http://"))
                location = "http://"+url.getHost()+"/" + location;
            post = new PostMethod(location);
            setupParams(post);
            ret = client.executeMethod(post);
        }
        if (ret != 200)
        {
            mError = String.valueOf(ret);
            return;
        }
        postProcess(post);
    }

    private void setupParams(PostMethod post)
    {
        if (mHTTP.getParams() != null)
        {
            List<NameValuePair> list = new ArrayList<NameValuePair>();
            for (String key : mHTTP.getParams().keySet())
            {
                String val = mHTTP.getParams().get(key);
                list.add(new NameValuePair(key, val));
            }
            NameValuePair[] data = new NameValuePair[list.size()];
            list.toArray(data);
            post.setRequestBody(data);
        }
    }
    
    private void doGet(HttpClient client, URL url) throws IOException
    {
        GetMethod get = new GetMethod(url.toExternalForm());
        try
        {
            setupHeaders(get);
            int ret = client.executeMethod(get);
            if (ret != 200)
            {
                mError = String.valueOf(ret);
                return;
            }
            postProcess(get);
        }
        catch (IOException e)
        {
            get.releaseConnection();
            throw e;
        }
    }
    
    private void postProcess(HttpMethod cmd) throws IOException
    {
        setdownHeaders(cmd);
        mHTTP.setContentType(cmd.getResponseHeader("Content-Type").getValue());
        int total = 0;
        Header contentLength = cmd.getResponseHeader("Content-Length");
        if (contentLength != null)
            total = FormatUtils.parseInt(contentLength.getValue());
        setTotalBytes(total);
        setComment("Downloading.");
        InputStream is = cmd.getResponseBodyAsStream();
        byte[] inbuf = new byte[10240];
        ByteArrayOutputStream outbuf = new ByteArrayOutputStream();
        for (int tot = 0;;)
        {
            int len = is.read(inbuf);
            if (len < 0)
                break;
            setLastActive(System.currentTimeMillis());
            outbuf.write(inbuf, 0, len);
            tot += len;
            setCurrentBytes(tot);
            if (isDone())
                break;
        }
        if (total > 0)
            setCurrentBytes(total);
        is.close();
        mHTTP.setContent(outbuf.toByteArray());
        mHTTP.setState(HTTPBean.STATE_CONTENT);
        if ((mFile != null) && (mHTTP.getContent().length > 0))
        {
            FileOutputStream fos = new FileOutputStream(mFile);
            fos.write(mHTTP.getContent());
            fos.close();
        }
        cmd.releaseConnection();
        mError = "";
    }

    private void setupHeaders(HttpMethod cmd)
    {
        if (mHTTP.getParent() != null)
        {
            cmd.setRequestHeader("Referer", mHTTP.getParent().getURL().toExternalForm());
        }
        if (mHTTP.getHeaders() != null)
        {
            for (Iterator<String> i = mHTTP.getHeaders().keySet().iterator(); i.hasNext(); )
            {
                String key = i.next();
                String val = (String)mHTTP.getHeaders().get(key);
                if (!key.equals("TYPE"))
                    cmd.setRequestHeader(key, val);
            }
        }
    }
    
    private void setdownHeaders(HttpMethod cmd)
    {
        Header[] headers = cmd.getResponseHeaders();
        Map<String,String> map = new HashMap<String,String>();
        for (int i = 0; i < headers.length; i++)
            map.put(headers[i].getName(), headers[i].getValue());
        mHTTP.setHeaders(map);
    }
    
    /**
     * Returns the done.
     * @return boolean
     */
    public boolean isDone()
    {
        return mDone;
    }

    /**
     * Returns the error.
     * @return String
     */
    public String getError()
    {
        return mError;
    }

    /**
     * Sets the done.
     * @param done The done to set
     */
    public void setDone(boolean done)
    {
        mDone = done;
    }

    /**
     * Sets the error.
     * @param error The error to set
     */
    public void setError(String error)
    {
        mError = error;
    }

    /**
     * Returns the lastActive.
     * @return long
     */
    public long getLastActive()
    {
        return mLastActive;
    }

    /**
     * Sets the lastActive.
     * @param lastActive The lastActive to set
     */
    public void setLastActive(long lastActive)
    {
        mLastActive = lastActive;
    }

    /**
     * Returns the currentBytes.
     * @return int
     */
    public int getCurrentBytes()
    {
        return mCurrentBytes;
    }

    /**
     * Returns the totalBytes.
     * @return int
     */
    public int getTotalBytes()
    {
        return mTotalBytes;
    }

    /**
     * Sets the currentBytes.
     * @param currentBytes The currentBytes to set
     */
    public void setCurrentBytes(int currentBytes)
    {
        mCurrentBytes = currentBytes;
    }

    /**
     * Sets the totalBytes.
     * @param totalBytes The totalBytes to set
     */
    public void setTotalBytes(int totalBytes)
    {
        mTotalBytes = totalBytes;
    }

    /**
     * Returns the comment.
     * @return String
     */
    public String getComment()
    {
        return mComment;
    }

    /**
     * Sets the comment.
     * @param comment The comment to set
     */
    public void setComment(String comment)
    {
        mComment = comment;
    }

    /**
     * @return the hTTP
     */
    public HTTPBean getHTTP()
    {
        return mHTTP;
    }

    /**
     * @param http the hTTP to set
     */
    public void setHTTP(HTTPBean http)
    {
        mHTTP = http;
    }

    /**
     * @return the file
     */
    public File getFile()
    {
        return mFile;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file)
    {
        mFile = file;
    }

}
