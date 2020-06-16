package jo.ttg.gen.tm;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.sec.SectorBean;
import jo.ttg.beans.uni.UniverseBean;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.xml.XMLUtils;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class TMUniverseBean extends UniverseBean
{
    private TMGenScheme mScheme;
    private int mXLow;
    private int mXHigh;
    private int mYLow;
    private int mYHigh;
    private boolean mInitialized;

    public TMUniverseBean(TMGenScheme scheme, int xLow, int yLow, int xHigh, int yHigh)
    {
        mScheme = scheme;
        mXLow = xLow;
        mXHigh = xHigh;
        mYLow = yLow;
        mYHigh = yHigh;
        mInitialized = false;
    }
    
    private void initiailze()
    {
        if (mInitialized)
            return;
        List<TMSectorBean> sectors = new ArrayList<TMSectorBean>();
        Document uni = TMUtils.getUniverse();
        for (Iterator<Node> i = XMLUtils.findNodes(uni, "Universe/Sector").iterator(); i.hasNext(); )
        {
            Node sector = i.next();
            String name = XMLUtils.getText(XMLUtils.findFirstNode(sector, "Name"));
            int x = IntegerUtils.parseInt(XMLUtils.getText(XMLUtils.findFirstNode(sector, "X")));
            int y = IntegerUtils.parseInt(XMLUtils.getText(XMLUtils.findFirstNode(sector, "Y")));
            // adjust
            x += 4;
            y += 1;
            if ((x < mXLow) || (x >= mXHigh) || (y < mYLow) || (y >= mYHigh))
                continue;
            TMSectorBean sec = new TMSectorBean(mScheme);
            sec.setUpperBound(new OrdBean(x*32, y*40, 0));
            sec.setLowerBound(new OrdBean((x+1)*32, (y+1)*40, 0));
            sec.setName(name);
            sec.setOID(mScheme.getXYZSeed(sec.getUpperBound(), 0));
            sectors.add(sec);
        }
        SectorBean[] secList = new SectorBean[sectors.size()];
        sectors.toArray(secList);
        super.setSectors(secList);
        mInitialized = true;
    }

    @Override
    public SectorBean[] getSectors()
    {
        initiailze();
        return super.getSectors();
    }

    @Override
    public SectorBean getSectors(int index)
    {
        initiailze();
        return super.getSectors(index);
    }

    @Override
    public Iterator<SectorBean> getSectorsIterator()
    {
        initiailze();
        return super.getSectorsIterator();
    }

    @Override
    public void setSectors(int index, SectorBean v)
    {
        initiailze();
        super.setSectors(index, v);
    }

    @Override
    public void setSectors(SectorBean[] v)
    {
        initiailze();
        super.setSectors(v);
    }
    
    
}
