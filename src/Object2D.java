import java.awt.*;

/**
 * Interface that sets up methods for GameObject to use.
 */
public interface Object2D {

  /**
   * @return Width of object
   */
  int getWidth();

  /**
   * @return Height of object
   */
  int getHeight();

  /**
   * Get the location of an object which contains the row, col, and
   * x and y coordinates.
   * @return Location of object
   */
  Location getLocation();

  /**
   * Get the euclidean distance of 1 object to another
   * @param other The other object to check.
   * @return double Distance between the 2 objects
   */
  double getDistance(Object2D other);

  /**
   * Get the bounding rectangle for the object
   * @return Bounding rectangle.
   */
  Rectangle getBoundingRectangle();

  /**
   * Does this object intersect another? (Checking if the bounding
   * rectangles intersect will generally suffice.)
   * @param other The other object to check.
   * @return True if objects intersect.
   */
  boolean intersects(Object2D other);
}
