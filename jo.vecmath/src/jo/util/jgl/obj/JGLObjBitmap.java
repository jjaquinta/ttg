package jo.util.jgl.obj;

import jo.util.jgl.obj.tri.JGLObj;

public class JGLObjBitmap extends JGLObj
{
    private int         mImageID;
    private int         mLeft;
    private int         mTop;
    private int         mWidth;
    private int         mHeight;
    
    public JGLObjBitmap(int imageID, int left, int top, int width, int height)
    {
        mImageID = imageID;
        mLeft = left;
        mTop = top;
        mWidth = width;
        mHeight = height;
    }

    public int getLeft()
    {
        return mLeft;
    }

    public void setLeft(int left)
    {
        mLeft = left;
    }

    public int getTop()
    {
        return mTop;
    }

    public void setTop(int top)
    {
        mTop = top;
    }

    public int getWidth()
    {
        return mWidth;
    }

    public void setWidth(int width)
    {
        mWidth = width;
    }

    public int getHeight()
    {
        return mHeight;
    }

    public void setHeight(int height)
    {
        mHeight = height;
    }

    public int getImageID()
    {
        return mImageID;
    }

    public void setImageID(int imageID)
    {
        mImageID = imageID;
    }
}
