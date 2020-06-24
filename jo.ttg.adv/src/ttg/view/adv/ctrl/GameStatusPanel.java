/*
 * Created on Dec 18, 2004
 *
 */
package ttg.view.adv.ctrl;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import jo.ttg.beans.dist.DistCapabilities;
import jo.ttg.beans.dist.DistConsumption;
import jo.ttg.beans.dist.DistTransition;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.core.ui.swing.ctrl.LocationPanel;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.ctrl.TTGActionListener;
import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.logic.dist.ConsumptionLogic;
import jo.ttg.logic.dist.TraverseException;
import jo.ttg.logic.dist.TraverseLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.logic.ShipReport;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.BodySpecialAdvBean;
import ttg.beans.adv.Game;
import ttg.beans.adv.PassengerBean;
import ttg.beans.adv.ShipInst;
import ttg.logic.adv.CrewLogic;
import ttg.logic.adv.FuelLogic;
import ttg.logic.adv.LocationLogic;
import ttg.logic.adv.ShipLogic;
import ttg.view.adv.dlg.BodyInfoDlg;
import ttg.view.adv.dlg.SetCourseDlg;
import ttg.view.adv.dlg.ShipInfoDlg;
import ttg.view.adv.dlg.ViewCrewDlg;
import ttg.view.adv.dlg.ViewHoldDlg;
import ttg.view.adv.dlg.ViewPassengersDlg;

/**
 * @author Jo
 *
 */
public class GameStatusPanel extends JPanel implements PropertyChangeListener, TTGActionListener
{
	private Game			mGame;
	private ImageIcon		mDockIcon;
	private ImageIcon		mUndockIcon;
	private ImageIcon		mFuelIcon;
	private ImageIcon		mScoopIcon;
	
	private JLabel			mShip;
	private JButton			mStats;
	private JProgressBar	mFuel;
	private JButton			mBuyFuel;
	private JProgressBar	mHold;
	private JButton			mViewHold;
	private JProgressBar	mPassengers;
	private JButton			mViewPassengers;
	private JProgressBar	mCrew;
	private JButton			mViewCrew;
	private LocationPanel	mLoc;
	private JButton			mDockShip;
	private LocationPanel	mDest;
	private JButton			mSetCourse;
	
	/**
	 *
	 */

	public GameStatusPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mDockIcon = TTGIconUtils.getIcon("tb_dock.gif");
	    mUndockIcon = TTGIconUtils.getIcon("tb_undock.gif");
	    mFuelIcon = TTGIconUtils.getIcon("tb_fuel.gif");
	    mScoopIcon = TTGIconUtils.getIcon("tb_scoop.gif");
	    
	    mShip = new JLabel();
	    mShip.setFont(new Font("Dialog", Font.BOLD, 24));
	    mStats = new AdvButton("tb_tool", "Stats");
		mLoc = new LocationPanel();
		mDockShip = new AdvButton("tb_dock", "Dock"); 
		mDest = new LocationPanel();
		mSetCourse = new AdvButton("tb_course", "Set Course"); 
		
		mHold = new JProgressBar();
		mHold.setStringPainted(true);
		mViewHold = new AdvButton("tb_view", "View");
		
		mPassengers = new JProgressBar();
		mPassengers.setStringPainted(true);
		mViewPassengers = new AdvButton("tb_view", "View");
		
		mFuel = new JProgressBar();
		mFuel.setStringPainted(true);
		mBuyFuel = new AdvButton("tb_fuel", "Refuel");
		

		mCrew = new JProgressBar();
		mCrew.setStringPainted(true);
		mViewCrew = new AdvButton("tb_view", "View");
	}

	private void initLink()
	{
	    ListenerUtils.listen(mViewHold, (e) -> doViewHold());
	    ListenerUtils.listen(mViewPassengers, (e) -> doViewPassengers());
	    ListenerUtils.listen(mViewCrew, (e) -> doViewCrew());
	    ListenerUtils.listen(mSetCourse, (e) -> doSetCourse());
	    ListenerUtils.listen(mDockShip, (e) -> doDockShip());
	    ListenerUtils.listen(mBuyFuel, (e) -> doBuyFuel());
	    ListenerUtils.listen(mStats, (e) -> doShipStats());
	    mLoc.addTTGActionListener(this);
	    mDest.addTTGActionListener(this);
	}

	private void initLayout()
	{
	    setLayout(new TableLayout());
	    add("1,+ fill=hv", mShip);
	    add("+,. anchor=e", mStats);
	    
	    add("1,+ anchor=w", new JLabel("Location:"));
	    add("+,. anchor=e", mDockShip);
	    add("1,+ 2x1 fill=h", mLoc);
	    
	    add("1,+ anchor=w", new JLabel("Course:"));
	    add("+,. anchor=e", mSetCourse);
	    add("1,+ 2x1 fill=h", mDest);
	    
	    add("1,+ anchor=w", new JLabel("Hold:"));
	    add("+,. anchor=e", mViewHold);
	    add("1,+ 2x1 fill=h", mHold);
	    
	    add("1,+ anchor=w", new JLabel("Passengers:"));
	    add("+,. anchor=e", mViewPassengers);
	    add("1,+ 2x1 fill=h", mPassengers);
	    
	    add("1,+ anchor=w", new JLabel("Crew:"));
	    add("+,. anchor=e", mViewCrew);
	    add("1,+ 2x1 fill=h", mCrew);

	    add("1,+ anchor=w", new JLabel("Fuel:"));
	    add("+,. anchor=e", mBuyFuel);
	    add("1,+ 2x1 fill=h", mFuel);
	}
	
	protected void doViewHold()
	{
	    ViewHoldDlg dlg = new ViewHoldDlg((JFrame)SwingUtilities.getRoot(this), mGame);
	    dlg.setModal(true);
	    dlg.show();
	}

	protected void doViewPassengers()
	{
	    ViewPassengersDlg dlg = new ViewPassengersDlg((JFrame)SwingUtilities.getRoot(this), mGame);
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void doViewCrew()
	{
	    ViewCrewDlg dlg = new ViewCrewDlg((JFrame)SwingUtilities.getRoot(this), mGame);
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void doShipStats()
	{
	    ShipInfoDlg dlg = new ShipInfoDlg((JFrame)SwingUtilities.getRoot(this), mGame, mGame.getShip().getStats());
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void doSetCourse()
	{
	    SetCourseDlg dlg = new SetCourseDlg((JFrame)SwingUtilities.getRoot(this), mGame);
	    dlg.setModal(true);
	    dlg.show();
	}
	
	protected void doDockShip()
	{
	    if (mGame.getShip().isDocked())
			LocationLogic.undock(mGame);
	    else
			LocationLogic.dock(mGame);
	}
	
	protected void doBuyFuel()
	{
	    if (mGame.getShip().isDocked())
	        FuelLogic.buyFuel(mGame);
	    else
	        FuelLogic.scoopFuel(mGame);
	}

	/**
	 * @param game
	 */
	public void setGame(Game game)
	{
		mGame = game;
		mLoc.setScheme(mGame.getScheme());
		mDest.setScheme(mGame.getScheme());
		mShip.setText(game.getShip().getName());
		update();
	}
	
	private void updateLists()
	{
		if (mGame == null)
			return;
		//System.out.println("ShipPanel:Updating lists.");
		ShipInst ship = mGame.getShip();
		if (ship == null)
			return;
		
		StringBuffer sb = new StringBuffer();
		int have = 0;
		int need = 0;
        int[] crewHave = CrewLogic.totalCrew(ship);
        int[] crewNeed = CrewLogic.neededCrew(ship);
        String[] jobNames = CrewLogic.jobNames();
        for (int i = 0; i < jobNames.length; i++)
        {
            if (jobNames[i] == null)
                continue;
            if (crewNeed[i] == 0)
                continue;
            have += crewHave[i];
            need += crewNeed[i];
            sb.append(jobNames[i]);
            sb.append(": ");
            sb.append(crewHave[i]);
            sb.append("/");
            sb.append(crewNeed[i]);
            sb.append(" ");
        }
        mCrew.setString(sb.toString());
        mCrew.setMaximum(need);
        mCrew.setValue(have);

		int cargoUsed = 0;
		for (Iterator i = mGame.getShip().getCargo().iterator(); i.hasNext(); )
		{
		    CargoBean cargo = (CargoBean)i.next();
		    cargoUsed += cargo.getQuantity();
		}

		ShipStats stats = ShipReport.report(ship.getDesign());
		mHold.setMaximum((int)(stats.getCargo()/13.5));
		mHold.setValue(cargoUsed);
		mHold.setString(cargoUsed+"/"+FormatUtils.sTons((int)(stats.getCargo()/13.5)));
		mShip.setText(mGame.getShip().getName());
	}
	
	private void updateDocked()
	{
        Object at = SchemeLogic.getFromURI(mGame.getScheme(), mGame.getShip().getLocation());
	    if (mGame.getShip().isDocked())
	    {
	        mLoc.setAdditionalText(" Docked.");
	        mDockShip.setIcon(mUndockIcon);
	        mDockShip.setToolTipText("Undock");
	        if (!ShipLogic.isSufficientCrew(mGame.getShip()))
	        {
	            mDockShip.setEnabled(false);
	            mDockShip.setToolTipText("Need more crew");
	        }
	        else if (!ShipLogic.isNoCargoPending(mGame.getDate(), mGame.getShip()))
	        {
	            mDockShip.setEnabled(false);
	            mDockShip.setToolTipText("Not all cargo delivered");
	        }
	        else
	        {
	            mDockShip.setEnabled(true);
	            mDockShip.setToolTipText(null);
	        }
	        mBuyFuel.setIcon(mFuelIcon);
	        if (FuelLogic.canRefuel(at))
	        {
	            mBuyFuel.setEnabled(true);
	            mBuyFuel.setToolTipText("Refuel @ "+FormatUtils.sCurrency(FuelLogic.getFuelPrice(mGame))+"/t");
	        }
	        else
	        {
	            mBuyFuel.setEnabled(false);
	            mBuyFuel.setToolTipText(null);
	        }
	    }
	    else
	    {
	        mDockShip.setIcon(mDockIcon);
	        mDockShip.setToolTipText("Dock");
	        mDockShip.setEnabled(at instanceof BodySpecialAdvBean);
	        mBuyFuel.setIcon(mScoopIcon);
	        mBuyFuel.setEnabled(FuelLogic.canScoop(mGame.getShip().getLocation(), at));
	        mBuyFuel.setToolTipText("Scoop Gas Giant");
	    }
	}
	
	private void updateLocation()
	{
	    mLoc.setLocation(mGame.getShip().getLocation());
	    updateDocked();
	}
	
	private void updateDestination()
	{
	    ShipInst ship = mGame.getShip();
	    mDest.setLocation(ship.getDestination());
	    if (!ship.getLocation().equals(ship.getDestination()))
	    {
	        try
	        {
	        	DistCapabilities caps = ShipLogic.getCaps(ship.getStats());
	            List<DistTransition> trans = TraverseLogic.calcTraverse(ship.getLocation(), ship.getDestination(), 
	                    caps, mGame.getScheme());
	            DistConsumption cons = ConsumptionLogic.calcConsumption(trans, caps);
	            mDest.setAdditionalText(" ETA:"+FormatUtils.formatElapsedTime(cons.getTime())+", "+Math.floor(cons.getFuel())+"t.");
	        }
	        catch (TraverseException e)
	        {
	            e.printStackTrace();
	        }
	    }
	}
	
	private void updateFuel()
	{
		mFuel.setMaximum((int)(mGame.getShip().getStats().getFuel()/13.5));
		mFuel.setValue((int)(mGame.getShip().getFuel()/13.5));
		StringBuffer sb = new StringBuffer();
		sb.append(FormatUtils.sTons(mGame.getShip().getFuel()/13.5));
		if (mGame.getShip().getUnrefinedFuel() > 0)
		{
		    sb.append(" + ");
			sb.append(FormatUtils.sTons(mGame.getShip().getUnrefinedFuel()/13.5));
		}
	    sb.append(" / ");
		sb.append(FormatUtils.sTons(mGame.getShip().getStats().getFuel()/13.5));
		mFuel.setString(sb.toString());
	}
	
	private void updatePassengers()
	{
	    ShipInst ship = mGame.getShip();
        int cabins = ship.getStats().getStaterooms();
        int berths = ship.getStats().getLowBerths();
        int cabinsCrew = ship.getCrew().size();
        int cabinsHigh = 0;
        int cabinsMiddle = 0;
        int berthsUsed = 0;
        for (Iterator i = ship.getPassengers().iterator(); i.hasNext(); )
        {
            PassengerBean p = (PassengerBean)i.next();
            if (p.getPassage() == PassengerBean.PASSAGE_LOW)
                berthsUsed++;
            else if (p.getPassage() == PassengerBean.PASSAGE_MIDDLE)
                cabinsMiddle++;
            else
                cabinsHigh++;
        }
        int cabinsUsed = cabinsCrew + cabinsHigh + cabinsMiddle;
        StringBuffer sb = new StringBuffer();
        sb.append("Cabins: ");
        if (cabinsUsed > 0)
        {
            sb.append("(");
            if (cabinsHigh > 0)
            {
                sb.append(" H:");
                sb.append(cabinsHigh);
            }
            if (cabinsMiddle > 0)
            {
                sb.append(" M:");
                sb.append(cabinsMiddle);
            }
            if (cabinsCrew > 0)
            {
                sb.append(" C:");
                sb.append(cabinsCrew);
            }
            sb.append(" )");
        }
        sb.append(cabinsUsed);
        sb.append("/");
        sb.append(cabins);
        if ((berths > 0) || (berthsUsed > 0))
        {
            sb.append(" Berths: ");
            sb.append("L:");
            if (berthsUsed > 0)
                sb.append(berthsUsed);
            else
                sb.append("-");
            sb.append("/");
            sb.append(berths);
        }
		mPassengers.setString(sb.toString());
		mPassengers.setMaximum(cabins+berths);
		mPassengers.setValue(cabinsUsed + berthsUsed);
	}
	
	private void update()
	{
	    updateLocation();
	    updateDestination();
	    updateLists();
	    updateFuel();
	    updatePassengers();
	}

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String name = evt.getPropertyName();
		if (name.equals("date") || name.equals("selectedShip")
				|| name.equals("location") || name.equals("cargo")
				|| name.equals("crew") || name.equals("*"))
					updateLists();
		if (name.equals("location") || name.equals("docked") || name.equals("*"))
		{
		    updateLocation();
		    updatePassengers();
		}
		if (name.equals("date") || name.equals("cargo") || name.equals("freight"))
		    updateDocked();
		if (name.equals("location") || name.equals("destination") || name.equals("*"))
		    updateDestination();
		if (name.equals("fuel") || name.equals("unrefinedFuel"))
		    updateFuel();
		if (name.equals("passengers") || name.equals("crew"))
		    updatePassengers();
		if (name.equals("name"))
		    mShip.setText(mGame.getShip().getName());
    }

    /* (non-Javadoc)
     * @see ttg.view.ctrl.TTGActionListener#actionPerformed(ttg.view.ctrl.TTGActionEvent)
     */
    public void actionPerformed(TTGActionEvent e)
    {
        if (e.getObject() instanceof BodyBean)
        {
            BodyInfoDlg dlg = new BodyInfoDlg((JFrame)SwingUtilities.getRoot(this), (BodyBean)e.getObject());
            dlg.setModal(true);
            dlg.show();
        }
    }
}
