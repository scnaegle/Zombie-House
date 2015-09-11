import javax.imageio.ImageIO;
import javax.xml.bind.SchemaOutputResolver;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMap
{

  // as the levels progress we may want to make them bigger and add more rooms
  // so these final varibles may not always be final
  private static final int X_SIZE = 50;
  private static final int Y_SIZE = 100;
  private static final int MAX_ROOM_SIZE = 7;
  private static final int MIN_ROOM_SIZE = 3;
  private static final int END_ROOM_SIZE = 4;
  private static final int EMPTY = 0;
  private static final int END_ROOM = 3;
  private static final int ROOM_WALL = 9;
  private static final int ROOM_CORNER = 8;
  private static final int BASIC_TILE = 1;
  private static final int START_ROOM = 2;
  private static final int HALL = 4;
  private static int roomSize;
  private static int numberOfRandomHalls = 3;
  private static int numberOfRooms = 15;
  private static int numberOfObsicles = 3;
  // we are going to need to find a way to change this number when
  // starting a new level and such
  private static int buildRoomX;
  private static int buildRoomY;

  private static boolean hallEast = false;
  private static boolean hallWest = false;
  private static boolean hallNorth = false;
  private static boolean hallSouth = false;

  private static Random random = new Random();

  private static int[][] intGrid = new int[X_SIZE][Y_SIZE];

  private int num_rows;
  private int num_cols;
  private Tile[][] grid;
  private ArrayList<Tile> walls = new ArrayList<Tile>();


  public GameMap(File file)
  {
    createFromFile(file);
  }

  public ArrayList<Tile> getWalls()
  {
    return walls;
  }

  /**
   * This paints the grid between the the given start (row, col) and end
   * (row, col)
   *
   * @param g
   * @param start
   * @param end
   * @param tile_size
   */
  public void paintSection(Graphics g, Location start, Location end,
                           int tile_size)
  {
    for (int row = start.row; row < end.row; row++)
    {
      for (int col = start.col; col < end.col; col++)
      {
        g.setColor(grid[row][col].tile_type.color);
        g.fillRect(col * tile_size, row * tile_size, tile_size, tile_size);
      }
    }
  }

  /**
   * This paints the entire grid from start to finish
   *
   * @param g
   * @param tile_size
   */
  public void paint(Graphics g, int tile_size)
  {
    paintSection(g, new Location(0, 0), new Location(num_rows, num_cols),
        tile_size);
  }

  public static int roomSize()
  {
    int roomSize;
    while (true)
    {
      roomSize = random.nextInt(MAX_ROOM_SIZE) + 1;
      if (roomSize >= MIN_ROOM_SIZE)
      {
        return roomSize;
      }
    }

  }

  private static void buildRoomExact()
  {
    buildRoomX = 39;
    buildRoomY = 39;
    int roomSize = 10;

    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        if (inBoundsWithBorder(x, y) && (x == buildRoomX ||
            x == (buildRoomX + roomSize - 1) || y == buildRoomY ||
            y == (buildRoomY + roomSize - 1)))
        {
          intGrid[x][y] = ROOM_WALL;
        }
        else if (inBoundsWithBorder(x, y))
        {
          intGrid[x][y] = BASIC_TILE;
        }
      }
    }
  }

  private static void resetRoomDimentions()
  {
    buildRoomX = random.nextInt(X_SIZE - 15) + 2;
    buildRoomY = random.nextInt(Y_SIZE - 15) + 2;
    roomSize = roomSize();
  }

  //checks to see if a wall tile
  private static boolean wallTile(int x, int y, int xSmallBoundry,
                                  int ySmallBoundry, int xLargeBoundry,
                                  int yLargeBoundry)
  {
    if (x == xSmallBoundry ||
        x == (xLargeBoundry) || y == ySmallBoundry ||
        y == yLargeBoundry)
    {
      return true;
    }
    return false;
  }

  //checks to see if it is a corner tile.
  private static boolean cornerTile(int x, int y, int xSmallBoundry,
                                    int ySmallBoundry, int xLargeBoundry,
                                    int yLargeBoundry)
  {
    if ((x == xSmallBoundry && y == ySmallBoundry) ||
        (x == xLargeBoundry && y == ySmallBoundry) ||
        (x == xSmallBoundry && y == yLargeBoundry) ||
        (x == xLargeBoundry && y == yLargeBoundry))
    {
      return true;
    }
    return false;
  }

  private static void buildEndRoom()
  {
    resetRoomDimentions();

    for (int x = buildRoomX; x < buildRoomX + END_ROOM_SIZE; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + END_ROOM_SIZE; y++)
      {
        if (touchingAnotherRoom(x, y))
        {
          resetRoomDimentions();
          x = buildRoomX;
          y = buildRoomY;
        }
      }
    }


    for (int x = buildRoomX; x < buildRoomX + END_ROOM_SIZE; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + END_ROOM_SIZE; y++)
      {
        // Need to create another method or this if statment... it will do
        // for now though.
        if (inBoundsWithBorder(x, y) &&
            cornerTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + END_ROOM_SIZE - 1),
                (buildRoomY + END_ROOM_SIZE - 1)))
        {
          intGrid[x][y] = ROOM_CORNER;
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + END_ROOM_SIZE - 1),
                (buildRoomY + END_ROOM_SIZE - 1)))
        {
          intGrid[x][y] = ROOM_WALL;
        }
        else if (inBoundsWithBorder(x, y))
        {
          intGrid[x][y] = END_ROOM;
        }
      }
    }
    System.out.println("finish end room");
  }

  private static void buildStartRoom()
  {
    resetRoomDimentions();

    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        if (touchingAnotherRoom(x, y))
        {
          resetRoomDimentions();
          x = buildRoomX;
          y = buildRoomY;
        }
      }
    }

    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        // Need to create another method or this if statment... it will do
        // for now though.
        if (inBoundsWithBorder(x, y) &&
            cornerTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          intGrid[x][y] = ROOM_CORNER;
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          intGrid[x][y] = ROOM_WALL;
        }
        else if (inBoundsWithBorder(x, y))
        {
          intGrid[x][y] = START_ROOM;
        }
      }
    }
    System.out.println("finish start room");
  }


  /**
   * builds a room to go into the maze
   */
  private static void buildRoom()
  {
    resetRoomDimentions();

    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        if (touchingAnotherRoom(x, y))
        {
          resetRoomDimentions();
          x = buildRoomX;
          y = buildRoomY;
        }
      }
    }

    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        // Need to create another method or this if statment... it will do
        // for now though.
        if (inBoundsWithBorder(x, y) &&
            cornerTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          intGrid[x][y] = ROOM_CORNER;
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          intGrid[x][y] = ROOM_WALL;
        }
        else if (inBoundsWithBorder(x, y))
        {
          intGrid[x][y] = BASIC_TILE;
        }
      }
    }
  }

  private static boolean touchingAnotherRoom(int x, int y)
  {

    if (intGrid[x][y] != EMPTY)
    {
      return true;
    }
    return false;
  }

  private static boolean inBoundsWithBorder(int x, int y)
  {
    if (x < X_SIZE - 1 && x > 1 && y < Y_SIZE - 1 && y > 1)
    {
      return true;
    }
    return false;
  }


  /**
   * If two rooms are connected it will break through the walls of the room
   * and form a path,
   * not only does this connect the rooms, but also builds the obsticle type
   * things
   * pretty stoked that it worked pleasently. I am sure I will add some
   * obsticles
   */
  private static void breakTouchingWalls()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (intGrid[x][y] == ROOM_WALL)
        {
          if (intGrid[x + 1][y] == ROOM_WALL &&
              intGrid[x - 1][y] == ROOM_WALL &&
              intGrid[x][y + 1] == ROOM_WALL)
          {
            intGrid[x][y] = BASIC_TILE;
            intGrid[x][y + 1] = BASIC_TILE;
          }
          else if (intGrid[x + 1][y] == ROOM_WALL &&
              intGrid[x - 1][y] == ROOM_WALL &&
              intGrid[x][y - 1] == ROOM_WALL)
          {
            intGrid[x][y] = BASIC_TILE;
            intGrid[x][y - 1] = BASIC_TILE;
          }
          else if (intGrid[x][y + 1] == ROOM_WALL &&
              intGrid[x][y - 1] == ROOM_WALL &&
              intGrid[x + 1][y] == ROOM_WALL)
          {
            intGrid[x][y] = BASIC_TILE;
            intGrid[x + 1][y] = BASIC_TILE;
          }
          else if (intGrid[x][y + 1] == ROOM_WALL &&
              intGrid[x][y - 1] == ROOM_WALL &&
              intGrid[x - 1][y] == ROOM_WALL)
          {
            intGrid[x][y] = BASIC_TILE;
            intGrid[x - 1][y] = BASIC_TILE;
          }
        }
      }
    }
  }


  private static void generateMap()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        intGrid[x][y] = EMPTY;
      }
    }

    buildStartRoom();
    buildEndRoom();

    //builds specified number of rooms
    for (int i = 0; i < numberOfRooms; i++)
    {
      buildRoom();
    }
    breakTouchingWalls();

    //makes obsticles
    for (int i = 0; i < numberOfObsicles; i++)
    {
      buildObsticales();
    }

    for (int i = 0; i < numberOfRandomHalls; i++)
    {
      makeRandomHalls();
    }

    // print out that maze
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        System.out.print(intGrid[x][y]);
      }
      System.out.println("");
    }
  }

  private static void makeRandomHalls()
  {
    boolean valid = false;
    int hallX = random.nextInt(X_SIZE - 10) + 5;
    int hallY = random.nextInt(Y_SIZE - 10) + 5;
    while (!valid)
    {
      if (touchingAnotherRoom(hallX, hallY) || !validHallLocation(hallX, hallY))
      {
        hallX = random.nextInt(X_SIZE - 10) + 5;
        hallY = random.nextInt(Y_SIZE - 10) + 5;
      }
      else
      {
        valid = true;
      }
    }

    if (hallEast)
    {
      for (int a = hallX; a < X_SIZE - 1; a++)
      {
        if (intGrid[a][hallY] == EMPTY)
        {
          intGrid[a][hallY] = HALL;
        }
        else
        {

        }
      }
      hallEast=false;
    }
    if (hallWest)
    {
      for (int a = hallX; a > 0; a--)
      {
        if (intGrid[a][hallY] == EMPTY)
        {
          intGrid[a][hallY] = HALL;
        }
        else
        {

        }
      }
      hallWest=false;
    }
    if (hallSouth)
    {
      for (int a = hallY; a < Y_SIZE - 1; a++)
      {
        if (intGrid[hallX][a] == EMPTY)
        {
          intGrid[hallX][a] = HALL;
        }
        else
        {

        }
      }
      hallSouth=false;
    }
    if (hallNorth)
    {
      for (int a = hallY; a > 0; a--)
      {
        if (intGrid[hallX][a] == EMPTY)
        {
          intGrid[hallX][a] = HALL;
        }
        else
        {

        }
      }
      hallNorth=false;
    }
  }

  private static boolean validHallLocation(int x, int y)
  {
    boolean isValid = false;
    for (int a = x; a < X_SIZE; a++)
    {
      if (intGrid[a][y] == ROOM_WALL)
      {
        hallEast = true;
        isValid = true;
      }
    }

    for (int a = x; a > 0; a--)
    {
      if (intGrid[a][y] == ROOM_WALL)
      {
        hallWest = true;
        isValid = true;
      }
    }

    for (int a = y; a < Y_SIZE; a++)
    {
      if (intGrid[x][a] == ROOM_WALL)
      {
        hallSouth = true;
        isValid = true;
      }
    }

    for (int a = y; a > 0; a--)
    {
      if (intGrid[x][a] == ROOM_WALL)
      {
        hallNorth = true;
        isValid = true;
      }
    }
    return isValid;
  }

  private static boolean cycleSpotsForObsticles(int s, int t)
  {
    boolean valid = true;
    for (int x = s - 1; x <= s + 1; x++)
    {
      for (int y = t - 1; y <= t + 1; y++)
      {
        if (intGrid[x][y] != 1)
        {
          valid = false;
        }
      }
    }
    return valid;
  }

  private static void buildObsticales()
  {
    boolean validSpot = false;
    int xCord = random.nextInt(X_SIZE - 2) + 1;
    int yCord = random.nextInt(Y_SIZE - 2) + 1;
    while (!validSpot)
    {
      if (cycleSpotsForObsticles(xCord, yCord))
      {
        intGrid[xCord][yCord] = 7;
        validSpot = true;
      }
      else
      {
        xCord = random.nextInt(X_SIZE - 2) + 1;
        yCord = random.nextInt(Y_SIZE - 2) + 1;
      }
    }
  }

  /**
   * Creates map from a file
   *
   * @param file
   */
  private void createFromFile(File file)
  {
    Scanner sc;
    ArrayList<ArrayList<Tile>> grid = new ArrayList<ArrayList<Tile>>();
    int row = 0;
    int col = 0;

    try
    {
      sc = new Scanner(file);
      while (sc.hasNextLine())
      {
        ArrayList<Tile> row_array = new ArrayList<Tile>();
        String row_string = sc.nextLine();
        if (!row_string.startsWith("!"))
        {
          char[] row_types = row_string.toCharArray();
          col = 0;

          for (char type : row_types)
          {
            Tile new_tile = new Tile(row, col, type);
            row_array.add(new_tile);
            if (new_tile.tile_type == TileType.WALL)
            {
              walls.add(new_tile);
            }
            col++;
          }
          grid.add(row_array);
        }
        row++;
      }

      this.num_rows = grid.size();
      this.num_cols = grid.get(0).size();
      this.grid = new Tile[num_rows][num_cols];
      for (int i = 0; i < grid.size(); i++)
      {
        Tile[] row_array = new Tile[grid.get(i).size()];
        row_array = grid.get(i).toArray(row_array);
        this.grid[i] = row_array;
      }
    }
    catch (IOException e)
    {
      System.err.println("Can't find file: " + file.getName());
      System.exit(1);
    }
  }

  @Override
  public String toString()
  {
    String ret = "GameMap{" +
        "num_rows=" + num_rows +
        ", num_cols=" + num_cols + "}\n";
    for (int row = 0; row < num_rows; row++)
    {
      for (int col = 0; col < num_cols; col++)
      {
        ret += grid[row][col].tile_type.grid_char;
      }
      ret += "\n";
    }
    return ret;
  }

  public static void main(String[] args)
  {
    /**
     File map_file = null;
     try
     {
     map_file =
     new File(GameMap.class.getResource("resources/level1.map").toURI());
     }
     catch (URISyntaxException e)
     {
     e.printStackTrace();
     }
     System.out.println("file: " + map_file);
     GameMap map = new GameMap(map_file);
     System.out.println("map: " + map.toString());
     System.out.println("map walls: ");
     for (Tile tile : map.getWalls())
     {
     System.out.println(tile);
     }
     */

    generateMap();
  }
}
