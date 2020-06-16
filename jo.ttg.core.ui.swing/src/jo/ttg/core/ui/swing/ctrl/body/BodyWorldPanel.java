/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl.body;

import javax.swing.JLabel;
import javax.swing.JTextArea;

import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.core.ui.swing.ctrl.BodyView;
import jo.ttg.core.ui.swing.ctrl.PopulatedStatsPanel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyWorldPanel extends PopulatedStatsPanel
{
    private JLabel				mNameLabel;
	private JTextArea			mName;
	
	public BodyWorldPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mNameLabel = new JLabel("Name:");
		mName = PopulatedStatsPanel.newJTextArea();
	}
	private void initLayout()
	{
		add("1,0 anchor=nw", mNameLabel);
		add("+,. fill=hv", mName);
	}
	private void initLink()
	{
	}
	
	/**
	 * @param bean
	 */
	public void setBody(BodyWorldBean bean)
	{
	    mNameLabel.setIcon(BodyView.getIcon(bean));
		mName.setText(bean.getName());
		super.setStats(bean.getPopulatedStats());
	}
}
