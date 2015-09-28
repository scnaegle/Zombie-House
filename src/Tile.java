/**
 * 
 */


import java.awt.*;

/**
 * @author Sean Naegle
 *
 * Tile contains all the information needed for it's box in the grid. This 
 * implements Comparable so that it can be used to 
 */
public class Tile extends GameObject implements Comparable<Tile> {
	static final int WIDTH = 16;
	static final int HEIGHT = 16;
	
	protected int row;
	protected int col;
  protected TileType tile_type;
	int x;
	int y;

  /*
   * Tile constructor
   * @param row
   * @param col
   */
  //row is y
  //col is x
  public Tile(int row, int col)
  {
    this.row = row;
    this.col = col;
    this.location = new Location(col * GUI.tile_size, row * GUI.tile_size, row, col);
    this.width = GUI.tile_size;
    this.height = GUI.tile_size;
  }

	/*
   * Tile constructor
	 * @param row
	 * @param col
	 * @param tile_type TileType object
	 */
  public Tile(int row, int col, TileType tile_type)
  {
    this(row, col);
    this.tile_type = tile_type;
  }
	
	/*
   * Tile constructor from blocks
	 * @param row
	 * @param col
	 * @param tile_val Char that corresponds to a TileType
	 */
  public Tile(int row, int col, char tile_val)
  {
    this(row, col);
    this.tile_type = TileType.fromGridChar(tile_val);
  }
	
	/*
	 * Tile constructor. Creates a new Tile based off the passed in tile.
	 * @param tile Tile object
	 */
	public Tile(Tile tile) {
		this(tile.row, tile.col, tile.tile_type);
  }

  public static void main(String[] args)
  {
//    Tile grass = new Tile(0,0, TileType.GRASS);
//    Tile forest = new Tile(0, 0, TileType.FOREST);
//    Tile hill = new Tile(0, 0, TileType.HILL);
//    Tile water = new Tile(0, 0, TileType.WATER);
//    Tile wall = new Tile(0, 0, TileType.WALL);

//    System.out.println("grass value is " + grass.tile_type.value
//    									 + " and the movement cost is " + grass.tile_type
// .movement_cost);
//    System.out.println("forest value is " + forest.tile_type.value
//    									 + " and the movement cost is " + forest.tile_type
// .movement_cost);
//    System.out.println("hill value is " + hill.tile_type.value
//    									 + " and the movement cost is " + hill.tile_type
// .movement_cost);
//    System.out.println("water value is " + water.tile_type.value
//    									 + " and the movement cost is " + water.tile_type
// .movement_cost);
//    System.out.println("wall value is " + wall.tile_type.value
//    									 + " and the movement cost is " + wall.tile_type
// .movement_cost);

	}

//  public BufferedImage loadTile(String file)
//  {
//
//    BufferedImage tile = null;
//
//    try
//    {
//      tile = ImageIO
//          .read(Sprite.class.getResource("resources/" + file + ".jpg"));
//    }
//    catch (IOException e)
//    {
//      e.printStackTrace();
//    }
//
//    return tile;
//  }

  /**
   * get X coordinate
   * @return int - X coordinate
   */
  public int getRow()
  {
    return row;
  }

  /**
   * get Y coordinate
   *
   * @return int - Y coordinate
   */
  public int getCol()
  {
    return col;
  }

  /**
   * Paint the tile on the panel
   * @param g Graphics object
   */
  public void paint(Graphics g) {
    // g.setColor(tile_type.);
    g.fillRect(row, col, WIDTH, HEIGHT);
    g.setColor(Color.WHITE);
    g.drawRect(row, col, WIDTH, HEIGHT);
  }

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public Rectangle getBoundingRectangle() {
		return new Rectangle(col * GUI.tile_size, row * GUI.tile_size, GUI.tile_size, GUI.tile_size);
	}

	@Override
	public boolean intersects(Object2D other) {
		return false;
	}
  
	/* (non-Javadoc)
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
	public int compareTo(Tile other) {
		return tile_type.movement_cost.compareTo(other.tile_type.movement_cost);
	}

  @Override
  public String toString() {
    return "Tile{" +
        "row=" + row +
        ", col=" + col +
        ", width=" + width +
        ", height=" + height +
        ", Location=" + location +
        '}';
  }

  /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tile_type == null) ? 0 : tile_type.hashCode());
		result = prime * result + row;
		result = prime * result + col;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tile other = (Tile) obj;
		if (tile_type != other.tile_type)
			return false;
		if (row != other.row)
			return false;
		return col == other.col;
	}

}
