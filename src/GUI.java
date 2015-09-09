import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class GUI
{
  JFrame window;
  static int SCENE_WIDTH;
  static int SCENE_HEIGHT;
  JPanel scorePanel;
  JPanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;
  BufferedImage player = null;
  final int PIXELS = 80;

  public void setUpGUI()
  {
    window = new JFrame("Zombie House");
    window.setLayout(new BorderLayout());
    window.setExtendedState(window.MAXIMIZED_BOTH);
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

    SCENE_WIDTH = window.getWidth();
    SCENE_HEIGHT = window.getHeight();

    gamePanel.setPreferredSize(new Dimension(SCENE_WIDTH, SCENE_HEIGHT - 25));
    gamePanel.setBackground(Color.white);

    
    window.add(gamePanel,BorderLayout.CENTER);
    window.add(scorePanel, BorderLayout.NORTH);
    window.setVisible(true);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

  public void initPlayerSprite()
  {
    BufferedImageLoader loader = new BufferedImageLoader();
    try
    {
      player = loader.loadPlayerSprite("resources/player.png");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
    player = loader.grabPlayerImage(1, 1, PIXELS, PIXELS);
  }
}
