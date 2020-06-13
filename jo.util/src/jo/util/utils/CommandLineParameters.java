/*
 * Created on Sep 14, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class CommandLineParameters implements ParametersEngine
{
    private Properties  mProps = null;

    public String getString(String key)
    {
        synchronized (this)
        {
            if (mProps == null)
                loadProps();
        }
        return mProps.getProperty(key);
    }

    private void loadProps()
    {
        mProps = new Properties();
        try
        {
            String home = System.getProperty("user.home");
            File f = new File(home, ".jo/neo2/user.properties");
            FileInputStream fis = new FileInputStream(f);
            mProps.load(fis);
            fis.close();
        }
        catch (IOException e)
        {
        }
    }
}
