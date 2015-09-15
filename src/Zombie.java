/**
 * Sets up everything that a zombie has, such as smell, speed, decision rate and
 * how the sprite will move. Each zombie is an object so it extends GameObject
 * which implements Object2D. Each zombie also needs to track the human, so it
 * implements HumanoidObject which lets it get the human's current location.
 */
public abstract class Zombie extends Humanoid implements HumanoidObject
{
  private final double MOVE_MULTIPLIER = (double)GUI.tile_size / GamePanel.FPS;
  public boolean inRange = false;
  protected int frame = 0;
  protected double decision_rate = 2.0;
  protected double smell = 7.0;
  protected Sprite sprite = new Sprite("ZombieSheet");
  Animation moveDown;
  Animation moveLeft;
  Animation moveRight;
  Animation moveUp;
  private SoundLoader groan;
  private SoundLoader zWalk;
  private SoundLoader bite;
  private SoundLoader hitObst;
  private SoundLoader sound;


  public Zombie(Location location) {
    this.location = location;
    this.defined_speed = .5;
    this.current_speed = .5;
    this.width = GUI.tile_size - 10;
    this.height = GUI.tile_size - 10;
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
    if (getDistance((Object2D)player) <= smell * GUI.tile_size) {
      return true;
    }
    return false;
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
    Location next_location = getNextLocation();
    if (!hitWall(map, next_location))
    {
      move(getNextLocation());
    }

    if (intersects((Object2D) player))
    {
      sound = bite;
      sound.play();

    }
    //Sees if zombie is in player hearing's range
    if (getDistance((Object2D) player) <=
        ((Player) player).getHearing() * GUI.tile_size)
    {
      sound = zWalk;
      playSound();
    }
    else
    {
      stopSound();
    }
    //Sees is zombie is in 2*hearing range and hits wall
    if (getDistance((Object2D) player) <=
        2 * ((Player) player).getHearing() * GUI.tile_size
        && hitWall(map, next_location))
    {
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

  public void playSound()
  {
    sound.playLooped();
  }

  public void stopSound()
  {
    sound.stop();
  }
}
