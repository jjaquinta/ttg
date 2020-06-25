/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.logic.gen;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.GoodBean;
import jo.ttg.gen.IGenLanguage;
import jo.ttg.gen.imp.GoodList;
import jo.ttg.gen.imp.ImpGenSystem;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.beans.Bean;
import jo.util.utils.obj.StringUtils;
import ttg.adv.beans.BodySpecialAdvBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvGenSystem extends ImpGenSystem
{
    
    public AdvGenSystem(AdvGenScheme scheme)
    {
        super(scheme);
    }

    /* (non-Javadoc)
     * @see ttg.gen.GenSystem#generateSystem(ttg.beans.OrdBean)
     */
    public SystemBean generateSystem(OrdBean ords)
    {
        // check cache, don't want to add our extra stuff twice.
        SystemBean sys = generateSystemFromCache(ords);
        if (sys != null)
            return sys;
        sys = super.generateSystem(ords);
        int industry = 0;
        int science = 0;
        int tech = 0;
        int law = 0;
        List<BodyBean> giants = new ArrayList<>();
        List<BodyBean> life = new ArrayList<>();
        for (Iterator<BodyBean> i = sys.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean body = i.next();
            if (body instanceof BodyGiantBean)
                giants.add(body);
            if ((body instanceof BodyWorldBean) && ((BodyWorldBean)body).getStatsAtm().isLife())
                life.add(body);
            if (!(body instanceof BodyPopulated))
                continue;
            RandBean rnd = new RandBean();
            rnd.setSeed(body.getSeed() + 8);
            PopulatedStatsBean stats = ((BodyPopulated)body).getPopulatedStats();
            if (stats.getUPP().getTech().getValue() > tech)
                tech = stats.getUPP().getTech().getValue();
            if (stats.getUPP().getLaw().getValue() > law)
                tech = stats.getUPP().getLaw().getValue();
            int demand = getDemand(stats);
            int production = getProduction(stats);
            if (stats.getUPP().getPort().isCanDock())
                addPorts(body, stats, rnd, demand, production);
            if (stats.isNavalBase())
            {
                addNavalBase(body, stats, rnd, demand, production);
                industry++;
            }
            if (stats.isScoutBase() || stats.isScoutWay())
            {
                addScoutBase(body, stats, rnd, demand, production, tech, law);
                science++;
            }
            int tl = stats.getUPP().getTech().getValue();
            if (tl > 12)
                science += (tl - 6)/4;
            if (stats.getUPP().isIn())
                industry += 3;
            if (!stats.getUPP().isNi())
                industry++;
        }
        RandBean rnd = new RandBean();
        rnd.setSeed(sys.getSeed() + 9);
        while (industry > 0)
        {
            if ((industry >= 6) || (RandLogic.D(rnd, 1) <= industry))
                addRefinery(sys, giants, rnd, tech, law);
            industry -= 6;
        }
        while (science > 0)
        {
            if ((science >= 6) || (RandLogic.D(rnd, 1) <= science))
                addLab(sys, life, rnd, tech, law);
            science -= 6;
        }
        return sys;
    }
    
    private static BodySpecialAdvBean addSpecial(BodyBean body, int subType, Bean specialInfo, String name, int orbit, RandBean rnd, int demand, int production)
    {
        if (body instanceof BodyToidsBean)
            orbit = 0;        
        BodySpecialAdvBean special = new BodySpecialAdvBean();
        name = StringUtils.substitute(name, "%n", body.getName());
        special.setSystem(body.getSystem());
        special.setSubType(subType);
        special.setName(name);
        special.setSpecialInfo(specialInfo);
        special.setDensity(1.0);
        special.setDiameter(0.0);
        special.setEccentricity(1.0);
        special.setMainworld(false);
        special.setMass(0);
        special.setOrbit(BodyBean.convRadiusToOrbit(body.getDiameter()*orbit));
        special.setSeed(rnd.getSeed());
        special.setDemand(demand + RandLogic.D(rnd, 1));
        special.setProduction(production + RandLogic.D(rnd, 1));
        //System.out.println(name+"->"+special.getOrbitalRadius()/body.getDiameter());
        body.addSatelites(special);
        return special;
    }

    private void addPorts(BodyBean b, PopulatedStatsBean stats, RandBean rnd, int demand, int production)
    {
        int port = stats.getUPP().getPort().getValue();
        int tl = stats.getUPP().getTech().getValue();
        int pop = stats.getUPP().getPop().getValue();
        if ((port >= 'A' && (port <= 'E')))
        {	// proper starport
            addPort(b, port, "%n System %p", (tl > 10) ? RandLogic.D(rnd, 3) : 0, rnd, demand, production);
            IGenLanguage lang = getScheme().getGeneratorLanguage();
            while ((pop >= 7) && (port <= 'C'))
            {
                String name = lang.generatePlaceName(b.getSystem().getOrds(), b.getSystem().getOrds(), stats.getAllegiance(), rnd);
                addPort(b, port - 'A' + 'F', name+" %p", (tl > 10) ? RandLogic.D(rnd, 3) : 0, rnd, demand, production);
                pop -= 2;
                port++;
            }
        }
        else if ((port >= 'F' && (port <= 'H')))
        {	// space port
            addPort(b, port, "%n %p", (tl > 10) ? RandLogic.D(rnd, 3) : 0, rnd, demand, production);
        }
    }
    
    private void addPort(BodyBean body, int type, String name, int orbit, RandBean rnd, int demand, int production)
    {
        String pName;
        int subType;
        if ((type >= 'A') && (type <= 'E'))
        {
            if (orbit == 0)
                pName = "Downport";
            else
                pName = "Starport";
            subType = BodySpecialBean.ST_STARPORT;
        }
        else
        {
            pName = "Spaceport";
            subType = BodySpecialBean.ST_SPACEPORT;
        }
        name = StringUtils.substitute(name, "%p", pName);
        UPPPorBean p = new UPPPorBean();
        p.setValue(type);
        double mod = getPortMod(type);
        BodySpecialAdvBean port = addSpecial(body, subType, p, name, orbit, rnd, (int)(demand*mod), (int)(production*mod));
        UPPBean upp = ((BodyPopulated)body).getPopulatedStats().getUPP();
        if ((type >= 'A') && (type <= 'E'))
        {
            port.setProductionGood(getGood(CargoBean.CP_DONTCARE, CargoBean.GT_MANUFACTURED, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
            port.setDemandGood(getGood(CargoBean.CP_DONTCARE, CargoBean.GT_PROCESSED, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
        }
        else
        {
            if (upp.isAg())
                port.setProductionGood(getGood(CargoBean.CP_ORGANIC, CargoBean.GT_NATURAL, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
            else if (upp.isIn())
                port.setProductionGood(getGood(CargoBean.CP_INORGANIC, CargoBean.GT_NATURAL, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
            if (upp.isNa())
                port.setDemandGood(getGood(CargoBean.CP_ORGANIC, CargoBean.GT_NATURAL, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
            else if (upp.isNi())
                port.setDemandGood(getGood(CargoBean.CP_INORGANIC, CargoBean.GT_NATURAL, upp.getTech().getValue(), upp.getLaw().getValue(), RandLogic.rand(rnd)));
        }
    }

    private void addNavalBase(BodyBean b, PopulatedStatsBean stats, RandBean rnd, int demand, int production)
    {
        if (b.getPrimary() instanceof BodyGiantBean)
            addSpecial(b.getPrimary(), BodySpecialBean.ST_NAVYBASE, null, "Fort "+b.getName(), 90, rnd, demand, production);
        else
            addSpecial(b, BodySpecialBean.ST_NAVYBASE, null, "Fort %n", 90, rnd, demand*2, production*2);
    }

    private void addScoutBase(BodyBean b, PopulatedStatsBean stats, RandBean rnd, int demand, int production, int tech, int law)
    {
        BodyBean[] children = b.getSatelites();
        for (int i = children.length - 1; i >= 0; i--)
            if (children[i] instanceof BodyWorldBean)
            {
                addSpecial(children[i], BodySpecialBean.ST_SCOUTBASE, null, b.getName()+" Scout Base", RandLogic.D(rnd, 10), rnd, 0, 0);
                return;
            }
        BodySpecialAdvBean scout = addSpecial(b, BodySpecialBean.ST_SCOUTBASE, null, "%n Scout Base", RandLogic.D(rnd, 10), rnd, demand, production);
        scout.setDemandGood(getGood(CargoBean.CP_DONTCARE, CargoBean.GT_INFORMATION, tech, law, RandLogic.rand(rnd)));
    }

    private void addRefinery(SystemBean sys, List<BodyBean> giants, RandBean rnd, int tech, int law)
    {
        BodyBean parent;
        if (giants.size() == 0)
            parent = sys.getSystemRoot();
        else
            parent = (BodyBean)giants.get(RandLogic.rand(rnd)%giants.size());
        IGenLanguage lang = getScheme().getGeneratorLanguage();
        String name = lang.generatePlaceName(sys.getOrds(), sys.getOrds(), "Im", rnd);
        BodySpecialAdvBean refinery = addSpecial(parent, BodySpecialBean.ST_REFINERY, null, name+" Refinery", RandLogic.D(rnd, 10), rnd, RandLogic.D(rnd, tech/4), RandLogic.D(rnd, tech/4));
        refinery.setDemandGood(getGood(CargoBean.CP_INORGANIC, CargoBean.GT_NATURAL, tech, law, RandLogic.rand(rnd)));
        refinery.setProductionGood(getGood(CargoBean.CP_INORGANIC, CargoBean.GT_PROCESSED, tech, law, RandLogic.rand(rnd)));
    }

    private void addLab(SystemBean sys, List<BodyBean> life, RandBean rnd, int tech, int law)
    {
        BodyBean parent;
        if (life.size() == 0)
            parent = SystemLogic.findMainworld(sys);
        else
            parent = (BodyBean)life.get(RandLogic.rand(rnd)%life.size());
        IGenLanguage lang = getScheme().getGeneratorLanguage();
        String name = lang.generatePlaceName(sys.getOrds(), sys.getOrds(), "Im", rnd);
        BodySpecialAdvBean lab = addSpecial(parent, BodySpecialBean.ST_LABBASE, null, name+" Lab", RandLogic.D(rnd, 10), rnd, RandLogic.D(rnd, tech/4), RandLogic.D(rnd, tech/4));
        lab.setDemandGood(getGood(CargoBean.CP_DONTCARE, CargoBean.GT_INFORMATION, tech, law, RandLogic.rand(rnd)));
        lab.setProductionGood(getGood(CargoBean.CP_DONTCARE, CargoBean.GT_NOVELTIES, tech, law, RandLogic.rand(rnd)));
    }
    
    private GoodBean getGood(int phylum, int type, int tech, int law, int rnd)
    {
        GoodList goods = AdvGenScheme.getGoods(phylum, type, tech, law);
        GoodBean good = AdvGenScheme.findGood(goods, rnd);
        return good;
    }
    
    private int getDemand(PopulatedStatsBean stats)
    {
        int ret = stats.getUPP().getPop().getValue();
        if (stats.getTravelZone() == 'R')
            ret += 2;
        else if (stats.getTravelZone() == 'A')
            ret++;
        if (stats.getUPP().isBa())
            ret++;
        if (stats.getUPP().isHi())
            ret++;
        if (stats.getUPP().isNa())
            ret++;
        if (stats.getUPP().isNi())
            ret++;
        if (stats.getUPP().isRi())
            ret++;
        return ret;
    }
    
    private int getProduction(PopulatedStatsBean stats)
    {
        int ret = stats.getUPP().getPop().getValue();
        if (stats.getTravelZone() == 'R')
            ret -= 2;
        else if (stats.getTravelZone() == 'A')
            ret--;
        if (stats.getUPP().isAg())
            ret++;
        if (stats.getUPP().isIn())
            ret++;
        if (stats.getUPP().isPo())
            ret++;
        if (stats.getUPP().isWa())
            ret++;
        return ret;
    }
    
    private static double getPortMod(int port)
    {
        switch (port)
        {
            case 'A':
                return 5.0;
            case 'B':
                return 4.0;
            case 'C':
                return 3.0;
            case 'D':
                return 2.0;
            case 'E':
                return 1.0;
            case 'F':
                return 2.0;
            case 'G':
                return 1.5;
            case 'H':
                return 1.0;
        }
        return 0.5;
    }
}
