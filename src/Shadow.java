import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by scnaegl on 9/20/15.
 * The Shadow class its what builds up the shadow overlay based on the given light
 * location and where all the game map walls are located. This works by first casting
 * rays and figuring out that the current visibility polygon would look like. Then, we
 * make a black overlay and subtract out the visibility polygon so that that part is
 * transparent to the map below.
 */
public class Shadow {

  private final double SIGHT_MULTIPLIER = 1.5;
  public BufferedImage overlay;
  private boolean show_all_walls = false;
  private ArrayList<EndPoint> output = new ArrayList<EndPoint>();
  private ArrayList<EndPoint> endpoints = new ArrayList<EndPoint>();
  private ArrayList<Segment> segments = new ArrayList<Segment>();
  private Point center = new Point();
  private GameMap map;
  private int width = 800;
  private int height = 800;
  private int sight = 5;
  private int sight_pixels = 5 * GUI.tile_size;
  private Point location;
  private Rectangle boundingRect;

  /**
   * Basic constructor. This takes in the GameMap object and calls the loadMap function
   * to load all the walls, endpoints, and segments
   * @param map GameMap object
   */
  public Shadow(GameMap map) {
    loadMap(map);
    location = new Point(0, 0);
    setBoundingRect();
  }


  /**
   * Set the Dimensions of the background overlay.
   * @param width Width of overlay
   * @param height Height of overlay
   */
  public void setDimensions(int width, int height) {
    this.width = width;
    this.height = height;
    setBoundingRect();
  }

  /**
   * Set the sight distance of wherever the light source is.
   * @param sight Sight in number of tiles
   */
  public void setSight(int sight) {
    this.sight = sight;
    this.sight_pixels = sight * GUI.tile_size;
  }

  /**
   * Set whether or not to show all the walls or just the walls that are touching
   * the visibility polygon
   * @param show_all_walls
   */
  public void setShowAllWalls(boolean show_all_walls) {
    this.show_all_walls = show_all_walls;
  }

  /**
   * Invert whatever show_all_walls is. If true then false, if false then true.
   */
  public void invertShowAllWalls() {
    if (show_all_walls) {
      show_all_walls = false;
    } else {
      show_all_walls = true;
    }
  }

  /**
   * Set the bounding rectangle, which is useful for limiting the walls in order to
   * speed up calculations
   */
  private void setBoundingRect() {
    boundingRect = new Rectangle(location.x, location.y, width, height);
  }

  /**
   * Use the GameMap passed in to add segments by using the walls in the map.
   * @param map GameMap object
   */
  public void loadMap(GameMap map) {
    this.map = map;
    endpoints.clear();
    segments.clear();
    int x, y, w, h;
    for(Tile wall : map.getWalls()) {
      x = wall.location.getX();
      y = wall.location.getY();
      w = wall.width;
      h = wall.height;
      addSegment(x, y, x + w, y); // top
      addSegment(x + w, y, x + w, y + h); // right
      addSegment(x + w, y + h, x, y + h); // bottom
      addSegment(x, y + h, x, y); // left
    }
  }

  /**
   * Add a wall segment to use when determining the visibility. This also
   * adds endpoints as well.
   * @param x1 First x coordinate
   * @param y1 First y coordinate
   * @param x2 Second x coordinate
   * @param y2 Second y coordinate
   */
  private void addSegment(int x1, int y1, int x2, int y2) {
    Segment segment = null;
    EndPoint p1 = new EndPoint(0, 0);
    p1.segment = segment;
    p1.visualize = true;
    EndPoint p2 = new EndPoint(0, 0);
    p2.segment = segment;
    p2.visualize = false;
    segment = new Segment();
    p1.x = x1; p1.y = y1;
    p2.x = x2; p2.y = y2;
    p1.segment = segment;
    p2.segment = segment;
    segment.p1 = p1;
    segment.p2 = p2;
    segment.d = 0.0;

    segments.add(segment);
    endpoints.add(p1);
    endpoints.add(p2);
  }

  /**
   * Set the light location to be used for calculating the visibility.
   * @param x X coordinate of light location
   * @param y Y coordinate of light location
   */
  public void setLightLocation(int x, int y) {
    if (x != center.x || y != center.y) {
      this.center = new Point(x, y);
      this.location.setLocation(x - width / 2, y - height / 2);
    }
  }

  /**
   * Get the intersection point between 2 lines
   * @param p1 Start point of the first line
   * @param p2 End point of the first line
   * @param p3 Start point of the second line
   * @param p4 End point of the second line
   * @return Point at which the 2 lines intersect
   */
  public Point lineIntersection(Point p1, Point p2, Point p3, Point p4) {
    if (! Line2D.linesIntersect(p1.x, p1.y, p2.x, p2.y, p3.x, p3.y, p4.x, p4.y)) return null;
    double px = p1.x,
        py = p1.y,
        rx = p2.x-px,
        ry = p2.y-py;
    double qx = p3.x,
        qy = p3.y,
        sx = p4.x-qx,
        sy = p4.y-qy;

    double det = sx*ry - sy*rx;
    if (det == 0) {
      return null;
    } else {
      double z = (sx*(qy-py)+sy*(px-qx))/det;
      if (z==0 ||  z==1) return null;  // intersection at end point!
      return new Point( (int)(px+z*rx), (int)(py+z*ry));
    }
  }

  /**
   * Run the algorithm, sweeping over all endpoints within the sight range
   * and created bunch of points that represent a polygon of visibility
   * @param sight_range The distance in pixels that we want to go out
   */
  public void sweep(double sight_range) {
    output.clear();  // output set of triangles
    for(Segment s : segments) {
      if (Math.hypot(center.x - s.p1.x, center.y - s.p1.y) > sight_range) {
        continue;
      }
      Point new_p1 = s.p1;
//      Point new_p2 = s.p2;
      for(Segment s2 : segments) {
        Point intersection = lineIntersection(new Point(center.x, center.y), new Point(s.p1.x, s.p1.y),
            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
        if (intersection != null && Math.abs(intersection.distance(center.x, center.y)) < Math.abs(new_p1.distance(center.x, center.y))) {
          new_p1 = intersection;
        }
//        Point intersection2 = lineIntersection(new Point(center.x, center.y), new Point(s.p2.x, s.p2.y),
//            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
//        if (intersection2 != null && Math.abs(intersection2.distance(center.x, center.y)) < Math.abs(new_p2.distance(center.x, center.y))) {
//          new_p2 = intersection2;
//        }
      }
      EndPoint new_e1 = new EndPoint(new_p1.x, new_p1.y);
      new_e1.angle = Math.atan2(new_e1.y - center.y, new_e1.x - center.x);
//      EndPoint new_e2 = new EndPoint(new_p2.x, new_p2.y);
//      new_e2.angle = Math.atan2(new_e2.y - center.y, new_e2.x - center.x);
      output.add(new_e1);
//      output.add(new_e2);
    }

    Collections.sort(output);
    setupOverlay();
  }

  /**
   * Run the algorithm, sweeping over all endpoints within the sight range
   * and created bunch of points that represent a polygon of visibility
   * Defaults the sight_range to the 1.5 * the sight_pixels
   */
  public void sweep() {
    sweep(sight_pixels * SIGHT_MULTIPLIER);
  }

  /**
   * Setup the overlay image to be used to draw on top of the given map. This
   * overlay should simply be a black square minus the visibility polygon and
   * all the walls that the visibility polygon touches.
   */
  private void setupOverlay() {
    overlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)overlay.getGraphics();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);

    int[] xs = output.stream().mapToInt(s -> s.x - location.x).toArray();
    int[] ys = output.stream().mapToInt(s -> s.y - location.y).toArray();
    int rule = AlphaComposite.CLEAR;
    float alpha = 0.1f;
    Composite comp = AlphaComposite.getInstance(rule, alpha);
    g.setComposite(comp);
    g.setPaint(Color.white);
    g.fillPolygon(xs, ys, xs.length);
    for(Tile tile : map.getWalls()) {
      Rectangle rect = tile.getBoundingRectangle();
      if (boundingRect.contains(rect)) {
        if (show_all_walls) {
          rect.x -= location.x;
          rect.y -= location.y;
          g.fill(rect);
        } else {
          rect.width += 1;
          rect.height += 1;
          for (EndPoint p : output) {
            if (rect.contains(p.x, p.y)) {
              rect.x -= location.x;
              rect.y -= location.y;
              rect.width -= 1;
              rect.height -= 1;
              g.fill(rect);
              break;
            }
          }
        }
      }
    }
  }

  /**
   * Draw the overlay image to the screen at the set location
   * @param g Graphics2D object with which to paint
   */
  public void paint(Graphics2D g) {
    g.drawImage(overlay, location.x, location.y, width, height, null);

//    drawSegmentsAndEndPoints(g);
//    drawCenter(g);
//    drawRays(g);
//    drawShortenedRays(g);
//    drawVisibilityPolygon(g);
  }

  /**
   * Draws a yellow circle at the center point
   * @param g Graphics2D object with which to paint
   */
  private void drawCenter(Graphics2D g) {
    g.setColor(Color.YELLOW);
    g.fillOval(center.x - 10, center.y - 10, 20, 20);
  }

  /**
   * Draws a blue circle at each end point and red line between each
   * of the segments
   * @param g Graphics2D object with which to paint
   */
  private void drawSegmentsAndEndPoints(Graphics2D g) {
    for(Segment s : segments) {
      g.setColor(Color.RED);
      g.drawLine(s.p1.x, s.p1.y, s.p2.x, s.p2.y);
      g.setColor(Color.BLUE);
      g.fillOval(s.p1.x - 3, s.p1.y - 3, 6, 6);
    }
  }

  /**
   * Draws a yellow line from the center to every end point
   * @param g Graphics2D object with which to paint
   */
  public void drawRays(Graphics2D g) {
    g.setColor(Color.YELLOW);
    for (Segment s : segments) {
      g.drawLine(center.x, center.y, s.p1.x, s.p1.y);
    }
  }

  /**
   * Draws a yellow line from the center towards every end point,
   * but stops at the first segment intersection
   * @param g Graphics2D object with which to paint
   */
  public void drawShortenedRays(Graphics2D g) {
    g.setColor(Color.YELLOW);
    for (EndPoint p : output) {
      g.drawLine(center.x, center.y, p.x, p.y);
    }
  }

  /**
   * Draws a semi transparent polygon where the visibility would be
   * @param g Graphics2D object with which to paint
   */
  public void drawVisibilityPolygon(Graphics2D g) {
    int[] xs = output.stream().mapToInt(s -> s.x).toArray();
    int[] ys = output.stream().mapToInt(s -> s.y).toArray();
    int rule = AlphaComposite.SRC_OVER;
    float alpha = 0.5f;
    Composite comp = AlphaComposite.getInstance(rule, alpha);
    g.setComposite(comp);
    g.setPaint(Color.YELLOW);
    g.fillPolygon(xs, ys, xs.length);
  }

  /**
   * EndPoint class to track all of the wall end points.
   */
  private class EndPoint extends Point implements Comparable {
    boolean begin = false;
    Segment segment = null;
    double angle = 0.0;
    boolean visualize = false;

    public EndPoint(int x, int y) {
      super(x, y);
    }

    @Override
    public int compareTo(Object o) {
      EndPoint ep = (EndPoint)o;
      // Traverse in angle order
      if (angle > ep.angle) return 1;
      if (angle < ep.angle) return -1;
      // But for ties (common), we want Begin nodes before End nodes
      if (!begin && ep.begin) return 1;
      if (begin && !ep.begin) return -1;
      return 0;
    }
  }

  /**
   * Segment class to track all of the wall line segments.
   */
  private class Segment {
    EndPoint p1;
    EndPoint p2;
    double d;
    double angle;

    public Segment() {
    }

    @Override
    public String toString() {
      return "Segment{" +
          "p1=" + p1 +
          ", p2=" + p2 +
          ", d=" + d +
          ", angle=" + angle +
          '}';
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Segment segment = (Segment) o;

      if (p1 != null ? !p1.equals(segment.p1) : segment.p1 != null) return false;
      return !(p2 != null ? !p2.equals(segment.p2) : segment.p2 != null);

    }

    @Override
    public int hashCode() {
      int result = p1 != null ? p1.hashCode() : 0;
      result = 31 * result + (p2 != null ? p2.hashCode() : 0);
      return result;
    }
  }

  /**
   * Main function for testing
   * @param args
   */
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
    shadow.setDimensions(map.getWidth(80), map.getHeight(80));
    shadow.setLightLocation(map.getWidth(80) / 2, map.getHeight(80) / 2);
    shadow.sweep(10000);
    JFrame frame = new JFrame("MapTest");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setLayout(new BorderLayout());
//    frame.setExtendedState(Frame.MAXIMIZED_BOTH);
    frame.setPreferredSize(new Dimension(1600, 800));


    JPanel map_panel = new JPanel()
    {
      public void paintComponent(Graphics g)
      {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(map.map_image, 0, 0, null);

        shadow.paint(g2);
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
}
