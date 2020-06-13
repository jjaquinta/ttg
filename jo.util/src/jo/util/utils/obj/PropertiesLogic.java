package jo.util.utils.obj;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

public class PropertiesLogic
{
    public static String toString(Properties props)
    {
        if (props == null)
            return null;
        StringWriter sw = new StringWriter();
        try
        {
            props.store(sw, "");
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
        return sw.toString();
    }
    
    public static Properties fromString(String data)
    {
        StringReader rdr = new StringReader(data);
        Properties props = new Properties();
        try
        {
            props.load(rdr);
        }
        catch (IOException e)
        {
            throw new IllegalStateException(e);
        }
        return props;
    }
}
