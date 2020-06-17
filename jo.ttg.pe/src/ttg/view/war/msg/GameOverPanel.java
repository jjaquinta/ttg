/*
 * Created on Mar 31, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war.msg;

import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ttg.beans.war.PlayerMessage;
import ttg.beans.war.SideInst;
import ttg.view.war.ObjectButton;
import ttg.view.war.SideRenderer;
import ttg.view.war.WarPanel;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GameOverPanel extends JPanel implements ListSelectionListener
{
	private WarPanel		mPanel;
	private PlayerMessage	mMessage;
	
	private ObjectButton	mWinner;
	private JList			mSides;
	
	/**
	 *
	 */

	public GameOverPanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mWinner = new ObjectButton(mPanel);
		mSides = new JList();
		mSides.setCellRenderer(new SideRenderer());
	}

	private void initLink()
	{
		mSides.addListSelectionListener(this);
	}

	private void initLayout()
	{
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		add(new JLabel("GAME OVER"));
		add(new JLabel("The winner is:"));
		add(mWinner);
		add(new JLabel("The players were:"));
		add(new JScrollPane(mSides));
	}
	
	public void setMessage(PlayerMessage msg)
	{
		mMessage = msg;
		int bestScore = -1;
		SideInst bestSide = null;
		for (Iterator i = mPanel.getGame().getSides().iterator(); i.hasNext(); )
		{
			SideInst side = (SideInst)i.next();
			if (side.getVictoryPoints() > bestScore)
			{
				bestSide = side;
				bestScore = side.getVictoryPoints();
			}
			else if (side.getVictoryPoints() == bestScore)
			{
				bestSide = null; // a tie
			}
		}
		mWinner.setObject(bestSide);
		if (bestSide == null)
			mWinner.setText("It's a tie!");
		mSides.setListData(mPanel.getGame().getSides().toArray());
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent arg0)
	{
		if (mSides.getSelectedValue() != null)
			mPanel.getInfoPanel().setObject(mSides.getSelectedValue());
	}
}
