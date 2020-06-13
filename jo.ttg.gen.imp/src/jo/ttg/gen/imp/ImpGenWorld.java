package jo.ttg.gen.imp;


import java.util.Iterator;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.CityBean;
import jo.ttg.beans.sys.StatsAtmBean;
import jo.ttg.beans.sys.StatsGovBean;
import jo.ttg.beans.sys.StatsGovBranchBean;
import jo.ttg.beans.sys.StatsHydBean;
import jo.ttg.beans.sys.StatsHydResourcesBean;
import jo.ttg.beans.sys.StatsLawBean;
import jo.ttg.beans.sys.StatsPopBean;
import jo.ttg.beans.sys.StatsSizBean;
import jo.ttg.beans.sys.StatsTecBean;
import jo.ttg.logic.RandLogic;
import jo.ttg.utils.ConvUtils;
import jo.util.utils.MathUtils;

class ImpGenWorld extends ImpGenSystemBase
{
    public ImpGenWorld(ImpGenScheme _scheme, ImpGenSystem root)
    {
        super(_scheme, root);
    }

    public void generateWorld(BodyBean primary, double orbit, MainWorldBean mw, RandBean r)
    {
        generateCommon(primary, orbit, mw, r);
    }

    public void generateMain(BodyBean primary, double orbit, MainWorldBean mw, RandBean r)
    {
        BodyWorldBean w = new BodyWorldBean();
        w.setName(mw.getName());
        w.setOrbit(orbit);
        w.setMainworld(true);
        primary.addSatelites(w);

        w.getPopulatedStats().set(mw.getPopulatedStats());
        generateBaseDetails(w, r, mw);
        // work out satelites
        if ((primary instanceof BodyStarBean) && (w.getPopulatedStats().getUPP().getSize().getValue() > 0))
        {
            int roll = RandLogic.D(r, 1) - 3;
            while (roll-- > 0)
                generateWorld(w, BodyBean.convRadiusToOrbit(generateSateliteRadius(w, r)), mw, r);
        }
        generateDetails(w, r, mw);
    }

    private void generateCommon(BodyBean primary, double orbit, MainWorldBean mw, RandBean r)
    {
        BodyWorldBean w = new BodyWorldBean();
        generateName(w, mw, r);
        w.setOrbit(orbit);
        primary.addSatelites(w);

        BodyStarBean star = (BodyStarBean)w.getStar();
        int sizMod = 0;
        if (primary instanceof BodyWorldBean)
            sizMod = ((BodyWorldBean)primary).getPopulatedStats().getUPP().getSize().getValue();

        generatePopulatedStats(w.getPopulatedStats(), orbit, star.getStarDecl().getStarType(), r, w.getZone(), mw, sizMod);
        generateBaseDetails(w, r, mw);
        // work out satelites
        if ((primary instanceof BodyStarBean) && (w.getPopulatedStats().getUPP().getSize().getValue() > 0))
        {
            int roll = RandLogic.D(r, 1) - 3;
            while (roll-- > 0)
            {
                double rad = generateSateliteRadius(w, r);
                rad = BodyBean.convRadiusToOrbit(rad);
                generateWorld(w, rad, mw, r);
            }
        }
        generateDetails(w, r, mw);
    }

    private void generateDetails(BodyWorldBean w, RandBean r, MainWorldBean mw)
    {
        generateSizeDetails(w, r);
        generateAtmosDetails(w, r);
        generateHydroDetails(w, r);
        generatePopDetails(w, r, mw);
        generateGovDetails(w, r);
        generateLawDetails(w, r);
        generateTechDetails(w, r);
    }

    private void generateBaseDetails(BodyWorldBean w, RandBean r, MainWorldBean mw)
    {
        int roll, tmp;
        double ftmp;
        if (w.getDiameter() == 0.0)
        {
            /* diameter */
            ftmp = w.getPopulatedStats().getUPP().getSize().getValue() * 1600.0;
            if (ftmp <= 0.0)
                ftmp = 400.0;
            double scale = (1.0 + (RandLogic.D(r, 2) - 7) / 10.0);
            ftmp *= scale;
            double au = ConvUtils.convKmToAU(ftmp);
            w.setDiameter(au);
        }
        if (w.getDensity() == 0.0)
        {
            /* core/density */
            roll = RandLogic.D(r, 2);
            if (mw.getPopulatedStats().getUPP().getSize().getValue() <= 4)
                roll++;
            else if (mw.getPopulatedStats().getUPP().getSize().getValue() >= 6)
                roll -= 2;
            if (mw.getPopulatedStats().getUPP().getAtmos().getValue() <= 3)
                roll++;
            else if (mw.getPopulatedStats().getUPP().getAtmos().getValue() >= 6)
                roll -= 2;
            if (w.isOuterZone())
                roll += 6;
            tmp = RandLogic.D(r, 3) - 3;
            if (roll <= 1)
            {
                w.getStatsSiz().setCore(StatsSizBean.WC_HEAVY);
                tmp = hdens[tmp];
            }
            else if (roll <= 10)
            {
                w.getStatsSiz().setCore(StatsSizBean.WC_MOLTEN);
                tmp = mdens[tmp];
            }
            else if (roll <= 14)
            {
                w.getStatsSiz().setCore(StatsSizBean.WC_ROCKY);
                tmp = rdens[tmp];
            }
            else
            {
                w.getStatsSiz().setCore(StatsSizBean.WC_ICY);
                tmp = idens[tmp];
            }
            w.setDensity(tmp / 100.0);
        }
        if (w.getMass() == 0.0)
            w.setMass(BodyBean.calcMassFromDensityAndDiameter(w.getDensity(), w.getDiameter()));
    }

    private void generateSizeDetails(BodyWorldBean w, RandBean r)
    {
        double ftmp;
        /* day */
        double day =
            (double) (RandLogic.D(r, 2) * 4.0
                + 5.0
                + w.getPrimary().getMass() * w.getOrbitalRadius());
        if (day > 40.0)
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                    day = - (double) (RandLogic.D(r, 1) * 10.0);
                    break;
                case 3 :
                    day = (double) (RandLogic.D(r, 1) * 20.0);
                    break;
                case 4 :
                case 10 :
                    day = (double) (RandLogic.D(r, 1) * 10.0);
                    break;
                case 5 :
                case 9 :
                    break;
                case 6 :
                case 7 :
                case 8 :
                    day = w.getOrbitalPeriod();
                    break;
                case 11 :
                    day = (double) (RandLogic.D(r, 1) * 50.0);
                    break;
                case 12 :
                    day = - (double) (RandLogic.D(r, 1) * 50.0);
                    break;
            }
        w.getStatsSiz().setDay(day);
        w.setEccentricity(generateEccentricity(r));
        w.setTilt(generateTilt(r));
        // seismic stress
        //System.out.print("Stress for "+w.getName());
        double stress = (double) (RandLogic.D(r, 1) - 3);
        if (w.getStatsSiz().getCore() == StatsSizBean.WC_HEAVY)
            stress += (double) (RandLogic.D(r, 1) - 2);
        else if (w.getStatsSiz().getCore() == StatsSizBean.WC_MOLTEN)
            stress += (double) (RandLogic.D(r, 1) - 3);
        for (Iterator<BodyBean> i = w.getSatelitesIterator(); i.hasNext(); )
        {
            BodyBean sptr = i.next();
            ftmp = sptr.getRadiusFromPrimaryInDiameters();
            if (ftmp <= 0)
                continue;
            double pstress = (double) (ConvUtils.convAUToKm(sptr.getDiameter()) / (ftmp / 64.0));
            pstress /= 10000.0; // Jo fudge factor
            stress += pstress;
        }
        stress += w.getPrimary().getMass() / w.getOrbitalRadius();
        stress = Math.floor(stress + 0.5);
        if (stress < 0)
            stress = (double) 0;
        w.getStatsSiz().setSeismicStress(stress);
        //System.out.println(" is "+stress);
    }

    private void generateAtmosDetails(BodyWorldBean w, RandBean r)
    {
        int atmValue = w.getPopulatedStats().getUPP().getAtmos().getValue();
        StatsAtmBean atm = w.getStatsAtm();
        //double Temp = (double) 0.0;
        /* attype */
        switch (atmValue)
        {
            case 0 :
            case 1 :
                atm.setType(StatsAtmBean.AT_TRACE);
                break;
            case 2 :
            case 3 :
                atm.setType(StatsAtmBean.AT_VTHIN);
                break;
            case 4 :
            case 5 :
                atm.setType(StatsAtmBean.AT_THIN);
                break;
            case 6 :
            case 7 :
                atm.setType(StatsAtmBean.AT_STD);
                break;
            case 8 :
            case 9 :
                atm.setType(StatsAtmBean.AT_DENSE);
                break;
            default :
                atm.setType(StatsAtmBean.AT_THIN);
                break;
        }
        /* attaint */
        generateTaint(w, r);
        /* pressure */
        int roll = RandLogic.D(r, 2) - 2;
        atm.setPressure(gpress[atm.getType()][roll]);
        /* base temp */
        generateTemp(w, r);
        /* life */
        roll = RandLogic.D(r, 2);
        if (atmValue == 0)
            roll -= 3;
        else if (
            (atmValue >= 4)
                && (atmValue <= 9))
            roll += 4;
        if (w.getPopulatedStats().getUPP().getHydro().getValue() == 0)
            roll -= 2;
        else if (
            (w.getPopulatedStats().getUPP().getHydro().getValue() >= 2)
                && (w.getPopulatedStats().getUPP().getHydro().getValue() <= 8))
            roll += 1;
        if ((atm.getTemperature() < -20.0 + 273.0) || (atm.getTemperature() > 30.0 + 273.0))
            roll--;
        int c = w.getStar().getStarDecl().getStarType();
        if ((c >= StarDeclBean.ST_G) && (c <= StarDeclBean.ST_K + 9))
            roll++;
        else if (c <= StarDeclBean.ST_F + 9)
            roll--;
        if (roll >= 10)
            atm.setLife(true);
        else
            atm.setLife(false);
    }

    private void generateHydroDetails(BodyWorldBean w, RandBean r)
    {
        StatsHydBean hyd = w.getStatsHyd();
        /* water coverage */
        int roll = w.getPopulatedStats().getUPP().getHydro().getValue() * 10 + RandLogic.D(r, 2) - 7;
        if (roll < 0)
            roll = 0;
        else if (roll > 100)
            roll = 100;
        else
        hyd.setWaterPercent(roll);
        /* plates */
        int nPlates;
        if ((w.getStatsSiz().getCore() == StatsSizBean.WC_ROCKY) || (w.getStatsSiz().getCore() == StatsSizBean.WC_ICY))
            nPlates = 1;
        else
        {
            nPlates =
                w.getPopulatedStats().getUPP().getSize().getValue()
                    + w.getPopulatedStats().getUPP().getHydro().getValue()
                    - RandLogic.D(r, 2);
            if (nPlates < 0)
                nPlates = 1;
        }
        hyd.setNumPlates(nPlates);
        generateMasses(w, r);
        /* water type */
        int hyTaint = StatsHydBean.HT_NONE;
        switch (w.getPopulatedStats().getUPP().getAtmos().getValue())
        {
            case 2 :
            case 4 :
            case 7 :
            case 9 :
                if (RandLogic.D(r, 1) < 5)
                    hyTaint = StatsHydBean.HT_TAINT;
                break;
            case 10 :
                if (RandLogic.D(r, 2) >= 10)
                    hyTaint = StatsHydBean.HT_TAINT;
                else
                    hyTaint = StatsHydBean.HT_ATMOS;
                break;
            case 11 :
            case 12 :
                hyTaint = StatsHydBean.HT_ATMOS;
                break;
        }
        hyd.setTaint(hyTaint);
        /* volcanoes */
        int tmp = hyd.getMajor() + hyd.getMinor();
        int nVolc = 0;
        while (tmp-- > 0)
        {
            roll = RandLogic.D(r, 2) - 7 + (int) w.getStatsSiz().getSeismicStress() / 2;
            if (roll > 0)
                nVolc += roll;
        }
        hyd.setNumVolcanos(nVolc);
        /* weather control */
        roll = RandLogic.D(r, 2);
        if ((w.getPopulatedStats().getUPP().getTech().getValue() >= 8)
            && (roll < w.getPopulatedStats().getUPP().getTech().getValue())
            && (roll < w.getPopulatedStats().getUPP().getPop().getValue()))
            hyd.setWeatherControl(true);
        else
            hyd.setWeatherControl(false);
        generateResources(w, r);
    }

    private void generatePopDetails(BodyWorldBean w, RandBean r, MainWorldBean mw)
    {
        int roll;

        generateCities(w, r, mw);
        generateAttitudes(w, r);

        if (w.getPopulatedStats().getUPP().getPop().getValue() <= 0)
            return; // no customs
        /* social outlook */
        roll = RandLogic.D(r, 1);
        while (roll-- > 0)
        {
            String custom = soccust[RandLogic.rand(r)%soccust.length];
            for (;;)
            {
                int o = custom.indexOf("%s");
                if (o < 0)
                    break;
                int g = RandLogic.rand(r)%pracgroup.length;
                custom = custom.substring(0, o) + pracgroup[g] + custom.substring(o+2);
            }
            w.getStatsPop().addCustoms(custom);
        }
    }

    private void generateGovDetails(BodyWorldBean w, RandBean r)
    {
        generateRepresentation(w, r);
        generateReligion(w, r);
    }

    private void generateLawDetails(BodyWorldBean w, RandBean r)
    {
        int lawValue = w.getPopulatedStats().getUPP().getLaw().getValue();
        StatsLawBean law = w.getStatsLaw();

        if (w.getPopulatedStats().getUPP().getPop().getValue() < 2)
        {
            law.setWeapons(lawValue);
            law.setTrade(lawValue);
            law.setCriminal(lawValue);
            law.setCivil(lawValue);
            law.setPersonalFreedom(lawValue);
            law.setUniformity(StatsLawBean.LU_UNSET);
            return;
        }
        /* law profile */
        int roll = RandLogic.D(r, 2) - 7 + lawValue;
        law.setWeapons(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + lawValue;
        law.setTrade(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + lawValue;
        law.setCriminal(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + lawValue;
        law.setCivil(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + lawValue;
        law.setPersonalFreedom(clipHex(roll));

        roll = RandLogic.D(r, 2);
        if (w.getStatsPop().getGlobExt() == StatsPopBean.GE_MONOLITHIC)
            roll += 2;
        if (lawValue >= 10)
            roll--;
        if (roll <= 5)
            law.setUniformity(StatsLawBean.LU_PERSONAL);
        else if (roll <= 7)
            law.setUniformity(StatsLawBean.LU_TERRITORIAL);
        else
            law.setUniformity(StatsLawBean.LU_UNDIVIDED);
    }

    private void generateTechDetails(BodyWorldBean w, RandBean r)
    {
        int tecValue = w.getPopulatedStats().getUPP().getTech().getValue();
        int popValue = w.getPopulatedStats().getUPP().getPop().getValue();
        StatsTecBean tec = w.getStatsTec();
        if (popValue <= 0)
            return;

        int u = tecValue;
        int l = tecValue / 2;
        int roll = u + getTechLevelModifier(r);
        if (popValue <= 5)
            roll++;
        else if (popValue >= 9)
            roll--;
        roll += 1 - w.getStatsPop().getGlobExt();
        tec.setLow(clip(roll, u, l));

        u = tecValue * 6 / 5;
        l = tecValue / 2;
        roll = tecValue + getTechLevelModifier(r);
        tec.setEnergy(clip(roll, u, l));

        u = tec.getEnergy();
        l = u / 3;
        roll = tecValue + getTechLevelModifier(r);
        if (popValue <= 5)
            roll++;
        else if (popValue >= 9)
            roll--;
        tec.setRobot(clip(roll, u, l));

        roll = tec.getRobot() + getTechLevelModifier(r);
        tec.setCommo(clip(roll, u, l));

        l = 0;
        roll = tec.getRobot() + getTechLevelModifier(r);
        if (w.getStatsPop().getIntrExt() == 0)
            roll++;
        tec.setMedical(clip(roll, u, l));

        l = u - 5;
        if (u < 0)
            u = 0;
        roll = tecValue + getTechLevelModifier(r);
        if ((w.getPopulatedStats().getUPP().getAtmos().getValue() != 5)
            && (w.getPopulatedStats().getUPP().getAtmos().getValue() != 6)
            && (w.getPopulatedStats().getUPP().getAtmos().getValue() != 8))
            roll++;
        if ((w.getPopulatedStats().getUPP().getHydro().getValue() == 0)
            || (w.getPopulatedStats().getUPP().getHydro().getValue() == 10))
            roll++;
        tec.setEnvironmental(clip(roll, u, l));

        roll = tec.getEnergy() + getTechLevelModifier(r);
        if (w.getPopulatedStats().getUPP().getHydro().getValue() == 10)
            roll--;
        tec.setLand(clip(roll, u, l));

        u = tec.getLand();
        l = u - 5;
        if (u < 0)
            u = 0;
        roll = tec.getLand() + getTechLevelModifier(r);
        if (w.getPopulatedStats().getUPP().getHydro().getValue() == 0)
            roll--;
        tec.setWater(clip(roll, u, l));

        if (u < 9)
            u = 9;
        l = u - 5;
        if (u < 2)
            u = 2;
        roll = tec.getEnergy() + getTechLevelModifier(r);
        tec.setAir(clip(roll, u, l));

        u = tec.getEnergy();
        l = u - 3;
        if (u < 0)
            u = 0;
        roll = getTechLevelModifier(r);
        if (tec.getEnergy() < tec.getRobot())
            roll += tec.getEnergy();
        else
            roll += tec.getRobot();
        if ((w.getPopulatedStats().getUPP().getPort().getValue() == 'A')
            || (w.getPopulatedStats().getUPP().getPort().getValue() == 'B'))
            roll++;
        if (w.getStatsPop().getIntrExt() == 0)
            roll++;
        else if (w.getStatsPop().getIntrExt() == 3)
            roll--;
        tec.setSpace(clip(roll, u, l));

        l = 0;
        roll = tec.getEnergy() + getTechLevelModifier(r);
        if (w.getStatsPop().getAggrAtt() == 0)
            roll++;
        else if (w.getStatsPop().getAggrAtt() == 3)
            roll -= 2;
        if (w.getStatsPop().getAggrAct() == 0)
            roll++;
        else if (w.getStatsPop().getAggrAct() == 3)
            roll--;
        tec.setPersonalMilitary(clip(roll, u, l));

        roll = tec.getLand() + getTechLevelModifier(r);
        if (w.getStatsPop().getAggrAtt() == 0)
            roll++;
        else if (w.getStatsPop().getAggrAtt() == 3)
            roll -= 2;
        if (w.getStatsPop().getAggrAct() == 0)
            roll++;
        else if (w.getStatsPop().getAggrAct() == 3)
            roll--;
        tec.setHeavyMilitary(clip(roll, u, l));

        roll = u = getTechLevelModifier(r);
        tec.setNovel(clip(roll, u, l));
    }
    private int getTechLevelModifier(RandBean r)
    {
        switch (RandLogic.D(r, 2))
        {
            case 2 :
                return -RandLogic.D(r, 1);
            case 3 :
                return -2;
            case 4 :
                return -1;
            case 10 :
                return 1;
            case 11 :
                return 2;
            case 12 :
                return RandLogic.D(r, 1);
        }
        return 0;
    }

    private void generateCities(BodyWorldBean w, RandBean r, MainWorldBean mw)
    {
        int popValue = w.getPopulatedStats().getUPP().getPop().getValue();
        StatsPopBean pop = w.getStatsPop();
        /* population */
        double fpop = Math.pow(10.0, (double) popValue);
        double tpop = Math.floor(fpop * ((RandLogic.rand(r) % 900) + 100) / 100.0);
        double totalPop = tpop;
        long Pop_2 = 0L;
        long Pop_3 = 0L;
        long Pop_4 = 0L;
        long Pop_5 = 0L;
        /* see if single city or no cities */
        if (popValue < 6)
        {
            if ((RandLogic.D(r, 1) > popValue)
                && (tpop >= 10.0))
            {
                /* all in one city */
                //cities = new TCity(w.Lang.GetName(), tpop, cities);
                CityBean c = new CityBean();
                c.setName(generateName(mw, r));
                c.setPop(tpop);
                pop.addCities(c);
                tpop = 0.0;
            }
            else if (popValue <= 3)
                tpop = 0.0;
        }
        /* very large cities */
        if ((tpop > 0.0)
            && ((int) (MathUtils.log10(tpop)) >= popValue)
            && (RandLogic.D(r, 3) < popValue))
        {
            for (;;)
            {
                int roll = (int) (tpop / fpop) - 1;
                if (roll <= 0)
                    break;
                double ftmp = (double) ((RandLogic.rand(r) % roll) + 1) * fpop;
                //cities = new TCity(w.Lang.GetName(), ftmp, cities);
                CityBean c = new CityBean();
                c.setName(generateName(mw, r));
                c.setPop(ftmp);
                pop.addCities(c);
                tpop -= ftmp;
            }
        }
        /* large cities */
        if ((tpop > 0.0)
            && ((int) MathUtils.log10(tpop) >= popValue - 1)
            && (RandLogic.D(r, 2) < popValue))
        {
            double mass = 0.0;
            while (2.0 * mass < tpop)
            {
                double ftmp = (double) ((RandLogic.rand(r) % 9) + 1) * fpop / 10.0;
                if (mass + ftmp > tpop)
                    break;
                mass += ftmp;
                CityBean c = new CityBean();
                c.setName(generateName(mw, r));
                c.setPop(ftmp);
                pop.addCities(c);
            }
            tpop -= mass;
        }
        /* medium large cities */
        Pop_2 = (int) (tpop / fpop * (double) (RandLogic.D(r, 1) + 9));
        tpop -= (double) Pop_2 * fpop * 5.0 / 100.0;
        /* moderate size cities */
        Pop_3 = (int) (tpop / fpop * (double) (RandLogic.D(r, 1) + 9) * 10.0);
        tpop -= (double) Pop_3 * fpop * 5.0 / 1000.0;
        /* small size cities */
        Pop_4 = (int) (tpop / fpop * (double) (RandLogic.D(r, 1) + 9) * 100.0);
        tpop -= (double) Pop_4 * fpop * 5.0 / 10000.0;
        /* very small size cities */
        Pop_5 = (long) (tpop / fpop * (double) (RandLogic.D(r, 1) + 9) * 1000.0);
        tpop -= (double) Pop_5 * fpop * 5.0 / 100000.0;
        long rural_pop = (long) tpop;

        pop.setTotalPop(totalPop);
        pop.setPop2(Pop_2);
        pop.setPop3(Pop_3);
        pop.setPop4(Pop_4);
        pop.setPop5(Pop_5);
        pop.setRuralPop(rural_pop);
    }

    private void generateAttitudes(BodyWorldBean w, RandBean r)
    {
        int lawValue = w.getPopulatedStats().getUPP().getLaw().getValue();
        int popValue = w.getPopulatedStats().getUPP().getPop().getValue();
        StatsPopBean pop = w.getStatsPop();
        if (popValue < 2)
        {
            pop.setProgAtt(StatsPopBean.PA_UNSET);
            pop.setProgAct(StatsPopBean.PC_UNSET);
            pop.setAggrAtt(StatsPopBean.AA_UNSET);
            pop.setAggrAct(StatsPopBean.AC_UNSET);
            pop.setGlobExt(StatsPopBean.GE_UNSET);
            pop.setIntrExt(StatsPopBean.IE_UNSET);
            return;
        }
        int roll = RandLogic.D(r, 2);
        if (popValue >= 6)
            roll++;
        if (popValue >= 9)
            roll++;
        if (lawValue >= 0xa)
            roll++;
        if (roll < 4)
            pop.setProgAtt(StatsPopBean.PA_RADICAL);
        else if (roll < 8)
            pop.setProgAtt(StatsPopBean.PA_PROGRESSIVE);
        else if (roll < 12)
            pop.setProgAtt(StatsPopBean.PA_CONSERVATIVE);
        else
            pop.setProgAtt(StatsPopBean.PA_REACTIONARY);

        roll = RandLogic.D(r, 2);
        if (pop.getProgAtt() == StatsPopBean.PA_PROGRESSIVE)
            roll += 3;
        if (pop.getProgAtt() == StatsPopBean.PA_RADICAL)
            roll += 6;
        if (lawValue >= 0xa)
            roll++;
        if (roll < 6)
            pop.setProgAct(StatsPopBean.PC_ENTERPRISING);
        else if (roll < 10)
            pop.setProgAct(StatsPopBean.PC_ADVANCING);
        else if (roll < 13)
            pop.setProgAct(StatsPopBean.PC_INDIFFERENT);
        else
            pop.setProgAct(StatsPopBean.PC_STAGNANT);

        roll = RandLogic.D(r, 2);
        if (lawValue >= 0xa)
            roll++;
        if (roll < 4)
            pop.setAggrAtt(StatsPopBean.AA_EXPANSIONISTIC);
        else if (roll < 7)
            pop.setAggrAtt(StatsPopBean.AA_COMPETITIVE);
        else if (roll < 11)
            pop.setAggrAtt(StatsPopBean.AA_UNAGRESSIVE);
        else
            pop.setAggrAtt(StatsPopBean.AA_PASSIVE);

        roll = RandLogic.D(r, 2);
        if (pop.getAggrAtt() == StatsPopBean.AA_EXPANSIONISTIC)
            roll -= 2;
        else if (pop.getAggrAtt() == StatsPopBean.AA_COMPETITIVE)
            roll--;
        else if (pop.getAggrAtt() == StatsPopBean.AA_UNAGRESSIVE)
            roll += 2;
        if (lawValue >= 0xa)
            roll++;
        if (roll < 5)
            pop.setAggrAct(StatsPopBean.AC_MILITANT);
        else if (roll < 9)
            pop.setAggrAct(StatsPopBean.AC_NEUTRAL);
        else if (roll < 12)
            pop.setAggrAct(StatsPopBean.AC_PEACEABLE);
        else
            pop.setAggrAct(StatsPopBean.AC_CONCILIATORY);

        roll = RandLogic.D(r, 2);
        if (pop.getAggrAtt() == StatsPopBean.AC_MILITANT)
            roll += 2;
        if (w.getPopulatedStats().getUPP().getGov().getValue() <= 2)
            roll++;
        else if (w.getPopulatedStats().getUPP().getGov().getValue() == 7)
            roll += 4;
        else if (w.getPopulatedStats().getUPP().getGov().getValue() == 0xf)
            roll--;
        if (lawValue <= 4)
            roll++;
        else if (lawValue >= 0xa)
            roll--;
        if (roll < 4)
            pop.setGlobExt(StatsPopBean.GE_MONOLITHIC);
        else if (roll < 8)
            pop.setGlobExt(StatsPopBean.GE_HARMONIOUS);
        else if (roll < 12)
            pop.setGlobExt(StatsPopBean.GE_DISCORDANT);
        else
            pop.setGlobExt(StatsPopBean.GE_FRAGMENTED);

        roll = RandLogic.D(r, 2);
        if (w.getPopulatedStats().getUPP().getPort().getValue() == 'A')
            roll -= 2;
        else if (w.getPopulatedStats().getUPP().getPort().getValue() == 'B')
            roll--;
        else if (w.getPopulatedStats().getUPP().getPort().getValue() == 'D')
            roll++;
        else if (w.getPopulatedStats().getUPP().getPort().getValue() == 'E')
            roll += 2;
        else if (w.getPopulatedStats().getUPP().getPort().getValue() == 'X')
            roll += 3;
        if (pop.getProgAtt() == StatsPopBean.PA_CONSERVATIVE)
            roll += 2;
        else if (pop.getProgAtt() == StatsPopBean.PA_REACTIONARY)
            roll += 4;
        if (lawValue >= 0xa)
            roll++;
        if (roll < 4)
            pop.setIntrExt(StatsPopBean.IE_XENOPHILIC);
        else if (roll < 8)
            pop.setIntrExt(StatsPopBean.IE_FRIENDLY);
        else if (roll < 12)
            pop.setIntrExt(StatsPopBean.IE_ALOOF);
        else
            pop.setIntrExt(StatsPopBean.IE_XENOPHOBIC);
    }
    private void generateMasses(BodyWorldBean w, RandBean r)
    {
        StatsHydBean hyd = w.getStatsHyd();
        int roll;

        /* land/water distribution */
        if (hyd.getWaterPercent() < 50)
            roll = RandLogic.D(r, 1) + w.getPopulatedStats().getUPP().getHydro().getValue() * 3;
        else
            roll = 37 - (RandLogic.D(r, 1) + w.getPopulatedStats().getUPP().getHydro().getValue() * 3);
        int Maj = 0;
        int Min = 0;
        int Isl = 0;
        int Arch = 0;
        if (roll >= 8)
        {
            Isl = RandLogic.D(r, 3) - 3;
            Arch = RandLogic.D(r, 2);
        }
        else if (roll == 7)
        {
            Isl = RandLogic.D(r, 2) - 3;
            Arch = RandLogic.D(r, 2);
        }
        else if (roll == 6)
        {
            Isl = RandLogic.D(r, 1) - 3;
            Arch = RandLogic.D(r, 2);
        }
        else if (roll >= 6)
            Arch = RandLogic.D(r, 1);
        if (Isl < 0)
            Isl = 0;
        if (roll >= 19)
            Maj = 1;
        else if (roll >= 16)
            Maj = RandLogic.D(r, 1);
        else if (roll >= 13)
            Maj = RandLogic.D(r, 1) - 1;
        else if (roll >= 11)
            Maj = RandLogic.D(r, 1) - 2;
        else if (roll >= 9)
            Maj = RandLogic.D(r, 1) - 3;
        else if (roll >= 7)
            Maj = RandLogic.D(r, 1) - 4;
        if (Maj < 0)
            Maj = 0;
        if (roll >= 13)
            Min = RandLogic.D(r, ((roll - 1) % 3) + 1) - (((roll - 1) % 3) + 1);
        else if (roll >= 12)
            Min = RandLogic.D(r, 2) - 2;
        else if (roll >= 9)
            Min = RandLogic.D(r, 1) - 1;
        else if (roll >= 8)
            Min = RandLogic.D(r, 1) - 2;
        else if (roll >= 7)
            Min = RandLogic.D(r, 1) - 3;
        if (Min < 0)
            Min = 0;
        hyd.setMajor(Maj);
        hyd.setMinor(Min);
        hyd.setIslands(Isl);
        hyd.setArchepeligos(Arch);
    }
    private void generateReligion(BodyWorldBean w, RandBean r)
    {
        StatsGovBean gov = w.getStatsGov();
        int roll;

        if (w.getPopulatedStats().getUPP().getPop().getValue() < 2)
        {
            gov.setGodView(-1);
            gov.setSpiritualAim(-1);
            gov.setDevotionRequired(-1);
            gov.setReligiousOrganisation(-1);
            gov.setLiturgicalFormality(-1);
            gov.setMissionaryFervour(-1);
            return;
        }
        /* religious profile */
        roll = RandLogic.D(r, 2) - 7 + w.getPopulatedStats().getUPP().getTech().getValue();
        gov.setGodView(clipHex(roll));
        roll = RandLogic.D(r, 2) - 2 + gov.getGodView() / 3;
        gov.setSpiritualAim(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + gov.getSpiritualAim();
        gov.setDevotionRequired(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + gov.getDevotionRequired();
        gov.setReligiousOrganisation(clipHex(roll));
        roll = RandLogic.D(r, 2) - 7 + gov.getReligiousOrganisation();
        gov.setLiturgicalFormality(clipHex(roll));
        gov.setMissionaryFervour(RandLogic.D(r, 2) - 2);
    }
    private void generateRepresentation(BodyWorldBean w, RandBean r)
    {
        int govValue = w.getPopulatedStats().getUPP().getGov().getValue();
        int popValue = w.getPopulatedStats().getUPP().getPop().getValue();
        StatsGovBean gov = w.getStatsGov();
        int roll;

        int baseGovOrg = StatsGovBranchBean.GO_NONE;
        if (popValue < 2)
            return;
        /* government */
        switch (govValue)
        {
            case 0 : /* none */
            case 7 : /* balkanization */
                baseGovOrg = StatsGovBranchBean.GO_NONE;
                break;
            case 1 : /* company/corporation */
            case 4 : /* representitive democracy */
            case 5 : /* feudal technocracy */
            case 6 : /* captive government */
                baseGovOrg = generateGovOrg(r);
                break;
            case 2 : /* participating democracy */
                baseGovOrg = StatsGovBranchBean.GO_DEMOS;
                break;
            case 3 : /* self-perpet olig */
                if (RandLogic.D(r, 1) <= 4)
                    baseGovOrg = StatsGovBranchBean.GO_ECOUNCIL;
                else
                    baseGovOrg = StatsGovBranchBean.GO_SCOUNCIL;
                break;
            case 8 : /* Civil Service Buero */
            case 9 : /* Impersonal Buero */
                baseGovOrg = StatsGovBranchBean.GO_SCOUNCIL;
                break;
            case 10 : /* charis dictator */
            case 11 : /* charis dictator */
                if (RandLogic.D(r, 1) <= 5)
                    baseGovOrg = StatsGovBranchBean.GO_RULER;
                else
                    baseGovOrg = StatsGovBranchBean.GO_ECOUNCIL;
                break;
            case 12 : /* charis oligar */
            case 15 : /* totalar oligar */
                if (RandLogic.D(r, 1) <= 4)
                    baseGovOrg = StatsGovBranchBean.GO_ECOUNCIL;
                else
                    baseGovOrg = StatsGovBranchBean.GO_SCOUNCIL;
                break;
            case 13 : /* relig dictat */
            case 14 : /* relig auto */
                do
                {
                    baseGovOrg = generateGovOrg(r);
                }
                while (baseGovOrg == StatsGovBranchBean.GO_DEMOS);
                break;
        }
        StatsGovBranchBean g1 = new StatsGovBranchBean();
        StatsGovBranchBean g2 = new StatsGovBranchBean();
        StatsGovBranchBean g3 = new StatsGovBranchBean();
        g1.setOrganization(baseGovOrg);
        g2.setOrganization(generateGovOrg(r));
        g3.setOrganization(generateGovOrg(r));
        if (baseGovOrg != StatsGovBranchBean.GO_NONE)
            switch (RandLogic.D(r, 1))
            {
                case 1 :
                case 2 : /* 3 way division */
                    roll = RandLogic.D(r, 1);
                    if (roll < 3)
                    {
                        g1.setExecutive(true);
                        g2.setLegislative(true);
                        g3.setJudicial(true);
                    }
                    else if (roll < 5)
                    {
                        g1.setLegislative(true);
                        g2.setExecutive(true);
                        g3.setJudicial(true);
                    }
                    else
                    {
                        g1.setJudicial(true);
                        g2.setExecutive(true);
                        g3.setLegislative(true);
                    }
                    gov.addBranches(g1);
                    gov.addBranches(g2);
                    gov.addBranches(g3);
                    break;
                case 3 :
                case 4 : /* 2 way division */
                    roll = RandLogic.D(r, 1);
                    if (roll < 3)
                    {
                        g1.setExecutive(true);
                        g1.setJudicial(true);
                        g2.setLegislative(true);
                    }
                    else if (roll < 6)
                    {
                        g1.setExecutive(true);
                        g1.setLegislative(true);
                        g2.setJudicial(true);
                    }
                    else
                    {
                        g1.setJudicial(true);
                        g1.setLegislative(true);
                        g2.setExecutive(true);
                    }
                    gov.addBranches(g1);
                    gov.addBranches(g2);
                    break;
                case 5 :
                case 6 : /* 1 way division */
                    g1.setExecutive(true);
                    g1.setLegislative(true);
                    g1.setJudicial(true);
                    gov.addBranches(g1);
                    break;
            }
    }
    private int generateGovOrg(RandBean  r)
    {
        switch (RandLogic.D(r, 2))
        {
            case 2: case 12:
                return StatsGovBranchBean.GO_DEMOS;
            case 3: case 4: case 5:
                return StatsGovBranchBean.GO_ECOUNCIL;
            case 6: case 7:
                return StatsGovBranchBean.GO_RULER;
        }
        return StatsGovBranchBean.GO_SCOUNCIL;
    }
    private void generateResources(BodyWorldBean w, RandBean r)
    {
        int govValue = w.getPopulatedStats().getUPP().getGov().getValue();
        int popValue = w.getPopulatedStats().getUPP().getPop().getValue();

        /* resources */
        int ResourcesRE_AGRI = 0;
        int ResourcesRE_ORES = 0;
        int ResourcesRE_RADI = 0;
        int ResourcesRE_CRYS = 0;
        int ResourcesRE_COMP = 0;
        int ResourcesRE_AGRO = 0;
        int ResourcesRE_METL = 0;
        int ResourcesRE_NMET = 0;
        int ResourcesRE_PART = 0;
        int ResourcesRE_DURA = 0;
        int ResourcesRE_CONS = 0;
        int ResourcesRE_WEAP = 0;
        int ResourcesRE_RECO = 0;
        int ResourcesRE_ARTF = 0;
        int ResourcesRE_SOFT = 0;
        int ResourcesRE_DOCU = 0;
        if (w.getStatsSiz().getCore() == StatsSizBean.WC_HEAVY)
        {
            ResourcesRE_AGRI += 1;
            ResourcesRE_ORES += 8;
            ResourcesRE_RADI += 7;
            ResourcesRE_CRYS += 6;
            ResourcesRE_COMP += 5;
            ResourcesRE_METL += 2;
            ResourcesRE_NMET += 1;
        }
        else if (w.getStatsSiz().getCore() == StatsSizBean.WC_MOLTEN)
        {
            ResourcesRE_AGRI += 4;
            ResourcesRE_ORES += 7;
            ResourcesRE_RADI += 5;
            ResourcesRE_CRYS += 5;
            ResourcesRE_COMP += 6;
            ResourcesRE_AGRO += 1;
        }
        else if (w.getStatsSiz().getCore() == StatsSizBean.WC_ROCKY)
        {
            ResourcesRE_AGRI += 4;
            ResourcesRE_ORES += 3;
            ResourcesRE_RADI += 3;
            ResourcesRE_CRYS += 2;
            ResourcesRE_COMP += 1;
            ResourcesRE_AGRO += 1;
        }
        else if (w.getStatsSiz().getCore() == StatsSizBean.WC_ICY)
        {
            ResourcesRE_AGRI -= 4;
            ResourcesRE_COMP -= 4;
            ResourcesRE_METL -= 1;
            ResourcesRE_NMET -= 1;
        }
        if ((w.getPopulatedStats().getUPP().getAtmos().getValue() >= 4)
            && (w.getPopulatedStats().getUPP().getAtmos().getValue() <= 9))
        {
            ResourcesRE_AGRI += 1;
            ResourcesRE_AGRO += 2;
            ResourcesRE_NMET += 1;
        }
        else
        {
            ResourcesRE_AGRI -= 3;
            ResourcesRE_ORES += 1;
            ResourcesRE_RADI += 1;
            ResourcesRE_COMP += 1;
            ResourcesRE_METL += 1;
            ResourcesRE_NMET += 1;
            ResourcesRE_PART += 1;
            ResourcesRE_DURA += 1;
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
        }
        if (popValue <= 4)
        {
            ResourcesRE_AGRO += 1;
            ResourcesRE_METL -= 1;
            ResourcesRE_PART -= 1;
            ResourcesRE_DURA -= 1;
            ResourcesRE_CONS -= 1;
            ResourcesRE_WEAP -= 1;
            ResourcesRE_DOCU -= 1;
        }
        else if (popValue <= 9)
        {
            ResourcesRE_AGRO += 2;
            ResourcesRE_METL += 1;
            ResourcesRE_NMET += 1;
            ResourcesRE_PART += 1;
            ResourcesRE_DURA += 1;
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 1;
            ResourcesRE_ARTF += 1;
            ResourcesRE_SOFT += 1;
        }
        else
        {
            ResourcesRE_AGRO += 2;
            ResourcesRE_METL += 1;
            ResourcesRE_NMET += 1;
            ResourcesRE_PART += 2;
            ResourcesRE_DURA += 3;
            ResourcesRE_CONS += 4;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 2;
            ResourcesRE_ARTF += 3;
            ResourcesRE_SOFT += 4;
            ResourcesRE_DOCU += 1;
        }
        if (w.getPopulatedStats().getUPP().getTech().getValue() <= 3)
        {
            ResourcesRE_AGRI += 1;
            ResourcesRE_ORES += 1;
            ResourcesRE_RADI += 1;
            ResourcesRE_CRYS += 1;
            ResourcesRE_COMP += 1;
            ResourcesRE_AGRO += 1;
            ResourcesRE_METL -= 1;
            ResourcesRE_RECO -= 3;
            ResourcesRE_ARTF += 2;
            ResourcesRE_SOFT -= 9;
        }
        else if (w.getPopulatedStats().getUPP().getTech().getValue() <= 6)
        {
            ResourcesRE_AGRO += 2;
            ResourcesRE_METL += 2;
            ResourcesRE_NMET += 2;
            ResourcesRE_DURA += 1;
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 1;
            ResourcesRE_ARTF += 1;
            ResourcesRE_DOCU += 1;
        }
        else if (w.getPopulatedStats().getUPP().getTech().getValue() <= 11)
        {
            ResourcesRE_AGRI -= 1;
            ResourcesRE_AGRO += 1;
            ResourcesRE_METL += 4;
            ResourcesRE_NMET += 4;
            ResourcesRE_PART += 2;
            ResourcesRE_DURA += 2;
            ResourcesRE_CONS += 2;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 2;
            ResourcesRE_ARTF += 1;
            ResourcesRE_SOFT += 1;
            ResourcesRE_DOCU += 3;
        }
        else
        {
            ResourcesRE_AGRI -= 2;
            ResourcesRE_ORES += 1;
            ResourcesRE_RADI += 1;
            ResourcesRE_CRYS += 1;
            ResourcesRE_COMP += 1;
            ResourcesRE_AGRO += 1;
            ResourcesRE_METL += 5;
            ResourcesRE_NMET += 6;
            ResourcesRE_PART += 4;
            ResourcesRE_DURA += 3;
            ResourcesRE_CONS += 4;
            ResourcesRE_WEAP += 2;
            ResourcesRE_RECO += 3;
            ResourcesRE_ARTF += 1;
            ResourcesRE_SOFT += 4;
            ResourcesRE_DOCU += 1;
        }
        if (w.getStatsAtm().isLife())
        {
            ResourcesRE_AGRI += 5;
            ResourcesRE_COMP += 1;
            ResourcesRE_AGRO += 5;
            ResourcesRE_NMET += 3;
            ResourcesRE_PART += 1;
            ResourcesRE_DURA += 1;
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
        }
        else
        {
            ResourcesRE_COMP -= 1;
        }
        if (govValue <= 1)
        {
            ResourcesRE_PART -= 1;
            ResourcesRE_DURA -= 1;
            ResourcesRE_CONS -= 1;
        }
        else if (govValue <= 6)
        {
            ResourcesRE_PART += 1;
            ResourcesRE_DURA += 1;
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 1;
            ResourcesRE_ARTF += 1;
            ResourcesRE_SOFT += 1;
            ResourcesRE_DOCU += 1;
        }
        else if (govValue == 7)
        {
            ResourcesRE_PART += 2;
            ResourcesRE_DURA += 2;
            ResourcesRE_CONS += 2;
            ResourcesRE_WEAP += 3;
            ResourcesRE_RECO += 1;
            ResourcesRE_ARTF += 2;
            ResourcesRE_SOFT += 1;
            ResourcesRE_DOCU += 2;
        }
        else
        {
            ResourcesRE_CONS += 1;
            ResourcesRE_WEAP += 1;
            ResourcesRE_RECO += 2;
            ResourcesRE_SOFT += 1;
            ResourcesRE_DOCU += 4;
        }
        if ((w.getPopulatedStats().getUPP().getLaw().getValue() >= 3)
            && (w.getPopulatedStats().getUPP().getLaw().getValue() <= 6))
        {
            ResourcesRE_RECO += 1;
            ResourcesRE_SOFT += 1;
            ResourcesRE_DOCU += 2;
        }
        else if (w.getPopulatedStats().getUPP().getLaw().getValue() <= 9)
        {
            ResourcesRE_RECO += 2;
            ResourcesRE_SOFT += 2;
            ResourcesRE_DOCU += 4;
        }
        else
        {
            ResourcesRE_RECO += 3;
            ResourcesRE_SOFT += 3;
            ResourcesRE_DOCU += 6;
        }
        ResourcesRE_AGRI -= RandLogic.D(r, 2) - 1;
        ResourcesRE_ORES -= RandLogic.D(r, 2) - 1;
        ResourcesRE_RADI -= RandLogic.D(r, 2) - 1;
        ResourcesRE_CRYS -= RandLogic.D(r, 2) - 1;
        ResourcesRE_COMP -= RandLogic.D(r, 2) - 1;
        ResourcesRE_AGRO -= RandLogic.D(r, 2) - 1;
        ResourcesRE_METL -= RandLogic.D(r, 2) - 1;
        ResourcesRE_NMET -= RandLogic.D(r, 2) - 1;
        ResourcesRE_PART -= RandLogic.D(r, 2) - 1;
        ResourcesRE_DURA -= RandLogic.D(r, 2) - 1;
        ResourcesRE_CONS -= RandLogic.D(r, 2) - 1;
        ResourcesRE_WEAP -= RandLogic.D(r, 2) - 1;
        ResourcesRE_RECO -= RandLogic.D(r, 2) - 1;
        ResourcesRE_ARTF -= RandLogic.D(r, 2) - 1;
        ResourcesRE_SOFT -= RandLogic.D(r, 2) - 1;
        ResourcesRE_DOCU -= RandLogic.D(r, 2) - 1;
        StatsHydResourcesBean resources = w.getStatsHyd().getResources();
        resources.setAgricultural(ResourcesRE_AGRI);
        resources.setOres        (ResourcesRE_ORES);
        resources.setRadioactives(ResourcesRE_RADI);
        resources.setCrystals    (ResourcesRE_CRYS);
        resources.setCompounds   (ResourcesRE_COMP);
        resources.setAgroproducts(ResourcesRE_AGRO);
        resources.setMetalic     (ResourcesRE_METL);
        resources.setNonMetalic  (ResourcesRE_NMET);
        resources.setParts       (ResourcesRE_PART);
        resources.setDurables    (ResourcesRE_DURA);
        resources.setConsumable  (ResourcesRE_CONS);
        resources.setWeapons     (ResourcesRE_WEAP);
        resources.setRecordings  (ResourcesRE_RECO);
        resources.setArtifacts   (ResourcesRE_ARTF);
        resources.setSoftware    (ResourcesRE_SOFT);
        resources.setDocuments   (ResourcesRE_DOCU);
    }

    private void generateTaint(BodyWorldBean w, RandBean r)
    {
        int a = w.getPopulatedStats().getUPP().getAtmos().getValue();
        int AtTaint = StatsAtmBean.AT_NONE;
        int AtType = StatsAtmBean.AT_VTHIN;
        if ((a == 0)
            || (a == 1)
            || (a == 3)
            || (a == 5)
            || (a == 6)
            || (a == 8)
            || (a == 0xd)
            || (a == 0xe)
            || (a == 0xf))
        {
            AtTaint = StatsAtmBean.AT_NONE;
        }
        else if ((a == 2) || (a == 4) || (a == 7) || (a == 9))
        {
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                case 12 :
                    AtTaint = StatsAtmBean.AT_DISEASE;
                    break;
                case 3 :
                case 11 :
                    AtTaint = StatsAtmBean.AT_GASMIX;
                    break;
                case 4 :
                    AtTaint = StatsAtmBean.AT_HIGHOX;
                    break;
                case 5 :
                case 7 :
                case 9 :
                    AtTaint = StatsAtmBean.AT_POLLUT;
                    break;
                case 6 :
                case 8 :
                    AtTaint = StatsAtmBean.AT_SULFUR;
                    break;
                case 10 :
                    AtTaint = StatsAtmBean.AT_LOWOX;
                    break;
            }
        }
        else if (a == 0xa) /* exotic */
        {
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                    AtType = StatsAtmBean.AT_VTHIN;
                    AtTaint = StatsAtmBean.AT_IRRITANT;
                    break;
                case 3 :
                    AtType = StatsAtmBean.AT_VTHIN;
                    AtTaint = StatsAtmBean.AT_EXOTIC;
                    break;
                case 4 :
                    AtType = StatsAtmBean.AT_THIN;
                    AtTaint = StatsAtmBean.AT_EXOTIC;
                    break;
                case 5 :
                    AtType = StatsAtmBean.AT_THIN;
                    AtTaint = StatsAtmBean.AT_IRRITANT;
                    break;
                case 6 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_EXOTIC;
                    break;
                case 7 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_IRRITANT;
                    break;
                case 8 :
                    AtType = StatsAtmBean.AT_DENSE;
                    AtTaint = StatsAtmBean.AT_EXOTIC;
                    break;
                case 9 :
                    AtType = StatsAtmBean.AT_DENSE;
                    AtTaint = StatsAtmBean.AT_IRRITANT;
                    break;
                case 10 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    AtTaint = StatsAtmBean.AT_EXOTIC;
                    break;
                case 11 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    AtTaint = StatsAtmBean.AT_IRRITANT;
                    break;
                case 12 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_OCCCOR;
                    break;
            }
        }
        else if (a == 0xb) /* corrosive */
        {
            AtTaint = StatsAtmBean.AT_COR;
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                    AtType = StatsAtmBean.AT_VTHIN;
                    w.getStatsAtm().setTemperature(- 100.0);
                    break;
                case 3 :
                    AtType = StatsAtmBean.AT_VTHIN;
                    w.getStatsAtm().setTemperature(- 25.0);
                    break;
                case 4 :
                    AtType = StatsAtmBean.AT_THIN;
                    w.getStatsAtm().setTemperature(12.0);
                    break;
                case 5 :
                    AtType = StatsAtmBean.AT_THIN;
                    w.getStatsAtm().setTemperature(75.0);
                    break;
                case 6 :
                    AtType = StatsAtmBean.AT_STD;
                    w.getStatsAtm().setTemperature(- 100.0);
                    break;
                case 7 :
                    AtType = StatsAtmBean.AT_STD;
                    w.getStatsAtm().setTemperature(12.0);
                    break;
                case 8 :
                    AtType = StatsAtmBean.AT_STD;
                    w.getStatsAtm().setTemperature(75.0);
                    break;
                case 9 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    w.getStatsAtm().setTemperature(- 100.0);
                    break;
                case 10 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    w.getStatsAtm().setTemperature(12.0);
                    break;
                case 11 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    w.getStatsAtm().setTemperature(75.0);
                    break;
                case 12 :
                    AtType = StatsAtmBean.AT_DENSE;
                    w.getStatsAtm().setTemperature(200.0);
                    break;
            }
        }
        else if (a == 0xc) /* insidious */
        {
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                case 3 :
                    AtType = StatsAtmBean.AT_VTHIN;
                    AtTaint = StatsAtmBean.AT_GASMIX;
                    break;
                case 4 :
                    AtType = StatsAtmBean.AT_THIN;
                    AtTaint = StatsAtmBean.AT_RAD;
                    break;
                case 5 :
                    AtType = StatsAtmBean.AT_THIN;
                    AtTaint = StatsAtmBean.AT_TEMP;
                    break;
                case 6 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_PRESS;
                    break;
                case 7 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_GASMIX;
                    break;
                case 8 :
                    AtType = StatsAtmBean.AT_DENSE;
                    AtTaint = StatsAtmBean.AT_PRESS;
                    break;
                case 9 :
                    AtType = StatsAtmBean.AT_DENSE;
                    AtTaint = StatsAtmBean.AT_TEMP;
                    break;
                case 10 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    AtTaint = StatsAtmBean.AT_RAD;
                    break;
                case 11 :
                    AtType = StatsAtmBean.AT_VDENSE;
                    AtTaint = StatsAtmBean.AT_GASMIX;
                    break;
                case 12 :
                    AtType = StatsAtmBean.AT_STD;
                    AtTaint = StatsAtmBean.AT_GASMIX;
                    break;
            }
        }
        w.getStatsAtm().setTaint(AtTaint);
        w.getStatsAtm().setType(AtType);
    }
    private void generateTemp(BodyWorldBean w, RandBean r)
    {
        int roll;
        double E, G;

        double Temp = w.getStatsAtm().getTemperature();
        if (Temp != 0.0)
            return;
        if (w.getPopulatedStats().getUPP().getAtmos().getValue() < 4)
            roll = 0;
        else if (w.getPopulatedStats().getUPP().getAtmos().getValue() <= 0xa)
            roll = 2;
        else if (w.getPopulatedStats().getUPP().getAtmos().getValue() == 0x0e)
            roll = 6;
        else
            roll = 4;
        if (w.isHabitableZone())
            roll++;
        E = (double) ads[roll][w.getPopulatedStats().getUPP().getHydro().getValue()] / 1000.0;
        switch (w.getPopulatedStats().getUPP().getAtmos().getValue())
        {
            case 4 :
            case 5 :
                G = 1.05;
                break;
            case 6 :
            case 7 :
            case 0x0e :
                G = 1.1;
                break;
            case 8 :
            case 9 :
            case 0x0d :
                G = 1.15;
                break;
            case 0x0a :
                G = (double) ((RandLogic.D(r, 2) - 2) / 2 + 12) / 10.0;
                break;
            case 0x0b :
            case 0x0c :
                G = (double) (RandLogic.D(r, 2) + 10) / 10.0;
                break;
            default :
                G = 1.0;
                break;
        }
        if (w.getPrimary() instanceof BodyStarBean)
        {
            Temp =
                (double) (((BodyStarBean) w.getPrimary())
                    .getTemperatureAt(w.getOrbit(), 1.0 - E, G));
        }
        else
        {
            Temp =
                (double) (((BodyStarBean) (w.getPrimary().getPrimary()))
                    .getTemperatureAt(w.getPrimary().getOrbit(), 1.0 - E, G));
        }
        w.getStatsAtm().setTemperature(Temp);
    }

    // tables

        private static final int hdens[] =
        {
            110,
            115,
            120,
            125,
            130,
            135,
            140,
            145,
            150,
            155,
            160,
            170,
            180,
            190,
            200,
            225 };
    private static final int mdens[] =
        {
            82,
            84,
            86,
            88,
            90,
            92,
            94,
            96,
            98,
            100,
            102,
            104,
            106,
            108,
            110,
            112 };
    private static final int rdens[] =
        { 50, 52, 54, 56, 58, 60, 62, 64, 66, 68, 70, 72, 74, 76, 78, 80 };
    private static final int idens[] =
        { 18, 20, 22, 24, 26, 28, 30, 32, 35, 36, 38, 40, 42, 44, 46, 48 };

    private static final int ads[][] =
        { { 800, 744, 736, 752, 738, 753, 767, 782, 796, 810, 818 }, {
            900, 829, 803, 811, 782, 789, 795, 802, 808, 814, 818 }, {
            800, 811, 789, 799, 774, 747, 718, 687, 654, 619, 619 }, {
            900, 900, 860, 860, 820, 780, 740, 700, 660, 620, 619 }, {
            680, 646, 635, 644, 625, 599, 570, 537, 500, 500, 500 }, {
            740, 697, 672, 676, 648, 613, 577, 539, 500, 500, 500 }, {
            800, 811, 807, 817, 813, 809, 805, 800, 794, 787, 773 }, {
            900, 900, 882, 883, 866, 850, 836, 821, 807, 793, 773 }
    };

    private static final double gpress[][] = {
        { 0.01, 0.05, 0.05, 0.06, 0.06, 0.07, 0.07, 0.07, 0.08, 0.08, 0.09 },
        { 0.10, 0.12, 0.14, 0.16, 0.18, 0.20, 0.23, 0.25, 0.30, 0.35, 0.40 },
        { 0.43, 0.45, 0.48, 0.50, 0.50, 0.50, 0.55, 0.60, 0.65, 0.70, 0.75 },
        { 0.76, 0.80, 0.85, 0.90, 0.95, 1.00, 1.00, 1.10, 1.20, 1.30, 1.40 },
        { 1.50, 1.60, 1.70, 1.80, 1.90, 2.00, 2.00, 2.20, 2.20, 2.40, 2.40 },
        { 2.5, 5.0, 10.0, 25.0, 50.0, 100.0, 150.0, 200.0, 250.0, 500.0, 750.0 }
        };
    private static final String soccust[] =
        {
            "Same clothes for all sexes",
            "Unusual clothes for %s",
            "Unusual headgear for %s",
            "Shaved heads for %s",
            "Hair never cut for %s",
            "Unusual hair colour for %s",
            "Unusual hairdos for %s",
            "Unusual eyebrows for %s",
            "Unusual facial alterations for %s",
            "Unusual body alterations for %s",
            "Unusual fingernails for %s",
            "Unusual toenails for %s",
            "Unusual cosmetics for %s",
            "Unusual jewellery for %s",
            "Unusual accessories for %s",
            "Unusual handgear for %s",
            "Tattooing on face of %s",
            "Tattooing on body of %s",
            "Hidden tatooing of %s",
            "Unusual foods for %s",
            "Unusual beverages for %s",
            "Unusual food preparation for %s",
            "%s segregated during meals",
            "%s are vegetarians",
            "%s are carnivorous",
            "%s are omnivorous",
            "Certain coloured food is taboo for %s",
            "Certain shaped food is taboo for %s",
            "Certain food sources are taboo for %s",
            "%s eat in special location",
            "%s eat only in private",
            "%s eat in special orientation",
            "%s eat with unusual utensils",
            "%s eat only at home",
            "%s eat at unusual times",
            "%s eat at only certain times",
            "%s eat only in certain ways",
            "Rituals before eating for %s",
            "Rituals after eating for %s",
            "One sex eats other's leftovers",
            "One age eats other's leftovers",
            "%s eats %s leftovers",
            "One class eats other's leftovers",
            "One race eats others leftovers",
            "%s are cannibalistic",
            "%s live privately",
            "%s live apart in groups",
            "%s live in special locations",
            "%s live at place of work",
            "%s live under special conditions",
            "%s are confined to quarters",
            "%s live under special care",
            "%s have extravagant quarters",
            "%s have minimal quarters",
            "%s have unusual quarters",
            "%s quarters are taboo for %s",
            "%s must visit %s quarters",
            "%s live with extended families",
            "%s live with groom's family",
            "%s live with bride's family",
            "%s live with children's family",
            "%s live with relatives",
            "%s live in communal housing",
            "%s live only in certain terrain",
            "%s must move around",
            "Child named by %s",
            "Child named for living relative",
            "Child named for dead relative",
            "Child named for hero",
            "Child named for %s",
            "Child named for object",
            "Child renamed at adulthood",
            "Child renamed when married",
            "Marriage arranged by %s",
            "Marriage performed by %s",
            "Marriage arranged by parents",
            "Marriage performed by parents",
            "Marriage only within %s",
            "Remarriage prohibited for %s",
            "Remarriage required for %s",
            "Groom's family pays dowery for %s",
            "Bride's family pays dowery for %s",
            "Dowery paid by outsider for %s",
            "Very int marriages the rule for %s",
            "Very long marriages the rule for %s",
            "Non-marriage the rule for %s",
            "Very int marriages prohibited for %s",
            "Very long marriages prohibited for %s",
            "Non-marriage prohibited for %s",
            "Divorce and remarriage required for %s",
            "%s's widow must marry husband's relative",
            "%s's widow/widower must commit suicide",
            "%s's widower must marry wife's relative",
            "Onerous prerequisite to marriage for %s",
            "Marriage only at certain times for %s",
            "Marriage must be blessed by %s",
            "Polyandry practiced amongst %s",
            "Polygyny practiced amongst %s",
            "Communal polygamy practices amongst %s",
            "Unusual sleeping location for %s",
            "Unusual sleeping time for %s",
            "Unusual sleep duration for %s",
            "Unusual sleep orientation for %s",
            "Special language for %s",
            "Sacred symbols for %s",
            "Unusual duties for %s",
            "Anonymity required for %s",
            "Drinking/drugs prohibited for %s",
            "Drinking/drugs required for %s",
            "Bodily abuse prohibited for %s",
            "Bodily abuse required for %s",
            "Special privilleges for %s",
            "Special privilliges prohibited for %s",
            "Unusual greetings for %s",
            "Unusual mannerisms for %s",
            "Unusual leavetakings for %s",
            "Unusual secret societies amongst %s",
            "Closed meetings taboo for %s",
            "Psionics allowed for %s",
            "Psionics mean instant death for %s",
            "Cloning allowed for %s",
            "Cloning required for %s",
            "Cloning prohibited for %s",
            "Robots allowed for %s",
            "Robots required for %s",
            "Robots prohibited for %s",
            "High-tech allowed for %s",
            "High-tech prohibited for %s",
            "Offworld contact allowed for %s",
            "Offworld contact required for %s",
            "Offworld contact prohibited for %s",
            "Unusual gift-giving customs by %s",
            "Free food/clothing required for %s",
            "Free food/clothing prohibited for %s",
            "Free education required for %s",
            "Free education prohibited for %s",
            "Unusual punishment required for %s",
            "Unusual punishment prohibited for %s",
            "Unusual training required for %s",
            "Unusual training prohibited for %s",
            "Unusual responsibilities for %s",
            "Fixed times for visiting %s",
            "Bargaining/haggling required for %s",
            "Bargaining/haggling prohibited for %s",
            "Travelling far away required for %s",
            "Travelling far away prohibited for %s",
            "Unusual holidays for %s",
            "No holidays for %s",
            "Unusual leisure/recreation for %s",
            "Regimented leisure/recreation for %s",
            "Unusual maturity ceremony for %s",
            "Unusual attitudes toward %s",
            "Unusual significance of flora for %s",
            "Unusual significance of fauna for %s",
            "Unusual significance of smell for %s",
            "Unusual significance of sound for %s",
            "Unusual significance of colour for %s",
            "Unusual significance of air for %s",
            "Unusual significance of water for %s",
            "Unusual significance of light for %s",
            "Unusual significance of clothing for %s",
            "Unusual significance of computers for %s",
            "Unusual significance of technology for %s",
            "Unusual significance of robots for %s",
            "Unusual significance of art for %s",
            "Unusual significance of superstition for %s",
            "Daytime siesta required for %s",
            "Daytime siesta prohibited for %s" };

    private static final String pracgroup[] =
        {
            "Certain political figures",
            "Certain geographical regions",
            "Certain sex",
            "Enforcement figures",
            "Entertainment figures",
            "Heroic Figures",
            "Athletic Figures",
            "Certain races",
            "Religious figures",
            "Military figures",
            "Certain occupations",
            "Political figures",
            "Medical figures",
            "Certain age groups",
            "Scientific figures",
            "Academic figures",
            "Low social class",
            "High social class",
            "Convicted criminals" };
}
