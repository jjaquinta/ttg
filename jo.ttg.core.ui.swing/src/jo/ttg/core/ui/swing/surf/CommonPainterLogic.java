package jo.ttg.core.ui.swing.surf;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.StatsSizBean;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.utils.io.ResourceUtils;

public class CommonPainterLogic
{
    protected static final Color COLOR_DEPTHS = ColorUtils.DarkSlateBlue;
    protected static final Color COLOR_ICEPACK = ColorUtils.GhostWhite;
    protected static final Color COLOR_OCEAN = ColorUtils.MediumBlue;
    protected static final Color COLOR_FOREST = ColorUtils.ForestGreen;
    protected static final Color COLOR_DESERT = ColorUtils.Yellow;
    protected static final Color COLOR_NOTE_FORE = ColorUtils.DarkCyan;
    protected static final Color COLOR_NOTE_BACK = new Color(255, 255, 255, 128);
    // images
    protected static BufferedImage ICEBERG;
    protected static BufferedImage MOUNTAIN;
    protected static BufferedImage VOLCANO;
    protected static BufferedImage HILL;
    protected static BufferedImage ALTITUDE;
    protected static BufferedImage BARREN;
    protected static Color[] COLOR_ALTITUDE_VERDANT;
    protected static Color[] COLOR_ALTITUDE_DESOLATE = new Color[] { ColorUtils.DarkSlateGray, ColorUtils.DarkGray };
    protected static Color[] COLOR_ALTITUDE_BARREN;
    protected static Color[] COLOR_ALTITUDE_ICY = new Color[] { ColorUtils.LightGray, ColorUtils.WhiteSmoke };
    static
    {
        try
        {
            ICEBERG = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/iceberg.png", CommonPainterLogic.class));
            MOUNTAIN = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/mountain.png", CommonPainterLogic.class));
            VOLCANO = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/volcano.png", CommonPainterLogic.class));
            HILL = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/hill.png", CommonPainterLogic.class));
            ALTITUDE = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/elevation.png", CommonPainterLogic.class));
            BARREN = ImageIO.read(ResourceUtils.loadSystemResourceStream("img/barren.png", CommonPainterLogic.class));
            COLOR_ALTITUDE_VERDANT = new Color[ALTITUDE.getHeight()];
            for (int i = 0; i < ALTITUDE.getHeight(); i++)
            {
                int rgb = ALTITUDE.getRGB(0, i);
                Color c = new Color(0xFF000000|rgb, true);
                COLOR_ALTITUDE_VERDANT[COLOR_ALTITUDE_VERDANT.length - i - 1] = c;
            }
            COLOR_ALTITUDE_BARREN = new Color[BARREN.getHeight()/2];
            for (int i = 0; i < COLOR_ALTITUDE_BARREN.length; i++)
            {
                int rgb = BARREN.getRGB(0, i + BARREN.getHeight()/4);
                Color c = new Color(0xFF000000|rgb, true);
                COLOR_ALTITUDE_BARREN[i] = c;
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    protected static final int SURFACE_VERDANT = 0;
    protected static final int SURFACE_DESOLATE = 1;
    protected static final int SURFACE_ICY = 2;
    protected static final int SURFACE_BARREN = 3;
    
    protected static int getSurfaceType(BodyWorldBean world)
    {
        if (world.getPopulatedStats().getUPP().getAtmos().getValue() <= 1)
        {
            if (world.getStatsSiz().getCore() == StatsSizBean.WC_ICY)
                return SURFACE_ICY;
            else
                return SURFACE_DESOLATE;
        }
        else if (world.getPopulatedStats().getUPP().getHydro().getValue() <= 1)
            return SURFACE_BARREN;
        else
            return SURFACE_VERDANT;
    }
}
