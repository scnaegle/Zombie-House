import java.awt.image.BufferedImage;

/**
 * Created by sean on 9/8/15.
 */
public class RandomWalkZombie extends Zombie
{
  BufferedImage[] down = initDown();
  BufferedImage[] left = initLeft();
  BufferedImage[] right = initRight();
  BufferedImage[] up = initUp();



  public RandomWalkZombie(Location location) {
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
    BufferedImage down[] = {sprite.getSprite(1, 7),
        sprite.getSprite(1, 8),
        sprite.getSprite(1, 9),};
    return down;
  }

  public BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(2, 7),
        sprite.getSprite(2, 8),
        sprite.getSprite(2, 9),};
    return left;
  }

  public BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(3, 7),
        sprite.getSprite(3, 8),
        sprite.getSprite(3, 9),};
    return right;
  }

  public BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(4, 7),
        sprite.getSprite(4, 8),
        sprite.getSprite(4, 9),};
    return up;
  }
}
