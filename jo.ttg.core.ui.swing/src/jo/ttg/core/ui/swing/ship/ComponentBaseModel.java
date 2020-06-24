/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ship;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.ship.beans.ShipBlockBean;
import jo.ttg.ship.beans.comp.ShipComponent;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComponentBaseModel
{
    public static final int ANY = -1;
    public static final int COMPONENTS = 0;
    public static final int BLOCKS = 1;
    
    private int			mSectionFilter;
    private int			mPhylumFilter; // -1 = both, 0 = components, 1 = blocks
    protected List<ShipComponent>	mAllComponents;
	protected List<ShipComponent>	mComponents;
	
	public ComponentBaseModel(List<ShipComponent> comps)
	{
		mAllComponents = comps;
		mSectionFilter = -1;
		mPhylumFilter = -1;
		mComponents = new ArrayList<>();
		updateComponents();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getSize()
	 */
	public int getSize()
	{
		return mComponents.size();
	}
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#getElementAt(int)
	 */
	public ShipComponent getElementAt(int row)
	{
	    if ((row < 0) || (row >= mComponents.size()))
	        return null;
		return mComponents.get(row);
	}
	public int getRowOf(Object element)
	{
		return mComponents.indexOf(element);
	}
    /**
     * @return Returns the sectionFilter.
     */
    public int getSectionFilter()
    {
        return mSectionFilter;
    }
    /**
     * @param sectionFilter The sectionFilter to set.
     */
    public void setSectionFilter(int sectionFilter)
    {
        mSectionFilter = sectionFilter;
		updateComponents();
    }
    
    /**
     * @return Returns the phylumFilter.
     */
    public int getPhylumFilter()
    {
        return mPhylumFilter;
    }
    /**
     * @param phylumFilter The phylumFilter to set.
     */
    public void setPhylumFilter(int phylumFilter)
    {
        mPhylumFilter = phylumFilter;
		updateComponents();
    }

    private boolean isRightPhylum(ShipComponent o)
    {
        if (mPhylumFilter < 0)
            return true;
        if ((o instanceof ShipComponent) && (mPhylumFilter != COMPONENTS))
            return false;
        if ((o instanceof ShipBlockBean) && (mPhylumFilter != BLOCKS))
            return false;
        return true;
    }
    
    private boolean isRightSection(ShipComponent obj)
    {
        if (mSectionFilter < 0)
            return true;
        if ((obj instanceof ShipComponent) && (((ShipComponent)obj).getSection() == mSectionFilter))
            return true;
        else if ((obj instanceof ShipBlockBean) && (((ShipBlockBean)obj).getSection() == mSectionFilter))
            return true;
        return false;
    }
    
    protected void updateComponents()
    {
        mComponents.clear();
        if (mSectionFilter < 0)
            mComponents.addAll(mAllComponents);
        else
            for (ShipComponent comp : mAllComponents)
	            if (isRightPhylum(comp) && isRightSection(comp))
	                mComponents.add(comp);
    }
}
