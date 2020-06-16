/*
 * Created on Nov 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComponent;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.ui.swing.ctrl.ComponentUtils;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.ui.swing.utils.MouseUtils;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class HexScroller extends JComponent implements IHexPanel
{
    private int                         mHexSide;
    private boolean                     mCanSelectEmpty;
    private OrdBean                     mCenter;
    private Point                       mCenterDelta = new Point();
    private OrdBean                     mFocus;
    private HexPanelPainter             mPainter;
    private Point                       mMouseDown;
    private List<TTGActionListener>     mTTGActionListeners = new ArrayList<>();
    private String                      mHoverURI;

    // derived
    private int                         mHexShortSide;
    private int                         mHexLongSide;
    private Map<OrdBean, MainWorldBean> mWorlds;
    private Map<OrdBean, Polygon>       mPolygons;
    private Color                       mForeColor;
    private Color                       mDisabledColor;
    private Color                       mFocusedColor;
    private Color                       mUnfocusedBackColor;
    private Color                       mDisabledBackColor;
    private Color                       mFocusedBackColor;

    public HexScroller()
    {
        mCenter = new OrdBean();
        if (getForeground() == null)
            setForeColor(ColorUtils.AliceBlue);
        mUnfocusedBackColor = new Color(32, 32, 32);
        mDisabledBackColor = Color.black;
        mFocusedBackColor = new Color(128, 128, 128);
        setHexSide(32);
        clearWorlds();
        clearPolygons();
        mPainter = new HexPanelPainter(this);
        mCanSelectEmpty = false;
        initLink();
    }
    
    private void initLink()
    {
        ComponentUtils.resized(this, (ev) -> refresh());
        MouseUtils.mouseWheelMoved(this, (ev) -> doMouseWheelMoved(ev));
        MouseUtils.mousePressed(this, (ev) -> doMousePressed(ev));
        MouseUtils.mouseDragged(this, (ev) -> doMouseDragged(ev));
        MouseUtils.mouseMoved(this, (ev) -> doMouseMoved(ev));
        MouseUtils.mouseClicked(this, (ev) -> doMouseClicked(ev));
    }

    /**
     * @return
     */
    public int getHexSide()
    {
        return mHexSide;
    }

    /**
     * @param i
     */
    public void setHexSide(int i)
    {
        mHexSide = i;
        mHexShortSide = mHexSide / 2;
        mHexLongSide = (mHexSide * 866) / 1000;
        clearPolygons();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.Component#getMinimumSize()
     */
    public Dimension getMinimumSize()
    {
        int w = (mHexShortSide + mHexSide) * 1 + mHexShortSide + 2;
        int h = mHexLongSide * (1 * 2 + 1) + 2;
        return new Dimension(w, h);
    }

    /**
     * @return
     */
    public OrdBean getCenter()
    {
        return mCenter;
    }

    /**
     * @param bean
     */
    public void setCenter(OrdBean bean)
    {
        mCenter = new OrdBean(bean);
        clearWorlds();
        repaint();
    }

    public void setCenterURI(String uri)
    {
        if (uri.startsWith("mw://"))
            setCenter(((MainWorldBean)(SchemeLogic.getFromURI(uri))).getOrds());
    }

    public void refresh()
    {
        clearWorlds();
        clearPolygons();
        repaint();
    }

    private void clearWorlds()
    {
        mWorlds = null;
    }

    /**
     * 
     */
    private void fillWorlds()
    {
        if (mWorlds != null)
            return;
        if (mForeColor == null)
        {
            mForeColor = getForeground();
            mFocusedColor = mForeColor.brighter().brighter().brighter();
            mDisabledColor = mForeColor.darker().darker().darker();
        }
        Dimension size = getSize();
        mWorlds = new HashMap<OrdBean, MainWorldBean>();
        int widthR = size.width/(mHexShortSide + mHexSide)/2 + 2; 
        int heightR = size.height/mHexLongSide/2/2 + 3;
        for (long x = mCenter.getX() - widthR; x <= mCenter.getX() + widthR; x++)
            for (long y = mCenter.getY() - heightR; y <= mCenter.getY() + heightR; y++)
            {
                OrdBean o = new OrdBean(x, y, mCenter.getZ());
                mWorlds.put(o, MainWorldLogic.getFromOrds(o));
            }
    }

    private void clearPolygons()
    {
        mPolygons = null;
    }

    /**
     * 
     */
    private void fillPolygons()
    {
        fillWorlds();
        Point center = getScreenCenter();
        if (mPolygons == null)
            mPolygons = new HashMap<>();
        for (OrdBean ord : mWorlds.keySet())
            if (!mPolygons.containsKey(ord))
            {
                Point o = calcHexOrigin(center, ord);
                int[] x = new int[7];
                int[] y = new int[7];
                x[0] = o.x + mHexShortSide;
                y[0] = o.y;
                x[1] = o.x + mHexShortSide + mHexSide;
                y[1] = o.y;
                x[2] = o.x + mHexShortSide + mHexSide + mHexShortSide;
                y[2] = o.y + mHexLongSide;
                x[3] = o.x + mHexShortSide + mHexSide;
                y[3] = o.y + mHexLongSide * 2;
                x[4] = o.x + mHexShortSide;
                y[4] = o.y + mHexLongSide * 2;
                x[5] = o.x;
                y[5] = o.y + mHexLongSide;
                x[6] = x[0];
                y[6] = y[0];
                mPolygons.put(ord, new Polygon(x, y, x.length));
            }
    }

    private Point getScreenCenter()
    {
        Dimension size = getSize();
        Point center = new Point(size.width/2 + mCenterDelta.x, size.height/2 + mCenterDelta.y);
        return center;
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void paint(Graphics g)
    {
        fillWorlds();
        fillPolygons();
        Graphics2D g2 = (Graphics2D)g;
        // int oddness = ((mOrigin.getX() % 2 == 0) ? 0 : 1);
        Dimension focusOrigin = null;
        Polygon focusPoly = null;
        MainWorldBean focusWorld = null;
        Dimension o = new Dimension();
        for (OrdBean ords : mWorlds.keySet())
        {
            MainWorldBean mw = mWorlds.get(ords);
            Polygon p = mPolygons.get(ords);
            o.width = p.xpoints[5];
            o.height = p.ypoints[0];
            if (mw == null)
            {
                int mode;
                if (ords.equals(mFocus))
                    mode = M_FOCUSED;
                else
                    mode = calcMode(ords);
                mPainter.paint(g2, o, p, ords, mode);
            }
            else if (ords.equals(mFocus))
            {
                focusOrigin = new Dimension(o);
                focusPoly = p;
                focusWorld = mw;
            }
            else
                mPainter.paint(g2, o, p, mw, calcMode(ords));
        }
        if (focusOrigin != null)
            mPainter.paint(g2, focusOrigin, focusPoly, focusWorld, M_FOCUSED);
    }

    public int calcMode(OrdBean o)
    {
        return M_NORM;
    }

    public MainWorldBean calcWorld(OrdBean o)
    {
        if (o == null)
            return null;
        if (mWorlds == null)
            return null;
        return mWorlds.get(o);
    }

    public Polygon calcHexPoly(OrdBean o)
    {
        if (o == null)
            return null;
        return mPolygons.get(o);
    }

    public Point calcHexOrigin(OrdBean o)
    {
        return calcHexOrigin(getScreenCenter(), o);
    }

    public Point calcHexOrigin(Point center, OrdBean ords)
    {
        return calcHexOrigin(center, (int)(ords.getX() - mCenter.getX()), (int)(ords.getY() - mCenter.getY()), (ords.getX() % 2) == 0);
    }

    public Point calcHexOrigin(Point center, int dx, int dy, boolean odd)
    {
        int ox = center.x + dx * (mHexShortSide + mHexSide);
        int oy = center.y + dy * (mHexLongSide * 2);
        if (!odd)
            oy += mHexLongSide;
        return new Point(ox + 1, oy + 1);
    }

    public OrdBean calcHexSource(int x, int y)
    {
        fillPolygons();
        for (OrdBean o : mPolygons.keySet())
        {
            Polygon p = mPolygons.get(o);
            if (p.contains(x, y))
                return o;
        }
        return null;
    }

    public void makeVisible(OrdBean o)
    {
        if ((mWorlds != null) && mWorlds.containsKey(o))
            return;
        setCenter(o);
    }
    
    private void doMouseWheelMoved(MouseWheelEvent ev)
    {
        if (ev.getWheelRotation() > 0)
        {
            if (mHexSide > 8)
            {
                setHexSide(mHexSide - 1);
                refresh();
            }
        }
        else if (ev.getWheelRotation() < 0)
        {
            if (mHexSide < 128)
            {
                setHexSide(mHexSide + 1);
                refresh();
            }
        }
    }

    private void doMousePressed(MouseEvent ev)
    {
        mMouseDown = ev.getPoint();
    }
    
    private void doMouseDragged(MouseEvent ev)
    {
        Point at = ev.getPoint();
        mCenterDelta.x += at.x - mMouseDown.x;
        mCenterDelta.y += at.y - mMouseDown.y;
        // normalize
        if (mCenterDelta.x >= mHexShortSide + mHexSide)
        {
            mCenterDelta.x -= mHexShortSide + mHexSide;
            mCenter.setX(mCenter.getX()-1);
            clearWorlds();
        }
        else if (mCenterDelta.x <= -mHexShortSide - mHexSide)
        {
            mCenterDelta.x += mHexShortSide + mHexSide;
            mCenter.setX(mCenter.getX()+1);
            clearWorlds();
        }
        if (mCenterDelta.y >= mHexLongSide*2)
        {
            mCenterDelta.y -= mHexLongSide*2;
            mCenter.setY(mCenter.getY()-1);
            clearWorlds();
        }
        else if (mCenterDelta.y <= -mHexLongSide*2)
        {
            mCenterDelta.y += mHexLongSide*2;
            mCenter.setY(mCenter.getY()+1);
            clearWorlds();
        }
        mMouseDown = at;
        clearPolygons();
        repaint();
    }

    private void doMouseMoved(MouseEvent e)
    {
        OrdBean o = calcHexSource(e.getX(), e.getY());
        if (o == null)
            return;
        MainWorldBean mw = calcWorld(o);
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
    }

    private void doMouseClicked(MouseEvent e)
    {
        OrdBean o = calcHexSource(e.getX(), e.getY());
        if (o == null)
            return;
        MainWorldBean mw = calcWorld(o);
        if ((e.getClickCount() == 1)
                && ((mw != null) || !isCanSelectEmpty()))
        {
            if (!o.equals(getFocus()) && (calcMode(o) == M_NORM))
            {
                setFocus(o);
                if (mw == null)
                    fireTTGActionEvent(new TTGActionEvent(this,
                            TTGActionEvent.SELECTED, o.getURI(), o));
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
                if (mw != null)
                    fireTTGActionEvent(new TTGActionEvent(this,
                        TTGActionEvent.DEACTIVATED, mw.getURI(), mw));
                else
                    fireTTGActionEvent(new TTGActionEvent(this,
                            TTGActionEvent.DEACTIVATED, o.getURI(), o));
        }
    }
    
    // listening

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

    // getters and setters
    
    /**
     * @return
     */
    public OrdBean getFocus()
    {
        return mFocus;
    }

    /**
     * @param bean
     */
    public void setFocus(OrdBean bean)
    {
        if ((mFocus != null) && mFocus.equals(bean))
            return;
        fillPolygons();
        Polygon oldPoly = calcHexPoly(mFocus);
        mFocus = bean;
        Polygon newPoly = calcHexPoly(mFocus);
        if (oldPoly != null)
        {
            repaint(oldPoly.getBounds());
            // System.out.print("oldrepaint("+oldPoly.getBounds()+")");
        }
        if (newPoly != null)
        {
            repaint(newPoly.getBounds());
            // System.out.print("newrepaint("+newPoly.getBounds()+")");
        }
        makeVisible(bean);
    }

    /**
     * @return
     */
    public Color getDisabledColor()
    {
        return mDisabledColor;
    }

    /**
     * @return
     */
    public Color getFocusedColor()
    {
        return mFocusedColor;
    }

    /**
     * @return
     */
    public Color getForeColor()
    {
        return mForeColor;
    }

    /**
     * @param color
     */
    public void setDisabledColor(Color color)
    {
        mDisabledColor = color;
    }

    /**
     * @param color
     */
    public void setFocusedColor(Color color)
    {
        mFocusedColor = color;
    }

    /**
     * @param color
     */
    public void setForeColor(Color color)
    {
        mForeColor = color;
    }

    /**
     * @return
     */
    public Color getDisabledBackColor()
    {
        return mDisabledBackColor;
    }

    /**
     * @return
     */
    public Color getFocusedBackColor()
    {
        return mFocusedBackColor;
    }

    /**
     * @return
     */
    public Color getUnfocusedBackColor()
    {
        return mUnfocusedBackColor;
    }

    /**
     * @param color
     */
    public void setDisabledBackColor(Color color)
    {
        mDisabledBackColor = color;
    }

    /**
     * @param color
     */
    public void setFocusedBackColor(Color color)
    {
        mFocusedBackColor = color;
    }

    /**
     * @param color
     */
    public void setUnfocusedBackColor(Color color)
    {
        mUnfocusedBackColor = color;
    }

    /**
     * @return
     */
    public HexPanelPainter getPainter()
    {
        return mPainter;
    }

    /**
     * @param painter
     */
    public void setPainter(HexPanelPainter painter)
    {
        mPainter = painter;
        mPainter.setPanel(this);
    }

    public boolean isCanSelectEmpty()
    {
        return mCanSelectEmpty;
    }

    public void setCanSelectEmpty(boolean b)
    {
        mCanSelectEmpty = b;
    }

}
