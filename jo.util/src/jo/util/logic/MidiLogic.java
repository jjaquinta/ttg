/*
 * Created on Apr 8, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package jo.util.logic;

import java.io.ByteArrayInputStream;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;

import jo.util.utils.io.ResourceUtils;

/**
 * @author jgrant
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class MidiLogic
{
	private static Sequencer mPlayer = null;
	
	public static synchronized Sequencer getPlayer()
	{
		if (mPlayer == null)
		{
			try
			{
				mPlayer = MidiSystem.getSequencer();
				mPlayer.open();
			}
			catch (MidiUnavailableException e)
			{
				mPlayer = null;
				e.printStackTrace();
			}
		}
		return mPlayer;
	}
	
	public static void playResource(String path, Class<?> wrt)
	{
		try
		{
			byte[] midi = ResourceUtils.loadSystemResourceBinary(path, wrt);
			if (midi == null)
				return;
			Sequencer player = getPlayer();
			player.setSequence(new ByteArrayInputStream(midi));
			player.start();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

    public static void playSequence(Sequence seq)
    {
        try
        {
            Sequencer player = getPlayer();
            player.setSequence(seq);
            player.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	public static void stopPlaying()
	{
		Sequencer player = getPlayer();
		if (player.isRunning())
			player.stop();
	}
}
