package jo.ttg.core.ui.swing.ship;

import java.awt.BorderLayout;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.ui.swing.utils.ListenerUtils;

public class AddCompDlg extends JDialog
{
	public static int			mLastSection = -1;
	public static int			mLastPhylum = -1;
	private Object				mComp;
	private List<ShipComponent>			mCompChoice;
	private ComponentListModel	mCompModel;

	private JComboBox<String>	mPhylum;
	private JList<String>		mSections;
	private JList<ShipComponent>		mComponents;
	private JButton mOK;
	private JButton mCancel;

	public AddCompDlg(JFrame parent, List<ShipComponent> compChoice)
	{
		super(parent, "Add Component to Ship", true);
		mCompChoice = compChoice;
		initInstantiate();
		initLink();
		initLayout();
	}

	public AddCompDlg(JDialog parent, List<ShipComponent> compChoice)
	{
		super(parent, "Add Component to Ship", true);
		mCompChoice = compChoice;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mCompModel = new ComponentListModel(mCompChoice);
		// controls
		mOK = new JButton("OK");
		mCancel = new JButton("Cancel");
		String[] choice = new String[ShipComponent.mSectionDescriptions.length + 1];
		choice[0] = "All Sections";
		System.arraycopy(ShipComponent.mSectionDescriptions, 0, choice, 1, ShipComponent.mSectionDescriptions.length);
		mSections = new JList<>(choice);
		mPhylum = new JComboBox<>(mPhylumChoices);
		mComponents = new JList<>(mCompModel);
		if (mLastSection >= 0)
		{
			mSections.setSelectedIndex(mLastSection);
			mCompModel.setSectionFilter(mLastSection - 1);
		}
		if (mLastPhylum >= 0)
		{
			mPhylum.setSelectedIndex(mLastPhylum);
			mCompModel.setPhylumFilter(mLastPhylum - 1);
		}
	}
	private void initLayout()
	{
		JPanel leftNav = new JPanel();
		leftNav.setLayout(new BorderLayout());
		leftNav.add("Center", new JScrollPane(mSections));
		leftNav.add("South", mPhylum);

		JPanel buttonBar1 = new JPanel();
		buttonBar1.add(mOK);
		buttonBar1.add(mCancel);

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("West", leftNav);
		getContentPane().add("Center", new JScrollPane(mComponents));
		getContentPane().add("South", buttonBar1);
		setSize(480, 300);
	}
	private void initLink()
	{
	    ListenerUtils.change(mSections, (ev) -> doNewSection());
	    ListenerUtils.listen(mPhylum, (ev) -> doNewPhylum());
	    ListenerUtils.listen(mOK, (ev) -> doOK());
	    ListenerUtils.listen(mCancel, (ev) -> doCancel());
	}

	/**
	 * 
	 */
	protected void doCancel()
	{
		mComp = null;
		dispose();
	}

	private void doNewSection()
	{
		mLastSection = mSections.getSelectedIndex();
		mCompModel.setSectionFilter(mLastSection - 1);
	}

	private void doNewPhylum()
	{
		mLastPhylum = mPhylum.getSelectedIndex();
		mCompModel.setPhylumFilter(mLastPhylum - 1);
	}
	/**
	 * 
	 */
	protected void doOK()
	{
		mComp = mComponents.getSelectedValue();
		dispose();
	}
    public Object getComp()
    {
        return mComp;
    }

    private static final String[] mPhylumChoices =
    {
            "List All",
            "List Components",
            "List Blocks",
    };
}
