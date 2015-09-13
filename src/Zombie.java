import java.util.Random;

/**
 * Sets up everything that a zombie has, such as smell, speed, decision rate and
 * how the sprite will move. Each zombie is an object so it extends GameObject
 * which implements Object2D. Each zombie also needs to track the human, so it
 * implements Humanoid which lets it get the human's current location.
 */
public abstract class Zombie extends GameObject implements Humanoid
{
  private final double MOVE_MULTIPLIER = (double)GUI.tile_size / GamePanel.FPS;
  protected int frame = 0;

  protected double decision_rate = 2.0;
  protected double smell = 7.0;
  protected Heading heading = Heading.NONE;
  protected double speed = .5;
  protected Sprite sprite = new Sprite("ZombieSheet");
  Animation moveDown;
  Animation moveLeft;
  Animation moveRight;
  Animation moveUp;
  Animation animation = moveLeft;


  public Zombie(Location location) {
    this.location = location;
  }

  protected void move() {
//    location.x += (speed * Math.cos(heading.getDegrees())) * MOVE_MULTIPLIER;
//    location.y += (speed * Math.sin(heading.getDegrees())) * MOVE_MULTIPLIER;
    location.x += (speed * heading.getXMovement()) * MOVE_MULTIPLIER;
    location.y += (speed * heading.getYMovement()) * MOVE_MULTIPLIER;
  }

  protected void determineAnimation() {
    double x_move = heading.getXMovement();
    double y_move = heading.getYMovement();
    if(x_move > y_move) {
      if (x_move > 0) {
        animation = moveRight;
      } else if (x_move < 0){
        animation = moveLeft;
      }
    } else {
      if (y_move > 0) {
        animation = moveDown;
      } else if (y_move < 0) {
        animation = moveUp;
      }
    }
  }

  protected boolean smellPlayer(Humanoid player) {
    if (getDistance((Object2D)player) <= smell * GUI.tile_size) {
      return true;
    }
    return false;
  }

  protected void chooseDirection(Humanoid player) {
    // This is a placeholder that should be overridden.
  }

  public void update(GameMap map, Humanoid player) {
    frame++;
    if (frame >= decision_rate * GamePanel.FPS) {
      frame = 0;
      chooseDirection(player);
    }
    move();
    determineAnimation();
    animation.start();
    animation.update();
  }

  @Override
  public double getSpeed()
  {
    return speed;
  }

  @Override
  public Heading getHeading()
  {
    return heading;
  }


  public void setHeading(Heading heading)
  {
    this.heading = heading;
  }

  @Override
  public Location getLocation()
  {
    return location;
  }

  @Override
  public void setLocation(Location new_location)
  {
    this.location = new_location;
  }
}
