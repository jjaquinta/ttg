package ttg.view.war;

import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import ttg.beans.war.ShipInst;
import ttg.logic.war.ShipLogic;

public class ShipNode implements TreeNode
{
    private String           mRootLabel;
    private ShipNode         mParent;
    private ShipInst         mShip;
    private List<ShipInst>   mShips;
    private Vector<ShipNode> mNodes;

    public ShipNode(ShipNode parent, List<ShipInst> ships)
    {
        mParent = parent;
        mShips = ships;
        mRootLabel = "Ships";
        mNodes = new Vector<>();
        for (ShipInst ship : mShips)
            mNodes.add(new ShipNode(this, ship));
        mShip = null;
    }

    public ShipNode(ShipNode parent, ShipInst ship)
    {
        this(parent, ship.getContains());
        mShip = ship;
    }

    public void getAllPaths(TreePath parent, List<TreePath> list)
    {
        TreePath thisPath;
        if (parent != null)
        {
            Object[] newPaths = new Object[parent.getPathCount() + 1];
            System.arraycopy(parent.getPath(), 0, newPaths, 0,
                    newPaths.length - 1);
            newPaths[newPaths.length - 1] = this;
            thisPath = new TreePath(newPaths);
        }
        else
            thisPath = new TreePath(this);
        if (mShip != null)
            list.add(thisPath);
        for (ShipNode node : mNodes)
            node.getAllPaths(thisPath, list);
    }

    /**
     *
     */

    public TreeNode getChildAt(int idx)
    {
        return (TreeNode)mNodes.get(idx);
    }

    /**
     *
     */

    public int getChildCount()
    {
        return mNodes.size();
    }

    /**
     *
     */

    public TreeNode getParent()
    {
        return mParent;
    }

    /**
     *
     */

    public int getIndex(TreeNode node)
    {
        return mNodes.indexOf(node);
    }

    /**
     *
     */

    public boolean getAllowsChildren()
    {
        if (mShip == null)
            return true;
        else
            return ShipLogic.additionalCapacity(mShip) > 0;
    }

    /**
     *
     */

    public boolean isLeaf()
    {
        return mNodes.size() == 0;
    }

    /**
     *
     */

    public Enumeration<ShipNode> children()
    {
        return mNodes.elements();
    }

    public ShipInst getShip()
    {
        return mShip;
    }

    public String toString()
    {
        if (mShip == null)
            return mRootLabel;
        return mShip.toString();
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

}
