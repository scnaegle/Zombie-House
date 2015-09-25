
/**
 * This class loads in audio in the form of .wav files and makes them playable.
 * Also decides which speaker to play sounds from.
 */
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * loads the sounds to be played from the zombies and player sprites
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
  FloatControl balanceCtrl;
  InputStream inputStream;


  public SoundLoader(String path)
  {
    inputStream =
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
    if (combust.audioClip.getFramePosition() ==
        combust.audioClip.getFrameLength())
    {
      combust.audioClip.setFramePosition(0);
      combust.audioClip.stop();
    }
  }

  /**
   * loads the sounds from specific files in are repository
   */
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

    zWalk.makeBalanceControlled();
    hitObst.makeBalanceControlled();

  }

  public static void playBite()
  {
    bite.play();
    scream.play();
  }

  public static void playScream()
  {
    scream.play();
  }

  public static void playHitObst(float balance)
  {
    hitObst.setBalance(balance);
    hitObst.play();
    if (hitObst.audioClip.getFramePosition() ==
        hitObst.audioClip.getFrameLength())
    {
      hitObst.audioClip.setFramePosition(0);
      hitObst.audioClip.stop();
    }
  }

  public static void playHitObst()
  {

    playHitObst(0);
  }

  public static void playZWalk(float balance)
  {
    zWalk.setBalance(balance);
    zWalk.playLooped();
  }

  public static void playZWalk()
  {
    playZWalk(0);
  }


  public static void stopSounds()
  {
    zWalk.stop();
    hitObst.stop();
    combust.stop();
    walkSound.stop();
    runSound.stop();
  }

  public static void stopMoving()
  {
    //System.out.println("player stopped moving");
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

//  public static void killSounds()
//  {
//
//    //Need to be able to reset all the sound threads, or kill them,
//    // but if they arent used then you get null pointer exception.
//
//    //Need a way to see which threads were created and then kill those
//
//    zWalk.thread.interrupt();
//    walkSound.thread.interrupt();
//    //runSound.thread.interrupt();
//    hitObst.thread.interrupt();
//    combust.thread.interrupt();
//    //groan.thread.interrupt();
//    //scream.thread.interrupt();
//    //bite.thread.interrupt();
//
//  }

  void makeBalanceControlled()
  {
    balanceCtrl =
        (FloatControl) audioClip.getControl(FloatControl.Type.BALANCE);
  }

  public void setBalance(float value)
  {
    balanceCtrl.setValue(value);
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
