package jo.util.ui.swing.logic;

import java.io.IOException;

import javax.swing.ImageIcon;

import jo.util.utils.io.ResourceUtils;

public class IconLogic
{
    public static ImageIcon loadFromResource(String resPath)
    {
        byte[] data;
        try
        {
            data = ResourceUtils.loadSystemResourceBinary(resPath);
        }
        catch (IOException e)
        {
            throw new IllegalArgumentException("Cannot load resource at path '"+resPath+"'");
        }
        if (data == null)
            throw new IllegalArgumentException("Cannot load resource at path '"+resPath+"'");
        ImageIcon ret = new ImageIcon(data, resPath);
        return ret;
    }
}
