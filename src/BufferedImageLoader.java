import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader
{
  static BufferedImage[] walking = new BufferedImage[10];
  static BufferedImage[] running = new BufferedImage[10];
  BufferedImage player;

  /**
   * Loads in the sprite sheet for the player.
   *
   * @param path
   * @return
   * @throws IOException
   */
  public BufferedImage loadPlayerSprite(String path) throws IOException
  {
    player = ImageIO.read(getClass().getResource(path));
    return player;
  }

  /**
   * Grabs exact 80x80 pixel image from sprite sheet.
   *
   * @param col
   * @param row
   * @param width
   * @param height
   * @return
   */
  public BufferedImage grabPlayerImage(int row, int col, int width, int height,
                                       BufferedImage img)
  {
    img = player.getSubimage((col * 80) - 80, (row * 80) - 80,
        width, height);
    return img;
  }

  public void initPlayerSprite()
  {
    //BufferedImageLoader loader = new BufferedImageLoader();
    try
    {
      player = loadPlayerSprite("resources/pWalk.png");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    walking[1] = grabPlayerImage(1, 1, GUI.PIXELS, GUI.PIXELS, player);
    walking[2] = grabPlayerImage(1, 2, GUI.PIXELS, GUI.PIXELS, player);
    walking[3] = grabPlayerImage(1, 3, GUI.PIXELS, GUI.PIXELS, player);
    walking[4] = grabPlayerImage(1, 4, GUI.PIXELS, GUI.PIXELS, player);
    walking[5] = grabPlayerImage(1, 5, GUI.PIXELS, GUI.PIXELS, player);
    walking[6] = grabPlayerImage(1, 6, GUI.PIXELS, GUI.PIXELS, player);
    walking[7] = grabPlayerImage(1, 7, GUI.PIXELS, GUI.PIXELS, player);

  }
}
