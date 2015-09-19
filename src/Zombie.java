/**
 * Sets up everything that a zombie has, such as zSmell, speed, decision rate
 * and
 * how the sprite will move. Each zombie is an object so it extends GameObject
 * which implements Object2D. Each zombie also needs to track the human, so it
 * implements HumanoidObject which lets it get the human's current location.
 */
public class Zombie extends Humanoid implements HumanoidObject
{
  public static double decision_rate;
  public static double smell;
  private final double MOVE_MULTIPLIER = (double)GUI.tile_size / GamePanel.FPS;
  public boolean inRange = false;
  public boolean bitPlayer = false;
  public SoundLoader sound;
  public boolean zombieDied = false;
  protected int frame = 0;
  protected Sprite sprite = new Sprite("ZombieSheet");
  Animation moveDown;
  Animation moveLeft;
  Animation moveRight;
  Animation moveUp;
  private SoundLoader groan;
  private SoundLoader zWalk;
  private SoundLoader bite;
  private SoundLoader hitObst;
  private FireTrap trap;


  public Zombie(Location location) {
    this.location = location;
    this.width = GUI.tile_size - 10;
    this.height = GUI.tile_size - 10;
  }

  public Zombie(double speed, double smell, double decision_rate, Location location)
  {
    this(location);
    this.current_speed = speed;
    this.defined_speed = speed;
    this.smell = smell;
    this.decision_rate = decision_rate;
  }

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

  public void update(GameMap map, HumanoidObject player) {
    frame++;
    if (frame >= decision_rate * GamePanel.FPS) {
      frame = 0;
      chooseDirection(player);
    }
//    System.out.println("heading: " + heading);
    if (!hitWallInXDirection(map)) {
      moveX();
    }
    if (!hitWallInYDirection(map)) {
      moveY();
    }


    if (bitesPlayer(player))
    {
      setBite();
      sound.play();
      bitPlayer = true;
    }

    //Sees if zombie is in player hearing's range
    double range = ((Player) player).getHearing() * GUI.tile_size;
    if (getDistance((Object2D) player) <= range)
    {
      //System.out.println(Math.round(getDistance((Object2D) player)));
      sound = zWalk;
      sound.playLooped();
    }
//    else
//    {
//      System.out.println("  Can't hear zombie anymore");
//      stopSound();
//    }


    Location next_location = getNextLocation();
    //Sees is zombie is in 2*hearing range and hits wall
    if (getDistance((Object2D) player) <= 2 * range &&
        hitWall(map, next_location))
    {
//      System.out.println("Zombie hit wall");
      sound = hitObst;
      sound.play();
    }

    determineAnimation();
    animation.start();
    animation.update();
  }

  public void loadNoises()
  {
    groan = new SoundLoader("zGroan.wav");
    zWalk = new SoundLoader("zWalk.wav");
    bite = new SoundLoader("zBite.wav");
    hitObst = new SoundLoader("zHitObst.wav");
    sound = zWalk;

  }

  public boolean bitesPlayer(HumanoidObject player)
  {
    return (intersects((Object2D) player));

  }

  public void setBite()
  {
    sound = bite;
  }

  public double getDecisionRate()
  {
    return decision_rate;
  }

  public double getSmell()
  {
    return smell;
  }

}
