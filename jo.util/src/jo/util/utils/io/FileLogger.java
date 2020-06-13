/*
 * Created on Sep 14, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import jo.util.utils.DebugUtils;

public class FileLogger implements LogEngine
{
    private File    mFile;
    
    public FileLogger(String fname)
    {
        mFile = new File(fname);
    }

    public void log(int severity, String msg, Throwable exception)
    {
        String prefix = "     ";
        switch (severity)
        {
            case DebugUtils.CRITICAL:
                prefix = "CRIT ";
                break;
            case DebugUtils.ERROR:
                prefix = "ERROR";
                break;
            case DebugUtils.WARN:
                prefix = "WARN ";
                break;
            case DebugUtils.INFO:
                prefix = "INFO ";
                break;
            case DebugUtils.TRACE:
                prefix = "TRACE";
                break;
        }
        synchronized (this)
        {
            try
            {
                FileOutputStream fos = new FileOutputStream(mFile, true);
                PrintStream os = new PrintStream(fos);
                if (msg != null)
                    os.println(prefix+" "+msg);
                if (exception != null)
                    exception.printStackTrace(os);
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

}
