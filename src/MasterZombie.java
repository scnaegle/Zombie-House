import java.awt.image.BufferedImage;

/**
 * Sets up the master zombie since his sprite is different, as well as
 * gives him a "special power". He extends the zombie class since he
 * must know everything the other zombie's do.
 */
public class MasterZombie extends Zombie
{

  BufferedImage[] down = initDown();
  BufferedImage[] left = initLeft();
  BufferedImage[] right = initRight();
  BufferedImage[] up = initUp();

  public MasterZombie(Location location) {
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
    BufferedImage down[] = {sprite.getSprite(5, 4),
        sprite.getSprite(5, 5),
        sprite.getSprite(5, 6),};
    return down;
  }

  private BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(6, 4),
        sprite.getSprite(6, 5),
        sprite.getSprite(6, 6),};
    return left;
  }

  private BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(7, 4),
        sprite.getSprite(7, 5),
        sprite.getSprite(7, 6),};
    return right;
  }

  private BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(8, 4),
        sprite.getSprite(8, 5),
        sprite.getSprite(8, 6),};
    return up;
  }
}
