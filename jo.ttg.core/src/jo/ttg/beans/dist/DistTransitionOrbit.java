/*
 * Created on Dec 11, 2004
 *
 */
package jo.ttg.beans.dist;

/**
 * @author Jo
 *
 */
public class DistTransitionOrbit extends DistTransition
{
    private String	mBodyURI;	// should be a body:// URI
    private double		mOriginOrbit;
    private double		mDestinationOrbit;
    private double		mDiameter;
    
    public DistTransitionOrbit()
    {
        setType(ORBIT_TO_ORBIT);
    }
    
    public DistTransitionOrbit(String bodyURI, double originOrbit, double destinationOrbit, double diameter)
    {
        this();
        setBodyURI(bodyURI);
        setOriginOrbit(originOrbit);
        setDestinationOrbit(destinationOrbit);
        setDiameter(diameter);
    }
    
    public String toString()
    {
        return "Orbital transfer at "+getBodyURI()+" from "+getOriginOrbit()+" to "+getDestinationOrbit();
    }
    
    /**
     * @return Returns the bodyURI.
     */
    public String getBodyURI()
    {
        return mBodyURI;
    }
    /**
     * @param bodyURI The bodyURI to set.
     */
    public void setBodyURI(String bodyURI)
    {
        mBodyURI = bodyURI;
    }
    /**
     * @return Returns the destinationOrbit.
     */
    public double getDestinationOrbit()
    {
        return mDestinationOrbit;
    }
    /**
     * @param destinationOrbit The destinationOrbit to set.
     */
    public void setDestinationOrbit(double destinationOrbit)
    {
        mDestinationOrbit = destinationOrbit;
    }
    /**
     * @return Returns the originOrbit.
     */
    public double getOriginOrbit()
    {
        return mOriginOrbit;
    }
    /**
     * @param originOrbit The originOrbit to set.
     */
    public void setOriginOrbit(double originOrbit)
    {
        mOriginOrbit = originOrbit;
    }
    public double getDiameter()
    {
        return mDiameter;
    }
    public void setDiameter(double radius)
    {
        mDiameter = radius;
    }
}
