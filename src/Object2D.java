import java.awt.*;

/**
 * Created by scnaegl on 9/9/15.
 */
public interface Object2D {

  /**
   * @return x coordinate of upper left corner of object
   */
  int getX();

  /**
   * @return y coordinate of upper left corner of object
   */
  int getY();

  /**
   * @return Row coordinate
   */
  int getRow();

  /**
   * @return Column coordinate
   */
  int getCol();

  /**
   * Get the bounding rectangle for the object
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
