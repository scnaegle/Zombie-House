import javax.swing.*;
import java.awt.*;


/**
 * Having this larger class that extends JPanel will allow easier access to
 * drawing, moving, sizing, and will make things neater.
 */
public class GamePanel extends JPanel
{


  public GamePanel()
  {
    setPreferredSize(new Dimension(GUI.SCENE_WIDTH, GUI.SCENE_HEIGHT - 25));
    setBackground(Color.white);


  }

  //Merrr.... stuck here. Trying to get image to paint on panel. Lack of time.
  public void paintComponent(Graphics2D g)
  {
    g.drawImage(GUI.player, 200, 200, null);
  }
}
