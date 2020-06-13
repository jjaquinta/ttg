package jo.ttg.gen.sw.logic;

import java.util.ArrayList;
import java.util.List;

import org.json.simple.FromJSONLogic;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.sw.SWGenMainWorld;
import jo.ttg.gen.sw.SWGenScheme;
import jo.ttg.gen.sw.SWGenSystem;
import jo.ttg.gen.sw.data.RuntimeBean;
import jo.ttg.gen.sw.data.SWMainWorldBean;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.gen.BodyLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.html.URIBuilder;

public class EditLogic
{

    public static void insertMainworld()
    {
        RuntimeBean rt = RuntimeLogic.getInstance();
        if (rt.getCursorMainWorld() != null)
            return;
        OrdBean ords = rt.getCursorPoint();
        SWGenScheme scheme = ((SWGenScheme)SchemeLogic.getDefaultScheme());
        SWGenMainWorld mwgen = (SWGenMainWorld)scheme.getGeneratorMainWorld();
        mwgen.insert(ords);
        SWMainWorldBean mw = (SWMainWorldBean)MainWorldLogic.getFromOrds(ords);
        rt.setCursorMainWorld(mw);
        rt.setCursorPoint(ords);
        rt.setDirty(true);
    }

    public static void deleteMainworld()
    {
        RuntimeBean rt = RuntimeLogic.getInstance();
        if (rt.getCursorMainWorld() == null)
            return;
        OrdBean ords = rt.getCursorPoint();
        SWGenScheme scheme = ((SWGenScheme)SchemeLogic.getDefaultScheme());
        SWGenMainWorld mwgen = (SWGenMainWorld)scheme.getGeneratorMainWorld();
        mwgen.erase(ords);
        rt.setCursorMainWorld(null);
        rt.setCursorPoint(ords);
        rt.setDirty(true);
    }

    public static void saveMainWorld(JSONObject json)
    {
        RuntimeBean rt = RuntimeLogic.getInstance();
        if (rt.getCursorMainWorld() == null)
            return;
        SWMainWorldBean mw = rt.getCursorMainWorld();
        mw.fromJSON(json);
        SWGenScheme scheme = ((SWGenScheme)SchemeLogic.getDefaultScheme());
        SWGenMainWorld mwgen = (SWGenMainWorld)scheme.getGeneratorMainWorld();
        mwgen.save(mw);
        rt.setCursorMainWorld(mw);
        rt.setCursorPoint(mw.getOrds());
        rt.setDirty(true);
    }

    public static void saveWorld(JSONObject json)
    {
        RuntimeBean rt = RuntimeLogic.getInstance();
        if (rt.getCursorWorld() == null)
            return;
        BodyBean body = rt.getCursorWorld();
        FromJSONLogic.fromJSONInto(json, body);
        SWGenScheme scheme = ((SWGenScheme)SchemeLogic.getDefaultScheme());
        SWGenSystem sysgen = (SWGenSystem)scheme.getGeneratorSystem();
        sysgen.save(body.getSystem());
        rt.setCursorWorld(body);
        rt.setDirty(true);
    }
    
    @SuppressWarnings("unchecked")
    public static void addBookmark(BodyBean b)
    {
        if (!RuntimeLogic.getInstance().getSettings().containsKey("bookmarks"))
            RuntimeLogic.getInstance().getSettings().put("bookmarks", new JSONArray());
        JSONArray bookmarks = (JSONArray)RuntimeLogic.getInstance().getSettings().get("bookmarks");
        bookmarks.add(b.getURI());
    }
    
    public static List<String> getBookmarks()
    {
        List<String> bms = new ArrayList<>();
        JSONArray bookmarks = (JSONArray)RuntimeLogic.getInstance().getSettings().get("bookmarks");
        if (bookmarks != null)
            for (Object o : bookmarks)
                bms.add(o.toString());
        return bms;
    }
    
    public static void gotoBookmark(String uri)
    {
        BodyBean body = BodyLogic.getFromURI(uri);
        URIBuilder u = new URIBuilder(uri);
        OrdBean o = OrdLogic.parseString(u.getAuthority());
        if (RuntimeLogic.getInstance().getZoom() == RuntimeBean.ZOOM_SURFACE)
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM);
        if (RuntimeLogic.getInstance().getZoom() == RuntimeBean.ZOOM_SYSTEM)
            RuntimeLogic.setZoom(RuntimeBean.ZOOM_SUBSECTOR);
        RuntimeLogic.setCursorPoint(o);
        RuntimeLogic.setZoom(RuntimeBean.ZOOM_SYSTEM);
        if (body.getPrimary() != null)
            RuntimeLogic.setFocusWorld(body.getPrimary());
        else
            RuntimeLogic.setFocusWorld(body);
        RuntimeLogic.setCursorWorld(body);
    }
}
