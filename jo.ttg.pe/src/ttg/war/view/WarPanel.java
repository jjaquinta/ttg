package ttg.war.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import jo.ttg.core.ui.swing.ctrl.HexField;
import jo.ttg.core.ui.swing.ctrl.TTGActionEvent;
import jo.ttg.core.ui.swing.util.TTGActionUtils;
import jo.util.ui.swing.ctrl.FixedPanel;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.DebugUtils;
import ttg.war.beans.GameInst;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.IOLogic;
import ttg.war.logic.IconLogic;
import ttg.war.logic.ShipLogic;
import ttg.war.logic.WorldLogic;
import ttg.war.view.act.ChooseGamePanel;
import ttg.war.view.act.ChooseSidePanel;
import ttg.war.view.act.FleeShipsPanel;
import ttg.war.view.act.MoveShipsPanel;
import ttg.war.view.act.RepairShipsPanel;
import ttg.war.view.act.SetupShipsPanel;
import ttg.war.view.act.TargetShipsPanel;
import ttg.war.view.info.InfoPanel;
import ttg.war.view.msg.MessagePanel;

public class WarPanel extends JPanel
{
	public static final int DONE = 0;
	public static final int MESSAGE = 1;
	public static final int SETUP = 2;
	public static final int MOVE = 3;
	public static final int FLEE = 4;
	public static final int TARGET = 5;
	public static final int REPAIR = 6;
	public static final int CHOOSE_SIDE = 7;
	public static final int CHOOSE_GAME = 8;
	public static final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 16);

	private GameInst mGame;
	private SideInst mSide;
	private WorldInst	mHoverWorld;
	private String[]	mHoverText;
	private Color[]		mHoverFG;
	private Color[]		mHoverBG;
	private int mMode;
	private Object mArg1;
	private Object mArg2;

	private HelpFrame mDlgHelp;
	private JPanel mClient;
	private CardLayout mLayout;
	private JLabel mStatus;
	private JButton mPhase;
	private JButton mAbout;
	private JButton mHelp;

	private JPanel mThreePanel;
	private HexField mStarPanel;
	private InfoPanel mInfoPanel;
	private JScrollPane mStarScroller;
	// actions		
	private ChooseGamePanel mGameChooser;
	private ChooseSidePanel mSideChooser;
	private SetupShipsPanel mSetupShips;
	private MoveShipsPanel mMoveShips;
	private MessagePanel mMessagePanel;
	private FleeShipsPanel mFleeShips;
	private TargetShipsPanel mTargetShips;
	private RepairShipsPanel mRepairShips;

	/**
	 *
	 */

	public WarPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		IOLogic.installProtocolHandlers();
		
		mClient = new FixedPanel();
		mLayout = new CardLayout();
		mClient.setLayout(mLayout);
		mStatus = new JLabel();
		mPhase = new JButton();
		mAbout = new WarButton(IconLogic.mButtonInfo);
		mHelp = new WarButton(IconLogic.mButtonHelp);

		mGameChooser = new ChooseGamePanel(this);
		mSideChooser = new ChooseSidePanel(this);
		mSetupShips = new SetupShipsPanel(this);
		mMoveShips = new MoveShipsPanel(this);
		mFleeShips = new FleeShipsPanel(this);
		mTargetShips = new TargetShipsPanel(this);
		mRepairShips = new RepairShipsPanel(this);
		mThreePanel = new JPanel();
		mInfoPanel = new InfoPanel(this);
		mMessagePanel = new MessagePanel(this);

		mStarPanel = new WarHexField(this);
		mDlgHelp = new HelpFrame();
	}

	private void initLink()
	{
	    ListenerUtils.listen(mPhase, (ev) -> doPhase());
        ListenerUtils.listen(mAbout, (ev) -> doAbout());
        ListenerUtils.listen(mHelp, (ev) -> doHelp());
        TTGActionUtils.listen(mStarPanel, (e) -> 
			{
				switch (e.getID())
				{
					case TTGActionEvent.HOVER :
						doHexHover(e.getObject());
						break;
					case TTGActionEvent.SELECTED :
					case TTGActionEvent.ACTIVATED:
						doHexSelected(e.getObject());
						break;
					default :
						DebugUtils.debug(e.toString()); 
						break;
				}
			}
		);
	}

	private void initLayout()
	{
		Border b;
		b = BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground());
		b = BorderFactory.createCompoundBorder(b,
				BorderFactory.createRaisedBevelBorder());
		b = BorderFactory.createCompoundBorder(b,
			BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground()));
		mClient.setBorder(b);
		mClient.add("gameChooser", mGameChooser);
		mClient.add("sideChooser", mSideChooser);
		mClient.add("setupShips", mSetupShips);
		mClient.add("moveShips", mMoveShips);
		mClient.add("fleeShips", mFleeShips);
		mClient.add("targetShips", mTargetShips);
		mClient.add("repairShips", mRepairShips);
		mClient.add("message", mMessagePanel);
		mClient.add("blank", new JPanel());

		mStarScroller = new JScrollPane(mStarPanel);

		mStarScroller.setBorder(
			BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(3, 3, 3, 3, getBackground()),
				mStarScroller.getBorder()));

		JPanel buttonBar = new JPanel();
		buttonBar.add(mPhase);
		buttonBar.add(mHelp);

		mThreePanel.setLayout(new BorderLayout());
		mClient.setSize(200, 600);
		mInfoPanel.setSize(200, 600);
		mThreePanel.add("Center", mStarScroller);
		mThreePanel.add("East", mInfoPanel);
		mThreePanel.add("West", mClient);

		JPanel statusBar = new JPanel();
		statusBar.setLayout(new BorderLayout());
		statusBar.add("Center", mStatus);
		statusBar.add("East", buttonBar);
		statusBar.add("West", mAbout);
		mPhase.setVisible(false);

		setLayout(new BorderLayout());
		add("Center", mThreePanel);
		add("South", statusBar);
	}

	protected void doStatusChange()
	{
		mStatus.setText(mGame.getStatus());
	}

	protected void doPhaseChange()
	{
		mPhase.setText(
			"Turn: " + mGame.getTurn() + ", Phase: " + mGame.getPhase());
	}

	protected void doHexSelected(Object obj)
	{
		WorldInst world = WorldLogic.getWorld(mGame, obj);
		mInfoPanel.setObject(world);
	}

	protected void doHexHover(Object obj)
	{
		mHoverWorld = WorldLogic.getWorld(mGame, obj);
		if (mHoverWorld == null)
		{
			mStarPanel.setToolTipText(null);
			return;
		}
		List<ShipInst> ships = WorldLogic.getVisibleShips(mGame, mHoverWorld, mSide);
		if (ships.size() == 0)
		{
			mStarPanel.setToolTipText(null);
			return;
		}
		Map<SideInst,int[]> factors = new HashMap<>();
		for (ShipInst ship : ships)
		{
			int[] f = (int[])factors.get(ship.getSideInst());
			if (f == null)
			{
				f = new int[2];
				f[0] = 0;
				f[1] = 0;
				factors.put(ship.getSideInst(), f);
			}
			f[0] += ShipLogic.getAttack(ship);
			f[1] += ShipLogic.getDefense(ship);
		}
		StringBuffer tip = new StringBuffer();
		mHoverText = new String[factors.size()]; 
		mHoverFG = new Color[factors.size()]; 
		mHoverBG = new Color[factors.size()];
		int idx = 0; 
		for (Iterator<SideInst> i = factors.keySet().iterator(); i.hasNext(); idx++)
		{
			SideInst side = i.next();
			int[] f = (int[])factors.get(side);
			mHoverText[idx] = f[0]+"."+f[1];
			mHoverFG[idx] = side.getColor2();
			mHoverBG[idx] = side.getColor1();
			if (tip.length() > 0)
				tip.append(" ");
			tip.append(mHoverText[idx]);
		}
		//tip.append(" ");
		mStarPanel.setToolTipText(tip.toString());
	}
	
	private void doPhase()
	{
		mInfoPanel.setObject(mGame);
	}
	
	private void doAbout()
	{
		WarAbout dlg = new WarAbout();
		dlg.setVisible(true);
	}
	
	private void doHelp()
	{
		mDlgHelp.setVisible(true);
		mDlgHelp.doHome();
	}
	
	public void doHelp(String page)
	{
		mDlgHelp.setVisible(true);
		mDlgHelp.showHelp(page);
	}

	public int getMode()
	{
		return mMode;
	}

	public void setMode(int i)
	{
		mMode = i;
		switch (mMode)
		{
			case DONE :
				mLayout.show(mClient, "blank");
				break;
			case SETUP :
				mSetupShips.init();
				mLayout.show(mClient, "setupShips");
				break;
			case MOVE :
				mMoveShips.init();
				mLayout.show(mClient, "moveShips");
				break;
			case TARGET :
				mTargetShips.init();
				mLayout.show(mClient, "targetShips");
				break;
			case REPAIR :
				mRepairShips.init();
				mLayout.show(mClient, "repairShips");
				break;
			case FLEE :
				mFleeShips.init();
				mLayout.show(mClient, "fleeShips");
				break;
			case MESSAGE :
				mLayout.show(mClient, "message");
				break;
			case CHOOSE_SIDE :
				mSideChooser.init();
				mLayout.show(mClient, "sideChooser");
				break;
			case CHOOSE_GAME :
				mGameChooser.init();
				mLayout.show(mClient, "gameChooser");
				break;
		}
		mStarPanel.repaint();
	}

	public SideInst getSide()
	{
		return mSide;
	}

	public void setSide(SideInst inst)
	{
		mSide = inst;
		mPhase.setVisible(true);
	}

	public HexField getStarPanel()
	{
		return mStarPanel;
	}

	public InfoPanel getInfoPanel()
	{
		return mInfoPanel;
	}

	/**
	 * @return
	 */
	public MessagePanel getMessagePanel()
	{
		return mMessagePanel;
	}

	/**
	 * @return
	 */
	public Object getArg1()
	{
		return mArg1;
	}

	/**
	 * @return
	 */
	public Object getArg2()
	{
		return mArg2;
	}

	/**
	 * @param object
	 */
	public void setArg1(Object object)
	{
		mArg1 = object;
	}

	/**
	 * @param object
	 */
	public void setArg2(Object object)
	{
		mArg2 = object;
	}

	public GameInst getGame()
	{
		return mGame;
	}

	public void setGame(GameInst inst)
	{
		mGame = inst;
		mStarPanel.setScheme(mGame.getScheme());
		mStarPanel.setHexesWide(
			(int) (mGame.getLowerBound().getX()
				- mGame.getUpperBound().getX()));
		mStarPanel.setHexesHigh(
			(int) (mGame.getLowerBound().getY()
				- mGame.getUpperBound().getY()));
		mStarPanel.setOrigin(mGame.getUpperBound());
		mStarPanel.setPainter(new WarHexPainter(this, mStarPanel));
		mGame.listen("status", (ov,nv) -> doStatusChange());
		mGame.listen("phase", (ov,nv) -> doPhaseChange());
		doPhaseChange();
		mGame.listen("turn", (ov,nv) -> doPhaseChange());
		mStarScroller.doLayout();
	}
	/**
	 * @return
	 */
	public WorldInst getHoverWorld()
	{
		return mHoverWorld;
	}

	/**
	 * @param inst
	 */
	public void setHoverWorld(WorldInst inst)
	{
		mHoverWorld = inst;
	}

	/**
	 * @return
	 */
	public Color[] getHoverBG()
	{
		return mHoverBG;
	}

	/**
	 * @return
	 */
	public Color[] getHoverFG()
	{
		return mHoverFG;
	}

	/**
	 * @return
	 */
	public String[] getHoverText()
	{
		return mHoverText;
	}

}
