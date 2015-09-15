
public class StartGame
{
  public static void main(String[] args)
  {
    GUI g = new GUI();
    g.setUpGUI();
    g.initPlayer();
    g.initZombies();
    g.initFireTraps();
    g.updateLabels();
  }
}
