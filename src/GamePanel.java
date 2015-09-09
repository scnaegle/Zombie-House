import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.net.URISyntaxException;


/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel
{
  private GameMap map;

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

  }


  public void paintComponent(Graphics2D g)
  {
    super.paintComponent(g);
    map.paint(g, 10);
    g.drawImage(GUI.player, 200, 200, null);
  }
}
