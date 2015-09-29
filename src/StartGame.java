
/**
 * starts the game and gets everything going
 */
public class StartGame
{
  static GUI g;

  /**
   * starts game from beginning
   *
   * @param args
   */
  public static void main(String[] args)
  {
    g = new GUI();
    g.getSettings();
  }

  /**
   * Restarts the game if player died or if user wants to start over.
   */
  public static void restartGame()
  {
    g.window.dispose();
    g = null;
    g = new GUI();
    g.getSettings();
  }
}
