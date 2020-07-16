package jo.ttg.deckplans.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.vecmath.Point3i;

import org.json.simple.JSONObject;
import org.json.simple.JSONUtils;

import jo.ttg.deckplans.beans.LibEntryBean;
import jo.ttg.deckplans.beans.RuntimeBean;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.ShipPlanHullLogic;
import jo.ttg.ship.logic.plan.ShipPlanLogic;
import jo.ttg.ship.logic.plan.ShipPlanScanMTLogic;
import jo.util.geom3d.Point3D;
import jo.util.utils.MathUtils;
import jo.util.utils.ThreadHelper;
import jo.util.utils.io.FileUtils;
import jo.vecmath.data.SparseMatrix;

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

    public static void setOrientation(int value)
    {
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        if (scan.getOrientation() != value)
        {
            scan.setOrientation(value);
            RuntimeLogic.getInstance().setDirty(true);
        }
    }

    public static void setAspectRatio(double x, double y, double z)
    {
        Point3D ar = new Point3D(x, y, z);
        ShipScanBean scan = RuntimeLogic.getInstance().getShipScan();
        if (scan.getAspectRatio().dist(ar) > .5)
        {
            scan.setAspectRatio(ar);
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
    
    private static int BLACK = 0xFF000000;
    private static int WHITE = 0x00FFFFFF;
    
    public static BufferedImage scanToProfile(ShipScanBean scan)
    {
        ShipPlanBean plan = new ShipPlanBean();
        ShipPlanHullLogic.generateHull(plan, scan.getVolume(), scan.getConfiguration(), scan.getAspectRatio(), scan.getOrientation());
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        int xw = upper.x - lower.x + 1;
        int yw = upper.y - lower.y + 1;
        int zw = upper.z - lower.z + 1;
        BufferedImage img = new BufferedImage(2 + yw + 2 + zw*2 + 2, 2 + xw + 2 + zw*2 + 2, BufferedImage.TYPE_INT_ARGB);
        for (int x = lower.x; x <= upper.x; x++)
            for (int y = lower.y; y <= upper.y; y++)
                img.setRGB(2 + y - lower.y, 2 + x - lower.x, isAnySet(plan.getSquares(), x, x, y, y, lower.z, upper.z));
        for (int x = lower.x; x <= upper.x; x++)
            for (int z = lower.z; z <= upper.z; z++)
            {
                int c = isAnySet(plan.getSquares(), x, x, lower.y, upper.y, z, z);
                img.setRGB(2 + yw + 2 + (z - lower.z)*2  , 2 + x - lower.x, c);
                img.setRGB(2 + yw + 2 + (z - lower.z)*2+1, 2 + x - lower.x, c);
            }
        for (int y = lower.y; y <= upper.y; y++)
            for (int z = lower.z; z <= upper.z; z++)
            {
                int c = isAnySet(plan.getSquares(), lower.x, upper.x, y, y, z, z);
                img.setRGB(2 + y - lower.y, 2 + xw + 2 + (z - lower.z)*2, c);
                img.setRGB(2 + y - lower.y, 2 + xw + 2 + (z - lower.z)*2+1, c);
            }
        return img;
    }


    private static int isAnySet(SparseMatrix<ShipSquareBean> squares, int x1,
            int x2, int y1, int y2, int z1, int z2)
    {
        for (int x = x1; x <= x2; x++)
            for (int y = y1; y <= y2; y++)
                for (int z = z1; z <= z2; z++)
                    if (squares.contains(x, y, z))
                    {
                        double t;
                        if (x1 != x2)
                            t = MathUtils.interpolate(x, x1, x2, 0, 1);
                        else if (y1 != y2)
                            t = MathUtils.interpolate(y, y1, y2, 0, 1);
                        else //if (z1 != z2)
                            t = MathUtils.interpolate(z, z1, z2, 0, 1);
                        int color = (int)(255*t);
                        return BLACK|(color)|(color<<8)|(color<<16);
                    }
        return WHITE;
    }
    
    
}
