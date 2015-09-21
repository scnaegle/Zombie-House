import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 *
 */
public class Sprite
{
  private BufferedImage sprite_sheet;
  private int tile_size = 80;

  public Sprite(String sprite, int tile_size)
  {
    sprite_sheet = loadSprite(sprite);
    this.tile_size = tile_size;
  }

  public BufferedImage loadSprite(String file)
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

  public BufferedImage getSprite(int row, int col) {
    // Set back the row and col so that the index starts at 1
    col--;
    row--;
    return sprite_sheet.getSubimage(col * tile_size, row * tile_size, tile_size,
        tile_size);
  }

}
