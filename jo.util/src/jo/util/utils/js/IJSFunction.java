package jo.util.utils.js;

import java.util.Map;

public interface IJSFunction
{
    public String[] getNames();
    public Object evaluate(String name, Object[] args, Map<String,Object> props) throws JSEvaluationException;
}
