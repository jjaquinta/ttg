package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.beans.trade.CargoBean;
import jo.ttg.beans.trade.GoodBean;
import jo.ttg.beans.trade.XMessageBean;
import jo.ttg.gen.IGenCargo;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.gen.SchemeLogic;
import jo.ttg.logic.sub.SubSectorLogic;

public class ImpGenCargo implements IGenCargo
{
    protected ImpGenScheme   mScheme;

    public ImpGenCargo(ImpGenScheme _scheme)
    {
        mScheme = _scheme;
    }

    public List<CargoBean> generateCargo(MainWorldBean origin, DateBean date)
    {
        int[] majorMinorIncidental = new int[3];
        RandBean r = generateCargoQuan(majorMinorIncidental, CargoBean.CC_CARGO, origin, null, date);
        List<CargoBean> cargoList = new ArrayList<CargoBean>();
        generateCargo(majorMinorIncidental, cargoList, origin, r, date);
        return cargoList;
    }

    public List<CargoBean> generateFreight(MainWorldBean origin, MainWorldBean destination, DateBean date)
    {
        List<CargoBean> cargoList = new ArrayList<CargoBean>();
		if (origin.getOrds().equals(destination.getOrds()))
			return cargoList;
        int[] majorMinorIncidental = new int[3];
        RandBean r = generateCargoQuan(majorMinorIncidental, CargoBean.CC_FREIGHT, origin, destination, date);
        generateCargo(majorMinorIncidental, cargoList, origin, r, date);
        for (CargoBean c : cargoList)
        {
        	c.setDestination(destination.getURI());
            c.setClassification(CargoBean.CC_FREIGHT);
        }
        return cargoList;
    }

    private int[][] cargoQuanTable =
    {  // majD majDM  minD minDM  incD incDM
        {    0,    0,    0,    0,    0,    0 }, // pop 0
        {    1,   -4,    1,   -4,    0,    0 }, // pop 1
        {    1,   -2,    1,   -1,    0,    0 }, // pop 2
        {    1,   -1,    1,    0,    0,    0 }, // pop 3
        {    1,    0,    1,   +1,    0,    0 }, // pop 4
        {    1,   +1,    1,   +2,    0,    0 }, // pop 5
        {    1,   +2,    1,   +3,    1,   -3 }, // pop 6
        {    1,   +3,    1,   +4,    1,   -3 }, // pop 7
        {    1,   +4,    1,   +5,    1,   -2 }, // pop 8
        {    1,   +5,    1,   +6,    1,   -2 }, // pop 9
        {    1,   +6,    1,   +7,    1,    0 }, // pop A
    };

    private RandBean generateCargoQuan(
        int majorMinorIncidental[],
        int type,
        MainWorldBean origin,
        MainWorldBean destination,
        DateBean day)
    {
        long date = day.getYear()*365 + day.getDay();
        RandBean r = new RandBean();

        int dm = setupDM(type, r, origin, destination, date);
        majorMinorIncidental[0] = dm;
        majorMinorIncidental[1] = dm;
        majorMinorIncidental[2] = dm;
        setupQuantities(majorMinorIncidental, r, origin.getPopulatedStats().getUPP().getPop().getValue());
        majorMinorIncidental[0] = (majorMinorIncidental[0] + 2)/4;
        majorMinorIncidental[1] = (majorMinorIncidental[1] + 2)/4;
        majorMinorIncidental[2] = (majorMinorIncidental[2] + 2)/4;
        return r;
    }

    protected void setupQuantities(int[] majorMinorIncidental, RandBean r, int pop)
    {
        if (pop > 10)
            pop = 10;
        if (pop <= 0)
        {
            majorMinorIncidental[0] = 0;
            majorMinorIncidental[1] = 0;
            majorMinorIncidental[2] = 0;
        }
        else
        {
            majorMinorIncidental[0] += RandLogic.D(r, cargoQuanTable[pop][0]) + cargoQuanTable[pop][1];
            majorMinorIncidental[1] += RandLogic.D(r, cargoQuanTable[pop][0]) + cargoQuanTable[pop][1];
            majorMinorIncidental[2] += RandLogic.D(r, cargoQuanTable[pop][0]) + cargoQuanTable[pop][1];
        }
        if (majorMinorIncidental[0] < 0)
            majorMinorIncidental[0] = 0;
        if (majorMinorIncidental[1] < 0)
            majorMinorIncidental[1] = 0;
        if (majorMinorIncidental[2] < 0)
            majorMinorIncidental[2] = 0;
    }

    private int setupDM(int type, RandBean r, MainWorldBean origin, MainWorldBean destination, long date)
    {
        int dm = 0;
        if (type == CargoBean.CC_FREIGHT)
        {
            RandLogic.setMagic(r, 
                (mScheme.getXYZSeed(origin) + mScheme.getXYZSeed(destination)) * date,
                (RandBean.PAS_MAGIC ^ (date + (long) type)));
            if (destination.getPopulatedStats().getUPP().getPop().getValue() < 5)
                dm -= 3;
            else if (destination.getPopulatedStats().getUPP().getPop().getValue() > 7)
                dm++;
            dm += origin.getPopulatedStats().getUPP().getTech().getValue() - destination.getPopulatedStats().getUPP().getTech().getValue();
        }
        else
            RandLogic.setMagic(r, 
                mScheme.getXYZSeed(origin) * date,
                (RandBean.PAS_MAGIC ^ (date + (long) type)));
        if (dm > 8)
            dm = 8;
        else if (dm < -8)
            dm = -8;
        return dm;
    }

    private void generateCargo(
        int majorMinorIncidental[],
        List<CargoBean> cargoList,
        MainWorldBean origin,
        RandBean r,
        DateBean date)
    {
    	generateCargo(majorMinorIncidental[0], 2, cargoList, origin, r, date);
		generateCargo(majorMinorIncidental[1], 1, cargoList, origin, r, date);
		generateCargo(majorMinorIncidental[2], 0, cargoList, origin, r, date);
    }

	private void generateCargo(
		int quan,
		int size,
		List<CargoBean> cargoList,
		MainWorldBean origin,
		RandBean r,
		DateBean date)
	{
		while (quan-- > 0)
		{
			CargoBean c = generateCargo(r, origin, size, date);
			if (c != null)
				cargoList.add(c);
		}
	}

    private CargoBean generateCargo(RandBean r, MainWorldBean origin, int size, DateBean date)
    {
        GoodBean g;
        CargoBean ret;

        g = getGood(r, origin);
        if (g == null)
            return null;
        ret = newCargoBean();
        ret.setOID(r.getSeed());
        ret.setOrigin(origin.getURI());
        ret.setQuantity(RandLogic.D(r, 1) + size * 5);
        setByGood(ret, g, r);
        if (ret.isPer())
        {
            DateBean d = ret.getBestBy();
            d.set(date);
            d.setDay(d.getDay() + RandLogic.D(r, 6));
        }
        return ret;
    }
    
    protected CargoBean newCargoBean()
    {
        return new CargoBean();
    }
    
    private boolean testFor(RandBean r, int chance)
    {
    	if (chance == 0)
    		return false;
    	else
    		return RandLogic.D(r, 2) >= chance;
    }

    protected void setByGood(CargoBean cargo, GoodBean g, RandBean r)
    {
        cargo.setCor(testFor(r, g.getChanceCor()));
        cargo.setFla(testFor(r, g.getChanceFla()));
        cargo.setFra(testFor(r, g.getChanceFra()));
        cargo.setRad(testFor(r, g.getChanceRad()));
        cargo.setExp(testFor(r, g.getChanceExp()));
        cargo.setPer(testFor(r, g.getChancePer()));
        cargo.setLegality(g.getLegality());
        cargo.setBaseValue(g.getBaseValue());
        cargo.setName(g.getName());
        cargo.setPhylum(g.getPhylum());
        cargo.setType(g.getType());
        cargo.setTechLevel(g.getTechLevel());
    }

    private GoodBean getGood(RandBean r, MainWorldBean origin)
    {
        int[] phylumType = new int[2];
        phylumType[0] = CargoBean.CP_DONTCARE;
        phylumType[1] = CargoBean.GT_NATURAL;

        getPhylumType(RandLogic.D(r, 2), origin.getPopulatedStats().getUPP(), phylumType);
		int tl = origin.getPopulatedStats().getUPP().getTech().getValue();
		int law = origin.getPopulatedStats().getUPP().getLaw().getValue();
        GoodList goods = ImpGenScheme.getGoods(phylumType[0], phylumType[1], tl, law);
		return ImpGenScheme.findGood(goods, RandLogic.rand(r));
    }
    
    public void getPhylumType(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.isAg()) // 8a
            getPhylumType8a(roll, upp, phylumType);
        else if (upp.isWa() || upp.isRi()) // 8b
            getPhylumType8b(roll, upp, phylumType);
        else if (upp.isAs() || upp.isVa() || upp.isDe() || upp.isNa()) // 8c
            getPhylumType8c(roll, upp, phylumType);
        else if (upp.isNi()) // 8d
            getPhylumType8d(roll, upp, phylumType);
        else if (upp.isIn()) // 8e
            getPhylumType8e(roll, upp, phylumType);
        else // 8f
            getPhylumType8f(roll, upp, phylumType);
    }

    private void getPhylumType8a(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (roll <= 3)
            phylumType[1] = CargoBean.GT_NATURAL;
        else if (roll <= 6)
        {
            phylumType[1] = CargoBean.GT_NATURAL;
            phylumType[0] = CargoBean.CP_ORGANIC;
        }
        else if (roll <= 8)
        {
            phylumType[1] = CargoBean.GT_PROCESSED;
            phylumType[0] = CargoBean.CP_ORGANIC;
        }
        else if (roll == 9)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

	private void getPhylumType8b(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (upp.getPop().getValue() >= 9)
            roll++;
        if (roll <= 5)
            phylumType[1] = CargoBean.GT_NATURAL;
        else if (roll <= 7)
            phylumType[1] = CargoBean.GT_PROCESSED;
        else if (roll == 8)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

	private void getPhylumType8c(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (upp.getPop().getValue() >= 9)
            roll++;
        if (upp.isBa())
            roll -= 5;
        if (roll <= 6)
        {
            phylumType[1] = CargoBean.GT_NATURAL;
            phylumType[0] = CargoBean.CP_INORGANIC;
        }
        else if (roll == 7)
        {
            phylumType[1] = CargoBean.GT_PROCESSED;
            phylumType[0] = CargoBean.CP_INORGANIC;
        }
        else if (roll <= 9)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

	private void getPhylumType8d(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (roll <= 6)
            phylumType[1] = CargoBean.GT_NATURAL;
        else if (roll == 7)
            phylumType[1] = CargoBean.GT_PROCESSED;
        else if (roll <= 9)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

	private void getPhylumType8e(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (roll <= 3)
            phylumType[1] = CargoBean.GT_NATURAL;
        else if (roll <= 5)
            phylumType[1] = CargoBean.GT_PROCESSED;
        else if (roll <= 9)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

	private void getPhylumType8f(int roll, UPPBean upp, int[] phylumType)
    {
        if (upp.getGov().getValue() >= 9)
            roll++;
        if (upp.getLaw().getValue() >= 9)
            roll++;
        if (upp.getPop().getValue() >= 9)
            roll++;
        if (upp.isBa())
            roll -= 7;
        if (roll <= 4)
            phylumType[1] = CargoBean.GT_NATURAL;
        else if (roll == 5)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else if (roll <= 8)
            phylumType[1] = CargoBean.GT_PROCESSED;
        else if (roll == 9)
            phylumType[1] = CargoBean.GT_MANUFACTURED;
        else if (roll <= 11)
            phylumType[1] = CargoBean.GT_INFORMATION;
        else
            phylumType[1] = CargoBean.GT_NOVELTIES;
    }

    @Override
    public List<XMessageBean> generateMessages(MainWorldBean origin, DateBean date)
    {
        long days = date.getDays();
        RandBean r = new RandBean();
        RandLogic.setMagic(r, mScheme.getXYZSeed(origin) * days, (RandBean.PAS_MAGIC ^ days));
        List<XMessageBean> messages = new ArrayList<XMessageBean>();
        int mult = 1 + getXBoatRoutes(origin.getOrds());
        Map<MainWorldBean, Integer> targets = new HashMap<MainWorldBean, Integer>();
        int totalWeight = 0;
        for (MainWorldBean destination : SchemeLogic.getWorldsWithin(mScheme, origin.getOrds(), 4))
        {
            if (destination.getOrds().equals(origin.getOrds()))
                continue;
            int weight = 1;
            if (destination.getPopulatedStats().isScoutBase())
                weight *= 3;
            if (destination.getPopulatedStats().isNavalBase())
                weight *= 2;
            weight *= (1 + getXBoatRoutes(destination.getOrds()));
            targets.put(destination, weight);
            totalWeight += weight;
        }
        if (targets.size() > 0)
        {
            if (origin.getPopulatedStats().isScoutBase())
                addMessages(messages, RandLogic.D(r, 2)*mult, origin, targets, totalWeight, date, XMessageBean.XT_SCOUT, r);
            if (origin.getPopulatedStats().isNavalBase())
                addMessages(messages, RandLogic.D(r, 1)*mult, origin, targets, totalWeight, date, XMessageBean.XT_NAVY, r);
            addMessages(messages, RandLogic.D(r, 1)*mult/2, origin, targets, totalWeight, date, XMessageBean.XT_CIVIL, r);
        }
        Collections.sort(messages, new Comparator<XMessageBean>() {
            @Override
            public int compare(XMessageBean object1, XMessageBean object2)
            {
                return object1.getDestination().compareTo(object2.getDestination());
            }
        });
        return messages;
    }

    protected int getXBoatRoutes(OrdBean ords)
    {
        return SubSectorLogic.getLinks(mScheme, ords).size();        
    }

    private void addMessages(List<XMessageBean> messages, int num,
            MainWorldBean origin, Map<MainWorldBean, Integer> targets, int totalWeight,
            DateBean date, int type, RandBean r)
    {
        while (num-- > 0)
            messages.add(createMessage(origin, targets, totalWeight, date, type, r));
    }

    private XMessageBean createMessage(MainWorldBean origin,
            Map<MainWorldBean, Integer> targets, int totalWeight, DateBean date, int type,
            RandBean r)
    {
        MainWorldBean target = findTarget(targets, totalWeight, r);
        int value = RandLogic.D(r, 6)*25;
        boolean encrypted = false;
        if (type == XMessageBean.XT_SCOUT)
        {
            value *= 2;
            if (target.getPopulatedStats().isScoutBase())
                value *= 2;
            if (RandLogic.D(r, 1) < 3)
                encrypted = true;
        }
        else if (type == XMessageBean.XT_NAVY)
        {
            value *= 3;
            if (target.getPopulatedStats().isNavalBase())
                value *= 3;
            if (RandLogic.D(r, 1) < 5)
                encrypted = true;
        }
        else
        {
            if (RandLogic.D(r, 1) < 2)
                encrypted = true;
        }
        XMessageBean msg = new XMessageBean();
        msg.setOrigin(origin.getURI());
        msg.setDestination(target.getURI());
        msg.setType(type);
        msg.setValue(value);
        msg.setEncrypted(encrypted);
        if (RandLogic.D(r, 1) < 3)
        {
            int days = 6 + RandLogic.D(r, 2); 
            msg.getExpires().setSeconds(date.getSeconds() + days*24*60*60);
        }
        msg.setName(MESSAGE_NAMES[RandLogic.nextInt(r, MESSAGE_NAMES.length)]);
        return msg;
    }

    private MainWorldBean findTarget(Map<MainWorldBean, Integer> targets,
            int totalWeight, RandBean r)
    {
        int weight = RandLogic.nextInt(r, totalWeight);
        for (MainWorldBean mw : targets.keySet())
        {
            int w = targets.get(mw);
            weight -= w;
            if (weight < 0)
                return mw;
        }
        throw new IllegalStateException("Rolled off end of table?!?");
    }

    protected static final String MESSAGE_NAMES[] =
    {
        "Writings",
        "2-D Still Pictures",
        "3-D Still Pictures",
        "Artistic Images",
        "Audio Recordings",
        "2-D Video Recordings",
        "3-D Video Recordings",
        "Raw Data",
        "Records",
        "Correspondence",
        "News",
    };

}