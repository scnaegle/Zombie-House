import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  private Zombie randomZombie =
      new RandomWalkZombie(new Location(10, 10, 200, 200));
  private Zombie lineZombie =
      new LineWalkZombie(new Location(20, 20, 300, 300));

  public GamePanel()
  {
//    System.out.println("scene_width: " + GUI.SCENE_WIDTH);
//    System.out.println("scene_height: " + GUI.SCENE_HEIGHT);
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
    randomZombie.setHeading(Heading.EAST);
    lineZombie.setHeading(Heading.EAST);


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        player.move();
        if (player.location.x > GUI.SCENE_WIDTH) {
          player.setLocation(new Location(player.location.row, player.location.col, 0, player.location.y));
        }
        randomZombie.move();
        if (randomZombie.location.x > GUI.SCENE_WIDTH) {
          randomZombie.setLocation(new Location(randomZombie.location.row, randomZombie.location.col, 0, randomZombie.location.y));
        }
        lineZombie.move();
        if (lineZombie.location.x > GUI.SCENE_WIDTH) {
          lineZombie.setLocation(new Location(lineZombie.location.row, lineZombie.location.col, 0, lineZombie.location.y));
        }
        repaint();
      }
    });
    //frame_timer.start();

  }


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    map.paint(g, GUI.tile_size);

    g.drawImage(player.animation.getSprite(), player.location.getX(),
        player.location.getY(), null);
    g.drawImage(randomZombie.animation.getSprite(), randomZombie.location.getX(),
        randomZombie.location.getY(), null);
    g.drawImage(lineZombie.animation.getSprite(), lineZombie.location.getX(),
        lineZombie.location.getY(), null);
  }
}
