/**
 * This class defines the player's speed, movement, ability to not walk through
 * walls, and keeps objects from intersecting other objects. Extends GameObject
 * and HumanoidObject since it needs to define the player as an object and allow
 * the zombies and other objects to get the player's info.
 */

import java.util.ArrayList;


/**
 * This class makes makes the basic layout for the player class, the zombie
 * class
 * these methods use things that they all will have in common such as a
 * heading movement
 * speed, a location and a width and height of the humanoid object, AKA a
 * sprite, or sprite object.
 */
public class Humanoid extends GameObject implements HumanoidObject
{
  protected final double MOVE_MULTIPLIER =
      (double) (GUI.tile_size / GamePanel.FPS);
  protected double defined_speed = 1.0;
  protected double current_speed = 1.0;
  Heading heading;
  Animation animation;

  public Humanoid()
  {
    super();
  }

  /**
   * This places the location, and the width and height of a sprite.
   *
   * @param location
   * @param width
   * @param height
   */
  public Humanoid(Location location, int width, int height)
  {
    super(location, width, height);
  }

  /**
   * gets the speed of sprite.
   *
   * @return
   */
  @Override
  public double getSpeed()
  {
    return current_speed;
  }


  /**
   * gets the heading of a sprite.
   *
   * @return
   */
  @Override
  public Heading getHeading()
  {
    return heading;
  }

  /**
   * sets the heading of a sprite.
   *
   * @param heading
   */
  public void setHeading(Heading heading)
  {
    this.heading = heading;
  }

  /**
   * gets the location of a sprite.
   *
   * @return
   */
  @Override
  public Location getLocation()
  {
    return location;
  }

  /**
   * sets location of sprite
   *
   * @param new_location New location object
   */
  @Override
  public void setLocation(Location new_location)
  {
    this.location = new_location;
  }

  /**
   * Tells the sprite how to move based on the heading we give it.
   * Heading is controlled by keyboard arrows.
   */
  public void move(Location next_location)
  {
    this.location = next_location;
  }

  /**
   * tells the sprite to move to right or left on map
   */
  public void moveX()
  {
    this.location.x += getXMovement();
  }

  /**
   * tells the sprite to move up or down on the map
   */
  public void moveY()
  {
    this.location.y += getYMovement();
  }

  /**
   * Calculates the nextLocation of object (mainly zombies) based on heading
   *
   * @return next location (x,y)
   */
  public Location getNextLocation()
  {
    double new_x = location.x + getXMovement();
    double new_y = location.y + getYMovement();
    return new Location(new_x, new_y);
  }

  /**
   * gets the up and down spreed movement of a sprite to tell it to move at
   * a certain speed
   *
   * @return
   */
  public double getYMovement() //In tiles per frame
  {
    return getSpeedMultiplier() * heading.getYMovement() * MOVE_MULTIPLIER;
  }


  /**
   * gets the right and left spreed movement of a sprite to tell it to move at
   * a certain speed
   *
   * @return
   */
  public double getXMovement() //In tiles per frame
  {
    return getSpeedMultiplier() * heading.getXMovement() * MOVE_MULTIPLIER;
  }

  /**
   * makes so the sprite moves at certain speed across the map, depending on
   * what the movement
   * player sets it to in the first part of the game.
   *
   * @return
   */
  public double getSpeedMultiplier()
  {
    return (current_speed * GUI.tile_size / (double) GamePanel.FPS);
  }


  /**
   * This used to make sure the zombie sprites do not overlap in the X plane
   * essentally making walls around them
   *
   * @param zombies
   * @return
   */
  protected boolean hitAnotherZombieInX(ArrayList<Zombie> zombies)
  {
    Location next_location =
        new Location(location.x + getXMovement(), location.y);
    GameObject new_location_object =
        new GameObject(next_location, width, height);

    for (Zombie zombie : zombies)
    {
      //    if(zombie instanceof MasterZombie)
      //    {
      //      return false;
      //    }
      if (this != zombie && new_location_object.getDistance(zombie) <
          GUI.tile_size)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * This used to make sure the zombie sprites do not overlap in the up down
   * plane
   * essentally making walls around them
   *
   * @param zombies
   * @return
   */
  protected boolean hitAnotherZombieInY(ArrayList<Zombie> zombies)
  {
    Location next_location =
        new Location(location.x, location.y + getYMovement());
    GameObject new_location_object =
        new GameObject(next_location, width, height);

    for (Zombie zombie : zombies)
    {
      if (this != zombie && new_location_object.getDistance(zombie) <
          GUI.tile_size)
      {
        return true;
      }
    }
    return false;
  }

  /**
   * listens to see if humanoid hits a wall, that way it can make a sound que
   * the sprite to make a noise
   *
   * @param map
   * @param next_location
   * @return
   */
  protected boolean hitWall(GameMap map, Location next_location)
  {
    int row = next_location.getRow(GUI.tile_size);
    int col = next_location.getCol(GUI.tile_size);
    GameObject new_location_object =
        new GameObject(next_location, width, height);

    Tile tile_check;
    for (int r = row - 1; r <= row + 1; r++)
    {
      for (int c = col - 1; c <= col + 1; c++)
      {
        tile_check = map.getTile(r, c);
        if (tile_check.tile_type.equals(TileType.WALL) ||
            (tile_check.tile_type.equals(TileType.BURNTWALL)) &&
                new_location_object.intersects(tile_check))
        {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * This is used to see if a zombie, or player sprite hits a wall. in the Y
   * plane
   *
   * @param map
   * @return
   */
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
          tile_check.tile_type.equals(TileType.INSIDEWALL)
          || tile_check.tile_type.equals(
          TileType.BURNTWALL)) &&
          new_location_object.getCenteredBoundingRectangle()
                             .intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }

  /**
   * This is used to see if a zombie, or player sprite hits a wall. in the Y
   * plane
   *
   * @param map
   * @return
   */
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
          tile_check.tile_type.equals(TileType.INSIDEWALL)
          || tile_check.tile_type.equals(TileType.BURNTWALL)) &&
          new_location_object.getCenteredBoundingRectangle().
              intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }


  @Override
  public String toString()
  {
    return "Humanoid{" +
        "defined_speed=" + defined_speed +
        ", current_speed=" + current_speed +
        ", location=" + location +
        ", heading=" + heading +
        '}';
  }
}
