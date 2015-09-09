import java.awt.*;

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
   * @param playerSight
   * @param playerHearing
   * @param playerSpeed
   * @param playerStamina
   */
  Player(int playerSight, int playerHearing, double playerSpeed,
         double playerStamina)
  {
    this.sight = playerSight;       // perhaps we can make upgrades that can be picked up for
    this.hearing = playerHearing;   // these things later, like flash light, and hearing aids
    this.speed = playerSpeed;       // I imaginge it would be similar to the traps we have to
    this.stamina = playerStamina;   // make
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
