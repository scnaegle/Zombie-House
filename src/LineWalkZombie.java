import java.awt.image.BufferedImage;
import java.util.Random;

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
    Random rand = new Random();
    switch(rand.nextInt(4)) {
      case 0:
        this.heading = new Heading(Heading.NORTH);
        break;
      case 1:
        this.heading = new Heading(Heading.WEST);
        break;
      case 2:
        this.heading = new Heading(Heading.EAST);
        break;
      case 3:
        this.heading = new Heading(Heading.SOUTH);
    }
  }

  public LineWalkZombie(double speed, double smell, double decision_rate, Location location) {
    this(location);
    this.defined_speed = speed;
    this.current_speed = speed;
    this.smell = smell;
    this.decision_rate = decision_rate;
  }

  @Override
  protected void chooseDirection(HumanoidObject player) {
//    System.out.println("Choosing Line Walk Zombie direction...");
    if (smellPlayer(player)) {
//      System.out.println("BRAAAAAIIINNNNNNZZZZ");
      double angle = getDirectionTo((Object2D)player);
//      System.out.println("new angle: " + angle);
      heading.setDegrees(angle);
    } // else if hit hall then choose random direction
  }

  private BufferedImage[] initDown()
  {
    BufferedImage down[] = {sprite.getSprite(1, 7),
        sprite.getSprite(1, 8),
        sprite.getSprite(1, 9),};
    return down;
  }

  private BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(2, 7),
        sprite.getSprite(2, 8),
        sprite.getSprite(2, 9),};
    return left;
  }

  private BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(3, 7),
        sprite.getSprite(3, 8),
        sprite.getSprite(3, 9),};
    return right;
  }

  private BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(4, 7),
        sprite.getSprite(4, 8),
        sprite.getSprite(4, 9),};
    return up;
  }
}
