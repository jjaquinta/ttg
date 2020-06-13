package jo.ttg.core.ui.swing.surf;

import java.awt.Point;
import java.util.Iterator;
import java.util.Map;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceAnnotationBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class SurfaceLogic
{
    public static MapHexBean[][] surfaceToGrid(SurfaceBean surface, Map<SurfaceAnnotationBean, Point> notes)
    {
        IHEALGlobe<MapHexBean> globe = surface.getGlobe();
        int gridSize = resolutionToGrid(globe.getResolution());
        MapHexBean[][] grid = new MapHexBean[4*gridSize][6*gridSize];
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord c = i.next();
            MapHexBean hex = globe.get(c);
            Point xy = coordToXY(c, gridSize);
            grid[xy.x][xy.y]= hex; 
        }
        for (SurfaceAnnotationBean note : surface.getAnnotations())
        {
            Point xy = coordToXY(note.getLocation(), gridSize);
            notes.put(note, xy);
        }
        return grid;
    }
    
    private static Point coordToXY(IHEALCoord o, int gridSize)
    {
        if (o instanceof jo.ttg.beans.surf.HEALCoord)
        {
            jo.ttg.beans.surf.HEALCoord c = (jo.ttg.beans.surf.HEALCoord)o;
            int tile = (int)c.getPixCoord(0);
            Point xy = new Point(gridSize*(tile%4 + 1) - 1, gridSize*(tile%4 + tile/4));
            for (int i = 1; i <= c.getResolution(); i++)
            {
                gridSize /= 2;
                int quad = (int)c.getPixCoord(i);
                switch (quad)
                {
                    case 0:
                        break;
                    case 2:
                        xy.y += gridSize;
                        break;
                    case 3:
                        xy.x -= gridSize;
                        xy.y += gridSize;
                        break;
                    case 1:
                        xy.x -= gridSize;
                       break;
                }
            }
            return xy;
        }
        else if (o instanceof jo.util.heal.impl.HEALGlobe.HEALCoord)
        {
            @SuppressWarnings("unchecked")
            jo.util.heal.impl.HEALGlobe<MapHexBean>.HEALCoord c = (jo.util.heal.impl.HEALGlobe<MapHexBean>.HEALCoord)o;
            Point xy = new Point(c.getX(), c.getY());
            return xy;
        }
        else if (o == null)
            throw new IllegalArgumentException("Expected a coord, not null.");
        else
            throw new IllegalArgumentException("Only jo.ttg.beans.surf.HEALCoord supported, not "+o.getClass().getName());
    }

    private static int resolutionToGrid(int resolution)
    {
        return (int)Math.pow(2, resolution);
    }
}
