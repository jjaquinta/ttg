/*
 * Created on Nov 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic;

import java.util.StringTokenizer;

import jo.ttg.beans.LocBean;
import jo.util.utils.obj.DoubleUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class LocLogic
{
	/**
	 * Add two ords
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean sum of the two
	 */
	public static LocBean add(double x1, double y1, double z1, double x2, double y2, double z2)
	{
		return new LocBean(x1 + x2, y1 + y2, z1 + z2);
	}

	/**
	 * Add two ords
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean sum of the two
	 */
	public static LocBean add(LocBean l, double _x, double _y, double _z)
	{
		return add(l.getX(), l.getY(), l.getZ(), _x, _y, _z);
	}

	/**
	 * Add two ords
	 * @param o
	 * @return LocBean
	 */
	public static LocBean add(LocBean l1, LocBean l2)
	{
		return add(l1.getX(), l1.getY(), l1.getZ(), l2.getX(), l2.getY(), l2.getZ());
	}

	/**
	 * Add two ords
	 * @param o
	 * @return LocBean
	 */
	public static LocBean diff(LocBean l1, LocBean l2)
	{
		return add(l1.getX(), l1.getY(), l1.getZ(), -l2.getX(), -l2.getY(), -l2.getZ());
	}

	/**
	 * Divide us by another ord.
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean
	 */
	public static LocBean div(LocBean l, double _x, double _y, double _z)
	{
		l.setX(l.getX() / _x);
		l.setY(l.getY() / _y);
		l.setZ(l.getZ() / _z);
		return l;
	}

	/**
	 * Divide us by another ord.
	 * @param o
	 * @return LocBean
	 */
	public static LocBean div(LocBean l, LocBean o)
	{
		return div(l, o.getX(), o.getY(), o.getZ());
	}
	/**
	 * Method equals.
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return boolean
	 */
	public static boolean equals(LocBean l, double _x, double _y, double _z)
	{
		return DoubleUtils.equals(l.getX(), _x) && DoubleUtils.equals(l.getY(), _y) && DoubleUtils.equals(l.getZ(), _z);
	}

	/**
	 * Extract values from a string.
	 * @param o
	 */
	public static LocBean fromString(String o)
	{
		if (o.charAt(0) == '[')
			o = o.substring(1);
		if (o.endsWith("]"))
			o = o.substring(0, o.length() - 1);
		StringTokenizer st = new StringTokenizer(o, ",");
		double x = 0;
		double y = 0;
		double z = 0;
		if (st.hasMoreTokens())
			x = DoubleUtils.parseDouble(st.nextToken());
		if (st.hasMoreTokens())
			y = DoubleUtils.parseDouble(st.nextToken());
		if (st.hasMoreTokens())
			z = DoubleUtils.parseDouble(st.nextToken());
		return new LocBean(x, y, z);
	}

	/**
	 * Get as loop increment.
	 * @return int
	 */
	public static int i(LocBean l)
	{
		return (int) l.getX();
	}

	/**
	 * Increment us by an ord.
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean
	 */
	public static LocBean incr(LocBean l, double _x, double _y, double _z)
	{
		l.setX(l.getX()+_x);
		l.setY(l.getY()+_y);
		l.setZ(l.getZ()+_z);
		return l;
	}

	/**
	 * Increment us by an ord.
	 * @param o
	 * @return LocBean
	 */
	public static LocBean incr(LocBean l, LocBean o)
	{
		return incr(l, o.getX(), o.getY(), o.getZ());
	}
	/**
	 * Use us to index into an array.
	 * @param a
	 * @return Object
	 */
	public static Object indexOf(LocBean l, Object a[][][])
	{
		return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
	}
	/**
	 * Use us to index into an array.
	 * @param a
	 * @return boolean
	 */
	public static boolean indexOf(LocBean l, boolean a[][][])
	{
		return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
	}
	/**
	 * Use us to index into an array.
	 * @param a
	 * @return int
	 */
	public static int indexOf(LocBean l, int a[][][])
	{
		return a[(int) l.getX()][(int) l.getY()][(int) l.getZ()];
	}
	/**
	 * Use us to set a value in an array.
	 * @param a
	 * @param o
	 * @return Object
	 */
	public static Object indexSet(LocBean l, Object a[][][], Object o)
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
	public static int indexSet(LocBean l, int a[][][], int o)
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
	public static boolean indexSet(LocBean l, boolean a[][][], boolean o)
	{
		a[(int) l.getX()][(int) l.getY()][(int) l.getZ()] = o;
		return o;
	}
	/**
	 * Get as loop increment.
	 * @return int
	 */
	public static int j(LocBean l)
	{
		return (int) l.getY();
	}
	/**
	 * Get as loop increment.
	 * @return int
	 */
	public static int k(LocBean l)
	{
		return (int) l.getZ();
	}
	/**
	 * Termination check for looping.
	 * @param wrt
	 * @return boolean
	 */
	public static boolean loopDone(LocBean l, LocBean wrt)
	{
		return (l.getX() >= wrt.getX());
	}
	/**
	 * Incrementation for looping.
	 * @param wrt
	 */
	public static void loopIncr2D(LocBean l, LocBean wrt)
	{
		l.setY(l.getY() + 1);
		if (l.getY() >= wrt.getY())
		{
			l.setY(0);
			l.setX(l.getX() + 1);
		}
	}
	/**
	 * Incrementation for looping.
	 * @param wrt
	 */
	public static void loopIncr3D(LocBean l, LocBean wrt)
	{
		l.setZ(l.getZ() + 1);
		if (l.getZ() >= wrt.getZ())
		{
			l.setZ(0);
			l.setY(l.getY() + 1);
			if (l.getY() >= wrt.getY())
			{
				l.setY(0);
				l.setX(l.getX() + 1);
			}
		}
	}
	/**
	 * Incrementation for looping.
	 * @param wrt
	 * @param delta
	 */
	public static void loopIncr3D(LocBean l, LocBean wrt, LocBean delta)
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
	public static void loopStart(LocBean l)
	{
		l.setX(0);
		l.setY(0);
		l.setZ(0);
	}
	/**
	 * Calculate magnitude.
	 * @return double
	 */
	public static double getMag(LocBean l)
	{
		return Math.sqrt(getMag2(l));
	}
	/**
	 * Calculate magnitude squared.
	 * @return double
	 */
	public static double getMag2(LocBean l)
	{
		return l.getX()*l.getX() + l.getY()*l.getY() + l.getZ()*l.getZ();
	}

	public static LocBean makeNorm(LocBean l)
	{
		double mag = getMag(l);
		if (mag > 0)
		{
			l.setX(l.getX() / mag);
			l.setY(l.getY() / mag);
			l.setZ(l.getZ() / mag);
		}
		return l;
	}

	/**
	 * Perform modulus on ourselves.
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean
	 */
	public static LocBean mod(LocBean l, double _x, double _y, double _z)
	{
		l.setX(l.getX() % _x);
		l.setY(l.getY() % _y);
		l.setZ(l.getZ() % _z);
		return l;
	}

	/**
	 * Perform modulus on ourselves.
	 * @param o
	 * @return LocBean
	 */
	public static LocBean mod(LocBean l, LocBean o)
	{
		return mod(l, o.getX(), o.getY(), o.getZ());
	}

	/**
	 * Multiply us by ord.
	 * @param _x
	 * @param _y
	 * @param _z
	 * @return LocBean
	 */
	public static LocBean mult(LocBean l, double _x, double _y, double _z)
	{
		l.setX(l.getX() * _x);
		l.setY(l.getY() * _y);
		l.setZ(l.getZ() * _z);
		return l;
	}

	/**
	 * Multiply us by ord.
	 * @param o
	 * @return LocBean
	 */
	public static LocBean mult(LocBean l, LocBean o)
	{
		return mult(l, o.getX(), o.getY(), o.getZ());
	}

	/**
	 * Get by index.
	 * @param n
	 * @return double
	 */
	public static double o(LocBean l, int n)
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
	 * @return LocBean
	 */
	public static LocBean set(LocBean l, int i, double v)
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
	 * @return LocBean
	 */
	public static LocBean set(LocBean l, double _x, double _y, double _z)
	{
		l.setX(_x);
		l.setY(_y);
		l.setZ(_z);
		return l;
	}
	/**
	 * Set by ord.
	 * @param o
	 * @return LocBean
	 */
	public static LocBean set(LocBean l, LocBean o)
	{
		return set(l, o.getX(), o.getY(), o.getZ());
	}
	/**
	 * Calculate offset into single array.
	 * @param wrt
	 * @return int
	 */
	public static int toOff(LocBean l, LocBean wrt)
	{
		return (int) (l.getX() + l.getY() * wrt.getX() + l.getZ() * wrt.getX() * wrt.getY());
	}
	/**
	 * Is point within two other points.
	 * @param p1 Upper Bound
	 * @param p2 Lower Bound
	 * @return boolean
	 */
	public static boolean within(LocBean l, LocBean p1, LocBean p2)
	{
		boolean ret = true;
		ret &= within(l.getX(), p1.getX(), p2.getX());
		ret &= within(l.getY(), p1.getY(), p2.getY());
		ret &= within(l.getZ(), p1.getZ(), p2.getZ());
		return ret;
	}
	private static boolean within(double v, double v1, double v2)
	{
		boolean ret;
		if (v1 < v2)
			ret = (v1 <= v) && (v < v2);
		else if (v2 > v1)
			ret = (v2 <= v) && (v < v1);
		else
			ret = DoubleUtils.equals(v, v1);
		return ret;
	}

	public static double dist(LocBean l, LocBean o2)
	{
		double dx = l.getX() - o2.getX();
		double dy = l.getY() - o2.getY();
		double dz = l.getZ() - o2.getZ();
		double dist2 = dx*dx + dy*dy + dz*dz;
		return Math.sqrt(dist2);
	}

	public static String getShortNum(LocBean l)
	{
		StringBuffer ret = new StringBuffer();
		double x = l.getX()%32;
		if (x < 0)
			x = 32 + x;
		if (x < 9)
			ret.append('0');
		ret.append(x+1);
		double y = l.getY()%40;
		if (y < 0)
			y = 40 + y;
		if (y < 9)
			ret.append('0');
		ret.append(y+1);
		return ret.toString();
	}

	static private double interpolate(double pc, double low, double high)
	{
		return low + pc*(high - low);
	}

	static public LocBean interpolate(double val, double low, double high, LocBean bLow, LocBean bHigh)
	{
		double pc = (val - low)/(high - low);
		return new LocBean(interpolate(pc, bLow.getX(), bHigh.getX()),
						   interpolate(pc, bLow.getY(), bHigh.getY()),
						   interpolate(pc, bLow.getZ(), bHigh.getZ()));
	}
}
