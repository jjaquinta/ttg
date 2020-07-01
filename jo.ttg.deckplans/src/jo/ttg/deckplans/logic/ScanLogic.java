package jo.ttg.deckplans.logic;

import java.io.File;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.logic.plan.ShipPlanLogic;
import jo.ttg.ship.logic.plan.ShipPlanScanMTLogic;
import jo.util.utils.ThreadHelper;
import jo.util.utils.io.FileUtils;

public class ScanLogic
{
    private static void read(JSONObject json)
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        scan.fromJSON(json);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("shipScan", scan);
    }


    public static void read(File scanFile)
    {
        try
        {
            JSONObject json = JSONUtils.readJSON(scanFile);
            read(json);
            RuntimeLogic.getInstance().setLastScan(scanFile);
            RuntimeLogic.getInstance().setDirty(false);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
    }

    public static void read(String mtText)
    {
        ShipScanBean newScan = ShipPlanScanMTLogic.scanMT(mtText);
        JSONObject json = newScan.toJSON();
        read(json);
        RuntimeLogic.getInstance().setDirty(true);
    }

    public static void read(LibEntryBean lib)
    {
        JSONObject json = lib.getJSON();
        read(json);
        RuntimeLogic.getInstance().setDirty(true);
    }

    public static void write(File scanFile)
    {
        try
        {
            JSONObject json = RuntimeLogic.getInstance().getShipScan().toJSON();
            FileUtils.writeFile(json.toJSONString(), scanFile);
            RuntimeLogic.getInstance().setLastScan(scanFile);
            RuntimeLogic.setStatus("Saved as "+scanFile);
            RuntimeLogic.getInstance().setDirty(false);
        }
        catch (IOException e)
        {
            RuntimeLogic.setStatus(e.toString());
            return;
        }
    }

    public static void setConfiguration(int value)
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        if (scan.getConfiguration() != value)
        {
            scan.setConfiguration(value);
            RuntimeLogic.getInstance().setDirty(true);
        }
    }

    public static void setVolume(int value)
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        if (scan.getVolume() != value)
        {
            scan.setVolume(value);
            RuntimeLogic.getInstance().setDirty(true);
        }
    }

    public static ThreadHelper process()
    {
        ThreadHelper t = new ThreadHelper("Creating Deckplan") { public void run() { doProcess(); } };
        t.start();
        return t;
    }
    
    private static void doProcess()
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        ShipPlanBean newPlan = ShipPlanLogic.generateFrame(scan);
        if (ThreadHelper.isCanceled())
            return;
        JSONObject json = newPlan.toJSON();
        ShipPlanBean plan = RuntimeLogic.getInstance().getShipPlan();
        plan.fromJSON(json);
        RuntimeLogic.getInstance().fireMonotonicPropertyChange("shipPlan");
        RuntimeLogic.getInstance().setDirty(true);
        RuntimeLogic.setMode(RuntimeBean.PLAN);
    }
}
