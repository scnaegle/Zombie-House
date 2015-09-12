import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;


public class GUI
{

  final static int PIXELS = 80;
  static int SCENE_WIDTH = 1000;
  static int SCENE_HEIGHT = 1000;
  static int tile_size = 80;
  static JPanel viewPanel; //Will probably need to make another class,
  static boolean running = false;
  static JViewport viewport;
  JFrame window;
  GamePanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  JButton startPause;
  boolean pause = true;
  private Point startPoint;


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
    gamePanel.addKeyListener(gamePanel);
    gamePanel.setFocusable(true);
    gamePanel.requestFocus();
    //gamePanel.setFocusTraversalKeysEnabled(false);

    startPoint = new Point(0, 0);
    viewport = new JViewport();
    viewport.setSize(SCENE_WIDTH, SCENE_HEIGHT - 25);
    viewport.setView(gamePanel);
    viewport.setViewPosition(startPoint);



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


    window.getContentPane().add(viewport, BorderLayout.CENTER);
    //window.getContentPane().add(gamePanel, BorderLayout.CENTER);
    window.getContentPane().add(viewPanel, BorderLayout.NORTH);
    window.pack();
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



}
