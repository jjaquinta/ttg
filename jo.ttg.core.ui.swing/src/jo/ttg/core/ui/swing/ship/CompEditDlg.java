package jo.ttg.core.ui.swing.ship;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.ui.swing.TableLayout;
import jo.util.ui.swing.utils.ListenerUtils;

public class CompEditDlg extends JDialog implements PropertyChangeListener
{
	private ShipComponent	mComp;
	private ShipStats		mStats;
	private ShipComponent	mOriginalComp;
	private int				mTechLevel;

	private JButton mOK;
	private JButton mCancel;
	
	private CompValueField[]	mCompValues;

	public CompEditDlg(JFrame parent, ShipComponent comp, ShipStats stats, int tl)
	{
		super(parent, "Edit Component", true);
		mOriginalComp = comp;
		mStats = stats;
		mTechLevel = tl;
		initInstantiate();
		initLink();
		initLayout();
	}

	public CompEditDlg(JDialog parent, ShipComponent comp, ShipStats stats, int tl)
	{
		super(parent, "Edit Component", true);
		mOriginalComp = comp;
		mStats = stats;
		mTechLevel = tl;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		try
        {
            mComp = (ShipComponent)mOriginalComp.clone();
			mComp.set(mOriginalComp);
        }
        catch (CloneNotSupportedException e)
        {
        }
		List<CompValueField> compFields = new ArrayList<>();
		Method[] m = mComp.getClass().getMethods();
		for (int i = 0; i < m.length; i++)
		{
			String name = m[i].getName();
			if (name.startsWith("get"))
				name = name.substring(3);
			else if (name.startsWith("is"))
				name = name.substring(2);
			else
				continue;
			if (name.equals("Children") || name.equals("Parent") || name.equals("Class") || name.equals("UNID"))
				continue;
			if ((!name.equals("Range") && name.endsWith("Range")) || name.endsWith("Description") || name.endsWith("Step"))
				continue;
			CompValueField fld = new CompValueField(mComp, name, mTechLevel);
			if (fld.isValid())
				compFields.add(fld);
			fld.setStats(mStats);
		}
		mCompValues = new CompValueField[compFields.size()];
		compFields.toArray(mCompValues);
		updateValues();
		// controls
		mOK = new JButton("OK");
		mCancel = new JButton("Cancel");
	}
	private void initLayout()
	{
		JPanel buttonBar1 = new JPanel();
		buttonBar1.add(mOK);
		buttonBar1.add(mCancel);
		
		JPanel p = new JPanel();
		p.setLayout(new TableLayout());
		int row = 9;
		for (int i = 0; i < mCompValues.length; i++)
		{
			int thisRow;
			String propName = mCompValues[i].getPropName();
			if (propName.equals("Name"))
				thisRow = 1;
			else if (propName.equals("Section"))
				thisRow = 2;
			else if (propName.equals("Volume"))
				thisRow = 3;
			else if (propName.equals("Power"))
				thisRow = 4;
			else if (propName.equals("Weight"))
				thisRow = 5;
			else if (propName.equals("Price"))
				thisRow = 6;
			else if (propName.equals("TechLevel"))
				thisRow = 7;
			else if (propName.equals("ControlPoints"))
				thisRow = 8;
			else
			{
				thisRow = row;
				row++;
			}
			p.add("1,"+thisRow, new JLabel(mCompValues[i].getLabel()));
			if (mCompValues[i].isReadOnly() && mCompValues[i].isDescriptive())
			{
				mCompValues[i].setDescription(new JLabel());
				p.add("2,"+thisRow+" fill=h", mCompValues[i].getDescription());
			}
			else
			{
				p.add("2,"+thisRow+" fill=h", mCompValues[i]);
				if (mCompValues[i].isDescriptive())
				{
					mCompValues[i].setDescription(new JLabel());
					p.add("3,"+thisRow+" fill=h", mCompValues[i].getDescription());
				}
			}
		}

		getContentPane().setLayout(new BorderLayout());
		getContentPane().add("Center", p);
		getContentPane().add("South", buttonBar1);
		setSize(320, 400);
	}
	private void initLink()
	{
		ListenerUtils.listen(mOK, (ev) -> doOK());
		ListenerUtils.listen(mCancel, (ev) -> doCancel());
		for (int i = 0; i < mCompValues.length; i++)
			mCompValues[i].addPropertyChangeListener("value", this);
		updateValues();
	}

	/**
	 * 
	 */
	protected void doCancel()
	{
		dispose();
	}
	/**
	 * 
	 */
	protected void doOK()
	{
		mOriginalComp.set(mComp);
		dispose();
	}
	public void updateValues()
	{ 
		for (int i = 0; i < mCompValues.length; i++)
			mCompValues[i].updateValues();
	}
	
	public ShipComponent getComp()
	{
		return mComp;
	}

    public void propertyChange(PropertyChangeEvent arg0)
    {
    	Object src = arg0.getSource();
		for (int i = 0; i < mCompValues.length; i++)
			if (src != mCompValues[i])
				mCompValues[i].updateValues();
    }

}
