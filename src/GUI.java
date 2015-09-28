/**
 * GUI class creates the settings window, initializes map, player, and sounds,
 * creates actual game window, and creates scrollpane which lets us follow the
 * player around.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * makes the GUI and all the objects that go along with it
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
  public int winLevel;
  public int whichLevel = 1;
  JFrame window = new JFrame("Zombie House");
  GameMap map;
  GamePanel gamePanel;
  private boolean gameStarted = false;
  private boolean pause = true;

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
        .showOptionDialog(parent.window,
            "Aaargghh! " + message + " Try again matey?",
            "YOU DIED",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE, null,
            options, options[0]);

    if (option == 0) //Restart
    {
      StartGame.restartGame();
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
      StartGame.restartGame();
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
    JLabel wiLevel = new JLabel("   Play to level: ");

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
    JTextField wLevel = new JTextField("6");

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

    //adds all labels
    words.add(Box.createRigidArea(new Dimension(10, 15)));
    words.add(PlSpeed);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(PlHearing);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(PlSight);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(PlStamina);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(PlRegen);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(ZoSpeed);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(ZoSmell);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(ZoRate);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(ZoSpawn);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(fSpawn);
    words.add(Box.createRigidArea(new Dimension(10, 25)));
    words.add(wiLevel);

    //Adds text boxes
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
    textFields.add(wLevel);

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
        boolean numsGood = true;
        try
        {
          speed = Double.parseDouble(pSpeed.getText());
          if (speed > 5.0 || speed < 1.0)
          {
            throw new NumberFormatException();
          }
          hearing = Integer.parseInt(pHearing.getText());
          if (hearing > 15 || hearing < sight)
          {
            throw new NumberFormatException();
          }
          sight = Integer.parseInt(pSight.getText());
          if (sight < 1 || sight > 10)
          {
            throw new NumberFormatException();
          }
          stamina = Double.parseDouble(pStamina.getText());
          if (stamina < 3.0 || stamina > 10.0)
          {
            throw new NumberFormatException();
          }
          regen = Double.parseDouble(pRegen.getText());
          if (regen < 0.01 || regen > stamina)
          {
            throw new NumberFormatException();
          }
          zspeed = Double.parseDouble(zSpeed.getText());
          if (zspeed < 0.3 || zspeed > 2.0)
          {
            throw new NumberFormatException();
          }
          zsmell = Double.parseDouble(zSmell.getText());
          if (zsmell < 4.0 || zsmell > 10.0)
          {
            throw new NumberFormatException();
          }
          drate = Double.parseDouble(zRate.getText());
          if (drate < 1.0 || drate > 4.0)
          {
            throw new NumberFormatException();
          }
          zspawn = Double.parseDouble(zSpawn.getText());
          if (zspawn < 0.01 || zspawn > 0.1)
          {
            throw new NumberFormatException();
          }
          fspawn = Double.parseDouble(fireSpawn.getText());
          if (fspawn < 0.01 || fspawn > 0.1)
          {
            throw new NumberFormatException();
          }
          winLevel = Integer.parseInt(wLevel.getText());
          if (winLevel < 2 || winLevel > 10)
          {
            throw new NumberFormatException();
          }
        }
        catch (NumberFormatException x)
        {
          numsGood = false;
          gameStarted = false;
          System.out.println("Error, bad input");
        }
        if (!numsGood) return;


        //initializes everything
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
//        System.out.println("SCENE_WIDTH = " + SCENE_WIDTH);
//        System.out.println("SCENE_HEIGHT = " + SCENE_HEIGHT);
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


    window.pack();
    window.setVisible(true);
    window.setResizable(true);
    window.getContentPane().add(scrollPane);
    SwingUtilities.invokeLater(() -> gamePanel.snapViewPortToPlayer());

  }

  //start and pause game methods
  public void startGame()
  {
    pause = false;
    running = true;
    gamePanel.frame_timer.start();
    gamePanel.startMusic();
  }

  public void pauseGame()
  {
    pause = true;
    running = false;
    gamePanel.frame_timer.stop();
    gamePanel.stopMusic();
    gamePanel.stopAllSounds();
  }

  /**
   * loads in all sounds once.
   */
  public void loadSounds()
  {
    gamePanel.loadMusic();
    SoundLoader.loadSounds();

  }


}
