import java.awt.image.BufferedImage;

/**
 * Player class sets up the player, loads the walking/ running sprites and
 * sets up the animation depending on the speed of the player.
 */
public class Player extends GameObject implements Humanoid
{
  private final int FPS = 60;
  private final double MOVE_MULTIPLIER = (double)GUI.tile_size / FPS;
  private final double STAMINA_PER_SEC = 1.0;
  private final double STAMINA_STEP = STAMINA_PER_SEC / FPS;
  private int sight = 5;
  private int hearing = 10;
  private double current_speed = 1.0;
  private double defined_speed = 1.0;
  double max_stamina = 5;
  double stamina = 5;
  double regen = .2;
  Heading heading;
  private Sprite stand_sprite = new Sprite("pStand");
  private BufferedImage[] still = {stand_sprite.getSprite(1, 1)};
  private BufferedImage[] walking = initPlayerSpriteWalk();
  private BufferedImage[] running = initPlayerSpriteRun();
  private Animation walk = new Animation(walking, 5);
  private Animation run = new Animation(running, 5);
  private Animation stand = new Animation(still, 5);
  Animation animation = walk;

  public Player(Location location) {
    this.location = location;
    //animation.start();
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
    this.defined_speed = player_speed;
    this.current_speed = player_speed;       // I imaginge it would be similar to the traps we
    // have to
    this.max_stamina = player_stamina;
    this.stamina = player_stamina;   // make
  }

  public BufferedImage[] initPlayerSpriteWalk()
  {

    Sprite sprite = new Sprite("pWalk");

    BufferedImage walking[] = {sprite.getSprite(1, 2),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 6),
        sprite.getSprite(1, 7)};
    return walking;
  }

  public BufferedImage[] initPlayerSpriteRun()
  {

    Sprite sprite = new Sprite("pRun");
    BufferedImage running[] = {sprite.getSprite(1, 1),
        sprite.getSprite(1, 2),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 6),
        sprite.getSprite(1, 7),
        sprite.getSprite(1, 8),
        sprite.getSprite(1, 9),
        sprite.getSprite(1, 10),
        sprite.getSprite(2, 1),
        sprite.getSprite(2, 2),
        sprite.getSprite(2, 3),
        sprite.getSprite(2, 4),
        sprite.getSprite(2, 5),
        sprite.getSprite(2, 6),
        sprite.getSprite(2, 7),
        sprite.getSprite(2, 8),
        sprite.getSprite(2, 9),
        sprite.getSprite(2, 10),
        sprite.getSprite(3, 1),
        sprite.getSprite(3, 2)};

    return running;
  }

  /*When not running, playerRegen  deltaTime is added
  to playerStamina up to a maximum of the original
  playerStamina attribute for the level.*/
  private void regenerate()
  {
    stamina += STAMINA_STEP;
    Math.min(stamina, max_stamina);
    //UHHHH Is stanima becoming too large, should it be reset to 5?
  }

  @Override
  public double getSpeed()
  {
    return current_speed;
  }

  public void setSpeed(double speed) {
    this.current_speed = speed;
  }

  @Override
  public Heading getHeading()
  {
    return heading;
  }


  public void setHeading(Heading heading) {
    this.heading = heading;
  }

  @Override
  public void setLocation(Location new_location) {
    this.location = new_location;
  }

  /**
   * Tells the sprite how to move based on the heading we give it.
   * Heading is controlled by keyboard arrows.
   */
  public void move() {
//    System.out.println("IN MOVE!!!!");
//    System.out.println("heading: " + heading);
//    System.out.println("NONE heading: " + Heading.NONE);
    if (heading != Heading.NONE) {
//      System.out.println("moving....");
//      location.x += (current_speed * Math.cos(heading.getDegrees())) * MOVE_MULTIPLIER;
//      location.y -= (current_speed * Math.sin(heading.getDegrees())) * MOVE_MULTIPLIER;
      location.x += (current_speed * heading.getXMovement()) * MOVE_MULTIPLIER;
      location.y += (current_speed * heading.getYMovement()) * MOVE_MULTIPLIER;
    }
  }

  public void update() {
    move();
    if (heading.equals(Heading.NONE)) {
      regenerate();
      current_speed = 0;
      animation = stand;
    } else if (current_speed > defined_speed && stamina > 0) {
      animation = run;
      stamina -= STAMINA_STEP;
    } else {
      regenerate();
      animation = walk;
      current_speed = 1;
    }
    animation.start();
    animation.update();
  }
}
