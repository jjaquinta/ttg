package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.trade.PassengersBean;
import jo.ttg.gen.IGenPassengers;
import jo.ttg.logic.RandLogic;

public class ImpGenPassengers implements IGenPassengers
{
    ImpGenScheme   scheme;

    public ImpGenPassengers(ImpGenScheme _scheme)
    {
        scheme = _scheme;
    }

    public List<PassengersBean> generatePassengers(MainWorldBean origin, MainWorldBean destination, DateBean date)
    {
        if (origin.equals(destination))
        {
            return new ArrayList<PassengersBean>();
        }
        UPPBean originUPP = origin.getPopulatedStats().getUPP();
        UPPBean destinationUPP = destination.getPopulatedStats().getUPP();
        List<PassengersBean> ret = new ArrayList<PassengersBean>();
        for (int d = -6; d <= 0; d++)
        {
            DateBean now = new DateBean();
            now.setMinutes((int)date.getMinutes() + d*24*60);
            RandBean r = new RandBean();
            RandLogic.setMagic(r, 
                scheme.getXYZSeed(origin) + scheme.getXYZSeed(destination),
                (RandBean.PAS_MAGIC ^ now.getYear()*365+now.getDay()));
            PassengersBean pass = generatePassengers(r, originUPP.getPop().getValue(), originUPP.getTech().getValue(),
                    destinationUPP.getPop().getValue(), destinationUPP.getTech().getValue(), now);
            pass.setOrigin(origin.getURI());
            pass.setDestination(destination.getURI());
            ret.add(pass);
        }
        return ret;
    }

    protected PassengersBean generatePassengers(RandBean r, int oPop, int oTech, int dPop, int dTech, DateBean date)
    {
        int dm;
        int h, m, l;

        PassengersBean pass = generatePassengersBean();
        pass.setSeed(r.getSeed());
        dm = (oTech - dTech);
        if (dPop <= 4)
            dm -= 3;
        else if (dPop >= 8)
            dm += 1;
        h = dm;
        m = dm;
        l = dm;
        /*
        TODO: Add in PC mods
        h += steward();
        m += admin();
        l += streetwise();
        */
        switch (oPop)
        {
            case 0 :
                break;
            case 1 :
                m += RandLogic.D(r, 1) - 2;
                l += RandLogic.D(r, 2) - 6;
                break;
            case 2 :
                h += RandLogic.D(r, 1) - RandLogic.D(r, 1);
                m += RandLogic.D(r, 1);
                l += RandLogic.D(r, 2);
                break;
            case 3 :
                h += RandLogic.D(r, 2) - RandLogic.D(r, 2);
                m += RandLogic.D(r, 2) - RandLogic.D(r, 1);
                l += RandLogic.D(r, 2);
                break;
            case 4 :
                h += RandLogic.D(r, 2) - RandLogic.D(r, 1);
                m += RandLogic.D(r, 2) - RandLogic.D(r, 1);
                l += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                break;
            case 5 :
                h += RandLogic.D(r, 2) - RandLogic.D(r, 1);
                m += RandLogic.D(r, 3) - RandLogic.D(r, 2);
                l += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                break;
            case 6 :
                h += RandLogic.D(r, 3) - RandLogic.D(r, 2);
                m += RandLogic.D(r, 3) - RandLogic.D(r, 2);
                l += RandLogic.D(r, 3);
                break;
            case 7 :
                h += RandLogic.D(r, 3) - RandLogic.D(r, 2);
                m += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                l += RandLogic.D(r, 3);
                break;
            case 8 :
                h += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                m += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                l += RandLogic.D(r, 4);
                break;
            case 9 :
                h += RandLogic.D(r, 3) - RandLogic.D(r, 1);
                m += RandLogic.D(r, 3);
                l += RandLogic.D(r, 5);
                break;
            default :
                h += RandLogic.D(r, 3);
                m += RandLogic.D(r, 4);
                l += RandLogic.D(r, 6);
                break;
        }
        // convert weekly roll to daily roll
        for (int i = h; i > 0; i--)
            if (RandLogic.nextInt(r, 7) > 0)
                h--;
        for (int i = m; i > 0; i--)
            if (RandLogic.nextInt(r, 7) > 0)
                m--;
        for (int i = l; i > 0; i--)
            if (RandLogic.nextInt(r, 7) > 0)
                l--;
        pass.setAvailableFirst(new DateBean(date));
        pass.setAvailableLast(new DateBean(date.getMinutes() + 24*60));
        pass.setHigh(h);
        pass.setMiddle(m);
        pass.setLow(l);
        return pass;
    }
    
    protected PassengersBean generatePassengersBean()
    {
        return new PassengersBean();
    }
}