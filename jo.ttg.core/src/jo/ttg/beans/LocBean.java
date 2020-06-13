package jo.ttg.beans;

import jo.util.geom3d.Point3D;

public class LocBean extends Point3D implements URIBean
{
    // X
    public double getX()
    {
        return x;
    }
    public void setX(double v)
    {
        x = v;
    }

    // Y
    public double getY()
    {
        return y;
    }
    public void setY(double v)
    {
        y = v;
    }

    // Z
    public double getZ()
    {
        return z;
    }
    public void setZ(double v)
    {
        z = v;
    }


    // constructor
    public LocBean()
    {
        super();
    }

    /**
     * Construct from ords.
     * @param _x
     * @param _y
     * @param _z
     */
    public LocBean(double _x, double _y, double _z)
    {
        super(_x, _y, _z);
    }

    /**
     * Copy constructor
     * @param o
     */
    public LocBean(LocBean o)
    {
        super(o);
    }

    // utils

    /**
     * @see java.lang.Object#clone()
     */
    public Object clone()
    {
        return new LocBean(this);
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "[" + x + "," + y + "," + z + "]";
    }
    public String getURI()
    {
        return "loc://"+toString();
    }
}
