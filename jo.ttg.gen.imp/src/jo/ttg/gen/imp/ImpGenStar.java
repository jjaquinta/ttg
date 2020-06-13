package jo.ttg.gen.imp;


import java.util.Iterator;

import jo.ttg.beans.RandBean;
import jo.ttg.beans.mw.MainWorldBean;
import jo.ttg.beans.mw.StarDeclBean;
import jo.ttg.beans.sys.BodyBean;
import jo.ttg.beans.sys.BodyGiantBean;
import jo.ttg.beans.sys.BodyStarBean;
import jo.ttg.logic.RandLogic;

public class ImpGenStar extends ImpGenSystemBase
{
    public ImpGenStar(ImpGenScheme _scheme, ImpGenSystem root)
    {
        super(_scheme, root);
    }

    public BodyStarBean generateStar(BodyBean primary, double orbit, MainWorldBean mw, RandBean r)
    {
        BodyStarBean star = new BodyStarBean();
		generateName(star, mw, r);
        star.setOrbit(orbit);
        if (primary != null)
            primary.addSatelites(star);
        else
            primary = star;

        int starCount = countStars(primary);
        star.setStarDecl(mw.getStars(starCount - 1));
        generateSatelites(star, starCount, mw, r);
        return star;
    }

    private int countStars(BodyBean primary)
    {
        if (primary == null)
        {
            return 0;
        }
        int ret = 0;
        for (Iterator<BodyBean> i = primary.getRoot().getAllSatelitesIterator(); i.hasNext(); )
        {
            BodyBean b = i.next();
            if (b instanceof BodyStarBean)
                ret++;
        }
        return ret;
    }

    private void generateSatelites(BodyStarBean star, int starCount, MainWorldBean mw, RandBean r)
    {
        int numEmpty;
        int numGas;
        int numToid;
        int numCapture;
        int[] orbits;

        orbits = new int[24];
        generateOrbitAvailabilty(star, orbits, starCount, r);
        if (starCount == 1)
            generateCompanions(star, orbits, mw, r);
        // set contents
        numGas = generateNumGas(starCount, mw, r);
        numToid = generateNumToid(starCount, mw, r);
        numEmpty = generateNumEmpty(r);
        generateEmpty(numEmpty, orbits, r);
        numCapture = generateNumCapture(r);
        generateCapture(star, numCapture, mw, r);
        generateGasGiants(star, numGas, orbits, mw, r);
        generateToids(star, numToid, orbits, mw, r);
        if (starCount == 1)
            generateMainWorld(star, orbits, mw, r);
        generateWorlds(star, orbits, mw, r);
    }

    private void generateOrbitAvailabilty(BodyStarBean star, int[] orbits, int starCount, RandBean r)
    {
        // calculate maximum orbits
        if ((starCount == 1) || (star.getOrbit() > 14.0))
        { // normal generation
            int maxOrb = generateMaxOrbits(star, r);
            for (int i = 0; i < orbits.length; i++)
                if (i > maxOrb)
                    orbits[i] = BodyBean.BT_EMPTY;
                else if (i < star.getUnavailable())
                    orbits[i] = BodyBean.BT_EMPTY; // inside star
                else
                    orbits[i] = BodyBean.BT_UNSET;
        }
        else
        {
            int i = 0;
            if (star.getOrbit() < 1)
                return;
            else if (star.getOrbit() < 2)
                i = 1;
            else if (star.getOrbit() < 4)
                i = 2;
            else if (star.getOrbit() < 6)
                i = 3;
            else if (star.getOrbit() < 8)
                i = 4;
            else if (star.getOrbit() < 10)
                i = 5;
            else if (star.getOrbit() < 12)
                i = 6;
            else
                i = 7;
            while (i < orbits.length)
                orbits[i++] = BodyBean.BT_EMPTY;
        }
    }

    private void generateCompanions(BodyStarBean star, int[] orbits, MainWorldBean mw, RandBean r)
    {
        int i;
        double o;

        StarDeclBean[] stars = mw.getStars();
        for (i = 1; i < stars.length; i++)
        {
            o = generateStellarOrbit(star, i, r);
            getGenSys().getGenStar().generateStar(star, o, mw, r);
            generateEmptyOrbits(orbits, o);
        }
    }

    private double generateStellarOrbit(BodyStarBean star, int starCount, RandBean r)
    {
        int roll;

        roll = RandLogic.D(r, 2);
        if (starCount == 3)
            roll += 4;
        else if ((starCount > 1) && (star.getOrbit() > 14))
            roll -= 4;
        if (roll >= 12)
            return BodyBean.convRadiusToOrbit(RandLogic.D(r, 1)*1000.0);
        switch (roll)
        {
            case 4:
                return 1.0;
            case 5:
                return 2.0;
            case 6:
                return 3.0;
            case 7:
                return 4.0 + RandLogic.D(r, 1);
            case 8:
                return 5.0 + RandLogic.D(r, 1);
            case 9:
                return 6.0 + RandLogic.D(r, 1);
            case 10:
                return 7.0 + RandLogic.D(r, 1);
            case 11:
                return 8.0 + RandLogic.D(r, 1);
        }
        return 0.0;
    }

    private int generateNumGas(int starCount, MainWorldBean mw, RandBean r)
    {
        if (starCount == 1)
            return mw.getNumGiants();
        // calculate number of gas giants
        if (RandLogic.D(r, 2) < 5)
            return 0;
        switch (RandLogic.D(r, 2))
        {
            case 2: case 3:
                return 1;
            case 4: case 5:
                return 2;
            case 6: case 7:
                return 3;
            case 8: case 9: case 10:
                return 4;
        }
        return 5;
    }

    private int generateNumToid(int starCount, MainWorldBean mw, RandBean r)
    {
        if (starCount == 1)
        {
            if (mw.getPopulatedStats().getUPP().getSize().getValue() == 0)
                return mw.getNumBelts() - 1;
            return mw.getNumBelts();
        }
        // calculate number of planetoid belts
        if (RandLogic.D(r, 2) < 8)
            return 0;
        switch (RandLogic.D(r, 2))
        {
            case 2: case 3: case 4: case 5: case 6:
                return 1;
            case 7: case 8: case 9: case 10: case 11:
                return 2;
        }
        return 3;
    }

    private int generateNumEmpty(RandBean r)
    {
        // calculate number of empty orbits
        if (RandLogic.D(r, 1) < 5)
            return 0;
        switch (RandLogic.D(r, 1))
        {
            case 1: case 2:
                return 1;
            case 4: case 5: case 6:
                return 3;
        }
        return 2;
    }

    private int generateNumCapture(RandBean r)
    {
        // calculate number of captured planets
        if (RandLogic.D(r, 1) < 5)
            return 0;
        switch (RandLogic.D(r, 1))
        {
            case 1: case 2: case 3:
                return 1;
            case 4: case 5:
                return 2;
        }
        return 3;
    }

    private void generateEmpty(int numEmpty, int[] orbits, RandBean r)
    {
        int amnt = unsetWithin(orbits, 0, 10);
        if (numEmpty > amnt)
            numEmpty = amnt;
        while (numEmpty-- > 0)
        {
            do {
                amnt = RandLogic.D(r, 2) - 2;
            } while (orbits[amnt] != BodyBean.BT_UNSET);
            orbits[amnt] = BodyBean.BT_EMPTY;
        }
    }
    private void generateCapture(BodyStarBean star, int numCapture, MainWorldBean mw, RandBean r)
    {
        double o;
        int   i;

        // place captured
        while (numCapture-- >= 0)
        {
            o = (double)(RandLogic.D(r, 2));
            do {
                i = RandLogic.D(r, 2) - 6;
            } while (i == 0);
            o += (double)((double)(i)*.1);
            getGenSys().getGenWorld().generateWorld(star, o, mw, r);
        }
    }
    
    private void generateGasGiants(BodyStarBean star, int numGas, int[] orbits, MainWorldBean mw, RandBean r)
    {
        int i, left, at;

        // ideal placement of gas giants in in orbits from
        // the last Inner one outwards.
        // first find inner orbit
        i = (int)Math.floor(star.getInnerZone());
        if (i < 0)
            i = 0;
        if (i + 11 >= orbits.length)
            i = orbits.length - 12;
        left = unsetWithin(orbits, i, i + 10);
        while ((left-- > 0) && (numGas-- > 0))
        {
            do {
                at = RandLogic.D(r, 2) - 2 + i;
            } while (orbits[at] != BodyBean.BT_UNSET);
            orbits[at] = BodyBean.BT_GIANT;
        }
        // if still more to do then place in inner zone
        left = unsetWithin(orbits, 0, 10);
        while ((left-- > 0) && (numGas-- > 0))
        {
            do {
                at = RandLogic.D(r, 2) - 2;
            } while (orbits[at] != BodyBean.BT_UNSET);
            orbits[at] = BodyBean.BT_GIANT;
        }
        // now place gas giants
        for (i = 0; i < orbits.length; i++)
            if (orbits[i] == BodyBean.BT_GIANT)
                getGenSys().getGenGiant().generateGiant(star, i, mw, r);
    }
    
    private void generateToids(BodyStarBean star, int numToid, int[] orbits, MainWorldBean mw, RandBean r)
    {
        int i, left, at;

        // ideal placement of planetoids is in orbits
        // just inside of gast giants.
        // first count idea orbits
        left = 0;
        for (i = 0; i < orbits.length - 1; i++)
            if ((orbits[i] == BodyBean.BT_UNSET) && (orbits[i+1] == BodyBean.BT_GIANT))
                left++;
        while ((left > 0) && (numToid > 0))
        {
            // calcualte one randomly
            at = RandLogic.rand(r)%left;
            // find orbit
            for (i = 0; i < orbits.length - 1; i++)
                if ((orbits[i] == BodyBean.BT_UNSET) && (orbits[i+1] == BodyBean.BT_GIANT))
                    if (at-- == 0)
                    {
                        orbits[i] = BodyBean.BT_TOIDS;
                        left--;
                        numToid--;
                        break;
                    }
        }
        // if still more to do then place anywhere
        left = unsetWithin(orbits, 0, 10);
        while ((left > 0) && (numToid > 0))
        {
            do {
                at = RandLogic.D(r, 2) - 2;
            } while (orbits[at] != BodyBean.BT_UNSET);
            orbits[at] = BodyBean.BT_TOIDS;
            left--;
            numToid--;
        }
        // now place planetoids
        for (i = 0; i < orbits.length; i++)
            if (orbits[i] == BodyBean.BT_TOIDS)
                getGenSys().getGenToids().generateToids(star, i, mw, r);
    }
    
    private void generateMainWorld(BodyStarBean star, int[] orbits, MainWorldBean mw, RandBean r)
    {
        int i;

        i = (int)((star.getInnerZone() + star.getOuterZone())/2.0 + .5);
        if (i < 0)
            i = 0;
        if (orbits[i] == BodyBean.BT_UNSET)
        {
            orbits[i] = BodyBean.BT_WORLD;
            if (mw.getPopulatedStats().getUPP().getSize().getValue() == 0)
                getGenSys().getGenToids().generateMain(star, i, mw, r);
            else
                getGenSys().getGenWorld().generateMain(star, i, mw, r);
            return;
        }
        if ((orbits[i] == BodyBean.BT_GIANT) && (mw.getPopulatedStats().getUPP().getSize().getValue() != 0))
        {
            BodyBean b = star.findBody((double)i);
            if (b.getType() == BodyBean.BT_GIANT)
            {
                BodyGiantBean g = (BodyGiantBean)b;
                getGenSys().getGenWorld().generateMain(g, generateSateliteRadius(g, r), mw, r);
                return;
            }
        }
        // can't find a place for it. Generate as a captured planet.
        //  double test = (double)DistanceFor(15.0 + 273.0);
        double test = (double)star.calcDistanceFor(288.0, 0.2, 1.0);
        if (mw.getPopulatedStats().getUPP().getSize().getValue() == 0)
            getGenSys().getGenToids().generateMain(star, test, mw, r);
        else
            getGenSys().getGenWorld().generateMain(star, test, mw, r);
    }
    
    private void generateWorlds(BodyStarBean star, int[] orbits, MainWorldBean mw, RandBean r)
    {
        int i;

        for (i = 0; i < orbits.length; i++)
            if (orbits[i] == BodyBean.BT_UNSET)
            {
                orbits[i] = BodyBean.BT_WORLD;
                getGenSys().getGenWorld().generateWorld(star, i, mw, r);
            }
    }

    private int generateMaxOrbits(BodyStarBean star, RandBean r)
    {
        // calculate max orbits
        int roll = RandLogic.D(r, 2);
        int starClass = star.getStarDecl().getStarClass();
        int starType = star.getStarDecl().getStarType();
        if (starClass <= StarDeclBean.SC_2)
            roll += 8;
        else if (starClass == StarDeclBean.SC_3)
            roll += 4;
        else if (starType == StarDeclBean.ST_M)
            roll -= 4;
        else if (starType == StarDeclBean.ST_K)
            roll -= 2;
        if (roll < 1)
            roll = 1;
        return roll;
    }

    public void  generateEmptyOrbits(int orbits[], double o)
    {
        int i, low, high;

        i = (int)o;
        if (i < 0)
            i = 0;
        if (i > 12)
            i = 12;
        high = i + 2;
        switch (i)
        {
            case 0:
                low = -1;
                break;
            case 1:
                low = 0;
                break;
            case 2:
                low = 1;
                break;
            case 3:
                low = 1;
                break;
            case 4:
                low = 2;
                break;
            case 5:
                low = 2;
                break;
            case 6:
                low = 3;
                break;
            case 7:
                low = 3;
                break;
            case 8:
                low = 4;
                break;
            case 9:
                low = 4;
                break;
            case 10:
                low = 5;
                break;
            case 11:
                low = 5;
                break;
            case 12: default:
                low = 6;
                break;
        }
        for (i = low + 1; i < high; i++)
            orbits[i] = BodyBean.BT_EMPTY;
    }
}
