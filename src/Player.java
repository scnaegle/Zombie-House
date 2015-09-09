import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Player
{
  int sight;
  int hearing;
  double speed;
  double stamina;
  double regen;
  BufferedImage player;

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

  private BufferedImage loadPlayerSprite(String path) throws IOException
  {
    player = ImageIO.read(getClass().getResource(path));
    return player;
  }
}
