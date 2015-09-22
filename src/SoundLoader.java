
/**
 * This class loads in audio in the form of .wav files and makes them playable.
 * Also decides which speaker to play sounds from.
 */
import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
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

//      System.out.println(
//          audioClip.isControlSupported(FloatControl.Type.BALANCE));

      //makeBalanceControlled();

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
    hitObst.makeBalanceControlled();
    decideBalance(hitObst);
    hitObst.play();
  }

  public static void playZWalk()
  {
    zWalk.makeBalanceControlled();

    decideBalance(zWalk);

    zWalk.playLooped();
  }

  private static void decideBalance(SoundLoader sound)
  {
    if (Zombie.toTheLeftOfPlayer)
    {
      sound.setBalance(-1f);
    }
    else if (Zombie.toTheRightOfPlayer)
    {
      sound.setBalance(1f);
    }
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

//  public static void main(String[] args)
//  {
//
//    loadSounds();
//    zWalk.makeBalanceControlled();
//    zWalk.setBalance(1f);
//    playZWalk();
//    while(true);
//
//
//  }
}
