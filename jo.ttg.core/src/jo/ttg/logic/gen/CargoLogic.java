/*
 * Created on Jan 22, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.logic.gen;

import java.util.Collection;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.utils.DisplayUtils;
import jo.ttg.utils.URIUtils;
import jo.util.utils.obj.StringUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class CargoLogic
{
    private static List<ICargoLogicHandler>	mHandlers;
    /*
    static
    {
        mHandlers = new ArrayList<ICargoLogicHandler>();
        try
        {
            for (Object o : ExtensionPointUtils.getExecutableExtensions("jo.ttg.core.cargologichandler", "id"))
                mHandlers.add((ICargoLogicHandler)o);
        }
        catch (Throwable t)
        {
            mHandlers.add(new DefaultCargoLogicHandler());
        }
    }
    */
    
    public static void addHandler(ICargoLogicHandler h)
    {
        mHandlers.add(h);
    }
    
    private static ICargoLogicHandler findHandler(Object o)
    {
        for (ICargoLogicHandler h : mHandlers)
            if (h.isHandlerFor(o))
                return h;
        return null;
    }
	
	public static double  salePrice(CargoBean cargo, String atURI, DateBean now, IGenScheme scheme)
	{
	    if (atURI == null)
	        return 0;
	    if ((cargo.getOrigin() != null) && cargo.getOrigin().startsWith("mw:") && atURI.startsWith("body:"))
	    	atURI = "mw://"+URIUtils.extractOrds(atURI);	// convert
		Object at = SchemeLogic.getFromURI(scheme, atURI);
		Object origin = SchemeLogic.getFromURI(scheme, cargo.getOrigin());
		Object destination = SchemeLogic.getFromURI(scheme, cargo.getDestination());
	    ICargoLogicHandler h = findHandler(origin);
	    if (h == null)
	    {
	    	h = findHandler(destination);
	    	if (h == null)
	    		h = findHandler(at);
	    }
	    if (h != null)
	        return h.salePrice(cargo, at, now, origin, destination);
	    else
	        return 0.0;
	}

	public static double  purchasePrice(CargoBean cargo, IGenScheme scheme)
	{
		if (StringUtils.isTrivial(cargo.getOrigin()))
			return 0;
	    Object origin = SchemeLogic.getFromURI(scheme, cargo.getOrigin());
	    ICargoLogicHandler h = findHandler(origin);
	    if (h != null)
	        return h.purchasePrice(cargo, origin);
	    else
	        return 0.0;
	}

    public static int[] getDestinationPreferences(CargoBean cargo, IGenScheme scheme)
    {
        Object origin;
        if (StringUtils.isTrivial(cargo.getOrigin()))
            origin = null;
        else
            origin = SchemeLogic.getFromURI(scheme, cargo.getOrigin());
        ICargoLogicHandler h = findHandler(origin);
        if (h != null)
            return h.getDestinationPreferences(cargo, origin);
        else
            return new int[0];
    }
	
	public static int totalTons(Collection<? extends CargoBean> cargos)
	{
	    int ret = 0;
	    for (CargoBean cargo : cargos)
	        ret += cargo.getQuantity();
	    return ret;
	}
    
    public static double totalPurchasePrice(Collection<? extends CargoBean> cargos, IGenScheme scheme)
    {
        double ret = 0;
        for (CargoBean cargo : cargos)
            ret += purchasePrice(cargo, scheme);
        return ret;
    }
    
    public static double totalSalePrice(Collection<? extends CargoBean> cargos, String atURI, DateBean now, IGenScheme scheme)
    {
        double ret = 0;
        for (CargoBean cargo : cargos)
            ret += salePrice(cargo, atURI, now, scheme);
        return ret;
    }
	
	public static String getPhylumText(CargoBean cargo)
	{
        try
        {
            return CargoBean.CP_DESCRIPTION[cargo.getPhylum()];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return "???";
        }
	}
	
	public static String getTypeText(CargoBean cargo)
	{
        try
        {
            return CargoBean.GT_DESCRIPTION[cargo.getType()];
        }
        catch (ArrayIndexOutOfBoundsException e)
        {
            return "???";
        }
	}
	
	public static String getClassificationText(CargoBean cargo)
	{
		switch (cargo.getClassification())
		{
			case CargoBean.CC_CARGO:
				return "Cargo";
			case CargoBean.CC_FREIGHT:
				return "Freight";
		}
		return "???";
	}
	
	public static String getBestByText(CargoBean cargo)
	{
		DateBean bestBy = cargo.getBestBy();
		if ((bestBy != null) && (bestBy.getDays() > 0))
			return DisplayUtils.formatDate(bestBy);
		return "";
	}

    public static String getWarningText(CargoBean cargo)
    {
        return cargo.getWarnings();
    }
}
