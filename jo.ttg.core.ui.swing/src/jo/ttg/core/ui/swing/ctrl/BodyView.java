/*
 * Created on Dec 20, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyPopulated;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.mw.UPPLogic;
import jo.util.ui.swing.ctrl.LinearMixedCtrl;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodyView extends LinearMixedCtrl
{
    private static List<BodyViewHandler> mHandlers = new ArrayList<BodyViewHandler>();
    static
    {
        mHandlers.add(new DefaultBodyViewHandler());
    }
    
	public static ImageIcon TOIDS_IMG = TTGIconUtils.getPlanet("Asteroids.gif");
    public static ImageIcon STAR_A_IMG = TTGIconUtils.getPlanet("body_star_a.gif");
    public static ImageIcon STAR_F_IMG = TTGIconUtils.getPlanet("body_star_f.gif");
    public static ImageIcon STAR_G_IMG = TTGIconUtils.getPlanet("body_star_g.gif");
    public static ImageIcon STAR_K_IMG = TTGIconUtils.getPlanet("body_star_k.gif");
    public static ImageIcon STAR_M_IMG = TTGIconUtils.getPlanet("body_star_m.gif");
	public static ImageIcon STAR_IMG = TTGIconUtils.getPlanet("TheSun.gif");
	public static ImageIcon LGG_IMG = TTGIconUtils.getPlanet("Jupiter.gif");
	public static ImageIcon SGG_IMG = TTGIconUtils.getPlanet("Neptune.gif");
	public static ImageIcon INNER_IMG = TTGIconUtils.getPlanet("Mars.gif");
	public static ImageIcon OUTER_IMG = TTGIconUtils.getPlanet("Callisto.gif");
	public static ImageIcon HABITABLE_IMG = TTGIconUtils.getPlanet("Venus.gif");
	public static ImageIcon POPULATED_IMG = TTGIconUtils.getPlanet("Earth2.gif");
	
	public static ImageIcon PORTA_IMG = TTGIconUtils.getUPP("port_a.gif");
	public static ImageIcon PORTB_IMG = TTGIconUtils.getUPP("port_b.gif");
	public static ImageIcon PORTG_IMG = TTGIconUtils.getUPP("port_g.gif");
	public static ImageIcon POPM_IMG = TTGIconUtils.getUPP("pop_med.gif");
	public static ImageIcon POPH_IMG = TTGIconUtils.getUPP("pop_hi.gif");

	private BodyBean	mBody;
	
	public BodyView()
	{
	}
    
    public BodyBean getBody()
    {
        return mBody;
    }
    public void setBody(BodyBean body)
    {
        mBody = body;
        if (body == null)
        {
            setItems(new Object[0]);
            return;
        }
        Object[] list = null;
	    for (Iterator<BodyViewHandler> i = mHandlers.iterator(); i.hasNext(); )
	    {
	        BodyViewHandler h = (BodyViewHandler)i.next();
	        list = h.getView(body);
	        if (list != null)
	            break;
	    }
		setItems(list);
        repaint();
    }

	public static ImageIcon getIcon(BodyBean b)
	{
	    for (Iterator<BodyViewHandler> i = mHandlers.iterator(); i.hasNext(); )
	    {
	        BodyViewHandler h = (BodyViewHandler)i.next();
	        ImageIcon ret = h.getIcon(b);
	        if (ret != null)
	            return ret;
	    }
	    return null;
	}
	
	public static void addHandler(BodyViewHandler h)
	{
	    mHandlers.add(0, h);
	}
}

class DefaultBodyViewHandler implements BodyViewHandler
{
    public ImageIcon getIcon(BodyBean b)
    {
		if (b instanceof BodyWorldBean)
		{
			BodyWorldBean w = (BodyWorldBean)b;
			if (w.isOuterZone())
				return BodyView.OUTER_IMG;
			else if (w.isInnerZone())
				return BodyView.INNER_IMG;
			else // if (w.isHabitableZone())
			{
				if (w.getPopulatedStats().getUPP().getPop().getValue() > 0)
					return BodyView.POPULATED_IMG;
				else
					return BodyView.HABITABLE_IMG;
			}
		}
		else if (b instanceof BodyToidsBean)
		{
			return BodyView.TOIDS_IMG;
		}
		else if (b instanceof BodyGiantBean)
		{
			BodyGiantBean g = (BodyGiantBean)b;
			if (g.getSize() == BodyGiantBean.GS_S)
				return BodyView.SGG_IMG;
			else
				return BodyView.LGG_IMG;
		}
		// else if (b instanceof BodyStarBean)
		{
		    int st = ((BodyStarBean)b).getStarDecl().getStarType();
		    st -= (st%10);
		    switch (st)
		    {
                case StarDeclBean.ST_A:
                    return BodyView.STAR_A_IMG;
                case StarDeclBean.ST_B:
                    return BodyView.STAR_A_IMG;
                case StarDeclBean.ST_F:
                    return BodyView.STAR_F_IMG;
                case StarDeclBean.ST_G:
                    return BodyView.STAR_G_IMG;
                case StarDeclBean.ST_K:
                    return BodyView.STAR_K_IMG;
                case StarDeclBean.ST_M:
                    return BodyView.STAR_M_IMG;
		        case StarDeclBean.ST_O:
		            return BodyView.STAR_A_IMG;
		    }
            return BodyView.STAR_IMG;
		}
    }
    
    public Object[] getView(BodyBean body)
    {
        List<Object> list = new ArrayList<Object>();
        list.add(BodyView.getIcon(body));
        list.add(body.getName());
		if (body instanceof BodyPopulated)
		{
		    PopulatedStatsBean stats = ((BodyPopulated)body).getPopulatedStats();
		    list.add(new Dimension(120, 0));
		    list.add(UPPLogic.getUPPDesc(stats.getUPP()));
		    list.add(new Dimension(200, 0));
		    list.add(UPPLogic.getTradeCodesDesc(stats.getUPP()));
		}
    	return list.toArray();
    }
}

