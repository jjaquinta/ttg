package ttg.adv.logic.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.GoodBean;
import jo.ttg.beans.trade.XMessageBean;
import jo.ttg.gen.IGenCargoEx;
import jo.ttg.gen.imp.GoodList;
import jo.ttg.gen.imp.ImpGenCargo;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.mw.UPPLogic;
import ttg.adv.beans.AdvCargoBean;
import ttg.adv.beans.BodySpecialAdvBean;
import ttg.adv.logic.LocationLogic;

public class AdvGenCargo extends ImpGenCargo implements
        IGenCargoEx
{
    public AdvGenCargo(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    public List<CargoBean> generateCargo(BodyBean origin, DateBean date)
    {
        if (origin.isMainworld())
            return generateCargo(mScheme.getGeneratorMainWorld()
                    .generateMainWorld(origin.getSystem().getOrds()), date);
        List<CargoBean> cargoList = new ArrayList<>();
        if (!(origin instanceof BodySpecialAdvBean))
            return cargoList;
        DateBean day = new DateBean();
        for (int i = 0; i < 7; i++)
        {
            int[] majorMinorIncidental = new int[3];
            day.setDay(date.getDay() - i);
            RandBean r = generateCargoQuanDay(majorMinorIncidental,
                    (BodySpecialAdvBean)origin, day);
            generateCargo(majorMinorIncidental, cargoList,
                    (BodySpecialAdvBean)origin, r, date);
        }
        for (Iterator<CargoBean> i = cargoList.iterator(); i.hasNext();)
        {
            AdvCargoBean c = (AdvCargoBean)i.next();
            c.setPurchasePrice(getValueAt(c, (BodySpecialAdvBean)origin));
        }
        return cargoList;
    }

    public List<CargoBean> generateFreight(BodyBean origin, BodyBean destination,
            DateBean date)
    {
        if (origin.isMainworld() && destination.isMainworld())
            return generateFreight(mScheme.getGeneratorMainWorld()
                    .generateMainWorld(origin.getSystem().getOrds()), mScheme
                    .getGeneratorMainWorld().generateMainWorld(
                            destination.getSystem().getOrds()), date);
        List<CargoBean> cargoList = new ArrayList<>();
        if (!(origin instanceof BodySpecialAdvBean)
                || !(destination instanceof BodySpecialAdvBean))
            return cargoList;
        if (origin.getURI().equals(destination.getURI()))
            return cargoList;
        double base = 1000.0
                * LocationLogic.calcTimeBetween(origin.getURI(), destination
                        .getURI(), mScheme) / (7 * 24 * 60);
        base = Math.max(base, 100.0);
        int[] majorMinorIncidental = new int[3];
        DateBean day = new DateBean();
        for (int i = 0; i < 7; i++)
        {
            day.setDay(date.getDay() - i);
            RandBean r = generateFreightQuanDay(majorMinorIncidental,
                    (BodySpecialAdvBean)origin,
                    (BodySpecialAdvBean)destination, day);
            generateCargo(majorMinorIncidental, cargoList,
                    (BodySpecialAdvBean)origin, r, date);
        }
        for (Iterator<CargoBean> i = cargoList.iterator(); i.hasNext();)
        {
            AdvCargoBean c = (AdvCargoBean)i.next();
            c.setDestination(destination.getURI());
            c.setClassification(CargoBean.CC_FREIGHT);
            c.setPurchasePrice(base * c.getQuantity());
        }
        return cargoList;
    }

    public List<XMessageBean> generateMessages(BodyBean origin, DateBean date)
    {
        if (!origin.isMainworld())
            return new ArrayList<>();
        return super.generateMessages(mScheme.getGeneratorMainWorld().generateMainWorld(origin.getSystem().getOrds()), date);
    }
    
    public RandBean generateCargoQuanDay(int majorMinorIncidental[],
            BodySpecialAdvBean origin, DateBean day)
    {
        RandBean r = new RandBean();
        RandLogic.setMagic(r, origin.getSeed() ^ day.getDays(),
                (RandBean.PAS_MAGIC ^ CargoBean.CC_CARGO));

        int prod = origin.getProduction();
        majorMinorIncidental[0] += prod / 20;
        majorMinorIncidental[1] += prod / 15;
        majorMinorIncidental[2] += prod / 10;
        return r;
    }

    public RandBean generateFreightQuanDay(int majorMinorIncidental[],
            BodySpecialAdvBean origin, BodySpecialAdvBean destination,
            DateBean day)
    {
        RandBean r = new RandBean();
        RandLogic.setMagic(r, origin.getSeed() ^ destination.getSeed() ^ day.getDays(),
                (RandBean.PAS_MAGIC ^ (long)CargoBean.CC_FREIGHT));

        int prod = Math.min(origin.getProduction(), destination.getDemand());
        majorMinorIncidental[0] += prod / 20;
        majorMinorIncidental[1] += prod / 15;
        majorMinorIncidental[2] += prod / 10;
        return r;
    }

    private void generateCargo(int majorMinorIncidental[], List<CargoBean> cargoList,
            BodySpecialAdvBean origin, RandBean r, DateBean date)
    {
        generateCargo(majorMinorIncidental[0], 2, cargoList, origin, r, date);
        generateCargo(majorMinorIncidental[1], 1, cargoList, origin, r, date);
        generateCargo(majorMinorIncidental[2], 0, cargoList, origin, r, date);
    }

    private void generateCargo(int quan, int size, List<CargoBean> cargoList,
            BodySpecialAdvBean origin, RandBean r, DateBean date)
    {
        while (quan-- > 0)
        {
            CargoBean c = generateCargo(r, origin, size, date);
            if (c != null)
                cargoList.add(c);
        }
    }

    private CargoBean generateCargo(RandBean r, BodySpecialAdvBean origin,
            int size, DateBean date)
    {
        GoodBean g;
        CargoBean ret;

        g = getGood(r, origin);
        if (g == null)
            return null;
        ret = newCargoBean();
        ret.setOID(r.getSeed());
        ret.setOrigin(origin.getURI());
        ret.setQuantity(RandLogic.D(r, 1) + size * 5);
        setByGood(ret, g, r);
        if (ret.isPer())
        {
            DateBean d = ret.getBestBy();
            d.set(date);
            d.setDay(d.getDay() + RandLogic.D(r, 6));
        }
        if (ret.getPhylum() == CargoBean.CP_DONTCARE)
        {
            if (origin.getSubType() == BodySpecialBean.ST_REFINERY)
                ret.setPhylum(CargoBean.CP_INORGANIC);
            else if (origin.getPrimary() instanceof BodyPopulated)
            {
                UPPBean upp = ((BodyPopulated)origin.getPrimary())
                        .getPopulatedStats().getUPP();
                if (upp.isAg())
                    ret.setPhylum(CargoBean.CP_ORGANIC);
                else if (upp.isIn())
                    ret.setPhylum(CargoBean.CP_INORGANIC);
            }
        }
        return ret;
    }

    private GoodBean getGood(RandBean r, BodySpecialAdvBean origin)
    {
        int[] phylumType = new int[2];
        phylumType[0] = CargoBean.CP_DONTCARE;
        phylumType[1] = CargoBean.GT_NATURAL;
        PopulatedStatsBean stats = LocationLogic
                .findNearestPopulatedStats(origin);
        int tl = stats.getUPP().getTech().getValue();
        int law = stats.getUPP().getTech().getValue();
        GoodBean produces = origin.getProductionGood();

        switch (origin.getSubType())
        {
            case BodySpecialBean.ST_LABBASE:
                if ((produces != null) && (RandLogic.D(r, 1) <= 2))
                    return produces;
                phylumType[0] = CargoBean.CP_DONTCARE;
                phylumType[1] = CargoBean.GT_INFORMATION;
                break;
            case BodySpecialBean.ST_LOCALBASE:
                phylumType[0] = CargoBean.CP_INORGANIC;
                phylumType[1] = CargoBean.GT_MANUFACTURED;
                break;
            case BodySpecialBean.ST_NAVYBASE:
                phylumType[0] = CargoBean.CP_INORGANIC;
                phylumType[1] = CargoBean.GT_MANUFACTURED;
                break;
            case BodySpecialBean.ST_REFINERY:
                if ((produces != null) && (RandLogic.D(r, 1) <= 3))
                    return produces;
                phylumType[0] = CargoBean.CP_NONORGANIC;
                phylumType[1] = CargoBean.GT_PROCESSED;
                break;
            case BodySpecialBean.ST_SCOUTBASE:
                phylumType[0] = CargoBean.CP_DONTCARE;
                phylumType[1] = CargoBean.GT_INFORMATION;
                break;
            case BodySpecialBean.ST_SPACEPORT:
            case BodySpecialBean.ST_STARPORT:
                if ((produces != null) && (RandLogic.D(r, 1) <= 1))
                    return produces;
                getPhylumType(RandLogic.D(r, 2), stats.getUPP(), phylumType);
                break;
        }
        GoodList goods = ImpGenScheme.getGoods(phylumType[0], phylumType[1], tl,
                law);
        return ImpGenScheme.findGood(goods, r.getSeed());
    }

    protected CargoBean newCargoBean()
    {
        return new AdvCargoBean();
    }

    // first index, type = NATURAL, PROCESSED, MANUFACTURED, INFORMATION,
    // NOVELTIES
    // second index, phylum = ORGANIC, INORGANIC
    /*
    private static final String[][] mTypePhylumCheaperCodes = {
            { "AgWa", "AsBa" }, { "AgPo", "InPo" }, { "AgHi", "InHi" },
            { "NaRi", "NiRi" }, { "Po", "Po" },            };
    */
    private static final String[][] mTypePhylumDearerCodes  = {
            { "NaBa", "AgWa" }, { "NaDe", "NiRi" }, { "NaLo", "NiLo" },
            { "AgNi", "InNa" }, { "Ri", "Ri" },            };
    private static final String[] mMilitaryCheapies = {
            "Exp", "Arm", "Weap", "Amm", "Blade", };
    private static final String[] mScoutCheapies = {
            "Record", "Pictures", };
    private static final String[] mLabCheapies = {
            "Data", "Write", "Software", };
    private static final String[] mRefineryCheapies = {
            "Composites", "Alloys", "Steel", "Copper", "Tin", "Polymers", "Petrochemicals"};
    private static final String[] mMilitaryDearies = {
            "Beverage", "Food", "Teas", };
    private static final String[] mScoutDearies = {
            "New", "Curio", "Artifact", };
    private static final String[] mLabDearies = {
            "Parts", "Computers", "Software", };
    private static final String[] mRefineryDearies = {
            "Ore", "Raw", "Nitro", };

    private static int hits(String hitters, String tradeCodes)
    {
        int count = 0;
        for (int hits = 0; hits < hitters.length(); hits += 2)
            if (tradeCodes.indexOf(hitters.substring(hits, hits + 2)) >= 0)
                count++;
        return count;
    }

    private static int hits(String[] hitters, String tradeCodes)
    {
        int count = 0;
        for (int hits = 0; hits < hitters.length; hits++)
            if (tradeCodes.indexOf(hitters[hits]) >= 0)
                count++;
        return count;
    }

    public static double getValueAt(AdvCargoBean c, BodySpecialAdvBean loc)
    {
        double base = c.getBaseValue() * c.getQuantity();
        PopulatedStatsBean stats = LocationLogic.findNearestPopulatedStats(loc);
        int tl = stats.getUPP().getTech().getValue();
        // exchange rate
        base *= UPPLogic.getXRate(stats.getUPP());
        // tech difference
        if (c.getTechLevel() > tl)
            base *= 1.0 + 0.05 * (c.getTechLevel() - tl);
        else if (c.getTechLevel() < tl - 4)
            base *= 1.0 + 0.01 * (tl - c.getTechLevel());
        // trade classification mods
        if (c.getPhylum() < CargoBean.CP_DONTCARE)
        {
            String tc = UPPLogic.getTradeCodesDesc(stats.getUPP());
            base *= 1.0 + .1 * hits(mTypePhylumDearerCodes[c.getType()][c
                    .getPhylum()], tc);
            base *= 1.0 - .1 * hits(mTypePhylumDearerCodes[c.getType()][c
                    .getPhylum()], tc);
        }
        // location specific stuff
        switch (loc.getSubType())
        {
            case BodySpecialBean.ST_LOCALBASE:
            case BodySpecialBean.ST_NAVYBASE:
                base *= 1.0 + .1 * hits(mMilitaryDearies, c.getName());
            	base *= 1.0 - .1 * hits(mMilitaryCheapies, c.getName());
                break;
            case BodySpecialBean.ST_REFINERY:
                base *= 1.0 + .1 * hits(mRefineryDearies, c.getName());
            	base *= 1.0 - .1 * hits(mRefineryCheapies, c.getName());
                break;
            case BodySpecialBean.ST_LABBASE:
                base *= 1.0 + .1 * hits(mLabDearies, c.getName());
            	base *= 1.0 - .1 * hits(mLabCheapies, c.getName());
                break;
            case BodySpecialBean.ST_SCOUTBASE:
                base *= 1.0 + .1 * hits(mScoutDearies, c.getName());
            	base *= 1.0 - .1 * hits(mScoutCheapies, c.getName());
                break;
        }
        if ((loc.getProductionGood() != null) && c.getName().equals(loc.getProductionGood().getName()))
                base /= 2.0;
        if ((loc.getDemandGood() != null) && c.getName().equals(loc.getDemandGood().getName()))
                base *= 2.0;
        return base;
    }
}