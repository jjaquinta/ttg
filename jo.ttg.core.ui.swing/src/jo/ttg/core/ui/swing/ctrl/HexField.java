/*
 * Created on Nov 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.AWTEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.mw.MainWorldLogic;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HexField extends HexPanel
{
    private int                     mRadius;

    // derived
    private OrdBean                 mCenter;
    private List<TTGActionListener> mTTGActionListeners;
    private String                  mHoverURI;

    public HexField(IGenScheme scheme)
    {
        super(scheme);
        mTTGActionListeners = new ArrayList<>();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public HexField()
    {
        super();
        mTTGActionListeners = new ArrayList<>();
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    public int calcMode(OrdBean o)
    {
        if ((mCenter == null) || (mRadius <= 0))
            return M_NORM;
        if (OrdLogic.within(o, mCenter, mRadius))
            return M_NORM;
        else
            return M_DISABLED;
    }

    protected void doProcessMouseEvent(MouseEvent e)
    {
        OrdBean o = calcHexSource(e.getX(), e.getY());
        if (o == null)
            return;
        MainWorldBean mw = calcWorld(o);
        switch (e.getID())
        {
            case MouseEvent.MOUSE_CLICKED:
                if ((e.getClickCount() == 1)
                        && ((mw != null) || !isCanSelectEmpty()))
                {
                    if (!o.equals(getFocus()) && (calcMode(o) == M_NORM))
                    {
                        setFocus(o);
                        if (mw == null)
                            fireTTGActionEvent(new TTGActionEvent(this,
                                    TTGActionEvent.SELECTED, "ord://" + o, o));
                        else
                            fireTTGActionEvent(new TTGActionEvent(this,
                                    TTGActionEvent.SELECTED, mw.getURI(), mw));
                    }
                }
                else if (e.getClickCount() == 2)
                {
                    // System.out.println("Button="+e.getButton()+",
                    // 1="+MouseEvent.BUTTON1+", 2="+MouseEvent.BUTTON2);
                    if (e.getButton() == MouseEvent.BUTTON1 && (mw != null))
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.ACTIVATED, mw.getURI(), mw));
                    else if (e.getButton() != MouseEvent.BUTTON1)
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.DEACTIVATED, mw.getURI(), mw));
                }
                break;
            // case MouseEvent.MOUSE_DRAGGED:
            // break;
            case MouseEvent.MOUSE_MOVED:
                if (mw != null)
                {
                    String uri = mw.getURI();
                    if (!uri.equals(mHoverURI))
                    {
                        mHoverURI = uri;
                        setToolTipText(MainWorldLogic.getExportLine(mw));
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.HOVER, mw.getURI(), mw));
                    }
                }
                else
                    mHoverURI = null;
                break;
            // case MouseEvent.MOUSE_EXITED:
            // break;
        }
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

    /**
     * @param i
     */
    public void setHexesHigh(int i)
    {
        super.setHexesHigh(i);
        newCenter();
    }

    /**
     * @param i
     */
    public void setHexesWide(int i)
    {
        super.setHexesWide(i);
        newCenter();
    }

    private void newCenter()
    {
        mCenter = new OrdBean(getOrigin().getX() + getHexesWide() / 2,
                getOrigin().getY() + getHexesHigh() / 2, getOrigin().getZ());
    }

    /**
     * @return
     */
    public int getRadius()
    {
        return mRadius;
    }

    /**
     * @param i
     */
    public void setRadius(int i)
    {
        mRadius = i;
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
