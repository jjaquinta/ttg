/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;


import javax.swing.JLabel;
import javax.swing.JTextArea;

import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.core.ui.swing.logic.FormatUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyToidsPanel extends PopulatedStatsPanel
{
    private JLabel		mNameLabel;
	private JTextArea	mName;
	private JTextArea	mRockSize;
	private JTextArea	mWidth;
	private JTextArea	mMakeup;
	
	public BodyToidsPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
	    mNameLabel = new JLabel("Name:");
		mName = PopulatedStatsPanel.newJTextArea();
		mRockSize = PopulatedStatsPanel.newJTextArea();
		mWidth = PopulatedStatsPanel.newJTextArea();
		mMakeup = PopulatedStatsPanel.newJTextArea();
	}
	private void initLayout()
	{
		add("1,+ anchor=nw", new JLabel("Rock Size:"));
		add("+,. fill=hv", mRockSize);
		add("1,+ anchor=nw", new JLabel("Width:"));
		add("+,. fill=hv", mWidth);
		add("1,+ anchor=nw", new JLabel("Makeup:"));
		add("+,. fill=hv", mMakeup);
		
		add("1,0 anchor=nw", mNameLabel);
		add("+,. fill=hv", mName);
	}
	private void initLink()
	{
	}
	
	/**
	 * @param bean
	 */
	public void setBody(BodyToidsBean toids)
	{
	    mNameLabel.setIcon(BodyView.getIcon(toids));
		mName.setText(toids.getName());
		mRockSize.setText(FormatUtils.sDistance(toids.getMaxDiam()));
		mWidth.setText(FormatUtils.sDistance(toids.getWidth()));
		mMakeup.setText(
			"N:"+FormatUtils.sPC(toids.getNZone())
			+"M:"+FormatUtils.sPC(toids.getMZone())
			+"C:"+FormatUtils.sPC(toids.getCZone())
		);
		super.setStats(toids.getPopulatedStats());
	}
}
