/**
 * Created by sean on 9/13/15.
 */
public class Humanoid extends GameObject implements HumanoidObject
{
  protected final double MOVE_MULTIPLIER =
      (double) (GUI.tile_size / GamePanel.FPS);
  protected double defined_speed = 1.0;
  protected double current_speed = 1.0;
  Heading heading;
  Animation animation;


  @Override
  public double getSpeed()
  {
    return current_speed;
  }

  public void setSpeed(double speed) {
    this.current_speed = speed;
  }

  @Override
  public Heading getHeading()
  {
    return heading;
  }

  public void setHeading(Heading heading) {
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
  public void move(Location next_location) {
    this.location = next_location;
  }

  public Location getNextLocation() {
//    System.out.println("moving...");
//    location.x += (current_speed * Math.cos(heading.getDegrees())) * MOVE_MULTIPLIER;
//    location.y -= (current_speed * Math.sin(heading.getDegrees())) * MOVE_MULTIPLIER;
    double new_x = location.x + ((current_speed * GUI.tile_size / GamePanel.FPS)
        * heading.getXMovement() * MOVE_MULTIPLIER);
    double new_y = location.y + ((current_speed * GUI.tile_size / GamePanel.FPS)
        * heading.getYMovement() * MOVE_MULTIPLIER);
    return new Location(new_x, new_y);
  }

  public boolean hitWall(GameMap map, Location next_location) {
    int row = next_location.getRow(GUI.tile_size);
    int col = next_location.getCol(GUI.tile_size);
    GameObject new_location_object = new GameObject(next_location, GUI.tile_size, GUI.tile_size);

//    Tile tile_check = map.getTile(row + (int)Math.ceil(heading.getYMovement()), col);
//    if (tile_check.tile_type.equals(TileType.WALL) &&
//        new_location_object.intersects(tile_check)) {
//      return true;
//    }
//    tile_check = map.getTile(row, col + (int)Math.ceil(heading.getXMovement()));
//    if (tile_check.tile_type.equals(TileType.WALL) &&
//        new_location_object.intersects(tile_check)) {
//      return true;
//    }

//    System.out.println("***************************************************");
//    System.out.println("new location: " + new_location_object.location);
//    System.out.format("current: [row=%d, col=%d]\n", row, col);
    Tile tile_check;
    for(int r = row - 1; r <= row + 1; r++) {
      for(int c = col - 1; c <= col + 1; c++) {
        tile_check = map.getTile(r, c);
//        System.out.println("tile_check: " + tile_check);
        if (tile_check.tile_type.equals(TileType.WALL) &&
            new_location_object.intersects(tile_check)) {
//          System.out.println("Hit wall...");
          return true;
        }
      }
    }
    return false;
  }
}
