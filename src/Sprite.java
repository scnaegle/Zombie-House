import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class Sprite
{

  private static final int TILE_SIZE = 80;
  private static BufferedImage spriteSheet;

  public static BufferedImage loadSprite(String file)
  {

    BufferedImage sprite = null;

    try
    {
      sprite =
          ImageIO.read(Sprite.class.getResource("resources/" + file + ".png"));
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return sprite;
  }

  public static BufferedImage getSprite(String sprite, int row, int col)
  {

    if (spriteSheet == null)
    {
      spriteSheet = loadSprite(sprite);
    }

    return spriteSheet.getSubimage((col * TILE_SIZE) - TILE_SIZE,
        (row * TILE_SIZE) - TILE_SIZE,
        TILE_SIZE, TILE_SIZE);
  }

}
