package jo.util.spline;

public class SplineLogic
{
    // where x = parameterized value
    public static double solveCubic(CubicPolynomial[] polys, double x)
    {
        int equasion = (int)Math.floor(x);
        if (equasion < 0)
            equasion = 0;
        else if (equasion >= polys.length)
            equasion = polys.length - 1;
        return polys[equasion].eval(x - equasion);
    }
    
    /*
     * calculates the natural cubic spline that interpolates y[0], y[1], ...
     * y[n] The first segment is returned as C[0].a + C[0].b*u + C[0].c*u^2 +
     * C[0].d*u^3 0<=u <1 the other segments are in C[1], C[2], ... C[n-1]
     */

    public static CubicPolynomial[] calcCubic(double[] x)
    {
        int n = x.length - 1;
        double[] gamma = new double[n + 1];
        double[] delta = new double[n + 1];
        double[] D = new double[n + 1];
        int i;
        /*
         * We solve the equation [2 1 ] [D[0]] [3(x[1] - x[0]) ] |1 4 1 | |D[1]|
         * |3(x[2] - x[0]) | | 1 4 1 | | . | = | . | | ..... | | . | | . | | 1 4
         * 1| | . | |3(x[n] - x[n-2])| [ 1 2] [D[n]] [3(x[n] - x[n-1])]
         * 
         * by using row operations to convert the matrix to upper triangular and
         * then back sustitution. The D[i] are the derivatives at the knots.
         */

        gamma[0] = 1.0f / 2.0f;
        for (i = 1; i < n; i++)
        {
            gamma[i] = 1 / (4 - gamma[i - 1]);
        }
        gamma[n] = 1 / (2 - gamma[n - 1]);

        delta[0] = 3 * (x[1] - x[0]) * gamma[0];
        for (i = 1; i < n; i++)
        {
            delta[i] = (3 * (x[i + 1] - x[i - 1]) - delta[i - 1]) * gamma[i];
        }
        delta[n] = (3 * (x[n] - x[n - 1]) - delta[n - 1]) * gamma[n];

        D[n] = delta[n];
        for (i = n - 1; i >= 0; i--)
        {
            D[i] = delta[i] - gamma[i] * D[i + 1];
        }

        /* now compute the coefficients of the cubics */
        CubicPolynomial[] C = new CubicPolynomial[n];
        for (i = 0; i < n; i++)
        {
            C[i] = new CubicPolynomial((double)x[i], 
                    D[i], 
                    3*(x[i + 1] - x[i]) - 2*D[i] - D[i + 1], 
                    2*(x[i] - x[i + 1]) + D[i] + D[i + 1]);
        }
        return C;
    }
}
