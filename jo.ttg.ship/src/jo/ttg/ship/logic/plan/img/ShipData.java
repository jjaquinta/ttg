package jo.ttg.ship.logic.plan.img;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipImageSettingsBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;

class ShipData
{
    ShipPlanBean          ship;
    ShipImageSettingsBean settings;
    List<ShipLabel>       labels = new ArrayList<>();
    Rectangle2D           triedWithin;
    Graphics2D            g;
    Point3i               sLower = new Point3i();
    Point3i               sUpper = new Point3i();
    int                   deck;
    Point3i               dUpper = new Point3i();
    Point3i               dLower = new Point3i();
    boolean               horizontal;
    Font                  smallFont;
    Font                  medFont;
    Font                  largeFont;
    Random                rnd;
    
    public void setG(Graphics _g)
    {
        g = (Graphics2D)_g;
        smallFont = makeFont(ss()/2);
        medFont = makeFont(ss());
        largeFont = makeFont(ss()*3/2);
    }
    
    private Font makeFont(int px)
    {
        int size = px;
        Font f = null;
        for (int i = 0; i < 8; i++)
        {
            f = new Font(Font.DIALOG, Font.PLAIN, size);
            FontMetrics fm = g.getFontMetrics(f);
            int d = fm.getHeight() - px;
            if (d == 0)
                break;
            if (d > 0)
                size--;
            else if (d < 0)
                size++;
        }
        return f;
    }
    
    public int ss()
    {
        return settings.getSquareSize();
    }
    
    void setShip(ShipPlanBean _ship)
    {
        ship = _ship;
        ship.getSquares().getBounds(sLower, sUpper);
    }
    
    void setDeck(int _deck)
    {
        deck = _deck;
        dLower = new Point3i(0, 0, deck);
        dUpper = new Point3i(0, 0, deck);
        ship.getSquares().getHorizontalBounds(dLower, dUpper);
   }
}
