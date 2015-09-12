import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;


/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel implements KeyListener
{
  final int FRAMES_PER_SECOND = 60;
  final int SKIP_TICKS = 1000 / FRAMES_PER_SECOND;

  private final ArrayList KEY_UP = new ArrayList<>(Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W));
  private final ArrayList KEY_DOWN = new ArrayList<>(Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S));
  private final ArrayList KEY_LEFT = new ArrayList<>(Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A));
  private final ArrayList KEY_RIGHT = new ArrayList<>(Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D));

  public Player player = new Player(new Location(20, 20, 500, 200));
  Timer frame_timer;
  private GameMap map;
  private Zombie randomZombie =
      new RandomWalkZombie(new Location(200, 200));
  private Zombie lineZombie =
      new LineWalkZombie(new Location(300, 300));
  private Zombie masterZ =
      new MasterZombie(new Location(100, 100));

  private FireTrap fireTrap = new FireTrap(new Location(50, 50, 100, 100));
  private FireTrap explodingTrap = new FireTrap(new Location(20, 10, 200, 100));

  public GamePanel()
  {

//    System.out.println("scene_width: " + GUI.SCENE_WIDTH);
//    System.out.println("scene_height: " + GUI.SCENE_HEIGHT);
//    setPreferredSize(new Dimension(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT - 25));
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

    player.setHeading(new Heading(Heading.NONE));
    player.setSpeed(1.0);
    System.out.println("Player initialized");

    randomZombie.setHeading(Heading.WEST);
    lineZombie.setHeading(Heading.EAST);

    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (GUI.running) {
          player.update();

          randomZombie.move();
          if (randomZombie.location.x < 0) {
            randomZombie.setLocation(
                new Location(randomZombie.location.row, randomZombie.location.col,
                    GUI.SCENE_WIDTH, randomZombie.location.y));
          }
          lineZombie.move();
          if (lineZombie.location.x > GUI.SCENE_WIDTH) {
            lineZombie.setLocation(
                new Location(lineZombie.location.row, lineZombie.location.col, 0,
                    lineZombie.location.y));
          }

          explodingTrap.move();
//          player.animation.start();
          repaint();
        }
      }
    });
//    frame_timer.start();

  }


  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    map.paint(g, GUI.tile_size);

//    System.out.println("player location: " + player.location.toString());
    g.drawImage(player.animation.getSprite(), player.location.getX(),
          player.location.getY(), null);

    g.drawImage(fireTrap.trap, fireTrap.location.getX(),
        fireTrap.location.getY(), null);
    g.drawImage(explodingTrap.fireAnimation.getSprite(),
        fireTrap.location.getX(), fireTrap.location.getY(), null);
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

    if (KEY_UP.contains(code))
    {
      player.heading.setYMovement(Heading.NORTH_STEP);
    }
    if (KEY_DOWN.contains(code))
    {
      player.heading.setYMovement(Heading.SOUTH_STEP);
    }
    if (KEY_RIGHT.contains(code))
    {
      player.heading.setXMovement(Heading.EAST_STEP);
    }
    if (KEY_LEFT.contains(code))
    {
      player.heading.setXMovement(Heading.WEST_STEP);
    }
  }

  @Override
  public void keyReleased(KeyEvent e)
  {
    int code = e.getKeyCode();

    if (KEY_UP.contains(code) || KEY_DOWN.contains(code)) {
      player.heading.setYMovement(0);
    }
    if (KEY_LEFT.contains(code) || KEY_RIGHT.contains(code)) {
      player.heading.setXMovement(0);
    }
  }
}