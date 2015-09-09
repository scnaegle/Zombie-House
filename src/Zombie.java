/**
 * Created by sean on 9/8/15.
 */
public abstract class Zombie implements Humanoid
{
  protected double speed = .5;
  protected double decision_rate = 2.0;
  protected double smell = 7.0;
  protected Heading heading = Heading.STILL;
  protected Location location;

  public Zombie(Location location) {
    this.location = location;
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

  @Override
  public Location getLocation()
  {
    return location;
  }

  @Override
  public boolean intersects(Humanoid other)
  {
    if (location.equals(other.getLocation())) {
      return true;
    }
    return false;
  }
}
