import java.awt.image.BufferedImage;

/**
 * Allows us to create firetraps and load images for the explosion
 */
public class FireTrap extends GameObject
{
  Sprite sprite = new Sprite("fireTrap");
  BufferedImage trap = sprite.getSprite(1, 1);

  BufferedImage[] explosion = initExplosion();
  Animation explode = new Animation(explosion, 4);
  Animation fireAnimation = explode;

  public FireTrap(Location location)
  {
    this.location = location;
  }

  private BufferedImage[] initExplosion()
  {
    Sprite sprite = new Sprite("explode");

    BufferedImage explode[] = {sprite.getSprite(1, 1),
        sprite.getSprite(1, 2),
        sprite.getSprite(1, 3),
        sprite.getSprite(1, 4),
        sprite.getSprite(1, 5),
        sprite.getSprite(1, 6),
        sprite.getSprite(2, 1),
        sprite.getSprite(2, 2),
        sprite.getSprite(2, 3),
        sprite.getSprite(2, 4),
        sprite.getSprite(2, 5),
        sprite.getSprite(2, 6),
        sprite.getSprite(3, 1),
        sprite.getSprite(3, 2),
        sprite.getSprite(3, 3),
        sprite.getSprite(3, 4),
        sprite.getSprite(3, 5),
        sprite.getSprite(3, 6),
        sprite.getSprite(4, 1),
        sprite.getSprite(4, 2),
        sprite.getSprite(4, 3),
        sprite.getSprite(4, 4),
        sprite.getSprite(4, 5),
        sprite.getSprite(4, 6)};
    return explode;
  }

  public void move()
  {
    fireAnimation.update();
    fireAnimation.start();
  }
}
