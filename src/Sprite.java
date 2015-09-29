import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Used to make the different sprites that the game uses like the zombies and
 * player.
 *
 */
public class Sprite
{
  private BufferedImage sprite_sheet;
  private int tile_size = 80;

  /**
   * When making a new sprite, it needs the name of the file and how big each
   * sprite needs to be.
   *
   * @param sprite    file name
   * @param tile_size 80 pixels
   */
  public Sprite(String sprite, int tile_size)
  {
    sprite_sheet = loadSprite(sprite);
    this.tile_size = tile_size;
  }

  /**
   * Loads in sprite from a file
   * @param file Sprite sheet .png
   * @return a buffered image
   */
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
   * Gets the subimage of the sprite sheet by moving over a certain amount of
   * pixels
   * each time and grabbing that image.
   * @param row Row of sheet
   * @param col Col of sheet
   * @return buffered image
   */
  public BufferedImage getSprite(int row, int col) {
    // Set back the row and col so that the index starts at 1
    col--;
    row--;
    return sprite_sheet.getSubimage(col * tile_size, row * tile_size, tile_size,
        tile_size);
  }

}
