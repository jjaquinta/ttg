package org.json.simple;

public interface IFromJSONHandler
{
    public boolean      isHandler(Object json, Class<?> hint);
    public Object       fromJSON(Object json, Class<?> hint);
    public void         fromJSONInto(Object json, Object bean);
}