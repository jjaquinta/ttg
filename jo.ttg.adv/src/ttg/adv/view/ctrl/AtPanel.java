/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.ctrl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jo.ttg.beans.LocationURI;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.SchemeLogic;
import ttg.adv.beans.Game;
import ttg.adv.view.dlg.ForSaleCargoDlg;
import ttg.adv.view.dlg.ForSaleCrewDlg;
import ttg.adv.view.dlg.ForSaleFreightDlg;
import ttg.adv.view.dlg.ForSalePassengersDlg;
import ttg.adv.view.dlg.ShipyardDlg;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AtPanel extends TXMLPanel implements PropertyChangeListener
{
    private Game                mGame;
    private Map<String, Object> mSystemVariables;

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
        mSystemVariables = new HashMap<>();
        updateInit();
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
            dlg = new ForSaleCargoDlg((JFrame)SwingUtilities.getRoot(this),
                    getGame());
        else if ("freight".equals(dialog))
            dlg = new ForSaleFreightDlg((JFrame)SwingUtilities.getRoot(this),
                    getGame());
        else if ("passengers".equals(dialog))
            dlg = new ForSalePassengersDlg((JFrame)SwingUtilities.getRoot(this),
                    getGame());
        else if ("crew".equals(dialog))
            dlg = new ForSaleCrewDlg((JFrame)SwingUtilities.getRoot(this),
                    getGame());
        else if ("shipyard".equals(dialog))
            dlg = new ShipyardDlg((JFrame)SwingUtilities.getRoot(this),
                    getGame());
        if (dlg != null)
        {
            dlg.setModal(true);
            dlg.setVisible(true);
        }
    }

    private void updateInit()
    {
        
    }

    private void updateLocation()
    {
        String newHome = null;
        LocationURI uri = new LocationURI(mGame.getShip().getLocation());
        String destSys = uri.getParam("destSys");
        if (destSys != null)
        {
            Object dSys = SchemeLogic.getFromURI(mGame.getScheme(),
                    "sys://" + destSys);
            mSystemVariables.put("jumpTarget", dSys);
            if (dSys instanceof BodyBean)
                mSystemVariables.put("locationIcon", "<img src='"+BodyView.getIconURI((BodyBean)dSys)+"'/>");
            else
                mSystemVariables.put("jumpTargetIcon", "<img src='"+TTGIconUtils.getPlanetURI("TheSun.gif")+"'/>");
        }
        int orbit = FormatUtils.atoi(uri.getParam("orbit"));
        Object loc = SchemeLogic.getFromURI(mGame.getScheme(),
                mGame.getShip().getLocation());
        mSystemVariables.put("location", loc);
        mSystemVariables.put("locationURI", uri);
        mSystemVariables.put("locationOrbit", new Integer(orbit));
        if (mGame.getShip().isDocked())
            newHome = "home_port.html";
        else if (loc instanceof BodyBean)
            newHome = "home_space.html";
        else
            newHome = "home_jump.html";
        if (loc instanceof BodyBean)
            mSystemVariables.put("locationIcon", "<img src='"+BodyView.getIconURI((BodyBean)loc)+"'/>");
        else
            mSystemVariables.put("locationIcon", "<img src='"+TTGIconUtils.getPlanetURI("TheSun.gif")+"'/>");
        //System.out.println("locationIcon="+mSystemVariables.get("locationIcon"));
        //System.out.println("jumpTargetIcon="+mSystemVariables.get("jumpTargetIcon"));
        // if (!newHome.equals(getHomePage()))
        {
            setHomePage(newHome);
            flushHistory();
            goHome();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String name = evt.getPropertyName();
        // System.out.println("AtPanel: propertyChange("+name+")");
        if (name.equals("location") || name.equals("docked")
                || name.equals("*"))
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
