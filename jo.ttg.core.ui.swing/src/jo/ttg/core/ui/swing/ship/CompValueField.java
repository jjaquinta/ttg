package jo.ttg.core.ui.swing.ship;

import java.awt.BorderLayout;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import jo.ttg.core.ui.swing.logic.FormatUtils;
import jo.ttg.ship.beans.ShipStats;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.ui.swing.utils.FocusUtils;
import jo.util.ui.swing.utils.ListenerUtils;

public class CompValueField extends JPanel
{
	private ShipComponent		mComp;
	private ShipStats			mStats;
	private boolean				mReadOnly;
	private boolean				mDescriptive;
	private boolean				mValid;
	private String				mLabel;
	private String				mPropName;
	private int					mValueType;
	private int					mTechLevel;
	
	private JLabel				mValueFixed;
	private JSpinner			mValueRanged;
	private JTextField			mValueOpen;
	
	private JLabel				mDescription;
	
	private Method 				mReadMethod;
	private Method 				mWriteMethod;
	private Method 				mDescMethod;
	private Method 				mRangeMethod;
	private Method 				mStepMethod;
	private SpinnerNumberModel 	mRangeModel;
	private double				mRangeDoubleMin;
	private double				mRangeDoubleMax;
	private double				mRangeDoubleStep;
	private int					mRangeIntMin;
	private int					mRangeIntMax;
	private int					mRangeIntStep;
	private static final int	R_INT = 0;
	private static final int	R_DOUBLE = 1;
	private static final int	R_STRING = 2;
	
    /**
     *
     */

    public CompValueField(ShipComponent comp, String name, int tl)
    {
    	mComp = comp;
    	mPropName = name;
    	mTechLevel = tl;
    	mValid = false;
    	mDescriptive = false;
    	try
    	{
			initInstantiate();
			initLink();
			initLayout();
			mValid = true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
    }
    private void initInstantiate()
    {
		String readMethodName = "get"+mPropName;
		String readMethodName2 = "is"+mPropName;
		String writeMethodName = "set"+mPropName;
		String rangeMethodName = "get"+mPropName+"Range";
		String stepMethodName = "get"+mPropName+"Step";
		String descMethodName = "get"+mPropName+"Description";
		Method[] m = mComp.getClass().getMethods();
		for (int i = 0; i < m.length; i++)
		{
			String name = m[i].getName();
			if (name.equals(readMethodName))
			{
				if ((mReadMethod == null) || (m[i].getParameterTypes().length > 0))
					mReadMethod = m[i];
			}
			else if (name.equals(readMethodName2))
				mReadMethod = m[i];
			else if (name.equals(writeMethodName))
				mWriteMethod = m[i];
			else if (name.equals(rangeMethodName))
				mRangeMethod = m[i];
			else if (name.equals(descMethodName))
				mDescMethod = m[i];
			else if (name.equals(stepMethodName))
				mStepMethod = m[i];
		}
		if (mReadMethod == null)
		{
			System.out.println("!!! No read method "+readMethodName);
		}
        mReadOnly = (mWriteMethod == null);
		try
        {
            Field labelField = mComp.getClass().getField(mPropName+"Label");
            Object val = labelField.get(mComp);
            mLabel = val.toString();
        }
        catch (Exception e)
        {
        	mLabel = mPropName;
        }
        capitalizeLabel();
        if (mReadOnly)
        {	// fixed
        	mValueFixed = new JLabel();
        }
        else
        {
			try
			{
				Object val = mRangeMethod.invoke(mComp, new Object[0]);
				Object step;
				if (mStepMethod != null)
					step = mStepMethod.invoke(mComp, new Object[0]);
				else
					step = null;
				if (val instanceof double[])
				{
					mRangeDoubleMin = ((double[])val)[0];
					mRangeDoubleMax = ((double[])val)[1];
					if (step != null)
						mRangeDoubleStep = ((Double)step).doubleValue();
					else
						mRangeDoubleStep = 1.0;
					mRangeModel = new SpinnerNumberModel(mRangeDoubleMin, mRangeDoubleMin, mRangeDoubleMax, mRangeDoubleStep);
					mValueType = R_DOUBLE;
				}
				else if (val instanceof int[])
				{
					mRangeIntMin = ((int[])val)[0];
					mRangeIntMax = ((int[])val)[1];
					if (mPropName.equals("TechLevel") && (mTechLevel >= 0) && (mTechLevel < mRangeIntMax))
						mRangeIntMax = mTechLevel;
					if (step != null)
						mRangeIntStep = ((Integer)step).intValue();
					else
						mRangeIntStep = 1;
					mRangeModel = new SpinnerNumberModel(mRangeIntMin, mRangeIntMin, mRangeIntMax, mRangeIntStep);
					mValueType = R_INT;
				}
				else
					throw new NullPointerException();
				mValueRanged = new JSpinner(mRangeModel);
			}
			catch (Exception e)
			{
				mValueOpen = new JTextField();
				String typeName = mReadMethod.getReturnType().getName();
				if (typeName.equals("double"))
					mValueType = R_DOUBLE;
				else if (typeName.equals("int"))
					mValueType = R_INT;
				else if (typeName.equals("java.lang.String"))
					mValueType = R_STRING;
				else
				{
					mValueType = -1;
				}
			}
        }
        mDescriptive = (mDescMethod != null);
    }
    private void initLink()
    {
    	if (mValueRanged != null)
    	    ListenerUtils.change(mValueRanged, (ev) -> doRangeChange());
		else if (mValueOpen != null)
		    FocusUtils.focusLost(mValueOpen, (ev) -> doOpenChange());
    }
    private void initLayout()
    {
        setLayout(new BorderLayout());
        if (mValueFixed != null)
        	add("Center", mValueFixed);
		else if (mValueOpen != null)
			add("Center", mValueOpen);
		else
			add("Center", mValueRanged);
    }
	private void doOpenChange()
	{
		try
		{
			String newText = mValueOpen.getText();
			Object[] args = new Object[mWriteMethod.getParameterTypes().length];
			if (mValueType == R_DOUBLE)
			{
				if (mPropName.equals("Volume") && newText.endsWith("t"))
				{
					double tons = FormatUtils.atod(newText)*13.5;
					args[0] = new Double(tons);
				}
				else
					args[0] = new Double(FormatUtils.atod(newText));
			}
			else if (mValueType == R_INT)
				args[0] = new Integer(newText);
			else if (mValueType == R_STRING)
				args[0] = newText;
			if (args.length > 1)
				args[1] = mStats;
			mWriteMethod.invoke(mComp, args);
			firePropertyChange("value", null, null);
		}
		catch (Exception e)
		{
		}
		updateValues();
	}
	private void doRangeChange()
	{
		try
		{
			Object[] args = new Object[mWriteMethod.getParameterTypes().length];
			args[0] = mRangeModel.getValue();
			if (args.length > 1)
				args[1] = mStats;
			mWriteMethod.invoke(mComp, args);
			firePropertyChange("value", null, null);
		}
		catch (Exception e)
		{
		}
		updateValues();
	}
	private void capitalizeLabel()
	{
		StringBuffer sb = new StringBuffer();
		char[] c = mLabel.toCharArray();
		for (int i = 0; i < c.length; i++)
			if (i == 0)
				sb.append(Character.toUpperCase(c[i]));
			else
			{
				if (Character.isUpperCase(c[i]))
					sb.append(' ');
				sb.append(c[i]);   
			}
		sb.append(':');
		mLabel = sb.toString();
	}
	public void updateValues()
	{
		try
        {
        	Object[] args = new Object[mReadMethod.getParameterTypes().length];
        	if (args.length > 0)
        		args[0] = mStats;
            Object val = mReadMethod.invoke(mComp, args);
            if (mValueFixed != null)
				mValueFixed.setText(val.toString());
            else if (mValueOpen != null)
            {
            	if (!val.toString().equals(mValueOpen.getText()))
            	{
					mValueOpen.setText(val.toString());
					firePropertyChange("value", null, null);
				}
			}
			else
			{
				Object range = mRangeMethod.invoke(mComp, new Object[0]);
				if (mValueType == R_DOUBLE)
				{
					double spinVal = ((Double)(mRangeModel.getValue())).doubleValue();
					double min = ((double[])range)[0];
					double max = ((double[])range)[1];
					double objVal = ((Double)val).doubleValue();
					if (objVal < min)
						objVal = min;
					else if (objVal > max)
						objVal = max;
					if (objVal != spinVal)
					{
						mRangeModel.setValue(new Double(objVal));
						firePropertyChange("value", objVal, spinVal);
					} 
					if (mRangeDoubleMin != min)
						mRangeModel.setMinimum(new Double(min));
					if (mRangeDoubleMax != max)
						mRangeModel.setMaximum(new Double(max));
				}
				else if (mValueType == R_INT)
				{
					int spinVal = ((Integer)(mRangeModel.getValue())).intValue();
					int min = ((int[])range)[0];
					int max = ((int[])range)[1];
					if (mPropName.equals("TechLevel") && (mTechLevel >= 0) && (mTechLevel < max))
						max = mTechLevel;
					int objVal = ((Integer)val).intValue();
					if (objVal < min)
						objVal = min;
					else if (objVal > max)
						objVal = max;
					if (objVal != spinVal)
					{
						mRangeModel.setValue(new Integer(objVal));
						firePropertyChange("value", objVal, spinVal);
					} 
					if (mRangeIntMin != min)
						mRangeModel.setMinimum(new Integer(min));
					if (mRangeIntMax != max)
						mRangeModel.setMaximum(new Integer(max));
				}
			}
            updateDescription();
        }
        catch (IllegalArgumentException e)
        {
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        } 
	}
    private void updateDescription()
        throws IllegalAccessException, InvocationTargetException
    {
        if (mDescriptive && (mDescription != null))
        {
        	Object desc = mDescMethod.invoke(mComp, new Object[0]);
        	if (desc != null)
        		mDescription.setText(desc.toString());
        }
    }
    public boolean isReadOnly()
    {
        return mReadOnly;
    }

    public boolean isValid()
    {
        return mValid;
    }
    public String getLabel()
    {
        return mLabel;
    }
    public boolean isDescriptive()
    {
        return mDescriptive;
    }

    public JLabel getDescription()
    {
        return mDescription;
    }

    public void setDescription(JLabel label)
    {
        mDescription = label;
		try
        {
            updateDescription();
        }
        catch (IllegalAccessException e)
        {
        }
        catch (InvocationTargetException e)
        {
        }
    }

    public void setStats(ShipStats s)
    {
        mStats = s;
    }
    public String getPropName()
    {
        return mPropName;
    }

}
