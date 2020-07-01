package jo.ttg.deckplans.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.deckplans.logic.RuntimeLogic;
import jo.ttg.deckplans.ui.deck.DeckPanel;
import jo.ttg.deckplans.ui.plan.PlanPanel;
import jo.ttg.deckplans.ui.scan.ScanPanel;

public class DeckerPanel extends JPanel
{
    private JPanel      mClient;
    private ScanPanel   mScanClient;
    private PlanPanel   mPlanClient;
    private DeckPanel   mDeckClient;
    
    private JLabel  mStatus;

	/**
	 *
	 */

	public DeckerPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
		doModeChange();
	}

	private void initInstantiate()
	{
	    mClient = new JPanel();
	    mScanClient = new ScanPanel();
	    mPlanClient = new PlanPanel();
	    mDeckClient = new DeckPanel();
	    mStatus = new JLabel();
	}

	private void initLink()
	{
	    RuntimeLogic.listen("mode", (ov,nv) -> doModeChange());
        RuntimeLogic.listen("status", (ov,nv) -> doStatusChange());
	}

	private void initLayout()
	{
	    mClient.setLayout(new CardLayout());
	    mClient.add("scan", mScanClient);
	    mClient.add("plan", mPlanClient);
	    mClient.add("deck", mDeckClient);
	    
	    setLayout(new BorderLayout());
	    add("Center", mClient);
	    add("South", mStatus);
	}

    private void doStatusChange()
    {
        mStatus.setText(RuntimeLogic.getInstance().getStatus());
    }

	private void doModeChange()
	{
		mStatus.setText(RuntimeLogic.getInstance().getStatus());
		switch (RuntimeLogic.getInstance().getMode())
		{
		    case RuntimeBean.DECK:
		        ((CardLayout)mClient.getLayout()).show(mClient, "deck");
		        break;
            case RuntimeBean.PLAN:
                ((CardLayout)mClient.getLayout()).show(mClient, "plan");
                break;
            case RuntimeBean.SCAN:
                ((CardLayout)mClient.getLayout()).show(mClient, "scan");
                break;
		}
	}
}
