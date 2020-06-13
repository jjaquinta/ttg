package jo.util.utils.iso;

import jo.util.beans.Bean;

public class ISO639Bean extends Bean implements Comparable<ISO639Bean>
{
    private String mFamily;
    private String mLanguageName;
    private String mNativeName;
    private String mISO639_1;
    private String mISO639_2t;
    private String mISO639_2b;
    private String mISO639_3;
    
    public String getFamily()
    {
        return mFamily;
    }
    public void setFamily(String family)
    {
        mFamily = family;
    }
    public String getLanguageName()
    {
        return mLanguageName;
    }
    public void setLanguageName(String languageName)
    {
        mLanguageName = languageName;
    }
    public String getNativeName()
    {
        return mNativeName;
    }
    public void setNativeName(String nativeName)
    {
        mNativeName = nativeName;
    }
    public String getISO639_1()
    {
        return mISO639_1;
    }
    public void setISO639_1(String iSO639_1)
    {
        mISO639_1 = iSO639_1;
    }
    public String getISO639_2t()
    {
        return mISO639_2t;
    }
    public void setISO639_2t(String iSO639_2t)
    {
        mISO639_2t = iSO639_2t;
    }
    public String getISO639_2b()
    {
        return mISO639_2b;
    }
    public void setISO639_2b(String iSO639_2b)
    {
        mISO639_2b = iSO639_2b;
    }
    public String getISO639_3()
    {
        return mISO639_3;
    }
    public void setISO639_3(String iSO639_3)
    {
        mISO639_3 = iSO639_3;
    }

    @Override
    public int compareTo(ISO639Bean o)
    {
        return getISO639_1().compareTo(o.getISO639_1());
    }
}
