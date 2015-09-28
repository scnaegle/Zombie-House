import java.awt.*;

/**
 * Created by scnaegl on 9/8/15.
 */

/**
 * has to do with bookkeeping and maintaing location
 */
public class Location
{
  int row;
  int col;
  double x;
  double y;

  public Location() {

  }

  /**
   * sets a location of an object generally
   * @param x
   * @param y
   */
  public Location(double x, double y) {
    this.x = x;
    this.y = y;
  }

  /**
   * sets a location of a object, but mainly used for tiles
   * @param x
   * @param y
   * @param row
   * @param col
   */
  public Location(double x, double y, int row, int col) {
    this(x, y);
    this.row = row;
    this.col = col;
  }

  /**
   * gets the row for a 2d grid an object is on
   * @param tile_size
   * @return
   */
  public int getRow(int tile_size) {
    Point center = getCenterPoint(tile_size, tile_size);
    return (center.y / tile_size);
  }


  /**
   * gets the column for a 2d grid an object is on
   * @param tile_size
   * @return
   */
  public int getCol(int tile_size) {
    Point center = getCenterPoint(tile_size, tile_size);
    return (center.x / tile_size);
  }

  public int getX() {
    return (int)x;
  }

  public int getY() {
    return (int)y;
  }

  /**
   * gets the center point of an object
   * @param width
   * @param height
   * @return
   */
  public Point getCenterPoint(int width, int height) {
    return new Point((int)x + width / 2, (int)y + height / 2);
  }

  public static Location snapToTile(double x, double y, int tile_size) {
    int new_x = (int)(x / GUI.tile_size) * tile_size;
    int new_y = (int)(y / GUI.tile_size) * tile_size;
    return new Location(new_x, new_y);
  }
  @Override
  public String toString() {
    return "Location{" +
        "x=" + x +
        ", y=" + y +
        ", row=" + row +
        ", col=" + col +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Location location = (Location) o;

    if (row != location.row) return false;
    if (col != location.col) return false;
    if (Double.compare(location.x, x) != 0) return false;
    return Double.compare(location.y, y) == 0;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = row;
    result = 31 * result + col;
    temp = Double.doubleToLongBits(x);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }
}
