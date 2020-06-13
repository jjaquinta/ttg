package jo.ttg.port.logic.build;

import jo.ttg.port.beans.PortBuildingBean;
import jo.ttg.port.beans.PortDesignBean;
import jo.ttg.port.beans.PortItemBean;

public class PadLogic
{
    public static final int SMALL_PAD_TONNAGE = 1000;
    public static final int MEDIUM_PAD_TONNAGE = 5*SMALL_PAD_TONNAGE;
    public static final int LARGE_PAD_TONNAGE = 5*MEDIUM_PAD_TONNAGE;
    
    public static void designPads(PortDesignBean design)
    {
        makeSmallPad(design);
        makeMediumPad(design);
        makeLargePad(design);
    }
    
    private static void makeSmallPad(PortDesignBean design)
    {
        makePad(design, design.getSpec().getSmallPads(), SMALL_PAD_TONNAGE, "Small Landing Pad");
    }
    
    private static void makeMediumPad(PortDesignBean design)
    {
        makePad(design, design.getSpec().getMediumPads(), SMALL_PAD_TONNAGE, "Medium Landing Pad");
    }
    
    private static void makeLargePad(PortDesignBean design)
    {
        makePad(design, design.getSpec().getLargePads(), LARGE_PAD_TONNAGE, "Large Landing Pad");
    }
    
    private static PortBuildingBean makePad(PortDesignBean design, int number, int size, String notes)
    {
        if (number <= 0)
            return null;
        PortBuildingBean pad = new PortBuildingBean();
        pad.setType(PortBuildingBean.LANDING_PAD);
        double shipVolume = size*13.5;
        double shipRadius = Math.cbrt(shipVolume*3/4/Math.PI);
        double padRadius = shipRadius*2;
        double padArea = Math.PI*padRadius*padRadius;
        double padVolume = padArea*6.25;
        pad.add(PortItemBean.APRON, padVolume, 1);
        pad.add(PortItemBean.MAINTENENCE, 4*13.5, size/1000);
        pad.add(PortItemBean.FUEL_PUMP, size/10/100, 1);
        pad.add(PortItemBean.FIRE_SUPPRESSION, 4*13.5, Math.max(1, size/5000));
        pad.setNumber(number);
        pad.setNotes(notes);
        design.add(pad);
        design.setMaxShipVolume(design.getMaxShipVolume() + number*size*13.5);
        return pad;
    }
    
}
