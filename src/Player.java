import java.awt.image.BufferedImage;

/**
 *
 */
public class Player extends GameObject implements Humanoid
{
  private final int FPS = 60;
  private final double MOVE_MULTIPLIER = 1.0 / FPS;
  private final double STAMINA_PER_SEC = 1.0;
  private final double STAMINA_STEP = STAMINA_PER_SEC / FPS;
  int sight = 5;
  int hearing = 10;
  double speed = 1.0;
  double max_stamina = 5;
  double stamina = 5;
  double regen = .2;
  Heading heading;
  BufferedImageLoader loader = new BufferedImageLoader();
  private BufferedImage[] walking = loader.initPlayerSpriteWalk();
//  private BufferedImage[] running = loader.initPlayerSpriteRun();
  private Animation walk = new Animation(walking, 5);
//  private Animation run = new Animation(running, 5);
  Animation animation = walk;


  public Player(Location location) {
    this.location = location;
    animation.start();
  }

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
    this.max_stamina = player_stamina;
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

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  @Override
  public Heading getHeading()
  {
    return heading;
  }

//  public void setHeading(String heading_str) {
//    switch(heading_str) {
//      case "up":
//        this.heading = Heading.NORTH;
//      case "down":
//        this.heading = Heading.
//    }
//  }

  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  @Override
  public void setLocation(Location new_location) {
    this.location = new_location;
  }

  public void move() {
    location.x += (speed * Math.cos(heading.getDegrees())) * MOVE_MULTIPLIER;
    location.y += (speed * Math.sin(heading.getDegrees())) * MOVE_MULTIPLIER;
    if (speed > 1 && stamina > 0) {
//      animation = run;
      stamina -= STAMINA_STEP;
    } else {
      stamina += STAMINA_STEP;
      Math.min(stamina, max_stamina);
      animation = walk;
      speed = 1;
    }
    animation.update();
  }

}
