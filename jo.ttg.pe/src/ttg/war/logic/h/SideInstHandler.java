package ttg.war.logic.h;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import ttg.war.beans.SideInst;

public class SideInstHandler implements IToJSONHandler, IFromJSONHandler
{
    private Set<String> mSkips = new HashSet<>();

    public SideInstHandler()
    {
        mSkips.add("ships");
        mSkips.add("worlds");
    }
    
    @Override
    public boolean isHandler(Object o)
    {
        return o instanceof SideInst;
    }

    @Override
    public Object toJSON(Object bean)
    {
        SideInst side = (SideInst)bean;
        JSONObject json = BeanHandler.doToJSON(bean, mSkips);
        json.put("ref", GameInstHandler.getSideRef(side));
        json.put("worldsRef", GameInstHandler.getWorldsRef(side.getWorlds()));
        json.put("shipsRef", GameInstHandler.getShipsRef(side.getShips()));
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        return SideInst.class.getName().equals(((JSONObject)json).get("$beanclass"));
    }

    @Override
    public Object fromJSON(Object j, Class<?> hint)
    {
        SideInst side = GameInstHandler.getSide(((JSONObject)j).getString("ref"));
        fromJSONInto(j, side);
        return side;
    }

    @Override
    public void fromJSONInto(Object j, Object bean)
    {
        SideInst side = (SideInst)bean;
        BeanHandler.doFromJSONInto(j, bean, mSkips);
        JSONObject json = (JSONObject)j;
        side.setWorlds(GameInstHandler.getWorlds((JSONArray)json.get("worldsRef")));
        side.setShips(GameInstHandler.getShips((JSONArray)json.get("shipsRef")));
    }

}
