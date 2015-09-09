import javax.swing.*;
import java.awt.*;


public class GUI
{
  JFrame window;
  int SCENE_WIDTH;
  int SCENE_HEIGHT;
  JPanel scorePanel;
  JPanel gamePanel;
  JLabel level;
  JLabel playerSight;
  JLabel playerHearing;
  JLabel playerSpeed;
  JLabel playerStamina;

  public void setUpGUI()
  {
    window = new JFrame("Zombie House");
    window.setLayout(new BorderLayout());
    window.setExtendedState(window.MAXIMIZED_BOTH);
    gamePanel = new JPanel();

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

}
