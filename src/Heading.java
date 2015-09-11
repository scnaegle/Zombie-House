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

  private int x_movement;
  private int y_movement;
  private double degrees;

  public Heading(int x_movement, int y_movement) {
    this.x_movement = x_movement;
    this.y_movement = y_movement;
    this.degrees = Math.atan((double)y_movement / x_movement);
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

  @Override
  public String toString() {
    return "Heading{" +
        "x_movement=" + x_movement +
        ", y_movement=" + y_movement +
        ", degrees=" + degrees +
        '}';
  }
}
