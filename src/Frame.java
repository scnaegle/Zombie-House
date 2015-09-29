import java.awt.image.BufferedImage;

/**
 * Class used for frames in each animation.
 */
public class Frame
{

  private BufferedImage frame;
  private int duration;

  /**
   * Every frame is a buffered image with an exact duration in which it is
   * played.
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


}
