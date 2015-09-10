import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class BufferedImageLoader
{
  //static BufferedImage[] walking = new BufferedImage[10];
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


  public BufferedImage[] initPlayerSpriteWalk()
  {
    try
    {
      player = loadPlayerSprite("resources/pWalk.png");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    BufferedImage walking[] = {Sprite.getSprite("pWalk", 1, 2),
        Sprite.getSprite("pWalk", 1, 3),
        Sprite.getSprite("pWalk", 1, 4),
        Sprite.getSprite("pWalk", 1, 5),
        Sprite.getSprite("pWalk", 1, 6),
        Sprite.getSprite("pWalk", 1, 7)};
    return walking;
  }

  public BufferedImage[] initPlayerSpriteRun()
  {
    try
    {
      player = loadPlayerSprite("resources/pWalk.png");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }


    BufferedImage walking[] = {Sprite.getSprite("pWalk", 1, 2),
        Sprite.getSprite("pWalk", 1, 3),
        Sprite.getSprite("pWalk", 1, 4),
        Sprite.getSprite("pWalk", 1, 5),
        Sprite.getSprite("pWalk", 1, 6),
        Sprite.getSprite("pWalk", 1, 7)};
    return walking;
  }
}
