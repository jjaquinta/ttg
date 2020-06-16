package jo.util.net.scrape;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import jo.util.logic.CSVLogic;
import jo.util.net.http.HTTPUtils;
import jo.util.utils.io.FileUtils;
import jo.util.utils.io.StreamUtils;

public class ScrapeIRLPDF
{
    public static void main(String[] argv) throws IOException
    {
        File dir = new File("d:\\temp\\data\\irl_schools");
        File csvFile = new File(dir, "schools.csv");
        BufferedReader rdr = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "utf-8"));
        for (;;)
        {
            String inbuf = rdr.readLine();
            if (inbuf == null)
                break;
            String[] line = CSVLogic.splitCSVLine(inbuf);
            String id = line[1];
            String county = line[2];
            String name = line[3];
            String url = "https://www.education.ie"+line[7];
            if (!county.equals("Dublin"))
                continue;
            File out = new File(dir, id+".pdf");
            if (!url.endsWith(".pdf"))
            {
                if (out.exists())
                    out.delete();
                int o = url.lastIndexOf('.');
                out = new File(dir, id+url.substring(o));
            }
            System.out.println(name);
            if (!out.exists())
            {
                try
                {
                    InputStream is = HTTPUtils.getURLAsStream(url);
                    OutputStream os = new FileOutputStream(out);
                    StreamUtils.copy(is, os);
                    is.close();
                    os.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            File txt = new File(dir, id+".txt");
            if (!txt.exists())
                if (out.getName().endsWith(".htm"))
                {
                    String html = FileUtils.readFileAsString(out.toString());
                    String plain = html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
                    FileUtils.writeFile(plain, txt);
                }
        }
        rdr.close();
    }
}
