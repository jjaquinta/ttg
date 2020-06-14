package jo.ttg.core.report.surf;

import java.util.Iterator;

import jo.ttg.beans.surf.MapHexBean;
import jo.ttg.core.report.sys.SVGHelper;
import jo.ttg.core.report.sys.SVGStyle;
import jo.util.heal.IHEALCoord;
import jo.util.heal.IHEALGlobe;

public class CoverReport
{    
    private static final String ICE_COLOR = "#F0F0F0";
    private static final String WATER_COLOR = "#3399FF";
    private static final String OPEN_COLOR = "#ADEBAD";
    
    private static SVGStyle GRID_STYLE = new SVGStyle();
    static
    {
        GRID_STYLE.mStroke = 0xC0C0C0;
        GRID_STYLE.mStrokeWidth = 0.002;
        GRID_STYLE.mFill = 0xE0E0E0;
        GRID_STYLE.mFillOpacity = 0.0;
    }
    private static SVGStyle WATER_CRACK_STYLE = new SVGStyle();
    static
    {
        WATER_CRACK_STYLE.mStroke = WATER_COLOR;
        WATER_CRACK_STYLE.mStrokeWidth = 0.002;
        WATER_CRACK_STYLE.mFill = 0xE0E0E0;
        WATER_CRACK_STYLE.mFillOpacity = 0.0;
    }
    private static SVGStyle OPEN_CRACK_STYLE = new SVGStyle();
    static
    {
        OPEN_CRACK_STYLE.mStroke = OPEN_COLOR;
        OPEN_CRACK_STYLE.mStrokeWidth = 0.002;
        OPEN_CRACK_STYLE.mFill = 0xE0E0E0;
        OPEN_CRACK_STYLE.mFillOpacity = 0.0;
    }
    private static SVGStyle CITY_STYLE = new SVGStyle();
    static
    {
        CITY_STYLE.mFill = "pink";
    }
    private static SVGStyle DEEP_STYLE = new SVGStyle();
    static
    {
        DEEP_STYLE.mFill = "#0066ff";
    }
    private static SVGStyle DESERT_STYLE = new SVGStyle();
    static
    {
        DESERT_STYLE.mFill = "#CCFF66";
    }
    private static SVGStyle FOREST_STYLE = new SVGStyle();
    static
    {
        FOREST_STYLE.mFill = "#009900";
    }
    private static SVGStyle ISLAND_STYLE = new SVGStyle();
    static
    {
        ISLAND_STYLE.mFill = OPEN_COLOR;
    }
    private static SVGStyle JUNGLE_STYLE = new SVGStyle();
    static
    {
        FOREST_STYLE.mFill = "#004c00";
    }
    private static SVGStyle LAKE_STYLE = new SVGStyle();
    static
    {
        LAKE_STYLE.mFill = WATER_COLOR;
    }
    private static SVGStyle MOUNTAINS_STYLE = new SVGStyle();
    static
    {
        MOUNTAINS_STYLE.mFill = "gray";
    }
    private static SVGStyle OPEN_STYLE = new SVGStyle();
    static
    {
        OPEN_STYLE.mFill = OPEN_COLOR;
    }
    private static SVGStyle ROUGH_STYLE = new SVGStyle();
    static
    {
        ROUGH_STYLE.mFill = "olive";
    }
    private static SVGStyle SICE_STYLE = new SVGStyle();
    static
    {
        SICE_STYLE.mFill = ICE_COLOR;
    }
    private static SVGStyle TUNDRA_STYLE = new SVGStyle();
    static
    {
        TUNDRA_STYLE.mFill = ICE_COLOR;
    }
    private static SVGStyle VOLCANO_STYLE = new SVGStyle();
    static
    {
        VOLCANO_STYLE.mFill = "red";
    }
    private static SVGStyle WATER_STYLE = new SVGStyle();
    static
    {
        WATER_STYLE.mFill = WATER_COLOR;
    }
    private static SVGStyle WICE_STYLE = new SVGStyle();
    static
    {
        WICE_STYLE.mFill = ICE_COLOR;
    }

    public static void drawMap(SVGHelper legend, SVGHelper grid, IHEALGlobe<MapHexBean> globe)
    {
        drawGrid(grid, globe);
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord c = i.next();
            MapHexBean hex = globe.get(c);
            drawHex(legend, hex);
        }
    }

    private static void drawHex(SVGHelper legend, MapHexBean hex)
    {
        int cover = hex.getCover();
        double[] hp = makeHexPoly(hex.getLocation());
        double[] o = new double[] { hp[4], hp[5] };
        double side = hp[2] - hp[0];
        switch (cover)
        {
            case MapHexBean.C_CITY:
                legend.drawPolygon(CITY_STYLE, hp);
                break;
            case MapHexBean.C_DEEP:
                legend.drawPolygon(DEEP_STYLE, hp);
                break;
            case MapHexBean.C_DESERT:
                legend.drawPolygon(DESERT_STYLE, hp);
                break;
            case MapHexBean.C_FOREST:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawPolygon(FOREST_STYLE, makeTreePoly(o, side));
                break;
            case MapHexBean.C_ISLAND:
                legend.drawPolygon(WATER_STYLE, hp);
                legend.drawCircle(ISLAND_STYLE, o[0], o[1]+side, side*.5);
                break;
            case MapHexBean.C_JUNGLE:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawPolygon(JUNGLE_STYLE, makeTreePoly(o, side));
                break;
            case MapHexBean.C_LAKE:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawCircle(LAKE_STYLE, o[0], o[1]+side, side*.5);
                break;
            case MapHexBean.C_MTNS:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawPolygon(MOUNTAINS_STYLE, makeMountainPoly(o, side));
                break;
            case MapHexBean.C_OPEN:
                legend.drawPolygon(OPEN_STYLE, hp);
                break;
            case MapHexBean.C_ROUGH:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawPolygon(ROUGH_STYLE, makeHillPoly(o[0], o[1] + 4*side/8, side));
                legend.drawPolygon(ROUGH_STYLE, makeHillPoly(o[0] - 4*side/8, o[1] + 8*side/8, side));
                legend.drawPolygon(ROUGH_STYLE, makeHillPoly(o[0] + 4*side/8, o[1] + 8*side/8, side));
                legend.drawPolygon(ROUGH_STYLE, makeHillPoly(o[0], o[1] + 12*side/8, side));
                break;
            case MapHexBean.C_SICE:
                legend.drawPolygon(SICE_STYLE, hp);
                legend.drawPolyline(WATER_CRACK_STYLE, makeCrack1Poly(o, side));
                legend.drawPolyline(WATER_CRACK_STYLE, makeCrack2Poly(o, side));
                break;
            case MapHexBean.C_TUNDRA:
                legend.drawPolygon(TUNDRA_STYLE, hp);
                legend.drawPolyline(OPEN_CRACK_STYLE, makeCrack1Poly(o, side));
                //legend.drawPolyline(OPEN_CRACK_STYLE, makeCrack2Poly(o, side));
                break;
            case MapHexBean.C_VOLC:
                legend.drawPolygon(OPEN_STYLE, hp);
                legend.drawPolygon(VOLCANO_STYLE, makeMountainPoly(o, side));
                break;
            case MapHexBean.C_WATER:
                legend.drawPolygon(WATER_STYLE, hp);
                break;
            case MapHexBean.C_WICE:
                legend.drawPolygon(WICE_STYLE, hp);
                legend.drawPolyline(WATER_CRACK_STYLE, makeCrack1Poly(o, side));
                break;
            default:
                System.out.println("Unknown cover: "+cover);
                break;
        }
    }
    
    public static double[] makeHexPoly(IHEALCoord c)
    {
        double[][] box = c.getThetaPhiBox();
        // normalize
        if (box[0][0] < box[3][0])
            box[0][0] += 1;
        if (box[2][0] < box[3][0])
            box[2][0] += 1;
        if (box[1][0] < box[0][0])
            box[1][0] += 1;
        // scale
        double[] points = new double[8];
        for (int i = 0; i < 4; i++)
        {
            points[i*2+0] = box[i][0];
            points[i*2+1] = box[i][1];
        }
        return points;
    }
    
    private static double[] makeMountainPoly(double[] xy, double side)
    {
        double[] points = new double[6];
        points[0] = xy[0];
        points[1] = xy[1] + side*1/6;
        points[2] = xy[0] + side*4/6;
        points[3] = xy[1] + side*8/6;
        points[4] = xy[0] - side*4/6;
        points[5] = xy[1] + side*8/6;
        return points;
    }
    
//    private static String makeHillPath(double x, double y, double side)
//    {
//        StringBuffer path = new StringBuffer();
//        path.append("M ");
//        path.append(x - side*2/8);
//        path.append(" ");
//        path.append(y);
//        path.append(" q ");
//        path.append(x);
//        path.append(" ");
//        path.append(y - side*3/8);
//        path.append(" ");
//        path.append(x + side*2/8);
//        path.append(" ");
//        path.append(y);
//        path.append(" Z");
//        return path.toString();
//    }
    
    private static double[] makeHillPoly(double x, double y, double side)
    {
        double[] points = new double[6];
        points[0] = x - side*2/8;
        points[1] = y;
        points[2] = x;
        points[3] = y - side*2/8;
        points[4] = x + side*2/8;
        points[5] = y;
        return points;
    }
    
    private static double[] makeTreePoly(double[] xy, double side)
    {
        double[] points = new double[14];
        points[0] = xy[0];
        points[1] = xy[1] + side*1/4;
        points[2] = xy[0] + side*3/4;
        points[3] = xy[1] + side*5/4;
        points[4] = xy[0] + side*1/4;
        points[5] = xy[1] + side*5/4;
        points[6] = xy[0] + side*1/4;
        points[7] = xy[1] + side*6/4;
        points[8] = xy[0] - side*1/4;
        points[9] = xy[1] + side*6/4;
        points[10] = xy[0] - side*1/4;
        points[11] = xy[1] + side*5/4;
        points[12] = xy[0] - side*3/4;
        points[13] = xy[1] + side*5/4;
        return points;
    }
    
    private static double[] makeCrack1Poly(double[] xy, double side)
    {
        double[] points = new double[12];
        points[0] = xy[0] - side*7/8;
        points[1] = xy[1] + side*7/8;
        points[2] = xy[0] + side*6/8;
        points[3] = xy[1] + side*8/8;
        points[4] = xy[0] - side*5/8;
        points[5] = xy[1] + side*6/8;
        points[6] = xy[0] - side*1/8;
        points[7] = xy[1] + side*9/8;
        points[8] = xy[0] + side*4/8;
        points[9] = xy[1] + side*4/8;
        points[10] = xy[0] + side*7/8;
        points[11] = xy[1] + side*9/8;
        return points;
    }

    private static double[] makeCrack2Poly(double[] xy, double side)
    {
        double[] points = new double[12];
        points[0] = xy[0] + side*1/8;
        points[1] = xy[1] + side*1/8;
        points[2] = xy[0] + side*0/8;
        points[3] = xy[1] + side*2/8;
        points[4] = xy[0] + side*4/8;
        points[5] = xy[1] + side*6/8;
        points[6] = xy[0] + side*1/8;
        points[7] = xy[1] + side*9/8;
        points[8] = xy[0] - side*2/8;
        points[9] = xy[1] + side*6/8;
        points[10] = xy[0] - side*6/8;
        points[11] = xy[1] + side*10/8;
        return points;
    }

    public static void drawGrid(SVGHelper grid, IHEALGlobe<MapHexBean> globe)
    {
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
            drawGrid(grid, i.next());
    }

    private static void drawGrid(SVGHelper grid, IHEALCoord c)
    {
        double[] points = makeHexPoly(c);
        grid.drawPolygon(GRID_STYLE, points);
    }
    
}
