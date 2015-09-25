/**
 * GUI class creates the settings window, initializes map, player, and sounds,
 * creates actual game window, and creates scrollpane which lets us follow the
 * player around.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * makes the GUI and all the things that go along with it
 */
public class GUI
{
  final static int tile_size = 80;
  //Making attributes static so every class can use them
  public static double speed;
  public static int hearing;
  public static int sight;
  public static double stamina;
  public static double regen;
  public static double zspeed;
  public static double zsmell;
  public static double drate;
  public static double zspawn;
  public static double fspawn;
  static int SCENE_WIDTH = 1920;
  static int SCENE_HEIGHT = 1080;
  static JPanel viewPanel;
  static boolean running = false;
  static JScrollPane scrollPane;
  public int whichLevel = 1;
  public GameMap map;
  public Player player;
  JFrame window = new JFrame("Zombie House");
  GamePanel gamePanel;
  private JLabel level;
  private JLabel playerSight;
  private JLabel playerHearing;
  private JLabel playerSpeed;
  private JLabel playerStamina;
  private JLabel zombieSpeed;
  private JLabel zombieSmell;
  private JButton startPause;
  private boolean pause = true;
  private FireTrap fireTrap;
  private JLabel traps;
  private boolean gameStarted = false;

  /**
   * Window that pops up once player is bitten or explodes.
   *
   * @param parent  Takes in the gui as a parent
   * @param message Takes a message
   */
  public static void showDeathDialog(GUI parent, String message)
  {

    Object[] options = {"Restart", "Exit"};

    int option = JOptionPane
        .showOptionDialog(parent.window, "Aaargghh! " + message + " Try again matey?",
            "YOU DIED",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
            options, options[0]);

    if (option == 0) //Restart
    {
      parent.whichLevel = 1;
      parent.window.dispose();
      parent.getSettings();
    }
    else if (option == 1) //Exit
    {
      System.exit(0);
    }
  }

  /**
   * come up if you win the game by completing 5 levels or something like that
   * @param parent
   * @param message
   */
  public static void showWinningDialog(GUI parent, String message)
  {
    Object[] options = {"Play again", "Exit"};
    int option = JOptionPane.showOptionDialog(parent.window, "Yay!" + message +
            " What would you like to do now?", "YOU MADE IT",
        JOptionPane.OK_CANCEL_OPTION,
        JOptionPane.PLAIN_MESSAGE, null, options, options[0]);

    if (option == 0)
    {
      parent.whichLevel = 1;
      parent.window.dispose();
      parent.getSettings();
    }
    else if (option == 1)
    {
      System.exit(0);
    }
  }

  /**
   * Window that pops up once the game has started. Allows user to change
   * any settings. THis is cool and has allowed for a much quicker debugging process
   */
  public void getSettings()
  {

    //this.map = map;
    JFrame popup = new JFrame("Settings");
    popup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    popup.setLayout(new BorderLayout());

    JLabel choose = new JLabel("Choose your settings: ");
    JButton start = new JButton("Start");

    JLabel PlSpeed = new JLabel("   Player Speed: ");
    JLabel PlSight = new JLabel("   Player Sight: ");
    JLabel PlHearing = new JLabel("   Player Hearing: ");
    JLabel PlStamina = new JLabel("   Player Stamina: ");
    JLabel ZoSpawn = new JLabel("   Zombie Spawn Rate: ");
    JLabel ZoSpeed = new JLabel("   Zombie Speed: ");
    JLabel ZoSmell = new JLabel("   Zombie Smell: ");
    JLabel ZoRate = new JLabel("   Zombie Decision Rate: ");
    JLabel fSpawn = new JLabel("    Fire Trap Spawn: ");
    JLabel PlRegen = new JLabel("   Player Regenerate: ");

    JTextField pSpeed = new JTextField("1.0");
    JTextField pSight = new JTextField("5");
    JTextField pHearing = new JTextField("10");
    JTextField pStamina = new JTextField("5.0");
    JTextField zSpawn = new JTextField("0.01");
    JTextField zSpeed = new JTextField("0.5");
    JTextField zSmell = new JTextField("7.0");
    JTextField zRate = new JTextField("2.0");
    JTextField fireSpawn = new JTextField("0.01");
    JTextField pRegen = new JTextField("0.2");

    JPanel settings = new JPanel();
    JPanel textFields = new JPanel();
    JPanel words = new JPanel();
    words.setLayout(new BoxLayout(words, BoxLayout.PAGE_AXIS));
    words.setPreferredSize(new Dimension(200, 400));
    JPanel everything = new JPanel();
    everything.setPreferredSize(new Dimension(500, 480));
    everything.setLayout(new BorderLayout());
    textFields.setLayout(new BoxLayout(textFields, BoxLayout.PAGE_AXIS));
    textFields.setPreferredSize(new Dimension(300, 400));
    settings.setLayout(new BorderLayout());

    settings.add(choose, BorderLayout.NORTH);
    settings.add(everything, BorderLayout.CENTER);

    words.add(Box.createRigidArea(new Dimension(10, 20)));
    words.add(PlSpeed);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(PlHearing);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(PlSight);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(PlStamina);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(PlRegen);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(ZoSpeed);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(ZoSmell);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(ZoRate);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(ZoSpawn);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(fSpawn);

    textFields.add(pSpeed);
    textFields.add(pHearing);
    textFields.add(pSight);
    textFields.add(pStamina);
    textFields.add(pRegen);
    textFields.add(zSpeed);
    textFields.add(zSmell);
    textFields.add(zRate);
    textFields.add(zSpawn);
    textFields.add(fireSpawn);

    everything.add(words, BorderLayout.WEST);
    everything.add(textFields, BorderLayout.EAST);
    settings.add(start, BorderLayout.SOUTH);

    settings.setPreferredSize(new Dimension(500, 500));
    popup.add(settings, BorderLayout.CENTER);


    popup.pack();
    popup.setVisible(true);

    start.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (gameStarted) //If not a completely new game
        {
          return;
        }
        gameStarted = true;
        String one = pSpeed.getText();
        String two = pHearing.getText();
        String three = pSight.getText();
        String four = pStamina.getText();
        String five = pRegen.getText();
        String six = zSpeed.getText();
        String seven = zSmell.getText();
        String eight = zRate.getText();
        String nine = zSpawn.getText();
        String ten = fireSpawn.getText();

        speed = Double.parseDouble(one);
        hearing = Integer.parseInt(two);
        sight = Integer.parseInt(three);
        stamina = Double.parseDouble(four);
        regen = Double.parseDouble(five);
        zspeed = Double.parseDouble(six);
        zsmell = Double.parseDouble(seven);
        drate = Double.parseDouble(eight);
        zspawn = Double.parseDouble(nine);
        fspawn = Double.parseDouble(ten);


        //initializes everything
        map = new GameMap();
        initPlayer(sight, hearing, speed, stamina, regen, 70, 70,
            map.start_location);
        setUpGUI();
        loadSounds();
        gameStarted = false;
        popup.dispose();
      }
    });

  }

  /**
   * Creates actual game window with a new GamePanel panel to display game.
   * Sets up start/pause button and displays labels with settings.
   */
  public void setUpGUI()
  {

    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLayout(new BorderLayout());
    //window.setExtendedState(window.MAXIMIZED_BOTH);
    window.setPreferredSize(new Dimension(SCENE_WIDTH, SCENE_HEIGHT));

    window.addComponentListener(new ComponentListener()
    {
      @Override
      public void componentResized(ComponentEvent e)
      {
        SCENE_WIDTH = window.getWidth();
        SCENE_HEIGHT = window.getHeight();

      }

      @Override
      public void componentMoved(ComponentEvent e)
      {

      }

      @Override
      public void componentShown(ComponentEvent e)
      {

      }

      @Override
      public void componentHidden(ComponentEvent e)
      {

      }
    });


    gamePanel = new GamePanel(this);
    gamePanel.addKeyListener(gamePanel);
    gamePanel.setFocusable(true);
    gamePanel.requestFocus();





    scrollPane = new JScrollPane(gamePanel);
    scrollPane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
    scrollPane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);


    /**
     * Keeps scrollpane from scrolling when arrow keys are pressed.
     * Should only move based on player's position.
     */
    InputMap im = gamePanel.getInputMap();
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "Arrow.up");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "Arrow.down");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "Arrow.right");
    im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "Arrow.left");

    AbstractAction doNothing = new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        // doing nothing
      }
    };
    ActionMap am = gamePanel.getActionMap();
    am.put("Arrow.up", doNothing);
    am.put("Arrow.down", doNothing);
    am.put("Arrow.right", doNothing);
    am.put("Arrow.left", doNothing);


    /**
     * sets the labels at the top of the map
     */
    level = new JLabel("Level: ");
    playerSight = new JLabel("Sight: ");
    playerHearing = new JLabel("Hearing: ");
    playerSpeed = new JLabel("Speed: ");
    playerStamina = new JLabel("Stamina: ");
    zombieSmell = new JLabel("Z-Smell: ");
    zombieSpeed = new JLabel("Z-Speed: ");
    traps = new JLabel("Fire traps: ");


    startPause = new JButton("Start");
    startPause.setPreferredSize(new Dimension(80, 23));
    startPause.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (pause)
        {
          startGame();
          gamePanel.requestFocusInWindow();
        }
        else
        {
          pauseGame();
        }

      }
    });

    //Shows all game labels
    viewPanel = new JPanel();
    viewPanel.setLayout(new BoxLayout(viewPanel, BoxLayout.LINE_AXIS));
    //viewPanel.add(Box.createHorizontalGlue());
    viewPanel.setPreferredSize(new Dimension(window.getWidth(), 25));

    viewPanel.add(Box.createRigidArea(new Dimension(50, 25)));
    viewPanel.add(level);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(playerSight);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(playerHearing);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(playerSpeed);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(playerStamina);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(startPause);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(traps);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(zombieSpeed);
    viewPanel.add(Box.createRigidArea(new Dimension(20, 25)));
    viewPanel.add(zombieSmell);
    viewPanel.add(Box.createRigidArea(new Dimension(50, 25)));

    //viewPanel.add(Box.createGlue());

    window.getContentPane().add(viewPanel, BorderLayout.NORTH);
    window.pack();
    window.setVisible(true);
    window.setResizable(true);
    window.getContentPane().add(scrollPane);
    SwingUtilities.invokeLater(() -> gamePanel.snapViewPortToPlayer());

  }

  //start and pause game methods
  private void startGame()
  {
    startPause.setText("Pause");
    pause = false;
    running = true;
    gamePanel.frame_timer.start();
    gamePanel.startMusic();

  }

  public void pauseGame()
  {
    startPause.setText("Start");
    pause = true;
    running = false;
    gamePanel.frame_timer.stop();
    gamePanel.stopMusic();
    gamePanel.stopAllSounds();


  }

  /**
   * updates the stats about the player, probably the most important thing is
   * the stanima
   */
  public void updatePlayerLabels()
  {
    level.setText("Level: " + whichLevel);
    playerSight.setText("Sight: " + player.getSight());
    playerHearing.setText("Hearing: " + player.getHearing());
    playerSpeed.setText("Speed: " + player.getSpeed());
    playerStamina
        .setText("Stamina: " + Math.round(player.getStamina() * 100.0) / 100.0);


  }

  /**
   * updates the zombie related things like speed and smell, but also the number
   * of firetraps that the player has
   */
  public void updateZombieLabels()
  {
    zombieSpeed.setText("Z-Speed: " + zspeed);

    zombieSmell.setText("Z-Smell: " + zsmell);
    traps.setText("Fire traps: " + player.getFire_traps());

  }

  /**
   * Takes in all numbers the player inputed during settings and creates a
   * new player with them.
   * @param sight
   * @param hearing
   * @param speed
   * @param stamina
   * @param regen
   * @param width
   * @param height
   * @param location
   */
  public void initPlayer(int sight, int hearing, double speed, double stamina,
                         double regen, int width, int height, Location location)
  {
    player = new Player(sight, hearing, speed, stamina, regen, width, height,
        location);
//    player.setLocation(new Location(800, 1120));
    player.setHeading(new Heading(Heading.NONE));

  }

  /**
   * loads in all sounds once.
   */
  public void loadSounds()
  {
    gamePanel.loadMusic();
    SoundLoader.loadSounds();
    //fireTrap.loadExplosion();
  }


}
