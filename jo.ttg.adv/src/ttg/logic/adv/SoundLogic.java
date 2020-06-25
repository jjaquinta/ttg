/*
 * Created on Jan 25, 2005
 *
 */
package ttg.logic.adv;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;

import jo.util.utils.io.ResourceUtils;
import ttg.view.adv.Adv;

/**
 * @author Jo
 *  
 */
public class SoundLogic
{
    private static List<String>		mQueue = new ArrayList<>();
    private static SoundLogicThread	mThread = null;
    private static Clip				mCurrentClip;

    public static void queueResource(String path)
    {
        synchronized (mQueue)
        {
            mQueue.add(path);
            startThread();
        }
    }

    public static void queueResourceIfNotThere(String path)
    {
        synchronized (mQueue)
        {
            if (!mQueue.contains(path))
            {
                mQueue.add(path);
                startThread();
            }
        }
    }
    
    static String getFirstInQueue()
    {
        synchronized (mQueue)
        {
            if (mQueue.size() == 0)
                return null;
            return (String)mQueue.get(0);
        }
    }
    
    static void removeFirstInQueue()
    {
        synchronized (mQueue)
        {
            mQueue.remove(0);
        }
    }
    
    private static void startThread()
    {
        if ((mThread == null) || !mThread.isAlive())
        {
            mThread = new SoundLogicThread();
            mThread.start();
        }
    }

    public static void playResource(String path)
    {
        byte[] midi = null;
        try
        {
            midi = ResourceUtils.loadSystemResourceBinary(path, Adv.class);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        if (midi == null)
            return;
        try
        {
            AudioInputStream stream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(midi));
            AudioFormat format = stream.getFormat();

            // Create the clip
            DataLine.Info info = new DataLine.Info(Clip.class, stream
                    .getFormat(), ((int)stream.getFrameLength() * format
                    .getFrameSize()));
            mCurrentClip = (Clip)AudioSystem.getLine(info);

            // This method does not return until the audio file is completely
            // loaded
            mCurrentClip.open(stream);

            // Start playing
            mCurrentClip.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public static Clip getCurrentClip()
    {
        return mCurrentClip;
    }
    
    public static void setCurrentClip(Clip clip)
    {
        mCurrentClip = clip;
    }
}

class SoundLogicThread extends Thread
{
    public void run()
    {
        for (;;)
        {
            String resource = SoundLogic.getFirstInQueue();
            if (resource == null)
                break;
            SoundLogic.playResource(resource);
            while (SoundLogic.getCurrentClip().isRunning())
            {
                try
                {
                    Thread.sleep(100);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
            SoundLogic.setCurrentClip(null);
            SoundLogic.removeFirstInQueue();
        }
    }
}
