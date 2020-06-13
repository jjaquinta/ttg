package jo.ttg.beans.trade;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;


public class CargoLotBean implements URIBean
{
    private String          mURI;
    private List<CargoBean> mCargos;
    private String          mOriginURI;
    private DateBean        mDate;
    
    public CargoLotBean()
    {
        mCargos = new ArrayList<CargoBean>();
        mDate = new DateBean();
    }
    
    public String getURI()
    {
        return mURI;
    }
    public void setURI(String uRI)
    {
        mURI = uRI;
    }
    public List<CargoBean> getCargos()
    {
        return mCargos;
    }
    public void setCargos(List<CargoBean> cargos)
    {
        mCargos = cargos;
    }
    public String getOriginURI()
    {
        return mOriginURI;
    }
    public void setOriginURI(String originURI)
    {
        mOriginURI = originURI;
    }

    public DateBean getDate()
    {
        return mDate;
    }

    public void setDate(DateBean date)
    {
        mDate = date;
    }
}
