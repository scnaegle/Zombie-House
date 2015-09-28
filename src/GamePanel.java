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

  public GamePanel(GUI parent) //Takes in the GUI so it can uses it's info
  {
    this.parent = parent;
    map = new GameMap(parent.whichLevel);
    player = new Player(GUI.sight, GUI.hearing, GUI.speed, GUI.stamina, GUI.regen, 70, 70, map.start_location);
    player.setHeading(new Heading(Heading.NONE));

    setBackground(Color.black);
    vignetteCanvas = makeVignette(player.getSight());

    shadow = new Shadow(map);
    shadow.setPlayerSight(player.getSight());

    setPreferredSize(new Dimension(map.getWidth(GUI.tile_size),
        map.getHeight(GUI.tile_size)));


    frame_timer = new Timer(SKIP_TICKS, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (parent.running)
        {
          //long end, start = System.currentTimeMillis();
          //System.out.println("timer going off");
          player.update(map); //Asks player for animations, sounds, movement


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
              //System.out.println("zombie bit player");
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
            if (parent.whichLevel == parent.winLevel)
            {
              parent.pauseGame();
              parent.showWinningDialog(parent, " You won the game!");
            }
            System.out.println("Next level");
            newMapByExit();
          }
          //end = System.currentTimeMillis();
          //System.out.printf("Everything not painting took %dms%n", end - start);

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
    shadow = new Shadow(map);
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
    long start = System.currentTimeMillis();
    Graphics2D g2 = (Graphics2D) g;
    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON);
    vp = GUI.scrollPane.getViewport();
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

      explodee = trap.explodee;
      activeTrap = trap;
    }

    //Draws zombies
    for (Zombie zombie : map.zombies)
    {
      zombie.paint(g2);
    }


    int vcX = player.getCenterPoint().x - vignetteCanvas.getWidth() / 2;
    int vcY = player.getCenterPoint().y - vignetteCanvas.getHeight() / 2;

    if (!explodee)
    {
      g2.drawImage(vignetteCanvas, vcX, vcY, null);
    }


    System.out.println("player location: " + player.location);
    shadow.paint(g2);

    //Draws vignette with player at center.
//    int absWidth = 2000;
//    int absHeight = 1500;
//    int vcX = player.getCenterPoint().x - absWidth / 2;
//    int vcY = player.getCenterPoint().y - absHeight / 2;
//
////    start = System.currentTimeMillis();
//    lightLayer =
//        new BufferedImage(absWidth, absHeight, BufferedImage.TYPE_4BYTE_ABGR);
//
//    Graphics2D lightGraphics = (Graphics2D) lightLayer.getGraphics();
//    lightGraphics.setColor(Color.black);
//    lightGraphics.fillRect(0, 0, absWidth, absHeight);
//
//    Composite composite = lightGraphics.getComposite();
//    lightGraphics.setComposite(
//        AlphaComposite.getInstance(AlphaComposite.SRC_IN, 1f));
//
//    lightGraphics.setColor(new Color(0, 0, 0, 0));
//    lightGraphics
//        .fillOval(lightLayer.getWidth() / 2 - player.getSight() * GUI
// .tile_size,
//            lightLayer.getHeight() / 2 - player.getSight() * GUI.tile_size,
//            2 * player.getSight() * GUI.tile_size,
//            2 * player.getSight() * GUI.tile_size);
//    //drawPlayerLight(player.getSight(), lightGraphics);
//
//
//    //drawFireLight(activeTrap,lightGraphics);
//    if (explodee)
//    {
//      lightGraphics
//          .fillOval((int) activeTrap.explosionObj.getX(),
//              (int) activeTrap.explosionObj.getY(),
//              (int) activeTrap.explosionObj.getWidth() * GUI.tile_size,
//              (int) activeTrap.explosionObj.getHeight() * GUI.tile_size);
//    }
//    lightGraphics.setComposite(composite);
////    System.out.println(System.currentTimeMillis() - start + " 1");
//    g2.drawImage(lightLayer, vcX, vcY, null);
    //g2.drawImage(vignetteCanvas, vcX, vcY, null);


    player.paint(g2);
    paintTextOverlay(g2);


  }

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


  private void paintTextOverlay(Graphics2D g)
  {
    vp = GUI.scrollPane.getViewport();
    Rectangle vp_rect = vp.getViewRect();
    int new_x = ((int) player.location.x - GUI.SCENE_WIDTH / 2);
    int new_y = ((int) player.location.y - GUI.SCENE_HEIGHT / 2);
    int width = GUI.SCENE_WIDTH;
    int height = GUI.SCENE_HEIGHT;
//    System.out.format("Viewport location: x=%d, y=%d\n", new_x, new_y);
//    System.out.format("Viewport width: %d, height: %d\n", width, height);
//    System.out.format("Player location: x=%f, y=%f\n", player.location.x,
// player.location.y);
//    System.out.format("Player top left: x=%d, y=%d\n", player
// .getCenterPoint().x - width / 2, player.getCenterPoint().y - height / 2);

//    g.setColor(Color.BLUE);
////    g2.drawRect(new_x, new_y, vp.getWidth(), vp.getHeight());
//    g.draw(vp.getViewRect());
//    g.setColor(Color.RED);
//    g.drawRect(player.getCenterPoint().x - width / 2,
//        player.getCenterPoint().y - height / 2, width, height);

    new_x += 50;
    new_y += 100;
    g.setColor(Color.white);
    Font font = new Font("Courier", Font.BOLD, 35);
    g.setFont(font);
    g.drawString("Level: " + parent.whichLevel, new_x,
        new_y);
    g.drawString("Fire traps: " + player.getFire_traps(), new_x,
        new_y + 50);
//    g.drawString("Stamina", new_x + width - 200, new_y);

    if (!GUI.running)
    {
      g.setColor(Color.RED);
      g.drawString("Press SPACE to start", new_x + width / 2 - 220, new_y);
    }



  }

  private void drawFireLight(FireTrap trap, Graphics2D g2)
  {
    float radius = (float) trap.explosionObj.getWidth() * GUI.tile_size;
    Point2D center = new Point2D.Float((float) trap.explosionObj.getWidth() / 2,
        (float) trap.explosionObj.getHeight() / 2);
    Color[] colors = {new Color(1f, 1f, 1f, 0f), Color.black};
    float[] dist = {0.0f, 1f};
    RadialGradientPaint p =
        new RadialGradientPaint(center, radius, dist, colors);

    g2.setPaint(p);
    g2.fillRect(0, 0, (int) trap.explosionObj.getWidth(),
        (int) trap.explosionObj.getHeight());

  }


  /**
   * Uses radial gradient to draw a vignette with the player's location
   * at the center.
   *
   * @param sight Uses player as radius for vignette opening
   * @return A buffered image
   */
  private void drawPlayerLight(int sight, Graphics2D g2)
  {
    float sight_pixels = (float)sight*GUI.tile_size;
    Point2D center = new Point2D.Float(lightLayer.getWidth() / 2,
        lightLayer.getHeight() / 2);
    Color[] colors = {new Color(1f,1f,1f,0f),  Color.black};
    float[] dist = {0.8f, 1f};
    RadialGradientPaint p = new RadialGradientPaint(center,sight_pixels,dist,colors);


    g2.setPaint(p);
    g2.fillRect(0, 0, GUI.SCENE_WIDTH, GUI.SCENE_WIDTH);


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
