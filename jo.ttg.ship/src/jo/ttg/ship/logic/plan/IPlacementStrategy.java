package jo.ttg.ship.logic.plan;

import java.util.List;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;

public interface IPlacementStrategy
{
    public void place(ShipPlanBean plan, List<PlanItem> items);
}
