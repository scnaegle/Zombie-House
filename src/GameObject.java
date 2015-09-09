import java.awt.*;

/**
 * Created by scnaegl on 9/9/15.
 */
public abstract class GameObject implements Object2D {
  protected int width;
  protected int height;
  protected Location location;

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
  public Rectangle getBoundingRectangle() {
    return new Rectangle(location.x, location.y, width, height);
  }

  @Override
  public boolean intersects(Object2D other) {
    if (location.equals(other.getLocation())) {
      return true;
    }
    return false;
  }
}
