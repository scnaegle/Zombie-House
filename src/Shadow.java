import javax.swing.*;
import java.awt.*;
import java.awt.Frame;
import java.awt.geom.Line2D;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by scnaegl on 9/20/15.
 */
public class Shadow
{

  public static GameMap shadowMap;
  public ArrayList<ArrayList<Point>> demo_intersectionsDetected =
      new ArrayList<ArrayList<Point>>();
  ArrayList<Point> output = new ArrayList<Point>();
  private ArrayList<EndPoint> endpoints = new ArrayList<EndPoint>();
  private ArrayList<Segment> segments = new ArrayList<Segment>();
  private Point center = new Point();
  private LinkedList<Segment> open = new LinkedList<Segment>();


  public Shadow()
  {
//    for (int degree = 0; degree < 360; degree++)
//    {
//      if (wall is within playersight only draw that line that far)
//      {
//        drawline(playerCenter, playerCenter, playerSight * cos(x),
//            playerSight * sin(x))
//      }
//    }
//    connect end points;
//    fill everything triangle;
  }

  public Shadow(GameMap map)
  {
    loadMap(map);
    shadowMap = map;
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

        int centerPointX = shadow.center.x;
        int centerPointY = shadow.center.y;
        int[] test_xs = new int[3];
        int[] test_ys = new int[3];
        for (double degree = 0; degree < 360; degree += 10)
        {
          test_xs[0] = shadow.center.x;
          test_xs[1] = (int) (150 * Math.cos(degree)) + shadow.center.x;
          test_xs[2] = (int) (150 * Math.cos(degree)) + shadow.center.x;

          test_ys[0] = shadow.center.y;
          test_ys[1] = (int) (150 * Math.sin(degree)) + shadow.center.y;
          test_ys[2] = (int) (150 * Math.sin(degree)) + shadow.center.y;

          if (!shadow.passesThroughWall(test_xs[1], test_ys[1]) && !shadow.passesThroughWall(test_xs[2], test_ys[2]))
          {
            g.fillPolygon(test_xs, test_ys, 3);
          }

        }

        int[] xs = shadow.output.stream().mapToInt(s -> s.x).toArray();
        int[] ys = shadow.output.stream().mapToInt(s -> s.y).toArray();
//        ArrayList<Integer> xs = CollectionUtils.collect(list,
// TransformerUtils.invokerTransformer("getName")
        //g.fillPolygon(xs, ys, shadow.output.size());

        g.fillPolygon(test_xs, test_ys, 3);
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

//  private Point getIntersectionPoint(Line2D.Double line, Rectangle rectangle)
//  {
//
//  }

  private boolean passesThroughWall(int x, int y)
  {
    boolean passesThroughWall = false;
    Location location;
    location = new Location(x, y);
    Line2D line = new Line2D.Double(center.x, center.y, x, y);

    for (Tile wall : shadowMap.getWalls())
    {
      if (line.intersects(wall.getBoundingRectangle()))
      {
        passesThroughWall =true;
      }
    }
    return passesThroughWall;
  }

  public void loadMap(GameMap map)
  {
    endpoints.clear();
    segments.clear();
//    setEndPoints(map.getWalls());
    int x, y, w, h;
    for (Tile wall : map.getWalls())
    {
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

  private void setEndPoints(ArrayList<Tile> walls)
  {
    int x, y, w, h;
    for (Tile wall : walls)
    {
      System.out.println("wall: " + wall);
      x = wall.location.getX();
      y = wall.location.getY();
      w = wall.width;
      h = wall.height;
//      System.out.format("x=%d, y=%, w=%w, h=%h\n", x, y, w, h);
      addEndPoint(x, y); // top left
      addEndPoint(x + w, y); // top right
      addEndPoint(x, y + h); // bottom left
      addEndPoint(x + w, y + h); // bottom right
    }
  }

  private void addSegment(int x1, int y1, int x2, int y2)
  {
//    segments.add(new Segment(x, y, x2, y2));
    Segment segment = null;
    EndPoint p1 = new EndPoint(0, 0);
    p1.segment = segment;
    p1.visualize = true;
    EndPoint p2 = new EndPoint(0, 0);
    p2.segment = segment;
    p2.visualize = false;
    segment = new Segment();
    p1.x = x1;
    p1.y = y1;
    p2.x = x2;
    p2.y = y2;
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

  private void addEndPoint(int x, int y)
  {
    endpoints.add(new EndPoint(x, y));
  }

  private void setLightLocation(int x, int y)
  {
    this.center = new Point(x, y);

    for (Segment segment : segments)
    {
      double dx = 0.5 * (segment.p1.x + segment.p2.x) - x;
      double dy = 0.5 * (segment.p1.y + segment.p2.y) - y;
      // NOTE: we only use this for comparison so we can use
      // distance squared instead of distance. However in
      // practice the sqrt is plenty fast and this doesn't
      // really help in this situation.
      segment.d = dx * dx + dy * dy;

      // NOTE: future optimization: we could record the quadrant
      // and the y/x or x/y ratio, and sort by (quadrant,
      // ratio), instead of calling atan2. See
      // <https://github.com/mikolalysenko/compare-slope> for a
      // library that does this. Alternatively, calculate the
      // angles and use bucket sort to get an O(N) sort.
      segment.p1.angle = Math.atan2(segment.p1.y - y, segment.p1.x - x);
      segment.p2.angle = Math.atan2(segment.p2.y - y, segment.p2.x - x);

      double dAngle = segment.p2.angle - segment.p1.angle;
      if (dAngle <= -Math.PI)
      {
        dAngle += 2 * Math.PI;
      }
      if (dAngle > Math.PI)
      {
        dAngle -= 2 * Math.PI;
      }
      segment.p1.begin = (dAngle > 0.0);
      segment.p2.begin = !segment.p1.begin;
    }
  }

  public boolean segmentInFrontOf(Segment a, Segment b, Point relativeTo)
  {
    // NOTE: we slightly shorten the segments so that
    // intersections of the endpoints (common) don't count as
    // intersections in this algorithm
//    var A1 = leftOf(a, interpolate(b.p1, b.p2, 0.01));
//    var A2 = leftOf(a, interpolate(b.p2, b.p1, 0.01));
//    var A3 = leftOf(a, relativeTo);
//    var B1 = leftOf(b, interpolate(a.p1, a.p2, 0.01));
//    var B2 = leftOf(b, interpolate(a.p2, a.p1, 0.01));
//    var B3 = leftOf(b, relativeTo);

    // NOTE: this algorithm is probably worthy of a short article
    // but for now, draw it on paper to see how it works. Consider
    // the line A1-A2. If both B1 and B2 are on one side and
    // relativeTo is on the other side, then A is in between the
    // viewer and B. We can do the same with B1-B2: if A1 and A2
    // are on one side, and relativeTo is on the other side, then
    // B is in between the viewer and A.
//    if (B1 == B2 && B2 != B3) return true;
//    if (A1 == A2 && A2 == A3) return true;
//    if (A1 == A2 && A2 != A3) return false;
//    if (B1 == B2 && B2 == B3) return false;
    if (a.d < b.d) return true;

    // If A1 != A2 and B1 != B2 then we have an intersection.
    // Expose it for the GUI to show a message. A more robust
    // implementation would split segments at intersections so
    // that part of the segment is in front and part is behind.
    ArrayList<Point> inner_list = new ArrayList<Point>();
    inner_list.add(a.p1);
    inner_list.add(a.p2);
    inner_list.add(b.p1);
    inner_list.add(b.p2);
    demo_intersectionsDetected.add(inner_list);
    return false;

    // NOTE: previous implementation was a.d < b.d. That's simpler
    // but trouble when the segments are of dissimilar sizes. If
    // you're on a grid and the segments are similarly sized, then
    // using distance will be a simpler and faster implementation.
  }

  private void addTriangle(double angle1, double angle2, Segment segment)
  {
    Point p1 = center;
    Point p2 = new Point((int) (center.x + Math.cos(angle1)),
        (int) (center.y + Math.sin(angle1)));
    Point p3 = new Point(0, 0);
    Point p4 = new Point(0, 0);

    if (segment != null)
    {
      // Stop the triangle at the intersecting segment
      p3.x = segment.p1.x;
      p3.y = segment.p1.y;
      p4.x = segment.p2.x;
      p4.y = segment.p2.y;
    }
    else
    {
      // Stop the triangle at a fixed distance; this probably is
      // not what we want, but it never gets used in the demo
      p3.x = (int) (center.x + Math.cos(angle1) * 500);
      p3.y = (int) (center.y + Math.sin(angle1) * 500);
      p4.x = (int) (center.x + Math.cos(angle2) * 500);
      p4.y = (int) (center.y + Math.sin(angle2) * 500);
    }

    Point pBegin = lineIntersection(p3, p4, p1, p2);

    p2.x = (int) (center.x + Math.cos(angle2));
    p2.y = (int) (center.y + Math.sin(angle2));
    Point pEnd = lineIntersection(p3, p4, p1, p2);

    if (pBegin != null)
    {
      output.add(pBegin);
    }
    if (pEnd != null)
    {
      output.add(pEnd);
    }
  }

  public Point lineIntersection(Point p1, Point p2, Point p3, Point p4)
  {
    // From http://paulbourke.net/geometry/lineline2d/
    System.out.format("p1=%s, p2=%s, p3=%s, p4=%s\n", p1, p2, p3, p4);
    try
    {
      int s = ((p4.x - p3.x) * (p1.y - p3.y) - (p4.y - p3.y) * (p1.x - p3.x))
          / ((p4.y - p3.y) * (p2.x - p1.x) - (p4.x - p3.x) * (p2.y - p1.y));
      return new Point(p1.x + s * (p2.x - p1.x), p1.y + s * (p2.y - p1.y));
    }
    catch (ArithmeticException e)
    {
      return null;
    }
  }

  // Run the algorithm, sweeping over all or part of the circle to find
  // the visible area, represented as a set of triangles
  public void sweep(double maxAngle)
  {
    output.clear();  // output set of triangles
    demo_intersectionsDetected.clear();
//    endpoints.sort(endpointCompare, true);
    for (EndPoint ep : endpoints)
    {
      System.out.println("endpoints: " + ep);
    }
    Collections.sort(endpoints);

    open.clear();
    double beginAngle = 0.0;

    // At the beginning of the sweep we want to know which
    // segments are active. The simplest way to do this is to make
    // a pass collecting the segments, and make another pass to
    // both collect and process them. However it would be more
    // efficient to go through all the segments, figure out which
    // ones intersect the initial sweep line, and then sort them.
//    for (pass in 0...2) {
    for (int pass = 0; pass <= 2; pass++)
    {
      System.out.println("pass: " + pass);
      for (EndPoint p : endpoints)
      {
        System.out.println("EndPoint: " + p);
        if (pass == 1 && p.angle > maxAngle)
        {
          // Early exit for the visualization to show the sweep process
          break;
        }

        Segment current_old = open.isEmpty() ? null : open.get(0);

        System.out.println("p begin: " + p.begin);
        if (p.begin)
        {
          // Insert into the right place in the list
          Iterator<Segment> open_iter = open.iterator();
          Segment node = null;
          if (open_iter.hasNext())
          {
            node = open_iter.next();
          }
          while (node != null && open_iter.hasNext() &&
              segmentInFrontOf(p.segment, node, center))
          {
            node = open_iter.next();
          }
          if (open.isEmpty())
          {
            open.add(p.segment);
          }
          else
          {
            open.add(open.indexOf(node), p.segment);
          }
        }
        else
        {
          open.remove(p.segment);
        }

        Segment current_new = open.isEmpty() ? null : open.get(0);
        if (current_old != current_new)
        {
          if (pass == 1)
          {
            System.out.println("Gonna create a triangle");
            addTriangle(beginAngle, p.angle, current_old);
          }
          beginAngle = p.angle;
        }
      }
    }
  }

  public void sweep()
  {
    sweep(999.0);
  }

  public class EndPoint extends Point implements Comparable
  {
    boolean begin = false;
    Segment segment = null;
    double angle = 0.0;
    boolean visualize = false;

    public EndPoint(int x, int y)
    {
      super(x, y);
    }

    @Override
    public int compareTo(Object o)
    {
      EndPoint ep = (EndPoint) o;
      // Traverse in angle order
      if (angle > ep.angle) return 1;
      if (angle < ep.angle) return -1;
      // But for ties (common), we want Begin nodes before End nodes
      if (!begin && ep.begin) return 1;
      if (begin && !ep.begin) return -1;
      return 0;
    }
  }

  public class Segment
  {
    EndPoint p1;
    EndPoint p2;
    double d;
    double angle;

    public Segment()
    {

    }

    public Segment(int x, int y, int x2, int y2)
    {
      p1 = new EndPoint(x, y);
      p2 = new EndPoint(x2, y2);
    }

    @Override
    public boolean equals(Object o)
    {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;

      Segment segment = (Segment) o;

      if (p1 != null ? !p1.equals(segment.p1) : segment.p1 != null)
      {
        return false;
      }
      return !(p2 != null ? !p2.equals(segment.p2) : segment.p2 != null);

    }

    @Override
    public int hashCode()
    {
      int result = p1 != null ? p1.hashCode() : 0;
      result = 31 * result + (p2 != null ? p2.hashCode() : 0);
      return result;
    }
  }
}
