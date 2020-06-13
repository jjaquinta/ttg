package jo.util.noise;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jo.util.utils.DebugUtils;

public class TestNoise
{
    private static final int BARREN = 0xFFDEAD;
    private static final int HILLS = 0xD2B48C;
    private static final int WOODS = 0x228B22;
    private static final int FOREST = 0x006400;
    private static final int DESERT = 0xFFFF00;
    private static final int DRY_PLAIN = 0xADFF2F;
    private static final int LUSH_PLAIN = 0x9ACD32;
    private static final int SWAMP = 0x808000;
    private static final int LAKE = 0x0000FF;
    private static final int MOUNTAIN = 0x808080;
    
    public static final void main(String[] argv)
    {
        Noise elevation = new Noise(100);
        Noise rainfall = new Noise(101);
        BufferedImage bi = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
        double maxElev = 0;
        double minElev = 0;
        double maxRain = 0;
        double minRain = 0;
        for (int x = 0; x < bi.getWidth(); x++)
            for (int y = 0; y < bi.getHeight(); y++)
            {
                /*
                double r = Noise.noise(x/64.0, y/64.0);
                double g = Noise.noise(x/32.0, y/32.0);
                double b = Noise.noise(x/128.0, y/128.0);
                int ir = (int)MathUtils.interpolate(r, -1, 1, 0, 255);
                int ig = (int)MathUtils.interpolate(g, -1, 1, 0, 255);
                int ib = (int)MathUtils.interpolate(b, -1, 1, 0, 255);
                int rgb = (ir<<16)|(ig<<8)|(ib);
                bi.setRGB(x, y, rgb);
                */
                double elev = elevation.noise(x/128.0, y/128.0);
                double rain = rainfall.noise(x/128.0, y/128.0);
                //double elev = 1 - y/256.0;
                //double rain = x/256.0 - 1;
                int rgb;
                if (rain < 0)
                {
                    if (elev < 0)
                        if (rain + elev < -.5)
                            rgb = DESERT;
                        else
                            rgb = DRY_PLAIN;
                    else
                    {
                        if (rain - elev > -.5)
                            rgb = HILLS;
                        else if (rain - elev > -.65)
                            rgb = BARREN;
                        else
                            rgb = MOUNTAIN;
                    }
                }
                else
                {
                    if (elev < 0)
                    {
                        if (rain - elev < .5)
                            rgb = LUSH_PLAIN;
                        else if (rain - elev < .65)
                            rgb = SWAMP;
                        else
                            rgb = LAKE;
                    }
                    else
                        if (elev + rain < .5)
                            rgb = WOODS;
                        else
                            rgb = FOREST;
                }
                bi.setRGB(x, y, rgb);
                maxElev = Math.max(maxElev, elev);
                minElev = Math.min(minElev, elev);
                maxRain = Math.max(maxRain, rain);
                minRain = Math.min(minRain, rain);
            }
        try
        {
            ImageIO.write(bi, "PNG", new File("c:\\temp\\perlin_noise.png"));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        DebugUtils.info("Elev: "+minElev + " to "+maxElev);
        DebugUtils.info("Rain: "+minRain+ " to "+maxRain);
    }
}
