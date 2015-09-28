
/**
 * starts the game and gets everything going
 */
public class StartGame
{
  static GUI g;

  public static void main(String[] args)
  {
    g = new GUI();
    g.getSettings();
  }

  public static void restartGame() {
    g.window.dispose();
    g = null;
    g = new GUI();
    g.getSettings();
  }
}
