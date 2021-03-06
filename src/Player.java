/**
 * Player class sets up the player, loads the walkingRight/ runningRight
 * sprites and
 * sets up the animation depending on the speed of the player. Also checks
 * for picking up and putting down firetraps.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * The player class takes care of the animation, the movement, the current
 * direction the sprite is moving, and collisions.
 */
public class Player extends Humanoid implements HumanoidObject
{
  private final double STAMINA_PER_SEC = 1.0;
  private final double STAMINA_STEP = STAMINA_PER_SEC / GamePanel.FPS;
  private final int PICKUP_TIME = 5; //for firetraps
  private final int PICKUP_FRAMES = PICKUP_TIME * GamePanel.FPS; //for firetraps
  public boolean is_putting_down = false;
  public boolean is_picking_up = false;
  public boolean playerDied = false;
  public boolean playerExploded = false;
  protected int frame = 0;
  private double max_stamina;
  private double stamina;
  private double regen;
  private int sight;
  private int hearing;
  private int fire_traps = 0;
  private FireTrap pickup_trap;
  private Sprite stand_sprite = new Sprite("pStandRight", GUI.tile_size);
  private Sprite standL_sprite = new Sprite("pStandLeft", GUI.tile_size);
  private BufferedImage[] stillRight = {stand_sprite.getSprite(1, 1)};
  private BufferedImage[] stillLeft = {stand_sprite.getSprite(1, 1)};
  private BufferedImage[] walkingRight = initPlayerSpriteWalkRight();
  private BufferedImage[] walkingLeft = initPlayerSpriteWalkLeft();
  private BufferedImage[] runningRight = initPlayerSpriteRunRight();
  private BufferedImage[] runningLeft = initPlayerSpriteRunLeft();


  //All animations used for player
  private Animation walkRight = new Animation(walkingRight, 2);
  private Animation animation = walkRight;
  private Animation walkLeft = new Animation(walkingLeft, 2);
  private Animation runRight = new Animation(runningRight, 1);
  private Animation runLeft = new Animation(runningLeft, 1);
  private Animation standRight = new Animation(stillRight, 5);
  private Animation standLeft = new Animation(stillLeft, 5);
  private int diedFrame = 0;

  //Reference inner classes
  private StaminaBar stamina_bar;
  private PickupBar pickup_bar;


  public Player(Location location)
  {
    this.location = location;
    this.width = GUI.tile_size;
    this.height = GUI.tile_size;
  }

  /**
   * gets the sprites location on the map
   *
   * @param width
   * @param height
   * @param location
   */
  public Player(int width, int height, Location location)
  {
    this(location);
    this.width = width;
    this.height = height;
  }

  /**
   * The player needs sight, hearing, speed, and maxStamina to start out with
   * This is where all of it gets set, You have control over it from the GUI.
   *
   * @param player_sight
   * @param player_hearing
   * @param player_speed
   * @param player_stamina
   */
  public Player(int player_sight, int player_hearing, double player_speed,
                double player_stamina)
  {
    this.sight = player_sight;
    this.hearing = player_hearing;
    this.defined_speed = player_speed;
    this.current_speed = player_speed;
    this.max_stamina = player_stamina;
    this.stamina = player_stamina;
    this.width = GUI.tile_size;
    this.height = GUI.tile_size;
    stamina_bar = new StaminaBar(GUI.stamina);
  }

  /**
   * Constructor used at beginning setup to create player and tell him where
   * to start
   *
   * @param sight
   * @param hearing
   * @param speed
   * @param stamina
   * @param regen
   * @param width
   * @param height
   * @param location
   */
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

  //Loads sprites for walkingRight animation
  private BufferedImage[] initPlayerSpriteWalkRight()
  {

    Sprite sprite = new Sprite("pWalk", 80);

    BufferedImage walkingRight[] = {sprite.getSprite(1, 2),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 6),
        sprite.getSprite(1, 7)};
    return walkingRight;
  }

  private BufferedImage[] initPlayerSpriteWalkLeft()
  {
    Sprite sprite = new Sprite("pWalkLeft", 80);

    BufferedImage walkingLeft[] = {sprite.getSprite(1, 6),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 2),
        sprite.getSprite(1, 1)};
    return walkingLeft;
  }

  //Loads sprites for runningRight animation
  private BufferedImage[] initPlayerSpriteRunRight()
  {

    Sprite sprite = new Sprite("pRun", 80);
    BufferedImage runningRight[] = {sprite.getSprite(1, 10),
        sprite.getSprite(1, 9),
        sprite.getSprite(1, 8),
        sprite.getSprite(1, 7),
        sprite.getSprite(1, 6),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 2),
        sprite.getSprite(1, 1),
        sprite.getSprite(2, 10),
        sprite.getSprite(2, 9),
        sprite.getSprite(2, 8),
        sprite.getSprite(2, 7),
        sprite.getSprite(2, 6),
        sprite.getSprite(2, 5),
        sprite.getSprite(2, 4),
        sprite.getSprite(2, 3),
        sprite.getSprite(2, 2),
        sprite.getSprite(2, 1)};

    return runningRight;
  }

  private BufferedImage[] initPlayerSpriteRunLeft()
  {
    Sprite sprite = new Sprite("pRunLeft", 80);
    BufferedImage runningLeft[] = {sprite.getSprite(1, 1),
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
        sprite.getSprite(2, 10)};


    return runningLeft;
  }


  /*When not runningRight, playerRegen  deltaTime is added
  to playerStamina up to a maximum of the original
  playerStamina attribute for the level.*/
  private void regenerate()
  {
    stamina += STAMINA_STEP * regen;
    stamina = Math.min(stamina, max_stamina);
  }

  /**
   * Updates player every timer tick. Takes in a map object in order to
   * get a hold of the list of randomly generated fire traps. Checks for
   * actions such as runningRight, walkingRight, picking up and putting down
   * traps.
   *
   * @param map
   */
  public void update(GameMap map)
  {
    diedFrame++;
    if (!heading.equals(Heading.NONE)) {
      is_picking_up = false;
      is_putting_down = false;
      if (heading.getXMovement() != 0 && !hitWallInXDirection(map))
      {
        moveX();
      }
      if (heading.getYMovement() != 0 && !hitWallInYDirection(map))
      {
        moveY();
      }
    }
    if (is_picking_up)
    {
      frame++;
      regenerate();
      if (frame >= PICKUP_FRAMES)
      {
        fire_traps++;
        frame = 0;
        is_picking_up = false;
        map.traps.remove(pickup_trap);
      }
    }
    else if (is_putting_down)
    {
      frame++;
      regenerate();
      if (frame >= PICKUP_FRAMES)
      {
        fire_traps--;
        frame = 0;
        is_putting_down = false;
        FireTrap t = new FireTrap(Location.snapToTile(getCenterPoint().x + 20,
            getCenterPoint().y - 20, GUI.tile_size));
        map.traps.add(t); //Add back to map copy of traps.
      }
    }
    else
    {
      if (heading.equals(Heading.NONE))
      {
        regenerate();
        current_speed = 0;
      }
      else if (current_speed > defined_speed && stamina > 0)
      {
        stamina -= STAMINA_STEP;
      }
      else
      {
        regenerate();
        current_speed = defined_speed;
      }

      // Decides which sound to play based on state of player
      if (isStill())
      {
        SoundLoader.stopMoving();
      }
      else if (isRunning())
      {
        SoundLoader.playerRun();
      }
      else if (isWalking())
      {
        SoundLoader.playerWalk();
      }

      determineAnimation();
      animation.start();
      animation.update();

      if (playerExploded && diedFrame >= GamePanel.FPS)
      {
        playerDied = true;
      }
    }
  }

  /**
   * Checks to see if player's location is on top of the exit.
   * @param exit_location Tile that represents the exit
   * @return true or false
   */
  public boolean reachedExit(Location exit_location)
  {
    return getCenteredBoundingRectangle()
        .contains(exit_location.x, exit_location.y);
  }

  // Called when 'r' is pressed so that the speed stays at 2 times what it was
  // instead of updating when r is held down.
  public void setRunning()
  {
    current_speed = 2 * defined_speed;
  }

  /**
   * if sprite is walking then he will move at walking speeds
   */
  public void setWalking()
  {
    this.current_speed = defined_speed;
  }

  public boolean isStill() {
    return heading.getXMovement() == 0 && heading.getYMovement() == 0;
  }

  public boolean isWalking() {
    return current_speed == defined_speed;
  }

  public boolean isRunning() {
    return current_speed > defined_speed;
  }


  /**
   * picks up the fire trap
   *
   * @param trap
   */
  public void pickupFireTrap(FireTrap trap)
  {
    is_picking_up = true;
    pickup_trap = trap;
    frame = 0;
    pickup_bar = new PickupBar(PICKUP_FRAMES);
  }

  public void dropFireTrap()
  {
    is_putting_down = true;
    frame = 0;
    pickup_bar = new PickupBar(PICKUP_FRAMES);
  }

  
  //Decides which bufferedImages to play and when
  protected void determineAnimation()
  {
    double x_move = heading.getXMovement();
    double y_move = heading.getYMovement();

    if (isStill())
    {
      if (animation == walkRight)
      {
        animation = standRight;
      }
      else if (animation == walkLeft)
      {
        animation = standLeft;
      }

    }
    if (isWalking())
    {
      if (x_move > 0)
      {
        animation = walkRight;
      }
      else if (x_move < 0)
      {
        animation = walkLeft;
      }
      else if (y_move != 0 && (animation != standLeft || animation != standRight)) {
        animation = walkRight;
      }
    }
    if (isRunning())
    {
      if (x_move > 0)
      {
        animation = runRight;
      }
      else if (x_move < 0)
      {
        animation = runLeft;
      }
      else if (y_move != 0 && (animation != standLeft || animation != standRight)) {
        animation = runRight;
      }
    }
  }


  public void playerDied()
  {
    diedFrame = 0;
    playerExploded = true;
    SoundLoader.playScream();

  }

  /**
   * draw the sprite on the map
   *
   * @param g2 Graphics object to draw with
   */
  public void paint(Graphics2D g2)
  {
    if (!playerExploded)
    {
      g2.drawImage(animation.getSprite(), location.getX(), location.getY(),
          null);
      stamina_bar.paint(g2); //Draws stamina bar
      if (is_picking_up || is_putting_down)
      {
        pickup_bar.paint(g2); //Draws pickup loading bar
      }
    }
  }

  /**
   * Private inner class that relates only to the player.
   * Draws a stamina bar next to the player and shows the user how much
   * stamina he has left after running.
   */
  private class StaminaBar
  {

    public final Color TIRED = new Color(93, 93, 93);
    public final Color ENERGY = new Color(14, 15, 246);
    public final Color BORDER = new Color(93, 93, 93);
    private double maxStamina;

    private int width = 5;
    private int height = 70;
    private int xOffset = -10;
    private int yOffset = 5;

    public StaminaBar(double maxStamina)
    {
      this.maxStamina = maxStamina;
    }

    public void paint(Graphics2D g2)
    {
      int x = (int) location.x + xOffset;
      int y = (int) location.y + yOffset;

      g2.setColor(TIRED);
      g2.fillRect(x, y, width, height);

      g2.setColor(ENERGY);
      g2.fillRect(x, y - (int) getStaminaAmount() + height, width,
          (int) getStaminaAmount());

      g2.setColor(BORDER);
      g2.drawRect(x, y, width, height);
    }

    public double getStaminaAmount()
    {
      return (stamina / maxStamina) * height;
    }
  }

  /**
   * Private inner class that also only pertains to the player.
   * Shows how much time it being used to pick up/ put down a firetrap.
   */
  private class PickupBar
  {
    public final Color TOTAL = new Color(249, 44, 25);
    public final Color PICKUP = new Color(108, 246, 16);
    private double max_frames;

    public PickupBar(int max_frames)
    {
//      this.maxStamina = maxStamina;
      this.max_frames = max_frames;
    }

    public void paint(Graphics2D g2)
    {
      int x = getLocation().getX();
      int y = getLocation().getY();

      g2.setColor(TOTAL);
      g2.fillRect(x, y - 20, GUI.tile_size, 8);

      g2.setColor(PICKUP);
      g2.fillRect(x, y - 20, (int) getPickupAmount(), 8);
    }

    private double getPickupAmount()
    {
      return (frame / max_frames) * GUI.tile_size;
    }

  }
}
