package ttg.view.war.act;

import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.beans.war.Game;
import ttg.beans.war.GameInst;
import ttg.logic.war.DefaultGame;
import ttg.logic.war.IconLogic;
import ttg.logic.war.SetupLogic;
import ttg.view.war.HelpPanel;
import ttg.view.war.WarButton;
import ttg.view.war.WarPanel;
import ttg.view.war.edit.DlgCustomGame;

public class ChooseGamePanel extends HelpPanel
{
	private List<Game>	mGameList;
	
	private JList<Game> mGames;
	private JButton		mAddCustom;
	private JButton		mOK;
	
	/**
	 *
	 */

	public ChooseGamePanel(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}
	
	public void init()
	{
	}

	private void initInstantiate()
	{
		try
        {
            mGameList = DefaultGame.getInternalGames();
        }
        catch (IOException e)
        {
        	e.printStackTrace();
        }
		mGames = new JList<Game>();
		mGames.setListData(mGameList.toArray(new Game[0]));
		mGames.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mAddCustom = new WarButton("Add Custom", IconLogic.mButtonAdd);
		mAddCustom.setToolTipText("Launch custom game editor");
		mOK = new WarButton("OK", IconLogic.mButtonDone);
	}

	private void initLink()
	{
	    ListenerUtils.listen(mOK, (ev) -> doOK());
        ListenerUtils.listen(mAddCustom, (ev) -> doAddCustom());
        ListenerUtils.change(mGames, (ev) -> doSelect());
	}

	private void initLayout()
	{
		setLayout(new TableLayout("anchor=w"));
		add("1,+ fill=h", makeTitle("Pick game:", "ActionChooseGame.htm"));
		add("1,+ fill=hv weighty=30", new JScrollPane(mGames));
		add("1,+ fill=h", mAddCustom);
		add("1,+ fill=h", mOK);
	}

	protected void doOK()
	{
		Game game = (Game)mGames.getSelectedValue();
		if (game == null)
			return;
		GameInst gameInst;
        try
        {
            gameInst = SetupLogic.newGame(game);
			mPanel.setGame(gameInst);
			mPanel.setMode(WarPanel.CHOOSE_SIDE);
        }
        catch (IOException e)
        {
        }
	}
	
	protected void doSelect()
	{
		mPanel.getInfoPanel().setObject(mGames.getSelectedValue());
	}
	
	protected void doAddCustom()
	{
		DlgCustomGame dlg = null;
		for (Component c = getParent(); c != null; c = ((Container)c).getParent())
			if (c instanceof Frame)
			{
				dlg = new DlgCustomGame((Frame)c);
				break;
			}
			else if (!(c instanceof Container))
				break;
		if (dlg == null)
			return;
		Game game = (Game)mGames.getSelectedValue();
		if (game != null)
			dlg.setText(game.getRawText()); 
		dlg.setVisible(true);
		if (!dlg.isAccepted())
			return;
		String txt = dlg.getText();
		try
		{
			game = DefaultGame.parseGame(new BufferedReader(new StringReader(txt)));
			mGameList.add(game);
			mGames.setListData(mGameList.toArray(new Game[0]));
		}
		catch (IOException e)
		{
		}
	}
}
