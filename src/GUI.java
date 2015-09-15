import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;


public class GUI
{


  final static int tile_size = 80;
  static int SCENE_WIDTH = 1920;
  static int SCENE_HEIGHT = 1080;
  static JPanel viewPanel; //Will probably need to make another class,
  static boolean running = false;
  static JScrollPane scrollPane;
  public List<Zombie> zombieList;
  JFrame window;
  GamePanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  JButton startPause;
  boolean pause = true;
  Player player;
  Zombie zombie;
  FireTrap fireTrap;
  Zombie randomZombie;
  Zombie lineZombie;
  Zombie masterZ;
  private int whichlevel = 1;

  public void setUpGUI()
  {
    window = new JFrame("Zombie House");
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
        System.out.format("SCENE SIZE: (%d, %d)\n", SCENE_WIDTH, SCENE_HEIGHT);
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


    level = new JLabel("Level: ");
    playerSight = new JLabel("Sight: ");
    playerHearing = new JLabel("Hearing: ");
    playerSpeed = new JLabel("Speed: ");
    playerStamina = new JLabel("Stamina: ");

    startPause = new JButton("Start");
    startPause.setPreferredSize(new Dimension(80, 23));
    startPause.addActionListener(new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (pause)
        {
          startPause.setText("Pause");
          startGame();
          gamePanel.requestFocusInWindow();
        }
        else
        {
          startPause.setText("Start");
          pauseGame();
        }

      }
    });


    viewPanel = new JPanel();
    viewPanel.setPreferredSize(new Dimension(SCENE_WIDTH, 25));
    viewPanel.add(startPause);
    viewPanel.add(level);
    viewPanel.add(playerSight);
    viewPanel.add(playerHearing);
    viewPanel.add(playerSpeed);
    viewPanel.add(playerStamina);


    //window.getContentPane().add(gamePanel, BorderLayout.CENTER);
    window.getContentPane().add(viewPanel, BorderLayout.NORTH);
    window.pack();
    window.setVisible(true);
    window.setResizable(true);
    window.getContentPane().add(scrollPane);
    SwingUtilities.invokeLater(() -> gamePanel.snapViewPortToPlayer());

  }

  private void startGame()
  {
    pause = false;
    running = true;
    gamePanel.frame_timer.start();
    gamePanel.startMusic();

  }

  private void pauseGame()
  {
    pause = true;
    running = false;
    gamePanel.frame_timer.stop();
    gamePanel.stopMusic();


  }


  public void updateLabels()
  {
    level.setText("Level: " + whichlevel);
    playerSight.setText("Sight: " + player.getSight());
    playerHearing.setText("Hearing: " + player.getHearing());
    playerSpeed.setText("Speed: " + player.getSpeed());
    playerStamina.setText("Stamina: " + player.getStamina());
  }

  public void initPlayer()
  {
    player = new Player(5, 10, 1.0, 5, 70, 70, new Location(800, 1120));
//    player.setLocation(new Location(800, 1120));
    player.setHeading(new Heading(Heading.NONE));

  }


  public void loadSounds()
  {
    gamePanel.loadMusic();
    player.loadSounds();
    randomZombie.loadNoises();
    lineZombie.loadNoises();
    masterZ.loadNoises();
    //fireTrap.loadExplosion();
  }

  /**
   * Will need to make arrays of zombies. But for now this is ok.
   */
  public void initZombies()
  {

    randomZombie = new RandomWalkZombie(new Location(700, 1200));
    lineZombie = new LineWalkZombie(new Location(800, 1300));
    masterZ = new MasterZombie(new Location(900, 1300));

    randomZombie.setHeading(new Heading(Heading.WEST));
    lineZombie.setHeading(new Heading(Heading.EAST));

//    zombieList.add(randomZombie);
//    zombieList.add(lineZombie);
//    zombieList.add(masterZ);


  }

  public void initFireTraps()
  {
    //need to create array of firetraps for later, based off of map
  }
}
