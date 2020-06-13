package jo.ttg.core.ui.swing.ctrl.surf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class HEALCanvasPainter<T>
{
    private IHEALGlobe<T>   mGlobe;
    private IHEALGlobe<HEALPolygon[]>   mPolygonCache;
    private Rectangle   mBounds;
    private boolean     mDrawOutline;
    private Map<String,IHEALCoord>     mDirtyCoords;
    private boolean     mDirtyAll;
    private Color       mBackground;
    
    public HEALCanvasPainter()
    {
        mDrawOutline = true;
        mDirtyCoords = new HashMap<>();
        mDirtyAll = true;
    }
    
    public void setDrawOutline(boolean v)
    {
        mDrawOutline = v;
        mDirtyAll = true;
    }
    
    public boolean isDrawOutline()
    {
        return mDrawOutline;
    }
    
    public void setGlobe(IHEALGlobe<T> globe)
    {
        mGlobe = globe;
        if (mPolygonCache == null)
            mPolygonCache = new jo.util.heal.impl.HEALGlobe<>(mGlobe.getResolution());
        mDirtyAll = true;
    }
    
    public IHEALGlobe<T> getGlobe()
    {
        return mGlobe;
    }

    public Point getCenter(IHEALCoord coord)
    {
        HEALPolygon[] tiles = getTile(coord);
        return tiles[0].getCenter();
    }
    
    public Rectangle getBounds(IHEALCoord coord)
    {
        HEALPolygon[] tiles = getTile(coord);
        return tiles[0].getBounds();
    }
    
    public HEALPolygon[] getTile(IHEALCoord coord)
    {
        HEALPolygon[] cache = mPolygonCache.get(coord);
        if (cache != null)
            return cache;
        HEALPolygon[] ret = null;
        // mercator
        double[][] pts = getThetaPhiBox(coord);
        if (pts[3][0] - pts[1][0] > .1)
        {
            for (int i = 0; i < pts.length; i++)
                if (pts[i][0] < .5)
                    pts[i][0] += 1.0;
        }
        HEALPolygon base = new HEALPolygon();
        ret = new HEALPolygon[1];
        ret[0] = base;
        for (int i = 0; i < pts.length; i++)
        {
           addPointShape1(pts, i, base);
        }
        /*
        if (pts[3][0] >= 1.0)
            base.translate(-mBounds.width, 0);
        if (pts[3][0] < 1.0 && pts[1][0] > 1.0)
        {
            ret = new HEALPolygon[2];
            ret[0] = base;
            base = new HEALPolygon();
            for (int i = 0; i < pts.length; i++)
            {
                pts[i][0] -= 1.0;
               addPointShape1(pts, i, base);
            }
            ret[1] = base;
        }
        */
        mPolygonCache.set(coord, ret);
        return ret;
    }
    
    private void addPointShape1(double[][] pts, int idx, HEALPolygon poly)
    {
        int x = (int)(pts[idx][0]*(mBounds.width-10));
        int y = -(int)(pts[idx][1]*mBounds.height*2) + mBounds.height/2;
        poly.addPoint(x, y);
    }
    
    public void paintControl(Graphics2D gc)
    {
        if (mGlobe == null)
            return;
        Iterator<IHEALCoord> i;
        if (mDirtyAll)
        {
            i = mGlobe.coordsIterator();
            mDirtyCoords.clear();
            gc.setColor(getBackground());
            gc.fillRect(0, 0, mBounds.width, mBounds.height);
            //System.out.print("<update size='globe'>");
        }
        else
        {
            List<IHEALCoord> l = new ArrayList<>();
            l.addAll(mDirtyCoords.values());
            mDirtyCoords.clear();
            i = l.iterator();
            //System.out.print("<update size='"+l.size()+"'>");
        }
        while (i.hasNext())
        {
            IHEALCoord c = (IHEALCoord)i.next();
            paint(gc, c);
        }
        //System.out.println("</update>");
        mDirtyAll = false;      
    }

    protected boolean mustPaint(IHEALCoord c)
    {
        return mDirtyAll || mDirtyCoords.containsKey(c.toString());
    }

    protected void paint(Graphics2D g, IHEALCoord c)
    {
        //System.out.print("  "+Long.toHexString(c.getData()));         
        Object o = mGlobe.get(c);
        HEALPolygon[] tile = getTile(c);
        for (int j = 0; j < tile.length; j++)
            if (tile[j] != null)
                paint(g, c, o, tile[j]);
    }

    public Color getFillColor(IHEALCoord c, Object o)
    {
        if (o instanceof Color)
            return (Color)o;
        return null;
    }

    public Color getEdgeColor(IHEALCoord c, Object o)
    {
        if (o instanceof Color)
            return (Color)o;
        return null;
    }
    
    public void paint(Graphics2D g, IHEALCoord c, Object o, HEALPolygon tile)
    {
        paintTile(g, c, o, tile.getPolygon());
        if (mDrawOutline)
            paintTileEdge(g, c, o, tile.getPolygon());
    }

    public void paintTileEdge(Graphics2D g, IHEALCoord c, Object o, Polygon tile)
    {
        Color edge = getEdgeColor(c, o);        
        if (edge != null)
        {
            g.setColor(edge);
            g.drawPolygon(tile);
        }
    }

    public void paintTile(Graphics2D g, IHEALCoord c, Object o, Polygon tile)
    {
        Color fill = getFillColor(c, o);        
        if (fill != null)
        {
            g.setColor(fill);
            g.fillPolygon(tile);
            //g.drawPolygon(tile);
        }
    }

    public void repaint(IHEALCoord ord)
    {
        if ((ord != null) && !mDirtyAll)
        {
            mDirtyCoords.put(ord.toString(), ord);
            if (mDirtyCoords.size() > mGlobe.size()/2)
                mDirtyAll = true;
        }
    }

    public IHEALCoord getCoordFromPoint(int x, int y)
    {
        for (Iterator<IHEALCoord> i = mGlobe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord c = i.next();
            Rectangle cp = getBounds(c);
            if (cp.contains(x, y))
                return c;
        }
        return null;
    }

    /**
     * @return Returns the dirtyAll.
     */
    public boolean isDirtyAll()
    {
        return mDirtyAll;
    }
    /**
     * @param dirtyAll The dirtyAll to set.
     */
    public void setDirtyAll(boolean dirtyAll)
    {
        mDirtyAll = dirtyAll;
    }

    public double[][] getThetaPhiBox(IHEALCoord c)
    {
        double[][] tpBox = c.getThetaPhiBox();
        return tpBox;
    }

    public Rectangle getBounds()
    {
        return mBounds;
    }

    public void setBounds(Rectangle bounds)
    {
        mBounds = bounds;
        if ((mPolygonCache == null) && (mGlobe != null))
            mPolygonCache = new jo.util.heal.impl.HEALGlobe<>(mGlobe.getResolution());
        mDirtyAll = true;
    }

    public Color getBackground()
    {
        return mBackground;
    }

    public void setBackground(Color background)
    {
        mBackground = background;
    }

}
