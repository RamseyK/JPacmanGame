import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Item objects are GameObject's that can be manipulated by the Player on the map (teleports, dots, powerups, fruit)
 * Item is a subclass of GameObject
 * 
 * @author Ramsey Kant
 */
public class Item extends GameObject {
	// Teleportation vars
	private int teleportDestX;
	private int teleportDestY;
	
	/**
	 * Class constructor for Item
	 * 
	 * @param type Object type
	 * @param color Base color of the item
	 * @param m Reference to the map object
	 * @param x X coordinate the item will occupy on the map
	 * @param y Y coordinate the item with occupy on the map
	 * @see GameObject
	 */
	public Item(int type, Color color, Map m, int x, int y) {
		super(type, color,  m, x, y);
		
		teleportDestX = 13;
		teleportDestY = 17;
	}
	
	/**
	 * Set the destination coordinates for teleportation. This isn't useful to any item other than a teleport
	 * 
	 * @param x X destination coordinate
	 * @param y Y destination coordinate
	 */
	public void setTeleport(int x, int y) {
		teleportDestX = x;
		teleportDestY = y;
	}
	
	/**
	 * Retrieve the teleport destination X coordinate 
	 * 
	 * @return X destination coordinate
	 * @see Item#setTeleport(int, int)
	 */
	public int getTeleportX() {
		return teleportDestX;
	}
	
	/**
	 * Retrieve the teleport destination Y coordinate 
	 * 
	 * @return Y destination coordinate
	 * @see Item#setTeleport(int, int)
	 */
	public int getTeleportY() {
		return teleportDestY;
	}

	/**
	 * Called when the item is picked up / used by the player (in the player's act() function)
	 * Add point values or trigger powerup modifiers here (using the pl object)
	 * 
	 * @param pl Player that uses the item
	 * @return True->Destroy the item. False->Keep the item on the map
	 * @see Player#act()
	 */
	public boolean use(Player pl) {
		boolean destroy = false;
		
		// Perform action based on type
		switch(objType) {
			case OBJECT_DOT:
				pl.incrementScore(10);
				destroy = true;
				break;
			case OBJECT_POWERUP:
				pl.incrementScore(50);
				pl.setPowerUp(true);
				destroy = true;
				break;
			case OBJECT_TELEPORT:
				pl.move(teleportDestX, teleportDestY);
				break;
			default:
				break;
		}
		
		return destroy;
	}

	/**
	 * Item's have no "think" process. Blank method
	 * 
	 * @see GameObject#act()
	 */
	@Override
	public void act() {
	}

	/**
	 * Draw the item based on it's type
	 * 
	 * @see GameObject#paint(java.awt.Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
		g.setColor(objColor);
		
		// Item's are placed in the center of a cell
		int center_x = (positionX*map.CELL_SIZE)+map.CELL_SIZE/2;
		int center_y = (positionY*map.CELL_SIZE)+map.CELL_SIZE/2;
		
		// Render item based on type
		switch(objType) {
			case OBJECT_DOT:
				g.fillArc(center_x-4, center_y-4, 8, 8, 0, 360);
				break;
			case OBJECT_POWERUP:
				g.fillArc(center_x-8, center_y-8, 16, 16, 0, 360);
				break;
			case OBJECT_TELEPORT:
				g.fillOval(center_x-6, center_y-8, 12, 16);
				break;
			default:
				break;
		}
	}

}
