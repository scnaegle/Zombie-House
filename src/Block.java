/**
 * Created by Tyler on 9/14/2015.
 */
public class Block
{
  boolean zombieSpawn = false;
  boolean partOfRoom = false;
  boolean partOfStartRoom=false;
  boolean partOfEndRoom=false;
  boolean visited =false;
  boolean goal = false;
  boolean corner = false;
  boolean wall = false;
  boolean hall =false;


  char type;
  int x;
  int y;
  public Block(int x, int y, char type)
  {
    this.x = x;
    this.y = y;
    this.type = type;
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