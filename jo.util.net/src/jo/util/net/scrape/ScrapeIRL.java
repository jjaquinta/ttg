package jo.util.net.scrape;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import jo.util.logic.CSVLogic;
import jo.util.net.http.HTTPUtils;
import jo.util.utils.io.ReaderUtils;
import jo.util.utils.obj.StringUtils;

public class ScrapeIRL
{
    public static void main(String[] argv) throws IOException
    {
        String baseURL = "https://www.education.ie/en/Publications/Inspection-Reports-Publications/Whole-School-Evaluation-Reports-List/?pageNumber=%d";
        File csvFile = new File("d:\\temp\\data\\irl_schools\\schools.csv");
        BufferedWriter wtr = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(csvFile), "utf-16"));
        for (int i = 1; i < 176; i++)
        {
            String url = String.format(baseURL, i);
            System.out.println(url);
            InputStream is = HTTPUtils.getURLAsStream(url);
            InputStreamReader rdr = new InputStreamReader(is, "utf-8");
            String html = ReaderUtils.readStream(rdr);
            rdr.close();
            String table = StringUtils.extract(html, "<table", "</table>");
            String[] rows = StringUtils.extractArray(table, "<tr", "</tr>");
            for (String row : rows)
            {
                String[] cols = StringUtils.extractArray(row, "<td", "</td>");
                List<String> csv = new ArrayList<>();
                for (int j = 0; j < cols.length; j++)
                {
                    String col = cols[j];
                    if (col.startsWith("<td"))
                    {
                        int o = col.indexOf('>');
                        col = col.substring(o + 1).trim();
                    }
                    int o = col.indexOf("href=\"");
                    if (o > 0)
                    {
                        col = col.substring(o + 6);
                        o = col.indexOf('\"');
                        col = col.substring(0, o);
                    }
                    csv.add(col);
                }
                if (csv.size() > 0)
                {
                    wtr.write(CSVLogic.toCSVLine(csv));
                    wtr.newLine();
                }
            }
        }
        wtr.close();
    }
}
