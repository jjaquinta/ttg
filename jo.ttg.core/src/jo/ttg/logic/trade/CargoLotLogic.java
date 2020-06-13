package jo.ttg.logic.trade;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.CargoLotBean;
import jo.ttg.gen.IGenCargoEx;
import jo.ttg.gen.IGenScheme;
import jo.ttg.logic.DateLogic;
import jo.ttg.logic.gen.CargoLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.util.html.URIBuilder;
import jo.util.utils.obj.BooleanUtils;
import jo.util.utils.obj.DoubleUtils;
import jo.util.utils.obj.IntegerUtils;
import jo.util.utils.obj.LongUtils;
import jo.util.utils.obj.StringUtils;

public class CargoLotLogic
{
    public static URIBean getFromURI(IGenScheme scheme, String uri)
    {
        //System.out.println("CargoLogLogic, get '"+uri+"'");
        if (uri.startsWith("cargolot://"))
            return getCargoLot(scheme, uri);
        else if (uri.startsWith("cargo://"))
            return getCargo(scheme, uri);
        else
            return null;
    }
    public static CargoLotBean getCargoLot(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith("cargolot://"))
            return null;
        URIBuilder u = new URIBuilder(uri);
        String originURI = u.getQuery().getProperty("originURI");
        if (StringUtils.isTrivial(originURI))
            return null;
        Object origin = SchemeLogic.getFromURI(scheme, originURI);
        if (origin == null)
            return null;
        String dateText = u.getQuery().getProperty("date");
        DateBean date;
        if (StringUtils.isTrivial(dateText))
            date = new DateBean();
        else
            date = DateLogic.fromString(dateText);
        CargoLotBean lot = new CargoLotBean();
        lot.setURI(uri);
        lot.setOriginURI(originURI);
        lot.setDate(date);
        if (origin instanceof MainWorldBean)
            lot.setCargos(scheme.getGeneratorCargo().generateCargo((MainWorldBean)origin, date));
        else if ((origin instanceof BodyBean) && (scheme.getGeneratorCargo() instanceof IGenCargoEx))
            lot.setCargos(((IGenCargoEx)scheme.getGeneratorCargo()).generateCargo((BodyBean)origin, date));
        else
            return null;
        URIBuilder cargoURI = new URIBuilder(uri);
        cargoURI.setScheme("cargo");
        for (int i = 0; i < lot.getCargos().size(); i++)
        {
            CargoBean cargo = lot.getCargos().get(i);
            cargoURI.setQuery("index", String.valueOf(i));
            cargo.setURI(cargoURI.toString());
            cargo.setPurchasePrice(CargoLogic.purchasePrice(cargo, scheme));
            cargo.setSalePrice(cargo.getPurchasePrice());
        }
        return lot;
    }
    public static CargoBean getCargo(IGenScheme scheme, String uri)
    {
        if (!uri.startsWith("cargo://"))
            return null;
        URIBuilder cargoURI = new URIBuilder(uri);
        if (cargoURI.getQuery().containsKey("index"))
            return getCargoFromCargolot(scheme, cargoURI);
        else
            return getCargoFromAdHoc(scheme, cargoURI);
    }
    
    private static CargoBean getCargoFromAdHoc(IGenScheme scheme, URIBuilder cargoURI) 
    {
        CargoBean cargo = new CargoBean();
        cargo.setURI(cargoURI.toString());
        cargo.setOrigin(cargoURI.getQuery("Origin"));
        cargo.setDestination(cargoURI.getQuery("Destination"));
        cargo.setName(cargoURI.getQuery("Name"));
        cargo.setPhylum(IntegerUtils.parseInt(cargoURI.getQuery("Phylum")));
        cargo.setType(IntegerUtils.parseInt(cargoURI.getQuery("Type")));
        cargo.setClassification(IntegerUtils.parseInt(cargoURI.getQuery("Classification")));
        cargo.setLegality(IntegerUtils.parseInt(cargoURI.getQuery("Legality")));
        cargo.setQuantity(IntegerUtils.parseInt(cargoURI.getQuery("Quantity")));
        cargo.setTechLevel(IntegerUtils.parseInt(cargoURI.getQuery("TechLevel")));
        cargo.setCor(BooleanUtils.parseBoolean(cargoURI.getQuery("Cor")));
        cargo.setFla(BooleanUtils.parseBoolean(cargoURI.getQuery("Fla")));
        cargo.setExp(BooleanUtils.parseBoolean(cargoURI.getQuery("Exp")));
        cargo.setRad(BooleanUtils.parseBoolean(cargoURI.getQuery("Rad")));
        cargo.setPer(BooleanUtils.parseBoolean(cargoURI.getQuery("Per")));
        cargo.setFra(BooleanUtils.parseBoolean(cargoURI.getQuery("Fra")));
        cargo.setBaseValue(LongUtils.parseLong(cargoURI.getQuery("BaseValue")));
        if (cargoURI.getQuery().containsKey("BestBy"))
            cargo.setBestBy(DateLogic.fromString(cargoURI.getQuery("BestBy")));
        cargo.setPurchasePrice(DoubleUtils.parseDouble(cargoURI.getQuery("PurchasePrice")));
        calcSalePrice(scheme, cargo, cargoURI);
        return cargo;
    }

    public static String getAdHocURI(IGenScheme scheme, CargoBean cargo)
    {
        URIBuilder cargoURI = new URIBuilder("cargo:///");
        if (!StringUtils.isTrivial(cargo.getOrigin()))
            cargoURI.getQuery().put("Origin", cargo.getOrigin());
        if (!StringUtils.isTrivial(cargo.getDestination()))
            cargoURI.getQuery().put("Destination", cargo.getDestination());
        if (!StringUtils.isTrivial(cargo.getName()))
            cargoURI.getQuery().put("Name", cargo.getName());
        if (cargo.getPhylum() != 0)
            cargoURI.getQuery()
                    .put("Phylum", String.valueOf(cargo.getPhylum()));
        if (cargo.getType() != 0)
            cargoURI.getQuery().put("Type", String.valueOf(cargo.getType()));
        if (cargo.getClassification() != 0)
            cargoURI.getQuery().put("Classification",
                    String.valueOf(cargo.getClassification()));
        if (cargo.getLegality() != 0)
            cargoURI.getQuery().put("Legality",
                    String.valueOf(cargo.getLegality()));
        if (cargo.getQuantity() != 0)
            cargoURI.getQuery().put("Quantity",
                    String.valueOf(cargo.getQuantity()));
        if (cargo.getTechLevel() != 0)
            cargoURI.getQuery().put("TechLevel",
                    String.valueOf(cargo.getTechLevel()));

        if (cargo.isCor())
            cargoURI.getQuery().put("Cor", "true");
        if (cargo.isFla())
            cargoURI.getQuery().put("Fla", "true");
        if (cargo.isExp())
            cargoURI.getQuery().put("Exp", "true");
        if (cargo.isRad())
            cargoURI.getQuery().put("Rad", "true");
        if (cargo.isPer())
            cargoURI.getQuery().put("Per", "true");
        if (cargo.isFra())
            cargoURI.getQuery().put("Fra", "true");

        if (cargo.getBaseValue() != 0)
            cargoURI.getQuery().put("BaseValue",
                    String.valueOf(cargo.getBaseValue()));

        if ((cargo.getBestBy() != null)
                && (cargo.getBestBy().getMinutes() != 0))
            cargoURI.getQuery().put("BestBy", cargo.getBestBy().toString());

        if (cargo.getPurchasePrice() >= 0)
            cargoURI.getQuery().put("PurchasePrice",
                    String.valueOf(cargo.getPurchasePrice()));
        calcSalePrice(scheme, cargo, cargoURI);
        cargo.setURI(cargoURI.toString());
        return cargo.getURI();
    }
    private static void calcSalePrice(IGenScheme scheme, CargoBean cargo,
            URIBuilder cargoURI)
    {
        String at = cargoURI.getQuery("at");
        String todayText = cargoURI.getQuery().getProperty("today");
        DateBean today = null;
        if (!StringUtils.isTrivial(todayText))
            today = DateLogic.fromString(todayText);
        if (!StringUtils.isTrivial(at) && (today != null))
            cargo.setSalePrice(CargoLogic.salePrice(cargo, at, today, scheme));
    }
    
    private static CargoBean getCargoFromCargolot(IGenScheme scheme, URIBuilder cargoURI) 
    {
        String sindex = cargoURI.getQuery("index");
        int index = IntegerUtils.parseInt(sindex);
        String originURI = cargoURI.getQuery("originURI");
        String at = cargoURI.getQuery("at");
        String dateText = cargoURI.getQuery().getProperty("date");
        String todayText = cargoURI.getQuery().getProperty("today");
        DateBean today = null;
        if (!StringUtils.isTrivial(todayText))
            today = DateLogic.fromString(todayText);
        URIBuilder cargoLotURI = new URIBuilder();
        cargoLotURI.setScheme("cargolot");
        cargoLotURI.setQuery("originURI", originURI);
        cargoLotURI.setQuery("date", dateText);
        CargoLotBean lot = getCargoLot(scheme, cargoLotURI.toString());
        if (index < lot.getCargos().size())
        {
            CargoBean cargo = lot.getCargos().get(index);
            cargo.setPurchasePrice(CargoLogic.purchasePrice(cargo, scheme));
            if (!StringUtils.isTrivial(at) && (today != null))
                cargo.setSalePrice(CargoLogic.salePrice(cargo, at, today, scheme));
            return cargo;
        }
        else
            return null;
    }
}
