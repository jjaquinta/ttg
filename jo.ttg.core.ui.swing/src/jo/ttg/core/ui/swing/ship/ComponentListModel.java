/*
 * Created on Dec 18, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ship;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.comp.ShipComponent;
import jo.util.utils.PCSBeanUtils;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComponentListModel extends ComponentBaseModel implements ListModel<ShipComponent>, PropertyChangeListener
{
    private ShipBean	mShip;
	private List<ListDataListener>	mListeners;
	
	public ComponentListModel(ShipBean ship)
	{
	    super(ship.getComponents());
	    mShip = ship;
		mListeners = new ArrayList<>();
		//mShip.addPropertyChangeListener("components", this);
		PCSBeanUtils.listen(mShip, "components", (ov,nv) -> updateComponents());
	}
	
	public ComponentListModel(List<ShipComponent> comps)
	{
	    super(comps);
		mListeners = new ArrayList<>();
	}
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#addListDataListener(javax.swing.event.ListDataListener)
	 */
	public void addListDataListener(ListDataListener arg0)
	{
		synchronized (mListeners)
		{
			mListeners.add(arg0);
		}
	}
	/* (non-Javadoc)
	 * @see javax.swing.ListModel#removeListDataListener(javax.swing.event.ListDataListener)
	 */
	public void removeListDataListener(ListDataListener arg0)
	{
		synchronized (mListeners)
		{
			mListeners.remove(arg0);
		}
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent arg0)
	{
	    if (mListeners == null)
	        return;
		ListDataEvent ev = new ListDataEvent(this, ListDataEvent.CONTENTS_CHANGED, 0, mComponents.size());
		synchronized (mListeners)
		{
			for (ListDataListener l : mListeners)
				l.contentsChanged(ev);
		}
	}
    
    protected void updateComponents()
    {
        super.updateComponents();
        propertyChange(null);
    }
}
