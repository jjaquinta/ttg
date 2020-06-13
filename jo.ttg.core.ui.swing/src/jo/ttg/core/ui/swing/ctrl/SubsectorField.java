/*
 * Created on Jan 6, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.ImageLogic;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SubsectorField extends JComponent
{
    // private static final int SEC_W = 32*3;
    // private static final int SEC_H = 40*3;
    private static final int        SUB_W = 8 * 3;
    private static final int        SUB_H = 10 * 3;

    private IGenScheme              mScheme;
    private List<TTGActionListener> mTTGActionListeners;
    private String                  mHoverURI;

    private OrdBean                 mSelected;
    private OrdBean                 mOrigin;

    public SubsectorField(IGenScheme scheme)
    {
        mScheme = scheme;
        mOrigin = new OrdBean();
        mTTGActionListeners = new ArrayList<>();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    private int mMouseDownX;
    private int mMouseDownY;

    protected void doProcessMouseEvent(MouseEvent e)
    {
        OrdBean o = calcSubSource(e.getX(), e.getY());
        SubSectorBean sub = mScheme.getGeneratorSubSector()
                .generateSubSector(o);
        switch (e.getID())
        {
            case MouseEvent.MOUSE_PRESSED:
                mMouseDownX = e.getX();
                mMouseDownY = e.getY();
                break;
            case MouseEvent.MOUSE_CLICKED:
                if ((e.getClickCount() == 1) && (o != null))
                {
                    if (!o.equals(getSelected()))
                    {
                        setSelected(o);
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.SELECTED, sub.getURI(), sub));
                    }
                }
                else if ((e.getClickCount() == 2) && (sub != null))
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.ACTIVATED, sub.getURI(), sub));
                    else
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.DEACTIVATED, sub.getURI(), sub));
                }
                break;
            case MouseEvent.MOUSE_DRAGGED:
                int dx = mMouseDownX - e.getX();
                int dy = mMouseDownY - e.getY();
                int sx = dx / SUB_W;
                int sy = dy / SUB_H;
                if (sx != 0)
                {
                    mOrigin.setX(mOrigin.getX() + sx * 8);
                    mMouseDownX -= sx * SUB_W;
                    repaint();
                }
                if (sy != 0)
                {
                    mOrigin.setY(mOrigin.getY() + sy * 10);
                    mMouseDownY -= sy * SUB_H;
                    repaint();
                }
                break;
            case MouseEvent.MOUSE_MOVED:
                if (sub != null)
                {
                    String uri = sub.getURI();
                    if (!uri.equals(mHoverURI))
                    {
                        mHoverURI = uri;
                        SectorBean sec = mScheme.getGeneratorSector()
                                .generateSector(sub.getUpperBound());
                        setToolTipText(sec.getName() + " / " + sub.getName());
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.HOVER, sub.getURI(), sub));
                    }
                }
                else
                    mHoverURI = null;
                break;
            // case MouseEvent.MOUSE_EXITED:
            // break;
        }
    }

    /**
     * @param i
     * @param j
     * @return
     */
    private OrdBean calcSubSource(int i, int j)
    {
        long x = mOrigin.getX() + (i / SUB_W) * 8;
        long y = mOrigin.getY() + (j / SUB_H) * 10;
        return new OrdBean(x, y, mOrigin.getZ());
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

    private Dimension getSubSize()
    {
        Dimension size = getSize();
        size.width = size.width / (SUB_W) + 1;
        size.height = size.height / (SUB_H) + 1;
        return size;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        Dimension size = getSubSize();
        Rectangle clip = g.getClipBounds();
        for (int x = 0; x < size.width; x++)
            for (int y = 0; y < size.height; y++)
            {
                Rectangle bounds = new Rectangle(x * SUB_W, y * SUB_H, SUB_W,
                        SUB_H);
                if (bounds.intersects(clip))
                    paintSubsector(g, x, y, bounds);
            }
        for (int x = 0; x < size.width; x++)
        {
            long ox = mOrigin.getX() + x * 8;
            if (ox % 32 == 0)
                g.setColor(Color.LIGHT_GRAY);
            else
                g.setColor(Color.GRAY);
            g.drawLine(x * SUB_W, clip.y, x * SUB_W, clip.y + clip.height);
        }
        for (int y = 0; y < size.height; y++)
        {
            long oy = mOrigin.getY() + y * 10;
            if (oy % 40 == 0)
                g.setColor(Color.LIGHT_GRAY);
            else
                g.setColor(Color.GRAY);
            g.drawLine(clip.x, y * SUB_H, clip.x + clip.width, y * SUB_H);
        }
        if (mSelected != null)
        {
            g.setColor(Color.WHITE);
            g.drawRect((int)(mSelected.getX() - mOrigin.getX()) * 3,
                    (int)(mSelected.getY() - mOrigin.getY()) * 3, SUB_W, SUB_H);
        }
    }

    private void paintSubsector(Graphics g, int x, int y, Rectangle subBounds)
    {
        OrdBean o = new OrdBean(mOrigin.getX() + x * 8, mOrigin.getY() + y * 10,
                mOrigin.getZ());
        SubSectorBean sub = mScheme.getGeneratorSubSector()
                .generateSubSector(o);
        Image i = ImageLogic.makeImage(sub);
        g.drawImage(i, subBounds.x, subBounds.y, null);
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

    /**
     * @return
     */
    public OrdBean getSelected()
    {
        return mSelected;
    }

    /**
     * @param bean
     */
    public void setSelected(OrdBean bean)
    {
        mSelected = bean;
        repaint();
    }

    /**
     * @return
     */
    public OrdBean getOrigin()
    {
        return mOrigin;
    }

    /**
     * @param bean
     */
    public void setOrigin(OrdBean bean)
    {
        mOrigin = bean;
        repaint();
    }

}
