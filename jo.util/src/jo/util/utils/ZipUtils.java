/*
 * Created on Aug 4, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jo.util.utils.io.StreamUtils;

public class ZipUtils
{
    public static void unzip(File dir, InputStream is) throws IOException
    {
        ZipInputStream zis = new ZipInputStream(is);
        for (;;)
        {
            ZipEntry entry = zis.getNextEntry();
            if (entry == null)
                break;
            System.out.println("  "+entry.getName());
            File entryFile = new File(dir, entry.getName());
            if (entry.isDirectory())
                entryFile.mkdir();
            else
            {
                FileOutputStream fos = new FileOutputStream(entryFile);
                StreamUtils.copy(zis, fos);
                fos.close();
            }
        }        
    }
}
