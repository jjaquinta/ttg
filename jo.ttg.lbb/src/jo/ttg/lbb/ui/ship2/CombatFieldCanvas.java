package jo.ttg.lbb.ui.ship2;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import jo.ttg.lbb.data.ship2.Combat;
import jo.ttg.lbb.data.ship2.CombatSide;
import jo.ttg.lbb.data.ship2.Missile;
import jo.ttg.lbb.data.ship2.Planet;
import jo.ttg.lbb.data.ship2.Ship2Instance;
import jo.ttg.lbb.logic.ship2.CombatLogic;
import jo.ttg.lbb.logic.ship2.PlanetLogic;
import jo.util.geom3d.Point3D;

public class CombatFieldCanvas extends Canvas
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 2101888078339849839L;

	private Combat	           mCombat;
	private double			   mShiftX;
	private double			   mShiftY;
	private double			   mScaleX;
	private double			   mScaleY;

	public CombatFieldCanvas()
	{
		super();
	}	
	
	public void resetTransform()
	{
		if (mCombat == null)
		{
	        mShiftX = 0;
	        mShiftY = 0;
	        mScaleX = 1.0;
	        mScaleY = 1.0;
	        return;
		}
        Point3D[] combatBounds = CombatLogic.getBounds(mCombat);
        double combatWidth = combatBounds[1].x - combatBounds[0].x;
        double combatHeight = combatBounds[1].y - combatBounds[0].y;
        Dimension screenBounds = getSize();
        //System.out.println("Need to map "+combatBounds[0].toIntString()+"--"+combatBounds[1].toIntString()+" to 0,0--"+screenBounds.width+","+screenBounds.height);
        mShiftX = combatBounds[0].x;
        mShiftY = combatBounds[0].y;
        mScaleX = Math.min(screenBounds.width/combatWidth, screenBounds.height/combatHeight*3/4);
        mScaleY = Math.min(screenBounds.width/combatWidth, screenBounds.height/combatHeight*4/3);
        repaint();
	}

	public Combat getCombat()
	{
		return mCombat;
	}

	public void setCombat(Combat combat)
	{
		mCombat = combat;
		resetTransform();
	}
	
	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		if (mCombat == null)
			return;
		Graphics2D g2 = (Graphics2D)g;
		paintSide(g2, mCombat.getSides().get(0), Color.blue);
		paintSide(g2, mCombat.getSides().get(1), Color.red);
		for (Planet p : mCombat.getScenario().getPlanets())
			paintPlanet(g2, p, Color.green);
	}

	private void paintSide(Graphics2D g2, CombatSide side, Color c)
	{
		for (Ship2Instance ship : side.getShips())
			paintShip(g2, ship, c);
		for (Missile m : side.getMissiles())
			paintMissile(g2, m, c);
	}

	private void paintShip(Graphics2D g2, Ship2Instance ship, Color c)
	{
		 Point s = transform(ship.getLocation());
		 int[] xs = new int[] { s.x - 3, s.x - 3, s.x + 3 };
		 int[] ys = new int[] { s.y - 3, s.y + 3, s.y };
		 g2.setColor(c);
		 g2.fillPolygon(xs, ys, 3);
		 for (Point3D tail : ship.getPastMovement())
		 {
			 Point t = transform(tail);
			 c = c.darker();
			 g2.setColor(c);
			 g2.drawLine(s.x, s.y, t.x, t.y);
			 s = t;
		 }
	}

	private void paintMissile(Graphics2D g2, Missile m, Color c)
	{
        Point s = transform(m.getLocation());
        g2.setColor(c);
        g2.fillOval(s.x - 2, s.y - 2, 4, 4);
	}

	private void paintPlanet(Graphics2D g2, Planet p, Color c)
	{
		Point3D radius = p.getLocation().add(new Point3D(PlanetLogic.getRadius(p), PlanetLogic.getRadius(p), 0));
		Point s = transform(p.getLocation());
		Point r = transform(radius);
		int deltaX = r.x - s.x;
        int deltaY = r.y - s.y;
        g2.setColor(c);
        g2.fillOval(s.x - deltaX, s.y - deltaY, deltaX*2, deltaY*2);
	}
	
	private Point transform(Point3D p)
	{
		double x = p.x;
		double y = p.y;
		//System.out.print((int)x+","+(int)y);
		x -= mShiftX;
		y -= mShiftY;
		//System.out.print("->"+(int)x+","+(int)y);
		x *= mScaleX;
		y *= mScaleY;
		//System.out.print("->"+(int)x+","+(int)y);
		//System.out.println();
		return new Point((int)x, (int)y);
	}
}
