/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.adv.view;

import java.awt.BorderLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingUtilities;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.Game;
import ttg.adv.beans.ShipInst;
import ttg.adv.logic.AdvEventLogic;
import ttg.adv.view.ctrl.AtPanel;
import ttg.adv.view.ctrl.DatePanel;
import ttg.adv.view.ctrl.GameStatusPanel;
import ttg.adv.view.ctrl.LoanPanel;
import ttg.adv.view.ctrl.MoneyPanel;
import ttg.adv.view.dlg.ShipCombatDlg;
import ttg.adv.view.dlg.ViewStatusDlg;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class AdvFrame extends JFrame implements PropertyChangeListener
{
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long            serialVersionUID = -7416311502254765299L;
    private Game                         mGame;
    private List<PropertyChangeListener> mListeners;
    private AdvSoundHandler              mSoundHandler;
    private AdvStatusHandler             mStatusHandler;

    private AdvToolbar                   mToolbar;
    private DatePanel                    mDate;
    private MoneyPanel                   mMoney;
    private LoanPanel                    mLoan;
    private JButton                      mStatusHistory;
    private JLabel                       mStatus;
    private GameStatusPanel              mGameStatus;
    private AtPanel                      mClient;

    /**
     *
     */

    public AdvFrame()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        setTitle("The Traveller Adventure");
        setIconImage(TTGIconUtils.getIcon("icons/frame_icon.gif").getImage());

        mListeners = new ArrayList<>();
        mStatusHandler = new AdvStatusHandler();
        mSoundHandler = new AdvSoundHandler();
        mDate = new DatePanel();
        mListeners.add(mDate);
        mStatus = new JLabel("");
        mStatusHistory = new JButton("^");
        mStatusHistory.setMargin(new Insets(0, 0, 0, 0));
        mGameStatus = new GameStatusPanel();
        mListeners.add(mGameStatus);
        mClient = new AtPanel();
        mListeners.add(mClient);
        mLoan = new LoanPanel();
        mListeners.add(mLoan);
        mMoney = new MoneyPanel();
        mListeners.add(mMoney);
        mToolbar = new AdvToolbar(this);
    }

    private void initLink()
    {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e)
            {
                doFrameShut();
            }

            public void windowOpened(WindowEvent e)
            {
                doFrameStart();
            }
        });
        ListenerUtils.listen(mStatusHistory, (ev) -> {
            if (Adv.CHEATS_ENABLED
                    && ((ev.getModifiers() & ActionEvent.CTRL_MASK) != 0))
                doTest();
            else
                doStatusHistory();
        });
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(mDate);
        buttonBar.add(mMoney);
        buttonBar.add(mLoan);
        JPanel statusBar = new JPanel();
        statusBar.setLayout(new BorderLayout());
        statusBar.add("West", mStatusHistory);
        statusBar.add("Center", mStatus);
        statusBar.add("East", buttonBar);

        getContentPane().setLayout(new BorderLayout());
        // getContentPane().add("East", new PopupPanel(mGameStatus,
        // PopupPanel.WEST));
        getContentPane().add("South", statusBar);
        getContentPane().add("Center", new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, mClient, mGameStatus));
        getContentPane().add("North", mToolbar);
        setSize(800, 600);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.
     * PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        Object src = ev.getSource();
        String name = ev.getPropertyName();
        // System.out.println("AdvFrame: propertyChange("+name+")");
        if (src == mGame)
        {
            if (name.equals("status") || name.equals("*"))
                updateStatus();
            else if (name.equals("ship"))
            {
                if (ev.getOldValue() != null)
                    ((ShipInst)ev.getOldValue())
                            .removePropertyChangeListener(this);
                if (ev.getNewValue() != null)
                    ((ShipInst)ev.getNewValue())
                            .addPropertyChangeListener(this);
            }
        }
        for (PropertyChangeListener pcl : mListeners)
            pcl.propertyChange(ev);
    }

    private void updateStatus()
    {
        mStatus.setText(mGame.getStatus());
        mStatusHistory.setEnabled(mGame.getStatusHistory().size() > 1);
    }

    /**
     * 
     */
    protected void doFrameShut()
    {
        mToolbar.doExit();
    }

    /**
     * 
     */
    protected void doFrameStart()
    {
        // mMenuGame.doNew();
    }

    protected void doStatusHistory()
    {
        ViewStatusDlg dlg = new ViewStatusDlg(
                (JFrame)SwingUtilities.getRoot(this), mGame);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    /**
     * @return
     */
    public Game getGame()
    {
        return mGame;
    }

    /**
     * @param game
     */
    public void setGame(Game game)
    {
        if (mGame != null)
        {
            mGame.removePropertyChangeListener(this);
            if (mGame.getShip() != null)
                mGame.getShip().removePropertyChangeListener(this);
            AdvEventLogic.removeEventHandler(mGame, mSoundHandler);
            AdvEventLogic.removeEventHandler(mGame, mStatusHandler);
        }
        mGame = game;
        mGameStatus.setGame(mGame);
        mClient.setGame(mGame);
        mDate.setGame(mGame);
        mMoney.setGame(mGame);
        mLoan.setGame(mGame);
        if (mGame != null)
        {
            mGame.addPropertyChangeListener(this);
            if (mGame.getShip() != null)
                mGame.getShip().addPropertyChangeListener(this);
        }
        AdvEventLogic.addEventHandler(mGame, mSoundHandler);
        AdvEventLogic.addEventHandler(mGame, mStatusHandler);
        propertyChange(new PropertyChangeEvent(this, "*", null, null));
        updateStatus();
    }

    private void doTest()
    {
        List<ShipInst> goodGuys = new ArrayList<>();
        goodGuys.add(mGame.getShip());
        List<ShipInst> badGuys = new ArrayList<>();
        ShipCombatDlg dlg = new ShipCombatDlg(this, mGame, goodGuys, badGuys);
        dlg.setVisible(true);
    }
}
