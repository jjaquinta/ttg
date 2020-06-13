package jo.util.jgl.obj;

import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Matrix3f;
import javax.vecmath.Matrix4f;
import javax.vecmath.Point3f;
import javax.vecmath.Tuple3f;
import javax.vecmath.Vector3f;

import jo.util.jgl.logic.ITransformer;

public class JGLNode
{
    private String      mID;
    protected Matrix4f mTransform;
    protected Matrix3f mRotation;
    protected Vector3f mTranslation;
    protected float    mScale;
    private ITransformer mTransformer;
    private JGLNode     mParent;
    private boolean     mCull;
    private boolean     mInitialized;
    private Point3f     mScreen;
    private Point3f     mScreenLowBounds;
    private Point3f     mScreenHighBounds;
    protected Point3f   mLowBounds;
    protected Point3f   mHighBounds;
    private Map<Object, Object> mData;

    public JGLNode()
    {
        mTransform = new Matrix4f();
        mTransform.setIdentity();
        mRotation = new Matrix3f();
        mTranslation = new Vector3f();
        decomposeTransform();
    }
    
    public void recycle()
    {
    }
    
    public void decomposeTransform()
    {
        mScale = mTransform.get(mRotation, mTranslation);
    }
    
    public void composeTransform()
    {
        mTransform.set(mRotation, mTranslation, mScale);
    }
    
    public Vector3f getLeftDir()
    {
        getRotation();
        Vector3f v = new Vector3f();
        mRotation.getColumn(0, v);
        return v;
    }
    
    public void setLeftDir(Tuple3f v)
    {
        getRotation();
        mRotation.setColumn(0, v.x, v.y, v.z);
        setRotation(mRotation);
    }
    
    public Vector3f getUpDir()
    {
        getRotation();
        Vector3f v = new Vector3f();
        mRotation.getColumn(1, v);
        return v;
    }
    
    public void setUpDir(Tuple3f v)
    {
        getRotation();
        mRotation.setColumn(1, v.x, v.y, v.z);
        setRotation(mRotation);
    }
    
    public Vector3f getForwardDir()
    {
        getRotation();
        Vector3f v = new Vector3f();
        mRotation.getColumn(2, v);
        return v;
    }
    
    public void setForwardDir(Tuple3f v)
    {
        getRotation();
        mRotation.setColumn(2, v.x, v.y, v.z);
        setRotation(mRotation);
    }
    
    public void setData(Object key, Object val)
    {
        if (mData == null)
            mData = new HashMap<Object, Object>();
        mData.put(key, val);
    }
    
    public Object getData(Object key)
    {
        if (mData == null)
            return null;
        return mData.get(key);
    }
    
    public String toString()
    {
        return mID;
    }
    
    public void init()
    {        
        mInitialized = true;
    }
    
    public Matrix4f calcTransform(long tick)
    {
        Matrix4f t = new Matrix4f(getTransform());
        if (mTransformer != null)
            t = mTransformer.calcTransform(t);
        //if (mParent != null)
        //    t.mult(mParent.calcTransform(tick));
        return t;
    }

    public String getID()
    {
        return mID;
    }

    public void setID(String iD)
    {
        mID = iD;
    }

    public Matrix4f getTransform()
    {
        return mTransform;
    }

    public void setTransform(Matrix4f transform)
    {
        mTransform = transform;
    }

    public JGLNode getParent()
    {
        return mParent;
    }

    public void setParent(JGLNode parent)
    {
        mParent = parent;
    }

    public boolean isCull()
    {
        return mCull;
    }

    public void setCull(boolean cull)
    {
        mCull = cull;
    }

    public ITransformer getTransformer()
    {
        return mTransformer;
    }

    public void setTransformer(ITransformer transforms)
    {
        mTransformer = transforms;
    }

    public boolean isInitialized()
    {
        return mInitialized;
    }

    public void setInitialized(boolean initialized)
    {
        mInitialized = initialized;
    }

    public Point3f getScreenLowBounds()
    {
        return mScreenLowBounds;
    }

    public void setScreenLowBounds(Point3f screenLowBounds)
    {
        mScreenLowBounds = screenLowBounds;
    }

    public Point3f getScreenHighBounds()
    {
        return mScreenHighBounds;
    }

    public void setScreenHighBounds(Point3f screenHighBounds)
    {
        mScreenHighBounds = screenHighBounds;
    }

    public Point3f getLowBounds()
    {
        return mLowBounds;
    }

    public void setLowBounds(Point3f lowBounds)
    {
        mLowBounds = lowBounds;
    }

    public Point3f getHighBounds()
    {
        return mHighBounds;
    }

    public void setHighBounds(Point3f highBounds)
    {
        mHighBounds = highBounds;
    }

    public Point3f getScreen()
    {
        return mScreen;
    }

    public void setScreen(Point3f screen)
    {
        mScreen = screen;
    }

    public Matrix3f getRotation()
    {
        decomposeTransform();
        return mRotation;
    }

    public void setRotation(Matrix3f rotation)
    {
        mRotation = rotation;
        composeTransform();
    }

    public Vector3f getTranslation()
    {
        decomposeTransform();
        return mTranslation;
    }

    public void setTranslation(Vector3f translation)
    {
        mTranslation = translation;
        composeTransform();
    }

    public float getScale()
    {
        decomposeTransform();
        return mScale;
    }

    public void setScale(float scale)
    {
        mScale = scale;
        composeTransform();
    }
}
