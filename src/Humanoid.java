/**
 * This class defines the player's speed, movement, ability to not walk through
 * walls, and keeps objects from intersecting other objects. Extends GameObject
 * and HumanoidObject since it needs to define the player as an object and allow
 * the zombies and other objects to get the player's info.
 */

import java.util.ArrayList;
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

  public Humanoid(Location location, int width, int height)
  {
    super(location, width, height);
  }

  @Override
  public double getSpeed()
  {
    return current_speed;
  }


  @Override
  public Heading getHeading()
  {
    return heading;
  }

  public void setHeading(Heading heading)
  {
    this.heading = heading;
  }

  @Override
  public Location getLocation()
  {
    return location;
  }

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

  public void moveX()
  {
    this.location.x += getXMovement();
  }

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

  public double getYMovement() //In tiles per frame
  {
    return getSpeedMultiplier() * heading.getYMovement() * MOVE_MULTIPLIER;
  }

  public double getXMovement() //In tiles per frame
  {
    return getSpeedMultiplier() * heading.getXMovement() * MOVE_MULTIPLIER;
  }

  public double getSpeedMultiplier()
  {
    return (current_speed * GUI.tile_size / (double) GamePanel.FPS);
  }

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
        if (tile_check.tile_type.equals(TileType.WALL) &&
            new_location_object.intersects(tile_check))
        {
          return true;
        }
      }
    }
    return false;
  }

  protected boolean hitWallInYDirectionMaster(GameMap map)
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
      if ((tile_check.tile_type.equals(TileType.WALL)) &&
          new_location_object.getCenteredBoundingRectangle()
                             .intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }


  protected boolean hitWallInXDirectionMaster(GameMap map)
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
          tile_check.tile_type.equals(TileType.BURNTWALL)
          || tile_check.tile_type.equals(TileType.INSIDEWALL)) &&
          new_location_object.getCenteredBoundingRectangle()
                             .intersects(tile_check.getBoundingRectangle()))
      {
        return true;
      }
    }
    return false;
  }

  //Zombie collision detection
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
      if (this != zombie && new_location_object.intersects(zombie))
      {
        return true;
      }
    }
    return false;
  }

  //Zombie collision detection
  protected boolean hitAnotherZombieInY(ArrayList<Zombie> zombies)
  {
    Location next_location =
        new Location(location.x, location.y + getYMovement());
    GameObject new_location_object =
        new GameObject(next_location, width, height);

    for (Zombie zombie : zombies)
    {
      if (this != zombie && new_location_object.intersects(zombie))
      {
        return true;
      }
    }
    return false;
  }



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

  protected boolean hitZombie(GameMap map, Location next_location)
  {
    GameObject new_location_object =
        new GameObject(next_location, width, height);
    for (Zombie zombie : map.zombies)
    {
      if (new_location_object.intersects(zombie))
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
