package jo.ttg.gen.sw.data;

import org.json.simple.IJSONAble;
import org.json.simple.JSONObject;
import org.json.simple.h.BeanHandler;

import jo.ttg.beans.mw.MainWorldBean;
import jo.util.geom3d.Point3D;

public class SWMainWorldBean extends MainWorldBean implements IJSONAble
{
    private Point3D mOrdsFine;
    private String  mNotes;
    
    // utilites
    public String getOrdsFineDesc()
    {
        return String.format("%.1f,%.1f,%.1f", mOrdsFine.getX(), mOrdsFine.getY(), mOrdsFine.getZ());
    }

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
    }
    
    // getters and setters

    public Point3D getOrdsFine()
    {
        return mOrdsFine;
    }

    public void setOrdsFine(Point3D ordsFine)
    {
        mOrdsFine = ordsFine;
    }

    public String getNotes()
    {
        return mNotes;
    }

    public void setNotes(String notes)
    {
        mNotes = notes;
    }

}
