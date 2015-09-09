
public class startGame
{
  public static void main(String[] args)
  {
    BufferedImageLoader load = new BufferedImageLoader();
    load.initPlayerSprite();
    GUI g = new GUI();
    g.setUpGUI();
  }
}
