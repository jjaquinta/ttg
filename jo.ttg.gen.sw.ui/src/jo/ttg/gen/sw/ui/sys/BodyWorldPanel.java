/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.sw.ui.sys;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;

import jo.ttg.beans.sys.BodyWorldBean;
import jo.ttg.beans.sys.CityBean;
import jo.util.ui.swing.ctrl.HTMLPane;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class BodyWorldPanel extends PopulatedStatsPanel
{
    private HTMLPane   mClient;
    
	public BodyWorldPanel()
	{
		initInstantiate();
		initLink();
		initLayout();
	}
    
    private static final String TEMPLATE = "<html>"
            + "<body style='font-family: Sans-Serif;'>"
            + "<table width='100%' align='top'>"
            + "<tr><th valign='baseline'>Name:</th><td colspan='2'><font size='+2'>[[NAME]]</font></td></tr>"
            + "<tr><th align='right'>Port:</th><td>[[populatedStats.upp.port.getValueDigit()]]</td><td>[[populatedStats.upp.port.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#size'>Size</a>:</th><td>[[populatedStats.upp.size.getValueDigit()]]</td><td>[[populatedStats.upp.size.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#atmos'>Atmosphere</a>:</th><td>[[populatedStats.upp.atmos.getValueDigit()]]</td><td>[[populatedStats.upp.atmos.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#hydro'>Hydrosphere</a>:</th><td>[[populatedStats.upp.hydro.getValueDigit()]]</td><td>[[populatedStats.upp.hydro.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#pop'>Population</a>:</th><td>[[populatedStats.upp.pop.getValueDigit()]]</td><td>[[populatedStats.upp.pop.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#gov'>Government</a>:</th><td>[[populatedStats.upp.gov.getValueDigit()]]</td><td>[[populatedStats.upp.gov.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#law'>Law Level</a>:</th><td>[[populatedStats.upp.law.getValueDigit()]]</td><td>[[populatedStats.upp.law.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'><a href='#tech'>Tech Level</a>:</th><td>[[populatedStats.upp.tech.getValueDigit()]]</td><td>[[populatedStats.upp.tech.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Bases:</th><td colspan='2'>[[populatedStats.getBasesDescLong()]]</td></tr>"
            + "<tr><th align='right'>Zone:</th><td colspan='2'>[[populatedStats.getTravelZoneDesc()]]</td></tr>"
            + "<tr><th align='right'>Alliegence:</th><td>[[populatedStats.allegiance]]</td><td>[[populatedStats.getAllegianceDesc()]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='size'>Size Related:</a></h2>"
            + "<table>"
            + "<tr><th align='right'>Diameter:</th><td align='left'>[[diameter:distance]]</td></tr>"
            + "<tr><th align='right'>Density:</th><td align='left'>[[density]] ([[statsSiz.coreDesc]])</td></tr>"
            + "<tr><th align='right'>Mass:</th><td align='left'>[[mass:mass]]</td></tr>"
            + "<tr><th align='right'>Orbit Period:</th><td align='left'>[[orbitalPeriod:days]]</td></tr>"
            + "<tr><th align='right'>Rotation Period:</th><td align='left'>[[statsSiz.day:hours]]</td></tr>"
            + "<tr><th align='right'>Axial Tilt:</th><td align='left'>[[tilt]]&deg;</td></tr>"
            + "<tr><th align='right'>Orb. Eccentricity:</th><td align='left'>[[eccentricity]]</td></tr>"
            + "<tr><th align='right'>Seismic Stress:</th><td align='left'>[[statsSiz.seismicStress]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='atmos'>Atmosphere Related:</a></h2>"
            + "<table>"
            + "<tr><th align='right'>Composition:</th><td align='left'>[[statsAtm.atmosDesc]]</td></tr>"
            + "<tr><th align='right'>Surface Pressure:</th><td align='left'>[[statsAtm.pressure]]</td></tr>"
            + "<tr><th align='right'>Surface Temperature:</th><td align='left'>[[statsAtm.temperature:temp]]</td></tr>"
            + "<tr><th align='right'>Native Life:</th><td align='left'>[[statsAtm.life]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='hydro'>Hydrosphere Related:</a></h2>"
            + "<table>"
            + "<tr><th align='right'>Coverage:</th><td align='left'>[[statsHyd.waterPercent]]%</td></tr>"
            + "<tr><th align='right'>Composition:</th><td align='left'>[[statsHyd.typeDesc]]</td></tr>"
            + "<tr><th align='right'>Tectonic Plates:</th><td align='left'>[[statsHyd.numPlates]]</td></tr>"
            + "<tr><th align='right'>[[statsHyd.majorDesc]]:</th><td align='left'>[[statsHyd.major]]</td></tr>"
            + "<tr><th align='right'>[[statsHyd.minorDesc]]:</th><td align='left'>[[statsHyd.minor]]</td></tr>"
            + "<tr><th align='right'>[[statsHyd.islandsDesc]]:</th><td align='left'>[[statsHyd.islands]]</td></tr>"
            + "<tr><th align='right'>[[statsHyd.archipeligosDesc]]:</th><td align='left'>[[statsHyd.archepeligos]]</td></tr>"
            + "<tr><th align='right'>Notable Volcanos:</th><td align='left'>[[statsHyd.numVolcanos]]</td></tr>"
            + "</table>"
            + "<h4>Natural Resources:</h4>"
            + "<table>"
            + "<tr><th align='right'>Agricultural:</th><td align='left'>[[statsHyd.resources.Agricultural]]</td>"
            + "<th align='right'>Ore:</th><td align='left'>[[statsHyd.resources.Ores]]</td>"
            + "<th align='right'>Radioactives:</th><td align='left'>[[statsHyd.resources.Radioactives]]</td>"
            + "<th align='right'>Crystals:</th><td align='left'>[[statsHyd.resources.Crystals]]</td></tr>"
            + "<tr><th align='right'>Compounds:</th><td align='left'>[[statsHyd.resources.Compounds]]</td></tr>"
            + "</table>"
            + "<h4>Processed Resources:</h4>"
            + "<table>"
            + "<tr><th align='right'>Agroproducts:</th><td align='left'>[[statsHyd.resources.Agroproducts]]</td>"
            + "<th align='right'>Metalic:</th><td align='left'>[[statsHyd.resources.Metalic]]</td>"
            + "<th align='right'>Nonmetalic:</th><td align='left'>[[statsHyd.resources.NonMetalic]]</td></tr>"
            + "</table>"
            + "<h4>Manufactured Resources:</h4>"
            + "<table>"
            + "<tr><th align='right'>Parts:</th><td align='left'>[[statsHyd.resources.Parts]]</td>"
            + "<th align='right'>Durables:</th><td align='left'>[[statsHyd.resources.Durables]]</td>"
            + "<th align='right'>Consumable:</th><td align='left'>[[statsHyd.resources.Consumable]]</td>"
            + "<th align='right'>Weapons:</th><td align='left'>[[statsHyd.resources.Weapons]]</td></tr>"
            + "</table>"
            + "<h4>Information:</h4>"
            + "<table>"
            + "<tr><th align='right'>Recordings:</th><td align='left'>[[statsHyd.resources.Recordings]]</td>"
            + "<th align='right'>Artifacts:</th><td align='left'>[[statsHyd.resources.Artifacts]]</td>"
            + "<th align='right'>Software:</th><td align='left'>[[statsHyd.resources.Software]]</td>"
            + "<th align='right'>Documents:</th><td align='left'>[[statsHyd.resources.Documents]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='pop'>Population Related:</a></h2>"
            + "<table>"
            + "<tr><th align='right'>Total Population:</th><td align='left'>[[statsPop.totalPop:int]]</td></tr>"
            + "<tr><th align='right' valign='top'>Local Customs:</th><td align='left'>[[LOCAL_CUSTOMS]]</td></tr>"
            + "<tr><th align='right'>Large Cities:</th><td align='left'>[[statsPop.pop2]]</td></tr>"
            + "<tr><th align='right'>Cities:</th><td align='left'>[[statsPop.pop3]]</td></tr>"
            + "<tr><th align='right'>Towns:</th><td align='left'>[[statsPop.pop4]]</td></tr>"
            + "<tr><th align='right'>Villages:</th><td align='left'>[[statsPop.pop5]]</td></tr>"
            + "<tr><th align='right'>Rural Population:</th><td align='left'>[[statsPop.ruralPop:int]]</td></tr>"
            + "<tr><th align='right'>Progressiveness:</th><td align='left'>[[statsPop.aggrDesc]]</td></tr>"
            + "<tr><th align='right'>Agressiveness:</th><td align='left'>[[statsPop.progDesc]]</td></tr>"
            + "<tr><th align='right'>Extensiveness:</th><td align='left'>[[statsPop.extDesc]]</td></tr>"
            + "</table>"
            + "[[CITIES]]"
            + "<hr/>"
            + "<h2><a name='gov'>Government Related:</a></h2>"
            + "<h4>Government Branches:</h4>"
            + "<table>"
            + "<tr><th align='right'>Branch</th><td align='left'>Organization</td></tr>"
            + "<tr><th align='right'>[[statsGov.branches.[0].organizationDescription]]</th><td align='left'>[[statsGov.branches.[0].coverage]]</td></tr>"
            + "<tr><th align='right'>[[statsGov.branches.[1].organizationDescription]]</th><td align='left'>[[statsGov.branches.[1].coverage]]</td></tr>"
            + "<tr><th align='right'>[[statsGov.branches.[2].organizationDescription]]</th><td align='left'>[[statsGov.branches.[2].coverage]]</td></tr>"
            + "</table>"
            + "<h4>Religious Profile:</h4>"
            + "<table>"
            + "<tr><th align='right'>God View:</th><td align='left'>[[statsGov.godViewDesc]]</td></tr>"
            + "<tr><th align='right'>Spiritual Aim:</th><td align='left'>[[statsGov.spiritualAimDescription]]</td></tr>"
            + "<tr><th align='right'>Devotion Required:</th><td align='left'>[[statsGov.devotionRequiredDesc]]</td></tr>"
            + "<tr><th align='right'>Organization:</th><td align='left'>[[statsGov.religiousOrganisationDesc]]</td></tr>"
            + "<tr><th align='right'>Liturgical Formality:</th><td align='left'>[[statsGov.liturgicalFormalityDesc]]</td></tr>"
            + "<tr><th align='right'>Missionary Fervor:</th><td align='left'>[[statsGov.missionaryFervourDesc]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='law'>Law Related:</a></h2>"
            + "<table>"
            + "<tr><th align='right'>Overall</th><td>[[populatedStats.upp.law.getValueDigit()]]</td><td align='left'>[[populatedStats.upp.law.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Uniformity</th><td></td><td align='left'>[[statsLaw.uniformityDesc]]</td></tr>"
            + "<tr><th align='right'>Weapons</th><td align='left'>[[statsLaw.weapons:uppdigit]]</td><td align='left'>[[statsLaw.weapons:upplaw]]</td></tr>"
            + "<tr><th align='right'>Trade</th><td align='left'>[[statsLaw.trade:uppdigit]]</td><td align='left'>[[statsLaw.trade:upplaw]]</td></tr>"
            + "<tr><th align='right'>Criminal Law</th><td align='left'>[[statsLaw.criminal:uppdigit]]</td><td align='left'>[[statsLaw.criminal:upplaw]]</td></tr>"
            + "<tr><th align='right'>Civil Law</th><td align='left'>[[statsLaw.civil:uppdigit]]</td><td align='left'>[[statsLaw.civil:upplaw]]</td></tr>"
            + "<tr><th align='right'>Personal Freedom</th><td align='left'>[[statsLaw.personalFreedom:uppdigit]]</td><td align='left'>[[statsLaw.personalFreedom:upplaw]]</td></tr>"
            + "</table>"
            + "<hr/>"
            + "<h2><a name='tech'>Technology Related:</a></h2>"
            + "</table>"
            + "<tr><td colspan='3'>Common</td></tr>"
            + "<tr><th align='right'>High Common</th><td>[[populatedStats.upp.tech.getValueDigit()]]</td><td>[[populatedStats.upp.tech.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Low Common</th><td align='left'>[[statsTec.low:uppdigit]]</td><td align='left'>[[statsTec.low:upptech]]</td></tr>"
            + "<tr><td colspan='3'>Quality of Life</td></tr>"
            + "<tr><th align='right'>Energy</th><td align='left'>[[statsTec.energy:uppdigit]]</td><td align='left'>[[statsTec.energy:upptech]]</td></tr>"
            + "<tr><th align='right'>Computers/Robotics</th><td align='left'>[[statsTec.robot:uppdigit]]</td><td align='left'>[[statsTec.robot:upptech]]</td></tr>"
            + "<tr><th align='right'>Communications</th><td align='left'>[[statsTec.commo:uppdigit]]</td><td align='left'>[[statsTec.commo:upptech]]</td></tr>"
            + "<tr><th align='right'>Medical</th><td align='left'>[[statsTec.medical:uppdigit]]</td><td align='left'>[[statsTec.medical:upptech]]</td></tr>"
            + "<tr><th align='right'>Environment</th><td align='left'>[[statsTec.environmental:uppdigit]]</td><td align='left'>[[statsTec.environmental:upptech]]</td></tr>"
            + "<tr><td colspan='3'>Transportation</td></tr>"
            + "<tr><th align='right'>Land</th><td align='left'>[[statsTec.land:uppdigit]]</td><td align='left'>[[statsTec.land:upptech]]</td></tr>"
            + "<tr><th align='right'>Water</th><td align='left'>[[statsTec.water:uppdigit]]</td><td align='left'>[[statsTec.water:upptech]]</td></tr>"
            + "<tr><th align='right'>Air</th><td align='left'>[[statsTec.air:uppdigit]]</td><td align='left'>[[statsTec.air:upptech]]</td></tr>"
            + "<tr><th align='right'>Space</th><td align='left'>[[statsTec.space:uppdigit]]</td><td align='left'>[[statsTec.space:upptech]]</td></tr>"
            + "<tr><td colspan='3'>Military</td></tr>"
            + "<tr><th align='right'>Personal Military</th><td align='left'>[[statsTec.personalMilitary:uppdigit]]</td><td align='left'>[[statsTec.personalMilitary:upptech]]</td></tr>"
            + "<tr><th align='right'>Heavy Military</th><td align='left'>[[statsTec.heavyMilitary:uppdigit]]</td><td align='left'>[[statsTec.heavyMilitary:upptech]]</td></tr>"
            + "<tr><td colspan='3'>Novelty</td></tr>"
            + "<tr><th align='right'>Novelty</th><td align='left'>[[statsTec.novel:uppdigit]]</td><td align='left'>[[statsTec.novel:upptech]]</td></tr>"
            + "<table>"
            + "<hr/>"
            + "<h3>Notes:</h3>"
            + "<p>[[notes]]</p>"
            + "</body>"
            + "</html>";

	private void initInstantiate()
	{
        mClient = new HTMLPane();
        mClient.setTemplate(TEMPLATE);
	}
	private void initLayout()
	{
        setLayout(new BorderLayout());
        add("Center", new JScrollPane(mClient));
	}
	private void initLink()
	{
	}
	
	/**
	 * @param bean
	 */
	public void setBody(BodyWorldBean bean)
	{
	    StringBuffer localCustoms = new StringBuffer();
	    for (String custom : bean.getStatsPop().getCustoms())
	    {
	        if (localCustoms.length() >  0)
	            localCustoms.append("<br/>");
	        localCustoms.append(custom);
	    }
	    StringBuffer cities = new StringBuffer();
	    if (bean.getStatsPop().getCities().length > 0)
	    {
            cities.append("<h4>Major Cities:</h4>");
	        cities.append("<table>");
	        cities.append("<tr><th align='left'>Name</th><th align='right'>Population</th></tr>");
	        for (CityBean city : bean.getStatsPop().getCities())
	            cities.append("<tr><td align='left'>"+city.getName()+"</td><td align='right'>"+(int)city.getPop()+"</td></tr>");
            cities.append("</table>");
	    }
        mClient.replaceBean(bean);
        mClient.setSubstitution("LOCAL_CUSTOMS", localCustoms.toString());
        mClient.setSubstitution("CITIES", cities.toString());
        mClient.updateText();
        doLayout();
	}
}
