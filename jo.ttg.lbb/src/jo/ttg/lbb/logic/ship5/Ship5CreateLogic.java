package jo.ttg.lbb.logic.ship5;

import jo.ttg.lbb.data.ship5.Ship5Design;

public class Ship5CreateLogic
{
    // e.g. BC-A2447G2-000410-50203-0
    public static Ship5Design createFromUSP(String usp)
    {
        Ship5Design ship = new Ship5Design();
        ship.setShipType(usp.substring(0, 2));

        ship.setHullTonnage(Ship5DesignLogic.hullCodeToTonnage(usp.charAt(3)));
        ship.setHullConfigurationCode(usp.charAt(4));
        ship.setJumpDriveNumber(fromUSP(usp.charAt(5)));
        ship.setManeuverDriveNumber(fromUSP(usp.charAt(6)));
        ship.setPowerPlantNumber(fromUSP(usp.charAt(7)));
        ship.setComputerCode(usp.charAt(8));
        // TODO: crew

        ship.setArmorFactors(fromUSP(usp.charAt(10)));
        // TODO: sand
        ship.setScreenMesonCode(usp.charAt(12));
        ship.setScreenNuclearCode(usp.charAt(13));
        ship.setScreenForceCode(usp.charAt(14));
        // TODO: repulsors

        // TODO: lasers
        // TODO: energy weapons
        // TODO: particle
        // TODO: meson
        // TODO: missiles
        
        // TODO: fighters

        return ship;
    }

    private static final String USP_DIGITS = "0123456789ABCDEFGHJKLMNPQRSTUVWXYZ";

    private static int fromUSP(char upp)
    {
        return USP_DIGITS.indexOf(upp);
    }
}
