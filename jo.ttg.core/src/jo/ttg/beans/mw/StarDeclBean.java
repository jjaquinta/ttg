package jo.ttg.beans.mw;

import jo.util.beans.Bean;

public class StarDeclBean extends Bean
{
	public final static int ST_UNKNOWN = -2;
    public final static int ST_UNSET = -1;
    public final static int ST_O = 00;
    public final static int ST_B = 10;
    public final static int ST_A = 20;
    public final static int ST_F = 30;
    public final static int ST_G = 40;
    public final static int ST_K = 50;
    public final static int ST_M = 60;

	public final static int SC_UNKNOWN = -1;
    public final static int SC_UNSET = 0;
    public final static int SC_1A = 1;
    public final static int SC_1B = 2;
    public final static int SC_2 = 3;
    public final static int SC_3 = 4;
    public final static int SC_4 = 5;
    public final static int SC_5 = 6;
    public final static int SC_6 = 7;
    public final static int SC_D = 8;

    // StarType
    private int mStarType;
    public int getStarType()
    {
        return mStarType;
    }
    public void setStarType(int v)
    {
        mStarType = v;
    }

    // StarClass
    private int mStarClass;
    public int getStarClass()
    {
        return mStarClass;
    }
    public void setStarClass(int v)
    {
        mStarClass = v;
    }


    // constructor
    public StarDeclBean()
    {
        mStarType = 0;
        mStarClass = 0;
    }

    public String getStarClassDesc()
    {
        switch (mStarClass)
        {
            case SC_1A :
                return "Ia";
            case SC_1B :
                return "Ib";
            case SC_2 :
                return "II";
            case SC_3 :
                return "III";
            case SC_4 :
                return "IV";
            case SC_5 :
                return "V";
            case SC_D :
                return "D";
            case SC_UNSET:
            	return "";
        }
        return "?";
    }
    public String importStarClass(String str)
    {
        if (str.startsWith("Ia"))
            mStarClass = SC_1A;
        else if (str.startsWith("Ib"))
            mStarClass = SC_1B;
        else if (str.startsWith("II"))
            mStarClass = SC_2;
        else if (str.startsWith("III"))
            mStarClass = SC_3;
        else if (str.startsWith("IV"))
            mStarClass = SC_4;
        else if (str.charAt(0) == 'V')
            mStarClass = SC_5;
        else if (str.charAt(0) == 'D')
            mStarClass = SC_D;
        else
            return str;
        return str.substring(getStarClassDesc().length()).trim();
    }
    public String getStarTypeDesc()
    {
        String buf;

        if (mStarType == ST_UNSET)
            return "";
		if (mStarType == ST_UNKNOWN)
			return "?";

        switch ((mStarType / 10) * 10)
        {
            case ST_A :
                buf = "A";
                break;
            case ST_B :
                buf = "B";
                break;
            case ST_F :
                buf = "F";
                break;
            case ST_G :
                buf = "G";
                break;
            case ST_K :
                buf = "K";
                break;
            case ST_M :
                buf = "M";
                break;
            case ST_O :
                buf = "O";
                break;
            default :
                buf = "X";
                break;
        }
        buf = buf + String.valueOf((char) ((mStarType % 10) + '0'));
        return buf;
    }
    public String importStarType(String str)
    {
        str = str.toUpperCase().trim();
        switch (str.charAt(0))
        {
            case 'A' :
                mStarType = ST_A;
                break;
            case 'B' :
                mStarType = ST_B;
                break;
            case 'F' :
                mStarType = ST_F;
                break;
            case 'G' :
                mStarType = ST_G;
                break;
            case 'K' :
                mStarType = ST_K;
                break;
            case 'M' :
                mStarType = ST_M;
                break;
            case 'O' :
                mStarType = ST_O;
                break;
            default :
                return str;
        }
        if (str.length() <= 1)
            return "";
        int v = str.charAt(1) - '0';
        if ((v >= 0) || (v <= 9))
        {
            mStarType += v;
            return str.substring(2).trim();
        }
        else
        {
            mStarType += 5;
            return str.substring(1).trim();
        }
    }
    public void importStarTypeClass(String str)
    {
        str = str.toUpperCase().trim();
        switch (str.charAt(0))
        {
            case 'A' :
                mStarType = ST_A;
                break;
            case 'B' :
                mStarType = ST_B;
                break;
            case 'F' :
                mStarType = ST_F;
                break;
            case 'G' :
                mStarType = ST_G;
                break;
            case 'K' :
                mStarType = ST_K;
                break;
            case 'M' :
                mStarType = ST_M;
                break;
            case 'O' :
                mStarType = ST_O;
                break;
            default :
                mStarType = ST_M;
                break;
        }
        if (str.length() > 1)
        {
            int v = str.charAt(1) - '0';
            if ((v >= 0) || (v <= 9))
                mStarType += v;
            else
                mStarType += 5;
        }
        if (str.indexOf("d") >= 0)
            mStarClass = SC_D;
        else if (str.indexOf("VI") >= 0)
            mStarClass = SC_6;
        else if (str.indexOf("IV") >= 0)
            mStarClass = SC_4;
        else if (str.indexOf("V") >= 0)
            mStarClass = SC_5;
        else if (str.indexOf("III") >= 0)
            mStarClass = SC_3;
        else if (str.indexOf("II") >= 0)
            mStarClass = SC_2;
        else if (str.indexOf("Ia") >= 0)
            mStarClass = SC_1A;
        else if (str.indexOf("Ib") >= 0)
            mStarClass = SC_1B;
        else
            mStarClass = SC_D;
    }
    public String getDesc()
    {
        return getStarTypeDesc() + " " + getStarClassDesc();
    }
    public boolean getIsSet()
    {
        return (mStarType != ST_UNSET) && (mStarClass != SC_UNSET);
    }
}
