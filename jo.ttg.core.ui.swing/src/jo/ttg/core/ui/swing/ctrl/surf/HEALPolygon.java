package jo.ttg.core.ui.swing.ctrl.surf;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;

public class HEALPolygon
{
    int[] mPoints = new int[0];
    
    public HEALPolygon()
    {
    }
    
    public HEALPolygon(int[] points)
    {
        mPoints = points;
    }
    
    public void addPoint(int x, int y)
    {
        int[] newPoints = new int[mPoints.length + 2];
        System.arraycopy(mPoints, 0, newPoints, 0, mPoints.length);
        newPoints[mPoints.length] = x;
        newPoints[mPoints.length+1] = y;
        mPoints = newPoints;
    }
    
    public void translate(int x, int y)
    {
        for (int o = 0; o < mPoints.length; o += 2)
        {
            mPoints[o] += x;
            mPoints[o+1] += y;
        }
    }
    
    public Point getCenter()
    {
        Point o = new Point(0, 0);
        for (int i = 0; i < mPoints.length; i+= 2)
        {
            o.x += mPoints[i+0];
            o.y += mPoints[i+1];
        }
        o.x /= mPoints.length/2;
        o.y /= mPoints.length/2;
        return o;
    }
    
    public Rectangle getBounds()
    {
        Rectangle r = new Rectangle(0, 0, 0, 0);
        if (mPoints.length == 0)
            return r;
        int xmin = mPoints[0];
        int xmax = mPoints[0];
        int ymin = mPoints[1];
        int ymax = mPoints[1];
        for (int i = 2; i < mPoints.length; i+= 2)
        {
            xmin = Math.min(xmin, mPoints[i+0]);
            xmax = Math.max(xmax, mPoints[i+0]);
            ymin = Math.min(ymin, mPoints[i+1]);
            ymax = Math.max(ymax, mPoints[i+1]);
        }
        r.x = xmin;
        r.y = ymin;
        r.width = xmax - xmin;
        r.height = ymax - ymin;
        return r;
    }

    public int[] getPoints()
    {
        return mPoints;
    }

    public void setPoints(int[] points)
    {
        mPoints = points;
    }
    
    public Polygon getPolygon()
    {
        int[] xpoints = new int[mPoints.length/2];
        int[] ypoints = new int[mPoints.length/2];
        for (int i = 0; i < xpoints.length; i++)
        {
            xpoints[i] = mPoints[i*2];
            ypoints[i] = mPoints[i*2+1];
        }
        return new Polygon(xpoints, ypoints, xpoints.length);
    }
}