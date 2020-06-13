/*
 * Created on Dec 9, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.beans.dist;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.OrdBean;
import jo.util.utils.FormatUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.StringUtils;

/**
 * @author jgrant
 *
 * Supported URIs:
 * On surface of a world:
 * sys://[x,y,z]/star/giant/world?orbit=0
 * In orbit around a world:
 * sys://[x,y,z]/star/giant/world?orbit=x
 * where 0 < x <= 100
 * Interplanetary space:
 * sys://[x,y,z]/?s=[x,y,z]
 * Interstellar space:
 * sys://[x,y,z]/?destSys=[x,y,z]&destPath=star/giant/world&timeLeft=h
 * where h is in hours
 */
public class DistLocation
{
    public String	mURI;
    public OrdBean	mOrds;
    public String[]	mPath;
    public double	mOrbit;
    public LocBean	mSpace;
    public OrdBean	mDestOrds;
    public String[]	mDestPath;
    public DateBean	mDestTimeLeft;
    
    public DistLocation()
    {
        mOrds = new OrdBean();
        mPath = new String[0];
        mSpace = new LocBean();
        mDestOrds = new OrdBean();
        mDestPath = new String[0];
        mDestTimeLeft = new DateBean();
    }
    
    public DistLocation(DistLocation copy)
    {
        mOrds = new OrdBean(copy.getOrds());
        mPath = StringUtils.copyStringArray(copy.getPath());
        mSpace = new LocBean(copy.getSpace());
        mDestOrds = new OrdBean(copy.getDestOrds());
        mDestPath = StringUtils.copyStringArray(copy.getDestPath());
        mDestTimeLeft = new DateBean(copy.getDestTimeLeft());
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object arg0)
    {
        if (this == arg0)
            return true;
        if (!(arg0 instanceof DistLocation))
            return false;
        DistLocation l2 = (DistLocation)arg0;
        if (!mOrds.equals(l2.getOrds()))
            return false;
        if (!StringUtils.compareStringArray(mPath, l2.getPath()))
            return false;
        if (!DoubleUtils.equals(mOrbit, l2.getOrbit()))
            return false;
        if (!mSpace.equals(l2.getSpace()))
            return false;
        if (!mDestOrds.equals(l2.getDestOrds()))
            return false;
        if (!StringUtils.compareStringArray(mDestPath, l2.getDestPath()))
            return false;
        if (!mDestTimeLeft.equals(l2.getDestTimeLeft()))
            return false;        
        return true;
    }
    
    @Override
    public int hashCode()
    {
        return mURI.hashCode();
    }
    
    public String toString()
    {
        if (mOrbit > 0)
            return "In the "+mOrds.toString()+" system orbit around "+StringUtils.fromStringArray(mPath, "->")+" at "+FormatUtils.formatDouble(mOrbit, 1)+" diameters";
        if (mSpace.getX() != 0)
            return "In interplanetary space in the "+mOrds.toString()+" system at "+mSpace.toString();
        if (mDestPath.length > 0)
            return "In interspace between "+mOrds.toString()+" and "+mDestOrds.toString();
        return "In the "+mOrds.toString()+" system on the surface of "+StringUtils.fromStringArray(mPath, "->");
    }
    
    /**
     * @return Returns the destOrds.
     */
    public OrdBean getDestOrds()
    {
        return mDestOrds;
    }
    /**
     * @param destOrds The destOrds to set.
     */
    public void setDestOrds(OrdBean destOrds)
    {
        mDestOrds = destOrds;
    }
    /**
     * @return Returns the destPath.
     */
    public String[] getDestPath()
    {
        return mDestPath;
    }
    /**
     * @param destPath The destPath to set.
     */
    public void setDestPath(String[] destPath)
    {
        mDestPath = destPath;
    }
    /**
     * @return Returns the destTimeLeft.
     */
    public DateBean getDestTimeLeft()
    {
        return mDestTimeLeft;
    }
    /**
     * @param destTimeLeft The destTimeLeft to set.
     */
    public void setDestTimeLeft(DateBean destTimeLeft)
    {
        mDestTimeLeft = destTimeLeft;
    }
    /**
     * @return Returns the orbit.
     */
    public double getOrbit()
    {
        return mOrbit;
    }
    /**
     * @param orbit The orbit to set.
     */
    public void setOrbit(double orbit)
    {
        mOrbit = orbit;
    }
    /**
     * @return Returns the ords.
     */
    public OrdBean getOrds()
    {
        return mOrds;
    }
    /**
     * @param ords The ords to set.
     */
    public void setOrds(OrdBean ords)
    {
        mOrds = ords;
    }
    /**
     * @return Returns the path.
     */
    public String[] getPath()
    {
        return mPath;
    }
    /**
     * @param path The path to set.
     */
    public void setPath(String[] path)
    {
        mPath = path;
    }
    /**
     * @return Returns the space.
     */
    public LocBean getSpace()
    {
        return mSpace;
    }
    /**
     * @param space The space to set.
     */
    public void setSpace(LocBean space)
    {
        mSpace = space;
    }
    /**
     * @return Returns the uRI.
     */
    public String getURI()
    {
        return mURI;
    }
    /**
     * @param uri The uRI to set.
     */
    public void setURI(String uri)
    {
        mURI = uri;
    }
}
