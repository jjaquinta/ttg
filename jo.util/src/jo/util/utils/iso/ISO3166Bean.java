package jo.util.utils.iso;

import jo.util.beans.Bean;

public class ISO3166Bean extends Bean implements Comparable<ISO3166Bean>
{
    private String mCountryName;
    private String mFlagURL;
    private String mISO3166_2;
    private String mISO3166_3;
    private String mISO3166_N;

    @Override
    public int compareTo(ISO3166Bean o)
    {
        return getISO3166_2().compareTo(o.getISO3166_2());
    }

    public String getCountryName()
    {
        return mCountryName;
    }

    public void setCountryName(String countryName)
    {
        mCountryName = countryName;
    }

    public String getFlagURL()
    {
        return mFlagURL;
    }

    public void setFlagURL(String flagURL)
    {
        mFlagURL = flagURL;
    }

    public String getISO3166_2()
    {
        return mISO3166_2;
    }

    public void setISO3166_2(String iSO3166_2)
    {
        mISO3166_2 = iSO3166_2;
    }

    public String getISO3166_3()
    {
        return mISO3166_3;
    }

    public void setISO3166_3(String iSO3166_3)
    {
        mISO3166_3 = iSO3166_3;
    }

    public String getISO3166_N()
    {
        return mISO3166_N;
    }

    public void setISO3166_N(String iSO3166_N)
    {
        mISO3166_N = iSO3166_N;
    }
}
