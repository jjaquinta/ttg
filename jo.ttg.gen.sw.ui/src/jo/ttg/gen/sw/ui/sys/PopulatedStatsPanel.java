/*
 * Created on Dec 2, 2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package jo.ttg.gen.sw.ui.sys;

import javax.swing.JPanel;

/**
 * @author jjaquinta
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class PopulatedStatsPanel extends JPanel
{
    protected static final String POPULATED_STATS_TEMPLATE = "" 
            + "<tr><th align='right'>Port:</th><td>[[populatedStats.upp.port.getValueDigit()]]</td><td>[[populatedStats.upp.port.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Size:</th><td>[[populatedStats.upp.size.getValueDigit()]]</td><td>[[populatedStats.upp.size.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Atmosphere:</th><td>[[populatedStats.upp.atmos.getValueDigit()]]</td><td>[[populatedStats.upp.atmos.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Hydrosphere:</th><td>[[populatedStats.upp.hydro.getValueDigit()]]</td><td>[[populatedStats.upp.hydro.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Population:</th><td>[[populatedStats.upp.pop.getValueDigit()]]</td><td>[[populatedStats.upp.pop.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Government:</th><td>[[populatedStats.upp.gov.getValueDigit()]]</td><td>[[populatedStats.upp.gov.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Law Level:</th><td>[[populatedStats.upp.law.getValueDigit()]]</td><td>[[populatedStats.upp.law.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Tech Level:</th><td>[[populatedStats.upp.tech.getValueDigit()]]</td><td>[[populatedStats.upp.tech.getValueDescription()]]</td></tr>"
            + "<tr><th align='right'>Bases:</th><td colspan='2'>[[populatedStats.getBasesDescLong()]]</td></tr>"
            + "<tr><th align='right'>Zone:</th><td colspan='2'>[[populatedStats.getTravelZoneDesc()]]</td></tr>"
            + "<tr><th align='right'>Alliegence:</th><td>[[populatedStats.allegiance]]</td><td>[[populatedStats.getAllegianceDesc()]]</td></tr>"
            ;
}
