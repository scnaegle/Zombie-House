import javafx.scene.shape.Line;

import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * Created by Ally-Bo-Bally on 9/27/15.
 */

/**
 * loop over endpoints:
 * remember which wall is nearest
 * add any walls that BEGIN at this endpoint to 'walls'
 * remove any walls that END at this endpoint from 'walls'
 * <p/>
 * SORT the open list
 * <p/>
 * if the nearest wall changed:
 * fill the current triangle and begin a new one
 */
public class Shadow2
{
  ArrayList<Point> endpoints = new ArrayList<>();
      //list of endpoints sorted by angle
  ArrayList<Line> openWalls = new ArrayList<>();
      //list of walls sorted by distance
  Point center;

  public Shadow2(GameMap map)
  {
    loadMap(map);
  }

  public static void main(String[] args)
  {
    File map_file = null;
    try
    {
      map_file =
          new File(
              Shadow.class.getResource("resources/shadow_test.map").toURI());
    }
    catch (URISyntaxException e)
    {
      e.printStackTrace();
    }

    GameMap map = new GameMap(map_file);
    Shadow shadow = new Shadow(map);

    shadow.sweep();
    JFrame frame = new JFrame("MapTest");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
    frame.setExtendedState(Frame.MAXIMIZED_BOTH);

    for (Point p : shadow.output)
    {
      System.out.println("point: " + p);
    }

    JPanel map_panel = new JPanel()
    {
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(map.map_image, 0, 0, null);

        g.setColor(Color.YELLOW);

      }
    };
    map_panel
        .setPreferredSize(new Dimension(map.getWidth(80), map.getHeight(80)));

    JScrollPane scroll_pane = new JScrollPane(map_panel);
    scroll_pane.setHorizontalScrollBarPolicy(
        ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll_pane.setVerticalScrollBarPolicy(
        ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    frame.add(scroll_pane);
    frame.pack();
    frame.setVisible(true);
  }

  public void loadMap(GameMap map)
  {
    endpoints.clear();
    openWalls.clear();
//    setEndPoints(map.getWalls());
    int x, y, w, h;
    for (Tile wall : map.getWalls())
    {
      x = wall.location.getX();
      y = wall.location.getY();
      w = wall.width;
      h = wall.height;
//      addSegment(x, y, x + w, y); // top
//      addSegment(x, y, x, y + h); // left
//      addSegment(x + w, y, x + w, y + h); // right
//      addSegment(x, y + h, x + w, y + h); // bottom
    }
  }

  public void setCenter(GameMap map)
  {
    center = new Point(map.getWidth(80) / 2, map.getHeight(80) / 2);
  }


}
