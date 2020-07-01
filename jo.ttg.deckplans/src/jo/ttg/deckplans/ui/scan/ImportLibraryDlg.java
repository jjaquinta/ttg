/*
 * Created on Jan 3, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.deckplans.ui.scan;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import jo.ttg.core.ui.swing.logic.TTGIconUtils;
import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.deckplans.logic.LibraryLogic;
import jo.util.ui.swing.ctrl.ListUtils;
import jo.util.ui.swing.utils.ListenerUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ImportLibraryDlg extends JDialog
{
	private LibEntryBean    mLibrary;
	
	private JTable          mLibraryCtrl;
	private JLabel          mEntryCtrl;
	private JButton			mOK;
	private JButton			mCancel;
	
	/**
	 *
	 */

	public ImportLibraryDlg(JFrame parent)
	{
		super(parent, "Import From Dean Files", Dialog.ModalityType.DOCUMENT_MODAL);
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
        setTitle("Import From Dean Files");
		mOK = new JButton("OK", TTGIconUtils.getIcon("tb_save.gif"));        
		mCancel = new JButton("Cancel");        
		mLibraryCtrl = new JTable(new LibEntryTableModel());
		mEntryCtrl = new JLabel();
	}

	private void initLink()
	{
		ListenerUtils.listen(mOK, (e) -> doOK());
		ListenerUtils.listen(mCancel, (e) -> doRename());
		ListUtils.onSelect(mLibraryCtrl, (ev) -> doUpdateEntry());
	}

    private void initLayout()
	{	    
		JPanel buttonBar = new JPanel();
		buttonBar.add(mOK);
		buttonBar.add(mCancel);
		
		JPanel client = new JPanel();
		client.setLayout(new GridLayout(2, 1));
		client.add(new JScrollPane(mLibraryCtrl));
		client.add(new JScrollPane(mEntryCtrl));
		
		getContentPane().setLayout(new BorderLayout());
        getContentPane().add("North", new JLabel("Select a ship from the Dean Files"));
		getContentPane().add("South", buttonBar);
		getContentPane().add("Center", client);
		setSize(1024, 768);
	}
    
    private void doUpdateEntry()
    {
        int row = mLibraryCtrl.getSelectedRow();
        if (row < 0)
            mLibrary = null;
        else
        {
            int idx = mLibraryCtrl.convertRowIndexToModel(row);
            mLibrary = LibraryLogic.getLibraryEntries().get(idx);
        }
        if (mLibrary == null)
            mEntryCtrl.setText("");
        else
            mEntryCtrl.setText(mLibrary.getHTML());
    }

	protected void doOK()
	{
		dispose();
	}
	
	protected void doRename()
	{
        mLibrary = null;
        dispose();
	}

    public LibEntryBean getLibrary()
    {
        return mLibrary;
    }

    public void setLibrary(LibEntryBean library)
    {
        mLibrary = library;
    }
}
