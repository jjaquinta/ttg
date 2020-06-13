/*
 * Created on Oct 28, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.ui.swing.logic;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import jo.util.utils.MathUtils;
import jo.util.utils.obj.IntegerUtils;

public class ColorUtils
{
    private static Map<Object,Object>  mRawColorMap;
    private static Map<String,Object> COLOR_MAP;
    private static Map<String,String> mColorNameMap;
    // https://htmlcolorcodes.com/color-names/
    public static Color IndianRed            = new Color(205, 92, 92);
    public static Color LightCoral           = new Color(240, 128, 128);
    public static Color Salmon               = new Color(250, 128, 114);
    public static Color DarkSalmon           = new Color(233, 150, 122);
    public static Color Crimson              = new Color(220, 20, 60);
    public static Color Red                  = new Color(255, 0, 0);
    public static Color FireBrick            = new Color(178, 34, 34);
    public static Color DarkRed              = new Color(139, 0, 0);
    public static Color Pink                 = new Color(255, 192, 203);
    public static Color LightPink            = new Color(255, 182, 193);
    public static Color HotPink              = new Color(255, 105, 180);
    public static Color DeepPink             = new Color(255, 20, 147);
    public static Color MediumVioletRed      = new Color(199, 21, 133);
    public static Color PaleVioletRed        = new Color(219, 112, 147);
    public static Color LightSalmon          = new Color(255, 160, 122);
    public static Color Coral                = new Color(255, 127, 80);
    public static Color Tomato               = new Color(255, 99, 71);
    public static Color OrangeRed            = new Color(255, 69, 0);
    public static Color DarkOrange           = new Color(255, 140, 0);
    public static Color Orange               = new Color(255, 165, 0);
    public static Color Gold                 = new Color(255, 215, 0);
    public static Color Yellow               = new Color(255, 255, 0);
    public static Color LightYellow          = new Color(255, 255, 224);
    public static Color LemonChiffon         = new Color(255, 250, 205);
    public static Color LightGoldenrodYellow = new Color(250, 250, 210);
    public static Color PapayaWhip           = new Color(255, 239, 213);
    public static Color Moccasin             = new Color(255, 228, 181);
    public static Color PeachPuff            = new Color(255, 218, 185);
    public static Color PaleGoldenrod        = new Color(238, 232, 170);
    public static Color Khaki                = new Color(240, 230, 140);
    public static Color DarkKhaki            = new Color(189, 183, 107);
    public static Color Lavender             = new Color(230, 230, 250);
    public static Color Thistle              = new Color(216, 191, 216);
    public static Color Plum                 = new Color(221, 160, 221);
    public static Color Violet               = new Color(238, 130, 238);
    public static Color Orchid               = new Color(218, 112, 214);
    public static Color Fuchsia              = new Color(255, 0, 255);
    public static Color Magenta              = new Color(255, 0, 255);
    public static Color MediumOrchid         = new Color(186, 85, 211);
    public static Color MediumPurple         = new Color(147, 112, 219);
    public static Color RebeccaPurple        = new Color(102, 51, 153);
    public static Color BlueViolet           = new Color(138, 43, 226);
    public static Color DarkViolet           = new Color(148, 0, 211);
    public static Color DarkOrchid           = new Color(153, 50, 204);
    public static Color DarkMagenta          = new Color(139, 0, 139);
    public static Color Purple               = new Color(128, 0, 128);
    public static Color Indigo               = new Color(75, 0, 130);
    public static Color SlateBlue            = new Color(106, 90, 205);
    public static Color DarkSlateBlue        = new Color(72, 61, 139);
    public static Color GreenYellow          = new Color(173, 255, 47);
    public static Color Chartreuse           = new Color(127, 255, 0);
    public static Color LawnGreen            = new Color(124, 252, 0);
    public static Color Lime                 = new Color(0, 255, 0);
    public static Color LimeGreen            = new Color(50, 205, 50);
    public static Color PaleGreen            = new Color(152, 251, 152);
    public static Color LightGreen           = new Color(144, 238, 144);
    public static Color MediumSpringGreen    = new Color(0, 250, 154);
    public static Color SpringGreen          = new Color(0, 255, 127);
    public static Color MediumSeaGreen       = new Color(60, 179, 113);
    public static Color SeaGreen             = new Color(46, 139, 87);
    public static Color ForestGreen          = new Color(34, 139, 34);
    public static Color Green                = new Color(0, 128, 0);
    public static Color DarkGreen            = new Color(0, 100, 0);
    public static Color YellowGreen          = new Color(154, 205, 50);
    public static Color OliveDrab            = new Color(107, 142, 35);
    public static Color Olive                = new Color(128, 128, 0);
    public static Color DarkOliveGreen       = new Color(85, 107, 47);
    public static Color MediumAquamarine     = new Color(102, 205, 170);
    public static Color DarkSeaGreen         = new Color(143, 188, 139);
    public static Color LightSeaGreen        = new Color(32, 178, 170);
    public static Color DarkCyan             = new Color(0, 139, 139);
    public static Color Teal                 = new Color(0, 128, 128);
    public static Color Aqua                 = new Color(0, 255, 255);
    public static Color Cyan                 = new Color(0, 255, 255);
    public static Color LightCyan            = new Color(224, 255, 255);
    public static Color PaleTurquoise        = new Color(175, 238, 238);
    public static Color Aquamarine           = new Color(127, 255, 212);
    public static Color Turquoise            = new Color(64, 224, 208);
    public static Color MediumTurquoise      = new Color(72, 209, 204);
    public static Color DarkTurquoise        = new Color(0, 206, 209);
    public static Color CadetBlue            = new Color(95, 158, 160);
    public static Color SteelBlue            = new Color(70, 130, 180);
    public static Color LightSteelBlue       = new Color(176, 196, 222);
    public static Color PowderBlue           = new Color(176, 224, 230);
    public static Color LightBlue            = new Color(173, 216, 230);
    public static Color SkyBlue              = new Color(135, 206, 235);
    public static Color LightSkyBlue         = new Color(135, 206, 250);
    public static Color DeepSkyBlue          = new Color(0, 191, 255);
    public static Color DodgerBlue           = new Color(30, 144, 255);
    public static Color CornflowerBlue       = new Color(100, 149, 237);
    public static Color MediumSlateBlue      = new Color(123, 104, 238);
    public static Color RoyalBlue            = new Color(65, 105, 225);
    public static Color Blue                 = new Color(0, 0, 255);
    public static Color MediumBlue           = new Color(0, 0, 205);
    public static Color DarkBlue             = new Color(0, 0, 139);
    public static Color Navy                 = new Color(0, 0, 128);
    public static Color MidnightBlue         = new Color(25, 25, 112);
    public static Color Cornsilk             = new Color(255, 248, 220);
    public static Color BlanchedAlmond       = new Color(255, 235, 205);
    public static Color Bisque               = new Color(255, 228, 196);
    public static Color NavajoWhite          = new Color(255, 222, 173);
    public static Color Wheat                = new Color(245, 222, 179);
    public static Color BurlyWood            = new Color(222, 184, 135);
    public static Color Tan                  = new Color(210, 180, 140);
    public static Color RosyBrown            = new Color(188, 143, 143);
    public static Color SandyBrown           = new Color(244, 164, 96);
    public static Color Goldenrod            = new Color(218, 165, 32);
    public static Color DarkGoldenrod        = new Color(184, 134, 11);
    public static Color Peru                 = new Color(205, 133, 63);
    public static Color Chocolate            = new Color(210, 105, 30);
    public static Color SaddleBrown          = new Color(139, 69, 19);
    public static Color Sienna               = new Color(160, 82, 45);
    public static Color Brown                = new Color(165, 42, 42);
    public static Color Maroon               = new Color(128, 0, 0);
    public static Color White                = new Color(255, 255, 255);
    public static Color Snow                 = new Color(255, 250, 250);
    public static Color HoneyDew             = new Color(240, 255, 240);
    public static Color MintCream            = new Color(245, 255, 250);
    public static Color Azure                = new Color(240, 255, 255);
    public static Color AliceBlue            = new Color(240, 248, 255);
    public static Color GhostWhite           = new Color(248, 248, 255);
    public static Color WhiteSmoke           = new Color(245, 245, 245);
    public static Color SeaShell             = new Color(255, 245, 238);
    public static Color Beige                = new Color(245, 245, 220);
    public static Color OldLace              = new Color(253, 245, 230);
    public static Color FloralWhite          = new Color(255, 250, 240);
    public static Color Ivory                = new Color(255, 255, 240);
    public static Color AntiqueWhite         = new Color(250, 235, 215);
    public static Color Linen                = new Color(250, 240, 230);
    public static Color LavenderBlush        = new Color(255, 240, 245);
    public static Color MistyRose            = new Color(255, 228, 225);
    public static Color Gainsboro            = new Color(220, 220, 220);
    public static Color LightGray            = new Color(211, 211, 211);
    public static Color Silver               = new Color(192, 192, 192);
    public static Color DarkGray             = new Color(169, 169, 169);
    public static Color Gray                 = new Color(128, 128, 128);
    public static Color DimGray              = new Color(105, 105, 105);
    public static Color LightSlateGray       = new Color(119, 136, 153);
    public static Color SlateGray            = new Color(112, 128, 144);
    public static Color DarkSlateGray        = new Color(47, 79, 79);
    public static Color Black                = new Color(0, 0, 0);

    private static final String[][] DEFAULT_COLORS = {
        { "black", "000000", },
        { "silver", "C0C0C0", }, 
        { "gray", "808080", }, 
        { "grey", "808080", }, 
        { "white", "FFFFFF", },
        { "maroon", "800000", }, 
        { "red", "FF0000", },
        { "purple", "800080", }, 
        { "fuchsia", "FF00FF", },
        { "green", "008000", }, 
        { "lime", "00FF00", },
        { "olive", "808000", }, 
        { "yellow", "FFFF00", },
        { "navy", "000080", }, 
        { "blue", "0000FF", },
        { "teal", "008080", }, 
        { "aqua", "00FFFF", },  
        { "magenta", "FF00FF", }, 
        { "aliceblue", "F0F8FF", }, 
        { "antiquewhite", "FAEBD7", }, 
        { "aquamarine", "7FFFD4", }, 
        { "beige", "F5F5DC", }, 
        { "bisque", "FFE4C4", }, 
        { "blanchedalmond", "FFEBCD", }, 
        { "blueviolet", "8A2BE2", }, 
        { "brown", "A52A2A", }, 
        { "burlywood", "DEB887", }, 
        { "cadetblue", "5F9EA0", }, 
        { "chartreuse", "7FFF00", }, 
        { "chocolate", "D2691E", }, 
        { "coral", "FF7F50", }, 
        { "cornflowerblue", "6495ED", }, 
        { "cornsilk", "FFF8DC", }, 
        { "crimson", "DC143C", }, 
        { "darkblue", "00008B", }, 
        { "darkcyan", "008B8B", }, 
        { "cyan", "00FFFF", }, 
        { "darkgoldenrod", "B8860B", }, 
        { "lightgray", "A9A9A9", }, 
        { "lightgrey", "A9A9A9", }, 
        { "darkgray", "404040", }, 
        { "darkgrey", "404040", }, 
        { "darkgreen", "006400", }, 
        { "darkkhaki", "BDB76B", }, 
        { "darkmagenta", "8B008B", }, 
        { "darkolivegreen", "556B2F", }, 
        { "darkorange", "FF8C00", }, 
        { "darkorchid", "9932CC", }, 
        { "darkred", "8B0000", }, 
        { "darksalmon", "E9967A", }, 
        { "darkseagreen", "8FBC8B", }, 
        { "dodgerblue", "1E90FF", }, 
        { "forestgreen", "228B22", }, 
        { "indianred", "CD5C5C", }, 
        { "lavender", "E6E6FA", }, 
        { "lemonchiffon", "FFFACD", }, 
        { "lightgrey", "D3D3D3", }, 
        { "lightseagreen", "20B2AA", }, 
        { "mediumslateblue", "7B68EE", }, 
        { "navy", "000080", }, 
        { "orange", "FFA500", }, 
        { "salmon", "FA8072", }, 
        { "slateblue", "6A5ACD", }, 
        { "yellowgreen", "9ACD32", }, 
        { "gold", "FFD700", }, 
        { "palegoldenrod", "EEE8AA", }, 
    };
    
    public static synchronized void init()
    {
        if (COLOR_MAP != null)
            return;
        mRawColorMap = new HashMap<Object, Object>();
        COLOR_MAP = new HashMap<String, Object>(DEFAULT_COLORS.length);
        for (int i = 0; i < DEFAULT_COLORS.length; i++)
        {
            String name = DEFAULT_COLORS[i][0];
            String rgb = DEFAULT_COLORS[i][1];
            //System.out.println(name+"->"+rgb);
            COLOR_MAP.put(name, rgb);
        }
        mColorNameMap = new HashMap<String, String>();
    }
    
    public static void addColor(String name, String rgb)
    {
        synchronized (COLOR_MAP)
        {
            COLOR_MAP.put(name, rgb);
        }
    }
    
    public static void addColor(String name, Color c)
    {
        synchronized (COLOR_MAP)
        {
            COLOR_MAP.put(name, c);
        }
    }
    
    public static Color getColor(int r, int g, int b)
    {
        Long key = new Long((r<<16)|(g<<8)|(b<<0));
        Color ret = (Color)mRawColorMap.get(key);
        if (ret == null)
        {
            ret = new Color(r, g, b);
            mRawColorMap.put(key, ret);
        }
        return ret;
    }
    
    public static Color getColor(long rgb)
    {
        Long key = new Long(rgb);
        Color ret = (Color)mRawColorMap.get(key);
        if (ret == null)
        {
            int r = (int)((rgb&0xff0000)>>16);
            int g = (int)((rgb&0xff00)>>8);
            int b = (int)((rgb&0xff)>>0);
            ret = new Color(r, g, b);
            mRawColorMap.put(key, ret);
        }
        return ret;
    }
    
    public static double[] RGBtoHSL(Color c)
    {
        double var_R = (c.getRed() / 255.0);                     //Where RGB values = 0 ÷ 255
        double var_G = (c.getGreen() / 255.0);
        double var_B = (c.getBlue() / 255.0);

        double var_Min = Math.min(var_R, Math.min(var_G, var_B));    //Min. value of RGB
        double var_Max = Math.max(var_R, Math.max(var_G, var_B));    //Max. value of RGB
        double del_Max = var_Max - var_Min;             //Delta RGB value

        double H = 0;
        double S = 0;
        double L = ( var_Max + var_Min ) / 2;

        if (del_Max == 0)                     //This is a gray, no chroma...
        {
           H = 0;                                //HSL results = 0 ÷ 1
           S = 0;
        }
        else                                    //Chromatic data...
        {
           if (L < 0.5)
               S = del_Max/(var_Max + var_Min);
           else
               S = del_Max/(2 - var_Max - var_Min);
           
           double del_R = (((var_Max - var_R)/6) + (del_Max/2))/del_Max;
           double del_G = (((var_Max - var_G)/6) + (del_Max/2))/del_Max;
           double del_B = (((var_Max - var_B)/6) + (del_Max/2))/del_Max;

           if  (var_R == var_Max) 
               H = del_B - del_G;
           else if (var_G == var_Max) 
               H = (1.0/3.0) + del_R - del_B;
           else if (var_B == var_Max) 
               H = (2.0/3.0) + del_G - del_R;

           if (H < 0)
               H += 1.0;
           if (H > 1)
               H -= 1.0;
        }        
        double[] ret = new double[3];
        ret[0] = H;
        ret[1] = S;
        ret[2] = L;
        return ret;
    }
    
    public static int[] HSLtoRGB(double[] hsl)
    {
        double H = hsl[0];
        double S = hsl[1];
        double L = hsl[2];
        double R = 0;
        double G = 0;
        double B = 0;
        
        if (S == 0)                       //HSL values = 0 ÷ 1
        {
           R = L*255;                      //RGB results = 0 ÷ 255
           G = L*255;
           B = L*255;
        }
        else
        {
            double var_2;
           if (L < 0.5) 
               var_2 = L*(1 + S);
           else           
               var_2 = (L + S) - (S*L);

           double var_1 = 2*L - var_2;

           R = 255*Hue_2_RGB(var_1, var_2, H + (1.0/3.0));
           G = 255*Hue_2_RGB(var_1, var_2, H);
           B = 255*Hue_2_RGB(var_1, var_2, H - (1.0/3.0));
        }
        int ret[] = new int[3];
        ret[0] = (int)R;
        ret[1] = (int)G;
        ret[2] = (int)B;
        return ret;
    }
    
    private static double Hue_2_RGB(double v1, double v2, double vH)             //Function Hue_2_RGB
    {
       if (vH < 0) 
           vH += 1;
       if (vH > 1) 
           vH -= 1;
       if ((6*vH) < 1) 
           return (v1 + (v2 - v1)*6*vH);
       if ((2*vH) < 1) 
           return (v2);
       if ((3*vH) < 2) 
           return (v1 + (v2 - v1)*((2.0/3.0) - vH)*6);
       return v1;
    }
    
    public static String toString(int[] rgb)
    {
        return "["+rgb[0]+","+rgb[1]+","+rgb[2]+"]";
    }
    
    public static String toString(double[] hsl)
    {
        return "<"+hsl[0]+","+hsl[1]+","+hsl[2]+">";
    }
    
    public static String toString(Color c)
    {
        return "["+c.getRed()+","+c.getGreen()+","+c.getBlue()+"]";
    }

    public static Color brighter(String name) 
    {
        Color ret = null;
        if (COLOR_MAP.containsKey(name+"!brighter"))
            ret = getColor(name+"!brighter");
        else
        {
            Color c = getColor(name);
            ret = brighter(c);
            addColor(name+"!brighter", ret);
        }
        return ret;
    }

    public static Color brighter(Color c) 
    {
        return brighter(c, .5);
    }

    public static Color brighter(Color c, double FACTOR) 
    {
        if ((c.getRed() == 0) && (c.getGreen() == 0) && (c.getBlue() == 0))
            return getColor((int)(255*FACTOR), (int)(255*FACTOR), (int)(255*FACTOR));
        //System.out.print(toString(c));
        double[] hsl = RGBtoHSL(c);
        //System.out.print(toString(hsl));
        hsl[2] = Math.min(1.0, hsl[2]*(1.0 +FACTOR));
        //System.out.print(toString(hsl));
        int[] rgb = HSLtoRGB(hsl);
        //System.out.println(toString(rgb));
        return getColor(rgb[0], rgb[1], rgb[2]);
    }

    public static Color darker(Color c) 
    {
        return darker(c, .7);
    }

    public static Color darker(Color c, double FACTOR) 
    {
        return getColor(Math.max((int)(c.getRed()  *FACTOR), 0), 
             Math.max((int)(c.getGreen()*FACTOR), 0),
             Math.max((int)(c.getBlue() *FACTOR), 0));
    }

    public static Color darker(String name) 
    {
        Color ret = null;
        if (COLOR_MAP.containsKey(name+"!darker"))
            ret = getColor(name+"!darker");
        else
        {
            Color c = getColor(name);
            ret = darker(c);
            addColor(name+"!darker", ret);
        }
        return ret;
    }
    
    public static Color getColor(String name)
    {
        init();
        if (name.startsWith("#") && (name.length() == 7))
        {
            int r = Integer.parseInt(name.substring(1, 3), 16);
            int g = Integer.parseInt(name.substring(3, 5), 16);
            int b = Integer.parseInt(name.substring(5, 7), 16);
            return getColor(r, g, b);
        }
        if (name.startsWith("rgb(") && name.endsWith(")"))
        {
            StringTokenizer st = new StringTokenizer(name.substring(4, name.length() - 1), ", ");
            int r = IntegerUtils.parseInt(st.nextToken());
            int g = IntegerUtils.parseInt(st.nextToken());
            int b = IntegerUtils.parseInt(st.nextToken());
            return getColor(r, g, b);
        }
        synchronized (COLOR_MAP)
        {
            Object val = COLOR_MAP.get(name);
            if (val == null)
                val = COLOR_MAP.values().iterator().next();
            if (val instanceof Color)
                return (Color)val;
            String rgb = val.toString();
            if (rgb.startsWith("*"))
                return getColor(rgb.substring(1));
            int r = Integer.parseInt(rgb.substring(0, 2), 16);
            int g = Integer.parseInt(rgb.substring(2, 4), 16);
            int b = Integer.parseInt(rgb.substring(4, 6), 16);
            //System.out.println(name+"->"+rgb+"->"+r+"."+g+"."+b);
            Color ret = getColor(r, g, b);
            COLOR_MAP.put(name, ret);
            return ret;
        }
    }
    
    public static Color getMappedColor(String name)
    {
        String path = (String)mColorNameMap.get(name);
        if (path == null)
            return null;
        else
            return getColor(path);
    }

    public static Color interpolate(double idx, double floor, double ceil,
            Color lowColor, Color highColor)
    {
        int r = (int)MathUtils.interpolate(idx, floor, ceil, lowColor.getRed(), highColor.getRed());
        int g = (int)MathUtils.interpolate(idx, floor, ceil, lowColor.getGreen(), highColor.getGreen());
        int b = (int)MathUtils.interpolate(idx, floor, ceil, lowColor.getBlue(), highColor.getBlue());
        return new Color(r, g, b);
    }
}
