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
  static final int START_GAME = 1;
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

  GameMap map;
  Timer frame_timer;
  JViewport vp;
  Iterator<Zombie> zombieIter;
  Zombie zombie;
  Iterator<FireTrap> trapIter;
  FireTrap trap;
  private SoundLoader loadAmbience;
  private GUI parent;
  private Player player;

  public GamePanel(GUI parent) //Takes in the GUI so it can uses it's info
  {
    this.parent = parent;
    map = new GameMap(parent.whichLevel);
    player = new Player(GUI.sight, GUI.hearing, GUI.speed, GUI.stamina, GUI.regen, 70, 70, map.start_location);
    player.setHeading(new Heading(Heading.NONE));

    setBackground(Color.black);
    vignetteCanvas = makeVignette(player.getSight());


    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (GUI.running)
        {
          //System.out.println("timer going off");
          player.update(map); //Asks player for animations, sounds, movement
          snapViewPortToPlayer();  //Makes viewport follow player


          //Deletes a zombie from list once it explodes in fire trap.
          //Just removing it from a list won't work.
          zombieIter = map.zombies.iterator();
          while (zombieIter.hasNext())
          {
            zombie = zombieIter.next();

            zombie.update(map, player);

            if (zombie.zombieDied) zombieIter.remove();

            if (zombie.bitPlayer)  //Game over
            {
              System.out.println("zombie bit player");
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
            if (parent.whichLevel == 6)
            {
              GUI.showWinningDialog(parent, " You won the game!");
            }
            System.out.println("Next level");
            newMapByExit();
          }

          repaint();

        }


      }
    });
  }

  //Starts a new game with a new map
  private void newMapByExit()
  {
    GameMap new_map = new GameMap(parent.whichLevel);
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
    boolean explodee = false;
    FireTrap activeTrap = null;
    for (FireTrap trap : map.traps)
    {
      trap.paint(g2, player);
      if (trap.exploding)
      {
        explodee = true;
        activeTrap = trap;

      }

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
//      lightGraphics.drawImage(light, (int)activeTrap.explosionObj.getX(),
//          (int) activeTrap.explosionObj.getY(), null);
//    }
//
//    g2.drawImage(lightLayer, vcX, vcY, null);
    g2.drawImage(vignetteCanvas, vcX, vcY, null);


    vp = GUI.scrollPane.getViewport();
    Rectangle vp_rect = vp.getViewRect();
    int new_x = ((int)vp_rect.getX());
    int new_y = ((int)vp_rect.getY());
    int width = GUI.SCENE_WIDTH;
    int height = GUI.SCENE_HEIGHT;
//    System.out.format("Viewport location: x=%d, y=%d\n", new_x, new_y);
//    System.out.format("Viewport width: %d, height: %d\n", width, height);
//    System.out.format("Player location: x=%f, y=%f\n", player.location.x,
// player.location.y);
//    System.out.format("Player top left: x=%d, y=%d\n", player
// .getCenterPoint().x - width / 2, player.getCenterPoint().y - height / 2);

    g2.setColor(Color.BLUE);
//    g2.drawRect(new_x, new_y, vp.getWidth(), vp.getHeight());
    g2.draw(vp.getViewRect());
    g2.setColor(Color.RED);
    g2.drawRect(player.getCenterPoint().x - width / 2, player.getCenterPoint().y - height / 2, width, height);

    new_x += 50;
    new_y += 50;
    g2.setColor(Color.white);
    Font font = new Font("Courier", Font.BOLD, 35);
    g2.setFont(font);
    g2.drawString("Level: " + parent.whichLevel, new_x,
        new_y - 50);
    g2.drawString("Fire traps: " + player.getFire_traps(), new_x,
        new_y);
    g2.drawString("Stamina", new_x + vp.getWidth(), new_y);

    if (!GUI.running)
    {
      g2.drawString("Press SPACE", new_x, new_y + vp.getHeight());
    }


    player.paint(g2,vp);
    g2.setColor(Color.BLUE);
    //g2.fillRect(20, GUI.SCENE_HEIGHT - 200, 20, 80);

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


  /**
   * Uses radial gradient to draw a vignette with the player's location
   * at the center.
   *
   * @param sight Uses player as radius for vignette opening
   * @return A buffered image
   */
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
    int code = e.getKeyCode();

    if (code == KeyEvent.VK_SPACE)
    {
      if (!GUI.running)
      {
        parent.startGame();
        requestFocusInWindow();
      }
      else
      {
        parent.pauseGame();
      }
    }
    if (KEY_RUN.contains(code))
    {
      player.setRunning();
    }
    if (KEY_UP.contains(code))
    {
      System.out.println("up");
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
