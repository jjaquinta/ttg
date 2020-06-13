package jo.ttg.gen.imp;


import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.logic.RandLogic;
import jo.ttg.utils.ConvUtils;

class ImpGenToids extends ImpGenSystemBase
{
    public ImpGenToids(ImpGenScheme _scheme, ImpGenSystem root)
    {
        super(_scheme, root);
    }

    private BodyToidsBean doGenerate(
        BodyBean primary,
        double orbit,
        MainWorldBean mw,
        RandBean r)
    {
        double fakeDiameter;

        BodyToidsBean t = new BodyToidsBean();
		generateName(t, mw, r);
        t.setOrbit(orbit);
        primary.addSatelites(t);

        generateDiameter(t, r);
        generateMaxDiam(t, r);
        generateZones(t, r);
        generateWidth(t, r);
        generateDensity(t, r);
        // shatter an imaginary planet
        fakeDiameter = ConvUtils.convMilesToAU((double) ((RandLogic.D(r, 2) - 1) * 1000.0));
        //fakeDiameter = (double)((RandLogic.D(r, 2) - 1)*1600/150000000.0);
        t.setMass(
            BodyBean.calcMassFromDensityAndDiameter(
                t.getDensity(),
                fakeDiameter));
        t.setEccentricity(generateEccentricity(r));
        t.setTilt(generateTilt(r));
        return t;
    }

    public void generateToids(
        BodyBean primary,
        double orbit,
        MainWorldBean mw,
        RandBean r)
    {
        BodyToidsBean t = doGenerate(primary, orbit, mw, r);
        BodyBean p = primary;
        while (!(p instanceof BodyStarBean))
            p = p.getPrimary();
        BodyStarBean star = (BodyStarBean)p;
        generatePopulatedStats(t.getPopulatedStats(), orbit, star.getStarDecl().getStarType(), r, t.getZone(), mw, 0);
        generateName(t, mw, r);
    }

    public void generateMain(
        BodyBean primary,
        double orbit,
        MainWorldBean mw,
        RandBean r)
    {
        BodyToidsBean t = doGenerate(primary, orbit, mw, r);
        t.getPopulatedStats().set(mw.getPopulatedStats());
        t.setName(mw.getName());
        t.setMainworld(true);
    }
    private void generateDensity(BodyToidsBean t, RandBean r)
    {
        int roll;

        roll = RandLogic.D(r, 3) - 3;
        t.setDensity(adens[roll] / 100.0 * 5.517);
    }
    private void generateWidth(BodyToidsBean t, RandBean r)
    {
        int roll;

        roll = RandLogic.D(r, 2);
        if (t.getOrbit() <= 4)
            roll -= 3;
        else if (t.getOrbit() <= 8)
            roll--;
        else if (t.getOrbit() <= 12)
            roll++;
        else
            roll += 2;
        if (roll < 2)
            roll = 2;
        double wid = 10.0;
        switch (roll)
        {
            case 2 :
                wid = 0.01;
                break;
            case 3 :
                wid = 0.05;
                break;
            case 4 :
            case 5 :
                wid = 0.1;
                break;
            case 6 :
            case 7 :
                wid = 0.5;
                break;
            case 8 :
                wid = 1.0;
                break;
            case 9 :
                wid = 1.5;
                break;
            case 10 :
                wid = 2.0;
                break;
            case 11 :
                wid = 5.0;
                break;
            case 12 :
                wid = 10.0;
                break;
        }
        t.setWidth(wid);
        ;
    }
    private void generateZones(BodyToidsBean t, RandBean r)
    {
        int roll, roll2, pc[];
        double tmp;

        roll = RandLogic.D(r, 2);
        tmp = t.getTemperatureAt(t.getOrbit());
        if (tmp < -15 + 273)
            roll += 2;
        else if (tmp > 50 + 273)
            roll -= 4;
        roll2 = RandLogic.D(r, 2) - 2;
        if (roll <= 4)
            pc = nzone[roll2];
        else if (roll >= 9)
            pc = czone[roll2];
        else
            pc = mzone[roll2];
        double NZone = ((double) (pc[0] + RandLogic.D(r, 2) - 7) / 100.0);
        double MZone = ((double) (pc[1] + RandLogic.D(r, 2) - 7) / 100.0);
        double CZone = ((double) (pc[2] + RandLogic.D(r, 2) - 7) / 100.0);
        // readjust to 100%
        tmp = NZone + MZone + CZone - 1.0;
        NZone += tmp / 3.0;
        MZone += tmp / 3.0;
        CZone += tmp / 3.0;
        // readjust so no -ve
        if (NZone < 0.0)
        {
            MZone -= NZone;
            NZone = 0;
        }
        if (MZone < 0.0)
        {
            CZone -= MZone;
            MZone = 0;
        }
        if (CZone < 0.0)
        {
            NZone -= CZone;
            CZone = 0;
        }
        t.setNZone(NZone);
        t.setMZone(MZone);
        t.setCZone(CZone);
    }
    private void generateMaxDiam(BodyToidsBean t, RandBean r)
    {
        double d;

        switch (RandLogic.D(r, 1))
        {
            case 1 :
            case 2 :
                d = 0;
                break;
            case 3 :
                d = 1;
                break;
            case 4 :
                d = 10;
                break;
            case 5 :
                d = 100;
                break;
            case 6 :
            default :
                d = 10000;
                break;
        }
        // convert to AU
        d = ConvUtils.convKmToAU(d);
        if (d < t.getDiameter())
            d = t.getDiameter();
        t.setMaxDiam(d);
    }
    private void generateDiameter(BodyToidsBean t, RandBean r)
    {
        double d;

        switch (RandLogic.D(r, 2))
        {
            case 2 :
                d = .001;
                break;
            case 3 :
                d = .005;
                break;
            case 4 :
                d = .01;
                break;
            case 5 :
                d = .025;
                break;
            case 6 :
                d = .05;
                break;
            case 7 :
                d = .1;
                break;
            case 8 :
                d = .3;
                break;
            case 9 :
                d = 1;
                break;
            case 10 :
                d = 5;
                break;
            case 11 :
                d = 50;
                break;
            case 12 :
            default :
                d = 500;
                break;
        }
        // convert to AU
        d = ConvUtils.convKmToAU(d);
        t.setDiameter(d);
    }
    // tables

    private static final int nzone[][] = { { 40, 30, 30 }, {
            40, 40, 20 }, {
            40, 40, 20 }, {
            40, 40, 20 }, {
            40, 40, 20 }, {
            50, 40, 10 }, {
            50, 40, 10 }, {
            50, 40, 10 }, {
            50, 30, 20 }, {
            60, 30, 10 }, {
            60, 40, 0 }
    };
    private static final int mzone[][] = { { 20, 50, 30 }, {
            30, 50, 20 }, {
            20, 60, 20 }, {
            20, 60, 20 }, {
            30, 60, 10 }, {
            20, 70, 10 }, {
            10, 70, 20 }, {
            10, 80, 10 }, {
            10, 80, 10 }, {
            0, 80, 20 }, {
            0, 90, 10 }
    };
    private static final int czone[][] = { { 20, 30, 50 }, {
            20, 30, 50 }, {
            20, 30, 50 }, {
            10, 30, 60 }, {
            10, 30, 60 }, {
            10, 20, 70 }, {
            10, 20, 70 }, {
            10, 10, 80 }, {
            0, 10, 90 }, {
            0, 10, 90 }, {
            0, 20, 80 }
    };
    private static final int adens[] =
        { 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80 };

}
