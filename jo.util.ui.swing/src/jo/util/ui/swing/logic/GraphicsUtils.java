package jo.util.ui.swing.logic;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

public class GraphicsUtils
{   
    public static int getStringWidth(Graphics g, String str)
    {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(str, g);
        return (int)bounds.getWidth();
    }

    public static Rectangle2D getStringExtent(Graphics g, String str)
    {
        FontMetrics fm = g.getFontMetrics();
        Rectangle2D bounds = fm.getStringBounds(str, g);
        return bounds;
    }

    public static void fillRect(Graphics g, Color color, Rectangle rect)
    {
        g.setColor(color);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }

    public static void drawRect(Graphics g, Color color, Rectangle rect)
    {
        g.setColor(color);
        g.drawRect(rect.x, rect.y, rect.width, rect.height);
    }

    public static void drawString(Graphics g, Color color, String txt, int x, int y)
    {
        g.setColor(color);
        g.drawString(txt, x, y);
    }

}
