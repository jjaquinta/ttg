package jo.ttg.ship0.logic;

import jo.ttg.ship0.beans.T0ShipInstance;
import jo.ttg.ship0.beans.T0ShipDesign;

public class T0ShipLogic
{
    static public int letOff(char Let)
    {
        if (Let < 'A')
            return -1;
        if (Let >= 'O')
            Let--;
        if (Let >= 'I')
            Let--;
        return Let - 'A';
    }

    static private final char letterToNumber[][] = {
    { /* 100*/ 'A', 'A', 'B', 'B', 'C', 'C' },
    { /* 200*/ 'A', 'B', 'C', 'D', 'E', 'F' },
    { /* 400*/ 'B', 'D', 'F', 'H', 'K', 'M' },
    { /* 600*/ 'C', 'F', 'J', 'M', 'Q', 'T' },
    { /* 800*/ 'D', 'H', 'M', 'R', 'V', 'X' },
    { /*1000*/ 'E', 'K', 'Q', 'V', 'W', 'X' },
    { /*2000*/ 'J', 'V', 'X', 'Y', 'Z', 'Z' },
    { /*3000*/ 'Q', 'X', 'Z', 'Z', 'a', 'a' },
    { /*4000*/ 'V', 'Y', 'Z', 'a', 'a', 'a' },
    { /*5000*/ 'W', 'Z', 'a', 'a', 'a', 'a' }
    };
    public static int calculateNumber(T0ShipDesign ship, char Let)
    {
        char    table[];
        if (ship.getSize() <= 100)
            table = letterToNumber[0];
        else if (ship.getSize() <= 200)
            table = letterToNumber[1];
        else if (ship.getSize() <= 400)
            table = letterToNumber[2];
        else if (ship.getSize() <= 600)
            table = letterToNumber[3];
        else if (ship.getSize() <= 800)
            table = letterToNumber[4];
        else if (ship.getSize() <= 1000)
            table = letterToNumber[5];
        else if (ship.getSize() <= 2000)
            table = letterToNumber[6];
        else if (ship.getSize() <= 3000)
            table = letterToNumber[7];
        else if (ship.getSize() <= 4000)
            table = letterToNumber[8];
        else
            table = letterToNumber[9];
        for (int i = 5; i > 0; i--)
            if (Let >= table[i])
                return i + 1;
        return 1;
    }

    public static int jumpMass(char Let)
    {
        int off = letOff(Let);
        if (off < 0)
            return 0;
        return 10 + 5 * off;
    }

    public static double jumpPrice(char Let)
    {
        int off = letOff(Let);
        if (off < 0)
            return 0;
        return (1 + off) * 10000000.0;
    }

    public static int manMass(char Let)
    {
        int off = letOff(Let);
        if (off < 0)
            return 0;
        return 1 + 2 * off;
    }

    public static int powMass(char Let)
    {
        int off = letOff(Let);
        if (off < 0)
            return 0;
        return 4 + 3 * off;
    }

    public static int compMass(int comp)
    {
        switch (comp)
        {
            case T0ShipDesign.S_COMP1:
                return 1;
            case T0ShipDesign.S_COMP1BIS:
                return 1;
            case T0ShipDesign.S_COMP2:
                return 2;
            case T0ShipDesign.S_COMP2BIS:
                return 2;
            case T0ShipDesign.S_COMP3:
                return 3;
            case T0ShipDesign.S_COMP4:
                return 4;
            case T0ShipDesign.S_COMP5:
                return 5;
            case T0ShipDesign.S_COMP6:
                return 7;
            case T0ShipDesign.S_COMP7:
                return 9;
        }
        return 0;
    }

    public static int calculateHold(T0ShipDesign ship)
    {
        int ret = 0;
        ret += ship.getFuelSize();
        ret += ship.getCabins() * 4;
        ret += ship.getBerths() / 2;
        ret += jumpMass(ship.getJumpType());
        ret += powMass(ship.getPowType());
        ret += manMass(ship.getManType());
        ret += compMass(ship.getComp());
        // bridge
        if (ship.getSize() < 20 * 50)
            ret += 20;
        else
            ret += ship.getSize() / 50;
        return ship.getSize() - ret;
    }

    public static double fuelNeeded(T0ShipDesign ship, int dist)
    {
        return dist * .1 * ship.getSize();
    }

    public static int getJumpCapacity(T0ShipDesign ship)
    {
        return (int)(ship.getFuelSize() * 10 / ship.getSize());
    }

    public static int jumpMass(T0ShipDesign ship)
    {
        return T0ShipLogic.jumpMass(ship.getJumpType());
    }

    public static int manMass(T0ShipDesign ship)
    {
        return T0ShipLogic.manMass(ship.getManType());
    }

    public static int powMass(T0ShipDesign ship)
    {
        return T0ShipLogic.powMass(ship.getPowType());
    }

    // derived
    public static int      getG(T0ShipInstance ship)
    {
        if (ship.getPowNow()>= ship.getManNow())
            return calculateNumber(ship, ship.getManNow());
        return calculateNumber(ship, ship.getPowNow());
    }
    public static int      getJump(T0ShipInstance ship)
    {
        if (ship.getPowNow() >= ship.getJumpNow())
            return calculateNumber(ship, ship.getJumpNow());
        return calculateNumber(ship, ship.getJumpNow());
    }
    public static int      getPow(T0ShipInstance ship)
    {
        return calculateNumber(ship, ship.getPowNow());
    }
}
