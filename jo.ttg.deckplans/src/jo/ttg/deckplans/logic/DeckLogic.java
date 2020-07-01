package jo.ttg.deckplans.logic;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import jo.util.utils.obj.StringUtils;

public class DeckLogic
{

    public static void write(File deckFile)
    {
        File dir = deckFile.getParentFile();
        String name = deckFile.getName();
        if (name.toLowerCase().endsWith(".png"))
            name = name.substring(0, name.length() - 4);
        if (name.length() > 3)
            if ((name.charAt(name.length() - 3) == '_') && Character.isDigit(name.charAt(name.length() - 2)) && Character.isDigit(name.charAt(name.length() - 1)))
                name = name.substring(0, name.length() - 3);
        List<BufferedImage> decks = RuntimeLogic.getInstance().getShipDeck().getDecks();
        for (int i = 0; i < decks.size(); i++)
        {
            BufferedImage deck = decks.get(i);
            File pngFile = new File(dir, name+"_"+StringUtils.zeroPrefix(i, 2)+".png");
            try
            {
                RuntimeLogic.setStatus("Writing "+pngFile);
                ImageIO.write(deck, "PNG", pngFile);
            }
            catch (IOException e)
            {
            }
        }
    }

}
