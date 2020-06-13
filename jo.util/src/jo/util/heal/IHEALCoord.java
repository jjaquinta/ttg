package jo.util.heal;

public interface IHEALCoord
{
    public static final int D_NORTH     = 0;
    public static final int D_NORTHEAST = 1;
    public static final int D_EAST      = 2;
    public static final int D_SOUTHEAST = 3;
    public static final int D_SOUTH     = 4;
    public static final int D_SOUTHWEST = 5;
    public static final int D_WEST      = 6;
    public static final int D_NORTHWEST = 7;
    public static final int D_MAX       = 8;

    public IHEALCoord next();
    public IHEALCoord next(int dir);
    public IHEALVector makeVector(int dir);
    public double[][] getThetaPhiBox();
}
