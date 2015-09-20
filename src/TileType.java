import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * 
 */

/**
 * @author sean
 *
 */


public enum TileType {


	GRASS(1, '.', 1, "grass"),
	BRICK(2, '*', 1, "floor"),
	BURNTWALL(3, '3',1, "burntFloor"), // only used for outside walls
	BURNTFLOOR(4, '4',1, "burntFloor"),
	START(5, 'S', 1, "floor"),
	EXIT(6, 'E', 9999, "exit"),
	WALL(7, 'B', 9999, "wall"),
	INSIDEWALL(8,'I',9999,"wall"),
	HALL(9,'H',9999,"floor")
	//DOOR(9,'`',1,new Color(100,100,100)
	;
		int value;
		char grid_char;
		Integer movement_cost;
	BufferedImage image;

	TileType(int value, char grid_char, int movement_cost, String image)
	{
			this.value = value;
			this.grid_char = grid_char;
			this.movement_cost = movement_cost;
		this.image = loadTile(image);


		}

	public static TileType fromGridChar(char value)
			throws IllegalArgumentException
	{
			try{
				for (TileType type : TileType.values()) {
					//System.out.println("getting tile value");
					if (type.grid_char == value) {
						return type;
					}
				}
				throw new IllegalArgumentException("Unknown enum value: " + value);
      } catch( ArrayIndexOutOfBoundsException e ) {
        throw new IllegalArgumentException("Unknown enum value: " + value);
      }
		}

	public BufferedImage loadTile(String file)
	{

		BufferedImage tile = null;

		try
		{
			tile = ImageIO
					.read(Sprite.class.getResource("resources/" + file + ".jpg"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

		return tile;
	}

}