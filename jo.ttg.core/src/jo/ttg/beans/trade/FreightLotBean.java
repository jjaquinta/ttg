package jo.ttg.beans.trade;

import java.util.ArrayList;
import java.util.List;

import jo.ttg.beans.DateBean;
import jo.ttg.beans.URIBean;


public class FreightLotBean implements URIBean
{
    private String          mURI;
    private List<CargoBean> mFreights;
    private String          mOriginURI;
    private String          mDestinationURI;
    private DateBean        mDate;
    
    public FreightLotBean()
    {
        mFreights = new ArrayList<CargoBean>();
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
    public List<CargoBean> getFreights()
    {
        return mFreights;
    }
    public void setFreights(List<CargoBean> cargos)
    {
        mFreights = cargos;
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

    public String getDestinationURI()
    {
        return mDestinationURI;
    }

    public void setDestinationURI(String destinationURI)
    {
        mDestinationURI = destinationURI;
    }
}
