package jo.util.geom2d;

import jo.util.spline.CubicPolynomial;
import jo.util.spline.SplineLogic;
import jo.util.utils.MathUtils;

public class Point2DSpline
{
    private Point2D[] mCanonicalPoints;
    private CubicPolynomial[] mSplineCalcs;
    
    public Point2DSpline(Point2D[] canonicalPoints)
    {
        mCanonicalPoints = canonicalPoints;
        double[] x = new double[mCanonicalPoints.length];
        for (int i = 0; i < mCanonicalPoints.length; i++)
            x[i] = mCanonicalPoints[i].y;
        mSplineCalcs = SplineLogic.calcCubic(x);
    }
    
    public Point2D spline(double x)
    {
        if (x < mCanonicalPoints[0].x)
            return new Point2D(x, mCanonicalPoints[0].y);
        if (x > mCanonicalPoints[mCanonicalPoints.length-1].x)
            return new Point2D(x, mCanonicalPoints[mCanonicalPoints.length-1].y);
        double xv = 0;
        for (int i = 0; i < mCanonicalPoints.length - 1; i++)
            if (mCanonicalPoints[i].x <= x && x <= mCanonicalPoints[i+1].x)
            {
                xv = MathUtils.interpolate(x, mCanonicalPoints[i].x, mCanonicalPoints[i+1].x, i, i+1);
                break;
            }
        double y = SplineLogic.solveCubic(mSplineCalcs, xv);
        return new Point2D(x, y);
    }
}
