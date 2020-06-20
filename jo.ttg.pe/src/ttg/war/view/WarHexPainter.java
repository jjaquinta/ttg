package ttg.war.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.core.ui.swing.ctrl.HexPanel;
import jo.ttg.core.ui.swing.ctrl.HexPanelPainter;
import jo.ttg.logic.OrdLogic;
import ttg.war.beans.ShipInst;
import ttg.war.beans.SideInst;
import ttg.war.beans.WorldInst;
import ttg.war.logic.WorldLogic;

public class WarHexPainter extends HexPanelPainter
{
	private WarPanel	mPanel;
	private WorldInst	mActiveWorld;
	
	public WarHexPainter(WarPanel warPanel, HexPanel hexPanel)
	{
		super(hexPanel);
		mPanel = warPanel;
	}
	
	public void paint(Graphics2D g, Dimension o, Polygon p, Object obj, int mode)
	{
		mActiveWorld = WorldLogic.getWorld(mPanel.getGame(), obj);
		MainWorldBean mw = mActiveWorld.getWorld();
		super.paint(g, o, p, mw, mode);
		if (mw == null)
			paintEmptyLoc(g, o, mActiveWorld.getOrds(), mode);
		paintShips(g, o, mActiveWorld, mPanel.getSide());
	}

	protected void paintEmptyLoc(Graphics2D g, Dimension o, OrdBean ords, int mode)
	{
		setColorFromMode(g, mode);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String str = OrdLogic.getShortNum(ords);
		int w = fm.stringWidth(str);
		g.drawString(str, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + 1 + fm.getAscent());
	}

	protected void paintWorld(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		int rad;
		if (mHexSide >= 18)
			rad = mHexLongSide/7;
		else
			rad = mHexLongSide/2;
		int size = mw.getPopulatedStats().getUPP().getSize().getValue();
		if (size == '0')
		{   // asteroid
			int rad2 = rad/3;
			setColorFromMode(g, mode);
			g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide - rad, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2, o.height + mHexLongSide - rad, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad/2, o.height + mHexLongSide - rad/2, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2, o.height + mHexLongSide, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad/2, o.height + mHexLongSide + rad/2, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide + rad, rad2, rad2);
			g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad, o.height + mHexLongSide + rad, rad2, rad2);
		}
		else
		{
			int hydro = mw.getPopulatedStats().getUPP().getHydro().getValue();
			if (hydro == 0)
				g.setColor(Color.orange);
			else
				g.setColor(Color.blue);
			int pop = mw.getPopulatedStats().getUPP().getPop().getValue();
			if (pop <= 3)
				rad = rad/3;
			else if (pop <= 8)
				rad = rad*2/3;
			g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide - rad, rad*2, rad*2);
		}
	}

	private void paintShips(Graphics2D g, Dimension o, WorldInst world, SideInst pov)
	{
		if (mActiveWorld == null)
			return;
		List<SideInst> sides = mPanel.getGame().getSides();
		List<ShipInst> ships = WorldLogic.getVisibleShips(mPanel.getGame(), world, pov);
		int[] number = new int[sides.size()];
		for (ShipInst ship : ships)
		{
			while (ship.getContainedBy() != null)
				ship = ship.getContainedBy();
			number[sides.indexOf(ship.getSideInst())]++;
		}
		int ox = o.width + mHexShortSide + mHexSide/2;
		int oy = o.height + mHexLongSide;
		int y = -number.length*2 + 1;
		for (int i = 0; i < number.length; i++)
		{
			if (number[i] == 0)
				continue;
			int wid = number[i]*2;
			if (wid > mHexShortSide)
				wid = mHexShortSide;
			SideInst side = (SideInst)sides.get(i);
			g.setColor(side.getColor1());
			g.drawLine(ox + 12, oy + y, ox + 12 + wid, oy + y);
			y++;
			g.setColor(side.getColor2());
			g.drawLine(ox + 12, oy + y, ox + 12 + wid, oy + y);
			y += 3;
		}
	}
	
	private Color getFG()
	{
		try
		{
			return mActiveWorld.getSide().getColor1();
		}
		catch (NullPointerException e)
		{
			return Color.LIGHT_GRAY;
		}
	}
	
	private Color getBG()
	{
		try
		{
			return mActiveWorld.getSide().getColor2();
		}
		catch (NullPointerException e)
		{
			return Color.DARK_GRAY;
		}
	}
	
	protected void setColorFromMode(Graphics2D g, int mode)
	{
		switch (mode)
		{
			case HexPanel.M_NORM:
				g.setColor(getBG());
				break;
			case HexPanel.M_FOCUSED:
				g.setColor(getBG().brighter());
				break;
			case HexPanel.M_DISABLED:
				g.setColor(getBG().darker());
				break;
			case HexPanel.M_BACK:
				g.setColor(getFG());
				break;
		}
	}
	
	protected void setBackColorFromMode(Graphics2D g, int mode)
	{
		SideInst side = null;
		if (mActiveWorld != null)
			side = mActiveWorld.getSide();
		if (side == null)
		{
			super.setBackColorFromMode(g, mode);
			return;
		}
		switch (mode)
		{
			case HexPanel.M_NORM:
				g.setColor(getFG());
				break;
			case HexPanel.M_FOCUSED:
				g.setColor(getFG().brighter());
				break;
			case HexPanel.M_DISABLED:
				g.setColor(getFG().darker());
				break;
			case HexPanel.M_BACK:
				g.setColor(getFG());
				break;
		}
	}
}
