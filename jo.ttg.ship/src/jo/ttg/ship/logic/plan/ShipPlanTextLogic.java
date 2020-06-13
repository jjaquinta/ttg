package jo.ttg.ship.logic.plan;

import javax.vecmath.Point3i;

import jo.ttg.ship.beans.plan.ShipPlanBean;
import jo.ttg.ship.beans.plan.ShipSquareBean;
import jo.vecmath.data.SparseMatrix;

public class ShipPlanTextLogic
{
    public static String printShipText(ShipPlanBean ship)
    {
        StringBuffer sb = new StringBuffer();
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        ship.getSquares().getBounds(lower, upper);
        for (int z = lower.z; z <= upper.z; z++)
        {
            SparseMatrix<ShipSquareBean> level = new SparseMatrix<ShipSquareBean>();
            for (int y = lower.y; y <= upper.y; y++)
                for (int x = lower.x; x <= upper.x; x++)
                {
                    ShipSquareBean sq = ship.getSquare(x, y, z);
                    if (sq != null)
                        level.set(x, y, z, sq);                       
                }
            printLevelText(sb, level, z);
        }
        return sb.toString();
    }
    
    public static void printLevelText(StringBuffer sb, SparseMatrix<ShipSquareBean> level, int z)
    {
        Point3i lower = new Point3i();
        Point3i upper = new Point3i();
        level.getBounds(lower, upper);
        lower.z = z;
        upper.z = z;
        sb.append(getLevelText(level, lower, upper));
    }
        
    public static String getLevelText(SparseMatrix<ShipSquareBean> level, Point3i lower, Point3i upper)
    {
        StringBuffer sb = new StringBuffer();
        int width = (upper.x - lower.x + 1)*3/2;
        int length = (upper.y - lower.y + 1)*3/2;
        for (int z = lower.z; z <= upper.z; z++)
        {
            sb.append("LEVEL "+z+"\n");
            if (width >= length)
            {
                sb.append("Size: "+width+"m x "+length+"m\n");
                sb.append("<--Port     Starboard-->\n");
                sb.append("\n");
                for (int y = lower.y; y <= upper.y; y++)
                {
                    for (int x = lower.x; x <= upper.x; x++)
                        sb.append(squareToChar(level, x, y, z));
                    sb.append("\n");
                }
                sb.append("\n");
            }
            else
            {
                sb.append("Size: "+length+"m x "+width+"m\n");
                sb.append("<--Fore       Aft-->\n");
                sb.append("\n");
                for (int x = lower.x; x <= upper.x; x++)
                {
                    for (int y = lower.y; y <= upper.y; y++)
                        sb.append(squareToChar(level, x, y, z));
                    sb.append("\n");
                }
                sb.append("\n");
            }
        }
        return sb.toString();
    }
    
    private static char squareToChar(SparseMatrix<ShipSquareBean> level, int x, int y, int z)
    {
        ShipSquareBean square = level.get(x, y, z);
        if (square == null)
        {
            if (x%5 == 0)
                if (y%5 == 0)
                    return '+';
                else
                    return '-';
            else
                if (y%5 == 0)
                    return '|';
                else
                    return ' ';
        }
        switch (square.getType())
        {
            case ShipSquareBean.UNSET:
                return '.';
            case ShipSquareBean.HULL:
                return '@';
            case ShipSquareBean.MANEUVER:
                return 'M';
            case ShipSquareBean.JUMP:
                return 'J';
            case ShipSquareBean.POWER:
                return 'P';
            case ShipSquareBean.FUEL:
                return '~';
            case ShipSquareBean.TURRET:
                return 't';
            case ShipSquareBean.BAY:
                return 'b';
            case ShipSquareBean.SPINE:
                return '=';
            case ShipSquareBean.HANGER:
                return 'H';
            case ShipSquareBean.CARGO:
                return ' ';
            case ShipSquareBean.CORRIDOR:
                return '#';
            case ShipSquareBean.STATEROOM:
                return '%';
        }
        return '?';
    }
}
