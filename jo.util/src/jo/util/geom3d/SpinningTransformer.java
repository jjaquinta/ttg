package jo.util.geom3d;

/*
 * If angle == 0, then rotation is in euler
 * If angle > 0, then rotation is the axis, and angle is the angle
 */
public class SpinningTransformer implements ITransformer
{
    private Point3D mRotation;  // per second
    private double  mAngle;
    private long    mStartTime;
    
    public SpinningTransformer()
    {
        mRotation = new Point3D();
        mStartTime = System.currentTimeMillis();
    }
    
    public SpinningTransformer(Point3D rotation)
    {
        mRotation = rotation;
        mStartTime = System.currentTimeMillis();
    }
    
    public SpinningTransformer(Point3D rotation, double angle)
    {
        mRotation = rotation;
        mAngle = angle;
        mStartTime = System.currentTimeMillis();
    }
    
    public SpinningTransformer(Point3D rotation, long startTime)
    {
        mRotation = rotation;
        mStartTime = startTime;
    }
    
    public SpinningTransformer(Point3D rotation, double angle, long startTime)
    {
        mRotation = rotation;
        mAngle = angle;
        mStartTime = startTime;
    }

    @Override
    public Transform3D calcTransform(Transform3D transform)
    {
        Transform3D t;
        long now = System.currentTimeMillis();
        if (now > mStartTime)
        {
            t = new Transform3D(transform);
            float seconds = (now - mStartTime)/1000.0f;
            if (Point3DLogic.equals(mAngle, 0))
                t.rotateEuler(Point3DLogic.mult(mRotation, seconds));
            else
            {             
                t = new Transform3D();
                t.rotate(mRotation, mAngle*seconds);
                t.mult(transform);
            }
        }
        else
            t = transform;
        return t;
    }

    public Point3D getRotation()
    {
        return mRotation;
    }

    public void setRotation(Point3D rotation)
    {
        mRotation = rotation;
    }

    public long getStartTime()
    {
        return mStartTime;
    }

    public void setStartTime(long startTime)
    {
        mStartTime = startTime;
    }

    public double getAngle()
    {
        return mAngle;
    }

    public void setAngle(double angle)
    {
        mAngle = angle;
    }

}
