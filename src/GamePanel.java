import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;


/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel
{
  private GameMap map;
  private BufferedImage[] walking = {Sprite.getSprite("player", 2, 5), Sprite.getSprite("player", 2, 6), Sprite.getSprite("player", 3, 3)}; // Gets the upper left images of my sprite sheet
  private Animation walk = new Animation(walking, 10);
  public Animation animation = walk;

  final int FRAMES_PER_SECOND = 25;
  final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
  Timer frame_timer;

  public GamePanel()
  {
    setPreferredSize(new Dimension(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT - 25));
    setBackground(Color.white);

    File map_file = null;
    try
    {
      map_file = new File(getClass().getResource("resources/level1.map").toURI());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }

    map = new GameMap(map_file);

    repaint();

    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        animation.update();
        repaint();
      }
    });
    frame_timer.start();

    animation.start();
    // These are animation states
////    private Animation walkRight = new Animation(walkingRight, 10);
//    private Animation standing = new Animation(standing, 10);

    // This is the actual animation
//    private Animation animation = standing;

  }


  public void startAnimation() {
    animation.start();
  }

  public void paintComponent(Graphics g) {
    super.paintComponent(g);

//    System.out.println("Got here!!!");
    g.setColor(Color.BLACK);
    g.fillRect(10,10, 50, 50);
    map.paint(g, 10);

    g.drawImage(animation.getSprite(), 200, 200, null);
  }
}
