package ttg.view.war;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

import jo.util.utils.DebugUtils;
import ttg.logic.war.IconLogic;

public class HelpFrame extends JFrame implements HyperlinkListener
{
	private static String 	HELP_PROTOCOL = "ttg";
	private static String 	HELP_HOST = "ttg_war";
	private static int 		HELP_PORT = 0;
	private static String 	HELP_ROOT = "ttg/view/war/help/";

	private WarPanel	mPanel;
	private JEditorPane mBrowser;
	private JButton		mHome;
	private JButton		mBack;
	private JButton		mForward;
	
	private ArrayList	mHistory;
	private int			mHistoryAt;
	
	/**
	 *
	 */

	public HelpFrame(WarPanel panel)
	{
		super("POCKET EMPIRES - Help");
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		HELP_PROTOCOL = System.getProperty("ttg.war.help.protocol", HELP_PROTOCOL);
		HELP_HOST = System.getProperty("ttg.war.help.host", HELP_HOST);
		HELP_PORT = Integer.parseInt(System.getProperty("ttg.war.help.port", String.valueOf(HELP_PORT)));
		HELP_ROOT = System.getProperty("ttg.war.help.root", HELP_ROOT);
		
		mHistory = new ArrayList();
		mHistoryAt = 0;
		mBrowser = new JEditorPane();
		mBrowser.setEditable(false);
		mBrowser.setContentType("text/html");
		mHome = new JButton("Home");
		mBack = new JButton("Back", IconLogic.mButtonReverse);
		mForward = new JButton("Forward", IconLogic.mButtonForward);
	}

    private void initLink()
	{
		addWindowListener(new WindowAdapter()
		  { public void windowClosing(WindowEvent e) { doFrameShut(); }          	
			public void windowOpened(WindowEvent e) { doFrameStart(); }
		  }
		);
		mHome.addActionListener(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				doHome();
			}
		});
		mBack.addActionListener(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				doBack();
			}
		});
		mForward.addActionListener(new AbstractAction()
		{
			public void actionPerformed(ActionEvent e)
			{
				doForward();
			}
		});
		mBrowser.addHyperlinkListener(this);
	}

	private void initLayout()
	{
		JPanel buttonBar = new JPanel();
		buttonBar.add(mHome);
		buttonBar.add(mBack);
		buttonBar.add(mForward);
		
		setIconImage(IconLogic.mLogoSmall.getImage());
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", new JScrollPane(mBrowser));
		getContentPane().add("South", buttonBar);
		setSize(640, 480);
	}

	/**
	 * 
	 */
	protected void doFrameShut()
	{
	}

	/**
	 * 
	 */
	protected void doFrameStart()
	{
	}

	public void doHome()
	{
		showHelp("index.htm");
	}
	
	private void doBack()
	{
		if (mHistoryAt > 1)
		{
			showPage((String)mHistory.get(--mHistoryAt - 1));
			updateEnabled();
		}
	}

	private void doForward()
	{
		if (mHistoryAt < mHistory.size())
		{
			showPage((String)mHistory.get(mHistoryAt++));
			updateEnabled();
		}
	}

	public void showHelp(String helpFile)
	{
		mHistory.clear();
		mHistory.add(helpFile);
		mHistoryAt = 1;
		updateEnabled();
		showPage(helpFile);
	}
	
	private void showPage(String helpFile)
	{
		try
		{
			URL url = new URL(HELP_PROTOCOL, HELP_HOST, HELP_PORT, HELP_ROOT+helpFile);
			mBrowser.setPage(url);
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}

    public void hyperlinkUpdate(HyperlinkEvent ev)
    {
		if (ev.getEventType() == HyperlinkEvent.EventType.ACTIVATED)
		{
			JEditorPane pane = (JEditorPane)ev.getSource();
			if (ev instanceof HTMLFrameHyperlinkEvent)
			{
				HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent)ev;
				HTMLDocument doc = (HTMLDocument)pane.getDocument();
				doc.processHTMLFrameHyperlinkEvent(evt);
			}
			else
			{
				try
				{
					pane.setPage(ev.getURL());
				}
				catch (Exception t)
				{
					DebugUtils.debug("Error following hyperlink event="+ev.getURL());
					t.printStackTrace();
				}
			}
			addToHistory(ev.getURL().getPath().substring(18));
		}
    }
    
    private void addToHistory(String history)
    {
    	if (mHistory.size() > mHistoryAt)
    	{
    		String next = (String)mHistory.get(mHistoryAt);
    		if (next.equals(history))
    			mHistoryAt++;
    		else
    		{
    			while (mHistory.size() > mHistoryAt)
    				mHistory.remove(mHistoryAt);
    			mHistory.add(history);
    			mHistoryAt++;
    		}
    	}
    	else
    	{
    		if (mHistoryAt > 0)
    		{
    			String prev = (String)mHistory.get(mHistoryAt - 1);
    			if (prev.equals(history))
		   			mHistoryAt--;
		   		else
		   		{
		   			mHistory.add(history);
		   			mHistoryAt++;
		   		}
    		}
			else
			{
				mHistory.add(history);
				mHistoryAt++;
			}
    	}
    	updateEnabled();
    }
    
    private void updateEnabled()
    {
    	mBack.setEnabled(mHistoryAt > 1);
    	mForward.setEnabled(mHistoryAt < mHistory.size());
    }
}
