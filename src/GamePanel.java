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
  Timer frame_timer;
  private GameMap map;
  private Player player = new Player(new Location(5, 5, 100, 100));

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

    player.setHeading(Heading.EAST);

    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        player.move();
        if (player.location.x > GUI.SCENE_WIDTH) {
          player.setLocation(new Location(player.location.row, player.location.col, 0, player.location.y));
        }
        repaint();
      }
    });
    frame_timer.start();

  }


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    map.paint(g, 80);

    g.drawImage(player.animation.getSprite(), player.location.x, player.location.y, null);
  }
}
