import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;


public class GUI
{

  final static int PIXELS = 80;
  static int SCENE_WIDTH;
  static int SCENE_HEIGHT;
  static int tile_size = 80;
  static JPanel scorePanel; //Will probably need to make another class,
  JFrame window;
  GamePanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  BufferedImage player = null;

  public void setUpGUI()
  {
    window = new JFrame("Zombie House");
    window.setLayout(new BorderLayout());
    window.setExtendedState(window.MAXIMIZED_BOTH);
    window.addComponentListener(new ComponentListener() {
      @Override
      public void componentResized(ComponentEvent e) {
        SCENE_WIDTH = window.getWidth();
        SCENE_HEIGHT = window.getHeight();
      }

      @Override
      public void componentMoved(ComponentEvent e) {

      }

      @Override
      public void componentShown(ComponentEvent e) {

      }

      @Override
      public void componentHidden(ComponentEvent e) {

      }
    });

    gamePanel = new GamePanel();

    level = new JLabel("Level: ");
    playerSight = new JLabel("Sight: ");
    playerHearing = new JLabel("Hearing: ");
    playerSpeed = new JLabel("Speed: ");
    playerStamina = new JLabel("Stamina: ");

    scorePanel = new JPanel();
    scorePanel.setPreferredSize(new Dimension(SCENE_WIDTH, 25));
    scorePanel.add(level);
    scorePanel.add(playerSight);
    scorePanel.add(playerHearing);
    scorePanel.add(playerSpeed);
    scorePanel.add(playerStamina);



    window.add(gamePanel,BorderLayout.CENTER);
    window.add(scorePanel, BorderLayout.NORTH);
    window.setVisible(true);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }


}
