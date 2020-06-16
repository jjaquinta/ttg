package jo.ttg.gen.imp.ui.swing.data;

import org.json.simple.JSONObject;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.gni.GNIGenScheme;
import jo.util.beans.PCSBean;

public class RuntimeBean extends PCSBean
{
    public static final int ZOOM_SECTOR    = -1;
    public static final int ZOOM_SUBSECTOR = 0;
    public static final int ZOOM_SYSTEM    = 1;
    public static final int ZOOM_SURFACE   = 2;

    private JSONObject      mSettings;
    private String          mError;
    private int             mZoom;
    private boolean         mDirty;
    // sector
    private OrdBean         mFocusPoint = GNIGenScheme.REGINA;
    private OrdBean         mCursorPoint = GNIGenScheme.REGINA;
    private MainWorldBean   mFocusMainWorld;
    private MainWorldBean   mCursorMainWorld;
    private SectorBean      mFocusSector;
    // subsector
    private SubSectorBean   mFocusSubSector;
    private boolean         mDisplayGrid   = true;
    private boolean         mDisplayLinks;
    private int             mHexSide = 8;
    // system
    private SystemBean      mSystem;
    private BodyBean        mFocusWorld;
    private BodyBean        mCursorWorld;
    private boolean         mDisplayList;
    private DateBean        mDate          = new DateBean(
            1100 * DateBean.ONE_YEAR);
    // surface
    private SurfaceBean     mSurface;

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

    public MainWorldBean getFocusMainWorld()
    {
        return mFocusMainWorld;
    }

    public void setFocusMainWorld(MainWorldBean focusMainWorld)
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

    public MainWorldBean getCursorMainWorld()
    {
        return mCursorMainWorld;
    }

    public void setCursorMainWorld(MainWorldBean cursorMainWorld)
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

    public SystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SystemBean system)
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

    public SectorBean getFocusSector()
    {
        return mFocusSector;
    }

    public void setFocusSector(SectorBean focusSector)
    {
        queuePropertyChange("focusSector", mFocusSector, focusSector);
        mFocusSector = focusSector;
        firePropertyChange();
    }

    public SubSectorBean getFocusSubSector()
    {
        return mFocusSubSector;
    }

    public void setFocusSubSector(SubSectorBean focusSubSector)
    {
        queuePropertyChange("focusSubSector", mFocusSubSector, focusSubSector);
        mFocusSubSector = focusSubSector;
        firePropertyChange();
    }

    public int getHexSide()
    {
        return mHexSide;
    }

    public void setHexSide(int hexSide)
    {
        mHexSide = hexSide;
    }
}
