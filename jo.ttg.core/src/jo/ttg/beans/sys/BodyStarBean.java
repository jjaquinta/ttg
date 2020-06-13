/**
 * Created on Oct 16, 2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of file comments go to
 * Window>Preferences>Java>Code Generation.
 */
package jo.ttg.beans.sys;

import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.utils.ConvUtils;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class BodyStarBean extends BodyBean
{
    public BodyStarBean()
    {
        setType(BT_STAR);
    }
    
    private StarDeclBean mStarDecl;
    /**
      * Radius of inside of star
      */
    private double mInside;
    /**
      * Radius of planetary evaporation
      */
    private double mUnavailable;
    /**
      * Radius of inner zone
      */
    private double mInner;
    /**
      * Radius of outer zone
      */
    private double mOuter;

    /**
     * Returns the starDecl.
     * @return ttg.beans.mw.StarDeclBean
     */
    public StarDeclBean getStarDecl()
    {
        return mStarDecl;
    }

    /**
     * Sets the starDecl.
     * @param starDecl The starDecl to set
     */
    public void setStarDecl(StarDeclBean starDecl)
    {
        mStarDecl = starDecl;
        setupDetails();
    }

    /**
     * Returns the inner.
     * @return double
     */
    public double getInnerZone()
    {
        return mInner;
    }

    /**
     * Returns the inside.
     * @return double
     */
    public double getInside()
    {
        return mInside;
    }

    /**
     * Returns the outer.
     * @return double
     */
    public double getOuterZone()
    {
        return mOuter;
    }

    /**
     * Returns the unavailable.
     * @return double
     */
    public double getUnavailable()
    {
        return mUnavailable;
    }

    /**
     * Sets the inner.
     * @param inner The inner to set
     */
    public void setInnerZone(double inner)
    {
        mInner = inner;
    }

    /**
     * Sets the inside.
     * @param inside The inside to set
     */
    public void setInside(double inside)
    {
        mInside = inside;
    }

    /**
     * Sets the outer.
     * @param outer The outer to set
     */
    public void setOuterZone(double outer)
    {
        mOuter = outer;
    }

    /**
     * Sets the unavailable.
     * @param unavailable The unavailable to set
     */
    public void setUnavailable(double unavailable)
    {
        mUnavailable = unavailable;
    }

    // utils

    public boolean isHabitableZone(double orbit)
    {
        return ((orbit >= getInnerZone()) && (orbit <= getOuterZone()));
    }
    public boolean isInnerZone(double orbit)
    {
        return (orbit < getInnerZone());
    }
    public boolean isOuterZone(double orbit)
    {
        return (orbit > getOuterZone());
    }

    public double getLuminosity()
    {
        return calcLuminosityFromTypeAndClass(mStarDecl.getStarType(), mStarDecl.getStarClass());
    }


    public String getMiscDesc()
    {
        return mStarDecl.getDesc();
    }
    public String getOneLineDesc()
    {
        return (getName() + "              ").substring(0, 14) + getMiscDesc();
    }
//    private int getStarOff()
//    {
//        return (int) mStarDecl.getStarType() / 5;
//    }
    public double getTemperatureAt(double Orbit)
    {
        return getTemperatureAt(Orbit, 0.2, 1.0);
    }
    public double getTemperatureAt(
        double Orbit,
        double Albedeo,
        double Greenhouse)
    {
        /*
        double L, O, E, G;
        L = Luminosity();
        O = OrbitFactor(Orbit);
        E = (double)(1.0 - Albedeo);
        G = Greenhouse;
        return L*O*E*G;
        */
        double Radius, Temperature;
        Radius = convOrbitToRadius(Orbit);
        if (Radius < 0.00001) // epsilon
            return 4000.0; // maximum
        Temperature =
            (374.025
                * Greenhouse
                * (1 - Albedeo)
                * Math.pow(getLuminosity(), .25)
                / Math.pow(Radius, .5));
        return Temperature;
    }

    public String getZone(double orbit)
    {
        if (orbit < getUnavailable())
            return "-";
        else if (orbit < getInnerZone())
            return "I";
        else if (orbit < getOuterZone())
            return "H";
        return "O";
    }

    public String toString()
    {
        return getOneLineDesc();
    }

    private void setupDetails()
    {
        setMass(calcMassFromTypeAndClass(mStarDecl.getStarType(), mStarDecl.getStarClass()));
        setDiameter(calcRadiusFromTypeAndClass(mStarDecl.getStarType(), mStarDecl.getStarClass()) * 2.0);
        setDensity(calcDensityFromMassAndDiameter(getMass(), getDiameter()));
        setInside(calcDistanceFor(4000.0, 0.2, 1.0)); // todo: check these numbers!
        setUnavailable(calcDistanceFor(2731.0, 0.2, 1.0)); // todo: check these numbers!
        //  Inner = (double)DistanceFor(273.0 + 30.0);
        setInnerZone(calcDistanceFor(303.0, 0.2, 1.0));
        setOuterZone(calcDistanceFor(273.0, 0.2, 1.0));
        if (getOuterZone() - getInnerZone() < 1.0)
            // must span at least one orbit!
        {
            double dif = (1.0 - (getOuterZone() - getInnerZone())) / 2.0;
            setOuterZone(getOuterZone() + dif);
            setInnerZone(getInnerZone() - dif);
            if (getInnerZone() < getUnavailable())
                setInnerZone(getUnavailable());
        }
    }

    // static calculations

    public static int calcClassFromTypeAndMass(int _class, double _mass)
    {
        // rather than backward computing we just look for the best
        // choice by examination
        int s, bs;
        double diff, bdiff, t;

        if (_class == 60) // just a point
            return StarDeclBean.SC_D;
        bs = StarDeclBean.SC_1A;
        t = calcMassFromTypeAndClass(_class, bs);
        bdiff = (_mass > t) ? (_mass - t) : (t - _mass);
        for (s = StarDeclBean.SC_1A; s <= StarDeclBean.SC_D; s = (s + 1))
        {
            t = calcMassFromTypeAndClass(_class, s);
            diff = (_mass > t) ? (_mass - t) : (t - _mass);
            if (diff < bdiff)
            {
                bdiff = diff;
                bs = s;
            }
        }
        return bs;
    }

    public double calcDistanceFor(
        double Temperature,
        double Albedeo,
        double Greenhouse)
    {
        /*
        double L, T, E, G;
        L = Luminosity();
        T = Temperature;
        E = (double)(1.0 - Albedeo);
        G = Greenhouse;
        return OrbitFromFactor(T/(L*E*G));
        */
        double Radius, Orbit;
        Radius =
            Math.pow(
                Math.pow(getLuminosity(), .25)
                    * 374.025
                    * Greenhouse
                    * (1 - Albedeo)
                    / Temperature,
                2);
        Orbit = convRadiusToOrbit(Radius);
        return Orbit;
    }

    private static double calcLuminosityFromTypeAndClass(int Clas, int Class)
    {
        int StarOff;
        double StarMult, Diff;

        if (Clas >= 69) // M9
            return tableStellarLuminosity[Class - 1][12];
        StarOff = Clas / 5;
        if (StarOff > 1)
            StarOff -= 2;
        StarMult = (double) (Clas % 5) / 5;
        Diff =
            (tableStellarLuminosity[Class
                - 1][StarOff
                + 1]
                - tableStellarLuminosity[Class
                - 1][StarOff]);
        return (tableStellarLuminosity[Class - 1][StarOff] + Diff * StarMult);
    }

    private static double calcMassFromTypeAndClass(int Clas, int Class)
    {
        int StarOff;
        double StarMult, Diff;

        if (Clas >= 69) // M9
            return tableStellarMass[Class - 1][12];
        StarOff = Clas / 5;
        if (StarOff > 1)
            StarOff -= 2;
        StarMult = (double) (Clas % 5) / 5;
        Diff =
            (tableStellarMass[Class
                - 1][StarOff
                + 1]
                - tableStellarMass[Class
                - 1][StarOff]);
        return (tableStellarMass[Class - 1][StarOff] + Diff * StarMult);
    }
    public static double calcOrbitFactor(double Orbit)
    {
        return Math.pow(10.0, - ((Orbit) + L1) / L2);
    }
    public static double calcOrbitFromFactor(double Factor)
    {
        return - (L1 + L2 * Math.log(Factor) / Math.log(10));
    }

    public static double calcRadiusFromTypeAndClass(int Clas, int Class)
    {
        int StarOff;
        double StarMult, Diff;
        double o1, o2;

        if (Clas >= 69) // M9
            return ConvUtils.convKmToAU(tableStellarRadius[Class - 1][12] * 1000000);
        StarOff = Clas / 5;
        if (StarOff > 1)
            StarOff -= 2;
        StarMult = (double) (Clas % 5) / 5;
        o1 = tableStellarRadius[Class - 1][StarOff];
        o2 = tableStellarRadius[Class - 1][StarOff + 1];
        Diff = o2 - o1;
        return ConvUtils.convKmToAU((o1 + Diff * StarMult) * 1000000);
    }

    // tables
    private static final double tableStellarMass[][] =
        {
            {
                60.0,
                30.0,
                18.0,
                15.0,
                13.0,
                12.0,
                12.0,
                13.0,
                14.0,
                18.0,
                20.0,
                25.0,
                30.0 },
            {
            50.0,
                25.0,
                16.0,
                13.0,
                12.0,
                10.0,
                10.0,
                12.0,
                13.0,
                16.0,
                16.0,
                20.0,
                25.0 },
                {
            30.0,
                20.0,
                14.0,
                11.0,
                10.0,
                8.1,
                8.1,
                10.0,
                11.0,
                14.0,
                14.0,
                16.0,
                18.0 },
                {
            25.0,
                15.0,
                12.0,
                9.0,
                8.0,
                5.0,
                2.5,
                3.2,
                4.0,
                5.0,
                6.3,
                7.4,
                9.2 },
                {
            20.0,
                10.0,
                6.0,
                4.0,
                2.5,
                2.0,
                1.75,
                2.0,
                2.3,
                2.3,
                2.3,
                2.3,
                2.3 },
                {
            18.0,
                6.5,
                3.2,
                2.1,
                1.7,
                1.3,
                1.04,
                0.94,
                0.825,
                0.570,
                0.489,
                0.331,
                0.215 },
                {
            0.8,
                0.8,
                0.8,
                0.8,
                0.8,
                0.8,
                0.6,
                0.528,
                0.430,
                0.330,
                0.154,
                0.104,
                0.058 },
                {
            0.26,
                0.26,
                0.36,
                0.36,
                0.42,
                0.42,
                0.63,
                0.63,
                0.83,
                0.83,
                1.11,
                1.11,
                1.11 }
    };
    private static final double tableStellarLuminosity[][] =
        {
            {
                560000,
                204000,
                107000,
                81000,
                61000,
                51000,
                67000,
                89000,
                97000,
                107000,
                117000,
                129000,
                141000 },
            {
            270000,
                46700,
                15000,
                11700,
                7400,
                5100,
                6100,
                8100,
                11700,
                20400,
                46000,
                89000,
                117000 },
                {
            170000,
                18600,
                2200,
                850,
                600,
                510,
                560,
                740,
                890,
                2450,
                4600,
                14900,
                16200 },
                {
            107000,
                6700,
                280,
                90,
                53,
                43,
                50,
                75,
                95,
                320,
                470,
                2280,
                2690 },
                {
            81000,
                2000,
                156,
                37,
                19,
                12,
                6.5,
                4.9,
                4.67,
                4.67,
                4.67,
                4.67,
                4.67 },
                {
            56000,
                1400,
                90,
                16,
                8.1,
                3.5,
                1.21,
                .67,
                .42,
                .08,
                .04,
                .007,
                .001 },
                {
            .977,
                .977,
                .977,
                .977,
                .977,
                .977,
                .322,
                .186,
                .117,
                .025,
                .011,
                .002,
                .00006 },
                {
            .046,
                .046,
                .005,
                .005,
                .0003,
                .0003,
                .00006,
                .00006,
                .00004,
                .00004,
                .00003,
                .00003,
                .00003 }
    };
        private static final double tableStellarRadius[][] = // in MKm
    { { 52, 75, 135, 149, 174, 204, 298, 454, 654, 1010, 1467, 3020, 3499 }, {
            30, 35, 50, 55, 59, 60, 84, 128, 216, 392, 857, 2073, 2876 }, {
            22, 20, 18, 14, 16, 18, 25, 37, 54, 124, 237, 712, 931 }, {
            16, 10, 6.2, 4.6, 4.7, 5.2, 7.1, 11, 16, 42, 63, 228, 360 }, {
            13, 5.3, 4.5, 2.7, 2.7, 2.6, 2.5, 2.8, 3.3, 3.3, 3.3, 3.3, 3.3 }, {
            10,
                4.4,
                3.2,
                1.8,
                1.7,
                1.4,
                1.03,
                .91,
                .908,
                .566,
                .549,
                .358,
                .201 },
                {
            1.14,
                1.14,
                1.14,
                1.14,
                1.14,
                1.14,
                1.02,
                .55,
                .4,
                .308,
                .256,
                .104,
                .053 },
                {
            .018,
                .018,
                .017,
                .017,
                .013,
                .013,
                .012,
                .012,
                .009,
                .009,
                .006,
                .006,
                .006 }
    };

    private static final double L1 = -20.977937695521;
    private static final double L2 = 7.1783609478385;

}
