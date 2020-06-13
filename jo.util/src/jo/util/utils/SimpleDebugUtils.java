package jo.util.utils;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;

public class SimpleDebugUtils
{
    public static final int CRITICAL = 1;
    public static final int ERROR = 2;
    public static final int WARN = 3;
    public static final int INFO = 4;
    public static final int DEBUG = 5;
    public static final int TRACE = 5;

    public static int       mDebugLevel = INFO;
    public static String    mDebugFile;
    
    public static synchronized void log(int severity, String msg, Throwable exception)
    {
        if ((mDebugFile == null) || (severity > mDebugLevel))
            return;
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
        try
        {
            FileOutputStream fos = new FileOutputStream(mDebugFile, true);
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
    
    public static void critical(String msg)
    {
        log(CRITICAL, msg, null);
    }
    public static void error(String msg)
    {
        log(ERROR, msg, null);
    }
    public static void warn(String msg)
    {
        log(WARN, msg, null);
    }
    public static void info(String msg)
    {
        log(INFO, msg, null);
    }
    public static void trace(String msg)
    {
        log(TRACE, msg, null);
    }
    public static void critical(String msg, Throwable t)
    {
        log(CRITICAL, msg, t);
    }
    public static void error(String msg, Throwable t)
    {
        log(ERROR, msg, t);
    }
    public static void warn(String msg, Throwable t)
    {
        log(WARN, msg, t);
    }
    public static void info(String msg, Throwable t)
    {
        log(INFO, msg, t);
    }
    public static void trace(String msg, Throwable t)
    {
        log(TRACE, msg, t);
    }
    public static void snapshot(String html, String fname)
    {
        try
        {
            if (System.getProperty("os.name").indexOf("Windows") >= 0)
                fname = "c:\\temp\\"+fname;
            else
                fname = "/tmp/"+fname;
            FileWriter fw = new FileWriter(fname);
            fw.write(html);
            fw.close();
            DebugUtils.info("Snapshot into "+fname);
        }
        catch (IOException e)
        {
        }
    }

    public static boolean debug = true;
    private static String   indent = "";
    
    public static void debug(String msg)
    {
        info(indent+msg, null);
    }
    
    public static void beginGroup(String name)
    {
        debug("<"+name+">");
        indent += "  ";
    }
    
    public static void endGroup(String name)
    {
        if (indent.length() > 2)
            indent = indent.substring(2);
        debug("</"+name+">");
    }
    
    public static void beginGroup()
    {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] stack = t.getStackTrace();
        beginGroup(stack[1].getMethodName());
    }
    
    public static void endGroup()
    {
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] stack = t.getStackTrace();
        endGroup(stack[1].getMethodName());
    }
        
    public static void dumpCallStack()
    {
        beginGroup("stack");
        Throwable t = new Throwable();
        t.fillInStackTrace();
        StackTraceElement[] stack = t.getStackTrace();
        for (int i = 1; i <= 5; i++)
            debug("<"+stack[i].getClassName()+"."+stack[i].getMethodName()+":"+stack[i].getLineNumber()+"/>");
        endGroup("stack");
    }

}
