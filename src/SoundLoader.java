import sun.audio.AudioStream;

import java.io.InputStream;

/**
 * This class loads in audio in the form of .wav files and makes them playable
 */
public class SoundLoader
{

  public static AudioStream loadSound(String file)
  {
    InputStream sound;
    AudioStream audio = null;
    try
    {
      sound = ClassLoader.getSystemClassLoader().getResourceAsStream(
          "resources/" + file);
      audio = new AudioStream(sound);
      // AudioPlayer.player.start(audio);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }

    return audio;
  }
}
