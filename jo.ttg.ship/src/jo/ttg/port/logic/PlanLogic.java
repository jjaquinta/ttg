package jo.ttg.port.logic;

import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortPlanBean;
import jo.ttg.port.logic.plan.ConcourseLogic;
import jo.ttg.port.logic.plan.FuelLogic;
import jo.ttg.port.logic.plan.PadLogic;
import jo.ttg.port.logic.plan.WarehouseLogic;

public class PlanLogic
{
    public static PortPlanBean build(PortDesignBean design)
    {
        PortPlanBean plan = new PortPlanBean();
        plan.setDesign(design);
        ConcourseLogic.planCentral(plan);
        ConcourseLogic.planConcourse(plan);
        WarehouseLogic.planWarehouse(plan);
        PadLogic.planPads(plan);
        FuelLogic.planRefinery(plan);
        FuelLogic.planTanks(plan);
        return plan;
    }
}
