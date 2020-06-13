package jo.util.geom2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Polygon2D
{
    private List<Point2D> mPoints = new ArrayList<>();
    
    // constructors
    
    public Polygon2D()
    {        
    }
    
    public Polygon2D(List<Point2D> points)
    {        
        mPoints.addAll(points);
    }
    
    public Polygon2D(Point2D... points)
    {        
        for (Point2D p : points)
            mPoints.add(p);
    }
    
    // utilities
    
    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer("{");
        for (int i = 0; i < mPoints.size(); i++)
        {
            if (i > 0)
                sb.append(", ");
            sb.append(mPoints.get(i).toString());
        }
        sb.append("}");
        return sb.toString();
    }
    
    public int size()
    {
        return mPoints.size();
    }
    
    public Point2D p(int n)
    {
        return mPoints.get((n+mPoints.size())%mPoints.size());
    }
    
    public Rectangle2D bounds()
    {
        return Polygon2DLogic.bounds(this);
    }
    
    public void setWinding(int winding)
    {
        if (winding != Polygon2DLogic.winding(this))
            Collections.reverse(mPoints);
    }
    
    public boolean contains(Point2D p)
    {
        return Polygon2DLogic.contains(this, p);
    }
    
    public boolean contains(Line2D l)
    {
        return Polygon2DLogic.contains(this, l.p1) && Polygon2DLogic.contains(this, l.p2);
    }
    
    // getters and setters

    public List<Point2D> getPoints()
    {
        return mPoints;
    }

    public void setPoints(List<Point2D> points)
    {
        mPoints = points;
    }
}
