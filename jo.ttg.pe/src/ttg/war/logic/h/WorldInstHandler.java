package ttg.war.logic.h;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import ttg.war.beans.WorldInst;

public class WorldInstHandler implements IToJSONHandler, IFromJSONHandler
{
    private Set<String> mSkips = new HashSet<>();

    public WorldInstHandler()
    {
        mSkips.add("ships");
        mSkips.add("shipsEnRoute");
        mSkips.add("world");
        mSkips.add("side");
    }
    
    @Override
    public boolean isHandler(Object o)
    {
        return o instanceof WorldInst;
    }

    @Override
    public Object toJSON(Object bean)
    {
        WorldInst world = (WorldInst)bean;
        JSONObject json = BeanHandler.doToJSON(bean, mSkips);
        json.put("ref", GameInstHandler.getWorldRef(world));
        json.put("sideRef", GameInstHandler.getSideRef(world.getSide()));
        json.put("shipsRef", GameInstHandler.getShipsRef(world.getShips()));
        json.put("shipsEnRouteRef", GameInstHandler.getShipsRef(world.getShipsEnRoute()));
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        return WorldInst.class.getName().equals(((JSONObject)json).get("$beanclass"));
    }

    @Override
    public Object fromJSON(Object j, Class<?> hint)
    {
        WorldInst world = GameInstHandler.getWorld(((JSONObject)j).getString("ref"));
        fromJSONInto(j, world);
        return world;
    }

    @Override
    public void fromJSONInto(Object j, Object bean)
    {
        WorldInst world = (WorldInst)bean;
        BeanHandler.doFromJSONInto(j, bean, mSkips);
        JSONObject json = (JSONObject)j;
        world.setWorld(GameInstHandler.getMainWorld(world.getOrds()));
        world.setSide(GameInstHandler.getSide(json.getString("sideRef")));
        world.setShips(GameInstHandler.getShips((JSONArray)json.get("shipsRef")));
        world.setShipsEnRoute(GameInstHandler.getShips((JSONArray)json.get("shipsEnRouteRef")));
    }

}
