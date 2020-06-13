/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.OrdBean;
import jo.util.utils.FormatUtils;

public class OrdLogic
{
    /**
     * Extract values from a string.
     * @param o
     */
    public static OrdBean parseString(String o)
    {
        if (o == null)
            return null;
        OrdBean ret = new OrdBean();
        if (o.charAt(0) == '[')
            o = o.substring(1);
        else if (o.startsWith("ord://["))
            o = o.substring(7);
        if (o.endsWith("]"))
            o = o.substring(0, o.length() - 1);
        int off = o.indexOf(",");
        if (off >= 0)
        {
            StringTokenizer st = new StringTokenizer(o, ",");
            if (st.hasMoreTokens())
                ret.setX(FormatUtils.parseLong(st.nextToken()));
            if (st.hasMoreTokens())
                ret.setY(FormatUtils.parseLong(st.nextToken()));
            if (st.hasMoreTokens())
                ret.setZ(FormatUtils.parseLong(st.nextToken()));
        }
        else
        {   // short string
            if (o.length() >= 2)
                ret.setX(FormatUtils.parseLong(o.substring(0, 2)));
            if (o.length() >= 4)
                ret.setY(FormatUtils.parseLong(o.substring(2, 4)));
            if (o.length() >= 6)
                ret.setZ(FormatUtils.parseLong(o.substring(4, 6)));
        }
        return ret;
    }
    /**
     * Add two ords
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean sum of the two
     */
    public static OrdBean add(OrdBean l, long _x, long _y, long _z)
    {
        return incr(((OrdBean) l.clone()), _x, _y, _z);
    }

    /**
     * Add two ords
     * @param o
     * @return OrdBean
     */
    public static OrdBean add(OrdBean l, OrdBean o)
    {
        return add(l, o.getX(), o.getY(), o.getZ());
    }

    /**
     * Divide us by another ord.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public static OrdBean div(OrdBean l, long _x, long _y, long _z)
    {
        l.setX(l.getX() / _x);
        l.setY(l.getY() / _y);
        l.setZ(l.getZ() / _z);
        return l;
    }

    /**
     * Divide us by another ord.
     * @param o
     * @return OrdBean
     */
    public static OrdBean div(OrdBean l, OrdBean o)
    {
        return div(l, o.getX(), o.getY(), o.getZ());
    }

    /**
     * Get as loop increment.
     * @return int
     */
    public static int i(OrdBean l)
    {
        return (int) l.getX();
    }

    /**
     * Increment us by an ord.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public static OrdBean incr(OrdBean l, long _x, long _y, long _z)
    {
        l.setX(l.getX() + _x);
        l.setY(l.getY() + _y);
        l.setZ(l.getZ() + _z);
        return l;
    }

    /**
     * Increment us by an ord.
     * @param o
     * @return OrdBean
     */
    public static OrdBean incr(OrdBean l, OrdBean o)
    {
        return incr(l, o.getX(), o.getY(), o.getZ());
    }
    /**
     * Use us to index into an array.
     * @param a
     * @return Object
     */
    public static Object indexOf(OrdBean l, Object a[][][])
    {
        return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
    }
    /**
     * Use us to index into an array.
     * @param a
     * @return boolean
     */
    public static boolean indexOf(OrdBean l, boolean a[][][])
    {
        return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
    }
    /**
     * Use us to index into an array.
     * @param a
     * @return int
     */
    public static int indexOf(OrdBean l, int a[][][])
    {
        return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
    }
    /**
     * Use us to set a value in an array.
     * @param a
     * @param o
     * @return Object
     */
    public static Object indexSet(OrdBean l, Object a[][][], Object o)
    {
        a[(int) l.getX()][(int) l.getY()][(int) l.getZ()] = o;
        return o;
    }
    /**
     * Use us to set a value in an array.
     * @param a
     * @param o
     * @return int
     */
    public static int indexSet(OrdBean l, int a[][][], int o)
    {
        a[(int) l.getX()][(int) l.getY()][(int) l.getZ()] = o;
        return o;
    }
    /**
     * Use us to set a value in an array.
     * @param a
     * @param o
     * @return boolean
     */
    public static boolean indexSet(OrdBean l, boolean a[][][], boolean o)
    {
        a[(int) l.getX()][(int) l.getY()][(int) l.getZ()] = o;
        return o;
    }
    /**
     * Get as loop increment.
     * @return int
     */
    public static int j(OrdBean l)
    {
        return (int) l.getY();
    }
    /**
     * Get as loop increment.
     * @return int
     */
    public static int k(OrdBean l)
    {
        return (int) l.getZ();
    }
    /**
     * Termination check for looping.
     * @param wrt
     * @return boolean
     */
    public static boolean loopDone(OrdBean l, OrdBean wrt)
    {
        return (l.getX() >= wrt.getX());
    }
    /**
     * Incrementation for looping.
     * @param wrt
     */
    public static void loopIncr2D(OrdBean l, OrdBean wrt)
    {
        l.setY(l.getY()+1);
        if (l.getY() >= wrt.getY())
        {
            l.setY(0);
            l.setX(l.getX()+1);
        }
    }
    /**
     * Incrementation for looping.
     * @param wrt
     */
    public static void loopIncr3D(OrdBean l, OrdBean wrt)
    {
        l.setZ(l.getZ()+1);
        if (l.getZ() >= wrt.getZ())
        {
            l.setZ(0);
            l.setY(l.getY()+1);
            if (l.getY() >= wrt.getY())
            {
                l.setY(0);
                l.setX(l.getX()+1);
            }
        }
    }
    /**
     * Incrementation for looping.
     * @param wrt
     * @param delta
     */
    public static void loopIncr3D(OrdBean l, OrdBean wrt, OrdBean delta)
    {
        l.setZ(l.getZ() + delta.getZ());
        if (l.getZ() >= wrt.getZ())
        {
            l.setZ(0);
            l.setY(l.getY() + delta.getY());
            if (l.getY() >= wrt.getY())
            {
                l.setY(0);
                l.setX(l.getX() + delta.getX());
            }
        }
    }
    /**
     * Set to start of increment state.
     */
    public static void loopStart(OrdBean l)
    {
        l.setX(0);
        l.setY(0);
        l.setZ(0);
    }
    /**
     * Calculate magnitude.
     * @return long
     */
    public static long getMag(OrdBean l)
    {
        return l.getX() * l.getY() * l.getZ();
    }
    /**
     * Perform modulus on ourselves.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public static OrdBean mod(OrdBean l, long _x, long _y, long _z)
    {
        l.setX(l.getX() % _x);
        l.setY(l.getY() % _y);
        l.setZ(l.getZ() % _z);
        return l;
    }

    /**
     * Perform modulus on ourselves.
     * @param o
     * @return OrdBean
     */
    public static OrdBean mod(OrdBean l, OrdBean o)
    {
        return mod(l, o.getX(), o.getY(), o.getZ());
    }

    /**
     * Multiply us by ord.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public static OrdBean mult(OrdBean l, long _x, long _y, long _z)
    {
        l.setX(l.getX() * _x);
        l.setY(l.getY() * _y);
        l.setZ(l.getZ() * _z);
        return l;
    }

    /**
     * Multiply us by ord.
     * @param o
     * @return OrdBean
     */
    public static OrdBean mult(OrdBean l, OrdBean o)
    {
        return mult(l, o.getX(), o.getY(), o.getZ());
    }

    /**
     * Get by index.
     * @param n
     * @return long
     */
    public static long o(OrdBean l, int n)
    {
        if (n == 0)
            return l.getX();
        if (n == 1)
            return l.getY();
        return l.getZ();
    }

    /**
     * Set by index.
     * @param i
     * @param v
     * @return OrdBean
     */
    public static OrdBean set(OrdBean l, int i, long v)
    {
        if (i == 0)
            l.setX(v);
        else if (i == 1)
            l.setY(v);
        else
            l.setZ(v);
        return l;
    }
    /**
     * Set by ords.
     * @param _x
     * @param _y
     * @param _z
     * @return OrdBean
     */
    public static OrdBean set(OrdBean l, long _x, long _y, long _z)
    {
        l.setX(_x);
        l.setY(_y);
        l.setZ(_z);
        return l;
    }
    /**
     * Set by ord.
     * @param o
     * @return OrdBean
     */
    public static OrdBean set(OrdBean l, OrdBean o)
    {
        return set(l, o.getX(), o.getY(), o.getZ());
    }
    /**
     * Calculate offset into single array.
     * @param wrt
     * @return int
     */
    public static int toOff(OrdBean l, OrdBean wrt)
    {
        return (int) (l.getX() + l.getY() * wrt.getX() + l.getZ() * wrt.getX() * wrt.getY());
    }
    /**
     * Do areas intersect.
     * @param ub1 Upper Bound first area
     * @param lb1 Lower Bound first area
     * @param ub2 Upper Bound second area
     * @param lb2 Lower Bound second area
     * @return boolean
     * A.LO <= B.HI && A.HI >= B.LO
     */
    public static boolean intersects(OrdBean ub1, OrdBean lb1, OrdBean ub2, OrdBean lb2)
    {
    	boolean intersect = (ub1.getX() <= lb2.getX()) && (lb1.getX() >= ub2.getX())  
    		&& (ub1.getY() <= lb2.getY()) && (lb1.getY() >= ub2.getY())
    		&& (ub1.getZ() <= lb2.getZ()) && (lb1.getZ() >= ub2.getZ());
    	return intersect;
    	/*
        // check if points of first cube within second cube
        if (       within(ub1.getX(), ub1.getY(), ub1.getZ(), ub2, lb2)
                || within(ub1.getX(), ub1.getY(), lb1.getZ(), ub2, lb2)
                || within(ub1.getX(), lb1.getY(), ub1.getZ(), ub2, lb2)
                || within(ub1.getX(), lb1.getY(), lb1.getZ(), ub2, lb2)
                || within(lb1.getX(), ub1.getY(), ub1.getZ(), ub2, lb2)
                || within(lb1.getX(), ub1.getY(), lb1.getZ(), ub2, lb2)
                || within(lb1.getX(), lb1.getY(), ub1.getZ(), ub2, lb2)
                || within(lb1.getX(), lb1.getY(), lb1.getZ(), ub2, lb2)
                )
        {
        	System.out.println("Yes");
        	return true;
        }
        // check if points of second cube within first cube
        if (       within(ub2.getX(), ub2.getY(), ub2.getZ(), ub1, lb1)
                || within(ub2.getX(), ub2.getY(), lb2.getZ(), ub1, lb1)
                || within(ub2.getX(), lb2.getY(), ub2.getZ(), ub1, lb1)
                || within(ub2.getX(), lb2.getY(), lb2.getZ(), ub1, lb1)
                || within(lb2.getX(), ub2.getY(), ub2.getZ(), ub1, lb1)
                || within(lb2.getX(), ub2.getY(), lb2.getZ(), ub1, lb1)
                || within(lb2.getX(), lb2.getY(), ub2.getZ(), ub1, lb1)
                || within(lb2.getX(), lb2.getY(), lb2.getZ(), ub1, lb1)
                )
        {
        	System.out.println("Yes");
        	return true;
        }
       	System.out.println("No");
        return false;
        */
    }
    /**
     * Is point within two other points.
     * @param p1 Upper Bound
     * @param p2 Lower Bound
     * @return boolean
     */
    public static boolean within(OrdBean l, OrdBean p1, OrdBean p2)
    {
        boolean ret = true;
        ret &= within(l.getX(), p1.getX(), p2.getX());
        ret &= within(l.getY(), p1.getY(), p2.getY());
        ret &= within(l.getZ(), p1.getZ(), p2.getZ());
        return ret;
    }
    /**
     * Is point within two other points.
     * @param p1 Upper Bound
     * @param p2 Lower Bound
     * @return boolean
     */
    public static boolean within(long x, long y, long z, OrdBean p1, OrdBean p2)
    {
        boolean ret = true;
        ret &= within(x, p1.getX(), p2.getX());
        ret &= within(y, p1.getY(), p2.getY());
        ret &= within(z, p1.getZ(), p2.getZ());
        return ret;
    }
    private static boolean within(long v, long v1, long v2)
    {
        boolean ret;
        if (v1 < v2)
            ret = (v1 <= v) && (v < v2);
        else if (v2 > v1)
            ret = (v2 <= v) && (v < v1);
        else
            ret =  v == v1;
        return ret;
    }

    public static long dist(OrdBean o1, OrdBean o2)
    {
        long dx = o1.getX() - o2.getX();
        long dy = o1.getY() - o2.getY();
        long dz = o1.getZ() - o2.getZ();
        long dist2 = dx*dx + dy*dy + dz*dz;
        return (long)Math.sqrt(dist2);
    }

    public static double dist2D(OrdBean o1, OrdBean o2)
    {
        double x1 = o1.getX()*.866;
        double y1 = o1.getY();
        if (o1.getX()%2 != 0)
            y1 += .5;
        double x2 = o2.getX()*.866;
        double y2 = o2.getY();
        if (o2.getX()%2 != 0)
            y2 += .5;
        double dx = x1 - x2;
        double dy = y1 - y2;
        double dist2 = dx*dx + dy*dy;
        return Math.sqrt(dist2);
    }
    /**
     * Is point within radius of another point.
     * @param l point
     * @param c center point
     * @param r radius
     * @return boolean
     */
    public static boolean within(OrdBean l, OrdBean c, long r)
    {
        return dist2D(l, c) <= r;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public static String toFileSafeString(OrdBean l)
    {
        return Long.toHexString(l.getX()) + "_" + Long.toHexString(l.getY()) + "_" + Long.toHexString(l.getZ());
    }

    public static String getShortNum(OrdBean l)
    {
        StringBuffer ret = new StringBuffer();
        long x = l.getX()%32;
        if (x < 0)
            x = 32 + x;
        if (x < 9)
            ret.append('0');
        ret.append(x+1);
        long y = l.getY()%40;
        if (y < 0)
            y = 40 + y;
        if (y < 9)
            ret.append('0');
        ret.append(y+1);
        return ret.toString();
    }

    public static void setShortNum(OrdBean o, String shortNum)
    {
        int x = Integer.parseInt(shortNum.substring(0, 2));
        int y = Integer.parseInt(shortNum.substring(2, 4));
        o.setX(o.getX() - (o.getX()%32) + x - 1);
        o.setY(o.getY() - (o.getY()%40) + y - 1);
    }
    
    public static boolean equals(OrdBean o1, OrdBean o2)
    {
        if (o1 == null)
            return (o2 == null);
        else
            if (o2 == null)
                return false;
            else
                return o1.equals(o2);
    }
    
    public static Iterator<OrdBean> getIterator2D(OrdBean o1, OrdBean o2)
    {
        return getIterator2D(o1, o2, 1, 1);
    }
    
    public static Iterator<OrdBean> getIterator2D(OrdBean o1, OrdBean o2, long dx, long dy)
    {
        long lx = Math.min(o1.getX(), o2.getX());
        long hx = Math.max(o1.getX(), o2.getX());
        long ly = Math.min(o1.getY(), o2.getY());
        long hy = Math.max(o1.getY(), o2.getY());
        List<OrdBean> ords = new ArrayList<OrdBean>();
        for (long x = lx; x < hx; x += dx) 
            for (long y = ly; y < hy; y += dy)
            {
                OrdBean o = new OrdBean(x, y, o1.getZ());
                ords.add(o);
            }
        return ords.iterator();
    }
    public static boolean isWithin(OrdBean o, OrdBean upperBound,
            OrdBean lowerBound)
    {
        if ((o.getX() < upperBound.getX())
                || (o.getX() >= lowerBound.getX())
                || (o.getY() < upperBound.getY())
                || (o.getY() >= lowerBound.getY()))
            return false;
        return true;
    }
}
