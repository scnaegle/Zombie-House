/**
 * Allows us to create firetraps and load images for the explosion.
 * Keeps track of zombies in list to see if they have touched a trap.
 * If they do set one off, it plays the animation, sound, and changes the
 * surrounding tiles to burnt.
 */

import java.awt.*;
import java.awt.image.BufferedImage;

public class FireTrap extends GameObject
{
  public static final int WIDTH = 50;
  public static final int HEIGHT = 50;
  private final int EXPLODE_TIME = 15 * GamePanel.FPS;
  public boolean exploding = false;
  public boolean explodee;
  public FireTrap activeTrap;
  protected int frame = 0;
  Sprite sprite = new Sprite("fireTrap", GUI.tile_size);
  BufferedImage trap = sprite.getSprite(1, 1);
  BufferedImage[] explosion = initExplosion();
  Animation explode = new Animation(explosion, 5);
  Animation fireAnimation = explode;
  boolean remove_me = false;
  Rectangle explosionObj;

  public FireTrap(int width, int height, Location location) {
    this.location = location;
    this.width = width;
    this.height = height;
    explosionObj = new Rectangle(location.getX() - GUI.tile_size,
        location.getY() - GUI.tile_size, 3 * GUI.tile_size, 3 * GUI.tile_size);
  }

  public FireTrap(Location location)
  {
    this(WIDTH, HEIGHT, location);
  }

  //Gets sprite images for explosion
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

  /**
   * Updates each fire trap every timer tick.
   * Checks for zombie and player intersections.
   * Reacts appropriately.
   */
  public void update(GameMap map, Player player)
  {
    frame++;
    for (Zombie zombie : map.zombies)
    {
      if (getDistance(zombie) < GUI.tile_size) {
        if (exploding) {
          if(explosionObj.intersects(zombie.getCenteredBoundingRectangle())) {
            zombie.zombieDied = true;
          }
        } else {
          //If zombie touches trap, it explodes
          if (getCenteredBoundingRectangle()
              .intersects(zombie.getCenteredBoundingRectangle())) {
            startExploding();
            zombie.zombieDied = true;
          }
        }
      }
    }

    //If firetrap is next to exploding trap, that trap should explode too
    for (FireTrap trap : map.traps)
    {
      if (!equals(trap) && trap.exploding && explosionObj.contains(trap.getCenteredBoundingRectangle()))
      {
        startExploding();
      }
    }

    //Waits 15 seconds before stopping explosion
    if (frame >= EXPLODE_TIME && exploding)
    {
      stopExploding(map);
    }

    //If player runs over trap or walks over trap while exploding
    if (exploding && explosionObj.intersects(player.getBoundingRectangle()) ||
        (getCenteredBoundingRectangle().intersects(player.getBoundingRectangle()) && player.isRunning()))
    {
      player.playerDied();
    }

    fireAnimation.update();
  }


  public void startExploding()
  {
    exploding = true;
    fireAnimation.start();
    SoundLoader.playExplosion();
    frame = 0;
  }

  public void stopExploding(GameMap map)
  {
    frame = 0;
    exploding = false;
    fireAnimation.stop();

    //Checks surrounding tiles and blackens them
    int trap_row = location.getRow(GUI.tile_size);
    int trap_col = location.getCol(GUI.tile_size);
    for (int row = trap_row - 1; row <= trap_row + 1; row++)
    {
      for (int col = trap_col - 1; col <= trap_col + 1; col++)
      {
        map.burnTile(row, col);
      }
    }
    remove_me = true;
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
    explosionObj.setLocation((int) newLocation.x, (int) newLocation.y);
  }

  public void paint(Graphics2D g2, Player player)
  {
    if (!player.is_picking_up || player.is_putting_down || !exploding)
    {
      g2.drawImage(trap, location.getX(), location.getY(), null);
    }

    if (exploding)
    {
      g2.drawImage(fireAnimation.getSprite(),
          location.getX() - GUI.tile_size,
          location.getY() - GUI.tile_size, null);

      activeTrap = this;
      explodee = true;
      g2.setColor(Color.BLUE);
      g2.draw(explosionObj);

    }
  }
}
