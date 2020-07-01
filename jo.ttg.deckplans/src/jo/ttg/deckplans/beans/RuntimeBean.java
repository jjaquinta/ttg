package jo.ttg.deckplans.beans;

import java.io.File;

import org.json.simple.JSONObject;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.util.beans.PCSBean;

public class RuntimeBean extends PCSBean
{
    public static final int SCAN      = 0;
    public static final int PLAN      = 1;
    public static final int DECK      = 2;

    private JSONObject      mSettings;
    private int             mMode;
    private String          mStatus;
    private File            mLastPlan;
    private File            mLastScan;
    private File            mLastDeck;
    private boolean         mDirty;
    private ShipScanBean    mShipScan = new ShipScanBean();
    private ShipPlanBean    mShipPlan = new ShipPlanBean();
    private ShipDeckBean    mShipDeck = new ShipDeckBean();

    // constructor
    public RuntimeBean()
    {
        mShipDeck.setPlan(mShipPlan);
    }

    // getters and setters

    public int getMode()
    {
        return mMode;
    }

    public void setMode(int mode)
    {
        queuePropertyChange("mode", mMode, mode);
        mMode = mode;
        firePropertyChange();
    }

    public String getStatus()
    {
        return mStatus;
    }

    public void setStatus(String status)
    {
        queuePropertyChange("status", mStatus, status);
        mStatus = status;
        firePropertyChange();
    }

    public JSONObject getSettings()
    {
        return mSettings;
    }

    public void setSettings(JSONObject settings)
    {
        mSettings = settings;
    }

    public boolean isDirty()
    {
        return mDirty;
    }

    public void setDirty(boolean dirty)
    {
        queuePropertyChange("dirty", mDirty, dirty);
        mDirty = dirty;
        firePropertyChange();
    }

    public File getLastPlan()
    {
        return mLastPlan;
    }

    public void setLastPlan(File lastPlan)
    {
        mLastPlan = lastPlan;
    }

    public File getLastScan()
    {
        return mLastScan;
    }

    public void setLastScan(File lastScan)
    {
        mLastScan = lastScan;
    }

    public File getLastDeck()
    {
        return mLastDeck;
    }

    public void setLastDeck(File lastDeck)
    {
        mLastDeck = lastDeck;
    }

    public ShipScanBean getShipScan()
    {
        return mShipScan;
    }

    public void setShipScan(ShipScanBean shipScan)
    {
        queuePropertyChange("shipScan", mShipScan, shipScan);
        mShipScan = shipScan;
        firePropertyChange();
    }

    public ShipPlanBean getShipPlan()
    {
        return mShipPlan;
    }

    public void setShipPlan(ShipPlanBean shipPlan)
    {
        queuePropertyChange("shipPlan", mShipPlan, shipPlan);
        mShipPlan = shipPlan;
        firePropertyChange();
    }

    public ShipDeckBean getShipDeck()
    {
        return mShipDeck;
    }

    public void setShipDeck(ShipDeckBean shipDeck)
    {
        queuePropertyChange("shipDeck", mShipDeck, shipDeck);
        mShipDeck = shipDeck;
        firePropertyChange();
    }
}
