import java.awt.Color;
import java.awt.Graphics2D;

/**
 * An actor is any object that has a degree of autonomy or intelligent input (Human / AIManager) dictating the object's behavior ingame
 * Subclass of GameObject
 * 
 * @author Ramsey Kant
 */
public class Actor extends GameObject {
	
	// Direction constants
	public static final int MOVE_NONE = 0;
	public static final int MOVE_UP = 1;
	public static final int MOVE_RIGHT = 2;
	public static final int MOVE_DOWN = 4;
	public static final int MOVE_LEFT = 8;
	
	// Dead status
	protected boolean isDead;
	
	// Movement / Location
	protected int spawnX;
	protected int spawnY;
	protected int moveDir;
	protected int dirOrient;
	protected float deltaX;
	protected float deltaY;
	protected float speed;

	/**
	 * Actor Class Constructor
	 * 
	 * @param type Object type that is an actor
	 * @param color Base color of the actor
	 * @param m Reference to the global map object
	 * @param x X coordinate to spawn the actor at
	 * @param y Y coordinate to spawn the actor at
	 * @see GameObject
	 */
	public Actor(int type, Color color, Map m, int x, int y) {
		super(type, color, m, x, y);
		
		isDead = false;
		
		// Movement
		spawnX = x;
		spawnY = y;
		moveDir = MOVE_NONE;
		dirOrient = 0;
		deltaX = 0;
		deltaY = 0;
		speed = 5;
	}
	
	// Getters and Setters
	
	/**
	 * Returns the original X coordinate the Actor was given in the constructor
	 * 
	 * @return the X coordinate of the spawn point
	 */
	public int getSpawnX() {
		return spawnX;
	}
	
	/**
	 * Returns the original Y coordinate the Actor was given in the constructor
	 * 
	 * @return the Y coordinate of the spawn point
	 */
	public int getSpawnY() {
		return spawnY;
	}
	
	/**
	 * Set the death status of the actor. Used by StateGame and AIManager to determine if the player / ghost has died
	 */
	public void setDead(boolean s) {
		isDead = s;
	}
	
	/**
	 * Get dead status
	 * 
	 * @return True if dead, false if alive
	 * @see Actor#setDead(boolean)
	 */
	public boolean isDead() {
		return isDead;
	}
	
	/**
	 * Speed is the number of pixels an actor moves across the screen in a given cycle. A full position change
	 * is the number of pixels defined in Map.CELL_SIZE
	 * 
	 * @param s New Speed
	 */
	public void setSpeed(float s) {
		speed = s;
	}
	
	/**
	 * Get the current speed of the actor
	 * 
	 * @return Current speed
	 * @see Actor#setSpeed(float)
	 */
	public float getSpeed() {
		return speed;
	}
	
	/**
	 * Set the direction actor should travel in. Player uses this to determine the direction to "auto-move" to
	 * Ghosts ignore what is set by this function because their direction is determined within act() based on
	 * the path
	 */
	public void setMoveDirection(int dir) {
		moveDir = dir;
	}
	
	// Public Methods
	
	/**
	 * Attempt to move the actor to the given x,y location. This method will check if a coordinate is valid with the Map class method
	 * canMove(). It is not necessary to call canMove() before this function
	 * 
	 * @param x A x coordinate to move to
	 * @param y A y coordinate to move to
	 * @return True if the move succeeded. False if otherwise
	 * @see Map#canMove(Actor, int, int)
	 */
	public boolean move(int x, int y) {
		boolean res = map.canMove(this, x, y);
		if(res) {
			positionX = x;
			positionY = y;
		}
		return res;
	}
	
	/**
	 * The primary logic function for actors. StateGame calls this for players directly in logic() and the AIManager calls
	 * this for ghosts in process()
	 *  
	 * @see GameObject#act()
	 * @see StateGame#logic()
	 * @see AIManager#process()
	 */
	@Override
	public void act() {
	}

	/**
	 * 
	 * @see GameObject#paint(java.awt.Graphics2D)
	 */
	@Override
	public void paint(Graphics2D g) {
	}

}
