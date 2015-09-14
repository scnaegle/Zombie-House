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

    for (int i = 0; i < numberOfRooms; i++)
    {
      buildRoom();
    }
    breakTouchingWalls();
    buildObsticales();
    for (int y = 0; y < Y_SIZE; y++)
    {
      for (int x = 0; x < X_SIZE; x++)
      {
        System.out.print(getBlock(x, y).type);
      }
      System.out.println("");
    }
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
        else if (inBoundsWithBorder(x, y))
        {
          setBlockType(x, y, BASIC_TILE);
          getBlock(x, y).partOfRoom = true;
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
    return (x < X_SIZE && x > 0 && y < Y_SIZE && y > 0);
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
