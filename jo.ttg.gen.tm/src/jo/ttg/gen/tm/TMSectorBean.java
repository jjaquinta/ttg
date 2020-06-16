package jo.ttg.gen.tm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jo.ttg.beans.HashBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.sub.LinkBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.imp.ImpGenMainWorld;
import jo.ttg.logic.mw.MainWorldLogic;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TMSectorBean extends SectorBean
{
    TMGenScheme mScheme;
    private boolean mInitializeProperties;
    private boolean mInitializeSubsectors;
    private boolean mInitializeWorlds;

    public TMSectorBean(TMGenScheme scheme)
    {
        mScheme = scheme;
        mInitializeProperties = false;
        mInitializeSubsectors = false;
        mInitializeWorlds = false;
    }

    private void initializeProperties()
    {
        if (mInitializeProperties)
            return;
        OrdBean ords = getUpperBound();
        Document credits = TMUtils.getCredits(ords); 
        for (Iterator<Node> i = XMLUtils.findNodes(credits, "Data/*").iterator(); i.hasNext(); )
        {
            Node child = i.next();
            String nodeName = child.getNodeName();
            if (!nodeName.startsWith("Sector"))
                continue;
            super.getProperties().put(nodeName.substring(6), XMLUtils.getText(child));
        }
        mInitializeProperties = true;
    }

    private void initializeSubsectors()
    {
        if (mInitializeSubsectors)
            return;
        try
        {
        String sectorMetadata = TMUtils.getMSEC(getName());
        TMSubSectorBean[] subs = new TMSubSectorBean[16];
        OrdBean ub = getUpperBound();
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
            {
                int o = x*4 + y;
                subs[o] = new TMSubSectorBean(this);
                subs[o].setUpperBound(new OrdBean(ub.getX() + x*8, ub.getY() + y*10, 0));
                subs[o].setLowerBound(new OrdBean(ub.getX() + (x+1)*8, ub.getY() + (y+1)*10, 0));
                subs[o].setOID(mScheme.getXYZSeed(subs[o].getUpperBound(), 0));
            }
        int orig_dx, orig_dy;
        int dest_dx, dest_dy;
        for (StringTokenizer st = new StringTokenizer(sectorMetadata, "\r\n"); st.hasMoreTokens(); )
        {
            String line = st.nextToken().trim();
            int o = line.indexOf(' ');
            if (o < 0)
                continue;
            String key = line.substring(0, o);
            if (key.length() == 1)
            {
                int off = key.charAt(0) - 'a';
                if ((off >= 0) && (off < 16))
                {
                    int x = off%4;
                    int y = off/4;
                    int transOff = x*4 + y;
                    String name = line.substring(o).trim();
                    subs[transOff].setName(name);
                }
            }
            else if (key.equals("route"))
            {
                //System.out.println(line);
                StringTokenizer st2 = new StringTokenizer(line.substring(6), " ");
                LinkBean link = new LinkBean();
                String origin = st2.nextToken();
                if (origin.length() != 4)
                {
                    orig_dx = IntegerUtils.parseInt(origin)*32;
                    orig_dy = IntegerUtils.parseInt(st2.nextToken())*40;
                    origin = st2.nextToken();
                }
                else
                {
                    orig_dx = 0;
                    orig_dy = 0;
                }
                int originX = IntegerUtils.parseInt(origin.substring(0, 2)) - 1 + orig_dx;
                int originY = IntegerUtils.parseInt(origin.substring(2, 4)) - 1 + orig_dy;
                String dest = st2.nextToken();
                if (dest.length() != 4)
                {
                    dest_dx = IntegerUtils.parseInt(dest)*32;
                    dest_dy = IntegerUtils.parseInt(st2.nextToken())*40;
                    dest = st2.nextToken();
                }
                else
                {
                    dest_dx = 0;
                    dest_dy = 0;
                }
                int destX = IntegerUtils.parseInt(dest.substring(0, 2)) - 1 + dest_dx;
                int destY = IntegerUtils.parseInt(dest.substring(2, 4)) - 1 + dest_dy;
                link.setType(0);
                link.setOrigin(new OrdBean(ub.getX() + originX, ub.getY() + originY, 0));
                link.setDestination(new OrdBean(ub.getX() + destX, ub.getY() + destY, 0));
                if (st2.hasMoreTokens())
                    link.setText(st2.nextToken());
                int sub_x = originX/8;
                int sub_y = originY/10;
                // clip for pass-thru routes
                if (sub_x < 0)
                    sub_x = 0;
                else if (sub_x > 3)
                    sub_x = 3;
                if (sub_y < 0)
                    sub_y = 0;
                else if (sub_y > 3)
                    sub_y = 3;
                subs[sub_x*4 + sub_y].addLink(link); 
            }
            else if (key.equals("border"))
            {
                // TODO: not implemented
            }
            else if (key.equals("label"))
            {
                // TODO: not implemented
            }
        }
        super.setSubSectors(subs);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
        }
        mInitializeSubsectors = true;
    }

    void initializeWorlds()
    {
        if (mInitializeWorlds)
            return;
        String sectorData = TMUtils.getSEC(getName());
        if (sectorData == null)
        {
            mInitializeWorlds = true;
            return; // BUG!
        }
        OrdBean ub = getUpperBound();
        List<List<MainWorldBean>> mws = new ArrayList<List<MainWorldBean>>();
        SubSectorBean[] subs = getSubSectors();
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
                mws.add(new ArrayList<MainWorldBean>());
        for (StringTokenizer st = new StringTokenizer(sectorData, "\r\n"); st.hasMoreTokens(); )
        {
            String line = st.nextToken().trim();
            if (line.startsWith("#"))
                continue;
            if (line.length() == 0)
                continue;
            MainWorldBean mw = new MainWorldBean();
            int off = MainWorldLogic.findHex(line.toCharArray());
            if (off < 0)
                continue;
            int x = Integer.parseInt(line.substring(off, off+2)) - 1;
            int y = Integer.parseInt(line.substring(off+2, off+4)) - 1;
            OrdBean o = new OrdBean(ub.getX() + x, ub.getY() + y, 0);
            mw.setOrds(o);
            //System.out.println("<"+line);
            MainWorldLogic.importLine(mw, line);
            //System.out.println(">"+MainWorldLogic.getExportLine(mw));
            mws.get(x/8 + (y/10)*4).add(mw);
            mw.setLocalSeed(mScheme.getXYZSeed(o, 0));
            mw.setOID(mw.getLocalSeed());
            mw.setSubSeed(subs[(x/8)*4 + (y/10)].getOID());
            mw.setSecSeed(getOID());
            if (mw.getStars().length == 0)
            {
                RandBean r = new RandBean();
                r.setSeed(mw.getLocalSeed());
                ImpGenMainWorld.generateStars(mw, r, 0);
            }
        }
        for (int x = 0; x < 4; x++)
            for (int y = 0; y < 4; y++)
            {
                MainWorldBean[] mwList = mws.get(x + y*4).toArray(new MainWorldBean[0]);
                subs[x*4 + y].setMainWorlds(mwList);
            }
        mInitializeWorlds = true;
    }
    
    @Override
    public HashBean getProperties()
    {
        initializeProperties();
        return super.getProperties();
    }

    @Override
    public SubSectorBean[] getSubSectors()
    {
        initializeSubsectors();
        return super.getSubSectors();
    }

    @Override
    public SubSectorBean getSubSectors(int index)
    {
        initializeSubsectors();
        return super.getSubSectors(index);
    }

    @Override
    public Iterator<SubSectorBean> getSubSectorsIterator()
    {
        initializeSubsectors();
        return super.getSubSectorsIterator();
    }

    @Override
    public void setSubSectors(int index, SubSectorBean v)
    {
        initializeSubsectors();
        super.setSubSectors(index, v);
    }

    @Override
    public void setSubSectors(SubSectorBean[] v)
    {
        initializeSubsectors();
        super.setSubSectors(v);
    }
    
}
