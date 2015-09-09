/**
 * Created by sean on 9/8/15.
 */
public abstract class Zombie extends GameObject implements Humanoid
{
  protected double decision_rate = 2.0;
  protected double smell = 7.0;
  protected Heading heading = Heading.STILL;
  protected double speed = .5;

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

  @Override
  public Location getLocation()
  {
    return location;
  }

  @Override
  public void setLocation(Location new_location) {
    this.location = new_location;
  }
}
