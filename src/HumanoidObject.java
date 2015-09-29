/**
 * Interface that Humanoid uses to override methods.
 */
public interface HumanoidObject
{

  /**
   * Get the current speed of the zombie
   * @return current speed
   */
  double getSpeed();

  /**
   * Get the current heading of the zombie
   * @return current heading
   */
  Heading getHeading();

  /**
   * Get the location of a zombie
   * @return location
   */
  Location getLocation();

  /**
   * Set the location
   * @param new_location New location object
   */
  void setLocation(Location new_location);


}
