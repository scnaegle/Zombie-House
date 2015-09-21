import java.awt.image.BufferedImage;

/**
 * Allows us to create firetraps and load images for the explosion
 */
public class FireTrap extends GameObject
{
  private final int EXPLODE_TIME = 15 * GamePanel.FPS;
  public boolean exploding = false;
  public boolean trapIsGone = false;
  protected int frame = 0;
  Sprite sprite = new Sprite("fireTrap", GUI.tile_size);
  BufferedImage trap = sprite.getSprite(1, 1);
  BufferedImage[] explosion = initExplosion();
  Animation explode = new Animation(explosion, 4);
  Animation fireAnimation = explode;
  boolean remove_me = false;
  private int explosionSize = 240;
  private SoundLoader combust;
  private GamePanel gamePanel;

  public FireTrap(Location location)
  {
    this.location = location;
  }

  public FireTrap(int width, int height, Location location) {
    this(location);
    this.width = width;
    this.height = height;
  }
  private BufferedImage[] initExplosion()
  {
    Sprite sprite = new Sprite("explode", 240);

    BufferedImage explode[] = {sprite.getSprite(1, 1),
        sprite.getSprite(1, 2),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 6),
        sprite.getSprite(2, 1),
        sprite.getSprite(2, 2),
        sprite.getSprite(2, 3),
        sprite.getSprite(2, 4),
        sprite.getSprite(2, 5),
        sprite.getSprite(2, 6),
        sprite.getSprite(3, 1),
        sprite.getSprite(3, 2),
        sprite.getSprite(3, 3),
        sprite.getSprite(3, 4),
        sprite.getSprite(3, 5),
        sprite.getSprite(3, 6),
        sprite.getSprite(4, 1),
        sprite.getSprite(4, 2),
        sprite.getSprite(4, 3),
        sprite.getSprite(4, 4),
        sprite.getSprite(4, 5),
        sprite.getSprite(4, 6)};
    return explode;
  }


  public void update(GameMap map, Player player)
  {
    frame++;

    for (Zombie zombie : map.zombies)
    {
      if (getDistance(zombie) < GUI.tile_size)
      {
        if (getCenteredBoundingRectangle()
            .intersects(zombie.getCenteredBoundingRectangle()))
        {
          //System.out.println(frame);

          //frame = fireAnimation.getFrameCount();
          //System.out.println("We should probably explode now....");
          startExploding();
          zombie.zombieDied = true;

          if (getBoundingRectangle().intersects(player.getBoundingRectangle()))
          {
            player.playerDied = true;
          }
        }
      }
    }

    //System.out.println("frame: " + frame);
    if (exploding && frame >= EXPLODE_TIME)
    {
      stopExploding(map);
    }

    if (getCenteredBoundingRectangle().intersects(player
        .getBoundingRectangle()) && player.isRunning)
    {
      //exploding = true;
      startExploding();
      player.playerDied = true;

    }

    fireAnimation.update();
  }


  public void startExploding()
  {
//    System.out.println("exploding!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
//    System.out.println("location: " + location);
    exploding = true;
    fireAnimation.start();
    SoundLoader.playExplosion();
    frame = 0;

  }

  public void stopExploding(GameMap map)
  {
//    System.out.println("We are no longer exploding!!!");
//    System.out.println("location: " + location);
    frame = 0;
    exploding = false;
    fireAnimation.stop();

    Tile test_tile;
    int trap_row = location.getRow(GUI.tile_size);
    int trap_col = location.getCol(GUI.tile_size);
    for (int row = trap_row - 1; row <= trap_row + 1; row++)
    {
      for (int col = trap_col - 1; col <= trap_col + 1; col++)
      {
        test_tile = map.getTile(row, col);

        if (test_tile.tile_type.equals(TileType.BRICK)
            || test_tile.tile_type.equals(TileType.INSIDEWALL))
        {
          test_tile.tile_type = TileType.BURNTFLOOR;

        }
        if (test_tile.tile_type.equals(TileType.WALL))
        {
          test_tile.tile_type.equals(TileType.BURNTWALL);
        }


      }
    }

    remove_me = true;

  //  map.updateBufferedImage(GUI.tile_size);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    FireTrap fireTrap = (FireTrap) o;

    return !(location != null ? !location.equals(fireTrap.location) : fireTrap.location != null);
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }

  public void setNewLocation(Location newLocation)
  {
    location.x = newLocation.x;
    location.y = newLocation.y;
  }
}
