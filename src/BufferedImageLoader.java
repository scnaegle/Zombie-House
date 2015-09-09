import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader
{
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
  public BufferedImage grabPlayerImage(int col, int row, int width, int height)
  {
    BufferedImage img = player.getSubimage((col * 80) - 80, (row * 80) - 80,
        width, height);
    return img;
  }
}
