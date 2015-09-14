import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by Tyler on 9/14/2015.
 */

public class GameMapWithBlocks
{

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


  private static final int X_SIZE = 40;
  private static final int Y_SIZE = 20;
  private static final int MAX_ROOM_SIZE = 5;
  private static final int MIN_ROOM_SIZE = 3;
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
  private static Block[][] blockGrid = new Block[Y_SIZE][X_SIZE];


  private static void generateMap()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        blockGrid[y][x] = new Block(x, y, EMPTY);
      }
    }

    buildRoom(START_ROOM);
    buildRoom(END_ROOM);
    for (int i = 0; i < numberOfRooms; i++)
    {
      buildRoom(BASIC_TILE);
    }
    breakTouchingWalls();
    buildObsticales();
    makeRandomHalls();
    searchAlgorithm(END_ROOM);
    expandHalls();
    addWallsToHalls();
    spawnZombie();

    for (int y = 0; y < Y_SIZE; y++)
    {
      for (int x = 0; x < X_SIZE; x++)
      {
        System.out.print(getBlock(x, y).type);
      }
      System.out.println("");
    }
  }

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
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getBlock(x, y).type == HALL)
        {
          putUpTheWalls(x, y);
        }
      }
    }
  }

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
        if (emptyBlock(x+1,y))
        {
          setVisitedTrue(x,y);
          System.out.println("Right");
          algorithm(x + 1, y, END_ROOM);
        }

      }
      if (pickRandomDirection[i] == UP)
      {

        if (emptyBlock(x,y-1))
        {

          setVisitedTrue(x,y);
          System.out.println("UP");
          algorithm(x, y - 1, END_ROOM);
        }
      }
      if (pickRandomDirection[i] == LEFT)
      {
        if (emptyBlock(x-1,y))
        {


          setVisitedTrue(x,y);
          System.out.println("Left");
          algorithm(x - 1, y, END_ROOM);
        }
      }
      if (pickRandomDirection[i] == RIGHT)
      {
        if (emptyBlock(x + 1, y))
        {
          setVisitedTrue(x,y);
          System.out.println("down");
          algorithm(x, y + 1, END_ROOM);
        }

      }
      System.out.println();
    }
  }

  private static void setVisitedTrue(int x, int y)
  {
    getBlock(x,y).visited =true;
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
            setBlockType(x, y, ZOMBIE_SPAWN);
          }
        }
      }
    }
  }

  private static void expandHalls()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
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

//    //hallRight=true;
//    System.out.println("x cord: " + hallY + " y cord: " + hallX);
////    System.out.println("Grid[y][x] = " + getGrid(hallX, hallY));
//    System.out.println("hall r: " + hallRight);
//    System.out.println("hall l: " + hallLeft);
//    System.out.println("hall d: " + hallDown);
//    System.out.println("hall u: " + hallUp);
//    System.out.println();
//

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
        setBlockType(xCord, yCord, OBSTICLE);
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


  private static void buildRoom(char type)
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
          setBlockType(x, y, ROOM_CORNER);
          getBlock(x, y).corner = true;

        }
        //seriously sorry about the if statments :( It is working though. :)
        else if (inBoundsWithBorder(x, y) &&
            wallTile(x, y, buildRoomX, buildRoomY,
                (buildRoomX + roomSize - 1),
                (buildRoomY + roomSize - 1)))
        {
          setBlockType(x, y, ROOM_WALL);
          getBlock(x, y).wall = true;
        }
        else if (inBoundsWithBorder(x, y) && type == BASIC_TILE)
        {
          setBlockType(x, y, BASIC_TILE);
          getBlock(x, y).partOfRoom = true;
        }
        else if (inBoundsWithBorder(x, y) && type == START_ROOM)
        {
          setBlockType(x, y, END_ROOM);
          getBlock(x, y).partOfEndRoom = true;
        }
        else
        {
          setBlockType(x, y, START_ROOM);
          getBlock(x, y).partOfStartRoom = true;
        }
      }
    }
    System.out.println("finish room");
  }

  private static void breakTouchingWalls()
  {
    for (int x = 0; x < X_SIZE; x++)
    {
      for (int y = 0; y < Y_SIZE; y++)
      {
        if (getBlock(x, y).type == ROOM_WALL)
        {
          if (getBlock(x, y + 1).type == ROOM_WALL &&
              getBlock(x, y - 1).type == ROOM_WALL &&
              getBlock(x + 1, y).type == ROOM_WALL)
          {
            setBlockType(x, y, BASIC_TILE);
            setBlockType(x + 1, y, BASIC_TILE);
          }
          else if (getBlock(x, y + 1).type == ROOM_WALL &&
              getBlock(x, y - 1).type == ROOM_WALL &&
              getBlock(x - 1, y).type == ROOM_WALL)
          {
            setBlockType(x, y, BASIC_TILE);
            setBlockType(x - 1, y, BASIC_TILE);
          }
          else if (getBlock(x + 1, y).type == ROOM_WALL &&
              getBlock(x - 1, y).type == ROOM_WALL &&
              getBlock(x, y + 1).type == ROOM_WALL)
          {
            setBlockType(x, y, BASIC_TILE);
            setBlockType(x, y + 1, BASIC_TILE);
          }
          else if (getBlock(x + 1, y).type == ROOM_WALL &&
              getBlock(x - 1, y).type == ROOM_WALL &&
              getBlock(x, y - 1).type == ROOM_WALL)
          {
            setBlockType(x, y, BASIC_TILE);
            setBlockType(x, y - 1, BASIC_TILE);
          }
        }
      }
    }
    System.out.println("walls");
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

  private static boolean cornerTile(int x, int y, int xSmallBoundry,
                                    int ySmallBoundry, int xLargeBoundry,
                                    int yLargeBoundry)
  {
    return (x == xSmallBoundry && y == ySmallBoundry) ||
        (x == xLargeBoundry && y == ySmallBoundry) ||
        (x == xSmallBoundry && y == yLargeBoundry) ||
        (x == xLargeBoundry && y == yLargeBoundry);
  }

  private static boolean inBoundsWithBorder(int x, int y)
  {
    return (x < X_SIZE - 1 && x > 0 && y < Y_SIZE - 1 && y > 0);
  }


  private static boolean touchingAnotherRoom(int x, int y)
  {
    return getBlock(x, y).getType() != EMPTY;
  }

  private static Block getBlock(int x, int y)
  {
    return blockGrid[y][x];
  }

  private static void resetRoomDimentions()
  {
    buildRoomX = random.nextInt(X_SIZE - 6) + 1;
    buildRoomY = random.nextInt(Y_SIZE - 6) + 1;
    roomSize = makeRoomSize();
  }

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

  public static void main(String[] args)
  {
    generateMap();
  }


}
