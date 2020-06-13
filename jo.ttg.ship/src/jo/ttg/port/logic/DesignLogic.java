package jo.ttg.port.logic;

import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortSpecsBean;
import jo.ttg.port.logic.build.ConcourseLogic;
import jo.ttg.port.logic.build.FuelLogic;
import jo.ttg.port.logic.build.PadLogic;
import jo.ttg.port.logic.build.WarehouseLogic;

public class DesignLogic
{
    public static PortDesignBean build(PortSpecsBean spec)
    {
        PortDesignBean design = new PortDesignBean();
        design.setSpec(spec);
        PadLogic.designPads(design);
        FuelLogic.designTanks(design);
        FuelLogic.designRefinery(design);
        WarehouseLogic.designWarehouse(design);
        ConcourseLogic.designConcourse(design);
        ConcourseLogic.designCentral(design);
        return design;
    }
}
