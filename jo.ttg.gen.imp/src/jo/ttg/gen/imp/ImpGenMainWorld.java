package jo.ttg.gen.imp;


import jo.ttg.beans.OrdBean;
import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.PopulatedStatsBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.mw.UPPBean;
import jo.ttg.gen.IGenMainWorld;
import jo.ttg.logic.RandLogic;
import jo.ttg.logic.mw.UPPLogic;

public class ImpGenMainWorld implements IGenMainWorld
{
    protected ImpGenScheme   mScheme;

    public ImpGenMainWorld(ImpGenScheme _scheme)
    {
        mScheme = _scheme;
    }

    public MainWorldBean generateMainWorld(OrdBean ords)
    {
        return generateMainWorld(ords, false);
    }

    public MainWorldBean generateMainWorld(OrdBean ords, boolean force)
    {
		if (!mScheme.exists(ords) && !force)
			return null;
        MainWorldBean mw = newMainWorldBean();

        mw.setOrds(ords);
        int density = mScheme.getMaturity(ords);
        long localSeed = mScheme.getXYZSeed(ords, ImpGenScheme.R_LOCAL);
        //long subSeed   = scheme.getXYZSeed(ords, GenScheme.R_SUBSECTOR);
        //long secSeed   = scheme.getXYZSeed(ords, GenScheme.R_SECTOR);
        RandBean r = new RandBean();
        RandLogic.setMagic(r, localSeed, RandBean.EXIST_MAGIC);
        RandLogic.rand(r); // flush one to make SYSGEN compatable
        mw.setOID(r.getSeed());

        generateUpp(mw.getPopulatedStats().getUPP(), r, density);
        generateBases(mw, r, density);
        generateGiants(mw, r, density);
        generateBelts(mw, r, density);
        generateZone(mw, r, density);
        mw.setPopDigit(RandLogic.rand(r) % 10);
        generateName(mw, r, density);
        generateStars(mw, r, density);
        return mw;
    }

    public MainWorldBean newMainWorldBean()
    {
        return new MainWorldBean();
    }

    private void generateUpp(UPPBean upp, RandBean r, int density)
    {
        upp.getPort ().setValue(generatePor(upp, r, density));
        upp.getSize ().setValue(generateSiz(upp, r, density));
        upp.getAtmos().setValue(generateAtm(upp, r, density));
        upp.getHydro().setValue(generateHyd(upp, r, density));
        upp.getPop  ().setValue(generatePop(upp, r, density));
        upp.getGov  ().setValue(generateGov(upp, r, density));
        upp.getLaw  ().setValue(generateLaw(upp, r, density));
        upp.getTech ().setValue(generateTec(upp, r, density));
        UPPLogic.updateTradeCodes(upp);
    }

    static String[] strprt = {
        "AABBCCCDEEX", /* backwater */
        "AAABBCCDEEX", /* standard */
        "AAABBCCDEEE", /* mature */
        "AAAABBCCDEX", /* cluster */
    };

    private int generatePor(UPPBean upp, RandBean r, int density)
    {
        if (density < 1)
            density = 1;
        else if (density > 4)
            density = 4;
        return (int) (strprt[density - 1].charAt(RandLogic.D(r, 2) - 2));
    }

    private int generateSiz(UPPBean upp, RandBean r, int density)
    {
        return RandLogic.D(r, 2) - 2;
    }

    private int generateAtm(UPPBean upp, RandBean r, int density)
    {
        int siz = upp.getSize().getValue();
        if (siz <= 0)
            return 0;
        else
            return RandLogic.D(r, 2) - 7 + siz;
    }

    private int generateHyd(UPPBean upp, RandBean r, int density)
    {
        int siz = upp.getSize().getValue();
        int atm = upp.getAtmos().getValue();
        int val;
        if (siz <= 1)
            val = 0;
        else
        {
            val = RandLogic.D(r, 2) - 7 + siz;
            if ((atm <= 1) || (atm >= 0xa))
                val -= 4;
        }
        return val;
    }

    private int generatePop(UPPBean upp, RandBean r, int density)
    {
        return RandLogic.D(r, 2) - 2;
    }

    private int generateGov(UPPBean upp, RandBean r, int density)
    {
        return RandLogic.D(r, 2) - 7 + upp.getPop().getValue();
    }

    private int generateLaw(UPPBean upp, RandBean r, int density)
    {
        return RandLogic.D(r, 2) - 7 + upp.getGov().getValue();
    }

    private int generateTec(UPPBean upp, RandBean r, int density)
    {
        int val = RandLogic.D(r, 1);
        val += upp.getPort().getTechMod();
        val += upp.getSize().getTechMod();
        val += upp.getAtmos().getTechMod();
        val += upp.getHydro().getTechMod();
        val += upp.getPop().getTechMod();
        val += upp.getGov().getTechMod();
        return val;
    }

    private void generateBases(MainWorldBean mw, RandBean r, int density)
    {
        if (density <= 0)
            return;
        UPPBean upp = mw.getPopulatedStats().getUPP();
        int navalTarget = 0;
        int scoutTarget = 0;
        int localTarget = 0;
        switch (upp.getPort().getValue())
        {
            case 'A':
                navalTarget = 8;
                scoutTarget = 10;
                localTarget = 10;
                break;
            case 'B':
                navalTarget = 8;
                scoutTarget = 9;
                localTarget = 9;
                break;
            case 'C':
                scoutTarget = 8;
                localTarget = 8;
                break;
            case 'D':
                scoutTarget = 7;
                break;
        }
        long bases = 0L;
        if ((navalTarget > 0) && (RandLogic.D(r, 2) >= navalTarget))
            bases |= PopulatedStatsBean.NAVAL_BASE;
        if ((scoutTarget > 0) && (RandLogic.D(r, 2) >= scoutTarget))
            bases |= PopulatedStatsBean.SCOUT_BASE;
        if ((localTarget > 0) && (RandLogic.D(r, 2) >= localTarget))
            bases |= PopulatedStatsBean.LOCAL_BASE;
        mw.getPopulatedStats().setBases(bases);
    }

    private void generateZone(MainWorldBean mw, RandBean r, int density)
    {
        int v;

        UPPBean upp = mw.getPopulatedStats().getUPP();
        v = upp.getLaw().getValue() + upp.getGov().getValue();
        if (v < 30)
            mw.getPopulatedStats().setTravelZone('G');
        else if (v < 33)
            mw.getPopulatedStats().setTravelZone('A');
        else
            mw.getPopulatedStats().setTravelZone('R');
    }

    private String genName(MainWorldBean mw, RandBean r)
    {
        return mScheme.getGeneratorLanguage().generatePlaceName(
            mw.getOrds(), mw.getOrds(),
            mw.getPopulatedStats().getAllegiance(),
            r);
    }

    private void generateName(MainWorldBean mw, RandBean r, int density)
    {
        mw.setName(genName(mw, r));
    }

    private void generateGiants(MainWorldBean mw, RandBean r, int density)
    {
        if ((density >= 0) && (RandLogic.D(r, 2) >= 5))
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                case 3 :
                    mw.setNumGiants(1);
                    break;
                case 4 :
                case 5 :
                    mw.setNumGiants(2);
                    break;
                case 6 :
                case 7 :
                    mw.setNumGiants(3);
                    break;
                case 8 :
                case 9 :
                case 10 :
                    mw.setNumGiants(4);
                    break;
                case 11 :
                case 12 :
                    mw.setNumGiants(5);
                    break;
            }
    }

    private void generateBelts(MainWorldBean mw, RandBean r, int density)
    {
        if ((density >= 0) && (RandLogic.D(r, 2) >= 8))
            switch (RandLogic.D(r, 2))
            {
                case 2 :
                case 3 :
                case 4 :
                case 5 :
                case 6 :
                    mw.setNumBelts(1);
                    break;
                case 7 :
                case 8 :
                case 9 :
                case 10 :
                case 11 :
                    mw.setNumBelts(2);
                    break;
                case 12 :
                    mw.setNumBelts(3);
                    break;
            }
    }

    public static void generateStars(MainWorldBean mw, RandBean r, int density)
    {
        int v = RandLogic.D(r, 2);
        int[] rolls = new int[2];
        StarDeclBean s = new StarDeclBean();
        generateStar(s, r, mw, rolls);
        if (v >= 8)
        {
            s = new StarDeclBean();
            generateStar(s, r, mw, rolls);
            if (v >= 12)
            {
                s = new StarDeclBean();
                generateStar(s, r, mw, rolls);
            }
        }
    }

    private static void generateStar(StarDeclBean s, RandBean r, MainWorldBean mw, int[] rolls)
    {
        StarDeclBean[] others = mw.getStars();
        if (others.length == 0)
            generateStarPrimary(s, r, mw, rolls);
        else
            generateStarSecondary(s, r, mw, rolls);
        mw.addStars(s);
    }

    private static void generateStarPrimary(StarDeclBean s, RandBean r, MainWorldBean mw, int[] rolls)
    {
        int dm;

        UPPBean upp = mw.getPopulatedStats().getUPP();
        if (((upp.getAtmos().getValue() >= 4) && (upp.getAtmos().getValue() <= 9))
            || (upp.getPop().getValue() >= 8))
            dm = 5;
        else
            dm = 0;
        int typeRoll = RandLogic.D(r, 2) + dm;
        if (typeRoll < 2)
            typeRoll = 2;
        else if (typeRoll > 12)
            typeRoll = 12;
        switch (typeRoll)
        {
            case 2 :
                s.setStarType(StarDeclBean.ST_A);
                break;
            case 3 :
            case 4 :
            case 5 :
            case 6 :
            case 7 :
                s.setStarType(StarDeclBean.ST_M);
                break;
            case 8 :
                s.setStarType(StarDeclBean.ST_K);
                break;
            case 9 :
            case 10 :
                s.setStarType(StarDeclBean.ST_G);
                break;
            case 11 :
            case 12 :
                s.setStarType(StarDeclBean.ST_F);
                break;
        }
        int classRoll = RandLogic.D(r, 2) + dm;
        if (classRoll < 2)
            classRoll = 2;
        else if (classRoll > 12)
            classRoll = 12;
        switch (classRoll)
        {
            case 2 :
                s.setStarClass(StarDeclBean.SC_2);
                break;
            case 3 :
                s.setStarClass(StarDeclBean.SC_3);
                break;
            case 4 :
                s.setStarClass(StarDeclBean.SC_4);
                break;
            case 5 :
            case 6 :
            case 7 :
            case 8 :
            case 9 :
            case 10 :
            case 11 :
            case 12 :
                s.setStarClass(StarDeclBean.SC_5);
                break;
        }
        s.setStarType(s.getStarType() + RandLogic.rand(r) % 10);
        rolls[0] = typeRoll;
        rolls[1] = classRoll;
    }

    private static void generateStarSecondary(StarDeclBean s, RandBean r, MainWorldBean mw, int[] rolls)
    {
        int typeRoll;

        typeRoll = RandLogic.D(r, 2) + rolls[0];
        if (typeRoll < 2)
            typeRoll = 2;
        else if (typeRoll > 12)
            typeRoll = 12;
        switch (typeRoll)
        {
            case 2 :
                s.setStarType(StarDeclBean.ST_A);
                break;
            case 3 :
            case 4 :
                s.setStarType(StarDeclBean.ST_F);
                break;
            case 5 :
            case 6 :
                s.setStarType(StarDeclBean.ST_G);
                break;
            case 7 :
            case 8 :
                s.setStarType(StarDeclBean.ST_K);
                break;
            case 9 :
            case 10 :
            case 11 :
            case 12 :
                s.setStarType(StarDeclBean.ST_M);
                break;
        }
        int classRoll = RandLogic.D(r, 2) + rolls[1];
        if (classRoll < 2)
            classRoll = 2;
        else if (classRoll > 12)
            classRoll = 12;
        switch (classRoll)
        {
            case 2 :
                s.setStarClass(StarDeclBean.SC_2);
                break;
            case 3 :
                s.setStarClass(StarDeclBean.SC_3);
                break;
            case 4 :
                s.setStarClass(StarDeclBean.SC_4);
                break;
            case 5 :
            case 6 :
            case 7 :
            case 8 :
            case 9 :
            case 10 :
            case 11 :
                s.setStarClass(StarDeclBean.SC_5);
                break;
            case 12 :
                s.setStarClass(StarDeclBean.SC_D);
                break;
        }
        s.setStarType(s.getStarType() + RandLogic.rand(r) % 10);
    }
}