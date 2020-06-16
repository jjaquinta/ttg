/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.PopulatedStatsPanel;
import jo.util.ui.swing.TableLayout;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyGiantPanel extends JPanel
{
	BodyGiantBean	mGiant;	
	
	private JLabel	mNameLabel;
	private JTextArea	mName;
	private JTextArea	mType;
	
	public BodyGiantPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mNameLabel = new JLabel("Name:");
		mName = PopulatedStatsPanel.newJTextArea();
		mType = PopulatedStatsPanel.newJTextArea();
	}

	private void initLayout()
	{
		setLayout(new TableLayout());
		add("1,+ anchor=nw", mNameLabel);
		add("+,. fill=hv", mName);
		add("1,+ anchor=nw", new JLabel("Type:"));
		add("+,. fill=hv", mType);
	}
	private void initLink()
	{
	}

	/**
	 * @param bean
	 */
	public void setBody(BodyGiantBean bean)
	{
		mGiant = bean;
		mNameLabel.setIcon(BodyView.getIcon(mGiant));
		mName.setText(mGiant.getName());
		if (mGiant.getSize() == BodyGiantBean.GS_S)
			mType.setText("Small");
		else
			mType.setText("Large");
	}
}
