package jo.ttg.ship.beans.plan;

import java.awt.Color;

public class ShipImageSettingsBean
{
    private int mBoundaryGutter = 48;
    private int mSquareSize = 24;
    private boolean mColoredBackgrounds = true;
    private boolean mDrawInteriors = true;
    private long mSeed;
    
    public Color BACKGROUND_COLOR = Color.LIGHT_GRAY;
    public Color GRID_BACK_COLOR = BACKGROUND_COLOR.darker();
    public Color GRID_SHIP_COLOR = new Color(GRID_BACK_COLOR.getRed(), GRID_BACK_COLOR.getGreen(), GRID_BACK_COLOR.getBlue(), 127);
    public Color BACKGROUND_UNSET = Color.LIGHT_GRAY;
    public Color BACKGROUND_HULL = Color.DARK_GRAY;
    public Color BACKGROUND_MANEUVER = Color.YELLOW.brighter().brighter().brighter();
    public Color BACKGROUND_JUMP = Color.YELLOW.brighter().brighter();
    public Color BACKGROUND_POWER = Color.YELLOW.brighter();
    public Color BACKGROUND_FUEL = Color.ORANGE;
    public Color BACKGROUND_TURRET = Color.RED.brighter().brighter().brighter();
    public Color BACKGROUND_BAY = Color.RED.brighter().brighter().brighter();
    public Color BACKGROUND_SPINE = Color.RED.brighter().brighter().brighter();
    public Color BACKGROUND_HANGER = Color.GREEN.brighter().brighter().brighter();
    public Color BACKGROUND_STATEROOM = Color.CYAN;
    public Color BACKGROUND_CORRIDOR = Color.CYAN;
    public Color BACKGROUND_CARGO = Color.BLUE.brighter().brighter().brighter();
    public Color TEXT_BACK = new Color(Color.PINK.getRed(), Color.PINK.getGreen(), Color.PINK.getBlue(), 127);
    public Color TEXT_FORE = Color.BLACK;
    
    // utilities
    public int x(int x)
    {
        return mBoundaryGutter + x*mSquareSize;
    }
    
    public int y(int y)
    {
        return mBoundaryGutter + y*mSquareSize;
    }

    public Color squareToColor(ShipSquareBean square)
    {
        if (!isColoredBackgrounds())
            return BACKGROUND_UNSET;
        switch (square.getType())
        {
            case ShipSquareBean.UNSET:
                return BACKGROUND_UNSET;
            case ShipSquareBean.HULL:
                return BACKGROUND_HULL;
            case ShipSquareBean.MANEUVER:
                return BACKGROUND_MANEUVER;
            case ShipSquareBean.JUMP:
                return BACKGROUND_JUMP;
            case ShipSquareBean.POWER:
                return BACKGROUND_POWER;
            case ShipSquareBean.FUEL:
                return BACKGROUND_FUEL;
            case ShipSquareBean.TURRET:
                return BACKGROUND_TURRET;
            case ShipSquareBean.BAY:
                return BACKGROUND_BAY;
            case ShipSquareBean.HANGER:
                return BACKGROUND_HANGER;
            case ShipSquareBean.CARGO:
                return BACKGROUND_CARGO;
            case ShipSquareBean.SPINE:
                return BACKGROUND_SPINE;
            case ShipSquareBean.STATEROOM:
                return BACKGROUND_STATEROOM;
            case ShipSquareBean.CORRIDOR:
                return BACKGROUND_CORRIDOR;
        }
        throw new IllegalArgumentException("Unsupported square type "+square.getType());
    }
    
    // getter and setters
    
    public int getBoundaryGutter()
    {
        return mBoundaryGutter;
    }
    public void setBoundaryGutter(int boundaryGutter)
    {
        mBoundaryGutter = boundaryGutter;
    }
    public int getSquareSize()
    {
        return mSquareSize;
    }
    public void setSquareSize(int squareSize)
    {
        mSquareSize = squareSize;
    }

    public boolean isColoredBackgrounds()
    {
        return mColoredBackgrounds;
    }

    public void setColoredBackgrounds(boolean coloredBackgrounds)
    {
        mColoredBackgrounds = coloredBackgrounds;
    }

    public boolean isDrawInteriors()
    {
        return mDrawInteriors;
    }

    public void setDrawInteriors(boolean drawInteriors)
    {
        mDrawInteriors = drawInteriors;
    }

    public long getSeed()
    {
        return mSeed;
    }

    public void setSeed(long seed)
    {
        mSeed = seed;
    }
    
    
}
