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

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getX() {
    return (int)x;
  }

  public int getY() {
    return (int)y;
  }

  public void setRow(int row) {
    this.row = row;
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
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }

    Location location = (Location) o;

    if (row != location.row)
    {
      return false;
    }
    return col == location.col;

  }

  @Override
  public int hashCode()
  {
    int result = row;
    result = 31 * result + col;
    return result;
  }
}
