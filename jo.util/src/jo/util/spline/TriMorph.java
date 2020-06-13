package jo.util.spline;

import java.awt.geom.AffineTransform;

public class TriMorph
{
	private Triangle		mFrom;
	private Triangle		mTo;
	private AffineTransform	mMorph;
	
	public TriMorph()
	{
		mMorph = new AffineTransform();
	}
	
	private void init()
	{
		if ((mFrom != null) && (mTo != null))
			init(mFrom.getX(0), mFrom.getY(0), mTo.getX(0), mTo.getY(0),
					mFrom.getX(1), mFrom.getY(1), mTo.getX(1), mTo.getY(1),
					mFrom.getX(2), mFrom.getY(2), mTo.getX(2), mTo.getY(2));
	}
	
	public void init(double sX1, double sY1, double eX1, double eY1,
			double sX2, double sY2, double eX2, double eY2,
			double sX3, double sY3, double eX3, double eY3)
	{
	    double a1 = ((eX1-eX2)*(sY1-sY3)-(eX1-eX3)*(sY1-sY2))/
	                ((sX1-sX2)*(sY1-sY3)-(sX1-sX3)*(sY1-sY2));
	    double a2 = ((eX1-eX2)*(sX1-sX3)-(eX1-eX3)*(sX1-sX2))/
	                ((sY1-sY2)*(sX1-sX3)-(sY1-sY3)*(sX1-sX2));
	    double a3 = eX1-a1*sX1-a2*sY1;
	    double a4 = ((eY1-eY2)*(sY1-sY3)-(eY1-eY3)*(sY1-sY2))/
	                ((sX1-sX2)*(sY1-sY3)-(sX1-sX3)*(sY1-sY2));
	    double a5 = ((eY1-eY2)*(sX1-sX3)-(eY1-eY3)*(sX1-sX2))/
	                ((sY1-sY2)*(sX1-sX3)-(sY1-sY3)*(sX1-sX2));
	    double a6 = eY1-a4*sX1-a5*sY1;
	    mMorph = new AffineTransform(a1, a4, a2, a5, a3, a6);
	}

	public double[] morph(double x, double y)
	{
		double[] from = new double[] { x, y };
		morph(from);
		return from;
	}
	
	public void morph(double[] from)
	{
		morph(from, from);
	}
	
	public void morph(double[] from, double[] to)
	{
		for (int i = 0; i < from.length; i += 2)
			morph(from, to, i);		
	}
	
	public void morph(double[] from, double[] to, int i)
	{
		mMorph.transform(from, i, to, i, 1);
	}

	public Triangle getFrom()
	{
		return mFrom;
	}

	public void setFrom(Triangle from)
	{
		mFrom = from;
		init();
	}

	public Triangle getTo()
	{
		return mTo;
	}

	public void setTo(Triangle to)
	{
		mTo = to;
	}
}
