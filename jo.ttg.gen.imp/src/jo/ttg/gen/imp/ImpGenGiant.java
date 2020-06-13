package jo.ttg.gen.imp;


import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.logic.RandLogic;
import jo.ttg.utils.ConvUtils;

public class ImpGenGiant extends ImpGenSystemBase
{
    public ImpGenGiant(ImpGenScheme _scheme, ImpGenSystem root)
    {
        super(_scheme, root);
    }

    public void generateGiant(BodyBean primary, double orbit, MainWorldBean mw, RandBean r)
    {
        int   roll;

        BodyGiantBean g = new BodyGiantBean();
        generateName(g, mw, r);
        g.setOrbit(orbit);
        primary.addSatelites(g);
        if (RandLogic.D(r, 1) < 4)
            g.setSize(BodyGiantBean.GS_S);
        else
            g.setSize(BodyGiantBean.GS_L);
        roll = RandLogic.D(r, 2) - 2;
        double diam;
        if (g.getSize() == BodyGiantBean.GS_L)
        {
            roll = RandLogic.D(r, 3);
            if (roll <= 7)
                diam = (roll + 8)*10000.0;
            else
                diam = (roll + 7)*10000.0;
        }
        else
        {
            roll = RandLogic.D(r, 2);
            if (roll <= 6)
                diam = (roll)*10000.0;
            else
                diam = (roll - 1)*10000.0;
        }
        diam += (RandLogic.D(r, 2) - 7)*1000.0;
        // convert from miles
        diam = ConvUtils.convMilesToAU(diam);
        g.setDiameter(diam);
        g.setDensity(gdens[RandLogic.D(r, 3) - 3]/100.0);
        /* tilt */
        g.setTilt(generateTilt(r));
        /* eccentricity */
        g.setEccentricity(generateEccentricity(r));
        g.setMass(BodyBean.calcMassFromDensityAndDiameter(g.getDensity(), g.getDiameter()));

        generateSatelites(g, r, g.getZone(), mw);
    }

    private void generateSatelites(BodyGiantBean g, RandBean r, String zone, MainWorldBean mw)
    {
        double rad;
        int roll, num;

        if (g.getSize() == BodyGiantBean.GS_L)
            num = RandLogic.D(r, 2);
        else
            num = RandLogic.D(r, 2) - 4;
        if (num <= 0)
            return;
        for (roll = 0; roll < num; roll++)
        {
            rad = generateSateliteRadius(g, r);
            getGenSys().getGenWorld().generateWorld(g, BodyBean.convRadiusToOrbit(rad), mw, r);
        }
    }

    // tables

    private static final int  gdens[] =
        { 10, 11, 12, 13, 14, 16, 18, 20, 22, 23, 24, 26, 27, 28, 29, 30 };
}
