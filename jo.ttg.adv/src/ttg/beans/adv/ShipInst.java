/*
 * Created on Jan 5, 2004
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ttg.beans.adv;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.trade.CargoBean;
import jo.ttg.ship.beans.ShipBean;
import jo.ttg.ship.beans.ShipStats;
import jo.util.beans.PCSBean;

/**
 * @author jjaquinta
 *
 *         To change the template for this generated type comment go to
 *         Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ShipInst extends PCSBean
{
    private String              mName;
    private ShipBean            mDesign;
    private ShipStats           mStats;
    private String              mLocation;
    private String              mDestination;
    private List<CargoBean>     mCargo      = new ArrayList<>();
    private List<CrewBean>      mCrew       = new ArrayList<>();
    private List<PassengerBean> mPassengers = new ArrayList<>();
    private double              mFuel;
    private double              mUnrefinedFuel;
    private boolean             mDocked;

    public ShipInst()
    {
    }

    public String toString()
    {
        return mName;
    }

    /**
     * @return
     */
    public List<CargoBean> getCargo()
    {
        return mCargo;
    }

    /**
     * @return
     */
    public List<CrewBean> getCrew()
    {
        return mCrew;
    }

    /**
     * @return
     */
    public ShipBean getDesign()
    {
        return mDesign;
    }

    /**
     * @return
     */
    public double getFuel()
    {
        return mFuel;
    }

    /**
     * @return
     */
    public String getLocation()
    {
        return mLocation;
    }

    /**
     * @return
     */
    public String getName()
    {
        return mName;
    }

    /**
     * @param bean
     */
    public void setDesign(ShipBean bean)
    {
        mDesign = bean;
    }

    /**
     * @param d
     */
    public void setFuel(double d)
    {
        queuePropertyChange("fuel", mFuel, d);
        mFuel = d;
        firePropertyChange();
    }

    /**
     * @param string
     */
    public void setLocation(String string)
    {
        queuePropertyChange("location", mLocation, string);
        mLocation = string;
        firePropertyChange();
    }

    /**
     * @param string
     */
    public void setName(String string)
    {
        queuePropertyChange("name", mName, string);
        mName = string;
        firePropertyChange();
    }

    /**
     * @return
     */
    public String getDestination()
    {
        return mDestination;
    }

    /**
     * @param string
     */
    public void setDestination(String string)
    {
        queuePropertyChange("destination", mDestination, string);
        mDestination = string;
        firePropertyChange();
    }

    /**
     * 
     */
    public void fireCargoChange()
    {
        queuePropertyChange("cargo", null, mCargo);
        firePropertyChange();
    }

    /**
     * 
     */
    public void fireCrewChange()
    {
        queuePropertyChange("crew", null, mCrew);
        firePropertyChange();
    }

    /**
     * 
     */
    public void firePassengersChange()
    {
        queuePropertyChange("passengers", null, mCrew);
        firePropertyChange();
    }

    /**
     * @return Returns the docked.
     */
    public boolean isDocked()
    {
        return mDocked;
    }

    /**
     * @param docked
     *            The docked to set.
     */
    public void setDocked(boolean docked)
    {
        queuePropertyChange("docked", mDocked, docked);
        mDocked = docked;
        firePropertyChange();
    }

    public ShipStats getStats()
    {
        return mStats;
    }

    public void setStats(ShipStats report)
    {
        mStats = report;
    }

    public double getUnrefinedFuel()
    {
        return mUnrefinedFuel;
    }

    public void setUnrefinedFuel(double unrefinedFuel)
    {
        queuePropertyChange("unrefinedFuel", mUnrefinedFuel, unrefinedFuel);
        mUnrefinedFuel = unrefinedFuel;
        firePropertyChange();
    }

    public List<PassengerBean> getPassengers()
    {
        return mPassengers;
    }

    public void setPassengers(List<PassengerBean> passengers)
    {
        mPassengers = passengers;
    }

    public void setCargo(List<CargoBean> cargo)
    {
        mCargo = cargo;
    }

    public void setCrew(List<CrewBean> crew)
    {
        mCrew = crew;
    }
}
