package jo.util.jgl.obj.flat;

import javax.vecmath.Color4f;
import javax.vecmath.Point2f;

import jo.util.jgl.obj.JGLNode;

public class JGLFlatRect extends JGLNode
{
    private Point2f mUpperLeft;
    private Point2f mLowerRight;
    private Color4f mColor;
    
    public Point2f getUpperLeft()
    {
        return mUpperLeft;
    }
    public void setUpperLeft(Point2f upperLeft)
    {
        mUpperLeft = upperLeft;
    }
    public Point2f getLowerRight()
    {
        return mLowerRight;
    }
    public void setLowerRight(Point2f lowerRight)
    {
        mLowerRight = lowerRight;
    }
    public Color4f getColor()
    {
        return mColor;
    }
    public void setColor(Color4f color)
    {
        mColor = color;
    }
}
