package ttg.war.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import jo.util.logic.MidiLogic;
import ttg.war.logic.IconLogic;

/**
 * @author jgrant
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class WarSplash extends JWindow
{
    private JLabel	mIcon;
    private JLabel	mTitle;
    private JLabel	mCopyright;
	private JLabel	mVersion;
	private JLabel	mDisclaimer;

	public WarSplash()
	{
        initInstantiate();
        initLink();
        initLayout();
	}
    
    private void initInstantiate()
    {
    	mIcon = new JLabel(IconLogic.mLogoLarge);
    	Font f1 = new Font("SansSerif", Font.BOLD, 24);
    	mTitle = new JLabel("Pocket Empires");
    	mTitle.setFont(f1);
    	Font f2 = new Font("SansSerif", Font.BOLD, 16);
    	mCopyright = new JLabel("(c) 2004 Sopwith Llama");
		mCopyright.setFont(f2);
		mVersion = new JLabel("Version "+War.VERSION);
		mVersion.setFont(f2);
		mDisclaimer = new JLabel("The Traveller game in all forms " +			"is owned by Far Future Enterprises. " +			"Copyright 1977 - 1998 Far Future Enterprises.");
		Font f3 = new Font("SansSerif", Font.BOLD, 9);
		mDisclaimer.setFont(f3);
    }

    private void initLink()
    {
		addWindowListener(new WindowAdapter()
		  { public void windowClosed(WindowEvent e) { doFrameShut(); }          	
			public void windowOpened(WindowEvent e) { doFrameStart(); }
		  }
		);
    }       

	private void initLayout()
    {
    	mTitle.setHorizontalAlignment(SwingConstants.CENTER);
		mVersion.setHorizontalAlignment(SwingConstants.CENTER);
		mCopyright.setHorizontalAlignment(SwingConstants.CENTER);
    	
		getContentPane().setBackground(Color.BLACK);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add("Center", mIcon);
        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.Y_AXIS));
        p1.add(mTitle);
		p1.add(mCopyright);
		p1.add(mVersion);
		p1.add(mDisclaimer);
		p1.setBackground(Color.WHITE);
        getContentPane().add("South", p1);
        Border border1 = BorderFactory.createRaisedBevelBorder();
        Border border2 = BorderFactory.createLineBorder(Color.GRAY, 3);
        Border border = BorderFactory.createCompoundBorder(border2, border1);
        ((JComponent)getContentPane()).setBorder(border);
		mCopyright.setForeground(Color.WHITE);
		mVersion.setForeground(Color.WHITE);
		mTitle.setForeground(Color.WHITE);
		mDisclaimer.setForeground(Color.WHITE);
		p1.setBackground(Color.BLACK);
		p1.setForeground(Color.WHITE);
		setForeground(Color.WHITE);
        pack();
        setLocation(320, 200);

    }
    
	/**
	 * 
	 */
	protected void doFrameStart()
	{
		MidiLogic.playResource("sound/mike.mid", WarSplash.class);
	}

	/**
	 * 
	 */
	protected void doFrameShut()
	{
		MidiLogic.stopPlaying();
	}
}
