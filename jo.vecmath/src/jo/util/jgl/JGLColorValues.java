package jo.util.jgl;

import javax.vecmath.Color3f;

import jo.util.utils.MathUtils;

public class JGLColorValues
{
    public static final Color3f   Black        = new Color3f(0f / 255f,
                                                       0f / 255f, 0f / 255f);
    public static final Color3f   White        = new Color3f(255f / 255f,
                                                       255f / 255f, 255f / 255f);
    public static final Color3f   Red          = new Color3f(255f / 255f,
                                                       0f / 255f, 0f / 255f);
    public static final Color3f   Lime         = new Color3f(0f / 255f,
                                                       255f / 255f, 0f / 255f);
    public static final Color3f   Blue         = new Color3f(0f / 255f,
                                                       0f / 255f, 255f / 255f);
    public static final Color3f   Yellow       = new Color3f(255f / 255f,
                                                       255f / 255f, 0f / 255f);
    public static final Color3f   Cyan         = new Color3f(0f / 255f,
                                                       255f / 255f, 255f / 255f);
    public static final Color3f   Aqua         = new Color3f(0f / 255f,
                                                       255f / 255f, 255f / 255f);
    public static final Color3f   Magenta      = new Color3f(255f / 255f,
                                                       0f / 255f, 255f / 255f);
    public static final Color3f   Fuchsia      = new Color3f(255f / 255f,
                                                       0f / 255f, 255f / 255f);
    public static final Color3f   Silver       = new Color3f(192f / 255f,
                                                       192f / 255f, 192f / 255f);
    public static final Color3f   Gray         = new Color3f(128f / 255f,
                                                       128f / 255f, 128f / 255f);
    public static final Color3f   Maroon       = new Color3f(128f / 255f,
                                                       0f / 255f, 0f / 255f);
    public static final Color3f   Olive        = new Color3f(128f / 255f,
                                                       128f / 255f, 0f / 255f);
    public static final Color3f   Green        = new Color3f(0f / 255f,
                                                       128f / 255f, 0f / 255f);
    public static final Color3f   Purple       = new Color3f(128f / 255f,
                                                       0f / 255f, 128f / 255f);
    public static final Color3f   Teal         = new Color3f(0f / 255f,
                                                       128f / 255f, 128f / 255f);
    public static final Color3f   Navy         = new Color3f(0f / 255f,
                                                       0f / 255f, 128f / 255f);

    public static final Color3f[] POINT_COLORS = { JGLColorValues.Red,
            JGLColorValues.Lime, JGLColorValues.Blue, JGLColorValues.Yellow,
            JGLColorValues.Cyan, JGLColorValues.Magenta, JGLColorValues.Silver,
            JGLColorValues.Gray, JGLColorValues.Maroon, JGLColorValues.Olive,
            JGLColorValues.Green, JGLColorValues.Purple, JGLColorValues.Teal,
            JGLColorValues.Navy, JGLColorValues.White, };

    // http://marcocorvi.altervista.org/games/imgpr/rgb-hsl.htm
    public static Color3f RGBtoHSL(Color3f rgb)
    {
        float max = Math.max(rgb.x, Math.max(rgb.y, rgb.z));
        float min = Math.min(rgb.x, Math.min(rgb.y, rgb.z));
        float l = (max + min) / 2;
        float s;
        float h;
        if (MathUtils.epsilonEquals(max, min))
            s = 0;
        else if (l < .5f)
            s = (max - min) / (max + min);
        else
            s = (max - min) / (2 - max - min);
        if (MathUtils.epsilonEquals(max, min))
            h = 0;
        else if (MathUtils.epsilonEquals(max, rgb.x))
            h = (rgb.y - rgb.z) / (max - min);
        else if (MathUtils.epsilonEquals(max, rgb.y))
            h = 2 + (rgb.z - rgb.x) / (max - min);
        else
            // if (MathUtils.epsilonEquals(max, rgb.z))
            h = 4 + (rgb.x - rgb.y) / (max - min);
        Color3f hsl = new Color3f(h, s, l);
        return hsl;
    }

    public static Color3f HSLtoRGB(Color3f hsl)
   {
       float hue = hsl.x;
       float saturation = hsl.y;
       float value = hsl.z;
       float r;
       float g;
       float b;
       int h = (int)(hue * 6);
       float f = hue * 6 - h;
       float p = value * (1 - saturation);
       float q = value * (1 - f * saturation);
       float t = value * (1 - (1 - f) * saturation);

       switch (h)
       {
           case 0:
               r = value;
               g = t;
               b = p;
               break;
           case 1:
               r = q;
               g = value;
               b = p;
               break;
           case 2:
               r = p;
               g = value;
               b = t;
               break;
           case 3:
               r = p;
               g = q;
               b = value;
               break;
           case 4:
               r = t;
               g = p;
               b = value;
               break;
           case 5:
               r = value;
               g = p;
               b = q;
               break;
           default:
               throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
       }
       Color3f rgb = new Color3f(r, g, b);
       return rgb;
   }
    
    public static Color3f brighten(Color3f rgb, float amnt)
    {
        Color3f b = new Color3f();
        b.x = Math.min(1, Math.max(0, rgb.x + amnt));
        b.y = Math.min(1, Math.max(0, rgb.y + amnt));
        b.z = Math.min(1, Math.max(0, rgb.z + amnt));
//        Color3f hsl = RGBtoHSL(rgb);
//        hsl.y -= amnt;
//        hsl.z += amnt;
//        return HSLtoRGB(hsl);
        return b;
    }
    
    public static Color3f darken(Color3f rgb, float amnt)
    {
        return brighten(rgb, -amnt);
    }
}
