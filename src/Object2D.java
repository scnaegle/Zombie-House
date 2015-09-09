import java.awt.Rectangle;

/**
 * Created by scnaegl on 9/9/15.
 */
public interface Object2D {

  /**
   * @return x coordinate of upper left corner of object
   */
//  int getX();

  /**
   * @return y coordinate of upper left corner of object
   */
//  int getY();

  /**
   * @return Row coordinate
   */
//  int getRow();

  /**
   * @return Column coordinate
   */
//  int getCol();

  /**
   * Get the location of an object which contains the row, col, and
   * x and y coordinates.
   * @return Location of object
   */
  Location getLocation();

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
