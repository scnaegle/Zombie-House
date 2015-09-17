import java.awt.*;

/**
 * Created by scnaegl on 9/9/15.
 */
public class GameObject implements Object2D {
  protected int width;
  protected int height;
  Location location;

  public GameObject() {

  }

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
    double diff_x = Math.abs(location.x - other.getLocation().x);
    double diff_y = Math.abs(location.y - other.getLocation().y);
    return (diff_y + diff_x);
    //return Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2));
  }

  public double getDirectionTo(Object2D other) {
    double diff_x = other.getLocation().x - location.x;
    double diff_y = other.getLocation().y - location.y;
    return Math.atan2(diff_y, diff_x);
  }

  public Point getCenterPoint() {
    return location.getCenterPoint(width, height);
  }

  @Override
  public Rectangle getBoundingRectangle() {
    return new Rectangle(location.getX(), location.getY(), width, height);
  }

  public Rectangle getCenteredBoundingRectangle() {
    Rectangle rect = getBoundingRectangle();
//    System.out.println("GETTING IN BOUNDING RECT!!");
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
