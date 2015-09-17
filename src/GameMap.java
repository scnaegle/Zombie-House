import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class GameMap
{
  private static final boolean SHOW_COORDS = false;

  ArrayList<Zombie> zombies = new ArrayList<>();
  ArrayList<FireTrap> traps = new ArrayList<>();
  private int num_rows;
  private int num_cols;
  private Tile[][] grid;

  private ArrayList<Tile> walls = new ArrayList<>();


  public GameMap(GameMapWithBlocks mapWithBlocks)
  {
    int r = 0;
    Random rand = new Random();
    for (Tile[] row : mapWithBlocks.tileGrid)
    {

      int c = 0;
      for (Tile col : row)
      {
        grid[r][c] = col;
        if (col.tile_type == TileType.WALL)
        {
          walls.add(col);
        }
        if (col.tile_type == TileType.BRICK)
        {
          if (rand.nextDouble() < GUI.zspawn)
          {
            Zombie zombie;
            Location location =
                new Location(c * GUI.tile_size, r * GUI.tile_size);
            if (rand.nextBoolean())
            {
              zombie = new RandomWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                  location);
            }
            else
            {
              zombie = new LineWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                  location);
            }
            zombies.add(zombie);
          }

          if (rand.nextDouble() < GUI.fspawn)
          {
            FireTrap fireTrap;
            Location location =
                new Location(c * GUI.tile_size, r * GUI.tile_size);

            fireTrap = new FireTrap(location);
            traps.add(fireTrap);

          }
        }
        c++;
      }
      r++;
    }
  }

  public GameMap(File file)
  {
    createFromFile(file);
  }


  public static void main(String[] args)
  {
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
    System.out.println("zombies: " + map.zombies.size());
//     System.out.println("map walls: ");
//     for (Tile tile : map.getWalls())
//     {
//     System.out.println(tile);
//     }

//    generateMap();
  }

  public double getZombieSpawnRate()
  {
    return GUI.zspawn;
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
        if (SHOW_COORDS)
        {
          g.setColor(Color.WHITE);
          g.drawString(String.format("(%d, %d)", row, col).toString(),
              col * tile_size + (tile_size / 4),
              row * tile_size + (tile_size / 2));
        }
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
    paintSection(g, new Location(0, 0, 0, 0),
        new Location(0, 0, num_rows, num_cols),
        tile_size);
  }

  public int getWidth(int tile_size)
  {
    return num_cols * tile_size;
  }

  public int getHeight(int tile_size)
  {
    return num_rows * tile_size;
  }

  public Tile getTile(int row, int col)
  {
    return grid[row][col];
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
    Random rand = new Random();
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
            if (new_tile.tile_type == TileType.BRICK)
            {
              if (rand.nextDouble() < GUI.zspawn)
              {
                Zombie zombie;
                Location location =
                    new Location(col * GUI.tile_size, row * GUI.tile_size);
                if (rand.nextBoolean())
                {
                  zombie =
                      new RandomWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                          location);
                }
                else
                {
                  zombie = new LineWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                      location);
                }
                zombies.add(zombie);
              }

              if (rand.nextDouble() < GUI.fspawn)
              {
                FireTrap fireTrap;
                Location location =
                    new Location(col * GUI.tile_size, row * GUI.tile_size);

                fireTrap = new FireTrap(50, 50, location);
                traps.add(fireTrap);

              }
            }
            col++;
          }
          grid.add(row_array);
          row++;
        }
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
}
