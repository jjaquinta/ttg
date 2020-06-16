package jo.util.net.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.io.FileUtils;
import jo.util.utils.io.StreamUtils;
import jo.util.utils.xml.XMLIOUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;

public class HTTPUtils
{
    private static final long CACHE_TIMEOUT = 7L*24*60*60*1000;
    private static Map<String,HTTPCacheBean> mCache;
    private static File mCacheDir = null;
    
    private static File getCacheDir()
    {
        if (mCacheDir == null)
        {
            File homeDir = new File(System.getProperty("java.home"));
            mCacheDir = new File(homeDir, ".cache");
            mCacheDir.mkdirs();
        }
        return mCacheDir;
    }
    
    public static void setCacheDir(File cacheDir)
    {
        mCacheDir = cacheDir;
    }
    
    private static File getCacheFile()
    {
        File cacheFile = new File(getCacheDir(), "cache.xml");
        return cacheFile;
    }
    
    private static File getCachedFile(HTTPCacheBean cache)
    {
        File cacheFile = new File(getCacheDir(), cache.getFileName());
        return cacheFile;
    }
    
    @SuppressWarnings("unchecked")
    public static synchronized void load()
    {
        if (mCache != null)
            return;
        File cache = getCacheFile();
        if (!cache.exists())
        {
            mCache = new HashMap<String,HTTPCacheBean>();
            return;
        }
        Document doc = XMLUtils.readFile(cache);
        if (doc == null)
            return;
        mCache = (HashMap<String,HTTPCacheBean>)XMLIOUtils.fromXML(doc.getFirstChild(), HTTPUtils.class.getClassLoader());
    }
    
    public static void save() throws IOException
    {
        Document doc = XMLUtils.newDocument();
        XMLIOUtils.toXML(doc, (Object)mCache, new HashMap<Object,Object>());
        String xml = XMLUtils.writeString(doc.getFirstChild());
        FileUtils.writeFile(xml, getCacheFile());
    }
    
    public static InputStream getURLAsStream(String url)
    {
        try
        {
            load();
            HTTPCacheBean cache = (HTTPCacheBean)mCache.get(url);
            if ((cache == null) || (cache.getDate() + CACHE_TIMEOUT < System.currentTimeMillis()))
            {
                URL u = new URL(url);
                URLConnection conn = u.openConnection();
                InputStream is = conn.getInputStream();
                cache = new HTTPCacheBean();
                cache.setURL(url);
                cache.setDate(System.currentTimeMillis());
                cache.setFileName(cache.getDate()+".cache");
                FileOutputStream os = new FileOutputStream(getCachedFile(cache));
                StreamUtils.copy(is, os);
                is.close();
                os.close();
                mCache.put(cache.getURL(), cache);
                save();
            }
            return new FileInputStream(getCachedFile(cache));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static String getURLAsString(String url)
    {
        InputStream is = getURLAsStream(url);
        if (is == null)
            return null;
        try
        {
            return StreamUtils.readStreamAsString(is);
        }
        catch (IOException e)
        {
            return null;
        }
    }
    
    public static Document getURLAsXML(String url)
    {
        InputStream is = getURLAsStream(url);
        if (is == null)
            return null;
        return XMLUtils.readStream(is);
    }
}
