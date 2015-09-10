import java.awt.image.BufferedImage;

/**
 * Created by sean on 9/8/15.
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

  public BufferedImage[] initDown()
  {
    BufferedImage down[] = {sprite.getSprite(5, 7),
        sprite.getSprite(5, 8),
        sprite.getSprite(5, 9),};
    return down;
  }

  public BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(6, 7),
        sprite.getSprite(6, 8),
        sprite.getSprite(6, 9),};
    return left;
  }

  public BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(7, 7),
        sprite.getSprite(7, 8),
        sprite.getSprite(7, 9),};
    return right;
  }

  public BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(8, 7),
        sprite.getSprite(8, 8),
        sprite.getSprite(8, 9),};
    return up;
  }
}
