package jo.ttg.port.logic.build;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortItemBean;

public class ConcourseLogic
{
    public static void designConcourse(PortDesignBean design)
    {
        int pads = design.getSpec().getPads();
        int numConcorses;
        if (pads < 8)
            numConcorses = 1;
        else if (pads < 24)
            numConcorses = 2;
        else
            numConcorses = 3;
        int padsPerConcorse = pads/numConcorses;
        PortBuildingBean building = new PortBuildingBean();
        building.setType(PortBuildingBean.CONCOURSE);
        building.setNumber(numConcorses);
        building.add(PortItemBean.WAITING_ROOM, 12*13.5, padsPerConcorse);
        building.add(PortItemBean.MAINTENENCE, 2*13.5, padsPerConcorse/4);
        if (padsPerConcorse > 2)
        {
            building.add(PortItemBean.CUSTOMS, 4*13.5*padsPerConcorse, 1);
            building.add(PortItemBean.DUTY_FREE, 2*13.5*padsPerConcorse, 1);
        }
        building.add(PortItemBean.TOILET, 4*13.5, Math.max(1, padsPerConcorse/6));
        building.add(PortItemBean.SHOP, 6*13.5, padsPerConcorse/6);
        building.add(PortItemBean.REFRESHMENTS, 6*13.5, padsPerConcorse/5);
        design.add(building);
        for (int g = 0; g < numConcorses; g++)
        {
            PortBuildingBean gangway = new PortBuildingBean();
            gangway.setType(PortBuildingBean.GANGWAY);
            gangway.setNumber(1);
            gangway.add(PortItemBean.GANGWAY, 4*4*PortItemBean.getAspectRatio(PortItemBean.GANGWAY)*6.25, 1);
            gangway.setNotes("Concourse "+(g+1)+" access");
            design.add(gangway);
        }
    }
    public static void designCentral(PortDesignBean design)
    {
        int pads = design.getSpec().getPads();
        PortBuildingBean building = new PortBuildingBean();
        building.setType(PortBuildingBean.CENTRAL);
        building.add(PortItemBean.ATRIUM, 4*6.25*pads, 1);
        building.add(PortItemBean.BOOKING, 4*6.25, pads/6);
        building.add(PortItemBean.TOILET, 4*13.5, pads/12);
        building.add(PortItemBean.SHOP, 6*13.5, pads/12);
        building.add(PortItemBean.REFRESHMENTS, 6*13.5, pads/10);
        OfficeLogic.addOffices(building, .1);
        design.add(building);
    }
}
