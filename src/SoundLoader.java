import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class loads in audio in the form of .wav files and makes them playable
 */
public class SoundLoader implements LineListener
{
  private static SoundLoader combust;
  private static SoundLoader groan;
  private static SoundLoader zWalk;
  private static SoundLoader bite;
  private static SoundLoader hitObst;
  private static SoundLoader walkSound;
  private static SoundLoader runSound;
  private static SoundLoader scream;
  boolean playFinished;
  //String audioFilePath;
  Thread thread;
  Clip audioClip;

  public SoundLoader(String path)
  {
    InputStream inputStream =
        ClassLoader.getSystemResourceAsStream("resources/" + path);

    try
    {
      AudioInputStream audioStream =
          AudioSystem.getAudioInputStream(inputStream);
      AudioFormat format = audioStream.getFormat();
      DataLine.Info info = new DataLine.Info(Clip.class, format);
      audioClip = (Clip) AudioSystem.getLine(info);
      audioClip.addLineListener(SoundLoader.this);
      audioClip.open(audioStream);

    }
    catch (UnsupportedAudioFileException e)
    {
      e.printStackTrace();
    }
    catch (LineUnavailableException e)
    {
      e.printStackTrace();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  public static void playExplosion()
  {
    combust.play();
  }

  public static void loadSounds()
  {
    combust = new SoundLoader("explosion.wav");
    groan = new SoundLoader("zGroan.wav");
    zWalk = new SoundLoader("zWalk.wav");
    bite = new SoundLoader("zBite.wav");
    hitObst = new SoundLoader("zHitObst.wav");
    runSound = new SoundLoader("pRunSound.wav");
    walkSound = new SoundLoader("pWalkSound.wav");
    scream = new SoundLoader("pScream.wav");

  }

  public static void playBite()
  {
    bite.play();
    scream.play();
  }

  public static void playHitObst()
  {
    hitObst.play();
  }

  public static void playZWalk()
  {
    zWalk.playLooped();
  }

  public static void stopSounds()
  {
    zWalk.stop();
    hitObst.stop();
    combust.stop();
  }

  public static void stopMoving()
  {
    runSound.stop();
    walkSound.stop();
  }

  public static void playerRun()
  {
    runSound.playLooped();
  }

  public static void playerWalk()
  {
    walkSound.playLooped();
  }

  void play()
  {
    thread = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        audioClip.start();

      }
    });
    thread.start();
  }

  @Override
  public void update(LineEvent event)
  {
    LineEvent.Type type = event.getType();

    if (type == LineEvent.Type.START)
    {
      // System.out.println("Playback started.");

    }
    else if (type == LineEvent.Type.STOP)
    {
      playFinished = true;
      //System.out.println("Playback completed.");
    }

  }

  public void playLooped(int loops)
  {
    audioClip.loop(loops);
    play();
  }

  public void playLooped()
  {
    audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    play();
  }

  public void stop()
  {
    audioClip.stop();
  }
}
