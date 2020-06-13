package jo.ttg.gen;

import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.XMessageBean;

public interface IGenCargoEx extends IGenCargo
{
    public List<CargoBean> generateCargo(BodyBean origin, DateBean date);
    public List<CargoBean> generateFreight(BodyBean origin, BodyBean destination, DateBean date);
    public List<XMessageBean> generateMessages(BodyBean origin, DateBean date);
}