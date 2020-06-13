/*
 * Created on Sep 24, 2005
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.ttg.logic;

import java.util.Random;

import jo.ttg.beans.RandBean;
import jo.util.utils.FormatUtils;

public class RandLogic
{
    static private long dice_rolls = 0;
    private static Random mRND = new Random();

    public static int D(RandBean rnd, int n)
    {
        return roll(rnd, n, 6);
    }
    public static int roll(RandBean rnd, String arg)
    {
        int n, off, dice, mod;

        n = FormatUtils.parseInt(arg);
        off = arg.indexOf('d');
        if (off < 0)
        {
            off = arg.indexOf('D');
            if (off < 0)
                return n;
            arg = arg.substring(off + 1);
            dice = 6;
        }
        else
        {
            arg = arg.substring(off + 1);
            dice = FormatUtils.parseInt(arg);
        }
        mod = 0;
        off = arg.indexOf('+');
        if (off > 0)
            mod = FormatUtils.parseInt(arg.substring(off));
        off = arg.indexOf('-');
        if (off > 0)
            mod = FormatUtils.parseInt(arg.substring(off));
        return roll(rnd, n, dice) + mod;
    }
    public static long getDiceRolls()
    {
        return dice_rolls;
    }
    public static void setMagic(RandBean rnd, long s, long magic)
    {
        setRand(rnd, s + magic);
    }
    public static int rand(RandBean rnd)
    {
        /*
        int l;

        dice_rolls++;
        l = (int)rnd.getSeed();
        rnd.setSeed(l * 1103515245 + 12345);
        return ((int) (((rnd.getSeed()) >> 16) & 0777777));
        */
        dice_rolls++;
        synchronized (mRND)
        {
            mRND.setSeed(rnd.getSeed());
            int ret = mRND.nextInt();
            rnd.setSeed(ret);
            return Math.abs(ret);
        }
    }
    public static double rnd(RandBean rnd)
    {
        return (double) (rand(rnd) % 16384) / 16384.0;
    }
    public static void setRand(RandBean rnd, long Seed)
    {
        rnd.setSeed((int) Seed);
    }
    public static void setRand(RandBean rnd, String Seed)
    {
        char[] ch = Seed.toCharArray();
        rnd.setSeed(0);
        for (int i = 0; i < ch.length; i++)
        {
            int hash = 0;
            switch (i % 4)
            {
                case 0 :
                    hash = ch[i];
                    break;
                case 1 :
                    hash = ch[i] << 8;
                    break;
                case 2 :
                    hash = ch[i] << 16;
                    break;
                case 3 :
                    hash = (((ch[i] & 0xff) << 24) | ((ch[i] & 0xff00) >> 8));
                    break;
            }
            rnd.setSeed(rnd.getSeed() ^ hash);
        }
    }
    public static void setRand(RandBean rnd)
    {
        setRand(rnd, System.currentTimeMillis());
    }

    // DND rolling
    public static int roll(RandBean rnd, int n, int dice, int mod)
    {
        int sum;
        boolean neg;

        neg = (n < 0);
        if (neg)
            sum = -n;
        else
            sum = n;
        while (n-- > 0)
            sum += (rand(rnd) % dice);
        if (neg)
            sum = -sum;
        return sum + mod;
    }
    public static int roll(RandBean rnd, int n, int dice)
    {
        return roll(rnd, n, dice, 0);
    }
    public static int roll(RandBean rnd, int dice)
    {
        return roll(rnd, 1, dice, 0);
    }
    public static int bestOf(RandBean rnd, int n, int of, int d, int mod)
    {
        int i;
        int[] rolls = new int[n];
        for (i = 0; i < rolls.length; i++)
            rolls[i] = roll(rnd, d);
        of -= d;
        while (of-- > 0)
        {
            int r = roll(rnd, d);
            for (i = 0; i < rolls.length; i++)
                if (r > rolls[i])
                {
                    rolls[i] = r;
                    break;
                }
        }
        int ret = mod;
        for (i = 0; i < rolls.length; i++)
            ret += rolls[i];
        return ret;
    }
    public static int bestOf(RandBean rnd, int n, int of, int d)
    {
        return bestOf(rnd, n, of, d, 0);
    }
    public static int bestOf(RandBean rnd, int n, int of)
    {
        return bestOf(rnd, n, of, 6, 0);
    }
    public static int bestOf(RandBean rnd, int of)
    {
        return bestOf(rnd, 3, of, 6, 0);
    }
    public static void randomize(Object[] objs, RandBean r)
    {
        for (int i = 0; i < objs.length; i++)
        {
            int j = rand(r)%objs.length;
            Object o = objs[i];
            objs[i] = objs[j];
            objs[j] = o;
        }
    }
    public static int nextInt(RandBean rnd, int i)
    {
        return rand(rnd)%i;
    }
}
