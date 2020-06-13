package jo.ttg.ship.beans.comp;

public abstract class Weapon extends NumberedComponent
{
    public static final int TYPE_DISINTEGRATOR = 1;
    public static final int TYPE_JUMPPROJECTOR = 2;
    public static final int TYPE_TRACTOR = 3;
    public static final int TYPE_BEAM = 4;
    public static final int TYPE_MESON = 5;
    public static final int TYPE_MISSILE = 6;
    public static final int TYPE_PA = 7;
    
    public static final int TYPE_JUMPDAMPER = -1;
    public static final int TYPE_REPULSOR = -2;
    public static final int TYPE_SAND = -3;

    public abstract int getType();
    public abstract double getHardponits();
}
