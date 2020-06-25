package ttg.adv.logic.h;

import java.util.HashSet;
import java.util.Set;

import org.json.simple.IToJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import ttg.adv.beans.Game;

public class GameHandler implements IToJSONHandler
{

    @Override
    public boolean isHandler(Object o)
    {
        return o instanceof Game;
    }

    @Override
    public Object toJSON(Object o)
    {
        Set<String> skips = new HashSet<>();
        skips.add("scheme");
        JSONObject json = BeanHandler.doToJSON(o, skips);
        return json;
    }
}
