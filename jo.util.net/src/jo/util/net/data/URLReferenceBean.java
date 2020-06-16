/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.data;

import jo.util.beans.Bean;

public class URLReferenceBean extends Bean
{
    private String  mURL;
    private Object  mReference;
    
    /**
     * @return Returns the reference.
     */
    public Object getReference()
    {
        return mReference;
    }
    /**
     * @param reference The reference to set.
     */
    public void setReference(Object reference)
    {
        mReference = reference;
    }
    /**
     * @return Returns the uRL.
     */
    public String getURL()
    {
        return mURL;
    }
    /**
     * @param url The uRL to set.
     */
    public void setURL(String url)
    {
        mURL = url;
    }
}
