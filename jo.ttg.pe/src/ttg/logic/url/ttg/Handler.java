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
import java.util.List;
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
		private Map<String,List<String>>	mHeaderFields;
		
		public TTGConnection(URL url)
		{
			super(url);
			mPath = url.getPath();
			mHeaderFields = new HashMap<>();
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
				if (mData == null)
				    System.err.println("Cannot find resource at path "+mPath);
				mByteCache[mCachePosition] = mData;
				mStringCache[mCachePosition] = mPath;
				mCachePosition = (mCachePosition + 1)%CACHE_SIZE;
        	}
        	List<String> contentLengths = new ArrayList<>();
        	contentLengths.add(String.valueOf(mData.length));
			mHeaderFields.put("content-length", contentLengths);
			String contentType = guessContentTypeFromName(mPath);
            List<String> contentTypes = new ArrayList<>();
            contentTypes.add(contentType);
			mHeaderFields.put("content-type", contentTypes);
			if ((contentType != null) && contentType.startsWith("text/"))
			{
	            List<String> contentEncodings = new ArrayList<>();
	            contentEncodings.add("iso-8859-1");
				mHeaderFields.put("content-encoding", contentEncodings);
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
        	String[] keys = mHeaderFields.keySet().toArray(new String[0]);
            return getHeaderField(keys[n]);
        }

        /**
         *
         */

        public String getHeaderField(String name)
        {
            if (mHeaderFields.containsKey(name))
                return mHeaderFields.get(name).get(0);
            return null;
        }

        /**
         *
         */

        public Map<String,List<String>> getHeaderFields()
        {
            return mHeaderFields;
        }

    }
}
