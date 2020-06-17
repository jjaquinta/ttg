package jo.ttg.gen.sw.data;

import jo.ttg.beans.mw.MainWorldBean;
import jo.util.geom3d.Point3D;

public class SWMainWorldBean extends MainWorldBean
{
    private Point3D mOrdsFine;
    private String  mNotes;
    
    // utilites
    public String getOrdsFineDesc()
    {
        return String.format("%.1f,%.1f,%.1f", mOrdsFine.getX(), mOrdsFine.getY(), mOrdsFine.getZ());
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
