import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

/**
 * This class loads in audio in the form of .wav files and makes them playable
 */
public class SoundLoader implements LineListener
{
  boolean playFinished;
  //String audioFilePath;
  Thread thread;
  Clip audioClip;

  public SoundLoader(String path)
  {
    InputStream inputStream =
        ClassLoader.getSystemResourceAsStream("resources/" + path);

    System.out.println(inputStream);
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
