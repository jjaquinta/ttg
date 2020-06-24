/*
 * Created on Dec 16, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.core.ui.swing.ship;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.comp.ShipComponent;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ComponentTreeModel implements TreeModel, PropertyChangeListener
{
	public static final String	ROOT = "Root";
	
	private ShipBean	mShip;
	private List<ShipComponent>[]	mSectionComps;
	private List<TreeModelListener>		mListeners;
	
	public ComponentTreeModel(ShipBean ship)
	{
		mShip = ship;
		mListeners = new ArrayList<>();
		resetCount();
		mShip.addPropertyChangeListener("components", this);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getRoot()
	 */
	public Object getRoot()
	{
		return ROOT;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChildCount(java.lang.Object)
	 */
	public int getChildCount(Object arg0)
	{
		if (arg0 == ROOT)
			return ShipComponent.mSectionDescriptions.length;
		if (arg0 instanceof String)
		{
			for (int i = 0; i < ShipComponent.mSectionDescriptions.length; i++)
				if (arg0 == ShipComponent.mSectionDescriptions[i])
				{
					ensureCount(i);
					return mSectionComps[i].size();
				}
			return 0;
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#isLeaf(java.lang.Object)
	 */
	public boolean isLeaf(Object arg0)
	{
		return !(arg0 instanceof String);
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#addTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void addTreeModelListener(TreeModelListener arg0)
	{
		synchronized (mListeners)
		{
			mListeners.add(arg0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#removeTreeModelListener(javax.swing.event.TreeModelListener)
	 */
	public void removeTreeModelListener(TreeModelListener arg0)
	{
		synchronized (mListeners)
		{
			mListeners.remove(arg0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getChild(java.lang.Object, int)
	 */
	public Object getChild(Object arg0, int arg1)
	{
		if (arg0 == ROOT)
			return ShipComponent.mSectionDescriptions[arg1];
		if (arg0 instanceof String)
		{
			for (int i = 0; i < ShipComponent.mSectionDescriptions.length; i++)
				if (arg0 == ShipComponent.mSectionDescriptions[i])
				{
					ensureCount(i);
					return mSectionComps[i].get(arg1);
				}
			return null;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#getIndexOfChild(java.lang.Object, java.lang.Object)
	 */
	public int getIndexOfChild(Object arg0, Object arg1)
	{
		if (arg0 == ROOT)
		{
			for (int i = 0; i < ShipComponent.mSectionDescriptions.length; i++)
				if (arg0 == ShipComponent.mSectionDescriptions[i])
					return i;
			return -1;
		}
		if (arg0 instanceof String)
		{
			for (int i = 0; i < ShipComponent.mSectionDescriptions.length; i++)
				if (arg0 == ShipComponent.mSectionDescriptions[i])
				{
					ensureCount(i);
					return mSectionComps[i].indexOf(arg1);
				}
			return -1;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see javax.swing.tree.TreeModel#valueForPathChanged(javax.swing.tree.TreePath, java.lang.Object)
	 */
	public void valueForPathChanged(TreePath arg0, Object arg1)
	{
	}
	
	private void ensureCount(int section)
	{
		if (mSectionComps[section] != null)
			return;
		mSectionComps[section] = new ArrayList<>();
		for (ShipComponent c : mShip.getComponents())
			if (c.getSection() == section)
				mSectionComps[section].add(c);
	}
	
	@SuppressWarnings("unchecked")
    private void resetCount()
	{
		mSectionComps = new ArrayList[ShipComponent.mSectionDescriptions.length];
		Object[] path = new Object[1];
		path[0] = ROOT;
		TreeModelEvent ev = new TreeModelEvent(this, path);
		synchronized (mListeners)
		{
			for (TreeModelListener l : mListeners)
				l.treeStructureChanged(ev);
		}
	}

    public void propertyChange(PropertyChangeEvent arg0)
    {
        resetCount();        
    }
}
