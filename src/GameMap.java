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

  // as the levels progress we may want to make them bigger and add more rooms
  // so these final varibles may not always be final
  private static final char END_ROOM = 'E';
  private static final char ROOM_WALL = 'W';
  private static final char ROOM_CORNER = 'C';
  private static final char BASIC_TILE = 'F';
  private static final char START_ROOM = 'S';
  private static final char HALL = 'H';
  private static final char EMPTY = '.';
  private static final char OBSTICLE = 'O';
  private static final char ZOMBIE_SPAWN = 'Z';


  private static final int X_SIZE = 100;
  private static final int Y_SIZE = 100;
  private static final int MAX_ROOM_SIZE = 12;
  private static final int MIN_ROOM_SIZE = 7;
  private static final int END_ROOM_SIZE = 4;
  private static final int UP = 0;
  private static final int DOWN = 1;
  private static final int LEFT = 2;
  private static final int RIGHT = 3;
  private static int roomSize;
  private static int numberOfRandomHalls = 7;
  private static int numberOfRooms = 7;
  private static int numberOfObsicles = 3;
  // we are going to need to find a way to change this number when
  // starting a new level and such
  private static int buildRoomX;
  private static int buildRoomY;
  private static boolean hallRight = false;
  private static boolean hallLeft = false;
  private static boolean hallUp = false;
  private static boolean hallDown = false;
  private static Random random = new Random();
//  private static boolean[][] visitedGrid = new boolean[Y_SIZE][X_SIZE];
  private static char[][] intGrid = new char[Y_SIZE][X_SIZE];
  private static Block[][] blockGrid= new Block[Y_SIZE][X_SIZE];
  ArrayList<Zombie> zombies = new ArrayList<>();
  ArrayList<FireTrap> traps = new ArrayList<>();
  private int num_rows;
  private int num_cols;
  private Tile[][] grid;

  private ArrayList<Tile> walls = new ArrayList<>();

  public GameMap(GameMapWithBlocks mapWithBlocks)
    {
      for (Tile[] row : mapWithBlocks.tileGrid)
      {
        for (Tile col : row)
        {

        }
      }

    }

  public GameMap(File file)
  {
    createFromFile(file);
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

  private static void setGrid(int x, int y, char val)
  {
    intGrid[y][x] = val;
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
    return (x == xSmallBoundry ||
        x == (xLargeBoundry) || y == ySmallBoundry ||
        y == yLargeBoundry);
  }

  //checks to see if it is a corner tile.
  private static boolean cornerTile(int x, int y, int xSmallBoundry,
                                    int ySmallBoundry, int xLargeBoundry,
                                    int yLargeBoundry)
  {
    return (x == xSmallBoundry && y == ySmallBoundry) ||
        (x == xLargeBoundry && y == ySmallBoundry) ||
        (x == xSmallBoundry && y == yLargeBoundry) ||
        (x == xLargeBoundry && y == yLargeBoundry);
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
          setGrid(x, y, ROOM_CORNER);
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + END_ROOM_SIZE - 1),
                (buildRoomY + END_ROOM_SIZE - 1)))
        {
          setGrid(x, y, ROOM_WALL);
        }
        else if (inBoundsWithBorder(x, y))
        {
          setGrid(x, y, END_ROOM);
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
          setGrid(x, y, ROOM_CORNER);
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setGrid(x, y, ROOM_WALL);
        }
        else if (inBoundsWithBorder(x, y))
        {
          setGrid(x, y, START_ROOM);
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
        if (inBoundsWithBorder(x, y) &&
            cornerTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setGrid(x, y, ROOM_CORNER);
        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setGrid(x, y, ROOM_WALL);
        }
        else if (inBoundsWithBorder(x, y))
        {
          setGrid(x, y, BASIC_TILE);
        }
      }
    }
    System.out.println("finish room");
  }

  private static boolean touchingAnotherRoom(int x, int y)
  {
    return getGrid(x, y) != EMPTY;
  }

  private static boolean inBoundsWithBorder(int x, int y)
  {
    return (x < X_SIZE - 1 && x > 1 && y < Y_SIZE - 1 && y > 1);
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
        if (getGrid(x, y) == ROOM_WALL)
        {
          if (getGrid(x, y + 1) == ROOM_WALL &&
              getGrid(x, y - 1) == ROOM_WALL &&
              getGrid(x + 1, y) == ROOM_WALL)
          {
            setGrid(x, y, BASIC_TILE);
            setGrid(x + 1, y, BASIC_TILE);
          }
          else if (getGrid(x, y + 1) == ROOM_WALL &&
              getGrid(x, y - 1) == ROOM_WALL &&
              getGrid(x - 1, y) == ROOM_WALL)
          {
            setGrid(x, y, BASIC_TILE);
            setGrid(x - 1, y, BASIC_TILE);
          }
          else if (getGrid(x + 1, y) == ROOM_WALL &&
              getGrid(x - 1, y) == ROOM_WALL &&
              getGrid(x, y + 1) == ROOM_WALL)
          {
            setGrid(x, y, BASIC_TILE);
            setGrid(x, y + 1, BASIC_TILE);
          }
          else if (getGrid(x + 1, y) == ROOM_WALL &&
              getGrid(x - 1, y) == ROOM_WALL &&
              getGrid(x, y - 1) == ROOM_WALL)
          {
            setGrid(x, y, BASIC_TILE);
            setGrid(x, y - 1, BASIC_TILE);
          }
        }
      }
    }
    System.out.println("walls");
  }

  private static void generateMap()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        setGrid(x, y, EMPTY);
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


    makeDoors();


//    expandHalls();

    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        System.out.print(getGrid(x, y));
      }
      System.out.println("");
    }
//    makePathFromEnd();

    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        System.out.print(getGrid(x, y));
      }
      System.out.println("");
    }
  }

  private static void makePathFromEnd()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getGrid(x, y) == END_ROOM)
        {

          System.out.println("starting out output " + x + " " + y);
//          startEndAlgorithm(x, y);

        }
      }
    }
  }

  private static void startEndAlgorithm(int x, int y)
  {
    //   System.out.println(getGrid(x, y - 1));
    if (getGrid(x - 1, y) == ROOM_WALL)
    {
      endAlgorithm(x - 1, y, UP);
    }

    else if (getGrid(x + 1, y) == ROOM_WALL)
    {
      endAlgorithm(x + 1, y, DOWN);
    }

    else if (getGrid(x, y + 1) == ROOM_WALL)
    {
      endAlgorithm(x, y + 1, RIGHT);
    }

    else if (getGrid(x, y - 1) == ROOM_WALL)
    {
      endAlgorithm(x, y - 1, LEFT);
    }


  }

  private static void endAlgorithm(int x, int y, int prevDirection)
  {
    System.out.println(x);
    System.out.println(y);
    System.out.println();
    int[] pickRandomDirection = {0, 1, 2, 3};
    shuffleArray(pickRandomDirection);
    boolean visited = false;
    for (int i = 0; i < 4; i++)
    {
      if (pickRandomDirection[i] == UP && getGrid(x - 1, y) == EMPTY)
      {
        visited = true;
        endAlgorithm(x - 1, y, UP);
      }
      if (pickRandomDirection[i] == DOWN && getGrid(x + 1, y) == EMPTY)
      {
        visited = true;
        endAlgorithm(x + 1, y, DOWN);
      }
      if (pickRandomDirection[i] == LEFT && getGrid(x, y - 1) == EMPTY)
      {
        visited = true;
        endAlgorithm(x, y - 1, LEFT);
      }
      if (pickRandomDirection[i] == RIGHT && getGrid(x, y + 1) == EMPTY)
      {
        visited = true;
        endAlgorithm(x, y + 1, RIGHT);
      }
    }
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


  // addWallsToHalls();

  //   spawnZombie();

  // print out that maze

  private static void endAlgorithm1(int x, int y, int pickDirection)
  {
//    boolean valid = false;
//
//    if (pickDirection == 0 && canMoveUp(x, y))
//    {
//      if (getGrid(x - 1, y) == EMPTY && inBoundsWithBorder(x, y))
//      {
//        System.out.println("hi 0");
//        System.out.println(getGrid(x - 1, y));
//        getGrid(x - 1, y);
//        valid = true;
//        endAlgorithm(x - 1, y, UP);
//      }
//
//      if (valid)
//      {
//        setGrid(x - 1, y, HALL);
//        return;
//      }
//      pickDirection = 1;
//
///*
//      if (pickDirection == 1)
//      {
//        if (getGrid(x + 1, y) == EMPTY && inBoundsWithBorder(x+1,y))
//        {
//          System.out.println("hi 1");
//          System.out.println(getGrid(x + 1, y));
//          getGrid(x + 1, y);
//          valid = true;
//          endAlgorithm(x + 1, y);
//        }
//        if (valid)
//        {
//          setGrid(x + 1, y, HALL);
//          return;
//        }
//      }
//      pickDirection = 2;
//      if (pickDirection == 2)
//      {
//
//        if (getGrid(x, y - 1) == EMPTY && inBoundsWithBorder(x,y-1))
//        {
//          System.out.println("hi 2");
//          System.out.println(getGrid(x, y - 1));
//          getGrid(x, y - 1);
//          valid = true;
//          endAlgorithm(x, y - 1);
//        }
//        if (valid)
//        {
//          setGrid(x, y - 1, HALL);
//          return;
//        }
//      }
//      pickDirection = 3;
//      if (pickDirection == 3)
//      {
//
//        if (getGrid(x, y + 1) == EMPTY && inBoundsWithBorder(x,y +1))
//        {
//          System.out.println("hi 3");
//          System.out.println(getGrid(x, y + 1));
//          getGrid(x, y + 1);
//          valid = true;
//          endAlgorithm(x, y + 1);
//        }
//        if (valid)
//        {
//          setGrid(x, y + 1, HALL);
//          return;
//        }
//      }
//      */
//      return;
//    }
//    else if (pickDirection == 1)
//    {
//
//    }
//    else if (pickDirection == 2 && touchingAnotherRoom(x, y))
//    {
//
//    }
//    else if (pickDirection == 3 && touchingAnotherRoom(x, y))
//    {
//
//    }
//  }

//  private static boolean canMoveUp(int x, int y)
//  {
//    for (int a = x; a > 0; a--)
//    {
//      if(getGrid(a,y)!=EMPTY){
//        return true;
//      }
//    }
//    return false;
//  }
//
//  private static boolean canMoveDown(int x, int y)
//  {
//    for (int a = x; a < X_SIZE; a++)
//    {
//      if(getGrid(a,y)!=EMPTY){
//        return true;
//      }
//    }
//    return false;
//  }
  }

  private static void makeDoors()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (getGrid(x, y) == HALL)
        {
          if (getGrid(x + 1, y) == ROOM_WALL)
          {
            setGrid(x + 1, y, BASIC_TILE);
          }
          if (getGrid(x - 1, y) == ROOM_WALL)
          {
            setGrid(x - 1, y, BASIC_TILE);
          }
          if (getGrid(x, y + 1) == ROOM_WALL)
          {
            setGrid(x, y + 1, BASIC_TILE);
          }
          if (getGrid(x, y - 1) == ROOM_WALL)
          {
            setGrid(x, y - 1, BASIC_TILE);
          }
        }
      }
    }
  }

  private static void addWallsToHalls()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (getGrid(x, y) == HALL)
        {
          putUpTheWalls(x, y);
        }
      }
    }
  }

  private static void putUpTheWalls(int x, int y)
  {
    if (getGrid(x + 1, y) == EMPTY)
    {
      setGrid(x + 1, y, ROOM_WALL);
    }
    if (getGrid(x - 1, y) == EMPTY)
    {
      setGrid(x - 1, y, ROOM_WALL);
    }
    if (getGrid(x, y + 1) == EMPTY)
    {
      setGrid(x, y + 1, ROOM_WALL);
    }
    if (getGrid(x, y - 1) == EMPTY)
    {
      setGrid(x, y - 1, ROOM_WALL);
    }

  }


  //making generic algorithm to push out straight hallways
  //need previous directions

  private static void expandHalls()
  {
    for (int x = 1; x < X_SIZE - 1; x++)
    {
      for (int y = 1; y < Y_SIZE - 1; y++)
      {
        if (getGrid(x, y) == HALL)
        {
          if (getGrid(x + 1, y) == HALL && getGrid(x - 1, y) == HALL)
          {
            setGrid(x, y - 1, HALL);
            setGrid(x + 1, y - 1, HALL);
            setGrid(x - 1, y - 1, HALL);

          }

          if (getGrid(x, y + 1) == HALL && getGrid(x, y - 1) == HALL)
          {
            setGrid(x - 1, y, HALL);
            setGrid(x - 1, y + 1, HALL);
            setGrid(x - 1, y - 1, HALL);
          }
        }
      }
    }
  }

  private static void spawnZombie()
  {
    int randomNumber;
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getGrid(x, y) == BASIC_TILE)
        {
          randomNumber = random.nextInt(99);
          if (randomNumber == 0)
          {
            setGrid(x, y, ZOMBIE_SPAWN);
          }
        }
      }
    }
  }

  private static char getGrid(int x, int y)
  {
    return intGrid[y][x];
  }

  private static void makeRandomHalls()
  {
    boolean valid = false;
    int hallX = random.nextInt(X_SIZE - 10) + 5;
    int hallY = random.nextInt(Y_SIZE - 10) + 5;
    while (!valid)
    {
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

    //hallRight=true;
    System.out.println("x cord: " + hallY + " y cord: " + hallX);
    System.out.println("Grid[y][x] = " + getGrid(hallX, hallY));
    System.out.println("hall r: " + hallRight);
    System.out.println("hall l: " + hallLeft);
    System.out.println("hall d: " + hallDown);
    System.out.println("hall u: " + hallUp);
    System.out.println();


    if (hallUp)
    {
      for (int a = hallX; a > 0; a--)
      {
        if (canMakeHall(a, hallY))
        {
          setGrid(a, hallY, HALL);
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
          setGrid(a, hallY, HALL);
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
          setGrid(hallX, a, HALL);
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
          setGrid(hallX, a, HALL);
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

  private static boolean canMakeHall(int x, int y)
  {
    return (getGrid(x, y) == EMPTY || getGrid(x, y) == HALL);
  }

  private static boolean canMakeHallForAlg(int x, int y)
  {
    return (getGrid(x, y) == EMPTY);
  }

  private static boolean validHallLocationForHalls(int x, int y)
  {

    int connectRooms = 0;
    int numberOfConnections = random.nextInt(3) + 1;
    for (int a = x; a < X_SIZE; a++)
    {
      if (canMakeHall(a, y))
      {

      }
      else if (getGrid(a, y) == ROOM_WALL)
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
      else if (getGrid(a, y) == ROOM_WALL)
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
      else if (getGrid(x, a) == ROOM_WALL)
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
      else if (getGrid(x, a) == ROOM_WALL)
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
    if (connectRooms > 2)
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

  private static boolean cycleSpotsForObsticles(int s, int t)
  {
    boolean valid = true;
    for (int x = s - 1; x <= s + 1; x++)
    {
      for (int y = t - 1; y <= t + 1; y++)
      {
        if (getGrid(x, y) != BASIC_TILE)
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
        setGrid(xCord, yCord, OBSTICLE);
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
        if (SHOW_COORDS) {
          g.setColor(Color.WHITE);
          g.drawString(String.format("(%d, %d)", row, col).toString(), col * tile_size + (tile_size / 4), row * tile_size + (tile_size / 2));
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
            if (new_tile.tile_type == TileType.BRICK) {
              if (rand.nextDouble() < GUI.zspawn)
              {
                Zombie zombie;
                Location location = new Location(col * GUI.tile_size, row * GUI.tile_size);
                if (rand.nextBoolean())
                {
                  zombie = new RandomWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate, location);
                } else {
                  zombie = new LineWalkZombie(GUI.zspeed, GUI.zsmell, GUI.drate, location);
                }
                zombies.add(zombie);
              }

              if (rand.nextDouble() < GUI.fspawn)
              {
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
