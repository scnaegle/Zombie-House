/**
 * Heading class is used by both player and zombies to determine which way
 * they are facing, both currently and next. Headings go in all directions
 * and are based on the x and y movement/coordinates.
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

  private double x_movement;
  private double y_movement;
  private double degrees;

  //Creates new headings, such as NORTH, SOUTH, etc.
  public Heading(double x_movement, double y_movement)
  {
    this.x_movement = x_movement;
    this.y_movement = y_movement;
    this.degrees = calculateDegrees();
  }

  public Heading(Heading heading)
  {
    this(heading.x_movement, heading.y_movement);
  }

  /**
   * gets the x_movement of the sprite
   * @return
   */
  public double getXMovement()
  {
    return x_movement;
  }

  /**
   * sets the x plane movement of the sprite
   * @param x_movement
   */
  public void setXMovement(double x_movement)
  {
    this.x_movement = x_movement;
    this.degrees = calculateDegrees();
  }

  /**
   * gets the y plane movement
   * @return
   */
  public double getYMovement()
  {
    return y_movement;
  }

  /**
   * sets the y plane movement
   * @param y_movement
   */
  public void setYMovement(double y_movement)
  {
    this.y_movement = y_movement;
    this.degrees = calculateDegrees();
  }


  /**
   * Getter for current movement along x-axis
   *
   * @return positive or negative one, or zero
   */
  public int getColMovement()
  {
    if (x_movement >= 0)
    {
      return (int) Math.ceil(x_movement);
    }
    else
    {
      return (int) Math.floor(x_movement);
    }
  }

  /**
   * Getter for current movement along y-axis
   *
   * @return positive or negative one, or zero
   */
  public int getRowMovement()
  {
    if (y_movement >= 0)
    {
      return (int) Math.ceil(y_movement);
    }
    else
    {
      return (int) Math.floor(y_movement);
    }
  }

  /**
   *Used for zombies when setting random walk and line walk
   * @param degrees
   */
  public void setDegrees(double degrees)
  {
    this.degrees = degrees;
    this.x_movement = Math.cos(degrees);
    this.y_movement = Math.sin(degrees);
  }

  private double calculateDegrees()
  {
    return Math.atan(y_movement / x_movement);
  }

  /**
   * used to see if one Object is directly equal to another this is important
   * becuse we often tiimes have to see if is a sprite is moving over a certain
   * tile type, and need to see the entire object not just a peice of it
   * @param o
   * @return
   */
  @Override
  public boolean equals(Object o)
  {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Heading heading = (Heading) o;

    if (Double.compare(heading.x_movement, x_movement) != 0) return false;
    if (Double.compare(heading.y_movement, y_movement) != 0) return false;
    return Double.compare(heading.degrees, degrees) == 0;

  }

  @Override
  public int hashCode()
  {
    int result;
    long temp;
    temp = Double.doubleToLongBits(x_movement);
    result = (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(y_movement);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    temp = Double.doubleToLongBits(degrees);
    result = 31 * result + (int) (temp ^ (temp >>> 32));
    return result;
  }

  @Override
  public String toString()
  {
    return "Heading{" +
        "x_movement=" + x_movement +
        ", y_movement=" + y_movement +
        ", degrees=" + degrees +
        '}';
  }
}
