package jo.ttg.port.beans;

public class PortCube
{
    private int mX;
    private int mY;
    private int mZ;
    private int mDX;
    private int mDY;
    private int mDZ;
    
    // constructors
    public PortCube(int x, int y, int z, int dx, int dy, int dz)
    {
        mX = x;
        mY = y;
        mZ = z;
        mDX = dx;
        mDY = dy;
        mDZ = dz;
    }
    
    // utilities
    
    public boolean isOnXM(int x)
    {
        return mX == x;
    }
    
    public boolean isOnXP(int x)
    {
        return mX + mDX - 1 == x;
    }
    
    public boolean isOnYM(int y)
    {
        return mY == y;
    }
    
    public boolean isOnYP(int y)
    {
        return mY + mDY - 1 == y;
    }
    
    public boolean isOnZM(int z)
    {
        return mZ == z;
    }
    
    public boolean isOnZP(int z)
    {
        return mZ + mDZ - 1 == z;
    }
    
    // getters and setters
    
    public int getX()
    {
        return mX;
    }
    public void setX(int x)
    {
        mX = x;
    }
    public int getY()
    {
        return mY;
    }
    public void setY(int y)
    {
        mY = y;
    }
    public int getZ()
    {
        return mZ;
    }
    public void setZ(int z)
    {
        mZ = z;
    }
    public int getDX()
    {
        return mDX;
    }
    public void setDX(int dX)
    {
        mDX = dX;
    }
    public int getDY()
    {
        return mDY;
    }
    public void setDY(int dY)
    {
        mDY = dY;
    }
    public int getDZ()
    {
        return mDZ;
    }
    public void setDZ(int dZ)
    {
        mDZ = dZ;
    }
}
