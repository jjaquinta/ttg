/*
 * Created on Jan 26, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.adv.view.ctrl;

import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

import jo.ttg.core.ui.swing.logic.FormatUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectedCapacityPanel extends JPanel
{
    private String	mType;
    private double	mQuanSelected;
    private double	mQuanCapacity;
	
	private JLabel	mLabelSelected;
	private JLabel	mLabelCapacity;
	private JLabel	mLabelFree;
	
	/**
	 *
	 */

	public SelectedCapacityPanel(String type)
	{
	    mType = type;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{
		mLabelSelected = new JLabel();
		mLabelCapacity = new JLabel();
		mLabelFree = new JLabel();
	}

	private void initLink()
	{
	}

	private void initLayout()
	{
		add(new JLabel(" Selected:"));
		add(mLabelSelected);
		if (mType.equals("tons"))
		    add(new JLabel(" Capacity:"));
	    else if (mType.equals("currency"))
	        add(new JLabel(" Total:"));
        else
            add(new JLabel(" Capacity:"));
		add(mLabelCapacity);
		if (mType.equals("tons"))
		    add(new JLabel(" Free:"));
	    else if (mType.equals("currency"))
	        add(new JLabel(" After:"));
        else
            add(new JLabel(" Free:"));
		add(mLabelFree);
	}
	
	private void update()
	{
	    double free = mQuanCapacity - mQuanSelected;
	    if (mType.equals("tons"))
	    {
	        mLabelCapacity.setText(FormatUtils.sTons(mQuanCapacity));
	        mLabelFree.setText(FormatUtils.sTons(free));
	        mLabelSelected.setText(FormatUtils.sTons(Math.abs(mQuanSelected)));
	    }
	    else if (mType.equals("currency"))
	    {
	        mLabelCapacity.setText(FormatUtils.sCurrency(mQuanCapacity));
	        mLabelFree.setText(FormatUtils.sCurrency(free));
	        mLabelSelected.setText(FormatUtils.sCurrency(Math.abs(mQuanSelected)));
	    }
	    else if (mType.equals("int"))
	    {
	        mLabelCapacity.setText(String.valueOf((int)mQuanCapacity));
	        mLabelFree.setText(String.valueOf((int)free));
	        mLabelSelected.setText(String.valueOf((int)Math.abs(mQuanSelected)));
	    }
	    else
	    {
	        mLabelCapacity.setText(String.valueOf(mQuanCapacity));
	        mLabelFree.setText(String.valueOf(free));
	        mLabelSelected.setText(String.valueOf(Math.abs(mQuanSelected)));
	    }
	    if (free < 0)
	        mLabelFree.setForeground(Color.RED);
        else
            mLabelFree.setForeground(Color.BLACK);
	}

	public double getQuanCapacity()
	{
	    return mQuanCapacity;
	}
	public void setQuanCapacity(double quanCapacity)
	{
	    mQuanCapacity = quanCapacity;
	    update();
	}
	public double getQuanSelected()
	{
	    return mQuanSelected;
	}
	public void setQuanSelected(double quanSelected)
	{
	    mQuanSelected = quanSelected;
	    update();
	}
}
