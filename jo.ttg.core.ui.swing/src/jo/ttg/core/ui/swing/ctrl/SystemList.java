/*
 * Created on Nov 25, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JList;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodySpecialBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.beans.sys.BodyToidsBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.SystemBean;
import jo.ttg.gen.IGenScheme;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class SystemList extends JList<BodyBean>
{
    public static final int WORLDS = 0x01;
    public static final int TOIDS = 0x02;
    public static final int STARS = 0x04;
    public static final int GIANTS = 0x08;
    public static final int SPECIALS = 0x10;
    
	private IGenScheme	mScheme;
	private OrdBean		mOrigin;
	private SystemBean  mSystem;
	private List<BodyBean>	mList;
	private int			mFilter;
    
    public SystemList()
    {
        mFilter = 0xff;
        setCellRenderer(new SystemListCellRenderer());
        mList = new ArrayList<BodyBean>();
    }
	
	public SystemList(IGenScheme scheme)
	{
	    this();
		mScheme = scheme;
	}
	/**
	 * @return
	 */
	public OrdBean getOrigin()
	{
		return mOrigin;
	}

	/**
	 * @param bean
	 */
	public void setOrigin(OrdBean bean)
	{
		mOrigin = bean;
        SystemBean sys = mScheme.getGeneratorSystem().generateSystem(mOrigin);
        setSystem(sys);
	}

    public void setSelectedValue(BodyBean b)
    {
        if (b == null)
        {
            setSelectedIndex(-1);
            return;
        }
        for (int i = 0; i < mList.size(); i++)
        {
            BodyBean bb = (BodyBean)mList.get(i);
            if (bb.getURI().equals(b.getURI()))
            {
                setSelectedIndex(i);
                break;
            }
        }
    }
    
    public int getFilter()
    {
        return mFilter;
    }
    public void setFilter(int filter)
    {
        mFilter = filter;
    }

    public SystemBean getSystem()
    {
        return mSystem;
    }

    public void setSystem(SystemBean system)
    {
        mSystem = system;
        mList.clear();
        for (Iterator<BodyBean> i = mSystem.getSystemRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = (BodyBean)i.next();
            if ((b instanceof BodyWorldBean) && ((mFilter&WORLDS) != 0))
                mList.add(b);
            else if ((b instanceof BodyToidsBean) && ((mFilter&TOIDS) != 0))
                mList.add(b);
            else if ((b instanceof BodyStarBean) && ((mFilter&STARS) != 0))
                mList.add(b);
            else if ((b instanceof BodyGiantBean) && ((mFilter&GIANTS) != 0))
                mList.add(b);
            else if ((b instanceof BodySpecialBean) && ((mFilter&SPECIALS) != 0))
                mList.add(b);
        }
        setListData(mList.toArray(new BodyBean[0]));
        if (getParent() != null)
            getParent().doLayout();
    }
}
