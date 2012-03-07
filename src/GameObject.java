import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A game object is anything on the pacman grid (wall, cherry, ghost, player).
 * GameObject is the base class of almost everything within the Map
 * 
 * @author Ramsey Kant
 */
public abstract class GameObject {
	// Static type vars
	public static final int OBJECT_DOT = 1;
	public static final int OBJECT_POWERUP = 2;
	public static final int OBJECT_CHERRY = 4;
	public static final int OBJECT_PLAYER = 8;
	public static final int OBJECT_GHOST = 16;
	public static final int OBJECT_MARKER = 32; // Virtual
	public static final int OBJECT_WALL = 64; // Virtual
	public static final int OBJECT_TELEPORT = 128;
	
	// Wall types (Walls aren't instanced GameObject's)
	public static final byte WALL_VERTICAL = 1;
	public static final byte WALL_HORIZONTAL = 2;
	public static final byte WALL_TOPLEFT = 3;
	public static final byte WALL_TOPRIGHT = 4;
	public static final byte WALL_BOTTOMLEFT = 5;
	public static final byte WALL_BOTTOMRIGHT = 6;
	public static final byte WALL_GHOSTBARRIER = 7;
	
	// Generic object attributes
	protected int objType;
	protected Color objColor;
	protected int positionX;
	protected int positionY;
	
	// Outside refereneces
	protected final Map map; // Can only be set once. Object only exists within the map. If the map changes, new objects are created
	
	// Getters and Setters
	
	/**
	 * Return the type of the object set in the constructor. See static types defined in GameObject
	 * 
	 * @return type of object
	 */
	public int getType() {
		return objType;
	}
	
	/**
	 * Grab the current java.awt.Color (base color) the object is being rendered in
	 * 
	 * @return Base Color of the object
	 */
	public Color getColor() {
		return objColor;
	}
	
	/**
	 * Set the current base color used when rendering the object
	 * 
	 * @param c java.awt.Color Color of object
	 */
	public void setColor(Color c) {
		objColor = c;
	}
	
	/**
	 * Grab the current X coordinate of the object on the map. This property is frequently modified by the Map class and move() method
	 * 
	 * @see Actor#move(int, int)
	 */
	public int getX() {
		return positionX;
	}
	
	/**
	 * Grab the current Y coordinate of the object on the map. This property is frequently modified by the Map class and move() method
	 * 
	 * @see Actor#move(int, int)
	 */
	public int getY() {
		return positionY;
	}
	
	// Public & Protected Abstract methods
	
	/**
	 * Class Constructor for a game object
	 * 
	 * @param type Type of game object (see static types above)
	 * @param color Standard java Color
	 * @param m Reference to the global Map
	 * @param x Initial x coordinate
	 * @param y Initial y coordinate
	 */
	public GameObject(int type, Color color, Map m, int x, int y) {
		objType = type;
		objColor = color;
		map = m;
		positionX = x;
		positionY = y;
	}
	
	/**
	 * Perform a "Think" cycle for the Object
	 * This includes things like self maintenance and movement
	 */
	public abstract void act();

	/**
	 * Draw the object. Subclasses should define how they are to be drawn. This is called in StateGame's logic()
	 * 
	 * @param g The graphics context
	 * @see StateGame#logic()
	 */
	public abstract void paint(Graphics2D g);
}
