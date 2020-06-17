package ttg.view.war.info;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JLabel;

import jo.ttg.logic.mw.UPPLogic;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.WorldInst;
import ttg.logic.war.IconLogic;
import ttg.logic.war.WorldLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.ObjectButton;
import ttg.view.war.ShipTree;
import ttg.view.war.WarButton;
import ttg.view.war.WarPanel;

public class WorldInfoPanel extends HelpPanel
{
	private InfoPanel	mInfo;
	private WorldInst	mWorld;
	
	private ObjectButton	mName;
	private JLabel			mUPP;
	private ObjectButton	mSide;
	private WarButton	mPort;
	private JLabel		mPop;
	private JLabel		mFuel;
	private JLabel		mRepairs;
	private JLabel		mPoints;
	private JLabel		mResGen;
	private	ShipTree	mShips;
	
	/**
	 *
	 */

	public WorldInfoPanel(WarPanel panel, InfoPanel info)
	{
		mPanel = panel;
		mInfo = info;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mName = new ObjectButton(mPanel);
		mUPP = new JLabel();
		mSide = new ObjectButton(mPanel);
		mPort = new WarButton("Starport");
		mPort.setIcon(IconLogic.mButtonBuild);
		mRepairs = new JLabel();
		mPop = new JLabel();
		mFuel = new JLabel();
		mResGen = new JLabel();
		mPoints = new JLabel();
		mShips = new ShipTree(mPanel);
		mShips.setName("WorldInfo");
		mShips.setInfoOnClick(true);
		mShips.setInfoOnSelect(false);
		mShips.setShipsOnSelect(true);
		mShips.setRootLabel("Ships:");
	}

	private void initLink()
	{
	    ListenerUtils.listen(mPort, (e) -> doPort());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ 2x1 fill=h", makeTitle("World Info", "InfoWorld.htm"));
		add("1,+", new JLabel("World:"));
		add("+,. fill=h", mName);
		add("1,+", new JLabel("UPP:"));
		add("+,. fill=h", mUPP);
		add("1,+", new JLabel("Side:"));
		add("+,. fill=h", mSide);
		add("1,+", new JLabel("Population:"));
		add("+,. fill=h", mPop);
		add("1,+", new JLabel("Fuel:"));
		add("+,. fill=h", mFuel);
		add("1,+", new JLabel("Repairs:"));
		add("+,. fill=h", mRepairs);
		add("1,+", new JLabel("Resources:"));
		add("+,. fill=h", mResGen);
		add("1,+", new JLabel("Points:"));
		add("+,. fill=h", mPoints);
		add("1,+ 2x1 weighty=20 fill=hv", mShips);
		add("2,+ fill=h", mPort);
	}
	
	public void setObject(WorldInst obj)
	{
		mWorld = obj;
		if (mWorld == null)
		{
			mName.setObject(null);
			mUPP.setText(null);
			mSide.setObject(null);
			mRepairs.setText(null);
			mPoints.setText(null);
			mPop.setText(null);
			mFuel.setText(null);
			mResGen.setText(null);
			mShips.done();
			mShips.init(new ArrayList());
			mPort.setVisible(false);
		}
		else
		{
			mName.setObject(mWorld);
			mSide.setObject(mWorld.getSide());
			if (mWorld.getWorld() != null)
			{
				mUPP.setText(UPPLogic.getUPPDesc(mWorld.getWorld().getPopulatedStats().getUPP()));
				mRepairs.setText(WorldLogic.getRepairsDesc(mWorld));
				mPoints.setText(String.valueOf(WorldLogic.getHaveWorldValue(mPanel.getGame(), mWorld)));
				mPop.setText(WorldLogic.getPopulationDesc(mWorld));
				mFuel.setText(WorldLogic.getFuelDesc(mWorld));
				if (WorldLogic.isFuelDefended(mPanel.getGame(), mWorld, mPanel.getSide()))
				{
					mFuel.setForeground(null);
					mFuel.setToolTipText("Fuel points adequately defended");
				}
				else
				{
					mFuel.setForeground(Color.RED);
					mFuel.setToolTipText("Fuel points inadequately defended");
				}
				mResGen.setText(String.valueOf(WorldLogic.getResourceGeneration(mPanel.getGame(), mWorld))
					+ "/turn");
				int port = mWorld.getWorld().getPopulatedStats().getUPP().getPort().getValue();
				if ((mWorld.getSide() == mPanel.getSide())
					&& mPanel.getGame().getGame().isAllowConstruction()
					&& ((port == 'A') || (port == 'B')))
				{
					mPort.setVisible(true);
				}
				else
				{
					mPort.setVisible(false);
				}
			}
			else
			{
				mUPP.setText(null);
				mRepairs.setText(null);
				mPoints.setText(null);
				mResGen.setText(null);
				mPop.setText(null);
				mFuel.setText(null);
				mPort.setVisible(false);
			}
			mShips.done();
			mShips.init(WorldLogic.getVisibleShips(mPanel.getGame(), mWorld, mPanel.getSide()));
		}
	}
	
	private void doPort()
	{
		mInfo.showInfo("port");
	}
}
