package jo.ttg.ship0.logic;

import jo.ttg.ship0.beans.T0ShipDesign;

public class T0StandardLogic
{
    public static String[] standardShips =
    {
        "The Parshidona;Scout/Courier;100;A;A;A;40;4;2;4;0;2.883E7;true;true;00000",
        "The March Harrier;Free Trader;200;A;A;A;30;84;1;10;20;3.688E7;true;false;00000;00000",
        "The Bountiful Plenty;Subsidised Merchant;400;C;C;C;50;203;1;13;9;1.0073E8;true;false;00000;00000;00000;00000",
        "The Sweet Repose;Subsidised Liner;600;J;C;J;210;132;5;30;20;2.3667E8;false;false;00000;00000;00000;00000;00000;00000",
        "The Idle Trickster;Yacht;200;A;A;A;50;12;1;14;0;5.0957E7;false;false;00000;00000",
        "The Persuasive Argument;Mercenary Cruiser;800;M;M;M;318;88;7;25;0;4.4355E8;false;true;00000;00000;00000;00000;00000;00000;00000;00000",
        "The Raider's Lament;Patrol Cruiser;400;F;H;H;120;54;5;12;4;2.0914E8;true;true;00000;00000;00000;00000",
    };

    static int[] comps = { T0ShipDesign.S_COMP1BIS, T0ShipDesign.S_COMP1, T0ShipDesign.S_COMP1, T0ShipDesign.S_COMP3, T0ShipDesign.S_COMP1, T0ShipDesign.S_COMP5, T0ShipDesign.S_COMP3 };

    public static void main(String[] argv)
    {
        T0ShipDesign d = new T0ShipDesign();
        for (int i = 0; i < standardShips.length; i++)
        {
            d.fromString(standardShips[i]);
            d.setComp(comps[i]);
            System.out.println(d.toString());
        }
    }

}
