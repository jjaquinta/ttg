package jo.ttg.gen.sw.data;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONObject;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.util.beans.PCSBean;

public class RuntimeBean extends PCSBean
{
    public static final int         ZOOM_SUBSECTOR  = 0;
    public static final int         ZOOM_SYSTEM     = 1;
    public static final int         ZOOM_SURFACE    = 2;

    private JSONObject              mSettings;
    private OrdBean                 mFocusPoint     = new OrdBean(1000, 1000,
            1000);
    private OrdBean                 mCursorPoint    = new OrdBean(1000, 1000,
            1000);
    private List<OrdBean>           mSelectedPoints = new ArrayList<>();
    private SWMainWorldBean         mFocusMainWorld;
    private SWMainWorldBean         mCursorMainWorld;
    private String                  mError;
    private int                     mZoom;
    private boolean                 mDirty;
    // subsector
    private boolean                 mDisplayGrid    = true;
    private boolean                 mDisplayLinks;
    // system
    private List<SWMainWorldBean>   mWorldList;
    private List<SWMainWorldBean>   mInnerWorldList;
    private List<SWMainWorldBean[]> mShortLinks;
    private List<SWMainWorldBean[]> mLongLinks;
    private SWSystemBean            mSystem;
    private BodyBean                mFocusWorld;
    private BodyBean                mCursorWorld;
    private boolean                 mDisplayList;
    private DateBean                mDate           = new DateBean(
            1100 * DateBean.ONE_YEAR);
    // surface
    private SurfaceBean             mSurface;

    // getters and setters

    public OrdBean getFocusPoint()
    {
        return mFocusPoint;
    }

    public void setFocusPoint(OrdBean focusPoint)
    {
        queuePropertyChange("focusPoint", mFocusPoint, focusPoint);
        mFocusPoint = focusPoint;
        firePropertyChange();
    }

    public SWMainWorldBean getFocusMainWorld()
    {
        return mFocusMainWorld;
    }

    public void setFocusMainWorld(SWMainWorldBean focusMainWorld)
    {
        queuePropertyChange("focusMainWorld", mFocusMainWorld, focusMainWorld);
        mFocusMainWorld = focusMainWorld;
        firePropertyChange();
    }

    public JSONObject getSettings()
    {
        return mSettings;
    }

    public void setSettings(JSONObject settings)
    {
        mSettings = settings;
    }

    public String getError()
    {
        return mError;
    }

    public void setError(String error)
    {
        queuePropertyChange("error", mError, error);
        mError = error;
        firePropertyChange();
    }

    public boolean isDirty()
    {
        return mDirty;
    }

    public void setDirty(boolean dirty)
    {
        mDirty = dirty;
    }

    public OrdBean getCursorPoint()
    {
        return mCursorPoint;
    }

    public void setCursorPoint(OrdBean cursorPoint)
    {
        queuePropertyChange("cursorPoint", mCursorPoint, cursorPoint);
        mCursorPoint = cursorPoint;
        firePropertyChange();
    }

    public List<OrdBean> getSelectedPoints()
    {
        return mSelectedPoints;
    }

    public void setSelectedPoints(List<OrdBean> selectedPoints)
    {
        queuePropertyChange("selectedPoints", mSelectedPoints, selectedPoints);
        mSelectedPoints = selectedPoints;
        firePropertyChange();
    }

    public SWMainWorldBean getCursorMainWorld()
    {
        return mCursorMainWorld;
    }

    public void setCursorMainWorld(SWMainWorldBean cursorMainWorld)
    {
        queuePropertyChange("cursorMainWorld", mCursorMainWorld,
                cursorMainWorld);
        mCursorMainWorld = cursorMainWorld;
        firePropertyChange();
    }

    public boolean isDisplayGrid()
    {
        return mDisplayGrid;
    }

    public void setDisplayGrid(boolean displayGrid)
    {
        queuePropertyChange("displayGrid", mDisplayGrid, displayGrid);
        mDisplayGrid = displayGrid;
        firePropertyChange();
    }

    public boolean isDisplayLinks()
    {
        return mDisplayLinks;
    }

    public void setDisplayLinks(boolean displayLinks)
    {
        queuePropertyChange("displayLinks", mDisplayLinks, displayLinks);
        mDisplayLinks = displayLinks;
        firePropertyChange();
    }

    public int getZoom()
    {
        return mZoom;
    }

    public void setZoom(int zoom)
    {
        queuePropertyChange("zoom", mZoom, zoom);
        mZoom = zoom;
        firePropertyChange();
    }

    public SWSystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SWSystemBean system)
    {
        queuePropertyChange("system", mSystem, system);
        mSystem = system;
        firePropertyChange();
    }

    public BodyBean getFocusWorld()
    {
        return mFocusWorld;
    }

    public void setFocusWorld(BodyBean focusWorld)
    {
        queuePropertyChange("focusWorld", mFocusWorld, focusWorld);
        mFocusWorld = focusWorld;
        firePropertyChange();
    }

    public BodyBean getCursorWorld()
    {
        return mCursorWorld;
    }

    public void setCursorWorld(BodyBean cursorWorld)
    {
        queuePropertyChange("cursorWorld", mCursorWorld, cursorWorld);
        mCursorWorld = cursorWorld;
        firePropertyChange();
    }

    public boolean isDisplayList()
    {
        return mDisplayList;
    }

    public void setDisplayList(boolean displayList)
    {
        queuePropertyChange("displayList", mDisplayList, displayList);
        mDisplayList = displayList;
        firePropertyChange();
    }

    public DateBean getDate()
    {
        return mDate;
    }

    public void setDate(DateBean date)
    {
        queuePropertyChange("date", mDate, date);
        mDate = date;
        firePropertyChange();
    }

    public SurfaceBean getSurface()
    {
        return mSurface;
    }

    public void setSurface(SurfaceBean surface)
    {
        queuePropertyChange("surface", mSurface, surface);
        mSurface = surface;
        firePropertyChange();
    }

    public List<SWMainWorldBean> getWorldList()
    {
        return mWorldList;
    }

    public void setWorldList(List<SWMainWorldBean> worldList)
    {
        mWorldList = worldList;
    }

    public List<SWMainWorldBean> getInnerWorldList()
    {
        return mInnerWorldList;
    }

    public void setInnerWorldList(List<SWMainWorldBean> innerWorldList)
    {
        mInnerWorldList = innerWorldList;
    }

    public List<SWMainWorldBean[]> getShortLinks()
    {
        return mShortLinks;
    }

    public void setShortLinks(List<SWMainWorldBean[]> shortLinks)
    {
        mShortLinks = shortLinks;
    }

    public List<SWMainWorldBean[]> getLongLinks()
    {
        return mLongLinks;
    }

    public void setLongLinks(List<SWMainWorldBean[]> longLinks)
    {
        mLongLinks = longLinks;
    }
}
