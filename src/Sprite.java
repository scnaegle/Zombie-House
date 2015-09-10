import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class Sprite
{
  private static BufferedImage sprite_sheet;
  private static final int TILE_SIZE = 80;

  public static BufferedImage loadSprite(String file)
  {

    BufferedImage sprite = null;

    try
    {
      sprite = ImageIO.read(Sprite.class.getResource("resources/" + file + ".png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return sprite;
  }

  public static BufferedImage getSprite(String sprite, int row, int col) {

    if (sprite_sheet == null) {
      sprite_sheet = loadSprite(sprite);
    }

    // Set back the row and col so that the index starts at 1
    col--;
    row--;
    return sprite_sheet.getSubimage(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
  }

}
