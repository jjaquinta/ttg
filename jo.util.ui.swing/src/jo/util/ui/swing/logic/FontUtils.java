/*
 * Created on Oct 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.ui.swing.logic;

import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import jo.util.utils.FormatUtils;

public class FontUtils
{
    private static Map<String, Object> FONT_MAP;
    
    private static final String[][] DEFAULT_FONTS = {
        { "H1", "Arial", "18", "b", },   
        { "H2", "Arial", "16", "b", },   
        { "H3", "Arial", "14", "b", },   
        { "H4", "Arial", "12", "b", },   
        { "H5", "Arial", "10", "b", },   
        { "Dialog", "Arial", "10", "n", },   
        { "DialogSmall", "Arial", "8", "n", },   
        { "DialogBold", "Arial", "10", "b", },   
    };
    
    public static synchronized void init()
    {
        if (FONT_MAP != null)
            return;
        FONT_MAP = new HashMap<String, Object>(DEFAULT_FONTS.length);
        for (int i = 0; i < DEFAULT_FONTS.length; i++)
        {  
            String name = DEFAULT_FONTS[i][0];
            String[] info = new String[3];
            info[0] = DEFAULT_FONTS[i][1];
            info[1] = DEFAULT_FONTS[i][2];
            info[2] = DEFAULT_FONTS[i][3];
            FONT_MAP.put(name, info);
        }
        /*
        System.out.println("SCALABLE FONTS:");
        FontData[] f = Display.getDefault().getFontList(null, true);
        for (int i = 0; i < f.length; i++)
            System.out.println("  "+f[i].getName()+"/"+f[i].getHeight()+"/"+f[i].getStyle());
        System.out.println("UNSCALABLE FONTS:");
        f = Display.getDefault().getFontList(null, false);
        for (int i = 0; i < f.length; i++)
            System.out.println("  "+f[i].getName()+"/"+f[i].getHeight()+"/"+f[i].getStyle());
        */
    }
    
    public static void addFont(String name, String[] info)
    {
        synchronized (FONT_MAP)
        {
            FONT_MAP.put(name, info);
        }
    }
    
    public static void addFont(String name, Font f)
    {
        synchronized (FONT_MAP)
        {
            FONT_MAP.put(name, f);
        }
    }
    
    public static Font getFont(String name)
    {
        synchronized (FONT_MAP)
        {
            Object val = FONT_MAP.get(name);
            if (val == null)
                val = FONT_MAP.values().iterator().next();
            if (val instanceof Font)
                return (Font)val;
            String[] info = (String[])val;
            int size = FormatUtils.parseInt(info[1], 10);
            int style = Font.PLAIN;
            if (info[2].indexOf("b") >= 0)
                style |= Font.BOLD;
            if (info[2].indexOf("i") >= 0)
                style |= Font.ITALIC;
            if (info[2].indexOf("n") >= 0)
                style |= Font.PLAIN;
            Font ret = new Font(info[0], style, size);
            FONT_MAP.put(name, ret);
            return ret;
        }
    }
    
    public static Font getFont(String name, int size, int style)
    {
        init();
        String key = "FONT:"+name+"/"+size+"/"+style;
        synchronized (FONT_MAP)
        {
            Object val = FONT_MAP.get(key);
            if (val instanceof Font)
                return (Font)val;
            Font ret = new Font(name, style, size);
            FONT_MAP.put(key, ret);
            return ret;
        }
    }
}
