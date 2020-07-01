package ttg.war.logic.h;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.mw.MainWorldLogic;
import ttg.war.beans.GameInst;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.view.War;

public class GameInstHandler implements IToJSONHandler, IFromJSONHandler
{
    private Set<String> mSkips = new HashSet<>();
    private List<String> mFirst = new ArrayList<>();
    private List<String> mLast = new ArrayList<>();
    
    private static Map<ShipInst, String> mShip2Refs = new HashMap<>();
    private static Map<SideInst, String> mSide2Refs = new HashMap<>();
    private static Map<WorldInst, String> mWorld2Refs = new HashMap<>();

    public GameInstHandler()
    {
        mSkips.add("scheme");
        mFirst.add("ships");
        mLast.add("worlds");
    }
    
    @Override
    public boolean isHandler(Object o)
    {
        return o instanceof GameInst;
    }

    @Override
    public Object toJSON(Object bean)
    {
        indexGame(bean);
        JSONObject json = BeanHandler.doToJSON(bean, mSkips, mFirst, mLast);
        json.put("version", War.VERSION);
        clearToRefs();
        return json;
    }

    private void indexGame(Object bean)
    {
        GameInst game = (GameInst)bean;
        int idx = 1000;
        clearToRefs();
        for (ShipInst ship : game.getShips())
            mShip2Refs.put(ship, String.valueOf(idx++));
        for (SideInst side : game.getSides())
            mSide2Refs.put(side, String.valueOf(idx++));
        for (WorldInst world : game.getWorlds().values())
            mWorld2Refs.put(world, String.valueOf(idx++));
    }

    private void clearToRefs()
    {
        mShip2Refs.clear();
        mSide2Refs.clear();
        mWorld2Refs.clear();
    }

    static String getShipRef(ShipInst ship)
    {
        return mShip2Refs.get(ship);
    }

    @SuppressWarnings("unchecked")
    static JSONArray getShipsRef(Collection<ShipInst> ships)
    {
        JSONArray json = new JSONArray();
        for (ShipInst ship : ships)
            json.add(getShipRef(ship));
        return json;
    }

    static String getSideRef(SideInst side)
    {
        return mSide2Refs.get(side);
    }

    static String getWorldRef(WorldInst world)
    {
        return mWorld2Refs.get(world);
    }

    @SuppressWarnings("unchecked")
    static JSONArray getWorldsRef(Collection<WorldInst> worlds)
    {
        JSONArray json = new JSONArray();
        for (WorldInst world : worlds)
            json.add(getWorldRef(world));
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        return GameInst.class.getName().equals(((JSONObject)json).get("$beanclass"));
    }

    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        throw new IllegalStateException("Shouldn't be here");
    }

    private static GameInst mGame;
    private static Map<String, ShipInst> mRef2Ship = new HashMap<>();
    private static Map<String, SideInst> mRef2Side = new HashMap<>();
    private static Map<String, WorldInst> mRef2World = new HashMap<>();

    private void clearFromRefs()
    {
        mGame = null;
        mRef2Ship.clear();
        mRef2Side.clear();
        mRef2World.clear();
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        clearFromRefs();
        mGame = (GameInst)bean;
        BeanHandler.doFromJSONInto(json, bean, mSkips);
        clearFromRefs();
    }
    
    static MainWorldBean getMainWorld(OrdBean ords)
    {
        return MainWorldLogic.getFromOrds(mGame.getScheme(), ords);
    }
    
    static WorldInst getWorld(String ref)
    {
        if (!mRef2World.containsKey(ref))
            mRef2World.put(ref, new WorldInst());
        return mRef2World.get(ref);
    }
    
    static List<WorldInst> getWorlds(JSONArray array)
    {
        List<WorldInst> worlds = new ArrayList<>();
        for (Object ref : array)
            worlds.add(getWorld((String)ref));
        return worlds;
    }
    
    static SideInst getSide(String ref)
    {
        if (!mRef2Side.containsKey(ref))
            mRef2Side.put(ref, new SideInst());
        return mRef2Side.get(ref);
    }
    
    static ShipInst getShip(String ref)
    {
        if (!mRef2Ship.containsKey(ref))
            mRef2Ship.put(ref, new ShipInst());
        return mRef2Ship.get(ref);
    }
    
    static List<ShipInst> getShips(JSONArray array)
    {
        List<ShipInst> ships = new ArrayList<>();
        for (Object ref : array)
            ships.add(getShip((String)ref));
        return ships;
    }
}
