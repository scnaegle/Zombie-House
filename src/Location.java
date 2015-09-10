/**
 * Created by scnaegl on 9/8/15.
 */
public class Location
{
  int row;
  int col;
  int x;
  int y;

  public Location() {

  }

  public Location(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public Location(int row, int col, int x, int y) {
    this(row, col);
    this.x = x;
    this.y = y;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setRow(int row) {
    this.row = row;
  }

  @Override
  public String toString() {
    return "Location{" +
        "row=" + row +
        ", col=" + col +
        ", x=" + x +
        ", y=" + y +
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
