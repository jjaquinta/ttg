/*
 * Created on Sep 25, 2005
 *
 */
package jo.util.utils.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.StringTokenizer;

public class ResourceUtils
{
    public static String loadSystemResourceString(String path) throws IOException
    {
        return new String(loadSystemResourceBinary(path));
    }
    
    public static String loadSystemResourceString(String path, Class<?>  source) throws IOException
    {
        return new String(loadSystemResourceBinary(path, source));
    }
    
    public static byte[] loadSystemResourceBinary(String path) throws IOException
    {
        return loadSystemResourceBinary(path, ResourceUtils.class);
    }
    public static byte[] loadSystemResourceBinary(String path, Class<?>  source) throws IOException
    {
        InputStream is = loadSystemResourceStream(path, source);
        if (is == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (;;)
        {
            int ch = is.read();
            if (ch < 0)
                break;
            baos.write(ch);
        }
        is.close();
        return baos.toByteArray();
    }
    
    public static InputStream loadSystemResourceStream(String path)
    {
        return loadSystemResourceStream(path, ResourceUtils.class);
    }

    private static String resolveRelative(String path, Class<?> source)
    {
        StringTokenizer st = new StringTokenizer(source.getName(), ".");
        int toks = st.countTokens();
        StringBuffer newPath = new StringBuffer();
        for (int i = 0; i < toks - 1; i++)
        {
            newPath.append(st.nextToken());
            newPath.append("/");
        }
        newPath.append(path);
        return newPath.toString();
    }
    
    public static InputStream loadSystemResourceStream(String path, Class<?> source)
    {
        ClassLoader loader = source.getClassLoader();
        InputStream is = loader.getResourceAsStream(path);
        if (is == null)
        {
            String newPath = resolveRelative(path, source);
            is = loader.getResourceAsStream(newPath.toString());
        }
        return is;
    }
    
    public static URL loadSystemResourceURL(String path)
    {
        return loadSystemResourceURL(path, ResourceUtils.class);
    }
    
    public static URL loadSystemResourceURL(String path, Class<?>  source)
    {
        ClassLoader loader = source.getClassLoader();
        URL ret = loader.getResource(path);
        if (ret == null)
        {
            String newPath = resolveRelative(path, source);
            ret = loader.getResource(newPath);
        }
        return ret;
    }


}
