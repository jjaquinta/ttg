package ttg.view.adv.ctrl;

import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumnModel;

import jo.ttg.core.ui.swing.ship.AddCompDlg;
import jo.ttg.core.ui.swing.ship.AutoDlg;
import jo.ttg.core.ui.swing.ship.CompEditDlg;
import jo.ttg.core.ui.swing.ship.ComponentListModel;
import jo.ttg.core.ui.swing.ship.ComponentTableModel;
import jo.ttg.core.ui.swing.ship.ComponentTreeModel;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.comp.Hull;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.ttg.ship.logic.BlockGen;
import jo.ttg.ship.logic.ComponentChoiceLogic;
import jo.ttg.ship.logic.ShipFormatLogic;
import jo.ttg.ship.logic.ShipModify;
import jo.ttg.ship.logic.ShipReport;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.utils.PCSBeanUtils;
import ttg.view.adv.dlg.ShipInfoDlg;

public class ShipEditPanel extends JPanel
{
	private Object				mParent;
	private ShipBean 			mShip;
	private ShipBean 			mOriginalShip;
	private ShipStats			mStats;
	private ComponentListModel	mCompListModel;
	private ComponentTableModel mCompTableModel;
	private int					mTechLevel;
	
	private JTextField		mName;
	private JComboBox		mTypePrimary;
	private JComboBox		mTypeQualifier;
	private JTextArea		mNotes;
	private JComboBox		mSection;
	private JList			mComponentList;
	private JTable			mComponentTable;
	private JButton 		mAdd;
	private JButton 		mEdit;
	private JButton 		mDelete;
	private JButton 		mReset;
	private JButton 		mAuto;
	private JButton 		mStatsButton;

	public ShipEditPanel(ShipBean ship)
	{
		//super(frame, "Edit Ship", true);
		mOriginalShip = ship;
		initInstantiate();
		initLink();
		initLayout();
	}

	public ShipEditPanel(JFrame frame, ShipBean ship)
	{
		this(ship);
		mParent = frame;
	}

	public ShipEditPanel(JDialog dialog, ShipBean ship)
	{
		this(ship);
		mParent = dialog;
	}

	private void initInstantiate()
	{
		try
        {
            mShip = (ShipBean)mOriginalShip.clone();
        }
        catch (CloneNotSupportedException e)
        {
        }
		mCompListModel = new ComponentListModel(mShip);
		ComponentTreeModel compTreeModel = new ComponentTreeModel(mShip);
		mCompTableModel = new ComponentTableModel(mShip);
		// controls
		mEdit = new JButton("Edit");
		mDelete = new JButton("Delete");
		mAdd = new JButton("Add");
		mReset = new JButton("Reset");
		mAuto = new JButton("Auto");
		mStatsButton = new JButton("Stats");
		mName = new JTextField(mShip.getName());
		mTypePrimary = new JComboBox(ShipFormatLogic.getPrimaryNames());
		if (mShip.getType().length() > 0)
		    mTypePrimary.setSelectedItem(ShipFormatLogic.findPrimaryNameFromCode(mShip.getType().substring(0, 1)));
		else
		    mTypePrimary.setSelectedIndex(0);
		mTypeQualifier = new JComboBox(ShipFormatLogic.getQualifierNames());
		if (mShip.getType().length() > 1)
		    mTypeQualifier.setSelectedItem(ShipFormatLogic.findQualifierNameFromCode(mShip.getType().substring(1, 2)));
		else
		    mTypeQualifier.setSelectedIndex(0);
		mNotes = new JTextArea(mShip.getNotes());
		mNotes.setLineWrap(true);
		String[] choice = new String[ShipComponent.mSectionDescriptions.length + 1];
		choice[0] = "All Sections";
		System.arraycopy(ShipComponent.mSectionDescriptions, 0, choice, 1, ShipComponent.mSectionDescriptions.length);
		mSection = new JComboBox(choice);
		mSection.setSelectedIndex(0);
		mComponentList = new JList(mCompListModel);
		mComponentTable = new JTable(mCompTableModel);
		mComponentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		doReset();
	}
	private void initLayout()
	{
		JPanel buttonBar2 = new JPanel();
		buttonBar2.add(mAdd);
		buttonBar2.add(mEdit);
		buttonBar2.add(mDelete);
		buttonBar2.add(mReset);
		buttonBar2.add(mAuto);
		buttonBar2.add(mStatsButton);
		
		JTabbedPane compView = new JTabbedPane();
		compView.addTab("List", new JScrollPane(mComponentList));
		compView.addTab("Table", new JScrollPane(mComponentTable, 
			JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
			JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED));

		setLayout(new TableLayout());
		add("1,+", new JLabel("Name:"));
		add("+,. 2x1 fill=h", mName);
		add("1,+", new JLabel("Type:"));
		add("+,. fill=h", mTypePrimary);
		add("+,. fill=h", mTypeQualifier);
		add("1,+ 3x1 fill=h", new JLabel("Notes:"));
		add("1,+ 3x1 fill=both", new JScrollPane(mNotes));
		add("1,+ 3x1 fill=h", new JLabel("Components:"));
		add("1,+", new JLabel("Section:"));
		add("+,. 2x1 fill=h", mSection);
		add("1,+ 3x1 fill=both", compView);
		add("1,+ 3x1 fill=h", buttonBar2);
	}
	private void initLink()
	{
		ListenerUtils.listen(mReset, (ev) -> doReset());
		ListenerUtils.listen(mAuto, (ev) -> doAuto());
		ListenerUtils.listen(mAdd, (ev) -> doAdd());
		ListenerUtils.listen(mDelete, (ev) -> doDelete());
		ListenerUtils.listen(mEdit, (ev) -> doEdit());
		ListenerUtils.listen(mStatsButton, (ev) -> doStatsButton());
		ListenerUtils.change(mComponentList, (ev) -> doSelect(mComponentList.getSelectedValue()));
		ListenerUtils.change(mComponentTable, (ev) -> {
				ListSelectionModel lsm = (ListSelectionModel)ev.getSource();
				int row = lsm.getMinSelectionIndex();
				if (row >= 0)
					doSelect(mCompTableModel.getElementAt(row));
			});
		PCSBeanUtils.listen(mShip, "components", (ov,nv) -> doStats());
		ListenerUtils.listen(mSection, (ev) -> doNewSection());
		ListenerUtils.listen(mTypePrimary, (ev) -> doStats());
		ListenerUtils.listen(mTypeQualifier, (ev) -> doStats());
		addMouseListenerToHeaderInTable();
	}
	protected void doSelect(Object object)
    {
	    int idx = mCompListModel.getRowOf(object);
	    mComponentList.setSelectedIndex(idx);
	    idx = mCompTableModel.getRowOf(object);
        mComponentTable.getSelectionModel().setSelectionInterval(idx, idx);
    }
	
	protected void doNewSection()
	{
	    int section = mSection.getSelectedIndex() - 1;
	    mCompListModel.setSectionFilter(section);
	    mCompTableModel.setSectionFilter(section);
	}

    /**
	 * 
	 */
	protected void doDelete()
	{
		Object sel = mComponentList.getSelectedValue();
		if (sel == null)
			return;
		if (sel instanceof ShipComponent)
		    ShipModify.remove(mShip, (ShipComponent)sel);
		else if (sel instanceof ShipBlockBean)
		    ShipModify.remove(mShip, (ShipBlockBean)sel);
		doStats();
	}

	private void doStats()
	{
		mShip.setName(mName.getText());
		mShip.setType(ShipFormatLogic.findCodeFromName(mTypePrimary.getSelectedItem().toString()+" "+mTypeQualifier.getSelectedItem().toString()));
		mShip.setNotes(mNotes.getText());
		firePropertyChange("ship", false, true);
		mStats = ShipReport.report(mShip);
		firePropertyChange("stats", false, true);
	}

	/**
	 * 
	 */
	protected void doEdit()
	{
		Object sel = mComponentList.getSelectedValue();
		if (sel == null)
			return;
		if (!(sel instanceof ShipComponent))
		    return;
		CompEditDlg dlg;
		if (mParent instanceof JFrame)
			dlg = new CompEditDlg((JFrame)mParent, (ShipComponent)sel, mStats, mTechLevel);
		else
			dlg = new CompEditDlg((JDialog)mParent, (ShipComponent)sel, mStats, mTechLevel);
		dlg.show();
		doStats();
	}

	private Hull findHull()
    {
		for (Iterator i = mShip.getComponents().iterator(); i.hasNext(); )
		{
			ShipComponent comp = (ShipComponent)i.next();
			if (comp instanceof Hull)
				return (Hull)comp;
		}
		return null;
    }

    /**
	 * 
	 */
	protected void doAdd()
	{
	    ArrayList compChoice = new ArrayList();
	    compChoice.addAll(ComponentChoiceLogic.getTechLevelComponents(mTechLevel));
	    compChoice.addAll(BlockGen.genBlocks(mTechLevel));
		AddCompDlg dlg;
		if (mParent instanceof JFrame)
			dlg = new AddCompDlg((JFrame)mParent, compChoice);
		else
			dlg = new AddCompDlg((JDialog)mParent, compChoice);
		dlg.show();
		Object obj = dlg.getComp();
		if (obj == null)
			return;
		try
        {
		    if (obj instanceof ShipComponent)
		    {
		        ShipComponent comp = (ShipComponent)((ShipComponent)obj).clone();
	            if (mTechLevel > 0)
					ShipModify.setTechLevel(comp, mTechLevel);
	            else
		            ShipModify.setTechLevel(comp, mStats.getTechLevel());
	            ShipModify.add(mShip, comp);
	            obj = comp;
		    }
		    else if (obj instanceof ShipBlockBean)
		    {
		        ShipBlockBean block = (ShipBlockBean)((ShipBlockBean)obj).clone();
	            ShipModify.add(mShip, block);
	            obj = block;
		    }
			doStats();
			doSelect(obj);
			doEdit();
        }
        catch (CloneNotSupportedException e)
        {
        }
	}

	/**
	 * 
	 */
	protected void doReset()
	{
		ArrayList newComps = new ArrayList();
		for (Iterator i = mOriginalShip.getComponents().iterator(); i.hasNext(); )
		{
		    Object o = i.next();
            try
            {
			    if (o instanceof ShipComponent)
			    {
					ShipComponent ori = (ShipComponent)o;
					Object clone = ori.clone();
					newComps.add(clone);
			    }
			    else if (o instanceof ShipBlockBean)
			    {
			        ShipBlockBean ori = (ShipBlockBean)o;
					Object clone = ori.clone();
					newComps.add(clone);
			    }
            }
            catch (CloneNotSupportedException e)
            {
            }
		}
		ShipModify.replace(mShip, newComps);
		mName.setText(mOriginalShip.getName());
		if (mOriginalShip.getType().length() > 0)
		    mTypePrimary.setSelectedItem(ShipFormatLogic.findPrimaryNameFromCode(mOriginalShip.getType().substring(0, 1)));
		else
		    mTypePrimary.setSelectedIndex(0);
		if (mOriginalShip.getType().length() > 1)
		    mTypeQualifier.setSelectedItem(ShipFormatLogic.findQualifierNameFromCode(mOriginalShip.getType().substring(1, 2)));
		else
		    mTypeQualifier.setSelectedIndex(0);
		mNotes.setText(mOriginalShip.getNotes());
		firePropertyChange("ship", false, true);
		mStats = ShipReport.report(mShip);
		firePropertyChange("stats", false, true);
	}

	/**
	 * 
	 */
	protected void doAuto()
	{
		ArrayList newComps = new ArrayList();
		ShipModify.replace(mShip, newComps);
		AutoDlg dlg = null;
		if (SwingUtilities.getRoot(this) != null)
		    dlg = new AutoDlg((JFrame)SwingUtilities.getRoot(this), mShip);
		else
			return;
		dlg.setModal(true);
		dlg.show();
		firePropertyChange("ship", false, true);
		mStats = ShipReport.report(mShip);
		firePropertyChange("stats", false, true);
		if (mShip.getType().length() > 0)
		    mTypePrimary.setSelectedItem(ShipFormatLogic.findPrimaryNameFromCode(mShip.getType().substring(0, 1)));
		else
		    mTypePrimary.setSelectedIndex(0);
		if (mShip.getType().length() > 1)
		    mTypeQualifier.setSelectedItem(ShipFormatLogic.findQualifierNameFromCode(mShip.getType().substring(1, 2)));
		else
		    mTypeQualifier.setSelectedIndex(0);
	}
	
	protected void doStatsButton()
	{
	    ShipInfoDlg dlg = new ShipInfoDlg((JFrame)SwingUtilities.getRoot(this), null, mStats);
	    dlg.setDisplayErrors(mStats.getErrors().size() > 0);
	    dlg.setModal(true);
	    dlg.show();
	}

	/**
	 * 
	 */
	public void commit()
	{
		mShip.setName(mName.getText());
		mShip.setType(ShipFormatLogic.findCodeFromName(mTypePrimary.getSelectedItem().toString()+" "+mTypeQualifier.getSelectedItem().toString()));
		mShip.setNotes(mNotes.getText());
		mOriginalShip.set(mShip);
	}
	
	private void addMouseListenerToHeaderInTable()
	{
		mComponentTable.setColumnSelectionAllowed(false);
		MouseAdapter listMouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				TableColumnModel columnModel = mComponentTable.getColumnModel();
				int viewColumn = columnModel.getColumnIndexAtX(e.getX());
				int column = mComponentTable.convertColumnIndexToModel(viewColumn);
				if (e.getClickCount() == 1 && column != -1) {
					//System.out.println("Sorting ...");
					int shiftPressed = e.getModifiers()&InputEvent.SHIFT_MASK;
					boolean ascending = (shiftPressed == 0);
					mCompTableModel.sortByColumn(column, ascending);
				}
			}
		};
		JTableHeader th = mComponentTable.getTableHeader();
		th.addMouseListener(listMouseListener);
	}
	/**
	 * @return
	 */
	public ShipBean getOriginalShip()
	{
		commit();
		return mOriginalShip;
	}

	/**
	 * @param bean
	 */
	public void setOriginalShip(ShipBean bean)
	{
		mOriginalShip = bean;
		doReset();
	}

	/**
	 * @return
	 */
	public ShipStats getStats()
	{
		return mStats;
	}

	/**
	 * @param stats
	 */
	public void setStats(ShipStats stats)
	{
		mStats = stats;
	}

	/**
	 * @return
	 */
	public ShipBean getShip()
	{
		return mShip;
	}
	/**
	 * @return
	 */
	public int getTechLevel()
	{
		return mTechLevel;
	}

	/**
	 * @param i
	 */
	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

}