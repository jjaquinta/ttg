package jo.ttg.core.report.surf;

import java.util.Iterator;
import java.util.Random;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.core.report.sys.SVGStyle;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class PlatesReport
{    
    private static int[] PLATE_COLORS = {
        //0x000000,
        0x000080,
        0x00008B,
        0x0000CD,
        //0x0000FF,
        0x006400,
        0x008000,
        0x008080,
        0x008B8B,
        0x00BFFF,
        0x00CED1,
        0x00FA9A,
        0x00FF00,
        0x00FF7F,
        //0x00FFFF,
        //0x00FFFF,
        0x191970,
        0x1E90FF,
        0x20B2AA,
        0x228B22,
        0x2E8B57,
        0x2F4F4F,
        0x32CD32,
        0x3CB371,
        0x40E0D0,
        0x4169E1,
        0x4682B4,
        0x483D8B,
        0x48D1CC,
        0x4B0082,
        0x556B2F,
        0x5F9EA0,
        0x6495ED,
        0x66CDAA,
        0x696969,
        0x6A5ACD,
        0x6B8E23,
        0x708090,
        0x778899,
        0x7B68EE,
        0x7CFC00,
        0x7FFF00,
        0x7FFFD4,
        0x800000,
        0x800080,
        0x808000,
        0x808080,
        0x87CEEB,
        0x87CEFA,
        0x8A2BE2,
        0x8B0000,
        0x8B008B,
        0x8B4513,
        0x8FBC8F,
        0x90EE90,
        0x9370DB,
        0x9400D3,
        0x98FB98,
        0x9932CC,
        0x9ACD32,
        0xA0522D,
        0xA52A2A,
        0xA9A9A9,
        0xADD8E6,
        0xADFF2F,
        0xAFEEEE,
        0xB0C4DE,
        0xB0E0E6,
        0xB22222,
        0xB8860B,
        0xBA55D3,
        0xBC8F8F,
        0xBDB76B,
        0xC0C0C0,
        0xC71585,
        0xCD5C5C,
        0xCD853F,
        0xD2691E,
        0xD2B48C,
        0xD3D3D3,
        0xD8BFD8,
        0xDA70D6,
        0xDAA520,
        0xDB7093,
        0xDC143C,
        0xDCDCDC,
        0xDDA0DD,
        0xDEB887,
        0xE0FFFF,
        0xE6E6FA,
        0xE9967A,
        0xEE82EE,
        0xEEE8AA,
        0xF08080,
        0xF0E68C,
        0xF0F8FF,
        0xF0FFF0,
        0xF0FFFF,
        0xF4A460,
        0xF5DEB3,
        0xF5F5DC,
        0xF5F5F5,
        0xF5FFFA,
        0xF8F8FF,
        0xFA8072,
        0xFAEBD7,
        0xFAF0E6,
        0xFAFAD2,
        0xFDF5E6,
        //0xFF0000,
        //0xFF00FF,
        //0xFF00FF,
        0xFF1493,
        0xFF4500,
        0xFF6347,
        0xFF69B4,
        0xFF7F50,
        0xFF8C00,
        0xFFA07A,
        0xFFA500,
        0xFFB6C1,
        0xFFC0CB,
        0xFFD700,
        0xFFDAB9,
        0xFFDEAD,
        0xFFE4B5,
        0xFFE4C4,
        0xFFE4E1,
        0xFFEBCD,
        0xFFEFD5,
        0xFFF0F5,
        0xFFF5EE,
        0xFFF8DC,
        0xFFFACD,
        0xFFFAF0,
        0xFFFAFA,
        0xFFFF00,
        0xFFFFE0,
        0xFFFFF0,
        //0xFFFFFF,
    };
    static
    {
        Random r = new Random(0);
        for (int i = 0; i < PLATE_COLORS.length; i++)
        {
            int j = r.nextInt(PLATE_COLORS.length);
            int c = PLATE_COLORS[i];
            PLATE_COLORS[i] = PLATE_COLORS[j];
            PLATE_COLORS[j] = c;
        }
    }

    public static void drawPlateMap(SVGHelper legend, SVGHelper grid, IHEALGlobe<MapHexBean> globe)
    {
        //CoverReport.drawGrid(grid, globe.getResolution());
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord c = i.next();
            MapHexBean hex = globe.get(c);
            drawHex(legend, hex);
        }
    }

    private static void drawHex(SVGHelper legend, MapHexBean hex)
    {
        int plate = hex.getPlate();
        int move = hex.getPlateMove();
        int color = PLATE_COLORS[plate%PLATE_COLORS.length];
        if (move == MapHexBean.M_CONVERGING)
            color = darken(color);
        else if (move == MapHexBean.M_DIVERGING)
            color = lighten(color);
        SVGStyle bg = new SVGStyle();        
        bg.mFill = color;
        legend.drawPolygon(bg, CoverReport.makeHexPoly(hex.getLocation()));
    }
    
    private static int darken(int color)
    {
        return modify(color, 0.95);
    }
    
    private static int lighten(int color)
    {
        return modify(color, 1.05);
    }
    
    private static int modify(int color, double mult)
    {
        short[] rgb = split(color);
        rgb[0] = modify(rgb[0], mult);
        rgb[1] = modify(rgb[1], mult);
        rgb[2] = modify(rgb[2], mult);
        return join(rgb);        
    }
    
    private static short modify(short color, double mult)
    {
        color = (short)(color*mult);
        if (color < 0)
            color = 0;
        else if (color > 255)
            color = 255;
        return color;
    }
    
    private static short[] split(int color)
    {
        short[] rgb = new short[3];
        rgb[0] = (short)((color>>0)&0xff);
        rgb[1] = (short)((color>>8)&0xff);
        rgb[2] = (short)((color>>16)&0xff);
        return rgb;
    }
    
    private static int join(short[] rgb)
    {
        int color = 0;
        color |= ((rgb[0]&0xff)<<0);
        color |= ((rgb[1]&0xff)<<0);
        color |= ((rgb[2]&0xff)<<0);
        return color;
    }
}
