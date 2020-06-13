package jo.ttg.gen.sw.h;

import org.json.simple.IFromJSONHandler;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.imp.ImpGenSystem;
import jo.ttg.logic.gen.SchemeLogic;

public class SystemHandler implements IFromJSONHandler
{
    @Override
    public boolean isHandler(Object json, Class<?> hint)
    {
        if ((json instanceof JSONObject) && hint.isAssignableFrom(SystemBean.class))
            return true;
        return false;
    }

    @Override
    public Object fromJSON(Object json, Class<?> hint)
    {
        SystemBean bean = ((ImpGenSystem)SchemeLogic.getDefaultScheme().getGeneratorSystem()).newSystemBean();
        fromJSONInto(json, bean);
        return bean;
    }

    @Override
    public void fromJSONInto(Object json, Object bean)
    {
        BeanHandler.doFromJSONInto(json, bean);
        SystemBean sys = (SystemBean)bean;
        setSystem(sys, null, sys.getSystemRoot());
    }
    
    private void setSystem(SystemBean sys, BodyBean parent, BodyBean body)
    {
        body.setSystem(sys);
        body.setPrimary(parent);
        for (BodyBean c : body.getAllSatelites())
            setSystem(sys, body, c);
    }
}
