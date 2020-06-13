package jo.ttg.core.ui.swing.ship.sys.twod;

import java.awt.AWTEvent;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.util.geom3d.Point3D;
import jo.util.geom3d.Transform3D;
import jo.util.utils.MathUtils;

public class System2DViewer extends Canvas
{
    private Image                   mStarfield;
    private Dimension               mFOV;
    private SystemBean              mSystem;
    private Transform3D             mTransform;
    private List<TransformedBody>   mTransformedObjects;
    private boolean                 mTransformedDirty;
    private ISystem2DHandler        mHandler;
    private List<TTGActionListener> mTTGActionListeners;
    private String                  mHoverURI;

    public System2DViewer()
    {
        mFOV = new Dimension(30, 20);
        mTransformedObjects = new ArrayList<TransformedBody>();
        mTransform = new Transform3D();
        mTTGActionListeners = new ArrayList<TTGActionListener>();
        setTransformedDirty(true);
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.MOUSE_MOTION_EVENT_MASK);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if (mTransformedDirty)
            updateTransformed();
        Dimension size = getSize();
        Graphics2D g2 = (Graphics2D)g;
        g2.translate(size.width / 2, size.height / 2);
        mHandler.preDraw(g2, mTransformedObjects, mFOV, size);
        for (TransformedBody tbody : mTransformedObjects)
            mHandler.draw(g2, tbody);
    }

    private void updateTransformed()
    {
        Dimension size = getSize();
        List<TransformedBody> newObjects = new ArrayList<TransformedBody>();
        for (Iterator<BodyBean> i = mSystem.getSystemRoot()
                .getAllSatelitesIterator(); i.hasNext();)
        {
            BodyBean body = i.next();
            Point3D loc = mHandler.getLocation(body);
            Point3D tloc = mTransform.transformNew(loc);
            double theta = Math.atan2(tloc.x, tloc.y);
            double phi = Math.atan2(tloc.z, tloc.y);
            TransformedBody tBody = new TransformedBody();
            tBody.setBody(body);
            tBody.setLocation(tloc);
            double diam = body.getDiameter();
            double dist = loc.mag();
            double radius = Math.atan2(diam, dist) * 180 / Math.PI;
            tBody.setDist(dist);
            tBody.setApparentRadius(radius);
            tBody.setTheta(theta / Math.PI * 180);
            tBody.setPhi(phi / Math.PI * 180);
            tBody.setX(MathUtils.interpolate(tBody.getTheta(), -mFOV.width,
                    mFOV.width, -size.width / 2, size.width / 2));
            tBody.setY(MathUtils.interpolate(tBody.getPhi(), -mFOV.height,
                    mFOV.height, -size.height / 2, size.height / 2));
            tBody.setInFieldOfView((tBody.getTheta()
                    + tBody.getApparentRadius() >= -mFOV.width)
                    && (tBody.getTheta() - tBody.getApparentRadius() <= mFOV.width)
                    && (tBody.getPhi() + tBody.getApparentRadius() >= -mFOV.height)
                    && (tBody.getPhi() - tBody.getApparentRadius() <= mFOV.height));
            // System.out.println(tBody.getBody().getName()+": t="+tBody.getTheta()+", p="+tBody.getPhi()+", r="+tBody.getApparentRadius()+", in="+tBody.isInFieldOfView());
            newObjects.add(tBody);
        }
        Collections.sort(newObjects, new Comparator<TransformedBody>() {
            @Override
            public int compare(TransformedBody object1, TransformedBody object2)
            {
                return MathUtils.sgn(object2.getDist() - object1.getDist());
            }
        });
        mTransformedObjects = newObjects;
        setTransformedDirty(false);
    }

    private int mLeftX = -999;
    private int mLeftY = -999;

    protected void doProcessMouseEvent(MouseEvent e)
    {
        Dimension s = getSize();
        int X = e.getX() - s.width / 2;
        int Y = e.getY() - s.height / 2;
        switch (e.getID())
        {
            case MouseEvent.MOUSE_PRESSED:
                if (!e.isMetaDown())
                {
                    mLeftX = X;
                    mLeftY = Y;
                }
                break;
            case MouseEvent.MOUSE_RELEASED:
                break;
            case MouseEvent.MOUSE_CLICKED:
            {
                TransformedBody tbody = getNearest(X, Y);
                if ((e.getClickCount() == 1) && (tbody != null))
                {
                    fireTTGActionEvent(new TTGActionEvent(this,
                            TTGActionEvent.SELECTED, tbody.getBody().getURI(),
                            tbody.getBody()));
                }
                else if ((e.getClickCount() == 2) && (tbody != null))
                {
                    // System.out.println("Button="+e.getButton()+", 1="+MouseEvent.BUTTON1+", 2="+MouseEvent.BUTTON2);
                    if (e.getButton() == MouseEvent.BUTTON1)
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.ACTIVATED, tbody.getBody()
                                        .getURI(), tbody.getBody()));
                    else if (e.getButton() != MouseEvent.BUTTON1)
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.DEACTIVATED, tbody.getBody()
                                        .getURI(), tbody.getBody()));
                }
                break;
            }
            case MouseEvent.MOUSE_DRAGGED:
                if (!e.isMetaDown())
                {
                    if (!isWithinTolerance(mLeftX, mLeftY, X, Y))
                    {
                        Point3D rot = new Point3D(mLeftY - Y, 0, mLeftX - X);
                        rot.scale(Math.PI / 180); // convert to radians
                        mTransform.transform(rot);
                        setTransformedDirty(true);
                        mLeftX = X;
                        mLeftY = Y;
                    }
                }
                break;
            case MouseEvent.MOUSE_MOVED:
            {
                TransformedBody tbody = getNearest(X, Y);
                if (tbody != null)
                {
                    String uri = tbody.getBody().getURI();
                    if (!uri.equals(mHoverURI))
                    {
                        mHoverURI = uri;
                        fireTTGActionEvent(new TTGActionEvent(this,
                                TTGActionEvent.HOVER, tbody.getBody().getURI(),
                                tbody.getBody()));
                    }
                }
                else
                    mHoverURI = null;
                break;
            }
        }
    }

    private boolean isWithinTolerance(int x1, int y1, int x2, int y2)
    {
        return ((Math.abs(x1 - x2) <= 4) && (Math.abs(y1 - y2) <= 4));
    }

    private TransformedBody getNearest(double x, double y)
    {
        TransformedBody best = null;
        double bestd = -1;
        for (TransformedBody body : mTransformedObjects)
        {
            double d = (x - body.getTheta()) * (x - body.getTheta())
                    + (y - body.getPhi()) * (y - body.getPhi());
            d -= body.getApparentRadius();
            if ((best == null) || (d < bestd))
            {
                best = body;
                bestd = d;
            }
        }
        return best;
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

    public SystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SystemBean system)
    {
        mSystem = system;
        setTransformedDirty(true);
    }

    public Image getStarfield()
    {
        return mStarfield;
    }

    public void setStarfield(Image starfield)
    {
        mStarfield = starfield;
    }

    public Transform3D getTransform()
    {
        return mTransform;
    }

    public void setTransform(Transform3D transform)
    {
        mTransform = transform;
        setTransformedDirty(true);
    }

    public ISystem2DHandler getHandler()
    {
        return mHandler;
    }

    public void setHandler(ISystem2DHandler handler)
    {
        mHandler = handler;
    }

    public Dimension getFOV()
    {
        return mFOV;
    }

    public void setFOV(Dimension fOV)
    {
        mFOV = fOV;
        setTransformedDirty(true);
    }

    public boolean isTransformedDirty()
    {
        return mTransformedDirty;
    }

    public void setTransformedDirty(boolean transformedDirty)
    {
        mTransformedDirty = transformedDirty;
        if (mTransformedDirty)
            repaint();
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
        for (TTGActionListener l : mTTGActionListeners
                .toArray(new TTGActionListener[0]))
            l.actionPerformed(ev);
    }
}
