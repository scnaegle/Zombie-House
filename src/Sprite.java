import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 *
 */
public class Sprite {

  private static BufferedImage spriteSheet;
  private static final int TILE_SIZE = 60;

  public static BufferedImage loadSprite(String file) {

    BufferedImage sprite = null;

    try {
      sprite = ImageIO.read(Sprite.class.getResource("resources/" + file + ".png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return sprite;
  }

  public static BufferedImage getSprite(String sprite, int xGrid, int yGrid) {

    if (spriteSheet == null) {
      spriteSheet = loadSprite(sprite);
    }

    return spriteSheet.getSubimage(xGrid * TILE_SIZE, yGrid * TILE_SIZE, TILE_SIZE, TILE_SIZE);
  }

}
