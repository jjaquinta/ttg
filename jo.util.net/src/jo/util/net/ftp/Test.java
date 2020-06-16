/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.ftp;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPFile;

public class Test
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            test("ftp://guest:@192.168.1.148/HDD_1_1_1");
            test("ftp://guest:@192.168.1.148/HDD_1_1_1/bin");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void test(String url) throws IOException
    {
        FTPFile[] list = FTPLogic.listFilesFTP(url);
        System.out.println("URL: "+url);
        for (int i = 0; i < list.length; i++)
            System.out.println("    "+list[i].getName());
    }

}
