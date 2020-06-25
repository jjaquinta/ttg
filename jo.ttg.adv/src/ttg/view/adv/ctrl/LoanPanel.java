/*
 * Created on Jan 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.ctrl;

import java.awt.Color;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import jo.ttg.beans.DateBean;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.adv.AdvEvent;
import ttg.beans.adv.Game;
import ttg.logic.adv.AdvEventLogic;
import ttg.logic.adv.MoneyLogic;
import ttg.view.adv.Adv;
import ttg.view.adv.dlg.AcctInfoDlg;

/**
 * @author jgrant
 * 
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class LoanPanel extends JButton implements PropertyChangeListener
{
    private Game mGame;

    public LoanPanel()
    {
        super("$");
        setMargin(new Insets(0,0,0,0));
        ListenerUtils.listen(this, (ev) -> {
    		    if (Adv.CHEATS_ENABLED && ((ev.getModifiers()&ActionEvent.CTRL_MASK) != 0))
    		        doTest();
    		    else
    		        doAccounts();
            });
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        String name = ev.getPropertyName();
        if (name.equals("*") || name.equals("accounts") || name.equals("date"))
        {
            Color oldColor = getForeground();
            Color newColor = oldColor;
            if (mGame.getAccounts().getLoanPaymentsLeft() <= 0)
            {
                newColor = Color.BLACK;
                setEnabled(false);
            }
            else
            {
                DateBean now = mGame.getDate();
                DateBean due = mGame.getAccounts().getLoanPaymentDue();
                int when = due.getMinutes() - now.getMinutes();
                if (when < 0)
                    newColor = Color.RED;
                else if (when < 2 * 24 * 60)
                    newColor = Color.ORANGE;
                else if (when < 4 * 24 * 60)
                    newColor = Color.YELLOW;
                else
                    newColor = Color.BLACK;
                setEnabled(true);
            }
            if (!oldColor.equals(newColor))
            {
                setForeground(newColor);
                if (newColor.equals(Color.RED))
                    AdvEventLogic.fireEvent(mGame, AdvEvent.LOAN_OVERDUE);
                else if (newColor.equals(Color.ORANGE))
                    AdvEventLogic.fireEvent(mGame, AdvEvent.LOAN_DUE);
                else if (newColor.equals(Color.YELLOW))
                    AdvEventLogic.fireEvent(mGame, AdvEvent.LOAN_UPCOMING);
            }
        }
    }
    
    protected void doAccounts()
    {
        AcctInfoDlg dlg = new AcctInfoDlg((JFrame)SwingUtilities.getRoot(this), mGame);
        dlg.setModal(true);
        dlg.setVisible(true);
    }

    protected void doTest()
    {
        MoneyLogic.creditToCash(mGame, 10000000, "Cheat cash advance");
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