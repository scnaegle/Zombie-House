/**
 *
 */
public class Player extends GameObject implements Humanoid
{
  int sight;
  int hearing;
  double speed;
  double stamina;
  double regen;
  Heading heading;
  Location location;


  /**
   * The player needs sight, hearing, speed, and stamina to start out with
   *
   * @param player_sight
   * @param player_hearing
   * @param player_speed
   * @param player_stamina
   */
  public Player(int player_sight, int player_hearing, double player_speed, double player_stamina)
  {
    this.sight = player_sight;       // perhaps we can make upgrades that can be picked
    // up for
    this.hearing = player_hearing;   // these things later, like flash light, and hearing
    // aids
    this.speed = player_speed;       // I imaginge it would be similar to the traps we
    // have to
    this.stamina = player_stamina;   // make
  }

  /*When not running, playerRegen  deltaTime is added
  to playerStamina up to a maximum of the original
  playerStamina attribute for the level.*/
  private double regenerate()
  {
    return regen;
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
  public void setLocation(Location new_location) {
    this.location = new_location;
  }

}
