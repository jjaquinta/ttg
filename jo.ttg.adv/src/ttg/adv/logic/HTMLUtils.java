/*
 * Created on Jan 23, 2005
 *
 */
package ttg.adv.logic;

import jo.util.utils.obj.StringUtils;

/**
 * @author Jo
 *
 */
public class HTMLUtils
{
    
    public static String createTable(Object[] colLabels, Object[] rowLabels, String[][] table)
    {
        StringBuffer html = new StringBuffer();
        html.append("<table>");
        // header row
        html.append("<tr>");
        html.append("<td></td>");
        for (int col = 0; col < colLabels.length; col++)
        {
            html.append("<td>");
            html.append(breakName(colLabels[col].toString()));
            html.append("</td>");
        }
        html.append("</tr>");
        // data rows
        for (int row = 0; row < rowLabels.length; row++)
        {
            html.append("<tr>");
            html.append("<td>");
            html.append(rowLabels[row].toString());
            html.append("</td>");
	        for (int col = 0; col < colLabels.length; col++)
	        {
	            html.append("<td>");
                html.append(table[row][col]);
	            html.append("</td>");
	        }
	        html.append("</tr>");
        }
        html.append("</table>");
        return html.toString();
    }
    
    private static String breakName(String name)
    {
        return StringUtils.substitute(name, " ", "<br>");
    }
}
