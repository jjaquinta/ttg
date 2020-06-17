package ttg.logic.url.ttg;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.io.ResourceUtils;

public class Handler extends URLStreamHandler
{
	private static final int CACHE_SIZE = 4;
	private static byte[][] mByteCache = new byte[CACHE_SIZE][];
	private static String[] mStringCache = new String[CACHE_SIZE];
	private static int mCachePosition = 0;

    protected URLConnection openConnection(URL u) throws IOException
    {
        return new TTGConnection(u);
    }
    
    class TTGConnection extends URLConnection
    {
    	
		private String	mPath;
		private byte[]	mData;
		private HashMap	mHeaderFields;
		
		public TTGConnection(URL url)
		{
			super(url);
			mPath = url.getPath();
			mHeaderFields = new HashMap();
		}

        public void connect() throws IOException
        {
        	if (mData != null)
        		return;
        	for (int i = 0; i < CACHE_SIZE; i++)
        		if (mPath.equals(mStringCache[i]))
        		{
        			mData = mByteCache[i];
        			break;
        		}
        	if (mData == null)
        	{
				mData = ResourceUtils.loadSystemResourceBinary(mPath);
				mByteCache[mCachePosition] = mData;
				mStringCache[mCachePosition] = mPath;
				mCachePosition = (mCachePosition + 1)%CACHE_SIZE;
        	}
			mHeaderFields.put("content-length", String.valueOf(mData.length));
			String contentType = guessContentTypeFromName(mPath);
			mHeaderFields.put("content-type", contentType);
			if ((contentType != null) && contentType.startsWith("text/"))
			{
				mHeaderFields.put("content-encoding", "iso-8859-1");
			}
        }
        /**
         *
         */

        public OutputStream getOutputStream() throws IOException
        {
        	throw new UnknownServiceException();
        }

        /**
         *
         */

        public InputStream getInputStream() throws IOException
        {
        	connect();
            return new ByteArrayInputStream(mData);
        }
        /**
         *
         */

        public String getHeaderField(int n)
        {
        	ArrayList keys = new ArrayList();
        	keys.addAll(mHeaderFields.keySet());
        	Object key = keys.get(n);
            return getHeaderField((String)key);
        }

        /**
         *
         */

        public String getHeaderField(String name)
        {
            return (String)mHeaderFields.get(name);
        }

        /**
         *
         */

        public Map getHeaderFields()
        {
            return mHeaderFields;
        }

    }
}
