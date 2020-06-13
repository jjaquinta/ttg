package jo.util.heal;

public class HEALTest
{
    /*
    private static int MULT = 16;

    private static BufferedImage testGrid(IHEALGlobe<?> globe)
    {
        int side = (int)Math.pow(2, globe.getResolution());
        BufferedImage img = new BufferedImage(side*4*MULT, side*6*MULT, BufferedImage.TYPE_INT_ARGB);
        Graphics g = img.getGraphics();
        g.setColor(Color.white);
        g.fillRect(0, 0, img.getWidth(), img.getHeight());
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            @SuppressWarnings("rawtypes")
            HEALCoord o = (HEALCoord)i.next();
            g.setColor(Color.lightGray);
            g.fillRect(o.getX()*MULT, o.getY()*MULT, MULT, MULT);
            g.setColor(Color.black);
            g.drawRect(o.getX()*MULT, o.getY()*MULT, MULT, MULT);
        }
        g.dispose();
        return img;
    }
    
    private static void drawLine(Graphics g, IHEALCoord c1, IHEALCoord c2)
    {
        @SuppressWarnings("rawtypes")
        HEALCoord o1 = (HEALGlobe<Object>.HEALCoord)c1;
        @SuppressWarnings("rawtypes")
        HEALCoord o2 = (HEALCoord)c2;        
        int x1 = o1.getX()*MULT + MULT/2;
        int y1 = o1.getY()*MULT + MULT/2;
        int x2 = o2.getX()*MULT + MULT/2;
        int y2 = o2.getY()*MULT + MULT/2;
        int x12 = (x1 + x2)/2;
        int y12 = (y1 + y2)/2;
        g.setColor(Color.RED);
        g.drawLine(x1, y1, x12, y12);
        g.setColor(Color.BLUE);
        g.drawLine(x12, y12, x2, y2);
    }
            
    
    private static BufferedImage testDir(IHEALGlobe<?> globe, int dir)
    {
        BufferedImage img = testGrid(globe);
        Graphics g = img.getGraphics();
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord o1 = i.next();
            IHEALCoord o2 = o1.next(dir);
            drawLine(g, o1, o2);
        }
        g.dispose();
        return img;
    }
    
    private static BufferedImage testVec(IHEALGlobe<?> globe, int dir)
    {
        System.out.println("Dir: "+dir);
        BufferedImage img = testGrid(globe);
        Graphics g = img.getGraphics();
        Set<String> done = new HashSet<>();
        List<IHEALCoord> coords = ArrayUtils.toList(globe.coordsIterator());
        Collections.reverse(coords);
        for (int i = 0; i < coords.size()/4; i++)
        {
            IHEALCoord o = coords.get(i);
            if (done.contains(o.toString()))
                continue;
            IHEALVector v = o.makeVector(dir);
            System.out.print(v+":"+v.getDirection());
            for (;;)
            {
                v = v.move();
                System.out.print(" "+v+":"+v.getDirection());
                drawLine(g, o, v);
                done.add(o.toString());
                o = v;
                if (done.contains(o.toString()))
                    break;
            }
            System.out.println();
        }
        g.dispose();
        return img;
    }
    
    private static boolean hashTest(IHEALGlobe<Object> globe)
    {
        Set<String> done = new HashSet<>();
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord o = i.next();
            if (done.contains(o.toString()))
            {
                System.out.println("Iterator hits this coord twice: "+o);
                return false;
            }
            done.add(o.toString());
        }
        for (Iterator<IHEALCoord> i = globe.coordsIterator(); i.hasNext(); )
        {
            IHEALCoord o = i.next();
            if (!done.contains(o.toString()))
            {
                System.out.println("This coord should be contained: "+o);
                for (String c : done)
                    System.out.println("  "+c);
                return false;
            }
        }
        return true;
    }
    
    public static void main(String[] argv) throws IOException
    {
        IHEALGlobe<Object> globe = HEALLogic.newInstance(2, new Object());
        if (!hashTest(globe))
            return;
        ImageIO.write(testGrid(globe), "PNG", new File("c:\\temp\\heal_grid.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_NORTHEAST), "PNG", new File("c:\\temp\\heal_ne.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_NORTHWEST), "PNG", new File("c:\\temp\\heal_nw.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_SOUTHEAST), "PNG", new File("c:\\temp\\heal_se.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_SOUTHWEST), "PNG", new File("c:\\temp\\heal_sw.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_EAST), "PNG", new File("c:\\temp\\heal_e.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_WEST), "PNG", new File("c:\\temp\\heal_w.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_NORTH), "PNG", new File("c:\\temp\\heal_n.png"));
        ImageIO.write(testDir(globe, IHEALCoord.D_SOUTH), "PNG", new File("c:\\temp\\heal_s.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_NORTHEAST), "PNG", new File("c:\\temp\\heal_v_ne.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_NORTHWEST), "PNG", new File("c:\\temp\\heal_v_nw.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_SOUTHEAST), "PNG", new File("c:\\temp\\heal_v_se.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_SOUTHWEST), "PNG", new File("c:\\temp\\heal_v_sw.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_EAST), "PNG", new File("c:\\temp\\heal_v_e.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_WEST), "PNG", new File("c:\\temp\\heal_v_w.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_SOUTH), "PNG", new File("c:\\temp\\heal_v_s.png"));
        ImageIO.write(testVec(globe, IHEALCoord.D_NORTH), "PNG", new File("c:\\temp\\heal_v_n.png"));
    }
    */
}
