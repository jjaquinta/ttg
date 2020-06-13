package jo.util.jgl.obj;

import javax.vecmath.Matrix4f;

import jo.util.jgl.logic.TransformEye;


public class JGLCamera extends JGLGroup
{
    public JGLCamera()
    {
        super();
        mTransform = new TransformEye(mTransform);
    }
    
    @Override
    public void setTransform(Matrix4f transform)
    {        
        if (!(transform instanceof TransformEye))
            transform = new TransformEye(transform);
        super.setTransform(transform);
    }
    
    public TransformEye getCamera()
    {
        return (TransformEye)getTransform();
    }
}
