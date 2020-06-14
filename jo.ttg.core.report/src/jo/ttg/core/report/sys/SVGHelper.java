package jo.ttg.core.report.sys;

import jo.util.utils.xml.XMLEditUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Node;

public class SVGHelper
{
    private Node    mSVG;
    
    public SVGHelper(Node svg)
    {
        mSVG = svg;
        if ("svg".equals(svg.getNodeName()))
        {
            XMLEditUtils.addAttribute(mSVG, "version", "1.1");
            XMLEditUtils.addAttribute(mSVG, "xmlns", "http://www.w3.org/2000/svg");
            XMLEditUtils.addAttribute(mSVG, "overflow", "visible");
            XMLEditUtils.addAttribute(mSVG, "xml:space", "preserve");
        }
    }
    
    public void setWidth(String txt)
    {
        XMLEditUtils.addAttribute(mSVG, "width", txt);
    }
    
    public void setHeight(String txt)
    {
        XMLEditUtils.addAttribute(mSVG, "height", txt);
    }
    
    public void setViewBox(String txt)
    {
        XMLEditUtils.addAttribute(mSVG, "viewBox", txt);
    }
    
    private Node addDef(String name, String id)
    {
        // find root
        Node root = mSVG;
        while (!"svg".equals(root.getNodeName()))
        {
            root = root.getParentNode();
            if (root == null)
                throw new IllegalStateException("Cannot find svg parent!");
        }
        // find defs
        Node defs = root.getFirstChild();
        if (defs != null)
            while (!"defs".equals(defs.getNodeName()))
            {
                defs = defs.getNextSibling();
                if (defs == null)
                    break;
            }
        if (defs == null)
        {
            defs = root.getOwnerDocument().createElement("defs");
            if (root.getFirstChild() == null)
                root.appendChild(defs);
            else
                root.insertBefore(defs, root.getFirstChild());
        }
        for (Node n = defs.getFirstChild(); n != null; n = n.getNextSibling())
            if (name.equals(n.getNodeName()) && id.equals(XMLUtils.getAttribute(n, "id")))
                return null;    // it is already there
        Node newDef = XMLEditUtils.addElement(defs, name);
        XMLEditUtils.addAttribute(newDef, "id", id);
        return newDef;
    }
    
    public Node addRadialGradient(String id)
    {
        return addDef("radialGradient", id);
    }
    
    public Node addLinearGradient(String id)
    {
        return addDef("linearGradient", id);
    }
    public Node addPath(String id, String path)
    {
        Node p = addDef("path", id);
        XMLEditUtils.addAttribute(p, "d", path);        
        return p;
    }
    
    private void addColor(Node n, String key, Object val)
    {
        if (val == null)
            return;
        if (val instanceof Integer)
            XMLEditUtils.addAttribute(n, key, String.format("#%06X", (Integer)val));
        else
            XMLEditUtils.addAttribute(n, key, val.toString());
    }
    
    private void addStyle(Node n, SVGStyle style)
    {
        if (style == null)
            return;
        if (style.mStroke != null)
        {
            addColor(n, "stroke", style.mStroke);
            if (style.mStrokeWidth != null)
                XMLEditUtils.addAttribute(n, "stroke-width", String.format("%f", style.mStrokeWidth));
            if (style.mStrokeLineCap != null)
                XMLEditUtils.addAttribute(n, "stroke-linecap", style.mStrokeLineCap);
            if (style.mStrokeDashArray != null)
                XMLEditUtils.addAttribute(n, "stroke-dasharray", style.mStrokeDashArray);
            if (style.mStrokeOpacity != null)
                XMLEditUtils.addAttribute(n, "stroke-opacity", String.format("%f", style.mStrokeOpacity));
        }
        else
            XMLEditUtils.addAttribute(n, "stroke", "none");
        if (style.mFill != null)
        {
            addColor(n, "fill", style.mFill);
            if (style.mFillOpacity != null)
                XMLEditUtils.addAttribute(n, "fill-opacity", String.format("%f", style.mFillOpacity));
        }
        else
            XMLEditUtils.addAttribute(n, "fill", "none");
        if (style.mFontWeight != null)
            XMLEditUtils.addAttribute(n, "font-weight", style.mFontWeight);
        if (style.mFontSize != null)
            XMLEditUtils.addAttribute(n, "font-size", style.mFontSize);
        if (style.mFontStyle != null)
            XMLEditUtils.addAttribute(n, "font-style", style.mFontStyle);
    }

    public Node drawLine(SVGStyle style, double x1, double y1, double x2, double y2)
    {
        Node n = XMLEditUtils.addElement(mSVG, "line");
        addStyle(n, style);
        XMLEditUtils.addAttribute(n, "x1", String.format("%f", x1));
        XMLEditUtils.addAttribute(n, "y1", String.format("%f", y1));
        XMLEditUtils.addAttribute(n, "x2", String.format("%f", x2));
        XMLEditUtils.addAttribute(n, "y2", String.format("%f", y2));
        return n;
    }

    public Node drawPolygon(SVGStyle style, double[] xy)
    {
        Node n = XMLEditUtils.addElement(mSVG, "polygon");
        addStyle(n, style);
        StringBuffer points = new StringBuffer();
        for (int i = 0; i < xy.length; i++)
        {
            if (i > 0)
                if ((i%2) == 1)
                    points.append(",");
                else
                    points.append(" ");
            points.append(String.format("%f", xy[i]));
        }
        XMLEditUtils.addAttribute(n, "points", points.toString());
        return n;
    }

    public Node drawPolyline(SVGStyle style, double[] xy)
    {
        Node n = XMLEditUtils.addElement(mSVG, "polyline");
        addStyle(n, style);
        StringBuffer points = new StringBuffer();
        for (int i = 0; i < xy.length; i++)
        {
            if (i > 0)
                if ((i%2) == 1)
                    points.append(",");
                else
                    points.append(" ");
            points.append(String.format("%f", xy[i]));
        }
        XMLEditUtils.addAttribute(n, "points", points.toString());
        return n;
    }

    public Node drawCircle(SVGStyle style, double cx, double cy, double r)
    {
        Node n = XMLEditUtils.addElement(mSVG, "circle");
        addStyle(n, style);
        XMLEditUtils.addAttribute(n, "cx", String.format("%f", cx));
        XMLEditUtils.addAttribute(n, "cy", String.format("%f", cy));
        XMLEditUtils.addAttribute(n, "r", String.format("%f", r));
        return n;
    }

    public Node drawEllipse(SVGStyle style, double cx, double cy, double rx, double ry)
    {
        Node n = XMLEditUtils.addElement(mSVG, "ellipse");
        addStyle(n, style);
        XMLEditUtils.addAttribute(n, "cx", String.format("%f", cx));
        XMLEditUtils.addAttribute(n, "cy", String.format("%f", cy));
        XMLEditUtils.addAttribute(n, "rx", String.format("%f", rx));
        XMLEditUtils.addAttribute(n, "ry", String.format("%f", ry));
        return n;
    }

    public Node drawText(SVGStyle style, double x, double y, String txt)
    {
        Node n = XMLEditUtils.addTextTag(mSVG, "text", txt);
        addStyle(n, style);
        XMLEditUtils.addAttribute(n, "x", String.format("%f", x));
        XMLEditUtils.addAttribute(n, "y", String.format("%f", y));
        return n;        
    }

    public Node drawText(SVGStyle style, String pathID, String txt)
    {
        Node n = XMLEditUtils.addElement(mSVG, "text");
        addStyle(n, style);
        Node tp = XMLEditUtils.addTextTag(n, "textPath", txt);
        XMLEditUtils.addAttribute(tp, "xlink:href", pathID);
        return n;        
    }

    public Node drawPath(SVGStyle style, String txt)
    {
        Node n = XMLEditUtils.addElement(mSVG, "path");
        addStyle(n, style);
        XMLEditUtils.addAttribute(n, "d", txt);
        return n;        
    }
}
