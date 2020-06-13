package jo.ttg.gen.imp;

import jo.ttg.beans.LanguageBean;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SurfaceParameters
{
	private int	mResolution;
	private int	mTectonicPlates;
	private int	mWaterCoverage;
	private boolean	mLife;
	private int	mVolcanos;
	private double	mFrostLine;
	private double	mPermafrostLine;
	private double	mTemperate; // 20 degrees C
	private double	mTropical; // 40 degrees C
	
	private int mMajor;
	private int mMinor;
	private int mIslands;
	private int mArchepeligos;
	
	private LanguageBean mLanguage;
	
	public SurfaceParameters()
	{
		mResolution = 4;
		mTectonicPlates = 4;
		mWaterCoverage = 70;
		mLife = true;
		mVolcanos = 8;
		mFrostLine = 70;
		mPermafrostLine = 80;
		mTemperate = 60;
		mTropical = 30;
		mMajor = 4;
		mMinor = 3;
		mIslands = 4;
		mArchepeligos = 3;
	}
	
	/**
	 * @return
	 */
	public int getResolution()
	{
		return mResolution;
	}

	/**
	 * @return
	 */
	public int getTectonicPlates()
	{
		return mTectonicPlates;
	}

	/**
	 * @return
	 */
	public int getWaterCoverage()
	{
		return mWaterCoverage;
	}

	/**
	 * @param i
	 */
	public void setResolution(int i)
	{
		mResolution = i;
	}

	/**
	 * @param i
	 */
	public void setTectonicPlates(int i)
	{
		mTectonicPlates = i;
	}

	/**
	 * @param i
	 */
	public void setWaterCoverage(int i)
	{
		mWaterCoverage = i;
	}

	/**
	 * @return
	 */
	public int getArchepeligos()
	{
		return mArchepeligos;
	}

	/**
	 * @return
	 */
	public int getIslands()
	{
		return mIslands;
	}

	/**
	 * @return
	 */
	public int getMajor()
	{
		return mMajor;
	}

	/**
	 * @return
	 */
	public int getMinor()
	{
		return mMinor;
	}

	/**
	 * @param i
	 */
	public void setArchepeligos(int i)
	{
		mArchepeligos = i;
	}

	/**
	 * @param i
	 */
	public void setIslands(int i)
	{
		mIslands = i;
	}

	/**
	 * @param i
	 */
	public void setMajor(int i)
	{
		mMajor = i;
	}

	/**
	 * @param i
	 */
	public void setMinor(int i)
	{
		mMinor = i;
	}

	/**
	 * @return
	 */
	public boolean isLife()
	{
		return mLife;
	}

	/**
	 * @param b
	 */
	public void setLife(boolean b)
	{
		mLife = b;
	}

	/**
	 * @return
	 */
	public double getFrostLine()
	{
		return mFrostLine;
	}

	/**
	 * @return
	 */
	public double getPermafrostLine()
	{
		return mPermafrostLine;
	}

	/**
	 * @param d
	 */
	public void setFrostLine(double d)
	{
		mFrostLine = d;
	}

	/**
	 * @param d
	 */
	public void setPermafrostLine(double d)
	{
		mPermafrostLine = d;
	}

	/**
	 * @return
	 */
	public int getVolcanos()
	{
		return mVolcanos;
	}

	/**
	 * @param i
	 */
	public void setVolcanos(int i)
	{
		mVolcanos = i;
	}

	/**
	 * @return
	 */
	public double getTemperate()
	{
		return mTemperate;
	}

	/**
	 * @return
	 */
	public double getTropical()
	{
		return mTropical;
	}

	/**
	 * @param d
	 */
	public void setTemperate(double d)
	{
		mTemperate = d;
	}

	/**
	 * @param d
	 */
	public void setTropical(double d)
	{
		mTropical = d;
	}

    public LanguageBean getLanguage()
    {
        return mLanguage;
    }

    public void setLanguage(LanguageBean language)
    {
        mLanguage = language;
    }

}
