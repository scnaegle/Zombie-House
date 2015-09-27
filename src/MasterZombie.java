import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

/**
 * Sets up the master zombie since his sprite is different, as well as
 * gives him a "special power". He extends the zombie class since he
 * must know everything the other zombie's do.
 *
 * This homies speical power is that he can walk through walls like a complete...
 * awesome zombie.
 */
public class MasterZombie extends Zombie
{

  /**
   * this array list will help him determine where a player is by using the
   * zombies that are around the player to know where the player is.
   * words and explaing are hard
   *
   * the master zombie can move faster and smell better than the other zombies
   * as well, but that is set up in the GameMap class, because it was easier
   * and made more sense to put it there
   */
  static ArrayList<Zombie> minons = new ArrayList<Zombie>();
  BufferedImage[] down = initDown();
  BufferedImage[] left = initLeft();
  BufferedImage[] right = initRight();
  BufferedImage[] up = initUp();
  public MasterZombie(Location location)
  {
    super(location);
    moveDown = new Animation(down, 5);
    moveLeft = new Animation(left, 5);
    moveRight = new Animation(right, 5);
    moveUp = new Animation(up, 5);
    animation = moveLeft;
    animation.start();
    Random rand = new Random();
    switch (rand.nextInt(4))
    {
      case 0:
        this.heading = new Heading(Heading.NORTH);
        break;
      case 1:
        this.heading = new Heading(Heading.WEST);
        break;
      case 2:
        this.heading = new Heading(Heading.EAST);
        break;
      case 3:
        this.heading = new Heading(Heading.SOUTH);
    }
  }

  public MasterZombie(double speed, double smell, double decision_rate,
                      Location location)
  {
    this(location);
    this.defined_speed = speed;
    this.current_speed = speed;
    Zombie.smell = smell;
    Zombie.decision_rate = decision_rate;
  }

  /**
   * add those zombies to the list
   * @param zombie
   */
  public static void addZombie(Zombie zombie)
  {
    minons.add(zombie);
  }

  private BufferedImage[] initDown()
  {
    BufferedImage down[] = {sprite.getSprite(5, 4),
        sprite.getSprite(5, 5),
        sprite.getSprite(5, 6),};
    return down;
  }

  private BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(6, 4),
        sprite.getSprite(6, 5),
        sprite.getSprite(6, 6),};
    return left;
  }

  private BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(7, 4),
        sprite.getSprite(7, 5),
        sprite.getSprite(7, 6),};
    return right;
  }

  private BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(8, 4),
        sprite.getSprite(8, 5),
        sprite.getSprite(8, 6),};
    return up;
  }


  /**
   * this is the method that gives the master zombie his speical powers to go
   * through inside wall. It makes so it ignores the inside walls. It is the
   * same as the other hit wall in xdirction,but doesnt include one check
   *
   * @param map
   * @return
   */
  @Override
  protected boolean hitWallInYDirection(GameMap map)
  {
    Location next_location =
        new Location(location.x, location.y + getYMovement());
    GameObject new_location_object =
        new GameObject(next_location, width, height);
    int row = next_location.getRow(GUI.tile_size) + heading.getRowMovement();
    int col = next_location.getCol(GUI.tile_size);

    Tile tile_check;
    for (int c = col - 1; c <= col + 1; c++)
    {
      tile_check = map.getTile(row, c);
      if ((tile_check.tile_type.equals(TileType.WALL) ||
          tile_check.tile_type.equals(TileType.BURNTWALL)) &&
          new_location_object.getCenteredBoundingRectangle()
              .intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * this is the method that gives the master zombie his speical powers to go
   * through inside wall. It makes so it ignores the inside walls. It is the
   * same as the other hit wall in xdirction,but doesnt include one check
   *
   * @param map
   * @return
   */
  @Override
  protected boolean hitWallInXDirection(GameMap map)
  {
    Location next_location =
        new Location(location.x + getXMovement(), location.y);
    GameObject new_location_object =
        new GameObject(next_location, width, height);
    int row = next_location.getRow(GUI.tile_size);
    int col = next_location.getCol(GUI.tile_size) + heading.getColMovement();

    Tile tile_check;
    for (int r = row - 1; r <= row + 1; r++)
    {
      tile_check = map.getTile(r, col);
      if ((tile_check.tile_type.equals(TileType.WALL) ||
          tile_check.tile_type.equals(TileType.BURNTWALL)) &&
          new_location_object.getCenteredBoundingRectangle()
              .intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }


  @Override
  protected void chooseDirection(HumanoidObject player)
  {
//    System.out.println("Choosing Line Walk Zombie direction...");
    if (smellPlayer(player))
    {
//      System.out.println("BRAAAAAIIINNNNNNZZZZ");
      double angle = getDirectionTo((Object2D) player);
//      System.out.println("new angle: " + angle);
      heading.setDegrees(angle);
    } // else if hit hall then choose random direction
  }

  @Override
  public void update(GameMap map, HumanoidObject player)
  {
    frame++;
    if (frame >= decision_rate * GamePanel.FPS)
    {
      frame = 0;
      chooseDirection(player);
    }
//    System.out.println("heading: " + heading);
    /**
     * like we say he can go thorugh walls and getting this
     * method allows for it
     */
    if (!hitWallInXDirection(map))
    {
      moveX();
    }
    if (!hitWallInYDirection(map))
    {
      moveY();
    }

    ///   if(touchingZombie(zombie))
    {

    }
    /**
     * biting the player
     * well thats a game over
     */
    if (bitesPlayer(player))
    {
      setBite();
      bitPlayer = true;
    }

    //Sees if zombie is in player hearing's range
    double range = ((Player) player).getHearing() * GUI.tile_size;
    if (getDistance((Object2D) player) <= range)
    {
      //System.out.println(Math.round(getDistance((Object2D) player)));
      SoundLoader.playZWalk();
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
      SoundLoader.playHitObst();
    }

    determineAnimation();
    animation.start();
    animation.update();
  }


}
