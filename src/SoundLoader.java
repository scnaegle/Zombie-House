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


  void play(String audioFilePath)
  {
    thread = new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        InputStream inputStream =
            getClass().getResourceAsStream("resources/" + audioFilePath);

        try
        {
          AudioInputStream audioStream =
              AudioSystem.getAudioInputStream(inputStream);
          AudioFormat format = audioStream.getFormat();
          DataLine.Info info = new DataLine.Info(Clip.class, format);
          audioClip = (Clip) AudioSystem.getLine(info);
          audioClip.addLineListener(SoundLoader.this);
          audioClip.open(audioStream);
          audioClip.start();

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
    });
    thread.start();

  }

//  public static AudioStream loadSound(String file)
//  {
//    InputStream sound;
//    AudioStream audio = null;
//    try
//    {
//      sound = ClassLoader.getSystemClassLoader().getResourceAsStream(
//          "resources/" + file);
//      audio = new AudioStream(sound);
//      // AudioPlayer.player.start(audio);
//    }
//    catch (Exception e)
//    {
//      e.printStackTrace();
//    }
//
//    return audio;
//  }

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


  public void stop()
  {
    audioClip.stop();
  }
}
