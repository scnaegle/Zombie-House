
public class StartGame
{
  public static void main(String[] args)
  {
    GUI g = new GUI();
    g.initPlayer();
    g.initZombies();
    g.initFireTraps();
    g.setUpGUI();
    g.loadSounds();
    g.updateLabels();


  }

}
