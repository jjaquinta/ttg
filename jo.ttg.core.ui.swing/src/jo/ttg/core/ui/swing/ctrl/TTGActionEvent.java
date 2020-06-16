/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.AWTEvent;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class TTGActionEvent extends AWTEvent
{
	public static final int	SELECTED = 0;
	public static final int	ACTIVATED = 1;
	public static final int	HOVER = 2;
	public static final int	DEACTIVATED = 3;
	
	private String	mURI;
	private Object	mObject;
    private Object  mDetail;
	
	public TTGActionEvent(Object source, int id)
	{
		super(source, id);
	}
	
	public TTGActionEvent(Object source, int id, String uri)
	{
		super(source, id);
		mURI = uri;
	}
	
	public TTGActionEvent(Object source, int id, String uri, Object object)
	{
		super(source, id);
		mURI = uri;
		mObject = object;
	}
    
    public TTGActionEvent(Object source, int id, String uri, Object object, Object detail)
    {
        super(source, id);
        mURI = uri;
        mObject = object;
        mDetail = detail;
    }
	/**
	 * @return
	 */
	public Object getObject()
	{
		return mObject;
	}

	/**
	 * @return
	 */
	public String getURI()
	{
		return mURI;
	}

	/**
	 * @param object
	 */
	public void setObject(Object object)
	{
		mObject = object;
	}

	/**
	 * @param string
	 */
	public void setURI(String string)
	{
		mURI = string;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		StringBuffer ret = new StringBuffer("TTGActionEvent[id=");
		switch (getID())
		{
			case SELECTED:
				ret.append("SELECTED");
				break;
			case ACTIVATED:
				ret.append("ACTIVATED");
				break;
			case HOVER:
				ret.append("HOVER");
				break;
			default:
				ret.append(getID());
				break;
		}
		if (mURI != null)
		{
			ret.append(",uri=");
			ret.append(mURI);
		}
		ret.append("]");
		return ret.toString();
	}

    public Object getDetail()
    {
        return mDetail;
    }

    public void setDetail(Object detail)
    {
        mDetail = detail;
    }
}
