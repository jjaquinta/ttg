/*
 * Created on Dec 24, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.core.ui.swing.ctrl.BodyViewHandler;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import ttg.adv.beans.BodySpecialAdvBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvViewHandler implements BodyViewHandler
{
	public static ImageIcon UPPORTA_IMG = TTGIconUtils.getUPP("upport_a.gif");
	public static ImageIcon UPPORTC_IMG = TTGIconUtils.getUPP("upport_c.gif");
	public static ImageIcon UPPORTE_IMG = TTGIconUtils.getUPP("upport_e.gif");
	public static ImageIcon DNPORTA_IMG = TTGIconUtils.getUPP("dnport_a.gif");
	public static ImageIcon DNPORTC_IMG = TTGIconUtils.getUPP("dnport_c.gif");
	public static ImageIcon DNPORTE_IMG = TTGIconUtils.getUPP("dnport_e.gif");

	public static ImageIcon BASEN_IMG = TTGIconUtils.getUPP("base_navy.gif");
	public static ImageIcon BASES_IMG = TTGIconUtils.getUPP("base_scout.gif");
	public static ImageIcon BASEL_IMG = TTGIconUtils.getUPP("base_lab.gif");
	public static ImageIcon BASER_IMG = TTGIconUtils.getUPP("base_ref.gif");
	public static ImageIcon BASEM_IMG = TTGIconUtils.getUPP("base_mil.gif");

    public static URL UPPORTA_URI = TTGIconUtils.getUPPURI("upport_a.gif");
    public static URL UPPORTC_URI = TTGIconUtils.getUPPURI("upport_c.gif");
    public static URL UPPORTE_URI = TTGIconUtils.getUPPURI("upport_e.gif");
    public static URL DNPORTA_URI = TTGIconUtils.getUPPURI("dnport_a.gif");
    public static URL DNPORTC_URI = TTGIconUtils.getUPPURI("dnport_c.gif");
    public static URL DNPORTE_URI = TTGIconUtils.getUPPURI("dnport_e.gif");

    public static URL BASEN_URI = TTGIconUtils.getUPPURI("base_navy.gif");
    public static URL BASES_URI = TTGIconUtils.getUPPURI("base_scout.gif");
    public static URL BASEL_URI = TTGIconUtils.getUPPURI("base_lab.gif");
    public static URL BASER_URI = TTGIconUtils.getUPPURI("base_ref.gif");
    public static URL BASEM_URI = TTGIconUtils.getUPPURI("base_mil.gif");

    /* (non-Javadoc)
     * @see ttg.view.ctrl.BodyViewHandler#getIcon(ttg.beans.sys.BodyBean)
     */
    public ImageIcon getIcon(BodyBean b)
    {
        if (b instanceof BodySpecialAdvBean)
        {
            BodySpecialAdvBean special = (BodySpecialAdvBean)b;
            if ((special.getSubType() == BodySpecialBean.ST_STARPORT) || (special.getSubType() == BodySpecialBean.ST_SPACEPORT))
                return getStationIcon(special);
            else if (special.getSubType() == BodySpecialBean.ST_NAVYBASE)
                return BASEN_IMG;
            else if (special.getSubType() == BodySpecialBean.ST_SCOUTBASE)
                return BASES_IMG;
            else if (special.getSubType() == BodySpecialBean.ST_LABBASE)
                return BASEL_IMG;
            else if (special.getSubType() == BodySpecialBean.ST_REFINERY)
                return BASER_IMG;
            else if (special.getSubType() == BodySpecialBean.ST_LOCALBASE)
                return BASEM_IMG;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see ttg.view.ctrl.BodyViewHandler#getIcon(ttg.beans.sys.BodyBean)
     */
    public URL getIconURI(BodyBean b)
    {
        if (b instanceof BodySpecialAdvBean)
        {
            BodySpecialAdvBean special = (BodySpecialAdvBean)b;
            if ((special.getSubType() == BodySpecialBean.ST_STARPORT) || (special.getSubType() == BodySpecialBean.ST_SPACEPORT))
                return getStationIconURI(special);
            else if (special.getSubType() == BodySpecialBean.ST_NAVYBASE)
                return BASEN_URI;
            else if (special.getSubType() == BodySpecialBean.ST_SCOUTBASE)
                return BASES_URI;
            else if (special.getSubType() == BodySpecialBean.ST_LABBASE)
                return BASEL_URI;
            else if (special.getSubType() == BodySpecialBean.ST_REFINERY)
                return BASER_URI;
            else if (special.getSubType() == BodySpecialBean.ST_LOCALBASE)
                return BASEM_URI;
        }
        return null;
    }
    
    private ImageIcon getStationIcon(BodySpecialAdvBean special)
    {
        UPPPorBean port = (UPPPorBean)special.getSpecialInfo();
        switch (port.getValue())
        {
            case 'A':
            case 'B':
            case 'F':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTA_IMG;
                else
                    return DNPORTA_IMG;
            case 'C':
            case 'D':
            case 'G':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTC_IMG;
                else
                    return DNPORTC_IMG;
            case 'E':
            case 'H':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTE_IMG;
                else
                    return DNPORTE_IMG;
        }
        return null;
    }
    
    private URL getStationIconURI(BodySpecialAdvBean special)
    {
        UPPPorBean port = (UPPPorBean)special.getSpecialInfo();
        switch (port.getValue())
        {
            case 'A':
            case 'B':
            case 'F':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTA_URI;
                else
                    return DNPORTA_URI;
            case 'C':
            case 'D':
            case 'G':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTC_URI;
                else
                    return DNPORTC_URI;
            case 'E':
            case 'H':
                if (special.getOrbitalRadius() > 0)
                    return UPPORTE_URI;
                else
                    return DNPORTE_URI;
        }
        return null;
    }

    /* (non-Javadoc)
     * @see ttg.view.ctrl.BodyViewHandler#getView(ttg.beans.sys.BodyBean)
     */
    public Object[] getView(BodyBean b)
    {
        if (b instanceof BodySpecialAdvBean)
        {
            BodySpecialAdvBean special = (BodySpecialAdvBean)b;
            List<Object> list = new ArrayList<>();
            list.add(getIcon(b));
            switch (special.getSubType())
            {
                case BodySpecialBean.ST_STARPORT:
                    getStationView(special, list);
                	break;
                default:
                    list.add(special.getOneLineDesc());
                	break;
            }
            return list.toArray();
        }
        return null;
    }

    private void getStationView(BodySpecialAdvBean special, List<Object> list)
    {
        list.add(special.getOneLineDesc());
    }
}
