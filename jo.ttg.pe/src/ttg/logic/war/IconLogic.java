/*
 * Created on Mar 10, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package ttg.logic.war;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.logic.OrdLogic;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.utils.io.ResourceUtils;
import ttg.beans.war.GameInst;
import ttg.beans.war.Ship;
import ttg.beans.war.ShipInst;
import ttg.beans.war.SideInst;
import ttg.beans.war.WorldInst;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class IconLogic
{
	private static ImageIcon[] mIconShields = 
	{
		loadWarIcon("side_shield1.gif"),
		loadWarIcon("side_shield2.gif"),
		loadWarIcon("side_shield3.gif"),
		loadWarIcon("side_shield4.gif"),
		loadWarIcon("side_shield5.gif"),
		loadWarIcon("side_shield6.gif"),
		loadWarIcon("side_shield7.gif"),
	};

	private static ImageIcon mIconShip = loadWarIcon("ship_base.gif");
	private static ImageIcon mIconDamage = loadWarIcon("ship_dam.gif");
	private static ImageIcon mIconFlee = loadWarIcon("ship_flee.gif");

	private static ImageIcon mArrowToDo = loadWarIcon("ship_todo.gif");
	private static ImageIcon mArrowDone = loadWarIcon("ship_done.gif");

	public static ImageIcon mLogoSmall = loadWarIcon("logo_pe32.gif");
	public static ImageIcon mLogoLarge = loadWarIcon("logo_lg.gif");
	public static ImageIcon mLogoBackground = loadWarIcon("logo_bg.gif");

	public static ImageIcon mButtonSet = loadWarIcon("button_set.gif");
	public static ImageIcon mButtonReset = loadWarIcon("button_reset.gif");
	public static ImageIcon mButtonDone = loadWarIcon("button_done.gif");
	public static ImageIcon mButtonForward = loadWarIcon("button_f.gif");
	public static ImageIcon mButtonFastForward = loadWarIcon("button_ff.gif");
	public static ImageIcon mButtonForwardToEnd = loadWarIcon("button_fff.gif");
	public static ImageIcon mButtonReverse = loadWarIcon("button_r.gif");
	public static ImageIcon mButtonFastReverse = loadWarIcon("button_rr.gif");
	public static ImageIcon mButtonReverseToEnd = loadWarIcon("button_rrr.gif");
	public static ImageIcon mButtonDock = loadWarIcon("button_dock.gif");
	public static ImageIcon mButtonUnDock = loadWarIcon("button_undock.gif");
	public static ImageIcon mButtonFuel = loadWarIcon("button_fuel.gif");
	public static ImageIcon mButtonFlee = loadWarIcon("button_flee.gif");
	public static ImageIcon mButtonInfo = loadWarIcon("button_info.gif");
	public static ImageIcon mButtonScrap = loadWarIcon("button_scrap.gif");
	public static ImageIcon mButtonBuild = loadWarIcon("button_build.gif");
	public static ImageIcon mButtonSave = loadWarIcon("button_save.gif");
	public static ImageIcon mButtonLoad = loadWarIcon("button_load.gif");
	public static ImageIcon mButtonUp = loadWarIcon("button_u.gif");
	public static ImageIcon mButtonDown = loadWarIcon("button_d.gif");
	public static ImageIcon mButtonAdd = loadWarIcon("button_add.gif");
	public static ImageIcon mButtonSubtract = loadWarIcon("button_sub.gif");
	public static ImageIcon mButtonEdit = loadWarIcon("button_edit.gif");
	public static ImageIcon mButtonCancel = loadWarIcon("button_cancel.gif");
	public static ImageIcon mButtonHelp = loadWarIcon("button_help.gif");
	
	private static ImageIcon loadWarIcon(String icon)
	{
		try
        {
            return new ImageIcon(ResourceUtils.loadSystemResourceBinary("icons/"+icon, IconLogic.class));
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
	}

	public static final int SEL_DONT = 0;
	public static final int SEL_TODO = 1;
	public static final int SEL_DONE = 2;
	
	private static Color mGenericFG = new Color(0xFFFFFFFF);
	private static Color mGenericBG = new Color(0xFFFF0000);

	public static ImageIcon getShipIcon(ShipInst ship, int todo)
	{
		Color fg = ship.getSideInst().getColor2(); 
		Color bg = ship.getSideInst().getColor1(); 
		String msg = ShipLogic.getAttack(ship)+"."+
			ShipLogic.getDefense(ship)+"."+
			ship.getShip().getJump();
		BufferedImage canvas = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics g = canvas.getGraphics();
		g.setColor(mGenericBG);
		g.fillRect(0, 0, 48, 48);
		g.drawImage(mIconShip.getImage(), 0, 0, null);
		if (ship.isDamaged())
			g.drawImage(mIconDamage.getImage(), 32, 0, null);
		if (ship.isFleeing() || (ship.getTarget() == ship))
			g.drawImage(mIconFlee.getImage(), 32, 12, null);
		if (todo != SEL_DONT)
		{
			if (ship.isToDo())
				g.drawImage(mArrowToDo.getImage(), 24, 32, null);
			else
				g.drawImage(mArrowDone.getImage(), 24, 32, null);
		}
		g.setColor(mGenericFG);
		g.drawString(msg, 2, 46);			
		int[] pix = canvas.getRGB(0, 0, 48, 48, null, 0, 48);
		swap(pix, mGenericFG.getRGB(), fg.getRGB());
		swap(pix, mGenericBG.getRGB(), bg.getRGB());
		canvas.setRGB(0, 0, 48, 48, pix, 0, 48);
		return new ImageIcon(canvas);
	}

	public static ImageIcon getShipIcon(Ship ship, int todo)
	{
		Color fg = Color.WHITE; 
		Color bg = Color.DARK_GRAY; 
		String msg = ship.getAttack()+"."+
			ship.getDefense()+"."+
			ship.getJump();
		BufferedImage canvas = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics g = canvas.getGraphics();
		g.setColor(mGenericBG);
		g.fillRect(0, 0, 48, 48);
		g.drawImage(mIconShip.getImage(), 0, 0, null);
		g.setColor(mGenericFG);
		g.drawString(msg, 2, 46);			
		int[] pix = canvas.getRGB(0, 0, 48, 48, null, 0, 48);
		swap(pix, mGenericFG.getRGB(), fg.getRGB());
		swap(pix, mGenericBG.getRGB(), bg.getRGB());
		canvas.setRGB(0, 0, 48, 48, pix, 0, 48);
		return new ImageIcon(canvas);
	}

	/**
	 * @param pix
	 */
	private static void swap(int[] pix, int oldRGB, int newRGB)
	{
		for (int i = 0; i < pix.length; i++)
		{
			if (pix[i] == oldRGB)
				pix[i] = newRGB;
		}
	}
	
//	private static ImageIcon getShipIcon(ShipInst ship)
//	{
//		return getShipIcon(ship, SEL_TODO);
//	}

	public static ImageIcon getSideIcon(Color fg, Color bg, int off)
	{
		BufferedImage canvas = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics g = canvas.getGraphics();
		g.drawImage(mIconShields[off%mIconShields.length].getImage(), 0, 0, null);
		int[] pix = canvas.getRGB(0, 0, 48, 48, null, 0, 48);
		swap(pix, 0xFFFF0000, fg.getRGB());
		swap(pix, 0xFFFFFFFF, bg.getRGB());
		canvas.setRGB(0, 0, 48, 48, pix, 0, 48);
		return new ImageIcon(canvas);
	}

	public static ImageIcon getSideIcon(SideInst side)
	{
		return getSideIcon(side.getColor1(), side.getColor2(), side.getIndex());
	}

	public static ImageIcon getSideIcon(SideInst side, boolean selected)
	{
		if (selected)
			return getSideIcon(side.getColor1().brighter(), side.getColor2().brighter(), 
				side.getIndex());
		else
			return getSideIcon(side.getColor1(), side.getColor2(),
				side.getIndex());
	}

	private static final int WI_OX = 24;
	private static final int WI_OY = 28;
	private static Font mHexFont = new Font("Arial", Font.PLAIN, 8);
	private static Font mNameFont = new Font("Arial", Font.PLAIN, 10);

	public static ImageIcon getWorldIcon(GameInst game, WorldInst world, SideInst pov)
	{
		MainWorldBean mw = world.getWorld();
		BufferedImage canvas = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
		Graphics g = canvas.getGraphics();
		setBackColorFromMode(g, world);
		g.fillRect(0, 0, 48, 48);
		paintLoc(g, world);
		if (mw != null)
		{
			paintName(g, world); 
			paintWorld(g, world);
			paintPort(g, world);
			paintBases(g, world);
			paintGiant(g, world);
		}
		paintShips(g, game, world, pov);
		return new ImageIcon(canvas);
	}

	private static void paintBases(Graphics g, WorldInst world)
	{
		int w;
		int rad = 24/4;

		g.setFont(mHexFont);
		FontMetrics fm = g.getFontMetrics();
		String m_Bases = world.getWorld().getPopulatedStats().getBasesDesc();
		if (m_Bases.indexOf("N") >= 0)
		{
			g.setColor(Color.yellow);
			w = fm.stringWidth("N");
			g.drawString("N", WI_OX - rad - w, WI_OY - fm.getDescent());
		}
		if (m_Bases.indexOf("S") >= 0)
		{
			g.setColor(Color.red);
			w = fm.stringWidth("S");
			g.drawString("S", WI_OX - rad - w, WI_OY + fm.getAscent());
		}
	}
	private static void paintGiant(Graphics g, WorldInst world)
	{
		if (world.getWorld().getNumGiants() < 1)
			return;
		int rad = 24/4;
		int rad2 = rad/2;
		setColorFromMode(g, world);
		g.fillOval(WI_OX + rad, WI_OY - rad - rad2, rad2, rad2);
	}
	private static void paintLoc(Graphics g, WorldInst world)
	{
		setColorFromMode(g, world);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String str = OrdLogic.getShortNum(world.getOrds());
		int w = fm.stringWidth(str);
		g.drawString(str, WI_OX - w/2, 1 + fm.getAscent());
	}
	private static void paintName(Graphics g, WorldInst world)
	{
		int rad = 24/4;
		setColorFromMode(g, world);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String str = MainWorldLogic.getNameDesc(world.getWorld());
		int w = fm.stringWidth(str);
		g.drawString(str, WI_OX - w/2, WI_OY + rad + fm.getAscent());
	}
	private static void paintPort(Graphics g, WorldInst world)
	{
		int rad = 24/4;
		setColorFromMode(g, world);
		g.setFont(mNameFont);
		FontMetrics fm = g.getFontMetrics();
		String Port = String.valueOf((char)world.getWorld().getPopulatedStats().getUPP().getPort().getValue());
		int w = fm.stringWidth(Port);
		g.drawString(Port, WI_OX - w/2, WI_OY - rad - fm.getDescent());
	}
	private static void paintWorld(Graphics g, WorldInst world)
	{
		int rad = 3;
		int size = world.getWorld().getPopulatedStats().getUPP().getSize().getValue();
		if (size == '0')
		{   // asteroid
			int rad2 = rad/3;
			setColorFromMode(g, world);
			g.fillOval(WI_OX - rad, WI_OY - rad, rad2, rad2);
			g.fillOval(WI_OX, WI_OY - rad, rad2, rad2);
			g.fillOval(WI_OX + rad/2, WI_OY - rad/2, rad2, rad2);
			g.fillOval(WI_OX, WI_OY, rad2, rad2);
			g.fillOval(WI_OX + rad/2, WI_OY + rad/2, rad2, rad2);
			g.fillOval(WI_OX - rad, WI_OY + rad, rad2, rad2);
			g.fillOval(WI_OX + rad, WI_OY + rad, rad2, rad2);
		}
		else
		{
			int hydro = world.getWorld().getPopulatedStats().getUPP().getHydro().getValue();
			if (hydro == 0)
				g.setColor(Color.orange);
			else
				g.setColor(Color.blue);
			g.fillOval(WI_OX - rad, WI_OY - rad, rad*2, rad*2);
		}
	}
	private static void paintShips(Graphics g, GameInst game, WorldInst world, SideInst pov)
	{
		List<SideInst> sides = game.getSides();
		List<ShipInst> ships = WorldLogic.getVisibleShips(game, world, pov);
		int[] number = new int[sides.size()];
		for (ShipInst ship : ships)
		{
			while (ship.getContainedBy() != null)
				ship = ship.getContainedBy();
			number[sides.indexOf(ship.getSideInst())]++;
		}
		int y = -number.length*2 + 1;
		for (int i = 0; i < number.length; i++)
		{
			if (number[i] == 0)
				continue;
			SideInst side = (SideInst)sides.get(i);
			if (number[i] > 8)
				number[i] = 8;
			g.setColor(side.getColor1());
			g.drawLine(WI_OX + 12, WI_OY + y, WI_OX + 12 + number[i]*2, WI_OY + y);
			y++;
			g.setColor(side.getColor2());
			g.drawLine(WI_OX + 12, WI_OY + y, WI_OX + 12 + number[i]*2, WI_OY + y);
			y += 3;
		}
	}
	/**
	 * @param g
	 * @param world
	 */
	private static void setColorFromMode(Graphics g, WorldInst world)
	{
		if (world.getSide() == null)
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(world.getSide().getColor2());
	}
	/**
	 * @param g
	 * @param world
	 */
	private static void setBackColorFromMode(Graphics g, WorldInst world)
	{
		if (world.getSide() == null)
			g.setColor(Color.DARK_GRAY);
		else
			g.setColor(world.getSide().getColor1());
	}
}
