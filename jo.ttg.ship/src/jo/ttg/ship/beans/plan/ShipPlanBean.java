package jo.ttg.ship.beans.plan;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import org.json.simple.IJSONAble;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.util.beans.PCSBean;
import jo.vecmath.data.SparseMatrix;

public class ShipPlanBean extends PCSBean implements IJSONAble
{
    private String                             mURI;
    private ShipScanBean                       mScan;
    private Map<Object, Object>                mMetadata = new HashMap<Object, Object>();
    private SparseMatrix<ShipSquareBean>       mSquares = new SparseMatrix<ShipSquareBean>();
    private Map<String, ShipPlanComponentBean> mComponents = new HashMap<>();
    private String                             mLog = "";
    private String                             mErrors = "";
    
    private static Map<Thread,WeakReference<ShipPlanBean>>    mCache = new HashMap<>();

    // constructor
    
    public ShipPlanBean()
    {
        register();
    }
    
    public ShipPlanBean(JSONObject json)
    {
        this();
        fromJSON(json);
    }
    
    // utilities

    public ShipSquareBean getSquare(int x, int y, int z)
    {
        return mSquares.get(x, y, z);
    }

    public void setSquare(int x, int y, int z, int type)
    {
        setSquare(new ShipSquareBean(x, y, z, type));
    }

    public void setSquare(ShipSquareBean square)
    {
        removeSquare(square.getPoint());
        mSquares.set(square.getPoint(), square);
        ShipPlanComponentBean comp = mComponents
                .get(ShipPlanComponentBean.getKey(square));
        if (comp == null)
        {
            comp = new ShipPlanComponentBean(this, square);
            mComponents.put(comp.getKey(), comp);
        }
        else
            comp.add(square);
    }

    public void removeSquare(Point3i p)
    {
        ShipSquareBean sq = getSquare(p);
        if (sq == null)
            return;
        mSquares.set(p, null);
        ShipPlanComponentBean comp = mComponents
                .get(ShipPlanComponentBean.getKey(sq));
        if (comp != null)
            comp.remove(sq);
    }

    public ShipSquareBean getSquare(Point3i p)
    {
        return mSquares.get(p);
    }

    public void setSquare(Point3i p, int type)
    {
        setSquare(new ShipSquareBean(p, type));
    }

    public boolean isType(Point3i p, int type)
    {
        return isType(p.x, p.y, p.z, type);
    }

    public boolean isType(int x, int y, int z, int type)
    {
        ShipSquareBean sq = getSquare(x, y, z);
        if (type == -1)
            return sq == null;
        if (type == -2)
            return sq != null;
        if (sq == null)
            return false;
        return type == sq.getType();
    }

    public void updatePerimiters()
    {
        for (ShipPlanComponentBean comp : mComponents.values())
            comp.updatePerimeter();
    }

    public ShipPlanComponentBean getComponent(int type, int compartment,
            String notes)
    {
        String key = ShipPlanComponentBean.getKey(type, compartment, notes);
        return mComponents.get(key);
    }

    public ShipPlanComponentBean getComponent(int type, int compartment)
    {
        return getComponent(type, compartment, null);
    }

    public ShipPlanComponentBean getComponent(int type)
    {
        ShipPlanComponentBean comp = getComponent(type, 0, null);
        if (comp == null)
            comp = getComponent(type, 1, null);
        return comp;
    }

    public ShipPlanComponentBean getComponent(ShipSquareBean sq)
    {
        return getComponent(sq.getType(), sq.getCompartment(), sq.getNotes());
    }

    public ShipPlanPerimeterBean getSubComponent(Point3i p)
    {
        updatePerimiters();
        String key = ShipPlanComponentBean.getKey(getSquare(p));
        ShipPlanComponentBean comp = mComponents.get(key);
        ShipPlanPerimeterBean peri = comp.findSubComponent(p);
        return peri;
    }
    
    private static SimpleDateFormat TIMESTAMP = new SimpleDateFormat("HH:mm:ss ");

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
        mCache.put(t, new WeakReference<ShipPlanBean>(this));
    }
    
    public void unregister()
    {
        mCache.remove(Thread.currentThread());
    }

    public static ShipPlanBean getPlan()
    {
        return getPlan(Thread.currentThread());
    }

    public static ShipPlanBean getPlan(Thread t)
    {
        WeakReference<ShipPlanBean> planRef = mCache.get(t);
        if (planRef == null)
            return null;
        else
            return planRef.get();
    }
    
    public static void printLn(String msg)
    {
        ShipPlanBean plan = getPlan();
        if (plan != null)
            plan.print(msg);
        else
            System.out.println(msg);
    }
    
    // I/O

    @SuppressWarnings("unchecked")
    public JSONObject toJSON()
    {
        JSONObject md = new JSONObject();
        for (Object key : mMetadata.keySet())
        {
            Object val = mMetadata.get(key);
            md.put(key.toString(), val.toString());
        }
        JSONArray squares = new JSONArray();
        for (Iterator<Point3i> i = mSquares.iteratorNonNull(); i.hasNext(); )
        {
            Point3i p = i.next();
            ShipSquareBean sq = mSquares.get(p);
            squares.add(sq.toJSON());
        }

        JSONObject json = new JSONObject();
        if (mURI != null)
            json.put("uri", mURI);
        if (md.size() > 0)
            json.put("metadata", md);
        if (mLog != null)
            json.put("log", mLog);
        if (mErrors != null)
            json.put("errors", mErrors);
        if (mScan != null)
            json.put("scan", mScan.toJSON());
        json.put("squares", squares);
        return json;
    }

    public void fromJSON(JSONObject json)
    {
        mURI = json.getString("uri");
        if (json.containsKey("metadata"))
        {
            mMetadata = new HashMap<>();
            JSONObject md = (JSONObject)json.get("metadata");
            for (String key : md.keySet())
                mMetadata.put(key, md.get(key));
        }
        mLog = json.getString("log");
        mErrors = json.getString("errors");
        if (json.containsKey("scan"))
            mScan = new ShipScanBean((JSONObject)json.get("scan"));
        if (json.containsKey("squares"))
        {
            JSONArray squares = (JSONArray)json.get("squares");
            for (int i = 0; i < squares.size(); i++)
            {
                JSONObject square = (JSONObject)squares.get(i);
                ShipSquareBean sq = new ShipSquareBean(square);
                setSquare(sq);
            }
        }
    }

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

    public SparseMatrix<ShipSquareBean> getSquares()
    {
        return mSquares;
    }

    public void setSquares(SparseMatrix<ShipSquareBean> squares)
    {
        mSquares = squares;
    }

    public Map<String, ShipPlanComponentBean> getComponents()
    {
        return mComponents;
    }

    public List<ShipPlanComponentBean> getComponents(int type)
    {
        List<ShipPlanComponentBean> components = new ArrayList<ShipPlanComponentBean>();
        for (ShipPlanComponentBean comp : mComponents.values())
            if (comp.getType() == type)
                components.add(comp);
        return components;
    }

    public void setComponents(Map<String, ShipPlanComponentBean> components)
    {
        mComponents = components;
    }

    public ShipScanBean getScan()
    {
        return mScan;
    }

    public void setScan(ShipScanBean scan)
    {
        mScan = scan;
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
}
