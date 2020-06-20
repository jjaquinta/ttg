/*
 * Created on Apr 1, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.war.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.TooManyListenersException;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolTip;
import javax.swing.plaf.ToolTipUI;
import javax.swing.plaf.basic.BasicToolTipUI;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.core.ui.swing.ctrl.HexField;
import jo.util.ui.swing.utils.ListenerUtils;
import ttg.war.beans.WorldInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class WarHexField extends HexField
{
	private WarPanel	mPanel;
	private JPopupMenu	mPopup;
	
	/**
	 * @param scheme
	 */
	public WarHexField(WarPanel panel)
	{
		super();
		mPanel = panel;
		setupDragAndDrop();
		setupPopup();
	}
	
	private void setupPopup()
	{
		JMenuItem menuItem;
		
		mPopup = new JPopupMenu();
		menuItem = new JMenuItem("Zoom In");
		ListenerUtils.listen(menuItem, (ev) -> doZoomIn());
		mPopup.add(menuItem);
		menuItem = new JMenuItem("Zoom Out");
		ListenerUtils.listen(menuItem, (ev) -> doZoomOut());
		mPopup.add(menuItem);
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				maybeShowPopup(e);
			}
			public void mouseReleased(MouseEvent e) {
				maybeShowPopup(e);
			}
			private void maybeShowPopup(MouseEvent e) {
				if (e.isPopupTrigger()) {
					mPopup.show(e.getComponent(),
					   e.getX(), e.getY());
				}
			}
		});
	}
	
	private void setupDragAndDrop()
	{
		DropTarget dt = new DropTarget();
		DropTargetListener dtl = new DropTargetAdapter()
		{
			public void drop(DropTargetDropEvent ev)
			{
				doDrop(ev);
			}
		};
		try
		{
			dt.addDropTargetListener(dtl);
		}
		catch (TooManyListenersException e)
		{
		}
		setDropTarget(dt);
	}

	private void doDrop(DropTargetDropEvent ev)
	{
		Transferable trans = ev.getTransferable();
		Object o = null;
		try
		{
			o = trans.getTransferData(DataFlavor.stringFlavor);
		}
		catch (UnsupportedFlavorException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		Point p = ev.getLocation();
		OrdBean ord = calcHexSource((int)p.getX(), (int)p.getY());
		MainWorldBean mw = calcWorld(ord);
		if (mw == null)
			fireTTGActionEvent(
				new WarTTGActionEvent(this, WarTTGActionEvent.DROPPED, "ord://"+ord, ord, o));
		else
			fireTTGActionEvent(
				new WarTTGActionEvent(this, WarTTGActionEvent.DROPPED, mw.getURI(), mw, o));
		ev.dropComplete(true);
	}

	public JToolTip createToolTip()
	{
		return new WarToolTip();
	}

	class WarToolTip extends JToolTip
	{
		private WarToolTipUI	mUI;
		
		public WarToolTip()
		{
			((WarToolTipUI)getUI()).mPanel = mPanel;
		}
		
		/**
		 * Resets the UI property to a value from the current look and feel.
		 *
		 * @see JComponent#updateUI
		 */
		public void updateUI() {
			setUI(getUI());
		}

		public ToolTipUI getUI()
		{
			if (mUI == null)
				mUI = new WarToolTipUI(); 
			return mUI;
		}
	}

	class WarToolTipUI extends BasicToolTipUI
	{
		WarPanel mPanel;
		
		public void paint(Graphics g, JComponent c)
		{
			Font font = c.getFont();
			g.setFont(font);
			Dimension size = c.getSize();
			WorldInst world = mPanel.getHoverWorld();
			if (world == null)
			{
				g.setColor(c.getBackground());
				g.fillRect(0, 0, size.width, size.height);
				return;
			} 
			String[] text = mPanel.getHoverText();
			Color[] fg = mPanel.getHoverFG();
			Color[] bg = mPanel.getHoverBG();
			FontMetrics metrics = g.getFontMetrics(font);
			int space = metrics.charWidth(' '); 
			Insets insets = c.getInsets();
			int x = insets.left;
			for (int i = 0; i < text.length; i++)
			{
				int w = metrics.stringWidth(text[i]);
				g.setColor(bg[i]);
				g.fillRect(x, insets.top, w+space, size.height - (insets.top + insets.bottom));
				g.setColor(fg[i]);
				g.drawString(text[i], x + 2, insets.top + metrics.getAscent());
				x += w + space;
			}
			g.setColor(bg[bg.length-1]);
			g.fillRect(x, insets.top, size.width-x, size.height - (insets.top + insets.bottom));
		}

	};
	
	private void doZoomIn()
	{
		setHexSide(getHexSide() + 8);
		repaint();
	}
	
	private void doZoomOut()
	{
		if (getHexSide() > 8)
		{
			setHexSide(getHexSide() - 8);
			repaint();
		}
	}
}
