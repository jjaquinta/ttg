package jo.ttg.gen;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.XMessageBean;

public interface IGenCargo
{
    public List<CargoBean> generateCargo(MainWorldBean origin, DateBean date);
    public List<CargoBean> generateFreight(MainWorldBean origin, MainWorldBean destination, DateBean date);
    public List<XMessageBean> generateMessages(MainWorldBean origin, DateBean date);
}