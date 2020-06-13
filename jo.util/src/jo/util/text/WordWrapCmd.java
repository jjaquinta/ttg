package jo.util.text;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jo.util.cmd.BaseCmd;

public class WordWrapCmd extends BaseCmd
{
    private String              mIn;
    public static final String  defIn = "-";
    public static final String  helpIn = "File for input";
    private String              mOut;
    public static final String  defOut = "-";
    public static final String  helpOut = "File for output";
    private int                 mWidth;
    public static final int     defWidth = 70;
    public static final String  helpWidth = "character width to wrap to";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        WordWrapCmd app = new WordWrapCmd();
        app.init(args);
    }

    @Override
    public String getLongDesc()
    {
        return "Wrap long lines in a text file.";
    }

    @Override
    public String getOneLineDesc()
    {
        return "Word wrap a text file.";
    }

    @Override
    public void run()
    {
        try
        {
            InputStream in;
            if (mIn.equals("-"))
                in = System.in;
            else
                in = new FileInputStream(mIn);
            OutputStream out;
            if (mOut.equals("-"))
                out = System.out;
            else
                out = new FileOutputStream(mOut);
            StringBuffer line = new StringBuffer();
            String eoln = "\n";
            for (;;)
            {
                int ch = in.read();
                if (ch == -1)
                    break;
                line.append((char)ch);
                if (ch == '\r')
                    eoln = "\r\n";
                if (ch == '\n')
                {
                    write(out, line.toString());
                    line.setLength(0);
                }
                else if (line.length() >= mWidth)
                {
                    int o = line.lastIndexOf(" ");
                    if (o >= 0)
                    {
                        write(out, line.substring(0, o)+eoln);
                        line.delete(0, o+1);
                    }
                }
            }
            if (line.length() > 0)
                write(out, line.toString());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void write(OutputStream os, String line) throws IOException
    {
        char[] c = line.toCharArray();
        for (int i = 0; i < c.length; i++)
            os.write((byte)c[i]);
    }

    public String getIn()
    {
        return mIn;
    }

    public void setIn(String in)
    {
        mIn = in;
    }

    public String getOut()
    {
        return mOut;
    }

    public void setOut(String out)
    {
        mOut = out;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = width;
    }
}
