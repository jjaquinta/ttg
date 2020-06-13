/*
 * Created on Aug 14, 2005
 *
 */
package jo.util.utils.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtils
{
    public static void writeFile(byte[] data, File f) throws IOException
    {
        FileOutputStream fos = new FileOutputStream(f);
        StreamUtils.writeStream(data, fos);
        fos.close();
    }

    public static void writeFile(String data, File f) throws IOException
    {
        FileWriter fw = new FileWriter(f);
        fw.write(data);
        fw.close();
    }

    public static byte[] readFile(String fname) throws IOException
    {
        FileInputStream fis = new FileInputStream(fname);
        byte[] ret = StreamUtils.readStream(fis);
        fis.close();
        return ret;
    }

    public static String readFileAsString(String fname) throws IOException
    {
        return new String(readFile(fname));
    }

    public static String readFileAsString(String fname, String charset) throws IOException
    {
        return new String(readFile(fname), charset);
    }
    
    public static void copy(File in, File out) throws IOException
    {
        InputStream is = new FileInputStream(in);
        OutputStream os = new FileOutputStream(out);
        StreamUtils.copy(is, os);
        is.close();
        os.close();
    }

    public static boolean isIdentical(File file1, File file2)
    {
        if (file1.length() != file2.length())
            return false;
        boolean ret = true;
        InputStream is1;
        InputStream is2;
        try
        {
            is1 = new BufferedInputStream(new FileInputStream(file1));
            is2 = new BufferedInputStream(new FileInputStream(file2));
            long max = Math.min(1024*16, file1.length());
            while (max-- > 0)
            {
                int ch1 = is1.read();
                int ch2 = is2.read();
                if (ch1 != ch2)
                {
                    ret = false;
                    break;
                }
            }
            is1.close();
            is2.close();
        }
        catch (IOException e)
        {
            ret = false;
        }
        return ret;
    }
    
    public static void rmdir(File f)
    {
        if (f.isFile())
            f.delete();
        else
        {
            if (f.getName().equals(".") || f.getName().equals(".."))
                return;
            File[] children = f.listFiles();
            for (int i = 0; i < children.length; i++)
                rmdir(children[i]);
            f.delete();
        }
    }
}
