import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.net.URISyntaxException;
import java.util.*;

/**
 * Created by scnaegl on 9/20/15.
 */
public class Shadow {

  ArrayList<EndPoint> output = new ArrayList<EndPoint>();
  private ArrayList<EndPoint> endpoints = new ArrayList<EndPoint>();
  private ArrayList<Segment> segments = new ArrayList<Segment>();
  private Point center = new Point();
//  private LinkedList<Segment> open = new LinkedList<Segment>();
  private GameMap map;
  private BufferedImage background;
  public BufferedImage overlay;
  private int width = 800;
  private int height = 800;
  private int sight = 5;
  private int sight_pixels = 5 * GUI.tile_size;
  private Point location;

  public Shadow() {
  }

  public Shadow(GameMap map) {
    loadMap(map);
    this.map = map;
    setupBackground();
    location = new Point(0, 0);
  }

  public void setDimensions(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public void setPlayerSight(int sight) {
    this.sight = sight;
    this.sight_pixels = sight * GUI.tile_size;
  }

  private void setupBackground(){
    background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D bg = (Graphics2D)background.getGraphics();
    bg.setColor(Color.BLACK);
    bg.fillRect(0, 0, width, height);
    overlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  private void setupOverlay() {
    long t1 = System.currentTimeMillis();
//    int width = map.getWidth(GUI.tile_size);
//    int height = map.getHeight(GUI.tile_size);
    overlay = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = (Graphics2D)overlay.getGraphics();
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);

//    overlay = deepCopy(background);
//    Graphics2D g = (Graphics2D)overlay.getGraphics();

    int[] xs = output.stream().mapToInt(s -> s.x - location.x).toArray();
    int[] ys = output.stream().mapToInt(s -> s.y - location.y).toArray();
    int rule = AlphaComposite.CLEAR;
    float alpha = 0.1f;
    Composite comp = AlphaComposite.getInstance(rule, alpha);
    g.setComposite(comp);
//    bg.setColor(Color.YELLOW);
    g.setPaint(Color.white);
    g.fillPolygon(xs, ys, xs.length);
    for(Tile tile : map.getWalls()) {
      Rectangle rect = tile.getBoundingRectangle();
      rect.width += 1;
      rect.height += 1;
//      if (Math.hypot(tile.location.x - center.x, tile.location.y - center.y) > sight_pixels) {
//        continue;
//      }
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
    long t2 = System.currentTimeMillis();
    System.out.println("setting up overlay took: " + (t2 - t1));
  }

  static BufferedImage deepCopy(BufferedImage bi) {
    ColorModel cm = bi.getColorModel();
    boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
    WritableRaster raster = bi.copyData(null);
    return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
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
    shadow.setLightLocation(map.getWidth(80) / 2, map.getHeight(80) / 2);
    shadow.sweep();
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

  public void paint(Graphics2D g) {
    System.out.println("Painting shadows...");
    System.out.format("x=%d, y=%d, width=%d, height=%d\n", location.x, location.y, width, height);
    g.drawImage(overlay, location.x, location.y, width, height, null);
  }

  public void paint_OLD(Graphics2D g) {
    g.setColor(Color.YELLOW);
//        int[] xs = shadow.output.stream().mapToInt(s -> s.x).toArray();
//        int[] ys = shadow.output.stream().mapToInt(s -> s.y).toArray();
////        ArrayList<Integer> xs = CollectionUtils.collect(list,
//// TransformerUtils.invokerTransformer("getName")
//        //g.fillPolygon(xs, ys, shadow.output.size());
//        int[] test_xs = new int[3];
//        test_xs[0] = shadow.output.get(0).x;
//        test_xs[1] = shadow.output.get(1).x;
//        test_xs[2] = shadow.center.x;
//        int[] test_ys = new int[3];
//        test_ys[0] = shadow.output.get(0).y;
//        test_ys[1] = shadow.output.get(1).y;
//        test_ys[2] = shadow.center.y;
//        g.fillPolygon(test_xs, test_ys, 3);
//    g.setColor(Color.YELLOW);
//    g.fillOval(center.x - 10, center.y - 10, 20, 20);
//    int i = 0;
//    output.clear();
//    for(Segment s : segments) {
//      i++;
//      System.out.println("Segment: " + s);
//      g.setColor(Color.RED);
//      g.drawLine(s.p1.x, s.p1.y, s.p2.x, s.p2.y);
//      g.setColor(Color.BLUE);
//      g.fillOval(s.p1.x - 3, s.p1.y - 3, 6, 6);
//      g.fillOval(s.p2.x - 3, s.p2.y - 3, 6, 6);
//      g.setColor(Color.YELLOW);
//      Point new_p1 = s.p1;
//      Point new_p2 = s.p2;
//      System.out.println("p1: " + new_p1);
//      System.out.println("p2: " + new_p2);
//      long t1 = System.currentTimeMillis();
//      for(Segment s2 : segments) {
//        Point intersection = lineIntersection(new Point(center.x, center.y), new Point(s.p1.x, s.p1.y),
//            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
//        System.out.println("intersection: " + intersection);
//        if (intersection != null && Math.abs(intersection.distance(center.x, center.y)) < Math.abs(new_p1.distance(center.x, center.y))) {
//          new_p1 = intersection;
//        }
//        Point intersection2 = lineIntersection(new Point(center.x, center.y), new Point(s.p2.x, s.p2.y),
//            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
//        System.out.println("intersection 2: " + intersection2);
//        if (intersection2 != null && Math.abs(intersection2.distance(center.x, center.y)) < Math.abs(new_p2.distance(center.x, center.y))) {
//          new_p2 = intersection2;
//        }
//      }
//      g.drawLine(center.x, center.y, new_p1.x, new_p1.y);
//      g.drawLine(center.x, center.y, new_p2.x, new_p2.y);
//      EndPoint new_e1 = new EndPoint(new_p1.x, new_p1.y);
//      new_e1.angle = Math.atan2(new_e1.y - center.y, new_e1.x - center.x);
//      EndPoint new_e2 = new EndPoint(new_p2.x, new_p2.y);
//      new_e2.angle = Math.atan2(new_e2.y - center.y, new_e2.x - center.x);
//      output.add(new_e1);
//      output.add(new_e2);
////      if (i > 5) {
////        break;
////      }
//      long t2 = System.currentTimeMillis();
//      System.out.println("This took " + (t2 - t1));
//    }

//    Collections.sort(output);
    long t1 = System.currentTimeMillis();
//    int[] xs = output.stream().mapToInt(s -> s.x).toArray();
//    int[] ys = output.stream().mapToInt(s -> s.y).toArray();
//    int width = map.getWidth(GUI.tile_size);
//    int height = map.getHeight(GUI.tile_size);
//    BufferedImage background = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//    Graphics2D bg = (Graphics2D)background.getGraphics();
//    bg.setColor(Color.BLACK);
//    bg.fillRect(0, 0, width, height);
//    int rule = AlphaComposite.CLEAR;
//    float alpha = 0.1f;
//    Composite comp = AlphaComposite.getInstance(rule, alpha);
//    bg.setComposite(comp);
////    bg.setColor(Color.YELLOW);
//    bg.setPaint(Color.white);
//    bg.fillPolygon(xs, ys, xs.length);
//    for(Tile tile : map.getWalls()) {
//      Rectangle rect = tile.getBoundingRectangle();
//      rect.width += 1;
//      rect.height += 1;
////      if (Math.hypot(tile.location.x - center.x, tile.location.y - center.y) > sight_pixels) {
////        continue;
////      }
//      for (EndPoint p : output) {
//        if (rect.contains(p.x, p.y)) {
//          bg.fill(tile.getBoundingRectangle());
//          break;
//        }
//      }
//    }
    g.drawImage(overlay, 0, 0, map.getWidth(GUI.tile_size), map.getHeight(GUI.tile_size), null);
    long t2 = System.currentTimeMillis();
    System.out.println("Drawing took: " + (t2 - t1));
  }

  public void loadMap(GameMap map) {
    endpoints.clear();
    segments.clear();
//    setEndPoints(map.getWalls());
    int x, y, w, h;
    for(Tile wall : map.getWalls()) {
      x = wall.location.getX();
      y = wall.location.getY();
      w = wall.width;
      h = wall.height;
      addSegment(x, y, x + w, y); // top
      addSegment(x, y, x, y + h); // left
      addSegment(x + w, y, x + w, y + h); // right
      addSegment(x, y + h, x + w, y + h); // bottom
    }
  }

  private void addSegment(int x1, int y1, int x2, int y2) {
//    segments.add(new Segment(x, y, x2, y2));
//    System.out.format("x1=%d, y2=%d, x2=%d, y2=%d\n", x1, y1, x2, y2);
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

//    if (!segments.contains(segment)) {
      segments.add(segment);
      endpoints.add(p1);
      endpoints.add(p2);
//    }
  }

  public void setLightLocation(int x, int y) {
    System.out.println("Setting light location...");
    System.out.format("x=%d, y=%d\n", x, y);
    if (x != center.x || y != center.y) {
      this.center = new Point(x, y);
      this.location.setLocation(x - width / 2, y - height / 2);
      System.out.println("new location: " + this.location);
    }

//    for (Segment segment : segments) {
//      double dx = 0.5 * (segment.p1.x + segment.p2.x) - x;
//      double dy = 0.5 * (segment.p1.y + segment.p2.y) - y;
//      // NOTE: we only use this for comparison so we can use
//      // distance squared instead of distance. However in
//      // practice the sqrt is plenty fast and this doesn't
//      // really help in this situation.
//      segment.d = dx*dx + dy*dy;
//
//      // NOTE: future optimization: we could record the quadrant
//      // and the y/x or x/y ratio, and sort by (quadrant,
//      // ratio), instead of calling atan2. See
//      // <https://github.com/mikolalysenko/compare-slope> for a
//      // library that does this. Alternatively, calculate the
//      // angles and use bucket sort to get an O(N) sort.
//      segment.p1.angle = Math.atan2(segment.p1.y - y, segment.p1.x - x);
//      segment.p2.angle = Math.atan2(segment.p2.y - y, segment.p2.x - x);
//
//      double dAngle = segment.p2.angle - segment.p1.angle;
//      if (dAngle <= -Math.PI) { dAngle += 2*Math.PI; }
//      if (dAngle > Math.PI) { dAngle -= 2*Math.PI; }
//      segment.p1.begin = (dAngle > 0.0);
//      segment.p2.begin = !segment.p1.begin;
//    }
  }

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

  // Run the algorithm, sweeping over all or part of the circle to find
  // the visible area, represented as a set of triangles
  public void sweep(double maxAngle) {
    output.clear();  // output set of triangles
    long t1 = System.currentTimeMillis();
    for(Segment s : segments) {
      if (Math.hypot(center.x - s.p1.x, center.y - s.p1.y) > sight_pixels) {
        continue;
      }
//      Point new_p1 = s.p1;
      Point new_p2 = s.p2;
      for(Segment s2 : segments) {
//        Point intersection = lineIntersection(new Point(center.x, center.y), new Point(s.p1.x, s.p1.y),
//            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
//        if (intersection != null && Math.abs(intersection.distance(center.x, center.y)) < Math.abs(new_p1.distance(center.x, center.y))) {
//          new_p1 = intersection;
//        }
        Point intersection2 = lineIntersection(new Point(center.x, center.y), new Point(s.p2.x, s.p2.y),
            new Point(s2.p1.x, s2.p1.y), new Point(s2.p2.x, s2.p2.y));
        if (intersection2 != null && Math.abs(intersection2.distance(center.x, center.y)) < Math.abs(new_p2.distance(center.x, center.y))) {
          new_p2 = intersection2;
        }
      }
//      EndPoint new_e1 = new EndPoint(new_p1.x, new_p1.y);
//      new_e1.angle = Math.atan2(new_e1.y - center.y, new_e1.x - center.x);
      EndPoint new_e2 = new EndPoint(new_p2.x, new_p2.y);
      new_e2.angle = Math.atan2(new_e2.y - center.y, new_e2.x - center.x);
//      output.add(new_e1);
      output.add(new_e2);
    }
    long t2 = System.currentTimeMillis();
    System.out.println("This took " + (t2 - t1));

    Collections.sort(output);
    setupOverlay();
  }

  public void sweep() {
    sweep(999.0);
  }

  public class EndPoint extends Point implements Comparable {
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

  public class Segment {
    EndPoint p1;
    EndPoint p2;
    double d;
    double angle;

    public Segment() {

    }

    public Segment(int x, int y, int x2, int y2) {
      p1 = new EndPoint(x, y);
      p2 = new EndPoint(x2, y2);
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
}
