/*
 * Created on Dec 26, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.ctrl;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.Game;
import ttg.adv.logic.TimeLogic;

/**
 * @author jgrant
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class DatePanel extends JPanel implements PropertyChangeListener
{
    private Game    mGame;

    private JLabel  mDate;
    private JButton mForward;
    private JButton mFastForward;

    /**
     *  
     */

    public DatePanel()
    {
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mDate = new JLabel();
        mForward = new AdvButton("tape_f", "Next Hour");
        mFastForward = new AdvButton("tape_ff", "Next Day");
    }

    private void initLink()
    {
        ListenerUtils.listen(mForward, (e) -> TimeLogic.passTime(mGame, 60));
        ListenerUtils.listen(mFastForward, (e) -> TimeLogic.passTime(mGame, 24*60));
    }

    private void initLayout()
    {
        JPanel buttonBar = new JPanel();
        buttonBar.add(mForward);
        buttonBar.add(mFastForward);
        setLayout(new BorderLayout());
        add("Center", mDate);
        add("East", buttonBar);
    }

    private void updateDate()
    {
        mDate.setText(FormatUtils.formatDateTime(mGame.getDate()));
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt)
    {
        String name = evt.getPropertyName();
        if (name.equals("date") || name.equals("*"))
            updateDate();
    }

    public Game getGame()
    {
        return mGame;
    }

    public void setGame(Game game)
    {
        mGame = game;
    }

}