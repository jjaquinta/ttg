/*
 * Created on Feb 6, 2005
 *
 */
package jo.ttg.core.ui.swing.ctrl;

import java.awt.Color;

import jo.ttg.beans.LocBean;

/**
 * @author Jo
 *
 */
public class Bogey
{
    private LocBean	mLocation;
    private int		mSprite;
    private Color	mColor;
    private String	mText;
    /**
     * @return Returns the color.
     */
    public Color getColor()
    {
        return mColor;
    }
    /**
     * @param color The color to set.
     */
    public void setColor(Color color)
    {
        mColor = color;
    }
    /**
     * @return Returns the location.
     */
    public LocBean getLocation()
    {
        return mLocation;
    }
    /**
     * @param location The location to set.
     */
    public void setLocation(LocBean location)
    {
        mLocation = location;
    }
    /**
     * @return Returns the sprite.
     */
    public int getSprite()
    {
        return mSprite;
    }
    /**
     * @param sprite The sprite to set.
     */
    public void setSprite(int sprite)
    {
        mSprite = sprite;
    }
    /**
     * @return Returns the text.
     */
    public String getText()
    {
        return mText;
    }
    /**
     * @param text The text to set.
     */
    public void setText(String text)
    {
        mText = text;
    }
}
