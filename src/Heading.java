/**
 * Created by sean on 9/8/15.
 */
public class Heading
{
  static final Heading NORTH = new Heading(0, -1);
  static final Heading SOUTH = new Heading(0, 1);
  static final Heading WEST = new Heading(-1, 0);
  static final Heading EAST = new Heading(1, 0);
  static final Heading NONE = new Heading(0, 0);

  static final Heading NE = new Heading(1, 1);
  static final Heading NW = new Heading(-1, 1);
  static final Heading SE = new Heading(1, -1);
  static final Heading SW = new Heading(-1, -1);

  static final int NORTH_STEP = -1;
  static final int SOUTH_STEP = 1;
  static final int WEST_STEP = -1;
  static final int EAST_STEP = 1;

  private int x_movement;
  private int y_movement;
  private double degrees;

  public Heading(int x_movement, int y_movement) {
    this.x_movement = x_movement;
    this.y_movement = y_movement;
    this.degrees = Math.atan((double)y_movement / x_movement);
  }

  public Heading(Heading heading) {
    this(heading.x_movement, heading.y_movement);
  }

  public int getXMovement() {
    return x_movement;
  }

  public int getYMovement() {
    return y_movement;
  }

  public double getDegrees() {
    return degrees;
  }

  public void setXMovement(int x_movement) {
    this.x_movement = x_movement;
  }

  public void setYMovement(int y_movement) {
    this.y_movement = y_movement;
  }

  @Override
  public boolean equals(Object o) {
    //if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Heading heading = (Heading) o;

    if (x_movement != heading.x_movement) return false;
    if (y_movement != heading.y_movement) return false;
    return true;

  }

  @Override
  public int hashCode() {
    int result;
    long temp;
    result = x_movement;
    result = 31 * result + y_movement;
    temp = Double.doubleToLongBits(degrees);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString() {
    return "Heading{" +
        "x_movement=" + x_movement +
        ", y_movement=" + y_movement +
        ", degrees=" + degrees +
        '}';
  }
}
