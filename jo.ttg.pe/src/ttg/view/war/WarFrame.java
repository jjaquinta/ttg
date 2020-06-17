/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.view.war;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import ttg.logic.war.IconLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class WarFrame extends JFrame
{
	private WarSplash	mSplash;
	private WarPanel	mClient;
	
	/**
	 *
	 */

	public WarFrame(WarSplash splash)
	{
		super("POCKET EMPIRES");
		mSplash = splash;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mClient = new WarPanel();
	}

	private void initLink()
	{
		addWindowListener(new WindowAdapter()
		  { public void windowClosing(WindowEvent e) { doFrameShut(); }          	
			public void windowOpened(WindowEvent e) { doFrameStart(); }
		  }
		);
	}

	private void initLayout()
	{
		setIconImage(IconLogic.mLogoSmall.getImage());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", mClient);
		setSize(800, 600);
	}

	/**
	 * 
	 */
	protected void doFrameShut()
	{
		System.exit(0);
	}

	/**
	 * 
	 */
	protected void doFrameStart()
	{
		mClient.setMode(WarPanel.CHOOSE_GAME);
		mSplash.setVisible(false);
		mSplash.dispose();
	}
}
