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
import jo.ttg.logic.gen.ImageLogic;
import jo.ttg.logic.sec.SectorLogic;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SectorField extends JComponent
{
    private static final int SEC_O_W = 32;
    private static final int SEC_O_H = 40;
    private static final int SEC_W = SEC_O_W*3;
    private static final int SEC_H = SEC_O_H*3;

    private List<TTGActionListener> mTTGActionListeners;
    private String                  mHoverURI;

    private OrdBean                 mSelected;
    private OrdBean                 mOrigin;

    public SectorField()
    {
        mOrigin = new OrdBean();
        mTTGActionListeners = new ArrayList<>();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    private int mMouseDownX;
    private int mMouseDownY;

    protected void doProcessMouseEvent(MouseEvent e)
    {
        OrdBean o = calcSecSource(e.getX(), e.getY());
        OrdBean fine = calcFineSource(e.getX(), e.getY());
        SectorBean sec = SectorLogic.getFromOrds(o);
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
                        if (sec != null)
                            fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.SELECTED, sec.getURI(), sec, fine));
                    }
                }
                else if ((e.getClickCount() == 2) && (sec != null))
                {
                    if (sec != null)
                        if (e.getButton() == MouseEvent.BUTTON1)
                            fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.ACTIVATED, sec.getURI(), sec, fine));
                        else
                            fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.DEACTIVATED, sec.getURI(), sec, fine));
                }
                break;
            case MouseEvent.MOUSE_DRAGGED:
                int dx = mMouseDownX - e.getX();
                int dy = mMouseDownY - e.getY();
                int sx = dx / SEC_W;
                int sy = dy / SEC_H;
                if (sx != 0)
                {
                    mOrigin.setX(mOrigin.getX() + sx * SEC_O_W);
                    mMouseDownX -= sx * SEC_W;
                    repaint();
                }
                if (sy != 0)
                {
                    mOrigin.setY(mOrigin.getY() + sy * SEC_O_H);
                    mMouseDownY -= sy * SEC_H;
                    repaint();
                }
                break;
            case MouseEvent.MOUSE_MOVED:
                if (sec != null)
                {
                    String uri = sec.getURI();
                    if (!uri.equals(mHoverURI))
                    {
                        mHoverURI = uri;
                        setToolTipText(sec.getName());
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.HOVER, sec.getURI(), sec, fine));
                    }
                }
                else
                    mHoverURI = null;
                break;
            // case MouseEvent.MOUSE_EXITED:
            // break;
        }
    }

    private OrdBean calcSecSource(int i, int j)
    {
        long x = mOrigin.getX() + (i / SEC_W) * SEC_O_W;
        long y = mOrigin.getY() + (j / SEC_H) * SEC_O_H;
        return new OrdBean(x, y, mOrigin.getZ());
    }

    private OrdBean calcFineSource(int i, int j)
    {
        long x = mOrigin.getX() + (i / (SEC_W/SEC_O_W));
        long y = mOrigin.getY() + (j / (SEC_H/SEC_O_H));
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

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        Dimension size = getSize();
        Rectangle clip = g.getClipBounds();
        for (int secx = 0; secx*SEC_W < size.width; secx++)
            for (int secy = 0; secy*SEC_H < size.height; secy++)
            {
                Rectangle bounds = new Rectangle(secx*SEC_W, secy*SEC_H, SEC_W,
                        SEC_H);
                if (bounds.intersects(clip))
                    paintSector(g, secx, secy, bounds);
            }
        if (mSelected != null)
        {
            g.setColor(Color.WHITE);
            g.drawRect((int)(mSelected.getX() - mOrigin.getX()) * SEC_W,
                    (int)(mSelected.getY() - mOrigin.getY()) * SEC_H, SEC_W, SEC_H);
        }
    }

    private void paintSector(Graphics g, int secx, int secy, Rectangle secBounds)
    {
        OrdBean o = new OrdBean(mOrigin.getX() + secx * SEC_O_W, mOrigin.getY() + secy * SEC_O_H,
                mOrigin.getZ());
        SectorBean sec = SectorLogic.getFromOrds(o);
        Image i = ImageLogic.makeImage(sec);
        g.drawImage(i, secBounds.x, secBounds.y, null);
        g.setColor(Color.GRAY);
        g.drawRect(secBounds.x, secBounds.y, secBounds.width, secBounds.height);
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
