package ttg.war.logic.h;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.IFromJSONHandler;
import org.json.simple.IToJSONHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import ttg.war.beans.ShipInst;

public class ShipInstHandler implements IToJSONHandler, IFromJSONHandler
{
    private Set<String> mSkips = new HashSet<>();

    public ShipInstHandler()
    {
        mSkips.add("sideInst");
        mSkips.add("containedBy");
        mSkips.add("contains");
        mSkips.add("location");
        mSkips.add("destination");
        mSkips.add("target");
    }
    
    @Override
    public boolean isHandler(Object o)
    {
        return o instanceof ShipInst;
    }

    @Override
    public Object toJSON(Object bean)
    {
        ShipInst ship = (ShipInst)bean;
        JSONObject json = BeanHandler.doToJSON(bean, mSkips);
        json.put("ref", GameInstHandler.getShipRef(ship));
        json.put("sideInstRef", GameInstHandler.getSideRef(ship.getSideInst()));
        json.put("containedByRef", GameInstHandler.getShipRef(ship.getContainedBy()));
        json.put("containsRef", GameInstHandler.getShipsRef(ship.getContains()));
        json.put("locationRef", GameInstHandler.getWorldRef(ship.getLocation()));
        json.put("destinationRef", GameInstHandler.getWorldRef(ship.getDestination()));
        json.put("targetRef", GameInstHandler.getShipRef(ship.getTarget()));
        return json;
    }

    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if (!(json instanceof JSONObject))
            return false;
        return ShipInst.class.getName().equals(((JSONObject)json).get("$beanclass"));
    }

    @Override
    public Object fromJSON(Object j, Class<?> hint)
    {
        ShipInst ship = GameInstHandler.getShip(((JSONObject)j).getString("ref"));
        fromJSONInto(j, ship);
        return ship;
    }

    @Override
    public void fromJSONInto(Object j, Object bean)
    {
        ShipInst ship = (ShipInst)bean;
        BeanHandler.doFromJSONInto(j, bean, mSkips);
        JSONObject json = (JSONObject)j;
        ship.setSideInst(GameInstHandler.getSide(json.getString("sideInstRef")));
        ship.setContainedBy(GameInstHandler.getShip(json.getString("containedByRef")));
        ship.setContains(GameInstHandler.getShips((JSONArray)json.get("containsRef")));
        ship.setLocation(GameInstHandler.getWorld(json.getString("locationRef")));
        ship.setDestination(GameInstHandler.getWorld(json.getString("destinationRef")));
        ship.setTarget(GameInstHandler.getShip(json.getString("targetRef")));
    }

}
