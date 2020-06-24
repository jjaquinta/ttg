/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.AccountsBean;
import ttg.beans.adv.Game;
import ttg.logic.adv.MoneyLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class AcctInfoDlg extends JDialog
{
	private Game			mGame;
	private AccountsBean	mAccounts;
	
	private JLabel			mCash;
	private JLabel			mDate;
	private JLabel			mDue;
	private JLabel			mAmount;
	private JLabel			mLeft;
	private JList			mAudit;
	private JButton			mOK;
	private JButton			mClear;
	private JButton			mPay;
	
	/**
	 *
	 */

	public AcctInfoDlg(JFrame parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	public AcctInfoDlg(JDialog parent, Game game)
	{
		super(parent);
		mGame = game;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Accounts");
		mAccounts = mGame.getAccounts();
		mCash = new JLabel();
		mDate = new JLabel();
		mDue = new JLabel();
		mAmount = new JLabel();
		mLeft = new JLabel();
		mAudit = new JList();
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mClear = new JButton("Clear Ledger");        
		mPay = new JButton("Make Payment");        
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
		ListenerUtils.listen(mClear, (e) -> doClear());
		ListenerUtils.listen(mPay, (e) -> doPay());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mPay);
		buttonBar.add(mClear);
		
		JPanel client = new JPanel();
		client.setLayout(new TableLayout());
		client.add("1,+", new JLabel("Cash:"));
		client.add("+,. fill=h", mCash);
		client.add("1,+", new JLabel("Today:"));
		client.add("+,. fill=h", mDate);
		client.add("1,+", new JLabel("New Payment Due:"));
		client.add("+,. fill=h", mDue);
		client.add("1,+", new JLabel("Payment Amount:"));
		client.add("+,. fill=h", mAmount);
		client.add("1,+", new JLabel("Payments Left:"));
		client.add("+,. fill=h", mLeft);
		client.add("1,+", new JLabel("Ledger:"));
		client.add("+,. fill=hv", new JScrollPane(mAudit));
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", client);
		
		update();
		pack();
	}
    
    private void update()
    {
        mCash.setText(FormatUtils.sCurrency(mAccounts.getCash()));
        mDate.setText(FormatUtils.formatDate(mGame.getDate()));
        mDue.setText(FormatUtils.formatDate(mAccounts.getLoanPaymentDue()));
        mAmount.setText(FormatUtils.sCurrency(mAccounts.getLoanPaymentAmount()));
        mLeft.setText(String.valueOf(mAccounts.getLoanPaymentsLeft()));
        mAudit.setListData(mAccounts.getAuditTrail().toArray());
    }

	protected void doOK()
	{
		dispose();
	}

	protected void doClear()
	{
	    MoneyLogic.clearAuditTrail(mGame);
		update();
	}

	protected void doPay()
	{
	    MoneyLogic.makeLoanPayment(mGame);
		update();
	}
}
