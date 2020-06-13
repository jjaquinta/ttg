package jo.util.jgl.obj.part;

import javax.vecmath.Color4f;

import jo.util.jgl.logic.Color4fLogic;
import jo.util.jgl.obj.tri.JGLObj;

public class JGLObjParticle extends JGLObj
{
    private long mStart;
    private long mEnd;
    private Color4f mStartColor;
    private Color4f mEndColor;
    
    public JGLObjParticle()
    {
        setMode(JGLObj.QUADS);
        setVertices(new float[] {
                -1, -1, 0,
                1, -1, 0,
                1, 1, 0,
                -1, 1, 0,
        });
        setIndices(new short[] {
                0, 1, 2, 3,
        });
        setTextures(new float[] {
            0, 0,
            1, 0,
            1, 1,
            0, 1,
        });
    }
    
    @Override
    public Color4f getTextureColor()
    {
        if (mStartColor == null)
            return super.getTextureColor();
        else if (mEndColor == null)
            return mStartColor;
        else
            return Color4fLogic.interpolate(System.currentTimeMillis() - mStart, 0, mEnd - mStart, mStartColor, mEndColor);
    }

    public long getStart()
    {
        return mStart;
    }

    public void setStart(long start)
    {
        mStart = start;
    }

    public long getEnd()
    {
        return mEnd;
    }

    public void setEnd(long end)
    {
        mEnd = end;
    }

    public Color4f getStartColor()
    {
        return mStartColor;
    }

    public void setStartColor(Color4f startColor)
    {
        mStartColor = startColor;
    }

    public Color4f getEndColor()
    {
        return mEndColor;
    }

    public void setEndColor(Color4f endColor)
    {
        mEndColor = endColor;
    }
}
