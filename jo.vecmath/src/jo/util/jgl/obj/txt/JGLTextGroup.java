package jo.util.jgl.obj.txt;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Color4f;

public class JGLTextGroup
{
    private boolean mCull;
    private String  mFace;
    private int     mStyle;
    private int     mSize;
    private Color4f mColor;
    private List<JGLText2D> mTexts;
    
    public JGLTextGroup()
    {
        mTexts = new ArrayList<JGLText2D>();
    }
    
    public void add(JGLText2D text)
    {
        synchronized (this)
        {
            mTexts.add(text);
        }
    }
    
    public void remove(JGLText2D text)
    {
        synchronized (this)
        {
            mTexts.remove(text);
        }
    }
    
    public String getFace()
    {
        return mFace;
    }
    public void setFace(String face)
    {
        mFace = face;
    }
    public int getStyle()
    {
        return mStyle;
    }
    public void setStyle(int style)
    {
        mStyle = style;
    }
    public int getSize()
    {
        return mSize;
    }
    public void setSize(int size)
    {
        mSize = size;
    }
    public List<JGLText2D> getTexts()
    {
        return mTexts;
    }
    public void setTexts(List<JGLText2D> texts)
    {
        mTexts = texts;
    }

    public boolean isCull()
    {
        return mCull;
    }

    public void setCull(boolean cull)
    {
        mCull = cull;
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
