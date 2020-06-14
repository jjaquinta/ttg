package jo.ttg.core.report.surf;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import jo.ttg.beans.URIBean;
import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.report.logic.ITTGHTMLReport;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.gen.IGenScheme;
import jo.util.heal.IHEALGlobe;
import jo.util.utils.xml.XMLEditUtils;

public class TTGSurfaceReport implements ITTGHTMLReport
{

    @Override
    public String getID()
    {
        return getClass().getName();
    }

    @Override
    public String getName()
    {
        return "Surface Map";
    }

    @Override
    public String getName(URIBean bean)
    {
        return "Surface of "+((BodyWorldBean)bean);
    }

    @Override
    public boolean isReportable(URIBean bean)
    {        
        return bean instanceof BodyWorldBean;
    }

    @Override
    public Node report(Document doc, IGenScheme scheme, URIBean bean)
    {
        BodyWorldBean world = (BodyWorldBean)bean;
        SurfaceBean surface = scheme.getGeneratorSurface().generateSurface(world);
        IHEALGlobe<MapHexBean> globe = surface.getGlobe();

        Node root = doc.createElement("div");
        coverReport(root, world, globe);
        if (world.getStatsHyd().getNumPlates() > 1)
            PlatesReport(world, globe, root);
        return root;
    }

    private void PlatesReport(BodyWorldBean world, IHEALGlobe<MapHexBean> globe, Node root)
    {
        XMLEditUtils.addAttribute(root, "id", world.getURI()+"cover");
        Node map = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addAttribute(map, "id", world.getURI()+"map");
        Node svgNode = XMLEditUtils.addElement(map, "svg");
        SVGHelper svg = new SVGHelper(svgNode);
        XMLEditUtils.addAttribute(svgNode, "id", world.getURI()+"$svg");
        //svg.setWidth("1024px");
        //svg.setHeight("410px");
        svg.setWidth("1792px");
        svg.setHeight("896px");
        svg.setViewBox("0, -.25, 1.1, .5");
        //BodySVGLogic.addGradients(svg);
        Node legendGroup = XMLEditUtils.addElement(svgNode, "g");
        Node gridGroup = XMLEditUtils.addElement(svgNode, "g");
        PlatesReport.drawPlateMap(new SVGHelper(legendGroup), new SVGHelper(gridGroup), globe);
    }

    private void coverReport(Node root, BodyWorldBean world, IHEALGlobe<MapHexBean> globe)
    {
        XMLEditUtils.addAttribute(root, "id", world.getURI()+"cover");
        Node map = XMLEditUtils.addElement(root, "div");
        XMLEditUtils.addAttribute(map, "id", world.getURI()+"map");
        Node svgNode = XMLEditUtils.addElement(map, "svg");
        SVGHelper svg = new SVGHelper(svgNode);
        XMLEditUtils.addAttribute(svgNode, "id", world.getURI()+"$svg");
        svg.setWidth("1792px");
        svg.setHeight("896px");
        svg.setViewBox("0, -.25, 1.1, .5");
        //BodySVGLogic.addGradients(svg);
        Node legendGroup = XMLEditUtils.addElement(svgNode, "g");
        Node gridGroup = XMLEditUtils.addElement(svgNode, "g");
        CoverReport.drawMap(new SVGHelper(legendGroup), new SVGHelper(gridGroup), globe);
    }
}
