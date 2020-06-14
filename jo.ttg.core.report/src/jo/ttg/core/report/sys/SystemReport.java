package jo.ttg.core.report.sys;

import java.util.List;

import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.core.report.body.BodySVGLogic;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.mw.UPPLogic;
import jo.util.utils.xml.XMLEditUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class SystemReport
{
    private static SVGStyle ORBIT_STYLE = new SVGStyle();
    static
    {
        ORBIT_STYLE.mStroke = 0xE0E0E0;
        ORBIT_STYLE.mStrokeWidth = .1;
        ORBIT_STYLE.mFill = 0xE0E0E0;
        ORBIT_STYLE.mFillOpacity = 0.0;
    }
    private static SVGStyle RING_STYLE = new SVGStyle();
    static
    {
        RING_STYLE.mFill = "url(#ring)";
    }
    private static SVGStyle NAME_STYLE = new SVGStyle();
    static
    {
        NAME_STYLE.mStroke = 0x202020;
        NAME_STYLE.mStrokeWidth = .02;
        NAME_STYLE.mFill = 0x202020;
        NAME_STYLE.mFontSize = ".5px";
    }
    
    public static Node report(Document doc, IGenScheme scheme, SystemBean sys)
    {
        Node root = doc.createElement("div");
        XMLEditUtils.addAttribute(root, "id", sys.getURI()+"$report");
        List<SubSystem> subs = SubSystemLogic.getSubSystems(sys, 20);
        for (SubSystem sub : subs)            
            drawSystemPage(root, sub);
        return root;
    }

    private static void drawSystemPage(Node root, SubSystem sub)
    {
        Node orbits = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addAttribute(orbits, "id", sub.getRoot().getURI()+"$orbits");
        Node svgNode = XMLEditUtils.addElement(orbits, "svg");
        SVGHelper svg = new SVGHelper(svgNode);
        XMLEditUtils.addAttribute(svgNode, "id", sub.getRoot().getURI()+"$svg");
        svg.setWidth("1024px");
        svg.setHeight("768px");
        svg.setViewBox("-13.333, -10, 26.666, 20");
        BodySVGLogic.addGradients(svg);
        Node orbitGroup = XMLEditUtils.addElement(svgNode, "g");
        Node planetGroup = XMLEditUtils.addElement(svgNode, "g");
        drawOrbits(new SVGHelper(planetGroup), new SVGHelper(orbitGroup), 0, 0, sub.getRoot(), sub);
        Node texts = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addAttribute(texts, "id", sub.getRoot().getURI()+"$texts");
        Node table = XMLEditUtils.addElement(texts, "table");
        XMLEditUtils.addAttribute(table, "id", sub.getRoot().getURI()+"$texts_table");
        drawTexts(table, 0, sub.getRoot(), sub);
    }

    private static void drawTexts(Node table, int depth, BodyBean b, SubSystem sub)
    {
        Node tr = XMLEditUtils.addElement(table, "tr");
        
        Node col1 = XMLEditUtils.addElement(tr, "td");
        for (int i = 0; i < depth; i++)
            XMLEditUtils.addText(col1, "&nbsp;");
        Node nameSpan = XMLEditUtils.addTextTag(col1, "span", b.getName());
        XMLEditUtils.addAttribute(nameSpan, "id", b.getRoot().getURI()+"$link");
        
        Node col2 = XMLEditUtils.addElement(tr, "td");
        if (b instanceof BodyPopulated)
        {
            String desc = UPPLogic.getUPPDesc(((BodyPopulated)b).getPopulatedStats().getUPP());
            Node uwpSpan = XMLEditUtils.addTextTag(col2, "span", desc);
            XMLEditUtils.addAttribute(uwpSpan, "id", b.getRoot().getURI()+"$uwp");
        }
        
        Node col3 = XMLEditUtils.addElement(tr, "td");
        if (b instanceof BodyPopulated)
        {
            String desc = UPPLogic.getTradeCodesDesc(((BodyPopulated)b).getPopulatedStats().getUPP());
            Node tcSpan = XMLEditUtils.addTextTag(col3, "span", desc);
            XMLEditUtils.addAttribute(tcSpan, "id", b.getRoot().getURI()+"$tc");
        }
                
        BodyBean[] sats = b.getSatelites();
        for (BodyBean sat : sats)
        {
            if (!sub.getContents().contains(sat))
                continue;
            drawTexts(table, depth + 1, sat, sub);
        }
    }

    private static void drawOrbits(SVGHelper planets, SVGHelper orbits, double ox, double oy, BodyBean b, SubSystem sub)
    {
        drawBody(planets, ox, oy, b);        
        BodyBean[] sats = b.getSatelites();
        double da = Math.PI*2/sats.length;
        double a = Math.PI;
        int r = 1;
        for (int i = 0; i < sats.length; i++)
        {
            BodyBean sat = sats[i];
            if (!sub.getContents().contains(sat))
                continue;
            double w = SubSystemLogic.getOrbitalWidth(sat, sub);
            double rx = (r + w/2.0)*1/2;
            double ry = (r + w/2.0)*1/2;
            double sx = Math.sin(a)*rx;
            double sy = Math.cos(a)*ry;
            if ((sat instanceof BodyWorldBean) && ((BodyWorldBean)sat).getPopulatedStats().isRing())
                orbits.drawEllipse(RING_STYLE, ox, oy, rx, ry);
            else
            {
                orbits.drawEllipse(ORBIT_STYLE, ox, oy, rx, ry);
                StringBuffer path = new StringBuffer();
                double a2 = a;
                double r2 = BodySVGLogic.getBodyRadius(sat)/2;
                double da2 = Math.atan2(r2, rx);
                double rx2 = rx + r2;
                double ry2 = ry + r2;
                for (int j = 0; j < 10; j++)
                {
                    a2 += da2;
                    double sx2 = Math.sin(a2)*rx2;
                    double sy2 = Math.cos(a2)*ry2;
                    if (j == 0)
                        path.append("M ");
                    else
                        path.append("L ");
                    path.append(ox + sx2);
                    path.append(" ");
                    path.append(oy + sy2);
                    path.append(" ");
                }
                planets.addPath(sat.getName()+"Path", path.toString());
                planets.drawText(NAME_STYLE, "#"+sat.getName()+"Path", sat.getName());
            }
            drawOrbits(planets, orbits, ox + sx, oy + sy, sat, sub);
            a += da;
            r += w;
        }
    }

    public static void drawBody(SVGHelper planets, double ox, double oy,
            BodyBean b)
    {
        if ((b instanceof BodyWorldBean) && ((BodyWorldBean)b).getPopulatedStats().isRing())
            ; // don't draw body
        else
        {
            double bodyRadius = BodySVGLogic.getBodyRadius(b);
            SVGStyle bodyStyle = BodySVGLogic.getBodyStyle(b);
            BodySVGLogic.ensureStyle(planets, bodyStyle);
            
            planets.drawCircle(bodyStyle, ox, oy, bodyRadius/2);
            //Node name = planets.drawText(NAME_STYLE, ox, oy + bodyRadius, b.getName());
            //XMLEditUtils.addAttribute(name, "text-anchor", "middle");
        }
    }
}
