package jo.ttg.core.report.sys;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import jo.ttg.logic.sys.SystemLogic;
import jo.util.utils.xml.XMLEditUtils;

public class TTGTravelTimeReport implements ITTGHTMLReport
{
    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "System Travel Times";
    }

    @Override
    public String getName(URIBean bean)
    {
        if (bean instanceof SystemBean)
            return ((SystemBean)bean).getName()+" Travel Times";
        else if (bean instanceof MainWorldBean)
            return ((MainWorldBean)bean).getName()+" Travel Times";
        else
            throw new IllegalArgumentException("Unhandled bean '"+bean.getClass().getCanonicalName()+"'");
    }

    @Override
    public boolean isReportable(URIBean bean)
    {
        return (bean instanceof SystemBean) || (bean instanceof MainWorldBean);
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        SystemBean sys;
        if (bean instanceof SystemBean)
            sys = (SystemBean)bean;
        else if (bean instanceof MainWorldBean)
            sys = SystemLogic.getFromOrds(scheme, ((MainWorldBean)bean).getOrds());
        else
            throw new IllegalArgumentException("Unhandled bean '"+bean.getClass().getCanonicalName()+"'");
        List<BodyBean> interestingBodies = new ArrayList<BodyBean>();
        interestingBodies.addAll(sys.getSystemRoot().getAllSatelites());
        for (Iterator<BodyBean> i = interestingBodies.iterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b instanceof BodyGiantBean)
                continue;
            else if (b instanceof BodyToidsBean)
            {
                if (((BodyToidsBean)b).getPopulatedStats().getUPP().getPop().getValue() >= 1)
                    continue;
            }
            else if (b instanceof BodyWorldBean)
            {
                if (((BodyWorldBean)b).getPopulatedStats().getUPP().getPop().getValue() >= 1)
                    continue;
                if (((BodyWorldBean)b).getPopulatedStats().getUPP().getSize().getValue() >= 2)
                    continue;
            }
            i.remove();
        }
        Collections.sort(interestingBodies, new Comparator<BodyBean>() {
            @Override
            public int compare(BodyBean object1, BodyBean object2)
            {
                return object1.getName().compareTo(object2.getName());
            }
        });
        Node root = doc.createElement("div");
        XMLEditUtils.addAttribute(root, "id", sys.getURI()+"$travelTimes");
        DistCapabilities caps = new DistCapabilities();
        for (int man = 1; man <= 6; man++)
        {
            caps.setAcceleration(man);
            report(root, sys, interestingBodies, caps, scheme);
        }
        return root;
    }

    private void report(Node root, SystemBean sys, List<BodyBean> interestingBodies, DistCapabilities caps, IGenScheme scheme)
    {
        Node base = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addAttribute(base, "base", sys.getURI()+"$travelTimes"+caps.getAcceleration());
        XMLEditUtils.addTextTag(base, "h2", caps.getAcceleration()+"G Acceleration");
        Node table = XMLEditUtils.addElement(root, "table");
        XMLEditUtils.addAttribute(table, "border", "1");
        // header
        Node header = XMLEditUtils.addElement(table, "tr");
        XMLEditUtils.addElement(header, "td"); // empty
        for (int i = 0; i < interestingBodies.size() - 1; i++)
        {
            BodyBean w = interestingBodies.get(i);
            Node td = XMLEditUtils.addElement(header, "td");
            Node svgNode = XMLEditUtils.addElement(td, "svg");
            SVGHelper svg = new SVGHelper(svgNode);
            svg.setWidth("24px");
            svg.setHeight("24px");
            svg.setViewBox("-.5, -.5, 1, 1");
            SystemReport.drawBody(svg, 0, 0, w);
            XMLEditUtils.addText(td, w.getName());
        }
        // distances
        for (int j = interestingBodies.size() - 1; j > 0; j--)
        {
            BodyBean from = interestingBodies.get(j);
            Node tr = XMLEditUtils.addElement(table, "tr");
            Node td = XMLEditUtils.addElement(tr, "td");
            Node svgNode = XMLEditUtils.addElement(td, "svg");
            SVGHelper svg = new SVGHelper(svgNode);
            svg.setWidth("24px");
            svg.setHeight("24px");
            svg.setViewBox("-.5, -.5, 1, 1");
            SystemReport.drawBody(svg, 0, 0, from);
            XMLEditUtils.addText(td, from.getName());
            for (int i = 0; i < j; i++)
            {
                BodyBean to = interestingBodies.get(i);
                td = XMLEditUtils.addElement(tr, "td");
                XMLEditUtils.addAttribute(td, "align", "right");
                try
                {
                    List<DistTransition> traverse = TraverseLogic.calcTraverse(from.getURI(), to.getURI(), caps, scheme);
                    DistConsumption cons = ConsumptionLogic.calcConsumption(traverse, caps);
                    XMLEditUtils.addText(td, cons.getTime().toElapsedString());
                }
                catch (TraverseException e)
                {
                    e.printStackTrace();
                    XMLEditUtils.addText(td, "???");
                }
                if (from.isMainworld() || to.isMainworld())
                    XMLEditUtils.addAttribute(td, "style", "background-color: #D0D0D0;");
            }            
        }
    }
}
