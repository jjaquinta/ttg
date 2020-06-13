package org.json.simple;

public interface IToJSONHandler
{
    public boolean      isHandler(Object o);
    public Object       toJSON(Object o);
}