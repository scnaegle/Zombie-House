/**
 * Created by Tyler on 9/14/2015.
 */

/**
 * block class
 * each block represents a tile grid in the map
 * blocks have many boolean values that can give much information into
 * game logic
 */
public class Block
{
  boolean zombieSpawn = false;
  boolean partOfRoom = false;
  boolean partOfStartRoom = false;
  boolean partOfEndRoom = false;
  boolean visited = false;
  boolean goal = false;
  boolean corner = false;
  boolean wall = false;
  boolean hall = false;
  boolean doorways = false;

  char type;
  int x;
  int y;

  /**
   * takes a int x, and y to let you know where the tile is in general
   * takes a type to tell you what kind of block it is
   * @param x
   * @param y
   * @param type
   */
  public Block(int x, int y, char type)
  {
    this.x = x;
    this.y = y;
    this.type = type;
  }

  /**
   * we have this to tile method because it is needed because the rest of the
   * program is using it.
   * @return
   */
  public Tile toTile()
  {

   return new Tile(y, x, type);
  }

  private int getX()
  {
    return x;
  }

  private int getY()
  {
    return y;
  }

  public char getType()
  {
    return type;
  }

  private boolean isVisited()
  {
    return visited;
  }

  private boolean isGoal()
  {
    return goal;
  }
}