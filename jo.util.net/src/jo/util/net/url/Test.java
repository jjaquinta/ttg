/*
 * Created on Feb 14, 2006
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.net.url;

import java.io.IOException;

import jo.util.net.data.URLReferenceBean;

public class Test
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        try
        {
            test("ftp://guest:@192.168.1.148/HDD_1_1_1", false);
            test("ftp://guest:@192.168.1.148/HDD_1_1_1/bin", false);
            test("ftp://guest:@192.168.1.148/HDD_1_1_1/bin", true);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
    
    private static void test(String url, boolean recursive) throws IOException
    {
        URLReferenceBean ref = ReferenceLogic.makeReference(url);
        URLReferenceBean[] list = ReferenceLogic.list(ref, recursive);
        System.out.println("URL: "+url);
        for (int i = 0; i < list.length; i++)
            System.out.println("    "+((list[i].getReference() == null) ? "x " : "o ") + list[i].getURL());
    }

}
