package jo.ttg.ship.logic.plan.img;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class LabelLogic
{
    static final int LEFT = -1;
    static final int RIGHT = 1;
    static final int CENTER = 0;

    static void addLabel(Shape s, String label, Font f, int align)
    {
        FontMetrics fm = ShipPlanImageLogic.mData.g.getFontMetrics(f);
        Rectangle2D r = fm.getStringBounds(label, ShipPlanImageLogic.mData.g);
        Rectangle bounds = s.getBounds();
        ShipLabel l = new ShipLabel();
        l.backColor = ShipPlanImageLogic.mData.settings.TEXT_BACK;
        l.textColor = ShipPlanImageLogic.mData.settings.TEXT_FORE;
        l.label = label;
        l.font = f;
        if (bounds.getWidth() > bounds.getHeight())
        {
            l.horizontal = true;
            int[] where = LabelLogic.fitWithin(s, (int)r.getWidth(), (int)r.getHeight(), align);
            where[1] += (int)(r.getHeight()/2 + fm.getAscent()/2) - fm.getDescent()/2;
            l.xy = where;
            l.bounds = ShipPlanImageLogic.mData.triedWithin;
        }
        else
        {
            l.horizontal = false;
            int[] where = LabelLogic.fitWithin(s, (int)r.getHeight(), (int)r.getWidth(), align);
            where[0] += (int)(r.getHeight()/2 - fm.getAscent()/2) + fm.getDescent();
            l.xy = where;
            l.bounds = ShipPlanImageLogic.mData.triedWithin;
        }
        ShipPlanImageLogic.mData.labels.add(l);
    }

    static void printLabels()
    {
        for (ShipLabel l : ShipPlanImageLogic.mData.labels)
        {
            if (l.bounds == null)
                continue;
            ShipPlanImageLogic.mData.g.setFont(l.font);
            if (l.horizontal)
            {
                int[] where = l.xy;
                ShipPlanImageLogic.mData.g.setColor(l.backColor);
                Area a = new Area(l.bounds);
                Ellipse2D e1 = new Ellipse2D.Double((l.bounds.getX() - l.bounds.getHeight()/2), 
                        l.bounds.getY(), 
                        l.bounds.getHeight(), 
                        l.bounds.getHeight());
                a.add(new Area(e1));
                Ellipse2D e2 = new Ellipse2D.Double((l.bounds.getX() + l.bounds.getWidth() - l.bounds.getHeight()/2), 
                        l.bounds.getY(), 
                        l.bounds.getHeight(), 
                        l.bounds.getHeight());
                a.add(new Area(e2));
                ShipPlanImageLogic.mData.g.fill(a);
                ShipPlanImageLogic.mData.g.setColor(l.textColor);
                ShipPlanImageLogic.mData.g.drawString(l.label, where[0], where[1]);
            }
            else
            {
                int[] where = l.xy;
                ShipPlanImageLogic.mData.g.setColor(l.backColor);
                Area a = new Area(l.bounds);
                Ellipse2D e1 = new Ellipse2D.Double(l.bounds.getX(), 
                        (l.bounds.getY() - l.bounds.getWidth()/2), 
                        l.bounds.getWidth(), 
                        l.bounds.getWidth());
                a.add(new Area(e1));
                Ellipse2D e2 = new Ellipse2D.Double(l.bounds.getX(), 
                        (l.bounds.getY() + l.bounds.getHeight() - l.bounds.getWidth()/2), 
                        l.bounds.getWidth(), 
                        l.bounds.getWidth());
                a.add(new Area(e2));
                ShipPlanImageLogic.mData.g.fill(a);
                ShipPlanImageLogic.mData.g.setColor(l.textColor);
                AffineTransform oldT = ShipPlanImageLogic.mData.g.getTransform();
                AffineTransform newT = new AffineTransform(oldT);
                newT.translate(where[0], where[1]);
                newT.rotate(Math.PI/2);
                ShipPlanImageLogic.mData.g.setTransform(newT);
                ShipPlanImageLogic.mData.g.drawString(l.label, 0, 0);
                ShipPlanImageLogic.mData.g.setTransform(oldT);
            }
        }
    }

    static int[] fitWithin(Shape bounds, int width, int height, int align)
    {
        int testX = -width/2;
        int testY = -height/2;
        if (align == LabelLogic.CENTER)
        {
            testX += (int)bounds.getBounds().getCenterX();
            testY += (int)bounds.getBounds().getCenterY();
        }
        else if (align == LabelLogic.LEFT)
        {
            testX += (int)bounds.getBounds().getX();
            testY += (int)bounds.getBounds().getCenterY();
        }
        else if (align == LabelLogic.RIGHT)
        {
            testX += (int)(bounds.getBounds().getX() + bounds.getBounds().getWidth());
            testY += (int)bounds.getBounds().getCenterY();
        }
        if (LabelLogic.tryWithin(bounds, testX, testY, width, height))
            return new int[] { testX, testY };
        for (int i = 1; i <= 8; i++)
        {
            int d = ShipPlanImageLogic.mData.ss()*i/2;
            if (LabelLogic.tryWithin(bounds, testX-d, testY, width, height))
                return new int[] { testX-d, testY };
            if (LabelLogic.tryWithin(bounds, testX+d, testY, width, height))
                return new int[] { testX+d, testY };
            if (LabelLogic.tryWithin(bounds, testX, testY-d, width, height))
                return new int[] { testX, testY-d };
            if (LabelLogic.tryWithin(bounds, testX, testY+d, width, height))
                return new int[] { testX, testY+d };            
        }
        ShipPlanImageLogic.mData.triedWithin = new Rectangle2D.Double(testX, testY, width, height);
        return new int[] { testX, testY };
    }

    static boolean tryWithin(Shape bounds, int testX, int testY, int width, int height)
    {
        Rectangle2D r = new Rectangle2D.Double(testX, testY, width, height);
        ShipPlanImageLogic.mData.triedWithin = r;
        return bounds.contains(r);
    }

}

