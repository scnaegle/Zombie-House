import java.awt.*;

/**
 * Sets up everything that a zombie has, such as smell, speed, decision rate
 * and how the sprite will move. Each zombie is an object so it extends
 * GameObject
 * which implements Object2D. Each zombie also needs to track the human, so it
 * implements HumanoidObject which lets it get the human's current location.
 */
public class Zombie extends Humanoid implements HumanoidObject
{
  public double decision_rate;
  public double smell;
  public boolean bitPlayer = false;
  public boolean zombieDied = false;
  protected int frame = 0;
  protected Sprite sprite = new Sprite("ZombieSheet", GUI.tile_size);
  Animation moveDown;
  Animation moveLeft;
  Animation moveRight;
  Animation moveUp;


  /**
   * Constructs zombie with location, width and height.
   *
   * @param location location of zombie
   */
  public Zombie(Location location) {
    this.location = location;
    this.width = GUI.tile_size - 10;
    this.height = GUI.tile_size - 10;
  }

  /**
   * Makes new zombie with such attributes
   */

  public Zombie(double speed, double smell, double decision_rate, Location location)
  {
    this(location);
    this.current_speed = speed;
    this.defined_speed = speed;
    this.smell = smell;
    this.decision_rate = decision_rate;
  }

  /**
   * Gets current speed of zombie
   * @return current_speed
   */
  public double getSpeed()
  {
    return current_speed;
  }

  protected void determineAnimation() {
    double x_move = heading.getXMovement();
    double y_move = heading.getYMovement();
    if(x_move > y_move) {
      if (x_move > 0) {
        animation = moveRight;
      } else if (x_move < 0){
        animation = moveLeft;
      }
    } else {
      if (y_move > 0) {
        animation = moveDown;
      } else if (y_move < 0) {
        animation = moveUp;
      }
    }
  }

  protected boolean smellPlayer(HumanoidObject player) {
    return getDistance((Object2D) player) <= smell * GUI.tile_size;
  }

  protected void chooseDirection(HumanoidObject player) {
    // This is a placeholder that should be overridden.
  }


  /**
   * Updates each zombie in list every timer tick.
   Checks for movement, if player gets biten, and sounds.
   * @param map Game map
   * @param player main player
   */
  public void update(GameMap map, HumanoidObject player) {
    frame++;
    if (frame >= decision_rate * GamePanel.FPS) {
      frame = 0;
      chooseDirection(player);
    }

    if (!hitWallInXDirection(map) && !hitAnotherZombieInX(map.zombies)) {
      moveX();
    }
    if (!hitWallInYDirection(map) && !hitAnotherZombieInY(map.zombies)) {
      moveY();
    }

    if (bitesPlayer(player))
    {
      setBite();
      bitPlayer = true;
    }

    //Sees if zombie is in player hearing's range
    double range = ((Player) player).getHearing() * GUI.tile_size;
    Location next_location = getNextLocation();
    if (getDistance((Object2D) player) <= range)
    {
      SoundLoader.playZWalk(checkZombieDirection(player));
    }


    //Sees is zombie is in 2*hearing range and hits wall
    if (getDistance((Object2D) player) <= 2 * range &&
        hitWall(map, next_location))
    {
      SoundLoader.playHitObst(checkZombieDirection(player));
    }


    determineAnimation();
    animation.start();
    animation.update();
  }

  private boolean toTheRight(HumanoidObject player)
  {
    return location.getX() > player.getLocation().getX();
  }

  private boolean toTheLeft(HumanoidObject player)
  {
    return location.getX() < player.getLocation().getX();

  }

  //Moves the balance between speakers depending on where zombie is
  private float checkZombieDirection(HumanoidObject player)
  {
    if (toTheLeft(player))
    {
      return -1f;
    }
    else if (toTheRight(player))
    {
      return 1f;
    }
    else
    {
      return 0;
    }
  }


  public boolean touchingZombie(Zombie zombie)
  {
    return (intersects(zombie));
  }

  /**
   * Sees if zombie touched player.
   * @param player only player
   * @return true or false
   */
  public boolean bitesPlayer(HumanoidObject player)
  {
    return (intersects((Object2D) player));

  }

  /**
   * Sets sound effect for when zombie bites player
   */
  public void setBite()
  {
    SoundLoader.playBite();
  }

  /**
   * Explains how to draw zombie and when to draw it.
   * @param g2
   */
  public void paint(Graphics2D g2)
  {
    if (!zombieDied)
    {
      g2.drawImage(animation.getSprite(), location.getX(),
          location.getY(), null);
    }

  }


}
