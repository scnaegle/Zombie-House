import java.awt.*;

/**
 * Created by scnaegl on 9/9/15.
 */
public abstract class GameObject implements Object2D {
  protected int width;
  protected int height;
  Location location;

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
    return Math.sqrt(Math.pow(diff_x, 2) + Math.pow(diff_y, 2));
  }

  public double getDirectionTo(Object2D other) {
    double diff_x = other.getLocation().x - location.x;
    double diff_y = other.getLocation().y - location.y;
    return Math.atan2(diff_y, diff_x);
  }

  @Override
  public Rectangle getBoundingRectangle() {
    return new Rectangle(location.getX(), location.getY(), width, height);
  }

  @Override
  public boolean intersects(Object2D other) {
    if (location.equals(other.getLocation())) {
      return true;
    }
    return false;
  }
}
