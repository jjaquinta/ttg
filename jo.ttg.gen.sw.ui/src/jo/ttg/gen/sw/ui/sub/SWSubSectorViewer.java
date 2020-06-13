package jo.ttg.gen.sw.ui.sub;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import jo.ttg.beans.OrdBean;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.logic.HexPainterLogic;
import jo.ttg.gen.sw.logic.RuntimeLogic;
import jo.ttg.gen.sw.ui.SWFrame;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Transform3D;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.ui.swing.utils.MouseUtils;

public class SWSubSectorViewer extends JPanel
{
    private static final double[]         BASE_TRANS        = {
            27.532687161592268, 17.355039372200135, 61.67764492661494, 0.0,
            64.0649094569881, -6.400279995844061, -26.79742394121155, 0.0,
            -1.0082914122952282, 67.23999674305836, -18.4700926923424, 0.0, 0.0,
            0.0, 0.0, 1.0 };
    private static final double           SHORT_LINK_LENGTH = Math.sqrt(2);
    private static final double           LONG_LINK_LENGTH  = Math.sqrt(4);

    private int                           mGridSize         = 9;
    private Color                         GRID              = ColorUtils.DarkGreen;
    private Color                         CURSOR            = ColorUtils.LightGreen;
    private Color                         SHORT_LINK_COLOR  = Color.yellow;
    private Color                         LONG_LINK_COLOR   = Color.red;

    private Dimension                     mSize;
    private Graphics2D                    mG;

    private OrdBean                       mFocusPoint;
    private SWMainWorldBean[][][]         mWorlds;
    private Point3D[][][]                 mPoints;
    private Point3D                       mLow              = new Point3D();
    private Point3D                       mHigh             = new Point3D();
    private List<SWMainWorldBean>         mWorldList        = new ArrayList<>();
    private List<SWMainWorldBean>         mInnerWorldList   = new ArrayList<>();
    private Map<SWMainWorldBean, Point3D> mWorldPoints      = new HashMap<>();
    private List<SWMainWorldBean[]>       mShortLinks       = new ArrayList<>();
    private List<SWMainWorldBean[]>       mLongLinks        = new ArrayList<>();
    private Transform3D                   mTrans            = new Transform3D();
    private int                           mMouseX;
    private int                           mMouseY;

    public SWSubSectorViewer()
    {
        initInstantiate();
        initLink();
        initLayout();
        doNewData();
    }

    private void initInstantiate()
    {
        mTrans.set(BASE_TRANS);
        HexPainterLogic.mForeColor = ColorUtils.DarkCyan;
        HexPainterLogic.mBackColor = getBackground();
        HexPainterLogic.mFocusedColor = ColorUtils.White;
        HexPainterLogic.mDisabledColor = ColorUtils.Gray;
        setMinimumSize(new Dimension(256, 256));
        setMaximumSize(new Dimension(2560, 2560));
    }

    private void initLayout()
    {
    }

    private void initLink()
    {
        RuntimeLogic.listen("focusPoint", (ov, nv) -> doNewData());
        MouseUtils.mousePressed(this, (ev) -> doMousePressed(ev));
        MouseUtils.mouseReleased(this, (ev) -> doMouseReleased(ev));
        MouseUtils.mouseExited(this, (ev) -> doMouseReleased(ev));
        MouseUtils.mouseDragged(this, (ev) -> doMouseDragged(ev));
        MouseUtils.mouseWheelMoved(this, (ev) -> doMouseWheelMoved(ev));
        MouseUtils.mouseClicked(this, (ev) -> doMouseClicked(ev));
        RuntimeLogic.listen("cursorPoint,selectedPoints,displayLinks,displayGrid", (ov,nv) -> this.repaint());
    }

    private void doMouseClicked(MouseEvent ev)
    {
        if (ev.getButton() == MouseEvent.BUTTON2)
        {
            mTrans.set(BASE_TRANS);
            doNewRotation();
        }
        else if (ev.getButton() == MouseEvent.BUTTON1)
        {
            SWMainWorldBean mw = findWorld(ev.getX(), ev.getY());
            if (mw != null)
            {
                if (ev.getClickCount() == 1)
                    RuntimeLogic.setCursorPoint(mw.getOrds());
                else if (ev.getClickCount() == 2)
                    if ((ev.getModifiers()&MouseEvent.SHIFT_MASK) != 0)
                        RuntimeLogic.setFocusPoint(mw.getOrds());
                    else
                        RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM);
            }
        }
    }

    private void doMousePressed(MouseEvent ev)
    {
        mMouseX = ev.getX();
        mMouseY = ev.getY();
    }

    private void doMouseReleased(MouseEvent ev)
    {
        doMouseDragged(ev);
        mMouseX = -1;
        mMouseY = -1;
    }

    private void doMouseDragged(MouseEvent ev)
    {
        if (mMouseX < 0)
            return;
        int dx = ev.getX() - mMouseX;
        int dy = ev.getY() - mMouseY;
        mTrans.yrot(dx);
        mTrans.xrot(dy);
        doNewRotation();
        doMousePressed(ev);
    }

    private void doMouseWheelMoved(MouseWheelEvent ev)
    {
        int d = ev.getWheelRotation();
        if (d == 0)
            return;
        if (d < 0)
            mTrans.scale(1.05);
        else
            mTrans.scale(0.95);
        doNewRotation();
    }

    private void doNewData()
    {
        mFocusPoint = RuntimeLogic.getInstance().getFocusPoint();
        OrdBean base = new OrdBean(mFocusPoint.getX() - mGridSize / 2,
                mFocusPoint.getY() - mGridSize / 2,
                mFocusPoint.getZ() - mGridSize / 2);
        mWorlds = new SWMainWorldBean[mGridSize][mGridSize][mGridSize];
        mWorldList.clear();
        mInnerWorldList.clear();
        mShortLinks.clear();
        mLongLinks.clear();
        calcPoints(base);
        calcLinks();
        doNewRotation();
    }

    private void calcLinks()
    {
        for (int i = 0; i < mWorldList.size() - 1; i++)
        {
            SWMainWorldBean mw1 = mWorldList.get(i);
            boolean inner1 = mInnerWorldList.contains(mw1);
            for (int j = i + 1; j < mWorldList.size() - 1; j++)
            {
                SWMainWorldBean mw2 = mWorldList.get(j);
                if (!inner1 && !mInnerWorldList.contains(mw2))
                    continue;
                double d = mw1.getOrdsFine().dist(mw2.getOrdsFine());
                if (d <= SHORT_LINK_LENGTH)
                    mShortLinks.add(new SWMainWorldBean[] { mw1, mw2 });
                else if (d <= LONG_LINK_LENGTH)
                    mLongLinks.add(new SWMainWorldBean[] { mw1, mw2 });
            }
        }
    }

    private void calcPoints(OrdBean base)
    {
        for (int dx = 0; dx < mGridSize; dx++)
            for (int dy = 0; dy < mGridSize; dy++)
                for (int dz = 0; dz < mGridSize; dz++)
                {
                    SWMainWorldBean mw = (SWMainWorldBean)MainWorldLogic
                            .getFromOrds(new OrdBean(base.getX() + dx,
                                    base.getY() + dy, base.getZ() + dz));
                    mWorlds[dx][dy][dz] = mw;
                    if (mWorlds[dx][dy][dz] != null)
                    {
                        mWorldList.add(mw);
                        if (isInner(dx, dy, dz))
                            mInnerWorldList.add(mw);
                    }
                }
        RuntimeLogic.getInstance().setWorldList(mWorldList);
        RuntimeLogic.getInstance().setInnerWorldList(mInnerWorldList);
        RuntimeLogic.getInstance().setShortLinks(mShortLinks);
        RuntimeLogic.getInstance().setLongLinks(mLongLinks);
    }
    
    private boolean isInner(int x)
    {
        return (x >= 2) && (x <= mGridSize - 2);
    }
    
    private boolean isInner(int x, int y, int z)
    {
        return isInner(x) && isInner(y) && isInner(z);
    }

    private Point3D rotate(Point3D p)
    {
        Point3D v = new Point3D(p.x - mFocusPoint.getX(),
                p.y - mFocusPoint.getY(), p.z - mFocusPoint.getZ());
        mTrans.transform(v);
        mLow.x = Math.min(mLow.x, v.x);
        mLow.y = Math.min(mLow.y, v.y);
        mLow.z = Math.min(mLow.z, v.z);
        mHigh.x = Math.max(mHigh.x, v.x);
        mHigh.y = Math.max(mHigh.y, v.y);
        mHigh.z = Math.max(mHigh.z, v.z);
        return v;
    }

    private void doNewRotation()
    {
        // System.out.println(DoubleUtils.toString(mTrans.toDoubleArray()));
        mPoints = new Point3D[mGridSize + 1][mGridSize + 1][mGridSize + 1];
        int h = mGridSize / 2;
        mLow.scale(0);
        mHigh.scale(0);
        for (int dx = 0; dx <= mGridSize; dx++)
            for (int dy = 0; dy <= mGridSize; dy++)
                for (int dz = 0; dz <= mGridSize; dz++)
                {
                    Point3D v = new Point3D(dx - h, dy - h, dz - h);
                    Point3D r = rotate(v);
                    mPoints[dx][dy][dz] = r;
                }
        mWorldPoints.clear();
        for (SWMainWorldBean mw : mWorldList)
        {
            Point3D p = mw.getOrdsFine();
            Point3D r = rotate(p);
            // System.out.println(mw.getName()+": "+p+" -> "+r);
            mWorldPoints.put(mw, r);
        }
        Collections.sort(mWorldList, new Comparator<SWMainWorldBean>() {
            @Override
            public int compare(SWMainWorldBean o1, SWMainWorldBean o2)
            {
                Point3D p1 = mWorldPoints.get(o1);
                Point3D p2 = mWorldPoints.get(o2);
                return (int)Math.signum(p1.getZ() - p2.getZ());
            }
        });
        repaint();
    }
    
    private SWMainWorldBean findWorld(int x, int y)
    {
        x -= mSize.width/2; // correct for origin transfer
        y -= mSize.height/2;
        for (SWMainWorldBean mw : mInnerWorldList)
        {
            double d = mWorldPoints.get(mw).dist(x, y);
            if (d < HexPainterLogic.getHexSide())
                return mw;
        }
        return null;
    }

    private int[] map(Point3D p)
    {
        int[] xy = new int[2];
        xy[0] = (int)p.x;
        xy[1] = (int)p.y;
        // System.out.println(p+" -> "+xy[0]+","+xy[1]);
        return xy;
    }

    private void line(Point3D p1, Point3D p2)
    {
        int[] xy1 = map(p1);
        int[] xy2 = map(p2);
        mG.drawLine(xy1[0], xy1[1], xy2[0], xy2[1]);
    }

    private void line(SWMainWorldBean mw1, SWMainWorldBean mw2)
    {
        line(mWorldPoints.get(mw1), mWorldPoints.get(mw2));
    }

    @Override
    public void paint(Graphics g)
    {
        mSize = getSize();
        HexPainterLogic
                .setHexSide(Math.min(mSize.width, mSize.height) / mGridSize / 2);
        mG = (Graphics2D)g;
        paintBackground();
        mG.translate(mSize.width / 2, mSize.height / 2);
        if (RuntimeLogic.getInstance().isDisplayGrid())
            paintGrid();
        if (RuntimeLogic.getInstance().isDisplayLinks())
            paintLinks();
        OrdBean cursor = RuntimeLogic.getInstance().getCursorPoint();
        Point3D c = rotate(new Point3D(cursor.getX(), cursor.getY(), cursor.getZ()));
        for (SWMainWorldBean mw : mInnerWorldList)
        {
            if ((c != null) && (c.z < mWorldPoints.get(mw).getZ()))
                paintCursorGrid();
            paintWorld(mw);
        }
    }

    private void paintBackground()
    {
        Point3D c = rotate(new Point3D(mSize.width, mSize.height, 0));
        int x0 = (int)c.x;
        while (x0 > 0)
            x0 -= SWFrame.STARFIELD.getWidth();
        int y0 = (int)c.y;
        while (y0 > 0)
            y0 -= SWFrame.STARFIELD.getWidth();
        for (int x = x0; x < mSize.width; x += SWFrame.STARFIELD.getWidth())
            for (int y = y0; y < mSize.height; y += SWFrame.STARFIELD.getHeight())
                mG.drawImage(SWFrame.STARFIELD, x, y, null);
    }

    private void paintWorld(SWMainWorldBean mw)
    {
        Point3D p1 = mWorldPoints.get(mw);
        if (RuntimeLogic.getInstance().isDisplayGrid())
        {
            Point3D p2 = rotate(new Point3D(mw.getOrdsFine().getX(),
                    mw.getOrdsFine().getY(), mFocusPoint.getZ()));
            mG.setColor(GRID);
            line(p1, p2);
        }
        int mode = HexPainterLogic.M_NORM;
        if (mw.getOrds().equals(RuntimeLogic.getInstance().getCursorPoint()))
            mode = HexPainterLogic.M_FOCUSED;
        HexPainterLogic.paint(mG, new Dimension((int)p1.x, (int)p1.y), null, mw,
                mode);
    }
    
    private void paintCursorGrid()
    {
        OrdBean o = RuntimeLogic.getInstance().getCursorPoint();
        Point3D p000 = rotate(new Point3D(o.getX(), o.getY(), o.getZ()));
        Point3D p001 = rotate(new Point3D(o.getX(), o.getY(), o.getZ()+1));
        Point3D p010 = rotate(new Point3D(o.getX(), o.getY()+1, o.getZ()));
        Point3D p011 = rotate(new Point3D(o.getX(), o.getY()+1, o.getZ()+1));
        Point3D p100 = rotate(new Point3D(o.getX()+1, o.getY(), o.getZ()));
        Point3D p101 = rotate(new Point3D(o.getX()+1, o.getY(), o.getZ()+1));
        Point3D p110 = rotate(new Point3D(o.getX()+1, o.getY()+1, o.getZ()));
        Point3D p111 = rotate(new Point3D(o.getX()+1, o.getY()+1, o.getZ()+1));
        mG.setColor(CURSOR);
        line(p000, p010);
        line(p010, p110);
        line(p110, p100);
        line(p100, p000);
        line(p001, p011);
        line(p011, p111);
        line(p111, p101);
        line(p101, p001);
        line(p000, p001);
        line(p010, p011);
        line(p110, p111);
        line(p100, p101);
    }

    private void paintLinks()
    {
        mG.setColor(SHORT_LINK_COLOR);
        paintLinks(mShortLinks);
        mG.setColor(LONG_LINK_COLOR);
        paintLinks(mLongLinks);
    }
    
    private void paintLinks(List<SWMainWorldBean[]> links)
    {
        for (SWMainWorldBean[] link : links)
        {
            line(link[0], link[1]);
        }
    }

    private void paintGrid()
    {
        mG.setColor(GRID);
        double ox = mFocusPoint.getX() - mGridSize / 2;
        double oy = mFocusPoint.getY() - mGridSize / 2;
        double oz = mFocusPoint.getZ();
        for (int xy = 2; xy <= mGridSize - 2; xy++)
        {
            line(rotate(new Point3D(ox + xy, oy + 2, oz)),
                    rotate(new Point3D(ox + xy, oy + mGridSize - 2, oz)));
            line(rotate(new Point3D(ox + 2, oy + xy, oz)),
                    rotate(new Point3D(ox + mGridSize - 2, oy + xy, oz)));
        }
    }
}
