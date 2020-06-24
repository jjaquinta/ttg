/*
 * Created on Jan 6, 2005
 *
 */
package ttg.view.adv.ctrl;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import jo.ttg.core.ui.swing.ctrl.CharStatsPanel;
import ttg.beans.adv.CrewBean;
import ttg.logic.adv.CrewLogic;

/**
 * @author Jo
 *
 */
public class CrewStatsPanel extends CharStatsPanel
{
    private JList	mJobQualifications;

    /**
	 * 
	 */
	protected void initInstantiate()
	{
	    super.initInstantiate();
		mJobQualifications = new JList();
	}


	/**
	 * 
	 */
	protected void initLink()
	{
	    super.initLink();
	}

	/**
	 * 
	 */
	protected void initLayout()
	{
	    super.initLayout();
		add("3,8 2x1", new JLabel("Qualifications:"));
		add("4,9 fill=both", new JScrollPane(mJobQualifications));
	}

	/**
	 * @param crew
	 */
	public void setChar(CrewBean crew)
	{
	    super.setChar(crew);
		if (crew == null)
		{
		    mJobQualifications.setListData(new Object[0]);
		}
		else
		{
		    mJobQualifications.setListData(CrewLogic.qualifiedJobs(crew).toArray());		    
		    mJobQualifications.setSelectedValue(CrewLogic.jobNames()[crew.getJob()], false);
		}
	}
	
	public int getSelectedJob()
	{
	    String jobName = (String)mJobQualifications.getSelectedValue();
	    if (jobName == null)
	        return 0;
	    String[] jobNames = CrewLogic.jobNames();
	    for (int i = 1; i < jobNames.length; i++)
	        if (jobNames[i] == null)
	            break;
	        else if (jobNames[i].equals(jobName))
	            return i;
	    return 0;
	}
}
