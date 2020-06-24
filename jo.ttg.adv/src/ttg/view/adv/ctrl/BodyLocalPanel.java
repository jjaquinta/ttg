/*
 * Created on Feb 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.ctrl;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class BodyLocalPanel extends BodyBasePanel
{
	public BodyLocalPanel()
	{
		super();
	}

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getType()
     */
    protected int getType()
    {
        return T_NAME|T_NOTES|T_DOCKING|T_FUEL;
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getNotes()
     */
    protected String getNotes()
    {
        return getDemandProduction();
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getDocking()
     */
    protected String getDocking()
    {
	    return "You may dock here.";
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getFuel()
     */
    protected String getFuel()
    {
        return "Refined and Unrefined fuel available here.";
    }

    /* (non-Javadoc)
     * @see ttg.view.adv.ctrl.BodyBasePanel#getRepair()
     */
    protected String getRepair()
    {
	    return null;
    }
}
