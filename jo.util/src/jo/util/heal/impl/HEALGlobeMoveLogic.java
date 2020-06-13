package jo.util.heal.impl;

import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALVector;

public class HEALGlobeMoveLogic
{
    public static IHEALVector move(HEALGlobe<?>.HEALVector v)
    {
        HEALGlobe<?>.HEALVector after = (HEALGlobe<?>.HEALVector)v.next(v.getDirection()).makeVector(v.getDirection());
        int delta = Math.abs(v.getX() - after.getX()) + Math.abs(v.getY() - after.getY());
        if (delta <= 2)
            return after;
        // change orientation
        switch (v.getDirection())
        {
            case IHEALCoord.D_NORTHEAST:
                if (v.getY() < v.getSide()*4)
                    after.setDirection(IHEALCoord.D_SOUTHEAST);
                break;
            case IHEALCoord.D_SOUTHWEST:
                if (v.getY() >= v.getSide()*2)
                    after.setDirection(IHEALCoord.D_NORTHWEST);
                break;
            case IHEALCoord.D_SOUTHEAST:
                after.setDirection(IHEALCoord.D_NORTHEAST);
                break;
            case IHEALCoord.D_NORTHWEST:
                after.setDirection(IHEALCoord.D_SOUTHWEST);
                break;
            case IHEALCoord.D_EAST:
                break;
            case IHEALCoord.D_WEST:
                break;
            case IHEALCoord.D_SOUTH:
                if (flipSouth(v))
                    after.setDirection(IHEALCoord.D_NORTH);
                break;
            case IHEALCoord.D_NORTH:
                if (flipNorth(v))
                    after.setDirection(IHEALCoord.D_SOUTH);
                break;
            default:
                throw new IllegalArgumentException("Unknown direction '"+v.getDirection()+"'");
        }
        return after;
    }
    
    private static boolean flipNorth(HEALGlobe<?>.HEALVector v)
    {
        if ((v.getX() == v.getSide()*1 - 1) && (v.getY() == v.getSide()*0))
            return true;
        if ((v.getX() == v.getSide()*2 - 1) && (v.getY() == v.getSide()*1))
            return true;
        if ((v.getX() == v.getSide()*3 - 1) && (v.getY() == v.getSide()*2))
            return true;
        if ((v.getX() == v.getSide()*4 - 1) && (v.getY() == v.getSide()*3))
            return true;
        return false;
    }
    
    private static boolean flipSouth(HEALGlobe<?>.HEALVector v)
    {
        if ((v.getY() == v.getSide()*3 - 1) && (v.getX() == v.getSide()*0))
            return true;
        if ((v.getY() == v.getSide()*4 - 1) && (v.getX() == v.getSide()*1))
            return true;
        if ((v.getY() == v.getSide()*5 - 1) && (v.getX() == v.getSide()*2))
            return true;
        if ((v.getY() == v.getSide()*6 - 1) && (v.getX() == v.getSide()*3))
            return true;
        return false;
    }

}
