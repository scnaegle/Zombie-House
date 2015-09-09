import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class GUI
{
  static int SCENE_WIDTH;
  static int SCENE_HEIGHT;
  static JPanel scorePanel; //Will probably need to make another class,
  static BufferedImage player = null;
  //  static BufferedImage[] walking = new BufferedImage[10];
//  static BufferedImage[] running = new BufferedImage[10];
  static int PIXELS = 80;
  JFrame window;
  // or could make interface to update settings. Either one.
  GamePanel gamePanel;
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


    window.add(gamePanel,BorderLayout.CENTER);
    window.add(scorePanel, BorderLayout.NORTH);
    window.setVisible(true);
    window.setResizable(true);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

  }

//  public void initPlayerSprite()
//  {
//    BufferedImageLoader loader = new BufferedImageLoader();
//    try
//    {
//      player = loader.loadPlayerSprite("resources/player.png");
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//
//    player = loader.grabPlayerImage(1, 1, PIXELS, PIXELS);
//    walking[1] = player;
////    player=loader.grabPlayerImage(3,9,PIXELS,PIXELS);
////    walking[2] = player;
//    walking[3] = loader.grabPlayerImage(3,10,PIXELS,PIXELS);
////    walking[4]= loader.grabPlayerImage(3,11,PIXELS,PIXELS);
////    walking[5]= loader.grabPlayerImage(4,1,PIXELS,PIXELS);
////    walking[6]= loader.grabPlayerImage(4,2,PIXELS,PIXELS);
////    walking[7]= loader.grabPlayerImage(4,3,PIXELS,PIXELS);
// }

}
