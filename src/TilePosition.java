/**
 * Created by scnaegl on 9/8/15.
 */
public class TilePosition {
  int row;
  int col;
  int x;
  int y;

  public TilePosition() {

  }

  public TilePosition(int row, int col) {
    this.row = row;
    this.col = col;
  }

  public TilePosition(int row, int col, int x, int y) {
    this(row, col);
    this.x = x;
    this.y = y;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setRow(int row) {
    this.row = row;
  }

}
