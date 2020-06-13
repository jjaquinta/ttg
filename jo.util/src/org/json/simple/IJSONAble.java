package org.json.simple;

public interface IJSONAble
{
    public JSONObject   toJSON();
    public void fromJSON(JSONObject o);
}