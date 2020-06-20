/*
 * Created on Mar 31, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view.msg;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ttg.war.beans.PlayerMessage;
import ttg.war.beans.SideInst;
import ttg.war.view.ObjectButton;
import ttg.war.view.SideRenderer;
import ttg.war.view.WarPanel;

/**
 * @author jgrant
 *
 *         To change the template for this generated type comment go to
 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class GameOverPanel extends JPanel implements ListSelectionListener
{
    private WarPanel        mPanel;

    private ObjectButton    mWinner;
    private JList<SideInst> mSides;

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
        mSides = new JList<>();
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
        int bestScore = -1;
        SideInst bestSide = null;
        for (SideInst side : mPanel.getGame().getSides())
        {
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
        mSides.setListData(
                mPanel.getGame().getSides().toArray(new SideInst[0]));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.
     * ListSelectionEvent)
     */
    public void valueChanged(ListSelectionEvent arg0)
    {
        if (mSides.getSelectedValue() != null)
            mPanel.getInfoPanel().setObject(mSides.getSelectedValue());
    }
}
