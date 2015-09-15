import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * Sets up the random walk zombies and their animation sprites
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
    this.heading = new Heading(Heading.NONE);
    animation = moveLeft;
    animation.start();
  }

  @Override
  protected void chooseDirection(HumanoidObject player) {
//    System.out.println("Choosing new direction");
    Random rand = new Random();
//    System.out.println("old heading: " + heading.toString());
    // Unless there is a wall in that direction
    heading.setDegrees(rand.nextInt(360));
//    System.out.println("new heading: " + heading.toString());
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
