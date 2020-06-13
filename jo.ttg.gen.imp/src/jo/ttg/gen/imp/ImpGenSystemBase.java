package jo.ttg.gen.imp;


import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.mw.UPPAtmBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.mw.UPPGovBean;
import jo.ttg.beans.mw.UPPHydBean;
import jo.ttg.beans.mw.UPPLawBean;
import jo.ttg.beans.mw.UPPPopBean;
import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.mw.UPPSizBean;
import jo.ttg.beans.mw.UPPTecBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.mw.UPPLogic;

class ImpGenSystemBase
{
    protected ImpGenScheme mScheme;
    private ImpGenSystem mGenSys;

    public ImpGenSystemBase(ImpGenScheme _scheme, ImpGenSystem root)
    {
        mScheme = _scheme;
        mGenSys = root;
    }

    public String generateName(MainWorldBean mw, RandBean r)
    {
        IGenLanguage lang = mScheme.getGeneratorLanguage();
        return lang.generatePlaceName(mw.getOrds(), mw.getOrds(), mw
                .getPopulatedStats().getAllegiance(), r);
    }

    public void generateName(BodyBean b, MainWorldBean mw, RandBean r)
    {
        String name = generateName(mw, r);
        b.setName(name);
        b.setSeed(r.getSeed());
    }

    public static int unsetWithin(int orbits[], int low, int high)
    {
        int tot, i;

        tot = 0;
        for (i = low; i <= high; i++)
            if (orbits[i] == BodyBean.BT_UNSET)
                tot++;
        return tot;
    }

    public static double generateSateliteRadius(BodyBean b, RandBean r)
    {
        int roll1, roll2;
        double mult;

        roll1 = RandLogic.D(r, 2);
        roll2 = RandLogic.D(r, 2);
        if (roll1 <= 7)
            mult = roll2 + 1;
        else if ((roll1 == 12) && (b.getPrimary() != null)
                && (b.getPrimary().getType() == BodyBean.BT_GIANT))
            mult = (roll2 + 1) * 25;
        else
            mult = (roll2 + 1) * 5;
        mult *= 1.0 + (RandLogic.D(r, 2) - 7.0) / 10.0;
        double radius = b.getDiameter() * mult;
        return radius;
    }

    protected double generateTilt(RandBean r)
    {
        int tilt;

        switch (RandLogic.D(r, 2))
        {
            case 2:
            case 3:
                tilt = 0;
                break;
            case 4:
            case 5:
                tilt = 10;
                break;
            case 6:
            case 7:
                tilt = 20;
                break;
            case 8:
            case 9:
                tilt = 30;
                break;
            case 10:
            case 11:
                tilt = 40;
                break;
            case 12:
            default:
                switch (RandLogic.D(r, 1))
                {
                    case 1:
                    case 2:
                        tilt = 50;
                        break;
                    case 3:
                        tilt = 60;
                        break;
                    case 4:
                        tilt = 70;
                        break;
                    case 5:
                        tilt = 80;
                        break;
                    case 6:
                    default:
                        tilt = 90;
                        break;
                }
        }
        if (tilt != 90)
            tilt += RandLogic.D(r, 2) - 2;
        return tilt;
    }

    protected double generateEccentricity(RandBean r)
    {
        switch (RandLogic.D(r, 2))
        {
            case 8:
                return ((double)0.005);
            case 9:
                return ((double)0.010);
            case 10:
                return ((double)0.015);
            case 11:
                return ((double)0.020);
            case 12:
                switch (RandLogic.D(r, 1))
                {
                    case 1:
                        return 0.025;
                    case 2:
                        return 0.050;
                    case 3:
                        return 0.100;
                    case 4:
                        return 0.200;
                    case 5:
                        return 0.250;
                    case 6:
                        return 0.300;
                }
        }
        return 0.0;
    }

    protected void generatePopulatedStats(PopulatedStatsBean stats, double orbit,
            int type, RandBean r, String zone, MainWorldBean mw, int sizeMod)
    {
        generateUPP(stats.getUPP(), r, orbit, type, zone, mw
                .getPopulatedStats().getUPP());
        stats.setBases(0);
        generateBases(stats, r, zone, mw);
        generateTech(stats.getUPP().getTech(), stats, mw.getPopulatedStats()
                .getUPP().getTech());
        generatePort(stats.getUPP().getPort(), r, stats.getUPP().getPop());
        UPPLogic.updateTradeCodes(stats.getUPP());
        generateZone(stats, r);
        stats.setAllegiance(mw.getPopulatedStats().getAllegiance());
    }

    protected void generateBases(PopulatedStatsBean stats, RandBean r,
            String zone, MainWorldBean mw)
    {
        UPPBean upp = stats.getUPP();
        long bases = 0L;
        if ((zone.equals("H")) && (upp.getAtmos().getValue() >= 4)
                && (upp.getAtmos().getValue() <= 9)
                && (upp.getPop().getValue() >= 2))
            bases |= PopulatedStatsBean.FARM_BASE;
        if (mw.getPopulatedStats().getUPP().isIn()
                && (upp.getPop().getValue() >= 2))
            bases |= PopulatedStatsBean.MINE_BASE;
        if ((upp.getGov().getValue() == 6) && (upp.getPop().getValue() >= 5))
            bases |= PopulatedStatsBean.COLONY_BASE;
        if ((mw.getPopulatedStats().getUPP().getTech().getValue() >= 9)
                && (mw.getPopulatedStats().getUPP().getPop().getValue() >= 1))
        {
            int roll = RandLogic.D(r, 2);
            if (mw.getPopulatedStats().getUPP().getTech().getValue() >= 10)
                roll += 2;
            if (roll >= 11)
                bases |= PopulatedStatsBean.LAB_BASE;
        }
        if ((mw.getPopulatedStats().getUPP().getPop().getValue() >= 8)
                && (!mw.getPopulatedStats().getUPP().isPo()))
        {
            int roll = RandLogic.D(r, 2);
            if (mw.getPopulatedStats().getUPP().getPop().getValue() >= 9)
                roll++;
            if (mw.getPopulatedStats().getUPP().getAtmos().getValue() == upp
                    .getAtmos().getValue())
                roll++;
            if (roll >= 12)
                bases |= PopulatedStatsBean.LOCAL_BASE;
        }
        if (mw.getPopulatedStats().isNavalBase()
                && (upp.getPop().getValue() >= 3))
            bases |= PopulatedStatsBean.NAVAL_BASE;
        if (mw.getPopulatedStats().isScoutBase()
                && (upp.getPop().getValue() >= 2))
            bases |= PopulatedStatsBean.SCOUT_BASE;
        stats.setBases(bases);
    }

    public void generateZone(PopulatedStatsBean stats, RandBean r)
    {
        int v;

        v = stats.getUPP().getLaw().getValue()
                + stats.getUPP().getGov().getValue();
        if (v < 30)
            stats.setTravelZone('G');
        else if (v < 33)
            stats.setTravelZone('A');
        else
            stats.setTravelZone('R');
    }

    private void generateUPP(UPPBean upp, RandBean r, double orbit, int type,
            String zone, UPPBean mwUPP)
    {
        generateSize(upp.getSize(), r, orbit, type);
        generateAtmos(upp.getAtmos(), r, upp.getSize(), zone);
        generateHydro(upp.getHydro(), r, upp.getSize(), upp.getAtmos(), zone);
        generatePop(upp.getPop(), r, upp.getSize(), upp.getAtmos(), zone);
        generateGov(upp.getGov(), r, upp.getPop(), mwUPP.getGov());
        generateLaw(upp.getLaw(), r, upp.getGov(), mwUPP.getLaw());
    }

    private void generatePort(UPPPorBean port, RandBean r, UPPPopBean pop)
    {
        int roll = RandLogic.D(r, 1);
        if (pop.getValue() >= 6)
            roll += 2;
        else if (pop.getValue() == 1)
            roll--;
        else if (pop.getValue() == 0)
            roll -= 3;
        int val;
        if (roll <= 2)
            val = 'Y';
        else if (roll == 3)
            val = 'H';
        else if (roll >= 6)
            val = 'F';
        else
            val = 'G';
        port.setValue(val);
    }

    private void generateSize(UPPSizBean siz, RandBean r, double orbit, int type)
    {
        int val = RandLogic.D(r, 2) - 2;
        if (orbit <= 0.0)
            val -= 5;
        else if (orbit <= 1.0)
            val -= 4;
        else if (orbit <= 2.0)
            val -= 2;
        if (type >= StarDeclBean.ST_M)
            val -= 2;
        siz.setValue(val);
    }

//    private void generateSize(UPPSizBean siz, RandBean r, int sizmod)
//    {
//        if (sizmod < 0)
//            siz.setValue(RandLogic.D(r, 2) + sizmod);
//        else
//            siz.setValue(RandLogic.D(r, 1) - sizmod);
//    }

    private void generateAtmos(UPPAtmBean atmos, RandBean r, UPPSizBean siz,
            String zone)
    {
        int val;
        if (siz.getValue() <= 0)
            val = 0;
        else
        {
            val = RandLogic.D(r, 2) - 7 + siz.getValue();
            if (zone.equals("I"))
                val -= 2;
            else if (zone.equals("O"))
                val -= 4;
        }
        if (zone.equals("O"))
            if (RandLogic.D(r, 2) == 12)
                val = 10;
        atmos.setValue(val);
    }

    private void generateHydro(UPPHydBean hydro, RandBean r, UPPSizBean siz,
            UPPAtmBean atmos, String zone)
    {
        int val;
        if ((siz.getValue() <= 1) || zone.equals("I"))
            val = 0;
        else
        {
            val = RandLogic.D(r, 2) - 7 + siz.getValue();
            if ((atmos.getValue() <= 1) || (atmos.getValue() >= 0xa))
                val -= 4;
            if (zone.equals("O"))
                val -= 2;
        }
        hydro.setValue(val);
    }

    private void generatePop(UPPPopBean pop, RandBean r, UPPSizBean siz,
            UPPAtmBean atmos, String zone)
    {
        int val;
        if (siz.getValue() < 0)
            val = 0;
        else
        {
            val = RandLogic.D(r, 2) - 2;
            if (zone.equals("I"))
                val -= 5;
            else if (zone.equals("O"))
                val -= 4;
            if (siz.getValue() <= 4)
                val -= 2;
            if ((atmos.getValue() != 5) && (atmos.getValue() != 6)
                    && (atmos.getValue() != 8))
                val -= 2;
        }
        pop.setValue(val);
    }

    private void generateGov(UPPGovBean gov, RandBean r, UPPPopBean pop,
            UPPGovBean mwGov)
    {
        int roll, val;

        if (pop.getValue() == 0)
            val = 0;
        else
        {
            roll = RandLogic.D(r, 1);
            if (mwGov.getValue() == 6)
                roll += pop.getValue();
            else if (mwGov.getValue() >= 7)
                roll--;
            switch (roll)
            {
                case 0:
                case 1:
                    val = 0;
                    break;
                case 2:
                    val = 1;
                    break;
                case 3:
                    val = 2;
                    break;
                case 4:
                    val = 4;
                    break;
                default:
                    val = 6;
                    break;
            }
        }
        gov.setValue(val);
    }

    private void generateLaw(UPPLawBean law, RandBean r, UPPGovBean gov,
            UPPLawBean mwLaw)
    {
        int val;
        if (gov.getValue() == 0)
            val = 0;
        else
            val = mwLaw.getValue() + RandLogic.D(r, 1) - 3;
        law.setValue(val);
    }

    private void generateTech(UPPTecBean tech, PopulatedStatsBean stats,
            UPPTecBean mwTech)
    {
        int val;
        if (stats.isLocalBase() || stats.isNavalBase() || stats.isLabBase())
            val = mwTech.getValue();
        else
            val = mwTech.getValue() - 1;
        tech.setValue(val);
    }

    protected int clipHex(int v)
    {
        return clip(v, 0, 15);
    }

    protected int clip(int v, int l, int h)
    {
        if (v < l)
            return l;
        else if (v > h)
            return h;
        else
            return v;
    }
    public ImpGenScheme getScheme()
    {
        return mScheme;
    }
    public ImpGenSystem getGenSys()
    {
        return mGenSys;
    }
}