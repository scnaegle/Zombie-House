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
    this.sight = playerSight;
    this.hearing = playerHearing;
    this.speed = playerSpeed;
    this.stamina = playerStamina;
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
