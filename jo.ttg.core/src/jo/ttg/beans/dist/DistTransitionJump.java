/*
 * Created on Dec 11, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package jo.ttg.beans.dist;

/**
 * @author Jo
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DistTransitionJump extends DistTransition
{
    private String	mOriginURI;		// should be a sys:// URI
    private String	mDestinationURI;// should be a sys:// URI
    private double	mDistance;
    private double	mTimeLeft; // in minutes
    
    public DistTransitionJump()
    {
        setType(STAR_TO_STAR);
    }
    
    public DistTransitionJump(String originURI, String destinationURI, double dist, double timeLeft)
    {
        setType(STAR_TO_STAR);
        setOriginURI(originURI);
        setDestinationURI(destinationURI);
        setDistance(dist);
        setTimeLeft(timeLeft);
    }
    
    public String toString()
    {
        return "Jump from "+getOriginURI()+" to "+getDestinationURI();
    }
   
    /**
     * @return Returns the destinationURI.
     */
    public String getDestinationURI()
    {
        return mDestinationURI;
    }
    /**
     * @param destinationURI The destinationURI to set.
     */
    public void setDestinationURI(String destinationURI)
    {
        mDestinationURI = destinationURI;
    }
    /**
     * @return Returns the originURI.
     */
    public String getOriginURI()
    {
        return mOriginURI;
    }
    /**
     * @param originURI The originURI to set.
     */
    public void setOriginURI(String originURI)
    {
        mOriginURI = originURI;
    }
    public double getDistance()
    {
        return mDistance;
    }
    public void setDistance(double distance)
    {
        mDistance = distance;
    }
    public double getTimeLeft()
    {
        return mTimeLeft;
    }
    public void setTimeLeft(double timeLeft)
    {
        mTimeLeft = timeLeft;
    }
}
    