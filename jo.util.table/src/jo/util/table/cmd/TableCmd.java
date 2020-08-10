package jo.util.table.cmd;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import jo.util.cmd.BaseCmd;
import jo.util.table.beans.TableGroup;
import jo.util.table.logic.RollLogic;
import jo.util.table.logic.TableGroupLogic;

public class TableCmd extends BaseCmd
{
    public static String helpN = "Number of times to roll";
    private int  mN = 10;

    public static String helpTable = "Table to roll on";
    private String  mTable;
    
    public String defaultArg()
    {
        return "table";
    }

    public void run()
    {
        TableGroup group = TableGroupLogic.create();
        TableGroupLogic.addTablepath(group, mTable);
        Random rnd = new Random();
        for (int i = 0; i < mN; i++)
            System.out.println(RollLogic.roll(group, "", rnd));
    }

    public String getOneLineDesc()
    {
        return "Roll some values on a table";
    }

    public String getLongDesc()
    {
        return "Roll up some values based on the given table";
    }

    public static void main(String[] argv)
    {
        TableCmd app = new TableCmd();
        app.init(argv);
    }

    public int getN()
    {
        return mN;
    }

    public void setN(int n)
    {
        mN = n;
    }

    public String getTable()
    {
        return mTable;
    }

    public void setTable(String table)
    {
        mTable = table;
        File f = new File(mTable);
        if (f.exists())
            try
            {
                mTable = f.getCanonicalPath();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
    }
}
