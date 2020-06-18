package ttg.view.war;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ttg.beans.war.SideInst;
import ttg.logic.war.IconLogic;

public class SideRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>
{
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
		if ((value instanceof SideInst) && (ret instanceof JLabel))
		{
			SideInst side = (SideInst)value;
			JLabel ctrl = (JLabel)ret;
			ctrl.setIcon(IconLogic.getSideIcon(side));
		}
		return ret;
	}
}
