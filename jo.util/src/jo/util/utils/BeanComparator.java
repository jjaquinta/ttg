/*
 * Created on Jun 27, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.utils;

import java.util.Comparator;

import jo.util.beans.Bean;

public class BeanComparator implements Comparator<Bean>
{
    private String  mSortBy;
    
    public BeanComparator(String sortBy)
    {
        mSortBy = sortBy;
    }
    
    public int compare(Bean arg0, Bean arg1)
    {
        if (arg0 == null)
            return arg1 == null ? 0 : -1;
        if (arg1 == null)
            return arg0 == null ? 0 : 1 ;
        Object v0 = BeanUtils.get(arg0, mSortBy);
        Object v1 = BeanUtils.get(arg1, mSortBy);
        if (v0 == null)
            return v1 == null ? 0 : -1;
        if (v1 == null)
            return v0 == null ? 0 : 1 ;
        if (v0 instanceof Number)
            return (int)(((Number)v0).longValue() - ((Number)v1).longValue());
        return v0.toString().compareTo(v1.toString());
    }
}
