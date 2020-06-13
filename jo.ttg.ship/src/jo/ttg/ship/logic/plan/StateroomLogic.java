package jo.ttg.ship.logic.plan;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.ship.beans.plan.PlanItem;
import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.ttg.ship.logic.plan.place.BlockStrategy;
import jo.ttg.ship.logic.plan.place.FillStrategy;

public class StateroomLogic
{
    public static final String CREW_LOUNGE = "Crew Lounge";
    public static final String OFFICER_LOUNGE = "Officer Lounge";
    public static final String GALLEY = "Galley";
    public static final String MED_BAY = "Med Bay";
    public static final String MAINTENENCE = "Maintenence";
    public static final String BRIEFING_ROOM = "Briefing Room";
    
    private static final Map<String, double[]> ROOM_ASPECT = new HashMap<String, double[]>();
    static
    {
        ROOM_ASPECT.put(CREW_LOUNGE, new double[] { 2,4,1 });
        ROOM_ASPECT.put(OFFICER_LOUNGE, new double[] { 2,4,1 });
        ROOM_ASPECT.put(GALLEY, new double[] { 8,8,1 });
        ROOM_ASPECT.put(MED_BAY, new double[] { 3,6,1 });
        ROOM_ASPECT.put(MAINTENENCE, new double[] { 2,4,1 });
        ROOM_ASPECT.put(BRIEFING_ROOM, new double[] { 3,6,1 });
    }
    private static final String[] ROOM_ORDER = {
            CREW_LOUNGE,
            CREW_LOUNGE,
            MAINTENENCE,
            CREW_LOUNGE,
            GALLEY,
            OFFICER_LOUNGE,
            CREW_LOUNGE,
            MAINTENENCE,
            MED_BAY,
            BRIEFING_ROOM,
            CREW_LOUNGE,
            CREW_LOUNGE,
            MAINTENENCE,
            OFFICER_LOUNGE,
            BRIEFING_ROOM,
            GALLEY,
            CREW_LOUNGE,
            MAINTENENCE,
            CREW_LOUNGE,
            OFFICER_LOUNGE,
            BRIEFING_ROOM,
            CREW_LOUNGE,
            GALLEY,
            CREW_LOUNGE,
            MAINTENENCE,
            OFFICER_LOUNGE,
            MED_BAY,
            CREW_LOUNGE,
            MAINTENENCE,
            CREW_LOUNGE,
            BRIEFING_ROOM,
            OFFICER_LOUNGE,
    };
    
    static void allocateStaterooms(ShipPlanBean plan,
            List<PlanItem> allItems)
    {
        List<PlanItem> items = ShipPlanLogic.extractItemsOfType(allItems, ShipSquareBean.STATEROOM);
        if (items.size() == 0)
            return;
        double extra = 0;
        for (PlanItem i : items)
        {
            extra += i.getVolume()*i.getQuantity()/2;
            i.setVolume(i.getVolume()/2);
        }
        for (int room = 0; extra > 0; room++)
        {
            String name = ROOM_ORDER[room%ROOM_ORDER.length];
            double[] ar = ROOM_ASPECT.get(name);
            double volume = ar[0]*ar[1]*ar[2]*1.5*1.5*3.0;
            //System.out.println("Room "+name+", aspect="+ar[0]+"x"+ar[1]+", vol="+volume);
            PlanItem pi = new PlanItem(ShipSquareBean.STATEROOM, volume, 1, name);
            items.add(pi);
            extra -= volume;
        }
        ShipPlanLogic.allocate(ShipSquareBean.STATEROOM, items, plan, 2);
    }

    public static IPlacementStrategy createStrategy()
    {
        BlockStrategy strat = new BlockStrategy(new int[] { FillStrategy.Y, FillStrategy.X, FillStrategy.Z },
                new int[] { FillStrategy.CENTER, FillStrategy.CENTER, FillStrategy.CENTER}, new double[] {3, 3, 3});
        for (String name : ROOM_ASPECT.keySet())
        {
            double[] ar = ROOM_ASPECT.get(name);
            //double volume = ar[0]*ar[1]*ar[2]*1.5*1.5*3.0;
            strat.addAspectRatio(name, ar);
        }
        return strat;
    }
    

}
