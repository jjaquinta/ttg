package jo.ttg.gen.imp;


import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.trade.GoodBean;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class GoodList
{
	private int			mPhylum;
	private int			mType;
	private int			mTechLevel;
	private int			mLawLevel;
	private int			mTotalChance;
	private List<GoodBean>	mGoods;
	
	public GoodList()
	{
		mGoods = new ArrayList<GoodBean>();
	}
	/**
	 * @return
	 */
	public List<GoodBean> getGoods()
	{
		return mGoods;
	}

	/**
	 * @return
	 */
	public int getLawLevel()
	{
		return mLawLevel;
	}

	/**
	 * @return
	 */
	public int getPhylum()
	{
		return mPhylum;
	}

	/**
	 * @return
	 */
	public int getTechLevel()
	{
		return mTechLevel;
	}

	/**
	 * @return
	 */
	public int getTotalChance()
	{
		return mTotalChance;
	}

	/**
	 * @return
	 */
	public int getType()
	{
		return mType;
	}

	/**
	 * @param list
	 */
	public void setGoods(List<GoodBean> list)
	{
		mGoods = list;
	}

	/**
	 * @param i
	 */
	public void setLawLevel(int i)
	{
		mLawLevel = i;
	}

	/**
	 * @param i
	 */
	public void setPhylum(int i)
	{
		mPhylum = i;
	}

	/**
	 * @param i
	 */
	public void setTechLevel(int i)
	{
		mTechLevel = i;
	}

	/**
	 * @param i
	 */
	public void setTotalChance(int i)
	{
		mTotalChance = i;
	}

	/**
	 * @param i
	 */
	public void setType(int i)
	{
		mType = i;
	}

}
