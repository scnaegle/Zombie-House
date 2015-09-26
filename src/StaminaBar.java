import java.awt.*;

/**
 * Created by Ally-Bo-Bally on 9/25/15.
 */
public class StaminaBar
{

  public static final Color TIRED = new Color(249, 44, 25);
  public static final Color ENERGY = new Color(108, 246, 16);
  Player player;
  double maxStamina;
  private double stamAmount;

  public StaminaBar(Player player, double maxStamina)
  {
    this.player = player;
    this.maxStamina = maxStamina;
  }

  public void paintBar(Graphics2D g2)
  {
    int x = player.getLocation().getX();
    int y = player.getLocation().getY();

    g2.setColor(TIRED);
    g2.fillRect(x, y - 20, GUI.tile_size, 8);

    g2.setColor(ENERGY);
    g2.fillRect(x, y - 20, (int) getStamAmount(), 8);
  }

  public double getStamAmount()
  {
    return (player.stamina / maxStamina) * GUI.tile_size;
  }
}