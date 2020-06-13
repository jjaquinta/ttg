package jo.ttg.port.beans;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.util.beans.PCSBean;
import jo.vecmath.data.SparseMatrix;

public class PortPlanBean extends PCSBean
{
    private String                                          mURI;
    private PortDesignBean                                  mDesign;
    private Map<Object, Object>                             mMetadata = new HashMap<Object, Object>();
    private SparseMatrix<PortSquareBean>                    mSquares  = new SparseMatrix<PortSquareBean>();
    private String                                          mLog      = "";
    private String                                          mErrors   = "";

    private static Map<Thread, WeakReference<PortPlanBean>> mCache    = new HashMap<>();

    // constructor

    public PortPlanBean()
    {
        register();
    }

    // utilities

    public PortSquareBean getSquare(int x, int y, int z)
    {
        return mSquares.get(x, y, z);
    }

    public void setSquare(int x, int y, int z, PortItemBean item)
    {
        setSquare(new PortSquareBean(x, y, z, item));
    }

    public void setSquare(PortSquareBean square)
    {
        removeSquare(square.getPoint());
        mSquares.set(square.getPoint(), square);
    }

    public void removeSquare(Point3i p)
    {
        PortSquareBean sq = getSquare(p);
        if (sq == null)
            return;
        mSquares.set(p, null);
    }

    public PortSquareBean getSquare(Point3i p)
    {
        return mSquares.get(p);
    }

    public void setSquare(Point3i p, PortItemBean item)
    {
        setSquare(new PortSquareBean(p, item));
    }

    public void setArea(int x, int y, int z, int dx, int dy, int dz, PortItemBean item, int inst)
    {
        for (int xx = 0; xx < dx; xx++)
            for (int yy = 0; yy < dy; yy++)
                for (int zz = 0; zz < dz; zz++)
                    setSquare(new PortSquareBean(x + xx, y + yy, z + zz, item, inst));
    }

    public void setArea(int x, int y, int z, int dx, int dy, int dz, PortItemBean item)
    {
        setArea(x, y, z, dx, dy, dz, item, 1);
    }

    public void setArea(int x, int y, int z, int dx, int dy, int dz, PortItemInstance ii)
    {
        setArea(x, y, z, dx, dy, dz, ii.getItem(), ii.getInstance());
    }

    public void setArea(PortCube cube, PortItemBean item)
    {
        setArea(cube.getX(), cube.getY(), cube.getZ(), cube.getDX(), cube.getDY(), cube.getDZ(), item);
    }

    public boolean isType(Point3i p, int type)
    {
        return isType(p.x, p.y, p.z, type);
    }

    public boolean isType(int x, int y, int z, int type)
    {
        PortSquareBean sq = getSquare(x, y, z);
        if (type == -1)
            return sq == null;
        if (type == -2)
            return sq != null;
        if (sq == null)
            return false;
        return type == sq.getItem().getType();
    }

    public boolean isSet(Point3i p)
    {
        return isSet(p.x, p.y, p.z);
    }

    public boolean isSet(int x, int y, int z)
    {
        return getSquare(x, y, z) == null;
    }

    private static SimpleDateFormat TIMESTAMP = new SimpleDateFormat(
            "HH:mm:ss ");

    public void print(String msg)
    {
        setLog(mLog + msg);
    }

    public void println(String msg)
    {
        setLog(mLog + TIMESTAMP.format(new Date()) + msg + "\n");
    }

    public String getLastLog()
    {
        if (mLog == null)
            return "";
        String msg = mLog;
        while (msg.endsWith("\n"))
            msg = msg.substring(0, msg.length() - 1).trim();
        int o = msg.lastIndexOf('\n');
        if (o < 0)
            return msg;
        else
            return msg.substring(o + 1).trim();
    }

    public void error(String msg)
    {
        setErrors(mErrors + msg);
    }

    public void errorln(String msg)
    {
        setErrors(mErrors + TIMESTAMP.format(new Date()) + msg + "\n");
    }

    public void register()
    {
        Thread t = Thread.currentThread();
        mCache.put(t, new WeakReference<PortPlanBean>(this));
    }

    public void unregister()
    {
        mCache.remove(Thread.currentThread());
    }

    public static PortPlanBean getPlan()
    {
        return getPlan(Thread.currentThread());
    }

    public static PortPlanBean getPlan(Thread t)
    {
        WeakReference<PortPlanBean> planRef = mCache.get(t);
        if (planRef == null)
            return null;
        else
            return planRef.get();
    }

    public static void printLn(String msg)
    {
        PortPlanBean plan = getPlan();
        if (plan != null)
            plan.print(msg);
        else
            System.out.println(msg);
    }

    // I/O

    // getters and setters

    /**
     * @return Returns the uRI.
     */
    public String getURI()
    {
        return mURI;
    }

    /**
     * @param uri
     *            The uRI to set.
     */
    public void setURI(String uri)
    {
        queuePropertyChange("uri", mURI, uri);
        mURI = uri;
        firePropertyChange();
    }

    /**
     * @return Returns the metadata.
     */
    public Map<Object, Object> getMetadata()
    {
        return mMetadata;
    }

    /**
     * @param metadata
     *            The metadata to set.
     */
    public void setMetadata(Map<Object, Object> metadata)
    {
        mMetadata = metadata;
    }

    public String getLog()
    {
        return mLog;
    }

    public void setLog(String log)
    {
        queuePropertyChange("log", mLog, log);
        mLog = log;
        firePropertyChange();
    }

    public String getErrors()
    {
        return mErrors;
    }

    public void setErrors(String errors)
    {
        queuePropertyChange("errors", mErrors, errors);
        mErrors = errors;
        firePropertyChange();
    }

    public PortDesignBean getDesign()
    {
        return mDesign;
    }

    public void setDesign(PortDesignBean design)
    {
        mDesign = design;
    }

    public SparseMatrix<PortSquareBean> getSquares()
    {
        return mSquares;
    }

    public void setSquares(SparseMatrix<PortSquareBean> squares)
    {
        mSquares = squares;
    }
}
