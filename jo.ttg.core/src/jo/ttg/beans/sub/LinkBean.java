package jo.ttg.beans.sub;

import jo.ttg.beans.OrdBean;
import jo.util.beans.Bean;

public class LinkBean extends Bean
{
    // Text
    private java.lang.String mText;
    public java.lang.String getText()
    {
        return mText;
    }
    public void setText(java.lang.String v)
    {
        mText = v;
    }

    // Type
    private int mType;
    public int getType()
    {
        return mType;
    }
    public void setType(int v)
    {
        mType = v;
    }

    // Origin
    private OrdBean mOrigin;
    public OrdBean getOrigin()
    {
        return mOrigin;
    }
    public void setOrigin(OrdBean v)
    {
        mOrigin = v;
    }

    // Destination
    private OrdBean mDestination;
    public OrdBean getDestination()
    {
        return mDestination;
    }
    public void setDestination(OrdBean v)
    {
        mDestination = v;
    }


    // constructor
    public LinkBean()
    {
        mText = new java.lang.String();
        mType = 0;
        mOrigin = new OrdBean();
        mDestination = new OrdBean();
    }

    // utils

    public String toString()
    {
        return mOrigin+"-"+mText+"->"+mDestination;
    }
}
