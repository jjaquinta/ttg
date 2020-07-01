package jo.ttg.ship.logic.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipScanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.place.Amorphous2Strategy;
import jo.ttg.ship.logic.plan.place.AmorphousStrategy;
import jo.ttg.ship.logic.plan.place.AxialStrategy;
import jo.ttg.ship.logic.plan.place.FillStrategy;
import jo.ttg.ship.logic.plan.place.SurfaceStrategy;
import jo.util.utils.ThreadHelper;

public class ShipPlanLogic
{
    public static boolean debug = false;
    
    private static Map<Integer, IPlacementStrategy> mStrategies = new HashMap<Integer, IPlacementStrategy>();
    static
    {
//        mStrategies.put(ShipSquareBean.MANEUVER, new AmorphousStrategy(new int[] { FillStrategy.Y, FillStrategy.X, FillStrategy.Z },
//                new int[] { FillStrategy.PLUS, FillStrategy.CENTER, FillStrategy.CENTER}, 2));
        mStrategies.put(ShipSquareBean.MANEUVER, new Amorphous2Strategy(new Point3i(0,10,0)));
//        mStrategies.put(ShipSquareBean.JUMP, new AmorphousStrategy(new int[] { FillStrategy.X, FillStrategy.Y, FillStrategy.Z },
//                new int[] { FillStrategy.BOTHQ, FillStrategy.HIGHQ, FillStrategy.CENTER}));
        mStrategies.put(ShipSquareBean.JUMP, new Amorphous2Strategy(new Point3i(10,3,0), new Point3i(-10,3,0)));
//        mStrategies.put(ShipSquareBean.POWER, new AmorphousStrategy(new int[] { FillStrategy.Y, FillStrategy.X, FillStrategy.Z },
//                new int[] { FillStrategy.HIGHQ, FillStrategy.CENTER, FillStrategy.CENTER}));
        mStrategies.put(ShipSquareBean.POWER, new Amorphous2Strategy(new Point3i(0,4,0)));
        mStrategies.put(ShipSquareBean.CARGO, new AmorphousStrategy(new int[] { FillStrategy.Z, FillStrategy.X, FillStrategy.Y },
                new int[] { FillStrategy.PLUS, FillStrategy.NONE, FillStrategy.NONE}, new Point3i(3,3,1)));
        //mStrategies.put(ShipSquareBean.CARGO, new Amorphous2Strategy(new Point3i(0,-4,40), new Point3i(0,0,40), new Point3i(0,4,40)));
        mStrategies.put(ShipSquareBean.FUEL, new AmorphousStrategy(new int[] { FillStrategy.X, FillStrategy.Z, FillStrategy.Y },
                new int[] { FillStrategy.OUTSIDE, FillStrategy.OUTSIDE, FillStrategy.OUTSIDE}));
        mStrategies.put(ShipSquareBean.TURRET, new SurfaceStrategy(new double[] {3, 1.5, 3}));
        mStrategies.put(ShipSquareBean.BAY, new SurfaceStrategy(new double[] {3, 6, 3}));
        mStrategies.put(ShipSquareBean.SPINE, new AxialStrategy(AxialStrategy.Y_AXIS, 4));
        mStrategies.put(ShipSquareBean.STATEROOM, StateroomLogic.createStrategy());
        mStrategies.put(ShipSquareBean.HANGER, new SurfaceStrategy(new double[] {3, 1.5, 3}, .7f, .9f));
    }
    
    // volume in cubic meters
    public static ShipPlanBean generateFrame(ShipScanBean scan)
    {
        ShipPlanBean plan = new ShipPlanBean();
        generateFrame(scan, plan);
        return plan;
    }
    public static void generateFrame(ShipScanBean scan, ShipPlanBean plan)
    {
        ThreadHelper.setCanCancel(true);
        ThreadHelper.setTotalUnits(18);
        plan.setScan(scan);
        int volume = scan.getVolume();
        int configuration = scan.getConfiguration();
        List<PlanItem> items = scan.getItems();
        ShipPlanHullLogic.generateHull(plan, volume, configuration);        
        int actualVolume = (int)(plan.getSquares().size()*6.75);
        int residual = actualVolume - volume;
        plan.println("Target Volume="+volume+", actual volume="+actualVolume+", Residual="+residual+", "+(int)(residual*100/volume)+"%");
        double used = 0;
        for (PlanItem pi : items)
        {
            double vol = pi.getVolume()*pi.getQuantity();
            used += vol;
            plan.println(pi.toString()+" "+(int)(vol*100/volume)+"%");
        }
        plan.println("Contents volume: "+used+" "+(int)(used*100/volume)+"%");
        ThreadHelper.work(1);

        List<PlanItem> todo = new ArrayList<>(items);
        plan.println("Allocating spine");
        allocate(ShipSquareBean.SPINE, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Axial Corridors");
        AxisLogic.allocateAxialCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating weapons");
        allocate(ShipSquareBean.BAY, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        allocate(ShipSquareBean.TURRET, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Connecting weapons");
        CorridorLogic.allocateCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating maneuver");
        allocate(ShipSquareBean.MANEUVER, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Connecting maneuver");
        CorridorLogic.allocateCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating jump");
        allocate(ShipSquareBean.JUMP, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Connecting jump");
        CorridorLogic.allocateCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating power");
        allocate(ShipSquareBean.POWER, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        //CorridorLogic.dump = true;
        
        plan.println("Connecting power");
        CorridorLogic.allocateCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating staterooms");
        //CorridorLogic.dump = true;
        StateroomLogic.allocateStaterooms(plan, todo);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        //CorridorLogic.dump = false;
        
        plan.println("Allocating hanger");
        allocate(ShipSquareBean.HANGER, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating cargo");
        allocate(ShipSquareBean.CARGO, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Connecting cargo");
        CorridorLogic.allocateCorridors(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        
        plan.println("Allocating fuel");
        allocate(ShipSquareBean.FUEL, todo, plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        if (todo.size() > 0)
            plan.println("Could not handle remaining "+todo.size()+" items");
        
        plateShip(plan);
        ThreadHelper.work(1);
        if (ThreadHelper.isCanceled())
            return;
        //CorridorLogic.removeExcessHatches(plan);
        plan.println("Done Planning");
    }

    private static void allocate(int type, List<PlanItem> allItems, ShipPlanBean plan)
    {
        allocate(type, allItems, plan, 0);
    }
    
    static void allocate(int type, List<PlanItem> allItems, ShipPlanBean plan, int intraCorridor)
    {
        List<PlanItem> items = extractItemsOfType(allItems, type);
        if (items.size() == 0)
            return;
        IPlacementStrategy strategy = mStrategies.get(type);
        if (strategy == null)
            throw new IllegalStateException("No strategy for placing "+type);
        if (intraCorridor == 0)
            strategy.place(plan, items);
        else
        {
            List<List<PlanItem>> splits = splitItems(items, intraCorridor);
            for (List<PlanItem> split : splits)
            {
                strategy.place(plan, split);
                if (ThreadHelper.isCanceled())
                    return;
                CorridorLogic.allocateCorridors(plan);
                if (ThreadHelper.isCanceled())
                    return;
            }
        }
    }
    
    private static List<List<PlanItem>> splitItems(List<PlanItem> items, int size)
    {
        List<List<PlanItem>> splits = new ArrayList<>();
        List<PlanItem> current = new ArrayList<>();
        splits.add(current);
        int currentSize = 0;
        for (PlanItem pi : items)
        {
            int num = pi.getQuantity();
            if (currentSize + num < size)
            {
                current.add(pi);
                currentSize += num;
                continue;
            }
            if (currentSize + num == size)
            {
                current.add(pi);
                current = new ArrayList<>();
                splits.add(current);
                currentSize = 0;
                continue;
            }
            // split item
            PlanItem p1 = new PlanItem(pi);
            p1.setNumber(size - currentSize);
            current.add(p1);
            pi.setNumber(pi.getQuantity() - p1.getQuantity());
            current = new ArrayList<>();
            splits.add(current);
            while (pi.getQuantity() >= size)
            {
                p1 = new PlanItem(pi);
                p1.setNumber(size);
                current.add(p1);
                pi.setNumber(pi.getQuantity() - size);
                current = new ArrayList<>();
                splits.add(current);
            }
            if (pi.getQuantity() > 0)
            {
                current.add(pi);
                currentSize = pi.getQuantity();
            }
            else
                currentSize = 0;
        }
        if (currentSize == 0)
            splits.remove(current);
        return splits;
    }
    
    static List<PlanItem> extractItemsOfType(List<PlanItem> allItems, int type)
    {
        List<PlanItem> items = new ArrayList<>();
        for (Iterator<PlanItem> i = allItems.iterator(); i.hasNext(); )
        {
            PlanItem item = i.next();
            if (item.getType() == type)
            {
                items.add(item);
                i.remove();
            }
        }
        return items;
    }

    private static int plateShip(ShipPlanBean ship)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        ship.getSquares().getBounds(lower, upper);
        lower.x--;
        lower.y--;
        upper.x++;
        upper.y++;
        int volume = 0;
        for (int z = lower.z; z <= upper.z; z++)
            for (int y = lower.y; y <= upper.y; y++)
                for (int x = lower.x; x <= upper.x; x++)
                {
                    Point3i p = new Point3i(x, y, z);
                    if (isEdge(ship, p))
                        ship.setSquare(p, ShipSquareBean.HULL);
                    volume++;
                }
        return volume;
    }

    private static boolean isEdge(ShipPlanBean ship, Point3i p)
    {
        if (ship.getSquare(p) != null)
            return false;
        for (Point3i p2 : ShipPlanUtils.getNeighbors(p, false))
            if (isBorder(ship, p2))
                return true;
        return false;
    }

    private static boolean isBorder(ShipPlanBean ship, Point3i p)
    {
        ShipSquareBean sq = ship.getSquare(p);
        if (sq == null)
            return false;
        if (sq.getType() == ShipSquareBean.HULL)
            return false;
        // check projecting
        if (sq.getType() == ShipSquareBean.TURRET)
            return false;
        if (sq.getType() == ShipSquareBean.BAY)
            return false;
        if (sq.getType() == ShipSquareBean.SPINE)
            return false;
        if (sq.getType() == ShipSquareBean.HANGER)
            return false;
        return true;
    }
}
