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

import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.PopulatedStatsPanel;
import jo.util.ui.swing.TableLayout;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyStarPanel extends JPanel
{
	private BodyStarBean	mStar;
	
	private JLabel	mNameLabel;
	private JTextArea	mName;
	private JTextArea	mType;
	private JTextArea	mClass;
	
	public BodyStarPanel()
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
		mClass = PopulatedStatsPanel.newJTextArea();
	}
	private void initLayout()
	{
	    setLayout(new TableLayout());
		add("1,+ anchor=nw", mNameLabel);
		add("+,. fill=hv", mName);
		add("1,+ anchor=nw", new JLabel("Type:"));
		add("+,. fill=hv", mType);
		add("1,+ anchor=nw", new JLabel("Class:"));
		add("+,. fill=hv", mClass);
	}
	private void initLink()
	{
	}
	/**
	 * @param bean
	 */
	public void setBody(BodyStarBean bean)
	{
		mStar = bean;
		mNameLabel.setIcon(BodyView.getIcon(mStar));
		mName.setText(mStar.getName());
		mType.setText(mStar.getStarDecl().getStarTypeDesc());
		mClass.setText(mStar.getStarDecl().getStarClassDesc());
	}
}
