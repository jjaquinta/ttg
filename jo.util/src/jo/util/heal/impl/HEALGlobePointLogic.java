package jo.util.heal.impl;

import jo.util.heal.HEALLogic;

public class HEALGlobePointLogic
{

    private final static int[][] TRI_NUM =
        {
                {  0, -1, -1, -1,},
                {  4,  1, -1, -1,},
                {  8,  5,  2, -1,},
                { -1,  9,  6,  3,},
                { -1, -1, 10,  7,},
                { -1, -1, -1, 11,},
        };

    private static int getTri(int x, int y, int side)
    {
        int tx = x/side;
        int ty = y/side;
        return TRI_NUM[ty][tx];
    }
    
    private static int[] getPixCoords(int x, int y, int r)
    {
        int side = (int)Math.pow(2, r);
        int[] pix = new int[r+1];
        pix[0] = getTri(x, y, side);
        for (int i = 1; i < pix.length; i++)
        {
            x %= side;
            y %= side;
            if (x < side/2)
                if (y < side/2)
                    pix[i] = 0;
                else
                    pix[i] = 3;
            else
                if (y < side/2)
                    pix[i] = 1;
                else
                    pix[i] = 2;
            side /= 2;
        }
        return pix;
    }

    public static double[][] getThetaPhiBox(int x, int y, int rez)
    {
        int[] pixCoord = getPixCoords(x, y, rez);
        return HEALLogic.getThetaPhiBox(rez, pixCoord);
    }
}
