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
 * GamePanel holds the game, along with the Keyevents to make the player move,
 * and uses it's parent, GUI, to keep track of variables.
 * Having this larger class that extends JPanel allows easier access to
 * drawing, moving, and sizing.
 */
public class GamePanel extends JPanel implements KeyListener
{

  // How fast the timer should tick. Ranges from 35ish to 50ish.
  static final int FPS = 45;
  static final int SKIP_TICKS = 1000 / FPS;
  final static int SHOWN_TILES = 24;
  final static int DEFAULT_WIDTH = SHOWN_TILES * GUI.tile_size;
  final BufferedImage vignetteCanvas;
  private final ArrayList KEY_UP =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_UP, KeyEvent.VK_W));
  private final ArrayList KEY_DOWN =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_DOWN, KeyEvent.VK_S));
  private final ArrayList KEY_LEFT =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_LEFT, KeyEvent.VK_A));
  private final ArrayList KEY_RIGHT =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_RIGHT, KeyEvent.VK_D));
  private final ArrayList KEY_RUN =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_R, KeyEvent.VK_SHIFT));
  private final ArrayList KEY_PICKUP =
      new ArrayList<>(Arrays.asList(KeyEvent.VK_P, KeyEvent.VK_E));

  public GameMap map;
  Timer frame_timer;
  JViewport vp;
  private SoundLoader loadAmbience;
  private GUI parent;
  private Player player;

  public GamePanel(GUI parent) //Takes in the GUI so it can uses it's info
  {
    this.parent = parent;
    player = parent.player;
    map = parent.map;

    setBackground(Color.black);
    vignetteCanvas = makeVignette(player.getSight());


    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (parent.running)
        {
          //System.out.println("timer going off");
          player.update(map); //Asks player for animations, sounds, movement
          snapViewPortToPlayer();  //Makes viewport follow player


          //Deletes a zombie from list once it explodes in fire trap.
          //Just removing it from a list won't work.
          Iterator<Zombie> zombieIter = map.zombies.iterator();
          Zombie zombie;
          while (zombieIter.hasNext())
          {
            zombie = zombieIter.next();

            zombie.update(map, player);

            if (zombie.zombieDied) zombieIter.remove();

            if (zombie.bitPlayer)  //Game over
            {
              System.out.println("zombie bit player");
              //parent.running = false;
              parent.pauseGame();
              //stopAllSounds();

              GUI.showDeathDialog(parent,
                  "Ye be bitten! Keep yer zombie erff yer tail by using yer " +
                      "fire traps!");
            }


          }
          Iterator<FireTrap> trapIter = map.traps.iterator();
          FireTrap trap;
          while (trapIter.hasNext())
          {
            trap = trapIter.next();
            trap.update(map, player);
            if (trap.remove_me)
            {
              trapIter.remove();
            }
          }


          //Shows dialog if player died at any time
          if (player.playerDied)
          {
            //parent.running = false;
            parent.pauseGame();
            //stopAllSounds();

            GUI.showDeathDialog(parent,
                "Ye ran into a fire trap! Feast your eyes and pay attention!");

          }


          // Checks if player made it to the exit tile
          if (player.reachedExit(map.end_location))
          {
            //If yes, then go to next level.
            parent.whichLevel++;
            if (parent.whichLevel == 6)
            {
              parent.showWinningDialog(parent, " You won the game!");
            }
            //SoundLoader.killSounds();
            System.out.println("Next level");
            newMapByExit();
          }

          repaint();

        }


      }
    });
  }


  private void newMapByExit() //Starts a new game with a new map
  {
    //SoundLoader.stopSounds();
    GameMap new_map = new GameMap(parent.whichLevel);
//    parent.map = new_map;
    map = new_map;
    player.location = new_map.start_location;
    //parent.loadSounds();
  }

  public void newMap() //Starts a new game with a new map
  {
    GameMap new_map = new GameMap();
    parent.map = new_map;
    map = new_map;
    player.location = new_map.start_location;
  }

  private boolean onScreen(GameObject object)
  {
    Point vp_point = vp.getViewPosition();
    return object.location.x < vp_point.x + vp.getWidth() &&
        object.location.x > vp_point.x &&
        object.location.y < vp_point.y + vp.getHeight() &&
        object.location.y > vp_point.y;
  }

  /**
   * Makes the screen follow the player and keeps him in the center of
   * the screen.
   */
  public void snapViewPortToPlayer()
  {
    JViewport parent_viewport = (JViewport) getParent(); //From scrollpane
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
    BufferedImage light;

    //For resizing purposes
    double scale = ((double) vp.getWidth()) / DEFAULT_WIDTH;
    g2.scale(scale, scale);

    g2.drawImage(map.map_image, 0, 0, null);


    //When to draw traps and which sprite
    for (FireTrap trap : map.traps)
    {
      trap.paint(g2, player);
    }

    //Draws zombies
    for (Zombie zombie : map.zombies)
    {
      zombie.paint(g2);
    }


    //Draws vignette with player at center.
    int vcX = player.getCenterPoint().x - vignetteCanvas.getWidth() / 2;
    int vcY = player.getCenterPoint().y - vignetteCanvas.getHeight() / 2;

//    BufferedImage lightLayer = new BufferedImage(vignetteCanvas.getWidth(),
//        vignetteCanvas.getHeight(),BufferedImage.TYPE_4BYTE_ABGR);
//    Graphics2D lightGraphics = (Graphics2D) lightLayer.getGraphics();
//    lightGraphics.drawImage(vignetteCanvas, 0, 0, null);
//    lightGraphics.setComposite(
//        AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1f));
//
//    if(explodee)
//    {
//      light = drawFireLight(activeTrap);
//      lightGraphics.drawImage(light, (int) activeTrap.explosionObj.getX(),
//          (int) activeTrap.explosionObj.getY(), null);
//    }

    //g2.drawImage(lightLayer, vcX, vcY, null);
    g2.drawImage(vignetteCanvas, vcX, vcY, null);

    int new_x = (player.getLocation().getX() - vp.getWidth() / 2);
    int new_y = (player.getLocation().getY() - vp.getHeight() / 2);

    g2.setColor(Color.white);
    Font font = new Font("Courier", Font.BOLD, 35);
    g2.setFont(font);
    g2.drawString("Level: " + parent.whichLevel, new_x - 200,
        new_y - 100);
    g2.drawString("Fire traps: " + player.getFire_traps(), new_x - 200,
        new_y - 50);
    g2.drawString("Stamina", new_x + 1320, new_y - 100);

    if (!parent.running)
    {
      g2.drawString("Press SPACE", new_x - 200, new_y + 800);
    }

    player.paint(g2);
    g2.setColor(Color.BLUE);
    g2.fillRect(20, GUI.SCENE_HEIGHT - 200, 20, 80);

  }

  private BufferedImage drawFireLight(FireTrap trap)
  {
    BufferedImage img = new BufferedImage((int) trap.explosionObj.getWidth()
        , (int) trap.explosionObj.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

    Graphics2D g = (Graphics2D) img.getGraphics();
    float radius = (float) trap.explosionObj.getWidth() * GUI.tile_size;
    Point2D center = new Point2D.Float((float) trap.explosionObj.getWidth() / 2,
        (float) trap.explosionObj.getHeight() / 2);
    Color[] colors = {new Color(1f, 1f, 1f, 0f), Color.black};
    float[] dist = {0.0f, 1f};
    RadialGradientPaint p =
        new RadialGradientPaint(center, radius, dist, colors);

    g.setPaint(p);
    g.fillRect(0, 0, (int) trap.explosionObj.getWidth(),
        (int) trap.explosionObj.getHeight());

    return img;
  }


  private BufferedImage makeVignette(int sight)
  {
    BufferedImage img = new BufferedImage(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();

    float sight_pixels = (float)sight*GUI.tile_size;
    Point2D center = new Point2D.Float(map.getWidth(GUI.tile_size) / 2,
        map.getHeight(GUI.tile_size) / 2);
    Color[] colors = {new Color(1f,1f,1f,0f),  Color.black};
    float[] dist = {0.0f, 1f};
    RadialGradientPaint p = new RadialGradientPaint(center,sight_pixels,dist,colors);


    g.setPaint(p);
    g.fillRect(0, 0, map.getWidth(GUI.tile_size), map.getHeight(GUI.tile_size));

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

    if (code == KeyEvent.VK_SPACE)
    {
      if (!parent.running)
      {
        parent.startGame();
        requestFocusInWindow();
      }
      else
      {
        parent.pauseGame();
      }
    }
    if (KEY_RUN.contains(code) && (player.getSpeed() != 0))
    {
      // System.out.println("R");
      player.setRunning();
      player.isRunning = true;
      player.isWalking = false;

    }
    if (KEY_UP.contains(code))
    {
      //System.out.println("up");
      player.heading.setYMovement(Heading.NORTH_STEP);
      player.isRunning = false;
      player.isWalking = true;

    }
    if (KEY_DOWN.contains(code))
    {
      //System.out.println("down");
      player.heading.setYMovement(Heading.SOUTH_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_RIGHT.contains(code))
    {
      //System.out.println("right");
      player.heading.setXMovement(Heading.EAST_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_LEFT.contains(code))
    {
      //System.out.println("left");
      player.heading.setXMovement(Heading.WEST_STEP);
      player.isRunning = false;
      player.isWalking = true;
    }
    if (KEY_PICKUP.contains(code))
    {
      //System.out.println("p");
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
        player.dropFireTrap();
        //System.out.println("player put down trap");
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
    //System.out.println("released");
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
