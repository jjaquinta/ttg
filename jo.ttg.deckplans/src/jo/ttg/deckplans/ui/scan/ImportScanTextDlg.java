/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;
import java.awt.Dialog;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.util.ui.swing.utils.ListenerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportScanTextDlg extends JDialog
{
	private String          mTextData;
	
	private JTextArea       mTextCtrl;
	private JButton			mOK;
	private JButton			mCancel;
	
	/**
	 *
	 */

	public ImportScanTextDlg(JFrame parent)
	{
		super(parent, "Import MT Text", Dialog.ModalityType.DOCUMENT_MODAL);
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Ship Info");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCancel = new JButton("Cancel");        
		mTextCtrl = new JTextArea();
		if (mTextData != null)
		    mTextCtrl.setText(mTextData);
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
		ListenerUtils.listen(mCancel, (e) -> doRename());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mCancel);
		
		getContentPane().setLayout(new BorderLayout());
        getContentPane().add("North", new JLabel("Paste in a textual description of the ship in MegaTraveller format"));
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", mTextCtrl);
		setSize(640, 480);
	}

	protected void doOK()
	{
	    mTextData = mTextCtrl.getText();
		dispose();
	}
	
	protected void doRename()
	{
        mTextData = null;
        dispose();
	}

    public String getTextData()
    {
        return mTextData;
    }

    public void setTextData(String textData)
    {
        mTextData = textData;
        if (mTextCtrl != null)
            mTextCtrl.setText(mTextData);
    }
}
