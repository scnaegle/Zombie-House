import java.awt.image.BufferedImage;
import java.util.Random;

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


  public MasterZombie(double speed, double smell, double decision_rate,
                      Location location)
  {
    this(location);
    this.defined_speed = speed;
    this.current_speed = speed;
    this.smell = smell;
    this.decision_rate = decision_rate;
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

}
