package jo.ttg.lbb.logic.ship5;

import jo.ttg.lbb.data.ship5.Ship5Design;

public class Ship5DesignLogic
{
    // High Guard, p 23
    public static char hullTonnageToCode(int tonnage)
    {
        if (tonnage <= 99)
            return '0';
        if (tonnage <= 199)
            return '1';
        if (tonnage <= 299)
            return '2';
        if (tonnage <= 399)
            return '3';
        if (tonnage <= 499)
            return '4';
        if (tonnage <= 599)
            return '5';
        if (tonnage <= 699)
            return '6';
        if (tonnage <= 799)
            return '7';
        if (tonnage <= 899)
            return '8';
        if (tonnage <= 999)
            return '9';
        if (tonnage <= 1999)
            return 'A';
        if (tonnage <= 2999)
            return 'B';
        if (tonnage <= 3999)
            return 'C';
        if (tonnage <= 4999)
            return 'D';
        if (tonnage <= 5999)
            return 'E';
        if (tonnage <= 6999)
            return 'F';
        if (tonnage <= 7999)
            return 'G';
        if (tonnage <= 8999)
            return 'H';
        if (tonnage <= 9999)
            return 'J';
        if (tonnage <= 19999)
            return 'K';
        if (tonnage <= 29999)
            return 'L';
        if (tonnage <= 39999)
            return 'M';
        if (tonnage <= 49999)
            return 'N';
        if (tonnage <= 74999)
            return 'P';
        if (tonnage <= 99999)
            return 'Q';
        if (tonnage <= 199999)
            return 'R';
        if (tonnage <= 299999)
            return 'S';
        if (tonnage <= 399999)
            return 'T';
        if (tonnage <= 499999)
            return 'U';
        if (tonnage <= 699999)
            return 'V';
        if (tonnage <= 899999)
            return 'W';
        if (tonnage <= 999999)
            return 'X';
        return 'Y';
    }

    // High Guard, p 23
    public static int hullCodeToTonnage(char code)
    {
        switch (code)
        {
            case '0':
                return 99;
            case '1':
                return 100;
            case '2':
                return 200;
            case '3':
                return 300;
            case '4':
                return 400;
            case '5':
                return 500;
            case '6':
                return 600;
            case '7':
                return 700;
            case '8':
                return 800;
            case '9':
                return 900;
            case 'A':
                return 1000;
            case 'B':
                return 2000;
            case 'C':
                return 3000;
            case 'D':
                return 4000;
            case 'E':
                return 5000;
            case 'F':
                return 6000;
            case 'G':
                return 7000;
            case 'H':
                return 8000;
            case 'J':
                return 9000;
            case 'K':
                return 10000;
            case 'L':
                return 20000;
            case 'M':
                return 30000;
            case 'N':
                return 40000;
            case 'P':
                return 50000;
            case 'Q':
                return 75000;
            case 'R':
                return 100000;
            case 'S':
                return 200000;
            case 'T':
                return 300000;
            case 'U':
                return 400000;
            case 'V':
                return 500000;
            case 'W':
                return 700000;
            case 'X':
                return 900000;
            case 'Y':
                return 100000;
        }
        throw new IllegalArgumentException("Unknown hull code '"+code+"'");
    }

    // High Guard, p 23
    public static String hullConfigurationToName(char code)
    {
        switch (code)
        {
            case Ship5Design.CONFIG_NEEDLE:
                return "Needle/Wedge";
            case Ship5Design.CONFIG_CONE:
                return "Cone";
            case Ship5Design.CONFIG_CYLINDER:
                return "Cylinder";
            case Ship5Design.CONFIG_CLOSE:
                return "Close Structure";
            case Ship5Design.CONFIG_SPHERE:
                return "Sphere";
            case Ship5Design.CONFIG_FLAT_SPHERE:
                return "Flattened Sphere";
            case Ship5Design.CONFIG_DISPERSED:
                return "Dispersed Structure";
            case Ship5Design.CONFIG_PLANETOID:
                return "Planetoid";
            case Ship5Design.CONFIG_BUFFERED_PLANETOID:
                return "Buffered Planetoid";
        }
        throw new IllegalArgumentException("Unknown hull configuratoin code '"+code+"'");
    }

    public static final String HULL_CONFIG_STREAMLINED = "yes";
    public static final String HULL_CONFIG_NOT_STREAMLINED = "no";
    public static final String HULL_CONFIG_PARTIALLY_STREAMLINED = "partial";
    
    // High Guard, p 23
    public static String hullConfigurationToStreamlined(char code)
    {
        switch (code)
        {
            case Ship5Design.CONFIG_NEEDLE:
                return HULL_CONFIG_STREAMLINED;
            case Ship5Design.CONFIG_CONE:
                return HULL_CONFIG_STREAMLINED;
            case Ship5Design.CONFIG_CYLINDER:
                return HULL_CONFIG_PARTIALLY_STREAMLINED;
            case Ship5Design.CONFIG_CLOSE:
                return HULL_CONFIG_PARTIALLY_STREAMLINED;
            case Ship5Design.CONFIG_SPHERE:
                return HULL_CONFIG_PARTIALLY_STREAMLINED;
            case Ship5Design.CONFIG_FLAT_SPHERE:
                return HULL_CONFIG_STREAMLINED;
            case Ship5Design.CONFIG_DISPERSED:
                return HULL_CONFIG_NOT_STREAMLINED;
            case Ship5Design.CONFIG_PLANETOID:
                return HULL_CONFIG_NOT_STREAMLINED;
            case Ship5Design.CONFIG_BUFFERED_PLANETOID:
                return HULL_CONFIG_NOT_STREAMLINED;
        }
        throw new IllegalArgumentException("Unknown hull configuratoin code '"+code+"'");
    }
    
    // High Guard, p 23
    public static double hullConfigurationPriceModifier(char code)
    {
        switch (code)
        {
            case Ship5Design.CONFIG_NEEDLE:
                return 1.2;
            case Ship5Design.CONFIG_CONE:
                return 1.1;
            case Ship5Design.CONFIG_CYLINDER:
                return 1.0;
            case Ship5Design.CONFIG_CLOSE:
                return 0.0;
            case Ship5Design.CONFIG_SPHERE:
                return 0.7;
            case Ship5Design.CONFIG_FLAT_SPHERE:
                return 0.8;
            case Ship5Design.CONFIG_DISPERSED:
                return 0.5;
            case Ship5Design.CONFIG_PLANETOID:
                return 1.0;
            case Ship5Design.CONFIG_BUFFERED_PLANETOID:
                return 1.0;
        }
        throw new IllegalArgumentException("Unknown hull configuratoin code '"+code+"'");
    }

    // High Guard, p 22
    public static long hullCost(int tonnage, char configurationCode)
    {
        long baseCost;
        if (configurationCode == Ship5Design.CONFIG_PLANETOID)
            baseCost = 1100L*(tonnage*80/100);
        else if (configurationCode == Ship5Design.CONFIG_BUFFERED_PLANETOID)
            baseCost = 1100L*(tonnage*65/100);
        else 
            baseCost = tonnage*100000L;
        long cost = (long)(baseCost*Ship5DesignLogic.hullConfigurationPriceModifier(configurationCode));
        return cost;
    }

    // High Guard, p 23
    public static int driveManeuverMinimumTechLevel(int manueverDriveNumber)
    {
        switch (manueverDriveNumber)
        {
            case 0:
                return 0;
            case 1:
                return 7;
            case 2:
                return 7;
            case 3:
                return 8;
            case 4:
                return 8;
            case 5:
                return 8;
            case 6:
                return 9;
        }
        throw new IllegalArgumentException("Unknown maneuver drive number '"+manueverDriveNumber+"'");
    }

    // High Guard, p 23
    public static int driveJumpMinimumTechLevel(int jumpDriveNumber)
    {
        switch (jumpDriveNumber)
        {
            case 0:
                return 0;
            case 1:
                return 9;
            case 2:
                return 11;
            case 3:
                return 12;
            case 4:
                return 13;
            case 5:
                return 14;
            case 6:
                return 15;
        }
        throw new IllegalArgumentException("Unknown jump drive number '"+jumpDriveNumber+"'");
    }

    // High Guard, p 23
    public static int driveManeuverVolumePercent(int manueverDriveNumber)
    {
        switch (manueverDriveNumber)
        {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 8;
            case 4:
                return 11;
            case 5:
                return 14;
            case 6:
                return 17;
        }
        throw new IllegalArgumentException("Unknown maneuver drive number '"+manueverDriveNumber+"'");
    }

    // High Guard, p 23
    public static int driveJumpVolumePercent(int jumpDriveNumber)
    {
        switch (jumpDriveNumber)
        {
            case 0:
                return 0;
            case 1:
                return 2;
            case 2:
                return 3;
            case 3:
                return 4;
            case 4:
                return 5;
            case 5:
                return 6;
            case 6:
                return 7;
        }
        throw new IllegalArgumentException("Unknown jump drive number '"+jumpDriveNumber+"'");
    }

    // High Guard, p 23
    public static int drivePowerVolumePercent(int techLevel)
    {
        if (techLevel < 7)
            throw new IllegalArgumentException("Unsupported tech level for power plant '"+techLevel+"'");
        else if (techLevel <= 8)
            return 4;
        else if (techLevel <= 12)
            return 3;
        else if (techLevel <= 14)
            return 2;
        else if (techLevel <= 15)
            return 1;
        throw new IllegalArgumentException("Unsupported tech level for power plant '"+techLevel+"'");
    }

    // High Guard, p 23
    public static long driveManeuverCostPerTon(int maneuverPlantNumber)
    {
        switch (maneuverPlantNumber)
        {
            case 0:
                return 0;
            case 1:
                return 1500000;
            case 2:
                return  700000;
            case 3:
                return  500000;
            case 4:
                return  500000;
            case 5:
                return  500000;
            case 6:
                return  500000;
        }
        throw new IllegalArgumentException("Unknown Maneuver drive number '"+maneuverPlantNumber+"'");
    }

    // High Guard, p 23
    public static long driveJumpCostPerTon(int jumpPlantNumber)
    {
        switch (jumpPlantNumber)
        {
            case 0:
                return 0;
            case 1:
                return 4000000;
            case 2:
                return 4000000;
            case 3:
                return 4000000;
            case 4:
                return 4000000;
            case 5:
                return 4000000;
            case 6:
                return 4000000;
        }
        throw new IllegalArgumentException("Unknown Jump drive number '"+jumpPlantNumber+"'");
    }

    // High Guard, p 23
    public static long drivePowerCostPerTon(int powerPlantNumber)
    {
        switch (powerPlantNumber)
        {
            case 0:
                return 0;
            case 1:
                return 3000000;
            case 2:
                return 3000000;
            case 3:
                return 3000000;
            case 4:
                return 3000000;
            case 5:
                return 3000000;
            case 6:
                return 3000000;
        }
        throw new IllegalArgumentException("Unknown power plant number '"+powerPlantNumber+"'");
    }

    // High Guard, p 22
    public static int fuelJumpVolumePercent(int jumpDriveNumber)
    {
        return 10*jumpDriveNumber;
    }

    // High Guard, p 27
    public static long fuelScoopsCostPerTon()
    {
        return 1000;
    }
    
    // TCS, p 15
    public static double fuelPurificationPercentOfFuel(int techLevel)
    {
        switch (techLevel)
        {
            case 8:
                return 0.50;
            case 9:
                return 0.45;
            case 10:
                return 0.40;
            case 11:
                return 0.35;
            case 12:
                return 0.30;
            case 13:
                return 0.25;
            case 14:
                return 0.20;
            case 15:
                return 0.15;
        }
        throw new IllegalArgumentException("Unsupported tech level for fuel purificatoin '"+techLevel+"'");
    }
    
    // TCS, p 15
    public static int fuelPurificationMinimumSize(int techLevel)
    {
        switch (techLevel)
        {
            case 8:
                return 10;
            case 9:
                return 9;
            case 10:
                return 8;
            case 11:
                return 7;
            case 12:
                return 6;
            case 13:
                return 5;
            case 14:
                return 4;
            case 15:
                return 3;
        }
        throw new IllegalArgumentException("Unsupported tech level for fuel purificatoin '"+techLevel+"'");
    }
    
    // TCS, p 15
    public static long fuelPurificationCostPerTon(int techLevel)
    {
        switch (techLevel)
        {
            case 8:
                return 4000;
            case 9:
                return 4222;
            case 10:
                return 4500;
            case 11:
                return 4857;
            case 12:
                return 5333;
            case 13:
                return 6000;
            case 14:
                return 7000;
            case 15:
                return 10000;
        }
        throw new IllegalArgumentException("Unsupported tech level for fuel purificatoin '"+techLevel+"'");
    }

    // High Guard, p 27
    public static int bridgeVolume(int hullTonnage)
    {
        int vol = hullTonnage*2/100;
        if (vol < 20)
            vol = 20;
        return vol;
    }

    // High Guard, p 27
    public static long bridgeCost(int hullTonnage)
    {
        return hullTonnage*5000;
    }

    // High Guard, p 27
    public static int energyProduced(int hullTonnage, int powerPlantNumber)
    {
        return hullTonnage*powerPlantNumber/100;
    }

    // High Guard, p 26
    public static long computerCost(char computerNumber)
    {
        switch (computerNumber)
        {
            case Ship5Design.COMPUTER_NONE:
                return 0;
            case Ship5Design.COMPUTER_1:
                return 2000000;
            case Ship5Design.COMPUTER_1_FIB:
                return 3000000;
            case Ship5Design.COMPUTER_1_BIS:
                return 4000000;
            case Ship5Design.COMPUTER_2:
                return 9000000;
            case Ship5Design.COMPUTER_2_FIB:
                return 14000000;
            case Ship5Design.COMPUTER_2_BIS:
                return 18000000;
            case Ship5Design.COMPUTER_3:
                return 18000000;
            case Ship5Design.COMPUTER_3_FIB:
                return 27000000;
            case Ship5Design.COMPUTER_4:
                return 30000000;
            case Ship5Design.COMPUTER_4_FIB:
                return 45000000;
            case Ship5Design.COMPUTER_5:
                return 45000000;
            case Ship5Design.COMPUTER_5_FIB:
                return 68000000;
            case Ship5Design.COMPUTER_6:
                return 55000000;
            case Ship5Design.COMPUTER_6_FIB:
                return 83000000;
            case Ship5Design.COMPUTER_7:
                return 80000000;
            case Ship5Design.COMPUTER_7_FIB:
                return 100000000;
            case Ship5Design.COMPUTER_8:
                return 110000000;
            case Ship5Design.COMPUTER_8_FIB:
                return 140000000;
            case Ship5Design.COMPUTER_9:
                return 140000000;
            case Ship5Design.COMPUTER_9_FIB:
                return 200000000;
        }
        throw new IllegalArgumentException("Unsupported comptuer model'"+computerNumber+"'");
    }

    // High Guard, p 26
    public static int computerVolume(char computerNumber)
    {
        switch (computerNumber)
        {
            case Ship5Design.COMPUTER_NONE:
                return 0;
            case Ship5Design.COMPUTER_1:
                return 1;
            case Ship5Design.COMPUTER_1_FIB:
                return 2;
            case Ship5Design.COMPUTER_1_BIS:
                return 1;
            case Ship5Design.COMPUTER_2:
                return 2;
            case Ship5Design.COMPUTER_2_FIB:
                return 4;
            case Ship5Design.COMPUTER_2_BIS:
                return 2;
            case Ship5Design.COMPUTER_3:
                return 3;
            case Ship5Design.COMPUTER_3_FIB:
                return 6;
            case Ship5Design.COMPUTER_4:
                return 4;
            case Ship5Design.COMPUTER_4_FIB:
                return 8;
            case Ship5Design.COMPUTER_5:
                return 5;
            case Ship5Design.COMPUTER_5_FIB:
                return 10;
            case Ship5Design.COMPUTER_6:
                return 7;
            case Ship5Design.COMPUTER_6_FIB:
                return 14;
            case Ship5Design.COMPUTER_7:
                return 9;
            case Ship5Design.COMPUTER_7_FIB:
                return 18;
            case Ship5Design.COMPUTER_8:
                return 11;
            case Ship5Design.COMPUTER_8_FIB:
                return 22;
            case Ship5Design.COMPUTER_9:
                return 13;
            case Ship5Design.COMPUTER_9_FIB:
                return 26;
        }
        throw new IllegalArgumentException("Unsupported comptuer model'"+computerNumber+"'");
    }

    // High Guard, p 26
    public static char computerHull(char computerNumber)
    {
        switch (computerNumber)
        {
            case Ship5Design.COMPUTER_NONE:
                return '0';
            case Ship5Design.COMPUTER_1:
                return '6';
            case Ship5Design.COMPUTER_1_FIB:
                return '6';
            case Ship5Design.COMPUTER_1_BIS:
                return '6';
            case Ship5Design.COMPUTER_2:
                return 'A';
            case Ship5Design.COMPUTER_2_FIB:
                return 'A';
            case Ship5Design.COMPUTER_2_BIS:
                return 'A';
            case Ship5Design.COMPUTER_3:
                return 'D';
            case Ship5Design.COMPUTER_3_FIB:
                return 'D';
            case Ship5Design.COMPUTER_4:
                return 'K';
            case Ship5Design.COMPUTER_4_FIB:
                return 'K';
            case Ship5Design.COMPUTER_5:
                return 'P';
            case Ship5Design.COMPUTER_5_FIB:
                return 'P';
            case Ship5Design.COMPUTER_6:
                return 'R';
            case Ship5Design.COMPUTER_6_FIB:
                return 'R';
            case Ship5Design.COMPUTER_7:
                return 'Y';
            case Ship5Design.COMPUTER_7_FIB:
                return 'Y';
            case Ship5Design.COMPUTER_8:
                return ' ';
            case Ship5Design.COMPUTER_8_FIB:
                return ' ';
            case Ship5Design.COMPUTER_9:
                return ' ';
            case Ship5Design.COMPUTER_9_FIB:
                return ' ';
        }
        throw new IllegalArgumentException("Unsupported comptuer model'"+computerNumber+"'");
    }

    // High Guard, p 26
    public static int computerTechLevel(char computerNumber)
    {
        switch (computerNumber)
        {
            case Ship5Design.COMPUTER_NONE:
                return 0;
            case Ship5Design.COMPUTER_1:
                return 5;
            case Ship5Design.COMPUTER_1_FIB:
                return 5;
            case Ship5Design.COMPUTER_1_BIS:
                return 6;
            case Ship5Design.COMPUTER_2:
                return 7;
            case Ship5Design.COMPUTER_2_FIB:
                return 7;
            case Ship5Design.COMPUTER_2_BIS:
                return 8;
            case Ship5Design.COMPUTER_3:
                return 9;
            case Ship5Design.COMPUTER_3_FIB:
                return 9;
            case Ship5Design.COMPUTER_4:
                return 10;
            case Ship5Design.COMPUTER_4_FIB:
                return 10;
            case Ship5Design.COMPUTER_5:
                return 11;
            case Ship5Design.COMPUTER_5_FIB:
                return 11;
            case Ship5Design.COMPUTER_6:
                return 12;
            case Ship5Design.COMPUTER_6_FIB:
                return 12;
            case Ship5Design.COMPUTER_7:
                return 13;
            case Ship5Design.COMPUTER_7_FIB:
                return 13;
            case Ship5Design.COMPUTER_8:
                return 14;
            case Ship5Design.COMPUTER_8_FIB:
                return 24;
            case Ship5Design.COMPUTER_9:
                return 15;
            case Ship5Design.COMPUTER_9_FIB:
                return 15;
        }
        throw new IllegalArgumentException("Unsupported comptuer model'"+computerNumber+"'");
    }

    // High Guard, p 26
    public static int computerEnergy(char computerNumber)
    {
        switch (computerNumber)
        {
            case Ship5Design.COMPUTER_NONE:
                return 0;
            case Ship5Design.COMPUTER_1:
                return 0;
            case Ship5Design.COMPUTER_1_FIB:
                return 0;
            case Ship5Design.COMPUTER_1_BIS:
                return 0;
            case Ship5Design.COMPUTER_2:
                return 0;
            case Ship5Design.COMPUTER_2_FIB:
                return 0;
            case Ship5Design.COMPUTER_2_BIS:
                return 0;
            case Ship5Design.COMPUTER_3:
                return 1;
            case Ship5Design.COMPUTER_3_FIB:
                return 1;
            case Ship5Design.COMPUTER_4:
                return 2;
            case Ship5Design.COMPUTER_4_FIB:
                return 2;
            case Ship5Design.COMPUTER_5:
                return 3;
            case Ship5Design.COMPUTER_5_FIB:
                return 3;
            case Ship5Design.COMPUTER_6:
                return 5;
            case Ship5Design.COMPUTER_6_FIB:
                return 5;
            case Ship5Design.COMPUTER_7:
                return 7;
            case Ship5Design.COMPUTER_7_FIB:
                return 7;
            case Ship5Design.COMPUTER_8:
                return 9;
            case Ship5Design.COMPUTER_8_FIB:
                return 9;
            case Ship5Design.COMPUTER_9:
                return 12;
            case Ship5Design.COMPUTER_9_FIB:
                return 12;
        }
        throw new IllegalArgumentException("Unsupported comptuer model'"+computerNumber+"'");
    }

    // High Guard, p 23
    public static int armorVolumePercent(int armorFactor, int techLevel)
    {
        switch (techLevel)
        {
            case 7:
            case 8:
            case 9:
                return 4 + 4*armorFactor;
            case 10:
            case 11:
                return 3 + 3*armorFactor;
            case 12:
            case 13:
                return 2 + 2*armorFactor;
            case 14:
            case 15:
                return 1 + 1*armorFactor;
        }
        throw new IllegalArgumentException("Unsupported tech level for armor '"+techLevel+"'");
    }

    // High Guard, p 23
    public static long armorCostPerTon(int armorFactor)
    {
        return 300000 + armorFactor*100000;
    }

    // High Guard, p 24
    public static int majorPAVolume(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 5500;
            case 'B':
                return 5000;
            case 'C':
                return 4500;
            case 'D':
                return 4000;
            case 'E':
                return 3500;
            case 'F':
                return 3000;
            case 'G':
                return 2500;
            case 'H':
                return 2500;
            case 'J':
                return 5000;
            case 'K':
                return 4500;
            case 'L':
                return 4000;
            case 'M':
                return 3500;
            case 'N':
                return 3000;
            case 'P':
                return 2500;
            case 'Q':
                return 4500;
            case 'R':
                return 4000;
            case 'S':
                return 3500;
            case 'T':
                return 3000;
        }
        throw new IllegalArgumentException("Unsupported code for major PA '"+code+"'");
    }

    // High Guard, p 24
    public static int majorPATechLevel(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 8;
            case 'B':
                return 9;
            case 'C':
                return 10;
            case 'D':
                return 11;
            case 'E':
                return 12;
            case 'F':
                return 13;
            case 'G':
                return 14;
            case 'H':
                return 15;
            case 'J':
                return 10;
            case 'K':
                return 11;
            case 'L':
                return 12;
            case 'M':
                return 13;
            case 'N':
                return 14;
            case 'P':
                return 15;
            case 'Q':
                return 12;
            case 'R':
                return 13;
            case 'S':
                return 14;
            case 'T':
                return 15;
        }
        throw new IllegalArgumentException("Unsupported code for major PA '"+code+"'");
    }

    // High Guard, p 24
    public static long majorPACost(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 3500000000L;
            case 'B':
                return 3000000000L;
            case 'C':
                return 2400000000L;
            case 'D':
                return 1500000000L;
            case 'E':
                return 1200000000L;
            case 'F':
                return 1200000000L;
            case 'G':
                return 800000000L;
            case 'H':
                return 500000000L;
            case 'J':
                return 3000000000L;
            case 'K':
                return 2000000000L;
            case 'L':
                return 1600000000L;
            case 'M':
                return 1200000000L;
            case 'N':
                return 1000000000L;
            case 'P':
                return 800000000L;
            case 'Q':
                return 2000000000L;
            case 'R':
                return 1500000000L;
            case 'S':
                return 1200000000L;
            case 'T':
                return 1000000000L;
        }
        throw new IllegalArgumentException("Unsupported code for major PA '"+code+"'");
    }

    // High Guard, p 24
    public static int majorPAEnergy(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 500;
            case 'B':
                return 500;
            case 'C':
                return 500;
            case 'D':
                return 600;
            case 'E':
                return 600;
            case 'F':
                return 600;
            case 'G':
                return 700;
            case 'H':
                return 700;
            case 'J':
                return 800;
            case 'K':
                return 800;
            case 'L':
                return 800;
            case 'M':
                return 900;
            case 'N':
                return 900;
            case 'P':
                return 900;
            case 'Q':
                return 1000;
            case 'R':
                return 1000;
            case 'S':
                return 1000;
            case 'T':
                return 1000;
        }
        throw new IllegalArgumentException("Unsupported code for major PA '"+code+"'");
    }

    // High Guard, p 24
    public static int majorMesonVolume(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 5000;
            case 'B':
                return 8000;
            case 'C':
                return 2000;
            case 'D':
                return 5000;
            case 'E':
                return 1000;
            case 'F':
                return 2000;
            case 'G':
                return 1000;
            case 'H':
                return 2000;
            case 'J':
                return 1000;
            case 'K':
                return 8000;
            case 'L':
                return 5000;
            case 'M':
                return 4000;
            case 'N':
                return 2000;
            case 'P':
                return 8000;
            case 'Q':
                return 7000;
            case 'R':
                return 5000;
            case 'S':
                return 8000;
            case 'T':
                return 7000;
        }
        throw new IllegalArgumentException("Unsupported code for major Meson '"+code+"'");
    }

    // High Guard, p 24
    public static int majorMesonTechLevel(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 11;
            case 'B':
                return 1;
            case 'C':
                return 12;
            case 'D':
                return 12;
            case 'E':
                return 13;
            case 'F':
                return 13;
            case 'G':
                return 14;
            case 'H':
                return 14;
            case 'J':
                return 15;
            case 'K':
                return 12;
            case 'L':
                return 13;
            case 'M':
                return 14;
            case 'N':
                return 15;
            case 'P':
                return 13;
            case 'Q':
                return 14;
            case 'R':
                return 15;
            case 'S':
                return 14;
            case 'T':
                return 15;
        }
        throw new IllegalArgumentException("Unsupported code for major Meson '"+code+"'");
    }

    // High Guard, p 24
    public static long majorMesonCost(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 10000000000L;
            case 'B':
                return 12000000000L;
            case 'C':
                return 3000000000L;
            case 'D':
                return 5000000000L;
            case 'E':
                return 800000000L;
            case 'F':
                return 1000000000L;
            case 'G':
                return 400000000L;
            case 'H':
                return 600000000L;
            case 'J':
                return 400000000L;
            case 'K':
                return 10000000000L;
            case 'L':
                return 3000000000L;
            case 'M':
                return 800000000L;
            case 'N':
                return 600000000L;
            case 'P':
                return 5000000000L;
            case 'Q':
                return 1000000000L;
            case 'R':
                return 800000000L;
            case 'S':
                return 2000000000L;
            case 'T':
                return 1000000000L;
        }
        throw new IllegalArgumentException("Unsupported code for major Meson '"+code+"'");
    }

    // High Guard, p 24
    public static int majorMesonEnergy(char code)
    {
        switch (code)
        {
            case ' ':
                return 0;
            case 'A':
                return 500;
            case 'B':
                return 600;
            case 'C':
                return 600;
            case 'D':
                return 700;
            case 'E':
                return 700;
            case 'F':
                return 800;
            case 'G':
                return 800;
            case 'H':
                return 900;
            case 'J':
                return 900;
            case 'K':
                return 1000;
            case 'L':
                return 1000;
            case 'M':
                return 1000;
            case 'N':
                return 1000;
            case 'P':
                return 1100;
            case 'Q':
                return 1100;
            case 'R':
                return 1100;
            case 'S':
                return 1200;
            case 'T':
                return 1200;
        }
        throw new IllegalArgumentException("Unsupported code for major Meson '"+code+"'");
    }

    // High Guard, p 24
    public static int bay100MesonEnergy()
    {
        return 200;
    }

    // High Guard, p 24
    public static int bay100ParticleEnergy()
    {
        return 60;
    }

    // High Guard, p 24
    public static int bay100RepulsorEnergy()
    {
        return 10;
    }

    // High Guard, p 24
    public static int bay100MissileEnergy()
    {
        return 0;
    }

    // High Guard, p 24
    public static int bay50MesonEnergy()
    {
        return 100;
    }

    // High Guard, p 24
    public static int bay50ParticleEnergy()
    {
        return 30;
    }

    // High Guard, p 24
    public static int bay50RepulsorEnergy()
    {
        return 5;
    }

    // High Guard, p 24
    public static int bay50MissileEnergy()
    {
        return 0;
    }

    // High Guard, p 24
    public static int bay50PlasmaEnergy()
    {
        return 10;
    }

    // High Guard, p 24
    public static int bay50FusionEnergy()
    {
        return 20;
    }

    // High Guard, p 24
    public static long bay100MesonCost()
    {
        return 70000000L;
    }

    // High Guard, p 24
    public static long bay100ParticleCost()
    {
        return 35000000L;
    }

    // High Guard, p 24
    public static long bay100RepulsorCost()
    {
        return 10000000L;
    }

    // High Guard, p 24
    public static long bay100MissileCost()
    {
        return 20000000L;
    }

    // High Guard, p 24
    public static long bay50MesonCost()
    {
        return 50000000L;
    }

    // High Guard, p 24
    public static long bay50ParticleCost()
    {
        return 20000000L;
    }

    // High Guard, p 24
    public static long bay50RepulsorCost()
    {
        return 6000000L;
    }

    // High Guard, p 24
    public static long bay50MissileCost()
    {
        return 12000000L;
    }

    // High Guard, p 24
    public static long bay50PlasmaCost()
    {
        return 5000000L;
    }

    // High Guard, p 24
    public static long bay50FusionCost()
    {
        return 8000000L;
    }

    // High Guard, p 25
    public static long turretMissileCost()
    {
        return 750000L;
    }

    // High Guard, p 25
    public static long turretBeamLaserCost()
    {
        return 1000000L;
    }

    // High Guard, p 25
    public static long turretPulseLaserCost()
    {
        return 500000L;
    }

    // High Guard, p 25
    public static long turretPlasmaGunCost()
    {
        return 1500000L;
    }

    // High Guard, p 25
    public static long turretFusionGunCost()
    {
        return 2000000L;
    }

    // High Guard, p 25
    public static long turretSandcasterCost()
    {
        return 250000L;
    }

    // High Guard, p 25
    public static long turretParticleCost()
    {
        return 3000000L;
    }

    // High Guard, p 25
    public static long barbetteParticleCost()
    {
        return 4000000L;
    }

    // High Guard, p 25
    public static int turretMissileEnergy()
    {
        return 0;
    }

    // High Guard, p 25
    public static int turretBeamLaserEnergy()
    {
        return 1;
    }

    // High Guard, p 25
    public static int turretPulseLaserEnergy()
    {
        return 1;
    }

    // High Guard, p 25
    public static int turretPlasmaGunEnergy()
    {
        return 1;
    }

    // High Guard, p 25
    public static int turretFusionGunEnergy()
    {
        return 2;
    }

    // High Guard, p 25
    public static int turretSandcasterEnergy()
    {
        return 0;
    }

    // High Guard, p 25
    public static int turretParticleEnergy()
    {
        return 5;
    }

    // High Guard, p 25
    public static int barbetteParticleEnergy()
    {
        return 5;
    }

    // High Guard, p 25
    public static int screenNuclearTechLevel(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 12;
            case '2':
                return 13;
            case '3':
                return 13;
            case '4':
                return 14;
            case '5':
                return 14;
            case '6':
                return 14;
            case '7':
                return 15;
            case '8':
                return 15;
            case '9':
                return 15;
        }
        throw new IllegalArgumentException("Unknown Nuclear screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenNuclearVolume(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 50;
            case '2':
                return 15;
            case '3':
                return 20;
            case '4':
                return 8;
            case '5':
                return 10;
            case '6':
                return 12;
            case '7':
                return 10;
            case '8':
                return 15;
            case '9':
                return 20;
        }
        throw new IllegalArgumentException("Unknown Nuclear screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenNuclearEnergy(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 10;
            case '2':
                return 20;
            case '3':
                return 30;
            case '4':
                return 40;
            case '5':
                return 50;
            case '6':
                return 60;
            case '7':
                return 70;
            case '8':
                return 80;
            case '9':
                return 90;
        }
        throw new IllegalArgumentException("Unknown Nuclear screen code '"+code+"'");
    }

    // High Guard, p 25
    public static long screenNuclearCost(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 50000000L;
            case '2':
                return 40000000L;
            case '3':
                return 45000000L;
            case '4':
                return 30000000L;
            case '5':
                return 35000000L;
            case '6':
                return 38000000L;
            case '7':
                return 30000000L;
            case '8':
                return 40000000L;
            case '9':
                return 50000000L;
        }
        throw new IllegalArgumentException("Unknown Nuclear screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenMesonTechLevel(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 12;
            case '2':
                return 13;
            case '3':
                return 13;
            case '4':
                return 14;
            case '5':
                return 14;
            case '6':
                return 14;
            case '7':
                return 15;
            case '8':
                return 15;
            case '9':
                return 15;
        }
        throw new IllegalArgumentException("Unknown Meson screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenMesonVolume(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 90;
            case '2':
                return 30;
            case '3':
                return 45;
            case '4':
                return 16;
            case '5':
                return 20;
            case '6':
                return 24;
            case '7':
                return 20;
            case '8':
                return 30;
            case '9':
                return 40;
        }
        throw new IllegalArgumentException("Unknown Meson screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenMesonEnergy(char code, int tonnage)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return (int)(.2*tonnage/100.0);
            case '2':
                return (int)(.4*tonnage/100.0);
            case '3':
                return (int)(.6*tonnage/100.0);
            case '4':
                return (int)(.8*tonnage/100.0);
            case '5':
                return (int)(1.0*tonnage/100.0);
            case '6':
                return (int)(1.2*tonnage/100.0);
            case '7':
                return (int)(1.4*tonnage/100.0);
            case '8':
                return (int)(1.6*tonnage/100.0);
            case '9':
                return (int)(1.8*tonnage/100.0);
        }
        throw new IllegalArgumentException("Unknown Meson screen code '"+code+"'");
    }

    // High Guard, p 25
    public static long screenMesonCost(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 80000000L;
            case '2':
                return 50000000L;
            case '3':
                return 55000000L;
            case '4':
                return 40000000L;
            case '5':
                return 45000000L;
            case '6':
                return 50000000L;
            case '7':
                return 40000000L;
            case '8':
                return 50000000L;
            case '9':
                return 60000000L;
        }
        throw new IllegalArgumentException("Unknown Meson screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenForceTechLevel(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 15;
            case '2':
                return 15;
            case '3':
                return 15;
            case '4':
                return 15;
            case '5':
                return 16;
            case '6':
                return 16;
            case '7':
                return 16;
            case '8':
                return 17;
            case '9':
                return 18;
        }
        throw new IllegalArgumentException("Unknown Force screen code '"+code+"'");
    }

    // High Guard, p 25
    public static int screenForceVolume(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 10;
            case '2':
                return 15;
            case '3':
                return 20;
            case '4':
                return 25;
            case '5':
                return 20;
            case '6':
                return 30;
            case '7':
                return 35;
            case '8':
                return 20;
            case '9':
                return 20;
        }
        throw new IllegalArgumentException("Unknown Force screen code '"+code+"'");
    }

    // High Guard, p 25
    public static long screenForceCost(char code)
    {
        switch (code)
        {
            case '0':
            case ' ':
                return 0;
            case '1':
                return 400000000L;
            case '2':
                return 600000000L;
            case '3':
                return 800000000L;
            case '4':
                return 1000000000L;
            case '5':
                return 0L;
            case '6':
                return 0L;
            case '7':
                return 0L;
            case '8':
                return 0L;
            case '9':
                return 0L;
        }
        throw new IllegalArgumentException("Unknown Force screen code '"+code+"'");
    }
    
    public static int getHangerVolume(int craftTonnage, int quantity, int shipTonnage)
    {
        int volume = craftTonnage*quantity;
        if (craftTonnage < 100)
        {
            if (shipTonnage > 1000)
                volume += volume*10/100;
        }
        else
            volume += volume*30/100;
        return volume;
    }
    
    public static long getHangerCost(int craftTonnage, int quantity, int shipTonnage)
    {
        return getHangerVolume(craftTonnage, quantity, shipTonnage)*2000L;
    }
    
    public static int getLaunchTubeVolume(int capacity, int quantity)
    {
        int volume = capacity*25*quantity;
        return volume;
    }
    
    public static long getLaunchTubeCost(int capacity, int quantity)
    {
        return getLaunchTubeVolume(capacity, quantity)*2000L;
    }
}
