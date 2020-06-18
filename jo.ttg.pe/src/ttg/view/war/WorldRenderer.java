package ttg.view.war;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import ttg.beans.war.WorldInst;
import ttg.logic.war.IconLogic;

public class WorldRenderer extends DefaultListCellRenderer implements ListCellRenderer<Object>
{
	private WarPanel	mPanel;
	
	public WorldRenderer(WarPanel panel)
	{
		mPanel = panel;
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
		if ((value instanceof WorldInst) && (ret instanceof JLabel))
		{
			WorldInst world = (WorldInst)value;
			JLabel ctrl = (JLabel)ret;
			ctrl.setIcon(IconLogic.getWorldIcon(mPanel.getGame(), world, mPanel.getSide()));
		}
		return ret;
	}
}
