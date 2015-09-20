import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel implements KeyListener
{

  // How fast the timer should tick. Ranges from 35ish to 50ish.
  static final int FPS = 40;
  static final int SKIP_TICKS = 1000 / FPS;
  final static int SHOWN_TILES = 24;
  final static int DEFAULT_WIDTH = SHOWN_TILES * GUI.tile_size;
  final BufferedImage vignetteCanvas;
  private final ArrayList KEY_UP = new ArrayList<>(Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W));
  private final ArrayList KEY_DOWN = new ArrayList<>(Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S));
  private final ArrayList KEY_LEFT = new ArrayList<>(Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A));
  private final ArrayList KEY_RIGHT = new ArrayList<>(Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D));
  private final ArrayList KEY_RUN = new ArrayList<>(Arrays.asList(KeyEvent.VK_R, KeyEvent.VK_SHIFT));
  private final ArrayList KEY_PICKUP = new ArrayList<>(Arrays.asList(KeyEvent.VK_P, KeyEvent.VK_E));

  public GameMap map;
  public GameMap procedureGenMap;
  Timer frame_timer;
  int xScale;
  JViewport vp;
  private SoundLoader loadAmbience;
  private GUI parent;
  private Player player;

//  private Zombie zombie;
//  private FireTrap fireTrap;

  private SoundLoader sound;

  public GamePanel(GUI parent)
  {
    this.parent = parent;
    player = parent.player;
    map = parent.map;

    setBackground(Color.black);
    vignetteCanvas = makeVignette(player.getSight());


//    File map_file = null;
//    try
//    {
//      map_file =
//          new File(getClass().getResource("resources/level1.map").toURI());
//    }
//    catch (URISyntaxException e)
//    {
//      e.printStackTrace();
//    }


    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));

    //System.out.println("reached this point");

//    for(Zombie zombie : map.zombies) {
//      zombie.loadNoises();
//    }


//    for (FireTrap traps : map.traps)
//    {
//      traps.sound.loadExplosion();
//    }


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (GUI.running) {

          player.update(map);
          snapViewPortToPlayer();


          //Deletes a zombie from list once it explodes in fire trap.
          //Just removing it from a list won't work.
          Iterator<Zombie> zombieIter = map.zombies.iterator();
          Zombie zombie;
          while (zombieIter.hasNext())
          {
            zombie = zombieIter.next();

            zombie.update(map, player);

            if (zombie.zombieDied) zombieIter.remove();

            if (zombie.bitPlayer)
            {
              System.out.println("zombie bit player");
              //parent.running = false;
              parent.pauseGame();
              //stopAllSounds();
              GUI.showDeathDialog(parent);
            }


          }
          for (FireTrap trap : map.traps)
          {
            trap.update(map, player);
          }


          parent.updatePlayerLabels();
          parent.updateZombieLabels();

          if (player.playerDied)
          {
            //parent.running = false;
            parent.pauseGame();
            //stopAllSounds();
            GUI.showDeathDialog(parent);
          }

//          if(player.getFootTile(map).tile_type == TileType.EXIT)
//          {
//            parent.whichLevel++;
//            parent.newGame();
//
//          }
          repaint();

        }


      }
    });

  }

  private boolean onScreen(Object2D object)
  {
    return object.getLocation().x < vp.getWidth() &&
        object.getLocation().x > 0 &&
        object.getLocation().y < vp.getHeight() && object.getLocation().y > 0;
  }

  /**
   * Makes the screen follow the player and keeps him in the center of
   * the screen.
   */
  public void snapViewPortToPlayer()
  {
    JViewport parent_viewport = (JViewport) getParent();
    Rectangle viewport_rect = parent_viewport.getViewRect();
    double scale = ((double) parent_viewport.getWidth()) / DEFAULT_WIDTH;

    int new_x =
        (int) (player.getCenterPoint().x * scale - viewport_rect.width / 2);
    int new_y =
        (int) (player.getCenterPoint().y * scale - viewport_rect.height / 2);
    parent_viewport.setViewPosition(new Point(new_x, new_y));
  }


  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    vp = (JViewport) getParent();

    double scale = ((double) vp.getWidth()) / DEFAULT_WIDTH;
    g2.scale(scale, scale);

    map.paint(g2, GUI.tile_size);
//    System.out.println(vp.getWidth());
//    System.out.println(DEFAULT_WIDTH);
//    System.out.println(scale);

    // Math to make vignette move with viewport
    int width = vp.getWidth();
    int height = vp.getHeight();
    boolean explodee = false;

    for (FireTrap trap : map.traps)
    {
      if (!player.is_picking_up || player.is_putting_down || !trap.exploding)
      {
        g2.drawImage(trap.trap, trap.location.getX(), trap.location.getY(),
            null);
      }

      if (trap.exploding && onScreen(trap))
      {

        g2.drawImage(trap.fireAnimation.getSprite(),
            trap.location.getX(), trap.location.getY(), null);
        explodee = true;


      }


    }





    for(Zombie zombie : map.zombies) {
      if (!zombie.zombieDied)
      {
        g2.drawImage(zombie.animation.getSprite(), zombie.location.getX(),
            zombie.location.getY(), null);
      }
    }

    g2.drawImage(player.animation.getSprite(), player.location.getX(),
        player.location.getY(), null);

    int vcX = player.getCenterPoint().x - vignetteCanvas.getWidth() / 2;
    int vcY = player.getCenterPoint().y - vignetteCanvas.getHeight() / 2;


    if (!explodee) //draw if trap is not exploding
    {
      g2.drawImage(vignetteCanvas, vcX, vcY, null);

    }


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
    player.isStill = false;
    int code = e.getKeyCode();

    if (KEY_RUN.contains(code) && (player.getSpeed() != 0))
    {
      player.setRunning();
//      player.isRunning = true;
      player.isWalking = false;

    }
    if (KEY_UP.contains(code))
    {
      player.heading.setYMovement(Heading.NORTH_STEP);
      player.isRunning = false;
      player.isWalking = true;

    }
    if (KEY_DOWN.contains(code))
    {
      player.heading.setYMovement(Heading.SOUTH_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_RIGHT.contains(code))
    {
      player.heading.setXMovement(Heading.EAST_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_LEFT.contains(code))
    {
      player.heading.setXMovement(Heading.WEST_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_PICKUP.contains(code))
    {
      FireTrap t = map.traps.stream()
                            .filter(player::intersects)
                            .findFirst()
                            .orElse(null);

      if (t != null)
      {
        player.pickupFireTrap(t);
      }
      else if (player.getFire_traps() > 0)
      {
        player.is_putting_down = true;
        System.out.println("player put down trap");
      }
      else
      {
        System.out.println(player.getFire_traps());
      }
    }
  }



  @Override
  public void keyReleased(KeyEvent e)
  {
    player.isStill = true;
    player.isRunning = false;
    player.isWalking = false;
    int code = e.getKeyCode();

    if (KEY_UP.contains(code) || KEY_DOWN.contains(code)) {
      player.heading.setYMovement(0);
    }
    if (KEY_LEFT.contains(code) || KEY_RIGHT.contains(code)) {
      player.heading.setXMovement(0);
    }
    if (KEY_RUN.contains(code))
    {
      player.setWalking();
      //player.isWalking = true;
    }


  }

  public void startMusic()
  {
    loadAmbience.playLooped();
  }

  public void stopMusic()
  {
    loadAmbience.stop();
  }

  public void stopAllSounds()
  {
    SoundLoader.stopSounds();
  }

  public void loadMusic()
  {
    loadAmbience = new SoundLoader("ambience.wav");

  }
}
