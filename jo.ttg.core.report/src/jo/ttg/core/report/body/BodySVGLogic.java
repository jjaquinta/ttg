package jo.ttg.core.report.body;

import org.w3c.dom.Node;

import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.core.report.sys.SVGStyle;
import jo.util.utils.xml.XMLEditUtils;

public class BodySVGLogic
{
    public static double getBodyRadius(BodyBean b)
    {
        if (b instanceof BodyWorldBean)
        {
            BodyWorldBean w = (BodyWorldBean)b;
            return w.getPopulatedStats().getUPP().getSize().getValue()/20.0 + .5;
        }
        else if (b instanceof BodyToidsBean)
        {
            return .5;
        }
        else if (b instanceof BodyGiantBean)
        {
            BodyGiantBean g = (BodyGiantBean)b;
            if (g.getSize() == BodyGiantBean.GS_S)
                return 1.2;
            else
                return 1.0;
        }
        else if (b instanceof BodyStarBean)
        {
            BodyStarBean star = (BodyStarBean)b;
            int type = star.getStarDecl().getStarType();
            type -= type%10;
            switch (type)
            {
                case StarDeclBean.ST_O: 
                    return 1.2;
                case StarDeclBean.ST_B: 
                    return 1.2;
                case StarDeclBean.ST_A: 
                    return 1.2;
                case StarDeclBean.ST_F: 
                    return 1.0;
                case StarDeclBean.ST_G: 
                    return .9;
                case StarDeclBean.ST_K: 
                    return .8;
                case StarDeclBean.ST_M: 
                    return .7;
            }
            return 1.0;
        }
        else if (b instanceof BodySpecialBean)
        {
            return 1.0;
        }
        return 1.0;
    }

    public static SVGStyle getBodyStyle(BodyBean b)
    {
        SVGStyle style = new SVGStyle();
        if (b instanceof BodyWorldBean)
        {
            BodyWorldBean w = (BodyWorldBean)b;
            if (w.isOuterZone())
            {
                style.mFill = "url(#outer)";
            }
            else if (w.isInnerZone())
            {
                style.mFill = "url(#inner)";
            }
            else // if (w.isHabitableZone())
            {
                if (w.getPopulatedStats().getUPP().getAtmos().getValue() <= 1)
                    style.mFill = "grey";
                else if (w.getPopulatedStats().getUPP().getPop().getValue() > 1)
                    style.mFill = "green";
                else
                    style.mFill = "blue";
            }
        }
        else if (b instanceof BodyToidsBean)
        {
            style.mFill = "pink";
        }
        else if (b instanceof BodyGiantBean)
        {
            BodyGiantBean g = (BodyGiantBean)b;
            if (g.getSize() == BodyGiantBean.GS_S)
                style.mFill = "url(#sgg)";
            else
                style.mFill = "url(#lgg)";
        }
        else if (b instanceof BodyStarBean)
        {
            BodyStarBean star = (BodyStarBean)b;
            int type = star.getStarDecl().getStarType();
            type -= type%10;
            switch (type)
            {
                case StarDeclBean.ST_O: 
                    style.mFill = "url(#starO)";
                    break;
                case StarDeclBean.ST_B: 
                    style.mFill = "url(#starB)";
                    break;
                case StarDeclBean.ST_A: 
                    style.mFill = "url(#starA)";
                    break;
                case StarDeclBean.ST_F: 
                    style.mFill = "url(#starF)";
                    break;
                case StarDeclBean.ST_G: 
                    style.mFill = "url(#starG)";
                    break;
                case StarDeclBean.ST_K: 
                    style.mFill = "url(#starK)";
                    break;
                case StarDeclBean.ST_M: 
                    style.mFill = "url(#starM)";
                    break;
                default: 
                    style.mFill = 0x000000;
                    break;
            }
        }
        else if (b instanceof BodySpecialBean)
        {
            BodySpecialBean special = (BodySpecialBean)b;
            if ((special.getSubType() == BodySpecialBean.ST_STARPORT) || (special.getSubType() == BodySpecialBean.ST_SPACEPORT))
                style.mFill = 0x00FFFF;
            else if (special.getSubType() == BodySpecialBean.ST_NAVYBASE)
                style.mFill = 0xFF0000;
            else if (special.getSubType() == BodySpecialBean.ST_SCOUTBASE)
                style.mFill = 0xFFFF00;
            else if (special.getSubType() == BodySpecialBean.ST_LABBASE)
                style.mFill = 0x0000FF;
            else if (special.getSubType() == BodySpecialBean.ST_REFINERY)
                style.mFill = 0x808080;
            else if (special.getSubType() == BodySpecialBean.ST_LOCALBASE)
                style.mFill = 0x00FF00;
        }
        return style;
    }
    
    public static void addGradients(SVGHelper svg)
    {
        addGradients(svg, null);
    }
    
    public static void addGradients(SVGHelper svg, String id)
    {
        if ((id == null) || (id.equals("starO")))
        {
            Node starO = svg.addRadialGradient("starO");
            if (starO != null)
            {
                XMLEditUtils.addAttribute(starO, "cx", "50%");
                XMLEditUtils.addAttribute(starO, "cy", "50%");
                XMLEditUtils.addAttribute(starO, "r", "50%");
                XMLEditUtils.addAttribute(starO, "fx", "50%");
                XMLEditUtils.addAttribute(starO, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starO, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:rgb(255,255,255); stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starO, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starB")))
        {
            Node starB = svg.addRadialGradient("starB");
            if (starB != null)
            {
                XMLEditUtils.addAttribute(starB, "cx", "50%");
                XMLEditUtils.addAttribute(starB, "cy", "50%");
                XMLEditUtils.addAttribute(starB, "r", "50%");
                XMLEditUtils.addAttribute(starB, "fx", "50%");
                XMLEditUtils.addAttribute(starB, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starB, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#bbccff; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starB, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starA")))
        {
            Node starA = svg.addRadialGradient("starA");
            if (starA != null)
            {
                XMLEditUtils.addAttribute(starA, "cx", "50%");
                XMLEditUtils.addAttribute(starA, "cy", "50%");
                XMLEditUtils.addAttribute(starA, "r", "50%");
                XMLEditUtils.addAttribute(starA, "fx", "50%");
                XMLEditUtils.addAttribute(starA, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starA, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#fbf8ff; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starA, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starF")))
        {
            Node starF = svg.addRadialGradient("starF");
            if (starF != null)
            {
                XMLEditUtils.addAttribute(starF, "cx", "50%");
                XMLEditUtils.addAttribute(starF, "cy", "50%");
                XMLEditUtils.addAttribute(starF, "r", "50%");
                XMLEditUtils.addAttribute(starF, "fx", "50%");
                XMLEditUtils.addAttribute(starF, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starF, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#ffffed; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starF, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#000000; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starG")))
        {
            Node starG = svg.addRadialGradient("starG");
            if (starG != null)
            {
                XMLEditUtils.addAttribute(starG, "cx", "50%");
                XMLEditUtils.addAttribute(starG, "cy", "50%");
                XMLEditUtils.addAttribute(starG, "r", "50%");
                XMLEditUtils.addAttribute(starG, "fx", "50%");
                XMLEditUtils.addAttribute(starG, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starG, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#ffff00; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starG, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starK")))
        {
            Node starK = svg.addRadialGradient("starK");
            if (starK != null)
            {
                XMLEditUtils.addAttribute(starK, "cx", "50%");
                XMLEditUtils.addAttribute(starK, "cy", "50%");
                XMLEditUtils.addAttribute(starK, "r", "50%");
                XMLEditUtils.addAttribute(starK, "fx", "50%");
                XMLEditUtils.addAttribute(starK, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starK, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#ff9833; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starK, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("starM")))
        {
            Node starM = svg.addRadialGradient("starM");
            if (starM != null)
            {
                XMLEditUtils.addAttribute(starM, "cx", "50%");
                XMLEditUtils.addAttribute(starM, "cy", "50%");
                XMLEditUtils.addAttribute(starM, "r", "50%");
                XMLEditUtils.addAttribute(starM, "fx", "50%");
                XMLEditUtils.addAttribute(starM, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(starM, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:#ff0000; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(starM, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:#FFFFFF; stop-opacity:0;");
            }
        }
        if ((id == null) || (id.equals("lgg")))
        {
            Node lgg = svg.addLinearGradient("lgg");
            if (lgg != null)
            {
                XMLEditUtils.addAttribute(lgg, "x1", "0%");
                XMLEditUtils.addAttribute(lgg, "y1", "0%");
                XMLEditUtils.addAttribute(lgg, "x2", "0%");
                XMLEditUtils.addAttribute(lgg, "y2", "100%");
                Node stop0 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop0, "offset", "0%");
                XMLEditUtils.addAttribute(stop0, "style", "stop-color:rgb(20,59,140); stop-opacity:1;");
                Node stop1 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "10%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:rgb(20,74,198); stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "20%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:rgb(83,164,243); stop-opacity:1;");
                Node stop3 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop3, "offset", "30%");
                XMLEditUtils.addAttribute(stop3, "style", "stop-color:rgb(78,164,252); stop-opacity:1;");
                Node stop4 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop4, "offset", "40%");
                XMLEditUtils.addAttribute(stop4, "style", "stop-color:rgb(61,145,254); stop-opacity:1;");
                Node stop5 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop5, "offset", "50%");
                XMLEditUtils.addAttribute(stop5, "style", "stop-color:rgb(53,140,254); stop-opacity:1;");
                Node stop6 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop6, "offset", "60%");
                XMLEditUtils.addAttribute(stop6, "style", "stop-color:rgb(34,124,254); stop-opacity:1;");
                Node stop7 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop7, "offset", "70%");
                XMLEditUtils.addAttribute(stop7, "style", "stop-color:rgb(79,167,251); stop-opacity:1;");
                Node stop9 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop9, "offset", "80%");
                XMLEditUtils.addAttribute(stop9, "style", "stop-color:rgb(26,118,246); stop-opacity:1;");
                Node stop8 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop8, "offset", "90%");
                XMLEditUtils.addAttribute(stop8, "style", "stop-color:rgb(19,70,194); stop-opacity:1;");
                Node stop10 = XMLEditUtils.addElement(lgg, "stop");
                XMLEditUtils.addAttribute(stop10, "offset", "100%");
                XMLEditUtils.addAttribute(stop10, "style", "stop-color:rgb(3,25,91); stop-opacity:1;");
            }
        }
        if ((id == null) || (id.equals("sgg")))
        {
            Node sgg = svg.addLinearGradient("sgg");
            if (sgg != null)
            {
                XMLEditUtils.addAttribute(sgg, "x1", "0%");
                XMLEditUtils.addAttribute(sgg, "y1", "0%");
                XMLEditUtils.addAttribute(sgg, "x2", "0%");
                XMLEditUtils.addAttribute(sgg, "y2", "100%");
                Node stop0 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop0, "offset", "0%");
                XMLEditUtils.addAttribute(stop0, "style", "stop-color:rgb(66,62,57); stop-opacity:1;");
                Node stop1 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "10%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:rgb(103,93,86); stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "20%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:rgb(143,145,150); stop-opacity:1;");
                Node stop3 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop3, "offset", "30%");
                XMLEditUtils.addAttribute(stop3, "style", "stop-color:rgb(162,170,185); stop-opacity:1;");
                Node stop4 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop4, "offset", "40%");
                XMLEditUtils.addAttribute(stop4, "style", "stop-color:rgb(147,125,105); stop-opacity:1;");
                Node stop5 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop5, "offset", "50%");
                XMLEditUtils.addAttribute(stop5, "style", "stop-color:rgb(176,180,185); stop-opacity:1;");
                Node stop6 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop6, "offset", "60%");
                XMLEditUtils.addAttribute(stop6, "style", "stop-color:rgb(153,145,142); stop-opacity:1;");
                Node stop7 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop7, "offset", "70%");
                XMLEditUtils.addAttribute(stop7, "style", "stop-color:rgb(160,175,194); stop-opacity:1;");
                Node stop9 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop9, "offset", "80%");
                XMLEditUtils.addAttribute(stop9, "style", "stop-color:rgb(118,113,109); stop-opacity:1;");
                Node stop8 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop8, "offset", "90%");
                XMLEditUtils.addAttribute(stop8, "style", "stop-color:rgb(103,99,98); stop-opacity:1;");
                Node stop10 = XMLEditUtils.addElement(sgg, "stop");
                XMLEditUtils.addAttribute(stop10, "offset", "100%");
                XMLEditUtils.addAttribute(stop10, "style", "stop-color:rgb(63,58,54); stop-opacity:1;");
            }
        }
        if ((id == null) || (id.equals("outer")))
        {
            Node outer = svg.addRadialGradient("outer");
            if (outer != null)
            {
                XMLEditUtils.addAttribute(outer, "cx", "20%");
                XMLEditUtils.addAttribute(outer, "cy", "20%");
                XMLEditUtils.addAttribute(outer, "r", "30%");
                XMLEditUtils.addAttribute(outer, "fx", "50%");
                XMLEditUtils.addAttribute(outer, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(outer, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:white; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(outer, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:black; stop-opacity:1;");
            }
        }
        if ((id == null) || (id.equals("inner")))
        {
            Node inner = svg.addRadialGradient("inner");
            if (inner != null)
            {
                XMLEditUtils.addAttribute(inner, "cx", "20%");
                XMLEditUtils.addAttribute(inner, "cy", "20%");
                XMLEditUtils.addAttribute(inner, "r", "30%");
                XMLEditUtils.addAttribute(inner, "fx", "50%");
                XMLEditUtils.addAttribute(inner, "fy", "50%");
                Node stop1 = XMLEditUtils.addElement(inner, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "0%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:white; stop-opacity:1;");
                Node stop2 = XMLEditUtils.addElement(inner, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "100%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:brown; stop-opacity:1;");
            }
        }
        if ((id == null) || (id.equals("ring")))
        {
            Node ring = svg.addRadialGradient("ring");
            if (ring != null)
            {
                XMLEditUtils.addAttribute(ring, "cx", "50%");
                XMLEditUtils.addAttribute(ring, "cy", "50%");
                XMLEditUtils.addAttribute(ring, "r", "50%");
                XMLEditUtils.addAttribute(ring, "fx", "50%");
                XMLEditUtils.addAttribute(ring, "fy", "50%");
                Node stop0 = XMLEditUtils.addElement(ring, "stop");
                XMLEditUtils.addAttribute(stop0, "offset", "0%");
                XMLEditUtils.addAttribute(stop0, "style", "stop-color:yellow; stop-opacity:0;");
                Node stop1 = XMLEditUtils.addElement(ring, "stop");
                XMLEditUtils.addAttribute(stop1, "offset", "90%");
                XMLEditUtils.addAttribute(stop1, "style", "stop-color:yellow; stop-opacity:0;");
                Node stop2 = XMLEditUtils.addElement(ring, "stop");
                XMLEditUtils.addAttribute(stop2, "offset", "95%");
                XMLEditUtils.addAttribute(stop2, "style", "stop-color:yellow; stop-opacity:1;");
                Node stop3 = XMLEditUtils.addElement(ring, "stop");
                XMLEditUtils.addAttribute(stop3, "offset", "100%");
                XMLEditUtils.addAttribute(stop3, "style", "stop-color:yellow; stop-opacity:0;");
            }
        }
    }
    
    public static void ensureStyle(SVGHelper svg, SVGStyle style)
    {
        if (style.mFill == null)
            return;
        if (!style.mFill.toString().startsWith("url(#"))
            return;
        String id = style.mFill.toString().substring(5);
        if (id.endsWith(")"))
            id = id.substring(0, id.length() - 1);
        addGradients(svg, id);
    }
}
