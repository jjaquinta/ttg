/*
 * Created on Dec 19, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.deckplans.ui;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.deckplans.logic.RuntimeLogic;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class DeckerFrame extends JFrame
{
	private DeckerPanel	mClient;
	
	/**
	 *
	 */

	public DeckerFrame()
	{
		super("TTG DeckPlanner");
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mClient = new DeckerPanel();
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
		//setIconImage(IconLogic.mLogoSmall.getImage());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", mClient);
		setSize(800, 600);
	}

	/**
	 * 
	 */
	protected void doFrameShut()
	{
	    RuntimeLogic.term();
		System.exit(0);
	}

	/**
	 * 
	 */
	protected void doFrameStart()
	{
		RuntimeLogic.setMode(RuntimeBean.SCAN);
	}
}
