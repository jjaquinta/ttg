/*
 * Created on Feb 6, 2005
 *
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import jo.ttg.beans.LocBean;

/**
 * @author Jo
 *
 */
public class BogeyPanel extends JComponent
{
    public static final int         S_DOT    = 0x01;
    public static final int         S_PLUS   = 0x02;
    public static final int         S_X      = 0x04;
    public static final int         S_CIRCLE = 0x10;
    public static final int         S_BOX    = 0x20;
    public static final int         S_CONE   = 0x010000;

    private List<Bogey>             mBogies;
    private List<BogeyLink>         mLinks;
    private Map<Bogey, Point>       mBogieLocation;
    private List<Bogey>             mSelectable;
    private LocBean                 mUpperLeft;
    private LocBean                 mLowerRight;
    private double                  mXoffset;
    private double                  mYoffset;
    private double                  mScale;
    private boolean                 mScaleUpdated;
    private List<TTGActionListener> mTTGActionListeners;

    private Color                   mGridColor;

    public BogeyPanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mBogies = new ArrayList<>();
        mLinks = new ArrayList<>();
        mSelectable = new ArrayList<>();
        mUpperLeft = new LocBean();
        mLowerRight = new LocBean();
        mBogieLocation = new HashMap<>();
        mTTGActionListeners = new ArrayList<>();

        mGridColor = new Color(0, 0, 128);
        setBackground(Color.BLACK);
    }

    private void initLayout()
    {
        mScaleUpdated = false;
    }

    private void initLink()
    {
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);

        addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent ev)
            {
                mScaleUpdated = false;
            }
        });
    }

    protected void doProcessMouseEvent(MouseEvent e)
    {
        Bogey b;
        switch (e.getID())
        {
            case MouseEvent.MOUSE_CLICKED:
                b = findBogey(e.getPoint(), true);
                if (b == null)
                    return;
                if (e.getClickCount() == 1)
                {
                    fireTTGActionEvent(new TTGActionEvent(this,
                            TTGActionEvent.SELECTED, "", b));
                }
                else if (e.getClickCount() == 2)
                {
                    fireTTGActionEvent(new TTGActionEvent(this,
                            TTGActionEvent.ACTIVATED, "", b));
                }
                break;
            // case MouseEvent.MOUSE_DRAGGED:
            // break;
            case MouseEvent.MOUSE_MOVED:
                b = findBogey(e.getPoint(), false);
                if (b == null)
                {
                    setToolTipText(null);
                    return;
                }
                fireTTGActionEvent(
                        new TTGActionEvent(this, TTGActionEvent.HOVER, "", b));
                setToolTipText(b.getText());
                break;
            // case MouseEvent.MOUSE_EXITED:
            // break;
        }
    }

    public void update()
    {
        mScaleUpdated = false;
    }

    private void doUpdate()
    {
        if (mScaleUpdated)
            return;
        updateExtents();
        updateScale();
        updateLocations();
        mScaleUpdated = true;
    }

    private Point convert(LocBean loc)
    {
        Point ret = new Point();
        ret.x = (int)((loc.getX() - mXoffset) * mScale);
        ret.y = (int)((loc.getY() - mYoffset) * mScale);
        return ret;
    }

    /**
     * 
     */
    private void updateScale()
    {
        Dimension size = getSize();
        double xScale = size.width / (mLowerRight.getX() - mUpperLeft.getX());
        double yScale = size.height / (mLowerRight.getY() - mUpperLeft.getY());
        if (xScale < yScale)
            mScale = xScale;
        else
            mScale = yScale;
        mXoffset = (mLowerRight.getX() + mUpperLeft.getX()) / 2
                - size.width / mScale / 2.0;
        mYoffset = (mLowerRight.getY() + mUpperLeft.getY()) / 2
                - size.height / mScale / 2.0;
    }

    private void updateExtents()
    {
        boolean first = true;
        for (Iterator<Bogey> i = mBogies.iterator(); i.hasNext();)
        {
            Bogey b = i.next();
            LocBean l = b.getLocation();
            if (first)
            {
                mUpperLeft.set(l);
                mLowerRight.set(l);
                first = false;
            }
            else
            {
                if (l.getX() < mUpperLeft.getX())
                    mUpperLeft.setX(l.getX());
                else if (l.getX() > mLowerRight.getX())
                    mLowerRight.setX(l.getX());
                if (l.getY() < mUpperLeft.getY())
                    mUpperLeft.setY(l.getY());
                else if (l.getY() > mLowerRight.getY())
                    mLowerRight.setY(l.getY());
                if (l.getZ() < mUpperLeft.getZ())
                    mUpperLeft.setZ(l.getZ());
                else if (l.getZ() > mLowerRight.getZ())
                    mLowerRight.setZ(l.getZ());
            }
        }
        if (first)
        { // no bogies
            mUpperLeft.setX(-10);
            mUpperLeft.setY(-10);
            mLowerRight.setX(10);
            mLowerRight.setY(10);
        }
        double dx = (mLowerRight.getX() - mUpperLeft.getX()) / 10.0;
        if (dx < 1)
            dx = 1;
        mUpperLeft.setX(mUpperLeft.getX() - dx);
        mLowerRight.setX(mLowerRight.getX() + dx);
        double dy = (mLowerRight.getY() - mUpperLeft.getY()) / 10.0;
        if (dy < 1)
            dy = 1;
        mUpperLeft.setY(mUpperLeft.getY() - dy);
        mLowerRight.setY(mLowerRight.getY() + dy);
        double dz = (mLowerRight.getZ() - mUpperLeft.getZ()) / 10.0;
        if (dz < 1)
            dz = 1;
        mUpperLeft.setZ(mUpperLeft.getZ() - dz);
        mLowerRight.setZ(mUpperLeft.getZ() + dz);
    }

    private void updateLocations()
    {
        Bogey[] bs = new Bogey[mBogies.size()];
        mBogies.toArray(bs);
        Point[] ps = new Point[bs.length];

        // calculate points
        for (int i = 0; i < bs.length; i++)
            ps[i] = convert(bs[i].getLocation());
        // look for collisions
        List<Integer> group = new ArrayList<>();
        for (int i = 0; i < bs.length - 1; i++)
        {
            group.clear();
            group.add(new Integer(i));
            for (int j = i + 1; j < bs.length; j++)
                if (dist(ps[i], ps[j]) <= 6)
                    group.add(new Integer(j));
            if (group.size() > 1)
            { // split up
                double delta = Math.PI * 2 / group.size();
                double dTheta = 0;
                for (Iterator<Integer> k = group.iterator(); k
                        .hasNext(); dTheta += delta)
                {
                    int off = k.next().intValue();
                    ps[off].x += (int)(6.0 * Math.sin(dTheta));
                    ps[off].y += (int)(6.0 * Math.cos(dTheta));
                }
            }
        }
        // record points
        for (int i = 0; i < bs.length; i++)
            mBogieLocation.put(bs[i], ps[i]);
    }

    public void addBogey(Bogey b)
    {
        mBogies.add(b);
        mScaleUpdated = false;
    }

    public void removeBogey(Bogey b)
    {
        mBogies.remove(b);
        mScaleUpdated = false;
    }

    public void addLink(BogeyLink b)
    {
        mLinks.add(b);
        repaint();
    }

    public void removeLink(BogeyLink b)
    {
        mLinks.remove(b);
        repaint();
    }

    /**
     * @return Returns the selectable.
     */
    public List<Bogey> getSelectable()
    {
        return mSelectable;
    }

    /**
     * @param selectable
     *            The selectable to set.
     */
    public void setSelectable(List<Bogey> selectable)
    {
        mSelectable = selectable;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        doUpdate();
        paintGrid(g);
        paintBogeys(g);
        paintLinks(g);
    }

    private void paintGrid(Graphics g)
    {
        double step;
        if (mScale >= 10.0)
            step = 1;
        else if (mScale >= 1.0)
            step = 10.0;
        else
            step = 100.0;
        Dimension size = getSize();
        g.setColor(mGridColor);
        // origin
        g.drawLine(0, size.height / 2, size.width, size.height / 2);
        g.drawLine(size.width / 2, 0, size.width / 2, size.height);
        for (double dx = step; dx * mScale < size.width / 2; dx += step)
        {
            int ox = (int)(dx * mScale);
            g.drawLine(size.width / 2 + ox, 0, size.width / 2 + ox,
                    size.height);
            g.drawLine(size.width / 2 - ox, 0, size.width / 2 - ox,
                    size.height);
        }
        for (double dy = step; dy * mScale < size.height / 2; dy += step)
        {
            int oy = (int)(dy * mScale);
            g.drawLine(0, size.height / 2 + oy, size.width,
                    size.height / 2 + oy);
            g.drawLine(0, size.height / 2 - oy, size.width,
                    size.height / 2 - oy);
        }
    }

    private void paintBogeys(Graphics g)
    {
        for (Iterator<Bogey> i = mBogies.iterator(); i.hasNext();)
            paintBogey(g, i.next());
    }

    private void paintBogey(Graphics g, Bogey b)
    {
        Point p = mBogieLocation.get(b);
        Color c;
        if ((mSelectable.size() > 0) && !mSelectable.contains(b))
            c = b.getColor().darker().darker();
        else
            c = b.getColor();
        int sprite = b.getSprite();
        paintSprite(g, c, p, sprite);
    }

    private void paintLinks(Graphics g)
    {
        for (Iterator<BogeyLink> i = mLinks.iterator(); i.hasNext();)
            paintLink(g, i.next());
    }

    private void paintLink(Graphics g, BogeyLink l)
    {
        Point source = mBogieLocation.get(l.getSource());
        Point target = mBogieLocation.get(l.getTarget());
        int sprite = l.getSprite();
        if ((sprite & 0xff) != 0)
            paintSprite(g, l.getColor(), source, sprite & 0xff);
        if (((sprite >> 8) & 0xff) != 0)
            paintSprite(g, l.getColor(), target, (sprite >> 8) & 0xff);
        if ((sprite & S_CONE) != 0)
        {
            Point mid = new Point((source.x + target.x) / 2,
                    (source.y + target.y) / 2);
            g.setColor(l.getColor().darker().darker());
            g.drawLine(source.x, source.y, mid.x - 1, mid.y + 2);
            g.drawLine(source.x, source.y, mid.x + 2, mid.y + 1);
            g.drawLine(source.x, source.y, mid.x + 1, mid.y - 2);
            g.drawLine(source.x, source.y, mid.x - 2, mid.y - 1);
            g.setColor(l.getColor().darker());
            g.drawLine(mid.x - 1, mid.y + 2, target.x - 2, target.y + 4);
            g.drawLine(mid.x + 2, mid.y + 1, target.x + 4, target.y + 2);
            g.drawLine(mid.x + 1, mid.y - 2, target.x + 2, target.y - 4);
            g.drawLine(mid.x - 2, mid.y - 1, target.x - 4, target.y - 2);
        }
    }

    private void paintSprite(Graphics g, Color c, Point p, int sprite)
    {
        g.setColor(c);
        if ((sprite & S_DOT) != 0)
        {
            g.fillOval(p.x - 2, p.y - 2, 5, 5);
        }
        if ((sprite & S_X) != 0)
        {
            g.drawLine(p.x - 4, p.y - 4, p.x + 4, p.y + 4);
            g.drawLine(p.x + 4, p.y - 4, p.x - 4, p.y + 4);
        }
        if ((sprite & S_PLUS) != 0)
        {
            g.drawLine(p.x - 6, p.y, p.x + 6, p.y);
            g.drawLine(p.x, p.y - 6, p.x, p.y + 6);
        }
        if ((sprite & S_BOX) != 0)
        {
            g.drawRect(p.x - 6, p.y - 6, 12, 12);
        }
        if ((sprite & S_CIRCLE) != 0)
        {
            g.drawOval(p.x - 6, p.y - 6, 12, 12);
        }
    }

    private Bogey findBogey(Point p, boolean selectableOnly)
    {
        for (Iterator<Bogey> i = mBogieLocation.keySet().iterator(); i
                .hasNext();)
        {
            Bogey b = i.next();
            Point bp = mBogieLocation.get(b);
            if (bp == null)
                continue;
            if ((dist(p, bp) <= 6)
                    && (selectableOnly || mSelectable.contains(b)))
                return b;
        }
        return null;
    }

    private int dist(Point p1, Point p2)
    {
        return Math.abs(p1.x - p2.x) + Math.abs(p1.y - p2.y);
    }

    protected void processMouseEvent(MouseEvent e)
    {
        super.processMouseEvent(e);
        doProcessMouseEvent(e);
    }

    protected void processMouseMotionEvent(MouseEvent e)
    {
        super.processMouseMotionEvent(e);
        doProcessMouseEvent(e);
    }

    public void addTTGActionListener(TTGActionListener l)
    {
        synchronized (mTTGActionListeners)
        {
            mTTGActionListeners.add(l);
        }
    }

    public void removeTTGActionListener(TTGActionListener l)
    {
        synchronized (mTTGActionListeners)
        {
            mTTGActionListeners.remove(l);
        }
    }

    protected void fireTTGActionEvent(TTGActionEvent ev)
    {
        for (TTGActionListener l : mTTGActionListeners)
            l.actionPerformed(ev);
    }
}
