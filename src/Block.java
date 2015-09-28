/**
 * Block class.
 * Each block represents a tile grid in the map.
 * Blocks have many boolean values that can give much information into
 * game logic.
 */
public class Block
{

  boolean partOfRoom = false;

  char type;
  int x;
  int y;

  /**
   * takes a int x, and y to let you know where the tile is in general
   * takes a type to tell you what kind of block it is
   * @param x gets x cordonate
   * @param y gets y cordonate
   * @param type gets the char or type of block
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
   * @return returns the tile
   */
  public Tile toTile()
  {

   return new Tile(y, x, type);
  }

  public char getType()
  {
    return type;
  }

}