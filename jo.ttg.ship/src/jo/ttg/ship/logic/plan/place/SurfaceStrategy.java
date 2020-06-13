package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.IPlacementStrategy;

public class SurfaceStrategy implements IPlacementStrategy
{
    private static double mLowInside = .4;
    private static double mHighInside = .6;
    
    private double[] mAspectRatio;
    private ShipPlanBean mLastPlan;
    private int   mCompartment;
    //private Random mRND;
    
    public SurfaceStrategy(double[] aspectRatio)
    {
        mAspectRatio = aspectRatio;
        mLastPlan = null;
    }
    
    public SurfaceStrategy(double[] aspectRatio, double lowInside, double highInside)
    {
        mAspectRatio = aspectRatio;
        mLastPlan = null;
        mLowInside = lowInside;
        mHighInside = highInside;
    }

    @Override
    public void place(ShipPlanBean plan, List<PlanItem> items)
    {
        Map<Double, List<PlanItem>> groups = FillStrategy.groupItems(items);
        for (List<PlanItem> g : groups.values())
            doPlace(plan, g);
    }
    
    private void doPlace(ShipPlanBean plan, List<PlanItem> items)
    {
        if (mLastPlan != plan)
        {
            mLastPlan = plan;
            mCompartment = 1;
        }
        else
            mCompartment++;
        //mRND = new Random(0);
        double aspectVolume = mAspectRatio[0]*mAspectRatio[1]*mAspectRatio[2];
        double aspectBase = Math.cbrt(items.get(0).getVolume()/aspectVolume);
        int xSize = (int)Math.round(mAspectRatio[0]*aspectBase/1.5);
        int ySize = (int)Math.round(mAspectRatio[1]*aspectBase/1.5);
        int zSize = (int)Math.round(mAspectRatio[2]*aspectBase/3.0);
        List<Candidate> freeSpace = getFreePositions(plan, xSize, ySize, zSize);
        List<Candidate> takenSpace = new ArrayList<>();
        for (PlanItem item : items)
        {
            int count = item.getQuantity();
            for (int i = 0; i < count; i++)
            {
                if (freeSpace.size() == 0)
                {
                    plan.println("No space ("+xSize+"x"+ySize+"x"+zSize+") allocating #"+item.getType()+", compartment "+mCompartment+" notes="+item.getNotes());
                    return;
                }
                Candidate todo;
                if (takenSpace.size() == 0)
                    todo = freeSpace.get(0);
                else
                    todo = getBestSpace(freeSpace, takenSpace);
                freeSpace.remove(todo);
                takenSpace.add(todo);
                FillStrategy.markComponent(plan, todo.p, todo.xSize, todo.ySize, todo.zSize, item, mCompartment);
                mCompartment++;
            }
        }
    }

    private Candidate getBestSpace(List<Candidate> freeSpace,
            List<Candidate> takenSpace)
    {
        Candidate best = null;
        double bestv = 0;
        for (Candidate p : freeSpace)
        {
            double v = dist(p.p, takenSpace.get(0).p);
            for (int i = 1; i < takenSpace.size(); i++)
                v = Math.min(v, dist(p.p, takenSpace.get(i).p));
            if ((best == null) || (v > bestv))
            {
                best = p;
                bestv = v;
            }
        }
        return best;
    }
    
    private double dist(Point3i p1, Point3i p2)
    {
        int dx = (p1.x - p2.x);
        int dy = (p1.y - p2.y);
        int dz = (p1.z - p2.z);
        return Math.sqrt(dx*dx + dy*dy + dz*dz);
    }

    private List<Candidate> getFreePositions(ShipPlanBean plan, int xSize, int ySize, int zSize)
    {
        Point3i upper = new Point3i();
        Point3i lower = new Point3i();
        plan.getSquares().getBounds(lower, upper);
        lower.x -= xSize;
        lower.y -= ySize;
        lower.z -= zSize;
        int chunkVolume = xSize*ySize*zSize;
        int lowInside = (int)Math.floor(mLowInside*chunkVolume);
        int highInside = (int)Math.ceil(mHighInside*chunkVolume);
        List<Candidate> positions = new ArrayList<Candidate>();        
        int zIncr = 1;
        int xIncr = Math.min(xSize, ySize);
        int yIncr = Math.min(xSize, ySize);
        
        if (xSize != ySize)
            lookForPositions(plan, positions, ySize, xSize, zSize, upper, lower,
                    chunkVolume, lowInside, highInside, yIncr, xIncr, zIncr);
        lookForPositions(plan, positions, xSize, ySize, zSize, upper, lower,
                chunkVolume, lowInside, highInside, xIncr, yIncr, zIncr);
        return positions;
    }

    private void lookForPositions(ShipPlanBean plan, List<Candidate> positions,
            int xSize, int ySize, int zSize, Point3i upper, Point3i lower,
            int chunkVolume, int lowInside, int highInside, int xIncr,
            int yIncr, int zIncr)
    {
        for (int y  = lower.y; y <= upper.y; y += yIncr)
            for (int x  = lower.x; x <= upper.x; x += xIncr)
                for (int z = lower.z; z <= upper.z; z += zIncr)
                {
                    int in = 0;
                    boolean breakout = false;
                    for (int dx = 0; dx < xSize && !breakout; dx++)
                        for (int dy = 0; dy < ySize && !breakout; dy++)
                            for (int dz = 0; dz < zSize && !breakout; dz++)
                            {
                                ShipSquareBean sq = plan.getSquare(x+dx, y+dy, z+dz);
                                if (sq == null)
                                    ;//out++;
                                else if (sq.getType() == ShipSquareBean.UNSET)
                                    in++;
                                else
                                    breakout = true;
                            }
                    if (breakout)
                        continue;
                    if ((in >= lowInside) && (in <= highInside))
                        positions.add(new Candidate(new Point3i(x, y, z), xSize, ySize, zSize));
                }
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
    
    class Candidate
    {
        Point3i p;
        int xSize;
        int ySize;
        int zSize;
        
        public Candidate(Point3i p, int xSize, int ySize, int zSize)
        {
            this.p = p;
            this.xSize = xSize;
            this.ySize = ySize;
            this.zSize = zSize;
        }
    }
}
