package ttg.view.war;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import ttg.beans.war.ShipInst;
import ttg.logic.war.IconLogic;

public class ShipNodeRenderer extends DefaultTreeCellRenderer
{

    /**
     *
     */

    public Component getTreeCellRendererComponent(
        JTree tree,
        Object value,
        boolean sel,
        boolean expanded,
        boolean leaf,
        int row,
        boolean hasFocus)
    {
        Component ret = super.getTreeCellRendererComponent(
			tree,
			value,
			sel,
			expanded,
			leaf,
			row,
			hasFocus);
		if ((value instanceof ShipNode) && (ret instanceof JLabel))
		{
			ShipInst ship = ((ShipNode)value).getShip();
			if (ship != null)
			{
				JLabel ctrl = (JLabel)ret;
				ctrl.setText(ship.getShip().getName());
				ctrl.setIcon(IconLogic.getShipIcon(ship, IconLogic.SEL_TODO));
			}
		}
        return ret;
    }

}
