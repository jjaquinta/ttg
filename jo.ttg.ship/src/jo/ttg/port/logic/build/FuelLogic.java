package jo.ttg.port.logic.build;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortItemBean;

public class FuelLogic
{
    public static void designTanks(PortDesignBean design)
    {
        double shipVolume = design.getMaxShipVolume();
        double fuelVolume = shipVolume/10*design.getSpec().getFuelStorage();
        double tankVolume = 13.5*100;
        while (fuelVolume/tankVolume > 16)
            tankVolume *= 2;
        int numTanks = (int)Math.ceil(fuelVolume/tankVolume);
        PortBuildingBean tank = new PortBuildingBean();
        tank.setType(PortBuildingBean.FUEL_TANK);
        tank.add(PortItemBean.FUEL_TANK, tankVolume, numTanks);
        tank.add(PortItemBean.FUEL_PUMP, tankVolume/100, 1);
        tank.add(PortItemBean.FIRE_SUPPRESSION, tankVolume/100, 1);
        design.add(tank);
        design.setFuelVolume(numTanks*tankVolume);        
    }
    public static void designRefinery(PortDesignBean design)
    {
        double fuelVolume = design.getFuelVolume();
        fuelVolume *= design.getSpec().getFuelRefinery();
        double fuelPerSixHours = fuelVolume/4;
        double refineryVolume = fuelPerSixHours*.4;
        if (refineryVolume < 60)
            return;
        double refinerVolume = 60;
        while (refineryVolume/refinerVolume > 16)
            refinerVolume *= 2;
        int numRefiners = (int)Math.ceil(refineryVolume/refinerVolume);
        PortBuildingBean refinery = new PortBuildingBean();
        refinery.setType(PortBuildingBean.FUEL_REFINERY);
        refinery.add(PortItemBean.FUEL_REFINER, refinerVolume, numRefiners);
        refinery.add(PortItemBean.FUEL_TANK, fuelPerSixHours/6, 1);
        refinery.add(PortItemBean.FUEL_PUMP, fuelPerSixHours/6/100, 1);
        refinery.add(PortItemBean.FIRE_SUPPRESSION, refineryVolume/60, 1);
        refinery.add(PortItemBean.MAINTENENCE, refineryVolume/40, 1);
        OfficeLogic.addOffices(refinery, 0.05);
        design.add(refinery);
    }
}
