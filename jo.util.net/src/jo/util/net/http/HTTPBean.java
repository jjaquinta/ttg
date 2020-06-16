/*
 * Created on May 3, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.http;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import jo.util.beans.Bean;

import org.apache.commons.httpclient.HttpClient;

public class HTTPBean extends Bean
{
    public static final int STATE_URL = 0;
    public static final int STATE_CONTENT = 1;
    public static final int STATE_CHILDREN = 2;
    
    private HttpClient          mClient;
    private int                 mState;
    private URL                 mURL;
    private byte[]              mContent;
    private String              mContentType;
    private List<HTTPBean>      mChildren;
    private HTTPBean            mParent;
    private Map<String,String>  mParams;
    private Map<String,String>  mHeaders;
    
    public HTTPBean()
    {
        mChildren = new ArrayList<HTTPBean>();
    }
    
    /**
     * @return the children
     */
    public List<HTTPBean> getChildren()
    {
        return mChildren;
    }
    /**
     * @param children the children to set
     */
    public void setChildren(List<HTTPBean> children)
    {
        mChildren = children;
    }
    /**
     * @return the content
     */
    public byte[] getContent()
    {
        return mContent;
    }
    /**
     * @param content the content to set
     */
    public void setContent(byte[] content)
    {
        mContent = content;
    }
    /**
     * @return the contentType
     */
    public String getContentType()
    {
        return mContentType;
    }
    /**
     * @param contentType the contentType to set
     */
    public void setContentType(String contentType)
    {
        mContentType = contentType;
    }
    /**
     * @return the parent
     */
    public HTTPBean getParent()
    {
        return mParent;
    }
    /**
     * @param parent the parent to set
     */
    public void setParent(HTTPBean parent)
    {
        mParent = parent;
    }
    /**
     * @return the state
     */
    public int getState()
    {
        return mState;
    }
    /**
     * @param state the state to set
     */
    public void setState(int state)
    {
        mState = state;
    }
    /**
     * @return the uRL
     */
    public URL getURL()
    {
        return mURL;
    }
    /**
     * @param url the uRL to set
     */
    public void setURL(URL url)
    {
        mURL = url;
    }

    public Map<String,String> getHeaders()
    {
        return mHeaders;
    }

    public void setHeaders(Map<String,String> headers)
    {
        mHeaders = headers;
    }

    public HttpClient getClient()
    {
        return mClient;
    }

    public void setClient(HttpClient client)
    {
        mClient = client;
    }

    public Map<String,String> getParams()
    {
        return mParams;
    }

    public void setParams(Map<String,String> params)
    {
        mParams = params;
    }
}
