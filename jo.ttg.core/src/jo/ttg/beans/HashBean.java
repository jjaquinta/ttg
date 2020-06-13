package jo.ttg.beans;

import jo.util.utils.obj.StringUtils;

public class HashBean extends java.util.HashMap<String, Object>
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3143176301517548464L;

    public void putNormalized(String key, Object val)
    {
        put(StringUtils.normalizePath(key), val);       
    }
    
    public Object getNormalized(String key)
    {
        key = StringUtils.normalizePath(key);
        return get(key);
    }
}
