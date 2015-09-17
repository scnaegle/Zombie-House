import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Allows us to create firetraps and load images for the explosion
 */
public class FireTrap extends GameObject
{
  public boolean exploding = false;
  Sprite sprite = new Sprite("fireTrap");
  BufferedImage trap = sprite.getSprite(1, 1);
  BufferedImage[] explosion = initExplosion();
  Animation explode = new Animation(explosion, 4);
  Animation fireAnimation = explode;
  private SoundLoader combust;
  private GamePanel gamePanel;
  private SoundLoader sound;

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


  public void update(List<Zombie> zombies, Player player)
  {
    for (Zombie zombie : zombies)
    {
      if (intersects(zombie))
      {
        System.out.println("Zombie touched trap");
        exploding = true;
        sound.play();
        fireAnimation.update();
        fireAnimation.start();
      }
      exploding = false;
    }
    if (intersects(player))
    {
      System.out.println("player on trap");
    }

  }

  public void loadExplosion()
  {
    combust = new SoundLoader("explosion.wav");
    sound = combust;
  }
}
