import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class GameMap {

  private int num_rows;
  private int num_cols;
  private Tile[][] grid;


  public GameMap(File file) {
    createFromFile(file);
  }

  public void createFromFile(File file) {
    Scanner sc;
    ArrayList<ArrayList<Tile>> grid = new ArrayList<ArrayList<Tile>>();
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

          for(char type : row_types) {
            row_array.add(new Tile(row, col, type));
            col++;
          }
          grid.add(row_array);
        }
        row++;
      }

      this.num_rows = grid.size();
      this.num_cols = grid.get(0).size();
      this.grid = new Tile[num_rows][num_cols];
      for(int i = 0; i < grid.size(); i++) {
        Tile[] row_array = new Tile[grid.get(i).size()];
        row_array = grid.get(i).toArray(row_array);
        this.grid[i] = row_array;
      }
    } catch (IOException e) {
      System.err.println("Can't find file: " + file.getName());
      System.exit(1);
    }
  }

  @Override
  public String toString() {
    String ret = "GameMap{" +
        "num_rows=" + num_rows +
        ", num_cols=" + num_cols + "}\n";
    for(int row = 0; row < num_rows; row++) {
      for(int col = 0; col < num_cols; col++) {
        System.out.format("row=%d, col=%d\n", row, col);
        ret += grid[row][col].tile_type.grid_char;
      }
      ret += "\n";
    }
    return ret;
  }

  public static void main(String[] args) {
    File map_file = new File("resources/level1.map");
    System.out.println("file: " + map_file);
    GameMap map = new GameMap(map_file);
    System.out.println("map: " + map.toString());
  }
}
