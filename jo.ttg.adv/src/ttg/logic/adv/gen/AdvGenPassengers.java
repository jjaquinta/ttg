/*
 * Created on Jan 9, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv.gen;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.UPPPorBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.PassengersBean;
import jo.ttg.gen.IGenPassengersEx;
import jo.ttg.gen.imp.ImpGenPassengers;
import jo.ttg.gen.imp.ImpGenScheme;
import jo.ttg.logic.RandLogic;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.logic.adv.LocationLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvGenPassengers extends ImpGenPassengers implements IGenPassengersEx
{
    public AdvGenPassengers(ImpGenScheme _scheme)
    {
        super(_scheme);
    }

    /* (non-Javadoc)
     * @see ttg.gen.GenPassengersEx#generatePassengers(ttg.beans.sys.BodyBean, ttg.beans.sys.BodyBean, ttg.beans.DateBean)
     */
    public List<PassengersBean> generatePassengers(BodyBean bOrigin, BodyBean bDestination, DateBean date)
    {
        if (!(bOrigin instanceof BodySpecialAdvBean) || !(bDestination instanceof BodySpecialAdvBean))
            return null;
        BodySpecialAdvBean origin = (BodySpecialAdvBean)bOrigin;
        BodySpecialAdvBean destination = (BodySpecialAdvBean)bDestination;
        boolean intrasystem = origin.getSystem().getOrds().equals(destination.getSystem().getOrds());
        if (!intrasystem)
        {
            if (origin.getSubType() != destination.getSubType())
                return null;
            if ((origin.getSubType() != BodySpecialBean.ST_NAVYBASE) && (origin.getSubType() != BodySpecialBean.ST_SCOUTBASE) && (origin.getSubType() != BodySpecialBean.ST_STARPORT))
                return null;
        }
        RandBean r = new RandBean();
        RandLogic.setMagic(r, 
                (origin.getSeed() + destination.getSeed()) * date.getDays(),
                (RandBean.PAS_MAGIC ^ (date.getDays() + (long) CargoBean.CC_FREIGHT)));
        int oPop = getModifiedPopulation(origin);
        int dPop = getModifiedPopulation(destination);
        int oTech = LocationLogic.findNearestPopulatedStats(origin).getUPP().getTech().getValue();
        int dTech = LocationLogic.findNearestPopulatedStats(destination).getUPP().getTech().getValue();
        List<PassengersBean> ret = generatePassengersWeek(origin.getSeed() + destination.getSeed(), 
                oPop, oTech, origin.getURI(), 
                dPop, dTech, destination.getURI(), date);
        for (PassengersBean pass : ret)
        {
            pass.setSeed(r.getSeed());
            if (intrasystem)
            {
                pass.setMiddle(pass.getMiddle() + pass.getLow());
            	pass.setLow(0);
        	}
            RandLogic.rand(r);
        }
        return ret;
    }
    /**
     * @param body
     * @return
     */
    public static int getModifiedPopulation(BodySpecialAdvBean body)
    {
        int pop = LocationLogic.findNearestPopulatedStats(body).getUPP().getPop().getValue();
        switch (body.getSubType())
        {
            case BodySpecialBean.ST_LABBASE:
                pop = 2;
                break;
            case BodySpecialBean.ST_LOCALBASE:
                pop /= 2;
                break;
            case BodySpecialBean.ST_NAVYBASE:
                pop = 3;
                break;
            case BodySpecialBean.ST_REFINERY:
                pop = 3;
                break;
            case BodySpecialBean.ST_SCOUTBASE:
                pop = 2;
                break;
            case BodySpecialBean.ST_STARPORT:
            case BodySpecialBean.ST_SPACEPORT:
                switch (((UPPPorBean)body.getSpecialInfo()).getValue())
                {
                    case 'A':
                        break;
                    case 'B':
                    case 'F':
                        pop -= 1;
                        break;
                    case 'C':
                    case 'G':
                        pop -= 2;
                        break;
                    case 'D':
                    case 'H':
                        pop -= 3;
                        break;
                    case 'E':
                        pop -= 4;
                        break;
                    default:
                        pop = 0;
                        break;
                }
                break;
        }
        return pop;
    }

}
