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


  public void move() {
//    double increment_x = (speed * Math.cos(heading.getDegrees())) * MOVE_MULTIPLIER;
//    double increment_y = (speed * Math.sin(heading.getDegrees())) * MOVE_MULTIPLIER;
    double increment_x = (speed * heading.getXMovement()) * MOVE_MULTIPLIER;
    double increment_y = (speed * heading.getYMovement()) * MOVE_MULTIPLIER;
    location.x += increment_x;
    location.y += increment_y;

    if(increment_x > increment_y) {
      if (increment_x > 0) {
        animation = moveRight;
      } else if (increment_x < 0){
        animation = moveLeft;
      }
    } else {
      if (increment_y > 0) {
        animation = moveUp;
      } else if (increment_y < 0){
        animation = moveDown;
      }
    }
    animation.start();
    animation.update();
  }

  public void update() {
    move();
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
