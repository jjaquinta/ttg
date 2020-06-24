/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.ctrl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.beans.adv.Game;
import ttg.view.adv.dlg.ForSaleCargoDlg;
import ttg.view.adv.dlg.ForSaleCrewDlg;
import ttg.view.adv.dlg.ForSaleFreightDlg;
import ttg.view.adv.dlg.ForSalePassengersDlg;
import ttg.view.adv.dlg.ShipyardDlg;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AtPanel extends TXMLPanel implements PropertyChangeListener
{
    private Game	mGame;
    private HashMap	mSystemVariables;
    
	/**
	 *
	 */

	public AtPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    setShowStatusBar(false);
	    mSystemVariables = new HashMap();
	    setValueMap(mSystemVariables);
	    setSystemResourceRoot("txml");
	}

	private void initLink()
	{
	}

	private void initLayout()
	{
	}
	
	public void go(String page)
	{
	    if (page.startsWith("dlg:"))
	        goDialog(page.substring(4));
	    else
	    {
	        super.go(page);
	        return;
	    }
	}
	
	private void goDialog(String dialog)
	{
	    JDialog dlg = null;
	    if ("cargo".equals(dialog))
	        dlg = new ForSaleCargoDlg((JFrame)SwingUtilities.getRoot(this), getGame());
	    else if ("freight".equals(dialog))
	        dlg = new ForSaleFreightDlg((JFrame)SwingUtilities.getRoot(this), getGame());
	    else if ("passengers".equals(dialog))
	        dlg = new ForSalePassengersDlg((JFrame)SwingUtilities.getRoot(this), getGame());
	    else if ("crew".equals(dialog))
	        dlg = new ForSaleCrewDlg((JFrame)SwingUtilities.getRoot(this), getGame());
	    else if ("shipyard".equals(dialog))
	        dlg = new ShipyardDlg((JFrame)SwingUtilities.getRoot(this), getGame());
	    if (dlg != null)
	    {
	        dlg.setModal(true);
	        dlg.show();
	    }
	}
	
	private void updateLocation()
	{
	    String newHome = null;
	    LocationURI uri = new LocationURI(mGame.getShip().getLocation());
	    String destSys = uri.getParam("destSys");
	    if (destSys != null)
	    {
	        Object dSys = SchemeLogic.getFromURI(mGame.getScheme(), "sys://"+destSys);
	        mSystemVariables.put("jumpTarget", dSys);
	    }
	    int orbit = FormatUtils.atoi(uri.getParam("orbit"));
	    Object loc = SchemeLogic.getFromURI(mGame.getScheme(), mGame.getShip().getLocation());	    
	    mSystemVariables.put("location", loc);
	    mSystemVariables.put("locationURI", uri);
	    mSystemVariables.put("locationOrbit", new Integer(orbit));
	    if (mGame.getShip().isDocked())
	        newHome = "home_port.html";
	    else if (loc instanceof BodyBean)
	        newHome = "home_space.html";
	    else
	        newHome = "home_jump.html";
	    //if (!newHome.equals(getHomePage()))
	    {
	        setHomePage(newHome);
	        flushHistory();
	        goHome();
	    }
	}

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String name = evt.getPropertyName();
		//System.out.println("AtPanel: propertyChange("+name+")");
		if (name.equals("location") || name.equals("docked") || name.equals("*"))
		{
		    updateLocation();
		}
    }
    public Game getGame()
    {
        return mGame;
    }
    public void setGame(Game game)
    {
        mGame = game;
        mSystemVariables.put("game", game);
        mSystemVariables.put("ship", game.getShip());
        updateLocation();
    }
}
