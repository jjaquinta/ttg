package jo.ttg.core.report.surf;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.LocBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.report.body.BodySVGLogic;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.core.report.sys.SVGStyle;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.BodyLogic;
import jo.ttg.utils.DisplayUtils;
import jo.util.utils.xml.XMLEditUtils;

public class TTGTacticalReport implements ITTGHTMLReport
{
    private double mOX;
    private double mOY;
    private double mMult;

    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "Tactical Map";
    }

    @Override
    public String getName(URIBean bean)
    {
        return "Tactical Map of "+((BodyBean)bean).getName();
    }

    @Override
    public boolean isReportable(URIBean bean)
    {        
        return bean instanceof BodyBean;
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        DateBean now = new DateBean();
        now.setYear(1100);
        BodyBean world = (BodyBean)bean;
        calculateBounds(world, now);
        SVGStyle nameStyle = new SVGStyle();
        nameStyle.mStroke = 0x202020;
        nameStyle.mStrokeWidth = .02;
        nameStyle.mFill = 0x202020;
        nameStyle.mFontSize = "12px";

        Node root = doc.createElement("div");
        Node map = XMLEditUtils.addElement(root, "div");
        Node svgNode = XMLEditUtils.addElement(map, "svg");
        SVGHelper svg = new SVGHelper(svgNode);
        svg.setWidth("1792px");
        svg.setHeight("896px");
        
        svg.setViewBox("-896, -448, 1792, 896");
        BodySVGLogic.addGradients(svg);
        drawObject(svg, world, nameStyle, now);

        Node text = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addTextTag(text, "h2", "Objects of "+world.getName());
        Node table = XMLEditUtils.addElement(text, "table");
        Node row = XMLEditUtils.addElement(table, "tr");
        XMLEditUtils.addTextTag(row, "th", "X");
        XMLEditUtils.addTextTag(row, "th", "Y");
        XMLEditUtils.addTextTag(row, "th", "Diameter");
        XMLEditUtils.addTextTag(row, "th", "World");
        XMLEditUtils.addTextTag(row, "th", "Details");
        describeObject(table, world, now);

        return root;
    }

    private void describeObject(Node table, BodyBean world, DateBean now)
    {
        double d = world.getDiameter();
        LocBean l = BodyLogic.getLocation(world, now);
        double x = l.getX() - mOX;
        double y = l.getY() - mOY;
        
        Node row = XMLEditUtils.addElement(table, "tr");
        Node cell;
        XMLEditUtils.addAttribute(row, "style", "font-family:Consolas,Monaco,Lucida Console,Liberation Mono,DejaVu Sans Mono,Bitstream Vera Sans Mono,Courier New, monospace;white-space:pre;");
        cell = XMLEditUtils.addTextTag(row, "td", DisplayUtils.formatDistance(x));
        XMLEditUtils.addAttribute(cell, "style", "text-align: right;");
        cell = XMLEditUtils.addTextTag(row, "td", DisplayUtils.formatDistance(y));
        XMLEditUtils.addAttribute(cell, "style", "text-align: right;");
        cell = XMLEditUtils.addTextTag(row, "td", DisplayUtils.formatDistance(d));
        XMLEditUtils.addAttribute(cell, "style", "text-align: right;");
        if (world instanceof BodyGiantBean)
            XMLEditUtils.addTextTag(row, "td", ((BodyGiantBean)world).getOneLineDesc());
        else if (world instanceof BodyStarBean)
            XMLEditUtils.addTextTag(row, "td", ((BodyStarBean)world).getOneLineDesc());
        else if (world instanceof BodyWorldBean)
            XMLEditUtils.addTextTag(row, "td", ((BodyWorldBean)world).getOneLineDesc());
        else if (world instanceof BodyToidsBean)
            XMLEditUtils.addTextTag(row, "td", ((BodyToidsBean)world).getOneLineDesc());
        else if (world instanceof BodySpecialBean)
            XMLEditUtils.addTextTag(row, "td", ((BodySpecialBean)world).getOneLineDesc());
        else
            XMLEditUtils.addTextTag(row, "td", "");

        for (BodyBean c : world.getSatelites())
            describeObject(table, c, now);
    }

    private void calculateBounds(BodyBean world, DateBean now)
    {
        LocBean origin = BodyLogic.getLocation(world, now);
        mOX = origin.getX();
        mOY = origin.getY();
        double lowX = 0;
        double highX = 0;
        double lowY = 0;
        double highY = 0;
        double lowZ = 0;
        double highZ = 0;
        for (BodyBean w : world.getAllSatelites())
        {
            LocBean l = BodyLogic.getLocation(w, now);
            double x = l.getX() - mOX;
            double y = l.getY() - mOY;
            lowX = Math.min(lowX, x);
            highX = Math.max(highX, x);
            lowY = Math.min(lowY, y);
            highY = Math.max(highY, y);
            lowZ = Math.min(lowZ, l.getZ());
            highZ = Math.max(highZ, l.getZ());
        }
        double xRad = Math.max(-lowX, highX);
        double yRad = Math.max(-lowY, highY);
        double xScale = 1792/(xRad*2);
        double yScale = 896/(yRad*2);
        double scale = Math.min(xScale, yScale); // pixels per AU
        
        mMult = scale;
        
        //System.out.println("Tactial space cenetered="+mOX+","+mOY+" scale="+mMult+" radius="+xRad*mMult+","+yRad*mMult);
    }

    private void drawObject(SVGHelper svg, BodyBean world, SVGStyle nameStyle, DateBean now)
    {
        double d = world.getDiameter();
        LocBean l = BodyLogic.getLocation(world, now);
        double x = (l.getX() - mOX)*mMult;
        double y = (l.getY() - mOY)*mMult;
        double bodyRadius = d/2*mMult;
        SVGStyle bodyStyle = BodySVGLogic.getBodyStyle(world);
        BodySVGLogic.ensureStyle(svg, bodyStyle);
        svg.drawCircle(bodyStyle, x, y, bodyRadius/2);
        svg.drawText(nameStyle, x+bodyRadius, y, world.getName());
        
        for (BodyBean c : world.getSatelites())
            drawObject(svg, c, nameStyle, now);
    }
}
