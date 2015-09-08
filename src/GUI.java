import javax.swing.*;
import java.awt.*;



/**
 * Created by Ally-Bo-Bally on 9/7/15.
 */
public class GUI
{
  JFrame window;
  int FRAME_WIDTH = window.MAXIMIZED_HORIZ;
  int FRAME_HEIGHT = window.MAXIMIZED_VERT;
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
    window.setExtendedState(window.MAXIMIZED_BOTH);

    level = new JLabel("Level: ");
    playerSight = new JLabel("Sight: ");
    playerHearing = new JLabel("Hearing: ");
    playerSpeed = new JLabel("Speed: ");
    playerStamina = new JLabel("Stamina: ");
    scorePanel = new JPanel();
    scorePanel.setPreferredSize(new Dimension(FRAME_WIDTH,100));
    scorePanel.add(level);
    scorePanel.add(playerSight);
    scorePanel.add(playerHearing);
    scorePanel.add(playerSpeed);
    scorePanel.add(playerStamina);
    window.add(scorePanel);
    window.setVisible(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }
}
