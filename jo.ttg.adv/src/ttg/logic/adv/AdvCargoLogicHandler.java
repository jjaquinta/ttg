/*
 * Created on Jan 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.adv;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.gen.DefaultCargoLogicHandler;
import jo.ttg.logic.gen.ICargoLogicHandler;
import ttg.beans.adv.AdvCargoBean;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.logic.adv.gen.AdvGenCargo;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvCargoLogicHandler extends DefaultCargoLogicHandler implements
        ICargoLogicHandler
{
    
    public AdvCargoLogicHandler(IGenScheme scheme)
    {
    }
    
    /* (non-Javadoc)
     * @see ttg.logic.gen.CargoLogicHandler#isHandlerFor(java.lang.Object)
     */
    public boolean isHandlerFor(Object obj)
    {
        return obj instanceof BodySpecialAdvBean;
    }
	
	public double  salePrice(CargoBean cargo, Object atObj, DateBean now, Object originObj, Object destinationObj)
	{
		if (cargo.getClassification() == CargoBean.CC_FREIGHT)
		    if (!((BodySpecialAdvBean)atObj).getURI().equals(((BodySpecialAdvBean)destinationObj).getURI()))
		        return 0;
		    else
		        return ((AdvCargoBean)cargo).getPurchasePrice()*2.0;
		return AdvGenCargo.getValueAt((AdvCargoBean)cargo, (BodySpecialAdvBean)atObj);
	}

	public double  purchasePrice(CargoBean c, Object ori)
	{
	    AdvCargoBean cargo = (AdvCargoBean)c;
		return cargo.getPurchasePrice();
	}
}
