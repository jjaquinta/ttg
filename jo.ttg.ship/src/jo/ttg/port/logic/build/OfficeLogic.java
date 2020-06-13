package jo.ttg.port.logic.build;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortItemBean;

public class OfficeLogic
{
    private static final double CUBICLE_SIZE = 2*13.5;
    private static final double BRIEFING_ROOM_SIZE = CUBICLE_SIZE*4;
    private static final double TOILET_SIZE = CUBICLE_SIZE*4;

    public static void addOffices(PortBuildingBean building, double pc)
    {
        double officeVolume = building.getUnitVolume()*pc;
        int cubicles = (int)Math.floor(officeVolume/CUBICLE_SIZE);
        if (cubicles < 0)
            return;
        building.add(PortItemBean.CUBICLE, CUBICLE_SIZE, cubicles);
        building.add(PortItemBean.BRIEFING_ROOM, BRIEFING_ROOM_SIZE, cubicles/8);
        building.add(PortItemBean.TOILET, TOILET_SIZE, cubicles/12);
    }

}
