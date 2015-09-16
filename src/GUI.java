import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class GUI
{


  final static int tile_size = 80;
  static int SCENE_WIDTH = 1920;
  static int SCENE_HEIGHT = 1080;
  static JPanel viewPanel; //Will probably need to make another class,
  static boolean running = false;
  static JScrollPane scrollPane;
  JFrame window;
  GamePanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  JLabel zombieSpawn;
  JLabel zombieSpeed;
  JLabel zombieSmell;
  JLabel zombieRate;
  JLabel fireSpawn;
  JButton startPause;
  boolean pause = true;
  Player player;
  FireTrap fireTrap;
  private JLabel traps;
  private int whichlevel = 1;
  private Zombie zombie;

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

    JTextField pSpeed = new JTextField();
    JTextField pSight = new JTextField();
    JTextField pHearing = new JTextField();
    JTextField pStamina = new JTextField();
    JTextField zSpawn = new JTextField();
    JTextField zSpeed = new JTextField();
    JTextField zSmell = new JTextField();
    JTextField zRate = new JTextField();
    JTextField fireSpawn = new JTextField();

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
    words.add(Box.createRigidArea(new Dimension(10, 35)));
    words.add(PlSight);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(PlStamina);
    words.add(Box.createRigidArea(new Dimension(10, 35)));
    words.add(ZoSpeed);
    words.add(Box.createRigidArea(new Dimension(10, 40)));
    words.add(ZoSmell);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(ZoRate);
    words.add(Box.createRigidArea(new Dimension(10, 30)));
    words.add(ZoSpawn);
    words.add(Box.createRigidArea(new Dimension(10, 35)));
    words.add(fSpawn);

    textFields.add(pSpeed);
    textFields.add(pSight);
    textFields.add(pHearing);
    textFields.add(pStamina);
    textFields.add(zSpawn);
    textFields.add(zSmell);
    textFields.add(zSpeed);
    textFields.add(zRate);
    textFields.add(fireSpawn);

    everything.add(words, BorderLayout.WEST);
    everything.add(textFields, BorderLayout.EAST);
    settings.add(start, BorderLayout.SOUTH);

    settings.setPreferredSize(new Dimension(500, 500));
    popup.add(settings, BorderLayout.CENTER);

    //Object[] option = {"Start"};
//    int dialog = JOptionPane.showOptionDialog(popup,"Please choose your
// settings:",
//        "Settings",JOptionPane.YES_OPTION,JOptionPane.PLAIN_MESSAGE,null,
// option,option[0]);

    popup.pack();
    popup.setVisible(true);
    //String input = JOptionPane.showInputDialog(window, "Type something");
    //int num = Integer.parseInt(input);
    //System.out.println(num);
    // if(dialog == JOptionPane.YES_OPTION){
    //initPlayer();
    //setUpGUI();
    // }
  }
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
    zombieSmell = new JLabel("Z-Smell: ");
    zombieRate = new JLabel("Z-Decision Rate: ");
    zombieSpawn = new JLabel("Z-Spawn Rate: ");
    zombieSpeed = new JLabel("Z-Speed: ");
    fireSpawn = new JLabel("Fire Trap Spawn: ");
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
    viewPanel.add(level);
    viewPanel.add(playerSight);
    viewPanel.add(playerHearing);
    viewPanel.add(playerSpeed);
    viewPanel.add(playerStamina);
    viewPanel.add(traps);
    viewPanel.add(startPause);
    viewPanel.add(zombieSpawn);
    viewPanel.add(zombieSpeed);
    viewPanel.add(zombieSmell);
    viewPanel.add(zombieRate);
    viewPanel.add(fireSpawn);


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


  public void updatePlayerLabels()
  {
    level.setText("Level: " + whichlevel);
    playerSight.setText("Sight: " + player.getSight());
    playerHearing.setText("Hearing: " + player.getHearing());
    playerSpeed.setText("Speed: " + player.getSpeed());
    playerStamina
        .setText("Stamina: " + Math.round(player.getStamina() * 100.0) / 100.0);
    traps.setText("Fire traps: " + player.getFire_traps());

  }

  public void updateZombieLabels()
  {
    zombieSpeed.setText("Z-Speed: " + zombie.getSpeed());
    zombieRate.setText("Z-Decision Rate: " + zombie.getDecisionRate());
    zombieSmell.setText("Z-Smell: " + zombie.getSmell());
    zombieSpawn.setText("Z-Spawn Rate: " + gamePanel.map.getZombieSpawnRate());
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
    //fireTrap.loadExplosion();
  }

  public void initFireTraps()
  {
    //need to create array of firetraps for later, based off of map
  }
}
