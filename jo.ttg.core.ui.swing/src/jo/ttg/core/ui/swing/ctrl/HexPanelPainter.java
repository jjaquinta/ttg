/*
 * Created on Mar 25, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.mw.MainWorldLogic;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class HexPanelPainter
{
	protected HexPanel	mPanel;
	
	protected int		mHexSide;
	protected int		mHexShortSide;
	protected int		mHexLongSide;
	protected Font		mNameFont;
	
	public HexPanelPainter(HexPanel panel)
	{
		mPanel = panel;
	}
	
	private void setup()
	{
		if (mHexSide != mPanel.getHexSide())
		{
			mHexSide = mPanel.getHexSide();
			mHexShortSide = mHexSide/2;
			mHexLongSide = (mHexSide*866)/1000;
			mNameFont = new Font("Arial", Font.PLAIN, mHexSide/3);
		}
	}
	
	public void paint(Graphics2D g, Dimension o, Polygon p, Object obj, int mode)
	{
		setup();
		MainWorldBean mw = null;
		if (obj instanceof MainWorldBean)
			mw = (MainWorldBean)obj;
		paintHex(g, p, mw, mode);
		if (mw == null)
			return;
		if (mHexSide >= 18)
		{
			paintZone(g, o, mw, mode);
			paintLoc(g, o, mw, mode);
			paintName(g, o, mw, mode);
			paintWorld(g, o, mw, mode);
			paintPort(g, o, mw, mode);
			paintBases(g, o, mw, mode);
			paintGiant(g, o, mw, mode);
		}
		else
		{
			paintWorld(g, o, mw, mode);
		}
	}
	protected void paintBases(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		int w;
		int rad = mHexLongSide/4;

		Font f = new Font("Arial", Font.PLAIN, 8);
		g.setFont(f);
		FontMetrics fm = g.getFontMetrics();
		String m_Bases = mw.getPopulatedStats().getBasesDesc();
		if (m_Bases.indexOf("N") >= 0)
		{
			g.setColor(Color.yellow);
			w = fm.stringWidth("N");
			g.drawString("N", o.width + mHexShortSide + mHexSide/2 - rad - w, o.height + mHexLongSide - fm.getDescent());
		}
		if (m_Bases.indexOf("S") >= 0)
		{
			g.setColor(Color.red);
			w = fm.stringWidth("S");
			g.drawString("S", o.width + mHexShortSide + mHexSide/2 - rad - w, o.height + mHexLongSide + fm.getAscent());
		}
	}
	protected void paintGiant(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		if (mw.getNumGiants() < 1)
			return;
		int rad = mHexLongSide/4;
		int rad2 = rad/2;
		setColorFromMode(g, mode);
		g.fillOval(o.width + mHexShortSide + mHexSide/2 + rad, o.height + mHexLongSide - rad - rad2, rad2, rad2);
	}
	protected void paintHex(Graphics2D g, Polygon p, MainWorldBean mw, int mode)
	{
		setBackColorFromMode(g, mode);
		g.fillPolygon(p);
		g.setColor(mPanel.getForeColor());
		g.drawPolygon(p);
	}
	protected void paintLoc(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		setColorFromMode(g, mode);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String str = OrdLogic.getShortNum(mw.getOrds());
		int w = fm.stringWidth(str);
		g.drawString(str, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + 1 + fm.getAscent());
	}
	protected void paintName(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		int rad = mHexLongSide/4;
		setColorFromMode(g, mode);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String str = MainWorldLogic.getNameDesc(mw);
		int w = fm.stringWidth(str);
		g.drawString(str, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + mHexLongSide + rad + fm.getAscent());
	}
	protected void paintPort(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		int rad = mHexLongSide/4;
		setColorFromMode(g, mode);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String Port = String.valueOf((char)mw.getPopulatedStats().getUPP().getPort().getValue());
		int w = fm.stringWidth(Port);
		g.drawString(Port, o.width + mHexShortSide + mHexSide/2 - w/2, o.height + mHexLongSide - rad - fm.getDescent());
	}
	protected void paintWorld(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		int rad;
		if (mHexSide >= 18)
			rad = mHexLongSide/8;
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
			g.fillOval(o.width + mHexShortSide + mHexSide/2 - rad, o.height + mHexLongSide - rad, rad*2, rad*2);
		}
	}
	protected void paintZone(Graphics2D g, Dimension o, MainWorldBean mw, int mode)
	{
		if (mw.getPopulatedStats().getTravelZone() == 'R')
		{
			g.setColor(Color.red);
			g.drawOval(o.width + 6, o.height + 6, mHexShortSide*2 + mHexSide - 12, mHexLongSide*2 - 12);
		}
		else if (mw.getPopulatedStats().getTravelZone() == 'A')
		{
			g.setColor(Color.yellow);
			g.drawOval(o.width + 6, o.height + 6, mHexShortSide*2 + mHexSide - 12, mHexLongSide*2 - 12);
		}
	}
	
	protected void setColorFromMode(Graphics2D g, int mode)
	{
		switch (mode)
		{
			case HexPanel.M_NORM:
				g.setColor(mPanel.getForeColor());
				break;
			case HexPanel.M_FOCUSED:
				g.setColor(mPanel.getFocusedColor());
				break;
			case HexPanel.M_DISABLED:
				g.setColor(mPanel.getDisabledColor());
				break;
			case HexPanel.M_BACK:
				g.setColor(mPanel.getBackground());
				break;
		}
	}
	
	protected void setBackColorFromMode(Graphics2D g, int mode)
	{
		switch (mode)
		{
			case HexPanel.M_NORM:
				g.setColor(mPanel.getUnfocusedBackColor());
				break;
			case HexPanel.M_FOCUSED:
				g.setColor(mPanel.getFocusedBackColor());
				break;
			case HexPanel.M_DISABLED:
				g.setColor(mPanel.getDisabledBackColor());
				break;
			case HexPanel.M_BACK:
				g.setColor(mPanel.getBackground());
				break;
		}
	}
	/**
	 * @return
	 */
	public HexPanel getPanel()
	{
		return mPanel;
	}

	/**
	 * @param panel
	 */
	public void setPanel(HexPanel panel)
	{
		mPanel = panel;
	}

}
