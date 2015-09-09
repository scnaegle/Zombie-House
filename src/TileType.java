import java.awt.Color;


/**
 * 
 */

/**
 * @author sean
 *
 */
public enum TileType {
		GRASS(1, '.',1, new Color(145, 179, 140)),
		BRICK(2, '*',1, new Color(191, 225, 184)),
		TRAP(3, '3',1, new Color(230, 9, 13)),
		BURNT(4, '4',1, new Color(22, 22, 22)),
		START(5, 'S',1, new Color(216, 216, 25)),
		EXIT(6, 'E', 9999, new Color(0, 0, 0)),
		WALL(5, 'B', 9999, Color.BLACK);
		int value;
		char grid_char;
		Integer movement_cost;
		Color color;
		
		TileType(int value, char grid_char, int movement_cost, Color color) {
			this.value = value;
			this.grid_char = grid_char;
			this.movement_cost = movement_cost;
			this.color = color;
		}
		
		public static TileType fromGridChar(char value) throws IllegalArgumentException {
			try{
				for (TileType type : TileType.values()) {
					if (type.grid_char == value) {
						return type;
					}
				}
				throw new IllegalArgumentException("Unknown enum value: " + value);
      } catch( ArrayIndexOutOfBoundsException e ) {
        throw new IllegalArgumentException("Unknown enum value: " + value);
      }
		}
	}