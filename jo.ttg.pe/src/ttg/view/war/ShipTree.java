/*
 * Created on Mar 29, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.view.war;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import jo.util.ui.swing.utils.ListenerUtils;
import jo.util.ui.swing.utils.MouseUtils;
import jo.util.utils.ArrayUtils;
import ttg.beans.war.ShipInst;
import ttg.logic.war.IconLogic;
import ttg.logic.war.ShipLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ShipTree extends JPanel implements TreeSelectionListener, PropertyChangeListener
{
	private WarPanel	mPanel;
	private String		mRootLabel;
	private List<TreeSelectionListener>	mTreeListeners;
	private boolean		mInfoOnSelect;
	private boolean		mInfoOnClick;
	private boolean		mShipsOnSelect;
	
	private JTree		mShipTree;
	private JButton		mDock;
	private JButton		mUnDock;
	private JButton		mAutoDock;
	private JButton		mFuel;

	private ShipInst	mShip;	
	private List<ShipInst>	mShips;
	private List<ShipInst>	mRootShips;
	private ShipNode	mTreeRoot;
	private boolean		mEclipse;
	
	/**
	 *
	 */

	public ShipTree(WarPanel panel)
	{
		mPanel = panel;
		initInstantiate();
		initLink();
		initLayout();
	}

	private void initInstantiate()
	{		
		mShips = new ArrayList<>();
		mRootShips = new ArrayList<>();
		mRootLabel = "Ships:";
		mTreeListeners = new ArrayList<>();
		mInfoOnSelect = true;
		mInfoOnClick = false;
		
		mShipTree = new JTree()/* {
			public String getSelectedShipList()
			{
				System.out.println("gettingSelectedShipList");
				StringBuffer sb = new StringBuffer();
				ShipInst[] ships = getSelectedShips();
				sb.append("ShipInst");
				for (int i = 0; i < ships.length; i++)
				{
					sb.append(",");
					sb.append(ships[i].hashCode());
				}
				System.out.println("SelectedShipList="+sb.toString());
				return sb.toString();
			}
			
			public void setSelectedShipList(String str)
			{
			}
		}*/;
		mShipTree.setCellRenderer(new ShipNodeRenderer());
		mShipTree.setDragEnabled(true);
		//mShipTree.setTransferHandler(new TransferHandler("selectedShipList"));
		mDock = new WarButton(IconLogic.mButtonDock);
		mDock.setToolTipText("Dock selected ships together");
		mUnDock = new WarButton(IconLogic.mButtonUnDock);
		mUnDock.setToolTipText("Undock selected ship from mothership");
		mAutoDock = new WarButton("A");
		mAutoDock.setToolTipText("AutoDock");
		mFuel = new WarButton(IconLogic.mButtonFuel);
		mFuel.setToolTipText("Transfer Fuel between selected ships");
	}

	private void initLink()
	{
		ListenerUtils.listen(mDock, (ev) -> doDock());
		ListenerUtils.listen(mUnDock, (ev) -> doUnDock());
		ListenerUtils.listen(mAutoDock, (ev) -> doAutoDock());
		ListenerUtils.listen(mFuel, (ev) -> doFuel());
		mShipTree.addTreeSelectionListener(this);
		MouseUtils.mouseClicked(mShipTree, (ev) -> {
				if (ev.getClickCount() == 2) doShipClick(ev.getX(), ev.getY());			
			});
	}

	private void initLayout()
	{
		JPanel p1 = new JPanel();
		p1.add(mDock);
		p1.add(mUnDock);
		//p1.add(mAutoDock);
		p1.add(mFuel);
		setLayout(new BorderLayout());
		add("Center", new JScrollPane(mShipTree));
		add("South", p1);
	}
	
	public void init(ShipInst ship)
	{
		mShip = ship;
		mShips.clear();
		if (ship != null)
		{
			//DebugLogic.debug(getName()+", connect to ships");
			mPanel.getInfoPanel().getPCS().addPropertyChangeListener("ships", this);
		}
		updateTree();
	}
	
	public void init(List<ShipInst> ships)
	{
		mShip = null;
		mShips.clear();
		mShips.addAll(ships);
		if (ships.size() > 0)
		{
			//DebugLogic.debug(getName()+", Connect to ships");
			mPanel.getInfoPanel().getPCS().addPropertyChangeListener("ships", this);
		}
		updateTree();
	}
	
	public void done()
	{
		//DebugLogic.debug(getName()+", disonnect from ships");
		mPanel.getInfoPanel().getPCS().removePropertyChangeListener("ships", this);
	}
	
	public void updateTree()
	{
		mRootShips.clear();
		for (ShipInst ship : mShips)
			if (ship.getContainedBy() == null)
				mRootShips.add(ship);
		if (mShip == null)
			mTreeRoot = new ShipNode(null, mRootShips);
		else
			mTreeRoot = new ShipNode(null, mShip);
		mTreeRoot.setRootLabel(mRootLabel);
		mShipTree.setModel(new DefaultTreeModel(mTreeRoot));
	}
	
	public ShipInst[] getSelectedShips()
	{
		TreePath[] shipPaths = mShipTree.getSelectionPaths();
		if (shipPaths == null)
			return new ShipInst[0];
		ShipInst[] ret = new ShipInst[shipPaths.length];
		for (int i = 0; i < shipPaths.length; i++)
		{
			ShipNode node = (ShipNode)shipPaths[i].getLastPathComponent();
			ret[i] = node.getShip();
		}
		return ret;
	}

	public void valueChanged(TreeSelectionEvent ev)
	{
		//DebugLogic.beginGroup(getName()+" event=\"tree selection changed\" eclipse=\""+mEclipse+"\"");
		if (mInfoOnSelect && !mEclipse)
		{
			TreePath path = ev.getNewLeadSelectionPath();
			if (path != null)
			{
				ShipNode node = (ShipNode)path.getLastPathComponent();
				ShipInst primaryShip = node.getShip();
				mPanel.getInfoPanel().setObject(primaryShip);
			}
		}
		if (mShipsOnSelect && !mEclipse)
		{
			mEclipse = true;
			ShipInst[] ships = getSelectedShips();
			if (ships.length > 0)
				mPanel.getInfoPanel().getPCS().firePropertyChange("ships", null, ships);
			mEclipse = false;
		}
		fireTreeSelectionEvent(ev);
		//DebugLogic.endGroup(getName());
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent ev)
	{
		//DebugLogic.beginGroup(getName()+" event=\"ships changed\" eclipse=\""+mEclipse+"\"");
		List<ShipInst> shipList = new ArrayList<>();
		ShipInst[] ships = (ShipInst[])ev.getNewValue();
		if (ships.length == 0)
			return;
		for (int i = 0; i < ships.length; i++)
			shipList.add(ships[i]);
		List<TreePath> paths = new ArrayList<>(); 
		mTreeRoot.getAllPaths(null, paths);
		List<TreePath> selectedPaths = new ArrayList<>();
		for (TreePath path : paths)
		{
			ShipNode node = (ShipNode)path.getLastPathComponent();
			if (shipList.contains(node.getShip()))
				selectedPaths.add(path);
		}
		TreePath[] treePaths = new TreePath[selectedPaths.size()];
		selectedPaths.toArray(treePaths);
		mEclipse = true;
		mShipTree.setSelectionPaths(treePaths);
		mEclipse = false;
		//DebugLogic.endGroup(getName());
	}

	/**
	 * @param i
	 * @param j
	 */
	protected void doShipClick(int i, int j)
	{
		//DebugLogic.beginGroup(getName()+" event=\"ship click\" eclipse=\""+mEclipse+"\"");
		if (!mInfoOnClick)
			return;
		int selRow = mShipTree.getRowForLocation(i, j);
		TreePath selPath = mShipTree.getPathForLocation(i, j);
		if (selRow != -1)
		{
			ShipNode node = (ShipNode)selPath.getLastPathComponent();
			mPanel.getInfoPanel().setObject(node.getShip());
		}
		//DebugLogic.endGroup(getName());
	}

	protected void doDock()
	{
		TreePath[] paths = mShipTree.getSelectionPaths();
		if (paths.length == 0)
			return;
		ShipInst[] ships = new ShipInst[paths.length];
		int largest = -1;
		for (int i = 0; i < paths.length; i++)
		{
			ships[i] = ((ShipNode)paths[i].getLastPathComponent()).getShip();
			if (ships[i].getSideInst() != mPanel.getSide())
			{
				ships[i] = null;
				continue;
			}
			if ((largest == -1) || (ShipLogic.additionalCapacity(ships[i]) > ShipLogic.additionalCapacity(ships[largest])))
				largest = i;
		}
		if (largest < 0)
			return;
		if (ShipLogic.additionalCapacity(ships[largest]) <= 0)
		{
			mPanel.getGame().setStatus("You can't dock those ships together!");
			return;
		}
		for (int i = 0; i < ships.length; i++)
			if (i != largest)
				if (ships[i] != null)
					ShipLogic.dock(ships[largest], ships[i]);
		updateTree();
	}

	protected void doUnDock()
	{
		TreePath[] paths = mShipTree.getSelectionPaths();
		if (paths.length == 0)
			return;
		for (int i = 0; i < paths.length; i++)
		{
			ShipNode node = (ShipNode)paths[i].getLastPathComponent();
			ShipInst ship = node.getShip();
			if (ship.getSideInst() == mPanel.getSide())
				ShipLogic.undock(ship);
		}
		updateTree();
	}

	protected void doAutoDock()
	{
		ShipLogic.autoDock(mShips, mPanel.getSide());
		updateTree();
	}

	protected void doFuel()
	{
		ShipInst[] ships = getSelectedShips();
		if (ships.length != 2)
		{
			mPanel.getGame().setStatus("Select only two ship to transfer fuel.");
			return;
		}
		if (ships[0].getLocation() != ships[1].getLocation())
		{
			mPanel.getGame().setStatus("Select must be in same location to transfer fuel.");
			return;
		}
		if ((ships[0].getSideInst() != mPanel.getSide())
			|| (ships[1].getSideInst() != mPanel.getSide()))
		{
			mPanel.getGame().setStatus("Ships must both be on your side to transfer fuel.");
			return;
		}
		DlgTransferFuel dlg = null;
		for (Component c = getParent(); c != null; c = ((Container)c).getParent())
			if (c instanceof Frame)
			{
				dlg = new DlgTransferFuel((Frame)c,  mPanel, ships[0], ships[1]);
				break;
			}
			else if (!(c instanceof Container))
				break;
		if (dlg == null)
			return;
		dlg.setVisible(true);
	}
	
	public void addTreeSelectionListener(TreeSelectionListener l)
	{
		synchronized (mTreeListeners)
		{
			mTreeListeners.add(l);
		}
	}
	
	public void removeTreeSelectionListener(TreeSelectionListener l)
	{
		synchronized (mTreeListeners)
		{
			mTreeListeners.remove(l);
		}
	}
	
	private void fireTreeSelectionEvent(TreeSelectionEvent ev)
	{
	    ArrayUtils.opCollection(mTreeListeners, (i) -> i.valueChanged(ev));
	}
	/**
	 * @return
	 */
	public boolean isInfoOnClick()
	{
		return mInfoOnClick;
	}

	/**
	 * @return
	 */
	public boolean isInfoOnSelect()
	{
		return mInfoOnSelect;
	}

	/**
	 * @param b
	 */
	public void setInfoOnClick(boolean b)
	{
		mInfoOnClick = b;
	}

	/**
	 * @param b
	 */
	public void setInfoOnSelect(boolean b)
	{
		mInfoOnSelect = b;
	}
	/**
	 * @return
	 */
	public String getRootLabel()
	{
		return mRootLabel;
	}

	/**
	 * @param string
	 */
	public void setRootLabel(String string)
	{
		mRootLabel = string;
	}

	/**
	 * @return
	 */
	public boolean isShipsOnSelect()
	{
		return mShipsOnSelect;
	}

	/**
	 * @param b
	 */
	public void setShipsOnSelect(boolean b)
	{
		mShipsOnSelect = b;
	}
}
