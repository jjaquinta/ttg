package jo.ttg.gen.sw.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.function.BiConsumer;

import javax.swing.ImageIcon;

import org.json.simple.FromJSONLogic;
import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;
import org.json.simple.ToJSONLogic;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.UPPLawBean;
import jo.ttg.beans.mw.UPPTecBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.core.report.logic.TTGReportLogic;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.IBodyViewHandler;
import jo.ttg.core.ui.swing.ctrl.body.BodyPanel;
import jo.ttg.core.ui.swing.ctrl.body.HTMLBodyPanelHandler;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.gen.sw.SWGenScheme;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.gen.sw.data.SWSystemBean;
import jo.ttg.gen.sw.data.SelectedRegionBean;
import jo.ttg.gen.sw.logic.rep.RegionListCSVReport;
import jo.ttg.gen.sw.logic.rep.RegionListHTMLReport;
import jo.ttg.gen.util.GenSchemePersistant;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.gen.SurfaceLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.ui.swing.ctrl.HTMLCtrlDetails;
import jo.util.utils.PCSBeanUtils;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;

public class RuntimeLogic
{
    private static RuntimeBean mRuntime = null;
    
    public static RuntimeBean getInstance()
    {
        if (mRuntime == null)
            init();
        return mRuntime;
    }
    
    public static void init()
    {
        mRuntime = new RuntimeBean();
        SchemeLogic.setDefaultScheme(new GenSchemePersistant(getDataDir(), new SWGenScheme()));
        loadSettings();
        HTMLCtrlDetails.addModifierFunction("distance", (val,arg) -> FormatUtils.sDistance(DoubleUtils.parseDouble(val)));
        HTMLCtrlDetails.addModifierFunction("mass", (val,arg) -> FormatUtils.sMass(DoubleUtils.parseDouble(val)));
        HTMLCtrlDetails.addModifierFunction("days", (val,arg) -> FormatUtils.sDays(DoubleUtils.parseDouble(val)));
        HTMLCtrlDetails.addModifierFunction("hours", (val,arg) -> FormatUtils.sHours(DoubleUtils.parseDouble(val)));
        HTMLCtrlDetails.addModifierFunction("temp", (val,arg) -> FormatUtils.sTemp(DoubleUtils.parseDouble(val)));
        HTMLCtrlDetails.addModifierFunction("upp", (val,arg) -> String.valueOf(FormatUtils.int2upp(IntegerUtils.parseInt(val))));
        HTMLCtrlDetails.addModifierFunction("upplaw", (val,arg) -> UPPLawBean.getValueDescription(IntegerUtils.parseInt(val)));
        HTMLCtrlDetails.addModifierFunction("upptech", (val,arg) -> UPPTecBean.getValueDescription(IntegerUtils.parseInt(val)));
        BodyView.addHandler(new IBodyViewHandler() {            
            @Override
            public Object[] getView(BodyBean b)
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ImageIcon getIcon(BodyBean b)
            {
                BufferedImage img = IconLogic.getIcon(b);
                if (img == null)
                    return null;
                return new ImageIcon(img);                        
            }
            
            @Override
            public URL getIconURI(BodyBean b)
            {
                return IconLogic.getIconURI(b);
            }
        });
        BodyPanel.setHandler(new HTMLBodyPanelHandler());
        TTGReportLogic.addReporter(new RegionListHTMLReport());
        TTGReportLogic.addReporter(new RegionListCSVReport());
    }

    static File getDataDir()
    {
        if (System.getProperty("jo.ttg.sw.data.dir") != null)
            return new File(System.getProperty("jo.ttg.sw.data.dir"));
        else
        {
            File pdir = new File(System.getProperty("user.home"), ".ttg");
            File ddir = new File(pdir, "data");
            ddir.mkdirs();
            return ddir;
        }
    }
    
    public static void term()
    {
        saveSettings();
    }
    
    private static void loadSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        File pfile = new File(pdir, "sw.json");
        if (!pfile.exists())
        {
            mRuntime.setSettings(new JSONObject());
            return;
        }
        try
        {
            JSONObject json = JSONUtils.readJSON(pfile);
            mRuntime.setSettings(json);
            serializeFromSettings();
        }
        catch (IOException e)
        {
            mRuntime.setError(e.toString());
            mRuntime.setSettings(new JSONObject());
        }
        mRuntime.setDirty(false);
    }
    
    static void saveSettings()
    {
        File pdir = new File(System.getProperty("user.home"), ".ttg");
        pdir.mkdirs();
        File pfile = new File(pdir, "sw.json");
        serializeToSettings();
        try
        {
            JSONUtils.writeJSON(pfile, mRuntime.getSettings());
        }
        catch (IOException e)
        {
            mRuntime.setError(e.toString());
        }
        mRuntime.setDirty(false);
    }
    
    private static void serializeFromSettings()
    {
        if (mRuntime.getSettings().containsKey("date"))
            setDate((DateBean)FromJSONLogic.fromJSON(mRuntime.getSettings().get("date"), DateBean.class));
        if (mRuntime.getSettings().containsKey("displayGrid"))
            setDisplayGrid(BooleanUtils.parseBoolean(mRuntime.getSettings().get("displayGrid")));
        if (mRuntime.getSettings().containsKey("displayLinks"))
            setDisplayLinks(BooleanUtils.parseBoolean(mRuntime.getSettings().get("displayLinks")));
        if (mRuntime.getSettings().containsKey("displayList"))
            setDisplayList(BooleanUtils.parseBoolean(mRuntime.getSettings().get("displayList")));
        if (mRuntime.getSettings().containsKey("zoom"))
        {
            int zoom = IntegerUtils.parseInt(mRuntime.getSettings().get("zoom"));
            if (zoom >= RuntimeBean.ZOOM_SUBSECTOR)
            {
                if (mRuntime.getSettings().containsKey("focusPoint"))
                    setFocusPoint((OrdBean)FromJSONLogic.fromJSON(mRuntime.getSettings().get("focusPoint"), OrdBean.class));
                if (mRuntime.getSettings().containsKey("cursorPoint"))
                    setCursorPoint((OrdBean)FromJSONLogic.fromJSON(mRuntime.getSettings().get("cursorPoint"), OrdBean.class));
                setZoom(RuntimeBean.ZOOM_SUBSECTOR);
            }
            if (zoom >= RuntimeBean.ZOOM_SYSTEM)
            {
                setZoom(RuntimeBean.ZOOM_SYSTEM);
                if (mRuntime.getSystem() == null)
                    setZoom(RuntimeBean.ZOOM_SUBSECTOR);
                else
                {
                    if (mRuntime.getSettings().containsKey("focusWorld"))
                    {
                        String uri = (String)mRuntime.getSettings().get("focusWorld");
                        BodyBean focusWorld = SystemLogic.findFromURI(mRuntime.getSystem(), uri);
                        mRuntime.setFocusWorld(focusWorld);
                    }
                    if (mRuntime.getSettings().containsKey("cursorWorld"))
                    {
                        String uri = (String)mRuntime.getSettings().get("cursorWorld");
                        BodyBean cursorWorld = SystemLogic.findFromURI(mRuntime.getSystem(), uri);
                        mRuntime.setCursorWorld(cursorWorld);
                    }
                }
            }
            if (zoom >= RuntimeBean.ZOOM_SURFACE)
            {
                setZoom(RuntimeBean.ZOOM_SURFACE);
            }
        }
    }
    
    private static void serializeToSettings()
    {
        if (mRuntime.getFocusPoint() != null)
            mRuntime.getSettings().put("focusPoint", ToJSONLogic.toJSON(mRuntime.getFocusPoint()));
        if (mRuntime.getCursorPoint() != null)
            mRuntime.getSettings().put("cursorPoint", ToJSONLogic.toJSON(mRuntime.getCursorPoint()));
        if (mRuntime.getFocusWorld() != null)
            mRuntime.getSettings().put("focusWorld", mRuntime.getFocusWorld().getURI());
        if (mRuntime.getCursorWorld() != null)
            mRuntime.getSettings().put("cursorWorld", mRuntime.getCursorWorld().getURI());
        if (mRuntime.getDate() != null)
            mRuntime.getSettings().put("date", ToJSONLogic.toJSON(mRuntime.getDate()));
        mRuntime.getSettings().put("zoom", mRuntime.getZoom());
        mRuntime.getSettings().put("displayGrid", mRuntime.isDisplayGrid());
        mRuntime.getSettings().put("displayLinks", mRuntime.isDisplayLinks());
        mRuntime.getSettings().put("displayList", mRuntime.isDisplayList());
    }
    
    public static Object getSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path);
    }
    
    public static boolean isSetting(String path)
    {
        return JSONUtils.get(mRuntime.getSettings(), path) != null;
    }
    
    public static void listen(String prop, BiConsumer<Object, Object> action)
    {
        PCSBeanUtils.listen(RuntimeLogic.getInstance(), prop, action);
    }
    
    public static void setFocusPoint(OrdBean o)
    {
        if ((mRuntime.getFocusPoint() == null) && (o == null))
            return;
        if (mRuntime.getFocusPoint().equals(o))
            return;
        SWMainWorldBean mw = (SWMainWorldBean)MainWorldLogic.getFromOrds(o);
        mRuntime.setFocusMainWorld(mw);
        mRuntime.setFocusPoint(o);
        mRuntime.setDirty(true);
    }
    
    public static void setCursorPoint(OrdBean o)
    {
        if ((mRuntime.getCursorPoint() == null) && (o == null))
            return;
        if ((mRuntime.getCursorPoint() != null) && mRuntime.getCursorPoint().equals(o))
            return;
        SWMainWorldBean mw = (SWMainWorldBean)MainWorldLogic.getFromOrds(o);
        mRuntime.setCursorMainWorld(mw);
        mRuntime.setCursorPoint(o);
    }
    
    public static void setFocusWorld(BodyBean o)
    {
        if ((mRuntime.getFocusWorld() == null) && (o == null))
            return;
        if ((mRuntime.getFocusWorld() != null) && mRuntime.getFocusWorld().equals(o))
            return;
        mRuntime.setFocusWorld(o);
    }
    
    public static void setCursorWorld(BodyBean o)
    {
        if ((mRuntime.getCursorWorld() == null) && (o == null))
            return;
        if ((mRuntime.getCursorWorld() != null) && mRuntime.getCursorWorld().equals(o))
            return;
        if ((o != null) && (o.getPrimary() != null))
            setFocusWorld(o.getPrimary());
        mRuntime.setCursorWorld(o);
    }
    
    public static void addToSelection(OrdBean o)
    {
        if (mRuntime.getSelectedPoints().contains(o))
            return;
        mRuntime.getSelectedPoints().add(o);
        mRuntime.fireMonotonicPropertyChange("selectedPoints");
    }
    
    public static void removeFromSelection(OrdBean o)
    {
        if (!mRuntime.getSelectedPoints().contains(o))
            return;
        mRuntime.getSelectedPoints().remove(o);
        mRuntime.fireMonotonicPropertyChange("selectedPoints");
    }
    
    public static void clearSelection()
    {
        if (mRuntime.getSelectedPoints().size() == 0)
            return;
        mRuntime.getSelectedPoints().clear();
        mRuntime.fireMonotonicPropertyChange("selectedPoints");
    }
    
    public static void setDisplayLinks(boolean v)
    {
        if (mRuntime.isDisplayLinks() == v)
            return;
        mRuntime.setDisplayLinks(v);
    }
    
    public static void setDisplayGrid(boolean v)
    {
        if (mRuntime.isDisplayGrid() == v)
            return;
        mRuntime.setDisplayGrid(v);
    }
    
    public static void setDisplayList(boolean v)
    {
        if (mRuntime.isDisplayList() == v)
            return;
        mRuntime.setDisplayList(v);
    }
    
    public static void setDate(DateBean date)
    {
        mRuntime.setDate(date);
    }

    public static void setDateDelta(int delta)
    {
        setDate(new DateBean(mRuntime.getDate().getMinutes() + delta));
    }
    
    public static void setZoom(int newZoom)
    {
        int oldZoom = mRuntime.getZoom();
        if (newZoom == oldZoom)
            return;
        if (oldZoom == RuntimeBean.ZOOM_SUBSECTOR)
        {
            if (newZoom == RuntimeBean.ZOOM_SYSTEM)
            {
                SWSystemBean sys = (SWSystemBean)SystemLogic.getFromOrds(mRuntime.getCursorPoint());
                if (sys != null)
                {
                    BodyBean mw = SystemLogic.findMainworld(sys);
                    mRuntime.setSystem(sys);
                    mRuntime.setFocusWorld(mw.getPrimary());
                    mRuntime.setCursorWorld(mw);
                    IconLogic.ensureIcons(sys);
                }
            }
        }
        else if (oldZoom == RuntimeBean.ZOOM_SYSTEM)
        {
            if (newZoom == RuntimeBean.ZOOM_SUBSECTOR)
            {
                
            }
            else if (newZoom == RuntimeBean.ZOOM_SURFACE)
            {
                BodyBean w = mRuntime.getCursorWorld();
                if (w == null)
                    return;
                SurfaceBean surface = SurfaceLogic.getFromBody(w);
                if (surface == null)
                    return;
                mRuntime.setSurface(surface);
                IconLogic.ensureIcon(surface);
            }
        }
        mRuntime.setZoom(newZoom);
    }

    public static void updateRegion(List<SWMainWorldBean> worldList,
            List<SWMainWorldBean> innerWorldList,
            List<SWMainWorldBean[]> shortLinks,
            List<SWMainWorldBean[]> longLinks)
    {
        SelectedRegionBean region = mRuntime.getRegion();
        region.setURI(mRuntime.getFocusMainWorld().getURI());
        region.getWorldList().clear();
        region.getWorldList().addAll(worldList);
        region.getInnerWorldList().clear();
        region.getInnerWorldList().addAll(innerWorldList);
        region.getShortLinks().clear();
        region.getShortLinks().addAll(shortLinks);
        region.getLongLinks().clear();
        region.getLongLinks().addAll(longLinks);
        mRuntime.fireMonotonicPropertyChange("region");
    }
}
