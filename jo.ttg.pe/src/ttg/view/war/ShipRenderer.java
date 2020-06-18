package ttg.view.war;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.logic.war.IconLogic;

public class ShipRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>
{
	int	mToDo;
	
	public ShipRenderer(int todo)
	{
		mToDo = todo;
	}
	/*
	 * This method finds the image and text corresponding
	 * to the selected value and returns the label, set up
	 * to display the text and image.
	 */
	public Component getListCellRendererComponent(
		JList<?> list,
		Object value,
		int index,
		boolean isSelected,
		boolean cellHasFocus)
	{
		Component ret = super.getListCellRendererComponent(list, value,
			index, isSelected, cellHasFocus);
		if ((value instanceof ShipInst) && (ret instanceof JLabel))
		{
			ShipInst ship = (ShipInst)value;
			JLabel ctrl = (JLabel)ret;
			ctrl.setText(ship.getShip().getName());
			ctrl.setIcon(IconLogic.getShipIcon(ship, mToDo));
		}
		else if ((value instanceof Ship) && (ret instanceof JLabel))
		{
			Ship ship = (Ship)value;
			JLabel ctrl = (JLabel)ret;
			ctrl.setText(ship.getName());
			ctrl.setIcon(IconLogic.getShipIcon(ship, mToDo));
		}
		return ret;
	}
}
