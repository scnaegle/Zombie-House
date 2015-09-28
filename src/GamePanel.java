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

  // How fast the timer should tick.
  static final int FPS = 45;
  static final int SKIP_TICKS = 1000 / FPS;
  final static int SHOWN_TILES = 24;
  final static int DEFAULT_WIDTH = SHOWN_TILES * GUI.tile_size;
  final BufferedImage vignetteCanvas;

  //Special lists for key events
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

  GameMap map;
  Timer frame_timer;
  JViewport vp;
  Iterator<Zombie> zombieIter;
  Zombie zombie;
  Iterator<FireTrap> trapIter;
  FireTrap trap;
  BufferedImage lightLayer = null;
  private SoundLoader loadAmbience;
  private GUI parent;
  private Player player;
  private Shadow shadow;

  /**
   * The constructor of the gamepanel sets everything up: drawing, logic,
   * some sounds. Main game timer is created here.
   * @param parent
   */
  public GamePanel(GUI parent) //Takes in the GUI so it can uses it's info
  {
    this.parent = parent;
    map = new GameMap(parent.whichLevel);
    player = new Player(GUI.sight, GUI.hearing, GUI.speed, GUI.stamina, GUI.regen, 70, 70, map.start_location);
    player.setHeading(new Heading(Heading.NONE));

    setBackground(Color.black);
    vignetteCanvas = makeVignette(player.getSight());

    //Creates shadows based off of the player's location and sight.
    shadow = new Shadow(map);
    shadow.setPlayerSight(player.getSight());

    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (parent.running) //Game is running
        {

          player.update(map); //Asks player for animations, sounds, movement


          //Deletes a zombie from list once it explodes in fire trap.
          //Just removing it from a list won't work.
          zombieIter = map.zombies.iterator();
          while (zombieIter.hasNext())
          {
            zombie = zombieIter.next();

            zombie.update(map, player); //Updates zombie movement, smell, etc.

            if (zombie.zombieDied) zombieIter.remove();

            if (zombie.bitPlayer)  //Game over
            {
              parent.pauseGame();
              GUI.showDeathDialog(parent,
                  "Ye be bitten! Keep yer zombie erff yer tail by using yer " +
                      "fire traps!");
            }


          }

          //Checks all traps for collisions
          trapIter = map.traps.iterator();
          while (trapIter.hasNext())
          {
            trap = trapIter.next();
            trap.update(map, player);
            if (trap.remove_me)
            {
              trapIter.remove();
              shadow.loadMap(map);
            }
          }


          //Shows dialog if player died at any time
          if (player.playerDied)
          {
            parent.pauseGame();
            GUI.showDeathDialog(parent,
                "Ye ran into a fire trap! Feast your eyes and pay attention!");

          }


          // Checks if player made it to the exit tile
          if (player.reachedExit(map.end_location))
          {
            //If yes, then go to next level.
            parent.whichLevel++;
            if (parent.whichLevel == parent.winLevel)
            {
              parent.pauseGame();
              parent.showWinningDialog(parent, " You won the game!");
            }
            System.out.println("Next level");
            newMapByExit();
          }

          // Calculates shadows
          shadow.setDimensions(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT);
          Point center_point = player.getCenterPoint();
          shadow.setLightLocation((int) center_point.getX(), (int) center_point.getY());
          shadow.sweep();

          snapViewPortToPlayer();  //Makes viewport follow player
        }
        repaint();

      }
    });
  }

  //Starts a new game with a new map
  private void newMapByExit()
  {
    GameMap new_map = new GameMap(parent.whichLevel);
    map = new_map;
    player.location = new_map.start_location;
    shadow.loadMap(map);
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
    vp = GUI.scrollPane.getViewport();


    //For resizing purposes
    double scale = ((double) vp.getWidth()) / DEFAULT_WIDTH;
    g2.scale(scale, scale);

    g2.drawImage(map.map_image, 0, 0, null);

    //When to draw traps and which sprite
    boolean explodee = false;
    FireTrap activeTrap = null;
    for (FireTrap trap : map.traps)
    {
      trap.paint(g2, player);

      explodee = trap.explodee;
      activeTrap = trap;
    }

    //Draws zombies
    for (Zombie zombie : map.zombies)
    {
      zombie.paint(g2);
    }


    //Finds starting point on screen according to player location
    //and draws vignette as player moves.
    int vcX = player.getCenterPoint().x - vignetteCanvas.getWidth() / 2;
    int vcY = player.getCenterPoint().y - vignetteCanvas.getHeight() / 2;

    g2.drawImage(vignetteCanvas, vcX, vcY, null);


    shadow.paint(g2);


    player.paint(g2);
    paintTextOverlay(g2);


  }

  //Uses radial gradients to draw vignette around player.
  private BufferedImage makeVignette(int sight)
  {
    BufferedImage img = new BufferedImage(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size), BufferedImage.TYPE_4BYTE_ABGR);
    Graphics2D g = (Graphics2D) img.getGraphics();

    float sight_pixels = (float) sight * GUI.tile_size;
    Point2D center = new Point2D.Float(map.getWidth(GUI.tile_size) / 2,
        map.getHeight(GUI.tile_size) / 2);
    Color[] colors = {new Color(1f, 1f, 1f, 0f), Color.black};
    float[] dist = {0.0f, 1f};
    RadialGradientPaint p = new RadialGradientPaint(center, sight_pixels,
        dist, colors);


    g.setPaint(p);
    g.fillRect(0, 0, map.getWidth(GUI.tile_size), map.getHeight(GUI
        .tile_size));

    return img;
  }


  //Uses the scrollpane dimensions to draw text on the screen for user
  private void paintTextOverlay(Graphics2D g)
  {
    vp = GUI.scrollPane.getViewport();
    int new_x = ((int) player.location.x - GUI.SCENE_WIDTH / 2);
    int new_y = ((int) player.location.y - GUI.SCENE_HEIGHT / 2);
    int width = GUI.SCENE_WIDTH;

    new_x += 50;
    new_y += 100;
    g.setColor(Color.white);
    Font font = new Font("Courier", Font.BOLD, 35);
    g.setFont(font);
    g.drawString("Level: " + parent.whichLevel, new_x,
        new_y);
    g.drawString("Fire traps: " + player.getFire_traps(), new_x,
        new_y + 50);


    if (!GUI.running)
    {
      g.setColor(Color.RED);
      g.drawString("Press SPACE to start", new_x + width / 2 - 220, new_y);
    }



  }



  @Override
  public void keyTyped(KeyEvent e)
  {
  }

  @Override
  public void keyPressed(KeyEvent e)
  {
    int code = e.getKeyCode();

    if (code == KeyEvent.VK_SPACE) //Pause/start
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
    if (KEY_RUN.contains(code)) //Presses 'r'
    {
      player.setRunning();
    }
    if (KEY_UP.contains(code)) //Up arrow or 'w'
    {
      player.heading.setYMovement(Heading.NORTH_STEP);
    }
    if (KEY_DOWN.contains(code)) //Down arrow or 's'
    {
      player.heading.setYMovement(Heading.SOUTH_STEP);
    }
    if (KEY_RIGHT.contains(code)) //Right arrow or 'd'
    {
      player.heading.setXMovement(Heading.EAST_STEP);
    }
    if (KEY_LEFT.contains(code)) //Left arrow or 'a'
    {
      player.heading.setXMovement(Heading.WEST_STEP);
    }
    if (KEY_PICKUP.contains(code)) //Presses 'p'
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
        player.dropFireTrap();
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
    int code = e.getKeyCode();

    //Tells player to pretty much stop moving, unless running turns into
    // walking.

    if (KEY_UP.contains(code) || KEY_DOWN.contains(code)) {
      player.heading.setYMovement(0);
    }
    if (KEY_LEFT.contains(code) || KEY_RIGHT.contains(code)) {
      player.heading.setXMovement(0);
    }
    if (KEY_RUN.contains(code))
    {
      player.setWalking();
    }
  }

  /**
   * Loops the background sound
   */
  public void startMusic()
  {
    loadAmbience.playLooped();
  }

  /**
   * Stops background sound
   */
  public void stopMusic()
  {
    loadAmbience.stop();
  }

  /**
   * Tells all sounds to stop playing
   */
  public void stopAllSounds()
  {
    SoundLoader.stopSounds();
  }

  /**
   * Loads in background music.
   */
  public void loadMusic()
  {
    loadAmbience = new SoundLoader("ambience.wav");

  }
}
