/*
 * Created on Jan 11, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.ctrl;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JLabel;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import ttg.beans.adv.Game;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MoneyPanel extends JLabel implements PropertyChangeListener
{
    private Game	mGame;
    
    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent ev)
    {
        String name = ev.getPropertyName();
        if (name.equals("*") || name.equals("accounts"))
        {
            setText(FormatUtils.sCurrency(mGame.getAccounts().getCash()));
        }
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
