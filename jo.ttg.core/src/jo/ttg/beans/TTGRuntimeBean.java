package jo.ttg.beans;

import java.util.HashMap;
import java.util.Map;

import jo.ttg.gen.IGenScheme;
import jo.util.beans.PCSBean;

public class TTGRuntimeBean extends PCSBean
{
    private DateBean    mDate;
    private IGenScheme  mScheme;
    private String      mLocationURI;
    private URIBean     mLocation;
    private String      mSelectionURI;
    private URIBean     mSelection;
    private int         mJump;
    private double      mManeuver;
    private Map<String, Object> mProps;
    
    public TTGRuntimeBean()
    {
        mDate = new DateBean();
        mDate.setYear(1110);
        mJump = 2;
        mManeuver = 2;
        mProps = new HashMap<String, Object>();
    }
    
    public DateBean getDate()
    {
        return mDate;
    }
    public void setDate(DateBean date)
    {
        queuePropertyChange("date", mDate, date);
        mDate = date;
        firePropertyChange();
    }
    public URIBean getLocation()
    {
        return mLocation;
    }
    public void setLocation(URIBean location)
    {
        queuePropertyChange("location", mLocation, location);
        mLocation = location;
        firePropertyChange();
    }
    public String getLocationURI()
    {
        return mLocationURI;
    }
    public void setLocationURI(String locationURI)
    {
        queuePropertyChange("locationURI", mLocationURI, locationURI);
        mLocationURI = locationURI;
        firePropertyChange();
    }
    public URIBean getSelection()
    {
        return mSelection;
    }
    public void setSelection(URIBean selection)
    {
        queuePropertyChange("selection", mSelection, selection);
        mSelection = selection;
        firePropertyChange();
    }
    public String getSelectionURI()
    {
        return mSelectionURI;
    }
    public void setSelectionURI(String selectionURI)
    {
        queuePropertyChange("selectionURI", mSelectionURI, selectionURI);
        mSelectionURI = selectionURI;
        firePropertyChange();
    }
    public IGenScheme getScheme()
    {
        return mScheme;
    }
    public void setScheme(IGenScheme scheme)
    {
        queuePropertyChange("scheme", mScheme, scheme);
        mScheme = scheme;
        firePropertyChange();
    }

    public int getJump()
    {
        return mJump;
    }

    public void setJump(int jump)
    {
        queuePropertyChange("jump", mJump, jump);
        mJump = jump;
        firePropertyChange();
    }

    public double getManeuver()
    {
        return mManeuver;
    }

    public void setManeuver(double maneuver)
    {
        mManeuver = maneuver;
    }

    public Map<String, Object> getProps()
    {
        return mProps;
    }

    public void setProps(Map<String, Object> props)
    {
        mProps = props;
    }
}
