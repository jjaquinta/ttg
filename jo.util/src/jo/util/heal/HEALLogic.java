package jo.util.heal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

import jo.util.heal.impl.HEALGlobe;
import jo.util.utils.MathUtils;

public class HEALLogic
{
    public static <T> IHEALGlobe<T> newInstance(int resolution, T init)
    {
        IHEALGlobe<T> globe = new HEALGlobe<T>(resolution);
        return globe;
    }
    
    public static IHEALVector move(IHEALVector v, int n)
    {
        IHEALVector ret = v;
        while (n-- > 0)
            ret = ret.move();
        return ret;
    }
    
    public static IHEALVector move(IHEALVector v, String instructions)
    {
        char[] inst = instructions.toCharArray();
        IHEALVector ret = v;
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
                    ret = ret.move();
                    ret.turnRight();
                    ret.turnRight();
                    break;
                case '1': case '2': case '3':
                case '4': case '5': case '6':
                case '7': case '8': case '9':
                    ret = move(ret, inst[i] - '0');
                    break;
                default:
                    ret = ret.move();
            }
        return ret;
    }
    
    public static IHEALCoord next(IHEALCoord o, int dir, int dist)
    {
        for (int i = 0; i < dist; i++)
            o = o.next(dir);
        return o;
    }
    
    public static double[] getThetaPhi(IHEALCoord c)
    {
        double[] ret = new double[3];
        double[][] box = c.getThetaPhiBox();
        tween(ret, box[0], box[2]);
        return ret;
    }
    
    public static double[] getLongLat(IHEALCoord c)
    {
        double[] thetaPhi = getThetaPhi(c);
        double[] ret = new double[2];
        ret[0] = thetaPhi[0]*360;
        ret[1] = thetaPhi[1]*360;
        return ret;
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
    
    private static double norm(double theta)
    {
        while (theta < 0)
            theta++;
        while (theta >= 1)
            theta--;
        return theta;
    }
    
    public static double getDistance(IHEALCoord c1, IHEALCoord c2)
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
    
    public static IHEALCoord getNearestOnPathTo(IHEALCoord ori, IHEALCoord dest)
    {
        Iterator<IHEALCoord> near = getNeighbors(ori).iterator();
        IHEALCoord ret = near.next();
        double best = getDistance(ret, dest);
        while (near.hasNext())
        {
            IHEALCoord tst = near.next();
            double tstDist = getDistance(tst, dest);
            if (tstDist <= best)
            {
                ret = tst;
                best = tstDist;
            }
        }
        return ret;
    }
    
    public static Collection<IHEALCoord> getPathBetween(IHEALCoord ori, IHEALCoord dest)
    {
        List<IHEALCoord> ret = new ArrayList<IHEALCoord>();
        //ret.add(ori);
        System.out.print('[');
        for (IHEALCoord i = ori; !i.equals(dest); i = getNearestOnPathTo(i, dest))
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
    
    private static Map<String, Collection<IHEALCoord>> mNeighborCache = new HashMap<>();
    
    public static Collection<IHEALCoord> getNeighbors(IHEALCoord o)
    {
        String key = o.toString();
        Collection<IHEALCoord> neighbors = mNeighborCache.get(key);
        if (neighbors != null)
            return neighbors;
        Map<String,IHEALCoord> ret = new HashMap<>();
        getSurrounds(o, 1, ret);
        ret.remove(o.toString());
        neighbors = ret.values();
        mNeighborCache.put(key, neighbors);
        return neighbors;
    }

    private static void getSurrounds(IHEALCoord o, int r, Map<String,IHEALCoord> map)
    {
        if (map.containsKey(o.toString()))
            return;
        map.put(o.toString(), o);
        if (r == 0)
            return;
        /*
        getSurrounds(o.next(IHEALCoord.D_NORTHEAST), r - 1, map);
        getSurrounds(o.next(IHEALCoord.D_SOUTHEAST), r - 1, map);
        getSurrounds(o.next(IHEALCoord.D_NORTHWEST), r - 1, map);
        getSurrounds(o.next(IHEALCoord.D_SOUTHWEST), r - 1, map);
        */
        for (int d = 0; d < IHEALCoord.D_MAX; d++)
            getSurrounds(o.next(d), r - 1, map);
    }
    
    public static Collection<IHEALCoord> getRadius(IHEALCoord o, int r)
    {
        Map<String,IHEALCoord> ret = new HashMap<>();
        getSurrounds(o, r, ret);
        Collection<IHEALCoord> radius = ret.values();
        return radius;
    }
    
    public static Collection<IHEALCoord> getNeighbors(Collection<IHEALCoord> group)
    {
        Map<String,IHEALCoord> ret = new HashMap<>();
        for (IHEALCoord iObj : group)
            for (IHEALCoord jObj : getNeighbors(iObj))
                ret.put(jObj.toString(), jObj);
        for (IHEALCoord iObj : group)
            ret.remove(iObj.toString());
        return ret.values();
    }
    
    public static int countBordering(IHEALCoord o, Collection<IHEALCoord> group)
    {
        Set<String> groupIndex = new HashSet<>();
        for (IHEALCoord i : group)
            groupIndex.add(i.toString());
        Map<String,IHEALCoord> edge = new HashMap<>();
        for (IHEALCoord i : getNeighbors(o))
            edge.put(i.toString(), i);
        Set<String> edgeIndex = edge.keySet();
        edgeIndex.retainAll(groupIndex);
        return edgeIndex.size();
    }
    
    public static double[][] getThetaPhiBox(int rez, int[] pixCoord)
    {
        double[][] ret = new double[4][2];
        int pix = pixCoord[0];
        double deltaTheta = (pix%4)*.250 + (pix/4)*.125;
        double deltaPhi = (pix/4)*.125;
        ret[0][0] = HEALLogic.norm(0.125 + deltaTheta); ret[0][1] = 0.250 - deltaPhi;
        ret[1][0] = HEALLogic.norm(0.250 + deltaTheta); ret[1][1] = 0.125 - deltaPhi;
        ret[2][0] = HEALLogic.norm(0.125 + deltaTheta); ret[2][1] = 0.000 - deltaPhi;
        ret[3][0] = HEALLogic.norm(0.000 + deltaTheta); ret[3][1] = 0.125 - deltaPhi;
        for (int r = 1; r <= rez; r++)
        {
            int subPix = pixCoord[r];
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
        for (int i = 0; i < ret.length; i++)
            normalize(ret[i]);
        return ret;
    }
    
    private static void normalize(double[] tp)
    {
        double p = Math.abs(tp[1]);
        if ((p <= .125) || (p >= .25)) 
            return;
        double oldt = tp[0];
        double tmed = Math.floor(oldt/0.25)*.25 + .125;
        double oldDelta = oldt - tmed;
        double oldSpread = MathUtils.interpolate(p, .125, .25, .125, .0);
        double newSpread = .125;
        double tmult = newSpread/oldSpread;
        double newDelta = oldDelta*tmult;
        double newt = tmed + newDelta;
        tp[0] = newt;
    }
    
    private static double[][] divide(double[][] pts)
    {
        double[][] ret = new double[9][2];
        
        copy(ret[0], pts[0]);
        copy(ret[2], pts[1]);
        copy(ret[4], pts[2]);
        copy(ret[6], pts[3]);
        HEALLogic.tween(ret[1], pts[0], pts[1]);
        HEALLogic.tween(ret[3], pts[2], pts[1]);
        HEALLogic.tween(ret[5], pts[3], pts[2]);
        HEALLogic.tween(ret[7], pts[3], pts[0]);
        HEALLogic.tween(ret[8], pts[0], pts[2]);
        return ret;
    }
    
    private static void copy(double[] dest, double[] p)
    {
        dest[0] = p[0];
        dest[1] = p[1];
    }
    
    public static <T> void opOnAll(IHEALGlobe<T> globe, Consumer<T> op)
    {
        for (Iterator<T> i = globe.iterator(); i.hasNext(); )
            op.accept(i.next());
    }
    
    public static <T> List<IHEALCoord> findAllCoords(IHEALGlobe<T> globe, Function<T, Boolean> matcher)
    {
        List<IHEALCoord> matches = new ArrayList<>();
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord o = i.next();
            if (matcher.apply(globe.get(o)))
                matches.add(o);
        }
        return matches;
    }
    
    public static <T> List<T> findAllContents(IHEALGlobe<T> globe, Function<T, Boolean> matcher)
    {
        List<T> matches = new ArrayList<>();
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord o = i.next();
            T item = globe.get(o);
            if (matcher.apply(item))
                matches.add(item);
        }
        return matches;
    }
    
    public static IHEALCoord findCenter(Collection<IHEALCoord> allOrds)
    {
        List<IHEALCoord> ords = new ArrayList<>();
        ords.addAll(allOrds);
        Map<String, IHEALCoord> coordMap = new HashMap<>();
        final Map<String, Integer> coordNeighbors = new HashMap<>();
        for (IHEALCoord o : ords)
            coordMap.put(o.toString(), o);
        for (IHEALCoord o : ords)
        {
            Collection<IHEALCoord> neighbors = getNeighbors(o);
            int count = 0;
            for (IHEALCoord j : neighbors)
                if (coordMap.containsKey(j.toString()))
                    count++;
            coordNeighbors.put(o.toString(), count);
        }
        Collections.sort(ords, new Comparator<IHEALCoord>() {
            @Override
            public int compare(IHEALCoord o1, IHEALCoord o2)
            {
                int c1 = coordNeighbors.get(o1.toString());
                int c2 = coordNeighbors.get(o2.toString());
                return c1 - c2;
            }
        });
        int max = coordNeighbors.get(ords.get(ords.size() - 1).toString());
        while (coordNeighbors.get(ords.get(0).toString()) < max)
            ords.remove(0);
        if (allOrds.size() == ords.size())
            return ords.get(0);
        return findCenter(ords);
    }
    
    public static IHEALCoord findCenterDist(Collection<IHEALCoord> allOrds)
    {
        List<IHEALCoord> ords = new ArrayList<>();
        ords.addAll(allOrds);
        double[] dist = new double[ords.size()];
        for (int i = 0; i < ords.size() - 1; i++)
        {
            IHEALCoord o1 = ords.get(i);
            for (int j = i + 1; j < ords.size(); j++)
            {
                IHEALCoord o2 = ords.get(j);
                double d = HEALLogic.getDistance(o1, o2);
                dist[i] += d;
                dist[j] += d;               
                //System.out.println("dist "+o1+"<->"+o2+" = "+d);
            }
        }
        IHEALCoord best = ords.get(0);
        double bestv = dist[0];
        //System.out.println(best+" = "+bestv);
        for (int i = 1; i < dist.length; i++)
        {
            //System.out.println(ords.get(i)+" = "+dist[i]);
            if (dist[i] < bestv)
            {
                bestv = dist[i];
                best = ords.get(i);
            }
        }
        //System.out.println("Center = "+best);
        return best;
    }
}
