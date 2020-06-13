package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.IPlacementStrategy;

public class BlockStrategy implements IPlacementStrategy
{
    private int[] mAxis;
    private int[] mStrategy;
    private double[] mAspectRatio;
    private ShipPlanBean mLastPlan;
    private int   mCompartment;
    private Map<String, double[]> mSubAspectRatios = new HashMap<String, double[]>();
    //private Random mRND;
    
    public BlockStrategy(int[] axis, int[] strategy, double[] aspectRatio)
    {
        mAxis = axis;
        mStrategy = strategy;
        mAspectRatio = aspectRatio;
        mLastPlan = null;
    }

    @Override
    public void place(ShipPlanBean plan, List<PlanItem> items)
    {
        Map<String, List<PlanItem>> groups = FillStrategy.groupItemsByNotes(items);
        for (String v : groups.keySet())
        {
            List<PlanItem> g = groups.get(v);
            double[] ar = mSubAspectRatios.get(v);
            if (ar == null)
                ar = mAspectRatio;
            doPlace(plan, g, ar);
        }
    }
    
    private void doPlace(ShipPlanBean plan, List<PlanItem> items, double[] ar)
    {
        if (mLastPlan != plan)
        {
            mLastPlan = plan;
            mCompartment = 1;
        }
        else
            mCompartment++;
        //mRND = new Random(0);
        double aspectVolume = ar[0]*ar[1]*ar[2]*2;
        double aspectBase = Math.cbrt(items.get(0).getVolume()/aspectVolume);
        int xSize = (int)Math.round(ar[0]*aspectBase/1.5);
        int ySize = (int)Math.round(ar[1]*aspectBase/1.5);
        int zSize = (int)Math.round(ar[2]*aspectBase/3.0);
        //System.out.println("Aspect Ratio="+ar[0]+"x"+ar[1]+"x"+ar[2]+", aspectvol="+aspectVolume+", aspectBase="+aspectBase+", size="+xSize+"x"+ySize+"x"+zSize);
        List<Point3i> freeSpace = getFreePositions(plan, xSize, ySize, zSize);
        Collections.sort(freeSpace, new FillStrategy(mStrategy, mAxis, plan));
        for (PlanItem item : items)
        {
            //System.out.println("Placing "+item.getNotes()+", vol="+item.getVolume());
            int count = item.getQuantity();
            for (int i = 0; i < count; i++)
            {
                if (freeSpace.size() == 0)
                {
                    plan.println("No space ("+xSize+"x"+ySize+"x"+zSize+") allocating #"+item.getType()+", compartment "+mCompartment+" notes="+item.getNotes());
                    return;
                }
                Point3i todo = freeSpace.get(0);
                freeSpace.remove(0);
                if (canFit(plan, todo.x, todo.y, todo.z, xSize, ySize, zSize))
                {
                    FillStrategy.markComponent(plan, todo, xSize, ySize, zSize, item, mCompartment);
                    mCompartment++;
                }
                else
                    i--; // skip this and re-do
            }
        }
    }

    private List<Point3i> getFreePositions(ShipPlanBean plan, int xSize, int ySize, int zSize)
    {
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        lower.x -= xSize;
        lower.y -= ySize;
        lower.z -= zSize;
        int xIncr = Math.min(xSize, ySize);
        int yIncr = Math.min(xSize, ySize);
        int zIncr = 1;
        
        List<Point3i> positions = new ArrayList<Point3i>();        
        for (int y  = lower.y; y <= upper.y; y += yIncr)
            for (int x  = lower.x; x <= upper.x; x += xIncr)
                for (int z = lower.z; z <= upper.z; z += zIncr)
                {
                    boolean valid = canFit(plan, x, y, z, xSize, ySize, zSize);
                    if (valid)
                        positions.add(new Point3i(x, y, z));
                }
        return positions;
    }

    private boolean canFit(ShipPlanBean plan, int x, int y, int z, int xSize,
            int ySize, int zSize)
    {
        for (int dx = 0; dx < xSize; dx++)
            for (int dy = 0; dy < ySize; dy++)
                for (int dz = 0; dz < zSize; dz++)
                {
                    ShipSquareBean sq = plan.getSquare(x+dx, y+dy, z+dz);
                    if (sq == null)
                        return false;
                    else if (sq.getType() != ShipSquareBean.UNSET)
                        return false;
                }
        return true;
    }
    
    /*
    private int[] getRandomRange(int low, int high)
    {
        int[] range = new int[high - low + 1];
        for (int i = 0; i < range.length; i++)
            range[i] = low + i;
        for (int i = 0; i < range.length*2; i++)
        {
            int idx1 = i%range.length;
            int idx2 = mRND.nextInt(range.length);
            int tmp = range[idx1];
            range[idx1] = range[idx2];
            range[idx2] = tmp;
        }
        return range;
    }
    */
    
    public void addAspectRatio(String name, double[] ar)
    {
        mSubAspectRatios.put(name, ar);
    }
}
