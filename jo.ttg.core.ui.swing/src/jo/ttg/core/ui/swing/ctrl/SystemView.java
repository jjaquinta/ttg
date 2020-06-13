/*
 * Created on Dec 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.ImageObserver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JComponent;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.BodyLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.ui.swing.utils.MouseUtils;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SystemView extends JComponent
{
    private static final Color                    GRID      = ColorUtils.DarkGreen;
    private static final Color                    SELECT    = ColorUtils.LightCyan;
    private static final int                      ICON_SIZE = 24;

    private List<TTGActionListener>  mTTGActionListeners = new ArrayList<TTGActionListener>();
    
    private IGenScheme               mScheme;
    private OrdBean                  mOrigin;
    private SystemBean               mSystem;
    private BodyBean                 mFocus;
    private BodyBean                 mCursor;
    private DateBean                 mDate     = new DateBean();
    private Map<BodyBean, LocBean>   mLocations;
    protected Map<BodyBean, Rectangle> mOnScreen = new HashMap<BodyBean, Rectangle>();
    private boolean                  mLocationsDirty;

    public SystemView()
    {
        mLocations = new HashMap<BodyBean, LocBean>();
        mLocationsDirty = true;
        MouseUtils.mouseClicked(this, (ev) -> doMouseClicked(ev));
        MouseUtils.mouseMoved(this, (ev) -> doMouseMoved(ev));
    }
    
    // listeners

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
        Object[] l = mTTGActionListeners.toArray();
        for (int i = 0; i < l.length; i++)
            ((TTGActionListener)l[i]).actionPerformed(ev);
    }

    // mouse management
    
    private BodyBean findBody(int x, int y)
    {
        for (BodyBean b : mOnScreen.keySet())
            if (mOnScreen.get(b).contains(x, y))
                return b;
        return null;
    }
    
    private void doMouseClicked(MouseEvent ev)
    {
        if (ev.getClickCount() == 1)
        {
            if (ev.getButton() == MouseEvent.BUTTON1)
            {
                BodyBean b = findBody(ev.getX(), ev.getY());
                if (b != null)
                {
                    setSelected(b);
                    fireTTGActionEvent(new TTGActionEvent(b, TTGActionEvent.SELECTED, b.getURI()));
                }
            }
        }
        else if (ev.getClickCount() == 2)
        {
            if (ev.getButton() == MouseEvent.BUTTON1)
            {
                BodyBean b = findBody(ev.getX(), ev.getY());
                if (b != null)
                {
                    setFocus(b);
                    fireTTGActionEvent(new TTGActionEvent(b, TTGActionEvent.ACTIVATED, b.getURI()));
                }
            }
            else if (ev.getButton() == MouseEvent.BUTTON3)
            {
                    if (mFocus.getPrimary() != null)
                    {
                        BodyBean fp = mFocus.getPrimary();
                        setFocus(fp);
                        fireTTGActionEvent(new TTGActionEvent(fp, TTGActionEvent.ACTIVATED, fp.getURI()));
                    }
                    else
                        fireTTGActionEvent(new TTGActionEvent(mSystem, TTGActionEvent.DEACTIVATED, mSystem.getURI()));
            }
        }
    }
    
    private void doMouseMoved(MouseEvent ev)
    {
        BodyBean b = findBody(ev.getX(), ev.getY());
        if (b != null)
            setToolTipText(b.getOneLineDesc());
    }
    
    
    // utilities
    
    private synchronized void recalcLocations()
    {
        if (!mLocationsDirty)
            return;
        mLocations.clear();
        for (Iterator<BodyBean> i = mSystem.getSystemRoot()
                .getAllSatelitesIterator(); i.hasNext();)
        {
            BodyBean b = i.next();
            LocBean loc = BodyLogic.getLocation(b, mDate);
            mLocations.put(b, loc);
        }
        mLocationsDirty = false;
    }

    public void paint(Graphics g)
    {
        recalcLocations();
        Dimension size = getSize();
        paintBackground(g, size);
        g.translate(size.width/2, size.height/2);
        int screenRadius = Math.min(size.width/2, size.height/2) - ICON_SIZE;
        double groupRadius = mFocus.isAnySatelites() ? mFocus.getLastSatelite().getOrbitalRadius() : mFocus.getOrbitalRadius();
        double scale = screenRadius/groupRadius;
        mOnScreen.clear();
        Graphics2D g2 = (Graphics2D)g;
        int pr = (int)(mFocus.getOrbitalRadius()*scale);
        paintBody(g2, 0, 0, mFocus, -pr, 0, pr, false, size.width/2, size.height/2);
        int r = 0;
        for (BodyBean b : mFocus.getSatelites())
        {
            int br = (int)(b.getOrbitalRadius()*scale);
            if (br < r + ICON_SIZE)
                br = r + ICON_SIZE;
            double a = BodyLogic.getOrbitalRadians(b, mDate);
            int x = (int)(br*Math.sin(a));
            int y = (int)(br*Math.cos(a));
            paintBody(g2, x, y, b, 0, 0, br, true, size.width/2, size.height/2);
            r = br;
        }
    }

    protected void paintBackground(Graphics g, Dimension size)
    {
        g.setColor(getBackground());
        g.fillRect(0, 0, size.width, size.height);
    }

    protected void paintBody(Graphics2D g2, int bx, int by, BodyBean b, int px, int py, int pr, boolean drawChildren, int dx, int dy)
    {
        if (pr > 0)
        {
            g2.setColor(GRID);
            g2.drawOval(px - pr, py - pr, pr * 2, pr * 2);
        }
        ImageIcon i = BodyView.getIcon(b);
        Rectangle onScreen = new Rectangle(bx - i.getIconWidth()/2, by - i.getIconHeight()/2, i.getIconWidth(), i.getIconHeight());
        g2.drawImage(i.getImage(), onScreen.x, onScreen.y, (ImageObserver)null);
        if (drawChildren && b.isAnySatelites())
        {
            g2.setColor(GRID);
            g2.drawOval(onScreen.x - 2, onScreen.y - 2, onScreen.width + 4, onScreen.height + 4);
            if (b.getSatelites().length > 1)
                g2.drawOval(onScreen.x - 4, onScreen.y - 4, onScreen.width + 8, onScreen.height + 8);
        }
        if (b == mCursor)
        {
            g2.setColor(SELECT);
            g2.drawRect(onScreen.x, onScreen.y, onScreen.width, onScreen.height);
        }
        onScreen.translate(dx, dy);
        mOnScreen.put(b, onScreen);
    }

    // getters and setters

    public OrdBean getOrigin()
    {
        return mOrigin;
    }

    public void setOrigin(OrdBean origin)
    {
        mOrigin = origin;
        setSystem(SystemLogic.getFromOrds(mScheme, mOrigin));
    }

    public IGenScheme getScheme()
    {
        return mScheme;
    }

    public void setScheme(IGenScheme scheme)
    {
        mScheme = scheme;
    }

    public SystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SystemBean system)
    {
        mSystem = system;
        mLocationsDirty = true;
        if (system != null)
            setFocus(SystemLogic.findMainworld(mSystem));
    }

    public BodyBean getFocus()
    {
        return mFocus;
    }

    public void setFocus(BodyBean focus)
    {
        mFocus = focus;
        repaint();
    }

    public DateBean getDate()
    {
        return mDate;
    }

    public void setDate(DateBean date)
    {
        mDate = date;
        mLocationsDirty = true;
        repaint();
    }
    
    public BodyBean getSelected()
    {
        return mCursor;
    }

    public void setSelected(BodyBean cursor)
    {
        mCursor = cursor;
        repaint();
    }
}
