package jo.util.html;

import java.io.*;
import java.net.*;
import java.util.Collection;

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
    OutputStream mOs;
    boolean mDone;
    String   mError;
    String   mPath;
    String	mReferer;
    String  mComment;
    int     mTotalBytes;
    int     mCurrentBytes;
    long    mLastActive;
    String	mType;
    Collection<String>	mDupLengths;

    public HTTPThread(OutputStream os, String path, String referer, Collection<String> dupLengths)
    {
        mOs = os;
        mDone = false;
        mPath = path;
        mReferer = referer;
        mError = null;
        mDupLengths = dupLengths;
        setDaemon(true);
        setLastActive(System.currentTimeMillis());
    }

    public void run()
    {
        URL url;
        try
        {
            url = new URL(mPath);
        }
        catch (MalformedURLException e)
        {
            mError = "bad url";
            mDone = true;
            return;
        }
        try
        {
            URLConnection urlConn = url.openConnection();
            if (mReferer != null)
            	urlConn.setRequestProperty("Referer", mReferer);
            urlConn.connect();
            mType = urlConn.getContentType();
            int total = urlConn.getContentLength();
            setTotalBytes(total);
            if (mDupLengths != null)
            	if (mDupLengths.contains(String.valueOf(total)))
            	{
		            setComment("Duplicate.");
		            mError = "duplicate";
     				mOs.close();
		            mDone = true;
		            return;
                }
            InputStream is = new BufferedInputStream(urlConn.getInputStream());
            setComment("Downloading.");
            byte[] inbuf = new byte[10240];
            for (int tot = 0;;)
            {
                int len = is.read(inbuf);
                if (len < 0)
                    break;
                setLastActive(System.currentTimeMillis());
                mOs.write(inbuf, 0, len);
                tot += len;
                setCurrentBytes(tot);
                if (isDone())
                    break;
            }
            setCurrentBytes(total);
            is.close();
            mOs.close();
        }
        catch (IOException e)
        {
            mError = "network";
            //e.printStackTrace();
        }
        mDone = true;
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
	 * @return
	 */
	public String getType()
	{
		return mType;
	}

	/**
	 * @param string
	 */
	public void setType(String string)
	{
		mType = string;
	}

}
