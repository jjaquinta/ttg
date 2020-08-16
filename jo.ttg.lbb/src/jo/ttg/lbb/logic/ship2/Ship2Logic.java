package jo.ttg.lbb.logic.ship2;

import jo.ttg.lbb.data.ship2.Ship2Design;
import jo.ttg.lbb.data.ship2.Ship2Instance;

public class Ship2Logic
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
    public static int calculateNumber(Ship2Design ship, char Let)
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
            case Ship2Design.S_COMP1:
                return 1;
            case Ship2Design.S_COMP1BIS:
                return 1;
            case Ship2Design.S_COMP2:
                return 2;
            case Ship2Design.S_COMP2BIS:
                return 2;
            case Ship2Design.S_COMP3:
                return 3;
            case Ship2Design.S_COMP4:
                return 4;
            case Ship2Design.S_COMP5:
                return 5;
            case Ship2Design.S_COMP6:
                return 7;
            case Ship2Design.S_COMP7:
                return 9;
        }
        return 0;
    }

    public static int calculateHold(Ship2Design ship)
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

    public static double fuelNeeded(Ship2Design ship, int dist)
    {
        return dist * .1 * ship.getSize();
    }

    public static int getJumpCapacity(Ship2Design ship)
    {
        return (int)(ship.getFuelSize() * 10 / ship.getSize());
    }

    public static int jumpMass(Ship2Design ship)
    {
        return Ship2Logic.jumpMass(ship.getJumpType());
    }

    public static int manMass(Ship2Design ship)
    {
        return Ship2Logic.manMass(ship.getManType());
    }

    public static int powMass(Ship2Design ship)
    {
        return Ship2Logic.powMass(ship.getPowType());
    }

    // derived
    public static int      getG(Ship2Instance ship)
    {
        if (ship.getPowNow()>= ship.getManNow())
            return calculateNumber(ship.getDesign(), ship.getManNow());
        return calculateNumber(ship.getDesign(), ship.getPowNow());
    }
    public static int      getJump(Ship2Instance ship)
    {
        if (ship.getPowNow() >= ship.getJumpNow())
            return calculateNumber(ship.getDesign(), ship.getJumpNow());
        return calculateNumber(ship.getDesign(), ship.getJumpNow());
    }
    public static int      getPow(Ship2Instance ship)
    {
        return calculateNumber(ship.getDesign(), ship.getPowNow());
    }
    
    public static int getShipType(Ship2Design ship)
    {
    	if (ship.getSize() < 100)
    		return Ship2Design.TYPE_SMALL_CRAFT;
    	if (ship.getJumpType() >= 'A')
    		return Ship2Design.TYPE_STARSHIP;
    	return Ship2Design.TYPE_SPACESHIP;
    }

	public static boolean isOutOfFuel(Ship2Instance ship)
	{
		return ship.getFuelHits()*10 >= ship.getDesign().getFuelSize();		
	}
	
	private static int[] COMPUTER_LOAD = {
		2, 4, 3, 6, 5, 8, 12, 15, 20,
	};
	
	private static int[] COMPUTER_STORE = {
		4, 0, 6, 0, 9, 15, 25, 35, 50,
	};
	
	public static int getComputerLoadedCapacity(Ship2Design ship)
	{
		return COMPUTER_LOAD[ship.getComp()];
	}
	
	public static int getComputerStorageCapacity(Ship2Design ship)
	{
		return COMPUTER_STORE[ship.getComp()];
	}
}
