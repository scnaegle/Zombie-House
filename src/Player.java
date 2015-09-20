import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Player class sets up the player, loads the walking/ running sprites and
 * sets up the animation depending on the speed of the player.
 */
public class Player extends Humanoid implements HumanoidObject
{
  private final double STAMINA_PER_SEC = 1.0;
  private final double STAMINA_STEP = STAMINA_PER_SEC / GamePanel.FPS;
  private final double PICKUP_TIME = 5.0;
  private final double PICKUP_FRAMES = PICKUP_TIME * GamePanel.FPS;
  public boolean isRunning = false;
  public boolean isWalking = false;
  public boolean isStill = true;
  public boolean is_putting_down = false;
  public boolean playerDied = false;
  protected int frame = 0;
  double max_stamina = 5;
  double stamina = 5;
  double regen = .2;
  boolean is_picking_up = false;
  private int sight = 5;
  private int hearing = 10;
  private int fire_traps = 0;
  private FireTrap pickup_trap;
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
  private SoundLoader scream;
  private ArrayList<FireTrap> traps = new ArrayList<>();


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

  public Player(int sight, int hearing, double speed, double stamina,
                double regen, int width, int height, Location location)
  {
    this(sight, hearing, speed, stamina);
    this.width = width;
    this.height = height;
    this.location = location;
    this.regen = regen;
  }

  public int getSight()
  {
    return sight;
  }

  public int getHearing()
  {
    return hearing;
  }

  public int getFire_traps()
  {
    return fire_traps;
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
    stamina = Math.min(stamina, max_stamina);
  }

  /**
   * Tells the sprite how to move based on the heading we give it.
   * Heading is controlled by keyboard arrows.
   */
  public void move(Location next_location) {
    location = next_location;
  }

  public void update(GameMap map)
  {
    if (is_picking_up)
    {
      frame++;
      if (frame >= PICKUP_FRAMES)
      {
        fire_traps++;
        frame = 0;
        is_picking_up = false;
        map.traps.remove(pickup_trap);
        traps.add(pickup_trap); //Adds trap to player's list of held traps
      }
    }
    else if (is_putting_down)
    {
      frame++;
      if (frame >= PICKUP_FRAMES)
      {
        fire_traps--;
        frame = 0;
        is_putting_down = false;
        FireTrap t = traps.remove(0);
        t.setNewLocation(location);
        map.traps.add(t);
      }
    }
    else
    {
//      Location next_location = getNextLocation();
      if (!heading.equals(Heading.NONE))
      {
        if (!hitWallInXDirection(map)) {
          moveX();
        }
        if (!hitWallInYDirection(map)) {
          moveY();
        }
      }
      if (heading.equals(Heading.NONE))
      {
        regenerate();
        current_speed = 0;
        animation = stand;
      }
      else if (current_speed > defined_speed && stamina > 0)
      {
        animation = run;
        stamina -= STAMINA_STEP;
      }
      else
      {
        regenerate();
        animation = walk;
        current_speed = defined_speed;
      }

      // Decides which sound to play based on state of player
      if (isStill)
      {
        stopSound();
      }
      else if (isRunning)
      {
        sound = runSound;
        playSound();
      }
      else if (isWalking)
      {
        sound = walkSound;
        playSound();
      }

      animation.start();
      animation.update();


    }
  }

  // Called when 'r' is pressed so that the speed stays at 2 times what it was
  // instead of updating when r is held down.
  public void setRunning()
  {
    current_speed = 2 * defined_speed;
    isRunning = true;
  }

  public void setWalking() {
    this.current_speed = defined_speed;
    isWalking = true;
  }

  public double getStamina()
  {
    return stamina;
  }

  public void loadSounds()
  {
    runSound = new SoundLoader("pRunSound.wav");
    walkSound = new SoundLoader("pWalkSound.wav");
    scream = new SoundLoader("pScream.wav");
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

  public void pickupFireTrap(FireTrap trap)
  {
    is_picking_up = true;
    pickup_trap = trap;
    frame = 0;
  }

  public double getRegenRate()
  {
    return regen;
  }

  public Tile getFootTile(GameMap map) {
    int row = (int)(location.y + GUI.tile_size) / GUI.tile_size;
    int col = (int)(location.x + (width / 2)) / GUI.tile_size;
    return map.getTile(row, col);
  }


}
