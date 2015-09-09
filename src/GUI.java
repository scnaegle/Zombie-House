import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * This is the GUI. This is going to house the game screen and all that good
 * Jazz
 *
 * It will have button such as pause and start, a score keeper, levels,
 */
public class GUI
{
  JFrame window;
  int SCENE_WIDTH;
  int SCENE_HEIGHT;
  JPanel scorePanel;
  JPanel gamePanel;
  JLabel level;

  JLabel playerSight;           // perhaps we can make upgrades that can be picked up for
  JLabel playerHearing;         // these things later, like flash light, and hearing aids
  JLabel playerSpeed;           // I imaginge it would be similar to the traps we have to
  JLabel playerStamina;         // make
  BufferedImage player = null;
  int PIXELS = 80;

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
