package jo.ttg.beans.surf.old;
import java.util.Random;

public class HEALVector extends HEALCoord
{
	public static final int	V_NE = 0;
	public static final int	V_SE = 1;
	public static final int	V_SW = 2;
	public static final int	V_NW = 3;
	public static final int	V_MAX = 4;

	private int	mDirection;
	
	public int getDirection()
	{
		return mDirection;
	}
	
	public void setDirection(int v)
	{
		mDirection = Math.abs(v)%V_MAX;
	}

	public HEALVector()
	{
		this(0 ,0, 0);
	}
	
	public HEALVector(HEALVector c)
	{
		this(c.getResolution(), c.getData(), c.getDirection());
	}
	
	public HEALVector(HEALCoord c)
	{
		this(c.getResolution(), c.getData(), 0);
	}
	
	public HEALVector(HEALCoord c, int direction)
	{
		this(c.getResolution(), c.getData(), direction);
	}
	
	public HEALVector(int resolution, long data, int direction)
	{
		super(resolution, data);
		mDirection = direction;
	}
	
	public HEALVector(int resolution, Random rnd)
	{
		super(resolution, rnd);
		setDirection(Math.abs(rnd.nextInt())%V_MAX);
	}
	
	public HEALVector getParentVector()
	{
		return new HEALVector(getResolution() - 1, getData(), mDirection);
	}
	
	public void turnLeft()
	{
		mDirection = (mDirection + 3)%V_MAX;
	}
	
	public void turnRight()
	{
		mDirection = (mDirection + 1)%V_MAX;
	}

	private static final int[][][] res0Map = 
	{
	  /* V_NE */ { { 1, 1}, { 2, 1}, { 3, 1}, { 0, 1}, { 1, 0}, { 2, 0}, { 3, 0}, { 0, 0}, { 5, 0}, { 6, 0}, { 7, 0}, { 4, 0} },
	  /* V_SE */ { { 4, 0}, { 5, 0}, { 6, 0}, { 7, 0}, { 8, 0}, { 9, 0}, {10, 0}, {11, 0}, { 9, 3}, {10, 3}, {11, 3}, { 8, 3} },
	  /* V_SW */ { { 7, 0}, { 4, 0}, { 5, 0}, { 6, 0}, {11, 0}, { 8, 0}, { 9, 0}, {10, 0}, {11, 1}, { 8, 1}, { 9, 1}, {10, 1} },
	  /* V_NW */ { { 3, 3}, { 0, 3}, { 1, 3}, { 2, 3}, { 0, 0}, { 1, 0}, { 2, 0}, { 3, 0}, { 4, 0}, { 5, 0}, { 6, 0}, { 7, 0} },
	};
	
	private static final int[][][] resNMap = 
	{
	  /* D_NE */ { {V_NE, 1}, {-1  , 0}, {V_NE, 3}, {-1  , 2} },
	  /* V_SE */ { {-1  , 2}, {-1  , 3}, {V_SE, 0}, {V_SE, 1} },
	  /* V_SW */ { {-1  , 1}, {V_SW, 0}, {-1  , 3}, {V_SW, 2} },
	  /* V_NW */ { {V_NW, 2}, {V_NW, 3}, {-1  , 0}, {-1  , 1} },
	};
	
	public HEALVector getNext()
	{
		HEALVector ret = new HEALVector(this);
		long pix = getPixCoord();
		if (getResolution() == 0)
		{
			ret.setPixCoord(res0Map[mDirection][(int)pix][0]);
			ret.setDirection(ret.getDirection()+res0Map[mDirection][(int)pix][1]);
		}
		else
		{
			if (resNMap[mDirection][(int)pix][0] != -1)
			{
				HEALVector par = getParentVector().getNext();
				ret.setData(par.getData());
				ret.setDirection(par.getDirection());
			}
			pix = resNMap[mDirection][(int)pix][1];
			if (getDirection() != ret.getDirection())
			{
				pix = rotatePixCoord(getDirection() - ret.getDirection(), (int)pix);
			}
			ret.setPixCoord(pix);
		}
		return ret;
	}
	
	public HEALVector getNext(int n)
	{
		HEALVector ret = this;
		while (n-- > 0)
			ret = ret.getNext();
		return ret;
	}
	
	public HEALVector getNext(String instructions)
	{
		char[] inst = instructions.toCharArray();
		HEALVector ret = new HEALVector(this);
		for (int i = 0; i < inst.length; i++)
			switch (inst[i])
			{
				case 'L':
				case 'l':
					ret.turnLeft();
					break;
				case 'R':
				case 'r':
					ret.turnRight();
					break;
				case 'B':
				case 'b':
					ret.turnRight();
					ret.turnRight();
					ret = ret.getNext();
					ret.turnRight();
					ret.turnRight();
					break;
				case '1': case '2': case '3':
				case '4': case '5': case '6':
				case '7': case '8': case '9':
					ret = ret.getNext(inst[i] - '0');
					break;
				default:
					ret = ret.getNext();
			}
		return ret;
	}
	
	private static final int[] rotLeft  = { 1, 3, 0, 2 };
	private static final int[] rotRight = { 2, 0, 3, 1 };
	private static final int[] rotHalf  = { 3, 2, 1, 0 };
	
	int rotatePixCoord(int spin, int pix)
	{
		if (spin < 0)
			spin = -spin + 2;
		spin %= V_MAX;
		switch (spin)
		{
			case 1:
				return rotLeft[pix];
			case 2:
				return rotHalf[pix];
			case 3:
				return rotRight[pix];
		}
		return pix;
	}

	public String toString()
	{
		return "HEALVector[resolution="+getResolution()+",data="+Long.toHexString(getData())+",dir="+mDirection+"]";
	}
	
}
