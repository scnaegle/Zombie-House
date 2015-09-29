
/**
 * This class loads in audio in the form of .wav files and makes them playable.
 * Also decides which speaker to play sounds from.
 */
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * loads the sounds to be played from the zombies, firetraps, and player sprites
 */
public class SoundLoader implements LineListener
{
  private static SoundLoader combust;
  private static SoundLoader zWalk;
  private static SoundLoader bite;
  private static SoundLoader hitObst;
  private static SoundLoader walkSound;
  private static SoundLoader runSound;
  private static SoundLoader scream;

  boolean playFinished;
  Thread thread;
  Clip audioClip;
  FloatControl balanceCtrl;
  InputStream inputStream;


  /**
   * Loads in a sound file from directory and makes it into a sound clip.
   *
   * @param path
   */
  public SoundLoader(String path)
  {
    //inputStream = getClass().getResourceAsStream("resources/" + path);
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

  /**
   * Plays explosion sound when called. Has to restart to begging frame in order
   * to make it play multiple times.
   */
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
    zWalk = new SoundLoader("zWalk.wav");
    bite = new SoundLoader("zBite.wav");
    hitObst = new SoundLoader("zHitObst.wav");
    runSound = new SoundLoader("pRunSound.wav");
    walkSound = new SoundLoader("pWalkSound.wav");
    scream = new SoundLoader("pScream.wav");

    zWalk.makeBalanceControlled();
    hitObst.makeBalanceControlled();

  }

  /**
   * Makes sounds for when zombie bites player
   */
  public static void playBite()
  {
    bite.play();
    scream.play();
  }

  /**
   * Makes sounds for when zombie bites player
   */
  public static void playScream()
  {
    scream.play();
  }

  /**
   * Plays sounds for zombie hitting obstacles.
   * Also gets balance and plays it on certain speaker.
   * @param balance
   */
  public static void playHitObst(float balance)
  {
    hitObst.setBalance(balance);

    FloatControl volumeControl =
        (FloatControl) hitObst.audioClip
            .getControl(FloatControl.Type.MASTER_GAIN);
    volumeControl.setValue(-10.0f);

    hitObst.play();
    if (hitObst.audioClip.getFramePosition() ==
        hitObst.audioClip.getFrameLength())
    {
      hitObst.audioClip.setFramePosition(0);
      hitObst.audioClip.stop();
    }
  }

  /**
   * Plays hit sound with a current balance of zero.
   */
  public static void playHitObst()
  {
    playHitObst(0);
  }

  /**
   * Plays zombie walk sound.
   *
   * @param balance which speaker it should be played from.
   */
  public static void playZWalk(float balance)
  {
    zWalk.setBalance(balance);
    zWalk.playLooped();
  }

  /**
   * Plays zombie walk with a current balance of zero.
   */
  public static void playZWalk()
  {
    playZWalk(0);
  }


  /**
   * Stops all sounds. Usefull for pause game and restart.
   */
  public static void stopSounds()
  {
    zWalk.stop();
    hitObst.stop();
    combust.stop();
    walkSound.stop();
    runSound.stop();
  }

  /**
   * Stops player sounds from playing when standing still.
   */
  public static void stopMoving()
  {
    //System.out.println("player stopped moving");
    runSound.stop();
    walkSound.stop();
  }


  /**
   * Plays the run sound on a loop.
   */
  public static void playerRun()
  {
    runSound.playLooped();
  }

  /**
   * Plays the walk sound on a loop.
   */
  public static void playerWalk()
  {
    walkSound.playLooped();
  }


  private void makeBalanceControlled()
  {
    balanceCtrl =
        (FloatControl) audioClip.getControl(FloatControl.Type.BALANCE);
  }

  /**
   * Sets the balance which corresponds to a certain speaker.
   * @param value 0, 1, or -1
   */
  public void setBalance(float value)
  {
    balanceCtrl.setValue(value);
  }

  private void play()
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

    }
    else if (type == LineEvent.Type.STOP)
    {
      playFinished = true;
    }

  }

  /**
   * Plays audio clip over and over until stopped.
   */
  public void playLooped()
  {
    audioClip.loop(Clip.LOOP_CONTINUOUSLY);
    play();
  }

  /**
   * Tells clip to stop playing.
   */
  public void stop()
  {
    audioClip.stop();
  }

}
