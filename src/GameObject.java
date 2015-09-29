import java.awt.*;

/**
 * This class implements object2D since we want our game objects to
 * use those methods specified, such as intersects and getBoundingRectangle.
 */
public class GameObject implements Object2D {
  protected int width;
  protected int height;
  Location location;

  public GameObject() {

  }

  //Every game object needs a location, a width, and a height.
  public GameObject(Location location, int width, int height) {
    this.location = location;
    this.width = width;
    this.height = height;
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public Location getLocation() {
    return location;
  }

  @Override
  public double getDistance(Object2D other) {
    double diff_x = Math.abs(location.x - other.getLocation().getX());
    double diff_y = Math.abs(location.y - other.getLocation().getY());
    return Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2));
  }

  public double getDirectionTo(Object2D other) {
    double diff_x = other.getLocation().x - location.x;
    double diff_y = other.getLocation().y - location.y;
    return Math.atan2(diff_y, diff_x);
  }

  /**
   * Gets center point of object based on location
   *
   * @return center point
   */
  public Point getCenterPoint() {
    return location.getCenterPoint(width, height);
  }

  @Override
  public Rectangle getBoundingRectangle() {
    return new Rectangle(location.getX(), location.getY(), width, height);
  }

  /**
   * Gets the bounding rectangle of an object based off of it's center point.
   * @return rectangle
   */
  public Rectangle getCenteredBoundingRectangle() {
    Rectangle rect = getBoundingRectangle();
    rect.x += (GUI.tile_size - width) / 2;
    rect.y += (GUI.tile_size - height) / 2;
    return rect;
  }

  @Override
  public boolean intersects(Object2D other) {
    return getBoundingRectangle().intersects(other.getBoundingRectangle());
  }

  @Override
  public String toString() {
    return "GameObject{" +
        "width=" + width +
        ", height=" + height +
        ", location=" + location +
        ", bounding_rectangle=" + getBoundingRectangle() +
        '}';
  }
}
