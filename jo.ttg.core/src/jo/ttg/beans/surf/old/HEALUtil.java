package jo.ttg.beans.surf.old;

public class HEALUtil
{
    /*
	public static final int	G_OPEN = 0;
	public static final int	G_CLOSED = 1;
	public static final int	G_TIGHT = 2;
	
	public static final int ROT_XPLUS = 0;
	public static final int ROT_XMINUS = 1;
	public static final int ROT_YPLUS = 2;
	public static final int ROT_YMINUS = 3;
	public static final int ROT_ZPLUS = 4;
	public static final int ROT_ZMINUS = 5;
	
	private static HEALCoord move(HEALCoord o, int dir)
	{
		switch (dir)
		{
			case HEALCoord.D_NORTHEAST:
				return doSimpleMove(o, HEALVector.V_NE);
			case HEALCoord.D_SOUTHEAST:
				return doSimpleMove(o, HEALVector.V_SE);
			case HEALCoord.D_SOUTHWEST:
				return doSimpleMove(o, HEALVector.V_SW);
			case HEALCoord.D_NORTHWEST:
				return doSimpleMove(o, HEALVector.V_NW);
			case HEALCoord.D_NORTH:
				return doComplicatedMove(o, HEALVector.V_NW);
			case HEALCoord.D_SOUTH:
				return doComplicatedMove(o, HEALVector.V_SE);
			case HEALCoord.D_EAST:
				return doComplicatedMove(o, HEALVector.V_NE);
			case HEALCoord.D_WEST:
				return doComplicatedMove(o, HEALVector.V_SW);
		}
		return null;
	}
	
	private static HEALCoord doSimpleMove(HEALCoord o, int dir)
	{
		HEALVector v = new HEALVector(o, dir);
		return v.getNext();
	}
	
	private static HEALCoord doComplicatedMove(HEALCoord o, int dir)
	{
		HEALVector v;
		if (o.getData()%2 == 0)
		{
			v = new HEALVector(o, dir);
			v = v.getNext();
			v.turnRight();
		}
		else
		{
			v = new HEALVector(o, (dir+1)%HEALVector.V_MAX);
			v = v.getNext();
			v.turnLeft();
		}
		return v.getNext();
	}
	
	private static Collection<HEALCoord> getRadius(HEALCoord o, double r)
	{
		List<HEALCoord> ret = new ArrayList<HEALCoord>();
		for (Iterator<IHEALCoord> i = new HEALIterateCoord(o.getResolution()); i.hasNext(); )
		{
			HEALCoord c = (HEALCoord)i.next();
			if (getDistance(o, c) <= r)
			{
				ret.add(new HEALCoord(c));
			}
		}
		return ret;
	}
	
	private static Collection<HEALCoord> getRadius(HEALCoord o, int r)
	{
		Set<HEALCoord> ret = new HashSet<HEALCoord>();
		getSurrounds(o, r, ret);
		return ret;
	}
	
	private static Collection<HEALCoord> getNeighbors(HEALCoord o)
	{
		Set<HEALCoord> ret = new HashSet<HEALCoord>();
		getSurrounds(o, 1, ret);
		ret.remove(o);
		return ret;
	}

	private static void getSurrounds(HEALCoord o, int r, Set<HEALCoord> map)
	{
		if (map.contains(o))
			return;
		map.add(o);
		if (r == 0)
			return;
		for (int dir = 0; dir < HEALCoord.D_MAX; dir++)
			getSurrounds(move(o, dir), r - 1, map);
	}
	
	private static Collection<HEALCoord> getNeighbors(Set<HEALCoord> group)
	{
		Set<HEALCoord> ret = new HashSet<HEALCoord>();
		for (HEALCoord iObj : group)
			ret.addAll(getNeighbors(iObj));
		ret.removeAll(group);
		return ret;
	}
	
	private static int countBordering(HEALCoord o, Collection<HEALCoord> group)
	{
		Set<HEALCoord> edge = new HashSet<HEALCoord>();
		edge.addAll(getNeighbors(o));
		edge.retainAll(group);
		return edge.size();
	}
	
	private static Collection<HEALCoord> getSet(HEALCoord o, int num, int openness, Random r)
	{
		Set<HEALCoord> ret = new HashSet<HEALCoord>();
		ret.add(o);
		num--;
		while (num > 0)
		{
			Collection<HEALCoord> n = getNeighbors(ret);
			HEALCoord sel;
			if (openness == G_OPEN)
				sel = simpleSelect(n, r);
			else if (openness == G_CLOSED)
			{
				Map<HEALCoord,Integer> index = new HashMap<HEALCoord,Integer>();
				for (HEALCoord c : n)
					index.put(c, new Integer(countBordering(c, ret)));
				sel = biasedSelect(index, r);
			}
			else //if (openness == G_TIGHT)
			{
				List<HEALCoord> choices = new ArrayList<HEALCoord>();
				int best = 0;
                for (HEALCoord c : n)
				{
					int near = countBordering(c, ret);
					if (near == best)
						choices.add(c);
					else if (near > best)
					{
						best = near;
						choices.clear();
						choices.add(c);
					}
				}
				sel = simpleSelect(choices, r);
			}
			if (!ret.contains(sel))
			{
				ret.add(sel);
				num--;
			}
		}
		return ret;
	}
	
	private static HEALCoord simpleSelect(Collection<HEALCoord> index, Random r)
	{
		int target = Math.abs(r.nextInt())%index.size();
		for (Iterator<HEALCoord> i = index.iterator(); i.hasNext(); )
		{
			target--;
			if (target < 0)
				return i.next();
			i.next();
		}
		return null; // can't happen
	}
	
	private static HEALCoord biasedSelect(Map<HEALCoord,Integer> index, Random r)
	{
		int size = 0;
		for (Integer count : index.values())
			size += count;
		int target = Math.abs(r.nextInt())%size;
		for (HEALCoord key : index.keySet())
		{
			target -= index.get(key);
			if (target < 0)
				return key;
		}
		return null; // can't happen
	}
    
    // longitude
//    private final static double[][] thetaPhiBase =
//    {
//        { .000, .125}, { .250, .125}, { .500, .125}, { .750, .125}, 
//        { .125, .000}, { .375, .000}, { .625, .000}, { .875, .000}, 
//        { .250,-.125}, { .500,-.125}, { .750,-.125}, { .000,-.125}, 
//    };
    
    private static double[] getThetaPhi(HEALCoord c)
    {
        double[] ret = new double[3];
		double[][] box = getThetaPhiBox(c);
		tween(ret, box[0], box[2]);
        return ret;
    }
    
    private static int[] getLongLat(HEALCoord c)
    {
		double[] thetaPhi = getThetaPhi(c);
		int[] ret = new int[2];
		ret[0] = (int)(thetaPhi[0]*360);
		ret[1] = (int)(thetaPhi[1]*360);
		return ret;
	}

    public static double[][] getThetaPhiBox(HEALCoord c)
    {
        double[][] ret = new double[4][2];
        int pix = (int)c.getPixCoord(0);
        double deltaTheta = (pix%4)*.250 + (pix/4)*.125;
        double deltaPhi = (pix/4)*.125;
        ret[0][0] = norm(0.125 + deltaTheta); ret[0][1] = 0.250 - deltaPhi;
        ret[1][0] = norm(0.250 + deltaTheta); ret[1][1] = 0.125 - deltaPhi;
        ret[2][0] = norm(0.125 + deltaTheta); ret[2][1] = 0.000 - deltaPhi;
        ret[3][0] = norm(0.000 + deltaTheta); ret[3][1] = 0.125 - deltaPhi;
        for (int r = 1; r <= c.getResolution(); r++)
        {
            int subPix = (int)c.getPixCoord(r);
			double[][] div = divide(ret);
            switch (subPix)
            {
                case 0:
					copy(ret[0], div[0]);
					copy(ret[1], div[1]);
					copy(ret[2], div[8]);
					copy(ret[3], div[7]);
                    break;
                case 1:
					copy(ret[0], div[7]);
					copy(ret[1], div[8]);
					copy(ret[2], div[5]);
					copy(ret[3], div[6]);
                    break;
                case 2:
					copy(ret[0], div[1]);
					copy(ret[1], div[2]);
					copy(ret[2], div[3]);
					copy(ret[3], div[8]);
                    break;
                case 3:
					copy(ret[0], div[8]);
					copy(ret[1], div[3]);
					copy(ret[2], div[4]);
					copy(ret[3], div[5]);
                    break;
            }
        }
        return ret;
    }
    
    private static double norm(double theta)
    {
        while (theta < 0)
            theta++;
        while (theta >= 1)
            theta--;
        return theta;
    }
	
	private static double[][] divide(double[][] pts)
	{
		double[][] ret = new double[9][2];
		
		copy(ret[0], pts[0]);
		copy(ret[2], pts[1]);
		copy(ret[4], pts[2]);
		copy(ret[6], pts[3]);
		tween(ret[1], pts[0], pts[1]);
		tween(ret[3], pts[2], pts[1]);
		tween(ret[5], pts[3], pts[2]);
		tween(ret[7], pts[3], pts[0]);
		tween(ret[8], pts[0], pts[2]);
		return ret;
	}
    
//    private static double[][] collapse(double[][] pts, int dir)
//    {
//        double[][] alt = new double[4][2];
//        tween(alt[dir], pts[dir], pts[dir]);
//        tween(alt[(dir+1)%4], pts[dir], pts[(dir+1)%4]);
//        tween(alt[(dir+2)%4], pts[(dir+1)%4], pts[(dir+3)%4]);
//        tween(alt[(dir+3)%4], pts[dir], pts[(dir+3)%4]);
//        return alt;
//    }
    
    private static void copy(double[] dest, double[] p)
    {
		dest[0] = p[0];
		dest[1] = p[1];
	}
    
    private static void tween(double[] dest, double[] p1, double[] p2)
    {
        if (p1 == p2)
        {
            dest[0] = p1[0];
            dest[1] = p1[1];
        }
        else
        {
            if (p1[0] <= p2[0])
                dest[0] = (p1[0] + p2[0])/2;
            else
                dest[0] = norm((p1[0] + 1 + p2[0])/2);
            dest[1] = (p1[1] + p2[1])/2;
        }
    }
	
    private static int getCircumphrence(HEALCoord c)
	{
		return (int)(Math.pow(2, c.getResolution())*6);
	}
	
    private static Collection<HEALVector> getGreatCircle(HEALCoord c, int dir)
	{
		HEALVector v = new HEALVector(c, dir);
		List<HEALVector> ret = new ArrayList<HEALVector>();
		for (int i = getCircumphrence(c); i > 0; i--)
		{
			ret.add(v);
			v = v.getNext();
		}
		return ret;
	}
	
	private static int[] pixelRot0 = { 0, 1, 2, 3 };
	private static int[] pixelRot90 = { 2, 0, 3, 1 };
	private static int[] pixelRot180 = { 3, 2, 1, 0 };
	private static int[] pixelRot270 = { 1, 3, 0, 2 };
	
	private static int[][][] globRot =
	{
		// ROT_XPLUS   
	        { { 1, 2, 3, 0, 5, 6, 7, 4, 9,10,11, 8 }, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0 },
		// ROT_XPLUS   
	        { { 3, 0, 1, 2, 7, 4, 5, 6,11, 8, 9,10 }, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0, 
			pixelRot0, pixelRot0, pixelRot0, pixelRot0 },
		// ROT_YPLUS   
	        { { 4, 8, 5, 1,11, 9, 2, 0,10, 6, 3, 7 }, 
			pixelRot90, pixelRot0, pixelRot270, pixelRot180, 
			pixelRot90, pixelRot270, pixelRot270, pixelRot90, 
			pixelRot180, pixelRot270, pixelRot0, pixelRot90 },
		// ROT_YMINUS   
	        { { 7, 3, 6,10, 0, 2, 9,11, 1, 5, 8, 4 }, 
			pixelRot270, pixelRot180, pixelRot90, pixelRot0, 
			pixelRot270, pixelRot90, pixelRot90, pixelRot270, 
			pixelRot0, pixelRot90, pixelRot180, pixelRot270 },
		// ROT_ZPLUS   
	        { {11, 4, 0, 7, 8, 1, 3,10, 5, 2, 6, 9 }, 
			pixelRot0, pixelRot270, pixelRot180, pixelRot90, 
			pixelRot270, pixelRot270, pixelRot90, pixelRot90, 
			pixelRot270, pixelRot0, pixelRot90, pixelRot180 },
		// ROT_ZMINUS   
	        { { 2, 5, 9, 6, 1, 8,10, 3, 4,11, 7, 0 }, 
			pixelRot180, pixelRot90, pixelRot0, pixelRot270, 
			pixelRot90, pixelRot90, pixelRot270, pixelRot270, 
			pixelRot90, pixelRot180, pixelRot270, pixelRot0 },
	};
	
	private static HEALCoord rotate(HEALCoord c, int dir6)
	{
		HEALCoord ret = new HEALCoord(c.getResolution(), 0L);
		int square = (int)c.getPixCoord(0);
		ret.setPixCoord(0, globRot[dir6][0][square]);
		for (int i = 1; i <= c.getResolution(); i++)
		{
			int pix = (int)c.getPixCoord(i);
			ret.setPixCoord(i, globRot[dir6][square+1][pix]);
		}
		return ret;
	}
	
//	public static HEALGlobe rotate(HEALGlobe g, int dir6)
//	{        
//		HEALGlobe ret = new HEALGlobe(g.getResolution());
//		
//		for (Iterator<HEALCoord> i = g.coordsIterator(); i.hasNext(); )
//		{
//			HEALCoord c = i.next();
//			Object o = g.get(c);
//			c = rotate(c, dir6);
//			ret.set(c, o);
//		}
//		
//		return ret;
//	}
	
	private static double getDistance(HEALCoord c1, HEALCoord c2)
	{
		double[] longLat1 = getThetaPhi(c1);
		double[] longLat2 = getThetaPhi(c2);
		double lat1 = longLat1[1]*Math.PI*2;
		double lat2 = longLat2[1]*Math.PI*2;
		double lon1 = longLat1[0]*Math.PI*2 - Math.PI;
		double lon2 = longLat2[0]*Math.PI*2 - Math.PI;
		double dlon = lon1 - lon2;
		double dlat = lat1 - lat2;
		double a = Math.pow(Math.sin(dlat/2),2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon/2),2);
		double c = 2 * Math.atan2( Math.sqrt(a), Math.sqrt(1-a));
		return c;
	}
	
	private static HEALCoord getNearestOnPathTo(HEALCoord ori, HEALCoord dest)
	{
		Iterator<HEALCoord> near = getNeighbors(ori).iterator();
		HEALCoord ret = near.next();
		double best = getDistance(ret, dest);
		while (near.hasNext())
		{
			HEALCoord tst = near.next();
			double tstDist = getDistance(tst, dest);
			if (tstDist <= best)
			{
				ret = tst;
				best = tstDist;
			}
		}
		return ret;
	}
	
	private static Collection<HEALCoord> getPathBetween(HEALCoord ori, HEALCoord dest)
	{
		List<HEALCoord> ret = new ArrayList<HEALCoord>();
		//ret.add(ori);
		System.out.print('[');
		for (HEALCoord i = ori; !i.equals(dest); i = getNearestOnPathTo(i, dest))
		{
			if (ret.contains(i))
			{
				System.out.println("\nHUH? We already have "+i);
				return ret;
			}
			ret.add(i);
			System.out.print(i+".");
		}
		ret.add(dest);
		System.out.println(dest+"]");
		return ret;
	}
	
	public static void main(String[] argv)
	{
	    for (int i = 0; i < 12; i++)
	    {
	        HEALCoord c = new HEALCoord(0, i);
//            double[] tp = getThetaPhi(c);
//            System.out.println("#"+i+" "+tp[0]+","+tp[1]);
            int[] ll = getLongLat(c);
            System.out.println("#"+i+" "+ll[0]+","+ll[1]);
	        //double[][] box = getThetaPhiBox(c);
	        //System.out.println("#"+i+" "+box[0][0]+","+box[0][1]+" "+box[1][0]+","+box[1][1]+" "+box[2][0]+","+box[2][1]+" "+box[3][0]+","+box[3][1]);
	        //System.out.println("#"+i+"\t"+box[0][0]+"\t"+box[1][0]+"\t"+box[2][0]+"\t"+box[3][0]);
//	        if (box[3][0] > box[1][0]) // spans meridian
//	        {
//	            System.out.println("#"+i+"\t"+box[0][0]+"\t"+box[1][0]+"\t"+box[2][0]+"\t"+box[3][0]);
//	            double[][] div = divide(box);
//	            for (int j = 0; j < div.length; j++)
//	            {
//	                System.out.println("#"+i+"."+j+"\t"+div[j][0]);
//	            }
//	        }
	    }
	}
	*/
}
