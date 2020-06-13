/*
 * Created on Jan 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.gen;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.logic.mw.UPPLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DefaultCargoLogicHandler implements ICargoLogicHandler
{

    /* (non-Javadoc)
     * @see jo.ttg.logic.gen.CargoLogicHandler#isHandlerFor(java.lang.Object)
     */
    public boolean isHandlerFor(Object obj)
    {
        return obj instanceof MainWorldBean;
    }
	
	public double  salePrice(CargoBean cargo, Object atObj, DateBean now, Object originObj, Object destinationObj)
	{
		double ret;
		MainWorldBean at;
	    MainWorldBean origin;
	    MainWorldBean destination;
	    try
	    {
			at = (MainWorldBean)atObj;
		    origin = (MainWorldBean)originObj;
		    destination = (MainWorldBean)destinationObj;
	    }
	    catch (ClassCastException e)
	    {
	    	return 0;
	    }
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
			if (!at.getOrds().equals(destination.getOrds()))
			    return 0;
//		if (isPirateBase)
//		{
//			if (Quantity > 0)
//				ret = Quantity*BaseValue;
//			else
//				ret = BaseValue;
//			return ret/4;
//		}
		ret = baseSalePrice(cargo, at, now, origin, destination);
		if (ret == 0)
			return 0;
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
			return ret;
		int thistech = at.getPopulatedStats().getUPP().getTech().getValue();
		if (thistech < cargo.getTechLevel())
		{   // we can't make this here, jack up the price
			ret *= 1.0 + (cargo.getTechLevel() - thistech)*.05;
		}
		if (cargo.getOrigin() != null)
		{
			int thattech = origin.getPopulatedStats().getUPP().getTech().getValue();
			if (thistech > thattech + 4)
			{   // import low tech to high tech, quaint
				ret *= 1.0 + (thistech - thattech)*.01;
			}
		}
		ret *= getLocationModifier(cargo, at.getPopulatedStats().getUPP());
//		if (baseGood.inSurplus)
//			ret /= 5.0;
//		if (baseGood.inDemand)
//			ret *= 5.0;
		return ret;
	}

	public double  purchasePrice(CargoBean cargo, Object ori)
	{
	    MainWorldBean origin = (MainWorldBean)ori;
		double ret = basePurchasePrice(cargo, origin);
		if ((cargo.getClassification() != CargoBean.CC_FREIGHT) && (origin != null))
		{
			ret *= getLocationModifier(cargo, origin.getPopulatedStats().getUPP());
//			if (inSurplus)
//				ret /= 5.0;
//			if (inDemand)
//				ret *= 5.0;
		}
		return ret;
	}
    
	private double baseSalePrice(CargoBean cargo, MainWorldBean at, DateBean now, MainWorldBean origin, MainWorldBean destination)
	{
		boolean sameSystem = at.getOrds().equals(origin.getOrds());
        PopulatedStatsBean destStats = null;
        if (destination != null)
            destStats = destination.getPopulatedStats();
	    return baseSalePrice(cargo, sameSystem, at.getPopulatedStats(), now, origin.getPopulatedStats(), destStats);
	}
	    
	protected double baseSalePrice(CargoBean cargo, boolean sameSystem, PopulatedStatsBean at, DateBean now, PopulatedStatsBean origin, PopulatedStatsBean destination)
	{
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
		{
			if (at.getTravelZone() == 'R')
				return 5.0*2000.0*(double)cargo.getQuantity();
			else if (at.getTravelZone() == 'A')
				return 2.0*2000.0*(double)cargo.getQuantity();
			else
				return 2000.0*(double)cargo.getQuantity();
		}
		else
		{
			if (sameSystem)
				return 0;
			if (cargo.isPer())
				if (now.getMinutes() > cargo.getBestBy().getMinutes())
					return 0.0; // gone off
			double value = cargo.getBaseValue();
			//System.out.print("Sale: "+cargo.getName()+": "+value);
			if (cargo.getQuantity() > 0) // not massless info packet
			    value *= cargo.getQuantity();
			//System.out.print("-quan->"+value);
			value = UPPLogic.convLocalToImperial(at.getUPP(), value);
			//System.out.print("-xrate->"+value);
			if (origin != null)
			{
				value *= UPPLogic.getTradePriceMod(origin.getUPP(), at.getUPP());
				//System.out.print("-origin->"+value);
			}
			System.out.println();
			return value;
		}
	}

	private double  basePurchasePrice(CargoBean cargo, MainWorldBean origin)
	{
	    return basePurchasePrice(cargo, origin.getPopulatedStats());
	}

	protected double  basePurchasePrice(CargoBean cargo, PopulatedStatsBean origin)
	{
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
			return 1000.0*cargo.getQuantity();
		double value = cargo.getBaseValue();
		//System.out.print("Buy: "+cargo.getName()+": "+value);
		if (cargo.getQuantity() > 0)
		    value *= cargo.getQuantity();
		//System.out.print("-quan->"+value);
		if (cargo.getOrigin() != null)
		{
			value = UPPLogic.convLocalToImperial(origin.getUPP(), value);
			//System.out.print("-xrate->"+value);
			value *= UPPLogic.getTradeCostMod(origin.getUPP());
			//System.out.print("-trade->"+value);
		}
		//System.out.println();
		return value;
	}

	public double getLocationModifier(CargoBean cargo, UPPBean uwp)
	{
		double ret = 1.0;
		if (cargo.getPhylum() == CargoBean.CP_ORGANIC)
		{
			if (uwp.isAg())
				ret /= 1.1;
			else if (uwp.isNa())
				ret *= 1.1;
		}
		else if (cargo.getPhylum() == CargoBean.CP_INORGANIC)
		{
			if (uwp.isAg())
				ret *= 1.1;
			else if (uwp.isNa())
				ret /= 1.1;
		}
		if (cargo.getType() == CargoBean.GT_NATURAL)
		{
			if (uwp.isNi())
				ret /= 1.1;
			if (uwp.isIn())
				ret *= 1.1;
			if (uwp.isLo())
				ret /= 1.1;
			if (uwp.isPo())
				ret *= 1.1;
		}
		else if (cargo.getType() == CargoBean.GT_PROCESSED)
		{
			if (uwp.isIn())
				ret *= 1.1;
			if (uwp.isLo())
				ret /= 1.1;
		}
		else if (cargo.getType() == CargoBean.GT_MANUFACTURED)
		{
			if (uwp.isRi())
				ret *= 1.1;
			if (uwp.isAg())
				ret *= 1.1;
			if (uwp.isLo())
				ret /= 1.1;
			if (uwp.isIn())
				ret /= 1.1;
			if (uwp.isNi())
				ret *= 1.1;
		}
		else if (cargo.getType() == CargoBean.GT_INFORMATION)
		{
			if (uwp.isPo())
				ret *= 1.1;
			if (uwp.isHi())
				ret /= 1.1;
		}
		else if (cargo.getType() == CargoBean.GT_NOVELTIES)
		{
			if (uwp.isRi())
				ret *= 1.1;
			if (uwp.isHi())
				ret *= 1.1;
		}
		return ret;
	}

    public int[] getDestinationPreferences(CargoBean cargo, Object originObj)
    {
        int[] ret = UPPLogic.getDestinationPreferences(((MainWorldBean)originObj).getPopulatedStats().getUPP());
        if (cargo.getPhylum() == CargoBean.CP_ORGANIC)
        {
            ret[UPPBean.TC_AG]--;
            ret[UPPBean.TC_NA]++;
        }
        else if (cargo.getPhylum() == CargoBean.CP_INORGANIC)
        {
            ret[UPPBean.TC_AG]++;
            ret[UPPBean.TC_NA]--;
        }
        if (cargo.getType() == CargoBean.GT_NATURAL)
        {
            ret[UPPBean.TC_NI]--;
            ret[UPPBean.TC_IN]++;
            ret[UPPBean.TC_LO]--;
            ret[UPPBean.TC_PO]++;
        }
        else if (cargo.getType() == CargoBean.GT_PROCESSED)
        {
            ret[UPPBean.TC_IN]++;
            ret[UPPBean.TC_LO]--;
        }
        else if (cargo.getType() == CargoBean.GT_MANUFACTURED)
        {
            ret[UPPBean.TC_RI]++;
            ret[UPPBean.TC_AG]++;
            ret[UPPBean.TC_LO]--;
            ret[UPPBean.TC_IN]--;
            ret[UPPBean.TC_NI]++;
        }
        else if (cargo.getType() == CargoBean.GT_INFORMATION)
        {
            ret[UPPBean.TC_PO]++;
            ret[UPPBean.TC_HI]--;
        }
        else if (cargo.getType() == CargoBean.GT_NOVELTIES)
        {
            ret[UPPBean.TC_RI]++;
            ret[UPPBean.TC_HI]++;
        }
        return ret;
    }
}
