/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.adv.dlg;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ReportDlg extends JDialog
{
    private String		mText;
    
	private JButton		mOK;
	private JTextPane	mClient;
	
	/**
	 *
	 */

	public ReportDlg(JFrame parent, String html)
	{
		super(parent);
		mText = html;
		initInstantiate();
		initLink();
		initLayout();
	}

	public ReportDlg(JDialog parent, String html)
	{
		super(parent);
		mText = html;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    int o = mText.indexOf("<title>");
	    if (o >= 0)
	    {
	        String title = mText.substring(o+7);
		    o = title.indexOf("</title>");
		    if (o >= 0)
		    {
		        title = title.substring(0, o);
		        setTitle(title);
		    }
	    }
		mClient = new JTextPane();
		mClient.setContentType("text/html");
		mClient.setText(mText);
		mClient.setEditable(false);
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", new JScrollPane(mClient));
		
		pack();
	}

	protected void doOK()
	{
		dispose();
	}
}
