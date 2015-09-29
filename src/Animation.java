import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This class allows each object's sprite list to be run every tick of a timer,
 * making it move like an animation.
 */
public class Animation
{

  private int frameCount;     // Counts ticks for change
  private int frameDelay;   //Delay between each image

  private int currentFrame;      // animations current frame
  private int animationDirection;
  // total amount of frames for your animation
  // animation direction (i.e counting forward or backward)
  private int totalFrames;
  private boolean stopped;                // has animations stopped
  private List<Frame> frames = new ArrayList<Frame>();    // Arraylist of frames

  /**
   * Takes in list of sprites and the delay between each one and creates
   * an animation.
   */
  public Animation(BufferedImage[] frames, int frameDelay)
  {
    this.frameDelay = frameDelay;
    this.stopped = true;

    for (int i = 0; i < frames.length; i++)
    {
      addFrame(frames[i], frameDelay);
    }

    this.frameCount = 0;
    this.frameDelay = frameDelay;
    this.currentFrame = 0;
    this.animationDirection = 1;
    this.totalFrames = this.frames.size();

  }

  /**
   * Starts animation from beginning frame.
   */
  public void start()
  {
    if (!stopped)
    {
      return;
    }

    if (frames.size() == 0)
    {
      return;
    }

    stopped = false;
  }

  /**
   * Stops animation if there are no frames left.
   */
  public void stop()
  {
    if (frames.size() == 0)
    {
      return;
    }

    stopped = true;
  }

  /**
   * Used to add images to animation sequence
   * @param frame
   * @param duration
   */
  private void addFrame(BufferedImage frame, int duration)
  {
    if (duration <= 0)
    {
      System.err.println("Invalid duration: " + duration);
      throw new RuntimeException("Invalid duration: " + duration);
    }

    frames.add(new Frame(frame, duration));
    currentFrame = 0;
  }

  /**
   * Getter used when the current frame is needed.
   * @return a buffered image from sprite list
   */
  public BufferedImage getSprite()
  {
    return frames.get(currentFrame).getFrame();
  }

  /**
   * Keeps updating list of sprites in order to make animation smooth.
   */
  public void update()
  {
    if (!stopped) //Loop through images
    {
      frameCount++;

      if (frameCount > frameDelay)
      {
        frameCount = 0;
        currentFrame += animationDirection;

        if (currentFrame > totalFrames - 1)
        {
          currentFrame = 0;

        }
        else if (currentFrame < 0)
        {
          currentFrame = totalFrames - 1;

        }

      }

    }

  }


}