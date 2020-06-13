package jo.ttg.port.logic.plan;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortCube;
import jo.ttg.port.beans.PortItemBean;
import jo.ttg.port.beans.PortItemInstance;
import jo.ttg.port.beans.PortPlanBean;

public class ConcourseLogic
{

    public static void planConcourse(PortPlanBean plan)
    {
        // TODO Auto-generated method stub
        
    }

    public static void planCentral(PortPlanBean plan)
    {
        PortBuildingBean building = plan.getDesign().getFirstBuilding(PortBuildingBean.CENTRAL);
        PortItemBean atrium = building.getFirstItem(PortItemBean.ATRIUM);
        List<PortItemInstance> bookings = building.getAllItemInstances(PortItemBean.BOOKING);
        List<PortBuildingBean> concourseConnectors = plan.getDesign().getAllBuildings(PortBuildingBean.CONCOURSE, "Concourse * access");
        List<PortItemInstance> connectors = new ArrayList<>();
        for (PortBuildingBean c : concourseConnectors)
            connectors.addAll(c.getAllItemInstances(PortItemBean.GANGWAY));
        List<PortItemInstance> shops = building.getAllItemInstances(PortItemBean.SHOP);
        List<PortItemInstance> refreshments = building.getAllItemInstances(PortItemBean.REFRESHMENTS);
        List<PortItemInstance> toilets = building.getAllItemInstances(PortItemBean.TOILET);
        double area = atrium.getVolume();
        area += PortItemBean.getTotalInstanceVolume(shops);
        area += PortItemBean.getTotalInstanceVolume(refreshments);
        double frontage = 0;
        for (PortItemInstance i : bookings)
            frontage += frontage(i);
        for (PortItemInstance i : toilets)
            frontage += frontage(i);
        for (PortItemInstance i : connectors)
            frontage += frontage(i);
        double aspectRatio = 2;
        double shortSide;
        double longSide;
        for (;;)
        {
            shortSide = Math.sqrt(area/aspectRatio);
            longSide = shortSide*aspectRatio;
            if (shortSide*2 + longSide > frontage)
                break;
            aspectRatio += 1;
        }
        int ls = (int)Math.ceil(longSide/.75);
        int ss = (int)Math.ceil(shortSide/.75);
        PortCube aBounds = new PortCube(-ls/2, 0, 0, ls, ss, 1);
        plan.setArea(aBounds, atrium);
        
        PerimeterWalker pw = new PerimeterWalker(plan, aBounds);
        List<List<PortItemInstance>> bs = PortItemInstance.split(bookings, connectors.size() + 1);
        List<List<PortItemInstance>> ts = PortItemInstance.split(bookings, connectors.size()*2);
        pw.placeFirst(bs);
        pw.placeFirst(ts);
        while (connectors.size() > 0)
        {
            pw.place(connectors.get(0));
            connectors.remove(0);
            pw.placeFirst(ts);
            pw.placeFirst(bs);
            pw.placeFirst(ts);
        }
    }

    private static double frontage(PortItemInstance i)
    {
        double v = i.getItem().getVolume();
        double ar = i.getItem().getAspectRatio();
        double f = Math.sqrt(v/ar);
        return f;
    }
}
