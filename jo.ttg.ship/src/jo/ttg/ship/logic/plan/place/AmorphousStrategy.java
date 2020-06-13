package jo.ttg.ship.logic.plan.place;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.IPlacementStrategy;

public class AmorphousStrategy implements IPlacementStrategy
{
    private int[] mAxis;
    private int[] mStrategy;
    private ShipPlanBean mLastPlan;
    private int   mCompartment;
    private int   mExtends;
    private Point3i mSize = new Point3i(1, 1, 1);

    public AmorphousStrategy(int[] axis, int[] strategy)
    {
        mAxis = axis;
        mStrategy = strategy;
        mLastPlan = null;
    }

    public AmorphousStrategy(int[] axis, int[] strategy, Point3i size)
    {
        mAxis = axis;
        mStrategy = strategy;
        mLastPlan = null;
        mSize = size;
    }
    
    public AmorphousStrategy(int[] axis, int[] strategy, int extention)
    {
        this(axis, strategy);
        mExtends = extention;
    }

    @Override
    public void place(ShipPlanBean plan, List<PlanItem> items)
    {
        if (mLastPlan != plan)
        {
            mLastPlan = plan;
            mCompartment = 1;
        }
        else
            mCompartment++;
        List<Point3i> freeSpace = new ArrayList<Point3i>();
        freeSpace.addAll(Amorphous2Strategy.getFreeSpace(plan, ShipSquareBean.UNSET, mExtends, mSize));
        Collections.sort(freeSpace, new FillStrategy(mStrategy, mAxis, plan));
        for (PlanItem item : items)
        {
            int count = item.getQuantity();
            for (int i = 0; i < count; i++)
            {
                if (!Amorphous2Strategy.allocate(item.getVolume(), item.getType(), item.getNotes(), plan, freeSpace, mCompartment, mSize))
                    break;
            }
        }
    }
}
