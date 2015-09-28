/**
 * Sets up line walk zombies and their sprites.
 * Line walk zombies extend the Zombie class since they do not need
 * their own copy of the information.
 */

import java.awt.image.BufferedImage;

public class LineWalkZombie extends Zombie
{
  //Arrays that hold images for animations
  BufferedImage[] down = initDown();
  BufferedImage[] left = initLeft();
  BufferedImage[] right = initRight();
  BufferedImage[] up = initUp();

  public LineWalkZombie(Location location) {
    super(location);
    moveDown = new Animation(down, 5);
    moveLeft = new Animation(left, 5);
    moveRight = new Animation(right, 5);
    moveUp = new Animation(up, 5);
    animation = moveLeft;
    animation.start();
    this.heading = new Heading(Heading.NORTH);
    setRandomHeading();
  }


  public LineWalkZombie(double speed, double smell, double decision_rate, Location location) {
    this(location);
    this.defined_speed = speed;
    this.current_speed = speed;
    this.smell = smell;
    this.decision_rate = decision_rate;
  }


  //sets the direction towards the player
  @Override
  protected void chooseDirection(HumanoidObject player) {
    if (smellPlayer(player)) {
      double angle = getDirectionTo((Object2D)player);
      heading.setDegrees(angle);
    }
  }

  //sprite looks down
  private BufferedImage[] initDown()
  {
    BufferedImage down[] = {sprite.getSprite(1, 7),
        sprite.getSprite(1, 8),
        sprite.getSprite(1, 9),};
    return down;
  }

  //sprite looks left
  private BufferedImage[] initLeft()
  {
    BufferedImage left[] = {sprite.getSprite(2, 7),
        sprite.getSprite(2, 8),
        sprite.getSprite(2, 9),};
    return left;
  }

  //sprite looks right
  private BufferedImage[] initRight()
  {
    BufferedImage right[] = {sprite.getSprite(3, 7),
        sprite.getSprite(3, 8),
        sprite.getSprite(3, 9),};
    return right;
  }

  //sprite looks up
  private BufferedImage[] initUp()
  {
    BufferedImage up[] = {sprite.getSprite(4, 7),
        sprite.getSprite(4, 8),
        sprite.getSprite(4, 9),};
    return up;
  }
}
