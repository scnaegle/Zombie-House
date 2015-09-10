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
  final int FRAMES_PER_SECOND = 60;
  final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
  BufferedImageLoader loader = new BufferedImageLoader();
  Timer frame_timer;
  private GameMap map;
  private BufferedImage[] walking = loader.initPlayerSpriteWalk();
  private Animation walk = new Animation(walking, 10);
  public Animation animation = walk;

  public GamePanel()
  {
    setPreferredSize(new Dimension(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT - 25));
    setBackground(Color.white);

    File map_file = null;
    try
    {
      map_file =
          new File(getClass().getResource("resources/level1.map").toURI());
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

  }


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

//    System.out.println("Got here!!!");
    g.setColor(Color.BLACK);
    g.fillRect(10, 10, 50, 50);
    map.paint(g, 10);

    g.drawImage(animation.getSprite(), 200, 200, null);
  }
}
