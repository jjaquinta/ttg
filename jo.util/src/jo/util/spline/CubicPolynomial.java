package jo.util.spline;

public class CubicPolynomial
{
    // a + b*u + c*u^2 +d*u^3
    double mA;
    double mB;
    double mC;
    double mD;

    public CubicPolynomial(double a, double b, double c, double d)
    {
        mA = a;
        mB = b;
        mC = c;
        mD = d;
    }

    /** evaluate cubic */
    public double eval(double u)
    {
        return (((mD * u) + mC) * u + mB) * u + mA;
    }
}
