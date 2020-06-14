package jo.ttg.core.report.sub;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.core.report.sys.SystemReport;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.utils.FormatUtils;
import jo.util.utils.xml.XMLEditUtils;

public class TTGPopulationReport implements ITTGHTMLReport
{
    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "Population";
    }

    @Override
    public String getName(URIBean bean)
    {
        if (bean instanceof SectorBean)
            return ((SectorBean)bean).getName()+" Population";
        else if (bean instanceof SubSectorBean)
            return ((SubSectorBean)bean).getName()+" Population";
        else if (bean instanceof SystemBean)
            return ((SystemBean)bean).getName()+" Population";
        else if (bean instanceof MainWorldBean)
            return ((MainWorldBean)bean).getName()+" Population";
        else
            throw new IllegalArgumentException("Unhandled bean '"+bean.getClass().getCanonicalName()+"'");
    }

    @Override
    public boolean isReportable(URIBean bean)
    {
        return (bean instanceof SectorBean) || (bean instanceof SubSectorBean) 
                || (bean instanceof SystemBean) || (bean instanceof MainWorldBean);
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        List<BodyPopulated> pops = new ArrayList<BodyPopulated>();
        if (bean instanceof SectorBean)
            reportSector(scheme, (SectorBean)bean, pops);
        else if (bean instanceof SubSectorBean)
            reportSubSector(scheme, (SubSectorBean)bean, pops);
        else if (bean instanceof MainWorldBean)
            reportMainWorld(scheme, (MainWorldBean)bean, pops);
        else if (bean instanceof SystemBean)
            reportSystem(scheme, (SystemBean)bean, pops);
        else if (bean instanceof BodyBean)
            reportBody(scheme, (BodyBean)bean, pops);
        Collections.sort(pops, new Comparator<BodyPopulated>() {
            @Override
            public int compare(BodyPopulated o1, BodyPopulated o2)
            {
                int signum = o2.getPopulatedStats().getUPP().getPop().getValue() - o1.getPopulatedStats().getUPP().getPop().getValue();
                return signum;
            }
        });
        Node root = doc.createElement("div");
        XMLEditUtils.addAttribute(root, "id", bean.getURI()+"$population");
        listTopWorlds(root, pops, scheme);
        return root;
    }

    private void reportSector(IGenScheme scheme, SectorBean bean,
            List<BodyPopulated> pops)
    {
        for (SubSectorBean sub : bean.getSubSectors())
            reportSubSector(scheme, sub, pops);
    }

    private void reportSubSector(IGenScheme scheme, SubSectorBean bean,
            List<BodyPopulated> pops)
    {
        for (MainWorldBean mw : bean.getMainWorlds())
            reportMainWorld(scheme, mw, pops);
    }

    private void reportMainWorld(IGenScheme scheme, MainWorldBean bean,
            List<BodyPopulated> pops)
    {
        reportSystem(scheme, SystemLogic.getFromOrds(scheme, bean.getOrds()), pops);
    }

    private void reportSystem(IGenScheme scheme, SystemBean bean,
            List<BodyPopulated> pops)
    {
        reportBody(scheme, bean.getSystemRoot(), pops);
    }

    private void reportBody(IGenScheme scheme, BodyBean bean,
            List<BodyPopulated> pops)
    {
        if (bean instanceof BodyPopulated)
        {
            BodyPopulated pop = (BodyPopulated)bean;
            PopulatedStatsBean stats = pop.getPopulatedStats();
            if (stats.getUPP().getPop().getValue() > 1)
                pops.add(pop);
        }
        for (BodyBean child : bean.getSatelites())
            reportBody(scheme, child, pops);
    }

    private void listTopWorlds(Node root, List<BodyPopulated> interestingBodies, IGenScheme scheme)
    {
        Node table = XMLEditUtils.addElement(root, "table");
        XMLEditUtils.addAttribute(table, "border", "1");
        // header
        Node header = XMLEditUtils.addElement(table, "tr");
        XMLEditUtils.addTextTag(header, "td", ""); // empty
        XMLEditUtils.addTextTag(header, "th", "World");
        XMLEditUtils.addTextTag(header, "th", "Population");
        XMLEditUtils.addTextTag(header, "th", "System");
        int max = Math.min(100, interestingBodies.size());
        for (int i = 0; i < max; i++)
        {
            Node tr = XMLEditUtils.addElement(table, "tr");
            BodyBean w = (BodyBean)interestingBodies.get(i);
            Node cell1 = XMLEditUtils.addElement(tr, "td");
            Node svgNode = XMLEditUtils.addElement(cell1, "svg");
            SVGHelper svg = new SVGHelper(svgNode);
            svg.setWidth("24px");
            svg.setHeight("24px");
            svg.setViewBox("-.5, -.5, 1, 1");
            SystemReport.drawBody(svg, 0, 0, w);
            XMLEditUtils.addTextTag(tr, "td", w.getName());
            if (w instanceof BodyWorldBean)
                XMLEditUtils.addTextTag(tr, "td", FormatUtils.formatCommaNumber((long)((BodyWorldBean)w).getStatsPop().getTotalPop()));
            else if (w instanceof BodyToidsBean)
            {
                int popDigit = ((BodyToidsBean)w).getPopulatedStats().getUPP().getPop().getValue();
                double pop = Math.pow(10, popDigit);
                XMLEditUtils.addTextTag(tr, "td", FormatUtils.formatCommaNumber((long)pop));
            }
            XMLEditUtils.addTextTag(tr, "td", w.getSystem().getName()+" ("+OrdLogic.getShortNum(w.getSystem().getOrds())+")");
        }
    }
}
