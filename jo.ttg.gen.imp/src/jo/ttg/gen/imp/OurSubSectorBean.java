package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import jo.ttg.beans.OrdBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sub.SubSectorBean;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.logic.OrdLogic;

public class OurSubSectorBean extends SubSectorBean
{
    ImpGenScheme   scheme;

    public OurSubSectorBean(ImpGenScheme _scheme)
    {
        super();
        scheme = _scheme;
    }

    boolean initWorlds = false;

    synchronized protected void doInitWorlds()
    {
        if (initWorlds)
            return;
        IGenMainWorld gworld = scheme.getGeneratorMainWorld();
        List<MainWorldBean> worlds = new ArrayList<MainWorldBean>();
        for (int x = 0; x < 8; x++)
            for (int y = 0; y < 10; y++)
            {
                OrdBean worldOrds = new OrdBean(getUpperBound());
                OrdLogic.incr(worldOrds, x, y, 0);
                if (scheme.exists(worldOrds))
                {
                    MainWorldBean mw = gworld.generateMainWorld(worldOrds);
                    //mw.setParent(this);
                    worlds.add(mw);
                }
            }
        MainWorldBean[] mws = new MainWorldBean[worlds.size()];
        setMainWorlds((MainWorldBean[])worlds.toArray(mws));
        initWorlds = true;
    }

    public MainWorldBean[] getMainWorlds()
    {
        doInitWorlds();
        return super.getMainWorlds();
    }
    public MainWorldBean getMainWorlds(int index)
    {
        doInitWorlds();
        return super.getMainWorlds(index);
    }
    public Iterator<MainWorldBean> getMainWorldsIterator()
    {
        doInitWorlds();
        return super.getMainWorldsIterator();
    }
}