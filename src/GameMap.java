import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * GameMap procedurally generates a map that is 1.5 times bigger than the
 * actual screen. It contains walls, corners, floors, obsticles , inside walls,
 * and hallways.
 */
public class GameMap
{

  public static final int X_SIZE = 40; // used to set size of grid
  public static final int Y_SIZE = 40; // used to set size of grid
  /**
   * These are the "types" that we have to put into the Tile class
   * <p>
   * they have corrosponding with enums
   */
  private static final char DOOR = '`';
  private static final char END_ROOM = 'E';
  private static final char ROOM_WALL = 'B';
  private static final char ROOM_CORNER = 'C';
  private static final char BASIC_TILE = '*';
  private static final char START_ROOM = 'S';
  private static final char HALL = 'H';
  private static final char EMPTY = '.';
  private static final char INSIDE_WALL = 'I';
  private static final int MAX_ROOM_SIZE = 12;
  private static final int MIN_ROOM_SIZE = 6;

  private static final int OFFSET = 24;
  private static final boolean SHOW_COORDS = false;

  private static int numberOfInitalHalls = 4;
  private static int numberOfRandomHalls = 3;
  private static int numberOfRooms = 10;

  // we are going to need to find a way to change this number when
  // starting a new level and such
  private static int numberOfObsicles = 8;
  private static int roomSize;
  private static int buildRoomX;
  private static int buildRoomY;
  /**
   * used to determine if a hall can be made
   */
  private static boolean hallRight = false;
  private static boolean hallLeft = false;
  private static boolean hallUp = false;
  private static boolean hallDown = false;
  private static Random random = new Random();
  private static Block[][] blockGrid = new Block[Y_SIZE][X_SIZE];
  public Location start_location;
  public Location end_location;
  ArrayList<Zombie> zombies = new ArrayList<>();
  ArrayList<FireTrap> traps = new ArrayList<>();
  public int trapSize = traps.size();
  Zombie master;
  BufferedImage map_image;
  private int num_rows;
  private int num_cols;
  private Tile[][] grid;
  private ArrayList<Tile> walls = new ArrayList<>();

  //this will be called the first

  /**
   * This is the game map and where the map gets generated, along with where
   * the player
   * starts, the zombies start, where the exit is.
   *
   * @param level the level that is called setting the xpmbie spawn
   */
  public GameMap(int level)
  {
    generateMap();


    this.num_rows = Y_SIZE + OFFSET;
    this.num_cols = X_SIZE + OFFSET;
    grid = new Tile[num_rows][num_cols];
    Random rand = new Random();
    int minRow = OFFSET / 2;
    int minCol = OFFSET / 2;

    int r = minRow;
    boolean spawnMasterZombie = true; // this boolean makes so the master zombie
    // will only be spawned once

    //intialize all the tiles to empty in order to make a nice border around
    // the
    //map
    for (int row = 0; row < num_rows; row++)
    {
      for (int col = 0; col < num_cols; col++)
      {
        Tile empty_tile = new Tile(row, col, TileType.WALL);
        grid[row][col] = empty_tile;
      }
    }

    // Make sure we have walls around the edges of the map in the walls list
    // so that the shadows will work properly
    for(int col = minCol - 1; col < num_cols - minCol; col++) {
      Tile tile = new Tile(minRow - 1, col, TileType.WALL);
      grid[minRow - 1][col] = tile;
      walls.add(tile);
      Tile tile2 = new Tile(num_rows - minRow, col, TileType.WALL);
      grid[num_rows - minRow][col] = tile2;
      walls.add(tile2);
    }
    for(int row = minRow; row < num_rows - minRow - 1; row++) {
      Tile tile = new Tile(row, minCol - 1, TileType.WALL);
      grid[row][minCol - 1] = tile;
      walls.add(tile);
      Tile tile2 = new Tile(row, num_cols - minCol, TileType.WALL);
      grid[num_rows - minRow][num_cols - minCol] = tile2;
      walls.add(tile2);
    }

    for (Block[] row : blockGrid)
    {
      //we have to set c = to a diffrent col that way it gareentees us that the
      // screen will be big enough to allow easuy repainting with no errors
      int c = minCol;
      for (Block block : row)
      {
        Tile new_tile =
            new Tile(block.y + minRow, block.x + minCol, block.type);
        grid[r][c] = new_tile;
        //if it is a start location, itsets the player start to this location
        if (new_tile.tile_type == TileType.START)
        {
          start_location = new Location(new_tile.col * GUI.tile_size,
              new_tile.row * GUI.tile_size);
        }
        // if exit location, sets it to exit
        if (new_tile.tile_type == TileType.EXIT)
        {
          end_location =
              new Location(new_tile.col * GUI.tile_size + GUI.tile_size / 2,
                  new_tile.row * GUI.tile_size + GUI.tile_size / 2);
        }
        //makes the wall array list that way the zombies and players have a
        //reference into what they run into
        if (new_tile.tile_type == TileType.WALL ||
            new_tile.tile_type == TileType.INSIDEWALL)
        {
          walls.add(new_tile);
        }


        //if it is just a normal room tile it will run through steps to see if
        // if it should spawn a zombie or firetrap.
        if (new_tile.tile_type == TileType.BRICK)
        {
          if (rand.nextDouble() < GUI.zspawn + (.01 * (level - 1)))
          {
            Zombie zombie;
            Location location =
                new Location(new_tile.col * GUI.tile_size,
                    new_tile.row * GUI.tile_size);
            if (spawnMasterZombie)
            {
              zombie =
                  new MasterZombie(GUI.zspeed * 1.5, GUI.zsmell * 2, GUI.drate,
                      location);
              spawnMasterZombie = false;
            }
            else
            {
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

            }
            zombies.add(zombie);

          }

          if (rand.nextDouble() < GUI.fspawn)
          {
            FireTrap fireTrap;
            Location location =
                new Location(c * GUI.tile_size, r * GUI.tile_size);

            fireTrap = new FireTrap(50, 50, location);
            traps.add(fireTrap);

          }
        }
        c++;
      }
      r++;
    }

    // if no zombies were spawned on the map, this will garentee that a master
    //zombie will spawn.
    if (spawnMasterZombie)
    {
      for (int row = 0; row < num_rows; row++)
      {
        for (int col = 0; col < num_cols; col++)
        {
          if (grid[row][col].tile_type == TileType.BRICK && spawnMasterZombie)
          {
            Zombie zombie;
            Location location =
                new Location(grid[row][col].col * GUI.tile_size,
                    grid[row][col].row * GUI.tile_size);
            zombie =
                new MasterZombie(GUI.zspeed * 1.5, GUI.zsmell * 2, GUI.drate,
                    location);
            zombies.add(zombie);
            spawnMasterZombie = false;
          }

        }
      }
    }

    this.map_image = convertMapToImage(GUI.tile_size);
  }

  /**
   * GameMap constuctor that creates map from a file.
   * @param file File from which to create the map
   */
  public GameMap(File file)
  {
    createFromFile(file);
    this.map_image = convertMapToImage(GUI.tile_size);
  }


  /**
   * this is where the magic happens and the map is made.
   * There are many different things that happen in this method in order to make
   * a map from char types, then transform them into ENums to print later on
   */
  public static void generateMap()
  {
    boolean mapIsBad = true;
    //This is for making a map that is garenteed to work.
    //there are many times when a map is generated that doesn't work because
    // the rooms don't have any where to be put
    while (mapIsBad)
    {
      mapIsBad = false;

      for (int x = 0; x < X_SIZE; x++)
      {
        for (int y = 0; y < Y_SIZE; y++)
        {
          blockGrid[y][x] =
              new Block(x, y, EMPTY); //initalizes the grid to emptyy
        }
      }


      // makes the halls that the rooms can be build on, in order to have them
      // all connected
      for (int i = 0; i < numberOfInitalHalls; i++)
      {
        makeInitialHalls();
      }


      // I am purposefully ignoring this return value. There will always be
      // a spot for the start room
      buildRoom(START_ROOM);
      for (int i = 0; i < numberOfRooms; i++)
      {
        //If build room doesn't return true after 100 recursion steps
        // it most likely means the map is bad and will try again.
        if (!buildRoom(BASIC_TILE))
        {
          mapIsBad = true;
          break;
        }


      }
      if (mapIsBad)
      {
        continue;
      }
      chizelWalls(); // shaves protruding halls off so map has nice square halls
      for (int i = 0; i < numberOfObsicles; i++)
      {
        buildObsticales(); // makes obsticles inside rooms
      }

      for (int i = 0; i < numberOfRandomHalls; i++)
      {
        makeRandomHalls(); //makes random halls that connect halls together
      }
      expandHalls();
      makeDoors(); // makes doors where halls and rooms meet
      addWallsToHalls(); // adds walls to the halls
      turnCornersToWalls(); // turns corners to walls
      // makes the end room by using 5000 steps of recursion to see if it can be
      // placed
      if (makeEndRoom(0)) // makes an end room
      {
        mapIsBad = false;
      }
      else
      {
        mapIsBad = true;
      }
      if (mapIsBad)
      {
        continue;
      }
      turnDoorToFloor(); // makes door to floor
      makeInteriorWalls();
      doubleCheck(); // one last double check to prevent error
    }
  }

  private static void doubleCheck()
  {
    for (int y = 1; y < Y_SIZE - 1; y++)
    {
      for (int x = 1; x < X_SIZE - 1; x++)
      {
        if (isEmpty(x, y) && surroundingSpotWall(x, y))
        {
          setBlockType(x, y,
              ROOM_WALL); // goes through, checks to see if door
          // turns it to hall
        }
      }
    }
  }

  private static boolean surroundingSpotWall(int x, int y)
  {

    return (isBasic(x + 1, y) || isBasic(x - 1, y) || isBasic(x, y - 1) ||
        isBasic(x, y + 1));
  }



  private static void turnDoorToFloor()
  {
    for (int y = 1; y < Y_SIZE - 1; y++)
    {
      for (int x = 1; x < X_SIZE - 1; x++)
      {
        if (isDoor(x, y))
        {
          setBlockType(x, y,
              BASIC_TILE); // goes through, checks to see if door
          // turns it to hall
        }
      }
    }
  }

  private static boolean makeEndRoom(int inputNumber)
  {
    roomSize = 3;

    int validX = resetValidSpots(X_SIZE);
    int validY = resetValidSpots(Y_SIZE);

    for (int x = validX; x < X_SIZE - 3; x++)
    {
      for (int y = validY; y < Y_SIZE - 3; y++)
      {
        //goes through map to see if it can be a valid location
        if (validEndLocationHorozantal(x, y) && checkForStart(x, y))
        {
          placeEndPeicesHorzantal(x, y);
          return true;
        }
        else if (validEndLocationVerticle(x, y) && checkForStart(x, y))
        {
          placeEndPeicesVerticle(x, y);
          return true;
        }
      }
    }
    inputNumber++;
    if (inputNumber > 5000)
    {
      return false;
    }
    return makeEndRoom(inputNumber);
  }

  private static boolean checkForStart(int x, int y)
  {
    for (int t = x - 20; t < x + 20; t++)
    {
      for (int l = y - 20; l < y + 20; l++)
      {
        if (inBoundsWithBorder(t, l))
        {
          if (isStart(t, l))
          {
            return false;
          }
        }
      }
    }
    return true;
  }

  private static int resetValidSpots(int size)
  {
    return random.nextInt(size - 6) + 3;
  }

  private static void placeEndPeicesVerticle(int x, int y)
  {
    if (isEmpty(x + 1, y + 1))
    {
      setBlockType(x + 1, y + 1, END_ROOM);
      setBlockType(x, y + 1, BASIC_TILE);
      setBlockType(x + 1, y, ROOM_WALL);
      setBlockType(x + 2, y, ROOM_WALL);
      setBlockType(x + 2, y + 1, ROOM_WALL);
      setBlockType(x + 2, y + 2, ROOM_WALL);
      setBlockType(x + 1, y + 2, ROOM_WALL);
    }
    else if (isEmpty(x - 1, y + 1))
    {
      setBlockType(x - 1, y + 1, END_ROOM);
      setBlockType(x, y + 1, BASIC_TILE);
      setBlockType(x - 1, y, ROOM_WALL);
      setBlockType(x - 2, y, ROOM_WALL);
      setBlockType(x - 2, y + 1, ROOM_WALL);
      setBlockType(x - 2, y + 2, ROOM_WALL);
      setBlockType(x - 1, y + 2, ROOM_WALL);
    }
  }

  private static void placeEndPeicesHorzantal(int x, int y)
  {
    if (isEmpty(x + 1, y + 1))
    {
      setBlockType(x + 1, y + 1, END_ROOM);
      setBlockType(x + 1, y, BASIC_TILE);
      setBlockType(x, y + 1, ROOM_WALL);
      setBlockType(x, y + 2, ROOM_WALL);
      setBlockType(x + 1, y + 2, ROOM_WALL);
      setBlockType(x + 2, y + 2, ROOM_WALL);
      setBlockType(x + 2, y + 1, ROOM_WALL);

    }
    else if (isEmpty(x + 1, y - 1))
    {
      setBlockType(x + 1, y - 1, END_ROOM);
      setBlockType(x + 1, y, BASIC_TILE);
      setBlockType(x, y - 1, ROOM_WALL);
      setBlockType(x, y - 2, ROOM_WALL);
      setBlockType(x + 1, y - 2, ROOM_WALL);
      setBlockType(x + 2, y - 2, ROOM_WALL);
      setBlockType(x + 2, y - 1, ROOM_WALL);

    }
  }

  private static boolean validEndLocationHorozantal(int x, int y)
  {
    return ((isWall(x, y) && isWall(x + 1, y) && isWall(x + 2, y) &&
        inBoundsWithBorder(x + 3, y) &&
        (isEmpty(x + 1, y + 1) || isEmpty(x + 1, y - 1))));
  }

  private static boolean isStart(int x, int y)
  {
    return getBlock(x, y).type == START_ROOM;
  }

  private static boolean validEndLocationVerticle(int x, int y)
  {
    return (isWall(x, y) && isWall(x, y + 1) && isWall(x, y + 2) &&
        inBoundsWithBorder(x, y + 3) &&
        (isEmpty(x + 1, y + 1) || isEmpty(x - 1, y + 1)));
  }

  private static void chizelWalls()
  {
    for (int y = 1; y < Y_SIZE - 1; y++)
    {
      for (int x = 1; x < X_SIZE - 1; x++)
      {
        if (isHall(x, y) && surroundedThreeSide(x, y))
        {
          // if it is surrounded by three sides by empty tiles it shaves the
          // hall off
          setBlockType(x, y, EMPTY);
        }
      }
    }

    for (int y = Y_SIZE - 1; y > 1; y--)
    {
      for (int x = X_SIZE - 1; x > 1; x--)
      {
        if (isHall(x, y) && surroundedThreeSide(x, y))
        {
          // if it is surrounded by three sides by empty tiles it shaves the
          // hall off
          setBlockType(x, y, EMPTY);
        }
      }
    }
  }

  private static boolean surroundedThreeSide(int x, int y)
  {
    //if surrounded on 3 sides by empty tiles return true
    return ((isEmpty(x + 1, y) && isEmpty(x - 1, y) && isEmpty(x, y + 1)) ||
        (isEmpty(x + 1, y) && isEmpty(x - 1, y) && isEmpty(x, y - 1)) ||
        (isEmpty(x + 1, y) && isEmpty(x, y + 1) && isEmpty(x, y - 1)) ||
        (isEmpty(x - 1, y) && isEmpty(x, y + 1) && isEmpty(x, y - 1)));
  }

   // makes inital halls that will help determine how rooms will be placed
   // halls will span entire grid
  private static void makeInitialHalls()
  {
    int randomX = random.nextInt(X_SIZE - 2) + 1;
    int randomY = random.nextInt(Y_SIZE - 2) + 1;

    for (int x = 2; x < X_SIZE - 2; x++)
    {
      setBlockType(x, randomY, HALL);
    }

    for (int y = 2; y < Y_SIZE - 2; y++)
    {
      setBlockType(randomX, y, HALL);
    }
  }

  private static void makeInteriorWalls()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (isWall(x, y))
        {
          if (surroundingSpotEmpty(x, y))
          {

          }
          else
          {
            setBlockType(x, y, INSIDE_WALL);
          }
        }
      }
    }
  }

  //used for making inside walls
  private static boolean surroundingSpotEmpty(int x, int y)
  {
    return (isEmpty(x + 1, y) || isEmpty(x - 1, y) || isEmpty(x, y - 1) ||
        isEmpty(x, y + 1));
  }

  private static boolean isEmpty(int x, int y)
  {
    //checks to see if spot in grid is empty
    return getBlock(x, y).type == EMPTY;
  }

  //turns corner to wall
  private static void turnCornersToWalls()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (isCorner(x, y))
        {
          setBlockType(x, y, ROOM_WALL);
        }
      }
    }
  }

  private static boolean isCorner(int x, int y)
  {
    //checks to see if is a corner in grid
    return getBlock(x, y).type == ROOM_CORNER;
  }

  //make doors
  private static void makeDoors()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {

        if (isHall(x, y))
        {
          if (isWall(x + 1, y))
          {
            setBlockType(x + 1, y, DOOR);
          }
          if (isWall(x - 1, y))
          {
            setBlockType(x - 1, y, DOOR);
          }
          if (isWall(x, y + 1))
          {
            setBlockType(x, y + 1, DOOR);
          }
          if (isWall(x, y - 1))
          {
            setBlockType(x, y - 1, DOOR);
          }
        }
      }
    }

    /**
     * resets walls to make rooms look like a good hall
     */
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (isDoor(x, y) && isDoor(x + 1, y) && isDoor(x + 2, y))
        {
          setBlockType(x, y, ROOM_WALL);
          setBlockType(x + 1, y, ROOM_WALL);
        }
        else if (isDoor(x, y) && isDoor(x, y + 1) && isDoor(x, y + 2))
        {
          setBlockType(x, y, ROOM_WALL);
          setBlockType(x, y + 1, ROOM_WALL);
        }
        else if (isDoor(x, y) && isDoor(x + 1, y) && chanceForRandomBuild())
        {
          setBlockType(x, y, ROOM_WALL);
        }
        else if (isDoor(x, y) && isDoor(x, y + 1) && chanceForRandomBuild())
        {
          setBlockType(x, y, ROOM_WALL);
        }
        if (isDoor(x, y) &&
            (isEmpty(x + 1, y) || isEmpty(x - 1, y) || isEmpty(x, y + 1) ||
                isEmpty(x, y - 1)))
        {
          //resets
          setBlockType(x, y, ROOM_WALL);
        }
      }
    }
  }

  //checks to see if it is a door
  private static boolean isDoor(int x, int y)
  {
    return getBlock(x, y).type == DOOR;
  }

  private static boolean chanceForRandomBuild()
  {
    boolean makeNumber = false;
    int number = random.nextInt(99);
    if (number < 50)
    {
      makeNumber = true;
    }
    //return true;
    return makeNumber;
  }

  //checks to see if it is a wall
  private static boolean isWall(int x, int y)
  {
    return getBlock(x, y).type == ROOM_WALL;
  }

  //checks to see if it is a wall
  private static boolean isHall(int x, int y)
  {
    return getBlock(x, y).type == HALL;
  }

  private static void addWallsToHalls()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (getBlock(x, y).type == HALL)
        {
          putUpTheWalls(x, y);
        }
      }
    }
  }

  //makes wall in make wall method
  private static void putUpTheWalls(int x, int y)
  {
    if (getBlock(x + 1, y).type == EMPTY)
    {
      setBlockType(x + 1, y, ROOM_WALL);
    }
    if (getBlock(x - 1, y).type == EMPTY)
    {
      setBlockType(x - 1, y, ROOM_WALL);
    }
    if (getBlock(x, y + 1).type == EMPTY)
    {
      setBlockType(x, y + 1, ROOM_WALL);
    }
    if (getBlock(x, y - 1).type == EMPTY)
    {
      setBlockType(x, y - 1, ROOM_WALL);
    }

  }
  //expand all made halls
  private static void expandHalls()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (getBlock(x, y).type == HALL)
        {
          if (getBlock(x + 1, y).type == HALL &&
              getBlock(x - 1, y).type == HALL)
          {
            setBlockType(x, y - 1, HALL);
            setBlockType(x + 1, y - 1, HALL);
            setBlockType(x - 1, y - 1, HALL);

          }

          if (getBlock(x, y + 1).type == HALL &&
              getBlock(x, y - 1).type == HALL)
          {
            setBlockType(x - 1, y, HALL);
            setBlockType(x - 1, y + 1, HALL);
            setBlockType(x - 1, y - 1, HALL);
          }
        }
      }
    }
  }

  private static void makeRandomHalls()
  {
    boolean valid = false;
    int hallX = random.nextInt(X_SIZE - 10) + 5;
    int hallY = random.nextInt(Y_SIZE - 10) + 5;
    while (!valid)
    {
      //checks to see if valid location
      if (touchingAnotherRoom(hallX, hallY) ||
          !validHallLocationForHalls(hallX, hallY))
      {
        hallX = random.nextInt(X_SIZE - 10) + 5;
        hallY = random.nextInt(Y_SIZE - 10) + 5;
      }
      else
      {
        valid = true;
      }
    }

    // each method will build a hall to a wall
    if (hallUp)
    {
      for (int a = hallX; a > 0; a--)
      {
        if (canMakeHall(a, hallY))
        {
          setBlockType(a, hallY, HALL);
        }
        else
        {
          break;
        }
      }
      hallUp = false;
    }

    if (hallDown)
    {
      for (int a = hallX; a < X_SIZE - 1; a++)
      {
        if (canMakeHall(a, hallY))
        {
          setBlockType(a, hallY, HALL);
        }
        else
        {
          break;
        }
      }
      hallDown = false;
    }
    if (hallRight)
    {
      for (int a = hallY; a < Y_SIZE - 1; a++)
      {
        if (canMakeHall(hallX, a))
        {
          setBlockType(hallX, a, HALL);
        }
        else
        {
          break;
        }
      }
      hallRight = false;
    }

    if (hallLeft)
    {
      for (int a = hallY; a > 0; a--)
      {
        if (canMakeHall(hallX, a))
        {
          setBlockType(hallX, a, HALL);
        }
        else
        {
          break;
        }
      }
      hallLeft = false;
    }
  }

  private static boolean validHallLocationForHalls(int x, int y)
  {

    int connectRooms = 0;
    int numberOfConnections = random.nextInt(3) + 1;
    // checks to see if it can make a hall and if it can the counter will
    // increase by one
    for (int a = x; a < X_SIZE; a++)
    {
      if (canMakeHall(a, y))
      {

      }
      else if (getBlock(a, y).type == ROOM_WALL)
      {

        connectRooms++;
        hallDown = true;
      }
      else
      {
        break;
      }
    }

    for (int a = x; a > 0; a--)
    {
      if (canMakeHall(a, y))
      {

      }
      else if (getBlock(a, y).type == ROOM_WALL)
      {
        connectRooms++;
        hallUp = true;
      }
      else
      {
        break;
      }
    }

    for (int a = y; a < Y_SIZE; a++)
    {
      if (canMakeHall(x, a))
      {

      }
      else if (getBlock(x, a).type == ROOM_WALL)
      {
        hallRight = true;
        connectRooms++;
      }
      else
      {
        break;
      }
    }

    for (int a = y; a > 0; a--)
    {
      if (canMakeHall(x, a))
      {

      }
      else if (getBlock(x, a).type == ROOM_WALL)
      {
        hallLeft = true;
        connectRooms++;

      }
      else
      {
        break;
      }
    }
    //checks to see if halls can connect a certain number of rooms specified
    // earlier
    if (connectRooms > numberOfConnections)
    {
      return true;
    }
    else
    {
      hallUp = false;
      hallLeft = false;
      hallRight = false;
      hallDown = false;
      return false;


    }
  }

  private static boolean canMakeHall(int x, int y)
  {
    return (getBlock(x, y).type == EMPTY || getBlock(x, y).type == HALL);
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
        setBlockType(xCord, yCord, INSIDE_WALL);
        validSpot = true;
      }
      else
      {
        xCord = random.nextInt(X_SIZE - 2) + 1;
        yCord = random.nextInt(Y_SIZE - 2) + 1;
      }
    }
  }

  private static boolean cycleSpotsForObsticles(int s, int t)
  {
    boolean valid = true;
    for (int x = s - 1; x <= s + 1; x++)
    {
      for (int y = t - 1; y <= t + 1; y++)
      {
        if (getBlock(x, y).type != BASIC_TILE)
        {
          valid = false;
        }
      }
    }
    return valid;
  }

  private static boolean buildRoom(char type)
  {
    //set random room dimetions
    resetRoomDimentions();
    //if start start ot end room rooms size'll be 4
    if (type == END_ROOM || type == START_ROOM)
    {
      roomSize = 5;
    }
    //this will keep reseting the dimetions till it is valid
    int numberOfRecusion = 0;
    if (!searchForRoom(type, numberOfRecusion))
    {
      return false;
    }


    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        //checks to see if corner and sets corner
        if (inBoundsWithBorder(x, y) &&
            cornerTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setBlockType(x, y, ROOM_CORNER);
          getBlock(x, y).partOfRoom = true;
        }
        //checks to see if Wall and sets wall
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setBlockType(x, y, ROOM_WALL);
          getBlock(x, y).partOfRoom = true;
        }
        else if (inBoundsWithBorder(x, y) && type == BASIC_TILE)
        {
          setBlockType(x, y, BASIC_TILE);
          getBlock(x, y).partOfRoom = true;
        }
        else if (inBoundsWithBorder(x, y) && type == END_ROOM)
        {
          setBlockType(x, y, END_ROOM);
          getBlock(x, y).partOfRoom = true;
        }
        else
        {
          setBlockType(x, y, START_ROOM);
          //        getBlock(x, y).partOfStartRoom = true;
          getBlock(x, y).partOfRoom = true;
        }
      }
    }
    return true;
  }

  private static boolean searchForRoom(char type, int numberOfRecursion)
  {

    boolean hallTouched = false;
    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        if (numberOfRecursion > 100)
        {
          return false;
        }

        if (isHall(x, y) && !wallTile(x, y, buildRoomX, buildRoomY,
            (buildRoomX + roomSize - 1),
            (buildRoomY + roomSize - 1)))
        {
          hallTouched = true;
        }
        if (placeOnTopOfAnotherRoom(x, y))
        {
          numberOfRecursion++;
          hallTouched = false;
          resetRoomDimentions();
          if (type == START_ROOM || type == END_ROOM)
          {
            roomSize = 5;
          }
          x = buildRoomX;
          y = buildRoomY;
        }
      }
    }
    if (!hallTouched)
    {
      numberOfRecursion++;
      resetRoomDimentions();
      if (type == START_ROOM || type == END_ROOM)
      {
        roomSize = 5;
      }
      return searchForRoom(type, numberOfRecursion);
    }
    return true;
  }


  private static boolean placeOnTopOfAnotherRoom(int x, int y)
  {
    return (touchingAnotherRoomExceptHall(x + 1, y - 1) ||
        touchingAnotherRoomExceptHall(x + 1, y) ||
        touchingAnotherRoomExceptHall(x + 1, y + 1) ||
        touchingAnotherRoomExceptHall(x, y - 1) ||
        touchingAnotherRoomExceptHall(x, y) ||
        touchingAnotherRoomExceptHall(x, y + 1) ||
        touchingAnotherRoomExceptHall(x - 1, y - 1) ||
        touchingAnotherRoomExceptHall(x - 1, y) ||
        touchingAnotherRoomExceptHall(x, y + 1));
  }

  private static boolean touchingAnotherRoomExceptHall(int x, int y)
  {

    return isWall(x, y) || isCorner(x, y) || isBasic(x, y);
  }

  private static boolean isBasic(int x, int y)
  {
    return getBlock(x, y).type == BASIC_TILE;
  }

  private static boolean wallTile(int x, int y, int xSmallBoundry,
                                  int ySmallBoundry, int xLargeBoundry,
                                  int yLargeBoundry)
  {
    return (x == xSmallBoundry ||
        x == (xLargeBoundry) || y == ySmallBoundry ||
        y == yLargeBoundry);
  }

  private static void setBlockType(int x, int y, char type)
  {
    getBlock(x, y).type = type;
  }

  //makes a room corner tile
  private static boolean cornerTile(int x, int y, int xSmallBoundry,
                                    int ySmallBoundry, int xLargeBoundry,
                                    int yLargeBoundry)
  {
    return (x == xSmallBoundry && y == ySmallBoundry) ||
        (x == xLargeBoundry && y == ySmallBoundry) ||
        (x == xSmallBoundry && y == yLargeBoundry) ||
        (x == xLargeBoundry && y == yLargeBoundry);
  }

  //checks to see if in border
  private static boolean inBoundsWithBorder(int x, int y)
  {
    return (x < X_SIZE - 1 && x > 0 && y < Y_SIZE - 1 && y > 0);
  }

  private static boolean touchingAnotherRoom(int x, int y)
  {
    return getBlock(x, y).getType() != EMPTY;
  }

  // get the block at x and y cordonates
  private static Block getBlock(int x, int y)
  {
    return blockGrid[y][x];
  }

  //resets the room dimentions
  private static void resetRoomDimentions()
  {
    buildRoomX = random.nextInt(X_SIZE - 14) + 1;
    buildRoomY = random.nextInt(Y_SIZE - 14) + 1;
    roomSize = makeRoomSize();
  }

  //makes a room of a certain size with min value and max
  private static int makeRoomSize()
  {
    while (true)
    {
      roomSize = random.nextInt(MAX_ROOM_SIZE) + 1;
      if (roomSize >= MIN_ROOM_SIZE)
      {
        return roomSize;
      }
    }
  }

  /**
   * gets the width of the entire image in pixels
   * @param tile_size The Tile size of the images in pixels
   * @return the width of the entire image in pixels
   */
  public int getWidth(int tile_size)
  {
    return num_cols * tile_size;
  }

  /**
   * gets the height in pixels of the entire image
   * @param tile_size The tile size of the images pixels
   * @return the height of the entire image in pixels
   */
  public int getHeight(int tile_size)
  {
    return num_rows * tile_size;
  }

  /**
   * gets a specific tile
   * @param row the y value of the tile
   * @param col the x value of the tile
   * @return the type of tile
   */
  public Tile getTile(int row, int col)
  {
    return grid[row][col];
  }

  /**
   * a getter for the array list of walls
   * @return the walls of the map
   */
  public ArrayList<Tile> getWalls()
  {
    return walls;
  }

  /**
   * This will convert our map to a buffered image so it will run smoothly
   * @param tile_size how large we want the tiles to be
   * @return an image of the map
   */
  public BufferedImage convertMapToImage(int tile_size)
  {
    BufferedImage new_image =
        new BufferedImage(num_cols * tile_size, num_rows * tile_size,
            BufferedImage.TYPE_INT_RGB);
    Graphics2D g = new_image.createGraphics();
    for (int row = 0; row < num_rows; row++)
    {
      for (int col = 0; col < num_cols; col++)
      {
        g.drawImage(grid[row][col].tile_type.image, col * tile_size,
            row * tile_size, tile_size, tile_size, null);
      }
    }
    return new_image;
  }

  /**
   * This allows for the changing of tiles on the map to change when they become
   * burnt by a firetrap.
   * @param row the y value of the grid
   * @param col the x value of the grid
   * @param tile_size size of the tiles we want to print in piels
   */
  public void updateTileOnImage(int row, int col, int tile_size)
  {
    Graphics2D g = (Graphics2D) map_image.getGraphics();
    g.drawImage(grid[row][col].tile_type.image, col * tile_size,
        row * tile_size, tile_size, tile_size, null);
  }

  /**
   * turns a tile into a burnt tile if a firetrap explodes nect to a tile
   *
   * @param row gets the row of the tile
   * @param col gets the col of the tile
   */
  public void burnTile(int row, int col)
  {

    if (grid[row][col].tile_type.equals(TileType.BRICK)
        || grid[row][col].tile_type.equals(TileType.INSIDEWALL))
    {
      walls.remove(grid[row][col]);
      grid[row][col].tile_type = TileType.BURNTFLOOR;
    }
    if (grid[row][col].tile_type.equals(TileType.WALL))
    {
      grid[row][col].tile_type = TileType.BURNTWALL;
    }
    updateTileOnImage(row, col, GUI.tile_size);
  }

  /**
   * This paints the entire grid from start to finish
   *
   * @param g Graphics object with which to paint
   */
  public void paint(Graphics2D g)
  {
    g.drawImage(map_image, 0, 0, null);
  }

  /**
   * Creates map from a file
   *
   * @param file File from which to create map
   */
  private void createFromFile(File file) {
    Scanner sc;
    ArrayList<ArrayList<Tile>> grid = new ArrayList<ArrayList<Tile>>();
    Random rand = new Random();
    int row = 0;
    int col = 0;

    try {
      sc = new Scanner(file);
      while (sc.hasNextLine()) {
        ArrayList<Tile> row_array = new ArrayList<Tile>();
        String row_string = sc.nextLine();
        if (!row_string.startsWith("!")) {
          char[] row_types = row_string.toCharArray();
          col = 0;

          for (char type : row_types) {
            Tile new_tile = new Tile(row, col, type);
            row_array.add(new_tile);
            if (new_tile.tile_type == TileType.WALL) {
              walls.add(new_tile);
            }
            if (new_tile.tile_type == TileType.BRICK) {
              if (rand.nextDouble() < GUI.zspawn) {
                Zombie zombie;
                Location location =
                    new Location(col * GUI.tile_size, row * GUI.tile_size);
                if (rand.nextBoolean()) {
                  System.out.println("made random zombie");
                  zombie =
                      new RandomWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                          location);
                } else {
                  System.out.println("made line zombie");

                  zombie =
                      new LineWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate,
                          location);
                }
                zombies.add(zombie);
                System.out.println(zombies);
              }

              if (rand.nextDouble() < GUI.fspawn) {
                FireTrap fireTrap;
                Location location =
                    new Location(col * GUI.tile_size, row * GUI.tile_size);

                fireTrap = new FireTrap(location);
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
      for (int i = 0; i < grid.size(); i++) {
        Tile[] row_array = new Tile[grid.get(i).size()];
        row_array = grid.get(i).toArray(row_array);
        this.grid[i] = row_array;
      }
    } catch (IOException e) {
      System.err.println("Can't find file: " + file.getName());
      System.exit(1);
    }
  }
}
