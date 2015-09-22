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
  @Override
  public void update(GameMap map, HumanoidObject player)
  {
    frame++;
    if (frame >= decision_rate * GamePanel.FPS) {
      frame = 0;
      chooseDirection(player);
    }
//    System.out.println("heading: " + heading);
    if (!hitWallInXDirectionMaster(map)) {
      moveX();
    }
    if (!hitWallInYDirectionMaster(map)) {
      moveY();
    }

 ///   if(touchingZombie(zombie))
    {

    }
    if (bitesPlayer(player))
    {
      setBite();
      bitPlayer = true;
    }

    //Sees if zombie is in player hearing's range
    double range = ((Player) player).getHearing() * GUI.tile_size;
    if (getDistance((Object2D) player) <= range)
    {
      //System.out.println(Math.round(getDistance((Object2D) player)));
      SoundLoader.playZWalk();
    }
//    else
//    {
//      System.out.println("  Can't hear zombie anymore");
//      stopSound();
//    }


    Location next_location = getNextLocation();
    //Sees is zombie is in 2*hearing range and hits wall
    if (getDistance((Object2D) player) <= 2 * range &&
        hitWall(map, next_location))
    {
//      System.out.println("Zombie hit wall");
      SoundLoader.playHitObst();
    }

    determineAnimation();
    animation.start();
    animation.update();
  }


}
