package jo.ttg.beans;

import jo.util.beans.Bean;

public class RandBean extends Bean
{
    public static final long LANG_MAGIC = 1L;
    public static final long EXIST_MAGIC = 2L;
    public static final long DETAIL_MAGIC = 8L;
    public static final long MAP_MAGIC = 16L;
    public static final long ANI_MAGIC = 32L;
    public static final long PAS_MAGIC = 64L;
    public static final long PASEXP_MAGIC = 128L;
    public static final long SECNAME_MAGIC = 256L;
    public static final long SUBNAME_MAGIC = 512L;
    public static final long ALL_MAGIC = 1024L;
    public static final long ECON_MAGIC = 2048L;
    public static final long PERTURB_MAGIC = 4096L;
	public static final long SHIP_MAGIC = 8192L;
	public static final long SURFACE_MAGIC = 16384L;

    // Seed
    private long mSeed;
    public long getSeed()
    {
        return mSeed;
    }
    public void setSeed(long v)
    {
        mSeed = v;
    }


    // constructor
    public RandBean()
    {
        mSeed = 1;
    }
    public RandBean(long seed)
    {
        mSeed = seed;
    }
}
