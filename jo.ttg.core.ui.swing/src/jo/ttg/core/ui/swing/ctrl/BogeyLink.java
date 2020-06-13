/*
 * Created on Feb 6, 2005
 *
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;

/**
 * @author Jo
 *
 */
public class BogeyLink
{
    private Bogey	mSource;
    private Bogey	mTarget;
    private int		mSprite;
    private Color	mColor;
    public Color getColor()
    {
        return mColor;
    }
    public void setColor(Color color)
    {
        mColor = color;
    }
    public Bogey getSource()
    {
        return mSource;
    }
    public void setSource(Bogey source)
    {
        mSource = source;
    }
    public int getSprite()
    {
        return mSprite;
    }
    public void setSprite(int sprite)
    {
        mSprite = sprite;
    }
    public Bogey getTarget()
    {
        return mTarget;
    }
    public void setTarget(Bogey target)
    {
        mTarget = target;
    }
}
