package jo.ttg.port.logic.build;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortItemBean;

public class WarehouseLogic
{
    public static void designWarehouse(PortDesignBean design)
    {
        double shipVolume = design.getMaxShipVolume();
        double cargoVolume = shipVolume/5*design.getSpec().getWarehousing();
        double warehouseVolume = 13.5*400;
        while (cargoVolume/warehouseVolume > 16)
            warehouseVolume *= 2;
        int numWarehouses = (int)Math.ceil(cargoVolume/warehouseVolume);
        PortBuildingBean building = new PortBuildingBean();
        building.setType(PortBuildingBean.WAREHOUSE);
        building.setNumber(numWarehouses);
        building.add(PortItemBean.WAREHOUSE, warehouseVolume, 1);
        building.add(PortItemBean.FIRE_SUPPRESSION, warehouseVolume/100, 1);
        building.add(PortItemBean.MAINTENENCE, warehouseVolume/80, 1);
        OfficeLogic.addOffices(building, 0.01);
        design.add(building);
        design.setWarehouseVolume(numWarehouses*warehouseVolume);        
    }
    
}
