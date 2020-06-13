/*
 * Created on Jan 5, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic.gen;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface ICargoLogicHandler
{
    public boolean isHandlerFor(Object obj);
    public double  salePrice(CargoBean cargo, Object atObj, DateBean now, Object originObj, Object destinationObj);
    public double  purchasePrice(CargoBean cargo, Object ori);
    public int[] getDestinationPreferences(CargoBean cargo, Object originObj);
}
