/*
 * Created on Jan 27, 2005
 *
 */
package ttg.adv.view;

import java.awt.FlowLayout;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import jo.util.ui.swing.utils.FileOpenUtils;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.adv.beans.Game;
import ttg.adv.logic.GameLogic;
import ttg.adv.view.ctrl.AdvButton;

/**
 * @author Jo
 *  
 */
public class AdvToolbar extends JPanel
{
	/**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 2804531289006764241L;

    private AdvFrame	mFrame;
	
    private AdvButton mNew;
    private AdvButton mOpen;
    private AdvButton mSave;
    private AdvButton mSaveAs;
    private AdvButton mExit;

    /**
     *  
     */

    public AdvToolbar(AdvFrame frame)
    {
        mFrame = frame;
        initInstantiate();
        initLink();
        initLayout();
    }

    private void initInstantiate()
    {
        mNew = new AdvButton("tb_new", "New");
        mOpen = new AdvButton("tb_open", "Open");
        mSave = new AdvButton("tb_save", "Save");
        mSaveAs = new AdvButton("tb_save", "Save As");
        mSaveAs.setText("...");
        mExit = new AdvButton("tb_cancel", "Exit");
    }

    private void initLink()
    {
        ListenerUtils.listen(mNew, (e) -> doNew());
        ListenerUtils.listen(mOpen, (e) -> doOpen());
        ListenerUtils.listen(mSave, (e) -> doSave());
        ListenerUtils.listen(mSaveAs, (e) -> doSaveAs());
        ListenerUtils.listen(mExit, (e) -> doExit());
    }

    private void initLayout()
    {
        ((FlowLayout)getLayout()).setAlignment(FlowLayout.LEADING);
        add(mNew);
        add(mOpen);
        add(mSave);
        add(mSaveAs);
        add(mExit);
    }

	/**
	 * 
	 */
	public void doNew()
	{
		Game game = mFrame.getGame();
		if (game.isAnyChange())
		{
			int ret = JOptionPane.showConfirmDialog(mFrame, "Game has changed! Lose all changes?",
				"New Game", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (ret != JOptionPane.YES_OPTION)
				return;
		}
		game = GameLogic.newGame();
		mFrame.setGame(game);
	}

	/**
	 * 
	 */
	public void doOpen()
	{
		if (mFrame.getGame().isAnyChange())
		{
			int ret = JOptionPane.showConfirmDialog(mFrame, "Game has changed! Lose all changes?",
				"Open Game", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (ret != JOptionPane.YES_OPTION)
				return;
		}
		File f = FileOpenUtils.selectFile(this, "Open", "TTG Adventure", ".adv.json");
		if (f == null)
			return;
		mFrame.setGame(GameLogic.open(f));
	}

	/**
	 * 
	 */
	public void doSave()
	{
		Game game = mFrame.getGame();
		if (game.getSaveFile() == null)
			doSaveAs();
		else
			GameLogic.save(game);
	}

	/**
	 * 
	 */
	public void doSaveAs()
	{
		File f = FileOpenUtils.selectFile(this, "Save", "TTG Adventure", ".adv.json");
		if (f == null)
			return;
		mFrame.getGame().setSaveFile(f.toString());
		doSave();
	}

	/**
	 * 
	 */
	public void doExit()
	{
		if (mFrame.getGame().isAnyChange())
		{
			int ret = JOptionPane.showConfirmDialog(mFrame, "Game has changed! Save game first?",
				"Exit Game", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
			if (ret != JOptionPane.YES_OPTION)
				doSave();
		}
		System.exit(0);
	}
}