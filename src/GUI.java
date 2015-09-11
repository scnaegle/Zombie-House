import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class GUI
{

  final static int PIXELS = 80;
  static int SCENE_WIDTH = 1920;
  static int SCENE_HEIGHT = 1080;
  static int tile_size = 80;
  static JPanel viewPanel; //Will probably need to make another class,
  static boolean running = false;
  JFrame window;
  GamePanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  JButton startPause;
  boolean pause = true;


  public void setUpGUI()
  {
    window = new JFrame("Zombie House");
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLayout(new BorderLayout());
    window.setExtendedState(window.MAXIMIZED_BOTH);
//    System.out.println("scene_width: " + SCENE_WIDTH);
//    System.out.println("scene_height: " + SCENE_HEIGHT);
//    window.setPreferredSize(new Dimension(SCENE_WIDTH, SCENE_HEIGHT));
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

    gamePanel = new GamePanel();
    //gamePanel.addKeyListener(this);
    //gamePanel.setFocusable(true);
    gamePanel.requestFocus();
    //gamePanel.setFocusTraversalKeysEnabled(false);

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



    window.add(gamePanel, BorderLayout.CENTER);
    window.add(viewPanel, BorderLayout.NORTH);
    window.setVisible(true);
    window.setResizable(true);

  }

  private void startGame()
  {
    pause = false;
    running = true;
    gamePanel.frame_timer.start();
  }

  private void pauseGame()
  {
    pause = true;
    running = false;
    gamePanel.frame_timer.stop();
  }


//  @Override
//  public void keyTyped(KeyEvent e)
//  {
//
//  }
//
//  @Override
//  public void keyPressed(KeyEvent e)
//  {
//    int code = e.getKeyCode();
//
//    while(running)
//    {
//      if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W)
//      {
//        System.out.println("Pressing up");
//        gamePanel.player.heading = Heading.NORTH;
//        gamePanel.player.move();
//      }
//      if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S)
//      {
//        gamePanel.player.heading = Heading.SOUTH;
//        gamePanel.player.move();
//      }
//      if (code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_D)
//      {
//        gamePanel.player.heading = Heading.EAST;
//        gamePanel.player.move();
//      }
//      if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_A)
//      {
//        gamePanel.player.heading = Heading.WEST;
//        gamePanel.player.move();
//      }
//      gamePanel.player.animation.start();
//      gamePanel.repaint();
//    }
//  }
//
//  @Override
//  public void keyReleased(KeyEvent e)
//  {
//    gamePanel.player.heading = Heading.NONE;
//    gamePanel.player.animation.stop();
//  }
}
