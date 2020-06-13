package jo.util.spline;

import jo.util.utils.MathUtils;

public class Triangle
{
	public double[]	mX;
	public double[]	mY;

	public Triangle(double x1, double y1, double x2, double y2, double x3, double y3)
	{
		this();
		double dx1 = x2 - x1;
		double dx2 = x3 - x1;
		double dy1 = y2 - y1;
		double dy2 = y3 - y1;
		double cross = dx1*dy2 - dx2*dy1;
		boolean ccw = (cross > 0);
		if (ccw)
		{
			mX[0] = x1;
			mX[1] = x2;
			mX[2] = x3;
			mY[0] = y1;
			mY[1] = y2;
			mY[2] = y3;
		} else
		{
			mX[0] = x1;
			mX[1] = x3;
			mX[2] = x2;
			mY[0] = y1;
			mY[1] = y3;
			mY[2] = y2;
		}
	}

	public Triangle()
	{
		mX = new double[3];
		mY = new double[3];
	}

	public boolean isInside(double x, double y)
	{
		double vx2 = x - mX[0];
		double vy2 = y - mY[0];
		double vx1 = mX[1] - mX[0];
		double vy1 = mY[1] - mY[0];
		double vx0 = mX[2] - mX[0];
		double vy0 = mY[2] - mY[0];

		double dot00 = vx0 * vx0 + vy0 * vy0;
		double dot01 = vx0 * vx1 + vy0 * vy1;
		double dot02 = vx0 * vx2 + vy0 * vy2;
		double dot11 = vx1 * vx1 + vy1 * vy1;
		double dot12 = vx1 * vx2 + vy1 * vy2;
		double invDenom = 1.0f / (dot00 * dot11 - dot01 * dot01);
		double u = (dot11 * dot02 - dot01 * dot12) * invDenom;
		double v = (dot00 * dot12 - dot01 * dot02) * invDenom;

		return ((u > 0) && (v > 0) && (u + v < 1));
	}
	
	public boolean isColinear()
	{
		return MathUtils.isColinear(mX[0], mY[0], mX[1], mY[1], mX[2], mY[2]);
	}
	
	public double area()
	{
		return MathUtils.area(mX[0], mY[0], mX[1], mY[1], mX[2], mY[2]);
	}

	public double[] getX()
	{
		return mX;
	}

	public double[] getY()
	{
		return mY;
	}

	public double getX(int i)
	{
		return mX[i];
	}

	public double getY(int i)
	{
		return mY[i];
	}

	public String toString()
	{
		return "("+mX[0]+","+mY[0]+")--("+mX[1]+","+mY[1]+")--("+mX[2]+","+mY[2]+")";
	}
}
