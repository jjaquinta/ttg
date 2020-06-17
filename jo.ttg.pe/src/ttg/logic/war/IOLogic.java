package ttg.logic.war;

public class IOLogic
{
    public static void installProtocolHandlers()
    {
        String path = System.getProperty("java.protocol.handler.pkgs");
        if ((path == null) || (path.length() == 0))
            path = "ttg.logic.url";
        else
            path = path + "|" + "ttg.logic.url";
        System.setProperty("java.protocol.handler.pkgs", path);
    }
}
