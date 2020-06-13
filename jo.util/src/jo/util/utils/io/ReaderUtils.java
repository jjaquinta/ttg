/*
 * Created on Aug 14, 2005
 *
 */
package jo.util.utils.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class ReaderUtils
{
    public static String readStream(Reader rdr) throws IOException
    {
        StringBuffer ret = new StringBuffer();
        BufferedReader br = new BufferedReader(rdr);
        for (;;)
        {
            int ch = br.read();
            if (ch < 0)
                break;
            ret.append((char)ch);
        }
        return ret.toString();
    }
    
    public static void copy(Reader rdr, Writer wtr) throws IOException
    {
        if (!(rdr instanceof BufferedReader))
            rdr = new BufferedReader(rdr);
        if (!(wtr instanceof BufferedWriter))
            wtr = new BufferedWriter(wtr);
        for (;;)
        {
            int ch = rdr.read();
            if (ch == -1)
                break;
            wtr.write(ch);
        }
        wtr.flush();
    }
    
}
