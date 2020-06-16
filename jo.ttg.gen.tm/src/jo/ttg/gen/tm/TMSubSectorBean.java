package jo.ttg.gen.tm;

import java.util.Iterator;

import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sub.LinkBean;
import jo.ttg.beans.sub.SubSectorBean;

public class TMSubSectorBean extends SubSectorBean
{
    private TMSectorBean    mSector;
    
    public TMSubSectorBean(TMSectorBean sector)
    {
        mSector = sector;
    }

    @Override
    public MainWorldBean[] getMainWorlds()
    {
        mSector.initializeWorlds();
        return super.getMainWorlds();
    }

    @Override
    public MainWorldBean getMainWorlds(int index)
    {
        mSector.initializeWorlds();
        return super.getMainWorlds(index);
    }

    @Override
    public Iterator<MainWorldBean> getMainWorldsIterator()
    {
        mSector.initializeWorlds();
        return super.getMainWorldsIterator();
    }
    
    void addLink(LinkBean link)
    {
        mLinks.add(link);
    }
}
