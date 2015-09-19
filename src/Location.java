import java.awt.*;

/**
 * Created by scnaegl on 9/8/15.
 */
public class Location
{
  int row;
  int col;
  double x;
  double y;

  public Location() {

  }

  public Location(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Location(double x, double y, int row, int col) {
    this(x, y);
    this.row = row;
    this.col = col;
  }

  public int getRow(int tile_size) {
    Point center = getCenterPoint(tile_size, tile_size);
    return (int)(center.y / tile_size);
  }

  public int getCol(int tile_size) {
    Point center = getCenterPoint(tile_size, tile_size);
    return (int)(center.x / tile_size);
  }

  public int getX() {
    return (int)x;
  }

  public int getY() {
    return (int)y;
  }

  public Point getCenterPoint(int width, int height) {
    return new Point((int)x + width / 2, (int)y + height / 2);
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
