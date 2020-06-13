package jo.util.jgl.obj;

import javax.vecmath.Point3f;
import javax.vecmath.Point4f;

public class JGLLight extends JGLNode
{
    private Point4f  mAmbient;
    private Point4f  mDiffuse;
    private Point4f  mSpecular;
    private Point4f  mPosition;
    private Point3f  mSpotDirection;
    private float    mSpotExponent;
    private float    mSpotCutoff;
    private float    mConstantAttenuation;
    private float    mLinearAttenuation;
    private float    mQuadraticAttenuation;
    
    public JGLLight()
    {
        mSpotExponent = 0;
        mSpotCutoff = 180;
        mConstantAttenuation = 1;
    }
    
    public Point4f getAmbient()
    {
        return mAmbient;
    }
    public void setAmbient(Point4f ambient)
    {
        mAmbient = ambient;
    }
    public Point4f getDiffuse()
    {
        return mDiffuse;
    }
    public void setDiffuse(Point4f diffuse)
    {
        mDiffuse = diffuse;
    }
    public Point4f getSpecular()
    {
        return mSpecular;
    }
    public void setSpecular(Point4f specular)
    {
        mSpecular = specular;
    }
    public Point4f getPosition()
    {
        return mPosition;
    }
    public void setPosition(Point4f position)
    {
        mPosition = position;
    }
    public Point3f getSpotDirection()
    {
        return mSpotDirection;
    }
    public void setSpotDirection(Point3f spotDirection)
    {
        mSpotDirection = spotDirection;
    }
    public float getSpotExponent()
    {
        return mSpotExponent;
    }
    public void setSpotExponent(float spotExponent)
    {
        mSpotExponent = spotExponent;
    }
    public float getSpotCutoff()
    {
        return mSpotCutoff;
    }
    public void setSpotCutoff(float spotCutoff)
    {
        mSpotCutoff = spotCutoff;
    }
    public float getConstantAttenuation()
    {
        return mConstantAttenuation;
    }
    public void setConstantAttenuation(float constantAttenuation)
    {
        mConstantAttenuation = constantAttenuation;
    }
    public float getLinearAttenuation()
    {
        return mLinearAttenuation;
    }
    public void setLinearAttenuation(float linearAttenuation)
    {
        mLinearAttenuation = linearAttenuation;
    }
    public float getQuadraticAttenuation()
    {
        return mQuadraticAttenuation;
    }
    public void setQuadraticAttenuation(float quadraticAttenuation)
    {
        mQuadraticAttenuation = quadraticAttenuation;
    }
}
