import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.net.URISyntaxException;


/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel implements KeyListener
{
  final int FRAMES_PER_SECOND = 60;
  final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;
  public Player player = new Player(new Location(5, 5, 80, 80));
  Timer frame_timer;
  private GameMap map;
  private Zombie randomZombie =
      new RandomWalkZombie(new Location(10, 10, 200, 200));
  private Zombie lineZombie =
      new LineWalkZombie(new Location(20, 20, 300, 300));

  public GamePanel()
  {

//    System.out.println("scene_width: " + GUI.SCENE_WIDTH);
//    System.out.println("scene_height: " + GUI.SCENE_HEIGHT);
    //setPreferredSize(new Dimension(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT - 25));
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

    player.setHeading(Heading.NONE);
    player.setSpeed(1.0);
    System.out.println("Player initialized");

    randomZombie.setHeading(Heading.WEST);
    lineZombie.setHeading(Heading.EAST);

    addKeyListener(this);
    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        //player.move();

        if (player.location.x > GUI.SCENE_WIDTH)
        {
          player.setLocation(
              new Location(player.location.row, player.location.col, 0,
                  player.location.y));
        }
        randomZombie.move();
        if (randomZombie.location.x > GUI.SCENE_WIDTH)
        {
          randomZombie.setLocation(
              new Location(randomZombie.location.row, randomZombie.location.col,
                  0, randomZombie.location.y));
        }
        lineZombie.move();
        if (lineZombie.location.x > GUI.SCENE_WIDTH)
        {
          lineZombie.setLocation(
              new Location(lineZombie.location.row, lineZombie.location.col, 0,
                  lineZombie.location.y));
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

    if (player.heading == Heading.NONE) //If standing still, draw that sprite
    {
      g.drawImage(player.still, player.location.getX(),
          player.location.getY(), null);
    }
    else //Otherwise, draw animation
    {
      g.drawImage(player.animation.getSprite(), player.location.getX(),
          player.location.getY(), null);
    }

    g.drawImage(randomZombie.animation.getSprite(),
        randomZombie.location.getX(),
        randomZombie.location.getY(), null);
    g.drawImage(lineZombie.animation.getSprite(), lineZombie.location.getX(),
        lineZombie.location.getY(), null);
  }

  @Override
  public void keyTyped(KeyEvent e)
  {

  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();

    while (GUI.running)
    {
      if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W)
      {
        System.out.println("Pressing up");
        player.heading = Heading.NORTH;
        player.move();
      }
      if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S)
      {
        player.heading = Heading.SOUTH;
        player.move();
      }
      if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D)
      {
        player.heading = Heading.EAST;
        player.move();
      }
      if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A)
      {
        player.heading = Heading.WEST;
        player.move();
      }
      player.animation.start();
      repaint();
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    player.heading = Heading.NONE;
    player.animation.stop();
  }

}