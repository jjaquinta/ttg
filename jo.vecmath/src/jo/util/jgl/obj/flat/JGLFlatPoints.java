package jo.util.jgl.obj.flat;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;
import javax.vecmath.Point2f;

import jo.util.jgl.obj.JGLNode;

public class JGLFlatPoints extends JGLNode
{
    private List<Point2f> mLocations;
    private float  mRadius;
    private Color4f mColor;
    private boolean mAntiAlias;
    
    public JGLFlatPoints()
    {
        mLocations = new ArrayList<Point2f>();
        mRadius = 1;
        mAntiAlias = true;
    }

    public float getRadius()
    {
        return mRadius;
    }

    public void setRadius(float radius)
    {
        mRadius = radius;
    }

    public Color4f getColor()
    {
        return mColor;
    }

    public void setColor(Color4f color)
    {
        mColor = color;
    }

    public boolean isAntiAlias()
    {
        return mAntiAlias;
    }

    public void setAntiAlias(boolean antiAlias)
    {
        mAntiAlias = antiAlias;
    }

    public List<Point2f> getLocations()
    {
        return mLocations;
    }

    public void setLocations(List<Point2f> locations)
    {
        mLocations = locations;
    }
}
