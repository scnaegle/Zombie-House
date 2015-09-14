import java.awt.image.BufferedImage;

/**
 * Player class sets up the player, loads the walking/ running sprites and
 * sets up the animation depending on the speed of the player.
 */
public class Player extends Humanoid implements HumanoidObject
{
  private final double STAMINA_PER_SEC = 1.0;
  private final double STAMINA_STEP = STAMINA_PER_SEC / GamePanel.FPS;
  public boolean isRunning;
  public boolean isWalking;
  public boolean isStill;
  double max_stamina = 5;
  double stamina = 5;
  double regen = .2;
  private int sight = 5;
  private int hearing = 10;
  private Sprite stand_sprite = new Sprite("pStand");
  private BufferedImage[] still = {stand_sprite.getSprite(1, 1)};
  private BufferedImage[] walking = initPlayerSpriteWalk();
  private BufferedImage[] running = initPlayerSpriteRun();
  private Animation walk = new Animation(walking, 2);
  Animation animation = walk;
  private Animation run = new Animation(running, 2);
  private Animation stand = new Animation(still, 5);
  private SoundLoader walkSound;
  private SoundLoader runSound;
  private SoundLoader sound;


  public Player(Location location) {
    this.location = location;
    this.width = GUI.tile_size;
    this.height = GUI.tile_size;
  }

  public Player(int width, int height, Location location) {
    this(location);
    this.width = width;
    this.height = height;
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
    this.width = GUI.tile_size;
    this.height = GUI.tile_size;
  }

  public Player(int sight, int hearing, double speed, double stamina, int width, int height, Location location) {
    this(sight, hearing, speed, stamina);
    this.width = width;
    this.height = height;
    this.location = location;
  }

  public int getSight()
  {
    return sight;
  }

  public int getHearing()
  {
    return hearing;
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

  /**
   * Tells the sprite how to move based on the heading we give it.
   * Heading is controlled by keyboard arrows.
   */
  public void move(Location next_location) {
    location = next_location;
  }

  public void update(GameMap map) {

    Location next_location = getNextLocation();
    if (!heading.equals(Heading.NONE) && !hitWall(map, next_location)) {
      move(next_location);
    }


    if(heading.equals(Heading.NONE)) {
      regenerate();
      current_speed = 0;
      animation = stand;
    } else if (current_speed > defined_speed && stamina > 0) {
      animation = run;
      stamina -= STAMINA_STEP;
    } else {
      regenerate();
      animation = walk;
      sound = walkSound;
      current_speed = 1.0;
    }

    if (isStill)
    {
      stopSound();
    }
    if (!isWalking && isRunning)
    {
      sound = runSound;
      playSound();
    }
    else if (!isRunning && isWalking)
    {
      sound = walkSound;
      playSound();
    }

    animation.start();
    animation.update();


  }

  // Called when 'r' is pressed so that the speed stays at 2 times what it was
  // instead of updating when r is held down.
  public void setRunning()
  {
    current_speed = 2 * defined_speed;
  }

  public void setWalking() {
    this.current_speed = defined_speed;
  }

  public double getStamina()
  {
    return stamina;
  }

  public void loadSounds()
  {
    runSound = new SoundLoader("pRunSound.wav");
    walkSound = new SoundLoader("pWalkSound.wav");
    sound = walkSound;
  }

  public void playSound()
  {
    sound.playLooped();
  }

  public void stopSound()
  {
    sound.stop();
  }

}
