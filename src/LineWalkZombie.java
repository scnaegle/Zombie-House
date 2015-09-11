import java.awt.image.BufferedImage;

/**
 * Sets up line walk zombies and their sprites.
 */
public class LineWalkZombie extends Zombie
{
  BufferedImage[] down = initDown();
  BufferedImage[] left = initLeft();
  BufferedImage[] right = initRight();
  BufferedImage[] up = initUp();
  public LineWalkZombie(Location location) {
    super(location);
    moveDown = new Animation(down, 5);
    moveLeft = new Animation(left, 5);
    moveRight = new Animation(right, 5);
    moveUp = new Animation(up, 5);
    animation = moveLeft;
    animation.start();
  }

  private BufferedImage[] initDown()
  {
    BufferedImage down[] = {sprite.getSprite(5, 7),
        sprite.getSprite(5, 8),
        sprite.getSprite(5, 9),};
    return down;
  }

  private BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(6, 7),
        sprite.getSprite(6, 8),
        sprite.getSprite(6, 9),};
    return left;
  }

  private BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(7, 7),
        sprite.getSprite(7, 8),
        sprite.getSprite(7, 9),};
    return right;
  }

  private BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(8, 7),
        sprite.getSprite(8, 8),
        sprite.getSprite(8, 9),};
    return up;
  }
}
