package jo.ttg.gen.sw.data;

import org.json.simple.IJSONAble;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;

public class SWSystemBean extends SystemBean implements IJSONAble
{

    // IO
    
    @Override
    public JSONObject toJSON()
    {
        return BeanHandler.doToJSON(this);
    }

    @Override
    public void fromJSON(JSONObject o)
    {
        BeanHandler.doFromJSONInto(o, this);
        setSystem(null, getSystemRoot());
    }
    
    private void setSystem(BodyBean parent, BodyBean body)
    {
        body.setSystem(this);
        body.setPrimary(parent);
        for (BodyBean c : body.getSatelites())
            setSystem(body, c);
    }
}
