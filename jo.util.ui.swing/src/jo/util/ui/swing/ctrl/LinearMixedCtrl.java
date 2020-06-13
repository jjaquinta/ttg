package jo.util.ui.swing.ctrl;
/*
 * Created on Dec 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.image.ImageObserver;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LinearMixedCtrl extends JComponent
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2677564077070130439L;
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    
    private int         mOrientation;
    private Object[]    mItems;
    private int         mIntraItemGap;
    private Dimension   mPreferredSize;
    private boolean     mDrawBorder;
    
    public LinearMixedCtrl()
    {
        mOrientation = HORIZONTAL;
        mItems = new Object[0];
        mIntraItemGap = 4;
        mPreferredSize = new Dimension(32*8, 32);
        mDrawBorder = true;
    }
    
    public Dimension getPreferredSize()
    {
        return mPreferredSize;
    }
    
    public Dimension getMinimumSize()
    {
        return getPreferredSize();
    }
    
    public void paint(Graphics g)
    {
        Rectangle r = new Rectangle();
        Dimension s = getSize();
        r.x = 0;
        r.y = 0;
        r.width = s.width;
        r.height = s.height;
        Point p = paint(g, mItems, r, mOrientation, mDrawBorder, mIntraItemGap, getForeground(), getBackground(), getFont());
        if (mOrientation == HORIZONTAL)
            mPreferredSize.width = p.x + mIntraItemGap*3;
        else
            mPreferredSize.height = p.y + mIntraItemGap*3;
        setSize(mPreferredSize);
    }
    
    public static Point paint(Graphics g, Object[] items, Rectangle r, int orientation, boolean drawBox, int intraItemSkip, Color fg, Color bg, Font f)
    {
        Point p = new Point(0, r.y + r.height/2);
        g.setColor(bg);
        g.fillRect(r.x, r.y, r.width, r.height);
        if (drawBox)
        {
            g.setColor(fg);
            g.drawRect(r.x, r.y, r.width, r.height);
        }
        for (int i = 0; i < items.length; i++)
        {
            Dimension size = null;
            if (items[i] instanceof ImageIcon)
                size = drawImage(g, p, (ImageIcon)items[i]);
            else if (items[i] instanceof String)
                size = drawString(g, p, orientation, r.width, r.height, (String)items[i], fg, f);
            else if (items[i] instanceof Color)
            {
                fg = (Color)items[i];
                size = null;
            }
            else if (items[i] instanceof Dimension)
            {
                Dimension d = (Dimension)items[i];
                if (orientation == HORIZONTAL)
                    p.x = d.width;
                else
                    p.y = d.height;
            }
            if (size != null)
                if (orientation == HORIZONTAL)
                    p.x += size.width + intraItemSkip;
                else
                    p.y += size.height + intraItemSkip;
        }
        return p;
    }
    
    private static Dimension drawString(Graphics g, Point p, int orientation, int width, int height, String s, Color c, Font f)
    {
        g.setFont(f);
        g.setColor(c);
        FontMetrics fm = g.getFontMetrics(f);
        Rectangle2D box = f.getStringBounds(s, ((Graphics2D)g).getFontRenderContext());
        if (orientation == HORIZONTAL)
        {
            int y = (int)((height - box.getHeight())/2 + fm.getAscent());
            g.drawString(s, p.x, y);
        }
        else
            g.drawString(s, (int)(p.x + width/2 - box.getWidth()/2), (int)(p.y + fm.getAscent()));
        return new Dimension((int)box.getWidth(), (int)box.getHeight());
    }
    
    private static Dimension drawImage(Graphics g, Point p, ImageIcon i)
    {
        g.drawImage(i.getImage(), p.x, p.y - i.getIconHeight()/2, (ImageObserver)null);
        return new Dimension(i.getIconHeight(), i.getIconWidth());
    }
    
    public int getOrientation()
    {
        return mOrientation;
    }
    public void setOrientation(int orientation)
    {
        mOrientation = orientation;
    }
    public int getIntraItemGap()
    {
        return mIntraItemGap;
    }
    public void setIntraItemGap(int intraItemGap)
    {
        mIntraItemGap = intraItemGap;
    }
    public Object[] getItems()
    {
        return mItems;
    }
    public void setItems(Object[] items)
    {
        mItems = items;
    }
    public boolean isDrawBorder()
    {
        return mDrawBorder;
    }
    public void setDrawBorder(boolean drawBorder)
    {
        mDrawBorder = drawBorder;
    }
}
