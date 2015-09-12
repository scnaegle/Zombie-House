import java.util.Random;

/**
 * Sets up everything that a zombie has, such as smell, speed, decision rate and
 * how the sprite will move. Each zombie is an object so it extends GameObject
 * which implements Object2D. Each zombie also needs to track the human, so it
 * implements Humanoid which lets it get the human's current location.
 */
public abstract class Zombie extends GameObject implements Humanoid
{
  private final int FPS = 60;
  private final double MOVE_MULTIPLIER = (double)GUI.tile_size / FPS;
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

 // lookForLight();
 // moveToPlayer();
 // roamStraight lines();
      // -for this method we will need to make sure that we are not trying to move
      //  the zombies one tile at a time but in a continuios movement, i'm thinking we have them travel in a
      // straight lines will be eaier than diagonol, but I could ver possibly be wrong
      // maybe having them traverse for a certain amount of time in a gerneral


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
        animation = moveUp;
      } else if (y_move < 0) {
        animation = moveDown;
      }
    }
  }

  protected boolean smellPlayer(Humanoid player) {
    if (getDistance((Object2D)player) <= smell * GUI.tile_size) {
      return true;
    }
    return false;
  }

  protected void chooseDirection() {
    // This is a placeholder that should be overridden.
  }

  public void update() {
    frame++;
    if (frame >= decision_rate * FPS) {
      frame = 0;
      chooseDirection();
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
