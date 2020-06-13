/*
 * Created on Oct 20, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.icons;

import java.awt.Image;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.mw.UPPLogic;

public class DefaultBodyViewHandler implements IBodyViewHandler
{
    public Image getImage(BodyBean b)
    {
        if (b instanceof BodyWorldBean)
        {
            BodyWorldBean w = (BodyWorldBean)b;
            if (w.isOuterZone())
                return TTGIconUtils.getImage(TTGIcons.ICON_OUTER);
            else if (w.isInnerZone())
                return TTGIconUtils.getImage(TTGIcons.ICON_INNER);
            else // if (w.isHabitableZone())
            {
                if (w.getPopulatedStats().getUPP().getAtmos().getValue() <= 1)
                    return TTGIconUtils.getImage(TTGIcons.ICON_AIRLESS);
                else if (w.getPopulatedStats().getUPP().getPop().getValue() > 1)
                    return TTGIconUtils.getImage(TTGIcons.ICON_POPULATED);
                else
                    return TTGIconUtils.getImage(TTGIcons.ICON_HABITABLE);
            }
        }
        else if (b instanceof BodyToidsBean)
        {
            return TTGIconUtils.getImage(TTGIcons.ICON_TOIDS);
        }
        else if (b instanceof BodyGiantBean)
        {
            BodyGiantBean g = (BodyGiantBean)b;
            if (g.getSize() == BodyGiantBean.GS_S)
                return TTGIconUtils.getImage(TTGIcons.ICON_SGG);
            else
                return TTGIconUtils.getImage(TTGIcons.ICON_LGG);
        }
        else if (b instanceof BodyStarBean)
        {
        	BodyStarBean star = (BodyStarBean)b;
        	int type = star.getStarDecl().getStarType();
        	type -= type%10;
        	switch (type)
        	{
        		case StarDeclBean.ST_O: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARO);
        		case StarDeclBean.ST_B: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARB);
        		case StarDeclBean.ST_A: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARA);
        		case StarDeclBean.ST_F: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARF);
        		case StarDeclBean.ST_G: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARG);
        		case StarDeclBean.ST_K: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARK);
        		case StarDeclBean.ST_M: 
                    return TTGIconUtils.getImage(TTGIcons.ICON_STARM);
        	}
            return TTGIconUtils.getImage(TTGIcons.ICON_STARG);
        }
        else if (b instanceof BodySpecialBean)
        {
            BodySpecialBean special = (BodySpecialBean)b;
            if ((special.getSubType() == BodySpecialBean.ST_STARPORT) || (special.getSubType() == BodySpecialBean.ST_SPACEPORT))
                return getStationIcon(special);
            else if (special.getSubType() == BodySpecialBean.ST_NAVYBASE)
                return TTGIconUtils.getImage(TTGIcons.ICON_BASEN);
            else if (special.getSubType() == BodySpecialBean.ST_SCOUTBASE)
                return TTGIconUtils.getImage(TTGIcons.ICON_BASES);
            else if (special.getSubType() == BodySpecialBean.ST_LABBASE)
                return TTGIconUtils.getImage(TTGIcons.ICON_BASEL);
            else if (special.getSubType() == BodySpecialBean.ST_REFINERY)
                return TTGIconUtils.getImage(TTGIcons.ICON_BASER);
            else if (special.getSubType() == BodySpecialBean.ST_LOCALBASE)
                return TTGIconUtils.getImage(TTGIcons.ICON_BASEM);
        }
        return null;
    }
    
    private static Image getStationIcon(BodySpecialBean special)
    {
        UPPPorBean port = (UPPPorBean)special.getSpecialInfo();
        switch (port.getValue())
        {
            case 'A':
            case 'B':
            case 'F':
                if (special.getOrbitalRadius() > 0)
                    return TTGIconUtils.getImage(TTGIcons.ICON_UPPORTA);
                else
                    return TTGIconUtils.getImage(TTGIcons.ICON_DNPORTA);
            case 'C':
            case 'D':
            case 'G':
                if (special.getOrbitalRadius() > 0)
                    return TTGIconUtils.getImage(TTGIcons.ICON_UPPORTC);
                else
                    return TTGIconUtils.getImage(TTGIcons.ICON_DNPORTC);
            case 'E':
            case 'H':
                if (special.getOrbitalRadius() > 0)
                    return TTGIconUtils.getImage(TTGIcons.ICON_UPPORTE);
                else
                    return TTGIconUtils.getImage(TTGIcons.ICON_DNPORTE);
        }
        return null;
    }

    public String getText(BodyBean b)
    {
        return b.getName();
    }

    public Image getColumnImage(BodyBean b, int columnIndex)
    {
        if (columnIndex == 0)
            return getImage(b);
        return null;
    }

    public String getColumnText(BodyBean body, int columnIndex)
    {
        switch (columnIndex)
        {
            case 0:
                return body.getName();
            case 1:
                if (body instanceof BodyPopulated)
                {
                    PopulatedStatsBean stats = ((BodyPopulated)body).getPopulatedStats();
                    return UPPLogic.getUPPDesc(stats.getUPP());
                }
                break;
            case 2:
                if (body instanceof BodyPopulated)
                {
                    PopulatedStatsBean stats = ((BodyPopulated)body).getPopulatedStats();
                    return UPPLogic.getTradeCodesDesc(stats.getUPP());
                }
                break;
        }
        return null;
    }
}

