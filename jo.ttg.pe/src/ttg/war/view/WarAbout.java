package ttg.war.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
public class WarAbout extends JFrame
{
    private JLabel mTitle;
    private JLabel mCopyright;
    private JLabel mVersion;
    private JTextArea mDisclaimer;

    //	public WarAbout()
    //	{
    //        initInstantiate();
    //        initLink();
    //        initLayout();
    //	}

    private void initInstantiate()
    {
        Font f1 = new Font("SansSerif", Font.BOLD, 24);
        mTitle = new JLabel("Pocket Empires");
        mTitle.setFont(f1);
        Font f2 = new Font("SansSerif", Font.BOLD, 16);
        mCopyright = new JLabel("(c) 2004 Sopwith Llama");
        mCopyright.setFont(f2);
        mVersion = new JLabel("Version " + War.VERSION);
        mVersion.setFont(f2);
        mDisclaimer = new JTextArea();
        mDisclaimer.setText(
            "The Traveller game in all forms "
                + "is owned by Far Future Enterprises. "
                + "Copyright 1977 - 2003 Far Future Enterprises. "
                + "Traveller is a registered trademark of Far Future Enterprises. "
                + "Far Future permits web sites and fanzines for this game, "
                + "provided it contains this notice, "
                + "that Far Future is notified, "
                + "and subject to a withdrawal of permission on 90 days notice. "
                + "The contents of this site are for personal, non-commercial use only. "
                + "Any use of Far Future Enterprises's copyrighted material "
                + "or trademarks anywhere on this web site "
                + "and its files should not be viewed as a challenge "
                + "to those copyrights or trademarks. "
                + "In addition, any program/articles/file on this site "
                + "cannot be republished or distributed without the consent "
                + "of the author who contributed it. ");
        mDisclaimer.setLineWrap(true);
        mDisclaimer.setWrapStyleWord(true);
        mDisclaimer.setEditable(false);
    }

    public WarAbout()
    {
        setSize(430, 430);
        initInstantiate();
		setIconImage(IconLogic.mLogoSmall.getImage());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setOpaque(false);
        mTitle.setForeground(Color.white);
        content.add(mTitle);

        mCopyright.setForeground(Color.white);
        content.add(mCopyright);

		mVersion.setForeground(Color.white);
        content.add(mVersion);

		mDisclaimer.setForeground(Color.white);
		mDisclaimer.setBackground(new Color(255, 255, 255, 80));
		content.add(mDisclaimer);

        getContentPane().setLayout(new FlowLayout());
        getContentPane().add(content);
        ((JPanel)getContentPane()).setOpaque(false);

        final ImageIcon m_image = IconLogic.mLogoBackground;
        final int winc = m_image.getIconWidth();
        final int hinc = m_image.getIconHeight();
        JLabel backlabel = new JLabel("");
        if (m_image.getIconWidth() > 0 && m_image.getIconHeight() > 0)
        {
            backlabel = new JLabel()
            {
                public void paintComponent(Graphics g)
                {
                    int w = getParent().getWidth();
                    int h = getParent().getHeight();
                    for (int i = 0; i < h + hinc; i = i + hinc)
                    {
                        for (int j = 0; j < w + winc; j = j + winc)
                        {
                            m_image.paintIcon(this, g, j, i);
                        }
                    }
                }
                public Dimension getPreferredSize()
                {
                    return new Dimension(super.getSize());
                }
                public Dimension getMinimumSize()
                {
                    return getPreferredSize();
                }
            };
        }

        getLayeredPane().add(backlabel, new Integer(Integer.MIN_VALUE));
        backlabel.setBounds(0, 0, 5000, 5000);

        setVisible(true);

		addWindowListener(new WindowAdapter()
		  { public void windowClosing(WindowEvent e) { doFrameShut(); }          	
			public void windowOpened(WindowEvent e) { doFrameStart(); }
		  }
		);

    }
    
	/**
	 * 
	 */
	protected void doFrameStart()
	{
		MidiLogic.playResource("sound/mike.mid", WarAbout.class);
	}

	/**
	 * 
	 */
	protected void doFrameShut()
	{
		MidiLogic.stopPlaying();
	}

}
