import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * used to make the different sprites that the game uses like the zombies and bob
 * We used sprite sheets to make what we needed
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

  /**
   * this is pretty cool because we use peices of a picture depending on what
   * sprite it is to set pictures that should be moving essentally putting many
   * pictures together to make it look like the sprites are moving.
   * @param row
   * @param col
   * @return
   */
  public BufferedImage getSprite(int row, int col) {
    // Set back the row and col so that the index starts at 1
    col--;
    row--;
    return sprite_sheet.getSubimage(col * tile_size, row * tile_size, tile_size,
        tile_size);
  }

}
