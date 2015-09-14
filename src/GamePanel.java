import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
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

  // How fast the timer should tick. Ranges from 35ish to 50ish.
  static final int FPS = 60;
  static final int SKIP_TICKS = 1000 / FPS;
  final BufferedImage vignetteCanvas;
  private final ArrayList KEY_UP = new ArrayList<>(Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W));
  private final ArrayList KEY_DOWN = new ArrayList<>(Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S));
  private final ArrayList KEY_LEFT = new ArrayList<>(Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A));
  private final ArrayList KEY_RIGHT = new ArrayList<>(Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D));
  private final ArrayList KEY_R = new ArrayList<>(Arrays.asList(KeyEvent.VK_R));
  Timer frame_timer;
  private SoundLoader loadAmbience;
  private GUI parent;
  private Player player;
  private GameMap map;
  private Zombie randomZombie =
      new RandomWalkZombie(new Location(700, 1200));
  private Zombie lineZombie =
      new LineWalkZombie(new Location(800, 1300));
  private Zombie masterZ =
      new MasterZombie(new Location(100, 100));
  private FireTrap fireTrap = new FireTrap(new Location(50, 50, 100, 100));
  private FireTrap explodingTrap = new FireTrap(new Location(20, 10, 200, 100));


  public GamePanel(GUI parent)
  {
    this.parent = parent;
    player = parent.player;

    setBackground(Color.white);
    vignetteCanvas = makeVignette(player.getSight());


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
    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));

    //player.setHeading(new Heading(Heading.NONE));
    //player.setLocation(new Location(GUI.SCENE_WIDTH / 2, GUI.SCENE_HEIGHT /
    // 2));
    //snapViewPortToPlayer();


    randomZombie.setHeading(new Heading(Heading.WEST));
    lineZombie.setHeading(new Heading(Heading.EAST));

    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (GUI.running) {

          player.update(map);
          snapViewPortToPlayer();



          randomZombie.update(map, player);
          if (randomZombie.location.x < 0) {
            randomZombie.setLocation(
                new Location(GUI.SCENE_WIDTH, randomZombie.location.y));
          }
          lineZombie.update(map, player);
          if (lineZombie.location.x > GUI.SCENE_WIDTH) {
            lineZombie.setLocation(
                new Location(0, lineZombie.location.y));
          }

          explodingTrap.move();
          repaint();

        }

      }
    });
  }

  public void snapViewPortToPlayer()
  {
    /**
     * Makes the screen follow the player and keeps him in the center of
     * the screen.
     */
    JViewport parent_viewport = (JViewport) getParent();
    Rectangle viewport_rect = parent_viewport.getViewRect();
    int new_x = (int) (player.location.x - viewport_rect.width / 2);
    int new_y = (int) (player.location.y - viewport_rect.height / 2);
    parent_viewport.setViewPosition(new Point(new_x, new_y));
  }


  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    map.paint(g, GUI.tile_size);

    g.drawImage(fireTrap.trap, fireTrap.location.getX(),
        fireTrap.location.getY(), null);
    g.drawImage(explodingTrap.fireAnimation.getSprite(),
        fireTrap.location.getX(), fireTrap.location.getY(), null);
    g.drawImage(randomZombie.animation.getSprite(),
        randomZombie.location.getX(),
        randomZombie.location.getY(), null);
    g.drawImage(lineZombie.animation.getSprite(), lineZombie.location.getX(),
        lineZombie.location.getY(), null);

    g.drawImage(player.animation.getSprite(), player.location.getX(),
        player.location.getY(), null);

    // Math to make vignette move with viewport
    JViewport vp = (JViewport) getParent();
    int width = vp.getWidth();
    int height = vp.getHeight();
    int x = vp.getViewPosition().x - (vignetteCanvas.getWidth() - width) / 2;
    int y = vp.getViewPosition().y - (vignetteCanvas.getHeight() - height) / 2;

    g.drawImage(vignetteCanvas, x, y, null);
  }

  private BufferedImage makeVignette(int sight)
  {
    BufferedImage img = new BufferedImage(GUI.SCENE_WIDTH,GUI.SCENE_HEIGHT,
                                          BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D) img.getGraphics();

    float sight_pixels = (float)sight*GUI.tile_size;
    Point2D center = new Point2D.Float(GUI.SCENE_WIDTH/2, GUI.SCENE_HEIGHT/2);
    Color[] colors = {new Color(1f,1f,1f,0f),  Color.black};
    float[] dist = {0.0f, 1f};
    RadialGradientPaint p = new RadialGradientPaint(center,sight_pixels,dist,colors);


    g.setPaint(p);
    g.fillRect(0, 0, GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT);

    return img;
  }



  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();

    if (KEY_R.contains(code) && (player.getSpeed() != 0))
    {
      player.setRunning();

    }
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
    //player.playSound();

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
    player.stopSound();


  }

  public void startMusic()
  {
    loadAmbience.play();
  }

  public void stopMusic()
  {
    loadAmbience.stop();
  }

  public void loadMusic()
  {
    loadAmbience = new SoundLoader("ambience.wav");

  }
}
