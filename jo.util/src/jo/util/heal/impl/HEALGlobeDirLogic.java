package jo.util.heal.impl;

import jo.util.heal.IHEALCoord;

public class HEALGlobeDirLogic
{
    static HEALGlobe<?>.HEALCoord next(HEALGlobe<?>.HEALCoord c, int dir)
    {
        switch (dir)
        {
            case IHEALCoord.D_NORTHEAST:
                return nextNE(c);
            case IHEALCoord.D_NORTHWEST:
                return nextNW(c);
            case IHEALCoord.D_SOUTHEAST:
                return nextSE(c);
            case IHEALCoord.D_SOUTHWEST:
                return nextSW(c);
            case IHEALCoord.D_NORTH:
                return nextN(c);
            case IHEALCoord.D_EAST:
                return nextE(c);
            case IHEALCoord.D_SOUTH:
                return nextS(c);
            case IHEALCoord.D_WEST:
                return nextW(c);
            default:
                throw new IllegalArgumentException("Unknown direction '"+dir+"'");
        }
    }
    
    private static HEALGlobe<?>.HEALCoord nextNE(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        c.setX(c.getX()+1);
        if (c.isValid())
            return c;
        if (c.getY() < c.getSide())
        {
            c.setX(c.getSide()*2 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide());
        }
        else if (c.getY() < c.getSide()*2)
        {
            c.setX(c.getSide()*3 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*2);
        }
        else if (c.getY() < c.getSide()*3)
        {
            c.setX(c.getSide()*4 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*3);
        }
        else if (c.getY() < c.getSide()*4)
        {
            c.setX(c.getSide() - (c.getY()%c.getSide()) - 1);
            c.setY(0);
        }
        else if (c.getY() < c.getSide()*5)
        {
            c.setX(0);
            c.setY((c.getY()%c.getSide()));
        }
        else // if (c.getY() < c.getSize()*6)
        {
            c.setX(0);
            c.setY((c.getY()%c.getSide()) + c.getSide());
        }
        if (!c.isValid())
            return ori;
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextSW(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        c.setX(c.getX()-1);
        if (c.isValid())
            return c;
        if (c.getY() < c.getSide())
        {
            c.setX(c.getSide()*4 - 1);
            c.setY((c.getY()%c.getSide()) + c.getSide()*4);
        }
        else if (c.getY() < c.getSide()*2)
        {
            c.setX(c.getSide()*4 - 1);
            c.setY((c.getY()%c.getSide()) + c.getSide()*5);
        }
        else if (c.getY() < c.getSide()*3)
        {
            c.setX(c.getSide()*4 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*6 - 1);
        }
        else if (c.getY() < c.getSide()*4)
        {
            c.setX(c.getSide()*1 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*3 - 1);
        }
        else if (c.getY() < c.getSide()*5)
        {
            c.setX(c.getSide()*2 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*4 - 1);
        }
        else // if (c.getY() < c.getSize()*6)
        {
            c.setX(c.getSide()*3 - (c.getY()%c.getSide()) - 1);
            c.setY(c.getSide()*5 - 1);
        }
        if (!c.isValid())
            return ori;
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextNW(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        c.setX(c.getY()-1);
        if (c.isValid())
            return c;
        if (c.getX() < c.getSide())
        {
            c.setY(c.getSide()*4 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*4 - 1);
        }
        else if (c.getX() < c.getSide()*2)
        {
            c.setY(c.getSide()*1 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*1 - 1);
        }
        else if (c.getX() < c.getSide()*3)
        {
            c.setY(c.getSide()*2 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*2 - 1);
        }
        else // if (c.getX() < c.getSize()*5)
        {
            c.setY(c.getSide()*3 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*3 - 1);
        }
        if (!c.isValid())
            return ori;
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextSE(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        c.setX(c.getY()+1);
        if (c.isValid())
            return c;
        if (c.getX() < c.getSide())
        {
            c.setY(c.getSide()*4 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide());
        }
        else if (c.getX() < c.getSide()*2)
        {
            c.setY(c.getSide()*5 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*2);
        }
        else if (c.getX() < c.getSide()*3)
        {
            c.setY(c.getSide()*6 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*3);
        }
        else // if (c.getX() < c.getSize()*5)
        {
            c.setY(c.getSide()*3 - (c.getX()%c.getSide()) - 1);
            c.setX(0);
        }
        if (!c.isValid())
            return ori;
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextE(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        if (c.getX() == c.getSide()*4 - 1)
        {
            if (c.getY() < c.getSide()*4)
            {
                c.setX(c.getSide() - (c.getY()%c.getSide()) - 1);
                c.setY(0);
            }
            else if (c.getY() < c.getSide()*6)
            {
                c.setX(0);
                c.setY(c.getY() - c.getSide()*3);
            }
        }
        else if (c.getY() == c.getSide()*6 - 1)
        {
            c.setY(c.getSide()*3 - (c.getX()%c.getSide()) - 1);
            c.setX(0);
        }
        else
            do
            {
                c.setX((c.getX() + 1)%(c.getSide()*4));
                c.setY((c.getY() + 1)%(c.getSide()*6));
            } while (!c.isValid());
        return c;
    }

    private static HEALGlobe<?>.HEALCoord nextW(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        if (c.getX() == 0)
        {
            if (c.getY() < c.getSide()*2)
            {
                c.setY(c.getY() + c.getSide()*4 - 1);
                c.setX(c.getSide()*4 - 1);
            }
            else if (c.getY() < c.getSide()*3)
            {
                c.setX(c.getSide()*4 - (c.getY()%c.getSide()) - 1);
                c.setY(c.getSide()*6 - 1);
            }
        }
        else if (c.getY() == 0)
        {
            c.setY(c.getSide()*4 - (c.getX()%c.getSide()) - 1);
            c.setX(c.getSide()*4 - 1);
        }
        else
            do
            {
                c.setX((c.getX() + c.getSide()*4 - 1)%(c.getSide()*4));
                c.setY((c.getY() + c.getSide()*6 - 1)%(c.getSide()*6));
            } while (!c.isValid());
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextN(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        if ((c.getY() >= c.getSide()*4) && (c.getY() < c.getSide()*6) && (c.getX() == c.getSide()*4 - 1))
        {
            c.setY(c.getY() - c.getSide()*4);
            c.setX(0);
        }
        else if ((c.getX() >= 0) && (c.getX() < c.getSide() - 1) && (c.getY() == 0))
            c.setX(c.getX()+1);
        else if ((c.getX() == c.getSide() - 1) && (c.getY() == 0))
        {
            c.setX(c.getSide()*3 - 1);
            c.setY(c.getSide()*2);
        }
        else if ((c.getY() > 0) && (c.getY() <= c.getSide()) && (c.getX() == c.getSide() - 1))
            c.setX(c.getY()-1);
        else if ((c.getX() >= c.getSide()*1) && (c.getX() < c.getSide()*2 - 1) && (c.getY() == c.getSide()*1))
            c.setX(c.getX()+1);
        else if ((c.getX() == c.getSide()*2 - 1) && (c.getY() == c.getSide()*1))
        {
            c.setX(c.getSide()*4 - 1);
            c.setY(c.getSide()*3);
        }
        else if ((c.getY() > c.getSide()*1) && (c.getY() <= c.getSide()*2) && (c.getX() == c.getSide()*2 - 1))
            c.setX(c.getY()-1);
        else if ((c.getX() >= c.getSide()*2) && (c.getX() < c.getSide()*3 - 1) && (c.getY() == c.getSide()*2))
            c.setX(c.getX()+1);
        else if ((c.getX() == c.getSide()*3 - 1) && (c.getY() == c.getSide()*2))
        {
            c.setX(c.getSide()*1 - 1);
            c.setY(c.getSide()*0);
        }
        else if ((c.getY() > c.getSide()*2) && (c.getY() <= c.getSide()*3) && (c.getX() == c.getSide()*3 - 1))
            c.setX(c.getY()-1);
        else if ((c.getX() >= c.getSide()*3) && (c.getX() < c.getSide()*4 - 1) && (c.getY() == c.getSide()*3))
            c.setX(c.getX()+1);
        else if ((c.getX() == c.getSide()*4 - 1) && (c.getY() == c.getSide()*3))
        {
            c.setX(c.getSide()*2 - 1);
            c.setY(c.getSide()*1);
        }
        else if ((c.getY() > c.getSide()*3) && (c.getY() <= c.getSide()*4) && (c.getX() == c.getSide()*4 - 1))
            c.setX(c.getY()-1);
        else
        {
            c.setX(c.getX()+1);
            c.setY(c.getY()-1);
        }
        return c;
    }
    
    private static HEALGlobe<?>.HEALCoord nextS(HEALGlobe<?>.HEALCoord ori)
    {
        HEALGlobe<?>.HEALCoord c = ori.copy();
        if ((c.getY() >= c.getSide()*0) && (c.getY() < c.getSide()*2 - 1) && (c.getX() == 0))
        {
            c.setY(c.getY() + c.getSide()*4);
            c.setX(c.getSide()*4 - 1);
        }
        else if ((c.getY() >= c.getSide()*2 - 1) && (c.getY() < c.getSide()*3 - 1) && (c.getX() == 0))
            c.setY(c.getY()+1);
        else if ((c.getY() == c.getSide()*3 - 1) && (c.getX() == 0))
        {
            c.setY(c.getSide()*5 - 1);
            c.setX(c.getSide()*2);
        }
        else if ((c.getX() > 0) && (c.getX() < c.getSide()) && (c.getY() == c.getSide()*3 - 1))
            c.setX(c.getX()-1);
        else if ((c.getY() >= c.getSide()*3 - 1) && (c.getY() < c.getSide()*4 - 1) && (c.getX() == c.getSide()*1))
            c.setY(c.getY()+1);
        else if ((c.getY() == c.getSide()*4 - 1) && (c.getX() == c.getSide()*1))
        {
            c.setY(c.getSide()*6 - 1);
            c.setX(c.getSide()*3);
        }
        else if ((c.getX() > c.getSide()*1) && (c.getX() < c.getSide()*2) && (c.getY() == c.getSide()*4 - 1))
            c.setX(c.getX()-1);
        else if ((c.getY() >= c.getSide()*4 - 1) && (c.getY() < c.getSide()*5 - 1) && (c.getX() == c.getSide()*2))
            c.setY(c.getY()+1);
        else if ((c.getY() == c.getSide()*5 - 1) && (c.getX() == c.getSide()*2))
        {
            c.setY(c.getSide()*3 - 1);
            c.setX(c.getSide()*0);
        }
        else if ((c.getX() > c.getSide()*2) && (c.getX() < c.getSide()*3) && (c.getY() == c.getSide()*5 - 1))
            c.setX(c.getX()-1);
        else if ((c.getY() >= c.getSide()*5 - 1) && (c.getY() < c.getSide()*6 - 1) && (c.getX() == c.getSide()*3))
            c.setY(c.getY()+1);
        else if ((c.getY() == c.getSide()*6 - 1) && (c.getX() == c.getSide()*3))
        {
            c.setY(c.getSide()*4 - 1);
            c.setX(c.getSide()*1);
        }
        else if ((c.getX() > c.getSide()*3) && (c.getX() < c.getSide()*4) && (c.getY() == c.getSide()*6 - 1))
            c.setX(c.getX()-1);
        else
        {
            c.setX(c.getX()-1);
            c.setY(c.getY()+1);
        }
        return c;
    }

}
