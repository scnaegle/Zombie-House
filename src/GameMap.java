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
  private static final char OBSTICLE = 'O';
  private static final char INSIDE_WALL = 'I';

  public static final int X_SIZE = 75; // used to set size of grid
  public static final int Y_SIZE = 75; // used to set size of grid
  private static final int MAX_ROOM_SIZE = 12;
  private static final int MIN_ROOM_SIZE = 6;
  private static final int END_ROOM_SIZE = 4;

  private static final int UP = 0;
  private static final int DOWN = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;


  private static int numberOfInitalHalls = 2;
  private static int numberOfRandomHalls = 2;
  private static int numberOfRooms = 10;
  private static int numberOfObsicles = 3;

  // we are going to need to find a way to change this number when
  // starting a new level and such

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

  private static final boolean SHOW_COORDS = false;

  ArrayList<Zombie> zombies = new ArrayList<>();
  ArrayList<FireTrap> traps = new ArrayList<>();
  private int num_rows;
  private int num_cols;
  private Tile[][] grid;

  private ArrayList<Tile> walls = new ArrayList<>();

  Location start_location;


  public GameMap()
  {
    generateMap();
    int r = 0;
    this.num_rows = Y_SIZE;
    this.num_cols = X_SIZE;
    grid = new Tile[num_rows][num_cols];
    Random rand = new Random();
    for (Block[] row : blockGrid)
    {
      int c = 0;
      for (Block block : row)
      {
        Tile new_tile = new Tile(block.y, block.x, block.type);
        grid[r][c] = new_tile;
        if (new_tile.tile_type == TileType.START) {
          start_location = new_tile.location;
        }
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
    System.out.println("map: ");
    System.out.println(toString());
  }

  public GameMap(File file)
  {
    createFromFile(file);
  }


  /**
   * generates the map through many many methods
   */
  public static void generateMap()
  {
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
      makeInitalHalls();
    }


    buildRoom(START_ROOM,
        true); // builds a start room where the player wills start
    for (int i = 0; i < numberOfRooms; i++)
    {
      buildRoom(BASIC_TILE,
          false); // builds rooms on the halls that had been made in
      // the previous halls
    }

    chizelWalls(); // makes protruding halls shaven off so map has even halls
    //   breakTouchingWalls(); // if two rooms are touching it will break the
    // walls
    /**
     * Need adjustment to make larger obsitcles and such
     */
    buildObsticales(); // makes obsticles inside rooms

    for (int i = 0; i < numberOfRandomHalls; i++)
    {
      makeRandomHalls(); //makes random halls that connect halls together
    }
    //   searchAlgorithm(END_ROOM);
    expandHalls(); //expands halls so
    makeDoors(); // makes doors where halls and rooms meet
    addWallsToHalls(); // adds walls to the halls
    /**
     * sets a zombie spawn. Need to change around to the generate map
     */
    spawnZombie();
    /**
     * these next couple of methods cause it to be used for basic testing until
     * I implement it to be more verssitle
     */
    turnHallsToFloors(); // this will change the halls to the floors
    turnCornersToWalls(); // turns corners to walls
    makeEndRoom(); // makes an end room
    turnDoorToFloor(); // makes door to floor
    //makeInteriorWalls();

    System.out.println("help1");
    for (int y = 0; y < Y_SIZE; y++)
    {
      for (int x = 0; x < X_SIZE; x++)
      {
        System.out.print(getBlock(x, y).type);
      }
      System.out.println("");
    }
  }

  private static void turnDoorToFloor()
  {
    for (int y = 1; y < Y_SIZE - 1; y++)
    {
      for (int x = 1; x < X_SIZE - 1; x++)
      {
        if (isDoor(x, y))
        {
          setBlockType(x, y, BASIC_TILE); // goes through, checks to see if door
          // turns it to hall
        }
      }
    }
  }


  /**
   *
   */
  private static void makeEndRoom()
  {
    roomSize = 4;

    for (int x = random.nextInt(X_SIZE - 6) + 3; x < X_SIZE - 3; x++)
    {
      for (int y = random.nextInt(Y_SIZE - 6) + 3; y < Y_SIZE - 3; y++)
      {
        //goes through map to see if it can be a valid location
        if (validEndLocationHorozantal(x, y))
        {
          placeEndPeicesHorzantal(x, y);
          return;
        }
        else if (validEndLocationVerticle(x, y))
        {
          placeEndPeicesVerticle(x, y);
          return;
        }
        //if it is able to run through location completly reset varibles and
        //try again
        if (x == X_SIZE - 2 && y == Y_SIZE - 2)
        {
          x = random.nextInt(X_SIZE - 2) + 1;
          y = random.nextInt(Y_SIZE - 2) + 1;
        }
      }
    }
  }

  /**
   * if it is able to place place the end peices verticly
   * it will make a 2x2 end room
   *
   * @param x
   * @param y
   */
  private static void placeEndPeicesVerticle(int x, int y)
  {
    if (isEmpty(x + 1, y + 1))
    {
      setBlockType(x + 1, y + 1, END_ROOM);
      setBlockType(x + 2, y + 1, END_ROOM);
      setBlockType(x + 1, y + 2, END_ROOM);
      setBlockType(x + 2, y + 2, END_ROOM);
      setBlockType(x, y + 1, BASIC_TILE);
      setBlockType(x, y + 2, BASIC_TILE);
      setBlockType(x + 1, y, ROOM_WALL);
      setBlockType(x + 2, y, ROOM_WALL);
      setBlockType(x + 3, y, ROOM_WALL);
      setBlockType(x + 3, y + 1, ROOM_WALL);
      setBlockType(x + 3, y + 2, ROOM_WALL);
      setBlockType(x + 3, y + 3, ROOM_WALL);
      setBlockType(x + 2, y + 3, ROOM_WALL);
      setBlockType(x + 1, y + 3, ROOM_WALL);

    }
    else if (isEmpty(x - 1, y + 1))
    {
      setBlockType(x - 1, y + 1, END_ROOM);
      setBlockType(x - 2, y + 1, END_ROOM);
      setBlockType(x - 1, y + 2, END_ROOM);
      setBlockType(x - 2, y + 2, END_ROOM);
      setBlockType(x, y + 1, BASIC_TILE);
      setBlockType(x, y + 2, BASIC_TILE);
      setBlockType(x - 1, y, ROOM_WALL);
      setBlockType(x - 2, y, ROOM_WALL);
      setBlockType(x - 3, y, ROOM_WALL);
      setBlockType(x - 3, y + 1, ROOM_WALL);
      setBlockType(x - 3, y + 2, ROOM_WALL);
      setBlockType(x - 3, y + 3, ROOM_WALL);
      setBlockType(x - 2, y + 3, ROOM_WALL);
      setBlockType(x - 1, y + 3, ROOM_WALL);
    }

  }


  /**
   * if it is able to place place the end peices horozantily
   * it will make a 2x2 end room
   * I wanted to do this another way, but honestly coding it in this way was
   * the easiest
   * thing i could think of
   *
   * @param x
   * @param y
   */
  private static void placeEndPeicesHorzantal(int x, int y)
  {
    if (isEmpty(x + 1, y + 1))
    {
      setBlockType(x + 1, y + 1, END_ROOM);
      setBlockType(x + 1, y + 2, END_ROOM);
      setBlockType(x + 2, y + 1, END_ROOM);
      setBlockType(x + 2, y + 2, END_ROOM);
      setBlockType(x + 1, y, BASIC_TILE);
      setBlockType(x + 2, y, BASIC_TILE);
      setBlockType(x, y + 1, ROOM_WALL);
      setBlockType(x, y + 2, ROOM_WALL);
      setBlockType(x, y + 3, ROOM_WALL);
      setBlockType(x + 1, y + 3, ROOM_WALL);
      setBlockType(x + 2, y + 3, ROOM_WALL);
      setBlockType(x + 3, y + 3, ROOM_WALL);
      setBlockType(x + 3, y + 2, ROOM_WALL);
      setBlockType(x + 3, y + 1, ROOM_WALL);


    }
    else if (isEmpty(x + 1, y - 1))
    {
      setBlockType(x + 1, y - 1, END_ROOM);
      setBlockType(x + 1, y - 2, END_ROOM);
      setBlockType(x + 2, y - 1, END_ROOM);
      setBlockType(x + 2, y - 2, END_ROOM);
      setBlockType(x + 1, y, BASIC_TILE);
      setBlockType(x + 2, y, BASIC_TILE);
      setBlockType(x, y - 1, ROOM_WALL);
      setBlockType(x, y - 2, ROOM_WALL);
      setBlockType(x, y - 3, ROOM_WALL);
      setBlockType(x + 1, y - 3, ROOM_WALL);
      setBlockType(x + 2, y - 3, ROOM_WALL);
      setBlockType(x + 3, y - 3, ROOM_WALL);
      setBlockType(x + 3, y - 2, ROOM_WALL);
      setBlockType(x + 3, y - 1, ROOM_WALL);

    }
  }

  /**
   * if 4 wall peices in a row horozantally return true
   *
   * @param x
   * @param y
   * @return
   */
  private static boolean validEndLocationHorozantal(int x, int y)
  {
    return ((isWall(x, y) && isWall(x + 1, y) && isWall(x + 2, y) &&
        isWall(x + 3, y)) && inBoundsWithBorder(x + 3, y) &&
        (isEmpty(x + 1, y + 1) || isEmpty(x + 1, y - 1)));
  }


  /**
   * if 4 wall peices in a row horozantally return true
   *
   * @param x
   * @param y
   * @return
   */
  private static boolean validEndLocationVerticle(int x, int y)
  {
    return (isWall(x, y) && isWall(x, y + 1) && isWall(x, y + 2) &&
        isWall(x, y + 3) && inBoundsWithBorder(x, y + 3) &&
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

    for (int y = Y_SIZE - 1; y > 0; y--)
    {
      for (int x = X_SIZE - 1; x > 0; x--)
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

  /**
   * makes inital halls that will help determine how rooms will be placed
   * halls will span entire grid
   */
  private static void makeInitalHalls()
  {
    int randomX = random.nextInt(X_SIZE - 2) + 1;
    int randomY = random.nextInt(Y_SIZE - 1) + 1;

    for (int x = 1; x < X_SIZE - 1; x++)
    {
      setBlockType(x, randomY, HALL);
    }

    for (int y = 1; y < Y_SIZE - 1; y++)
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
          System.out.println(getBlock(x, y).corner);
          if (surroundingSpotEmpty(x, y))
          {

          }
          else
          {
            setBlockType(x, y, INSIDE_WALL);
          }
        }
        if (isInsideWall(x, y) && cornerOfRoom(x, y))
        {
          setBlockType(x, y, ROOM_WALL);
        }
      }
    }
  }

  /**
   * checks to see if it is the corner peice on a room
   *
   * @param x
   * @param y
   * @return
   */
  private static boolean cornerOfRoom(int x, int y)
  {
    return (isWall(x + 1, y) && isWall(x, y + 1)) ||
        (isWall(x - 1, y) && isWall(x, y - 1)) ||
        (isWall(x + 1, y) && isWall(x, y - 1)) ||
        (isWall(x - 1, y) && isWall(x, y + 1));
  }

  private static boolean isInsideWall(int x, int y)
  {
    return getBlock(x, y).type == INSIDE_WALL;
  }

  /**
   * checks to see if spots
   *
   * @param x
   * @param y
   * @return
   */
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

  private static void turnHallsToFloors()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (isHall(x, y))
        {
          setBlockType(x, y, BASIC_TILE);
        }
      }
    }
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
     * resets walls to make rooms look like a good ball
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

  /**
   * chance to build a room wall
   *
   * @return
   */
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

  /**
   * work on later
   *
   * @param type
   */
  private static void searchAlgorithm(char type)
  {
    boolean didAlg = false;
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getBlock(x, y).type == ROOM_WALL &&
            getBlock(x, y + 1).type == END_ROOM)
        {
          algorithm(x, y, END_ROOM);
          didAlg = true;
        }

        if (didAlg)
        {
          break;
        }
      }
      if (didAlg)
      {
        break;
      }
    }
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

  /**
   * makes the walls in put make wall method
   *
   * @param x
   * @param y
   */
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

  /**
   * unworking algorithm
   *
   * @param x
   * @param y
   * @param type
   */
  private static void algorithm(int x, int y, char type)
  {
    getBlock(x, y).visited = true;
    int[] pickRandomDirection = {UP, DOWN, LEFT, RIGHT};
    shuffleArray(pickRandomDirection);
    System.out.println(x);
    System.out.println(y);
    for (int i = 0; i < 4; i++)
    {
      if (pickRandomDirection[i] == RIGHT)
      {
        if (emptyBlock(x + 1, y)/*inbounds and empty type*/)
        {
          setVisitedTrue(x, y);
          System.out.println("Right");
          algorithm(x + 1, y, END_ROOM);
        }
        else if (getBlock(x + 1, y).partOfRoom == true)
        {
          getBlock(x, y).hall = true;
          setBlockType(x, y, HALL);
          return;
        }
      }
      if (pickRandomDirection[i] == UP)
      {
        if (emptyBlock(x, y - 1))
        {
          setVisitedTrue(x, y);
          System.out.println("UP");
          algorithm(x, y - 1, END_ROOM);
        }
        else if (getBlock(x, y - 1).partOfRoom == true)
        {
          getBlock(x, y).hall = true;
          setBlockType(x, y, HALL);
          return;
        }
      }
      if (pickRandomDirection[i] == LEFT)
      {
        if (emptyBlock(x - 1, y))
        {
          setVisitedTrue(x, y);
          System.out.println("Left");
          algorithm(x - 1, y, END_ROOM);
        }
        else if (getBlock(x - 1, y).partOfRoom == true)
        {
          getBlock(x, y).hall = true;
          setBlockType(x, y, HALL);
          return;
        }
      }
      if (pickRandomDirection[i] == RIGHT)
      {
        if (emptyBlock(x, y + 1))
        {
          setVisitedTrue(x, y);
          System.out.println("down");
          algorithm(x, y + 1, END_ROOM);
        }
        else if (getBlock(x + 1, y).partOfRoom == true)
        {
          getBlock(x, y).hall = true;
          setBlockType(x, y, HALL);
          return;
        }
      }
      System.out.println();
    }
    if (getBlock(x, y).visited == true)
    {
      setBlockType(x, y, HALL);
      getBlock(x, y).hall = true;
      return;
    }

  }

  private static void setVisitedTrue(int x, int y)
  {
    getBlock(x, y).visited = true;
  }

  private static boolean emptyBlock(int x, int y)
  {
    return getBlock(x, y).type == EMPTY && inBoundsWithBorder(x, y);
  }


  private static void shuffleArray(int[] pickRandomDirection)
  {
    for (int i = 0; i < pickRandomDirection.length; i++)
    {
      int randVar = random.nextInt(3);
      int temp = pickRandomDirection[i];
      pickRandomDirection[i] = pickRandomDirection[randVar];
      pickRandomDirection[randVar] = temp;
    }
  }

  private static void spawnZombie()
  {
    int randomNumber;
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getBlock(x, y).type == BASIC_TILE)
        {
          randomNumber = random.nextInt(99);
          if (randomNumber == 0)
          {
            getBlock(x, y).zombieSpawn = true;
          }
        }
      }
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
          getBlock(a, hallY).hall = true;
        }
        else
        {
          System.out.println("making hall up, ended at a=" + a);
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
          getBlock(a, hallY).hall = true;
        }
        else
        {
          System.out.println("making hall down, ended at a=" + a);
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
          System.out.println("making hall right, ended at a=" + a);
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
          System.out.println("making hall left, ended at a=" + a);
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
    //   numberOfConnections =2;
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
        setBlockType(xCord, yCord, ROOM_WALL);
        validSpot = true;
      }
      else
      {
        xCord = random.nextInt(X_SIZE - 2) + 1;
        yCord = random.nextInt(Y_SIZE - 2) + 1;
      }
    }
    System.out.println("Obsticle");
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


  private static void buildRoom(char type, boolean firstRoom)
  {
    //set random room dimetions
    resetRoomDimentions();
    //if start start ot end room rooms size'll be 4
    if (type == END_ROOM || type == START_ROOM)
    {
      roomSize = 3;
    }
    //this will keep reseting the dimetions till it is valid
    alreadyBuilt(type);

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
          getBlock(x, y).corner = true;
          getBlock(x, y).partOfRoom = true;
        }
        //checks to see if Wall and sets wall
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setBlockType(x, y, ROOM_WALL);
          getBlock(x, y).wall = true;
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
          getBlock(x, y).partOfEndRoom = true;
          getBlock(x, y).partOfRoom = true;
        }
        else
        {
          setBlockType(x, y, START_ROOM);
          getBlock(x, y).partOfStartRoom = true;
          getBlock(x, y).partOfRoom = true;
        }
      }
    }
    System.out.println("finish room");
  }

  /**
   * chooses start and end points and room size for wall
   *
   * @param type
   */
  private static void alreadyBuilt(char type)
  {

    boolean hallTouched = false;
    for (int x = buildRoomX; x < buildRoomX + roomSize; x++)
    {
      for (int y = buildRoomY; y < buildRoomY + roomSize; y++)
      {
        if (isHall(x, y) && !wallTile(x, y, buildRoomX, buildRoomY,
            (buildRoomX + roomSize - 1),
            (buildRoomY + roomSize - 1)))
        {
          hallTouched = true;
        }
        if (placeRoom(x, y))
        {
          hallTouched = false;
          resetRoomDimentions();
          if (type == START_ROOM || type == END_ROOM)
          {
            roomSize = 4;
          }
          x = buildRoomX;
          y = buildRoomY;
        }
      }
    }
    if (!hallTouched)
    {
      resetRoomDimentions();
      if (type == START_ROOM || type == END_ROOM)
      {
        roomSize = 4;
      }
      alreadyBuilt(type);
    }
  }

  private static boolean placeRoom(int x, int y)
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

  /**
   * makes sure rooms are not being built on top of of oneAnother and on all
   *
   * @param x
   * @param y
   * @return
   */
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
        //g.setColor(grid[row][col].tile_type.color);
        //g.fillRect(col * tile_size, row * tile_size, tile_size, tile_size);
        g.drawImage(grid[row][col].tile_type.image, col * GUI.tile_size,
            row * GUI.tile_size, null);
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


//    GameMapWithBlocks gameMap = new GameMapWithBlocks();
//    GameMap procedureGenerateMap = new GameMap(gameMap.generateMap());
//    File map_file = null;
//    try
//    {
//      map_file =
//          new File(GameMap.class.getResource("resources/level1.map").toURI());
//    }
//    catch (URISyntaxException e)
//    {
//      e.printStackTrace();
//    }
//    System.out.println("file: " + map_file);
//    GameMap map = new GameMap(map_file);
//    System.out.println("map: " + map.toString());
//    System.out.println("zombies: " + map.zombies.size());
//     System.out.println("map walls: ");
//     for (Tile tile : map.getWalls())
//     {
//     System.out.println(tile);
//     }

//    generateMap();
//


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
            if (new_tile.tile_type == TileType.BRICK) {
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
                  zombie = new LineWalkZombie(GUI.zspeed, GUI.zsmell, GUI
 .drate,
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

  public static void main(String[] args)
  {
    generateMap();
  }
}
