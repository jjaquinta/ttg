/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.dlg;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.BodySpecialAdvBean;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;
import ttg.adv.logic.LocationLogic;
import ttg.adv.logic.ShipLogic;
import ttg.adv.view.ctrl.ShipEditPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShipyardDlg extends JDialog
{
	private Game			mGame;
	private ShipInst		mShip;

	private ShipEditPanel	mEditor;
	
	private JButton			mOK;
	private JButton			mStats;
	private JLabel			mCost;
	
	/**
	 *
	 */

	public ShipyardDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	public ShipyardDlg(JDialog parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mShip = mGame.getShip();
		BodySpecialAdvBean loc = (BodySpecialAdvBean)SchemeLogic.getFromURI(mGame.getScheme(), mShip.getLocation());
	    PopulatedStatsBean popStats = LocationLogic.findNearestPopulatedStats(loc);
	    
        setTitle("Shipyard");
		
		mEditor = new ShipEditPanel(this, mShip.getDesign());
		mEditor.setTechLevel(popStats.getUPP().getTech().getValue());
		
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mStats = new JButton("Stats");        
		mCost = new JLabel("");        
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
		ListenerUtils.listen(mStats, (e) -> doStats());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mStats);
		buttonBar.add(new JLabel(" Cost: "));
		buttonBar.add(mCost);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", mEditor);
		
		pack();
	}
    
    private void update()
    {
    }

	protected void doOK()
	{
	    List<ShipComponent> selling = getForSale();
	    if (selling.size() > 0)
	        ShipLogic.sellComponents(mGame, selling);
	    List<ShipComponent> buying = getToBuy();
	    if (buying.size() > 0)
	        ShipLogic.buyComponents(mGame, buying);
		dispose();
	}

    /**
     * @return
     */
    private List<ShipComponent> getToBuy()
    {
        List<ShipComponent> buying = new ArrayList<>();
	    // TODO:
        return buying;
    }

    /**
     * @return
     */
    private List<ShipComponent> getForSale()
    {
        List<ShipComponent> selling = new ArrayList<>();
	    // TODO:
        return selling;
    }

	protected void doAdd()
	{
	    // TODO:
		update();
	}

	protected void doDel()
	{
	    // TODO:
		update();
	}
	
	protected void doStats()
	{
	    // TODO:
	    //ShipInfoDlg dlg = new ShipInfoDlg((JFrame)SwingUtilities.getRoot(this), null, mShipStats);
	    //dlg.setDisplayErrors(mShipStats.getErrors().size() > 0);
	    //dlg.setModal(true);
	    //dlg.setVisible(true);
	}
}
