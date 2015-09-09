/**
 * Created by sean on 9/8/15.
 */
public interface Humanoid
{

  /**
   * Get the current speed of the zombie
   * @return current speed
   */
  int getSpeed();

  /**
   * Get the current heading of the zombie
   * @return current heading
   */
  Heading getHeading();

  /**
   * Get the location of a zombie
   * @return location
   */
  double getLocation();

  /**
   * Does this zombie intersect another? Check if this zombie is in the same location as another zombie.
   * @param other
   * @return
   */
  boolean intersects(Humanoid other);
}
