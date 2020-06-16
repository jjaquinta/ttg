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
import jo.ttg.logic.gen.ImageLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.sec.SectorLogic;
import jo.ttg.logic.sub.SubSectorLogic;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SubSectorField extends JComponent
{
    private static final int        SUB_O_W = 8;
    private static final int        SUB_O_H = 10;
    private static final int        SUB_W   = SUB_O_W * 3;
    private static final int        SUB_H   = SUB_O_H * 3;

    private List<TTGActionListener> mTTGActionListeners;
    private String                  mHoverURI;

    private OrdBean                 mSelected;
    private OrdBean                 mOrigin;

    public SubSectorField()
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
        OrdBean o = calcSubSource(e.getX(), e.getY());
        OrdBean fine = calcFineSource(e.getX(), e.getY());
        SubSectorBean sub = SubSectorLogic.getFromOrds(o);
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
                        if (sub != null)
                            fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.SELECTED, sub.getURI(), sub,
                                fine));
                        else
                            fireTTGActionEvent(new TTGActionEvent(this,
                                    TTGActionEvent.SELECTED, o.getURI(), o,
                                    fine));
                    }
                }
                else if ((e.getClickCount() == 2) && (sub != null))
                {
                    if (e.getButton() == MouseEvent.BUTTON1)
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.ACTIVATED, sub.getURI(), sub,
                                fine));
                    else
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.DEACTIVATED, sub.getURI(), sub,
                                fine));
                }
                break;
            case MouseEvent.MOUSE_DRAGGED:
                int dx = mMouseDownX - e.getX();
                int dy = mMouseDownY - e.getY();
                int sx = dx / SUB_W;
                int sy = dy / SUB_H;
                if (sx != 0)
                {
                    mOrigin.setX(mOrigin.getX() + sx * SUB_O_W);
                    mMouseDownX -= sx * SUB_W;
                    repaint();
                }
                if (sy != 0)
                {
                    mOrigin.setY(mOrigin.getY() + sy * SUB_O_H);
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
                        SectorBean sec = SectorLogic
                                .getFromOrds(sub.getUpperBound());
                        setToolTipText(sec.getName() + " / " + sub.getName());
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.HOVER, sub.getURI(), sub, fine));
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
        long x = mOrigin.getX() + (i / SUB_W) * SUB_O_W;
        long y = mOrigin.getY() + (j / SUB_H) * SUB_O_H;
        return new OrdBean(x, y, mOrigin.getZ());
    }

    private OrdBean calcFineSource(int i, int j)
    {
        long x = mOrigin.getX() + (i / (SUB_W / SUB_O_W));
        long y = mOrigin.getY() + (j / (SUB_H / SUB_O_H));
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
        for (int subx = 0; subx*SUB_W < size.width; subx++)
            for (int suby = 0; suby*SUB_H < size.height; suby++)
            {
                Rectangle bounds = new Rectangle(subx * SUB_W, suby * SUB_H, SUB_W,
                        SUB_H);
                if (bounds.intersects(clip))
                    paintSubsector(g, subx, suby, bounds);
            }
        for (int subx = 0; subx*SUB_W < size.width; subx++)
        {
            long ox = mOrigin.getX() + subx * SUB_O_W;
            if (ox % 32 == 0)
                g.setColor(Color.LIGHT_GRAY);
            else
                g.setColor(Color.GRAY);
            g.drawLine(subx * SUB_W, clip.y, subx * SUB_W, clip.y + clip.height);
        }
        for (int suby = 0; suby*SUB_H < size.height; suby++)
        {
            long oy = mOrigin.getY() + suby * SUB_O_H;
            if (oy % 40 == 0)
                g.setColor(Color.LIGHT_GRAY);
            else
                g.setColor(Color.GRAY);
            g.drawLine(clip.x, suby * SUB_H, clip.x + clip.width, suby * SUB_H);
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
        OrdBean o = new OrdBean(mOrigin.getX() + x * SUB_O_W, mOrigin.getY() + y * SUB_O_H,
                mOrigin.getZ());
        SubSectorBean sub = SubSectorLogic.getFromOrds(o);
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
        mSelected = new OrdBean(bean);
        SchemeLogic.getDefaultScheme().nearestSub(mSelected);
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
        mOrigin = new OrdBean(bean);
        SchemeLogic.getDefaultScheme().nearestSub(mOrigin);
        repaint();
    }

}
