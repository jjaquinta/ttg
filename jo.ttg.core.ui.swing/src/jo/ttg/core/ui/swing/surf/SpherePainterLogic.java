package jo.ttg.core.ui.swing.surf;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.beans.surf.SurfaceBean;
import jo.ttg.beans.sys.BodyWorldBean;
import jo.util.ui.swing.logic.ColorUtils;
import jo.util.utils.MathUtils;

public class SpherePainterLogic extends CommonPainterLogic
{
    // globals thread safe because of synchronization
    private static SurfaceBean  mSurface;
    private static int mSurfaceType;
    private static double mLowPoint;
    private static double mHighPoint;
    
    private static Graphics2D mG;
    
    public static synchronized BufferedImage paintSphere(SurfaceBean surface, int width, int height, double rotation)
    {
        BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        paintSphere((Graphics2D)img.getGraphics(), surface, width, height, rotation);
        return img;
    }
    public static synchronized void paintSphere(Graphics2D g, SurfaceBean surface, int width, int height, double rotation)
    {
        mG = g;
        mSurface = surface;
        BodyWorldBean world = (BodyWorldBean)surface.getBody();
        mSurfaceType = getSurfaceType(world);
        mG.setColor(new Color(0, 0, 0, 0));
        mG.fillRect(0, 0, width, height);
        int radius = Math.min(width, height)/2;
        double scaley = radius/90.0;
        double scalex = radius/90.0;
        mG.translate(0, height/2);
        mG.scale(scalex, scaley);
        categorize();
        drawSurface(rotation);
        mG.dispose();
    }
    
    private static void categorize()
    {
        boolean first = true;
        for (Iterator<MapHexBean> i = mSurface.getGlobe().iterator(); i.hasNext(); )
        {
            MapHexBean hex = i.next();
            if (!isSea(hex))
            {
                if (first)
                {
                    mLowPoint = hex.getAltitude();
                    mHighPoint = hex.getAltitude();
                    first = false;
                }
                else
                {
                    mLowPoint = Math.min(mLowPoint, hex.getAltitude());
                    mHighPoint = Math.max(mHighPoint, hex.getAltitude());
                }
            }
        }
    }
    
    private static Color[] getColorAltitude()
    {
        switch (mSurfaceType)
        {
            case SURFACE_VERDANT:
                return COLOR_ALTITUDE_VERDANT;
            case SURFACE_ICY:
                return COLOR_ALTITUDE_ICY;
            case SURFACE_DESOLATE:
                return COLOR_ALTITUDE_DESOLATE;
            case SURFACE_BARREN:
                return COLOR_ALTITUDE_BARREN;
        }
        throw new IllegalArgumentException("Unhandled surface type "+mSurfaceType);
    }
    
    private static void drawSurface(double rotation)
    {
        Color[] COLOR_ALTITUDE = getColorAltitude();
        double minx = 0;
        double miny = 0;
        double maxx = 0;
        double maxy = 0;
        Ellipse2D sp = new Ellipse2D.Double(0, -90, 180, 180);
        mG.setColor(COLOR_ALTITUDE[COLOR_ALTITUDE.length/2]);
        mG.fill(sp);
        for (Iterator<MapHexBean> i = mSurface.getGlobe().iterator(); i.hasNext(); )
        {
            MapHexBean hex = i.next();
            /*
            double[] longlat = HEALLogic.getLongLat(hex.getLocation());
            if (longlat[0] > 180)
                continue;
            Color c = calcColor(hex, COLOR_ALTITUDE);
            mG.setColor(c);
            mG.fill(new Rectangle2D.Double(longlat[0], longlat[1], 1, 1));
            minx = Math.min(minx, longlat[0]);
            maxx = Math.max(maxx, longlat[0]);
            miny = Math.min(miny, longlat[1]);
            maxy = Math.max(maxy, longlat[1]);
            */
            double[][] tpb = getPoints(hex, rotation);
            if (tpb == null)
                continue;
            minx = Math.min(minx, tpb[0][0]);
            maxx = Math.max(maxx, tpb[0][0]);
            miny = Math.min(miny, tpb[0][1]);
            maxy = Math.max(maxy, tpb[0][1]);
            Path2D p = new Path2D.Double();
            p.moveTo(tpb[0][0], tpb[0][1]);
            for (int j = 1; j < tpb.length; j++)
                p.lineTo(tpb[j][0], tpb[j][1]);
            Color c = calcColor(hex, COLOR_ALTITUDE);
            mG.setColor(c);
            mG.fill(p);
        }
        /*
        System.out.println("Ords range from "+minx+","+miny+" to "+maxx+","+maxy);
        mG.setColor(Color.WHITE);
        mG.drawLine(0, -90, 180, 90);
        mG.drawLine(0, 90, 180, -90);
        mG.drawLine(0, -90, 180, -90);
        mG.drawLine(0, 90, 180, 90);
        mG.drawLine(0, -90, 0, 90);
        mG.drawLine(180, -90, 180, 90);
        */
    }
    private static double[][] getPoints(MapHexBean hex, double rotation)
    {
        double[][] tpb = hex.getLocation().getThetaPhiBox();
        for (int i = 0; i < tpb.length; i++)
        {
            double x = tpb[i][0];
            double y = tpb[i][1];
            x *= 360;
            y *= 360;
            if (x > rotation + 360)
                x -= 360;
            else if (x < rotation)
                x += 360;
            if ((x < rotation) || (x > rotation + 180))
                return null;
            // to units
            x = (x - rotation)/90 - 1;
            y = y/90;
            double tx = x * Math.sqrt(1 - y * y / 2); 
            double ty = y * Math.sqrt(1 - x * x / 2);
            // convert from units
            tx = tx*90 + 90;
            ty = ty*90;
            tpb[i][0] = tx;
            tpb[i][1] = ty;
        }
        return tpb;
    }

    private static Color calcColor(MapHexBean hex, Color[] COLOR_ALTITUDE)
    {
        switch (hex.getCover())
        {
            case MapHexBean.C_DEEP:
                return COLOR_DEPTHS;                
            case MapHexBean.C_WICE:
            case MapHexBean.C_WATER:
                return COLOR_OCEAN;                
            case MapHexBean.C_SICE:
                return COLOR_ICEPACK;                
        }
        double idx = MathUtils.interpolate(hex.getAltitude(), mLowPoint, mHighPoint, 0, COLOR_ALTITUDE.length-1);
        Color lowColor = COLOR_ALTITUDE[(int)Math.floor(idx)];
        Color highColor = COLOR_ALTITUDE[(int)Math.ceil(idx)];
        Color thisColor = ColorUtils.interpolate(idx, Math.floor(idx), Math.ceil(idx), lowColor, highColor);
        return thisColor;
    }

    private static boolean isSea(MapHexBean hex)
    {
        switch (hex.getCover())
        {
            case MapHexBean.C_DEEP:
            case MapHexBean.C_SICE:
            case MapHexBean.C_WATER:
            case MapHexBean.C_WICE:
                return true;                
        }
        return false;
    }
}
