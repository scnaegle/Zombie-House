import java.awt.image.BufferedImage;

/**
 * makes the frame to hold everything
 */
public class Frame
{

  private BufferedImage frame;
  private int duration;

  /**
   * Makes the frame and inserts the imiage into it
   * @param frame the buffered image
   * @param duration how many frames we are going to be using
   */
  public Frame(BufferedImage frame, int duration)
  {
    this.frame = frame;
    this.duration = duration;
  }

  /**
   * gets the frame
   * @return get the frames
   */
  public BufferedImage getFrame()
  {
    return frame;
  }

  /**
   * sets the image in the frame
   * @param frame image in the frame
   */
  public void setFrame(BufferedImage frame)
  {
    this.frame = frame;
  }

  /**
   * gets the time that we use a buffered image for
   * @return the durations
   */
  public int getDuration()
  {
    return duration;
  }

  /**
   * Sets how long we use a buffered image for
   * @param duration how long a buffered image should be used
   */
  public void setDuration(int duration)
  {
    this.duration = duration;
  }

}
