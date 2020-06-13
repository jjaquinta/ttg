package jo.util.jgl.obj;

import javax.vecmath.Color3f;

import jo.util.jgl.JGLUtils;
import jo.util.jgl.obj.tri.JGLObj;

public class JGLObjMesh extends JGLObj
{
    private int          mMeshID;
    private Color3f      mBaseColor;
    private Color3f      mDeltaColor;
    
    public JGLObjMesh(int meshID, Color3f baseColor, Color3f deltaColor)
    {
        setMeshID(meshID);
        setBaseColor(baseColor);
        setDeltaColor(deltaColor);
    }
    public JGLObjMesh(int meshID, int textureID)
    {
        setMeshID(meshID);
        setTextureID(textureID);
    }
    
    @Override
    public void init()
    {
        if (mBaseColor != null)
            setColors(JGLUtils.rndColors(getIndices(), mBaseColor, mDeltaColor));
        super.init();
    }

    public int getMeshID()
    {
        return mMeshID;
    }

    public void setMeshID(int meshID)
    {
        mMeshID = meshID;
    }

    public Color3f getBaseColor()
    {
        return mBaseColor;
    }

    public void setBaseColor(Color3f baseColor)
    {
        mBaseColor = baseColor;
    }

    public Color3f getDeltaColor()
    {
        return mDeltaColor;
    }

    public void setDeltaColor(Color3f deltaColor)
    {
        mDeltaColor = deltaColor;
    }
}
