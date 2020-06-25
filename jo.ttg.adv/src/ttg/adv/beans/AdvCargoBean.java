/*
 * Created on Jan 19, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.beans;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.trade.CargoBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AdvCargoBean extends CargoBean
{
    private DateBean	mDelivered;
    private double		mPurchasePrice;
    
    public AdvCargoBean()
    {
        super();
        mDelivered = new DateBean();
    }
    
    public DateBean getDelivered()
    {
        return mDelivered;
    }
    public void setDelivered(DateBean delivered)
    {
        mDelivered = delivered;
    }
    /**
     * @return Returns the purchasePrice.
     */
    public double getPurchasePrice()
    {
        return mPurchasePrice;
    }
    /**
     * @param purchasePrice The purchasePrice to set.
     */
    public void setPurchasePrice(double purchasePrice)
    {
        mPurchasePrice = purchasePrice;
    }
}
